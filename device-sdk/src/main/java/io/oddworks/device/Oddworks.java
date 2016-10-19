package io.oddworks.device;

public class Oddworks {
    public static final String DEFAULT_API_BASE_URL = "https://content.oddworks.io/v2";
    public static final String DEFAULT_ANALYTICS_API_BASE_URL = "https://analytics.oddworks.io";

    public static final String CONFIG_JWT_KEY = "io.oddworks.configJWT";
    public static final String API_BASE_URL_KEY = "io.oddworks.apiBaseURL";
    public static final String ANALYTICS_API_BASE_URL_KEY = "io.oddworks.analyticsApiBaseURL";

    private Oddworks() {
        throw new AssertionError();
    }
}
