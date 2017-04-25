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

public class CompanyWebApi extends TopicClient {
    private static final String STAG = "CompanyWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);

    private static int connectCount = 0;

    public CompanyWebApi(Listener listener) {
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

    public boolean subCompanyWebApi() {
        return register("TOPIC_ID_WEB_API_V2/CompanyWebApi");
    }

    /**
     * Swagger operationId: addTag
     * Adds a tag to a company for selection on a work order basis
     *
     * @param tag Tag
     */
    public static void addTag(Context context, Tag tag) {
        try {
            String key = misc.md5("POST//api/rest/v2/company/tags");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/company/tags");

            if (tag != null)
                builder.body(tag.getJson().toString());

            JsonObject methodParams = new JsonObject();
            if (tag != null)
                methodParams.put("tag", tag.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/company/tags")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "addTag", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addTag
     * Adds a tag to a company for selection on a work order basis
     *
     * @param tag Tag
     * @param async Async (Optional)
     */
    public static void addTag(Context context, Tag tag, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/company/tags?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/company/tags")
                    .urlParams("?async=" + async);

            if (tag != null)
                builder.body(tag.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("async", async);
            if (tag != null)
                methodParams.put("tag", tag.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/company/tags")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "addTag", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
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

            JsonObject methodParams = new JsonObject();
            methodParams.put("companyId", companyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/{company_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getCompanyDetails", methodParams))
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
     * Swagger operationId: getCompanyManagers
     * Gets a list of company managers
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getCompanyManagers(Context context, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/managers");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/managers");

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/managers")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getCompanyManagers", methodParams))
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
     * Swagger operationId: getCompanyUserCreditCards
     * Get a list of all credit cards for a company user.
     *
     * @param accessToken  null
     * @param isBackground indicates that this call is low priority
     */
    public static void getCompanyUserCreditCards(Context context, String accessToken, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/creditcards?access_token=" + accessToken);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/creditcards")
                    .urlParams("?access_token=" + accessToken);

            JsonObject methodParams = new JsonObject();
            methodParams.put("accessToken", accessToken);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/creditcards")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getCompanyUserCreditCards", methodParams))
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
     * Swagger operationId: getFeatures
     * Get a list of all features for a company.
     *
     * @param accessToken  null
     * @param status       Company feature status (enabled, disabled, requested, denied)
     * @param isBackground indicates that this call is low priority
     */
    public static void getFeatures(Context context, String accessToken, String status, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/features?access_token=" + accessToken + "&status=" + status);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/features")
                    .urlParams("?access_token=" + accessToken + "&status=" + status);

            JsonObject methodParams = new JsonObject();
            methodParams.put("accessToken", accessToken);
            methodParams.put("status", status);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/features")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getFeatures", methodParams))
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
     * @param companyId    null
     * @param accessToken  null
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

            JsonObject methodParams = new JsonObject();
            methodParams.put("companyId", companyId);
            methodParams.put("accessToken", accessToken);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/{company_id}/integrations")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getIntegrations", methodParams))
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
     * Swagger operationId: getManagedProviders
     * Get Company Managed Providers
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getManagedProviders(Context context, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/managed-providers");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/managed-providers");

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/managed-providers")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getManagedProviders", methodParams))
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
     * Swagger operationId: getManagedProviders
     * Get Company Managed Providers
     *
     * @param getManagedProvidersOptions Additional optional parameters
     * @param isBackground               indicates that this call is low priority
     */
    public static void getManagedProviders(Context context, GetManagedProvidersOptions getManagedProvidersOptions, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/managed-providers" + (getManagedProvidersOptions.getCompanyId() != null ? "?company_id=" + getManagedProvidersOptions.getCompanyId() : "")
                    + (getManagedProvidersOptions.getMarketplaceOn() != null ? "&marketplace_on=" + getManagedProvidersOptions.getMarketplaceOn() : "")
            );

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/managed-providers")
                    .urlParams("" + (getManagedProvidersOptions.getCompanyId() != null ? "?company_id=" + getManagedProvidersOptions.getCompanyId() : "")
                            + (getManagedProvidersOptions.getMarketplaceOn() != null ? "&marketplace_on=" + getManagedProvidersOptions.getMarketplaceOn() : "")
                    );

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/managed-providers")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getManagedProviders", methodParams))
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
     * Swagger operationId: getPredefinedExpensesByWorkOrder
     * Get a list of predefined expenses by work order.
     *
     * @param workOrderId  null
     * @param isBackground indicates that this call is low priority
     */
    public static void getPredefinedExpenses(Context context, String workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/predefined-expenses/" + workOrderId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/predefined-expenses/" + workOrderId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/predefined-expenses/{work_order_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getPredefinedExpenses", methodParams))
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
     * @param companyId    Company ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getRatings(Context context, Integer companyId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/" + companyId + "/ratings");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/" + companyId + "/ratings");

            JsonObject methodParams = new JsonObject();
            methodParams.put("companyId", companyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/{company_id}/ratings")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getRatings", methodParams))
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
     * Swagger operationId: getSelectionRulesByCompany
     * Returns a list of selection rules for company
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getSelectionRules(Context context, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/selection_rules");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/selection_rules");

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/selection_rules")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getSelectionRules", methodParams))
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
     * Swagger operationId: getSendRequestedFeatures
     * Send a request to enable a feature of a company.
     *
     * @param featureId   Feature ID
     * @param accessToken null
     */
    public static void getSendRequestedFeatures(Context context, Integer featureId, String accessToken, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("getSendRequestedFeatures")
                .label(featureId + "")
                .category("company")
                .addContext(uiContext)
                .build()
        );

        try {
            String key = misc.md5("PUT//api/rest/v2/company/features/" + featureId + "?access_token=" + accessToken);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/company/features/" + featureId)
                    .urlParams("?access_token=" + accessToken);

            JsonObject methodParams = new JsonObject();
            methodParams.put("featureId", featureId);
            methodParams.put("accessToken", accessToken);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/company/features/{feature_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getSendRequestedFeatures", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getTags
     * Gets tags/labels
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getTags(Context context, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/tags");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/tags");

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/tags")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getTags", methodParams))
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
     * Swagger operationId: getTagsByWorkOrder
     * Gets available tags for a work order and the company it belongs to (provider friendly route)
     *
     * @param workOrderId  work order ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getTags(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/tags/" + workOrderId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/tags/" + workOrderId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/tags/{work_order_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getTags", methodParams))
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
    public static void updateFund(Context context, Integer companyId, Integer financeId, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("updateFundByFund")
                .label(companyId + "")
                .category("funds")
                .addContext(uiContext)
                .property("finance_id")
                .value(financeId)
                .build()
        );

        try {
            String key = misc.md5("POST//api/rest/v2/company/" + companyId + "/funds/" + financeId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/company/" + companyId + "/funds/" + financeId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("companyId", companyId);
            methodParams.put("financeId", financeId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/company/{company_id}/funds/{finance_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "updateFund", methodParams))
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
     * @param companyId       ID of company
     * @param financeId       ID of finance account
     * @param fundTransaction Transaction attempting to be created (Optional)
     */
    public static void updateFund(Context context, Integer companyId, Integer financeId, FundTransaction fundTransaction, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("updateFundByFund")
                .label(companyId + "")
                .category("funds")
                .addContext(uiContext)
                .property("finance_id")
                .value(financeId)
                .build()
        );

        try {
            String key = misc.md5("POST//api/rest/v2/company/" + companyId + "/funds/" + financeId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/company/" + companyId + "/funds/" + financeId);

            if (fundTransaction != null)
                builder.body(fundTransaction.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("companyId", companyId);
            methodParams.put("financeId", financeId);
            if (fundTransaction != null)
                methodParams.put("fundTransaction", fundTransaction.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/company/{company_id}/funds/{finance_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "updateFund", methodParams))
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
                if (success) {
                    switch (transactionParams.apiFunction) {
                        case "addTag":
                            successObject = Tag.fromJson(new JsonObject(data));
                            break;
                        case "getCompanyUserCreditCards":
                            successObject = SavedCreditCards.fromJson(new JsonObject(data));
                            break;
                        case "getTags":
                            successObject = Tags.fromJson(new JsonObject(data));
                            break;
                        case "getSelectionRules":
                            //successObject = SelectionRules.fromJson(new JsonObject(data));
                            break;
                        case "getFeatures":
                            successObject = CompanyFeatures.fromJson(new JsonObject(data));
                            break;
                        case "getIntegrations":
                            successObject = CompanyIntegrations.fromJson(new JsonObject(data));
                            break;
                        case "getRatings":
                            successObject = CompanyRating.fromJson(new JsonObject(data));
                            break;
                        case "getPredefinedExpenses":
                            //successObject = PredefinedExpenses.fromJson(new JsonObject(data));
                            break;
                        case "getCompanyDetails":
                        case "getCompanyManagers":
                        case "getManagedProviders":
                        case "getSendRequestedFeatures":
                        case "updateFund":
                            successObject = data;
                            break;
                        default:
                            Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                            break;
                    }
                } else {
                    switch (transactionParams.apiFunction) {
                        case "addTag":
                        case "getCompanyDetails":
                        case "getCompanyManagers":
                        case "getCompanyUserCreditCards":
                        case "getFeatures":
                        case "getIntegrations":
                        case "getManagedProviders":
                        case "getPredefinedExpenses":
                        case "getRatings":
                        case "getSelectionRules":
                        case "getSendRequestedFeatures":
                        case "getTags":
                        case "updateFund":
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
