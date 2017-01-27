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

public class LocationsWebApi extends TopicClient {
    private static final String STAG = "LocationsWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);


    public LocationsWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }
    /**
     * Adds a note to a stored location
     *
     * @param locationId Location id
     * @param json Notes
     */
    public static void addNotes(Context context, Integer locationId, LocationNote json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/locations/" + locationId + "/notes");

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/locations/{location_id}/notes")
                    .key(misc.md5("POST/" + "/api/rest/v2/locations/" + locationId + "/notes"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddNotes(Integer locationId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/locations/" + locationId + "/notes"));
    }
    /**
     * Adds an attribute to a stored location
     *
     * @param locationId Location id
     * @param attribute Attribute
     * @param json JSON Model
     */
    public static void addAttribute(Context context, Integer locationId, Integer attribute, LocationAttribute json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/locations/" + locationId + "/attributes/" + attribute);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/locations/{location_id}/attributes/{attribute}")
                    .key(misc.md5("POST/" + "/api/rest/v2/locations/" + locationId + "/attributes/" + attribute))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddAttribute(Integer locationId, Integer attribute) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/locations/" + locationId + "/attributes/" + attribute));
    }
    /**
     * Removes an attribute from a stored location
     *
     * @param locationId Location id
     * @param attribute Attribute
     */
    public static void removeAttribute(Context context, Integer locationId, Integer attribute) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/locations/" + locationId + "/attributes/" + attribute);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/locations/{location_id}/attributes/{attribute}")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/locations/" + locationId + "/attributes/" + attribute))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveAttribute(Integer locationId, Integer attribute) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/locations/" + locationId + "/attributes/" + attribute));
    }
    /**
     * Get a list of supported countries for selection
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getCountries(Context context, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/locations/countries");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/locations/countries")
                    .key(misc.md5("GET/" + "/api/rest/v2/locations/countries"))
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

    public boolean subGetCountries() {
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/locations/countries"));
    }
    /**
     * Add a location to company
     *
     * @param json JSON payload
     */
    public static void addLocations(Context context, StoredLocation json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/locations");

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/locations")
                    .key(misc.md5("POST/" + "/api/rest/v2/locations"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddLocations() {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/locations"));
    }
    /**
     * Gets stored locations
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getLocations(Context context, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/locations");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/locations")
                    .key(misc.md5("GET/" + "/api/rest/v2/locations"))
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

    public boolean subGetLocations() {
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/locations"));
    }
    /**
     * Removes a note attached to a stored location
     *
     * @param locationId Location id
     * @param noteId Location note id
     */
    public static void removeNote(Context context, Integer locationId, Integer noteId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/locations/" + locationId + "/notes/" + noteId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/locations/{location_id}/notes/{note_id}")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/locations/" + locationId + "/notes/" + noteId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveNote(Integer locationId, Integer noteId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/locations/" + locationId + "/notes/" + noteId));
    }
    /**
     * Updates a note attached to a stored location
     *
     * @param locationId Location id
     * @param noteId Location note id
     * @param json Notes
     */
    public static void updateNote(Context context, Integer locationId, Integer noteId, LocationNote json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/locations/" + locationId + "/notes/" + noteId);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/locations/{location_id}/notes/{note_id}")
                    .key(misc.md5("PUT/" + "/api/rest/v2/locations/" + locationId + "/notes/" + noteId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateNote(Integer locationId, Integer noteId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/locations/" + locationId + "/notes/" + noteId));
    }
    /**
     * Soft deletes a stored location
     *
     * @param locationId Location id
     */
    public static void removeLocation(Context context, Integer locationId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/locations/" + locationId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/locations/{location_id}")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/locations/" + locationId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveLocation(Integer locationId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/locations/" + locationId));
    }
    /**
     * Updates a stored location
     *
     * @param locationId Location id
     */
    public static void updateLocation(Context context, Integer locationId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/locations/" + locationId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/locations/{location_id}")
                    .key(misc.md5("PUT/" + "/api/rest/v2/locations/" + locationId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateLocation(Integer locationId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/locations/" + locationId));
    }
}
