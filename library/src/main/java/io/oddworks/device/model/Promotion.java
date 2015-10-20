package io.oddworks.device.model;

public class Promotion {
    public final static String TAG = "promotion";

    private String mId;
    private String mType;
    private String mTitle;
    private String mDescription;
    private MediaImage mMediaImage;

    public Promotion(final String id, final String type, final String title, final String description, final MediaImage mediaImage) {
        setId(id);
        setType(type);
        setTitle(title);
        setDescription(description);
        setMediaImage(mediaImage);
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public MediaImage getMediaImage() {
        return mMediaImage;
    }

    public void setMediaImage(MediaImage mediaImage) {
        mMediaImage = mediaImage;
    }
}
