package io.oddworks.device.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Promotion implements Parcelable {
    public final static String TAG = "promotion";

    private String mId;
    private String mType;
    private String mTitle;
    private String mDescription;
    private MediaImage mMediaImage;

    public static final Creator<Promotion> CREATOR = new Creator<Promotion>() {

        @Override
        public Promotion createFromParcel(final Parcel source) {
            return new Promotion(source);
        }

        @Override
        public Promotion[] newArray(int size) {
            return new Promotion[size];
        }
    };

    public Promotion(final String id, final String type, final String title, final String description, final MediaImage mediaImage) {
        setId(id);
        setType(type);
        setTitle(title);
        setDescription(description);
        setMediaImage(mediaImage);
    }

    private Promotion(final Parcel parcel) {
        setId(parcel.readString());
        setType(parcel.readString());
        setTitle(parcel.readString());
        setDescription(parcel.readString());
        mMediaImage = parcel.readParcelable(MediaImage.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeString(getType());
        dest.writeString(getTitle());
        dest.writeString(getDescription());
        dest.writeParcelable(getMediaImage(), flags);
    }

    @Override
    public int describeContents() {
        return 0;
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
