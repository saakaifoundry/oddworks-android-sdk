package io.oddworks.device.metric;

public class OddVideoErrorMetric extends OddMetric {
    private static final String TAG = OddVideoErrorMetric.class.getSimpleName();

    private static String videoErrorAction = ACTION_VIDEO_ERROR;
    private static boolean videoErrorEnabled = false;

    public OddVideoErrorMetric() {

    }

    public static void setAction(String action) {
        videoErrorAction = action;
    }

    public static void setEnabled(boolean enabled) {
        videoErrorEnabled = enabled;
    }

    @Override
    public String getAction() {
        return videoErrorAction;
    }

    @Override
    public boolean getEnabled() {
        return videoErrorEnabled;
    }
}