package io.oddworks.device.metric;

public class OddVideoErrorMetric extends OddMetric {
    private static final String TAG = OddVideoErrorMetric.class.getSimpleName();
    private static OddVideoErrorMetric INSTANCE = new OddVideoErrorMetric();

    private OddVideoErrorMetric() {
        mAction = ACTION_VIDEO_ERROR;
        // singleton
    }

    public static OddVideoErrorMetric getInstance() {
        return INSTANCE;
    }
}