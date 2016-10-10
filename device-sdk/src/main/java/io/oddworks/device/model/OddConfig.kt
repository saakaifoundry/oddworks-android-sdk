package io.oddworks.device.model

import android.util.Log
import io.oddworks.device.model.common.OddIdentifier
import io.oddworks.device.model.common.OddResource
import io.oddworks.device.exception.OddResourceException
import io.oddworks.device.model.common.OddResourceType
import io.oddworks.device.model.config.Display
import io.oddworks.device.model.config.Features
import org.json.JSONObject

class OddConfig(identifier: OddIdentifier,
                meta: JSONObject?,
                val views: Map<String, String>,
                val display: Display,
                val features: Features,
                val jwt: String?) : OddResource(identifier, mutableSetOf(), mutableSetOf(), meta){

    init {
        if (identifier.type != OddResourceType.CONFIG) {
            throw OddResourceException("Mismatched OddResourceType identifier: $identifier")
        }

        Log.d(OddConfig::class.java.simpleName, "views[${views.keys}] jwt[exists: ${!jwt.isNullOrBlank()}")
    }
}
