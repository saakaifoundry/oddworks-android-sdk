package io.oddworks.device.metric;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class OddViewLoadMetricTest {
    private String orgId = "odd-networks";
    private String contentType = "aThing";
    private String contentId = "thingId";
    private OddViewLoadMetric oddViewLoadMetric;

    @Before
    public void beforeEach() {
        oddViewLoadMetric = new OddViewLoadMetric();
    }

    @Test
	public void testGetType() throws Exception {
        assertEquals("event", oddViewLoadMetric.getType());
    }

    @Test
	public void testGetOrganizationId() throws Exception {
        oddViewLoadMetric.setOrganizationId(orgId);
        assertEquals(orgId, oddViewLoadMetric.getOrganizationId());
    }

    @Test
	public void testGetAction() throws Exception {
        assertEquals(OddAppInitMetric.ACTION_VIEW_LOAD, oddViewLoadMetric.getAction());
    }

    @Test
	public void testGetContentType() throws Exception {
        oddViewLoadMetric.setContentType(contentType);
        assertEquals(contentType, oddViewLoadMetric.getContentType());
    }

    @Test
	public void testGetContentId() throws Exception {
        oddViewLoadMetric.setContentId(contentId);
        assertEquals(contentId, oddViewLoadMetric.getContentId());
    }

    @Test
	public void testToJSONObject() throws Exception {
        OddViewLoadMetric metric = oddViewLoadMetric;
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