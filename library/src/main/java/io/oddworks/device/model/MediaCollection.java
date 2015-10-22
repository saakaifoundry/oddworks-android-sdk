package io.oddworks.device.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by brkattk on 9/21/15.
 */
public class MediaCollection extends OddMediaObject {
    private static final String TAG = MediaCollection.class.getSimpleName();

    public MediaCollection(final Identifier identifier) {
        super(identifier);
    }

    public MediaCollection(final String id, final String type) {
      super(id, type);
    }

    @Override
    public void setAttributes(HashMap<String, Object> attributes) {
        mTitle = (String) attributes.get("title");
        mDescription = (String) attributes.get("description");
        mMediaImage = (MediaImage) attributes.get("mediaImage");
        mReleaseDate = (String) attributes.get("releaseDate");
    }

    @Override
    public HashMap<String, Object> getAttributes() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("title", getTitle());
        attributes.put("description", getDescription());
        attributes.put("mediaImage", getMediaImage());
        attributes.put("releaseDate", getReleaseDate());
        return attributes;
    }

    @Override
    public String toString() {
        return TAG + "(" +
                "id='" + getId() + "', " +
                "type='" + getType() + "', " +
                "title='" + getTitle() + "')";
    }
}
