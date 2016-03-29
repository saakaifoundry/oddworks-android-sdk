package io.oddworks.device.request;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import io.oddworks.device.model.AuthToken;

/**
 * Service for retrieving and storing authentication tokens. Is instantiated using the ReqeustServicesInitializer and
 * afterward can be accessed through the instance field
 */
public class AuthenticationService {
    protected static AuthenticationService instance;
    private static final String AUTH_PREFS = "AuthenticationService Prefs";
    public static final String TOKEN_KEY = "token";
    public static final String TOKEN_TYPE_KEY = "token type";
    public static final String ENTITLEMENT_CREDENTIALS_KEY = "entitlement credentials key";
    private final ApiCaller apiCaller;
    private final Context context;

    protected AuthenticationService(ApiCaller apiCaller, Context context) {
        this.apiCaller = apiCaller;
        this.context = context;
    }

    /** returns a PollingAuthenticator for getting an auth key from the server */
    public void getPollingAuthenticator(OddCallback<PollingAuthenticator> cb) {
        apiCaller.getPollingAuthenticator(cb);
    }

    /**
     * Stores auth token persistently
     */
    public void storeAuthToken(AuthToken authToken) {
        SharedPreferences.Editor prefsEditor = getSharedPreferences().edit();
        prefsEditor.putString(TOKEN_KEY, authToken.getToken());
        prefsEditor.putString(TOKEN_TYPE_KEY, authToken.getTokenType());

        JSONObject entitlementCredentials = authToken.getEntitlementCredentials();
        if (entitlementCredentials != null) {
            prefsEditor.putString(ENTITLEMENT_CREDENTIALS_KEY, entitlementCredentials.toString());
        }

        prefsEditor.apply();
    }

    /**
     * @return stored auth token or null if none exists
     */
    public AuthToken getStoredToken() {
        SharedPreferences prefs = getSharedPreferences();
        String type = prefs.getString(TOKEN_TYPE_KEY, null);
        String token = prefs.getString(TOKEN_KEY, null);
        String entitlementCredentialsString = prefs.getString(ENTITLEMENT_CREDENTIALS_KEY, null);
        if(type == null || token == null) {
            return null;
        } else {
            JSONObject entitlementCredentials = null;
            try {
                entitlementCredentials = new JSONObject(entitlementCredentialsString);
            } catch (Throwable e) {
                Log.e(AuthenticationService.class.getSimpleName(), "Unable to parse entitlementCredentials: " + e);
            }

            return new AuthToken(token, type, entitlementCredentials);
        }
    }

    /** delete stored key from preferences */
    public void deleteStoredToken() {
        SharedPreferences.Editor prefsEditor = getSharedPreferences().edit();
        prefsEditor.remove(TOKEN_KEY);
        prefsEditor.remove(TOKEN_TYPE_KEY);
        prefsEditor.remove(ENTITLEMENT_CREDENTIALS_KEY);
        prefsEditor.apply();
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
    }
}
