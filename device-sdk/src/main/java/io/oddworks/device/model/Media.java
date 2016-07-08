package io.oddworks.device.model;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.oddworks.device.model.players.Player;

import static io.oddworks.device.Util.getBoolean;
import static io.oddworks.device.Util.getDateTime;
import static io.oddworks.device.Util.getInteger;
import static io.oddworks.device.Util.getString;

public class Media extends OddObject {
    public static final String TAG = Media.class.getSimpleName();
    private String title;
    private String description;
    private List<MediaImage> images;
    private DateTime releaseDate;
    private Integer duration;
    private String url;
    private boolean currentlyLive;

    public Media(Identifier identifier) {
        super(identifier);
    }

    public Media(String id, String type) {
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

    public DateTime getReleaseDate() {
        return releaseDate;
    }

    public String getUrl() {
        return url;
    }

    public int getDuration() {
        if (duration == null) {
            duration = 0;
        }

        return duration;
    }


    /** returns true if this Media is a liveStream object in the api's catalog */
    public Boolean isLive() {
        return getType().equals(OddObject.TYPE_LIVE_STREAM);
    }

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        this.title = getString(attributes, "title", null);
        this.description = getString(attributes, "description", null);
        this.images = (List<MediaImage>) attributes.get("images");
        this.releaseDate = getDateTime(attributes, "releaseDate", null);
        this.duration = getInteger(attributes, "duration", null);
        this.url = getString(attributes, "url", null);
        this.currentlyLive = getBoolean(attributes, "isLive");
    }

    @Override
    public Map<String, Object> getAttributes() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("title", getTitle());
        attributes.put("description", getDescription());
        attributes.put("images", getImages());
        attributes.put("releaseDate", getReleaseDate());
        attributes.put("duration", getDuration());
        attributes.put("url", getUrl());
        attributes.put("isLive", isCurrentlyLive());
        return attributes;
    }

    /** returns true if this Media stream is broadcasting now. */
    public boolean isCurrentlyLive() {
        return this.currentlyLive;
    }


    @Override
    public String toString() {
        return "Media{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", images=" + images +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                ", url='" + url + '\'' +
                '}';
    }
}
