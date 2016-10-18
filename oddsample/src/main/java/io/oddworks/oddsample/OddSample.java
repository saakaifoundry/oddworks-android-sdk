package io.oddworks.oddsample;

import android.app.Application;

import io.oddworks.device.handler.OddMetricHandler;
import io.oddworks.device.metric.OddAppInitMetric;
import io.oddworks.device.service.OddRxBus;


public class OddSample extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable handling of published analytics events
        OddMetricHandler.INSTANCE.enableRx(this);


        OddAppInitMetric metric = new OddAppInitMetric();
        metric.setOrganizationId("oddsample");

        OddRxBus.INSTANCE.publish(metric);
    }
}
