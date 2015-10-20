package io.oddworks.device.request;

import android.util.Log;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import io.oddworks.device.exception.BadResponseCodeException;
import io.oddworks.device.exception.DeviceCodeExpiredException;
import io.oddworks.device.model.AuthToken;

/**
 * Class for polling authentication server until an auth code is generated
 * @author Dan Pallas
 * @since v1.0 on 9/24/15
 */
public class PollingAuthenticator {
    private final String deviceCode;
    private final String userCode;
    private final String verificationUrl;
    private final Date expirationDate;
    private final int interval;
    private final Object syncLock = new Object();
    private final ApiCaller apiCaller;
    private final Timer timer;
    /** don't access without using syncLock*/
    private volatile Boolean authenticating;

    protected PollingAuthenticator(String deviceCode, String userCode, String verificationUrl, Date userCodeExpires,
                                   int interval, ApiCaller apiCaller) {
        this.deviceCode = deviceCode;
        this.userCode = userCode;
        // todo stop hardcoding verificationUrl when api stops hardcoding verificationurl in its response
//        this.verificationUrl = verificationUrl;
        this.verificationUrl = "https://odd-auth-reference.herokuapp.com/device/link/android";
        this.expirationDate = userCodeExpires;
        this.interval = interval;
        this.apiCaller = apiCaller;
        this.timer = new Timer("Auth timer for userCode: " + userCode);
        this.authenticating = false;
    }

    public String getUserCode() {
        return userCode;
    }

    public String getVerificationUrl() {
        return verificationUrl;
    }

    public void cancelAuthentication() {
        synchronized (syncLock) {
            authenticating = false;
            timer.cancel();
        }
    }

    /** @return true if currently polling for authentication token. Otherwise false */
    public boolean isAuthenticating() {
        synchronized (syncLock) {
            return authenticating;
        }
    }

    /** will continue polling the server until time expires or an auth token is obtained
     * Callback.onFailure will be completed with DeviceCodeExpiredException if this object expires without getting an
     * auth token. */
    public void authenticate(final OddCallback<AuthToken> callback) {
        synchronized (syncLock) {
            if(!authenticating) {
                authenticating = true;
                timer.purge();

                TimerTask task = getPollTask(callback);
                timer.schedule(task, new Date(), interval);
                 TimerTask expiredTask = getExpiredTask(callback);
                timer.schedule(expiredTask, expirationDate);
            }
        }
    }

    private TimerTask getExpiredTask(final OddCallback<AuthToken> callback) {
        return new TimerTask() {
            @Override
            public void run() {
                synchronized (syncLock) {
                    if(authenticating) {
                        authenticating = false;
                        timer.cancel();
                        callback.onFailure(new DeviceCodeExpiredException());
                    }
                }
            }
        };
    }

    private TimerTask getPollTask(final OddCallback<AuthToken> callback) {
        return new TimerTask() {
            @Override
            public void run() {
                apiCaller.tryGetAuthToken(deviceCode, new OddCallback<AuthToken>() {
                    @Override
                    public void onSuccess(AuthToken entity) {
                        synchronized (syncLock) {
                            Log.d(PollingAuthenticator.class + ".getPollTask", "onSuccess AuthToken: " + entity.toString());
                            if (authenticating) {
                                timer.cancel();
                                authenticating = false;
                                callback.onSuccess(entity);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        if (exception instanceof BadResponseCodeException){
                            if(((BadResponseCodeException) exception).getCode() != ApiCaller.RESPONSE_NOT_FOUND) {
                                synchronized (syncLock) {
                                    timer.cancel();
                                    authenticating = false;
                                    callback.onFailure(exception);
                                }
                            }
                        }
                    }
                });
            }
        };
    }

    @Override
    public String toString() {
        return "OddAuthenticator{" +
                "deviceCode='" + deviceCode + '\'' +
                ", userCode='" + userCode + '\'' +
                ", verificationUrl='" + verificationUrl + '\'' +
                ", expirationDate=" + expirationDate +
                ", interval=" + interval +
                ", syncLock=" + syncLock +
                ", apiCaller=" + apiCaller +
                ", timer=" + timer +
                ", authenticating=" + authenticating +
                '}';
    }
}
