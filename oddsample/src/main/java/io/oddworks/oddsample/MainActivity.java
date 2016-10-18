package io.oddworks.oddsample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;

import io.oddworks.device.exception.BadResponseCodeException;
import io.oddworks.device.model.OddCollection;
import io.oddworks.device.model.OddConfig;
import io.oddworks.device.model.OddError;
import io.oddworks.device.model.OddPromotion;
import io.oddworks.device.model.OddVideo;
import io.oddworks.device.model.OddView;
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

    /**
     * example using plain OddCallback
     */
    private void initializeOddData() {
        final Context ctx = this;

        OddCallback<OddConfig> configCallback = new OddCallback<OddConfig>() {
            @Override
            public void onSuccess(OddConfig resource) {
                String viewId = resource.getViews().get("homepage");
                getHomepage(viewId);
            }

            @Override
            public void onFailure(@NotNull Exception exception) {
                handleRequestException("getConfig", exception);
            }
        };

        new OddRequest.Builder(ctx, OddResourceType.CONFIG)
                .build()
                .enqueueRequest(configCallback);
    }

    /**
     * example using RxOddCall, wrapping OddRequest in an RxJava Observable
     */
    private void getHomepage(final String viewId) {
        final Context ctx = this;
        RxOddCall
                .observableFrom(new Action1<OddCallback<OddView>>() {
                    @Override
                    public void call(OddCallback<OddView> oddCallback) {
                        new OddRequest.Builder(ctx, OddResourceType.VIEW)
//                                .resourceId(viewId)
                                .resourceId("unknown-view-id")
                                .include("personalities,promotion")
                                .build()
                                .enqueueRequest(oddCallback);
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
                        getPersonalityEntities(personality1.getIdentifier().getId());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        handleRequestException("getHomepage", throwable);
                    }
                });
    }

    private void getVideos() {
        final Context ctx = this;
        RxOddCall
                .observableFrom(new Action1<OddCallback<LinkedHashSet<OddVideo>>>() {

                    @Override
                    public void call(OddCallback<LinkedHashSet<OddVideo>> oddCallback) {
                        new OddRequest.Builder(ctx, OddResourceType.VIDEO)
                                .build()
                                .enqueueRequest(oddCallback);
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
                        handleRequestException("getVideos", throwable);
                    }
                });
    }

    private void getPersonalityEntities(final String personalityId) {
        final Context ctx = this;
        RxOddCall
                .observableFrom(new Action1<OddCallback<LinkedHashSet<OddResource>>>() {
                    @Override
                    public void call(OddCallback<LinkedHashSet<OddResource>> oddCallback) {
                        new OddRequest.Builder(ctx, OddResourceType.COLLECTION)
                                .resourceId(personalityId)
                                .relationshipName(OddCollection.RELATIONSHIP_ENTITIES)
                                .build()
                                .enqueueRequest(oddCallback);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LinkedHashSet<OddResource>>() {
                    @Override
                    public void call(LinkedHashSet<OddResource> oddResources) {
                        Log.d(TAG, "get collection entities success " + oddResources.size());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        handleRequestException("getPersonalityEntities", throwable);
                    }
                });
    }

    private void handleRequestException(String requestType, Throwable throwable) {
        Log.w(TAG, requestType + " request failed", throwable);
        if (throwable instanceof BadResponseCodeException) {
            int code = ((BadResponseCodeException) throwable).getCode();
            LinkedHashSet<OddError> errors = ((BadResponseCodeException) throwable).getOddErrors();
            Log.w(TAG, requestType + " request failed - code: " + code + " errors: " + errors);
        }
    }
}
