package io.oddworks.device.request;

import android.content.Context;

/**
 * Class to initialize services in the io.oddworks.device.request package
 */
public class RequestServicesInitializer {
    private final Context context;

    public RequestServicesInitializer(Context context) {
        this.context = context;
    }

    /** initialize services */
    public void initializeServices() {
        OddParser.instance = new OddParser();
        RequestHandler.instance = new RequestHandler(this.context);
        ApiCaller.instance = new ApiCaller(RequestHandler.instance, OddParser.instance);
        AuthenticationService.instance = new AuthenticationService(ApiCaller.instance, this.context);
    }
}
