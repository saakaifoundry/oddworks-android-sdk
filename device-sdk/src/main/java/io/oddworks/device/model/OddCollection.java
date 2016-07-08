package io.oddworks.device.model;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.oddworks.device.Util;

import static io.oddworks.device.Util.getString;

public class OddCollection extends OddObject {
    private static final String TAG = OddCollection.class.getSimpleName();
    public static final String ENTITIES = "entities";

    private String title;
    private String subtitle;
    private String description;
    private List<MediaImage> images;
    private DateTime releaseDate;

    public OddCollection(final Identifier identifier) {
        super(identifier);
    }

    public OddCollection(final String id, final String type) {
        super(id, type);
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public List<MediaImage> getImages() {
        return images;
    }

    public DateTime getReleaseDate() {
        return releaseDate;
    }

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        this.title = getString(attributes, "title", null);
        this.subtitle = getString(attributes, "subtitle", null);
        this.description = getString(attributes, "description", null);
        this.images = (List<MediaImage>) attributes.get("images");
        this.releaseDate = Util.getDateTime(attributes, "releaseDate", null);
    }

    @Override
    public Map<String, Object> getAttributes() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("title", getTitle());
        attributes.put("subtitle", getSubtitle());
        attributes.put("description", getDescription());
        attributes.put("images", getImages());
        attributes.put("releaseDate", getReleaseDate());
        return attributes;
    }

    @Override
    public String toString() {
        return "OddCollection{" +
                "title='" + getTitle() + '\'' +
                ", subtitle='" + getSubtitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", images=" + getImages() +
                ", releaseDate=" + getReleaseDate() +
                '}';
    }
}
