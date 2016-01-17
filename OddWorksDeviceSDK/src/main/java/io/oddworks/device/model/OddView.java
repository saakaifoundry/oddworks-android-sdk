package io.oddworks.device.model;

import java.util.HashMap;
import java.util.Map;

public class OddView extends OddObject {
    public static final String TAG = OddView.class.getSimpleName();

    private String mTitle;

    public OddView(Identifier identifier) {
        super(identifier);
    }

    public OddView(String id, String type) {
        super(id, type);
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public void setAttributes(Map<String, Object> attributes) {
        mTitle = (String) attributes.get("title");
    }

    @Override
    public Map<String, Object> getAttributes() {
        HashMap<String, Object> attributes = new HashMap<>();

        attributes.put("title", getTitle());

        return attributes;
    }


    @Override
    public String toString() {
        return TAG + "(" +
                "id='" + getId() + "', " +
                "title='" + getTitle() + "', " +
                "relationships='" + getRelationships().size() + "', " +
                "included='" + getIncluded().size() + "')";
    }
}
