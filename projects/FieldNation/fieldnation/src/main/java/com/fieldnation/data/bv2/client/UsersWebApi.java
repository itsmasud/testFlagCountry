package com.fieldnation.data.bv2.client;

import android.content.Context;
import android.net.Uri;

import com.fieldnation.data.bv2.model.*;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by dmgen from swagger on 1/27/17.
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
    /**
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
                    .key(misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/verify/text"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subSendVerificationCodeViaSms(Integer userId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/verify/text"));
    }
    /**
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
                    .key(misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/verify/email"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subSendAccountActivationLink(Integer userId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/verify/email"));
    }
    /**
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
                    .key(misc.md5("PATCH/" + "/api/rest/v2/users/" + userId + "/settings"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateSettings(Integer userId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PATCH/" + "/api/rest/v2/users/" + userId + "/settings"));
    }
    /**
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
                    .key(misc.md5("GET/" + "/api/rest/v2/users/" + userId + "/settings"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/users/" + userId + "/settings"));
    }
    /**
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
                    .key(misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/tax"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateTax(Integer userId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/tax"));
    }
    /**
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
                    .key(misc.md5("GET/" + "/api/rest/v2/users/" + userId + "/tax"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/users/" + userId + "/tax"));
    }
    /**
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
                    .key(misc.md5("PATCH/" + "/api/rest/v2/users/" + userId + "/pay"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdatePay(Integer userId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PATCH/" + "/api/rest/v2/users/" + userId + "/pay"));
    }
    /**
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
                    .key(misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/pay"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddPay(Integer userId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/pay"));
    }
    /**
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
                    .key(misc.md5("GET/" + "/api/rest/v2/users/" + userId + "/pay"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/users/" + userId + "/pay"));
    }
    /**
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
                    .key(misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/preferences/" + preference))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subSetUserPreference(Integer userId, String preference) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/preferences/" + preference));
    }
    /**
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
                    .key(misc.md5("GET/" + "/api/rest/v2/users/" + userId + "/preferences/" + preference))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/users/" + userId + "/preferences/" + preference));
    }
    /**
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
                    .key(misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/profile/avatar"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUploadProfilePhoto(Integer userId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/profile/avatar"));
    }
    /**
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
                    .key(misc.md5("PATCH/" + "/api/rest/v2/users/" + userId + "/tour"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateTour(Integer userId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PATCH/" + "/api/rest/v2/users/" + userId + "/tour"));
    }
    /**
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
                    .key(misc.md5("GET/" + "/api/rest/v2/users/" + userId + "/tour"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/users/" + userId + "/tour"));
    }
    /**
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
                    .key(misc.md5("GET/" + "/api/rest/v2/users/" + user))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/users/" + user));
    }
    /**
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
                    .key(misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/verify/phone"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subSendVerificationCodeViaVoiceCall(Integer userId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/verify/phone"));
    }
    /**
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
                    .key(misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/types-of-work"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddTypesOfWork(Integer userId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/types-of-work"));
    }
    /**
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
                    .key(misc.md5("GET/" + "/api/rest/v2/users/" + userId + "/types-of-work"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/users/" + userId + "/types-of-work"));
    }
    /**
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
                    .key(misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/verify/2fa"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subVerifyAccount(Integer userId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/users/" + userId + "/verify/2fa"));
    }
}
