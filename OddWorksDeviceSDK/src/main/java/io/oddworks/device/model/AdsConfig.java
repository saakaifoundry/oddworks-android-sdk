package io.oddworks.device.model;

public class AdsConfig {
    public enum AdProvider { FREEWHEEL, BRIGHTCOVE, YOESPACE, GOOGLE}
    public enum AdFormat { VAST }
    private final AdProvider provider;
    private final AdFormat format;
    private final String url;

    public AdsConfig(AdProvider provider, AdFormat format, String url) {
        this.provider = provider;
        this.format = format;
        this.url = url;
    }

    public AdProvider getProvider() {
        return provider;
    }

    public AdFormat getFormat() {
        return format;
    }

    public String getUrl() {
        return url;
    }
}
