package io.oddworks.device.metric;

public class OddVideoPlayMetric extends OddMetric {
    private static final String TAG = OddVideoPlayMetric.class.getSimpleName();
    private static final OddVideoPlayMetric INSTANCE = new OddVideoPlayMetric();

    private OddVideoPlayMetric() {
        mAction = ACTION_VIDEO_PLAY;
        // singleton
    }

    public static OddVideoPlayMetric getInstance() {
        return INSTANCE;
    }
}