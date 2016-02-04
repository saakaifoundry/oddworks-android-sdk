package io.oddworks.device.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract public class OddObject {
    public static final String TAG = OddObject.class.getSimpleName();
    public static final String TYPE_ARTICLE = "article";
    public static final String TYPE_COLLECTION = "collection";
    public static final String TYPE_EVENT = "event";
    public static final String TYPE_EXTERNAL = "external";
    public static final String TYPE_LIVE_STREAM = "liveStream";
    public static final String TYPE_PROMOTION = "promotion";
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

    public abstract void setAttributes(Map<String, Object> attributes);

    public abstract Map<String, Object> getAttributes();

    public void fillData(OddObject oddObject) {
        setAttributes(oddObject.getAttributes());
    }

    public void addRelationship(Relationship relationship) {
        getRelationships().add(relationship);
    }

    public List<Relationship> getRelationships() {
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

    /**
     *
     * @return  true - if there are any missing related entities
     */
    public boolean isMissingIncluded() {
        for(Relationship relationship : getRelationships()) {
            for(Identifier identifier : relationship.getIdentifiers()) {
                if (findIncludedByIdentifier(identifier) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /** @return the count of OddObject directly related to this OddObject */
    public int getRelatedObjectCount() {
        int count = 0;
        for (Relationship relationship : mRelationships) {
            count += relationship.getIdentifiers().size();
        }
        return count;
    }

    public void addIncluded(OddObject oddObject) {
        getIncluded().add(oddObject);
    }

    public List<OddObject> getIncluded() {
        if (mIncluded == null) {
            mIncluded = new ArrayList<>();
        }
        return mIncluded;
    }

    public List<OddObject> getIncludedByType(String type) {
        ArrayList<OddObject> includedOfType = new ArrayList<>();
        for(OddObject oddObject : getIncluded()) {
            if (oddObject.getType().equals(type)) {
                includedOfType.add(oddObject);
            }
        }
        return includedOfType;
    }

    public List<OddObject> getIncludedByType(List<String> types) {
        ArrayList<OddObject> includedOfType = new ArrayList<>();
        for(OddObject oddObject : getIncluded()) {
            if (types.indexOf(oddObject.getType()) > -1) {
                includedOfType.add(oddObject);
            }
        }
        return includedOfType;
    }

    public List<OddObject> getIncludedByRelationship(String name) {
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

    /** @return  all identifiers for relationship, or null if that relationship doesn't exist */
    public List<Identifier> getIdentifiersByRelationship(String name) {
        Relationship rel = getRelationship(name);
        return rel == null ? null : rel.getIdentifiers();
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
        return "OddObject{" +
                "mIdentifier=" + mIdentifier +
                ", mId='" + mId + '\'' +
                ", mType='" + mType + '\'' +
                ", mRelationships=" + mRelationships +
                ", mIncluded=" + mIncluded +
                '}';
    }
}