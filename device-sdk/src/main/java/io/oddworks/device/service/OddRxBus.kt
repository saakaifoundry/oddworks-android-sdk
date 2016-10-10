package io.oddworks.device.service

import android.util.Log

import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject

/**
 * Bus implementation using RxJava. Replacement for OddBus

 * @author Dan Pallas
 * *
 * @since v1.2 on 02/26/2016
 */
class OddRxBus private constructor() {

    private val bus: SerializedSubject<OddRxBusEvent, OddRxBusEvent>

    init {
        bus = SerializedSubject(PublishSubject.create<OddRxBusEvent>())
        bus.onBackpressureBuffer()
    }

    fun publish(event: OddRxBusEvent) {
        bus.onNext(event)
        Log.d(TAG, "event published:" + event)
    }

    /** events will be emitted through this observable  */
    val observable: Observable<OddRxBusEvent>
        get() = bus.asObservable()

    /** for type safety and hinting  */
    interface OddRxBusEvent

    companion object {
        val TAG = OddRxBus::class.java.simpleName
        val instance = OddRxBus()
    }
}
