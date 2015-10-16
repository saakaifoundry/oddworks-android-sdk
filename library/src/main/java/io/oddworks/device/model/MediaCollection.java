package io.oddworks.device.model;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by brkattk on 9/21/15.
 */
public class MediaCollection extends OddMedia {
    private static final String TAG = MediaCollection.class.getSimpleName();
    public static final String VIDEO_COLLECTION = "videoCollection";
    private ArrayList<Media> mMedia;
    private String mReleaseDate;

    public static final Creator<MediaCollection> CREATOR = new Creator<MediaCollection>() {

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
        super.writeToParcel(dest, flags);
        dest.writeTypedList(getMedia());
    }

    public MediaCollection(final String id, final String type) {
      super(id, type);
    }

    public MediaCollection(final String id, final String type, final String title, final String description,
                           final MediaImage mediaImage, final String releaseDate) {
        super(id, type, title, description, mediaImage, releaseDate);
    }

    public MediaCollection(Parcel source) {
        super(source);
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

    public void updateMedia(final Media media) {
        if (mMedia != null) {
            for (int i = 0; i < mMedia.size(); i++) {
                if (mMedia.get(i).getId().equals(media.getId())) {
                    mMedia.set(i, media);
                }
            }
        }
    }

    public boolean isMediaEmpty() {
        for (Media media : mMedia) {
            if ((media.getTitle() == null || media.getTitle().isEmpty()) || (media.getUrl() ==
                    null || media.getUrl().isEmpty())) {
                return true;
            }
        }
        return false;
    }
}
