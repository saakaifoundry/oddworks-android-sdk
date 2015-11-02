package io.oddworks.device.request;

/**
 * Created by Dan Pallas on 9/24/15.
 */
public interface OddGetAuthTokenCallback {
    void hasToken(String authToken);
    void notAuthed(PollingAuthenticator authenticator);
    void failure(Exception ex);
}