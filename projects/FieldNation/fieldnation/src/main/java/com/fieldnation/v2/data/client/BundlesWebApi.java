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
import com.fieldnation.v2.data.model.WorkOrders;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmgen from swagger.
 */

public abstract class BundlesWebApi extends Pigeon {
    private static final String TAG = "BundlesWebApi";

    public void sub() {
        PigeonRoost.sub(this, "ADDRESS_WEB_API_V2/BundlesWebApi");
    }

    public void sub(boolean isSync) {
        PigeonRoost.sub(this, isSync ? "ADDRESS_WEB_API_V2_SYNC/BundlesWebApi" : "ADDRESS_WEB_API_V2/BundlesWebApi");
    }

    public void unsub() {
        PigeonRoost.unsub(this, "ADDRESS_WEB_API_V2/BundlesWebApi");
    }

    public void unsub(boolean isSync) {
        PigeonRoost.unsub(this, isSync ? "ADDRESS_WEB_API_V2_SYNC/BundlesWebApi" : "ADDRESS_WEB_API_V2/BundlesWebApi");
    }

    /**
     * Swagger operationId: getBundleWorkOrders
     * Returns a list of work orders in a bundle.
     *
     * @param bundleId Bundle ID
     * @param type     indicates that this call is low priority
     */
    public static void getBundleWorkOrders(Context context, Integer bundleId, boolean allowCacheResponse, WebTransaction.Type type) {
        try {
            String key = misc.md5("GET//api/rest/v2/bundles/" + bundleId);
            String topicId = (type != WebTransaction.Type.NORMAL) ? "ADDRESS_WEB_API_V2_SYNC/BundlesWebApi" : "ADDRESS_WEB_API_V2/BundlesWebApi";

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/bundles/" + bundleId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("bundleId", bundleId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/bundles/{bundle_id}")
                    .key(key)
                    .priority((type != WebTransaction.Type.NORMAL) ? Priority.LOW : Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params(topicId,
                                    BundlesWebApi.class, "getBundleWorkOrders", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key, topicId);
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
        private static List<BundlesWebApi> APIS = new LinkedList<>();
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

        public static void parse(BundlesWebApi bundlesWebApi, Bundle bundle) {
            synchronized (SYNC_LOCK) {
                APIS.add(bundlesWebApi);
                BUNDLES.add(bundle);
            }

            getManager().wakeUp();
        }

        private static class Parser extends ThreadManager.ManagedThread {
            public Parser(ThreadManager manager) {
                super(manager);
                setName("BundlesWebApi/Parser");
                start();
            }

            @Override
            public boolean doWork() {
                BundlesWebApi webApi = null;
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
                            case "getBundleWorkOrders":
                                successObject = WorkOrders.fromJson(new JsonObject(data));
                                break;
                            default:
                                Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                                break;
                        }
                    } else if (data != null) {
                        switch (transactionParams.apiFunction) {
                            case "getBundleWorkOrders":
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
        private BundlesWebApi bundlesWebApi;
        private TransactionParams transactionParams;
        private Object successObject;
        private boolean success;
        private Object failObject;

        public Deliverator(BundlesWebApi bundlesWebApi, TransactionParams transactionParams,
                           Object successObject, boolean success, Object failObject) {
            this.bundlesWebApi = bundlesWebApi;
            this.transactionParams = transactionParams;
            this.successObject = successObject;
            this.success = success;
            this.failObject = failObject;
        }

        @Override
        public void run() {
            bundlesWebApi.onComplete(transactionParams, transactionParams.apiFunction, successObject, success, failObject);
        }
    }
}
