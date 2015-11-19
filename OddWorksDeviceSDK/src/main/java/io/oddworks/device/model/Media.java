package io.oddworks.device.model;

import java.util.HashMap;

public class Media extends OddObject {
    public static final String TAG = Media.class.getSimpleName();
    private String mTitle;
    private String mDescription;
    private MediaImage mMediaImage;
    private String mReleaseDate;
    private MediaAd mMediaAd;
    private Integer mDuration;
    private String mUrl;

    public Media(Identifier identifier) {
        super(identifier);
    }

    public Media(String id, String type) {
        super(id, type);
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public MediaImage getMediaImage() {
        return mMediaImage;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public Integer getDuration() {
        if (mDuration == null) {
            mDuration = 0;
        }

        return mDuration;
    }

    public MediaAd getMediaAd() {
        return mMediaAd;
    }

    public Boolean isLive() {
        return getType().equals(OddObject.TYPE_LIVE_STREAM);
    }

    @Override
    public void setAttributes(HashMap<String, Object> attributes) {
        mTitle = (String) attributes.get("title");
        mDescription = (String) attributes.get("description");
        mMediaImage = (MediaImage) attributes.get("mediaImage");
        mReleaseDate = (String) attributes.get("releaseDate");
        mMediaAd = (MediaAd) attributes.get("mediaAd");
        mDuration = (int) attributes.get("duration");
        mUrl = (String) attributes.get("url");
    }

    @Override
    public HashMap<String, Object> getAttributes() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("title", getTitle());
        attributes.put("description", getDescription());
        attributes.put("mediaImage", getMediaImage());
        attributes.put("mediaAd", getMediaAd());
        attributes.put("releaseDate", getReleaseDate());
        attributes.put("duration", getDuration());
        attributes.put("url", getUrl());
        return attributes;
    }

    @Override
    public boolean isPresentable() {
        return true;
    }

    @Override
    public Presentable toPresentable() {
        return new Presentable(mTitle, mDescription, mMediaImage);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id='" + getId() + "', " +
                "type='" + getType() + "', " +
                "title='" + getTitle() + "')";
    }

}
