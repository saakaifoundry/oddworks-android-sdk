package io.oddworks.device.model;

import java.util.LinkedHashMap;

public class Config {
    public static final String TAG = Config.class.getSimpleName();
    private final boolean mAuthEnabled;
    private final AdsConfig mAdsConfig;
    private final MetricsConfig mMetricsConfig;

    private LinkedHashMap<String, String> mViews;

    public Config(LinkedHashMap<String, String> views,
                  boolean authEnabled,
                  AdsConfig adsConfig,
                  MetricsConfig metricsConfig) {
        this.mViews = views;
        this.mAuthEnabled = authEnabled;
        this.mAdsConfig = adsConfig;
        this.mMetricsConfig = metricsConfig;
    }


    public boolean isAuthEnabled() {
        return mAuthEnabled;
    }

    public AdsConfig getAdsConfig() {
        return mAdsConfig;
    }

    public MetricsConfig getMetricsConfig() {
        return mMetricsConfig;
    }

    public LinkedHashMap<String, String> getViews() {
        return mViews;
    }
}
