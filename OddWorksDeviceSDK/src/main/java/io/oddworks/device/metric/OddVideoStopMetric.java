package io.oddworks.device.metric;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class OddVideoStopMetric extends OddMetric {
    private static final String TAG = OddVideoStopMetric.class.getSimpleName();

    private int mElapsed;
    private int mDuration;

    /**
     * @param organizationId OddWorks organization slug
     * @param contentType OddWorks entity type
     * @param contentId OddWorks entity id
     * @param elapsed time in milliseconds
     * @param duration time in milliseconds
     */
    public OddVideoStopMetric(final String organizationId, final String contentType, final String contentId, final int elapsed, final int duration) {
        mAction = ACTION_VIDEO_STOP;
        mOrganizationId = organizationId;
        mContentType = contentType;
        mContentId = contentId;
        mElapsed = elapsed;
        mDuration = duration;
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
