package io.oddworks.device.model;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.oddworks.device.Util;


public class Article extends OddObject {
    public static final String TAG = Article.class.getSimpleName();
    private String title;
    private String description;
    private List<MediaImage> images;
    private String url;
    private String category;
    private String source;
    private DateTime createdAt;

    public Article(Identifier identifier) {
        super(identifier);
    }

    public Article(String id, String type) {
        super(id, type);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<MediaImage> getImages() {
        return images;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    public String getSource() {
        return source;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String date) {
        createdAt = new DateTime(date);
    }

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        title = Util.getString(attributes, "title", null);
        description = Util.getString(attributes, "description", null);
        images = (List<MediaImage>) attributes.get("images");
        url = Util.getString(attributes, "url", null);
        category = Util.getString(attributes, "category", null);
        source = Util.getString(attributes, "source", null);
        createdAt = Util.getDateTime(attributes, "createdAt", null);
    }

    @Override
    public Map<String, Object> getAttributes() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("title", getTitle());
        attributes.put("description", getDescription());
        attributes.put("images", getImages());
        attributes.put("url", getUrl());
        attributes.put("category", getCategory());
        attributes.put("source", getSource());
        attributes.put("createdAt", getCreatedAt());
        return attributes;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id='" + getId() + "', " +
                "type='" + getType() + "', " +
                "title='" + getTitle() + "')";
    }
}
