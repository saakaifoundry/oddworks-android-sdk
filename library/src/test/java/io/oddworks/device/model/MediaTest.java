package io.oddworks.device.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by brkattk on 10/22/15.
 */
public class MediaTest {
    private String id = "123";
    private String typeVideo = "video";
    private String title = "Odd the Great and Powerful";
    private String description = "Odd is good! Odd is great!";
    private String releaseDate = "for.ev.er.";
    private String url = "http://oddnetworks.com";
    private int duration = 2345;
    private MediaAds mediaAds = new MediaAds();
    private MediaImage mediaImage = new MediaImage("a", "b", "c", "d", "e");
    private Media video = new Media(id, typeVideo);
    private HashMap<String, Object> attributes = new HashMap<>();

    @Before
    public void beforeEach() {
        attributes.put("title", title);
        attributes.put("description", description);
        attributes.put("releaseDate", releaseDate);
        attributes.put("url", url);
        attributes.put("duration", duration);
        attributes.put("mediaAds", mediaAds);
        attributes.put("mediaImage", mediaImage);
        video.setAttributes(attributes);
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(id, video.getId());
    }

    @Test
    public void testGetType() throws Exception {
        assertEquals(typeVideo, video.getType());
    }

    @Test
    public void testGetTitle() throws Exception {
        assertEquals(title, video.getTitle());
    }

    @Test
    public void testGetDescription() throws Exception {
        assertEquals(description, video.getDescription());
    }

    @Test
    public void testGetReleaseDate() throws Exception {
        assertEquals(releaseDate, video.getReleaseDate());
    }

    @Test
    public void testGetMediaImage() throws Exception {
        assertEquals(mediaImage, video.getMediaImage());
    }

    @Test
    public void testGetDuration() throws Exception {
        assertEquals(Integer.toString(duration), Integer.toString(video.getDuration()));
    }

    @Test
    public void testGetUrl() throws Exception {
        assertEquals(url, video.getUrl());
    }

    @Test
    public void testGetMediaAds() throws Exception {
        assertEquals(mediaAds, video.getMediaAds());
    }

    @Test
    public void testIsLiveTrue() throws Exception {
        Media liveStream = new Media(id, "liveStream");
        liveStream.setAttributes(attributes);
        assertTrue(liveStream.isLive());
    }

    @Test
    public void testIsLiveFalse() throws Exception {
        assertFalse(video.isLive());
    }

    @Test
    public void testSetAttributesWhenNotSet() throws Exception {
        Media media = new Media(id, typeVideo);

        assertEquals(null, media.getTitle());
        assertEquals(null, media.getDescription());
        assertEquals(null, media.getReleaseDate());
        assertEquals(null, media.getMediaImage());
        assertEquals(Integer.toString(0), Integer.toString(media.getDuration()));
        assertEquals(null, media.getUrl());
        assertEquals(null, media.getMediaAds());
    }

    @Test
    public void testSetAttributesWhenSet() throws Exception {
        Media media = new Media(id, typeVideo);
        media.setAttributes(attributes);

        assertEquals(title, media.getTitle());
        assertEquals(description, video.getDescription());
        assertEquals(releaseDate, video.getReleaseDate());
        assertEquals(mediaImage, video.getMediaImage());
        assertEquals(Integer.toString(duration), Integer.toString(video.getDuration()));
        assertEquals(url, video.getUrl());
        assertEquals(mediaAds, video.getMediaAds());
    }

    @Test
    public void testGetAttributes() throws Exception {
        assertEquals(attributes, video.getAttributes());
    }
}