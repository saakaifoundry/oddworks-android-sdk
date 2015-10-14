package io.oddworks.device.model;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;

/**
 * Response from /v1/auth/device/code
 */
public class DeviceCodeResponse {
    final private String deviceCode;
    final private String userCode;
    final private String verificationUrl;
    final private int expiresInSeconds;
    final private int intervalSeconds;
    final private Date expirationDate;

    public DeviceCodeResponse(String deviceCode, String userCode, String verificationUrl, int expiresInSeconds,
                              int intervalSeconds) {
        this.deviceCode = deviceCode;
        this.userCode = userCode;
        this.verificationUrl = verificationUrl;
        this.expiresInSeconds = expiresInSeconds;
        this.intervalSeconds = intervalSeconds;
        this.expirationDate = calcExpiration(expiresInSeconds);
    }

    private Date calcExpiration(int expiresInSeconds) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, expiresInSeconds);
        return cal.getTime();
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public String getVerificationUrl() {
        return verificationUrl;
    }

    public int getExpiresInSeconds() {
        return expiresInSeconds;
    }

    public int getIntervalSeconds() {
        return intervalSeconds;
    }
}
