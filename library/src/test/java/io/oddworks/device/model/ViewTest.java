package io.oddworks.device.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by brkattk on 10/20/15.
 */
public class ViewTest {
    private String id = "theId";
    private String type = View.class.getSimpleName();
    private String title = "Android View";
    private String relationshipName = "viewRel";
    private View view = new View(id, type);

    @Before
    public void beforeEach() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("title", title);
        view.setAttributes(attributes);
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(id, view.getId());
    }

    @Test
	public void testGetType() throws Exception {
        assertEquals(type, view.getType());
    }

    @Test
	public void testGetTitle() throws Exception {
        assertEquals(title, view.getTitle());
    }

    @Test
	public void testGetRelationship_when_not_present() throws Exception {
        assertEquals(null, view.getRelationship(relationshipName));
    }

    @Test
	public void testGetRelationship() throws Exception {
        Relationship expected = new Relationship("rel", new ArrayList<Identifier>());
        view.addRelationship(expected);

        assertEquals(expected, view.getRelationship("rel"));
    }

    @Test
	public void testGetRelationships() throws Exception {
        Relationship relationship = new Relationship(relationshipName, new ArrayList<Identifier>());
        ArrayList<Relationship> expected = new ArrayList<>();
        expected.add(relationship);
        view.addRelationship(relationship);

        assertEquals(expected, view.getRelationships());
    }

    @Test
	public void testAddRelationship() throws Exception {
        Relationship expected = new Relationship(relationshipName, new ArrayList<Identifier>());
        view.addRelationship(expected);
        assertEquals(expected, view.getRelationship(relationshipName));
    }

    @Test
	public void testAddIncluded() throws Exception {
        Identifier mediaIdentifier = new Identifier(id, type);
        Media expected = new Media(mediaIdentifier);
        view.addIncluded(expected);
        assertEquals(expected, view.findIncludedByIdentifier(mediaIdentifier));
    }

    @Test
	public void testGetIncluded_when_not_present() throws Exception {
        assertEquals(new ArrayList<>(), view.getIncluded());
    }

    @Test
	public void testGetIncluded() throws Exception {
        Media media = new Media(id, type);
        view.addIncluded(media);

        ArrayList<OddObject> expected = new ArrayList<>();
        expected.add(media);

        assertEquals(expected, view.getIncluded());
    }

    @Test
	public void testGetIncludedByType() throws Exception {
        Media media = new Media("foo", OddObject.TYPE_VIDEO);
        MediaCollection mediaCollection = new MediaCollection("foo", OddObject.TYPE_VIDEO_COLLECTION);

        view.addIncluded(media);
        view.addIncluded(mediaCollection);
        assertEquals(2, view.getIncluded().size());

        ArrayList<OddObject> expected = new ArrayList<>();
        expected.add(media);

        assertEquals(expected, view.getIncludedByType(OddObject.TYPE_VIDEO));
    }

    @Test
	public void testFillIncludedMediaCollections() throws Exception {
        String mcId = "12345";
        String mcType = "videoCollection";
        Identifier mcIdentifier = new Identifier(mcId, mcType);

        MediaCollection mediaCollection = new MediaCollection(mcIdentifier);

        ArrayList<Identifier> mcIdentifiers = new ArrayList<>();
        mcIdentifiers.add(mcIdentifier);

        String mId = "67890";
        String mType = "video";
        Identifier mIdentifier = new Identifier(mId, mType);
        Media media = new Media(mIdentifier);

        ArrayList<Identifier> mIdentifiers = new ArrayList<>();
        mIdentifiers.add(mIdentifier);
        mediaCollection.addRelationship(new Relationship("videos", mIdentifiers));

        view.addRelationship(new Relationship("featured", mcIdentifiers));
        view.addIncluded(mediaCollection);
        view.addIncluded(media);


        MediaCollection foundMediaCollection = (MediaCollection) view.getIncludedByRelationship("featured").get(0);
        assertEquals(0, foundMediaCollection.getIncludedByRelationship("videos").size());

        view.fillIncludedMediaCollections();
        assertEquals(1, foundMediaCollection.getIncludedByRelationship("videos").size());
    }
}