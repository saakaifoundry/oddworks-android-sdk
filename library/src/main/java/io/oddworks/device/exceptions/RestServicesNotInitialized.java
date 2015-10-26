package io.oddworks.device.exceptions;

/** An operation that depends on RestServicesProvider being initialized was called before RestServicesProvider was
 * initialized.
 */
public class RestServicesNotInitialized extends RuntimeException {
    public RestServicesNotInitialized() {
    }

    public RestServicesNotInitialized(String detailMessage) {
        super(detailMessage);
    }

    public RestServicesNotInitialized(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RestServicesNotInitialized(Throwable throwable) {
        super(throwable);
    }
}
