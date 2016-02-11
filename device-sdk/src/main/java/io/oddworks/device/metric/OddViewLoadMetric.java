package io.oddworks.device.metric;

public class OddViewLoadMetric extends OddMetric {
    private static final String TAG = OddViewLoadMetric.class.getSimpleName();

    protected static String viewLoadAction = ACTION_VIEW_LOAD;
    protected static boolean viewLoadEnabled = false;

    public OddViewLoadMetric() {

    }

    public static void setAction(String action) {
        viewLoadAction = action;
    }

    public static void setEnabled(boolean enabled) {
        viewLoadEnabled = enabled;
    }

    @Override
    public String getAction() {
        return viewLoadAction;
    }

    @Override
    public boolean getEnabled() {
        return viewLoadEnabled;
    }
}
