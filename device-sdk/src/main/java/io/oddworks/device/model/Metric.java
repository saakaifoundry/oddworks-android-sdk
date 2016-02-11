package io.oddworks.device.model;

import java.util.Map;

public class Metric {
    private final String mType;
    private final Map<String, Object> mAttributes;
    public static final String ENABLED = "enabled";
    public static final String INTERVAL = "interval";
    public static final String ACTION = "action";


    public Metric(String type, Map<String, Object> attributes) {
        this.mType = type;
        this.mAttributes = attributes;
    }

    public String getType() {
        return mType;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        switch (key) {
            case ENABLED:
                Boolean enabled = (Boolean) mAttributes.get(key);
                if (enabled == null) {
                    return (T) Boolean.FALSE;
                }
                return (T) enabled;
            case INTERVAL:
                return (T) ((Integer) mAttributes.get(key));
            default:
                return (T) ((String) mAttributes.get(key));
        }
    }

    public Map<String, Object> getAttributes() {
        return mAttributes;
    }
}
