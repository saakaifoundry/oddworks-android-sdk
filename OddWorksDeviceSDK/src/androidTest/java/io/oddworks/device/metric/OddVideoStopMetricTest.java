package io.oddworks.device.metric;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class OddVideoStopMetricTest {
    private String orgId = "odd-networks";
    private String contentType = "aThing";
    private String contentId = "thingId";
    private int elapsed = 456;
    private int duration = 1234;
    private OddVideoStopMetric oddVideoStopMetric;

    @Before
    public void beforeEach() {
        oddVideoStopMetric = new OddVideoStopMetric();
    }

    @Test
    public void testGetType() throws Exception {
        assertEquals("event", oddVideoStopMetric.getType());
    }

    @Test
    public void testGetOrganizationId() throws Exception {
        oddVideoStopMetric.setOrganizationId(orgId);
        assertEquals(orgId, oddVideoStopMetric.getOrganizationId());
    }

    @Test
    public void testGetAction() throws Exception {
        assertEquals(OddAppInitMetric.ACTION_VIDEO_STOP, oddVideoStopMetric.getAction());
    }

    @Test
    public void testGetContentType() throws Exception {
        oddVideoStopMetric.setContentType(contentType);
        assertEquals(contentType, oddVideoStopMetric.getContentType());
    }

    @Test
    public void testGetContentId() throws Exception {
        oddVideoStopMetric.setContentId(contentId);
        assertEquals(contentId, oddVideoStopMetric.getContentId());
    }

    @Test
    public void testToJSONObject() throws Exception {
        OddVideoStopMetric metric = new OddVideoStopMetric();
        metric.setOrganizationId(orgId);
        metric.setContentType(contentType);
        metric.setContentId(contentId);
        metric.setElapsed(elapsed);
        metric.setDuration(duration);

        String expected = "{" +
                "type: \"" + metric.getType() + "\"," +
                "attributes: {" +
                "organizationId: \"" + metric.getOrganizationId() + "\"," +
                "action: \"" + metric.getAction() + "\"," +
                "contentType: \"" + metric.getContentType() + "\"," +
                "contentId: \"" + metric.getContentId() + "\"," +
                "elapsed: " + elapsed + "," +
                "duration: " + duration + "" +
                "}" +
                "}";
        JSONAssert.assertEquals(expected, metric.toJSONObject(), true);
    }
}
