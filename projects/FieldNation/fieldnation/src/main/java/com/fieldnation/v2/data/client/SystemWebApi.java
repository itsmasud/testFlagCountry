package com.fieldnation.v2.data.client;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;
import com.fieldnation.v2.data.listener.CacheDispatcher;
import com.fieldnation.v2.data.listener.TransactionListener;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Error;
import com.fieldnation.v2.data.model.KeyValue;
import com.fieldnation.v2.data.model.UpdateModel;

/**
 * Created by dmgen from swagger.
 */

public class SystemWebApi extends TopicClient {
    private static final String STAG = "SystemWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);

    private static int connectCount = 0;

    public SystemWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public void connect(Context context) {
        super.connect(context);
        connectCount++;
        Log.v(STAG + ".state", "connect " + connectCount);
    }

    @Override
    public void disconnect(Context context) {
        super.disconnect(context);
        connectCount--;
        Log.v(STAG + ".state", "disconnect " + connectCount);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public boolean subSystemWebApi() {
        return register("TOPIC_ID_WEB_API_V2/SystemWebApi");
    }

    /**
     * Swagger operationId: getBanners
     * Get a list of all banners.
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getBanners(Context context, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/system/banners");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/system/banners");

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/system/banners")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/SystemWebApi",
                                    SystemWebApi.class, "getBanners", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getBanners
     * Get a list of all banners.
     *
     * @param getBannersOptions Additional optional parameters
     * @param isBackground      indicates that this call is low priority
     */
    public static void getBanners(Context context, GetBannersOptions getBannersOptions, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/system/banners" + (getBannersOptions.getActive() != null ? "?active=" + getBannersOptions.getActive() : "")
                    + (getBannersOptions.getAllowedBanners() != null ? "&allowedBanners=" + getBannersOptions.getAllowedBanners() : "")
            );

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/system/banners")
                    .urlParams("" + (getBannersOptions.getActive() != null ? "?active=" + getBannersOptions.getActive() : "")
                            + (getBannersOptions.getAllowedBanners() != null ? "&allowedBanners=" + getBannersOptions.getAllowedBanners() : "")
                    );

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/system/banners")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/SystemWebApi",
                                    SystemWebApi.class, "getBanners", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: systemUpdateModel
     * Fires an event that a model has been updated and propogates the new model to all interested parties.
     *
     * @param path  The route for obtaining the new model
     * @param event operationId from the swagger API route
     * @param json  JSON parameters of the change
     */
    public static void systemUpdateModel(Context context, String path, String event, KeyValue json) {
        try {
            String key = misc.md5("POST//api/rest/v2/system/update-model?path=" + path + "&event=" + event);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/system/update-model")
                    .urlParams("?path=" + path + "&event=" + event);

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("path", path);
            methodParams.put("event", event);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/system/update-model")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/SystemWebApi",
                                    SystemWebApi.class, "systemUpdateModel", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: systemUpdateModel
     * Fires an event that a model has been updated and propogates the new model to all interested parties.
     *
     * @param path  The route for obtaining the new model
     * @param event operationId from the swagger API route
     * @param json  JSON parameters of the change
     * @param async Return the model in the response (slower) (Optional)
     */
    public static void systemUpdateModel(Context context, String path, String event, KeyValue json, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/system/update-model?path=" + path + "&event=" + event + "&async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/system/update-model")
                    .urlParams("?path=" + path + "&event=" + event + "&async=" + async);

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("path", path);
            methodParams.put("event", event);
            methodParams.put("async", async);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/system/update-model")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/SystemWebApi",
                                    SystemWebApi.class, "systemUpdateModel", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }


    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Log.v(STAG, "Listener " + topicId);

            String type = ((Bundle) payload).getString("type");
            switch (type) {
                case "queued": {
                    Bundle bundle = (Bundle) payload;
                    TransactionParams transactionParams = bundle.getParcelable("params");
                    onQueued(transactionParams, transactionParams.apiFunction);
                    break;
                }
                case "start": {
                    Bundle bundle = (Bundle) payload;
                    TransactionParams transactionParams = bundle.getParcelable("params");
                    onStart(transactionParams, transactionParams.apiFunction);
                    break;
                }
                case "progress": {
                    Bundle bundle = (Bundle) payload;
                    TransactionParams transactionParams = bundle.getParcelable("params");
                    onProgress(transactionParams, transactionParams.apiFunction, bundle.getLong("pos"), bundle.getLong("size"), bundle.getLong("time"));
                    break;
                }
                case "paused": {
                    Bundle bundle = (Bundle) payload;
                    TransactionParams transactionParams = bundle.getParcelable("params");
                    onPaused(transactionParams, transactionParams.apiFunction);
                    break;
                }
                case "complete": {
                    new AsyncParser(this, (Bundle) payload);
                    break;
                }
            }
        }

        public void onQueued(TransactionParams transactionParams, String methodName) {
        }

        public void onStart(TransactionParams transactionParams, String methodName) {
        }

        public void onPaused(TransactionParams transactionParams, String methodName) {
        }

        public void onProgress(TransactionParams transactionParams, String methodName, long pos, long size, long time) {
        }

        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
        }
    }

    private static class AsyncParser extends AsyncTaskEx<Object, Object, Object> {
        private static final String TAG = "SystemWebApi.AsyncParser";

        private Listener listener;
        private TransactionParams transactionParams;
        private boolean success;
        private byte[] data;

        private Object successObject;
        private Object failObject;

        public AsyncParser(Listener listener, Bundle bundle) {
            this.listener = listener;
            transactionParams = bundle.getParcelable("params");
            success = bundle.getBoolean("success");
            data = bundle.getByteArray("data");

            executeEx();
        }

        @Override
        protected Object doInBackground(Object... params) {
            Log.v(TAG, "Start doInBackground");
            Stopwatch watch = new Stopwatch(true);
            try {
                if (success) {
                    switch (transactionParams.apiFunction) {
                        case "systemUpdateModel":
                            successObject = UpdateModel.fromJson(new JsonObject(data));
                            break;
                        case "getBanners":
                            //successObject = Banner.fromJson(new JsonObject(data));
                            break;
                        default:
                            Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                            break;
                    }
                } else {
                    switch (transactionParams.apiFunction) {
                        case "getBanners":
                        case "systemUpdateModel":
                            failObject = Error.fromJson(new JsonObject(data));
                            break;
                        default:
                            Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                            break;
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            } finally {
                Log.v(TAG, "doInBackground: " + transactionParams.apiFunction + " time: " + watch.finish());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            try {
                if (failObject != null && failObject instanceof Error) {
                    ToastClient.toast(App.get(), ((Error) failObject).getMessage(), Toast.LENGTH_SHORT);
                }
                listener.onComplete(transactionParams, transactionParams.apiFunction, successObject, success, failObject);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    }
}
