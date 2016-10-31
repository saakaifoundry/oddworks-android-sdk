package io.oddworks.device.model

import io.oddworks.device.model.common.OddResource
import org.json.JSONObject

data class OddWatchlist(val viewer: OddViewer, val resource: OddResource, val addToWatchlist: Boolean) {

    val viewerId: String
        get() = viewer.identifier.id

    fun toJSONObject(): JSONObject {
        return resource.toRelationshipJSONObject()
    }
}
