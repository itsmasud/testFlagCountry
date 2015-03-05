package com.fieldnation.service.transaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.service.transaction.WebTransactionSqlHelper.Column;

/**
 * Created by Michael Carver on 3/3/2015.
 */
public class WebTransaction implements Parcelable, WebTransactionConstants {
    private static final String TAG = "service.transaction.Transaction";
    private static final Object LOCK = new Object();

    private long _id;
    private String _handlerName;
    private State _state;
    private JsonObject _meta;
    private Object _handler;
    private Priority _priority;
    private Long _storedObjectId = null;
    private String _key;

    public enum Priority {
        HIGH, NORMAL, LOW
    }

    public enum State {
        BUILDING, IDLE
    }

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    WebTransaction(Cursor cursor) {
        _id = cursor.getLong(Column.ID.getIndex());
        _handlerName = cursor.getString(Column.HANDLER.getIndex());
        _state = State.values()[cursor.getInt(Column.STATE.getIndex())];
        try {
            _meta = new JsonObject(cursor.getBlob(Column.META.getIndex()));
        } catch (Exception ex) {
        }
        _priority = Priority.values()[cursor.getInt(Column.PRIORITY.getIndex())];
        if (!cursor.isNull(Column.STORED_OBJECT_ID.getIndex())) {
            _storedObjectId = cursor.getLong(Column.STORED_OBJECT_ID.getIndex());
        }
        _key = cursor.getString(Column.KEY.getIndex());
    }

    public WebTransaction(Bundle bundle) {
        _id = bundle.getLong(PARAM_ID);
        _handlerName = bundle.getString(PARAM_HANDLER_NAME);
        _state = State.values()[bundle.getInt(PARAM_STATE)];
        try {
            _meta = new JsonObject(bundle.getByteArray(PARAM_META));
        } catch (Exception ex) {
        }
        _priority = Priority.values()[bundle.getInt(PARAM_PRIORITY)];
        if (bundle.containsKey(PARAM_STORED_OBJECT_ID))
            _storedObjectId = bundle.getLong(PARAM_STORED_OBJECT_ID);
        _key = bundle.getString(PARAM_KEY);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_ID, _id);
        bundle.putString(PARAM_HANDLER_NAME, _handlerName);
        bundle.putInt(PARAM_STATE, _state.ordinal());
        bundle.putByteArray(PARAM_META, _meta.toByteArray());
        bundle.putInt(PARAM_PRIORITY, _priority.ordinal());
        if (_storedObjectId != null)
            bundle.putLong(PARAM_STORED_OBJECT_ID, _storedObjectId);
        bundle.putString(PARAM_KEY, _key);
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

    public State getState() {
        return _state;
    }

    public void setState(State state) {
        _state = state;
    }

    public JsonObject getMeta() {
        return _meta;
    }

    public void setMeta(JsonObject meta) {
        _meta = meta;
    }

    public Priority getPriority() {
        return _priority;
    }

    public void setPriority(Priority priority) {
        _priority = priority;
    }

    public long getStoedObjectId() {
        if (_storedObjectId == null)
            return -1;
        return _storedObjectId;
    }

    public String getKey() {
        return _key;
    }

    public void setKey(String key) {
        _key = key;
    }

    public WebTransaction save(Context context) {
        return put(context, this);
    }

    /*-*****************************************-*/
    /*-             Database interface          -*/
    /*-*****************************************-*/
    public static WebTransaction get(Context context, long id) {
        WebTransaction obj = null;
        synchronized (LOCK) {
            WebTransactionSqlHelper helper = new WebTransactionSqlHelper(context);
            try {
                SQLiteDatabase db = helper.getReadableDatabase();
                try {
                    Cursor cursor = db.query(
                            WebTransactionSqlHelper.TABLE_NAME,
                            WebTransactionSqlHelper.getColumnNames(),
                            Column.ID + "=?",
                            new String[]{id + ""},
                            null, null, null, "LIMIT 1");

                    try {
                        if (cursor.moveToFirst()) {
                            obj = new WebTransaction(cursor);
                        }
                    } finally {
                        cursor.close();
                    }
                } finally {
                    db.close();
                }
            } finally {
                helper.close();
            }
        }
        return obj;
    }

    public static WebTransaction getNext(Context context) {
        WebTransaction obj = null;
        synchronized (LOCK) {
            WebTransactionSqlHelper helper = new WebTransactionSqlHelper(context);
            try {
                SQLiteDatabase db = helper.getReadableDatabase();
                try {
                    Cursor cursor = db.query(
                            WebTransactionSqlHelper.TABLE_NAME,
                            WebTransactionSqlHelper.getColumnNames(),
                            Column.STATE + "=?",
                            new String[]{State.IDLE.ordinal() + ""},
                            null, null,
                            "PRIORITY DESC, ID ASC ",
                            "LIMIT 1");
                    try {
                        if (cursor.moveToFirst()) {
                            obj = new WebTransaction(cursor);
                        }
                    } finally {
                        cursor.close();
                    }
                } finally {
                    db.close();
                }
            } finally {
                helper.close();
            }
        }
        return obj;
    }

    public static WebTransaction put(Context context, WebTransaction obj) {
        ContentValues v = new ContentValues();
        v.put(Column.HANDLER.getName(), obj._handlerName);
        v.put(Column.STATE.getName(), obj._state.ordinal());
        v.put(Column.META.getName(), obj._meta.toByteArray());
        v.put(Column.KEY.getName(), obj._key);
        v.put(Column.PRIORITY.getName(), obj._priority.ordinal());
        if (obj._storedObjectId != null)
            v.put(Column.STORED_OBJECT_ID.getName(), obj._storedObjectId);

        boolean success = false;
        synchronized (LOCK) {
            WebTransactionSqlHelper helper = new WebTransactionSqlHelper(context);
            try {
                SQLiteDatabase db = helper.getWritableDatabase();
                try {
                    success = db.update(
                            WebTransactionSqlHelper.TABLE_NAME,
                            v, Column.ID + "=" + obj._id, null) > 0;

                } finally {
                    db.close();
                }
            } finally {
                helper.close();
            }
        }
        if (success) {
            return get(context, obj._id);
        } else {
            return null;
        }
    }

    public static WebTransaction put(Context context, Priority priority, String key, JsonObject meta, long storedObjectId, String handlerName) {
        ContentValues v = new ContentValues();
        v.put(Column.HANDLER.getName(), handlerName);
        v.put(Column.KEY.getName(), key);
        v.put(Column.STATE.getName(), State.BUILDING.ordinal());
        v.put(Column.META.getName(), meta.toByteArray());
        v.put(Column.PRIORITY.getName(), priority.ordinal());
        v.put(Column.STORED_OBJECT_ID.getName(), storedObjectId);

        long id = -1;
        synchronized (LOCK) {
            WebTransactionSqlHelper helper = new WebTransactionSqlHelper(context);
            try {
                SQLiteDatabase db = helper.getWritableDatabase();
                try {
                    id = db.insert(WebTransactionSqlHelper.TABLE_NAME,
                            null, v);
                } finally {
                    db.close();
                }
            } finally {
                helper.close();
            }
        }
        if (id != 1) {
            return get(context, id);
        } else {
            return null;
        }
    }

    public static boolean delete(Context context, long id) {
        Transform.deleteTransaction(context, id);
        boolean success = false;
        synchronized (LOCK) {
            WebTransactionSqlHelper helper = new WebTransactionSqlHelper(context);
            try {
                SQLiteDatabase db = helper.getWritableDatabase();
                try {
                    success = db.delete(
                            WebTransactionSqlHelper.TABLE_NAME,
                            Column.ID + "=",
                            new String[]{id + ""}) > 0;
                } finally {
                    db.close();
                }
            } finally {
                helper.close();
            }
        }
        return success;
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
