package io.oddworks.device.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brkattk on 10/16/15.
 */
abstract public class OddMedia implements Parcelable {
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeString(getType());
        dest.writeString(getTitle());
        dest.writeString(getDescription());
        dest.writeParcelable(getMediaImage(), flags);
        dest.writeString(getReleaseDate());
    }

    protected OddMedia(Parcel in) {
        mId = in.readString();
        mType = in.readString();
        mTitle = in.readString();
        mDescription = in.readString();
        mMediaImage = in.readParcelable(MediaImage.class.getClassLoader());
        mReleaseDate = in.readString();
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
