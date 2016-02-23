package io.oddworks.device.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONObject;

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

    protected Identifier identifier;
    protected String id;
    protected String type;
    protected ArrayList<Relationship> relationships;
    protected ArrayList<OddObject> included;
    protected JSONObject meta;

    public OddObject(final Identifier identifier) {
        id = identifier.getId();
        type = identifier.getType();
    }

    public OddObject(final String id, final String type) {
        this.id = id;
        this.type = type;
    }

    @Nullable public String getId() {
        return id;
    }

    @Nullable public String getType() {
        return type;
    }

    @NonNull public Identifier getIdentifier() {
        if (identifier == null) {
            identifier = new Identifier(getId(), getType());
        }
        return identifier;
    }

    public abstract void setAttributes(Map<String, Object> attributes);

    public abstract Map<String, Object> getAttributes();

    public void fillData(OddObject oddObject) {
        setAttributes(oddObject.getAttributes());
    }

    public void addRelationship(Relationship relationship) {
        getRelationships().add(relationship);
    }

    @NonNull public List<Relationship> getRelationships() {
        if (relationships == null) {
            relationships = new ArrayList<>();
        }
        return relationships;
    }

    @Nullable Relationship getRelationship(String name) {
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
        for (Relationship relationship : relationships) {
            count += relationship.getIdentifiers().size();
        }
        return count;
    }

    public void addIncluded(OddObject oddObject) {
        getIncluded().add(oddObject);
    }

    @NonNull public List<OddObject> getIncluded() {
        if (included == null) {
            included = new ArrayList<>();
        }
        return included;
    }

    public void setMeta(@Nullable JSONObject jsonObject) {
        this.meta = jsonObject;
    }

    @Nullable public JSONObject getMeta() {
        return meta;
    }

    @NonNull public List<OddObject> getIncludedByType(String type) {
        ArrayList<OddObject> includedOfType = new ArrayList<>();
        for(OddObject oddObject : getIncluded()) {
            if (oddObject.getType().equals(type)) {
                includedOfType.add(oddObject);
            }
        }
        return includedOfType;
    }

    @NonNull public List<OddObject> getIncludedByType(List<String> types) {
        ArrayList<OddObject> includedOfType = new ArrayList<>();
        for(OddObject oddObject : getIncluded()) {
            if (types.indexOf(oddObject.getType()) > -1) {
                includedOfType.add(oddObject);
            }
        }
        return includedOfType;
    }

    @NonNull public List<OddObject> getIncludedByRelationship(String name) {
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
    @Nullable public List<Identifier> getIdentifiersByRelationship(String name) {
        Relationship rel = getRelationship(name);
        return rel == null ? null : rel.getIdentifiers();
    }

    @Nullable public OddObject findIncludedByIdentifier(Identifier identifier) {
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
                "identifier=" + identifier +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", relationships=" + relationships +
                ", included=" + included +
                '}';
    }
}
