package io.oddworks.device.request;

import android.util.Log;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.oddworks.device.exception.BadResponseCodeException;
import io.oddworks.device.exception.DeviceCodeExpiredException;
import io.oddworks.device.exception.RestServicesNotInitialized;
import io.oddworks.device.model.AuthToken;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

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
    private Timer timer;
    /* used for rx authenticate method */
    private Observable<Long> authTicker;
    /** don't access without using syncLock*/
    private volatile Boolean authenticating;

    protected PollingAuthenticator(String deviceCode, String userCode, String verificationUrl, Date userCodeExpires,
                                   int interval, ApiCaller apiCaller) {
        this.deviceCode = deviceCode;
        this.userCode = userCode;
        this.verificationUrl = verificationUrl;
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

    /** if this object is authenticating using the callback based authenticate method, then this will cancel future
     * authentication requests. if using authenticate that returns an Observable, then this has no effect.
     */
    public void cancelAuthentication() {
        synchronized (syncLock) {
            authenticating = false;
            timer.cancel();
            timer.purge();
        }
    }

    public boolean isExpired() {
        Date now = new Date();
        return expirationDate.compareTo(now) <= 0;
    }

    /** @return true if currently polling for authentication token. Otherwise false.
     *
     * note: if authenticating through observable authenticate method, then this will return false. It only returns
     * true if authenticating
     *  using the callback based authenticate method */
    public boolean isAuthenticating() {
        synchronized (syncLock) {
            return authenticating;
        }
    }

    /** If not expired then this will continue polling the server until time expires or an auth token is obtained
     * Callback.onFailure will be completed with DeviceCodeExpiredException if this object expires without getting an
     * auth token.
     *
     * It's not neccessary to use this method if you are using the rxAuthenticate method
     * @return true if polling started, otherwise false (expired) */
    public boolean authenticate(final OddCallback<AuthToken> callback) {
        synchronized (syncLock) {
            if(isExpired()) {
                return false;
            }
            if(!authenticating) {
                authenticating = true;
                timer = new Timer();
                TimerTask task = getPollTask(callback);
                timer.schedule(task, new Date(), interval);
                TimerTask expiredTask = getExpiredTask(callback);
                timer.schedule(expiredTask, expirationDate);
            }
            return true;
        }
    }

    /** Begin polling for an AuthToken. Unsubscribing will stop polling for an AuthToken.
     * PollingAuthenticator.cancelAuthentication and PollingAuthenticator will have no effect.
     * @return an observable that will emit 1 AuthToken or 1 exception. 404 responses will not be emitted.
     * If this instance of PollingAuthenticator expires, then a BadResponseCodeException will be emitted
     */
    public Observable<AuthToken> authenticate() {
        authTicker = Observable.interval(interval, TimeUnit.MILLISECONDS);
        return authTicker.map(new Func1<Long, Long>() {

            @Override
            public Long call(Long tick) {
                if(isExpired())
                    throw new DeviceCodeExpiredException();
                else
                    return tick;
            }
        })
                .flatMap(new Func1<Long, Observable<AuthToken>>() {
                    @Override
                    public Observable<AuthToken> call(Long aLong) {
                        return Observable.create(new Observable.OnSubscribe<AuthToken>() {
                            @Override
                            public void call(final Subscriber<? super AuthToken> subscriber) {
                                apiCaller.tryGetAuthToken(deviceCode, new OddCallback<AuthToken>() {
                                    @Override
                                    public void onSuccess(AuthToken entity) {
                                        subscriber.onNext(entity);
                                        subscriber.onCompleted();
                                    }

                                    @Override
                                    public void onFailure(Exception exception) {
                                        // if the response is 404, then don't emit anything.
                                        // if some other error occurred, then emit exception and complete
                                        if(!(exception instanceof BadResponseCodeException &&
                                                ((BadResponseCodeException)exception).getCode() == 404)) {
                                            subscriber.onError(exception);
                                            subscriber.onCompleted();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
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

    /**
     * @return a string representation of this object which can be used in PollingAuthenticator.fromSerialized
     */
    public String serialize() {
        return deviceCode + " " +
                userCode + " " +
                verificationUrl + " " +
                expirationDate.getTime() + " " +
                interval;

    }

    /**
     * @param serialized a string returned by PollingAuthenticator.serialize()
     * @throws RestServicesNotInitialized if this method was called before RestServicesProvider was initialized.
     */
    public static PollingAuthenticator fromSerialized(String serialized) {
        if(RestServiceProvider.getInstance() == null) {
            throw new RestServicesNotInitialized();
        }

        String[] strings = serialized.split(" ");
        return new PollingAuthenticator(strings[0],
                strings[1],
                strings[2],
                new Date(Long.parseLong(strings [3])),
                Integer.parseInt(strings[4]),
                ApiCaller.instance);
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
