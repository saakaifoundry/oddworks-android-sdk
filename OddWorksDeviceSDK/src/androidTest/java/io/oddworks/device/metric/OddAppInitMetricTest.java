package io.oddworks.device.metric;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class OddAppInitMetricTest {
    private String orgId = "odd-networks";

    @Test
	public void testGetType() throws Exception {
        assertEquals("event", OddAppInitMetric.getInstance().getType());
    }

    @Test
	public void testGetOrganizationId() throws Exception {
        OddAppInitMetric.getInstance().setOrganizationId(orgId);
        assertEquals(orgId, OddAppInitMetric.getInstance().getOrganizationId());
    }

    @Test
	public void testGetAction() throws Exception {
        assertEquals(OddAppInitMetric.ACTION_APP_INIT, OddAppInitMetric.getInstance().getAction());
    }

    @Test
	public void testGetContentType() throws Exception {
        assertEquals(null, OddAppInitMetric.getInstance().getContentType());
    }

    @Test
	public void testGetContentId() throws Exception {
        assertEquals(null, OddAppInitMetric.getInstance().getContentId());
    }

    @Test
	public void testToJSONObject() throws Exception {
        OddAppInitMetric metric = (OddAppInitMetric) OddAppInitMetric
                .getInstance()
                .setOrganizationId(orgId);

        String expected = "{" +
                "type: \"" + metric.getType() + "\"," +
                "attributes: {" +
                "organizationId: \"" + metric.getOrganizationId() + "\"," +
                "action: \"" + metric.getAction() + "\"" +
                "}" +
                "}";

        JSONAssert.assertEquals(expected, metric.toJSONObject(), true);
    }
}