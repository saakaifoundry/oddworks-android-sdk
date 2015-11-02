package io.oddworks.device.model;

/**
 * Created by brkattk on 9/25/15.
 */
public class MediaAds {
    private static final String TAG = MediaAds.class.getSimpleName();
    private static final String FORMAT_VMAP = "vmap";

    private String mProvider;
    private String mFormat;
    private String mUrl;

    public MediaAds(){};

    public MediaAds(final String provider, final String format, final String url) {
        mProvider = provider;
        mFormat = format;
        mUrl = url;
    }

    @Override
    public String toString() {
        return TAG + "(" +
                "provider='" + getProvider() + "', " +
                "format='" + getFormat() + "', " +
                "url='" + getUrl() + "')";
    }

    public String getProvider() {
        return mProvider;
    }

    public String getFormat() {
        return mFormat;
    }

    public String getUrl() {
        return mUrl;
    }

    public Boolean isAvailable() {
        return getFormat() != null && getUrl() != null;
    }

    public Boolean isVMAP() {
        if (getFormat() == null) {
            return false;
        }

        return getFormat().equals(FORMAT_VMAP);
    }
}
