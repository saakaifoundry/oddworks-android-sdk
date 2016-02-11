package io.oddworks.device.model;

public class Identifier {
    public static final String TAG = Identifier.class.getSimpleName();
    private String mId;
    private String mType;

    public Identifier(final String id, final String type) {
        mId = id;
        mType = type;
    }

    public String getId() {
        return mId;
    }

    public String getType() {
        return mType;
    }
}
