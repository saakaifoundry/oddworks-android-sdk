package io.oddworks.device.request;

import android.content.Context;

import io.oddworks.device.R;

/**
 * Class to initialize services in the io.oddworks.device.request package
 */
public class RestServiceProvider {
    private static RestServiceProvider instance;

    private ApiCaller apiCaller;
    private AuthenticationService authenticationService;

    private RestServiceProvider(Context context, String apiVersion, String accessToken, String appVersion,
                                boolean useStagingApi) {
        OddParser.instance = new OddParser();
        String baseUrl = useStagingApi ? context.getString(R.string.odd_base_url_staging, apiVersion) :
                                        context.getString(R.string.odd_base_url, apiVersion);
        RequestHandler.instance = new RequestHandler(context, baseUrl, accessToken, appVersion);
        ApiCaller.instance = new ApiCaller(RequestHandler.instance, OddParser.instance);
        this.apiCaller = ApiCaller.instance;
        AuthenticationService.instance = new AuthenticationService(ApiCaller.instance, context);
        this. authenticationService = AuthenticationService.instance;
    }

    /** initialize services and this provider
     * @param apiVersion OddWorks API version. e.g. "v1"
     * @param accessToken OddWorks API access token
     * @param appVersion git revision or app version string
     * @param useStagingApi if true, api calls will use the staging api */
    public static void init(Context context, String apiVersion, String accessToken, String appVersion,
                            boolean useStagingApi) {
        instance = new RestServiceProvider(context, apiVersion, accessToken, appVersion, useStagingApi);
    }

    /** initialize services and this provider
     * @param apiVersion OddWorks API version. e.g. "v1"
     * @param accessToken OddWorks API access token
     * @param appVersion git revision or app version string */
    public static void init(Context context, String apiVersion, String accessToken, String appVersion) {
        init(context, apiVersion, accessToken, appVersion, false);
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

    public AuthenticationService getAuthenticationService() {
        return this.authenticationService;
    }
}