package io.oddworks.device.model;

import java.util.ArrayList;
import java.util.List;

public class Relationship {
    public static final String TAG = Relationship.class.getSimpleName();
    private String name;
    private ArrayList<Identifier> identifiers;

    public Relationship(final String name) {
        this.name = name;
    }

    public Relationship(final String name, final ArrayList<Identifier> identifiers) {
        this.name = name;
        this.identifiers = identifiers;
    }

    public String getName() {
        return name;
    }

    public void addIdentifier(Identifier identifier) {
        getIdentifiers().add(identifier);
    }

    public List<Identifier> getIdentifiers() {
        if (identifiers == null) {
            identifiers = new ArrayList<>();
        }
        return identifiers;
    }
}
