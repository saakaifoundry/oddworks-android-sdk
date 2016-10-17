package io.oddworks.device.handler

import android.content.Context
import android.util.Log

import io.oddworks.device.metric.OddMetric
import io.oddworks.device.model.common.OddResourceType
import io.oddworks.device.request.OddCallback
import io.oddworks.device.request.OddRequest
import io.oddworks.device.service.OddRxBus
import rx.Observable
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 * A singleton that handles the HTTP POST calls for OddMetric events.

 * This class must be explicitly enabled, otherwise it will not listen
 * for events posted to the OddBus.

 * Once enabled, any OddMetric object that is picked up on the OddBus
 * will automatically be sent back to Oddworks via the ApiCaller.

 * @author Erik Straub, Dan Pallas
 * *
 * @since v1.0
 */
object OddMetricHandler {

    private val TAG = OddMetricHandler::class.java.simpleName

    /**
     * Registers the instance of OddMetricHandler on the OddRxBus
     * so it can begin receiving posted event objects.
     */
    fun enableRx(context: Context) {
        val observable = OddRxBus.observable
        val oddMetricCallback = object : OddCallback<OddMetric> {
            override fun onSuccess(resource: OddMetric) {
                Log.d(TAG, "handleOddMetric: SUCCESS $resource}")
            }

            override fun onFailure(exception: Exception) {
                Log.d(TAG, "handleOddMetric: FAILURE $exception")
            }
        }

        observable.observeOn(Schedulers.io()).subscribe { event ->
            if (event is OddMetric) {
                OddRequest.Builder(context, OddResourceType.EVENT)
                        .event(event)
                        .build()
                        .enqueueRequest(oddMetricCallback)
            }
        }
    }
}