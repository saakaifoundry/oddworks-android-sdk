package io.oddworks.device.model.config.features

import io.oddworks.device.metric.*

class Metric(val type: MetricType, val enabled: Boolean, action: String?, val interval: Int = 0) {

    val action: String

    init {
        if (action == null) {
            this.action = type.defaultAction
        } else {
            this.action = action
        }
    }

    enum class MetricType(val key: String, val defaultAction: String) {
        APP_INIT("appInit", "app:init"),
        VIEW_LOAD("viewLoad", "view:load"),
        VIDEO_LOAD("videoLoad", "video:load"),
        VIDEO_PLAY("videoPlay", "video:play"),
        VIDEO_PLAYING("videoPlaying", "video:playing"),
        VIDEO_STOP("videoStop", "video:stop"),
        VIDEO_ERROR("videoError", "video:error"),
        USER_NEW("userNew", "user:new")
    }

    companion object {
        fun setupOddMetrics(metrics: Set<Metric>) {
            metrics.forEach {
                setupOddMetric(it)
            }
        }

        private fun setupOddMetric(metric: Metric) {
            when(metric.type) {
                MetricType.APP_INIT -> {
                    OddAppInitMetric.action = metric.action
                }
                MetricType.VIEW_LOAD -> {
                    OddViewLoadMetric.enabled = metric.enabled
                    OddViewLoadMetric.action = metric.action
                }
                MetricType.VIDEO_LOAD -> {
                    OddVideoLoadMetric.enabled = metric.enabled
                    OddVideoLoadMetric.action = metric.action
                }
                MetricType.VIDEO_PLAY -> {
                    OddVideoPlayMetric.enabled = metric.enabled
                    OddVideoPlayMetric.action = metric.action
                }
                MetricType.VIDEO_PLAYING -> {
                    OddVideoPlayingMetric.enabled = metric.enabled
                    OddVideoPlayingMetric.action = metric.action
                    OddVideoPlayingMetric.interval = metric.interval
                }
                MetricType.VIDEO_STOP -> {
                    OddVideoStopMetric.enabled = metric.enabled
                    OddVideoStopMetric.action = metric.action
                }
                MetricType.VIDEO_ERROR -> {
                    OddVideoErrorMetric.enabled = metric.enabled
                    OddVideoErrorMetric.action = metric.action
                }
                MetricType.USER_NEW -> {
                    OddUserNewMetric.enabled = metric.enabled
                    OddUserNewMetric.action = metric.action
                }
            }
        }
    }
}

