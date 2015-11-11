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
public class OddVideoErrorMetricTest {
    private String orgId = "odd-networks";
    private String contentType = "aThing";
    private String contentId = "thingId";

    @Test
	public void testGetType() throws Exception {
        OddVideoErrorMetric metric = new OddVideoErrorMetric(orgId, contentType, contentId);
        assertEquals("event", metric.getType());
    }

    @Test
	public void testGetOrganizationId() throws Exception {
        OddVideoErrorMetric metric = new OddVideoErrorMetric(orgId, contentType, contentId);
        assertEquals(orgId, metric.getOrganizationId());
    }

    @Test
	public void testGetAction() throws Exception {
        OddVideoErrorMetric metric = new OddVideoErrorMetric(orgId, contentType, contentId);
        assertEquals(OddAppInitMetric.ACTION_VIDEO_ERROR, metric.getAction());
    }

    @Test
	public void testGetContentType() throws Exception {
        OddVideoErrorMetric metric = new OddVideoErrorMetric(orgId, contentType, contentId);
        assertEquals(contentType, metric.getContentType());
    }

    @Test
	public void testGetContentId() throws Exception {
        OddVideoErrorMetric metric = new OddVideoErrorMetric(orgId, contentType, contentId);
        assertEquals(contentId, metric.getContentId());
    }

    @Test
	public void testToJSONObject() throws Exception {
        OddVideoErrorMetric metric = new OddVideoErrorMetric(orgId, contentType, contentId);
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