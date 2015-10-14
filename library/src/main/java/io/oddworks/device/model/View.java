package io.oddworks.device.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class View implements Parcelable {

    public static final Creator<View> CREATOR = new Creator<View>() {

        @Override
        public View createFromParcel(final Parcel source) {
            return new View(source);
        }

        @Override
        public View[] newArray(int size) {
            return new View[size];
        }
    };

    private String mTitle;
    private ArrayList<MediaCollection> mFeaturedVideoCollections;
    private ArrayList<MediaCollection> mShows;
    private ArrayList<Media> mFeaturedMedia;
    private Promotion mPromotion;

    public View(final String title) {
        mTitle = title;
        mFeaturedVideoCollections = new ArrayList<>();
        mShows = new ArrayList<>();
        mFeaturedMedia = new ArrayList<>();
    }

    private View(final Parcel parcel) {
        mTitle = parcel.readString();
        mFeaturedVideoCollections = new ArrayList<>();
        mShows = new ArrayList<>();
        mFeaturedMedia = new ArrayList<>();

        parcel.readTypedList(mFeaturedVideoCollections, MediaCollection.CREATOR);
        parcel.readTypedList(mShows, MediaCollection.CREATOR);
        parcel.readTypedList(mFeaturedMedia, Media.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeTypedList(mFeaturedVideoCollections);
        dest.writeTypedList(mShows);
        dest.writeTypedList(mFeaturedMedia);
    }

    public String getTitle() {
        return mTitle;
    }

    public ArrayList<MediaCollection> getFeaturedVideoCollections() {
        return mFeaturedVideoCollections;
    }

    public void addFeaturedMediaCollection(final MediaCollection mediaCollection) {
        if (mFeaturedVideoCollections == null) {
            mFeaturedVideoCollections = new ArrayList<>();
        }
        mFeaturedVideoCollections.add(mediaCollection);
    }

    public void fillFeaturedMediaCollection(final MediaCollection mediaCollection) {
        if (mFeaturedVideoCollections == null) {
            mFeaturedVideoCollections = new ArrayList<>();
        }

        for (int i=0; i< mFeaturedVideoCollections.size(); i++) {
            if (mediaCollection.getId().equals(mFeaturedVideoCollections.get(i).getId())) {
                mFeaturedVideoCollections.set(i, mediaCollection);
                return;
            }
        }
    }

    public ArrayList<MediaCollection> getShows() {
        return mShows;
    }

    public void addShow(final MediaCollection show) {
        if (mShows == null) {
            mShows = new ArrayList<>();
        }
        mShows.add(show);
    }

    public void fillShow(final MediaCollection show) {
        if (mShows == null) {
            mShows = new ArrayList<>();
        }

        for (int i=0; i<mShows.size(); i++) {
            if (show.getId().equals(mShows.get(i).getId())) {
                mShows.set(i, show);
                return;
            }
        }
    }

    public void addFeaturedMedia(final Media media) {
        if (mFeaturedMedia == null) {
            mFeaturedMedia = new ArrayList<>();
        }
        mFeaturedMedia.add(media);
    }

    public void fillFeaturedMedia(final Media media) {
        if (mFeaturedMedia == null) {
            mFeaturedMedia = new ArrayList<>();
        }

        for (int i=0; i<mFeaturedMedia.size(); i++) {
            if (media.getId().equals(mFeaturedMedia.get(i).getId())) {
                mFeaturedMedia.set(i, media);
                return;
            }
        }
    }

    public void fillMedia(final Media media) {
        if (mFeaturedVideoCollections == null) {
            mFeaturedVideoCollections = new ArrayList<>();
        }
        if (mShows == null) {
            mShows = new ArrayList<>();
        }

        for (MediaCollection col : mFeaturedVideoCollections) {
            for (Media mediaInCol : col.getMedia()) {
                if (mediaInCol.getId().equals(media.getId())) {
                    mediaInCol.fillData(media);
                }
            }
        }

        for (MediaCollection show : mShows) {
            for (Media mediaInShow : show.getMedia()) {
                if (mediaInShow.getId().equals(media.getId())) {
                    mediaInShow.fillData(media);
                }
            }
        }
    }

    public ArrayList<Media> getFeaturedMedia() {
        return mFeaturedMedia;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "View{" +
                "mTitle='" + mTitle + '\'' +
                '}';
    }

    public Promotion getPromotion() {
        return mPromotion;
    }

    public void setPromotion(Promotion promotion) {
        mPromotion = promotion;
    }
}
