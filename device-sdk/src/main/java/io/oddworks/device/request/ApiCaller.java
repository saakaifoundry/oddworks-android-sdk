package io.oddworks.device.request;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;

import io.oddworks.device.exception.BadResponseCodeException;
import io.oddworks.device.exception.OddParseException;
import io.oddworks.device.metric.OddMetric;
import io.oddworks.device.model.OddConfig;
import io.oddworks.device.model.OddView;

/**
 * Class of methods for handling api calls. Is instantiated using the ReqeustServicesInitializer and afterward can be
 * accessed through the instance field
 */
public class ApiCaller {
    public static final String AUTH_TOKEN_MISMATCH = "DeviceID accessToken/authorizationToken mismatch";
    protected static ApiCaller instance;
    public static final int RESPONSE_OK = 200;
    public static final int RESPONSE_CREATED = 201;
    public static final int RESPONSE_NOT_FOUND = 404;


    public static final int MS_IN_SECONDS = 1000;
    public static String TAG = ApiCaller.class.getSimpleName();
    private final RequestHandler requestHandler;
    private final OddParser parser;

    protected ApiCaller(RequestHandler requestHandler, OddParser parser){
        this.requestHandler = requestHandler;
        this.parser = parser;
    }

    public void getConfig(final OddCallback<OddConfig> cb) {
        Callback requestCallback = getRequestCallback(cb, new ParseCall<OddConfig>() {
            @Override
            public OddConfig parse(String responseBody) {
                try {
                    OddConfig oddConfig = (OddConfig) parser.parseSingleResponse(responseBody);

                    // TODO - get and store oddConfig.jwt

                    return oddConfig;
                } catch(JSONException e) {
                    Log.e(TAG, "getConfig parse failed", e);
                    return null;
                } catch (OddParseException e) {
                    Log.e(TAG, "getConfig parse failed", e);
                    return null;
                }
            }
        });
        requestHandler.getConfig(requestCallback);
    }


    public void getView(final String id, final OddCallback<OddView> cb) {
        Callback requestCallback = getRequestCallback(cb, new ParseCall<OddView>() {
            @Override
            public OddView parse(String responseBody) {
                try {
                    return (OddView) parser.parseSingleResponse(responseBody);
                } catch (OddParseException | JSONException e) {
                    return null;
                }
            }
        });
        requestHandler.getView(id, requestCallback);
    }
//
//    public void getCollectionEntities(final OddCollection col, final OddCallback<List<OddObject>> cb) {
//        Callback requestCallback = getRequestCallback(cb, new ParseCall<List<OddObject>>() {
//            @Override
//            public List<OddObject> parse(String responseBody) {
//                return parser.parseEntityList(responseBody);
//            }
//        });
//        requestHandler.getCollectionEntities(col.getId(), requestCallback);
//    }
//
//    /** gets OddCollection and all entities within **/
//    public void getCollection(String collectionId, final OddCallback<OddCollection> cb) {
//        Callback requestCallback = getRequestCallback(cb, new ParseCall<OddCollection>() {
//            @Override
//            public OddCollection parse(String responseBody) {
//                return parser.parseCollectionResponse(responseBody);
//            }
//        });
//        requestHandler.getCollection(collectionId, requestCallback);
//    }
//
//    public void getVideo(String collectionId, final OddCallback<OddVideo> cb) {
//        Callback requestCallback = getRequestCallback(cb, new ParseCall<OddVideo>() {
//            @Override
//            public OddVideo parse(String responseBody) {
//                return parser.parseMediaResponse(responseBody);
//            }
//        });
//
//        requestHandler.getVideo(collectionId, requestCallback);
//    }
//
//    public void getSearch(final String term, final int limit, final int offset, final OddCallback<List<OddObject>> cb) {
//        Callback requestCallback = getRequestCallback(cb, new ParseCall<List<OddObject>>() {
//
//            @Override
//            public List<OddObject> parse(String responseBody) throws JSONException {
//                return parser.parseSearch(responseBody);
//            }
//        });
//
//        requestHandler.getSearch(term, limit, offset, requestCallback);
//    }

    public void postMetric(final OddMetric event, final OddCallback<OddMetric> cb) {
        Callback requestCallback = getRequestCallback(cb, new ParseCall<OddMetric>() {
            @Override
            public OddMetric parse(String responseBody) throws JSONException {
                return event;
            }
        });

        requestHandler.postMetric(event, requestCallback);
    }

    private <T> Callback getRequestCallback(final OddCallback<T> cb, final ParseCall<T> parseCall) {
        return new Callback() {
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
                    T obj = null;
                    Exception exceptionCaught = null;
                    String bodyString = null;
                    try {
                        obj = parseCall.parse(response.body().string());
                    } catch (Exception e) {
                        exceptionCaught = e;
                        bodyString = tryGetResponseBody(response);
                    }

                    if(exceptionCaught != null) {
                        cb.onFailure(new OddParseException("Response body parse failed: " + bodyString, exceptionCaught));
                    } else {
                        cb.onSuccess(obj);
                    }
                } else {
                    cb.onFailure(new BadResponseCodeException(response.code()));
                }
            }

            private String tryGetResponseBody(Response response) {
                String bodyString;
                try {
                    bodyString = response.body().string();
                } catch(Exception e) {
                    bodyString = "body could not be captured in this error message." +
                            " This is not the cause of the exception and can be ignored";;
                }
                return bodyString;
            }
        };
    }

    private interface ParseCall<T> {
        T parse(String responseBody) throws JSONException;
    }
}
