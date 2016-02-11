package io.oddworks.device.metric;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class OddVideoPlayMetricTest {
    private String orgId = "odd-networks";
    private String contentType = "aThing";
    private String contentId = "thingId";
    private OddVideoPlayMetric oddVideoPlayMetric;

    @Before
    public void beforeEach() {
        oddVideoPlayMetric = new OddVideoPlayMetric();
    }

    @Test
	public void testGetType() throws Exception {
        assertEquals("event", oddVideoPlayMetric.getType());
    }

    @Test
	public void testGetOrganizationId() throws Exception {
        oddVideoPlayMetric.setOrganizationId(orgId);
        assertEquals(orgId, oddVideoPlayMetric.getOrganizationId());
    }

    @Test
	public void testGetAction() throws Exception {
        assertEquals(OddAppInitMetric.ACTION_VIDEO_PLAY, oddVideoPlayMetric.getAction());
    }

    @Test
	public void testGetContentType() throws Exception {
        oddVideoPlayMetric.setContentType(contentType);
        assertEquals(contentType, oddVideoPlayMetric.getContentType());
    }

    @Test
	public void testGetContentId() throws Exception {
        oddVideoPlayMetric.setContentId(contentId);
        assertEquals(contentId, oddVideoPlayMetric.getContentId());
    }

    @Test
	public void testToJSONObject() throws Exception {
        OddVideoPlayMetric metric = new OddVideoPlayMetric();
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