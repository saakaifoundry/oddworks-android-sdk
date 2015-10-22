package io.oddworks.device.model;

import java.util.ArrayList;

/**
 * Created by brkattk on 10/20/15.
 */
public class Relationship {
    private static final String TAG = Relationship.class.getSimpleName();
    private String mName;
    private ArrayList<Identifier> mIdentifiers;

    public Relationship(final String name, final ArrayList<Identifier> identifiers) {
        mName = name;
        mIdentifiers = identifiers;
    }

    public String getName() {
        return mName;
    }

    public ArrayList<Identifier> getIdentifiers() {
        return mIdentifiers;
    }
}
