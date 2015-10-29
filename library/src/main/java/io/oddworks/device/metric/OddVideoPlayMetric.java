package io.oddworks.device.metric;

/**
 * Created by brkattk on 10/5/15.
 */
public class OddVideoPlayMetric extends OddMetric {
    private static final String TAG = OddVideoPlayMetric.class.getSimpleName();

    public OddVideoPlayMetric(final String organizationId, final String contentType, final String contentId) {
        mAction = ACTION_VIDEO_PLAY;
        mOrganizationId = organizationId;
        mContentType = contentType;
        mContentId = contentId;
    }
}