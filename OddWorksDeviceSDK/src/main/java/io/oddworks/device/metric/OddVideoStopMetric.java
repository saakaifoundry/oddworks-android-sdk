package io.oddworks.device.metric;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class OddVideoStopMetric extends OddMetric {
    private static final String TAG = OddVideoStopMetric.class.getSimpleName();
    private static final OddVideoStopMetric INSTANCE = new OddVideoStopMetric();

    private int mElapsed;
    private int mDuration;

    private OddVideoStopMetric() {
        mAction = ACTION_VIDEO_STOP;
        // singleton
    }

    public static OddVideoStopMetric getInstance() {
        return INSTANCE;
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
    public OddVideoStopMetric setContentId(String contentId) {
        super.setContentId(contentId);
        return this;
    }

    @Override
    public OddVideoStopMetric setContentType(String contentType) {
        super.setContentType(contentType);
        return this;
    }

    @Override
    public OddVideoStopMetric setOrganizationId(String organizationId) {
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
