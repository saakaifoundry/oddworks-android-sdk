package io.oddworks.device.metric;

import android.support.test.runner.AndroidJUnit4;

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

    @Test
    public void testGetType() throws Exception {
        assertEquals("event", OddVideoStopMetric.getInstance().getType());
    }

    @Test
    public void testGetOrganizationId() throws Exception {
        OddVideoStopMetric.getInstance().setOrganizationId(orgId);
        assertEquals(orgId, OddVideoStopMetric.getInstance().getOrganizationId());
    }

    @Test
    public void testGetAction() throws Exception {
        assertEquals(OddAppInitMetric.ACTION_VIDEO_STOP, OddVideoStopMetric.getInstance().getAction());
    }

    @Test
    public void testGetContentType() throws Exception {
        OddVideoStopMetric.getInstance().setContentType(contentType);
        assertEquals(contentType, OddVideoStopMetric.getInstance().getContentType());
    }

    @Test
    public void testGetContentId() throws Exception {
        OddVideoStopMetric.getInstance().setContentId(contentId);
        assertEquals(contentId, OddVideoStopMetric.getInstance().getContentId());
    }

    @Test
    public void testToJSONObject() throws Exception {
        OddVideoStopMetric metric = (OddVideoStopMetric) OddVideoStopMetric
                .getInstance()
                .setOrganizationId(orgId)
                .setContentType(contentType)
                .setContentId(contentId)
                .setElapsed(elapsed)
                .setDuration(duration);

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
