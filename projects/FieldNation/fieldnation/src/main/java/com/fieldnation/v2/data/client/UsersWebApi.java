package com.fieldnation.v2.data.client;

import android.content.Context;
import android.net.Uri;
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

public class UsersWebApi extends TopicClient {
    private static final String STAG = "UsersWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);


    public UsersWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public boolean subUsersWebApi(){
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi");
    }

    /**
     * Swagger operationId: sendVerificationCodeViaSms
     * Send account verification code via text message
     *
     * @param userId User ID
     * @param json JSON Payload
     */
    public static void sendVerificationCodeViaSms(Context context, Integer userId, String json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/verify/text");

            if (json != null)
                builder.body(json);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/verify/text")
                    .key(misc.md5("POST//api/rest/v2/users/" + userId + "/verify/text"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/verify/text",
                                    UsersWebApi.class, "sendVerificationCodeViaSms"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subSendVerificationCodeViaSms(Integer userId) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/verify/text");
    }

    /**
     * Swagger operationId: sendAccountActivationLink
     * Send account activation link
     *
     * @param userId User ID
     * @param json JSON Payload
     */
    public static void sendAccountActivationLink(Context context, Integer userId, String json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/verify/email");

            if (json != null)
                builder.body(json);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/verify/email")
                    .key(misc.md5("POST//api/rest/v2/users/" + userId + "/verify/email"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/verify/email",
                                    UsersWebApi.class, "sendAccountActivationLink"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subSendAccountActivationLink(Integer userId) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/verify/email");
    }

    /**
     * Swagger operationId: updateSettingsByUser
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     */
    public static void updateSettings(Context context, Integer userId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PATCH")
                    .path("/api/rest/v2/users/" + userId + "/settings");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PATCH//api/rest/v2/users/{user_id}/settings")
                    .key(misc.md5("PATCH//api/rest/v2/users/" + userId + "/settings"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/settings",
                                    UsersWebApi.class, "updateSettings"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateSettings(Integer userId) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/settings");
    }

    /**
     * Swagger operationId: getSettingsByUser
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getSettings(Context context, Integer userId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + userId + "/settings");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/settings")
                    .key(misc.md5("GET//api/rest/v2/users/" + userId + "/settings"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/settings",
                                    UsersWebApi.class, "getSettings"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetSettings(Integer userId) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/settings");
    }

    /**
     * Swagger operationId: updateTaxByUser
     * Update tax info.
     *
     * @param userId User ID
     * @param json Json User tax info object for updating
     */
    public static void updateTax(Context context, Integer userId, UserTaxInfoUpdate json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/tax");

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/tax")
                    .key(misc.md5("POST//api/rest/v2/users/" + userId + "/tax"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/tax",
                                    UsersWebApi.class, "updateTax"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateTax(Integer userId) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/tax");
    }

    /**
     * Swagger operationId: getTaxByUser
     * Get tax info
     *
     * @param userId User ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getTax(Context context, Integer userId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + userId + "/tax");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/tax")
                    .key(misc.md5("GET//api/rest/v2/users/" + userId + "/tax"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/tax",
                                    UsersWebApi.class, "getTax"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetTax(Integer userId) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/tax");
    }

    /**
     * Swagger operationId: updatePayByUser
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     */
    public static void updatePay(Context context, Integer userId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PATCH")
                    .path("/api/rest/v2/users/" + userId + "/pay");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PATCH//api/rest/v2/users/{user_id}/pay")
                    .key(misc.md5("PATCH//api/rest/v2/users/" + userId + "/pay"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/pay",
                                    UsersWebApi.class, "updatePay"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdatePay(Integer userId) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/pay");
    }

    /**
     * Swagger operationId: addPayByUser
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     */
    public static void addPay(Context context, Integer userId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/pay");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/pay")
                    .key(misc.md5("POST//api/rest/v2/users/" + userId + "/pay"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/pay",
                                    UsersWebApi.class, "addPay"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddPay(Integer userId) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/pay");
    }

    /**
     * Swagger operationId: getPayByUser
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getPay(Context context, Integer userId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + userId + "/pay");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/pay")
                    .key(misc.md5("GET//api/rest/v2/users/" + userId + "/pay"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/pay",
                                    UsersWebApi.class, "getPay"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetPay(Integer userId) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/pay");
    }

    /**
     * Swagger operationId: setUserPreference
     * Set user preference
     *
     * @param userId User ID
     * @param preference Preference Key
     * @param json JSON Model
     */
    public static void setUserPreference(Context context, Integer userId, String preference, String json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/preferences/" + preference);

            if (json != null)
                builder.body(json);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/preferences/{preference}")
                    .key(misc.md5("POST//api/rest/v2/users/" + userId + "/preferences/" + preference))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/preferences/" + preference,
                                    UsersWebApi.class, "setUserPreference"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subSetUserPreference(Integer userId, String preference) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/preferences/" + preference);
    }

    /**
     * Swagger operationId: getUserPreferenceValueByKey
     * Get user preference value
     *
     * @param userId User ID
     * @param preference Preference Key
     * @param isBackground indicates that this call is low priority
     */
    public static void getUserPreferenceValue(Context context, Integer userId, String preference, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + userId + "/preferences/" + preference);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/preferences/{preference}")
                    .key(misc.md5("GET//api/rest/v2/users/" + userId + "/preferences/" + preference))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/preferences/" + preference,
                                    UsersWebApi.class, "getUserPreferenceValue"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetUserPreferenceValue(Integer userId, String preference) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/preferences/" + preference);
    }

    /**
     * Swagger operationId: uploadProfilePhoto
     * Upload profile photo
     *
     * @param userId User ID
     * @param file Photo to upload
     */
    public static void uploadProfilePhoto(Context context, Integer userId, java.io.File file) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/profile/avatar")
                    .multipartFile("file", file.getName(), Uri.fromFile(file));

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/profile/avatar")
                    .key(misc.md5("POST//api/rest/v2/users/" + userId + "/profile/avatar"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/profile/avatar",
                                    UsersWebApi.class, "uploadProfilePhoto"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUploadProfilePhoto(Integer userId) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/profile/avatar");
    }

    /**
     * Swagger operationId: updateTourByUser
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     */
    public static void updateTour(Context context, Integer userId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PATCH")
                    .path("/api/rest/v2/users/" + userId + "/tour");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PATCH//api/rest/v2/users/{user_id}/tour")
                    .key(misc.md5("PATCH//api/rest/v2/users/" + userId + "/tour"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/tour",
                                    UsersWebApi.class, "updateTour"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateTour(Integer userId) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/tour");
    }

    /**
     * Swagger operationId: getTourByUser
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getTour(Context context, Integer userId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + userId + "/tour");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/tour")
                    .key(misc.md5("GET//api/rest/v2/users/" + userId + "/tour"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/tour",
                                    UsersWebApi.class, "getTour"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetTour(Integer userId) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/tour");
    }

    /**
     * Swagger operationId: getUser
     * Returns summary details about a user profile.
     *
     * @param user User ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getUser(Context context, String user, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + user);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user}")
                    .key(misc.md5("GET//api/rest/v2/users/" + user))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + user,
                                    UsersWebApi.class, "getUser"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetUser(String user) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + user);
    }

    /**
     * Swagger operationId: sendVerificationCodeViaVoiceCall
     * Send account verification code via phone call
     *
     * @param userId User ID
     * @param json JSON Payload
     */
    public static void sendVerificationCodeViaVoiceCall(Context context, Integer userId, String json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/verify/phone");

            if (json != null)
                builder.body(json);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/verify/phone")
                    .key(misc.md5("POST//api/rest/v2/users/" + userId + "/verify/phone"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/verify/phone",
                                    UsersWebApi.class, "sendVerificationCodeViaVoiceCall"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subSendVerificationCodeViaVoiceCall(Integer userId) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/verify/phone");
    }

    /**
     * Swagger operationId: addTypesOfWork
     * Add types of work to profile
     *
     * @param userId User ID
     * @param json JSON model
     */
    public static void addTypesOfWork(Context context, Integer userId, String json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/types-of-work");

            if (json != null)
                builder.body(json);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/types-of-work")
                    .key(misc.md5("POST//api/rest/v2/users/" + userId + "/types-of-work"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/types-of-work",
                                    UsersWebApi.class, "addTypesOfWork"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddTypesOfWork(Integer userId) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/types-of-work");
    }

    /**
     * Swagger operationId: getUserTypesOfWork
     * Get all types of work of a specific user
     *
     * @param userId User ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getUserTypesOfWork(Context context, Integer userId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/users/" + userId + "/types-of-work");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/types-of-work")
                    .key(misc.md5("GET//api/rest/v2/users/" + userId + "/types-of-work"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/types-of-work",
                                    UsersWebApi.class, "getUserTypesOfWork"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetUserTypesOfWork(Integer userId) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/types-of-work");
    }

    /**
     * Swagger operationId: verifyAccount
     * Verify account
     *
     * @param userId User ID
     * @param json Json Payload
     */
    public static void verifyAccount(Context context, Integer userId, String json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/users/" + userId + "/verify/2fa");

            if (json != null)
                builder.body(json);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/verify/2fa")
                    .key(misc.md5("POST//api/rest/v2/users/" + userId + "/verify/2fa"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/verify/2fa",
                                    UsersWebApi.class, "verifyAccount"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subVerifyAccount(Integer userId) {
        return register("TOPIC_ID_WEB_API_V2/UsersWebApi/" + userId + "/verify/2fa");
    }


    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            new AsyncParser(this, (Bundle) payload);
        }

        public void onUsersWebApi(String methodName, Object successObject, boolean success, Object failObject) {
        }

        public void onSendVerificationCodeViaSms(boolean success, Error error) {
        }

        public void onSendAccountActivationLink(boolean success, Error error) {
        }

        public void onUpdateSettings(User user, boolean success, Error error) {
        }

        public void onGetSettings(User user, boolean success, Error error) {
        }

        public void onUpdateTax(boolean success, Error error) {
        }

        public void onGetTax(UserTaxInfo userTaxInfo, boolean success, Error error) {
        }

        public void onUpdatePay(User user, boolean success, Error error) {
        }

        public void onAddPay(User user, boolean success, Error error) {
        }

        public void onGetPay(User user, boolean success, Error error) {
        }

        public void onSetUserPreference(boolean success, Error error) {
        }

        public void onGetUserPreferenceValue(AaaaPlaceholder aaaaPlaceholder, boolean success, Error error) {
        }

        public void onUploadProfilePhoto(boolean success, Error error) {
        }

        public void onUpdateTour(User user, boolean success, Error error) {
        }

        public void onGetTour(User user, boolean success, Error error) {
        }

        public void onGetUser(User user, boolean success, Error error) {
        }

        public void onSendVerificationCodeViaVoiceCall(boolean success, Error error) {
        }

        public void onAddTypesOfWork(boolean success, Error error) {
        }

        public void onGetUserTypesOfWork(TypesOfWork typesOfWork, boolean success, Error error) {
        }

        public void onVerifyAccount(boolean success, Error error) {
        }

    }

    private static class AsyncParser extends AsyncTaskEx<Object, Object, Object> {
        private static final String TAG = "UsersWebApi.AsyncParser";

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
                    case "sendVerificationCodeViaSms":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "sendAccountActivationLink":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateSettings":
                        if (success)
                            successObject = User.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getSettings":
                        if (success)
                            successObject = User.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateTax":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getTax":
                        if (success)
                            successObject = UserTaxInfo.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updatePay":
                        if (success)
                            successObject = User.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addPay":
                        if (success)
                            successObject = User.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getPay":
                        if (success)
                            successObject = User.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "setUserPreference":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getUserPreferenceValue":
                        if (success)
                            successObject = AaaaPlaceholder.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "uploadProfilePhoto":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateTour":
                        if (success)
                            successObject = User.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getTour":
                        if (success)
                            successObject = User.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getUser":
                        if (success)
                            successObject = User.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "sendVerificationCodeViaVoiceCall":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addTypesOfWork":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getUserTypesOfWork":
                        if (success)
                            successObject = TypesOfWork.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "verifyAccount":
                        if (!success)
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
                listener.onUsersWebApi(transactionParams.apiFunction, successObject, success, failObject);
                switch (transactionParams.apiFunction) {
                    case "sendVerificationCodeViaSms":
                        listener.onSendVerificationCodeViaSms(success, (Error) failObject);
                        break;
                    case "sendAccountActivationLink":
                        listener.onSendAccountActivationLink(success, (Error) failObject);
                        break;
                    case "updateSettings":
                        listener.onUpdateSettings((User) successObject, success, (Error) failObject);
                        break;
                    case "getSettings":
                        listener.onGetSettings((User) successObject, success, (Error) failObject);
                        break;
                    case "updateTax":
                        listener.onUpdateTax(success, (Error) failObject);
                        break;
                    case "getTax":
                        listener.onGetTax((UserTaxInfo) successObject, success, (Error) failObject);
                        break;
                    case "updatePay":
                        listener.onUpdatePay((User) successObject, success, (Error) failObject);
                        break;
                    case "addPay":
                        listener.onAddPay((User) successObject, success, (Error) failObject);
                        break;
                    case "getPay":
                        listener.onGetPay((User) successObject, success, (Error) failObject);
                        break;
                    case "setUserPreference":
                        listener.onSetUserPreference(success, (Error) failObject);
                        break;
                    case "getUserPreferenceValue":
                        listener.onGetUserPreferenceValue((AaaaPlaceholder) successObject, success, (Error) failObject);
                        break;
                    case "uploadProfilePhoto":
                        listener.onUploadProfilePhoto(success, (Error) failObject);
                        break;
                    case "updateTour":
                        listener.onUpdateTour((User) successObject, success, (Error) failObject);
                        break;
                    case "getTour":
                        listener.onGetTour((User) successObject, success, (Error) failObject);
                        break;
                    case "getUser":
                        listener.onGetUser((User) successObject, success, (Error) failObject);
                        break;
                    case "sendVerificationCodeViaVoiceCall":
                        listener.onSendVerificationCodeViaVoiceCall(success, (Error) failObject);
                        break;
                    case "addTypesOfWork":
                        listener.onAddTypesOfWork(success, (Error) failObject);
                        break;
                    case "getUserTypesOfWork":
                        listener.onGetUserTypesOfWork((TypesOfWork) successObject, success, (Error) failObject);
                        break;
                    case "verifyAccount":
                        listener.onVerifyAccount(success, (Error) failObject);
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
