package io.oddworks.device.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by brkattk on 9/21/15.
 */
public class MediaCollection implements Parcelable {
    public static final String TAG = "MediaCollection";
    public static final String VIDEO_COLLECTION = "videoCollection";
    private ArrayList<Media> mMedia;
    private String mId;
    private String mType;
    private String mTitle;
    private String mDescription;
    private MediaImage mMediaImage;
    private String mReleaseDate;

    public static final Parcelable.Creator<MediaCollection> CREATOR = new Parcelable.Creator<MediaCollection>() {

        @Override
        public MediaCollection createFromParcel(final Parcel source) {
            return new MediaCollection(source);
        }

        @Override
        public MediaCollection[] newArray(int size) {
            return new MediaCollection[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeString(getType());
        dest.writeString(getTitle());
        dest.writeString(getDescription());
        dest.writeParcelable(getMediaImage(), flags);
        dest.writeString(getReleaseDate());
        dest.writeTypedList(getMedia());
    }

    public MediaCollection(final String id, final String type) {
      setId(id);
      setType(type);
    }

    public MediaCollection(final String id, final String type, final String title, final String description,
                           final MediaImage mediaImage, final String releaseDate) {
        setId(id);
        setType(type);
        setTitle(title);
        setDescription(description);
        setMediaImage(mediaImage);
        setReleaseDate(releaseDate);
    }

    public MediaCollection(Parcel source) {
        setId(source.readString());
        setType(source.readString());
        setTitle(source.readString());
        setDescription(source.readString());
        mMediaImage = source.readParcelable(MediaImage.class.getClassLoader());
        setReleaseDate(source.readString());
        mMedia = new ArrayList<>();
        source.readTypedList(mMedia, Media.CREATOR);
    }

    public void addMedia(final Media media) {
        if (mMedia== null) {
            mMedia = new ArrayList<>();
        }
        mMedia.add(media);
    }

    public void setMedia(final ArrayList<Media> media) {
        if (mMedia == null) {
            mMedia = new ArrayList<>();
        }
        mMedia.clear();
        mMedia.addAll(media);
    }

    public ArrayList<Media> getMedia() {
        return mMedia;
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

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }
}
