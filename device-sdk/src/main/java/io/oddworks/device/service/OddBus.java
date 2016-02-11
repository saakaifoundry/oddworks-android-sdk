package io.oddworks.device.service;

import com.squareup.otto.Bus;

public class OddBus {
    private static final String TAG = OddBus.class.getSimpleName();
    private static final Bus INSTANCE = new Bus();

    private OddBus() {
        // singleton
    }

    public static Bus getInstance() {
        return INSTANCE;
    }
}
