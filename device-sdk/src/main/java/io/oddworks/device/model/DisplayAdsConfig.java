package io.oddworks.device.model;

public class DisplayAdsConfig {
    private final boolean enabled;
    private final String publisherId;
    private final String defaultUnitId;
    private final String homeUnitId;

    public DisplayAdsConfig(boolean enabled,
                            String publisherId,
                            String defaultUnitId,
                            String homeUnitId) {
        this.enabled = enabled;
        this.publisherId = publisherId;
        this.defaultUnitId = defaultUnitId;
        this.homeUnitId = homeUnitId;
    }

    public boolean getEnabled() { return enabled; }
    public String getPublisherId() { return publisherId; }
    public String getDefaultUnitId() { return defaultUnitId; }
    public String getHomeUnitId() { return homeUnitId; }
}
