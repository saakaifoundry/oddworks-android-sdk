package io.oddworks.device.handler;

import android.util.Log;

import com.squareup.otto.Subscribe;

import io.oddworks.device.metric.OddMetric;
import io.oddworks.device.request.ApiCaller;
import io.oddworks.device.request.OddCallback;
import io.oddworks.device.request.RestServiceProvider;
import io.oddworks.device.service.OddBus;

public class OddMetricHandler {
    private static final String TAG = OddMetricHandler.class.getSimpleName();
    private static final OddMetricHandler INSTANCE = new OddMetricHandler();

    private OddMetricHandler() {
        // singleton
        OddBus.getInstance().register(this);
    }

    @Subscribe
    public void handleOddMetric(OddMetric metric) {
        RestServiceProvider restServiceProvider = RestServiceProvider.getInstance();
        ApiCaller apiCaller = restServiceProvider.getApiCaller();

        if (!metric.getEnabled()) {
            // do not post the metric if it is disabled
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
