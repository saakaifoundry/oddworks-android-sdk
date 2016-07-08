package io.oddworks.device.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.oddworks.device.Util;

public class Promotion extends OddObject {
    public static final String TAG = Promotion.class.getSimpleName();

    private String title;
    private String description;
    private List<MediaImage> images;
    private String url;

    public Promotion(Identifier identifier) {
        super(identifier);
    }

    public Promotion(String id, String type) {
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

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        this.title = Util.getString(attributes, "title", null);
        this.description = Util.getString(attributes, "description", null);
        this.images = (List<MediaImage>) attributes.get("images");
        this.url = Util.getString(attributes, "url", null);
    }

    @Override
    public Map<String, Object> getAttributes() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("title", getTitle());
        attributes.put("description", getDescription());
        attributes.put("images", getImages());
        attributes.put("url", getUrl());

        return attributes;
    }

    @Override
    public String toString() {
        return TAG + "(" +
                "id='" + getId() + "', " +
                "type='" + getType() + "', " +
                "title='" + getTitle() + "', " +
                "description='" + getDescription() + "', " +
                "images='" + getImages() + "', " +
                "url='" + getUrl() + "')";
    }
}
