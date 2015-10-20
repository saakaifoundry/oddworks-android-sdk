package io.oddworks.device.model;

import java.util.ArrayList;

/**
 * Created by brkattk on 9/21/15.
 */
public class MediaCollection extends OddMedia {
    private static final String TAG = MediaCollection.class.getSimpleName();
    public static final String VIDEO_COLLECTION = "videoCollection";
    private ArrayList<Media> mMedia;
    private String mReleaseDate;

    public MediaCollection(final String id, final String type) {
      super(id, type);
    }

    public MediaCollection(final String id, final String type, final String title, final String description,
                           final MediaImage mediaImage, final String releaseDate) {
        super(id, type, title, description, mediaImage, releaseDate);
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
