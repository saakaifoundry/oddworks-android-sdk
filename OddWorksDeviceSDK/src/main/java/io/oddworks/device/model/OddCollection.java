package io.oddworks.device.model;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

public class OddCollection extends OddObject {
    private static final String TAG = OddCollection.class.getSimpleName();
    public static final String ENTITIES = "entities";

    private String mTitle;
    private String mDescription;
    private MediaImage mMediaImage;
    private DateTime mReleaseDate;

    public OddCollection(final Identifier identifier) {
        super(identifier);
    }

    public OddCollection(final String id, final String type) {
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

    public DateTime getReleaseDate() {
        return mReleaseDate;
    }

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        mTitle = (String) attributes.get("title");
        mDescription = (String) attributes.get("description");
        mMediaImage = (MediaImage) attributes.get("mediaImage");
        mReleaseDate = (DateTime) attributes.get("releaseDate");
    }

    @Override
    public Map<String, Object> getAttributes() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("title", getTitle());
        attributes.put("description", getDescription());
        attributes.put("mediaImage", getMediaImage());
        attributes.put("releaseDate", getReleaseDate());
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
        return "OddCollection{" +
                "mTitle='" + mTitle + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mMediaImage=" + mMediaImage +
                ", mReleaseDate=" + mReleaseDate +
                '}';
    }
}
