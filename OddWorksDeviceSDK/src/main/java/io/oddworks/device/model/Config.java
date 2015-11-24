package io.oddworks.device.model;

import java.util.LinkedHashMap;

public class Config {
    public static final String TAG = Config.class.getSimpleName();
    private final boolean mAuthEnabled;
    private final AdsConfig mAds;

    private LinkedHashMap<String, String> mViews;

    public Config(LinkedHashMap<String, String> views,
                  boolean authEnabled,
                  AdsConfig ads) {
        this.mViews = views;
        this.mAuthEnabled = authEnabled;
        this.mAds = ads;
    }


    public boolean isAuthEnabled() {
        return mAuthEnabled;
    }

    public AdsConfig getAds() {
        return mAds;
    }

    public LinkedHashMap<String, String> getViews() {
        return mViews;
    }
}
