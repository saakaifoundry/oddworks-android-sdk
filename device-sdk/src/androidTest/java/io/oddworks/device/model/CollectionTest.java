package io.oddworks.device.model;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class CollectionTest {
    private String id = "123";
    private String type = "videoCollection";
    private String title = "Odd the Great and Powerful";
    private String subtitle = null;
    private String description = "Odd is good! Odd is great!";
    private DateTime releaseDate = new DateTime("2015-04-20T16:20:00-0400");
    private MediaImage mediaImage = new MediaImage("a", "b", 1, 1, "e");
    private OddCollection collection = new OddCollection(id, type);
    private Map<String, Object> attributes = new HashMap<>();

    @Before
    public void beforeEach() {
        attributes.put("title", title);
        attributes.put("subtitle", subtitle);
        attributes.put("description", description);
        attributes.put("releaseDate", releaseDate);
        ArrayList<MediaImage> images = new ArrayList<>();
        images.add(mediaImage);
        attributes.put("images", images);
        collection.setAttributes(attributes);
    }

    @Test
    public void testGetIdentifier() throws Exception {
        assertThat(collection.getIdentifier(), isA(Identifier.class));
    }

    @Test
    public void testGetId() throws Exception {
        assertThat(collection.getId(), is(equalTo(id)));
    }

    @Test
    public void testGetType() throws Exception {
        assertThat(collection.getType(), is(equalTo(type)));
    }

    @Test
    public void testGetTitle() throws Exception {
        assertThat(collection.getTitle(), is(equalTo(title)));
    }

    @Test
    public void testGetDescription() throws Exception {
        assertThat(collection.getDescription(), is(equalTo(description)));
    }

    @Test
    public void testGetReleaseDate() throws Exception {
        assertThat(collection.getReleaseDate().toString(), is(equalTo(releaseDate.toString())));
    }

    @Test
    public void testGetImages() throws Exception {
        ArrayList<MediaImage> images = new ArrayList<>();
        images.add(mediaImage);
        assertEquals(images, collection.getImages());
    }

    @Test
    public void testSetAttributesWhenNotSet() throws Exception {
        OddCollection collection = new OddCollection(id, type);

        assertThat(collection.getTitle(), is(nullValue()));
        assertThat(collection.getDescription(), is(nullValue()));
        assertThat(collection.getReleaseDate(), is(is(nullValue())));
        assertThat(collection.getImages(), is(nullValue()));
    }

    @Test
    public void testSetAttributesWhenSet() throws Exception {
        OddCollection collection = new OddCollection(id, type);
        collection.setAttributes(attributes);

        assertThat(collection.getTitle(), is(equalTo(title)));
        assertThat(collection.getDescription(), is(equalTo(description)));
        assertThat(collection.getReleaseDate().toString(), is(equalTo(releaseDate.toString())));
        ArrayList<MediaImage> images = new ArrayList<>();
        images.add(mediaImage);
        assertEquals(images, collection.getImages());
    }

    @Test
    public void testGetAttributes() throws Exception {
        assertThat(collection.getAttributes(), is(equalTo(attributes)));
    }

    @Test
    public void testFillData() throws Exception {
        Map<String, Object> otherAttributes = new HashMap<>();
        otherAttributes.put("title", "AAA");
        otherAttributes.put("subtitle", null);
        otherAttributes.put("description", "ZZZ");
        otherAttributes.put("releaseDate", new DateTime("2012-11-13T09:30:00+0100"));
        otherAttributes.put("mediaImage", new MediaImage("e", "d", 1, 1, "a"));

        OddCollection collection2 = new OddCollection("1", "type");
        collection2.setAttributes(otherAttributes);

        collection.fillData(collection2);

        assertThat(collection.getAttributes(), is(equalTo(otherAttributes)));
    }

    @Test
    public void testAddRelationshipGetRelationships() throws Exception {
        Relationship rel = new Relationship("rel", new ArrayList<Identifier>());

        collection.addRelationship(rel);

        List<Relationship> expected = new ArrayList<>();
        expected.add(rel);

        assertThat(collection.getRelationships(), is(equalTo(expected)));
    }

    @Test
    public void testGetRelationship() throws Exception {
        Relationship rel = new Relationship("rel", new ArrayList<Identifier>());
        Relationship expected = new Relationship("rel2", new ArrayList<Identifier>());

        collection.addRelationship(rel);
        collection.addRelationship(expected);

        assertThat(collection.getRelationship("rel2"), is(equalTo(expected)));
    }

    @Test
    public void testAddIncludedGetIncluded() throws Exception {
        Media media = new Media("1", "video");

        List<OddObject> expected = new ArrayList<>();
        assertThat(collection.getIncluded(), is(equalTo(expected)));

        collection.addIncluded(media);
        expected.add(media);
        assertThat(collection.getIncluded(), is(equalTo(expected)));
    }

    @Test
    public void testGetIncludedByTypeString() throws Exception {
        Media video = new Media("1", "video");
        Media live = new Media("2", "liveStream");

        collection.addIncluded(video);
        collection.addIncluded(live);

        List<OddObject> expected = new ArrayList<>();
        expected.add(live);

        assertThat(collection.getIncludedByType("liveStream"), is(equalTo(expected)));
    }

    @Test
    public void testGetIncludedByTypeArrayList() throws Exception {
        Media video = new Media("1", "video");
        Media live = new Media("2", "liveStream");

        collection.addIncluded(video);
        collection.addIncluded(live);

        List<OddObject> expected = new ArrayList<>();
        expected.add(video);
        expected.add(live);

        ArrayList<String> types = new ArrayList<>();
        types.add("video");
        types.add("liveStream");

        assertThat(collection.getIncludedByType(types), is(equalTo(expected)));
    }

    @Test
    public void testGetIncludedByRelationshipNotPresent() throws Exception {
        List<OddObject> expected = new ArrayList<>();

        assertThat(collection.getIncludedByRelationship("liveStrema"), is(equalTo(expected)));
    }

    @Test
    public void testGetIncludedByRelationship() throws Exception {
        Identifier videoId = new Identifier("1", "video");
        Media video = new Media(videoId);
        Identifier liveId = new Identifier("2", "liveStream");
        Media live = new Media(liveId);

        collection.addIncluded(video);
        collection.addIncluded(live);

        Relationship videoRel = new Relationship("videos");
        videoRel.addIdentifier(videoId);
        collection.addRelationship(videoRel);

        Relationship liveRel = new Relationship("liveStreams");
        liveRel.addIdentifier(liveId);
        collection.addRelationship(liveRel);


        List<OddObject> expected = new ArrayList<>();
        expected.add(video);

        assertThat(collection.getIncludedByRelationship("videos"), is(equalTo(expected)));
    }

    @Test
    public void testFindIncludedByIdentifier() throws Exception {
        Identifier videoId = new Identifier("1", "video");
        Media video = new Media(videoId);
        Identifier liveId = new Identifier("2", "liveStream");
        Media live = new Media(liveId);

        collection.addIncluded(video);
        collection.addIncluded(live);

        assertThat(collection.findIncludedByIdentifier(videoId), is(equalTo((OddObject) video)));
    }

    @Test
    public void testFindIncludedByIdentifierNotPresent() throws Exception {
        Identifier videoId = new Identifier("1", "video");

        assertThat(collection.findIncludedByIdentifier(videoId), is(nullValue()));
    }
}