package io.oddworks.device.request;

public interface OddGetAuthTokenCallback {
    void hasToken(String authToken);
    void notAuthed(PollingAuthenticator authenticator);
    void failure(Exception ex);
}