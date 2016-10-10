package io.oddworks.device.model

import android.util.Log
import io.oddworks.device.model.config.Display
import io.oddworks.device.model.config.Features

data class OddConfig(val views: Map<String, String>,
                     val display: Display,
                     val features: Features,
                     val jwt: String?) {
    init {
        Log.d(OddConfig::class.java.simpleName, "views[${views.keys}] jwt[exists: ${!jwt.isNullOrBlank()}")
    }
}
