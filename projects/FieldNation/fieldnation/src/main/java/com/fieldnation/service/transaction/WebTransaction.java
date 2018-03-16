package com.fieldnation.service.transaction;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import com.fieldnation.App;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ContextProvider;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.tracker.TrackerEnum;
import com.fieldnation.service.tracker.UploadTrackerClient;
import com.fieldnation.service.transaction.WebTransactionSqlHelper.Column;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.AttachmentFolders;

import java.text.ParseException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Michael Carver on 3/3/2015.
 */
public class WebTransaction implements Parcelable, WebTransactionConstants {
    private static final String TAG = "WebTransaction";

    public static final String BROADCASE_ON_CHANGE = "WebTransaction.BROADCASE_ON_CHANGE";

    private long _id;
    private long _createdTime;
    private String _listenerClassName;
    private byte[] _listenerParams;
    private boolean _useAuth;
    private Type _type;
    private State _state;
    private Priority _priority;
    private String _requestString;
    private String _key;
    private long _queueTime;
    private boolean _wifiRequired;
    private boolean _track;
    private TrackerEnum _trackType;
    private String _timingKey;
    private boolean _wasZombie = false;
    private UUIDGroup _uuid;
    private int _tryCount = 0;
    private int _maxTries = 0;

    private int _notifId = -1;

    private NotificationDefinition _notifStart;
    private byte[] _notifStartArray;

    private NotificationDefinition _notifSuccess;
    private byte[] _notifSuccessArray;

    private NotificationDefinition _notifFailed;
    private byte[] _notifFailedArray;

    private NotificationDefinition _notifRetry;
    private byte[] _notifRetryArray;

    public enum State {
        BUILDING, IDLE, WORKING, ZOMBIE
    }

    public enum Type {
        ANY, NORMAL, CRAWLER, SYNC
    }


    public enum ActivityType {
        ACTIVITY_NAME,
        ACTIVITY_VALUE;
    }

    public enum ActivityName {
        DISCOUNT("Discount: "),
        EXPENSE("Expense: "),
        MESSAGE("Message: "),
        SHIPMENT("Shipment: "),
        SIGNATURE("Signature: "),
        CLOSING_NOTES("Closing Notes: "),
        ATTACHMENT("Attachment: "),
        CUSTOM_FIELD("Custom Field: "),
        REMOVE("Removed: ");

        private String value;

        ActivityName(String activityName) {
            this.value = activityName;
        }

        public static String getActivityTitleByType(ActivityName activityType, String tailingString) {
            return activityType.value + (misc.isEmptyOrNull(tailingString) ? "" : tailingString);
        }

        public static ActivityName fromString(String value) {
            ActivityName[] values = values();
            for (ActivityName v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }
    }


    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    WebTransaction(Cursor cursor) {
        _id = cursor.getLong(Column.ID.getIndex());
        _createdTime = cursor.getLong(Column.CREATED_TIME.getIndex());
        _listenerClassName = cursor.getString(Column.LISTENER.getIndex());
        _listenerParams = cursor.getBlob(Column.LISTENER_PARAMS.getIndex());
        _useAuth = cursor.getInt(Column.USE_AUTH.getIndex()) == 1;
        _state = State.values()[cursor.getInt(Column.STATE.getIndex())];
        _type = Type.values()[cursor.getInt(Column.TYPE.getIndex())];
        _queueTime = cursor.getLong(Column.QUEUE_TIME.getIndex());
        _tryCount = cursor.getInt(Column.TRY_COUNT.getIndex());
        _maxTries = cursor.getInt(Column.MAX_TRIES.getIndex());
        try {
            _requestString = new String(cursor.getBlob(Column.REQUEST.getIndex()));
        } catch (Exception ex) {
        }
        _priority = Priority.values()[cursor.getInt(Column.PRIORITY.getIndex())];
        _key = cursor.getString(Column.KEY.getIndex());
        _wifiRequired = cursor.getInt(Column.WIFI_REQUIRED.getIndex()) == 1;
        _track = cursor.getInt(Column.TRACK.getIndex()) == 1;
        _trackType = TrackerEnum.values()[cursor.getInt(Column.TRACK_TYPE.getIndex())];
        _timingKey = cursor.getString(Column.TIMING_KEY.getIndex());
        _wasZombie = cursor.getInt(Column.WAS_ZOMBIE.getIndex()) == 1;
        try {
            _uuid = UUIDGroup.fromJson(new JsonObject(cursor.getString(Column.UUID.getIndex())));
        } catch (Exception ex) {
        }
        _notifId = cursor.getInt(Column.NOTIF_ID.getIndex());

        _notifStartArray = cursor.getBlob(Column.NOTIF_START.getIndex());
        _notifSuccessArray = cursor.getBlob(Column.NOTIF_SUCCESS.getIndex());
        _notifFailedArray = cursor.getBlob(Column.NOTIF_FAILED.getIndex());
        _notifRetryArray = cursor.getBlob(Column.NOTIF_RETRY.getIndex());
    }

    public WebTransaction(Bundle bundle) {
        _id = bundle.getLong(PARAM_ID, -1);
        _createdTime = bundle.getLong(PARAM_CREATED_TIME);
        _listenerClassName = bundle.getString(PARAM_LISTENER_NAME);
        _listenerParams = bundle.getByteArray(PARAM_LISTENER_PARAMS);
        _useAuth = bundle.getBoolean(PARAM_USE_AUTH);
        _state = (State) bundle.getSerializable(PARAM_STATE);
        _type = (Type) bundle.getSerializable(PARAM_TYPE);
        _queueTime = bundle.getLong(PARAM_QUEUE_TIME);
        _tryCount = bundle.getInt(PARAM_TRY_COUNT);
        _maxTries = bundle.getInt(PARAM_MAX_TRIES);
        try {
            _requestString = new String(bundle.getByteArray(PARAM_REQUEST));
        } catch (Exception ex) {
        }
        _priority = (Priority) bundle.getSerializable(PARAM_PRIORITY);
        _key = bundle.getString(PARAM_KEY);
        _wifiRequired = bundle.getBoolean(PARAM_WIFI_REQUIRED);
        _track = bundle.getBoolean(PARAM_TRACK);
        _trackType = TrackerEnum.values()[bundle.getInt(PARAM_TRACK_ENUM)];
        _timingKey = bundle.getString(PARAM_TIMING_KEY);
        _wasZombie = bundle.getBoolean(PARAM_ZOMBIE);
        if (bundle.containsKey(PARAM_UUID))
            _uuid = bundle.getParcelable(PARAM_UUID);

        _notifId = bundle.getInt(PARAM_NOTIFICATION_ID);
        _notifStartArray = bundle.getByteArray(PARAM_NOTIFICATION_START);
        _notifSuccessArray = bundle.getByteArray(PARAM_NOTIFICATION_SUCCESS);
        _notifFailedArray = bundle.getByteArray(PARAM_NOTIFICATION_FAILED);
        _notifRetryArray = bundle.getByteArray(PARAM_NOTIFICATION_RETRY);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_ID, _id);
        bundle.putLong(PARAM_CREATED_TIME, _createdTime);

        if (_listenerClassName != null)
            bundle.putString(PARAM_LISTENER_NAME, _listenerClassName);

        if (_listenerParams != null)
            bundle.putByteArray(PARAM_LISTENER_PARAMS, _listenerParams);

        bundle.putSerializable(PARAM_STATE, _state);
        if (_requestString != null) {
            bundle.putByteArray(PARAM_REQUEST, _requestString.getBytes());
        }
        bundle.putSerializable(PARAM_PRIORITY, _priority);

        if (_key != null)
            bundle.putString(PARAM_KEY, _key);

        bundle.putBoolean(PARAM_USE_AUTH, _useAuth);
        bundle.putSerializable(PARAM_TYPE, _type);
        bundle.putLong(PARAM_QUEUE_TIME, _queueTime);
        bundle.putInt(PARAM_TRY_COUNT, _tryCount);
        bundle.putInt(PARAM_MAX_TRIES, _maxTries);
        bundle.putBoolean(PARAM_WIFI_REQUIRED, _wifiRequired);
        bundle.putBoolean(PARAM_TRACK, _track);
        bundle.putInt(PARAM_TRACK_ENUM, _trackType.ordinal());

        if (_timingKey != null)
            bundle.putString(PARAM_TIMING_KEY, _timingKey);

        bundle.putBoolean(PARAM_ZOMBIE, _wasZombie);

        if (_uuid != null)
            bundle.putParcelable(PARAM_UUID, _uuid);

        bundle.putInt(PARAM_NOTIFICATION_ID, _notifId);
        if (_notifStartArray != null)
            bundle.putByteArray(PARAM_NOTIFICATION_START, _notifStartArray);

        if (_notifSuccessArray != null)
            bundle.putByteArray(PARAM_NOTIFICATION_SUCCESS, _notifSuccessArray);

        if (_notifFailedArray != null)
            bundle.putByteArray(PARAM_NOTIFICATION_FAILED, _notifFailedArray);

        if (_notifRetryArray != null)
            bundle.putByteArray(PARAM_NOTIFICATION_RETRY, _notifRetryArray);

        return bundle;
    }

    /*-*************************************-*/
    /*-         Getters and setters         -*/
    /*-*************************************-*/
    public long getId() {
        return _id;
    }

    public long getCreatedTime() {
        return _createdTime;
    }

    public String getListenerName() {
        return _listenerClassName;
    }

    public void setListenerName(String listenerName) {
        _listenerClassName = listenerName;
    }

    public boolean useAuth() {
        return _useAuth;
    }

    public Type getType() {
        return _type;
    }

    public State getState() {
        return _state;
    }

    public void setState(State state) {
        _state = state;
    }

    public String getRequestString() {
        return _requestString;
    }

    public void setRequest(String request) {
        _requestString = request;
    }

    public Priority getPriority() {
        return _priority;
    }

    public void setPriority(Priority priority) {
        _priority = priority;
    }

    public String getKey() {
        return _key;
    }

    public void setKey(String key) {
        _key = key;
    }

    public byte[] getListenerParams() {
        return _listenerParams;
    }

    public void setListenerParams(byte[] params) {
        _listenerParams = params;
    }

    public long getQueueTime() {
        return _queueTime;
    }

    public void setQueueTime(long queueTime) {
        _queueTime = queueTime;
    }

    public int getTryCount() {
        return _tryCount;
    }

    public void setTryCount(int tryCount) {
        Log.v(TAG, "setTryCount " + tryCount);
        _tryCount = tryCount;
    }

    public int getMaxTries() {
        return _maxTries;
    }

    public void setMaxTries(int maxTries) {
        _maxTries = maxTries;
    }

    public void setWifiRequired(boolean required) {
        _wifiRequired = required;
    }

    public boolean isWifiRequired() {
        return _wifiRequired;
    }

    public void setTracking(boolean track) {
        _track = track;
    }

    public boolean isTracked() {
        return _track;
    }

    public TrackerEnum getTrackType() {
        return _trackType;
    }

    public void setTrackType(TrackerEnum trackType) {
        _trackType = trackType;
    }

    public String getTimingKey() {
        return _timingKey;
    }

    public boolean isZombie() {
        return getState() == State.ZOMBIE;
    }

    public boolean wasZombie() {
        return _wasZombie;
    }

    public UUIDGroup getUUID() {
        return _uuid;
    }

    public int getNotificationId() {
        return _notifId;
    }

    public NotificationDefinition getNotificationStart() {
        if (_notifStart == null && _notifStartArray != null) {
            try {
                _notifStart = NotificationDefinition.fromJson(new JsonObject(_notifStartArray));
            } catch (Exception ex) {
            }
        }
        return _notifStart;
    }

    public NotificationDefinition getNotificationSuccess() {
        if (_notifSuccess == null && _notifSuccessArray != null) {
            try {
                _notifSuccess = NotificationDefinition.fromJson(new JsonObject(_notifSuccessArray));
            } catch (Exception ex) {
            }
        }
        return _notifSuccess;
    }

    public NotificationDefinition getNotificationFailed() {
        if (_notifFailed == null && _notifFailedArray != null) {
            try {
                _notifFailed = NotificationDefinition.fromJson(new JsonObject(_notifFailedArray));
            } catch (Exception ex) {
            }
        }
        return _notifFailed;
    }

    public NotificationDefinition getNotificationRetry() {
        if (_notifRetry == null && _notifRetryArray != null) {
            try {
                _notifRetry = NotificationDefinition.fromJson(new JsonObject(_notifRetryArray));
            } catch (Exception ex) {
            }
        }
        return _notifRetry;
    }

    public void requeue(long retryTime) {
        Log.v(TAG, "requeue " + retryTime);
        setState(State.IDLE);
        setQueueTime(System.currentTimeMillis() + retryTime);
        setTryCount(getTryCount() + 1);
        save();

        if (isTracked())
            UploadTrackerClient.uploadRetry(this, getTrackType());
    }

    public void setZombie() {
        setState(State.ZOMBIE);
        _wasZombie = true;
    }

    public WebTransaction save() {
        return put(this);
    }

    /*-*****************************************-*/
    /*-             Database interface          -*/
    /*-*****************************************-*/
    public static boolean keyExists(String key) {
        if (key == null)
            return false;

        Log.v(TAG, "keyExists(" + key + ")");
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.query(
                            WebTransactionSqlHelper.TABLE_NAME,
                            WebTransactionSqlHelper.getColumnNames(),
                            Column.KEY + " = ? AND " + Column.STATE + " = ?",
                            new String[]{key, State.IDLE.ordinal() + ""},
                            null, null, null, "1");
                    //logCursor(cursor);
                    return cursor.getCount() > 0;
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }
    }

    private static void logCursor(Cursor cursor) {
        Log.v(TAG, "****Dumping cursor to log****");
        if (cursor.moveToFirst()) {
            String[] columnNames = cursor.getColumnNames();
            String names = "";
            for (String name : columnNames) {
                names += name + " ";
            }
            Log.v(TAG, names);
            do {
                String row = "";
                for (String name : columnNames) {
                    try {
                        row += cursor.getString(cursor.getColumnIndex(name));
                    } catch (Exception ex) {
                        row += new String(cursor.getBlob(cursor.getColumnIndex(name))) + ", ";
                    }
                }
                Log.v(TAG, row);

            } while (cursor.moveToNext());
        }

    }

    public static WebTransaction get(long id) {
//        Log.v(TAG, "get(" + id + ")");
        WebTransaction obj = null;
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.query(
                            WebTransactionSqlHelper.TABLE_NAME,
                            WebTransactionSqlHelper.getColumnNames(),
                            Column.ID + "=?",
                            new String[]{id + ""},
                            null, null, null, "1");

                    if (cursor.moveToFirst()) {
                        obj = new WebTransaction(cursor);
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }
        return obj;
    }

    public static WebTransaction get(String key) {
//        Log.v(TAG, "get(" + id + ")");
        WebTransaction obj = null;
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.query(
                            WebTransactionSqlHelper.TABLE_NAME,
                            WebTransactionSqlHelper.getColumnNames(),
                            Column.KEY + "=?",
                            new String[]{key + ""},
                            null, null, null, "1");

                    if (cursor.moveToFirst()) {
                        obj = new WebTransaction(cursor);
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }
        return obj;
    }

    public static List<WebTransaction> getZombies() {
        List<WebTransaction> zombies = new LinkedList<>();
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.query(
                            WebTransactionSqlHelper.TABLE_NAME,
                            WebTransactionSqlHelper.getColumnNames(),
                            Column.WAS_ZOMBIE + "=1"
                                    + " AND " + Column.STATE + " !=?",
                            new String[]{State.IDLE.ordinal() + ""}, null, null, null, null);

                    while (cursor.moveToNext()) {
                        WebTransaction trans = new WebTransaction(cursor);
                        zombies.add(trans);
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }
        return zombies;
    }

    public static int getWorkOrderCount(List<WebTransaction> list) {
        Set<Integer> workorders = new HashSet<>();

        for (WebTransaction wt : list) {
            try {
                TransactionParams tl = TransactionParams.fromJson(new JsonObject(wt.getListenerParams()));
                int workOrderId = tl.getMethodParamInt("workOrderId");
                workorders.add(workOrderId);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        return workorders.size();
    }

    public static List<WebTransaction> getSyncing() {
        List<WebTransaction> syncing = new LinkedList<>();
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.query(
                            WebTransactionSqlHelper.TABLE_NAME,
                            WebTransactionSqlHelper.getColumnNames(),
                            Column.TYPE + "=" + Type.SYNC.ordinal(),
                            null, null, null, null, null);

                    while (cursor.moveToNext()) {
                        WebTransaction trans = new WebTransaction(cursor);
                        syncing.add(trans);
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }
        return syncing;
    }

    public static List<WebTransaction> getPaused(Type type) {
        List<WebTransaction> paused = new LinkedList<>();
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.query(
                            WebTransactionSqlHelper.TABLE_NAME,
                            WebTransactionSqlHelper.getColumnNames(),
                            Column.STATE + " =?"
                                    + ((type == Type.ANY) ? "" : " AND " + Column.TYPE + "=" + type.ordinal()),
                            new String[]{State.IDLE.ordinal() + ""}, null, null, null, null);

                    while (cursor.moveToNext()) {
                        WebTransaction trans = new WebTransaction(cursor);
                        paused.add(trans);
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }
        return paused;
    }

    public static List<WebTransaction> getPaused() {
        List<WebTransaction> paused = new LinkedList<>();
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.query(
                            WebTransactionSqlHelper.TABLE_NAME,
                            WebTransactionSqlHelper.getColumnNames(),
                            Column.STATE + " =? ",
                            new String[]{State.IDLE.ordinal() + ""}, null, null, null, null);

                    while (cursor.moveToNext()) {
                        WebTransaction trans = new WebTransaction(cursor);
                        paused.add(trans);
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }
        return paused;
    }

    public static void list() {
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.query(
                            WebTransactionSqlHelper.TABLE_NAME,
                            WebTransactionSqlHelper.getColumnNames(),
                            null, null, null, null, null, "1");

                    while (cursor.moveToNext()) {
                        WebTransaction trans = new WebTransaction(cursor);
                        Log.v(TAG, trans._id + " " + trans._key + " " + trans._state);
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }
    }

    public static List<WebTransaction> findByKey(String query) {
        List<WebTransaction> list = new LinkedList<>();
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.query(
                            WebTransactionSqlHelper.TABLE_NAME,
                            WebTransactionSqlHelper.getColumnNames(),
                            Column.KEY + " LIKE ?",
                            new String[]{query}, null, null, null, null);

                    while (cursor.moveToNext()) {
                        WebTransaction trans = new WebTransaction(cursor);
                        list.add(trans);
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }
        return list;
    }

    private static final String[] GET_NEXT_PARAMS = new String[]{State.IDLE.ordinal() + ""};
    private static final String GET_NEXT_SORT = Column.PRIORITY + " DESC, " + Column.QUEUE_TIME + " ASC, " + Column.ID + " ASC";

    public static WebTransaction getNext(Type type, boolean allowAuth, Priority minPriority) {
//        Log.v(TAG, "getNext()");
        WebTransaction obj = null;
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.query(
                            WebTransactionSqlHelper.TABLE_NAME,
                            WebTransactionSqlHelper.getColumnNames(),
                            Column.STATE + "=?"
                                    + " AND " + Column.PRIORITY + " >= " + minPriority.ordinal()
                                    + " AND " + Column.QUEUE_TIME + " < " + System.currentTimeMillis()
                                    + " AND " + Column.STATE + " <> " + State.ZOMBIE.ordinal()
                                    + (type == Type.ANY ? "" : " AND " + Column.TYPE + " = " + type.ordinal())
                                    + (allowAuth ? "" : " AND " + Column.USE_AUTH + " = 0")
                                    + ((!App.get().haveWifi()) ? " AND " + Column.WIFI_REQUIRED + " = 0" : ""),
                            GET_NEXT_PARAMS, null, null, GET_NEXT_SORT, "1");

                    if (cursor.moveToFirst()) {
                        obj = new WebTransaction(cursor);
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
            if (obj != null) {
                obj.setState(State.WORKING);
                obj.save();
            }
        }
        return obj;
    }

    public static void saveOrphans() {
        ContentValues v = new ContentValues();
        v.put(Column.STATE.getName(), State.IDLE.ordinal());
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                int rowcount = db.update(
                        WebTransactionSqlHelper.TABLE_NAME, v,
                        Column.STATE + "=" + State.WORKING.ordinal()
                                + " OR " + Column.STATE + "=" + State.BUILDING.ordinal(), null);
                Log.v(TAG, "Orphans saved: " + rowcount);
            } finally {
                if (db != null) db.close();
            }
        }

        LocalBroadcastManager.getInstance(App.get()).sendBroadcast(new Intent(BROADCASE_ON_CHANGE));
    }

    public static WebTransaction put(WebTransaction obj) {
//        Log.v(TAG, "put(" + obj._key + ")");
        ContentValues v = new ContentValues();
        v.put(Column.CREATED_TIME.getName(), obj._createdTime);
        v.put(Column.LISTENER.getName(), obj._listenerClassName);
        v.put(Column.LISTENER_PARAMS.getName(), obj._listenerParams);
        v.put(Column.USE_AUTH.getName(), obj._useAuth ? 1 : 0);

        if (obj._state != null) {
            v.put(Column.STATE.getName(), obj._state.ordinal());
        } else {
            v.put(Column.STATE.getName(), State.BUILDING.ordinal());
        }

        if (obj._requestString != null) {
            v.put(Column.REQUEST.getName(), obj._requestString.getBytes());
        }
        v.put(Column.KEY.getName(), obj._key);
        v.put(Column.PRIORITY.getName(), obj._priority.ordinal());
        v.put(Column.TYPE.getName(), obj._type.ordinal());
        v.put(Column.QUEUE_TIME.getName(), obj._queueTime);
        v.put(Column.TRY_COUNT.getName(), obj._tryCount);
        v.put(Column.MAX_TRIES.getName(), obj._maxTries);
        v.put(Column.WIFI_REQUIRED.getName(), obj._wifiRequired ? 1 : 0);
        v.put(Column.TRACK.getName(), obj._track ? 1 : 0);
        v.put(Column.TRACK_TYPE.getName(), obj._trackType.ordinal());
        v.put(Column.TIMING_KEY.getName(), obj._timingKey);
        v.put(Column.WAS_ZOMBIE.getName(), obj._wasZombie ? 1 : 0);
        if (obj._uuid != null)
            v.put(Column.UUID.getName(), obj._uuid.toJson().toString());

        v.put(Column.NOTIF_ID.getName(), obj._notifId);
        v.put(Column.NOTIF_FAILED.getName(), obj._notifFailedArray);
        v.put(Column.NOTIF_RETRY.getName(), obj._notifRetryArray);
        v.put(Column.NOTIF_START.getName(), obj._notifStartArray);
        v.put(Column.NOTIF_SUCCESS.getName(), obj._notifSuccessArray);

        boolean success = false;
        long id = obj._id;
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                if (id == -1) {
                    id = db.insert(WebTransactionSqlHelper.TABLE_NAME, null, v);
                    success = true;
                } else {
                    success = db.update(WebTransactionSqlHelper.TABLE_NAME, v, Column.ID + "=" + obj._id, null) > 0;
                }
            } finally {
                if (db != null) db.close();
            }
        }
        try {
            if (success) {
                return get(id);
            } else {
                return null;
            }
        } finally {
            LocalBroadcastManager.getInstance(App.get()).sendBroadcast(new Intent(BROADCASE_ON_CHANGE));
        }
    }

    public static boolean delete(long id) {
//        Log.v(TAG, "delete(" + id + ")");
        Transform.deleteTransaction(id);
        boolean success = false;
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                success = db.delete(
                        WebTransactionSqlHelper.TABLE_NAME,
                        Column.ID + "=?",
                        new String[]{id + ""}) > 0;
            } finally {
                if (db != null) db.close();
            }
        }
        LocalBroadcastManager.getInstance(App.get()).sendBroadcast(new Intent(BROADCASE_ON_CHANGE));
        return success;
    }

    public static boolean deleteAll() {
//        Log.v(TAG, "delete(" + id + ")");
        Transform.deleteAll();
        boolean success = false;
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                success = db.delete(
                        WebTransactionSqlHelper.TABLE_NAME,
                        null, null) > 0;
            } finally {
                if (db != null) db.close();
            }
        }

        LocalBroadcastManager.getInstance(App.get()).sendBroadcast(new Intent(BROADCASE_ON_CHANGE));
        return success;
    }

    public static int count() {
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.rawQuery(
                            "SELECT COUNT(*) FROM " + WebTransactionSqlHelper.TABLE_NAME
                                    + " WHERE " + Column.STATE + " <> " + State.ZOMBIE.ordinal(), null);
                    if (cursor.moveToNext()) {
                        return cursor.getInt(0);
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }
        return 0;
    }

    public static int countWifiRequired() {
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.rawQuery(
                            "SELECT COUNT(*) FROM " + WebTransactionSqlHelper.TABLE_NAME
                                    + " WHERE " + Column.WIFI_REQUIRED + " = 1", null);
                    if (cursor.moveToNext()) {
                        return cursor.getInt(0);
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }
        return 0;
    }

    public static int countTracked() {
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.rawQuery(
                            "SELECT COUNT(*) FROM " + WebTransactionSqlHelper.TABLE_NAME
                                    + " WHERE " + Column.TRACK + " = 1", null);
                    if (cursor.moveToNext()) {
                        return cursor.getInt(0);
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }
        return 0;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Creator<WebTransaction> CREATOR = new Creator<WebTransaction>() {
        @Override
        public WebTransaction createFromParcel(Parcel source) {
            return new WebTransaction(source.readBundle(getClass().getClassLoader()));
        }

        @Override
        public WebTransaction[] newArray(int size) {
            return new WebTransaction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(toBundle());
    }

    /*-*********************************-*/
    /*-             Builder             -*/
    /*-*********************************-*/
    public static class Builder {
        private Bundle params = new Bundle();
        private List<Parcelable> transforms = new LinkedList<>();

        public Builder() {
            params.putLong(PARAM_CREATED_TIME, System.currentTimeMillis());
            params.putSerializable(PARAM_PRIORITY, Priority.NORMAL);
            params.putSerializable(PARAM_TYPE, Type.NORMAL);
            params.putBoolean(PARAM_WIFI_REQUIRED, false);
            params.putBoolean(PARAM_TRACK, false);
            params.putInt(PARAM_TRACK_ENUM, 0);
            params.putBoolean(PARAM_ZOMBIE, false);
            params.putInt(PARAM_NOTIFICATION_ID, -1);
            params.putParcelable(PARAM_UUID, null);
            params.putInt(PARAM_TRY_COUNT, 0);
            params.putInt(PARAM_MAX_TRIES, 0);
            params.putByteArray(PARAM_NOTIFICATION_START, (byte[]) null);
            params.putByteArray(PARAM_NOTIFICATION_SUCCESS, (byte[]) null);
            params.putByteArray(PARAM_NOTIFICATION_FAILED, (byte[]) null);
            params.putByteArray(PARAM_NOTIFICATION_RETRY, (byte[]) null);
        }

        public WebTransaction build() {
            if (transforms.size() > 0) {
                Parcelable[] parcels = transforms.toArray(new Parcelable[transforms.size()]);
                params.putParcelableArray(PARAM_TRANSFORM_LIST, parcels);
            }
            return new WebTransaction(params);
        }

        public Builder priority(Priority priority) {
            params.putSerializable(PARAM_PRIORITY, priority);
            return this;
        }

        public Builder uuid(UUIDGroup uuid) {
            params.putParcelable(PARAM_UUID, uuid);
            return this;
        }

        public Builder key(String key) {
            params.putString(PARAM_KEY, key);
            return this;
        }

        public Builder useAuth(boolean use) {
            params.putBoolean(PARAM_USE_AUTH, use);
            return this;
        }

        public Builder setType(Type type) {
            params.putSerializable(PARAM_TYPE, type);
            return this;
        }

        public Builder setWifiRequired(boolean required) {
            params.putBoolean(PARAM_WIFI_REQUIRED, required);
            return this;
        }

        public Builder setTrack(boolean track) {
            params.putBoolean(PARAM_TRACK, track);
            return this;
        }

        public Builder setTrackType(TrackerEnum trackerEnum) {
            params.putInt(PARAM_TRACK_ENUM, trackerEnum.ordinal());
            return this;
        }

        public Builder setTryCount(int tryCount) {
            params.putInt(PARAM_TRY_COUNT, tryCount);
            return this;
        }

        public Builder setMaxTries(int maxTries) {
            params.putInt(PARAM_MAX_TRIES, maxTries);
            return this;
        }

        public Builder listener(Class<? extends WebTransactionListener> clazz) {
            params.putString(PARAM_LISTENER_NAME, clazz.getName());
            return this;
        }

        public Builder listenerParams(byte[] params) {
            this.params.putByteArray(PARAM_LISTENER_PARAMS, params);
            return this;
        }

        public Builder timingKey(String timingKey) throws ParseException {
            params.putString(PARAM_TIMING_KEY, timingKey);
            return this;
        }

        public Builder zombie(boolean zombie) {
            params.putBoolean(PARAM_ZOMBIE, zombie);
            return this;
        }

        private void addNotificationId() {
            if (!params.containsKey(PARAM_NOTIFICATION_ID)) {
                params.putInt(PARAM_NOTIFICATION_ID, App.secureRandom.nextInt(Integer.MAX_VALUE));
            }
        }

        public Builder notifyOnStart(NotificationDefinition start) throws ParseException {
            addNotificationId();
            params.putByteArray(PARAM_NOTIFICATION_START, start.toJson().toByteArray());
            return this;
        }

        public Builder notifyOnSuccess(NotificationDefinition success) throws ParseException {
            addNotificationId();
            params.putByteArray(PARAM_NOTIFICATION_SUCCESS, success.toJson().toByteArray());
            return this;
        }

        public Builder notifyOnFail(NotificationDefinition failed) throws ParseException {
            addNotificationId();
            params.putByteArray(PARAM_NOTIFICATION_FAILED, failed.toJson().toByteArray());
            return this;
        }

        public Builder notifyOnRetry(NotificationDefinition retry) throws ParseException {
            addNotificationId();
            params.putByteArray(PARAM_NOTIFICATION_RETRY, retry.toJson().toByteArray());
            return this;
        }

        public Builder addTransform(Bundle transform) {
            transforms.add(transform);
            return this;
        }

        public Builder request(HttpJsonBuilder builder) {
            params.putByteArray(PARAM_REQUEST, builder.build().toByteArray());
            return this;
        }
    }

    public static void cleanZombies(AttachmentFolders attachmentFolders) {
        // TODO disabled for now. I don't have a good solution for this
/*
        try {
            List<WebTransaction> zombies = WebTransaction.getZombies();
            for (WebTransaction zombie : zombies) {
                TransactionParams params = TransactionParams.fromJson(new JsonObject(zombie.getListenerParams()));
                JsonObject methodParams = new JsonObject(params.methodParams);
                int folderId = methodParams.getInt("folderId");
                String fileHash = methodParams.getString("fileHash").toLowerCase();

                boolean kill = false;
                for (AttachmentFolder folder : attachmentFolders.getResults()) {
                    if (folderId == folder.getId()) {
                        for (Attachment attachment : folder.getResults()) {
                            if (attachment.getFile() != null
                                    && attachment.getFile().getHash() != null
                                    && fileHash.equals(attachment.getFile().getHash().toLowerCase())) {
                                kill = true;
                                break;
                            }
                        }
                    }
                    if (kill) break;
                }

                if (kill)
                    WebTransaction.delete(zombie.getId());
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
*/
    }
}
