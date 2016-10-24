package io.oddworks.device.metric;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class OddVideoErrorMetricTest {
    private String contentType = "aThing";
    private String contentId = "thingId";
    private OddVideoErrorMetric oddVideoErrorMetric;

    @Before
    public void beforeEach() {
        oddVideoErrorMetric = new OddVideoErrorMetric(contentType, contentId, null);
    }

    @Test
	public void testGetType() throws Exception {
        assertEquals("event", oddVideoErrorMetric.getType());
    }
    @Test
	public void testGetAction() throws Exception {
        assertEquals(OddMetric.Companion.getACTION_VIDEO_ERROR(), oddVideoErrorMetric.getAction());
    }

    @Test
	public void testGetContentType() throws Exception {
        assertEquals(contentType, oddVideoErrorMetric.getContentType());
    }

    @Test
	public void testGetContentId() throws Exception {
        assertEquals(contentId, oddVideoErrorMetric.getContentId());
    }

    @Test
	public void testToJSONObject() throws Exception {
        OddVideoErrorMetric metric = new OddVideoErrorMetric(contentType, contentId, null);

        String expected = "{\"data\": {" +
                "type: \"" + metric.getType() + "\"," +
                "attributes: {" +
                "action: \"" + metric.getAction() + "\"," +
                "contentType: \"" + metric.getContentType() + "\"," +
                "contentId: \"" + metric.getContentId() + "\"" +
                "}" +
                "}}";

        JSONAssert.assertEquals(expected, metric.toJSONObject(), true);
    }
}