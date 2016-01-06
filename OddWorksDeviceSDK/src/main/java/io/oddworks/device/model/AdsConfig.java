package io.oddworks.device.model;

public class AdsConfig {
    public enum AdProvider { FREEWHEEL, GOOGLE, VIDEOPLAZA }
    public enum AdFormat { FREEWHEEL, VAST, VMAP, DFP }
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
