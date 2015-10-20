package io.oddworks.device.model;

public class Media extends OddMedia {
    public final static String TAG = "Media";
    public final static String LIVE_STREAM = "liveStream";
    public final static String VIDEO = "video";
    private MediaAds mMediaAds;
    private Integer mDuration;
    private String mUrl;

    public Media(final String id, final String type) {
        super(id, type);
    }

    public Media(final String id, final String type, final String title,
            final String description, final MediaImage mediaImage, final MediaAds mediaAds, final String releaseDate, final int duration, final String url) {
        super(id, type, title, description, mediaImage, releaseDate);
        mMediaAds = mediaAds;
        mDuration = duration;
        mUrl = url;
    }

    @Override
    public String toString() {
        return TAG + "{" +
                "mId='" + mId + "', " +
                "mTitle='" + mTitle + "'}";
    }

    public String getUrl() {
        return mUrl;
    }

    public Integer getDuration() {
        return mDuration;
    }

    public MediaAds getMediaAds() {
        return mMediaAds;
    }

    public Boolean isLive() {
        return getType().equals(LIVE_STREAM);
    }

    public void fillData(final Media media) {
        mId = media.getId();
        mType = media.getType();
        mTitle = media.getTitle();
        mDescription = media.getDescription();
        mMediaImage = media.getMediaImage();
        mReleaseDate = media.getReleaseDate();

        mMediaAds = media.getMediaAds();
        mDuration = media.getDuration();
        mUrl = media.getUrl();
    }
}
