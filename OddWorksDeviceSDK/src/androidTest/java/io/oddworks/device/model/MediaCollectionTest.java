package io.oddworks.device.model;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by brkattk on 10/22/15.
 */
@RunWith(AndroidJUnit4.class)
public class MediaCollectionTest {
    private String id = "123";
    private String type = "videoCollection";
    private String title = "Odd the Great and Powerful";
    private String description = "Odd is good! Odd is great!";
    private String releaseDate = "for.ev.er.";
    private MediaImage mediaImage = new MediaImage("a", "b", "c", "d", "e");
    private MediaCollection mediaCollection = new MediaCollection(id, type);
    private HashMap<String, Object> attributes = new HashMap<>();

    @Before
    public void beforeEach() {
        attributes.put("title", title);
        attributes.put("description", description);
        attributes.put("releaseDate", releaseDate);
        attributes.put("mediaImage", mediaImage);
        mediaCollection.setAttributes(attributes);
    }

    @Test
    public void testGetIdentifier() throws Exception {
        assertThat(mediaCollection.getIdentifier(), isA(Identifier.class));
    }

    @Test
    public void testGetId() throws Exception {
        assertThat(mediaCollection.getId(), is(equalTo(id)));
    }

    @Test
    public void testGetType() throws Exception {
        assertThat(mediaCollection.getType(), is(equalTo(type)));
    }

    @Test
    public void testGetTitle() throws Exception {
        assertThat(mediaCollection.getTitle(), is(equalTo(title)));
    }

    @Test
    public void testGetDescription() throws Exception {
        assertThat(mediaCollection.getDescription(), is(equalTo(description)));
    }

    @Test
    public void testGetReleaseDate() throws Exception {
        assertThat(mediaCollection.getReleaseDate(), is(equalTo(releaseDate)));
    }

    @Test
    public void testGetMediaImage() throws Exception {
        assertThat(mediaCollection.getMediaImage(), is(equalTo(mediaImage)));
    }

    @Test
    public void testSetAttributesWhenNotSet() throws Exception {
        MediaCollection mediaCollection = new MediaCollection(id, type);

        assertThat(mediaCollection.getTitle(), is(nullValue()));
        assertThat(mediaCollection.getDescription(), is(nullValue()));
        assertThat(mediaCollection.getReleaseDate(), is(is(nullValue())));
        assertThat(mediaCollection.getMediaImage(), is(nullValue()));
    }

    @Test
    public void testSetAttributesWhenSet() throws Exception {
        MediaCollection mediaCollection = new MediaCollection(id, type);
        mediaCollection.setAttributes(attributes);

        assertThat(mediaCollection.getTitle(), is(equalTo(title)));
        assertThat(mediaCollection.getDescription(), is(equalTo(description)));
        assertThat(mediaCollection.getReleaseDate(), is(equalTo(releaseDate)));
        assertThat(mediaCollection.getMediaImage(), is(equalTo(mediaImage)));
    }

    @Test
    public void testGetAttributes() throws Exception {
        assertThat(mediaCollection.getAttributes(), is(equalTo(attributes)));
    }

    @Test
    public void testFillData() throws Exception {
        HashMap<String, Object> otherAttributes = new HashMap<>();
        otherAttributes.put("title", "AAA");
        otherAttributes.put("description", "ZZZ");
        otherAttributes.put("releaseDate", "XXX");
        otherAttributes.put("mediaImage", new MediaImage("e", "d", "c", "b", "a"));

        MediaCollection mediaCollection2 = new MediaCollection("1", "type");
        mediaCollection2.setAttributes(otherAttributes);

        mediaCollection.fillData(mediaCollection2);

        assertThat(mediaCollection.getAttributes(), is(equalTo(otherAttributes)));
    }

    @Test
    public void testAddRelationshipGetRelationships() throws Exception {
        Relationship rel = new Relationship("rel", new ArrayList<Identifier>());

        mediaCollection.addRelationship(rel);

        ArrayList<Relationship> expected = new ArrayList<>();
        expected.add(rel);

        assertThat(mediaCollection.getRelationships(), is(equalTo(expected)));
    }

    @Test
    public void testGetRelationship() throws Exception {
        Relationship rel = new Relationship("rel", new ArrayList<Identifier>());
        Relationship expected = new Relationship("rel2", new ArrayList<Identifier>());

        mediaCollection.addRelationship(rel);
        mediaCollection.addRelationship(expected);

        assertThat(mediaCollection.getRelationship("rel2"), is(equalTo(expected)));
    }

    @Test
    public void testAddIncludedGetIncluded() throws Exception {
        Media media = new Media("1", "video");

        ArrayList<OddObject> expected = new ArrayList<>();
        assertThat(mediaCollection.getIncluded(), is(equalTo(expected)));

        mediaCollection.addIncluded(media);
        expected.add(media);
        assertThat(mediaCollection.getIncluded(), is(equalTo(expected)));
    }

    @Test
    public void testGetIncludedByTypeString() throws Exception {
        Media video = new Media("1", "video");
        Media live = new Media("2", "liveStream");

        mediaCollection.addIncluded(video);
        mediaCollection.addIncluded(live);

        ArrayList<OddObject> expected = new ArrayList<>();
        expected.add(live);

        assertThat(mediaCollection.getIncludedByType("liveStream"), is(equalTo(expected)));
    }

    @Test
    public void testGetIncludedByTypeArrayList() throws Exception {
        Media video = new Media("1", "video");
        Media live = new Media("2", "liveStream");

        mediaCollection.addIncluded(video);
        mediaCollection.addIncluded(live);

        ArrayList<OddObject> expected = new ArrayList<>();
        expected.add(video);
        expected.add(live);

        ArrayList<String> types = new ArrayList<>();
        types.add("video");
        types.add("liveStream");

        assertThat(mediaCollection.getIncludedByType(types), is(equalTo(expected)));
    }

    @Test
    public void testGetIncludedByRelationshipNotPresent() throws Exception {
        ArrayList<OddObject> expected = new ArrayList<>();

        assertThat(mediaCollection.getIncludedByRelationship("liveStrema"), is(equalTo(expected)));
    }

    @Test
    public void testGetIncludedByRelationship() throws Exception {
        Identifier videoId = new Identifier("1", "video");
        Media video = new Media(videoId);
        Identifier liveId = new Identifier("2", "liveStream");
        Media live = new Media(liveId);

        mediaCollection.addIncluded(video);
        mediaCollection.addIncluded(live);

        Relationship videoRel = new Relationship("videos");
        videoRel.addIdentifier(videoId);
        mediaCollection.addRelationship(videoRel);

        Relationship liveRel = new Relationship("liveStreams");
        liveRel.addIdentifier(liveId);
        mediaCollection.addRelationship(liveRel);


        ArrayList<OddObject> expected = new ArrayList<>();
        expected.add(video);

        assertThat(mediaCollection.getIncludedByRelationship("videos"), is(equalTo(expected)));
    }

    @Test
    public void testFindIncludedByIdentifier() throws Exception {
        Identifier videoId = new Identifier("1", "video");
        Media video = new Media(videoId);
        Identifier liveId = new Identifier("2", "liveStream");
        Media live = new Media(liveId);

        mediaCollection.addIncluded(video);
        mediaCollection.addIncluded(live);

        assertThat(mediaCollection.findIncludedByIdentifier(videoId), is(equalTo((OddObject) video)));
    }

    @Test
    public void testFindIncludedByIdentifierNotPresent() throws Exception {
        Identifier videoId = new Identifier("1", "video");

        assertThat(mediaCollection.findIncludedByIdentifier(videoId), is(nullValue()));
    }

    @Test
    public void isPresentableShouldReturnTrue() {
        assertThat(mediaCollection.isPresentable(), is(true));
    }

    @Test
    public void toPresentableFieldsShouldEqualMediasFields() {
        Presentable presentable = mediaCollection.toPresentable();

        assertThat("titles should match", presentable.getTitle(), is(equalTo(mediaCollection.getTitle())));
        assertThat("descriptions should match", presentable.getDescription(),
                is(equalTo(mediaCollection.getDescription())));
        assertThat("MediaImages should match", presentable.getMediaImage(),
                is(equalTo(mediaCollection.getMediaImage())));
    }
}