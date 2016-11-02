package io.oddworks.device.model

import io.oddworks.device.exception.OddResourceException
import io.oddworks.device.model.common.*

import org.json.JSONObject

class OddPromotion(identifier: OddIdentifier,
                   relationships: MutableSet<OddRelationship>,
                   included: MutableSet<OddResource>,
                   meta: JSONObject?,
                   val title: String,
                   val description: String,
                   override val images: Set<OddImage>,
                   val url: String) : OddResource(identifier, relationships, included, meta), OddImageable {
    init {
        if (identifier.type != OddResourceType.PROMOTION) {
            throw OddResourceException("Mismatched OddResourceType identifier: $identifier")
        }
    }
}
