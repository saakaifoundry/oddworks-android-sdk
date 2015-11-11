package io.oddworks.device.model;

/**
 * Objects that can be directly represented in a gui (except images) should generate this object.
 */
public class Presentable {
    private final String mTitle;
    private final String mDescription;
    private final MediaImage mMediaImage;

    protected Presentable(String title, String description, MediaImage mediaImage) {
        mTitle = title;
        mDescription = description;
        mMediaImage = mediaImage;
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

    @Override
    public String toString() {
        return "Presentable{" +
                "mTitle='" + mTitle + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mMediaImage=" + mMediaImage +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Presentable that = (Presentable) o;

        if (mTitle != null ? !mTitle.equals(that.mTitle) : that.mTitle != null) return false;
        if (mDescription != null ? !mDescription.equals(that.mDescription) : that.mDescription != null) return false;
        return !(mMediaImage != null ? !mMediaImage.equals(that.mMediaImage) : that.mMediaImage != null);

    }

    @Override
    public int hashCode() {
        int result = mTitle != null ? mTitle.hashCode() : 0;
        result = 31 * result + (mDescription != null ? mDescription.hashCode() : 0);
        result = 31 * result + (mMediaImage != null ? mMediaImage.hashCode() : 0);
        return result;
    }
}
