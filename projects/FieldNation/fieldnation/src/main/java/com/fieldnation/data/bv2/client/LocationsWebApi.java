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

public class LocationsWebApi {
    private static final String TAG = "LocationsWebApi";

    /**
     * Adds a note to a stored location
     *
     * @param locationId Location id
     * @param json Notes
     * @param isBackground indicates that this call is low priority
     */
    public static void addNotes(Context context, int locationId, LocationNote json, boolean isBackground) {
    }

    /**
     * Adds an attribute to a stored location
     *
     * @param locationId Location id
     * @param attribute Attribute
     * @param json JSON Model
     * @param isBackground indicates that this call is low priority
     */
    public static void addAttribute(Context context, int locationId, int attribute, LocationAttribute json, boolean isBackground) {
    }

    /**
     * Removes an attribute from a stored location
     *
     * @param locationId Location id
     * @param attribute Attribute
     * @param isBackground indicates that this call is low priority
     */
    public static void removeAttribute(Context context, int locationId, int attribute, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//locations/{location_id}/attributes/{attribute}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/locations/" + locationId + "/attributes/" + attribute)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Get a list of supported countries for selection
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getCountries(Context context, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//locations/countries")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/locations/countries")
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Add a location to company
     *
     * @param json JSON payload
     * @param isBackground indicates that this call is low priority
     */
    public static void addLocations(Context context, StoredLocation json, boolean isBackground) {
    }

    /**
     * Gets stored locations
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getLocations(Context context, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//locations")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/locations")
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Removes a note attached to a stored location
     *
     * @param locationId Location id
     * @param noteId Location note id
     * @param isBackground indicates that this call is low priority
     */
    public static void removeNote(Context context, int locationId, int noteId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//locations/{location_id}/notes/{note_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/locations/" + locationId + "/notes/" + noteId)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Updates a note attached to a stored location
     *
     * @param locationId Location id
     * @param noteId Location note id
     * @param json Notes
     * @param isBackground indicates that this call is low priority
     */
    public static void updateNote(Context context, int locationId, int noteId, LocationNote json, boolean isBackground) {
    }

    /**
     * Soft deletes a stored location
     *
     * @param locationId Location id
     * @param isBackground indicates that this call is low priority
     */
    public static void removeLocation(Context context, int locationId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//locations/{location_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/locations/" + locationId)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Updates a stored location
     *
     * @param locationId Location id
     * @param isBackground indicates that this call is low priority
     */
    public static void updateLocation(Context context, int locationId, boolean isBackground) {
    }

}
