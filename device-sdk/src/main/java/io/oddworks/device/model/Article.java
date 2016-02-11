package io.oddworks.device.model;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;


public class Article extends OddObject {
    public static final String TAG = Article.class.getSimpleName();
    private String mTitle;
    private String mDescription;
    private MediaImage mMediaImage;
    private String mUrl;
    private String mCategory;
    private String mSource;
    private DateTime mCreatedAt;

    public Article(Identifier identifier) {
        super(identifier);
    }

    public Article(String id, String type) {
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

    public String getUrl() {
        return mUrl;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getSource() {
        return mSource;
    }

    public DateTime getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String date) {
        mCreatedAt = new DateTime(date);
    }

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        mTitle = (String) attributes.get("title");
        mDescription = (String) attributes.get("description");
        mMediaImage = (MediaImage) attributes.get("mediaImage");
        mUrl = (String) attributes.get("url");
        mCategory = (String) attributes.get("category");
        mSource = (String) attributes.get("source");
        mCreatedAt = (DateTime) attributes.get("createdAt");
    }

    @Override
    public Map<String, Object> getAttributes() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("title", getTitle());
        attributes.put("description", getDescription());
        attributes.put("mediaImage", getMediaImage());
        attributes.put("url", getUrl());
        attributes.put("category", getCategory());
        attributes.put("source", getSource());
        attributes.put("createdAt", getCreatedAt());
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
