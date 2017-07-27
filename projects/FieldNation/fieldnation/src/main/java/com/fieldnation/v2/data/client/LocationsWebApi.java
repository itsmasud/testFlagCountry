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

public class LocationsWebApi extends TopicClient {
    private static final String STAG = "LocationsWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);

    private static int connectCount = 0;

    public LocationsWebApi(Listener listener) {
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

    public boolean subLocationsWebApi() {
        return register("TOPIC_ID_WEB_API_V2/LocationsWebApi");
    }

    /**
     * Swagger operationId: addAttributeByLocationAndAttribute
     * Adds an attribute to a stored location
     *
     * @param locationId Location id
     * @param attribute  Attribute
     * @param json       JSON Model
     */
    public static void addAttribute(Context context, Integer locationId, Integer attribute, LocationAttribute json, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("addAttributeByLocationAndAttribute")
                .label(locationId + "")
                .category("workorder")
                .addContext(uiContext)
                .addContext(new SpWorkOrderContext.Builder().workOrderId(locationId).build())
                .property("attribute")
                .value(attribute)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/locations/" + locationId + "/attributes/" + attribute);

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("locationId", locationId);
            methodParams.put("attribute", attribute);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/locations/{location_id}/attributes/{attribute}")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "addAttribute", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
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
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/locations")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "addLocations", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addNotesByLocation
     * Adds a note to a stored location
     *
     * @param locationId Location id
     * @param json       Notes
     */
    public static void addNotes(Context context, Integer locationId, LocationNote json, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("addNotesByLocation")
                .label(locationId + "")
                .category("location")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/locations/" + locationId + "/notes");

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("locationId", locationId);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/locations/{location_id}/notes")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "addNotes", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteAttributeByLocationAndAttribute
     * Delete an attribute from a stored location
     *
     * @param locationId Location id
     * @param attribute  Attribute
     */
    public static void deleteAttribute(Context context, Integer locationId, Integer attribute, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("deleteAttributeByLocationAndAttribute")
                .label(locationId + "")
                .category("workorder")
                .addContext(uiContext)
                .addContext(new SpWorkOrderContext.Builder().workOrderId(locationId).build())
                .property("attribute")
                .value(attribute)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/locations/" + locationId + "/attributes/" + attribute);

            JsonObject methodParams = new JsonObject();
            methodParams.put("locationId", locationId);
            methodParams.put("attribute", attribute);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/locations/{location_id}/attributes/{attribute}")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "deleteAttribute", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteLocation
     * Soft deletes a stored location
     *
     * @param locationId Location id
     */
    public static void deleteLocation(Context context, Integer locationId, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("deleteLocation")
                .label(locationId + "")
                .category("location")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/locations/" + locationId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("locationId", locationId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/locations/{location_id}")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "deleteLocation", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteNoteByLocation
     * Deletes a note attached to a stored location
     *
     * @param locationId Location id
     * @param noteId     Location note id
     */
    public static void deleteNote(Context context, Integer locationId, Integer noteId, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("deleteNoteByLocation")
                .label(locationId + "")
                .category("location")
                .addContext(uiContext)
                .property("note_id")
                .value(noteId)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/locations/" + locationId + "/notes/" + noteId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("locationId", locationId);
            methodParams.put("noteId", noteId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/locations/{location_id}/notes/{note_id}")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "deleteNote", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getCountries
     * Get a list of supported countries for selection
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getCountries(Context context, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/locations/countries" + (isBackground ? ":isBackground" : ""));

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/locations/countries");

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/locations/countries")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "getCountries", methodParams))
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
     * Swagger operationId: getLocations
     * Gets stored locations
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getLocations(Context context, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/locations" + (isBackground ? ":isBackground" : ""));

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/locations");

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/locations")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "getLocations", methodParams))
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
     * Swagger operationId: getProvidersByLocationId
     * Get Providers Info By Location ID
     *
     * @param locationId   Location ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getProviders(Context context, Integer locationId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/locations/" + locationId + "/providers" + (isBackground ? ":isBackground" : ""));

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/locations/" + locationId + "/providers");

            JsonObject methodParams = new JsonObject();
            methodParams.put("locationId", locationId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/locations/{location_id}/providers")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "getProviders", methodParams))
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
     * Swagger operationId: updateLocation
     * Updates a stored location
     *
     * @param locationId Location id
     * @param json       JSON payload
     */
    public static void updateLocation(Context context, Integer locationId, StoredLocation json, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("updateLocation")
                .label(locationId + "")
                .category("location")
                .addContext(uiContext)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/locations/" + locationId);

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("locationId", locationId);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/locations/{location_id}")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "updateLocation", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateNoteByLocation
     * Updates a note attached to a stored location
     *
     * @param locationId Location id
     * @param noteId     Location note id
     * @param json       Notes
     */
    public static void updateNote(Context context, Integer locationId, Integer noteId, LocationNote json, EventContext uiContext) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action("updateNoteByLocation")
                .label(locationId + "")
                .category("location")
                .addContext(uiContext)
                .property("note_id")
                .value(noteId)
                .build()
        );

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/locations/" + locationId + "/notes/" + noteId);

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("locationId", locationId);
            methodParams.put("noteId", noteId);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/locations/{location_id}/notes/{note_id}")
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "updateNote", methodParams))
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

            Bundle bundle = (Bundle) payload;
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
                    new AsyncParser(this, bundle);
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
            Log.v(TAG, "Start doInBackground");
            Stopwatch watch = new Stopwatch(true);
            try {
                if (success) {
                    switch (transactionParams.apiFunction) {
                        case "getCountries":
                            successObject = Countries.fromJson(new JsonObject(data));
                            break;
                        case "addNotes":
                        case "deleteAttribute":
                        case "deleteLocation":
                        case "deleteNote":
                        case "updateLocation":
                        case "updateNote":
                            successObject = data;
                            break;
                        case "getProviders":
                            successObject = LocationProviders.fromJson(new JsonObject(data));
                            break;
                        case "getLocations":
                            successObject = StoredLocations.fromJson(new JsonObject(data));
                            break;
                        case "addAttribute":
                        case "addLocations":
                            successObject = IdResponse.fromJson(new JsonObject(data));
                            break;
                        default:
                            Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                            break;
                    }
                } else {
                    switch (transactionParams.apiFunction) {
                        case "addAttribute":
                        case "addLocations":
                        case "addNotes":
                        case "deleteAttribute":
                        case "deleteLocation":
                        case "deleteNote":
                        case "getCountries":
                        case "getLocations":
                        case "getProviders":
                        case "updateLocation":
                        case "updateNote":
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
