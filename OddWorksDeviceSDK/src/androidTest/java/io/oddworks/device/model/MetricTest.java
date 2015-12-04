package io.oddworks.device.model;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MetricTest {

    private String type = "action";
    private Map<String, Object> attributes = new HashMap<>();
    private Metric metric;

    @Test
    public void testGetType() throws Exception {
        metric = new Metric(type, attributes);

        assertEquals("when type is set", type, metric.getType());
    }

    @Test
    public void testGetTypeNull() throws Exception {
        metric = new Metric(null, attributes);

        assertEquals("when type is null", null, metric.getType());
    }

    @Test
    public void testGetEnabledTrue() throws Exception {
        attributes.put(Metric.ENABLED, true);

        metric = new Metric(type, attributes);

        assertEquals("when attributes.enabled is true", true, metric.get(Metric.ENABLED));
    }

    @Test
    public void testGetEnabledFalse() throws Exception {
        attributes.put(Metric.ENABLED, false);

        metric = new Metric(type, attributes);

        assertEquals("when attributes.enabled is false", false, metric.get(Metric.ENABLED));
    }

    @Test
    public void testGetEnabledNull() throws Exception {
        metric = new Metric(type, attributes);

        assertEquals("when attributes.enabled is null", false, metric.get(Metric.ENABLED));
    }

    @Test
    public void testGetInterval() throws Exception {
        attributes.put(Metric.INTERVAL, 10);

        metric = new Metric(type, attributes);

        assertEquals("when attributes.interval is set", 10, metric.get(Metric.INTERVAL));
    }

    @Test
    public void testGetIntervalNull() throws Exception {
        metric = new Metric(type, attributes);

        assertEquals("when attributes.interval is null", null, metric.get(Metric.INTERVAL));
    }

    @Test
    public void testGet() throws Exception {
        attributes.put("foo", "anything");
        metric = new Metric(type, attributes);

        assertEquals("when attributes.foo is set", "anything", metric.get("foo"));
    }

    @Test
    public void testGetNull() throws Exception {
        metric = new Metric(type, attributes);

        assertEquals("when attributes.foo is null", null, metric.get("foo"));
    }

    @Test
    public void testGetAttributes() throws Exception {

    }
}