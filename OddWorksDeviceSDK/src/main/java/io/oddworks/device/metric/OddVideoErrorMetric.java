package io.oddworks.device.metric;

public class OddVideoErrorMetric extends OddMetric {
    private static final String TAG = OddVideoErrorMetric.class.getSimpleName();

    public OddVideoErrorMetric(final String organizationId, final String contentType, final String contentId) {
        mAction = ACTION_VIDEO_ERROR;
        mOrganizationId = organizationId;
        mContentType = contentType;
        mContentId = contentId;
    }
}