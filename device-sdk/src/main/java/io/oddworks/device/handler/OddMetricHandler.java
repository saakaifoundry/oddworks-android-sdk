package io.oddworks.device.handler;

import android.util.Log;

import com.squareup.otto.Subscribe;

import io.oddworks.device.metric.OddMetric;
import io.oddworks.device.request.ApiCaller;
import io.oddworks.device.request.OddCallback;
import io.oddworks.device.request.RestServiceProvider;
import io.oddworks.device.service.OddBus;

/**
 * A singleton that handles the HTTP POST calls for OddMetric events.
 * 
 * This class must be explicitly enabled, otherwise it will not listen
 * for events posted to the OddBus.
 * 
 * Once enabled, any OddMetric object that is picked up on the OddBus
 * will automatically be sent back to Oddworks via the ApiCaller.
 * 
 **/
public class OddMetricHandler {
    private static final String TAG = OddMetricHandler.class.getSimpleName();
    private static final OddMetricHandler INSTANCE = new OddMetricHandler();

    private OddMetricHandler() {
        // singleton
    }

    /**
     * Registers the instance of OddMetricHandler on the OddBus
     * so it can begin receiving posted event objects.
     **/
    public static void enable() {
        OddBus.getInstance().register(INSTANCE);
    }

    @Subscribe
    public void handleOddMetric(OddMetric metric) {
        RestServiceProvider restServiceProvider = RestServiceProvider.getInstance();
        ApiCaller apiCaller = restServiceProvider.getApiCaller();

        if (!metric.getEnabled()) {
            // do not post the metric if it is disabled
            Log.d(TAG, "handleOddMetric: " + metric.getClass().getSimpleName() + " disabled");
            return;
        }

        apiCaller.postMetric(metric, new OddCallback<OddMetric>() {
            @Override
            public void onSuccess(OddMetric entity) {
                Log.d(TAG, "handleOddMetric: SUCCESS " + entity.toString());
            }

            @Override
            public void onFailure(Exception exception) {
                Log.d(TAG, "handleOddMetric: FAILURE " + exception.toString());
            }
        });
    }
}
