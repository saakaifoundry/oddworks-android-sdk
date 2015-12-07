package io.oddworks.device.metric;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class OddVideoPlayMetricTest {
    private String orgId = "odd-networks";
    private String contentType = "aThing";
    private String contentId = "thingId";

    @Test
	public void testGetType() throws Exception {
        assertEquals("event", OddVideoPlayMetric.getInstance().getType());
    }

    @Test
	public void testGetOrganizationId() throws Exception {
        OddVideoPlayMetric.getInstance().setOrganizationId(orgId);
        assertEquals(orgId, OddVideoPlayMetric.getInstance().getOrganizationId());
    }

    @Test
	public void testGetAction() throws Exception {
        assertEquals(OddAppInitMetric.ACTION_VIDEO_PLAY, OddVideoPlayMetric.getInstance().getAction());
    }

    @Test
	public void testGetContentType() throws Exception {
        OddVideoPlayMetric.getInstance().setContentType(contentType);
        assertEquals(contentType, OddVideoPlayMetric.getInstance().getContentType());
    }

    @Test
	public void testGetContentId() throws Exception {
        OddVideoPlayMetric.getInstance().setContentId(contentId);
        assertEquals(contentId, OddVideoPlayMetric.getInstance().getContentId());
    }

    @Test
	public void testToJSONObject() throws Exception {
        OddVideoPlayMetric metric = OddVideoPlayMetric.getInstance();
        metric.setOrganizationId(orgId);
        metric.setContentType(contentType);
        metric.setContentId(contentId);
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