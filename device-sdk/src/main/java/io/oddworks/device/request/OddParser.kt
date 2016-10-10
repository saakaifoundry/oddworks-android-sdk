package io.oddworks.device.request

import android.util.Log

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedHashMap

import io.oddworks.device.exception.OddParseException
import io.oddworks.device.exception.UnhandledPlayerTypeException
import io.oddworks.device.model.Article
import io.oddworks.device.model.AuthToken
import io.oddworks.device.model.Config
import io.oddworks.device.model.DeviceCodeResponse
import io.oddworks.device.model.Event
import io.oddworks.device.model.External
import io.oddworks.device.model.Identifier
import io.oddworks.device.model.Media
import io.oddworks.device.model.MediaAd
import io.oddworks.device.model.MediaImage
import io.oddworks.device.model.Metric
import io.oddworks.device.model.MetricsConfig
import io.oddworks.device.model.OddCollection
import io.oddworks.device.model.OddObject
import io.oddworks.device.model.OddView
import io.oddworks.device.model.Promotion
import io.oddworks.device.model.Relationship
import io.oddworks.device.model.Sharing
import io.oddworks.device.model.config.Display
import io.oddworks.device.model.config.Features
import io.oddworks.device.model.players.ExternalPlayer
import io.oddworks.device.model.players.OoyalaPlayer
import io.oddworks.device.model.players.Player

//todo stop swallowing exceptions

class OddParser private constructor()// singleton
{

    @Throws(JSONException::class)
    fun parseMediaImage(data: JSONObject?): MediaImage? {
        if (data == null) {
            return null
        }
        val aspect16x9 = JSON.getString(data, "aspect16x9")
        val aspect4x3 = JSON.getString(data, "aspect4x3")
        val aspect3x4 = JSON.getString(data, "aspect3x4")
        val aspect1x1 = JSON.getString(data, "aspect1x1")
        val aspect2x3 = JSON.getString(data, "aspect2x3")

        return MediaImage(aspect16x9, aspect3x4, aspect4x3, aspect1x1, aspect2x3)
    }

    @Throws(JSONException::class)
    fun parseMediaAd(data: JSONObject): MediaAd {
        try {
            val rawAds = data.getJSONObject("ads")

            val properties = HashMap<String, Any>()
            val adKeys = rawAds.keys()
            while (adKeys.hasNext()) {
                val adProperty = adKeys.next()
                if (adProperty == "enabled") {
                    properties.put(adProperty, JSON.getBoolean(rawAds, adProperty))
                } else if (adProperty == "networkId") {
                    properties.put(adProperty, JSON.getInt(rawAds, adProperty))
                } else {
                    properties.put(adProperty, JSON.getString(rawAds, adProperty))
                }
            }
            return MediaAd(properties)
        } catch (e: Exception) {
            return MediaAd()
        }

    }

    @Throws(JSONException::class)
    fun parseCollection(data: JSONObject): OddCollection? {
        val rawAttributes = JSON.getJSONObject(data, ATTRIBUTES, true)
        val meta = JSON.getJSONObject(data, META, false)
        val images = JSON.getJSONObject(rawAttributes, "images", false)

        val id = JSON.getString(data, "id")
        val type = JSON.getString(data, "type")

        val attributes = HashMap<String, Any>()
        attributes.put("title", JSON.getString(rawAttributes, "title"))
        attributes.put("subtitle", JSON.getString(rawAttributes, "subtitle"))
        attributes.put("description", JSON.getString(rawAttributes, "description"))
        attributes.put("releaseDate", JSON.getDateTime(rawAttributes, "releaseDate"))
        attributes.put("mediaImage", parseMediaImage(images))

        val collection = OddCollection(id, type)
        collection.attributes = attributes
        collection.meta = meta


        val relationships = JSON.getJSONObject(data, "relationships", true)

        addRelationshipsToOddObject(relationships, collection)

        collection.fillIncludedCollections()

        return collection
    }

    /**
     * parses included and adds it to an OddObject
     * @param addTo addTo.addIncluded is called on the parsed included objects
     * *
     * @throws JSONException
     */
    @Throws(JSONException::class)
    private fun addIncluded(addTo: OddObject, included: JSONArray) {
        for (i in 0..included.length() - 1) {
            val includedObject = included.getJSONObject(i)
            val includedType = JSON.getString(includedObject, "type")

            when (includedType) {
                OddObject.TYPE_ARTICLE -> addTo.addIncluded(parseArticle(includedObject))
                OddObject.TYPE_COLLECTION -> addTo.addIncluded(parseCollection(includedObject))
                OddObject.TYPE_EVENT -> addTo.addIncluded(parseEvent(includedObject))
                OddObject.TYPE_EXTERNAL -> addTo.addIncluded(parseExternal(includedObject))
                OddObject.TYPE_PROMOTION -> addTo.addIncluded(parsePromotion(includedObject))
                OddObject.TYPE_LIVE_STREAM, OddObject.TYPE_VIDEO -> addTo.addIncluded(parseMedia(includedObject))
            }
        }
    }

    @Throws(JSONException::class)
    protected fun parseEvent(dataObject: JSONObject): Event? {
        val rawAttributes = JSON.getJSONObject(dataObject, ATTRIBUTES, true)
        val meta = JSON.getJSONObject(dataObject, META, false)
        val images = JSON.getJSONObject(rawAttributes, "images", false)
        val ical = JSON.getJSONObject(rawAttributes, "ical", true)

        val event = Event(
                JSON.getString(dataObject, "id"),
                JSON.getString(dataObject, "type"))
        event.meta = meta

        val attributes = HashMap<String, Any>()
        attributes.put("title", JSON.getString(rawAttributes, "title"))
        attributes.put("description", JSON.getString(rawAttributes, "description"))
        attributes.put("mediaImage", parseMediaImage(images))
        attributes.put("category", JSON.getString(rawAttributes, "category"))
        attributes.put("source", JSON.getString(rawAttributes, "source"))
        attributes.put("createdAt", JSON.getDateTime(rawAttributes, "createdAt"))
        attributes.put("url", JSON.getString(rawAttributes, "url"))

        attributes.put("dateTimeStart", JSON.getDateTime(ical, "dtstart"))
        attributes.put("dateTimeEnd", JSON.getDateTime(ical, "dtend"))
        attributes.put("location", JSON.getString(ical, "location"))

        event.attributes = attributes

        return event
    }

    @Throws(JSONException::class)
    protected fun parseExternal(dataObject: JSONObject): External? {
        val rawAttributes = JSON.getJSONObject(dataObject, ATTRIBUTES, true)
        val meta = JSON.getJSONObject(dataObject, META, false)
        val images = JSON.getJSONObject(rawAttributes, "images", false)

        val external = External(
                JSON.getString(dataObject, "id"),
                JSON.getString(dataObject, "type"))
        external.meta = meta

        val attributes = HashMap<String, Any>()
        attributes.put("title", JSON.getString(rawAttributes, "title"))
        attributes.put("description", JSON.getString(rawAttributes, "description"))
        attributes.put("mediaImage", parseMediaImage(images))
        attributes.put("url", JSON.getString(rawAttributes, "url"))

        external.attributes = attributes

        return external
    }

    @Throws(JSONException::class)
    protected fun parseArticle(dataObject: JSONObject): Article? {
        val rawAttributes = JSON.getJSONObject(dataObject, ATTRIBUTES, true)
        val meta = JSON.getJSONObject(dataObject, META, false)
        val images = JSON.getJSONObject(rawAttributes, "images", false)

        val article = Article(
                JSON.getString(dataObject, "id"),
                JSON.getString(dataObject, "type"))
        article.meta = meta

        val attributes = HashMap<String, Any>()
        attributes.put("title", JSON.getString(rawAttributes, "title"))
        attributes.put("description", JSON.getString(rawAttributes, "description"))
        attributes.put("mediaImage", parseMediaImage(images))
        attributes.put("category", JSON.getString(rawAttributes, "category"))
        attributes.put("source", JSON.getString(rawAttributes, "source"))
        attributes.put("createdAt", JSON.getDateTime(rawAttributes, "createdAt"))
        attributes.put("url", JSON.getString(rawAttributes, "url"))

        article.attributes = attributes

        return article
    }

    @Throws(JSONException::class)
    protected fun parseMedia(dataObject: JSONObject): Media? {
        val rawAttributes = JSON.getJSONObject(dataObject, ATTRIBUTES, true)
        val meta = JSON.getJSONObject(dataObject, META, false)
        val images = JSON.getJSONObject(rawAttributes, "images", false)

        val media = Media(
                JSON.getString(dataObject, "id"),
                JSON.getString(dataObject, "type"))
        media.meta = meta

        val attributes = HashMap<String, Any>()
        attributes.put("title", JSON.getString(rawAttributes, "title"))
        attributes.put("subtitle", JSON.getString(rawAttributes, "subtitle"))
        attributes.put("description", JSON.getString(rawAttributes, "description"))
        attributes.put("releaseDate", JSON.getDateTime(rawAttributes, "releaseDate"))
        try {
            attributes.put("duration", JSON.getInt(rawAttributes, "duration"))
        } catch (e: Exception) {
            Log.d(TAG, "Invalid duration: " + e.toString())
            attributes.put("duration", 0)
        }

        attributes.put("url", JSON.getString(rawAttributes, "url"))
        attributes.put("isLive", JSON.getString(rawAttributes, "isLive"))
        attributes.put("mediaImage", parseMediaImage(images))
        attributes.put("mediaAd", parseMediaAd(rawAttributes))
        media.attributes = attributes
        val relationships = JSON.getJSONObject(dataObject, "relationships", false)
        if (relationships != null) addRelationshipsToOddObject(relationships, media)
        media.player = parsePlayer(JSON.getJSONObject(rawAttributes, "player", true))
        media.sharing = parseSharing(JSON.getJSONObject(rawAttributes, "sharing", false))

        return media
    }

    @Throws(JSONException::class)
    fun parsePlayer(rawPlayer: JSONObject): Player {
        var type: Player.PlayerType
        try {
            type = Player.PlayerType.valueOf(JSON.getString(rawPlayer, "type").toUpperCase())
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Parsed player does not exist", e)
            throw UnhandledPlayerTypeException("Unsupported player type: " + rawPlayer)
        } catch (e: Exception) {
            Log.e(TAG, "Unable to parse player, falling back to native", e)
            type = Player.PlayerType.NATIVE
        }

        var player: Player? = null
        when (type) {
            Player.PlayerType.NATIVE, Player.PlayerType.BRIGHTCOVE -> player = Player(type)
            Player.PlayerType.EXTERNAL -> player = parseExternalPlayer(rawPlayer)
            Player.PlayerType.OOYALA -> player = parseOoyalaPlayer(rawPlayer)
            else -> throw UnhandledPlayerTypeException("Unsupported player type: " + type)
        }
        return player
    }

    @Throws(JSONException::class)
    protected fun parseSharing(rawSharing: JSONObject?): Sharing {
        var enabled = false
        var text = ""
        if (rawSharing != null) {
            enabled = JSON.getBoolean(rawSharing, "enabled")
            text = JSON.getString(rawSharing, "text")
        }
        return Sharing(enabled, text)
    }

    @Throws(JSONException::class)
    private fun parseOoyalaPlayer(rawPlayer: JSONObject): OoyalaPlayer {
        return OoyalaPlayer(Player.PlayerType.OOYALA,
                JSON.getString(rawPlayer, "pCode"),
                JSON.getString(rawPlayer, "embedCode"),
                JSON.getString(rawPlayer, "domain"))
    }

    @Throws(JSONException::class)
    private fun parseExternalPlayer(rawPlayer: JSONObject): ExternalPlayer {
        return ExternalPlayer(Player.PlayerType.EXTERNAL, JSON.getString(rawPlayer, "url"))
    }

    @Throws(JSONException::class)
    protected fun parsePromotion(rawPromotion: JSONObject): Promotion {
        val rawAttributes = JSON.getJSONObject(rawPromotion, ATTRIBUTES, true)
        val meta = JSON.getJSONObject(rawPromotion, META, false)
        val images = JSON.getJSONObject(rawAttributes, "images", false)

        val promotion = Promotion(
                JSON.getString(rawPromotion, "id"),
                JSON.getString(rawPromotion, "type"))
        promotion.meta = meta

        val attributes = HashMap<String, Any>()
        attributes.put("title", JSON.getString(rawAttributes, "title"))
        attributes.put("description", JSON.getString(rawAttributes, "description"))
        attributes.put("url", JSON.getString(rawAttributes, "url"))
        attributes.put("mediaImage", parseMediaImage(images))

        promotion.attributes = attributes

        return promotion
    }

    fun parseEntityList(result: String): List<OddObject> {
        val entities = ArrayList<OddObject>()
        try {
            val entitiesResponse = JSONObject(result)
            val rawEntities = entitiesResponse.getJSONArray(DATA)
            for (i in 0..rawEntities.length() - 1) {
                val rawEntity = rawEntities.getJSONObject(i)
                val type = JSON.getString(rawEntity, "type")
                when (type) {
                    OddObject.TYPE_ARTICLE -> entities.add(parseArticle(rawEntity))
                    OddObject.TYPE_COLLECTION -> entities.add(parseCollection(rawEntity))
                    OddObject.TYPE_EVENT -> entities.add(parseEvent(rawEntity))
                    OddObject.TYPE_EXTERNAL -> entities.add(parseExternal(rawEntity))
                    OddObject.TYPE_PROMOTION -> entities.add(parsePromotion(rawEntity))
                    OddObject.TYPE_LIVE_STREAM, OddObject.TYPE_VIDEO -> entities.add(parseMedia(rawEntity))
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return entities
    }

    protected fun parseMetrics(rawFeatures: JSONObject): MetricsConfig? {
        try {
            val rawMetrics = JSON.getJSONObject(rawFeatures, "metrics", true)

            val metrics = ArrayList<Metric>()
            for (key in MetricsConfig.ACTION_KEYS) {
                val attributes = HashMap<String, Any>()
                val rawMetric = JSON.getJSONObject(rawMetrics, key, true)
                val mkeys = rawMetric.keys()
                while (mkeys.hasNext()) {
                    val mkey = mkeys.next()
                    when (mkey) {
                        Metric.ENABLED -> attributes.put(mkey, JSON.getBoolean(rawMetric, mkey))
                        Metric.INTERVAL -> attributes.put(mkey, JSON.getInt(rawMetric, mkey))
                        else -> attributes.put(mkey, JSON.getString(rawMetric, mkey))
                    }
                }

                metrics.add(Metric(key, attributes))
            }

            val metricsConfig = MetricsConfig(metrics)

            metricsConfig.setupOddMetrics() // MAGIC!

            return metricsConfig
        } catch (e: JSONException) {
            Log.w(TAG, "failed to parse metrics feature from config")
            return null
        }

    }

    fun parseConfig(result: String): Config? {
        try {
            val resultJSONObject = JSONObject(result)
            val dataJSONObject = JSON.getJSONObject(resultJSONObject, DATA, true)
            val rawAttributes = JSON.getJSONObject(dataJSONObject, ATTRIBUTES, true)

            val views = LinkedHashMap<String, String>()
            val rawViews = JSON.getJSONObject(rawAttributes, "views", true)
            val viewNames = rawViews.keys()
            while (viewNames.hasNext()) {
                val viewName = viewNames.next()
                views.put(viewName, JSON.getString(rawViews, viewName))
            }

            val display = parseDisplay(rawAttributes)
            val features = parseFeatures(rawAttributes)

            val jwt = JSON.getString(rawAttributes, "jwt")

            return Config(views, display, features, jwt)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return null
    }

    @Throws(JSONException::class)
    private fun parseDisplay(rawAttributes: JSONObject): Display {
        val rawDisplay = JSON.getJSONObject(rawAttributes, "display", true)

        return Display(null, null, null)
    }

    @Throws(JSONException::class)
    private fun parseFeatures(rawAttributes: JSONObject): Features {
        val rawFeatures = JSON.getJSONObject(rawAttributes, "features", true)

        return Features(null, null, null)
    }

    @Throws(JSONException::class)
    private fun isAuthEnabled(rawFeatures: JSONObject): Boolean {
        try {
            val rawAuth = JSON.getJSONObject(rawFeatures, "authentication", true)
            return JSON.getBoolean(rawAuth, "enabled")
        } catch (e: JSONException) {
            return false
        }

    }

    fun parseViewResponse(result: String): OddView? {
        try {
            val resultObject = JSONObject(result)
            val data = JSON.getJSONObject(resultObject, DATA, true)
            val meta = JSON.getJSONObject(resultObject, META, false)
            val rawAttributes = JSON.getJSONObject(data, ATTRIBUTES, true)
            val view = OddView(
                    JSON.getString(data, "id"),
                    JSON.getString(data, "type"))
            view.meta = meta

            val attributes = HashMap<String, Any>()
            attributes.put("title", JSON.getString(rawAttributes, "title"))
            view.attributes = attributes

            // use relationships to build view's ArrayList<Relationship>
            addRelationshipsToOddObject(JSON.getJSONObject(data, "relationships", true), view)

            // fill the view's included{Type} arrays with parsable objects
            addIncludedFromResponse(view, resultObject)
            // backfill newly created collections with newly created entities
            view.fillIncludedCollections()

            return view
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return null
    }

    @Throws(JSONException::class)
    fun parseAuthToken(responseBody: String): AuthToken {
        val raw = JSONObject(responseBody)
        val data = JSON.getJSONObject(raw, DATA, true)
        val attributes = JSON.getJSONObject(data, ATTRIBUTES, true)
        val deviceUserProfile = JSON.getJSONObject(attributes, "deviceUserProfile", true)
        val entitlementCredentials = JSON.getJSONObject(deviceUserProfile, "entitlementCredentials", false)
        val accessToken = attributes.getString("access_token")
        val tokenType = attributes.getString("token_type")
        return AuthToken(accessToken, tokenType, entitlementCredentials)
    }

    @Throws(JSONException::class)
    protected fun parseDeviceCodeResponse(responseBody: String): DeviceCodeResponse {
        val raw = JSONObject(responseBody)
        val data = JSON.getJSONObject(raw, DATA, true)
        val attributes = JSON.getJSONObject(data, ATTRIBUTES, true)
        val deviceCode = attributes.getString("device_code")
        val userCode = attributes.getString("user_code")
        val verificationUrl = attributes.getString("verification_url")
        val expiresIn = attributes.getInt("expires_in")
        val interval = attributes.getInt("interval")
        return DeviceCodeResponse(deviceCode, userCode, verificationUrl, expiresIn, interval)
    }

    fun parseCollectionResponse(responseBody: String): OddCollection {
        var collection: OddCollection? = null
        try {
            val raw = JSONObject(responseBody)
            val data = JSON.getJSONObject(raw, DATA, true)
            collection = parseCollection(data)
            addIncludedFromResponse(collection, raw)
        } catch (e: Exception) {
            throw OddParseException(e)
        }

        return collection
    }

    fun parseSearch(result: String): List<OddObject> {
        val searchResult = ArrayList<OddObject>()
        try {
            val resultObject = JSONObject(result)
            val dataArray = resultObject.getJSONArray(DATA)
            for (i in 0..dataArray.length() - 1) {
                val item = dataArray.getJSONObject(i)
                val type = JSON.getString(item, "type")

                when (type) {
                    OddObject.TYPE_VIDEO, OddObject.TYPE_LIVE_STREAM -> {
                        val video = parseMedia(item)
                        if (video != null) {
                            searchResult.add(video)
                        }
                    }
                    OddObject.TYPE_ARTICLE -> {
                        val article = parseArticle(item)
                        if (article != null) {
                            searchResult.add(article)
                        }
                    }
                    OddObject.TYPE_EVENT -> {
                        val event = parseEvent(item)
                        if (event != null) {
                            searchResult.add(event)
                        }
                    }
                    OddObject.TYPE_EXTERNAL -> {
                        val external = parseExternal(item)
                        if (external != null) {
                            searchResult.add(external)
                        }
                    }
                    OddObject.TYPE_COLLECTION -> {
                        val collection = parseCollection(item)
                        if (collection != null) {
                            searchResult.add(collection)
                        }
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return searchResult
    }

    protected fun addRelationshipsToOddObject(relationships: JSONObject, relatable: OddObject) {
        val relationshipNames = relationships.keys()
        try {
            while (relationshipNames.hasNext()) {
                val name = relationshipNames.next()
                val relationship = JSON.getJSONObject(relationships, name, true)
                if (relationship != null) {
                    val identifiers = ArrayList<Identifier>()

                    if (relationship.get(DATA) is JSONObject) {
                        //handle single relationship.data
                        val identifier = JSON.getJSONObject(relationship, DATA, true)
                        val relId = identifier.getString("id")
                        val relType = identifier.getString("type")
                        identifiers.add(Identifier(relId, relType))

                    } else if (relationship.get(DATA) is JSONArray) {
                        // handle multiple relationship.data
                        val rawIdentifiers = JSON.getJSONArray(relationship, DATA, true)

                        for (i in 0..rawIdentifiers.length() - 1) {
                            val identifier = rawIdentifiers.getJSONObject(i)
                            val relId = identifier.getString("id")
                            val relType = identifier.getString("type")
                            identifiers.add(Identifier(relId, relType))
                        }
                    }

                    relatable.addRelationship(Relationship(name, identifiers))
                }
            }
        } catch (e: JSONException) {
            Log.e(TAG, e.toString())
            e.printStackTrace()
        }

    }

    @Throws(JSONException::class)
    fun parseErrorMessage(responseBody: String): String {
        return JSONObject(responseBody).getString("message")
    }

    /** adds included objects from response to the OddObject if there are included objects in the response. Otherwise
     * does nothing.
     */
    @Throws(JSONException::class)
    protected fun addIncludedFromResponse(oddObject: OddObject, response: JSONObject) {
        val included = JSON.getJSONArray(response, "included", false)
        if (included != null) addIncluded(oddObject, included)
    }

    fun parseMediaResponse(responseBody: String): Media {
        var media: Media? = null
        try {
            val response = JSONObject(responseBody)
            val data = JSON.getJSONObject(response, DATA, true)
            media = parseMedia(data)
            addIncludedFromResponse(media, response)
        } catch (e: Throwable) {
            throw OddParseException(e)
        }

        return media
    }

    fun parsePromotionResponse(responseBody: String): Promotion {
        var promotion: Promotion? = null
        try {
            val response = JSONObject(responseBody)
            val data = JSON.getJSONObject(response, DATA, true)
            promotion = parsePromotion(data)
            addIncludedFromResponse(promotion, response)
        } catch (e: Throwable) {
            throw OddParseException(e)
        }

        return promotion
    }

    protected fun parseExternalResponse(responseBody: String): External {
        var external: External? = null
        try {
            val response = JSONObject(responseBody)
            val data = JSON.getJSONObject(response, DATA, true)
            external = parseExternal(data)
            addIncludedFromResponse(external, response)
        } catch (e: Throwable) {
            throw OddParseException(e)
        }

        return external
    }

    protected fun parseEventResponse(responseBody: String): Event {
        var event: Event? = null
        try {
            val response = JSONObject(responseBody)
            val data = JSON.getJSONObject(response, DATA, true)
            event = parseEvent(data)
            addIncludedFromResponse(event, response)
        } catch (e: Throwable) {
            throw OddParseException(e)
        }

        return event
    }

    protected fun parseArticleResponse(responseBody: String): Article {
        var article: Article? = null
        try {
            val response = JSONObject(responseBody)
            val data = JSON.getJSONObject(response, DATA, true)
            article = parseArticle(data)
            addIncludedFromResponse(article, response)
        } catch (e: Throwable) {
            throw OddParseException(e)
        }

        return article
    }

    companion object {
        private val TAG = OddParser::class.java.simpleName
        val instance = OddParser()
        private val JSON = JSONParser.getInstance()

        private val DATA = "data"
        private val ATTRIBUTES = "attributes"
        private val META = "meta"
    }
}
