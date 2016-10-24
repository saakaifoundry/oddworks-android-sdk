package io.oddworks.device.metric;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class OddVideoPlayMetricTest {
    private String contentType = "aThing";
    private String contentId = "thingId";
    private OddVideoPlayMetric oddVideoPlayMetric;

    @Before
    public void beforeEach() {
        oddVideoPlayMetric = new OddVideoPlayMetric(contentType, contentId, null);
    }

    @Test
	public void testGetType() throws Exception {
        assertEquals("event", oddVideoPlayMetric.getType());
    }

    @Test
	public void testGetAction() throws Exception {
        assertEquals(OddMetric.Companion.getACTION_VIDEO_PLAY(), oddVideoPlayMetric.getAction());
    }

    @Test
	public void testGetContentType() throws Exception {
        assertEquals(contentType, oddVideoPlayMetric.getContentType());
    }

    @Test
	public void testGetContentId() throws Exception {
        assertEquals(contentId, oddVideoPlayMetric.getContentId());
    }

    @Test
	public void testToJSONObject() throws Exception {
        String expected = "{\"data\": {" +
                "type: \"" + oddVideoPlayMetric.getType() + "\"," +
                "attributes: {" +
                "action: \"" + oddVideoPlayMetric.getAction() + "\"," +
                "contentType: \"" + oddVideoPlayMetric.getContentType() + "\"," +
                "contentId: \"" + oddVideoPlayMetric.getContentId() + "\"" +
                "}" +
                "}}";
        JSONAssert.assertEquals(expected, oddVideoPlayMetric.toJSONObject(), true);
    }
}