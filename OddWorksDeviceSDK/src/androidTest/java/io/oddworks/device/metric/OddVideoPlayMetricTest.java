package io.oddworks.device.metric;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

/**
 * Created by brkattk on 10/20/15.
 */
@RunWith(AndroidJUnit4.class)
public class OddVideoPlayMetricTest {
    private String orgId = "odd-networks";
    private String contentType = "aThing";
    private String contentId = "thingId";

    @Test
	public void testGetType() throws Exception {
        OddVideoPlayMetric metric = new OddVideoPlayMetric(orgId, contentType, contentId);
        assertEquals("event", metric.getType());
    }

    @Test
	public void testGetOrganizationId() throws Exception {
        OddVideoPlayMetric metric = new OddVideoPlayMetric(orgId, contentType, contentId);
        assertEquals(orgId, metric.getOrganizationId());
    }

    @Test
	public void testGetAction() throws Exception {
        OddVideoPlayMetric metric = new OddVideoPlayMetric(orgId, contentType, contentId);
        assertEquals(OddAppInitMetric.ACTION_VIDEO_PLAY, metric.getAction());
    }

    @Test
	public void testGetContentType() throws Exception {
        OddVideoPlayMetric metric = new OddVideoPlayMetric(orgId, contentType, contentId);
        assertEquals(contentType, metric.getContentType());
    }

    @Test
	public void testGetContentId() throws Exception {
        OddVideoPlayMetric metric = new OddVideoPlayMetric(orgId, contentType, contentId);
        assertEquals(contentId, metric.getContentId());
    }

    @Test
	public void testToJSONObject() throws Exception {
        OddVideoPlayMetric metric = new OddVideoPlayMetric(orgId, contentType, contentId);
        String expected = "{" +
                "type: \"" + metric.getType() + "\"," +
                "attributes: {" +
                "organizationId: \"" + metric.getOrganizationId() + "\"," +
                "action: \"" + metric.getAction() + "\"," +
                "contentType: \"" + metric.getContentType() + "\"," +
                "contentId: \"" + metric.getContentId() + "\"" +
                "}" +
                "}";
        JSONAssert.assertEquals(expected, metric.toJSONObject(), true);
    }
}