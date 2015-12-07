package io.oddworks.device.model;

import java.util.ArrayList;

public class Relationship {
    public static final String TAG = Relationship.class.getSimpleName();
    private String mName;
    private ArrayList<Identifier> mIdentifiers;

    public Relationship(final String name) {
        mName = name;
    }

    public Relationship(final String name, final ArrayList<Identifier> identifiers) {
        mName = name;
        mIdentifiers = identifiers;
    }

    public String getName() {
        return mName;
    }

    public void addIdentifier(Identifier identifier) {
        getIdentifiers().add(identifier);
    }

    public ArrayList<Identifier> getIdentifiers() {
        if (mIdentifiers == null) {
            mIdentifiers = new ArrayList<>();
        }
        return mIdentifiers;
    }
}
