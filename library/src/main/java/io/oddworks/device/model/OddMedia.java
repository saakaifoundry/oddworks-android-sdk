package io.oddworks.device.model;

/**
 * Created by brkattk on 10/16/15.
 */
abstract public class OddMedia {
    private static final String TAG = OddMedia.class.getSimpleName();
    protected String mId;
    protected String mType;
    protected String mTitle;
    protected String mDescription;
    protected MediaImage mMediaImage;
    protected String mReleaseDate;

    protected OddMedia(final String id, final String type) {
        mId = id;
        mType = type;
    }

    protected OddMedia(final String id, final String type, final String title, final String description,
                           final MediaImage mediaImage, final String releaseDate) {
        mId = id;
        mType = type;
        mTitle = title;
        mDescription = description;
        mMediaImage = mediaImage;
        mReleaseDate = releaseDate;
    }

    public String getId() {
        return mId;
    }

    public String getType() {
        return mType;
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
}
