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
import com.fieldnation.v2.data.model.EmailTemplates;
import com.fieldnation.v2.data.model.Error;
import com.fieldnation.v2.data.model.Robocalls;

/**
 * Created by dmgen from swagger.
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

    public boolean subStaffWebApi() {
        return register("TOPIC_ID_WEB_API_V2/StaffWebApi");
    }

    /**
     * Swagger operationId: getEmailTemplates
     * Get email templates by category.
     *
     * @param category     email category
     * @param isBackground indicates that this call is low priority
     */
    public static void getEmailTemplates(Context context, String category, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/staff/email-templates/category/" + category);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/staff/email-templates/category/" + category);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/staff/email-templates/category/{category}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/StaffWebApi",
                                    StaffWebApi.class, "getEmailTemplates"))
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
     * Swagger operationId: getRobocalls
     * Get robocalls
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getRobocalls(Context context, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/staff/robocalls");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/staff/robocalls");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/staff/robocalls")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/StaffWebApi",
                                    StaffWebApi.class, "getRobocalls"))
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
     * Swagger operationId: sendCommunicationByWorkOrder
     * Send recruitment email or robocalls
     *
     * @param workOrderId ID of work order
     * @param body        null
     */
    public static void sendCommunication(Context context, Integer workOrderId, String body) {
        try {
            String key = misc.md5("POST//api/rest/v2/staff/recruitment/send-communications/" + workOrderId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/staff/recruitment/send-communications/" + workOrderId);

            if (body != null)
                builder.body(body);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/staff/recruitment/send-communications/{work_order_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/StaffWebApi",
                                    StaffWebApi.class, "sendCommunication"))
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
                case "progress": {
                    Bundle bundle = (Bundle) payload;
                    TransactionParams transactionParams = bundle.getParcelable("params");
                    onProgress(transactionParams.apiFunction, bundle.getLong("pos"), bundle.getLong("size"), bundle.getLong("time"));
                    break;
                }
                case "start": {
                    Bundle bundle = (Bundle) payload;
                    TransactionParams transactionParams = bundle.getParcelable("params");
                    onStart(transactionParams.apiFunction);
                    break;
                }
                case "complete": {
                    new AsyncParser(this, (Bundle) payload);
                    break;
                }
            }
        }

        public void onStart(String methodName) {
        }

        public void onProgress(String methodName, long pos, long size, long time) {
        }

        public void onComplete(String methodName, Object successObject, boolean success, Object failObject) {
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
            Log.v(TAG, "Start doInBackground");
            Stopwatch watch = new Stopwatch(true);
            try {
                if (success) {
                    switch (transactionParams.apiFunction) {
                        case "sendCommunication":
                            successObject = data;
                            break;
                        case "getEmailTemplates":
                            successObject = EmailTemplates.fromJson(new JsonObject(data));
                            break;
                        case "getRobocalls":
                            successObject = Robocalls.fromJson(new JsonObject(data));
                            break;
                        default:
                            Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                            break;
                    }
                } else {
                    switch (transactionParams.apiFunction) {
                        case "getEmailTemplates":
                        case "getRobocalls":
                        case "sendCommunication":
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
                listener.onComplete(transactionParams.apiFunction, successObject, success, failObject);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    }
}
