package io.oddworks.device.request;

import android.content.Context;

/**
 * Class to initialize services in the io.oddworks.device.request package
 */
public class RestServiceProvider {
    private static RestServiceProvider instance;

    private ApiCaller apiCaller;
    private AuthenticationService authenticationService;

    private RestServiceProvider(Context context, String apiVersion, String accessToken, String appVersion) {
        OddParser.instance = new OddParser();
        RequestHandler.instance = new RequestHandler(context, apiVersion, accessToken, appVersion);
        ApiCaller.instance = new ApiCaller(RequestHandler.instance, OddParser.instance);
        this.apiCaller = ApiCaller.instance;
        AuthenticationService.instance = new AuthenticationService(ApiCaller.instance, context);
        this. authenticationService = AuthenticationService.instance;
    }

    /** initialize services and this provider
     * @param apiVersion OddWorks API version. e.g. "v1"
     * @param accessToken OddWorks API access token
     * @param appVersion git revision or app version string*/
    public static void init(Context context, String apiVersion, String accessToken, String appVersion) {
        instance = new RestServiceProvider(context, apiVersion, accessToken, appVersion);
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