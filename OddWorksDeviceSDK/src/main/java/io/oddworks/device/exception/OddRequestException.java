package io.oddworks.device.exception;

/**
 * Created by Dan Pallas on 9/16/15.
 */
public class OddRequestException extends RuntimeException {
    public OddRequestException() {
    }

    public OddRequestException(String detailMessage) {
        super(detailMessage);
    }

    public OddRequestException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public OddRequestException(Throwable throwable) {
        super(throwable);
    }
}
