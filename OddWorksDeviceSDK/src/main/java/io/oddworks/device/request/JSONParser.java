package io.oddworks.device.request;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
    private static JSONParser INSTANCE = new JSONParser();

    private JSONParser() {
        // singleton
    }

    public static JSONParser getInstance() {
        return INSTANCE;
    }

    /**
     * @param json  object containing the String value
     * @param key   key at which the String value is located
     * @return      defaults to null
     **/
    public String getString(final JSONObject json, String key) {
        return json.optString(key);
    }

    /**
     * @param json  object containing the DateTime value
     * @param key   key at which the DateTime value is located
     * @return      defaults to null
     **/
    public DateTime getDateTime(final JSONObject json, String key) throws JSONException {
        if (!json.isNull(key)) {
            return new DateTime(json.getString(key));
        }
        return null;
    }

    /**
     * @param json  object containing the int value
     * @param key   key at which the int value is located
     * @return      defaults to 0
     **/
    public int getInt(final JSONObject json, String key) {
        return json.optInt(key, 0);
    }

    /**
     * @param json  object containing the boolean value
     * @param key   key at which the boolean value is located
     * @return      defaults to false
     **/
    public boolean getBoolean(final JSONObject json, String key) {
        return json.optBoolean(key, false);
    }

    public JSONObject getJSONObject(final JSONObject json, String key) throws JSONException {
        JSONObject obj = null;
        if (!json.isNull(key)) {
            obj = json.getJSONObject(key);
        }
        if (obj == null) {
            throw new JSONException(key);
        }

        return obj;
    }

    public JSONArray getJSONArray(final JSONObject json, String key) throws JSONException {
        JSONArray obj = null;
        if (!json.isNull(key)) {
            obj = json.getJSONArray(key);
        }
        if (obj == null) {
            throw new JSONException(key);
        }

        return obj;
    }
}
