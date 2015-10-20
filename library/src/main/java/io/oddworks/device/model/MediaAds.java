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
        setProvider(provider);
        setFormat(format);
        setUrl(url);
    }

    @Override
    public String toString() {
        return TAG + "{" +
                "provider='" + getProvider() + "', " +
                "format='" + getFormat() + "', " +
                "url='" + getUrl() + "'}";
    }

    public String getProvider() {
        return mProvider;
    }

    public void setProvider(String provider) {
        mProvider = provider;
    }

    public String getFormat() {
        return mFormat;
    }

    public void setFormat(String format) {
        mFormat = format;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Boolean isAvailable() {
        return getFormat() != null && getUrl() != null;
    }

    public Boolean isVMAP() {
        if (getFormat() == null) {
            return false;
        }
        return getFormat().matches(FORMAT_VMAP);
    }
}
