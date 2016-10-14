package io.oddworks.device.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.oddworks.device.model.common.OddIdentifier;
import io.oddworks.device.model.common.OddRelationship;
import io.oddworks.device.model.common.OddResource;

public class OddResourceCache {
    private final ConcurrentMap<String, StoredObject> objectStore = new ConcurrentHashMap<>();

    protected OddResourceCache() { }

    @Nullable protected OddResource getObject(@NonNull String id) {
        StoredObject storedObject = objectStore.get(id);
        OddResource object = null;
        if (storedObject != null) {
            if(storedObject.expiration > new Date().getTime())
                object = storedObject.object;
            else
                objectStore.remove(id);
        }
        return object;
    }

    @Nullable protected OddResource getObject(@NonNull OddIdentifier identifier) {
        return getObject(identifier.getId());
    }

    /** stores object and all included. Sets a maxAge for this object and all included objects
     * @param maxAge object will expire after this many milliseconds */
    protected void storeObject(@NonNull OddResource object, long maxAge) {
        long expiration = maxAge + new Date().getTime();
        objectStore.put(object.getIdentifier().getId(), new StoredObject(object, expiration));
        Set<OddResource> included = object.getIncluded();
        if (!included.isEmpty()) {
            storeObjects(included, maxAge);
        }
    }

    protected void storeObjects(@NonNull Collection<OddResource> objects, Long maxAge) {
        for (OddResource object : objects) {
            storeObject(object, maxAge);
        }
    }

    /** objects that do not exist will be represented by null entries in the list.
     * @param identifiers elements may be null */
    @NonNull protected List<OddResource> getObjects(@NonNull Collection<OddIdentifier> identifiers) {
        List<OddResource> objects = new ArrayList<>();
        for (OddIdentifier identifier : identifiers) {
            OddResource object = getObject(identifier);
            objects.add(object);
        }
        return objects;
    }

    /** Get all OddResources contained in all of the relationships of an OddResource. Recursive relationships are not
     * resolved.
     * @return if all related objects are contained in the store then they are returned as a list, otherwise null
     *  Related objects that aren't in the OddResourceCache will not be included in list. */
    @Nullable protected Set<OddResource> getRelatedObjectsOrNull(@NonNull OddResource oddObject) {
        LinkedHashSet<OddResource> items = new LinkedHashSet<>();
        for (OddRelationship relationship : oddObject.getRelationships()) {
            List<OddResource> objects = getObjects(relationship.getIdentifiers());
            for(OddResource object : objects) {
                if(object != null)
                    items.add(object);
                else
                    return null;
            }
        }

        return items;
    }


    /** removes all objects from store */
    protected void clear() {
        objectStore.clear();
    }

    private static class StoredObject {
        private final OddResource object;
        private final long expiration;

        private StoredObject(OddResource object, long expiration) {
            this.object = object;
            this.expiration = expiration;
        }
    }
}
