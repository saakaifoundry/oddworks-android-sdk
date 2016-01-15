package io.oddworks.device.metric;


import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
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
    private int customInterval = 312;
    private OddVideoPlayingMetric oddVideoPlayingMetric;

    @Before
    public void beforeEach() {
        oddVideoPlayingMetric = new OddVideoPlayingMetric();
    }

    @Test
    public void testGetType() throws Exception {
        assertEquals("event", oddVideoPlayingMetric.getType());
    }

    @Test
    public void testGetOrganizationId() throws Exception {
        oddVideoPlayingMetric.setOrganizationId(orgId);
        assertEquals(orgId, oddVideoPlayingMetric.getOrganizationId());
    }

    @Test
    public void testGetAction() throws Exception {
        assertEquals(OddAppInitMetric.ACTION_VIDEO_PLAYING, oddVideoPlayingMetric.getAction());
    }

    @Test
    public void testGetContentType() throws Exception {
        oddVideoPlayingMetric.setContentType(contentType);
        assertEquals(contentType, oddVideoPlayingMetric.getContentType());
    }

    @Test
    public void testGetContentId() throws Exception {
        oddVideoPlayingMetric.setContentId(contentId);
        assertEquals(contentId, oddVideoPlayingMetric.getContentId());
    }

    @Test
    public void testGetInterval() throws Exception {
        OddVideoPlayingMetric.setInterval(OddVideoPlayingMetric.DEFAULT_INTERVAL);
        assertEquals(OddVideoPlayingMetric.DEFAULT_INTERVAL, OddVideoPlayingMetric.getInterval());
    }

    @Test
    public void testGetIntervalCustom() throws Exception {
        OddVideoPlayingMetric.setInterval(customInterval);
        assertEquals(customInterval, OddVideoPlayingMetric.getInterval());
        assertEquals(customInterval, oddVideoPlayingMetric.getInterval());
    }

    @Test
    public void testToJSONObject() throws Exception {
        OddVideoPlayingMetric metric = new OddVideoPlayingMetric();
        metric.setOrganizationId(orgId);
        metric.setContentType(contentType);
        metric.setContentId(contentId);
        metric.setElapsed(elapsed);
        metric.setDuration(duration);

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