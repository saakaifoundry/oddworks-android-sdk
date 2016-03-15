package io.oddworks.device.model;

import android.support.annotation.Nullable;

import org.json.JSONObject;

/**
 * Authorization token
 */
public class AuthToken {
    public static final String TAG = AuthToken.class.getSimpleName();
    private final String token;
    private final String tokenType;
    private final JSONObject entitlementCredentials;

    public AuthToken(String token, String tokenType, JSONObject entitlementCredentials) {
        this.token = token;
        this.tokenType = tokenType;
        this.entitlementCredentials = entitlementCredentials;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    @Nullable
    public JSONObject getEntitlementCredentials() { return entitlementCredentials; }

    @Override
    public String toString() {
        return "AuthToken{" +
                "token='" + token + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}