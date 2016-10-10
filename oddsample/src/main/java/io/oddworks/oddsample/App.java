package io.oddworks.oddsample;

import android.app.Application;

import io.oddworks.device.Oddworks;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Oddworks.initialize(this);
    }
}
