package io.oddworks.device.metric;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class OddVideoStopMetric extends OddMetric {
    private static final String TAG = OddVideoStopMetric.class.getSimpleName();

    protected static String videoStopAction = ACTION_VIDEO_STOP;
    protected static boolean videoStopEnabled = false;

    private int mElapsed;
    private int mDuration;

    public OddVideoStopMetric() {

    }

    public static void setAction(String action) {
        videoStopAction = action;
    }

    public static void setEnabled(boolean enabled) {
        videoStopEnabled = enabled;
    }


    public OddVideoStopMetric setElapsed(int elapsed) {
        mElapsed = elapsed;
        return this;
    }

    public OddVideoStopMetric setDuration(int duration) {
        mDuration = duration;
        return this;
    }

    @Override
    public String getAction() {
        return videoStopAction;
    }

    @Override
    public boolean getEnabled() {
        return videoStopEnabled;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = super.toJSONObject();

        try {
            JSONObject attributes = json.getJSONObject("attributes");

            attributes.put("elapsed", mElapsed);
            attributes.put("duration", mDuration);

            json.put("attributes", attributes);
        } catch (JSONException e) {
            Log.d(TAG, e.toString());
        }

        return json;
    }
}
