package io.oddworks.device.model;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

public class Event extends OddObject {
    public static final String TAG = Event.class.getSimpleName();
    private String mTitle;
    private String mDescription;
    private MediaImage mMediaImage;
    private String mUrl;
    private String mCategory;
    private String mSource;
    private DateTime mCreatedAt;
    private DateTime mDateTimeStart;
    private DateTime mDateTimeEnd;
    private String mLocation;

    public Event(Identifier identifier) {
        super(identifier);
    }

    public Event(String id, String type) {
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

    public DateTime getDateTimeStart() {
        return mDateTimeStart;
    }

    public DateTime getDateTimeEnd() {
        return mDateTimeEnd;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setCreatedAt(String date) {
        mCreatedAt = new DateTime(date);
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
        mTitle = (String) attributes.get("title");
        mDescription = (String) attributes.get("description");
        mMediaImage = (MediaImage) attributes.get("mediaImage");
        mUrl = (String) attributes.get("url");
        mCategory = (String) attributes.get("category");
        mSource = (String) attributes.get("source");
        mCreatedAt = (DateTime) attributes.get("createdAt");
        mDateTimeStart = (DateTime) attributes.get("dateTimeStart");
        mDateTimeEnd = (DateTime) attributes.get("dateTimeEnd");
        mLocation = (String) attributes.get("location");
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
        return new Presentable(mTitle, mDescription, mMediaImage);
    }

    @Override
    public String toString() {
        return "Event{" +
                "mTitle='" + mTitle + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mMediaImage=" + mMediaImage +
                ", mUrl='" + mUrl + '\'' +
                ", mCategory='" + mCategory + '\'' +
                ", mSource='" + mSource + '\'' +
                ", mCreatedAt=" + mCreatedAt +
                ", mDateTimeStart=" + mDateTimeStart +
                ", mDateTimeEnd=" + mDateTimeEnd +
                ", mLocation='" + mLocation + '\'' +
                '}';
    }
}
