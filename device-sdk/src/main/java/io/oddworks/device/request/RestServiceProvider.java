package io.oddworks.device.request;

import android.content.Context;
import android.support.annotation.NonNull;

import net.danlew.android.joda.JodaTimeAndroid;


/**
 * Class to initialize services in the io.oddworks.device.request package
 */
public class RestServiceProvider {
    private static RestServiceProvider instance;

    private ApiCaller apiCaller;
    private CachingApiCaller cachingApiCaller;

    private RestServiceProvider(@NonNull Context context) {
        RequestHandler.instance = new RequestHandler(context);
        ApiCaller.instance = new ApiCaller(RequestHandler.instance, OddParser.INSTANCE);
        this.cachingApiCaller =
                new CachingApiCaller(RequestHandler.instance, OddParser.INSTANCE, new OddResourceCache());
        this.apiCaller = ApiCaller.instance;

        JodaTimeAndroid.init(context);
    }

    /**
     * @param context Application context for accessing resources */
    public static void init(@NonNull Context context) {
        instance = new RestServiceProvider(context);
    }

    /**
     * @return a RestServiceProvider instance if init has already been called. Otherwise returns null.
     */
    public static RestServiceProvider getInstance() {
        return RestServiceProvider.instance;
    }

    public ApiCaller getApiCaller() {
        return this.apiCaller;
    }

    public CachingApiCaller getCachingApiCaller() {
        return this.cachingApiCaller;
    }
}