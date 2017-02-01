package com.fieldnation.v2.data.client;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.v2.data.listener.TransactionListener;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.*;
import com.fieldnation.v2.data.model.Error;
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
 * Created by dmgen from swagger on 1/31/17.
 */

public class CompanyWebApi extends TopicClient {
    private static final String STAG = "CompanyWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);


    public CompanyWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public boolean subCompanyWebApi(){
        return register("TOPIC_ID_WEB_API_V2/CompanyWebApi");
    }

    /**
     * Swagger operationId: getIntegrations
     * Get a list of all company_integrations for a company.
     *
     * @param companyId null
     * @param accessToken null
     * @param isBackground indicates that this call is low priority
     */
    public static void getIntegrations(Context context, String companyId, String accessToken, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/" + companyId + "/integrations")
                    .urlParams("?access_token=" + accessToken);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/{company_id}/integrations")
                    .key(misc.md5("GET//api/rest/v2/company/" + companyId + "/integrations?access_token=" + accessToken))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi/" + companyId + "/integrations",
                                    CompanyWebApi.class, "getIntegrations"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetIntegrations(String companyId) {
        return register("TOPIC_ID_WEB_API_V2/CompanyWebApi/" + companyId + "/integrations");
    }


    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            new AsyncParser(this, (Bundle) payload);
        }

        public void onCompanyWebApi(String methodName, Object successObject, boolean success, Object failObject) {
        }

        public void onGetIntegrations(CompanyIntegrations companyIntegrations, boolean success, Error error) {
        }

    }

    private static class AsyncParser extends AsyncTaskEx<Object, Object, Object> {
        private static final String TAG = "CompanyWebApi.AsyncParser";

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
                    case "getIntegrations":
                        if (success)
                            successObject = CompanyIntegrations.fromJson(new JsonObject(data));
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
                listener.onCompanyWebApi(transactionParams.apiFunction, successObject, success, failObject);
                switch (transactionParams.apiFunction) {
                    case "getIntegrations":
                        listener.onGetIntegrations((CompanyIntegrations) successObject, success, (Error) failObject);
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
