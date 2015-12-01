package io.oddworks.device.metric;

public class OddAppInitMetric extends OddMetric {
    private static final String TAG = OddAppInitMetric.class.getSimpleName();

    public OddAppInitMetric(final String organizationId) {
        mAction = ACTION_APP_INIT;
        mOrganizationId = organizationId;
    }
}