package io.oddworks.device.metric;


import android.support.test.runner.AndroidJUnit4;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class OddAppInitMetricTest {
    private OddAppInitMetric oddAppInitMetric;
    private JSONObject customJSON;

    @Before
    public void beforeEach() {
        customJSON = new JSONObject();
        oddAppInitMetric = new OddAppInitMetric(null, null, customJSON);
    }

    @Test
	public void testGetType() throws Exception {
        assertEquals("event", oddAppInitMetric.getType());
    }


    @Test
	public void testGetAction() throws Exception {
        assertEquals(OddMetric.Companion.getACTION_APP_INIT(), oddAppInitMetric.getAction());
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
        customJSON.put("foo", "bar");
        OddAppInitMetric metric = new OddAppInitMetric(null, null, customJSON);

        String expected = "{\"data\": {" +
                "type: \"" + metric.getType() + "\"," +
                "attributes: {" +
                "action: \"" + metric.getAction() + "\"" +
                "}," +
                "meta: {" +
                "foo: \"bar\"" +
                "}}}";

        JSONAssert.assertEquals(expected, metric.toJSONObject(), true);
    }
}