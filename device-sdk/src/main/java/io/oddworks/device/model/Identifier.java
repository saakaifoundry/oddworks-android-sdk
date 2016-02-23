package io.oddworks.device.model;

public class Identifier {
    public static final String TAG = Identifier.class.getSimpleName();
    private String id;
    private String type;

    public Identifier(final String id, final String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
