package io.oddworks.device.exception;

/**
 * Response code returned from server was not what was expected
 */
public class BadResponseCodeException extends OddRequestException {
    private final int code;

    public BadResponseCodeException(int code) {
        this.code = code;
    }

    public BadResponseCodeException(String detailMessage, int code) {
        super(detailMessage);
        this.code = code;
    }

    public BadResponseCodeException(String detailMessage, Throwable throwable, int code) {
        super(detailMessage, throwable);
        this.code = code;
    }

    public BadResponseCodeException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
