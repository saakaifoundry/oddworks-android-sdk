package io.oddworks.device.metric;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class OddVideoErrorMetricTest {
    private String orgId = "odd-networks";
    private String contentType = "aThing";
    private String contentId = "thingId";
    private OddVideoErrorMetric oddVideoErrorMetric;

    @Before
    public void beforeEach() {
        oddVideoErrorMetric = new OddVideoErrorMetric();
    }

    @Test
	public void testGetType() throws Exception {
        assertEquals("event", oddVideoErrorMetric.getType());
    }

    @Test
	public void testGetOrganizationId() throws Exception {
        oddVideoErrorMetric.setOrganizationId(orgId);
        assertEquals(orgId, oddVideoErrorMetric.getOrganizationId());
    }

    @Test
	public void testGetAction() throws Exception {
        assertEquals(OddAppInitMetric.ACTION_VIDEO_ERROR, oddVideoErrorMetric.getAction());
    }

    @Test
	public void testGetContentType() throws Exception {
        oddVideoErrorMetric.setContentType(contentType);
        assertEquals(contentType, oddVideoErrorMetric.getContentType());
    }

    @Test
	public void testGetContentId() throws Exception {
        oddVideoErrorMetric.setContentId(contentId);
        assertEquals(contentId, oddVideoErrorMetric.getContentId());
    }

    @Test
	public void testToJSONObject() throws Exception {
        OddVideoErrorMetric metric = new OddVideoErrorMetric();
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