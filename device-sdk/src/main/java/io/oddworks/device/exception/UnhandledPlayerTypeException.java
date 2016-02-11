package io.oddworks.device.exception;

/**
 * Created by dan on 12/7/15.
 */
public class UnhandledPlayerTypeException extends RuntimeException{
    public UnhandledPlayerTypeException() {
    }

    public UnhandledPlayerTypeException(String detailMessage) {
        super(detailMessage);
    }

    public UnhandledPlayerTypeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UnhandledPlayerTypeException(Throwable throwable) {
        super(throwable);
    }
}
