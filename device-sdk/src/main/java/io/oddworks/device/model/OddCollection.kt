package io.oddworks.device.model

import io.oddworks.device.exception.OddResourceException
import io.oddworks.device.model.common.*
import org.json.JSONObject
import java.util.*

class OddCollection(identifier: OddIdentifier,
                    relationships: Set<OddRelationship>,
                    included: MutableSet<OddResource>,
                    meta: JSONObject?,
                    val title: String,
                    val description: String,
                    val images: Set<OddImage>,
                    val genres: Set<String>,
                    val releaseDate: Date?) : OddResource(identifier, relationships, included, meta) {

    init {
        if (identifier.type != OddResourceType.COLLECTION) {
            throw OddResourceException("Mismatched OddResourceType identifier: $identifier")
        }
    }

    companion object {
        @JvmField val RELATIONSHIP_ENTITIES = "entities"
        @JvmField val RELATIONSHIP_FEATURED = "featured"
    }
}
