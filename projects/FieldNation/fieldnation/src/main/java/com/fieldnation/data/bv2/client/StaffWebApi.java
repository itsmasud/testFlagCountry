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

public class StaffWebApi extends TopicClient {
    private static final String STAG = "StaffWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);


    public StaffWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public boolean subStaffWebApi(){
        return register("TOPIC_ID_WEB_API_V2/StaffWebApi");
    }

    /**
     * Swagger operationId: getEmailTemplates
     * Get email templates by category.
     *
     * @param category email category
     * @param isBackground indicates that this call is low priority
     */
    public static void getEmailTemplates(Context context, String category, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/staff/email-templates/category/" + category);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/staff/email-templates/category/{category}")
                    .key(misc.md5("GET//api/rest/v2/staff/email-templates/category/" + category))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/StaffWebApi/" + category,
                                    StaffWebApi.class, "getEmailTemplates"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetEmailTemplates(String category) {
        return register("TOPIC_ID_WEB_API_V2/StaffWebApi/" + category);
    }


    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            new AsyncParser(this, (Bundle) payload);
        }

        public void onStaffWebApi(String methodName, Object successObject, boolean success, Object failObject) {
        }

        public void onGetEmailTemplates(byte[] data, boolean success, Error error) {
        }

    }

    private static class AsyncParser extends AsyncTaskEx<Object, Object, Object> {
        private static final String TAG = "StaffWebApi.AsyncParser";

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
                    case "getEmailTemplates":
                        if (success)
                            successObject = data;
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
                listener.onStaffWebApi(transactionParams.apiFunction, successObject, success, failObject);
                switch (transactionParams.apiFunction) {
                    case "getEmailTemplates":
                        listener.onGetEmailTemplates((byte[]) successObject, success, (Error) failObject);
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
