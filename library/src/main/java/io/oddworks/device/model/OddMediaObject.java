package io.oddworks.device.model;

/**
 * Created by brkattk on 10/16/15.
 */
abstract public class OddMediaObject extends OddObject {
    private static final String TAG = OddMediaObject.class.getSimpleName();

    protected String mTitle;
    protected String mDescription;
    protected MediaImage mMediaImage;
    protected String mReleaseDate;

    public OddMediaObject(Identifier identifier) {
        super(identifier);
    }

    public OddMediaObject(String id, String type) {
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
    public String toString() {
        return OddMediaObject.class.getSimpleName() + "(" +
                "id='" + getId() + "', " +
                "type='" + getType() + "', " +
                "title='" + getTitle() + "')";
    }
}
