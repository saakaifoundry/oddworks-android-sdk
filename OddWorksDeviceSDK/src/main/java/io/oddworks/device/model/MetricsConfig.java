package io.oddworks.device.model;

import java.util.List;

import io.oddworks.device.metric.*;

public class MetricsConfig {
    public static final String ACTION_APP_INIT = "appInit";
    public static final String ACTION_VIEW_LOAD = "viewLoad";
    public static final String ACTION_VIDEO_PLAY = "videoPlay";
    public static final String ACTION_VIDEO_PLAYING = "videoPlaying";
    public static final String ACTION_VIDEO_STOP = "videoStop";
    public static final String ACTION_VIDEO_ERROR = "videoError";
    public static final String[] ACTION_KEYS = { ACTION_APP_INIT, ACTION_VIEW_LOAD, ACTION_VIDEO_PLAY, ACTION_VIDEO_PLAYING, ACTION_VIDEO_STOP, ACTION_VIDEO_ERROR };

    private final List<Metric> mMetrics;

    public MetricsConfig(List<Metric> metrics) {
        this.mMetrics = metrics;
    }

    public List<Metric> getMetrics() {
        return mMetrics;
    }

    public void setupOddMetrics() {
        if (mMetrics != null || !mMetrics.isEmpty()) {
            for(Metric metric : mMetrics) {
                setupOddMetric(metric);
            }
        }
    }

    private void setupOddMetric(Metric metric) {
        Boolean enabled = metric.get(Metric.ENABLED);
        String action = metric.get(Metric.ACTION);

        switch (metric.getType()) {
            case ACTION_APP_INIT:
                OddAppInitMetric
                        .getInstance()
                        .setEnabled(enabled)
                        .setAction(action);
                break;
            case ACTION_VIEW_LOAD:
                OddViewLoadMetric
                        .getInstance()
                        .setEnabled(enabled)
                        .setAction(action);
                break;
            case ACTION_VIDEO_PLAY:
                OddVideoPlayMetric
                        .getInstance()
                        .setEnabled(enabled)
                        .setAction(action);
                break;
            case ACTION_VIDEO_PLAYING:
                Integer interval = metric.get(Metric.INTERVAL);
                OddVideoPlayingMetric
                        .getInstance()
                        .setEnabled(enabled)
                        .setAction(action)
                        .setInterval(interval);
                break;
            case ACTION_VIDEO_STOP:
                OddVideoStopMetric
                        .getInstance()
                        .setEnabled(enabled)
                        .setAction(action);
                break;
            case ACTION_VIDEO_ERROR:
                OddVideoErrorMetric
                        .getInstance()
                        .setEnabled(enabled)
                        .setAction(action);
                break;
            default:
                // no-op
                break;
        }
    }
}
