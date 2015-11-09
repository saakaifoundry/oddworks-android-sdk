package io.oddworks.device.metric;


import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

public class OddVideoPlayingMetricTest {
    private String orgId = "odd-networks";
    private String contentType = "aThing";
    private String contentId = "thingId";
    private int elapsed = 456;
    private int duration = 1234;

    @Test
    public void testGetType() throws Exception {
        OddVideoPlayingMetric metric = new OddVideoPlayingMetric(orgId, contentType, contentId, elapsed, duration);
        assertEquals("event", metric.getType());
    }

    @Test
    public void testGetOrganizationId() throws Exception {
        OddVideoPlayingMetric metric = new OddVideoPlayingMetric(orgId, contentType, contentId, elapsed, duration);
        assertEquals(orgId, metric.getOrganizationId());
    }

    @Test
    public void testGetAction() throws Exception {
        OddVideoPlayingMetric metric = new OddVideoPlayingMetric(orgId, contentType, contentId, elapsed, duration);
        assertEquals(OddAppInitMetric.ACTION_VIDEO_PLAYING, metric.getAction());
    }

    @Test
    public void testGetContentType() throws Exception {
        OddVideoPlayingMetric metric = new OddVideoPlayingMetric(orgId, contentType, contentId, elapsed, duration);
        assertEquals(contentType, metric.getContentType());
    }

    @Test
    public void testGetContentId() throws Exception {
        OddVideoPlayingMetric metric = new OddVideoPlayingMetric(orgId, contentType, contentId, elapsed, duration);
        assertEquals(contentId, metric.getContentId());
    }

    @Test
    public void testToJSONObject() throws Exception {
        OddVideoPlayingMetric metric = new OddVideoPlayingMetric(orgId, contentType, contentId, elapsed, duration);
        String expected = "{" +
                "type: \"" + metric.getType() + "\"," +
                "attributes: {" +
                "organizationId: \"" + metric.getOrganizationId() + "\"," +
                "action: \"" + metric.getAction() + "\"," +
                "contentType: \"" + metric.getContentType() + "\"," +
                "contentId: \"" + metric.getContentId() + "\"," +
                "elapsed: " + elapsed + "," +
                "duration: " + duration + "" +
                "}" +
                "}";
        JSONAssert.assertEquals(expected, metric.toJSONObject(), true);
    }
}