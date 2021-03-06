package com.fieldnation.service.transaction;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ContextProvider;
import com.fieldnation.service.transaction.TransformSqlHelper.Column;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/4/2015.
 */
public class Transform implements Parcelable, TransformConstants {
    private static final String TAG = "Transform";

    private final long _id;
    private final long _transactionId;

    private final String _objectNameKey;
    private final String _objectName;
    private final String _objectKey;
    private final String _action;
    private final byte[] _data;

    Transform(Cursor cursor) {
        _id = cursor.getLong(Column.ID.getIndex());
        _transactionId = cursor.getLong(Column.TRANSACTION_ID.getIndex());
        _objectNameKey = cursor.getString(Column.OBJECT_NAME_KEY.getIndex());
        _objectName = cursor.getString(Column.OBJECT_NAME.getIndex());
        _objectKey = cursor.getString(Column.OBJECT_KEY.getIndex());
        _action = cursor.getString(Column.ACTION.getIndex());
        _data = cursor.getBlob(Column.DATA.getIndex());

//        Log.v(TAG, "get Transform(" + _id + "," + _transactionId + "," + _objectNameKey + ")");
//        Log.v(TAG, "action: " + _action);
//        Log.v(TAG, "data: " + new String(_data));
//        Log.v(TAG, "------------");
    }

    public Transform(Bundle bundle) {
        _id = bundle.getLong(PARAM_ID);
        _transactionId = bundle.getLong(PARAM_TRANSACTION_ID);
        _objectNameKey = bundle.getString(PARAM_OBJECT_NAME_KEY);
        _objectName = bundle.getString(PARAM_OBJECT_NAME);
        _objectKey = bundle.getString(PARAM_OBJECT_KEY);
        _action = bundle.getString(PARAM_ACTION);
        _data = bundle.getByteArray(PARAM_DATA);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_ID, _id);
        bundle.putLong(PARAM_TRANSACTION_ID, _transactionId);
        bundle.putString(PARAM_OBJECT_NAME_KEY, _objectNameKey);
        bundle.putString(PARAM_OBJECT_NAME, _objectName);
        bundle.putString(PARAM_OBJECT_KEY, _objectKey);
        bundle.putString(PARAM_DATA, _action);
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

    public String getAction() {
        return _action;
    }

    public byte[] getData() {
        return _data;
    }

    public static Bundle makeTransformQuery(String objectName, long objectKey, String action, byte[] data) {
        return makeTransformQuery(objectName, objectKey + "", action, data);
    }

    public static Bundle makeTransformQuery(String objectName, String objectKey, String action, byte[] data) {
//        Log.v(TAG, "makeTransformQuery(" + objectName + "," + objectKey + "," + action + ")");
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_OBJECT_NAME_KEY, objectName + "/" + objectKey);
        bundle.putString(PARAM_OBJECT_NAME, objectName);
        bundle.putString(PARAM_OBJECT_KEY, objectKey);
        bundle.putString(PARAM_ACTION, action);
        bundle.putByteArray(PARAM_DATA, data);
        return bundle;
    }

    /*-*****************************-*/
    /*-         Database            -*/
    /*-*****************************-*/
    public static Transform get(long id) {
//        Log.v(TAG, "get(" + id + ")");
        Transform obj = null;
        synchronized (TAG) {
            TransformSqlHelper helper = TransformSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = db.query(
                        TransformSqlHelper.TABLE_NAME,
                        TransformSqlHelper.getColumnNames(),
                        Column.ID + "=?",
                        new String[]{id + ""},
                        null, null, null, "1");
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
        }
        return obj;
    }

    public static void applyTransform(JsonObject dest, String objectName, long objectKey) {
        applyTransform(dest, objectName, objectKey + "");
    }

    public static void applyTransform(JsonObject dest, String objectName, String objectKey) {
        List<Transform> transList = getObjectTransforms(objectName, objectKey);
        for (int i = 0; i < transList.size(); i++) {
            Transform t = transList.get(i);
            try {
                JsonObject tObj = new JsonObject(t.getData());
                dest.deepmerge(tObj);
            } catch (Exception ex) {
                Log.v(TAG, "Transform " + new String(t.getData()));
            }
        }
    }

    public static List<Transform> getObjectTransforms(String objectName, long objectKey) {
        return getObjectTransforms(objectName, objectKey + "");
    }

    public static List<Transform> getObjectTransforms(String objectName, String objectKey) {
        final String objectNameKey = objectName + "/" + objectKey;
        List<Transform> list = new LinkedList<>();
        synchronized (TAG) {
            TransformSqlHelper helper = TransformSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = db.query(
                        TransformSqlHelper.TABLE_NAME,
                        TransformSqlHelper.getColumnNames(),
                        Column.OBJECT_NAME_KEY + "=?",
                        new String[]{objectNameKey},
                        null, null,
                        Column.ID + " ASC");

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
        }
        return list;
    }

    public static Transform put(long transactionId, Bundle query) {
        return put(transactionId, query.getString(PARAM_OBJECT_NAME), query.getString(PARAM_OBJECT_KEY),
                query.getString(PARAM_ACTION), query.getByteArray(PARAM_DATA));
    }

    public static Transform put(long transactionId, String objectName, long objectKey, String action, byte[] data) {
        return put(transactionId, objectName, objectKey + "", action, data);
    }

    public static Transform put(long transactionId, String objectName, String objectKey, String action, byte[] data) {
        final String objectNameKey = objectName + "/" + objectKey;

//        Log.v(TAG, "put(" + transactionId + "," + objectName + "," + objectKey + "," + action + "," + new String(data) + ")");
        ContentValues v = new ContentValues();
        v.put(Column.TRANSACTION_ID.getName(), transactionId);
        v.put(Column.OBJECT_NAME_KEY.getName(), objectNameKey);
        v.put(Column.OBJECT_NAME.getName(), objectName);
        v.put(Column.OBJECT_KEY.getName(), objectKey);
        v.put(Column.ACTION.getName(), action);
        v.put(Column.DATA.getName(), data);

        long id = -1;
        synchronized (TAG) {
            TransformSqlHelper helper = TransformSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                id = db.insert(TransformSqlHelper.TABLE_NAME, null, v);
            } finally {
                db.close();
            }
        }
        if (id != -1)
            return get(id);
        else
            return null;
    }

    public static boolean deleteTransaction(long transactionId) {
//        Log.v(TAG, "deleteTransaction(" + transactionId + ")");
        boolean success = false;
        synchronized (TAG) {
            TransformSqlHelper helper = TransformSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                success = db.delete(
                        TransformSqlHelper.TABLE_NAME,
                        Column.TRANSACTION_ID + "=?",
                        new String[]{transactionId + ""}) > 0;
            } finally {
                db.close();
            }
        }
        return success;
    }

    public static boolean delete(long id) {
//        Log.v(TAG, "delete(" + id + ")");
        boolean success = false;
        synchronized (TAG) {
            TransformSqlHelper helper = TransformSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                success = db.delete(
                        TransformSqlHelper.TABLE_NAME,
                        Column.ID + "=?",
                        new String[]{id + ""}) > 0;
            } finally {
                db.close();
            }
        }
        return success;
    }

    public static boolean deleteAll() {
//        Log.v(TAG, "delete(" + id + ")");
        boolean success = false;
        synchronized (TAG) {
            TransformSqlHelper helper = TransformSqlHelper.getInstance(ContextProvider.get());
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                success = db.delete(
                        TransformSqlHelper.TABLE_NAME,
                        null, null) > 0;
            } finally {
                db.close();
            }
        }
        return success;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/

    public static final Creator<Transform> CREATOR = new Creator<Transform>() {
        @Override
        public Transform createFromParcel(Parcel source) {
            return new Transform(source.readBundle(getClass().getClassLoader()));
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
