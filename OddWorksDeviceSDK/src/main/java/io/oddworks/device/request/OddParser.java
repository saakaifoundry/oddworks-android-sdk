package io.oddworks.device.request;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import io.oddworks.device.model.AuthToken;
import io.oddworks.device.model.Config;
import io.oddworks.device.model.DeviceCodeResponse;
import io.oddworks.device.model.Identifier;
import io.oddworks.device.model.Media;
import io.oddworks.device.model.MediaAd;
import io.oddworks.device.model.MediaCollection;
import io.oddworks.device.model.MediaImage;
import io.oddworks.device.model.OddObject;
import io.oddworks.device.model.OddView;
import io.oddworks.device.model.Promotion;
import io.oddworks.device.model.Relationship;
//todo stop swallowing exceptions

public class OddParser {
    private static final String TAG = OddParser.class.getSimpleName();
    protected static OddParser instance;

    protected OddParser(){}

    private String parseString(final JSONObject json, String key) throws JSONException {
        String value = null;
        if (!json.isNull(key)) {
            value = json.getString(key);
        }

        return value;
    }

    private int parseInt(final JSONObject json, String key) throws JSONException {
        int value = 0;
        if (!json.isNull(key)) {
            value = json.getInt(key);
        }

        return value;
    }

    private JSONObject parseJSONObject(final JSONObject json, String key) throws JSONException {
        JSONObject obj = null;
        if (!json.isNull(key)) {
            obj = json.getJSONObject(key);
        }
        if (obj == null) {
            throw new JSONException(key);
        }

        return obj;
    }

    private JSONArray parseJSONArray(final JSONObject json, String key) throws JSONException {
        JSONArray obj = null;
        if (!json.isNull(key)) {
            obj = json.getJSONArray(key);
        }
        if (obj == null) {
            throw new JSONException(key);
        }

        return obj;
    }

    public MediaImage parseMediaImage(final JSONObject data) throws JSONException {
        String aspect16x9 = parseString(data, "aspect16x9");
        String aspect4x3 = parseString(data, "aspect4x3");
        String aspect3x4 = parseString(data, "aspect3x4");
        String aspect1x1 = parseString(data, "aspect1x1");
        String aspect2x3 = parseString(data, "aspect2x3");

        return new MediaImage(aspect16x9, aspect3x4, aspect4x3, aspect1x1, aspect2x3);
    }

    public MediaAd parseMediaAd(final JSONObject data) throws JSONException {
        try {
            JSONObject ads = data.getJSONObject("ads");
            String provider = parseString(ads, "provider");
            String format = parseString(ads, "format");
            String url = parseString(ads, "url");
            return new MediaAds(provider, format, url);
        } catch (Exception e) {
            return new MediaAd();
        }
    }

    public MediaCollection parseMediaCollection(final JSONObject data) throws JSONException {
        JSONObject rawAttributes = data.getJSONObject("attributes");
        JSONObject images = rawAttributes.getJSONObject("images");

        String id = parseString(data, "id");
        String type = parseString(data, "type");

        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("title", parseString(rawAttributes, "title"));
        attributes.put("description", parseString(rawAttributes, "description"));
        attributes.put("releaseDate", parseString(rawAttributes, "releaseDate"));
        attributes.put("mediaImage", parseMediaImage(images));

        MediaCollection collection = new MediaCollection(id, type);
        collection.setAttributes(attributes);


        JSONObject relationships = data.getJSONObject("relationships");

        addRelationshipsToOddObject(relationships, collection);

        return collection;
    }

    protected Media parseMedia(final JSONObject dataObject) throws JSONException {
        JSONObject rawAttributes = dataObject.getJSONObject("attributes");
        JSONObject images = rawAttributes.getJSONObject("images");

        Media media = new Media(
                parseString(dataObject, "id"),
                parseString(dataObject, "type"));

        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("title", parseString(rawAttributes, "title"));
        attributes.put("description", parseString(rawAttributes, "description"));
        attributes.put("releaseDate", parseString(rawAttributes, "releaseDate"));
        try {
            attributes.put("duration", parseInt(rawAttributes, "duration"));
        } catch (Exception e) {
            Log.d(TAG, "Invalid duration: " + e.toString());
            attributes.put("duration", 0);
        }

        attributes.put("url", parseString(rawAttributes, "url"));
        attributes.put("mediaImage", parseMediaImage(images));
        attributes.put("mediaAd", parseMediaAd(rawAttributes));

        media.setAttributes(attributes);

        return media;
    }

    protected Promotion parsePromotion(final JSONObject rawPromotion) throws JSONException {
        JSONObject rawAttributes = rawPromotion.getJSONObject("attributes");
        JSONObject images = rawAttributes.getJSONObject("images");

        Promotion promotion = new Promotion(
                parseString(rawPromotion, "id"),
                parseString(rawPromotion, "type"));

        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("title", parseString(rawAttributes, "title"));
        attributes.put("description", parseString(rawAttributes, "description"));
        attributes.put("mediaImage", parseMediaImage(images));

        promotion.setAttributes(attributes);

        return promotion;
    }

    protected ArrayList<Media> parseMediaList(final String result) {
        ArrayList<Media> videos = new ArrayList<>();
        try {
            JSONObject videosObjectResponse = new JSONObject(result);
            JSONArray videosArray = videosObjectResponse.getJSONArray("data");
            for (int i = 0; i < videosArray.length(); i++) {
                Media video = parseMedia(videosArray.getJSONObject(i));
                videos.add(video);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return videos;
    }

    protected Config parseConfig(final String result) {
        try {
            JSONObject resultJSONObject = new JSONObject(result);
            JSONObject dataJSONObject = parseJSONObject(resultJSONObject, "data");
            JSONObject rawAttributes = parseJSONObject(dataJSONObject, "attributes");

            Config config = new Config(
                    parseString(dataJSONObject, "id"),
                    parseString(dataJSONObject, "type"));


            // TODO - figure out a better way of managing views and features @EJS
            HashMap<String, Object> views = new HashMap<>();
            JSONObject rawViews = parseJSONObject(rawAttributes, "views");
            Iterator<String> viewNames = rawViews.keys();
            while(viewNames.hasNext()) {
                String viewName = viewNames.next();
                views.put(viewName, parseString(rawViews, viewName));
            }

            HashMap<String, Object> features = new HashMap<>();
            JSONObject rawFeatures = parseJSONObject(rawAttributes, "features");
            Iterator<String> featureNames = rawFeatures.keys();
            while(featureNames.hasNext()) {
                String featureName = featureNames.next();
                features.put(featureName, parseJSONObject(rawFeatures, featureName));
            }

            HashMap<String, Object> attributes = new HashMap<>();
            attributes.put("views", views);
            attributes.put("features", features);
            config.setAttributes(attributes);

            return config;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected OddView parseView(final String result) {
        try {
            JSONObject resultObject = new JSONObject(result);
            JSONObject data = parseJSONObject(resultObject, "data");
            JSONObject rawAttributes = parseJSONObject(data, "attributes");
            OddView view = new OddView(
                    parseString(rawAttributes, "id"),
                    parseString(rawAttributes, "type"));

            HashMap<String, Object> attributes = new HashMap<>();
            attributes.put("title", parseString(rawAttributes, "title"));
            view.setAttributes(attributes);

            // use relationships to build view's ArrayList<Relationship>
            addRelationshipsToOddObject(data.getJSONObject("relationships"), view);

            // fill the view's included{Type} arrays with parsable objects
            JSONArray includedArray = parseJSONArray(resultObject, "included");
            for (int i = 0; i < includedArray.length(); i++) {
                JSONObject includedObject = includedArray.getJSONObject(i);
                String includedType = parseString(includedObject, "type");

                switch (includedType) {
                    case OddObject.TYPE_VIDEO_COLLECTION:
                        view.addIncluded(parseMediaCollection(includedObject));
                        break;
                    case OddObject.TYPE_LIVE_STREAM:
                    case OddObject.TYPE_VIDEO:
                        view.addIncluded(parseMedia(includedObject));
                        break;
                    case OddObject.TYPE_PROMOTION:
                        view.addIncluded(parsePromotion(includedObject));
                        break;
                }
            }

            // backfill newly created includedMediaCollections with newly created media
            view.fillIncludedMediaCollections();

            return view;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected AuthToken parseAuthToken(String responseBody) throws JSONException {
        JSONObject attributes = new JSONObject(responseBody).getJSONObject("data").getJSONObject("attributes");
        String accessToken = attributes.getString("access_token");
        String tokenType = attributes.getString("token_type");
        return new AuthToken(accessToken, tokenType);
    }

    protected DeviceCodeResponse parseDeviceCodeResponse(String responseBody) throws JSONException {
        JSONObject attributes = new JSONObject(responseBody).getJSONObject("data").getJSONObject("attributes");
        String deviceCode = attributes.getString("device_code");
        String userCode = attributes.getString("user_code");
        String verificationUrl = attributes.getString("verification_url");
        int expiresIn = attributes.getInt("expires_in");
        int interval = attributes.getInt("interval");
        return new DeviceCodeResponse(deviceCode, userCode, verificationUrl, expiresIn, interval);
    }

    protected ArrayList<OddObject> parseSearch(final String result) {
        ArrayList<OddObject> searchResult = new ArrayList<>();
        ArrayList<Media> searchMedias = new ArrayList<>();
        ArrayList<MediaCollection> searchCollections = new ArrayList<>();
        try {
            JSONObject resultObject = new JSONObject(result);
            JSONArray dataArray = resultObject.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject pokerObj = dataArray.getJSONObject(i);
                String type = parseString(pokerObj, "type");
                if (OddObject.TYPE_VIDEO.equals(type)) {
                    Media video = parseMedia(pokerObj);
                    if (video != null) {
                        searchMedias.add(video);
                    }
                } else if (OddObject.TYPE_VIDEO_COLLECTION.equals(type)) {
                    MediaCollection collection = parseMediaCollection(pokerObj);
                    if (collection != null) {
                        searchCollections.add(collection);
                    }
                }
            }
            if (!searchCollections.isEmpty()) {
                searchResult.addAll(searchCollections);
            }
            if (!searchMedias.isEmpty()) {
                searchResult.addAll(searchMedias);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return searchResult;
    }

    protected void addRelationshipsToOddObject(JSONObject relationships, OddObject relatable) {
        Iterator<String> relationshipNames = relationships.keys();
        try {
            while (relationshipNames.hasNext()) {
                String name = relationshipNames.next();
                JSONObject relationship = relationships.getJSONObject(name);
                if (relationship != null) {
                    ArrayList<Identifier> identifiers = new ArrayList<>();

                    if (relationship.get("data") instanceof JSONObject) {
                        //handle single relationship.data
                        JSONObject identifier = relationship.getJSONObject("data");
                        String relId = identifier.getString("id");
                        String relType = identifier.getString("type");
                        identifiers.add(new Identifier(relId, relType));

                    } else if (relationship.get("data") instanceof JSONArray) {
                        // handle multiple relationship.data
                        JSONArray rawIdentifiers = relationship.getJSONArray("data");

                        for (int i = 0; i < rawIdentifiers.length(); i++) {
                            JSONObject identifier = rawIdentifiers.getJSONObject(i);
                            String relId = identifier.getString("id");
                            String relType = identifier.getString("type");
                            identifiers.add(new Identifier(relId, relType));
                        }
                    }

                    relatable.addRelationship(new Relationship(name, identifiers));
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    protected String parseErrorMessage(String responseBody) throws JSONException {
        return new JSONObject(responseBody).getString("message");
    }
}
