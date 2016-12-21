package io.oddworks.device.metric

import org.json.JSONObject

class OddVideoErrorMetric(contentType: String, contentId: String, title: String, meta: JSONObject? = null) : OddMetric(contentType, contentId, title, meta) {
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