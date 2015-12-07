package io.oddworks.device.metric;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class OddVideoErrorMetricTest {
    private String orgId = "odd-networks";
    private String contentType = "aThing";
    private String contentId = "thingId";

    @Test
	public void testGetType() throws Exception {
        assertEquals("event", OddVideoErrorMetric.getInstance().getType());
    }

    @Test
	public void testGetOrganizationId() throws Exception {
        OddVideoErrorMetric.getInstance().setOrganizationId(orgId);
        assertEquals(orgId, OddVideoErrorMetric.getInstance().getOrganizationId());
    }

    @Test
	public void testGetAction() throws Exception {
        assertEquals(OddAppInitMetric.ACTION_VIDEO_ERROR, OddVideoErrorMetric.getInstance().getAction());
    }

    @Test
	public void testGetContentType() throws Exception {
        OddVideoErrorMetric.getInstance().setContentType(contentType);
        assertEquals(contentType, OddVideoErrorMetric.getInstance().getContentType());
    }

    @Test
	public void testGetContentId() throws Exception {
        OddVideoErrorMetric.getInstance().setContentId(contentId);
        assertEquals(contentId, OddVideoErrorMetric.getInstance().getContentId());
    }

    @Test
	public void testToJSONObject() throws Exception {
        OddVideoErrorMetric metric = (OddVideoErrorMetric) OddVideoErrorMetric
                .getInstance()
                .setOrganizationId(orgId)
                .setContentType(contentType)
                .setContentId(contentId);

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