package io.oddworks.device.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MetricsConfigTest {
    private Metric metric;
    private Map<String, Object> attributes = new HashMap<>();

    @Before
    public void beforeEach() {
        attributes.put("foo", "bar");
        metric = new Metric("typeofmetric", attributes);
    }

    @Test
    public void testGetMetrics() throws Exception {
        List<Metric> metrics = new ArrayList<>();
        metrics.add(metric);

        MetricsConfig metricsConfig = new MetricsConfig(metrics);

        assertEquals("getMetrics returns List<Metric>", metrics, metricsConfig.getMetrics());
    }
}