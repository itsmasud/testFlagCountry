package com.fieldnation.v2.data.client;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.analytics.contexts.SpWorkOrderContext;
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
import com.fieldnation.v2.data.model.Countries;
import com.fieldnation.v2.data.model.Error;
import com.fieldnation.v2.data.model.IdResponse;
import com.fieldnation.v2.data.model.LocationAttribute;
import com.fieldnation.v2.data.model.LocationNote;
import com.fieldnation.v2.data.model.LocationProviders;
import com.fieldnation.v2.data.model.StoredLocation;
import com.fieldnation.v2.data.model.StoredLocations;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmgen from swagger.
 */

public abstract class LocationsWebApi extends Pigeon {
    private static final String TAG = "LocationsWebApi";

    public void sub() {
        PigeonRoost.sub(this, "ADDRESS_WEB_API_V2/LocationsWebApi");
    }

    public void unsub() {
        PigeonRoost.unsub(this, "ADDRESS_WEB_API_V2/LocationsWebApi");
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "addAttribute", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "addLocations", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "addNotes", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "deleteAttribute", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "deleteLocation", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "deleteNote", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "getCountries", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "getLocations", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "getProviders", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "updateLocation", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
                            TransactionListener.params("ADDRESS_WEB_API_V2/LocationsWebApi",
                                    LocationsWebApi.class, "updateNote", methodParams))
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
        private static List<LocationsWebApi> APIS = new LinkedList<>();
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

        public static void parse(LocationsWebApi locationsWebApi, Bundle bundle) {
            synchronized (SYNC_LOCK) {
                APIS.add(locationsWebApi);
                BUNDLES.add(bundle);
            }

            getManager().wakeUp();
        }

        private static class Parser extends ThreadManager.ManagedThread {
            public Parser(ThreadManager manager) {
                super(manager);
                setName("LocationsWebApi/Parser");
                start();
        }

        @Override
            public boolean doWork() {
                LocationsWebApi webApi = null;
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
                } else if (data != null) {
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
        private LocationsWebApi locationsWebApi;
        private TransactionParams transactionParams;
        private Object successObject;
        private boolean success;
        private Object failObject;

        public Deliverator(LocationsWebApi locationsWebApi, TransactionParams transactionParams,
                           Object successObject, boolean success, Object failObject) {
            this.locationsWebApi = locationsWebApi;
            this.transactionParams = transactionParams;
            this.successObject = successObject;
            this.success = success;
            this.failObject = failObject;
        }

        @Override
        public void run() {
            locationsWebApi.onComplete(transactionParams, transactionParams.apiFunction, successObject, success, failObject);
        }
    }
}
