package com.fieldnation.v2.data.client;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.ThreadManager;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionSystem;
import com.fieldnation.v2.data.listener.CacheDispatcher;
import com.fieldnation.v2.data.listener.TransactionListener;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Error;
import com.fieldnation.v2.data.model.KeyValue;
import com.fieldnation.v2.data.model.UpdateModel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmgen from swagger.
 */

public abstract class SystemWebApi extends Pigeon {
    private static final String TAG = "SystemWebApi";

    public void sub() {
        PigeonRoost.sub(this, "ADDRESS_WEB_API_V2/SystemWebApi");
    }

    public void unsub() {
        PigeonRoost.unsub(this, "ADDRESS_WEB_API_V2/SystemWebApi");
    }

    /**
     * Swagger operationId: getBanners
     * Get a list of all banners.
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getBanners(Context context, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/system/banners" + (isBackground ? ":isBackground" : ""));

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
                            TransactionListener.params("ADDRESS_WEB_API_V2/SystemWebApi",
                                    SystemWebApi.class, "getBanners", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/SystemWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
                    + (isBackground ? ":isBackground" : ""));

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
                            TransactionListener.params("ADDRESS_WEB_API_V2/SystemWebApi",
                                    SystemWebApi.class, "getBanners", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/SystemWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/SystemWebApi",
                                    SystemWebApi.class, "systemUpdateModel", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/SystemWebApi",
                                    SystemWebApi.class, "systemUpdateModel", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    @Override
    public void onMessage(String address, Object message) {
        Log.v(TAG, "Listener " + address);

        Bundle bundle = (Bundle) message;
        String type = bundle.getString("type");
        TransactionParams transactionParams = bundle.getParcelable("params");

        if (!processTransaction(transactionParams, transactionParams.apiFunction))
            return;

        switch (type) {
            case "queued": {
                onQueued(transactionParams, transactionParams.apiFunction);
                break;
            }
            case "start": {
                onStart(transactionParams, transactionParams.apiFunction);
                break;
            }
            case "progress": {
                onProgress(transactionParams, transactionParams.apiFunction, bundle.getLong("pos"), bundle.getLong("size"), bundle.getLong("time"));
                break;
            }
            case "paused": {
                onPaused(transactionParams, transactionParams.apiFunction);
                break;
            }
            case "complete": {
                BgParser.parse(this, bundle);
                break;
            }
        }
    }

    public abstract boolean processTransaction(TransactionParams transactionParams, String methodName);

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

    private static class BgParser {
        private static final Object SYNC_LOCK = new Object();
        private static List<Bundle> BUNDLES = new LinkedList<>();
        private static List<SystemWebApi> APIS = new LinkedList<>();
        private static ThreadManager _manager;
        private static Handler _handler = null;
        private static final long IDLE_TIMEOUT = 30000;

        private static ThreadManager getManager() {
            synchronized (SYNC_LOCK) {
                if (_manager == null) {
                    _manager = new ThreadManager();
                    _manager.addThread(new Parser(_manager));
                    startActivityMonitor();
                }
            }
            return _manager;
        }

        private static Handler getHandler() {
            synchronized (SYNC_LOCK) {
                if (_handler == null)
                    _handler = new Handler(App.get().getMainLooper());
            }
            return _handler;
        }

        private static void startActivityMonitor() {
            getHandler().postDelayed(_activityChecker_runnable, IDLE_TIMEOUT);
        }

        private static final Runnable _activityChecker_runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (SYNC_LOCK) {
                    if (APIS.size() > 0) {
                        startActivityMonitor();
                        return;
                    }
                }
                stop();
            }
        };

        private static void stop() {
            if (_manager != null)
                _manager.shutdown();
            _manager = null;
            synchronized (SYNC_LOCK) {
                _handler = null;
            }
        }

        public static void parse(SystemWebApi systemWebApi, Bundle bundle) {
            synchronized (SYNC_LOCK) {
                APIS.add(systemWebApi);
                BUNDLES.add(bundle);
            }

            getManager().wakeUp();
        }

        private static class Parser extends ThreadManager.ManagedThread {
            public Parser(ThreadManager manager) {
                super(manager);
                setName("SystemWebApi/Parser");
                start();
            }

            @Override
            public boolean doWork() {
                SystemWebApi webApi = null;
                Bundle bundle = null;
                synchronized (SYNC_LOCK) {
                    if (APIS.size() > 0) {
                        webApi = APIS.remove(0);
                        bundle = BUNDLES.remove(0);
                    }
                }

                if (webApi == null || bundle == null)
                    return false;

                Object successObject = null;
                Object failObject = null;

                TransactionParams transactionParams = bundle.getParcelable("params");
                boolean success = bundle.getBoolean("success");
                byte[] data = bundle.getByteArray("data");

                Stopwatch watch = new Stopwatch(true);
                try {
                    if (data != null && success) {
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
                    } else if (data != null) {
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

                try {
                    if (failObject != null && failObject instanceof Error) {
                        ToastClient.toast(App.get(), ((Error) failObject).getMessage(), Toast.LENGTH_SHORT);
                    }
                    getHandler().post(new Deliverator(webApi, transactionParams, successObject, success, failObject));
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }

                return true;
            }
        }
    }

    private static class Deliverator implements Runnable {
        private SystemWebApi systemWebApi;
        private TransactionParams transactionParams;
        private Object successObject;
        private boolean success;
        private Object failObject;

        public Deliverator(SystemWebApi systemWebApi, TransactionParams transactionParams,
                           Object successObject, boolean success, Object failObject) {
            this.systemWebApi = systemWebApi;
            this.transactionParams = transactionParams;
            this.successObject = successObject;
            this.success = success;
            this.failObject = failObject;
        }

        @Override
        public void run() {
            systemWebApi.onComplete(transactionParams, transactionParams.apiFunction, successObject, success, failObject);
        }
    }
}
