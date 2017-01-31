package com.fieldnation.data.bv2.client;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.data.bv2.listener.TransactionListener;
import com.fieldnation.data.bv2.listener.TransactionParams;
import com.fieldnation.data.bv2.model.*;
import com.fieldnation.data.bv2.model.Error;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class PenaltiesWebApi extends TopicClient {
    private static final String STAG = "PenaltiesWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);


    public PenaltiesWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public boolean subPenaltiesWebApi(){
        return register("TOPIC_ID_WEB_API_V2/PenaltiesWebApi");
    }

    /**
     * Swagger operationId: addPenalty
     * Add a penalty which can be added as an option to a work order and applied during the approval process to lower the amount paid to the provider pending a condition is met.
     *
     */
    public static void addPenalty(Context context) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/penalties");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/penalties")
                    .key(misc.md5("POST//api/rest/v2/penalties"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/penalties",
                                    PenaltiesWebApi.class, "addPenalty"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddPenalty() {
        return register("TOPIC_ID_WEB_API_V2/penalties");
    }

    /**
     * Swagger operationId: removePenaltyByPenalty
     * Removes a penalty which can be added as an option to a work order and applied during the approval process to lower the amount paid to the provider pending a condition is met.
     *
     * @param penaltyId Penalty ID
     */
    public static void removePenalty(Context context, Integer penaltyId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/penalties/" + penaltyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/penalties/{penalty_id}")
                    .key(misc.md5("DELETE//api/rest/v2/penalties/" + penaltyId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/PenaltiesWebApi/" + penaltyId,
                                    PenaltiesWebApi.class, "removePenalty"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemovePenalty(Integer penaltyId) {
        return register("TOPIC_ID_WEB_API_V2/PenaltiesWebApi/" + penaltyId);
    }

    /**
     * Swagger operationId: updatePenaltyByPenalty
     * Update a penalty which can be added as an option to a work order and applied during the approval process to lower the amount paid to the provider pending a condition is met.
     *
     * @param penaltyId Penalty ID
     * @param json JSON Model
     */
    public static void updatePenalty(Context context, String penaltyId, PayModifier json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/penalties/" + penaltyId);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/penalties/{penalty_id}")
                    .key(misc.md5("PUT//api/rest/v2/penalties/" + penaltyId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/PenaltiesWebApi/" + penaltyId,
                                    PenaltiesWebApi.class, "updatePenalty"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdatePenalty(String penaltyId) {
        return register("TOPIC_ID_WEB_API_V2/PenaltiesWebApi/" + penaltyId);
    }


    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            new AsyncParser(this, (Bundle) payload);
        }

        public void onPenaltiesWebApi(String methodName, Object successObject, boolean success, Object failObject) {
        }

        public void onAddPenalty(PayModifier payModifier, boolean success, Error error) {
        }

        public void onRemovePenalty(boolean success, Error error) {
        }

        public void onUpdatePenalty(boolean success, Error error) {
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
            try {
                switch (transactionParams.apiFunction) {
                    case "addPenalty":
                        if (success)
                            successObject = PayModifier.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removePenalty":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updatePenalty":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    default:
                        Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                        break;
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            try {
                listener.onPenaltiesWebApi(transactionParams.apiFunction, successObject, success, failObject);
                switch (transactionParams.apiFunction) {
                    case "addPenalty":
                        listener.onAddPenalty((PayModifier) successObject, success, (Error) failObject);
                        break;
                    case "removePenalty":
                        listener.onRemovePenalty(success, (Error) failObject);
                        break;
                    case "updatePenalty":
                        listener.onUpdatePenalty(success, (Error) failObject);
                        break;
                    default:
                        Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                        break;
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    }
}
