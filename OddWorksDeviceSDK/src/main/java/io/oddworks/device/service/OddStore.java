package io.oddworks.device.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.oddworks.device.model.Identifier;
import io.oddworks.device.model.OddObject;
import io.oddworks.device.model.Relationship;

public class OddStore {
    private static final OddStore INSTANCE = new OddStore();
    private final Map<String, OddObject> objectStore = new HashMap<>();

    private OddStore() {
        // singleton
    }

    public static OddStore getInstance() {
        return INSTANCE;
    }

    public OddObject getObject(String id) {
        return objectStore.get(id);
    }

    public OddObject getObject(Identifier identifier) {
        return objectStore.get(identifier.getId());
    }

    /** stores object and all included */
    public void storeObject(OddObject object) {
        objectStore.put(object.getId(), object);
        List<OddObject> included = object.getIncluded();
        if (included != null) {
            storeObjects(included);
        }
    }

    public void storeObjects(Collection<? extends OddObject> objects) {
        for (OddObject object : objects) {
            storeObject(object);
        }
    }

    public List<OddObject> getObjects(Collection<Identifier> identifiers) {
        List<OddObject> objects = new ArrayList<>();
        for (Identifier identifier : identifiers) {
            objects.add(getObject(identifier));
        }
        return objects;
    }

    /** Checks if the store contains all objects that are related to the oddObject non recursively
     * @return true if all related objects are stored, otherwise false.
     */
    public boolean containsAllRelatedObjects(OddObject oddObject) {
        for (Relationship relationship : oddObject.getRelationships()) {
            for (Identifier identifier : relationship.getIdentifiers()) {
                if (getObject(identifier) == null)
                    return false;
            }
        }
        return true;
    }

    /** Get all OddObjects contained in all of the relationships of an OddObject. Recursive relationships are not
     * resolved.
     * @return a list of all related OddObjects. Related objects that aren't in the OddAndroid store will be null. */
    public List<OddObject> getRelatedObjects(OddObject oddObject) {
        List<OddObject> items = new ArrayList<>();
        for (Relationship relationship : oddObject.getRelationships()) {
            items.addAll(getObjects(relationship.getIdentifiers()));
        }
        return items;
    }
}
