package com.fieldnation.data.bv2.client;

import android.content.Context;

import com.fieldnation.data.bv2.model.UserTaxInfoUpdate;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class UsersWebApi {
    private static final String TAG = "UsersWebApi";

    /**
     * Send account verification code via text message
     *
     * @param userId User ID
     * @param json   JSON Payload
     */
    public static void sendVerificationCodeViaSms(Context context, int userId, String json) {
    }

    /**
     * Send account activation link
     *
     * @param userId User ID
     * @param json   JSON Payload
     */
    public static void sendAccountActivationLink(Context context, int userId, String json) {
    }

    /**
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     */
    public static void updateSettings(Context context, int userId) {
    }

    /**
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     */
    public static void getSettings(Context context, int userId) {
    }

    /**
     * Update tax info.
     *
     * @param userId User ID
     * @param json   Json User tax info object for updating
     */
    public static void updateTax(Context context, int userId, UserTaxInfoUpdate json) {
    }

    /**
     * Get tax info
     *
     * @param userId User ID
     */
    public static void getTax(Context context, int userId) {
    }

    /**
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     */
    public static void updatePay(Context context, int userId) {
    }

    /**
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     */
    public static void addPay(Context context, int userId) {
    }

    /**
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     */
    public static void getPay(Context context, int userId) {
    }

    /**
     * Set user preference
     *
     * @param userId     User ID
     * @param preference Preference Key
     * @param json       JSON Model
     */
    public static void setUserPreference(Context context, int userId, String preference, String json) {
    }

    /**
     * Get user preference value
     *
     * @param userId     User ID
     * @param preference Preference Key
     */
    public static void getUserPreferenceValue(Context context, int userId, String preference) {
    }

    /**
     * Upload profile photo
     *
     * @param userId User ID
     * @param file   Photo to upload
     */
    public static void uploadProfilePhoto(Context context, int userId, java.io.File file) {
    }

    /**
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     */
    public static void updateTour(Context context, int userId) {
    }

    /**
     * Submit individual updates to the tour state as a user onboards the site.
     *
     * @param userId User ID
     */
    public static void getTour(Context context, int userId) {
    }

    /**
     * Returns summary details about a user profile.
     *
     * @param user User ID
     */
    public static void getUser(Context context, String user) {
    }

    /**
     * Send account verification code via phone call
     *
     * @param userId User ID
     * @param json   JSON Payload
     */
    public static void sendVerificationCodeViaVoiceCall(Context context, int userId, String json) {
    }

    /**
     * Add types of work to profile
     *
     * @param userId User ID
     * @param json   JSON model
     */
    public static void addTypesOfWork(Context context, int userId, String json) {
    }

    /**
     * Get all types of work of a specific user
     *
     * @param userId User ID
     */
    public static void getUserTypesOfWork(Context context, int userId) {
    }

    /**
     * Verify account
     *
     * @param userId User ID
     * @param json   Json Payload
     */
    public static void verifyAccount(Context context, int userId, String json) {
    }

}
