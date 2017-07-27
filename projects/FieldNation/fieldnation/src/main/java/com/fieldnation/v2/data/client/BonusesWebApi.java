package com.fieldnation.v2.data.client;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.analytics.contexts.SpWorkOrderContext;
import com.fieldnation.fnanalytics.EventContext;
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

public class BonusesWebApi extends TopicClient {
    private static final String STAG = "BonusesWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);

    private static int connectCount = 0;

    public BonusesWebApi(Listener listener) {
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

    public boolean subBonusesWebApi() {
        return register("TOPIC_ID_WEB_API_V2/BonusesWebApi");
    }

    /**
     * Swagger operationId: addBonus
     * Adds a bonus that can be applied to a work order to increase the amount paid upon a condition being met
     *
     * @param bonus JSON Model
     */
    public static void addBonus(Context context, PayModifier bonus) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/bonuses");

            if (bonus != null)
                builder.body(bonus.getJson().toString());

            JsonObject methodParams = new JsonObject();
            if (bonus != null)
                methodParams.put("bonus", bonus.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/bonuses")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/BonusesWebApi",
                                    BonusesWebApi.class, "addBonus", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteBonusByBonus
     * Removes a bonus that can be applied to a work order to increase the amount paid upon a condition being met
     *
     * @param bonusId ID of Bonus
     */
    public static void deleteBonus(Context context, Integer bonusId, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("deleteBonusByBonus")
                .label(bonusId + "")
                .category("bonus")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/bonuses/" + bonusId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("bonusId", bonusId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/bonuses/{bonus_id}")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/BonusesWebApi",
                                    BonusesWebApi.class, "deleteBonus", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateBonusByBonus
     * Updates a bonus that can be applied to a work order to increase the amount paid upon a condition being met
     *
     * @param bonusId Bonus ID
     * @param json JSON Model
     */
    public static void updateBonus(Context context, Integer bonusId, PayModifier json, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("updateBonusByBonus")
                .label(bonusId + "")
                .category("bonus")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/bonuses/" + bonusId);

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("bonusId", bonusId);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/bonuses/{bonus_id}")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/BonusesWebApi",
                                    BonusesWebApi.class, "updateBonus", methodParams))
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

            Bundle bundle = (Bundle) payload;
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
                    new AsyncParser(this, bundle);
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
    }

    private static class AsyncParser extends AsyncTaskEx<Object, Object, Object> {
        private static final String TAG = "BonusesWebApi.AsyncParser";

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
                        case "deleteBonus":
                        case "updateBonus":
                            successObject = data;
                            break;
                        case "addBonus":
                            successObject = IdResponse.fromJson(new JsonObject(data));
                            break;
                        default:
                            Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                            break;
                    }
                } else {
                    switch (transactionParams.apiFunction) {
                        case "addBonus":
                        case "deleteBonus":
                        case "updateBonus":
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
