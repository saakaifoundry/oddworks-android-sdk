package io.oddworks.device.request

import io.oddworks.device.exception.OddParseException
import io.oddworks.device.model.*
import io.oddworks.device.model.common.*
import io.oddworks.device.model.config.Display
import io.oddworks.device.model.config.Features
import io.oddworks.device.model.config.features.Authentication
import io.oddworks.device.model.config.features.Metric
import io.oddworks.device.model.video.OddCast
import io.oddworks.device.model.video.OddSource
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

object OddParser {
    private val JSON = JSONParser.getInstance()

    private val DATA = "data"
    private val ERRORS = "errors"
    private val ATTRIBUTES = "attributes"
    private val META = "meta"
    private val INCLUDED = "included"
    private val ID = "id"
    private val TYPE = "type"
    private val RELATIONSHIPS = "relationships"

    @Throws(JSONException::class)
    fun parseMultipleResponse(responseBody: String): LinkedHashSet<OddResource> {
        val rawBody = JSONObject(responseBody)
        val rawData = JSON.getJSONArray(rawBody, DATA, true)!!
        return parseResourceArray(rawData)
    }

    @Throws(JSONException::class, IllegalArgumentException::class, OddParseException::class)
    fun parseSingleResponse(responseBody: String): OddResource {
        val rawBody = JSONObject(responseBody)
        val rawData = JSON.getJSONObject(rawBody, DATA, true)!!
        val rawIncluded = JSON.getJSONArray(rawBody, INCLUDED, false)
        val rawType = JSON.getString(rawData, TYPE)?.toUpperCase() ?: throw OddParseException("parseSingleResponse() unable to determine resource type")

        return when (OddResourceType.valueOf(rawType)) {
            OddResourceType.COLLECTION -> parseCollection(rawData, rawIncluded)
            OddResourceType.PROMOTION -> parsePromotion(rawData, rawIncluded)
            OddResourceType.VIDEO -> parseVideo(rawData, rawIncluded)
            OddResourceType.VIEW -> parseView(rawData, rawIncluded)
            OddResourceType.CONFIG -> parseConfig(rawData)
            else -> {
                throw OddParseException("Attempting to parse un-parseable OddResourceType")
            }
        }

    }

    @Throws(JSONException::class)
    fun parseErrorMessage(responseBody: String): LinkedHashSet<OddError> {
        val rawBody = JSONObject(responseBody)
        val rawErrors = JSON.getJSONArray(rawBody, ERRORS, true)!!
        val errors = linkedSetOf<OddError>()

        for(i in 0..(rawErrors.length() -1)) {
            val rawError = rawErrors.getJSONObject(i)

            val id = JSON.getString(rawError, ID) ?: ""
            val status = JSON.getString(rawError, "status") ?: ""
            val code = JSON.getString(rawError, "code") ?: ""
            val title = JSON.getString(rawError, "title") ?: ""
            val detail = JSON.getString(rawError, "detail") ?: ""
            val meta = JSON.getJSONObject(rawError, META, false)

            errors.add(OddError(id, status, code, title, detail, meta))
        }

        return errors
    }

    @Throws(JSONException::class, IllegalArgumentException::class, OddParseException::class)
    private fun parseResourceArray(rawArray: JSONArray?): LinkedHashSet<OddResource> {
        val resources = linkedSetOf<OddResource>()
        if (rawArray == null) {
            return resources
        }
        for (i in 0..rawArray.length() - 1) {
            val includedObject = rawArray.getJSONObject(i)
            val rawType = JSON.getString(includedObject, TYPE)?.toUpperCase() ?: throw OddParseException("parseResourceArray() unable to determine resource type")

            val includedType = OddResourceType.valueOf(rawType)

            when (includedType) {
                OddResourceType.COLLECTION -> resources.add(parseCollection(includedObject))
                OddResourceType.PROMOTION -> resources.add(parsePromotion(includedObject))
                OddResourceType.VIDEO -> resources.add(parseVideo(includedObject))
                OddResourceType.VIEW -> resources.add(parseView(includedObject))
                OddResourceType.CONFIG -> resources.add(parseConfig(includedObject))
                else -> {
                    throw OddParseException("Attempting to parse un-parseable OddResourceType")
                }
            }
        }

        return resources
    }

    @Throws(JSONException::class, IllegalArgumentException::class)
    private fun parseConfig(rawData: JSONObject): OddConfig {
        val id = JSON.getString(rawData, ID) ?: throw OddParseException("parseConfig() missing id property")
        val identifier = OddIdentifier(id, OddResourceType.CONFIG)

        val rawAttributes = JSON.getJSONObject(rawData, ATTRIBUTES, true)
        val meta = JSON.getJSONObject(rawData, META, false)

        val views = mutableMapOf<String, String>()
        val rawViews = JSON.getJSONObject(rawAttributes, "views", false)
        if (rawViews != null) {
            val viewNames = rawViews.keys()
            while (viewNames.hasNext()) {
                val viewName = viewNames.next()
                val viewId = JSON.getString(rawViews, viewName) ?: throw OddParseException("parseConfig() missing View id")
                views.put(viewName, viewId)
            }
        }

        val display = parseDisplay(rawAttributes!!)
        val features = parseFeatures(rawAttributes!!)

        val jwt = JSON.getString(rawAttributes, "jwt")

        return OddConfig(identifier, meta, views, display, features, jwt)
    }

    @Throws(JSONException::class)
    private fun parseCollection(rawData: JSONObject, rawIncluded: JSONArray? = null): OddCollection {
        val id = JSON.getString(rawData, ID) ?: throw OddParseException("parseCollection() missing id property")
        val identifier = OddIdentifier(id, OddResourceType.COLLECTION)

        val rawAttributes = JSON.getJSONObject(rawData, ATTRIBUTES, true)
        val meta = JSON.getJSONObject(rawData, META, false)
        val rawImages = JSON.getJSONArray(rawAttributes, "images", false)

        val title = JSON.getString(rawAttributes, "title") ?: ""
        val description = JSON.getString(rawAttributes, "description") ?: ""
        val releaseDate = JSON.getDate(rawAttributes, "releaseDate")
        val images = parseImages(rawImages)

        val rawRelationships = JSON.getJSONObject(rawData, RELATIONSHIPS, false)
        val relationships = parseRelationships(rawRelationships)

        val rawGenres = JSON.getJSONArray(rawAttributes, "genres", false)
        val genres = parseGenres(rawGenres)

        val included = parseResourceArray(rawIncluded)

        return OddCollection(
                identifier,
                relationships,
                included,
                meta,
                title,
                description,
                images,
                genres,
                releaseDate)
    }

    @Throws(JSONException::class)
    private fun parseVideo(rawData: JSONObject, rawIncluded: JSONArray? = null): OddVideo {
        val id = JSON.getString(rawData, ID) ?: throw OddParseException("parseVideo() missing id property")
        val identifier = OddIdentifier(id, OddResourceType.VIDEO)

        val rawAttributes = JSON.getJSONObject(rawData, ATTRIBUTES, true)
        val meta = JSON.getJSONObject(rawData, META, false)
        val rawImages = JSON.getJSONArray(rawAttributes, "images", false)

        val title = JSON.getString(rawAttributes, "title") ?: ""
        val description = JSON.getString(rawAttributes, "description") ?: ""
        val images = parseImages(rawImages)
        val releaseDate = JSON.getDate(rawAttributes, "releaseDate")
        val duration = JSON.getInt(rawAttributes, "duration")

        val rawSources = JSON.getJSONArray(rawAttributes, "sources", false)
        val sources = parseSources(rawSources)

        val rawRelationships = JSON.getJSONObject(rawData, RELATIONSHIPS, false)
        val relationships = parseRelationships(rawRelationships)

        val rawGenres = JSON.getJSONArray(rawAttributes, "genres", false)
        val genres = parseGenres(rawGenres)


        val rawCast = JSON.getJSONArray(rawAttributes, "cast", false)
        val cast = parseCast(rawCast)

        val included = parseResourceArray(rawIncluded)

        return OddVideo(
                identifier,
                relationships,
                included,
                meta,
                title,
                description,
                images,
                sources,
                duration,
                genres,
                cast,
                releaseDate)
    }

    @Throws(JSONException::class)
    private fun parsePromotion(rawData: JSONObject, rawIncluded: JSONArray? = null): OddPromotion {
        val id = JSON.getString(rawData, ID) ?: throw OddParseException("parseVideo() missing id property")
        val identifier = OddIdentifier(id, OddResourceType.PROMOTION)

        val rawAttributes = JSON.getJSONObject(rawData, ATTRIBUTES, true)
        val meta = JSON.getJSONObject(rawData, META, false)
        val rawImages = JSON.getJSONArray(rawAttributes, "images", false)

        val title = JSON.getString(rawAttributes, "title") ?: ""
        val description = JSON.getString(rawAttributes, "description") ?: ""
        val url = JSON.getString(rawAttributes, "url") ?: ""
        val images = parseImages(rawImages)

        val rawRelationships = JSON.getJSONObject(rawData, RELATIONSHIPS, false)
        val relationships = parseRelationships(rawRelationships)

        val included = parseResourceArray(rawIncluded)

        return OddPromotion(identifier, relationships, included, meta, title, description, images, url)
    }

    @Throws(JSONException::class)
    private fun parseView(rawData: JSONObject, rawIncluded: JSONArray? = null): OddView {
        val id = JSON.getString(rawData, ID) ?: throw OddParseException("parseVideo() missing id property")
        val identifier = OddIdentifier(id, OddResourceType.VIEW)

        val rawAttributes = JSON.getJSONObject(rawData, ATTRIBUTES, true)
        val meta = JSON.getJSONObject(rawData, META, false)
        val rawImages = JSON.getJSONArray(rawAttributes, "images", false)

        val images = parseImages(rawImages)

        val title = JSON.getString(rawAttributes, "title") ?: ""

        val rawRelationships = JSON.getJSONObject(rawData, RELATIONSHIPS, false)
        val relationships = parseRelationships(rawRelationships)

        val included = parseResourceArray(rawIncluded)

        return OddView(
                identifier,
                relationships,
                included,
                meta,
                title,
                images
                )
    }

    @Throws(JSONException::class)
    private fun parseDisplay(rawAttributes: JSONObject): Display {
        val rawDisplay = JSON.getJSONObject(rawAttributes, "display", false)

        if (rawDisplay != null) {
            val rawImages = JSON.getJSONArray(rawDisplay, "images", false)
            val rawColors = JSON.getJSONArray(rawDisplay, "colors", false)
            val rawFonts = JSON.getJSONArray(rawDisplay, "fonts", false)
            return Display(parseImages(rawImages), parseColors(rawColors), parseFonts(rawFonts))
        } else {
            return Display(emptySet(), emptySet(), emptySet())
        }
    }

    @Throws(JSONException::class)
    private fun parseImages(rawImages: JSONArray?): Set<OddImage> {
        if (rawImages == null) {
            return emptySet()
        }
        val images = mutableSetOf<OddImage>()

        for(i in 0..(rawImages.length() -1)) {
            val rawImage = rawImages.getJSONObject(i)

            val url = JSON.getString(rawImage, "url") ?: ""
            val mimeType = JSON.getString(rawImage, "mimeType") ?: ""
            val width = JSON.getInt(rawImage, "width")
            val height = JSON.getInt(rawImage, "height")
            val label = JSON.getString(rawImage, "label") ?: ""

            images.add(OddImage(url, mimeType, width, height, label))
        }

        return images.toSet()
    }

    @Throws(JSONException::class)
    private fun parseColors(rawColors: JSONArray?): Set<OddColor> {
        if (rawColors == null) {
            return emptySet()
        }
        val colors = mutableSetOf<OddColor>()

        for(i in 0..(rawColors.length() -1)) {
            val rawColor = rawColors.getJSONObject(i)

            val red = JSON.getInt(rawColor, "red")
            val green = JSON.getInt(rawColor, "green")
            val blue = JSON.getInt(rawColor, "blue")
            val alpha = JSON.getInt(rawColor, "alpha")
            val label = JSON.getString(rawColor, "label") ?: ""

            colors.add(OddColor(red, green, blue, alpha, label))
        }

        return colors.toSet()
    }

    @Throws(JSONException::class)
    private fun parseFonts(rawFonts: JSONArray?): Set<OddFont> {
        if (rawFonts == null) {
            return emptySet()
        }
        val fonts = mutableSetOf<OddFont>()

        for(i in 0..(rawFonts.length() -1)) {
            val rawFont = rawFonts.getJSONObject(i)

            val name = JSON.getString(rawFont, "name") ?: ""
            val size = JSON.getInt(rawFont, "size")
            val label = JSON.getString(rawFont, "label") ?: ""

            fonts.add(OddFont(name, size, label))
        }

        return fonts.toSet()
    }

    private fun parseFeatures(rawAttributes: JSONObject): Features {
        val rawFeatures = JSON.getJSONObject(rawAttributes, "features", false) ?: return Features(Authentication(false, Authentication.AuthenticationType.DISABLED), Sharing(false), emptySet(), false)

        val metricsConfig = parseMetricsFeature(rawFeatures)

        return Features(parseAuthenticationFeature(rawFeatures), parseSharingFeature(rawFeatures), metricsConfig.first, metricsConfig.second)
    }

    @Throws(JSONException::class, IllegalArgumentException::class)
    private fun parseAuthenticationFeature(rawFeatures: JSONObject): Authentication {
        val rawAuthentication = JSON.getJSONObject(rawFeatures, "authentication", false) ?: return Authentication(false, Authentication.AuthenticationType.DISABLED)

        val enabled = JSON.getBoolean(rawAuthentication, "enabled")
        val type = JSON.getString(rawAuthentication, TYPE)?.toUpperCase()
        val authenticationType = if (type == null) {
            Authentication.AuthenticationType.DISABLED
        } else {
            Authentication.AuthenticationType.valueOf(type)
        }


        val properties = mutableMapOf<String, String>()
        rawAuthentication.keys().forEach {
            when (it) {
                "enabled" -> {}
                TYPE -> {}
                else -> {
                    properties.put(it, JSON.getString(rawAuthentication, it) ?: "")
                }
            }
        }


        return Authentication(enabled, authenticationType, properties)
    }

    @Throws(JSONException::class)
    private fun parseSharingFeature(rawFeatures: JSONObject): Sharing {
        val rawSharing = JSON.getJSONObject(rawFeatures, "sharing", false) ?: return Sharing(false)
        return parseSharing(rawSharing)
    }

    @Throws(JSONException::class)
    private fun parseMetricsFeature(rawFeatures: JSONObject): Pair<Set<Metric>, Boolean> {
        val rawMetrics = JSON.getJSONObject(rawFeatures, "metrics", false) ?: return Pair(emptySet(), false)

        val enabled = JSON.getBoolean(rawMetrics, "enabled")

        val metrics = mutableSetOf<Metric>()
        Metric.MetricType.values().forEach {
            val rawMetric = JSON.getJSONObject(rawMetrics, it.key, false) ?: return@forEach
            val individualEnabled = JSON.getBoolean(rawMetric, "enabled")
            val action = JSON.getString(rawMetric, "action")
            val interval = JSON.getInt(rawMetric, "interval")
            metrics.add(Metric(it, individualEnabled, action, interval))
        }

        Metric.setupOddMetrics(metrics.toSet()) // MAGIC!

        return Pair(metrics, enabled)

    }

    @Throws(JSONException::class)
    private fun parseSharing(rawSharing: JSONObject): Sharing {
        val enabled = JSON.getBoolean(rawSharing, "enabled")
        val text = JSON.getString(rawSharing, "text") ?: ""
        return Sharing(enabled, text)
    }

    @Throws(JSONException::class)
    private fun parseGenres(rawGenres: JSONArray?): MutableSet<String> {
        val genres = mutableSetOf<String>()
        if (rawGenres != null) {
            for (i in 0..(rawGenres.length() -1)) {
                val genre = rawGenres.getString(i)
                if (genre != null) {
                    genres.add(genre)
                }
            }
        }
        return genres
    }

    @Throws(JSONException::class)
    private fun parseSources(rawSources: JSONArray?): MutableSet<OddSource> {
        val sources = mutableSetOf<OddSource>()
        if (rawSources != null) {
            for (i in 0..(rawSources.length() -1)) {
                val rawSource = rawSources.getJSONObject(i)

                val url = JSON.getString(rawSource, "url") ?: ""
                val container = JSON.getString(rawSource, "container") ?: ""
                val mimeType = JSON.getString(rawSource, "mimeType") ?: ""
                val width = JSON.getInt(rawSource, "width")
                val height = JSON.getInt(rawSource, "height")
                val maxBitrate = JSON.getInt(rawSource, "maxBitrate")
                val label = JSON.getString(rawSource, "label") ?: ""

                sources.add(OddSource(url, container, mimeType, width, height, maxBitrate, label))
            }
        }

        return sources
    }

    @Throws(JSONException::class)
    private fun parseCast(rawCast: JSONArray?): MutableSet<OddCast> {
        val cast = mutableSetOf<OddCast>()
        if (rawCast != null) {
            for (i in 0..(rawCast.length() -1)) {
                val rawCastMember = rawCast.getJSONObject(i)

                val name = JSON.getString(rawCastMember, "name") ?: ""
                val role = JSON.getString(rawCastMember, "role") ?: ""
                val character = JSON.getString(rawCastMember, "character") ?: ""

                cast.add(OddCast(name, role, character))
            }
        }

        return cast
    }

//    @Throws(JSONException::class)
//    protected fun parsePromotion(rawPromotion: JSONObject): OddPromotion {
//        val rawAttributes = JSON.getJSONObject(rawPromotion, ATTRIBUTES, true)
//        val meta = JSON.getJSONObject(rawPromotion, META, false)
//        val images = JSON.getJSONObject(rawAttributes, "images", false)
//
//        val promotion = OddPromotion(
//                JSON.getString(rawPromotion, ID),
//                JSON.getString(rawPromotion, TYPE))
//        promotion.meta = meta
//
//        val attributes = HashMap<String, Any>()
//        attributes.put("title", JSON.getString(rawAttributes, "title"))
//        attributes.put("description", JSON.getString(rawAttributes, "description"))
//        attributes.put("url", JSON.getString(rawAttributes, "url"))
//        attributes.put("mediaImage", parseMediaImage(images))
//
//        promotion.attributes = attributes
//
//        return promotion
//    }

//    fun parseEntityList(result: String): List<OddObject> {
//        val entities = ArrayList<OddObject>()
//        try {
//            val entitiesResponse = JSONObject(result)
//            val rawEntities = entitiesResponse.getJSONArray(DATA)
//            for (i in 0..rawEntities.length() - 1) {
//                val rawEntity = rawEntities.getJSONObject(i)
//                val type = JSON.getString(rawEntity, TYPE)
//                when (type) {
//                    OddObject.TYPE_ARTICLE -> entities.add(parseArticle(rawEntity))
//                    OddObject.TYPE_COLLECTION -> entities.add(parseCollection(rawEntity))
//                    OddObject.TYPE_EVENT -> entities.add(parseEvent(rawEntity))
//                    OddObject.TYPE_EXTERNAL -> entities.add(parseExternal(rawEntity))
//                    OddObject.TYPE_PROMOTION -> entities.add(parsePromotion(rawEntity))
//                    OddObject.TYPE_LIVE_STREAM, OddObject.TYPE_VIDEO -> entities.add(parseMedia(rawEntity))
//                }
//            }
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//        return entities
//    }

//    fun parseViewResponse(result: String): OddView? {
//        try {
//            val resultObject = JSONObject(result)
//            val data = JSON.getJSONObject(resultObject, DATA, true)
//            val meta = JSON.getJSONObject(resultObject, META, false)
//            val rawAttributes = JSON.getJSONObject(data, ATTRIBUTES, true)
//            val view = OddView(
//                    JSON.getString(data, ID),
//                    JSON.getString(data, TYPE))
//            view.meta = meta
//
//            val attributes = HashMap<String, Any>()
//            attributes.put("title", JSON.getString(rawAttributes, "title"))
//            view.attributes = attributes
//
//            // use relationships to build view's ArrayList<OddRelationship>
//            parseRelationships(JSON.getJSONObject(data, RELATIONSHIPS, true), view)
//
//            // fill the view's included{Type} arrays with parsable objects
//            addIncludedFromResponse(view, resultObject)
//            // backfill newly created collections with newly created entities
//            view.fillIncludedCollections()
//
//            return view
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//        return null
//    }

//    fun parseSearch(result: String): List<OddObject> {
//        val searchResult = ArrayList<OddObject>()
//        try {
//            val resultObject = JSONObject(result)
//            val dataArray = resultObject.getJSONArray(DATA)
//            for (i in 0..dataArray.length() - 1) {
//                val item = dataArray.getJSONObject(i)
//                val type = JSON.getString(item, TYPE)
//
//                when (type) {
//                    OddObject.TYPE_VIDEO, OddObject.TYPE_LIVE_STREAM -> {
//                        val video = parseMedia(item)
//                        if (video != null) {
//                            searchResult.add(video)
//                        }
//                    }
//                    OddObject.TYPE_COLLECTION -> {
//                        val collection = parseCollection(item)
//                        if (collection != null) {
//                            searchResult.add(collection)
//                        }
//                    }
//                }
//            }
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//        return searchResult
//    }

    @Throws(JSONException::class, IllegalArgumentException::class)
    private fun parseRelationships(rawRelationships: JSONObject?): MutableSet<OddRelationship> {
        val relationships = mutableSetOf<OddRelationship>()
        if (rawRelationships == null) {
            return relationships
        }
        val relationshipNames = rawRelationships.keys()
        while (relationshipNames.hasNext()) {
            val name = relationshipNames.next()
            val relationship = JSON.getJSONObject(rawRelationships, name, true)
            if (relationship != null) {
                val identifiers = linkedSetOf<OddIdentifier>()

                if (relationship.get(DATA) is JSONObject) {
                    //handle single relationship.data
                    val identifier = JSON.getJSONObject(relationship, DATA, true)!!
                    val relId = JSON.getString(identifier, ID) ?: throw OddParseException("parseRelationships() missing id property")
                    val relType = JSON.getString(identifier, TYPE)?.toUpperCase() ?: throw OddParseException("parseRelationships() missing type property")
                    identifiers.add(OddIdentifier(relId, OddResourceType.valueOf(relType)))

                } else if (relationship.get(DATA) is JSONArray) {
                    // handle multiple relationship.data
                    val rawIdentifiers = JSON.getJSONArray(relationship, DATA, true)!!

                    for (i in 0..rawIdentifiers.length() - 1) {
                        val identifier = rawIdentifiers.getJSONObject(i)
                        val relId = JSON.getString(identifier, ID) ?: throw OddParseException("parseRelationships() missing id property")
                        val relType = JSON.getString(identifier, TYPE)?.toUpperCase() ?: throw OddParseException("parseRelationships() missing type property")
                        identifiers.add(OddIdentifier(relId, OddResourceType.valueOf(relType)))
                    }
                }

                relationships.add(OddRelationship(name, identifiers))
            }
        }
        return relationships
    }

}
