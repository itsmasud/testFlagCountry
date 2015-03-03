package com.fieldnation.service.transaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.service.transaction.TransactionSqlHelper.Column;

/**
 * Created by Michael Carver on 3/3/2015.
 */
public class Transaction implements Parcelable, TransactionConstants {
    private static final String TAG = "Transaction";

    private long _id;
    private String _handlerName;
    private Object _handler;
    private Priority _priority;
    private String _request;
    private String _key;

    public enum Priority {
        HIGH(), NORMAL(), LOW();
    }

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    Transaction(Cursor cursor) {
        _id = cursor.getLong(Column.ID.getIndex());
        _handlerName = cursor.getString(Column.HANDLER.getIndex());
        _priority = Priority.values()[cursor.getInt(Column.PRIORITY.getIndex())];
        _request = cursor.getString(Column.REQUEST.getIndex());
        _key = cursor.getString(Column.KEY.getIndex());
    }

    public Transaction(Bundle bundle) {
        _id = bundle.getLong(PARAM_ID);
        _handlerName = bundle.getString(PARAM_HANDLER_NAME);
        _priority = Priority.values()[bundle.getInt(PARAM_PRIORITY)];
        _request = bundle.getString(PARAM_REQUEST);
        _key = bundle.getString(PARAM_KEY);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_ID, _id);
        bundle.putString(PARAM_HANDLER_NAME, _handlerName);
        bundle.putInt(PARAM_PRIORITY, _priority.ordinal());
        bundle.putString(PARAM_REQUEST, _request);
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

    public Priority getPriority() {
        return _priority;
    }

    public void setPriority(Priority priority) {
        _priority = priority;
    }

    public String getRequest() {
        return _request;
    }

    public void setRequest(String request) {
        _request = request;
    }

    public String getKey() {
        return _key;
    }

    public void setKey(String key) {
        _key = key;
    }

    Transaction save(Context context) {
        return put(context, this);
    }

    /*-*****************************************-*/
    /*-             Database interface          -*/
    /*-*****************************************-*/
    static Transaction get(Context context, long id) {
        TransactionSqlHelper helper = new TransactionSqlHelper(context);
        Transaction obj = null;
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = db.query(
                        TransactionSqlHelper.TABLE_NAME,
                        TransactionSqlHelper.getColumnNames(),
                        Column.ID + "=?",
                        new String[]{id + ""},
                        null, null, null, "LIMIT 1");

                try {
                    if (cursor.moveToFirst()) {
                        obj = new Transaction(cursor);
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

        return obj;
    }

    static Transaction getNext(Context context) {
        TransactionSqlHelper helper = new TransactionSqlHelper(context);
        Transaction obj = null;
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = db.query(
                        TransactionSqlHelper.TABLE_NAME,
                        TransactionSqlHelper.getColumnNames(),
                        null, null, null, null,
                        "PRIORITY DESC, ID ASC ",
                        "LIMIT 1");
                try {
                    if (cursor.moveToFirst()) {
                        obj = new Transaction(cursor);
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

        return obj;
    }

    static Transaction put(Context context, Transaction obj) {
        ContentValues v = new ContentValues();
        v.put(Column.HANDLER.getName(), obj._handlerName);
        v.put(Column.KEY.getName(), obj._key);
        v.put(Column.PRIORITY.getName(), obj._priority.ordinal());
        v.put(Column.REQUEST.getName(), obj._request);

        boolean success = false;
        TransactionSqlHelper helper = new TransactionSqlHelper(context);
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                success = db.update(
                        TransactionSqlHelper.TABLE_NAME,
                        v, Column.ID + "=" + obj._id, null) > 0;

            } finally {
                db.close();
            }
        } finally {
            helper.close();
        }

        if (success) {
            return get(context, obj._id);
        } else {
            return null;
        }
    }

    static Transaction put(Context context, Priority priority, String key, String request, String handlerName) {
        ContentValues v = new ContentValues();
        v.put(Column.HANDLER.getName(), handlerName);
        v.put(Column.KEY.getName(), key);
        v.put(Column.PRIORITY.getName(), priority.ordinal());
        v.put(Column.REQUEST.getName(), request);

        long id = -1;
        TransactionSqlHelper helper = new TransactionSqlHelper(context);
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                id = db.insert(TransactionSqlHelper.TABLE_NAME,
                        null, v);
            } finally {
                db.close();
            }
        } finally {
            helper.close();
        }

        if (id != 1) {
            return get(context, id);
        } else {
            return null;
        }
    }


    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
