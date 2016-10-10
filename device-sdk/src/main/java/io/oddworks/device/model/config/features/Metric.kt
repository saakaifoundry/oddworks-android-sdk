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
                    OddAppInitMetric.setAction(metric.action)
                }
                MetricType.VIEW_LOAD -> {
                    OddViewLoadMetric.setEnabled(metric.enabled)
                    OddViewLoadMetric.setAction(metric.action)
                }
                MetricType.VIDEO_LOAD -> {
                    OddVideoLoadMetric.setEnabled(metric.enabled)
                    OddVideoLoadMetric.setAction(metric.action)
                }
                MetricType.VIDEO_PLAY -> {
                    OddVideoPlayMetric.setEnabled(metric.enabled)
                    OddVideoPlayMetric.setAction(metric.action)
                }
                MetricType.VIDEO_PLAYING -> {
                    OddVideoPlayingMetric.setEnabled(metric.enabled)
                    OddVideoPlayingMetric.setAction(metric.action)
                    OddVideoPlayingMetric.setInterval(metric.interval)
                }
                MetricType.VIDEO_STOP -> {
                    OddVideoStopMetric.setEnabled(metric.enabled)
                    OddVideoStopMetric.setAction(metric.action)
                }
                MetricType.VIDEO_ERROR -> {
                    OddVideoErrorMetric.setEnabled(metric.enabled)
                    OddVideoErrorMetric.setAction(metric.action)
                }
                MetricType.USER_NEW -> {
                    OddUserNewMetric.setEnabled(metric.enabled)
                    OddUserNewMetric.setAction(metric.action)
                }
            }
        }
    }
}

