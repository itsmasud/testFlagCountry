package com.fieldnation.v2.data.client;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.fnanalytics.EventContext;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.ThreadManager;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionSystem;
import com.fieldnation.v2.data.listener.CacheDispatcher;
import com.fieldnation.v2.data.listener.TransactionListener;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.AaaaPlaceholder;
import com.fieldnation.v2.data.model.Clients;
import com.fieldnation.v2.data.model.CompanyFeatures;
import com.fieldnation.v2.data.model.CompanyIntegrations;
import com.fieldnation.v2.data.model.CompanyRating;
import com.fieldnation.v2.data.model.Error;
import com.fieldnation.v2.data.model.FundTransaction;
import com.fieldnation.v2.data.model.SavedCreditCards;
import com.fieldnation.v2.data.model.Tag;
import com.fieldnation.v2.data.model.Tags;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmgen from swagger.
 */

public abstract class CompanyWebApi extends Pigeon {
    private static final String TAG = "CompanyWebApi";

    public void sub() {
        PigeonRoost.sub(this, "ADDRESS_WEB_API_V2/CompanyWebApi");
    }

    public void unsub() {
        PigeonRoost.unsub(this, "ADDRESS_WEB_API_V2/CompanyWebApi");
    }

    /**
     * Swagger operationId: addTag
     * Adds a tag to a company for selection on a work order basis
     *
     * @param tag Tag
     */
    public static void addTag(Context context, Tag tag) {
        try {
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
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "addTag", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: addTag
     * Adds a tag to a company for selection on a work order basis
     *
     * @param tag   Tag
     * @param async Async (Optional)
     */
    public static void addTag(Context context, Tag tag, Boolean async) {
        try {
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
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "addTag", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getClients
     * Get a list of clients and projects of the clients
     *
     * @param type indicates that this call is low priority
     */
    public static void getClients(Context context, boolean allowCacheResponse, WebTransaction.Type type) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/clients");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/clients");

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/clients")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getClients", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/CompanyWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getCompanyDetails
     * Get Company Details
     *
     * @param companyId ID of company
     * @param type      indicates that this call is low priority
     */
    public static void getCompanyDetails(Context context, Integer companyId, boolean allowCacheResponse, WebTransaction.Type type) {
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getCompanyDetails", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/CompanyWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getCompanyManagers
     * Gets a list of company managers
     *
     * @param type indicates that this call is low priority
     */
    public static void getCompanyManagers(Context context, boolean allowCacheResponse, WebTransaction.Type type) {
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getCompanyManagers", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/CompanyWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getCompanyUserCreditCards
     * Get a list of all credit cards for a company user.
     *
     * @param type indicates that this call is low priority
     */
    public static void getCompanyUserCreditCards(Context context, boolean allowCacheResponse, WebTransaction.Type type) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/creditcards");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/creditcards");

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/creditcards")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getCompanyUserCreditCards", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/CompanyWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getCreditCardFeesByCompany
     * Get credit card processing fees
     *
     * @param companyId ID of company
     * @param type      indicates that this call is low priority
     */
    public static void getCreditCardFees(Context context, Integer companyId, boolean allowCacheResponse, WebTransaction.Type type) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/" + companyId + "/settings/ccfees");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/" + companyId + "/settings/ccfees");

            JsonObject methodParams = new JsonObject();
            methodParams.put("companyId", companyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/{company_id}/settings/ccfees")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getCreditCardFees", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/CompanyWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getFeatures
     * Get a list of all features for a company.
     *
     * @param status Company feature status (enabled, disabled, requested, denied)
     * @param type   indicates that this call is low priority
     */
    public static void getFeatures(Context context, String status, boolean allowCacheResponse, WebTransaction.Type type) {
        try {
            String key = misc.md5("GET//api/rest/v2/company/features?status=" + status);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/company/features")
                    .urlParams("?status=" + status);

            JsonObject methodParams = new JsonObject();
            methodParams.put("status", status);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/company/features")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getFeatures", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/CompanyWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getIntegrations
     * Get a list of all company_integrations for a company.
     *
     * @param companyId   null
     * @param accessToken null
     * @param type        indicates that this call is low priority
     */
    public static void getIntegrations(Context context, String companyId, String accessToken, boolean allowCacheResponse, WebTransaction.Type type) {
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getIntegrations", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/CompanyWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getManagedProviders
     * Get Company Managed Providers
     *
     * @param type indicates that this call is low priority
     */
    public static void getManagedProviders(Context context, boolean allowCacheResponse, WebTransaction.Type type) {
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getManagedProviders", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/CompanyWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getManagedProviders
     * Get Company Managed Providers
     *
     * @param getManagedProvidersOptions Additional optional parameters
     * @param type                       indicates that this call is low priority
     */
    public static void getManagedProviders(Context context, GetManagedProvidersOptions getManagedProvidersOptions, boolean allowCacheResponse, WebTransaction.Type type) {
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getManagedProviders", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/CompanyWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getPredefinedExpensesByWorkOrder
     * Get a list of predefined expenses by work order.
     *
     * @param workOrderId null
     * @param type        indicates that this call is low priority
     */
    public static void getPredefinedExpenses(Context context, String workOrderId, boolean allowCacheResponse, WebTransaction.Type type) {
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getPredefinedExpenses", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/CompanyWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getRatingsByCompanyId
     * Get Rating Details of a Company
     *
     * @param companyId Company ID
     * @param type      indicates that this call is low priority
     */
    public static void getRatings(Context context, Integer companyId, boolean allowCacheResponse, WebTransaction.Type type) {
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getRatings", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/CompanyWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getSelectionRulesByCompany
     * Returns a list of selection rules for company
     *
     * @param type indicates that this call is low priority
     */
    public static void getSelectionRules(Context context, boolean allowCacheResponse, WebTransaction.Type type) {
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getSelectionRules", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/CompanyWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getTags
     * Gets tags/labels
     *
     * @param type indicates that this call is low priority
     */
    public static void getTags(Context context, boolean allowCacheResponse, WebTransaction.Type type) {
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getTags", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/CompanyWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getTagsByWorkOrder
     * Gets available tags for a work order and the company it belongs to (provider friendly route)
     *
     * @param workOrderId work order ID
     * @param type        indicates that this call is low priority
     */
    public static void getTags(Context context, Integer workOrderId, boolean allowCacheResponse, WebTransaction.Type type) {
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "getTags", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/CompanyWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: requestFeatureByFeature
     * Send a request to enable a feature of a company.
     *
     * @param featureId Feature ID
     */
    public static void requestFeature(Context context, Integer featureId, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("requestFeatureByFeature")
                .label(featureId + "")
                .category("company")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/company/features/" + featureId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("featureId", featureId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/company/features/{feature_id}")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "requestFeature", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/company/" + companyId + "/funds/" + financeId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("companyId", companyId);
            methodParams.put("financeId", financeId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/company/{company_id}/funds/{finance_id}")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "updateFund", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/CompanyWebApi",
                                    CompanyWebApi.class, "updateFund", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    @Override
    public void onMessage(String address, Object message) {
        Log.v(TAG, "Listener " + address);

        Bundle bundle = (Bundle) message;
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
                BgParser.parse(this, bundle);
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

    private static class BgParser {
        private static final Object SYNC_LOCK = new Object();
        private static List<Bundle> BUNDLES = new LinkedList<>();
        private static List<CompanyWebApi> APIS = new LinkedList<>();
        private static ThreadManager _manager;
        private static Handler _handler = null;
        private static final long IDLE_TIMEOUT = 30000;

        private static ThreadManager getManager() {
            synchronized (SYNC_LOCK) {
                if (_manager == null) {
                    _manager = new ThreadManager();
                    _manager.addThread(new Parser(_manager));
                    startActivityMonitor();
                }
            }
            return _manager;
        }

        private static Handler getHandler() {
            synchronized (SYNC_LOCK) {
                if (_handler == null)
                    _handler = new Handler(App.get().getMainLooper());
            }
            return _handler;
        }

        private static void startActivityMonitor() {
            getHandler().postDelayed(_activityChecker_runnable, IDLE_TIMEOUT);
        }

        private static final Runnable _activityChecker_runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (SYNC_LOCK) {
                    if (APIS.size() > 0) {
                        startActivityMonitor();
                        return;
                    }
                }
                stop();
            }
        };

        private static void stop() {
            if (_manager != null)
                _manager.shutdown();
            _manager = null;
            synchronized (SYNC_LOCK) {
                _handler = null;
            }
        }

        public static void parse(CompanyWebApi companyWebApi, Bundle bundle) {
            synchronized (SYNC_LOCK) {
                APIS.add(companyWebApi);
                BUNDLES.add(bundle);
            }

            getManager().wakeUp();
        }

        private static class Parser extends ThreadManager.ManagedThread {
            public Parser(ThreadManager manager) {
                super(manager);
                setName("CompanyWebApi/Parser");
                start();
            }

            @Override
            public boolean doWork() {
                CompanyWebApi webApi = null;
                Bundle bundle = null;
                synchronized (SYNC_LOCK) {
                    if (APIS.size() > 0) {
                        webApi = APIS.remove(0);
                        bundle = BUNDLES.remove(0);
                    }
                }

                if (webApi == null || bundle == null)
                    return false;

                Object successObject = null;
                Object failObject = null;

                TransactionParams transactionParams = bundle.getParcelable("params");
                boolean success = bundle.getBoolean("success");
                byte[] data = bundle.getByteArray("data");

                Stopwatch watch = new Stopwatch(true);
                try {
                    if (data != null && success) {
                        switch (transactionParams.apiFunction) {
                            case "addTag":
                                successObject = Tag.fromJson(new JsonObject(data));
                                break;
                            case "getCreditCardFees":
                                successObject = AaaaPlaceholder.fromJson(new JsonObject(data));
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
                            case "getClients":
                                successObject = Clients.fromJson(new JsonObject(data));
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
                            case "requestFeature":
                            case "updateFund":
                                successObject = data;
                                break;
                            default:
                                Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                                break;
                        }
                    } else if (data != null) {
                        switch (transactionParams.apiFunction) {
                            case "addTag":
                            case "getClients":
                            case "getCompanyDetails":
                            case "getCompanyManagers":
                            case "getCompanyUserCreditCards":
                            case "getCreditCardFees":
                            case "getFeatures":
                            case "getIntegrations":
                            case "getManagedProviders":
                            case "getPredefinedExpenses":
                            case "getRatings":
                            case "getSelectionRules":
                            case "getTags":
                            case "requestFeature":
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

                try {
                    if (failObject != null && failObject instanceof Error) {
                        ToastClient.toast(App.get(), ((Error) failObject).getMessage(), Toast.LENGTH_SHORT);
                    }
                    getHandler().post(new Deliverator(webApi, transactionParams, successObject, success, failObject));
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }

                return true;
            }
        }
    }

    private static class Deliverator implements Runnable {
        private CompanyWebApi companyWebApi;
        private TransactionParams transactionParams;
        private Object successObject;
        private boolean success;
        private Object failObject;

        public Deliverator(CompanyWebApi companyWebApi, TransactionParams transactionParams,
                           Object successObject, boolean success, Object failObject) {
            this.companyWebApi = companyWebApi;
            this.transactionParams = transactionParams;
            this.successObject = successObject;
            this.success = success;
            this.failObject = failObject;
        }

        @Override
        public void run() {
            companyWebApi.onComplete(transactionParams, transactionParams.apiFunction, successObject, success, failObject);
        }
    }
}
