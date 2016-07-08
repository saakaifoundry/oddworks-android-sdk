package io.oddworks.device.metric;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.oddworks.device.service.OddBus;

public abstract class OddMetric implements OddBus.OddRxBusEvent {
    private static final String TAG = OddMetric.class.getSimpleName();
    private static final String TYPE = "event";
    public static final String ACTION_APP_INIT = "app:init";
    public static final String ACTION_VIEW_LOAD = "view:load";
    public static final String ACTION_VIDEO_PLAY = "video:play";
    public static final String ACTION_VIDEO_PLAYING = "video:playing";
    public static final String ACTION_VIDEO_STOP = "video:stop";
    public static final String ACTION_VIDEO_ERROR = "video:error";

    protected String organizationId;
    protected String contentType;
    protected String contentId;


    public abstract String getAction();
    public abstract boolean getEnabled();

    public String getType() {
        return TYPE;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContentId() {
        return contentId;
    }

    public OddMetric setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    public OddMetric setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public OddMetric setContentId(String contentId) {
        this.contentId = contentId;
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
                "type=" + getType() + ", " +
                "organizationId=" + getOrganizationId() + ", " +
                "action=" + getAction() + ", " +
                "contentType=" + getContentType() + ", " +
                "contentId=" + getContentId() +
                ")";

    }
}