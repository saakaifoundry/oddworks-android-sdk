package io.oddworks.device.exception;

public class OddAuthTokenUserMismatch extends BadResponseCodeException {

    public OddAuthTokenUserMismatch(int code) {
        super(code);
    }

    public OddAuthTokenUserMismatch(String detailMessage, int code) {
        super(detailMessage, code);
    }

    public OddAuthTokenUserMismatch(String detailMessage, Throwable throwable, int code) {
        super(detailMessage, throwable, code);
    }

    public OddAuthTokenUserMismatch(Throwable throwable, int code) {
        super(throwable, code);
    }
}
