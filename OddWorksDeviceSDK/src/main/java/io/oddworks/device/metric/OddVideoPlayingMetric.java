package io.oddworks.device.metric;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class OddVideoPlayingMetric extends OddMetric {
    private static final String TAG = OddVideoPlayingMetric.class.getSimpleName();
    private static final OddVideoPlayingMetric INSTANCE = new OddVideoPlayingMetric();

    private int mElapsed;
    private int mDuration;
    private int mInterval;

    private OddVideoPlayingMetric() {
        mAction = ACTION_VIDEO_PLAYING;
        // singleton
    }

    public static OddVideoPlayingMetric getInstance() {
        return INSTANCE;
    }

    public OddVideoPlayingMetric setElapsed(int elapsed) {
        mElapsed = elapsed;
        return this;
    }

    public OddVideoPlayingMetric setDuration(int duration) {
        mDuration = duration;
        return this;
    }

    public OddVideoPlayingMetric setInterval(int interval) {
        mInterval = interval;
        return this;
    }

    public int getInterval() {
        return mInterval;
    }

    @Override
    public OddVideoPlayingMetric setAction(String action) {
        super.setAction(action);
        return this;
    }

    @Override
    public OddVideoPlayingMetric setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        return this;
    }

    @Override
    public OddVideoPlayingMetric setContentId(String contentId) {
        super.setContentId(contentId);
        return this;
    }

    @Override
    public OddVideoPlayingMetric setContentType(String contentType) {
        super.setContentType(contentType);
        return this;
    }

    @Override
    public OddVideoPlayingMetric setOrganizationId(String organizationId) {
        super.setOrganizationId(organizationId);
        return this;
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
