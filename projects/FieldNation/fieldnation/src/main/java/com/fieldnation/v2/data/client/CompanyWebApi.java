package com.fieldnation.v2.data.client;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.fieldnation.App;
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

    public boolean subCompanyWebApi() {
        return register("TOPIC_ID_WEB_API_V2/CompanyWebApi");
    }

    /**
     * Swagger operationId: getCompanyDetails
     * Get Company Details
     *
     * @param companyId ID of company
     * @param isBackground indicates that this call is low priority
     */
    public static void getCompanyDetails(Context context, Integer companyId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/" + companyId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/" + companyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/{company_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getCompanyDetails"))
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
     * Swagger operationId: getIntegrations
     * Get a list of all company_integrations for a company.
     *
     * @param companyId null
     * @param accessToken null
     * @param isBackground indicates that this call is low priority
     */
    public static void getIntegrations(Context context, String companyId, String accessToken, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/" + companyId + "/integrations?access_token=" + accessToken);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/" + companyId + "/integrations")
                    .urlParams("?access_token=" + accessToken);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/{company_id}/integrations")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getIntegrations"))
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
     * Swagger operationId: getRatingsByCompanyId
     * Get Rating Details of a Company
     *
     * @param companyId Company ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getRatings(Context context, Integer companyId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/" + companyId + "/ratings");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/" + companyId + "/ratings");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/{company_id}/ratings")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getRatings"))
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
     * Swagger operationId: updateFundByFund
     * Perform fund deposit
     *
     * @param companyId ID of company
     * @param financeId ID of finance account
     */
    public static void updateFund(Context context, Integer companyId, Integer financeId) {
        try {
            String key = misc.md5("POST//api/rest/v2/company/" + companyId + "/funds/" + financeId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/company/" + companyId + "/funds/" + financeId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/company/{company_id}/funds/{finance_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "updateFund"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateFundByFund
     * Perform fund deposit
     *
     * @param companyId ID of company
     * @param financeId ID of finance account
     * @param fundTransaction Transaction attempting to be created (Optional)
     */
    public static void updateFund(Context context, Integer companyId, Integer financeId, FundTransaction fundTransaction) {
        try {
            String key = misc.md5("POST//api/rest/v2/company/" + companyId + "/funds/" + financeId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/company/" + companyId + "/funds/" + financeId);

            if (fundTransaction != null)
                builder.body(fundTransaction.getJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/company/{company_id}/funds/{finance_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "updateFund"))
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
            new AsyncParser(this, (Bundle) payload);
        }

        public void onCompanyWebApi(String methodName, Object successObject, boolean success, Object failObject) {
        }

        public void onGetCompanyDetails(boolean success, Error error) {
        }

        public void onGetIntegrations(CompanyIntegrations companyIntegrations, boolean success, Error error) {
        }

        public void onGetRatings(CompanyRating companyRating, boolean success, Error error) {
        }

        public void onUpdateFund(boolean success, Error error) {
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
            Log.v(TAG, "Start doInBackground");
            Stopwatch watch = new Stopwatch(true);
            try {
                switch (transactionParams.apiFunction) {
                    case "getCompanyDetails":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getIntegrations":
                        if (success)
                            successObject = CompanyIntegrations.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getRatings":
                        if (success)
                            successObject = CompanyRating.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateFund":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    default:
                        Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                        break;
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
                listener.onCompanyWebApi(transactionParams.apiFunction, successObject, success, failObject);
                switch (transactionParams.apiFunction) {
                    case "getCompanyDetails":
                        listener.onGetCompanyDetails(success, (Error) failObject);
                        break;
                    case "getIntegrations":
                        listener.onGetIntegrations((CompanyIntegrations) successObject, success, (Error) failObject);
                        break;
                    case "getRatings":
                        listener.onGetRatings((CompanyRating) successObject, success, (Error) failObject);
                        break;
                    case "updateFund":
                        listener.onUpdateFund(success, (Error) failObject);
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
