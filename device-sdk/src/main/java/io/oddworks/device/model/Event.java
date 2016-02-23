package io.oddworks.device.model;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

import io.oddworks.device.Util;

public class Event extends OddObject {
    public static final String TAG = Event.class.getSimpleName();
    private String title;
    private String description;
    private MediaImage mediaImage;
    private String url;
    private String category;
    private String source;
    private DateTime createdAt;
    private DateTime dateTimeStart;
    private DateTime dateTimeEnd;
    private String location;

    public Event(Identifier identifier) {
        super(identifier);
    }

    public Event(String id, String type) {
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

    public String getCategory() {
        return category;
    }

    public String getSource() {
        return source;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public DateTime getDateTimeStart() {
        return dateTimeStart;
    }

    public DateTime getDateTimeEnd() {
        return dateTimeEnd;
    }

    public String getLocation() {
        return location;
    }

    public void setCreatedAt(String date) {
        createdAt = new DateTime(date);
    }

    public boolean isMultiDayEvent() {
        return hasStartAndEndDateTime() &&
               getDateTimeStart().getDayOfYear() != getDateTimeEnd().getDayOfYear();
    }

    public boolean hasStartAndEndDateTime() {
        return getDateTimeStart() != null && getDateTimeEnd() != null;
    }

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        title = Util.getString(attributes, "title", null);
        description = Util.getString(attributes, "description", null);
        mediaImage = (MediaImage) attributes.get("mediaImage");
        url = Util.getString(attributes, "url", null);
        category = Util.getString(attributes, "category", null);
        source = Util.getString(attributes, "source", null);
        createdAt = Util.getDateTime(attributes, "createdAt", null);
        dateTimeStart = Util.getDateTime(attributes, "dateTimeStart", null);
        dateTimeEnd = Util.getDateTime(attributes, "dateTimeEnd", null);
        location = Util.getString(attributes, "location", null);
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
        attributes.put("dateTimeStart", getDateTimeStart());
        attributes.put("dateTimeEnd", getDateTimeEnd());
        attributes.put("location", getLocation());
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
        return "Event{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", mediaImage=" + mediaImage +
                ", url='" + url + '\'' +
                ", category='" + category + '\'' +
                ", source='" + source + '\'' +
                ", createdAt=" + createdAt +
                ", dateTimeStart=" + dateTimeStart +
                ", dateTimeEnd=" + dateTimeEnd +
                ", location='" + location + '\'' +
                '}';
    }
}
