package io.oddworks.device.service;

import android.support.annotation.NonNull;
import android.util.Log;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Bus implementation using RxJava. Replacement for OddBus
 *
 * @author Dan Pallas
 * @since v1.2 on 02/26/2016
 */
public class OddRxBus {
    public static final String TAG = OddRxBus.class.getSimpleName();
    private static final OddRxBus INSTANCE = new OddRxBus();


    private SerializedSubject<OddRxBusEvent, OddRxBusEvent> bus =
            new SerializedSubject<>(PublishSubject.<OddRxBusEvent>create());
    private OddRxBus() { /* singleton */ }

    @NonNull public static OddRxBus getInstance() {
        return INSTANCE;
    }

    public void publish(@NonNull OddRxBusEvent event) {
        bus.onNext(event);
        Log.d(TAG, "event published:" + event);
    }

    /** events will be emitted through this observable */
    @NonNull public Observable<OddRxBusEvent> getObservable() {
        return bus.asObservable();
    }

    /** for type safety and hinting */
    public interface OddRxBusEvent { }
}
