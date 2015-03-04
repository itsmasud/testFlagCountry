package com.fieldnation.service.transaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.service.transaction.TransformSqlHelper.Column;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/4/2015.
 */
public class Transform implements Parcelable, TransformConstants {
    private static final String TAG = "Transform";

    private long _id;
    private long _transactionId;

    private String _objectName;
    private String _objectKey;
    private Action _action;
    private byte[] _data;

    public enum Action {
        ADD, MERGE, DELETE;
    }

    Transform(Cursor cursor) {
        _id = cursor.getLong(Column.ID.getIndex());
        _transactionId = cursor.getLong(Column.TRANSACTION_ID.getIndex());
        _objectName = cursor.getString(Column.OBJECT_NAME.getIndex());
        _objectKey = cursor.getString(Column.OBJECT_KEY.getIndex());
        _action = Action.values()[cursor.getInt(Column.ACTION.getIndex())];
        _data = cursor.getBlob(Column.DATA.getIndex());

    }

    public Transform(Bundle bundle) {
        _id = bundle.getLong(PARAM_ID);
        _transactionId = bundle.getLong(PARAM_TRANSACTION_ID);
        _objectName = bundle.getString(PARAM_OBJECT_NAME);
        _objectKey = bundle.getString(PARAM_OBJECT_KEY);
        _action = Action.values()[bundle.getInt(PARAM_ACTION)];
        _data = bundle.getByteArray(PARAM_DATA);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_ID, _id);
        bundle.putLong(PARAM_TRANSACTION_ID, _transactionId);
        bundle.putString(PARAM_OBJECT_NAME, _objectName);
        bundle.putString(PARAM_OBJECT_KEY, _objectKey);
        bundle.putInt(PARAM_DATA, _action.ordinal());
        bundle.putByteArray(PARAM_DATA, _data);

        return bundle;
    }

    /*-************************************-*/
    /*-         Getters/Setters            -*/
    /*-************************************-*/
    public long getId() {
        return _id;
    }

    public long getTransactionId() {
        return _transactionId;
    }

    public String getObjectName() {
        return _objectName;
    }

    public String getObjectKey() {
        return _objectKey;
    }

    public Action getAction() {
        return _action;
    }

    public byte[] getData() {
        return _data;
    }

    public Bundle makeTransformQuery(String objectName, String objectKey, Action action, byte[] data) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_OBJECT_NAME, objectName);
        bundle.putString(PARAM_OBJECT_KEY, objectKey);
        bundle.putInt(PARAM_ACTION, action.ordinal());
        bundle.putByteArray(PARAM_DATA, data);
        return bundle;
    }

    /*-*****************************-*/
    /*-         Database            -*/
    /*-*****************************-*/
    static Transform get(Context context, long id) {
        TransformSqlHelper helper = new TransformSqlHelper(context);
        Transform obj = null;
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = db.query(
                        TransformSqlHelper.TABLE_NAME,
                        TransactionSqlHelper.getColumnNames(),
                        Column.ID + "=?",
                        new String[]{id + ""},
                        null, null, null, "LIMIT 1");
                try {
                    if (cursor.moveToFirst()) {
                        obj = new Transform(cursor);
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

    static List<Transform> getObjectTransforms(Context context, String objectName, String objectKey) {
        TransformSqlHelper helper = new TransformSqlHelper(context);
        List<Transform> list = new LinkedList<>();
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = db.query(
                        TransformSqlHelper.TABLE_NAME,
                        TransactionSqlHelper.getColumnNames(),
                        Column.OBJECT_NAME + "=? AND " + Column.OBJECT_KEY + "=?",
                        new String[]{objectName, objectKey},
                        null, null, Column.ID + " ASC");

                try {
                    while (cursor.moveToNext()) {
                        list.add(new Transform(cursor));
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
        return list;
    }

    static Transform put(Context context, long transactionId, String objectName, String objectKey, Action action, byte[] data) {
        ContentValues v = new ContentValues();
        v.put(Column.TRANSACTION_ID.getName(), transactionId);
        v.put(Column.OBJECT_NAME.getName(), objectName);
        v.put(Column.OBJECT_KEY.getName(), objectKey);
        v.put(Column.ACTION.getName(), action.ordinal());
        v.put(Column.DATA.getName(), data);

        long id = -1;
        TransformSqlHelper helper = new TransformSqlHelper(context);
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                id = db.insert(
                        TransformSqlHelper.TABLE_NAME, null, v);
            } finally {
                db.close();
            }
        } finally {
            helper.close();
        }

        if (id != -1)
            return get(context, id);
        else
            return null;
    }

    static boolean deleteTransaction(Context context, long transactionId) {
        TransformSqlHelper helper = new TransformSqlHelper(context);
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                return db.delete(
                        TransformSqlHelper.TABLE_NAME,
                        Column.TRANSACTION_ID + "=?",
                        new String[]{transactionId + ""}) > 0;
            } finally {
                db.close();
            }
        } finally {
            helper.close();
        }
    }

    static boolean delete(Context context, long id) {
        TransformSqlHelper helper = new TransformSqlHelper(context);
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                return db.delete(
                        TransformSqlHelper.TABLE_NAME,
                        Column.ID + "=?",
                        new String[]{id + ""}) > 0;
            } finally {
                db.close();
            }
        } finally {
            helper.close();
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/

    public static final Creator<Transform> CREATOR = new Creator<Transform>() {
        @Override
        public Transform createFromParcel(Parcel source) {
            return new Transform(source.readBundle());
        }

        @Override
        public Transform[] newArray(int size) {
            return new Transform[size];
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
