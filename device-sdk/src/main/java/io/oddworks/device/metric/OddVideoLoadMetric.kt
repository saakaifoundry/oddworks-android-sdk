package io.oddworks.device.metric

import org.json.JSONObject


class OddVideoLoadMetric(contentType: String, contentId: String, meta: JSONObject? = null) : OddMetric(contentType, contentId, meta) {

    override val action: String
        get() = OddVideoLoadMetric.action

    override val enabled: Boolean
        get() = OddVideoLoadMetric.enabled

    companion object {
        private val TAG = OddVideoLoadMetric::class.java.simpleName

        var action = OddMetric.ACTION_VIDEO_LOAD
        var enabled = false
    }
}
