package io.oddworks.device.model

import io.oddworks.device.exception.OddResourceException
import io.oddworks.device.model.common.*
import org.json.JSONObject

class OddView(identifier: OddIdentifier,
              relationships: MutableSet<OddRelationship>,
              included: MutableSet<OddResource>,
              meta: JSONObject?, val title: String, val images: Set<OddImage>) : OddResource(identifier, relationships, included, meta) {
    init {
        if (identifier.type != OddResourceType.VIEW) {
            throw OddResourceException("Mismatched OddResourceType identifier: $identifier")
        }
    }
}
