package io.oddworks.device.exception;

public class DeviceCodeExpiredException extends RuntimeException {
    public DeviceCodeExpiredException() {
    }

    public DeviceCodeExpiredException(String detailMessage) {
        super(detailMessage);
    }

    public DeviceCodeExpiredException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DeviceCodeExpiredException(Throwable throwable) {
        super(throwable);
    }
}
