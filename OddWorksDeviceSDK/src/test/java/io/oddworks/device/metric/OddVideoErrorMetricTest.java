package io.oddworks.device.metric;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

/**
 * Created by brkattk on 10/20/15.
 */
public class OddVideoErrorMetricTest {
    private String orgId = "odd-networks";
    private String contentType = "aThing";
    private String contentId = "thingId";

    @Test
	public void testGetType() throws Exception {
        OddVideoErrorMetric event = new OddVideoErrorMetric(orgId, contentType, contentId);
        assertEquals("event", event.getType());
    }

    @Test
	public void testGetOrganizationId() throws Exception {
        OddVideoErrorMetric event = new OddVideoErrorMetric(orgId, contentType, contentId);
        assertEquals(orgId, event.getOrganizationId());
    }

    @Test
	public void testGetAction() throws Exception {
        OddVideoErrorMetric event = new OddVideoErrorMetric(orgId, contentType, contentId);
        assertEquals(OddAppInitMetric.ACTION_VIDEO_ERROR, event.getAction());
    }

    @Test
	public void testGetContentType() throws Exception {
        OddVideoErrorMetric event = new OddVideoErrorMetric(orgId, contentType, contentId);
        assertEquals(contentType, event.getContentType());
    }

    @Test
	public void testGetContentId() throws Exception {
        OddVideoErrorMetric event = new OddVideoErrorMetric(orgId, contentType, contentId);
        assertEquals(contentId, event.getContentId());
    }

    @Test
	public void testToJSONObject() throws Exception {
        OddVideoErrorMetric event = new OddVideoErrorMetric(orgId, contentType, contentId);
        String expected = "{" +
                "type: \"" + event.getType() + "\"," +
                "attributes: {" +
                "organizationId: \"" + event.getOrganizationId() + "\"," +
                "action: \"" + event.getAction() + "\"," +
                "contentType: \"" + event.getContentType() + "\"," +
                "contentId: \"" + event.getContentId() + "\"" +
                "}" +
                "}";
        JSONAssert.assertEquals(expected, event.toJSONObject(), true);
    }
}