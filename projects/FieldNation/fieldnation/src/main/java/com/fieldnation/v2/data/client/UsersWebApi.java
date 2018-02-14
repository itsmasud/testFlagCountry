package com.fieldnation.v2.data.client;

import android.content.Context;
import android.net.Uri;
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
import com.fieldnation.v2.data.model.Coords;
import com.fieldnation.v2.data.model.Error;
import com.fieldnation.v2.data.model.PPNs;
import com.fieldnation.v2.data.model.ProfileAndWorkHistory;
import com.fieldnation.v2.data.model.TypesOfWork;
import com.fieldnation.v2.data.model.User;
import com.fieldnation.v2.data.model.UserTaxInfo;
import com.fieldnation.v2.data.model.UserTaxInfoUpdate;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmgen from swagger.
 */

public abstract class UsersWebApi extends Pigeon {
    private static final String TAG = "UsersWebApi";

    public void sub() {
        PigeonRoost.sub(this, "ADDRESS_WEB_API_V2/UsersWebApi");
    }

    public void unsub() {
        PigeonRoost.unsub(this, "ADDRESS_WEB_API_V2/UsersWebApi");
    }

    /**
     * Swagger operationId: addCoordsByUser
     * Stores user's current location during on my way reporting
     *
     * @param userId user id of user to store coordinates for
     * @param coords coordinate data. Only need latitude and longitude fields
     */
    public static void addCoords(Context context, Integer userId, Coords coords) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/coords");

            if (coords != null)
                builder.body(coords.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);
            if (coords != null)
                methodParams.put("coords", coords.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/coords")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "addCoords", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: addPayByUser
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     */
    public static void addPay(Context context, Integer userId, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("addPayByUser")
                .label(userId + "")
                .category("user")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/pay");

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/pay")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "addPay", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: addTypesOfWork
     * Add types of work to profile
     *
     * @param userId User ID
     * @param json   JSON model
     */
    public static void addTypesOfWork(Context context, Integer userId, String json, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("addTypesOfWork")
                .label(userId + "")
                .category("user")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/types-of-work");

            if (json != null)
                builder.body(json);

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);
            if (json != null)
                methodParams.put("json", json);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/types-of-work")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "addTypesOfWork", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getPayByUser
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     * @param type   indicates that this call is low priority
     */
    public static void getPay(Context context, Integer userId, boolean allowCacheResponse, WebTransaction.Type type) {
        try {
            String key = misc.md5("GET//api/rest/v2/users/" + userId + "/pay");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + userId + "/pay");

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/pay")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "getPay", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/UsersWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getProfileAndWorkHistory
     * Get Profile and Work History By The User and Work Order
     *
     * @param userId      User ID
     * @param workOrderId Work Order ID
     * @param type        indicates that this call is low priority
     */
    public static void getProfileAndWorkHistory(Context context, Integer userId, Integer workOrderId, boolean allowCacheResponse, WebTransaction.Type type) {
        try {
            String key = misc.md5("GET//api/rest/v2/users/" + userId + "/workorder/" + workOrderId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + userId + "/workorder/" + workOrderId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/workorder/{work_order_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "getProfileAndWorkHistory", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/UsersWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getSettingsByUser
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     * @param type   indicates that this call is low priority
     */
    public static void getSettings(Context context, Integer userId, boolean allowCacheResponse, WebTransaction.Type type) {
        try {
            String key = misc.md5("GET//api/rest/v2/users/" + userId + "/settings");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + userId + "/settings");

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/settings")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "getSettings", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/UsersWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getTaxByUser
     * Get tax info
     *
     * @param userId User ID
     * @param type   indicates that this call is low priority
     */
    public static void getTax(Context context, Integer userId, boolean allowCacheResponse, WebTransaction.Type type) {
        try {
            String key = misc.md5("GET//api/rest/v2/users/" + userId + "/tax");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + userId + "/tax");

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/tax")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "getTax", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/UsersWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getTourByUser
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     * @param type   indicates that this call is low priority
     */
    public static void getTour(Context context, Integer userId, boolean allowCacheResponse, WebTransaction.Type type) {
        try {
            String key = misc.md5("GET//api/rest/v2/users/" + userId + "/tour");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + userId + "/tour");

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/tour")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "getTour", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/UsersWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getUser
     * Returns summary details about a user profile.
     *
     * @param user User ID
     * @param type indicates that this call is low priority
     */
    public static void getUser(Context context, String user, boolean allowCacheResponse, WebTransaction.Type type) {
        try {
            String key = misc.md5("GET//api/rest/v2/users/" + user);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + user);

            JsonObject methodParams = new JsonObject();
            methodParams.put("user", user);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "getUser", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/UsersWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getUserPreferenceValueByKey
     * Get user preference value
     *
     * @param userId     User ID
     * @param preference Preference Key
     * @param type       indicates that this call is low priority
     */
    public static void getUserPreferenceValue(Context context, Integer userId, String preference, boolean allowCacheResponse, WebTransaction.Type type) {
        try {
            String key = misc.md5("GET//api/rest/v2/users/" + userId + "/preferences/" + preference);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + userId + "/preferences/" + preference);

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);
            methodParams.put("preference", preference);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/preferences/{preference}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "getUserPreferenceValue", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/UsersWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getUserPreferredProviderNetworks
     * Get the Preferred Provider Networks the user is a part of
     *
     * @param userId User ID
     * @param type   indicates that this call is low priority
     */
    public static void getUserPreferredProviderNetworks(Context context, Integer userId, boolean allowCacheResponse, WebTransaction.Type type) {
        try {
            String key = misc.md5("GET//api/rest/v2/users/" + userId + "/preferredprovidernetworks");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + userId + "/preferredprovidernetworks");

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/preferredprovidernetworks")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "getUserPreferredProviderNetworks", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/UsersWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getUserTypesOfWork
     * Get all types of work of a specific user
     *
     * @param userId User ID
     * @param type   indicates that this call is low priority
     */
    public static void getUserTypesOfWork(Context context, Integer userId, boolean allowCacheResponse, WebTransaction.Type type) {
        try {
            String key = misc.md5("GET//api/rest/v2/users/" + userId + "/types-of-work");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + userId + "/types-of-work");

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/types-of-work")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "getUserTypesOfWork", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/UsersWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: getWorkHistory
     * Get work history of a user
     *
     * @param userId User ID
     * @param type   indicates that this call is low priority
     */
    public static void getWorkHistory(Context context, Integer userId, boolean allowCacheResponse, WebTransaction.Type type) {
        try {
            String key = misc.md5("GET//api/rest/v2/users/" + userId + "/work_history");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + userId + "/work_history");

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/work_history")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "getWorkHistory", methodParams))
                    .useAuth(true)
                    .setType(type)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse)
                new CacheDispatcher(context, key, "ADDRESS_WEB_API_V2/UsersWebApi");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: sendAccountActivationLink
     * Send account activation link
     *
     * @param userId User ID
     * @param json   JSON Payload
     */
    public static void sendAccountActivationLink(Context context, Integer userId, String json, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("sendAccountActivationLink")
                .label(userId + "")
                .category("user")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/verify/email");

            if (json != null)
                builder.body(json);

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);
            if (json != null)
                methodParams.put("json", json);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/verify/email")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "sendAccountActivationLink", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: sendVerificationCodeViaSms
     * Send account verification code via text message
     *
     * @param userId User ID
     * @param json   JSON Payload
     */
    public static void sendVerificationCodeViaSms(Context context, Integer userId, String json, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("sendVerificationCodeViaSms")
                .label(userId + "")
                .category("user")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/verify/text");

            if (json != null)
                builder.body(json);

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);
            if (json != null)
                methodParams.put("json", json);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/verify/text")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "sendVerificationCodeViaSms", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: sendVerificationCodeViaVoiceCall
     * Send account verification code via phone call
     *
     * @param userId User ID
     * @param json   JSON Payload
     */
    public static void sendVerificationCodeViaVoiceCall(Context context, Integer userId, String json, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("sendVerificationCodeViaVoiceCall")
                .label(userId + "")
                .category("user")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/verify/phone");

            if (json != null)
                builder.body(json);

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);
            if (json != null)
                methodParams.put("json", json);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/verify/phone")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "sendVerificationCodeViaVoiceCall", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: setUserPreference
     * Set user preference
     *
     * @param userId     User ID
     * @param preference Preference Key
     * @param json       JSON Model
     */
    public static void setUserPreference(Context context, Integer userId, String preference, String json, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("setUserPreference")
                .label(userId + "")
                .category("user")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/preferences/" + preference);

            if (json != null)
                builder.body(json);

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);
            methodParams.put("preference", preference);
            if (json != null)
                methodParams.put("json", json);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/preferences/{preference}")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "setUserPreference", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: swipNotification
     * Swip Notification
     *
     * @param userId         User ID
     * @param notificationId Notification ID
     */
    public static void swipNotification(Context context, Integer userId, Integer notificationId, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("swipNotification")
                .label(userId + "")
                .category("user")
                .addContext(uiContext)
                .property("notification_id")
                .value(notificationId)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/notifications/" + notificationId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);
            methodParams.put("notificationId", notificationId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/notifications/{notification_id}")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "swipNotification", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: switchUser
     * Switches to the proficed user if they are a allowed as a service company admin
     *
     * @param json User id of the user to be switch to, just need user.id
     */
    public static void switchUser(Context context, User json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/users/switch");

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/users/switch")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "switchUser", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: updatePayByUser
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     */
    public static void updatePay(Context context, Integer userId, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("updatePayByUser")
                .label(userId + "")
                .category("user")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PATCH")
                    .path("/api/rest/v2/users/" + userId + "/pay");

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PATCH//api/rest/v2/users/{user_id}/pay")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "updatePay", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: updateSettingsByUser
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     */
    public static void updateSettings(Context context, Integer userId, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("updateSettingsByUser")
                .label(userId + "")
                .category("user")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PATCH")
                    .path("/api/rest/v2/users/" + userId + "/settings");

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PATCH//api/rest/v2/users/{user_id}/settings")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "updateSettings", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: updateTaxByUser
     * Update tax info.
     *
     * @param userId User ID
     * @param json   Json User tax info object for updating
     */
    public static void updateTax(Context context, Integer userId, UserTaxInfoUpdate json, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("updateTaxByUser")
                .label(userId + "")
                .category("user")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/tax");

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/tax")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "updateTax", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: updateTourByUser
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     */
    public static void updateTour(Context context, Integer userId, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("updateTourByUser")
                .label(userId + "")
                .category("user")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PATCH")
                    .path("/api/rest/v2/users/" + userId + "/tour");

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PATCH//api/rest/v2/users/{user_id}/tour")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "updateTour", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: uploadProfilePhoto
     * Upload profile photo
     *
     * @param userId User ID
     * @param file   Photo to upload
     */
    public static void uploadProfilePhoto(Context context, Integer userId, java.io.File file, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("uploadProfilePhoto")
                .label(userId + "")
                .category("user")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/profile/avatar")
                    .multipartFile("file", file.getName(), Uri.fromFile(file));

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);
            methodParams.put("file", Uri.fromFile(file));

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/profile/avatar")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "uploadProfilePhoto", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Swagger operationId: verifyAccount
     * Verify account
     *
     * @param userId User ID
     * @param json   Json Payload
     */
    public static void verifyAccount(Context context, Integer userId, String json, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("verifyAccount")
                .label(userId + "")
                .category("user")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/verify/2fa");

            if (json != null)
                builder.body(json);

            JsonObject methodParams = new JsonObject();
            methodParams.put("userId", userId);
            if (json != null)
                methodParams.put("json", json);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/verify/2fa")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("ADDRESS_WEB_API_V2/UsersWebApi",
                                    UsersWebApi.class, "verifyAccount", methodParams))
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
        private static List<UsersWebApi> APIS = new LinkedList<>();
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

        public static void parse(UsersWebApi usersWebApi, Bundle bundle) {
            synchronized (SYNC_LOCK) {
                APIS.add(usersWebApi);
                BUNDLES.add(bundle);
            }

            getManager().wakeUp();
        }

        private static class Parser extends ThreadManager.ManagedThread {
            public Parser(ThreadManager manager) {
                super(manager);
                setName("UsersWebApi/Parser");
                start();
            }

            @Override
            public boolean doWork() {
                UsersWebApi webApi = null;
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
                            case "addCoords":
                            case "addTypesOfWork":
                            case "sendAccountActivationLink":
                            case "sendVerificationCodeViaSms":
                            case "sendVerificationCodeViaVoiceCall":
                            case "setUserPreference":
                            case "swipNotification":
                            case "switchUser":
                            case "updateTax":
                            case "uploadProfilePhoto":
                            case "verifyAccount":
                                successObject = data;
                                break;
                            case "getProfileAndWorkHistory":
                                successObject = ProfileAndWorkHistory.fromJson(new JsonObject(data));
                                break;
                            case "getWorkHistory":
                                successObject = new JsonObject(data);
                                break;
                            case "getUserPreferenceValue":
                                successObject = AaaaPlaceholder.fromJson(new JsonObject(data));
                                break;
                            case "getUserPreferredProviderNetworks":
                                successObject = PPNs.fromJson(new JsonObject(data));
                                break;
                            case "getUserTypesOfWork":
                                successObject = TypesOfWork.fromJson(new JsonObject(data));
                                break;
                            case "getTax":
                                successObject = UserTaxInfo.fromJson(new JsonObject(data));
                                break;
                            case "addPay":
                            case "getPay":
                            case "getSettings":
                            case "getTour":
                            case "getUser":
                            case "updatePay":
                            case "updateSettings":
                            case "updateTour":
                                successObject = User.fromJson(new JsonObject(data));
                                break;
                            default:
                                Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                                break;
                        }
                    } else if (data != null) {
                        switch (transactionParams.apiFunction) {
                            case "addCoords":
                            case "addPay":
                            case "addTypesOfWork":
                            case "getPay":
                            case "getProfileAndWorkHistory":
                            case "getSettings":
                            case "getTax":
                            case "getTour":
                            case "getUser":
                            case "getUserPreferenceValue":
                            case "getUserPreferredProviderNetworks":
                            case "getUserTypesOfWork":
                            case "getWorkHistory":
                            case "sendAccountActivationLink":
                            case "sendVerificationCodeViaSms":
                            case "sendVerificationCodeViaVoiceCall":
                            case "setUserPreference":
                            case "swipNotification":
                            case "switchUser":
                            case "updatePay":
                            case "updateSettings":
                            case "updateTax":
                            case "updateTour":
                            case "uploadProfilePhoto":
                            case "verifyAccount":
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
        private UsersWebApi usersWebApi;
        private TransactionParams transactionParams;
        private Object successObject;
        private boolean success;
        private Object failObject;

        public Deliverator(UsersWebApi usersWebApi, TransactionParams transactionParams,
                           Object successObject, boolean success, Object failObject) {
            this.usersWebApi = usersWebApi;
            this.transactionParams = transactionParams;
            this.successObject = successObject;
            this.success = success;
            this.failObject = failObject;
        }

        @Override
        public void run() {
            usersWebApi.onComplete(transactionParams, transactionParams.apiFunction, successObject, success, failObject);
        }
    }
}
