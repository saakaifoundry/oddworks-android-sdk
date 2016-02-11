package io.oddworks.device.metric;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class OddVideoPlayingMetric extends OddMetric {
    private static final String TAG = OddVideoPlayingMetric.class.getSimpleName();

    public static final int DEFAULT_INTERVAL = 10000;

    private static String videoPlayingAction = ACTION_VIDEO_PLAYING;
    private static boolean videoPlayingEnabled = false;
    private static int mInterval = DEFAULT_INTERVAL;

    private int mElapsed;
    private int mDuration;

    public OddVideoPlayingMetric() {

    }

    public static void setAction(String action) {
        videoPlayingAction = action;
    }

    public static void setEnabled(boolean enabled) {
        videoPlayingEnabled = enabled;
    }

    public static void setInterval(int interval) {
        mInterval = interval;
    }

    public static int getInterval() {
        return mInterval;
    }


    public OddVideoPlayingMetric setElapsed(int elapsed) {
        mElapsed = elapsed;
        return this;
    }

    public OddVideoPlayingMetric setDuration(int duration) {
        mDuration = duration;
        return this;
    }

    @Override
    public String getAction() {
        return videoPlayingAction;
    }

    @Override
    public boolean getEnabled() {
        return videoPlayingEnabled;
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
