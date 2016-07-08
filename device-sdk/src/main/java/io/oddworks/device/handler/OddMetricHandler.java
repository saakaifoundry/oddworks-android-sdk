package io.oddworks.device.handler;

import android.util.Log;

import com.squareup.otto.Subscribe;

import io.oddworks.device.metric.OddMetric;
import io.oddworks.device.request.ApiCaller;
import io.oddworks.device.request.OddCallback;
import io.oddworks.device.request.RestServiceProvider;
import io.oddworks.device.service.OddBus;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A singleton that handles the HTTP POST calls for OddMetric events.
 * 
 * This class must be explicitly enabled, otherwise it will not listen
 * for events posted to the OddBus.
 * 
 * Once enabled, any OddMetric object that is picked up on the OddBus
 * will automatically be sent back to Oddworks via the ApiCaller.
 *
 * @author Erik Straub, Dan Pallas
 * @since v1.0
 **/
public class OddMetricHandler {
    private static final String TAG = OddMetricHandler.class.getSimpleName();
    private static final OddMetricHandler INSTANCE = new OddMetricHandler();

    private OddMetricHandler() {
        // singleton
    }

    public static OddMetricHandler getInstance() {
        return INSTANCE;
    }

    /**
     * Registers the instance of OddMetricHandler on the OddRxBus
     * so it can begin receiving posted event objects.
     */
    public void enable() {
        Observable<OddBus.OddRxBusEvent> observable = OddBus.getInstance().getObservable();
        observable
                .observeOn(Schedulers.io())
                .subscribe(new Action1<OddBus.OddRxBusEvent>() {
                    @Override
                    public void call(OddBus.OddRxBusEvent event) {
                        if (event instanceof OddMetric) {
                            postMetric((OddMetric) event);
                        }
                    }
                });
    }

    @Subscribe
    public void handleOddMetric(OddMetric metric) {
        if (!metric.getEnabled()) {
            // do not post the metric if it is disabled
            Log.d(TAG, "handleOddMetric: " + metric.getClass().getSimpleName() + " disabled");
            return;
        }

        postMetric(metric);
    }

    private void postMetric(OddMetric metric) {
        RestServiceProvider restServiceProvider = RestServiceProvider.getInstance();
        ApiCaller apiCaller = restServiceProvider.getApiCaller();
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
