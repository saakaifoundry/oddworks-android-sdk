package io.oddworks.device.model;

import java.util.List;

import io.oddworks.device.metric.OddAppInitMetric;
import io.oddworks.device.metric.OddVideoErrorMetric;
import io.oddworks.device.metric.OddVideoPlayMetric;
import io.oddworks.device.metric.OddVideoPlayingMetric;
import io.oddworks.device.metric.OddVideoStopMetric;
import io.oddworks.device.metric.OddViewLoadMetric;

public class MetricsConfig {
    public static final String ACTION_APP_INIT = "appInit";
    public static final String ACTION_VIEW_LOAD = "viewLoad";
    public static final String ACTION_VIDEO_PLAY = "videoPlay";
    public static final String ACTION_VIDEO_PLAYING = "videoPlaying";
    public static final String ACTION_VIDEO_STOP = "videoStop";
    public static final String ACTION_VIDEO_ERROR = "videoError";
    public static final String[] ACTION_KEYS = { ACTION_APP_INIT, ACTION_VIEW_LOAD, ACTION_VIDEO_PLAY, ACTION_VIDEO_PLAYING, ACTION_VIDEO_STOP, ACTION_VIDEO_ERROR };

    private final List<Metric> metrics;

    public MetricsConfig(List<Metric> metrics) {
        this.metrics = metrics;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setupOddMetrics() {
        if (metrics != null && !metrics.isEmpty()) {
            for(Metric metric : metrics) {
                setupOddMetric(metric);
            }
        }
    }

    private void setupOddMetric(Metric metric) {
        Boolean enabled = metric.get(Metric.ENABLED);
        String action = metric.get(Metric.ACTION);

        switch (metric.getType()) {
            case ACTION_APP_INIT:
                OddAppInitMetric.setAction(action);
                break;
            case ACTION_VIEW_LOAD:
                OddViewLoadMetric.setEnabled(enabled);
                OddViewLoadMetric.setAction(action);
                break;
            case ACTION_VIDEO_PLAY:
                OddVideoPlayMetric.setEnabled(enabled);
                OddVideoPlayMetric.setAction(action);
                break;
            case ACTION_VIDEO_PLAYING:
                Integer interval = metric.get(Metric.INTERVAL);
                OddVideoPlayingMetric.setEnabled(enabled);
                OddVideoPlayingMetric.setAction(action);
                OddVideoPlayingMetric.setInterval(interval);
                break;
            case ACTION_VIDEO_STOP:
                OddVideoStopMetric.setEnabled(enabled);
                OddVideoStopMetric.setAction(action);
                break;
            case ACTION_VIDEO_ERROR:
                OddVideoErrorMetric.setEnabled(enabled);
                OddVideoErrorMetric.setAction(action);
                break;
            default:
                // no-op
                break;
        }
    }
}
