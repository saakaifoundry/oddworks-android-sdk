package io.oddworks.device.request;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {
    private static final JSONParser INSTANCE = new JSONParser();

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
        try {
            if (json.isNull(key)) {
                return null;
            }
            return json.getString(key);
        } catch (JSONException e) {
            return null;
        }
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

    public JSONObject getJSONObject(final JSONObject json, String key, Boolean throwOnNull) throws JSONException {
        JSONObject jsonObject = json.optJSONObject(key);
        if (throwOnNull && jsonObject == null) {
            throw new JSONException("Unable to parse object: " + key);
        }
        return jsonObject;
    }

    public JSONArray getJSONArray(final JSONObject json, String key, Boolean throwOnNull) throws JSONException {
        JSONArray jsonArray = json.optJSONArray(key);
        if (throwOnNull && jsonArray == null) {
            throw new JSONException("Unable to parse array: " + key);
        }
        return jsonArray;
    }
}
