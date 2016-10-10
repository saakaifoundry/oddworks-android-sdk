package io.oddworks.device.model


import io.oddworks.device.exception.OddRequestException
import io.oddworks.device.exception.OddResourceException
import io.oddworks.device.model.common.*

import io.oddworks.device.model.video.OddCast
import io.oddworks.device.model.video.OddSource
import org.joda.time.DateTime
import org.json.JSONObject

class OddVideo(identifier: OddIdentifier,
               relationships: MutableSet<OddRelationship>,
               included: MutableSet<OddResource>,
               meta: JSONObject?,
               val title: String,
               val description: String,
               val images: Set<OddImage>,
               val sources: Set<OddSource>,
               val duration: Int = 0,
               val genres: Set<String>,
               val cast: Set<OddCast>,
               val releaseDate: DateTime?) : OddResource(identifier, relationships, included, meta) {
    init {
        if (identifier.type != OddResourceType.VIDEO) {
            throw OddResourceException("Mismatched OddResourceType identifier: $identifier")
        }
    }
}
