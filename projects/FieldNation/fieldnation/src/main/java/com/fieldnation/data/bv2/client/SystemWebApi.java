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
 * Created by dmgen from swagger on 1/30/17.
 */

public class SystemWebApi extends TopicClient {
    private static final String STAG = "SystemWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);


    public SystemWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public boolean subSystemWebApi(){
        return register("TOPIC_ID_WEB_API_V2/SystemWebApi");
    }

    /**
     * Swagger operationId: updateModel
     * Fires an event that a model has been updated and propogates the new model to all interested parties.
     *
     * @param path The route for obtaining the new model
     * @param event operationId from the swagger API route
     * @param json JSON parameters of the change
     */
    public static void updateModel(Context context, String path, String event, KeyValue json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/system/update-model")
                    .urlParams("?path=" + path + "&event=" + event);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/system/update-model")
                    .key(misc.md5("POST//api/rest/v2/system/update-model?path=" + path + "&event=" + event))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/system/update-model",
                                    SystemWebApi.class, "updateModel"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateModel() {
        return register("TOPIC_ID_WEB_API_V2/system/update-model");
    }

    /**
     * Swagger operationId: updateModel
     * Fires an event that a model has been updated and propogates the new model to all interested parties.
     *
     * @param path The route for obtaining the new model
     * @param event operationId from the swagger API route
     * @param json JSON parameters of the change
     * @param async Return the model in the response (slower) (Optional)
     */
    public static void updateModel(Context context, String path, String event, KeyValue json, Boolean async) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/system/update-model")
                    .urlParams("?path=" + path + "&event=" + event + "&async=" + async);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/system/update-model")
                    .key(misc.md5("POST//api/rest/v2/system/update-model?path=" + path + "&event=" + event + "&async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/system/update-model",
                                    SystemWebApi.class, "updateModel"))
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
            new AsyncParser(this, (Bundle) payload);
        }

        public void onSystemWebApi(String methodName, Object successObject, boolean success, Object failObject) {
        }

        public void onUpdateModel(UpdateModel updateModel, boolean success, Error error) {
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
            try {
                switch (transactionParams.apiFunction) {
                    case "updateModel":
                        if (success)
                            successObject = UpdateModel.fromJson(new JsonObject(data));
                        else
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
                listener.onSystemWebApi(transactionParams.apiFunction, successObject, success, failObject);
                switch (transactionParams.apiFunction) {
                    case "updateModel":
                        listener.onUpdateModel((UpdateModel) successObject, success, (Error) failObject);
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
