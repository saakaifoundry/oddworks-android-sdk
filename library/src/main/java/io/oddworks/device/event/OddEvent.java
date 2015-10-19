package io.oddworks.device.event;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by brkattk on 10/5/15.
 */
public abstract class OddEvent {
    private static final String TAG = OddEvent.class.getSimpleName();
    private static final String TYPE = "event";

    protected String mOrganizationId;
    protected String mAction;
    protected String mContentType;
    protected String mContentId;

    public String getType() {
        return TYPE;
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