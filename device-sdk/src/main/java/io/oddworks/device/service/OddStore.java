package io.oddworks.device.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.oddworks.device.model.Identifier;
import io.oddworks.device.model.OddObject;
import io.oddworks.device.model.Relationship;

public class OddStore {
    private static final OddStore INSTANCE = new OddStore();
    private final ConcurrentMap<String, StoredObject> objectStore = new ConcurrentHashMap<>();
    private Integer maxAge = null;
    private OddStore() {
        // singleton
    }

    @NonNull public static OddStore getInstance() {
        return INSTANCE;
    }

    /** set expiration for objects in cache. If this method is not called, then objects will remain in the cache forever
     * @param maxAge maximum age of objects in milliseconds before they are removed from cache */
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    @Nullable public OddObject getObject(@NonNull String id) {
        StoredObject storedObject = objectStore.get(id);
        OddObject object = null;
        if (storedObject != null) {
            if(storedObject.expiration > new Date().getTime())
                object = storedObject.object;
            else
                objectStore.remove(id);
        }
        return object;
    }

    @Nullable public OddObject getObject(@NonNull Identifier identifier) {
        return getObject(identifier.getId());
    }

    /** stores object and all included. */
    public void storeObject(@NonNull OddObject object) {
        long expiration = maxAge != null ? maxAge + new Date().getTime() : Long.MAX_VALUE;
        objectStore.put(object.getId(), new StoredObject(object, expiration));
        List<OddObject> included = object.getIncluded();
        if (included != null) {
            storeObjects(included);
        }
    }

    public void storeObjects(@NonNull Collection<? extends OddObject> objects) {
        for (OddObject object : objects) {
            storeObject(object);
        }
    }

    /** objects that do not exist will not be included in the returned list.
     * @param identifiers all elements should be non null
     * @return all elements will be non null */
    @NonNull public List<OddObject> getObjects(@NonNull Collection<Identifier> identifiers) {
        List<OddObject> objects = new ArrayList<>();
        for (Identifier identifier : identifiers) {
            OddObject object = getObject(identifier);
            if(object != null)
                objects.add(object);
        }
        return objects;
    }

    /**
     * Deprecated because of potential issue when using maxAge. The store could contain all objects and then the objects
     * could be removed before retreiving them.
     *
     * Checks if the store contains all objects that are related to the oddObject non recursively
     * @return true if all related objects are stored, otherwise false.
     */
    @Deprecated public boolean containsAllRelatedObjects(@NonNull OddObject oddObject) {
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
     * @return a list of all related nonnull OddObjects.
     *  Related objects that aren't in the OddStore will not be included in list. */
    @NonNull public List<OddObject> getRelatedObjects(@NonNull OddObject oddObject) {
        LinkedHashSet<OddObject> items = new LinkedHashSet<>();
        for (Relationship relationship : oddObject.getRelationships()) {
            List<OddObject> objects = getObjects(relationship.getIdentifiers());
            for(OddObject object : objects) {
                if(object != null)
                    items.addAll(objects);
            }
        }
        return Arrays.asList((OddObject[])items.toArray());
    }

    public void remove(@NonNull OddObject object) {
        objectStore.remove(object.getId());
    }

    /** removes all objects from store */
    public void clear() {
        objectStore.clear();
    }

    private static class StoredObject {
        private final OddObject object;
        private final long expiration;

        private StoredObject(OddObject object, long expiration) {
            this.object = object;
            this.expiration = expiration;
        }
    }
}
