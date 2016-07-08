package io.oddworks.device.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.oddworks.device.Util;

public class External extends OddObject {
    public static final String TAG = External.class.getSimpleName();
    private String title;
    private String description;
    private List<MediaImage> images;
    private String url;

    public External(Identifier identifier) {
        super(identifier);
    }

    public External(String id, String type) {
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
        title = Util.getString(attributes, "title", null);
        description = Util.getString(attributes, "description", null);
        images = (List<MediaImage>) attributes.get("images");
        url = Util.getString(attributes, "url", null);
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
        return "External{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", images=" + images +
                ", url='" + url + '\'' +
                '}';
    }
}
