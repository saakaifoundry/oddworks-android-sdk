package io.oddworks.device.metric;


import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

/**
 * Created by brkattk on 10/20/15.
 */
public class OddVideoPlayMetricTest {
    private String orgId = "odd-networks";
    private String contentType = "aThing";
    private String contentId = "thingId";

    @Test
	public void testGetType() throws Exception {
        OddVideoPlayMetric event = new OddVideoPlayMetric(orgId, contentType, contentId);
        assertEquals("event", event.getType());
    }

    @Test
	public void testGetOrganizationId() throws Exception {
        OddVideoPlayMetric event = new OddVideoPlayMetric(orgId, contentType, contentId);
        assertEquals(orgId, event.getOrganizationId());
    }

    @Test
	public void testGetAction() throws Exception {
        OddVideoPlayMetric event = new OddVideoPlayMetric(orgId, contentType, contentId);
        assertEquals(OddAppInitMetric.ACTION_VIDEO_PLAY, event.getAction());
    }

    @Test
	public void testGetContentType() throws Exception {
        OddVideoPlayMetric event = new OddVideoPlayMetric(orgId, contentType, contentId);
        assertEquals(contentType, event.getContentType());
    }

    @Test
	public void testGetContentId() throws Exception {
        OddVideoPlayMetric event = new OddVideoPlayMetric(orgId, contentType, contentId);
        assertEquals(contentId, event.getContentId());
    }

    @Test
	public void testToJSONObject() throws Exception {
        OddVideoPlayMetric event = new OddVideoPlayMetric(orgId, contentType, contentId);
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