package io.oddworks.device.metric

import org.json.JSONObject

class OddViewLoadMetric(contentType: String, contentId: String, meta: JSONObject? = null) : OddMetric(contentType, contentId, meta) {

    override val action: String
        get() = OddViewLoadMetric.action

    override val enabled: Boolean
        get() = OddViewLoadMetric.enabled

    companion object {
        private val TAG = OddViewLoadMetric::class.java.simpleName

        var action = OddMetric.ACTION_VIEW_LOAD
        var enabled = false
    }
}
