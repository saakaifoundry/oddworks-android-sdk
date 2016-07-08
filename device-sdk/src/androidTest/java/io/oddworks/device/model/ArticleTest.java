package io.oddworks.device.model;

import android.support.test.runner.AndroidJUnit4;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ArticleTest {
    private String id = "123";
    private String typeArticle = "article";
    private Article article = new Article(id, typeArticle);
    private String title = "Odd the Great and Powerful";
    private String description = "Odd is good! Odd is great!";
    private String category = "categorically";
    private String source = "asourceisasourceofcourseofcourse";
    private String url = "http://oddnetworks.com/foo.html";
    private DateTime createdAt = new DateTime("2015-12-01T01:45:21Z");
    private MediaImage mediaImage = new MediaImage("a", "b", 1, 1, "e");
    private HashMap<String, Object> attributes = new HashMap<>();

    @Before
    public void beforeEach() {
        attributes.put("title", title);
        attributes.put("description", description);
        attributes.put("url", url);
        attributes.put("category", category);
        attributes.put("source", source);
        ArrayList<MediaImage> images = new ArrayList<>();
        images.add(mediaImage);
        attributes.put("images", images);
        attributes.put("createdAt", createdAt);
        article.setAttributes(attributes);
    }

    @Test
    public void testGetIdentifier() throws Exception {
        assertThat(article.getIdentifier(), is(instanceOf(Identifier.class)));
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(id, article.getId());
    }

    @Test
    public void testGetType() throws Exception {
        assertEquals(typeArticle, article.getType());
    }

    @Test
    public void testGetTitle() throws Exception {
        assertEquals(title, article.getTitle());
    }

    @Test
    public void testGetDescription() throws Exception {
        assertEquals(description, article.getDescription());
    }

    @Test
    public void testGetImages() throws Exception {
        ArrayList<MediaImage> images = new ArrayList<>();
        images.add(mediaImage);
        assertEquals(images, article.getImages());
    }

    @Test
    public void testGetUrl() throws Exception {
        assertEquals(url, article.getUrl());
    }

    @Test
    public void testGetCategory() throws Exception {
        assertEquals(category, article.getCategory());
    }

    @Test
    public void testGetSource() throws Exception {
        assertEquals(source, article.getSource());
    }

    @Test
    public void testGetCreatedAt() throws Exception {
        assertEquals(createdAt, article.getCreatedAt());
    }

    @Test
    public void testSetAttributesWhenNotSet() throws Exception {
        Article article = new Article(id, typeArticle);

        assertEquals(null, article.getTitle());
        assertEquals(null, article.getDescription());
        assertEquals(null, article.getImages());
        assertEquals(null, article.getUrl());
        assertEquals(null, article.getCategory());
        assertEquals(null, article.getSource());
        assertEquals(null, article.getCreatedAt());
    }

    @Test
    public void testSetAttributesWhenSet() throws Exception {
        Article article = new Article(id, typeArticle);
        article.setAttributes(attributes);

        assertEquals(title, article.getTitle());
        assertEquals(description, article.getDescription());
        ArrayList<MediaImage> images = new ArrayList<>();
        images.add(mediaImage);
        assertEquals(images, article.getImages());
        assertEquals(url, article.getUrl());
        assertEquals(category, article.getCategory());
        assertEquals(source, article.getSource());
        assertEquals(createdAt, article.getCreatedAt());
    }

    @Test
    public void testGetAttributes() throws Exception {
        assertEquals(attributes, article.getAttributes());
    }
}
