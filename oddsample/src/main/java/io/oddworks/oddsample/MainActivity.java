package io.oddworks.oddsample;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedHashSet;

import io.oddworks.device.exception.BadResponseCodeException;
import io.oddworks.device.metric.OddAppInitMetric;
import io.oddworks.device.metric.OddViewLoadMetric;
import io.oddworks.device.model.OddCollection;
import io.oddworks.device.model.OddConfig;
import io.oddworks.device.model.OddError;
import io.oddworks.device.model.OddPromotion;
import io.oddworks.device.model.OddVideo;
import io.oddworks.device.model.OddView;
import io.oddworks.device.model.OddViewer;
import io.oddworks.device.model.common.OddResource;
import io.oddworks.device.model.common.OddResourceType;
import io.oddworks.device.request.OddCallback;
import io.oddworks.device.request.OddRequest;
import io.oddworks.device.request.RxOddCall;
import io.oddworks.device.service.OddRxBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getConfig();
    }

    /**
     * example using plain OddCallback
     */
    private void getConfig() {
        OddCallback<OddConfig> configCallback = new OddCallback<OddConfig>() {
            @Override
            public void onSuccess(OddConfig config) {
                String viewId = config.getViews().get("homepage");

                OddAppInitMetric initMetric = new OddAppInitMetric();
                OddViewLoadMetric viewLoadMetric = new OddViewLoadMetric("view", viewId, null);

                OddRxBus.publish(initMetric);
                OddRxBus.publish(viewLoadMetric);

                Log.d(TAG, "getConfig success: " + config);

                if (config.getFeatures().getAuthentication().getEnabled()) {
                    Log.d(TAG, "authentication enabled");
                }
                getAuthentication(config);
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
     * example login using Oddworks and RxOddCall
     */
    private void getAuthentication(final OddConfig config) {
        final String viewId = config.getViews().get("homepage");
        RxOddCall
                .observableFrom(new Action1<OddCallback<OddViewer>>() {
                    @Override
                    public void call(OddCallback<OddViewer> oddViewerCallback) {
                        new OddRequest.Builder(ctx, OddResourceType.VIEWER)
                                .login("paul@oddnetworks.com", "PaulIsPurple")
                                .build()
                                .enqueueRequest(oddViewerCallback);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OddViewer>() {
                   @Override
                   public void call(OddViewer oddViewer) {
                       Log.d(TAG, "getAuthentication success: " + oddViewer);

                       getHomepage(viewId, oddViewer);
                   }
               },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        handleRequestException("getAuthentication", throwable);
                        getHomepage(viewId, null);
                    }
                });
    }

    /**
     * example using RxOddCall, wrapping OddRequest in an RxJava Observable
     */
    private void getHomepage(final String viewId, @Nullable final OddViewer viewer) {
        RxOddCall
                .observableFrom(new Action1<OddCallback<OddView>>() {
                    @Override
                    public void call(OddCallback<OddView> oddCallback) {
                        new OddRequest.Builder(ctx, OddResourceType.VIEW)
                                .resourceId(viewId)
                                .include("featuredCollections,promotion")
                                .build()
                                .enqueueRequest(oddCallback);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OddView>() {
                    @Override
                    public void call(OddView oddView) {
                        LinkedHashSet<OddResource> featuredCollections = oddView.getIncludedByRelationship("featuredCollections");
                        LinkedHashSet<OddResource> promotions = oddView.getIncludedByRelationship("promotion");

                        OddPromotion promotion = (OddPromotion) promotions.iterator().next();
                        OddCollection collection1 = (OddCollection) featuredCollections.iterator().next();

                        Log.d(TAG, "getHomepage success: promotion " + promotion.getTitle() + " collection: " + collection1.getTitle());
                        getVideos(viewer);
                        getCollectionEntities(collection1.getIdentifier().getId());
                        if (viewer != null) {
                            addToWatchlist(viewer, collection1);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        handleRequestException("getHomepage", throwable);
                    }
                });
    }

    private void getVideos(@Nullable final OddViewer viewer) {
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
                        Log.d(TAG, "getVideos success: " + videos.size());
                        Iterator<OddVideo> oddVideoIterator = videos.iterator();
                        if (oddVideoIterator.hasNext()) {
                            OddVideo vid = oddVideoIterator.next();
                            if (viewer != null) {
                                addToWatchlist(viewer, vid);
                                removeFromWatchlist(viewer, vid);
                            }
                        }
                    }
                }, new Action1<Throwable>() {

                    @Override
                    public void call(Throwable throwable) {
                        handleRequestException("getVideos", throwable);
                    }
                });
    }

    private void getCollectionEntities(final String collectionId) {
        RxOddCall
                .observableFrom(new Action1<OddCallback<LinkedHashSet<OddResource>>>() {
                    @Override
                    public void call(OddCallback<LinkedHashSet<OddResource>> oddCallback) {
                        new OddRequest.Builder(ctx, OddResourceType.COLLECTION)
                                .resourceId(collectionId)
                                .relationshipName(OddCollection.RELATIONSHIPS.ENTITIES)
                                .build()
                                .enqueueRequest(oddCallback);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LinkedHashSet<OddResource>>() {
                    @Override
                    public void call(LinkedHashSet<OddResource> oddResources) {
                        Log.d(TAG, "getCollectionEntities success: " + oddResources.size());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        handleRequestException("getCollectionEntities", throwable);
                    }
                });
    }

    private void addToWatchlist(final OddViewer viewer, final OddResource resource) {
        RxOddCall
                .observableFrom(new Action1<OddCallback<OddResource>>() {
                    @Override
                    public void call(OddCallback<OddResource> oddResourceOddCallback) {
                        new OddRequest.Builder(ctx, OddResourceType.WATCHLIST)
                                .addResourceToWatchlist(viewer, resource)
                                .build()
                                .enqueueRequest(oddResourceOddCallback);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OddResource>() {
                    @Override
                    public void call(OddResource oddResource) {
                        Log.d(TAG, "addToWatchlist success: " + oddResource.getIdentifier());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        handleRequestException("addToWatchlist", throwable);
                    }
                });
    }

    private void removeFromWatchlist(final OddViewer viewer, final OddResource resource) {
        RxOddCall
                .observableFrom(new Action1<OddCallback<OddResource>>() {
                    @Override
                    public void call(OddCallback<OddResource> oddResourceOddCallback) {
                        new OddRequest.Builder(ctx, OddResourceType.WATCHLIST)
                                .removeResourceFromWatchlist(viewer, resource)
                                .build()
                                .enqueueRequest(oddResourceOddCallback);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OddResource>() {
                    @Override
                    public void call(OddResource oddResource) {
                        Log.d(TAG, "removeFromWatchlist success: " + oddResource.getIdentifier());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        handleRequestException("removeFromWatchlist", throwable);
                    }
                });
    }

    private void handleRequestException(String requestType, Throwable throwable) {
        Log.w(TAG, requestType + " failed", throwable);
        if (throwable instanceof BadResponseCodeException) {
            int code = ((BadResponseCodeException) throwable).getCode();
            LinkedHashSet<OddError> errors = ((BadResponseCodeException) throwable).getOddErrors();
            Log.w(TAG, requestType + " code: " + code + " errors: " + errors);
        }
    }
}
