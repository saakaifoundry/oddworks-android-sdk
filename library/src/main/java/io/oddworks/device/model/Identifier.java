package io.oddworks.device.model;

/**
 * Created by brkattk on 10/20/15.
 */
public class Identifier {
    private static final String TAG = Identifier.class.getSimpleName();
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
