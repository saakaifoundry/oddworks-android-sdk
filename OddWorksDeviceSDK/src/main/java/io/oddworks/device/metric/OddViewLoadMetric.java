package io.oddworks.device.metric;

public class OddViewLoadMetric extends OddMetric {
    private static final String TAG = OddViewLoadMetric.class.getSimpleName();

    public OddViewLoadMetric(final String organizationId, final String contentType, final String contentId) {
        mAction = ACTION_VIEW_LOAD;
        mOrganizationId = organizationId;
        mContentType = contentType;
        mContentId = contentId;
    }
}
