package io.oddworks.oddsample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.oddworks.device.Oddworks;
import io.oddworks.device.model.OddConfig;
import io.oddworks.device.model.OddView;
import io.oddworks.device.model.common.OddRelationship;
import io.oddworks.device.model.common.OddResourceType;
import io.oddworks.device.request.ApiCaller;
import io.oddworks.device.request.OddCallback;
import io.oddworks.device.request.OddRequest;
import io.oddworks.device.request.RxOddCall;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeOddData();
    }

    private void initializeOddData() {
        final ApiCaller apiCaller = Oddworks.getApiCaller();
        final Context ctx = this;
        RxOddCall
                .observableFrom(new Action1<OddCallback<OddConfig>>() {
                    @Override
                    public void call(OddCallback<OddConfig> configOddCallback) {
                        new OddRequest.Builder(ctx)
                                .resourceType(OddResourceType.CONFIG)
                                .build()
                                .enqueueRequest(configOddCallback);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OddConfig>() {
                    @Override
                    public void call(OddConfig oddConfig) {
                        String viewId = oddConfig.getViews().get("homepage");
                        getHomepage(viewId);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "getConfig failed", throwable);
                    }
                });
    }

    private void getHomepage(final String viewId) {
        final ApiCaller apiCaller = Oddworks.getApiCaller();
        final Context ctx = this;
        RxOddCall
                .observableFrom(new Action1<OddCallback<OddView>>() {

                    @Override
                    public void call(OddCallback<OddView> oddViewOddCallback) {
                        new OddRequest.Builder(ctx)
                                .resourceType(OddResourceType.VIEW)
                                .resourceId(viewId)
                                .fromCache(true)
                                .build()
                                .enqueueRequest(oddViewOddCallback);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OddView>() {
                    @Override
                    public void call(OddView oddView) {
                        OddRelationship personalitites = oddView.getRelationship("personalities");
                        OddRelationship promotion = oddView.getRelationship("promotion");



                    }
                }, new Action1<Throwable>() {

                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "getView failed", throwable);
                    }
                });
    }
}
