package io.oddworks.device.handler;

import android.content.Context;
import android.util.Log;

import io.oddworks.device.metric.OddMetric;
import io.oddworks.device.model.common.OddResourceType;
import io.oddworks.device.request.OddCallback;
import io.oddworks.device.request.OddRequest;
import io.oddworks.device.service.OddRxBus;
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
    public void enableRx(final Context context) {
        Observable<OddRxBus.OddRxBusEvent> observable = OddRxBus.INSTANCE.getObservable();
        final OddCallback<OddMetric> oddMetricCallback = new OddCallback<OddMetric>() {
            @Override
            public void onSuccess(OddMetric entity) {
                Log.d(TAG, "handleOddMetric: SUCCESS " + entity.toString());
            }

            @Override
            public void onFailure(Exception exception) {
                Log.d(TAG, "handleOddMetric: FAILURE " + exception.toString());
            }
        };

        observable
                .observeOn(Schedulers.io())
                .subscribe(new Action1<OddRxBus.OddRxBusEvent>() {
                    @Override
                    public void call(OddRxBus.OddRxBusEvent event) {
                        if (event instanceof OddMetric) {
                            new OddRequest.Builder(context, OddResourceType.EVENT).event((OddMetric) event).build().enqueueRequest(oddMetricCallback);
                        }
                    }
                });
    }
}
