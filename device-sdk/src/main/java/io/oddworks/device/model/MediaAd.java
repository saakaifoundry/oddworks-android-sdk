package io.oddworks.device.model;

import java.util.Map;

public class MediaAd {
    public static final String TAG = MediaAd.class.getSimpleName();
    private static final String FORMAT_VMAP = "vmap";
    private static final String FORMAT_FREEWHEEL = "freewheel";
    private static final String FORMAT_DFP = "dfp";

    private boolean mEnabled;
    private String mProvider;
    private String mFormat;
    private String mUrl;
    private int mNetworkId;
    private String mProfileName;
    private String mSiteSectionId;
    private String mVHost;
    private String mAssetId;

    public MediaAd(){};

    public MediaAd(final Map<String, Object> properties) {
        mEnabled = properties.containsKey("enabled") && (boolean) properties.get("enabled");
        mProvider = (String) properties.get("provider");
        mFormat = (String) properties.get("format");
        mUrl = (String) properties.get("url");
        if (properties.containsKey("networkId")) {
            mNetworkId = (int) properties.get("networkId");
        }
        mProfileName = (String) properties.get("profileName");
        mSiteSectionId = (String) properties.get("siteSectionId");
        mVHost = (String) properties.get("vHost");
        mAssetId = (String) properties.get("assetId");
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

    public boolean isEnabled() {
        return mEnabled;
    }

    public boolean isVMAP() {
        if (getFormat() == null) {
            return false;
        }

        return getFormat().equals(FORMAT_VMAP);
    }

    public boolean isFreeWheel() {
        if (getFormat() == null) {
            return false;
        }

        return getFormat().equals(FORMAT_FREEWHEEL);
    }

    public boolean isDFP() {
        if (getFormat() == null) {
            return false;
        }

        return getFormat().equals(FORMAT_DFP);
    }

    @Override
    public String toString() {
        return "MediaAd{" +
                "mProvider='" + mProvider + '\'' +
                ", mFormat='" + mFormat + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mNetworkId=" + mNetworkId +
                ", mProfileName='" + mProfileName + '\'' +
                ", mSiteSectionId='" + mSiteSectionId + '\'' +
                ", mVHost='" + mVHost + '\'' +
                ", mAssetId='" + mAssetId + '\'' +
                '}';
    }
}
