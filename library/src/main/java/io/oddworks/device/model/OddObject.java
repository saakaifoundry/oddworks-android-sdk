package io.oddworks.device.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by brkattk on 10/22/15.
 */
abstract public class OddObject {
    private static final String TAG = OddObject.class.getSimpleName();

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

    public void fillIncludedMediaCollections() {
        ArrayList<String> mediaTypes = new ArrayList<>();
        mediaTypes.add(OddObject.TYPE_LIVE_STREAM);
        mediaTypes.add(OddObject.TYPE_VIDEO);
        for(OddObject mediaCollection : getIncludedByType(OddObject.TYPE_VIDEO_COLLECTION)) {
            for (Relationship relationship : mediaCollection.getRelationships()) {
                for(Identifier identifier : relationship.getIdentifiers()) {
                    for (OddObject media : getIncludedByType(mediaTypes)) {
                        if (media.getId().equals(identifier.getId()) && media.getType().equals(identifier.getType())) {
                            mediaCollection.addIncluded(media);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id='" + getId() + "', " +
                "type='" + getType() + "')";
    }
}
