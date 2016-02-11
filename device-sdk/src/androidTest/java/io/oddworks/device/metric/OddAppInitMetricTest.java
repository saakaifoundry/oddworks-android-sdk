package io.oddworks.device.metric;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class OddAppInitMetricTest {
    private String orgId = "odd-networks";
    private OddAppInitMetric oddAppInitMetric;

    @Before
    public void beforeEach() {
        oddAppInitMetric = new OddAppInitMetric();
    }

    @Test
	public void testGetType() throws Exception {
        assertEquals("event", oddAppInitMetric.getType());
    }

    @Test
	public void testGetOrganizationId() throws Exception {
        oddAppInitMetric.setOrganizationId(orgId);
        assertEquals(orgId, oddAppInitMetric.getOrganizationId());
    }

    @Test
	public void testGetAction() throws Exception {
        assertEquals(OddAppInitMetric.ACTION_APP_INIT, oddAppInitMetric.getAction());
    }

    @Test
	public void testGetContentType() throws Exception {
        assertEquals(null, oddAppInitMetric.getContentType());
    }

    @Test
	public void testGetContentId() throws Exception {
        assertEquals(null, oddAppInitMetric.getContentId());
    }

    @Test
	public void testToJSONObject() throws Exception {
        OddAppInitMetric metric = new OddAppInitMetric();
        metric.setOrganizationId(orgId);

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