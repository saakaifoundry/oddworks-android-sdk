package io.oddworks.device.metric;

public class OddVideoPlayMetric extends OddMetric {
    private static final String TAG = OddVideoPlayMetric.class.getSimpleName();

    protected static String videoPlayAction = ACTION_VIDEO_PLAY;
    protected static boolean videoPlayEnabled = false;

    public OddVideoPlayMetric() {

    }

    public static void setAction(String action) {
        videoPlayAction = action;
    }

    public static void setEnabled(boolean enabled) {
        videoPlayEnabled = enabled;
    }

    @Override
    public String getAction() {
        return videoPlayAction;
    }

    @Override
    public boolean getEnabled() {
        return videoPlayEnabled;
    }
}