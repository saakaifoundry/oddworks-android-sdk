package io.oddworks.device.metric;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class OddViewLoadMetricTest {
    private String orgId = "odd-networks";
    private String contentType = "aThing";
    private String contentId = "thingId";

    @Test
	public void testGetType() throws Exception {
        assertEquals("event", OddViewLoadMetric.getInstance().getType());
    }

    @Test
	public void testGetOrganizationId() throws Exception {
        OddViewLoadMetric.getInstance().setOrganizationId(orgId);
        assertEquals(orgId, OddViewLoadMetric.getInstance().getOrganizationId());
    }

    @Test
	public void testGetAction() throws Exception {
        assertEquals(OddAppInitMetric.ACTION_VIEW_LOAD, OddViewLoadMetric.getInstance().getAction());
    }

    @Test
	public void testGetContentType() throws Exception {
        OddViewLoadMetric.getInstance().setContentType(contentType);
        assertEquals(contentType, OddViewLoadMetric.getInstance().getContentType());
    }

    @Test
	public void testGetContentId() throws Exception {
        OddViewLoadMetric.getInstance().setContentId(contentId);
        assertEquals(contentId, OddViewLoadMetric.getInstance().getContentId());
    }

    @Test
	public void testToJSONObject() throws Exception {
        OddViewLoadMetric metric = OddViewLoadMetric.getInstance();
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