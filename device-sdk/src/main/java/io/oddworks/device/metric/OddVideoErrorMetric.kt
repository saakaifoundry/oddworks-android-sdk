package io.oddworks.device.metric

import org.json.JSONObject

class OddVideoErrorMetric(contentType: String, contentId: String, meta: JSONObject? = null) : OddMetric(contentType, contentId, meta) {
    override val action: String
        get() = OddVideoErrorMetric.action

    override val enabled: Boolean
        get() = OddVideoErrorMetric.enabled

    companion object {
        private val TAG = OddUserNewMetric::class.java.simpleName

        var action = OddMetric.ACTION_VIDEO_ERROR
        var enabled = false
    }
}