package io.oddworks.device.model;

import java.util.HashMap;
import java.util.Map;

import io.oddworks.device.Util;

public class External extends OddObject {
    public static final String TAG = External.class.getSimpleName();
    private String title;
    private String description;
    private MediaImage mediaImage;
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

    public MediaImage getMediaImage() {
        return mediaImage;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        title = Util.getString(attributes, "title", null);
        description = Util.getString(attributes, "description", null);
        mediaImage = (MediaImage) attributes.get("mediaImage");
        url = Util.getString(attributes, "url", null);
    }

    @Override
    public Map<String, Object> getAttributes() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("title", getTitle());
        attributes.put("description", getDescription());
        attributes.put("mediaImage", getMediaImage());
        attributes.put("url", getUrl());
        return attributes;
    }

    @Override
    public boolean isPresentable() {
        return true;
    }

    @Override
    public Presentable toPresentable() {
        return new Presentable(title, description, mediaImage);
    }

    @Override
    public String toString() {
        return "External{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", mediaImage=" + mediaImage +
                ", url='" + url + '\'' +
                '}';
    }
}
