package io.oddworks.device.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ExternalTest {
    private String id = "123";
    private String typeExternal = "external";
    private External external = new External(id, typeExternal);
    private String title = "Odd the Great and Powerful";
    private String description = "Odd is good! Odd is great!";
    private String url = "http://oddnetworks.com/foo.html";
    private MediaImage mediaImage = new MediaImage("a", "b", 1, 1, "e");
    private HashMap<String, Object> attributes = new HashMap<>();

    @Before
    public void beforeEach() {
        attributes.put("title", title);
        attributes.put("description", description);
        ArrayList<MediaImage> images = new ArrayList<>();
        images.add(mediaImage);
        attributes.put("images", images);
        attributes.put("url", url);
        attributes.put("mediaImage", mediaImage);
        external.setAttributes(attributes);
    }

    @Test
    public void testGetIdentifier() throws Exception {
        assertThat(external.getIdentifier(), is(instanceOf(Identifier.class)));
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(id, external.getId());
    }

    @Test
    public void testGetType() throws Exception {
        assertEquals(typeExternal, external.getType());
    }

    @Test
    public void testGetTitle() throws Exception {
        assertEquals(title, external.getTitle());
    }

    @Test
    public void testGetDescription() throws Exception {
        assertEquals(description, external.getDescription());
    }

    @Test
    public void testGetImages() throws Exception {
        ArrayList<MediaImage> images = new ArrayList<>();
        images.add(mediaImage);
        assertEquals(images, external.getImages());
    }

    @Test
    public void testGetUrl() throws Exception {
        assertEquals(url, external.getUrl());
    }

    @Test
    public void testSetAttributesWhenNotSet() throws Exception {
        Article article = new Article(id, typeExternal);

        assertEquals(null, article.getTitle());
        assertEquals(null, article.getDescription());
        assertEquals(null, article.getImages());
        assertEquals(null, article.getUrl());
    }

    @Test
    public void testSetAttributesWhenSet() throws Exception {
        Article article = new Article(id, typeExternal);
        article.setAttributes(attributes);

        assertEquals(title, article.getTitle());
        assertEquals(description, article.getDescription());
        ArrayList<MediaImage> images = new ArrayList<>();
        images.add(mediaImage);
        assertEquals(images, article.getImages());
        assertEquals(url, article.getUrl());
    }

    @Test
    public void testGetAttributes() throws Exception {
        assertEquals(attributes, external.getAttributes());
    }
}
