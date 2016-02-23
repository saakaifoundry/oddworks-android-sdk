package io.oddworks.device.model;

import android.support.annotation.NonNull;

public class Identifier {
    public static final String TAG = Identifier.class.getSimpleName();
    private String id;
    private String type;

    public Identifier(@NonNull final String id, @NonNull final String type) {
        this.id = id;
        this.type = type;
    }

    @NonNull public String getId() {
        return id;
    }

    @NonNull public String getType() {
        return type;
    }
}
