package com.fieldnation.service.transaction;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.App;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.tracker.UploadTrackerClient;
import com.fieldnation.service.transaction.WebTransactionSqlHelper.Column;

/**
 * Created by Michael Carver on 3/3/2015.
 */
public class WebTransaction implements Parcelable, WebTransactionConstants {
    private static final String TAG = "WebTransaction";

    private final long _id;
    private String _handlerName;
    private byte[] _handlerParams;
    private final boolean _useAuth;
    private final boolean _isSync;
    private State _state;
    private Priority _priority;
    private String _requestString;
    private String _key;
    private long _queueTime;
    private boolean _wifiRequired;
    private boolean _track;

    private int _notifId = -1;

    private byte[] _notifStartArray;
    private NotificationDefinition _notifStart;
    private byte[] _notifSuccessArray;
    private NotificationDefinition _notifSuccess;
    private byte[] _notifFailedArray;
    private NotificationDefinition _notifFailed;
    private byte[] _notifRetryArray;
    private NotificationDefinition _notifRetry;

    public enum State {
        BUILDING, IDLE, WORKING
    }

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    WebTransaction(Cursor cursor) {
        _id = cursor.getLong(Column.ID.getIndex());
        _handlerName = cursor.getString(Column.HANDLER.getIndex());
        _handlerParams = cursor.getBlob(Column.HANDLER_PARAMS.getIndex());
        _useAuth = cursor.getInt(Column.USE_AUTH.getIndex()) == 1;
        _state = State.values()[cursor.getInt(Column.STATE.getIndex())];
        _isSync = cursor.getInt(Column.IS_SYNC.getIndex()) == 1;
        _queueTime = cursor.getLong(Column.QUEUE_TIME.getIndex());
        try {
            _requestString = new String(cursor.getBlob(Column.REQUEST.getIndex()));
        } catch (Exception ex) {
        }
        _priority = Priority.values()[cursor.getInt(Column.PRIORITY.getIndex())];
        _key = cursor.getString(Column.KEY.getIndex());
        _wifiRequired = cursor.getInt(Column.WIFI_REQUIRED.getIndex()) == 1;
        _track = cursor.getInt(Column.TRACK.getIndex()) == 1;

        _notifId = cursor.getInt(Column.NOTIF_ID.getIndex());

        _notifStartArray = cursor.getBlob(Column.NOTIF_START.getIndex());
        _notifSuccessArray = cursor.getBlob(Column.NOTIF_SUCCESS.getIndex());
        _notifFailedArray = cursor.getBlob(Column.NOTIF_FAILED.getIndex());
        _notifRetryArray = cursor.getBlob(Column.NOTIF_RETRY.getIndex());
    }

    public WebTransaction(Bundle bundle) {
        _id = bundle.getLong(PARAM_ID);
        _handlerName = bundle.getString(PARAM_HANDLER_NAME);
        _handlerParams = bundle.getByteArray(PARAM_HANDLER_PARAMS);
        _useAuth = bundle.getBoolean(PARAM_USE_AUTH);
        _state = (State) bundle.getSerializable(PARAM_STATE);
        _isSync = bundle.getBoolean(PARAM_IS_SYNC);
        _queueTime = bundle.getLong(PARAM_QUEUE_TIME);
        try {
            _requestString = new String(bundle.getByteArray(PARAM_REQUEST));
        } catch (Exception ex) {
        }
        _priority = (Priority) bundle.getSerializable(PARAM_PRIORITY);
        _key = bundle.getString(PARAM_KEY);
        _wifiRequired = bundle.getBoolean(PARAM_WIFI_REQUIRED);
        _track = bundle.getBoolean(PARAM_TRACK);

        _notifId = bundle.getInt(PARAM_NOTIFICATION_ID);
        _notifStartArray = bundle.getByteArray(PARAM_NOTIFICATION_START);
        _notifSuccessArray = bundle.getByteArray(PARAM_NOTIFICATION_SUCCESS);
        _notifFailedArray = bundle.getByteArray(PARAM_NOTIFICATION_FAILED);
        _notifRetryArray = bundle.getByteArray(PARAM_NOTIFICATION_RETRY);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_ID, _id);
        bundle.putString(PARAM_HANDLER_NAME, _handlerName);
        bundle.putByteArray(PARAM_HANDLER_PARAMS, _handlerParams);
        bundle.putSerializable(PARAM_STATE, _state);
        if (_requestString != null) {
            bundle.putByteArray(PARAM_REQUEST, _requestString.getBytes());
        }
        bundle.putSerializable(PARAM_PRIORITY, _priority);
        bundle.putString(PARAM_KEY, _key);
        bundle.putBoolean(PARAM_USE_AUTH, _useAuth);
        bundle.putBoolean(PARAM_IS_SYNC, _isSync);
        bundle.putLong(PARAM_QUEUE_TIME, _queueTime);
        bundle.putBoolean(PARAM_WIFI_REQUIRED, _wifiRequired);
        bundle.putBoolean(PARAM_TRACK, _track);

        bundle.putInt(PARAM_NOTIFICATION_ID, _notifId);
        bundle.putByteArray(PARAM_NOTIFICATION_START, _notifStartArray);
        bundle.putByteArray(PARAM_NOTIFICATION_SUCCESS, _notifSuccessArray);
        bundle.putByteArray(PARAM_NOTIFICATION_FAILED, _notifFailedArray);
        bundle.putByteArray(PARAM_NOTIFICATION_RETRY, _notifRetryArray);

        return bundle;
    }

    /*-*************************************-*/
    /*-         Getters and setters         -*/
    /*-*************************************-*/
    public long getId() {
        return _id;
    }

    public String getHandlerName() {
        return _handlerName;
    }

    public void setHandlerName(String handlerName) {
        _handlerName = handlerName;
    }

    public boolean useAuth() {
        return _useAuth;
    }

    public boolean isSync() {
        return _isSync;
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

    public byte[] getHandlerParams() {
        return _handlerParams;
    }

    public void setHandlerParams(byte[] params) {
        _handlerParams = params;
    }

    public long getQueueTime() {
        return _queueTime;
    }

    public void setQueueTime(long queueTime) {
        _queueTime = queueTime;
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

    public void requeue() {
        setState(State.IDLE);
        setQueueTime(System.currentTimeMillis());
        save();

        if (isTracked())
            UploadTrackerClient.uploadRequeued(App.get());

    }

    public WebTransaction save() {
        return put(this);
    }

    /*-*****************************************-*/
    /*-             Database interface          -*/
    /*-*****************************************-*/
    public static boolean keyExists(String key) {
        Log.v(TAG, "keyExists(" + key + ")");
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(App.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query(
                    WebTransactionSqlHelper.TABLE_NAME,
                    WebTransactionSqlHelper.getColumnNames(),
                    Column.KEY + " = ?",
                    new String[]{key},
                    null, null, null, "1");
            try {
                //logCursor(cursor);
                return cursor.getCount() > 0;
            } finally {
                if (cursor != null) cursor.close();
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
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(App.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query(
                    WebTransactionSqlHelper.TABLE_NAME,
                    WebTransactionSqlHelper.getColumnNames(),
                    Column.ID + "=?",
                    new String[]{id + ""},
                    null, null, null, "1");

            try {
                if (cursor.moveToFirst()) {
                    obj = new WebTransaction(cursor);
                }
            } finally {
                cursor.close();
            }
        }
        return obj;
    }


    private static final String[] GET_NEXT_PARAMS = new String[]{State.IDLE.ordinal() + ""};
    private static final String GET_NEXT_SORT = Column.QUEUE_TIME + " ASC, " + Column.PRIORITY + " DESC, " + Column.ID + " ASC";

    public static WebTransaction getNext(boolean allowSync, boolean allowAuth, Priority minPriority) {
//        Log.v(TAG, "getNext()");
        WebTransaction obj = null;
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(App.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query(
                    WebTransactionSqlHelper.TABLE_NAME,
                    WebTransactionSqlHelper.getColumnNames(),
                    Column.STATE + "=?"
                            + " AND priority >= " + minPriority.ordinal()
                            + (allowSync ? "" : " AND is_sync = 0")
                            + (allowAuth ? "" : " AND use_auth = 0")
                            + ((!App.get().haveWifi()) ? " AND wifi_req = 0" : ""),
                    GET_NEXT_PARAMS,
                    null, null, GET_NEXT_SORT, "1");
            try {
                if (cursor.moveToFirst()) {
                    obj = new WebTransaction(cursor);
                }
            } finally {
                cursor.close();
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
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(App.get());
            SQLiteDatabase db = helper.getWritableDatabase();
            int rowcount = db.update(
                    WebTransactionSqlHelper.TABLE_NAME,
                    v, Column.STATE + "=" + State.WORKING.ordinal() + " OR " + Column.STATE + "=" + State.BUILDING.ordinal(), null);
            Log.v(TAG, "Orphans saved: " + rowcount);
        }
    }

    public static WebTransaction put(WebTransaction obj) {
//        Log.v(TAG, "put(" + obj._key + ")");
        ContentValues v = new ContentValues();
        v.put(Column.HANDLER.getName(), obj._handlerName);
        v.put(Column.HANDLER_PARAMS.getName(), obj._handlerParams);
        v.put(Column.STATE.getName(), obj._state.ordinal());
        if (obj._requestString != null) {
            v.put(Column.REQUEST.getName(), obj._requestString.getBytes());
        }
        v.put(Column.KEY.getName(), obj._key);
        v.put(Column.PRIORITY.getName(), obj._priority.ordinal());
        v.put(Column.USE_AUTH.getName(), obj._useAuth ? 1 : 0);
        v.put(Column.IS_SYNC.getName(), obj._isSync ? 1 : 0);
        v.put(Column.QUEUE_TIME.getName(), obj._queueTime);
        v.put(Column.WIFI_REQUIRED.getName(), obj._wifiRequired ? 1 : 0);
        v.put(Column.TRACK.getName(), obj._track ? 1 : 0);

        v.put(Column.NOTIF_ID.getName(), obj._notifId);
        v.put(Column.NOTIF_FAILED.getName(), obj._notifFailedArray);
        v.put(Column.NOTIF_RETRY.getName(), obj._notifRetryArray);
        v.put(Column.NOTIF_START.getName(), obj._notifStartArray);
        v.put(Column.NOTIF_SUCCESS.getName(), obj._notifSuccessArray);

        boolean success = false;
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(App.get());
            SQLiteDatabase db = helper.getWritableDatabase();
            success = db.update(
                    WebTransactionSqlHelper.TABLE_NAME,
                    v, Column.ID + "=" + obj._id, null) > 0;
        }
        if (success) {
            return get(obj._id);
        } else {
            return null;
        }
    }

    public static WebTransaction put(Priority priority, String key, boolean useAuth,
                                     boolean isSync, byte[] request, boolean wifiRequired,
                                     boolean track, String handlerName, byte[] handlerParams,
                                     int notifId, byte[] notifStart, byte[] notifSuccess,
                                     byte[] notifRetry, byte[] notifFailed) {
//        Log.v(TAG, "put(" + key + ")");
        ContentValues v = new ContentValues();
        v.put(Column.HANDLER.getName(), handlerName);
        v.put(Column.HANDLER_PARAMS.getName(), handlerParams);
        v.put(Column.USE_AUTH.getName(), useAuth ? 1 : 0);
        v.put(Column.IS_SYNC.getName(), isSync ? 1 : 0);
        v.put(Column.STATE.getName(), State.BUILDING.ordinal());
        v.put(Column.REQUEST.getName(), request);
        v.put(Column.PRIORITY.getName(), priority.ordinal());
        v.put(Column.KEY.getName(), key);
        v.put(Column.QUEUE_TIME.getName(), 0);
        v.put(Column.WIFI_REQUIRED.getName(), wifiRequired ? 1 : 0);
        v.put(Column.TRACK.getName(), track ? 1 : 0);

        v.put(Column.NOTIF_ID.getName(), notifId);
        v.put(Column.NOTIF_START.getName(), notifStart);
        v.put(Column.NOTIF_SUCCESS.getName(), notifSuccess);
        v.put(Column.NOTIF_RETRY.getName(), notifRetry);
        v.put(Column.NOTIF_FAILED.getName(), notifFailed);

        long id = -1;
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(App.get());
            SQLiteDatabase db = helper.getWritableDatabase();
            id = db.insert(WebTransactionSqlHelper.TABLE_NAME,
                    null, v);
        }
        if (id != -1) {
            return get(id);
        } else {
            return null;
        }
    }

    public static boolean delete(long id) {
//        Log.v(TAG, "delete(" + id + ")");
        Transform.deleteTransaction(id);
        boolean success = false;
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(App.get());
            SQLiteDatabase db = helper.getWritableDatabase();
            success = db.delete(
                    WebTransactionSqlHelper.TABLE_NAME,
                    Column.ID + "=?",
                    new String[]{id + ""}) > 0;
        }
        return success;
    }

    public static int count() {
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(App.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + WebTransactionSqlHelper.TABLE_NAME, null);

            try {
                if (cursor.moveToNext()) {
                    return cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }
        return 0;
    }

    public static int countWifiRequired() {
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(App.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + WebTransactionSqlHelper.TABLE_NAME + " WHERE wifi_req = 1", null);

            try {
                if (cursor.moveToNext()) {
                    return cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }
        return 0;
    }

    public static int countTracked() {
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(App.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + WebTransactionSqlHelper.TABLE_NAME + " WHERE track = 1", null);

            try {
                if (cursor.moveToNext()) {
                    return cursor.getInt(0);
                }
            } finally {
                cursor.close();
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
}
