package io.oddworks.device.model;

import java.util.HashMap;

/**
 * Created by brkattk on 9/21/15.
 */
public class MediaCollection extends OddObject {
    public static final String TAG = MediaCollection.class.getSimpleName();

    private String mTitle;
    private String mDescription;
    private MediaImage mMediaImage;
    private String mReleaseDate;

    public MediaCollection(final Identifier identifier) {
        super(identifier);
    }

    public MediaCollection(final String id, final String type) {
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

    public String getReleaseDate() {
        return mReleaseDate;
    }

    @Override
    public void setAttributes(HashMap<String, Object> attributes) {
        mTitle = (String) attributes.get("title");
        mDescription = (String) attributes.get("description");
        mMediaImage = (MediaImage) attributes.get("mediaImage");
        mReleaseDate = (String) attributes.get("releaseDate");
    }

    @Override
    public HashMap<String, Object> getAttributes() {
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
        return TAG + "(" +
                "id='" + getId() + "', " +
                "type='" + getType() + "', " +
                "title='" + getTitle() + "')";
    }
}
