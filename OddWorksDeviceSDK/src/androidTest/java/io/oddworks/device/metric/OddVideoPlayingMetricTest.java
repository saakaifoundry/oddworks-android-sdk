package io.oddworks.device.metric;


import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class OddVideoPlayingMetricTest {
    private String orgId = "odd-networks";
    private String contentType = "aThing";
    private String contentId = "thingId";
    private int elapsed = 456;
    private int duration = 1234;

    @Test
    public void testGetType() throws Exception {
        assertEquals("event", OddVideoPlayingMetric.getInstance().getType());
    }

    @Test
    public void testGetOrganizationId() throws Exception {
        OddVideoPlayingMetric.getInstance().setOrganizationId(orgId);
        assertEquals(orgId, OddVideoPlayingMetric.getInstance().getOrganizationId());
    }

    @Test
    public void testGetAction() throws Exception {
        assertEquals(OddAppInitMetric.ACTION_VIDEO_PLAYING, OddVideoPlayingMetric.getInstance().getAction());
    }

    @Test
    public void testGetContentType() throws Exception {
        OddVideoPlayingMetric.getInstance().setContentType(contentType);
        assertEquals(contentType, OddVideoPlayingMetric.getInstance().getContentType());
    }

    @Test
    public void testGetContentId() throws Exception {
        OddVideoPlayingMetric.getInstance().setContentId(contentId);
        assertEquals(contentId, OddVideoPlayingMetric.getInstance().getContentId());
    }

    @Test
    public void testToJSONObject() throws Exception {
        OddVideoPlayingMetric metric = (OddVideoPlayingMetric) OddVideoPlayingMetric
                .getInstance()
                .setOrganizationId(orgId)
                .setContentType(contentType)
                .setContentId(contentId)
                .setElapsed(elapsed)
                .setDuration(duration);

        Log.d("TESTING", metric.getContentId());
        Log.d("TESTING", metric.toString());
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