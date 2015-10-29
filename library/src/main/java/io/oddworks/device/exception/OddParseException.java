package io.oddworks.device.exception;

/**
 * Created by brkattk on 9/15/15.
 */
public class OddParseException extends RuntimeException {
    public OddParseException() {
        super();
    }

    public OddParseException(String detailMessage) {
        super(detailMessage);
    }

    public OddParseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public OddParseException(Throwable throwable) {
        super(throwable);
    }
}
