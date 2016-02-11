package io.oddworks.device.model;

/**
 * Authorization token
 */
public class AuthToken {
    public static final String TAG = AuthToken.class.getSimpleName();
    private final String token;
    private final String tokenType;

    public AuthToken(String token, String tokenType) {
        this.token = token;
        this.tokenType = tokenType;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "token='" + token + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}