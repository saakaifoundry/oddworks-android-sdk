package io.oddworks.device.request

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.squareup.okhttp.*
import io.oddworks.device.Oddworks
import io.oddworks.device.exception.BadResponseCodeException
import io.oddworks.device.exception.OddParseException
import io.oddworks.device.exception.OddRequestException
import io.oddworks.device.metric.OddMetric
import io.oddworks.device.model.OddConfig
import io.oddworks.device.model.common.OddResourceType
import org.json.JSONException
import java.io.IOException
import java.util.*

class OddRequest(builder: Builder) {
    private val acceptLanguageHeader: String
    private val authorizationJWT: String
    private val baseURL: HttpUrl
    private val context: Context
    private val event: OddMetric?
    private val include: String?
    private val limit: Int?
    private val offset: Int?
    private val query: String?
    private val relationshipName: String?
    private val resourceId: String?
    private val resourceType: OddResourceType
    private val skipCache: Boolean
    private val sort: String?
    private val versionName: String

    init {
        context = builder.context
        resourceType = builder.resourceType ?: throw OddRequestException("Missing resourceType")

        // use package info to retrieve default values
        val info = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)

        val baseUrlString = builder.apiBaseURL ?: info.metaData?.getString(Oddworks.API_BASE_URL_KEY, Oddworks.DEFAULT_API_BASE_URL) ?: throw OddRequestException("Missing ${Oddworks.API_BASE_URL_KEY} in Application meta-data")

        // Set OkHttp Cache if it isn't already set
        if (OKHTTP_CLIENT.cache == null) {
            OKHTTP_CLIENT.cache = Cache(context.cacheDir, MAX_CACHE_SIZE)
        }

        // override default values (or empty) with builder values
        baseURL = HttpUrl.parse(baseUrlString)
        resourceId = builder.resourceId
        relationshipName = builder.relationshipName
        include = builder.include
        acceptLanguageHeader = builder.acceptLanguageHeader
        authorizationJWT = builder.authorizationJWT ?: fetchJWTSharedPreferences() ?: info.metaData?.getString(Oddworks.CONFIG_JWT_KEY) ?: throw OddRequestException("Missing ${Oddworks.CONFIG_JWT_KEY} in Application meta-data")
        versionName = builder.versionName ?: packageInfo.versionName ?: throw OddRequestException("Application PackageInfo versionName is somehow missing")
        limit = builder.limit
        offset = builder.offset
        sort = builder.sort
        query = builder.query
        event = builder.event
        skipCache = builder.skipCache

        if (null == event && resourceType == OddResourceType.EVENT) {
            throw OddRequestException("Missing event metric for POST request")
        }
    }

    /**
     * Builds and executes an [com.squareup.okhttp.Request]
     *
     * @param callback - an [OddCallback]
     */
    fun <T> enqueueRequest(oddCallback: OddCallback<T>) {
        // get base endpoint
        val endpoint = baseURL.newBuilder()
        // add uri
        getPath().split("/").forEach {
            endpoint.addPathSegment(it)
        }
        getQueryParameters().forEach {
            endpoint.addQueryParameter(it.key, it.value)
        }
        // add headers
        val builder = Request.Builder()
                .url(endpoint.toString())
                .addHeader("authorization", getAuthorizationHeader())
                .addHeader("x-odd-user-agent", getOddUserAgent())
                .addHeader("accept", ACCEPT_HEADER)
                .addHeader("accept-language", acceptLanguageHeader)


        val request = if (isEventPost()) {
            builder.post(RequestBody.create(JSON, event!!.toJSONObject().toString())).build()
        } else if (shouldSkipCache()) {
            builder.cacheControl(CacheControl.FORCE_NETWORK).build()
        } else {
            builder.build()
        }

        Log.d(OddRequest::class.java.simpleName, request.toString())

        val callback = getCallback(oddCallback, getParseCall<T>())

        OKHTTP_CLIENT.newCall(request).enqueue(callback)
    }

    private fun <T> getCallback(oddCallback: OddCallback<T>, parseCall: ParseCall<T>): Callback {
        return object : Callback {
            private val MS_IN_SECONDS = 1000

            override fun onFailure(request: Request, e: IOException) {
                Log.e("ResponseCb onFailure", "Failed", e)
                oddCallback.onFailure(e)
            }

            override fun onResponse(response: Response) {
                // TODO - handle when the response body is empty a little more gracefully
                Log.d("ResponseCb onResponse", "code: ${response.code()} responseBody: ${response.body()}")
                if (response.isSuccessful) {
                    var responseBody = ""
                    try {
                        responseBody = response.body().string()
                        val obj = parseCall.parse(responseBody)
                        oddCallback.onSuccess(obj)
                    } catch (e: Exception) {
                        val responseBody = if (responseBody.isNullOrBlank()) {
                            "response.body() was empty"
                        } else {
                            responseBody
                        }
                        oddCallback.onFailure(OddParseException("Response body parse failed: $responseBody", e))
                    }
                } else {
                    // remove JWT on status 401
                    if (response.code() == 401) {
                        clearJWTSharedPreferences()
                    }
                    oddCallback.onFailure(BadResponseCodeException(response.code()))
                }
            }

            private fun tryGetResponseBody(response: Response): String {
                return try {
                    response.body().string()
                } catch (e: Exception) {
                    "response.body() was empty"
                }
            }
        }
    }

    private interface ParseCall<out Any> {
        @Throws(JSONException::class)
        fun parse(responseBody: String): Any
    }

    private fun <T> getParseCall(): ParseCall<T> {
        return if (isListEndpoint()) {
            object: ParseCall<T> {
                override fun parse(responseBody: String): T {
                    return OddParser.parseMultipleResponse(responseBody) as T
                }
            }
        } else if (isEventPost()) {
            object: ParseCall<T> {
                override fun parse(responseBody: String): T {
                    return event as T
                }
            }
        } else {
            object: ParseCall<T> {
                override fun parse(responseBody: String): T {
                    val obj = OddParser.parseSingleResponse(responseBody) as T

                    if (obj is OddConfig) {
                        stashJWTSharedPreferences(obj.jwt)
                    }

                    return obj
                }
            }
        }
    }

    private fun getPath(): String {
        return when {
            resourceType == OddResourceType.SEARCH || resourceType == OddResourceType.CONFIG -> {
                resourceType.toString().toLowerCase()
            }
            resourceId == null -> {
                // /resourceType
                "${resourceType.toString().toLowerCase()}s"
            }
            relationshipName == null -> {
                // /resourceType/resourceId
                "${resourceType.toString().toLowerCase()}s/$resourceId"
            }
            include == null -> {
                // /resourceType/resourceId/relationships/{relationshipName}
                "${resourceType.toString().toLowerCase()}s/$resourceId/relationships/$relationshipName"
            }
            else -> {
                // this should throw
                throw OddRequestException("Unable to determine request path")
            }
        }
    }

    private fun getQueryParameters(): Map<String, String> {
        val parameters = mutableMapOf<String, String>()
        if (relationshipName == null && include != null && !isListEndpoint() && !isEventPost() && resourceType != OddResourceType.CONFIG) {
            parameters.put("include", include)
        }
        if (limit != null && isListEndpoint()) {
            parameters.put("limit", limit.toString())
        }
        if (offset != null && isListEndpoint()) {
            parameters.put("offset", offset.toString())
        }
        if (sort != null && isListEndpoint()) {
            parameters.put("sort", sort)
        }
        if (query != null && resourceType == OddResourceType.SEARCH) {
            parameters.put("q", query)
        }
        return parameters
    }

    private fun isListEndpoint(): Boolean {
        return (resourceType != OddResourceType.CONFIG && resourceType != OddResourceType.EVENT) && ((resourceId == null && relationshipName == null) || (resourceId != null && relationshipName != null))
    }

    private fun isEventPost(): Boolean {
        return resourceType == OddResourceType.EVENT
    }

    private fun shouldSkipCache(): Boolean {
        if (skipCache) return true
        return when (resourceType) {
            OddResourceType.CONFIG -> { true }
            OddResourceType.COLLECTION -> { skipCache }
            OddResourceType.EVENT -> { true }
            OddResourceType.PROMOTION -> { skipCache }
            OddResourceType.SEARCH -> { true }
            OddResourceType.VIDEO -> { skipCache }
            OddResourceType.VIEW -> { skipCache }
        }
    }

    private fun getAuthorizationHeader(): String {
        return "Bearer $authorizationJWT"
    }

    private fun getOddUserAgent(): String {
        return "platform[name]=Android&model[name]=${Build.MANUFACTURER}&model[version]=${Build.MODEL}&os[name]=${Build.VERSION.CODENAME}&os[version]=${Build.VERSION.SDK_INT}&build[version]=$versionName"
    }

    private fun fetchJWTSharedPreferences(): String? {
        val prefs = context.getSharedPreferences(AUTHORIZATION_PREFERENCES, Context.MODE_PRIVATE)
        return prefs.getString(AUTHORIZATION_PREFERENCE_JWT, null)
    }

    private fun stashJWTSharedPreferences(jwt: String?) {
        if (jwt == null) return
        val prefs = context.getSharedPreferences(AUTHORIZATION_PREFERENCES, Context.MODE_PRIVATE).edit()
        prefs.putString(AUTHORIZATION_PREFERENCE_JWT, jwt)
        prefs.apply()
    }

    private fun clearJWTSharedPreferences() {
        val prefs = context.getSharedPreferences(AUTHORIZATION_PREFERENCES, Context.MODE_PRIVATE).edit()
        prefs.remove(AUTHORIZATION_PREFERENCE_JWT)
        prefs.apply()
    }

    class Builder(val context: Context) {
        var include: String? = null
        var authorizationJWT: String? = null
        var versionName: String? = null
        var acceptLanguageHeader: String = LOCALE
        var apiBaseURL: String? = null
        var resourceType: OddResourceType? = null
        var resourceId: String? = null
        var relationshipName: String? = null
        var limit: Int? = null
        var offset: Int? = null
        var sort: String? = null
        var query: String? = null
        var event: OddMetric? = null
        var skipCache: Boolean = false

        /**
         * Allows Builder to tell which resource path to use
         *
         * Ex. `resourceType(OddResourceType.VIEW)`
         * `https://base.url/views`
         *
         * @param resourceType - the [OddResourceType]
         */
        fun resourceType(resourceType: OddResourceType): Builder {
            this.resourceType = resourceType
            return this
        }

        /**
         * Allows Builder to tell which resource id to request
         *
         * Requires [resourceType] to be present
         *
         * Ex. `resourceId("12345")`
         * `https://base.url/resourceType/12345`
         *
         * @param resourceId - the OddResource ID
         */
        fun resourceId(resourceId: String): Builder {
            this.resourceId = resourceId
            return this
        }

        /**
         * Allows Builder to tell when to request a resource's relationship resources
         *
         * Requires [resourceType] and [resourceId] to be present.
         *
         * Ex. `relationshipName("videos")`
         * `https://base.url/resourceType/resourceId/relationships/videos`
         *
         * @param relationshipName - the relationship name
         */
        fun relationshipName(relationshipName: String): Builder {
            this.relationshipName = relationshipName
            return this
        }

        /**
         * Used to specify which relationships' resources you want to include in the
         * response. The relationship must exist.
         *
         * For best results, use sparingly. This can drastically increase response sizes
         * if a relationship's data set is large.
         *
         * When present will enable 'include' query parameter.
         *
         * Ex: `include("relationship,relationship2")`
         * `https://base.url/resourceType/resourceId?include=relationship,relationship2`
         *
         * This is only applied to single-resource response types (non-list)
         *
         * @param include - comma separated list of relationship names to include
         * with request
         */
        fun include(include: String): Builder {
            this.include = include
            return this
        }

        /**
         * Overrides the default Authorization JWT. This should be overridden for all
         * non-config requests.
         *
         * Defaults to the token specified in [Oddworks.CONFIG_JWT_KEY] in the application
         * meta data.
         *
         * @param authorizationJWT - the JWT you wish to specify
         */
        fun authorizationJWT(authorizationJWT: String): Builder {
            this.authorizationJWT = authorizationJWT
            return this
        }

        /**
         * Overrides the default versionName of the application, taken from the
         * applications package info.
         *
         * This is only used in the `x-odd-user-agent` header used by metric events service.
         *
         * @param versionName - the version name you wish to specify
         */
        fun versionName(versionName: String): Builder {
            this.versionName = versionName
            return this
        }

        /**
         * Overrides the default base URL of the Oddworks instance where requests
         * will be sent.
         *
         * The default is set in [Oddworks.API_BASE_URL_KEY] in the
         * application meta data.
         *
         * If not set there, a fallback of [Oddworks.DEFAULT_API_BASE_URL]
         * is used.
         *
         * This is usually not necessary to override.
         *
         * @param apiBaseURL - the base URL for your Oddworks instance
         */
        fun apiBaseURL(apiBaseURL: String): Builder {
            this.apiBaseURL = apiBaseURL
            return this
        }

        /**
         * Overrides the default Accept-Language header.
         *
         * Defaults to a combination of [Locale] default language and country.
         *
         * @param acceptLanguage - the accept language header value
         */
        fun acceptLanguage(acceptLanguage: String): Builder {
            this.acceptLanguageHeader = acceptLanguage
            return this
        }

        /**
         * Limits the number of results from a multi-resource (list) request.
         *
         * Ex. `limit(5)`
         * `https://base.url/resourceType?page\[limit\]=5`
         *
         * @param limit - the number of resources to return
         */
        fun limit(limit: Int): Builder {
            this.limit = limit
            return this
        }

        /**
         * Offset the result set in a multi-resource (list) request.
         *
         * Ex. `offset(10)`
         * `https://base.url/resourceType?page\[offset\]=10`
         *
         * @param offset - the number of resources to offset
         */
        fun offset(offset: Int): Builder {
            this.offset = offset
            return this
        }

        /**
         * Sort the result set in a multi-resource (list) request.
         *
         * Use this to specify the property used to sort the result
         * set with a single property using dot notation.
         *
         * Ex. `sort("meta.source")`
         * `https://base.url/resourceType?sort=meta.source`
         *
         * @param sort - the property to use to sort the result set
         */
        fun sort(sort: String): Builder {
            this.sort = sort
            return this
        }

        /**
         * Specifies the search query.
         *
         * <p>Required when [resourceType] is [OddResourceType.SEARCH]
         *
         * Ex. `query("the term")`
         * `https://base.url/search?q=the%20term`
         *
         * @param query - the search term
         */
        fun query(query: String): Builder {
            this.query = query
            return this
        }

        /**
         * Specifies the [OddMetric] event to POST.
         *
         * Required when [resourceType] is [OddResourceType.EVENT]
         *
         * @param event - the event metric
         */
        fun event(event: OddMetric): Builder {
            this.event = event
            return this
        }

        /**
         * Force OkHttp to hit Oddworks API instead of relying on cache.
         *
         * Defaults to `true`
         *
         * Certain endpoints are not cacheable. Also, see [OddRequest.MAX_CACHE_SIZE]
         *
         * @param skipCache - whether to skip cache and hit API
         */
        fun skipCache(skipCache: Boolean): Builder {
            this.skipCache = skipCache
            return this
        }

        fun build(): OddRequest {
            return OddRequest(this)
        }
    }


    companion object {
        private val AUTHORIZATION_PREFERENCES = "${OddRequest::class.java.name}.AUTHORIZATION_PREFERENCES"
        private val AUTHORIZATION_PREFERENCE_JWT = "${OddRequest::class.java.name}.AUTHORIZATION_PREFERENCE_JWT"
        private val JSON = MediaType.parse("application/json; charset=utf-8")
        private val ACCEPT_HEADER = "application/json"
        private val LANGUAGE = Locale.getDefault().language.toLowerCase()
        private val COUNTRY = Locale.getDefault().country.toLowerCase()
        private val LOCALE = "$LANGUAGE-$COUNTRY"
        private val MAX_CACHE_SIZE: Long = 10 * 1024 * 1024 // 10MB
        private val OKHTTP_CLIENT = OkHttpClient()
    }
}
