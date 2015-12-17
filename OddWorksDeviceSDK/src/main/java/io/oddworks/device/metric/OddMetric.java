package io.oddworks.device.metric;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class OddMetric {
    private static final String TAG = OddMetric.class.getSimpleName();
    private static final String TYPE = "event";
    public static final String ACTION_APP_INIT = "app:init";
    public static final String ACTION_VIEW_LOAD = "view:load";
    public static final String ACTION_VIDEO_PLAY = "video:play";
    public static final String ACTION_VIDEO_PLAYING = "video:playing";
    public static final String ACTION_VIDEO_STOP = "video:stop";
    public static final String ACTION_VIDEO_ERROR = "video:error";

    protected boolean mEnabled = false;
    protected String mOrganizationId;
    protected String mAction;
    protected String mContentType;
    protected String mContentId;

    public String getType() {
        return TYPE;
    }

    public boolean getEnabled() {
        return mEnabled;
    }

    public String getOrganizationId() {
        return mOrganizationId;
    }

    public String getAction() {
        return mAction;
    }

    public String getContentType() {
        return mContentType;
    }

    public String getContentId() {
        return mContentId;
    }

    public OddMetric setEnabled(boolean isEnabled) {
        mEnabled = isEnabled;
        return this;
    }

    public OddMetric setOrganizationId(String organizationId) {
        mOrganizationId = organizationId;
        return this;
    }

    public OddMetric setAction(String action) {
        mAction = action;
        return this;
    }

    public OddMetric setContentType(String contentType) {
        mContentType = contentType;
        return this;
    }

    public OddMetric setContentId(String contentId) {
        mContentId = contentId;
        return this;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        JSONObject attributes = new JSONObject();

        try {
            json.put("type", getType());

            attributes.put("organizationId", getOrganizationId());
            attributes.put("action", getAction());
            attributes.put("contentType", getContentType());
            attributes.put("contentId", getContentId());

            json.put("attributes", attributes);
        } catch (JSONException e) {
            Log.d(TAG, e.toString());
        }

        return json;
    }

    @Override
    public String toString() {
        return TAG + "(" +
                "mType=" + getType() + ", " +
                "mOrganizationId=" + getOrganizationId() + ", " +
                "mAction=" + getAction() + ", " +
                "mContentType=" + getContentType() + ", " +
                "mContentId=" + getContentId() +
                ")";

    }
}