package io.oddworks.device.model;

import java.util.Map;

import io.oddworks.device.Util;

public class Metric {
    private final String type;
    private final Map<String, Object> attributes;
    public static final String ENABLED = "enabled";
    public static final String INTERVAL = "interval";
    public static final String ACTION = "action";


    public Metric(String type, Map<String, Object> attributes) {
        this.type = type;
        this.attributes = attributes;
    }

    public String getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        switch (key) {
            case ENABLED:
                Boolean enabled = (Boolean) attributes.get(key);
                if (enabled == null) {
                    return (T) Boolean.FALSE;
                }
                return (T) enabled;
            case INTERVAL:
                return (T) (Util.getInteger(attributes, key, null));
            default:
                return (T) (Util.getString(attributes, key, null));
        }
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
