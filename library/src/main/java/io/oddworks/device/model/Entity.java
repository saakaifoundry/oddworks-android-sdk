package io.oddworks.device.model;

import android.os.Parcelable;

public abstract class Entity implements Parcelable {
    String mId;
    String mType;

    public Entity(final String type) { mType = type; }

    public String getId() { return mId; }

    public String getType() {
        return mType;
    }
}
