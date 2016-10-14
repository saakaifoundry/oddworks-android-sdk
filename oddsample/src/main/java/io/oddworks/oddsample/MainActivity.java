package io.oddworks.oddsample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.LinkedHashSet;

import io.oddworks.device.model.OddCollection;
import io.oddworks.device.model.OddConfig;
import io.oddworks.device.model.OddPromotion;
import io.oddworks.device.model.OddVideo;
import io.oddworks.device.model.OddView;
import io.oddworks.device.model.common.OddRelationship;
import io.oddworks.device.model.common.OddResource;
import io.oddworks.device.model.common.OddResourceType;
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
                        Log.e(TAG, "get config failed", throwable);
                    }
                });
    }

    private void getHomepage(final String viewId) {
        final Context ctx = this;
        RxOddCall
                .observableFrom(new Action1<OddCallback<OddView>>() {

                    @Override
                    public void call(OddCallback<OddView> oddViewOddCallback) {
                        new OddRequest.Builder(ctx)
                                .resourceType(OddResourceType.VIEW)
                                .resourceId(viewId)
                                .include("personalities,promotion")
                                .build()
                                .enqueueRequest(oddViewOddCallback);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OddView>() {
                    @Override
                    public void call(OddView oddView) {
                        LinkedHashSet<OddResource> personalitites = oddView.getIncludedByRelationship("personalities");
                        LinkedHashSet<OddResource> promotions = oddView.getIncludedByRelationship("promotion");

                        OddPromotion promotion = (OddPromotion) promotions.iterator().next();
                        OddCollection personality1 = (OddCollection) personalitites.iterator().next();

                        Log.d(TAG, "promotion: " + promotion.getTitle());
                        Log.d(TAG, "first personality: " + personality1.getTitle());
                        getVideos();
                    }
                }, new Action1<Throwable>() {

                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "get view failed", throwable);
                    }
                });
    }

    private void getVideos() {
        final Context ctx = this;
        RxOddCall
                .observableFrom(new Action1<OddCallback<LinkedHashSet<OddVideo>>>() {

                    @Override
                    public void call(OddCallback<LinkedHashSet<OddVideo>> oddVideoCallback) {
                        new OddRequest.Builder(ctx)
                                .resourceType(OddResourceType.VIDEO)
                                .build()
                                .enqueueRequest(oddVideoCallback);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LinkedHashSet<OddVideo>>() {
                    @Override
                    public void call(LinkedHashSet<OddVideo> videos) {
                        Log.d(TAG, "get videos success " + videos.size());
                    }
                }, new Action1<Throwable>() {

                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "get videos failed", throwable);
                    }
                });
    }
}
