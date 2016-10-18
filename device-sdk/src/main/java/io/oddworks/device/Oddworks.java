package io.oddworks.device;

import android.content.Context;

/**
 * Created by brkattk on 10/5/16.
 */

public class Oddworks {

    public static final String ENDPOINT_AUTH_DEVICE_CODE = "auth/device/code";
    public static final String ENDPOINT_AUTH_DEVICE_TOKEN = "auth/device/token";
    public static final String ENDPOINT_COLLECTION = "collections/%s";
    public static final String ENDPOINT_VIDEO = "videos/%s";
    public static final String ENDPOINT_LIVE_STREAM = "liveStreams/%s";
    public static final String ENDPOINT_PROMOTION = "promotions/%s";
    public static final String ENDPOINT_EXTERNAL  = "externals/%s";
    public static final String ENDPOINT_EVENT  = "events/%s";
    public static final String ENDPOINT_ARTICLES  = "events/%s";
    public static final String ENDPOINT_COLLECTION_ENTITIES = "collections/%s/relationships/entities";
    public static final String ENDPOINT_CONFIG = "config";
    public static final String ENDPOINT_EVENTS = "events";
    public static final String ENDPOINT_VIEW = "views/%s";
    public static final String ENDPOINT_SEARCH = "search";

    public static final String QUERY_PARAM_INCLUDE = "include";
    public static final String QUERY_PARAM_LIMIT = "limit";
    public static final String QUERY_PARAM_OFFSET = "offset";
    public static final String QUERY_PARAM_TERM = "term";


    public static final String DEFAULT_API_BASE_URL = "https://content.oddworks.io/v2";
    public static final String DEFAULT_ANALYTICS_API_BASE_URL = "https://analytics.oddworks.io";

    public static final String CONFIG_JWT_KEY = "io.oddworks.configJWT";
    public static final String API_BASE_URL_KEY = "io.oddworks.apiBaseURL";

    private Oddworks() {
        throw new AssertionError();
    }
}
