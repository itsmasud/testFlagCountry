package com.fieldnation.v2.data.client;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.analytics.contexts.SpWorkOrderContext;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.tracker.TrackerEnum;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;
import com.fieldnation.v2.data.listener.CacheDispatcher;
import com.fieldnation.v2.data.listener.TransactionListener;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.*;
import com.fieldnation.v2.data.model.Error;

/**
 * Created by dmgen from swagger.
 */

public class PenaltiesWebApi extends TopicClient {
    private static final String STAG = "PenaltiesWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);

    private static int connectCount = 0;

    public PenaltiesWebApi(Listener listener) {
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

    public boolean subPenaltiesWebApi() {
        return register("TOPIC_ID_WEB_API_V2/PenaltiesWebApi");
    }

    /**
     * Swagger operationId: addPenalty
     * Add a penalty which can be added as an option to a work order and applied during the approval process to lower the amount paid to the provider pending a condition is met.
     *
     */
    public static void addPenalty(Context context) {
        try {
            String key = misc.md5("POST//api/rest/v2/penalties");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/penalties");

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/penalties")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/PenaltiesWebApi",
                                    PenaltiesWebApi.class, "addPenalty", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deletePenaltyByPenalty
     * Removes a penalty which can be added as an option to a work order and applied during the approval process to lower the amount paid to the provider pending a condition is met.
     *
     * @param penaltyId Penalty ID
     */
    public static void deletePenalty(Context context, Integer penaltyId) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("deletePenaltyByPenalty")
                .label(penaltyId + "")
                .category("penalty")
                .addContext(App.get().spUiContext)
                .build()
        );

        try {
            String key = misc.md5("DELETE//api/rest/v2/penalties/" + penaltyId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/penalties/" + penaltyId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("penaltyId", penaltyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/penalties/{penalty_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/PenaltiesWebApi",
                                    PenaltiesWebApi.class, "deletePenalty", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updatePenaltyByPenalty
     * Update a penalty which can be added as an option to a work order and applied during the approval process to lower the amount paid to the provider pending a condition is met.
     *
     * @param penaltyId Penalty ID
     * @param json      JSON Model
     */
    public static void updatePenalty(Context context, String penaltyId, PayModifier json) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("updatePenaltyByPenalty")
                .label(penaltyId + "")
                .category("penalty")
                .addContext(App.get().spUiContext)
                .build()
        );

        try {
            String key = misc.md5("PUT//api/rest/v2/penalties/" + penaltyId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/penalties/" + penaltyId);

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("penaltyId", penaltyId);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/penalties/{penalty_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/PenaltiesWebApi",
                                    PenaltiesWebApi.class, "updatePenalty", methodParams))
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
        private static final String TAG = "PenaltiesWebApi.AsyncParser";

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
                        case "deletePenalty":
                        case "updatePenalty":
                            successObject = data;
                            break;
                        case "addPenalty":
                            successObject = PayModifier.fromJson(new JsonObject(data));
                            break;
                        default:
                            Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                            break;
                    }
                } else {
                    switch (transactionParams.apiFunction) {
                        case "addPenalty":
                        case "deletePenalty":
                        case "updatePenalty":
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
