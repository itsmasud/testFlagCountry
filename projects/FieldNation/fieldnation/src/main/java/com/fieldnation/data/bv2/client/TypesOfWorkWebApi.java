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

public class TypesOfWorkWebApi extends TopicClient {
    private static final String STAG = "TypesOfWorkWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);


    public TypesOfWorkWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public boolean subTypesOfWorkWebApi(){
        return register("TOPIC_ID_WEB_API_V2/TypesOfWorkWebApi");
    }

    /**
     * Swagger operationId: getTypesOfWork
     * Gets a list of type of work
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getTypesOfWork(Context context, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/types-of-work");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/types-of-work")
                    .key(misc.md5("GET//api/rest/v2/types-of-work"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/types-of-work",
                                    TypesOfWorkWebApi.class, "getTypesOfWork"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetTypesOfWork() {
        return register("TOPIC_ID_WEB_API_V2/types-of-work");
    }


    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            new AsyncParser(this, (Bundle) payload);
        }

        public void onTypesOfWorkWebApi(String methodName, Object successObject, boolean success, Object failObject) {
        }

        public void onGetTypesOfWork(TypesOfWork typesOfWork, boolean success, Error error) {
        }

    }

    private static class AsyncParser extends AsyncTaskEx<Object, Object, Object> {
        private static final String TAG = "TypesOfWorkWebApi.AsyncParser";

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
                    case "getTypesOfWork":
                        if (success)
                            successObject = TypesOfWork.fromJson(new JsonObject(data));
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
                listener.onTypesOfWorkWebApi(transactionParams.apiFunction, successObject, success, failObject);
                switch (transactionParams.apiFunction) {
                    case "getTypesOfWork":
                        listener.onGetTypesOfWork((TypesOfWork) successObject, success, (Error) failObject);
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
