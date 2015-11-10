package io.oddworks.device.model;

import java.util.HashMap;

public class Config extends OddObject {
    public static final String TAG = Config.class.getSimpleName();

    private HashMap<String, Object> mViews;
    private HashMap<String, Object> mFeatures;

    public Config(Identifier identifier) {
        super(identifier);
    }

    public Config(String id, String type) {
        super(id, type);
    }

    public final HashMap<String, Object> getViews() {
        if (mViews == null) {
            mViews = new HashMap<>();
        }
        return mViews;
    }

    public final HashMap<String, Object> getFeatures() {
        if (mFeatures == null) {
            mFeatures = new HashMap<>();
        }
        return mFeatures;
    }

    @Override
    public HashMap<String, Object> getAttributes() {
        HashMap<String, Object> attributes = new HashMap<>();

        attributes.put("views", getViews());
        attributes.put("features", getFeatures());

        return attributes;
    }

    @Override
    public void setAttributes(HashMap<String, Object> attributes) {
        mViews = (HashMap<String, Object>) attributes.get("views");
        mFeatures = (HashMap<String, Object>) attributes.get("features");
    }

    @Override
    public String toString() {
        return "Config{" +
                "views='" + getViews().toString() + '\'' +
                "features='" + getFeatures().toString() + '\'' +
                '}';
    }
}
