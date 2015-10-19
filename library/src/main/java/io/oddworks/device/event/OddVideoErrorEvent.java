package io.oddworks.device.event;

/**
 * Created by brkattk on 10/5/15.
 */
public class OddVideoErrorEvent extends OddEvent {
    private static final String TAG = OddVideoErrorEvent.class.getSimpleName();
    private static final String ACTION = "video:error";

    public OddVideoErrorEvent(final String organizationId, final String contentType, final String contentId) {
        this.mAction = ACTION;
        this.mOrganizationId = organizationId;
        this.mContentType = contentType;
        this.mContentId = contentId;
    }
}