package com.fieldnation.data.bv2.client;

import android.content.Context;

import com.fieldnation.data.bv2.model.*;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class UsersWebApi {
    private static final String TAG = "UsersWebApi";

    /**
     * Send account verification code via text message
     *
     * @param userId User ID
     * @param json JSON Payload
     * @param isBackground indicates that this call is low priority
     */
    public static void sendVerificationCodeViaSms(Context context, Integer userId, String json, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/verify/text")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .path("/api/rest/v2/users/" + userId + "/verify/text")
                            .body(json)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Send account activation link
     *
     * @param userId User ID
     * @param json JSON Payload
     * @param isBackground indicates that this call is low priority
     */
    public static void sendAccountActivationLink(Context context, Integer userId, String json, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/verify/email")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .path("/api/rest/v2/users/" + userId + "/verify/email")
                            .body(json)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     * @param isBackground indicates that this call is low priority
     */
    public static void updateSettings(Context context, Integer userId, boolean isBackground) {
    }

    /**
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getSettings(Context context, Integer userId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/settings")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v2/users/" + userId + "/settings")
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Update tax info.
     *
     * @param userId User ID
     * @param json Json User tax info object for updating
     * @param isBackground indicates that this call is low priority
     */
    public static void updateTax(Context context, Integer userId, UserTaxInfoUpdate json, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/tax")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .path("/api/rest/v2/users/" + userId + "/tax")
                            .body(json.toJson().toString())
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Get tax info
     *
     * @param userId User ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getTax(Context context, Integer userId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/tax")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v2/users/" + userId + "/tax")
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     * @param isBackground indicates that this call is low priority
     */
    public static void updatePay(Context context, Integer userId, boolean isBackground) {
    }

    /**
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     * @param isBackground indicates that this call is low priority
     */
    public static void addPay(Context context, Integer userId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/pay")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .path("/api/rest/v2/users/" + userId + "/pay")
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getPay(Context context, Integer userId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/pay")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v2/users/" + userId + "/pay")
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Set user preference
     *
     * @param userId User ID
     * @param preference Preference Key
     * @param json JSON Model
     * @param isBackground indicates that this call is low priority
     */
    public static void setUserPreference(Context context, Integer userId, String preference, String json, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/preferences/{preference}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .path("/api/rest/v2/users/" + userId + "/preferences/" + preference)
                            .body(json)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/preferences/{preference}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v2/users/" + userId + "/preferences/" + preference)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Upload profile photo
     *
     * @param userId User ID
     * @param file Photo to upload
     * @param isBackground indicates that this call is low priority
     */
    public static void uploadProfilePhoto(Context context, Integer userId, java.io.File file, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/profile/avatar")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .path("/api/rest/v2/users/" + userId + "/profile/avatar")
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     * @param isBackground indicates that this call is low priority
     */
    public static void updateTour(Context context, Integer userId, boolean isBackground) {
    }

    /**
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getTour(Context context, Integer userId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/tour")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v2/users/" + userId + "/tour")
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Returns summary details about a user profile.
     *
     * @param user User ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getUser(Context context, String user, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v2/users/" + user)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Send account verification code via phone call
     *
     * @param userId User ID
     * @param json JSON Payload
     * @param isBackground indicates that this call is low priority
     */
    public static void sendVerificationCodeViaVoiceCall(Context context, Integer userId, String json, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/verify/phone")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .path("/api/rest/v2/users/" + userId + "/verify/phone")
                            .body(json)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Add types of work to profile
     *
     * @param userId User ID
     * @param json JSON model
     * @param isBackground indicates that this call is low priority
     */
    public static void addTypesOfWork(Context context, Integer userId, String json, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/types-of-work")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .path("/api/rest/v2/users/" + userId + "/types-of-work")
                            .body(json)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Get all types of work of a specific user
     *
     * @param userId User ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getUserTypesOfWork(Context context, Integer userId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/users/{user_id}/types-of-work")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v2/users/" + userId + "/types-of-work")
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Verify account
     *
     * @param userId User ID
     * @param json Json Payload
     * @param isBackground indicates that this call is low priority
     */
    public static void verifyAccount(Context context, Integer userId, String json, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/users/{user_id}/verify/2fa")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .path("/api/rest/v2/users/" + userId + "/verify/2fa")
                            .body(json)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

}
