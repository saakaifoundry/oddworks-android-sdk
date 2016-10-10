package io.oddworks.device.request;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.Locale;

import io.oddworks.device.Oddworks;
import io.oddworks.device.exception.RestServicesNotInitialized;
import io.oddworks.device.metric.OddMetric;

/**
 * For calling the api and returning raw responses
 */
public class RequestHandler {
    protected static RequestHandler instance;

    /** supported request methods. if you add new ones, you'll probably have to make changes to existing
    RequestHandler methods */
    private enum RequestMethod { GET, POST }
    private static MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String acceptHeader = "application/json";

    private OkHttpClient okHttpClient = new OkHttpClient();
    private String versionName;
    private HttpUrl baseURL;
    private String configJWT;
    private String language = Locale.getDefault().getLanguage().toLowerCase();
    private String country = Locale.getDefault().getCountry().toLowerCase();
    private String locale = language + "-" + country;

    /**
     * @param context Application context for fetching resources
     */
    protected RequestHandler(@NonNull Context context) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String configJWT = info.metaData.getString(Oddworks.CONFIG_JWT_KEY);
            String apiBaseURL = info.metaData.getString(Oddworks.API_BASE_URL_KEY);

            this.versionName = packageInfo.versionName;

            if (configJWT != null) {
                this.configJWT = configJWT;
            } else {
                throw new RestServicesNotInitialized(Oddworks.CONFIG_JWT_KEY + " is required");
            }

            if (apiBaseURL != null) {
                this.baseURL = HttpUrl.parse(apiBaseURL);
            } else {
                this.baseURL = HttpUrl.parse(Oddworks.DEFAULT_API_BASE_URL);
            }
            Log.i("Oddworks", " - using host: " + this.baseURL);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RestServicesNotInitialized(Oddworks.CONFIG_JWT_KEY + " and " + Oddworks.API_BASE_URL_KEY + " are required");
        }
    }

    /** get an authorized (if AuthToken is set) get request for the endpoint */
    private Request getOddGetRequest(HttpUrl endpoint) {
        return getOddRequest(endpoint, RequestMethod.GET, null);
    }

    private Request getOddRequest(HttpUrl endpoint, RequestMethod method, RequestBody body) {
        Request.Builder builder = new Request.Builder();

        builder.url(endpoint.toString())
                .addHeader("authorization", getAuthorization())
                .addHeader("x-odd-user-agent", getOddUserAgent())
                .addHeader("accept", acceptHeader)
                .addHeader("accept-language", locale);

        if(method == RequestMethod.POST) {
            builder.post(body);
        }
        return builder.build();
    }

    private String getAuthorization() {
        // TODO - logic to swap out with new JWT
        return "Bearer " + configJWT;
    }

    private String getOddUserAgent() {
        String oddUserAgent = "";

        oddUserAgent += "platform[name]=Google";
        oddUserAgent += "&model[name]=" + Build.MANUFACTURER;
        oddUserAgent += "&model[version]=" + Build.MODEL;
        oddUserAgent += "&os[name]=Android";
        oddUserAgent += "&os[version]=" + Build.VERSION.RELEASE;
        oddUserAgent += "&build[version]=" + versionName;

        return oddUserAgent;
    }

    private void enqueueOddCall(Request request, Callback callback) {
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    protected void getConfig(final Callback callback) {
        HttpUrl.Builder builder = baseURL.newBuilder();
        HttpUrl endpoint = withPath(builder, Oddworks.ENDPOINT_CONFIG).build();

        Request request = getOddRequest(
                endpoint,
                RequestMethod.GET,
                null);
        enqueueOddCall(request, callback);
    }

    protected void getView(final String id, final Callback callback) {
        String viewPath = String.format(Oddworks.ENDPOINT_VIEW, id);

        HttpUrl.Builder builder = withIncluded(baseURL.newBuilder());
        HttpUrl endpoint = withPath(builder, viewPath).build();

        Request request = getOddGetRequest(endpoint);
        enqueueOddCall(request, callback);
    }

    protected void getCollectionEntities(String collectionId, Callback callback) {
        String path = String.format(Oddworks.ENDPOINT_COLLECTION_ENTITIES, collectionId);

        HttpUrl endpoint = withPath(baseURL.newBuilder(), path).build();
        Request request = getOddGetRequest(endpoint);
        enqueueOddCall(request, callback);
    }

    protected void getSearch(String term, int limit, int offset, Callback callback) {
        HttpUrl endpoint = withPath(baseURL.newBuilder(), Oddworks.ENDPOINT_SEARCH)
                .addQueryParameter(Oddworks.QUERY_PARAM_TERM, term)
                .addQueryParameter(Oddworks.QUERY_PARAM_LIMIT, String.valueOf(limit))
                .addQueryParameter(Oddworks.QUERY_PARAM_OFFSET, String.valueOf(offset))
                .build();
        Request request = getOddGetRequest(endpoint);
        enqueueOddCall(request, callback);
    }

    protected void getCollection(String collectionId, Callback callback) {
        String path = String.format(Oddworks.ENDPOINT_COLLECTION, collectionId);
        HttpUrl.Builder builder = withIncluded(baseURL.newBuilder());
        HttpUrl endpoint = withPath(builder, path).build();
        Request request = getOddGetRequest(endpoint);
        enqueueOddCall(request, callback);
    }

    protected void postMetric(OddMetric event, Callback callback) {
        HttpUrl endpoint = withPath(baseURL.newBuilder(), Oddworks.ENDPOINT_EVENTS).build();

        Request request = getOddRequest(endpoint, RequestMethod.POST, RequestBody.create(JSON, event.toJSONObject().toString()));
        enqueueOddCall(request, callback);
    }

    protected void getVideo(String videoId, Callback callback) {
        String path = String.format(Oddworks.ENDPOINT_VIDEO, videoId);
        HttpUrl.Builder builder = withIncluded(baseURL.newBuilder());
        HttpUrl endpoint = withPath(builder, path).build();
        Request request = getOddGetRequest(endpoint);
        enqueueOddCall(request, callback);
    }

    protected void getPromotion(String promotionId, Callback callback, boolean fetchIncluded) {
        String path = String.format(Oddworks.ENDPOINT_PROMOTION, promotionId);
        HttpUrl.Builder builder = null;
        if(fetchIncluded)
            builder = withIncluded(baseURL.newBuilder());
        else
            builder = baseURL.newBuilder();
        HttpUrl endpoint = withPath(builder, path).build();
        Request request = getOddGetRequest(endpoint);
        enqueueOddCall(request, callback);
    }

    /**
     * adds query parameter includ=true to endpoint string.
     * @param endpoint endpoint string can optionally contain query params
     */
    private HttpUrl.Builder withIncluded(HttpUrl.Builder endpoint) {
        return endpoint.addQueryParameter(Oddworks.QUERY_PARAM_INCLUDE, "true");
    }

    private HttpUrl.Builder withPath(HttpUrl.Builder endpoint, String path) {
        String[] parts = path.split("/");
        for (String part : parts) {
            endpoint.addPathSegment(part);
        }
        return endpoint;
    }
}
