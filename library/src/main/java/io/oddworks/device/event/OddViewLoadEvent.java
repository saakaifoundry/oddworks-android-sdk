package io.oddworks.device.event;

/**
 * Created by brkattk on 10/16/15.
 */
public class OddViewLoadEvent extends OddEvent {
    private static final String TAG = OddViewLoadEvent.class.getSimpleName();
    private static final String ACTION = "view:load";

    public OddViewLoadEvent(final String organizationId, final String contentType, final String contentId) {
        this.mAction = ACTION;
        this.mOrganizationId = organizationId;
        this.mContentType = contentType;
        this.mContentId = contentId;
    }
}
