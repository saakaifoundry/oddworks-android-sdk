package io.oddworks.device.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Media implements Parcelable {
    public final static String TAG = "Media";
    public final static String LIVE_STREAM = "liveStream";
    public final static String VIDEO = "video";
    private String mId;
    private String mType;
    private String mTitle;
    private String mDescription;
    private MediaImage mMediaImage;
    private MediaAds mMediaAds;
    private String mReleaseDate;
    private Integer mDuration;
    private String mUrl;

    public static final Creator<Media> CREATOR = new Creator<Media>() {

        @Override
        public Media createFromParcel(final Parcel source) {
            return new Media(source);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    public Media(final String id, final String type) {
        setId(id);
        setType(type);
    }

    public Media(final String id, final String type, final String title,
            final String description, final MediaImage mediaImage, final MediaAds mediaAds, final String releaseDate, final int duration, final String url) {
        setId(id);
        setType(type);
        setTitle(title);
        setDescription(description);
        setMediaImage(mediaImage);
        setMediaAds(mediaAds);
        setReleaseDate(releaseDate);
        setDuration(duration);
        setUrl(url);
    }

    public Media(Parcel parcel) {
        setId(parcel.readString());
        setType(parcel.readString());
        setTitle(parcel.readString());
        setDescription(parcel.readString());
        mMediaImage = parcel.readParcelable(MediaImage.class.getClassLoader());
        mMediaAds = parcel.readParcelable(MediaAds.class.getClassLoader());
        setReleaseDate(parcel.readString());
        setDuration(parcel.readInt());
        setUrl(parcel.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeString(getType());
        dest.writeString(getTitle());
        dest.writeString(getDescription());
        dest.writeParcelable(getMediaImage(), flags);
        dest.writeParcelable(getMediaAds(), flags);
        dest.writeString(getReleaseDate());
        dest.writeInt(getDuration());
        dest.writeString(getUrl());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return TAG + "{" +
                "mId='" + mId + "', " +
                "mTitle='" + mTitle + "'}";
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
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

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public Integer getDuration() {
        return mDuration;
    }

    public void setDuration(Integer duration) {
        mDuration = duration;
    }

    public MediaAds getMediaAds() {
        return mMediaAds;
    }

    public void setMediaAds(MediaAds mediaAds) {
        mMediaAds = mediaAds;
    }

    public Boolean isLive() {
        return getType().equals(LIVE_STREAM);
    }

    public void fillData(final Media media) {
        setId(media.getId());
        setType(media.getType());
        setTitle(media.getTitle());
        setDescription(media.getDescription());
        setMediaImage(media.getMediaImage());
        setMediaAds(media.getMediaAds());
        setReleaseDate(media.getReleaseDate());
        setDuration(media.getDuration());
        setUrl(media.getUrl());
    }
}
