package io.oddworks.device.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.oddworks.device.model.common.OddIdentifier;
import io.oddworks.device.model.OddObject;
import io.oddworks.device.model.common.Relationship;

/**
 * Cache to be used by CachingApiCaller
 *
 * @author Dan Pallas
 * @since v1.4 03/01/2016
 */
public class OddResourceCache {
    private final ConcurrentMap<String, StoredObject> objectStore = new ConcurrentHashMap<>();

    protected OddResourceCache() { }

    @Nullable protected OddObject getObject(@NonNull String id) {
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

    @Nullable protected OddObject getObject(@NonNull OddIdentifier identifier) {
        return getObject(identifier.getId());
    }

    /** stores object and all included. Sets a maxAge for this object and all included objects
     * @param maxAge object will expire after this many milliseconds */
    protected void storeObject(@NonNull OddObject object, long maxAge) {
        long expiration = maxAge + new Date().getTime();
        objectStore.put(object.getId(), new StoredObject(object, expiration));
        List<OddObject> included = object.getIncluded();
        if (!included.isEmpty()) {
            storeObjects(included, maxAge);
        }
    }

    protected void storeObjects(@NonNull Collection<? extends OddObject> objects, Long maxAge) {
        for (OddObject object : objects) {
            storeObject(object, maxAge);
        }
    }

    /** objects that do not exist will be represented by null entries in the list.
     * @param identifiers elements may be null */
    @NonNull protected List<OddObject> getObjects(@NonNull Collection<OddIdentifier> identifiers) {
        List<OddObject> objects = new ArrayList<>();
        for (OddIdentifier identifier : identifiers) {
            OddObject object = getObject(identifier);
            objects.add(object);
        }
        return objects;
    }

    /** Get all OddObjects contained in all of the relationships of an OddObject. Recursive relationships are not
     * resolved.
     * @return if all related objects are contained in the store then they are returned as a list, otherwise null
     *  Related objects that aren't in the OddResourceCache will not be included in list. */
    @Nullable protected List<OddObject> getRelatedObjectsOrNull(@NonNull OddObject oddObject) {
        LinkedHashSet<OddObject> items = new LinkedHashSet<>();
        for (Relationship relationship : oddObject.getRelationships()) {
            List<OddObject> objects = getObjects(relationship.getIdentifiers());
            for(OddObject object : objects) {
                if(object != null)
                    items.add(object);
                else
                    return null;
            }
        }
        ArrayList<OddObject> list = new ArrayList<OddObject>();
        list.addAll(items);
        return list;
    }


    /** removes all objects from store */
    protected void clear() {
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
