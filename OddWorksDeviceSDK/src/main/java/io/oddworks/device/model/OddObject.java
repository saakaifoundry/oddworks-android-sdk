package io.oddworks.device.model;

import java.util.ArrayList;
import java.util.HashMap;

abstract public class OddObject {
    public static final String TAG = OddObject.class.getSimpleName();
    public static final String TYPE_ARTICLE = "article";
    public static final String TYPE_COLLECTION = "collection";
    public static final String TYPE_EVENT = "event";
    public static final String TYPE_EXTERNAL = "external";
    public static final String TYPE_LIVE_STREAM = "liveStream";
    public static final String TYPE_PROMOTION = "promotion";
    public static final String TYPE_VIDEO_COLLECTION = "videoCollection";
    public static final String TYPE_VIDEO = "video";

    protected Identifier mIdentifier;
    protected String mId;
    protected String mType;
    protected ArrayList<Relationship> mRelationships;
    protected ArrayList<OddObject> mIncluded;

    public OddObject(final Identifier identifier) {
        mId = identifier.getId();
        mType = identifier.getType();
    }

    public OddObject(final String id, final String type) {
        mId = id;
        mType = type;
    }

    public String getId() {
        return mId;
    }

    public String getType() {
        return mType;
    }

    public Identifier getIdentifier() {
        if (mIdentifier == null) {
            mIdentifier = new Identifier(getId(), getType());
        }
        return mIdentifier;
    }

    public void setAttributes(HashMap<String, Object> attributes) {

    }

    public HashMap<String, Object> getAttributes() {
        return new HashMap<>();
    }

    public void fillData(OddObject oddObject) {
        setAttributes(oddObject.getAttributes());
    }

    public void addRelationship(Relationship relationship) {
        getRelationships().add(relationship);
    }

    public ArrayList<Relationship> getRelationships() {
        if (mRelationships == null) {
            mRelationships = new ArrayList<>();
        }
        return mRelationships;
    }

    public Relationship getRelationship(String name) {
        for(Relationship rel : getRelationships()) {
            if(rel.getName().equals(name)) {
                return rel;
            }
        }
        return null;
    }

    public void addIncluded(OddObject oddObject) {
        getIncluded().add(oddObject);
    }

    public ArrayList<OddObject> getIncluded() {
        if (mIncluded == null) {
            mIncluded = new ArrayList<>();
        }
        return mIncluded;
    }

    public ArrayList<OddObject> getIncludedByType(String type) {
        ArrayList<OddObject> includedOfType = new ArrayList<>();
        for(OddObject oddObject : getIncluded()) {
            if (oddObject.getType().equals(type)) {
                includedOfType.add(oddObject);
            }
        }
        return includedOfType;
    }

    public ArrayList<OddObject> getIncludedByType(ArrayList<String> types) {
        ArrayList<OddObject> includedOfType = new ArrayList<>();
        for(OddObject oddObject : getIncluded()) {
            if (types.indexOf(oddObject.getType()) > -1) {
                includedOfType.add(oddObject);
            }
        }
        return includedOfType;
    }

    public ArrayList<OddObject> getIncludedByRelationship(String name) {
        ArrayList<OddObject> includedOfRelationship = new ArrayList<>();
        Relationship relationship = getRelationship(name);
        if (relationship == null) {
            return includedOfRelationship;
        }
        for(OddObject oddObject : getIncluded()) {
            for(Identifier identifier : relationship.getIdentifiers()) {
                if(identifier.getId().equals(oddObject.getId()) && identifier.getType().equals(oddObject.getType())) {
                    includedOfRelationship.add(oddObject);
                }
            }
        }
        return includedOfRelationship;
    }

    public OddObject findIncludedByIdentifier(Identifier identifier) {
        for(OddObject item : getIncluded()) {
            if (identifier.getId().equals(item.getId()) && identifier.getType().equals(item.getType())) {
                return item;
            }
        }
        return null;
    }

    public void fillIncludedCollections() {
        for(OddObject collection : getIncludedByType(OddObject.TYPE_COLLECTION)) {
            fillCollectionWithIncluded(collection);
        }

        // SUPPORT LEGACY TYPE_VIDEO_COLLECTION
        for(OddObject collection : getIncludedByType(OddObject.TYPE_VIDEO_COLLECTION)) {
            fillCollectionWithIncluded(collection);
        }
    }

    protected void fillCollectionWithIncluded(OddObject collection) {
        for (Relationship relationship : collection.getRelationships()) {
            for(Identifier identifier : relationship.getIdentifiers()) {
                OddObject oddObject = findIncludedByIdentifier(identifier);
                if (oddObject != null) {
                    collection.addIncluded(oddObject);
                }
            }
        }
    }

    /**
     * @return true if a presentable can be created from this object using the method toPresentable, otherwise false

     */
    public boolean isPresentable() {
        return false;
    }

    /**
     * Generate a Presentable from this odd object. Should be overridden by any child classes that can be presented.
     * If overriding, remember to override isPresentable too.
     * @return a Presentable representation of this object or null if object is not presentable.
     */
    public Presentable toPresentable() {
        return null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id='" + getId() + "', " +
                "type='" + getType() + "')";
    }

}
