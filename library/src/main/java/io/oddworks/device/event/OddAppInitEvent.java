package io.oddworks.device.event;

/**
 * Created by brkattk on 10/5/15.
 */
public class OddAppInitEvent extends OddEvent {
    private static final String TAG = OddAppInitEvent.class.getSimpleName();
    private static final String ACTION = "app:init";

    public OddAppInitEvent(final String organizationId) {
        this.mAction = ACTION;
        this.mOrganizationId = organizationId;
    }
}