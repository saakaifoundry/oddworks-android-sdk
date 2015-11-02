package io.oddworks.device.metric;

/**
 * Created by brkattk on 10/5/15.
 */
public class OddVideoErrorMetric extends OddMetric {
    private static final String TAG = OddVideoErrorMetric.class.getSimpleName();

    public OddVideoErrorMetric(final String organizationId, final String contentType, final String contentId) {
        mAction = ACTION_VIDEO_ERROR;
        mOrganizationId = organizationId;
        mContentType = contentType;
        mContentId = contentId;
    }
}