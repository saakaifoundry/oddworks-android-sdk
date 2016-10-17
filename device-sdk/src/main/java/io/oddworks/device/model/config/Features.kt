package io.oddworks.device.model.config

import android.util.Log
import io.oddworks.device.model.common.Sharing
import io.oddworks.device.model.config.features.Authentication
import io.oddworks.device.model.config.features.Metric

data class Features(val authentication: Authentication, val sharing: Sharing, val metrics: Set<Metric>, val metricsEnabled: Boolean) {
    init {
        Log.d(Features::class.java.simpleName, "authentication[type: ${authentication.type}] sharing[enabled: ${sharing.enabled}] metrics[enabled: $metricsEnabled, total: ${metrics.size}] ")
    }
}