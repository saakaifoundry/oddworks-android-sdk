package io.oddworks.device.request;

import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import io.oddworks.device.exception.BadResponseCodeException;
import io.oddworks.device.exception.OddParseException;
import io.oddworks.device.model.common.OddIdentifier;
import io.oddworks.device.model.common.OddRelationship;
import io.oddworks.device.model.common.OddResource;

/**
 * This class contains methods for fetching data from the API Server with automatic caching. Http Cache-Control headers
 * returned by the API are respected. Whenever possible, this should be used instead of the ApiCaller.
 *
 * @author Dan Pallas
 * @since 1.4 on 03/02/2016
 */
public class CachingApiCaller {
    public static final String AUTH_TOKEN_MISMATCH = "DeviceID accessToken/authorizationToken mismatch";
    private final OddResourceCache cache;
    private final OddParser parser;
    private final RequestHandler requestHandler;

    protected CachingApiCaller(RequestHandler requestHandler,
                               OddParser parser,
                               OddResourceCache cache) {
        this.cache = cache;
        this.parser = parser;
        this.requestHandler = requestHandler;
    }

//    /**
//     * @param forceFetchFromApi if true, do not attempt to get from cache. Get directly from api
//     */
//    public void getPromotion(@NonNull String id,
//                             @NonNull OddCallback<OddPromotion> cb,
//                             boolean forceFetchFromApi) {
//        OddPromotion stored = (OddPromotion)cache.getObject(id);
//        if(!forceFetchFromApi && stored != null) {
//            cb.onSuccess(stored);
//        } else {
//            getPromotionFromApi(id, cb, false);
//        }
//    }
//
//
//    public void getPromotion(@NonNull String id, @NonNull OddCallback<OddPromotion> cb) {
//        getPromotion(id, cb, false);
//    }
//
//    /**
//     * @param forceFetchFromApi if true, do not attempt to get from cache. Get directly from api
//     */
//    public void getProtionWithAllRelated(@NonNull final String id,
//                                         @NonNull final OddCallback<ObjectWithRelated<OddPromotion>> cb,
//                                         boolean forceFetchFromApi) {
//        if(forceFetchFromApi || !completeGetAllRelatedFromCache(id, cb))
//            getPromotionFromApi(id, new GatherAllRelatedCb<OddPromotion>(cb), true);
//    }
//
//    /**
//     * @param forceFetchFromApi if true, do not attempt to get from cache. Get directly from api
//     */
//    public void getProtionWithRelationship(@NonNull final String id,
//                                         @NonNull final OddCallback<ObjectWithRelated<OddPromotion>> cb,
//                                         @NonNull final String relationshipName,
//                                         boolean forceFetchFromApi) {
//        if(forceFetchFromApi || !completeGetRelationshipFromCache(id, relationshipName, cb))
//            getPromotionFromApi(id, new GatherRelationshipCb<OddPromotion>(cb, relationshipName), true);
//    }
//
//    private void getPromotionFromApi(String promotionId, final OddCallback<OddPromotion> cb, boolean fetchIncluded) {
//        Callback requestCallback = new RequestCallback<>(cb, new ParseCall<OddPromotion>() {
//            @Override
//            public OddPromotion parse(String responseBody) {
//                return parser.parsePromotionResponse(responseBody);
//            }
//        });
//        requestHandler.getPromotion(promotionId, requestCallback, fetchIncluded);
//    }

//
//    /**
//     * @param forceFetchFromApi if true, do not attempt to get from cache. Get directly from api
//     */
//    public void getCollection(@NonNull String id,
//                              @NonNull OddCallback<OddCollection> cb,
//                              boolean forceFetchFromApi) {
//        OddCollection stored = (OddCollection)cache.getObject(id);
//        if(!forceFetchFromApi && stored != null) {
//            cb.onSuccess(stored);
//        } else {
//            getCollectionFromApi(id, cb, false);
//        }
//    }
//
//    public void getCollection(@NonNull String id, @NonNull OddCallback<OddCollection> cb) {
//        getCollection(id, cb, false);
//    }
//
//    /**
//     * @param forceFetchFromApi if true, do not attempt to get from cache. Get directly from api
//     */
//    public void getCollectionWithAllRelated(@NonNull final String id,
//                                            @NonNull final OddCallback<ObjectWithRelated<OddCollection>> cb,
//                                            boolean forceFetchFromApi) {
//        if(forceFetchFromApi || !completeGetAllRelatedFromCache(id, cb))
//            getCollectionFromApi(id, new GatherAllRelatedCb<OddCollection>(cb), true);
//    }
//
//    /**
//     * @param forceFetchFromApi if true, do not attempt to get from cache. Get directly from api
//     */
//    public void getCollectionWithRelationship(@NonNull final String id,
//                                              @NonNull final OddCallback<ObjectWithRelated<OddCollection>> cb,
//                                              @NonNull final String relationshipName,
//                                              boolean forceFetchFromApi) {
//        if(forceFetchFromApi || !completeGetRelationshipFromCache(id, relationshipName, cb))
//            getCollectionFromApi(id, new GatherRelationshipCb<>(cb, relationshipName), true);
//    }
//
//    private void getCollectionFromApi(String id, OddCallback<OddCollection> cb, boolean fetchIncluded) {
//        Callback requestCallback = new RequestCallback<>(cb, new ParseCall<OddCollection>() {
//            @Override
//            public OddCollection parse(String responseBody) {
//                return parser.parseCollectionResponse(responseBody);
//            }
//        });
//        requestHandler.getCollection(id, requestCallback);
//    }
//
//    /**
//     * @param forceFetchFromApi if true, do not attempt to get from cache. Get directly from api
//     **/
//    public void getVideo(@NonNull String id,
//                         @NonNull OddCallback<OddVideo> cb,
//                         boolean forceFetchFromApi) {
//        OddVideo stored = (OddVideo)cache.getObject(id);
//        if(!forceFetchFromApi && stored != null) {
//            cb.onSuccess(stored);
//        } else {
//            getVideoFromApi(id, cb, false);
//        }
//    }
//
//    /**
//     * @param forceFetchFromApi if true, do not attempt to get from cache. Get directly from api
//     */
//    public void getVideoWithAllRelated(@NonNull final String id,
//                                       @NonNull final OddCallback<ObjectWithRelated<OddVideo>> cb,
//                                       boolean forceFetchFromApi) {
//        if(forceFetchFromApi || !completeGetAllRelatedFromCache(id, cb))
//            getVideoFromApi(id, new GatherAllRelatedCb<>(cb), true);
//    }
//
//    /**
//     * @param forceFetchFromApi if true, do not attempt to get from cache. Get directly from api
//     */
//    public void getVideoWithRelationship(@NonNull final String id,
//                                         @NonNull final OddCallback<ObjectWithRelated<OddVideo>> cb,
//                                         @NonNull final String relationshipName,
//                                         boolean forceFetchFromApi) {
//        if(forceFetchFromApi || !completeGetRelationshipFromCache(id, relationshipName, cb))
//            getVideoFromApi(id, new GatherRelationshipCb<>(cb, relationshipName), true);
//    }
//
//    private void getVideoFromApi(String id, OddCallback<OddVideo> cb, boolean fetchIncluded) {
//        Callback requestCallback = new RequestCallback<>(cb, new ParseCall<OddVideo>() {
//            @Override
//            public OddVideo parse(String responseBody) {
//                return parser.parseMediaResponse(responseBody);
//            }
//        });
//
//        requestHandler.getVideo(id, requestCallback);
//    }
//
//    /**
//     * @param forceFetchFromApi if true, do not attempt to get from cache. Get directly from api */
//    public void getView(@NonNull String id,
//                        @NonNull OddCallback<OddView> cb,
//                        boolean forceFetchFromApi) {
//        OddView stored = (OddView)cache.getObject(id);
//        if(!forceFetchFromApi && stored != null) {
//            cb.onSuccess(stored);
//        } else {
//            getViewFromApi(id, cb, true);
//        }
//    }
//
//    public void getView(@NonNull String id, @NonNull OddCallback<OddView> cb) {
//        getView(id, cb, false);
//    }
//
//    /**
//     * @param forceFetchFromApi if true, do not attempt to get from cache. Get directly from api
//     */
//    public void getViewWithAllRelated(@NonNull final String id,
//                                       @NonNull final OddCallback<ObjectWithRelated<OddView>> cb,
//                                       boolean forceFetchFromApi) {
//        if(forceFetchFromApi || !completeGetAllRelatedFromCache(id, cb))
//            getViewFromApi(id, new GatherAllRelatedCb<>(cb), true);
//    }
//
//    /**
//     * @param forceFetchFromApi if true, do not attempt to get from cache. Get directly from api
//     */
//    public void getViewWithRelationship(@NonNull final String id,
//                                         @NonNull final OddCallback<ObjectWithRelated<OddView>> cb,
//                                         @NonNull final String relationshipName,
//                                         boolean forceFetchFromApi) {
//        if(forceFetchFromApi || !completeGetRelationshipFromCache(id, relationshipName, cb))
//            getViewFromApi(id, new GatherRelationshipCb<>(cb, relationshipName), true);
//    }
//
//    private void getViewFromApi(String id, OddCallback<OddView> cb, boolean fetchIncluded) {
//        Callback requestCallback = new RequestCallback<>(cb, new ParseCall<OddView>() {
//            @Override
//            public OddView parse(String responseBody) {
//                return parser.parseViewResponse(responseBody);
//            }
//        });
//        requestHandler.getView(id, requestCallback);
//    }

    /** attempt to complete callback with data from cache
     * @return true if all data was in the cache so the callback was completed, otherwise false */
    private <T extends OddResource> boolean completeGetAllRelatedFromCache (
            @NonNull String id, @NonNull OddCallback<ObjectWithRelated<T>> cb) {
        T stored = (T)cache.getObject(id);
        if (stored != null) {
            Set<OddResource> related = cache.getRelatedObjectsOrNull(stored);
            if (related != null) {
                cb.onSuccess(new ObjectWithRelated<>(stored, related));
                return true;
            }
        }
        return false;
    }

    /** attempt to complete callback with data from cache
     * @return true if all data was in the cache so the callback was completed, otherwise false */
    private <T extends OddResource> boolean completeGetRelationshipFromCache (
            @NonNull String id, @NonNull String relationshipName, @NonNull OddCallback<ObjectWithRelated<T>> cb) {
        T stored = (T)cache.getObject(id);
        if (stored != null) {
            Set<OddIdentifier> ids = stored.getIdentifiersByRelationship(relationshipName);
            if(ids != null) {
                Set<OddResource> relatedList = new HashSet<>(ids.size());
                for (OddIdentifier identifier : ids) {
                    OddResource related = cache.getObject(identifier);
                    if(related != null)
                        relatedList.add(related);
                    else
                        return false; // at least one object was null so the cb could not be completed from the cache
                }
                cb.onSuccess(new ObjectWithRelated<T>(stored, relatedList));
                return true;
            }
        }
        return false;
    }

    /** returns a list of all objects which are both included in this object and included in a 1st level relationship */
    private Set<OddResource> getAllRelated(OddResource object) {
        Set<OddResource> related = new LinkedHashSet<>();
        for (OddRelationship relationship : object.getRelationships()) {
            Set<OddResource> relationshipEntities = object.getIncludedByRelationship(relationship.getName());
            related.addAll(relationshipEntities);
        }
        return related;
    }

    protected class RequestCallback<T> implements Callback {

        private static final int MS_IN_SECONDS = 1000;
        private final ParseCall<T> parseCall;
        private final OddCallback<T> cb;

        public RequestCallback(final OddCallback<T> cb, final ParseCall<T> parseCall) {
            this.cb = cb;
            this.parseCall = parseCall;
        }

        @Override
        public void onFailure(Request request, IOException e) {
            Log.e("ResponseCb onFailure", "Failed", e);
            cb.onFailure(e);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            Log.d("ResponseCb onResponse", "code :"
                    + response.code() + " responseBody: " + response);
            if (response.isSuccessful()) {
                handleOk(response);
            } else {
                handleNotOk(response);
            }
        }

        private void handleNotOk(Response response) {
            cb.onFailure(new BadResponseCodeException(response.code()));
        }

        private void handleOk(Response response) {
            T obj = null;
            Exception exceptionCaught = null;
            String bodyString = null;
            try {
                obj = parseCall.parse(response.body().string());
                attemptCache(response, obj);
            } catch (Exception e) {
                exceptionCaught = e;
                bodyString = tryGetResponseBody(response);
            }

            if(exceptionCaught != null) {
                cb.onFailure(new OddParseException(
                        "Response body parse failed: " + bodyString, exceptionCaught));
            } else {
                cb.onSuccess(obj);
            }
        }

        /** only caches if obj is OddResource and the response header allows for caching. */
        private void attemptCache(Response response, T obj) {
            if(obj instanceof OddResource &&
                    (!response.cacheControl().noCache() || !response.cacheControl().noStore())) {
                cache.storeObject((OddResource) obj, response.cacheControl().maxAgeSeconds() * MS_IN_SECONDS);
            }
        }

        private String tryGetResponseBody(Response response) {
            String bodyString;
            try {
                bodyString = response.body().string();
            } catch(Exception e) {
                bodyString = "body could not be captured in this error message." +
                        " This is not the cause of the exception and can be ignored";
            }
            return bodyString;
        }
    }

    protected interface ParseCall<T> {
        T parse(String responseBody) throws JSONException;
    }

    public class ObjectWithRelated<T extends OddResource> {
        private final Set<OddResource> related;
        private final T oddObject;

        public ObjectWithRelated(@NonNull T oddObject, @NonNull Set<OddResource> related) {
            this.oddObject = oddObject;
            this.related = related;
        }

        @NonNull public Set<OddResource> getRelated() {
            return related;
        }

        @NonNull public T getOddResource() {
            return oddObject;
        }
    }

    private class GatherAllRelatedCb<T extends OddResource> implements OddCallback<T> {
        private final OddCallback<ObjectWithRelated<T>> cb;

        private GatherAllRelatedCb(OddCallback<ObjectWithRelated<T>> cb) {
            this.cb = cb;
        }

        @Override
        public void onSuccess(T entity) {
            Set<OddResource> related = getAllRelated(entity);
            cb.onSuccess(new ObjectWithRelated<T>(entity, related));
        }

        @Override
        public void onFailure(Exception exception) {
            cb.onFailure(exception);
        }
    }

    private class GatherRelationshipCb<T extends OddResource> implements OddCallback<T> {
        private final OddCallback<ObjectWithRelated<T>> cb;
        private final String relationshipName;

        private GatherRelationshipCb(OddCallback<ObjectWithRelated<T>> cb, String relationshipName) {
            this.cb = cb;
            this.relationshipName = relationshipName;
        }

        @Override
        public void onSuccess(T entity) {
            Set<OddResource> related = entity.getIncludedByRelationship(relationshipName);
            cb.onSuccess(new ObjectWithRelated<T>(entity, related));
        }

        @Override
        public void onFailure(Exception exception) {
            cb.onFailure(exception);
        }
    }

    public void clearCache() {
        cache.clear();
    }
}
