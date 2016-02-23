package io.oddworks.device.model;

import java.util.Map;

import static io.oddworks.device.Util.getInteger;
import static io.oddworks.device.Util.getString;

public class MediaAd {
    public static final String TAG = MediaAd.class.getSimpleName();
    private static final String FORMAT_VMAP = "vmap";
    private static final String FORMAT_FREEWHEEL = "freewheel";
    private static final String FORMAT_DFP = "dfp";

    private boolean enabled;
    private String provider;
    private String format;
    private String url;
    private int networkId;
    private String profileName;
    private String siteSectionId;
    private String vHost;
    private String assetId;

    public MediaAd(){};

    public MediaAd(final Map<String, Object> properties) {
        enabled = properties.containsKey("enabled") && (boolean) properties.get("enabled");
        provider = getString(properties, "provider", null);
        format = getString(properties, "format", null);
        url = getString(properties, "url", null);
        if (properties.containsKey("networkId")) {
            networkId = getInteger(properties, "networkId", null);
        }
        profileName = getString(properties, "profileName", null);
        siteSectionId = getString(properties, "siteSectionId", null);
        vHost = getString(properties, "vHost", null);
        assetId = getString(properties, "assetId", null);
    }

    public String getProvider() {
        return provider;
    }

    public String getFormat() {
        return format;
    }

    public String getUrl() {
        return url;
    }

    public int getNetworkId() {
        return networkId;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getSiteSectionId() {
        return siteSectionId;
    }

    public String getVHost() {
        return vHost;
    }

    public String getAssetId() {
        return assetId;
    }

    public boolean isEnabled() {
        return enabled;
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
                "provider='" + provider + '\'' +
                ", format='" + format + '\'' +
                ", url='" + url + '\'' +
                ", networkId=" + networkId +
                ", profileName='" + profileName + '\'' +
                ", siteSectionId='" + siteSectionId + '\'' +
                ", vHost='" + vHost + '\'' +
                ", assetId='" + assetId + '\'' +
                '}';
    }
}
