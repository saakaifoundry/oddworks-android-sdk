package io.oddworks.device.request;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.Locale;

import io.oddworks.device.R;
import io.oddworks.device.event.OddEvent;
import io.oddworks.device.model.AuthToken;

/**
 * For calling the api and returning raw responses
 */
public class RequestHandler {
    protected static RequestHandler instance;

    /** supported request methods, if adding new ones, you'll probably have to make changes to existing
    RequestHandler methods */
    private enum RequestMethod { GET, POST};
    private static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private AuthToken authToken;
    private OkHttpClient mClient = new OkHttpClient();
    private String mBaseUrl;
    private Context mContext;
    private String mAccessToken;
    private String mAccept;
    private String mLanguage = Locale.getDefault().getLanguage().toLowerCase();
    private String mCountry = Locale.getDefault().getCountry().toLowerCase();
    private String mLocale = mLanguage + "-" + mCountry;

    protected RequestHandler(Context context) {
        mContext = context;
        mAccessToken = mContext.getString(R.string.odd_access_token);
        mBaseUrl =  mContext.getString(R.string.odd_base_url);
        mAccept = mContext.getString(R.string.odd_request_content_type);
        authToken = null;
    }

    protected void getConfig(final Callback callback) {
        Request request = getOddGetRequest(mContext.getString(R.string.odd_config_endpoint));
        enqueueOddCall(request, callback);
    }

    protected void getView(final String id, final Callback callback) {
        Request request = getOddGetRequest(mContext.getString(R.string.odd_views_endpoint) + "/" + id);
        enqueueOddCall(request, callback);
    }

    /** get an authorized (if AuthToken is set) get request for the endpoint */
    private Request getOddGetRequest(String endpoint) {
        return getOddRequest(endpoint, RequestMethod.GET, null, false);
    }

    private Request getOddRequest(String endpoint, RequestMethod method, RequestBody body, boolean forceNoAuth) {
        Request.Builder builder = new Request.Builder();
        builder.url(mBaseUrl + endpoint + "?include=true")
                .addHeader("x-access-token", mAccessToken)
                .addHeader("accept", mAccept)
                .addHeader("accept-language", mLocale);
        if(!forceNoAuth && authToken != null) {
            builder.addHeader("authorization", authToken.getTokenType() + " " + authToken.getToken());
        }
        if(method == RequestMethod.POST) {
            builder.post(body);
        }
        return builder.build();
    }

    private void enqueueOddCall(Request request, Callback callback) {
        Call call = mClient.newCall(request);
        call.enqueue(callback);
    }

    protected void getVideos(String collectionId, Callback callback) {
        Request request = getOddGetRequest("videoCollection/" + collectionId + "/videos");
        enqueueOddCall(request, callback);
    }

    protected void getSearch(String term, int limit, int offset, Callback callback) {
        Request request = getOddGetRequest("search?term=" + term + "&limit=" + limit + "&offset=" + offset);
        enqueueOddCall(request, callback);
    }

    protected void postEvent(OddEvent event, Callback callback) {
        String endpoint = mContext.getString(R.string.endpoint_events);
        Request request = getOddRequest(endpoint, RequestMethod.POST, RequestBody.create(JSON, event.toJSONObject().toString()), true);
        enqueueOddCall(request, callback);
    }

    protected void getAuthDeviceCode(Callback callback) {
        String endpoint = mContext.getString(R.string.auth_device_code_endpoint);
        Request request = getOddRequest(endpoint, RequestMethod.POST, RequestBody.create(JSON, ""), true);
        enqueueOddCall(request, callback);
    }

    protected void getAuthToken(Callback callback, String deviceCode) {
        String endpoint = mContext.getString(R.string.odd_auth_token_endpoint);
        RequestBody body = RequestBody.create(JSON,
                "{ \"type\":\"authorized_user\", \"attributes\": {\"device_code\":\"" + deviceCode + "\"}}");
        Request request = getOddRequest(endpoint, RequestMethod.POST, body, true);
        Log.d("getAuthToken", "request body: " + request.body().toString());
        enqueueOddCall(request, callback);
    }

    /** Set an AuthToken to be used in api calls */
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    /** remove auth token. Requests will no longer use auth */
    public void removeAuthToken() {
        this.authToken = null;
    }

    /**
     * @return true if a token has been set, otherwise false
     */
    public boolean isAuthTokenSet() {
        return this.authToken != null;
    }
}
