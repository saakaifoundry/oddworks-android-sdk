package io.oddworks.device.metric;


public class OddUserNewMetric extends OddMetric {
    private static final String TAG = OddUserNewMetric.class.getSimpleName();

    protected static String userNewAction = ACTION_USER_NEW;
    protected static boolean userNewEnabled = false;

    public OddUserNewMetric() {

    }

    public static void setAction(String action) {
        userNewAction = action;
    }

    public static void setEnabled(boolean enabled) {
        userNewEnabled = enabled;
    }

    @Override
    public String getAction() {
        return userNewAction;
    }

    @Override
    public boolean getEnabled() {
        return userNewEnabled;
    }
}
