package io.oddworks.device.model;

import java.util.HashMap;
import java.util.Map;

import io.oddworks.device.Util;

public class Promotion extends OddObject {
    public static final String TAG = Promotion.class.getSimpleName();

    private String title;
    private String description;
    private MediaImage mediaImage;
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

    public MediaImage getMediaImage() {
        return mediaImage;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        this.title = Util.getString(attributes, "title", null);
        this.description = Util.getString(attributes, "description", null);
        this.mediaImage = (MediaImage) attributes.get("mediaImage");
        this.url = Util.getString(attributes, "url", null);
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
        return TAG + "(" +
                "id='" + getId() + "', " +
                "type='" + getType() + "', " +
                "title='" + getTitle() + "', " +
                "description='" + getDescription() + "', " +
                "mediaImage='" + getMediaImage() + "', " +
                "url='" + getUrl() + "')";
    }
}
