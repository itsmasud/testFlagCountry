package com.fieldnation.data.bv2.client;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.data.bv2.listener.TransactionListener;
import com.fieldnation.data.bv2.listener.TransactionParams;
import com.fieldnation.data.bv2.model.*;
import com.fieldnation.data.bv2.model.Error;
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
 * Created by dmgen from swagger on 1/30/17.
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

    public boolean subLocationsWebApi(){
        return register("TOPIC_ID_WEB_API_V2/LocationsWebApi");
    }

    /**
     * Swagger operationId: addNotesByLocation
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
                    .key(misc.md5("POST//api/rest/v2/locations/" + locationId + "/notes"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi/" + locationId + "/notes",
                                    LocationsWebApi.class, "addNotes"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddNotes(Integer locationId) {
        return register("TOPIC_ID_WEB_API_V2/LocationsWebApi/" + locationId + "/notes");
    }

    /**
     * Swagger operationId: addAttributeByLocationAndAttribute
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
                    .key(misc.md5("POST//api/rest/v2/locations/" + locationId + "/attributes/" + attribute))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi/" + locationId + "/attributes/" + attribute,
                                    LocationsWebApi.class, "addAttribute"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddAttribute(Integer locationId, Integer attribute) {
        return register("TOPIC_ID_WEB_API_V2/LocationsWebApi/" + locationId + "/attributes/" + attribute);
    }

    /**
     * Swagger operationId: removeAttributeByLocationAndAttribute
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
                    .key(misc.md5("DELETE//api/rest/v2/locations/" + locationId + "/attributes/" + attribute))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi/" + locationId + "/attributes/" + attribute,
                                    LocationsWebApi.class, "removeAttribute"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveAttribute(Integer locationId, Integer attribute) {
        return register("TOPIC_ID_WEB_API_V2/LocationsWebApi/" + locationId + "/attributes/" + attribute);
    }

    /**
     * Swagger operationId: getCountries
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
                    .key(misc.md5("GET//api/rest/v2/locations/countries"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/locations/countries",
                                    LocationsWebApi.class, "getCountries"))
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
        return register("TOPIC_ID_WEB_API_V2/locations/countries");
    }

    /**
     * Swagger operationId: addLocations
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
                    .key(misc.md5("POST//api/rest/v2/locations"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/locations",
                                    LocationsWebApi.class, "addLocations"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddLocations() {
        return register("TOPIC_ID_WEB_API_V2/locations");
    }

    /**
     * Swagger operationId: getLocations
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
                    .key(misc.md5("GET//api/rest/v2/locations"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/locations",
                                    LocationsWebApi.class, "getLocations"))
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
        return register("TOPIC_ID_WEB_API_V2/locations");
    }

    /**
     * Swagger operationId: removeNoteByLocation
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
                    .key(misc.md5("DELETE//api/rest/v2/locations/" + locationId + "/notes/" + noteId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi/" + locationId + "/notes/" + noteId,
                                    LocationsWebApi.class, "removeNote"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveNote(Integer locationId, Integer noteId) {
        return register("TOPIC_ID_WEB_API_V2/LocationsWebApi/" + locationId + "/notes/" + noteId);
    }

    /**
     * Swagger operationId: updateNoteByLocation
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
                    .key(misc.md5("PUT//api/rest/v2/locations/" + locationId + "/notes/" + noteId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi/" + locationId + "/notes/" + noteId,
                                    LocationsWebApi.class, "updateNote"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateNote(Integer locationId, Integer noteId) {
        return register("TOPIC_ID_WEB_API_V2/LocationsWebApi/" + locationId + "/notes/" + noteId);
    }

    /**
     * Swagger operationId: removeLocation
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
                    .key(misc.md5("DELETE//api/rest/v2/locations/" + locationId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi/" + locationId,
                                    LocationsWebApi.class, "removeLocation"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveLocation(Integer locationId) {
        return register("TOPIC_ID_WEB_API_V2/LocationsWebApi/" + locationId);
    }

    /**
     * Swagger operationId: updateLocation
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
                    .key(misc.md5("PUT//api/rest/v2/locations/" + locationId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi/" + locationId,
                                    LocationsWebApi.class, "updateLocation"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateLocation(Integer locationId) {
        return register("TOPIC_ID_WEB_API_V2/LocationsWebApi/" + locationId);
    }


    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            new AsyncParser(this, (Bundle) payload);
        }

        public void onAddNotes(byte[] data, boolean success, Error error) {
        }

        public void onAddAttribute(IdResponse idResponse, boolean success, Error error) {
        }

        public void onRemoveAttribute(byte[] data, boolean success, Error error) {
        }

        public void onGetCountries(Countries countries, boolean success, Error error) {
        }

        public void onAddLocations(IdResponse idResponse, boolean success, Error error) {
        }

        public void onGetLocations(StoredLocations storedLocations, boolean success, Error error) {
        }

        public void onRemoveNote(byte[] data, boolean success, Error error) {
        }

        public void onUpdateNote(byte[] data, boolean success, Error error) {
        }

        public void onRemoveLocation(byte[] data, boolean success, Error error) {
        }

        public void onUpdateLocation(byte[] data, boolean success, Error error) {
        }

    }

    private static class AsyncParser extends AsyncTaskEx<Object, Object, Object> {
        private static final String TAG = "LocationsWebApi.AsyncParser";

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
                    case "addNotes":
                        if (success)
                            successObject = data;
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addAttribute":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeAttribute":
                        if (success)
                            successObject = data;
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getCountries":
                        if (success)
                            successObject = Countries.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addLocations":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getLocations":
                        if (success)
                            successObject = StoredLocations.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeNote":
                        if (success)
                            successObject = data;
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateNote":
                        if (success)
                            successObject = data;
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeLocation":
                        if (success)
                            successObject = data;
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateLocation":
                        if (success)
                            successObject = data;
                        else
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
                switch (transactionParams.apiFunction) {
                    case "addNotes":
                        listener.onAddNotes((byte[]) successObject, success, (Error) failObject);
                        break;
                    case "addAttribute":
                        listener.onAddAttribute((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "removeAttribute":
                        listener.onRemoveAttribute((byte[]) successObject, success, (Error) failObject);
                        break;
                    case "getCountries":
                        listener.onGetCountries((Countries) successObject, success, (Error) failObject);
                        break;
                    case "addLocations":
                        listener.onAddLocations((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "getLocations":
                        listener.onGetLocations((StoredLocations) successObject, success, (Error) failObject);
                        break;
                    case "removeNote":
                        listener.onRemoveNote((byte[]) successObject, success, (Error) failObject);
                        break;
                    case "updateNote":
                        listener.onUpdateNote((byte[]) successObject, success, (Error) failObject);
                        break;
                    case "removeLocation":
                        listener.onRemoveLocation((byte[]) successObject, success, (Error) failObject);
                        break;
                    case "updateLocation":
                        listener.onUpdateLocation((byte[]) successObject, success, (Error) failObject);
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
