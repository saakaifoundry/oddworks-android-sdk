package io.oddworks.device.metric;

public class OddAppInitMetric extends OddMetric {
    private static final String TAG = OddAppInitMetric.class.getSimpleName();
    private static OddAppInitMetric INSTANCE = new OddAppInitMetric();

    private OddAppInitMetric(){
        mAction = ACTION_APP_INIT;
        // singleton
    }

    public static OddAppInitMetric getInstance() {
        return INSTANCE;
    }
}