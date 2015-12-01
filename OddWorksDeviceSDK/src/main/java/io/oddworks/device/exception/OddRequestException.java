package io.oddworks.device.exception;

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
