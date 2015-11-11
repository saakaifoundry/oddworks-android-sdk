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
public class OddAppInitMetricTest {
    private String orgId = "odd-networks";

    @Test
	public void testGetType() throws Exception {
        OddAppInitMetric metric = new OddAppInitMetric(orgId);
        assertEquals("event", metric.getType());
    }

    @Test
	public void testGetOrganizationId() throws Exception {
        OddAppInitMetric metric = new OddAppInitMetric(orgId);
        assertEquals(orgId, metric.getOrganizationId());
    }

    @Test
	public void testGetAction() throws Exception {
        OddAppInitMetric metric = new OddAppInitMetric(orgId);
        assertEquals(OddAppInitMetric.ACTION_APP_INIT, metric.getAction());
    }

    @Test
	public void testGetContentType() throws Exception {
        OddAppInitMetric metric = new OddAppInitMetric(orgId);
        assertEquals(null, metric.getContentType());
    }

    @Test
	public void testGetContentId() throws Exception {
        OddAppInitMetric metric = new OddAppInitMetric(orgId);
        assertEquals(null, metric.getContentId());
    }

    @Test
	public void testToJSONObject() throws Exception {
        OddAppInitMetric metric = new OddAppInitMetric(orgId);
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