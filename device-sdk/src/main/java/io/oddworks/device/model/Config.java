package io.oddworks.device.model;

import java.util.LinkedHashMap;

public class Config {
    public static final String TAG = Config.class.getSimpleName();
    private final boolean authEnabled;
    private final MetricsConfig metricsConfig;
    private final DisplayAdsConfig displayAdsConfig;

    private LinkedHashMap<String, String> views;

    public Config(LinkedHashMap<String, String> views,
                  boolean authEnabled,
                  MetricsConfig metricsConfig,
                  DisplayAdsConfig displayAdsConfig) {
        this.views = views;
        this.authEnabled = authEnabled;
        this.metricsConfig = metricsConfig;
        this.displayAdsConfig = displayAdsConfig;
    }


    public boolean isAuthEnabled() {
        return authEnabled;
    }

    public MetricsConfig getMetricsConfig() {
        return metricsConfig;
    }

    public LinkedHashMap<String, String> getViews() {
        return views;
    }

    public DisplayAdsConfig getDisplayAdsConfig() { return displayAdsConfig; }
}
