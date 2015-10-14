package io.oddworks.device.request;

import android.content.Context;

import android.util.Log;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import io.oddworks.device.exceptions.OddParseException;
import io.oddworks.device.exceptions.BadResponseCodeException;
import io.oddworks.device.model.*;
import org.json.JSONException;

/**
 * Class of methods for handling api calls. Is instantiated using the ReqeustServicesInitializer and afterward can be
 * accessed through the instance field
 */
public class ApiCaller {
    public static ApiCaller instance;
    public static final int RESPONSE_OK = 200;
    public static final int RESPONSE_CREATED = 201;
    public static final int RESPONSE_NOT_FOUND = 404;


    public static final int MS_IN_SECONDS = 1000;
    public static String TAG = ApiCaller.class.getSimpleName();
    private final RequestHandler requestHandler;
    private final OddParser parser;

    protected ApiCaller(RequestHandler requestHandler, OddParser parser){
        this.requestHandler = requestHandler;
        this.parser = parser;
    }

    public void getConfig(final OddCallback<Config> cb) {
        Callback requestCallback = getRequestCallback(cb, new ParseCall<Config>() {
            @Override
            public Config parse(String responseBody) {
                return parser.parseConfig(responseBody);
            }
        });
        requestHandler.getConfig(requestCallback);
    }

    public void getView(final String id, final OddCallback<View> cb) {
        Callback requestCallback = getRequestCallback(cb, new ParseCall<View>() {
            @Override
            public View parse(String responseBody) {
                return parser.parseView(responseBody);
            }
        });
        requestHandler.getView(id, requestCallback);
    }

    public void getVideos(final MediaCollection col, final OddCallback<List<Media>> cb) {
        Callback requestCallback = getRequestCallback(cb, new ParseCall<List<Media>>() {
            @Override
            public List<Media> parse(String responseBody) {
                return parser.parseMediaList(responseBody);
            }
        });
        requestHandler.getVideos(col.getId(), requestCallback);
    }

    protected void getPollingAuthenticator(final OddCallback<PollingAuthenticator> cb) {
        final ApiCaller thisApiCaller = this;
        ParseCall<PollingAuthenticator> parseAuthenticator = new ParseCall<PollingAuthenticator>() {
            @Override
            public PollingAuthenticator parse(String responseBody) throws JSONException {
                DeviceCodeResponse parsedResponse = parser.parseDeviceCodeResponse(responseBody);

                return new PollingAuthenticator(parsedResponse.getDeviceCode(),
                        parsedResponse.getUserCode(),
                        parsedResponse.getVerificationUrl(),
                        parsedResponse.getExpirationDate(),
                        parsedResponse.getIntervalSeconds() * MS_IN_SECONDS,
                        thisApiCaller
                );
            }
        };
        Callback requestCallback = getRequestCallback(cb, parseAuthenticator);
        requestHandler.getAuthDeviceCode(requestCallback);
    }

    protected void tryGetAuthToken(final String deviceCode, final OddCallback<AuthToken> cb){
        Log.d(this.getClass().getSimpleName(), "start tryGetAuthToken");
        Callback requestCallback = getRequestCallback(cb, new ParseCall<AuthToken>() {
            @Override
            public AuthToken parse(String responseBody) throws JSONException {
                return parser.parseAuthToken(responseBody);
            }
        });
        requestHandler.getAuthToken(requestCallback, deviceCode);
    }

    private <T> Callback getRequestCallback(final OddCallback<T> cb, final ParseCall<T> parseCall) {
        return new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("ResponseCb onFailure", "Failed", e);

                cb.onFailure(e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                        Log.d("ResponseCb onResponse", "code :"
                                + response.code() + " responseBody: " + response);
                if (response.isSuccessful()) {
                    try {
                        T obj = parseCall.parse(response.body().string());
                        cb.onSuccess(obj);
                    } catch (Exception e) {
                        cb.onFailure(new OddParseException(
                                "Parse for response body failed: " + response.body().string(), e));
                    }
                } else {
                    cb.onFailure(new BadResponseCodeException(response.code()));
                }

            }
        };
    }

    /** Set an AuthToken to be used in api calls */
    public void setAuthToken(AuthToken authToken) {
        requestHandler.setAuthToken(authToken);
    }

    /** remove auth token. Requests will no longer use auth */
    public void removeAuthToken() {
        requestHandler.removeAuthToken();
    }

    /**
     * @return true if a token has been set, otherwise false
     */
    public boolean isAuthTokenSet() {
        return requestHandler.isAuthTokenSet();
    }

    private interface ParseCall<T> {
        T parse(String responseBody) throws JSONException;
    }
}
