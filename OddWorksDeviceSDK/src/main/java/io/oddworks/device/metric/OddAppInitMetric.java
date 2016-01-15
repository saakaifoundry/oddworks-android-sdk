package io.oddworks.device.metric;

public class OddAppInitMetric extends OddMetric {
    private static final String TAG = OddAppInitMetric.class.getSimpleName();

    private static String appInitAction = ACTION_APP_INIT;

    public OddAppInitMetric() {

    }

    public static void setAction(String action) {
        appInitAction = action;
    }

    public static void setEnabled(String mEnabled) {
        // Don't allow AppInit to be disabled
    }

    @Override
    public String getAction() {
        return appInitAction;
    }

    @Override
    public boolean getEnabled() {
        return true;
    }
}