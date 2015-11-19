package io.oddworks.device.model;

import java.util.HashMap;

public class MediaAd {
    public static final String TAG = MediaAd.class.getSimpleName();
    private static final String FORMAT_VMAP = "vmap";
    private static final String FORMAT_FREEWHEEL = "freewheel";

    private String mProvider;
    private String mFormat;
    private String mUrl;
    private int mNetworkId;
    private String mProfileName;
    private String mSiteSectionId;
    private String mVHost;
    private String mAssetId;

    public MediaAd(){};

    public MediaAd(final HashMap<String, Object> properties) {
        mProvider = (String) properties.get("provider");
        mFormat = (String) properties.get("format");
        mUrl = (String) properties.get("url");
        mNetworkId = (int) properties.get("networkId");
        mProfileName = (String) properties.get("profileName");
        mSiteSectionId = (String) properties.get("siteSectionId");
        mVHost = (String) properties.get("vHost");
        mAssetId = (String) properties.get("assetId");
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

    public int getNetworkId() {
        return mNetworkId;
    }

    public String getProfileName() {
        return mProfileName;
    }

    public String getSiteSectionId() {
        return mSiteSectionId;
    }

    public String getVHost() {
        return mVHost;
    }

    public String getAssetId() {
        return mAssetId;
    }

    public Boolean isAvailable() {
        return getProvider() != null && getFormat() != null;
    }

    public Boolean isVMAP() {
        if (getFormat() == null) {
            return false;
        }

        return getFormat().equals(FORMAT_VMAP);
    }

    public Boolean isFreeWheel() {
        if (getFormat() == null) {
            return false;
        }

        return getFormat().equals(FORMAT_FREEWHEEL);
    }
}
