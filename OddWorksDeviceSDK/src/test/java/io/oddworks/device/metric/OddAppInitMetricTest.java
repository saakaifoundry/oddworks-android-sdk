package io.oddworks.device.metric;


import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

/**
 * Created by brkattk on 10/20/15.
 */
public class OddAppInitMetricTest {
    private String orgId = "odd-networks";

    @Test
	public void testGetType() throws Exception {
        OddAppInitMetric event = new OddAppInitMetric(orgId);
        assertEquals("event", event.getType());
    }

    @Test
	public void testGetOrganizationId() throws Exception {
        OddAppInitMetric event = new OddAppInitMetric(orgId);
        assertEquals(orgId, event.getOrganizationId());
    }

    @Test
	public void testGetAction() throws Exception {
        OddAppInitMetric event = new OddAppInitMetric(orgId);
        assertEquals(OddAppInitMetric.ACTION_APP_INIT, event.getAction());
    }

    @Test
	public void testGetContentType() throws Exception {
        OddAppInitMetric event = new OddAppInitMetric(orgId);
        assertEquals(null, event.getContentType());
    }

    @Test
	public void testGetContentId() throws Exception {
        OddAppInitMetric event = new OddAppInitMetric(orgId);
        assertEquals(null, event.getContentId());
    }

    @Test
	public void testToJSONObject() throws Exception {
        OddAppInitMetric event = new OddAppInitMetric(orgId);
        String expected = "{" +
                "type: \"" + event.getType() + "\"," +
                "attributes: {" +
                "organizationId: \"" + event.getOrganizationId() + "\"," +
                "action: \"" + event.getAction() + "\"" +
                "}" +
                "}";
        JSONAssert.assertEquals(expected, event.toJSONObject(), true);
    }
}