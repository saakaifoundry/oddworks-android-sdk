package io.oddworks.device.metric;


public class OddVideoLoadMetric extends OddMetric {
    private static final String TAG = OddVideoLoadMetric.class.getSimpleName();

    protected static String videoLoadAction = ACTION_VIDEO_LOAD;
    protected static boolean videoLoadEnabled = false;

    public OddVideoLoadMetric() {

    }

    public static void setAction(String action) {
        videoLoadAction = action;
    }

    public static void setEnabled(boolean enabled) {
        videoLoadEnabled = enabled;
    }

    @Override
    public String getAction() {
        return videoLoadAction;
    }

    @Override
    public boolean getEnabled() {
        return videoLoadEnabled;
    }
}
