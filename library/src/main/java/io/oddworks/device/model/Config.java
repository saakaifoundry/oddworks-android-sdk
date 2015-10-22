package io.oddworks.device.model;

import java.util.HashMap;

/**
 * Created by brkattk on 9/15/15.
 */
public class Config extends OddObject {

    private String mViewId;

    public Config(Identifier identifier) {
        super(identifier);
    }

    public Config(String id, String type) {
        super(id, type);
    }

    public final String getViewId() {
        return mViewId;
    }

    @Override
    public HashMap<String, Object> getAttributes() {
        HashMap<String, Object> attributes = new HashMap<>();

        attributes.put("viewId", getViewId());

        return attributes;
    }

    @Override
    public void setAttributes(HashMap<String, Object> attributes) {
        mViewId = (String) attributes.get("viewId");
    }

    @Override
    public String toString() {
        return "Config{" +
                "mViewId='" + mViewId + '\'' +
                '}';
    }
}
