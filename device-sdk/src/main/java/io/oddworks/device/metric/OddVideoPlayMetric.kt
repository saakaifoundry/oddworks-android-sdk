package io.oddworks.device.metric

import org.json.JSONObject

class OddVideoPlayMetric(contentType: String, contentId: String, meta: JSONObject? = null) : OddMetric(contentType, contentId, meta) {

    override val action: String
        get() = OddVideoPlayMetric.action

    override val enabled: Boolean
        get() = OddVideoPlayMetric.enabled

    companion object {
        private val TAG = OddVideoPlayMetric::class.java.simpleName

        var action = OddMetric.ACTION_VIDEO_PLAY
        var enabled = false
    }
}