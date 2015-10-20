package io.oddworks.device.request;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.oddworks.device.model.AuthToken;
import io.oddworks.device.model.Config;
import io.oddworks.device.model.DeviceCodeResponse;
import io.oddworks.device.model.Media;
import io.oddworks.device.model.MediaAds;
import io.oddworks.device.model.MediaCollection;
import io.oddworks.device.model.MediaImage;
import io.oddworks.device.model.OddMedia;
import io.oddworks.device.model.Promotion;
import io.oddworks.device.model.View;
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

    public MediaAds parseMediaAds(final JSONObject data) throws JSONException {
        try {
            JSONObject ads = data.getJSONObject("ads");
            String provider = parseString(ads, "provider");
            String format = parseString(ads, "format");
            String url = parseString(ads, "url");
            return new MediaAds(provider, format, url);
        } catch (Exception e) {
            return new MediaAds();
        }
    }

    public MediaCollection parseMediaCollection(final JSONObject data) throws JSONException {
        JSONObject attributes = data.getJSONObject("attributes");
        JSONObject images = attributes.getJSONObject("images");

        String id = parseString(data, "id");
        String type = parseString(data, "type");
        String title = parseString(attributes, "title");
        String description = parseString(attributes, "description");
        String releaseDate = parseString(attributes, "releaseDate");

        MediaImage mediaImage = parseMediaImage(images);

        MediaCollection collection = new MediaCollection(id, type, title, description, mediaImage, releaseDate);

        JSONObject relationships = data.getJSONObject("relationships");
        JSONObject videoRelationship = relationships.getJSONObject("videos");
        JSONArray videoIdentifiers = videoRelationship.getJSONArray("data");
        for (int i = 0; i < videoIdentifiers.length(); i++) {
            JSONObject dataRelationObject = videoIdentifiers.getJSONObject(i);
            String mediaType = parseString(dataRelationObject, "type");
            if (Media.VIDEO.equals(mediaType) || Media.LIVE_STREAM.equals(mediaType)) {
                String mediaId = parseString(dataRelationObject, "id");
                Media media = new Media(mediaId, mediaType);
                collection.addMedia(media);
            }
        }

        return collection;
    }

    protected Media parseMedia(final JSONObject dataObject) throws JSONException {
        JSONObject attributes = dataObject.getJSONObject("attributes");
        JSONObject images = attributes.getJSONObject("images");

        String id = parseString(dataObject, "id");
        String type = parseString(dataObject, "type");
        String title = parseString(attributes, "title");
        String description = parseString(attributes, "description");
        String releaseDate = parseString(attributes, "releaseDate");
        int duration = 0;
        try {
            duration = parseInt(attributes, "duration");
        } catch (Exception e) {
            Log.d(TAG, "Invalid duration: " + e.toString());
        }

        String url = parseString(attributes, "url");

        MediaImage mediaImage = parseMediaImage(images);
        MediaAds mediaAds = parseMediaAds(attributes);

        return new Media(id, type, title, description, mediaImage, mediaAds, releaseDate, duration, url);
    }

    protected Promotion parsePromotion(final JSONObject promotion) throws JSONException {
        JSONObject attributes = promotion.getJSONObject("attributes");
        JSONObject images = attributes.getJSONObject("images");

        String id = parseString(promotion, "id");
        String type = parseString(promotion, "type");
        String title = parseString(attributes, "title");
        String description = parseString(attributes, "description");

        MediaImage mediaImage = parseMediaImage(images);

        return new Promotion(id, type, title, description, mediaImage);
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
        String viewId = "";
        try {
            JSONObject resultJSONObject = new JSONObject(result);
            JSONObject dataJSONObject = parseJSONObject(resultJSONObject, "data");
            JSONObject attributesJSONObject = parseJSONObject(dataJSONObject, "attributes");
            viewId = parseString(attributesJSONObject, "view");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new Config(viewId);
    }

    protected View parseView(final String result) {
        View view = null;
        try {
            JSONObject resultObject = new JSONObject(result);
            JSONObject dataObject = parseJSONObject(resultObject, "data");
            JSONObject attributesObject = parseJSONObject(dataObject, "attributes");
            String title = parseString(attributesObject, "title");
            view = new View(title);

            JSONObject relationshipsObject = dataObject.getJSONObject("relationships");

            JSONObject featuredObject = relationshipsObject.getJSONObject("featured");
            JSONArray featuredDataArray = featuredObject.getJSONArray("data");
            for (int i = 0; i < featuredDataArray.length(); i++) {
                JSONObject featuredDataObject = featuredDataArray.getJSONObject(i);
                String featuredId = parseString(featuredDataObject, "id");
                String featuredType = parseString(featuredDataObject, "type");
                if (MediaCollection.VIDEO_COLLECTION.equals(featuredType)) {
                    MediaCollection collection = new MediaCollection(featuredId, featuredType);
                    view.addFeaturedMediaCollection(collection);
                }
            }

            JSONObject showsObject = relationshipsObject.getJSONObject("shows");
            JSONArray showsDataArray = showsObject.getJSONArray("data");
            for (int i = 0; i < showsDataArray.length(); i++) {
                JSONObject showsDataObject = showsDataArray.getJSONObject(i);
                String showId = parseString(showsDataObject, "id");
                String showType = parseString(showsDataObject, "type");
                if (MediaCollection.VIDEO_COLLECTION.equals(showType)) {
                    MediaCollection show = new MediaCollection(showId, showType);
                    view.addShow(show);
                }
            }

            JSONObject featuredMediaObject = relationshipsObject.getJSONObject("featuredMedia");
            JSONArray featuredMediaDataArray = featuredMediaObject.getJSONArray("data");
            for (int i = 0; i < featuredMediaDataArray.length(); i++) {
                JSONObject featuredMediaDataObject = featuredMediaDataArray.getJSONObject(i);
                String featuredId = parseString(featuredMediaDataObject, "id");
                String featuredType = parseString(featuredMediaDataObject, "type");
                if (Media.VIDEO.equals(featuredType) || Media.LIVE_STREAM.equals(featuredType)) {
                    Media video = new Media(featuredId, featuredType);
                    view.addFeaturedMedia(video);
                }
            }

            JSONArray includedArray = parseJSONArray(resultObject, "included");
            for (int i = 0; i < includedArray.length(); i++) {
                JSONObject includedObject = includedArray.getJSONObject(i);
                String includedType = parseString(includedObject, "type");
                if (MediaCollection.VIDEO_COLLECTION.equals(includedType)) {
                    MediaCollection videoCol = parseMediaCollection(includedObject);
                    view.fillFeaturedMediaCollection(videoCol);
                    view.fillShow(videoCol);
                } else if (Promotion.TAG.equals(includedType)) {
                    Promotion promotion = parsePromotion(includedObject);
                    view.setPromotion(promotion);
                }
            }

            for (int i = 0; i < includedArray.length(); i++) {
                JSONObject includedObject = includedArray.getJSONObject(i);
                String includedType = parseString(includedObject, "type");
                if (Media.VIDEO.equals(includedType) || Media.LIVE_STREAM.equals(includedType)) {
                    Media media = parseMedia(includedObject);
                    view.fillMedia(media);
                    view.fillFeaturedMedia(media);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
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

    protected ArrayList<OddMedia> parseSearch(final String result) {
        ArrayList<OddMedia> searchResult = new ArrayList<>();
        ArrayList<Media> searchMedias = new ArrayList<>();
        ArrayList<MediaCollection> searchCollections = new ArrayList<>();
        try {
            JSONObject resultObject = new JSONObject(result);
            JSONArray dataArray = resultObject.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject pokerObj = dataArray.getJSONObject(i);
                String type = parseString(pokerObj, "type");
                if (Media.VIDEO.equals(type)) {
                    Media video = parseMedia(pokerObj);
                    if (video != null) {
                        searchMedias.add(video);
                    }
                } else if (MediaCollection.VIDEO_COLLECTION.equals(type)) {
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
}
