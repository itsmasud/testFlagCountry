package com.fieldnation.service.transaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.transaction.WebTransactionSqlHelper.Column;

/**
 * Created by Michael Carver on 3/3/2015.
 */
public class WebTransaction implements Parcelable, WebTransactionConstants {
    private static final String TAG = "WebTransaction";

    private long _id;
    private String _handlerName;
    private byte[] _handlerParams;
    private boolean _useAuth;
    private boolean _isSync;
    private State _state;
    private Priority _priority;
    private JsonObject _request;
    private String _key;
    private long _queueTime;

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
            _request = new JsonObject(cursor.getBlob(Column.REQUEST.getIndex()));
        } catch (Exception ex) {
        }
        _priority = Priority.values()[cursor.getInt(Column.PRIORITY.getIndex())];
        _key = cursor.getString(Column.KEY.getIndex());
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
            _request = new JsonObject(bundle.getByteArray(PARAM_REQUEST));
        } catch (Exception ex) {
        }
        _priority = (Priority) bundle.getSerializable(PARAM_PRIORITY);
        _key = bundle.getString(PARAM_KEY);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_ID, _id);
        bundle.putString(PARAM_HANDLER_NAME, _handlerName);
        bundle.putByteArray(PARAM_HANDLER_PARAMS, _handlerParams);
        bundle.putSerializable(PARAM_STATE, _state);
        if (_request != null)
            bundle.putByteArray(PARAM_REQUEST, _request.toByteArray());
        bundle.putSerializable(PARAM_PRIORITY, _priority);
        bundle.putString(PARAM_KEY, _key);
        bundle.putBoolean(PARAM_USE_AUTH, _useAuth);
        bundle.putBoolean(PARAM_IS_SYNC, _isSync);
        bundle.putLong(PARAM_QUEUE_TIME, _queueTime);
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

    public JsonObject getRequest() {
        return _request;
    }

    public void setRequest(JsonObject request) {
        _request = request;
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

    public void requeue(Context context) {
        setState(State.IDLE);
        setQueueTime(System.currentTimeMillis());
        save(context);
    }

    public WebTransaction save(Context context) {
        return put(context, this);
    }

    /*-*****************************************-*/
    /*-             Database interface          -*/
    /*-*****************************************-*/
    public static boolean keyExists(Context context, String key) {
        Log.v(TAG, "keyExists(" + key + ")");
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
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
                    cursor.close();
                }
            } finally {
                //db.close();
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

    public static WebTransaction get(Context context, long id) {
//        Log.v(TAG, "get(" + id + ")");
        WebTransaction obj = null;
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
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
            } finally {
                //db.close();
            }
        }
        return obj;
    }


    private static final String[] GET_NEXT_PARAMS = new String[]{State.IDLE.ordinal() + ""};
    private static final String GET_NEXT_SORT = Column.QUEUE_TIME + " ASC, " + Column.PRIORITY + " DESC, " + Column.ID + " ASC";

    public static WebTransaction getNext(Context context, boolean allowSync, boolean allowAuth) {
//        Log.v(TAG, "getNext()");
        WebTransaction obj = null;
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = db.query(
                        WebTransactionSqlHelper.TABLE_NAME,
                        WebTransactionSqlHelper.getColumnNames(),
                        Column.STATE + "=?"
                                + (allowSync ? "" : " AND is_sync = 0")
                                + (allowAuth ? "" : " AND use_auth = 0"),
                        GET_NEXT_PARAMS,
                        null, null, GET_NEXT_SORT, "1");
                try {
                    if (cursor.moveToFirst()) {
                        obj = new WebTransaction(cursor);
                    }
                } finally {
                    cursor.close();
                }
            } finally {
                //db.close();
            }
            if (obj != null) {
                obj.setState(State.WORKING);
                obj.save(context);
            }
        }
        return obj;
    }

    public static void saveOrphans(Context context) {
        ContentValues v = new ContentValues();
        v.put(Column.STATE.getName(), State.IDLE.ordinal());
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                int rowcount = db.update(
                        WebTransactionSqlHelper.TABLE_NAME,
                        v, Column.STATE + "=" + State.WORKING.ordinal() + " OR " + Column.STATE + "=" + State.BUILDING.ordinal(), null);
                Log.v(TAG, "Orphans saved: " + rowcount);
            } finally {
                //db.close();
            }
        }
    }

    public static WebTransaction put(Context context, WebTransaction obj) {
//        Log.v(TAG, "put(" + obj._key + ")");
        ContentValues v = new ContentValues();
        v.put(Column.HANDLER.getName(), obj._handlerName);
        v.put(Column.HANDLER_PARAMS.getName(), obj._handlerParams);
        v.put(Column.STATE.getName(), obj._state.ordinal());
        if (obj._request != null) {
            v.put(Column.REQUEST.getName(), obj._request.toByteArray());
        }
        v.put(Column.KEY.getName(), obj._key);
        v.put(Column.PRIORITY.getName(), obj._priority.ordinal());
        v.put(Column.USE_AUTH.getName(), obj._useAuth ? 1 : 0);
        v.put(Column.IS_SYNC.getName(), obj._isSync ? 1 : 0);
        v.put(Column.QUEUE_TIME.getName(), obj._queueTime);

        boolean success = false;
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                success = db.update(
                        WebTransactionSqlHelper.TABLE_NAME,
                        v, Column.ID + "=" + obj._id, null) > 0;

            } finally {
                //db.close();
            }
        }
        if (success) {
            return get(context, obj._id);
        } else {
            return null;
        }
    }

    public static WebTransaction put(Context context, Priority priority, String key, boolean useAuth,
                                     boolean isSync, byte[] request, String handlerName, byte[] handlerParams) {
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

        long id = -1;
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                id = db.insert(WebTransactionSqlHelper.TABLE_NAME,
                        null, v);
            } finally {
                //db.close();
            }
        }
        if (id != -1) {
            return get(context, id);
        } else {
            return null;
        }
    }

    public static boolean delete(Context context, long id) {
//        Log.v(TAG, "delete(" + id + ")");
        Transform.deleteTransaction(context, id);
        boolean success = false;
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                success = db.delete(
                        WebTransactionSqlHelper.TABLE_NAME,
                        Column.ID + "=?",
                        new String[]{id + ""}) > 0;
            } finally {
                //db.close();
            }
        }
        return success;
    }

    public static int count(Context context) {
        synchronized (TAG) {
            WebTransactionSqlHelper helper = WebTransactionSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + WebTransactionSqlHelper.TABLE_NAME, null);

                try {
                    if (cursor.moveToNext()) {
                        return cursor.getInt(0);
                    }
                } finally {
                    cursor.close();
                }
            } finally {
                //db.close();
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
            return new WebTransaction(source.readBundle());
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
