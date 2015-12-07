package io.oddworks.device.metric;

public class OddViewLoadMetric extends OddMetric {
    private static final String TAG = OddViewLoadMetric.class.getSimpleName();
    private static OddViewLoadMetric INSTANCE = new OddViewLoadMetric();

    private OddViewLoadMetric() {
        mAction = ACTION_VIEW_LOAD;
        // singleton
    }

    public static OddViewLoadMetric getInstance() {
        return INSTANCE;
    }
}
