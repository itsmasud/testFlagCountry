package com.fieldnation.service.objectstore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.service.objectstore.ObjectStoreSqlHelper.Column;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 2/26/2015.
 */
public class StoredObject implements Parcelable {

    private long _id;
    private long _objId;
    private String _objName;
    private boolean _isFile;
    private byte[] _data;
    private File _file;

    public StoredObject(Cursor cursor) {
        _id = cursor.getLong(Column.ID.getIndex());
        _objId = cursor.getLong(Column.OBJ_ID.getIndex());
        _objName = cursor.getString(Column.OBJ_NAME.getIndex());
        _isFile = cursor.getInt(Column.IS_FILE.getIndex()) == 1;
        if (_isFile) {
            _file = new File(new String(cursor.getBlob(Column.DATA.getIndex())));
        } else {
            _data = cursor.getBlob(Column.DATA.getIndex());
        }
    }

    public StoredObject(Bundle bundle) {

    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        bundle.putLong("ID", _id);
        bundle.putLong("OBJ_ID", _objId);
        bundle.putString("OBJ_NAME", _objName);
        bundle.putBoolean("IS_FILE", _isFile);
        if (_isFile)
            bundle.putByteArray("DATA", _file.getAbsolutePath().getBytes());
        else
            bundle.putByteArray("DATA", _data);

        return bundle;
    }

    public long getId() {
        return _id;
    }

    public long getObjId() {
        return _objId;
    }

    public String getObjName() {
        return _objName;
    }

    public boolean isFile() {
        return _isFile;
    }

    public File getFile() {
        return _file;
    }

    public void setFile(File file) {
        _isFile = true;
        _file = file;
    }

    public byte[] getData() {
        return _data;
    }

    public void setData(byte[] data) {
        _isFile = false;
        _data = data;
    }

    public StoredObject save(Context context) {
        return put(context, this);
    }

    /*-*****************************************-*/
    /*-             Database interface          -*/
    /*-*****************************************-*/
    public static StoredObject get(Context context, long id) {
        ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(context);
        StoredObject obj = null;
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = db.query(
                        ObjectStoreSqlHelper.TABLE_NAME,
                        ObjectStoreSqlHelper.getColumnNames(),
                        Column.OBJ_ID + "=?",
                        new String[]{id + ""},
                        null, null, null, "LIMIT 1");

                try {
                    if (cursor.moveToFirst()) {
                        obj = new StoredObject(cursor);
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


    public static StoredObject get(Context context, String objectTypeName, long objectId) {
        ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(context);
        StoredObject obj = null;
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = db.query(
                        ObjectStoreSqlHelper.TABLE_NAME,
                        ObjectStoreSqlHelper.getColumnNames(),
                        Column.OBJ_NAME + "=? AND " + Column.OBJ_ID + "=?",
                        new String[]{objectTypeName, objectId + ""},
                        null, null, null, "LIMIT 1");

                try {
                    if (cursor.moveToFirst()) {
                        obj = new StoredObject(cursor);
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

    public static StoredObject put(Context context, StoredObject obj) {
        ContentValues v = new ContentValues();
        v.put(Column.OBJ_NAME.getName(), obj._objName);
        v.put(Column.OBJ_ID.getName(), obj._objId);
        v.put(Column.IS_FILE.getName(), obj._isFile);
        if (obj._isFile) {
            v.put(Column.DATA.getName(), obj._file.getAbsolutePath());
        } else {
            v.put(Column.DATA.getName(), obj._data);
        }

        ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(context);
        boolean success = false;
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                success = db.update(ObjectStoreSqlHelper.TABLE_NAME, v, Column.ID + "=" + obj._id, null) > 0;
            } finally {
                db.close();
            }
        } finally {
            helper.close();
        }

        if (success)
            return get(context, obj._id);
        else {
            return null;
        }
    }

    public static StoredObject put(Context context, String objectTypeName, long objectId, File file) {
        ContentValues v = new ContentValues();
        v.put(Column.OBJ_NAME.getName(), objectTypeName);
        v.put(Column.OBJ_ID.getName(), objectId);
        v.put(Column.IS_FILE.getName(), true);
        v.put(Column.DATA.getName(), file.getAbsolutePath());

        long id = -1;
        ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(context);
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                id = db.insert(ObjectStoreSqlHelper.TABLE_NAME, null, v);
            } finally {
                db.close();
            }
        } finally {
            helper.close();
        }
        if (id != -1)
            return get(context, id);

        return null;
    }

    public static StoredObject put(Context context, String objectTypeName, long objectId, byte[] data) {
        ContentValues v = new ContentValues();
        v.put(Column.OBJ_NAME.getName(), objectTypeName);
        v.put(Column.OBJ_ID.getName(), objectId);
        v.put(Column.IS_FILE.getName(), false);
        v.put(Column.DATA.getName(), data);

        long id = -1;
        ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(context);
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                id = db.insert(ObjectStoreSqlHelper.TABLE_NAME, null, v);
            } finally {
                db.close();
            }
        } finally {
            helper.close();
        }
        if (id != -1)
            return get(context, id);

        return null;
    }

    public static List<StoredObject> list(Context context, String objectTypeName) {
        List<StoredObject> list = new LinkedList<>();

        ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(context);
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = db.query(
                        ObjectStoreSqlHelper.TABLE_NAME,
                        ObjectStoreSqlHelper.getColumnNames(),
                        Column.OBJ_NAME + "=?",
                        new String[]{objectTypeName},
                        null, null, null);

                try {
                    while (cursor.moveToNext()) {
                        list.add(new StoredObject(cursor));
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

    public static List<StoredObject> list(Context context, String objectTypeName, long[] ids) {
        List<StoredObject> list = new LinkedList<>();

        if (ids == null || ids.length == 0)
            return list;

        ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(context);
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                String[] param = new String[ids.length + 1];
                param[0] = objectTypeName;

                for (int i = 0; i < ids.length; i++) {
                    param[i + 1] = ids[i] + "";
                }

                Cursor cursor = db.query(
                        ObjectStoreSqlHelper.TABLE_NAME,
                        ObjectStoreSqlHelper.getColumnNames(),
                        Column.OBJ_NAME + "=? AND " + Column.OBJ_ID + " IN (" + makePlaceholders(ids.length) + ")",
                        param,
                        null, null, null);
                try {
                    while (cursor.moveToNext()) {
                        list.add(new StoredObject(cursor));
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

    public static boolean delete(Context context, String objectTypeName, long objId) {
        ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(context);
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                return db.delete(
                        ObjectStoreSqlHelper.TABLE_NAME,
                        Column.OBJ_NAME + "=? AND " + Column.OBJ_ID + "=?",
                        new String[]{objectTypeName, objId + ""}) > 0;
            } finally {
                db.close();
            }
        } finally {
            helper.close();
        }
    }

    public static boolean delete(Context context, long id) {
        ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(context);
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                return db.delete(
                        ObjectStoreSqlHelper.TABLE_NAME,
                        Column.ID + "=",
                        new String[]{id + ""}) > 0;
            } finally {
                db.close();
            }
        } finally {
            helper.close();
        }
    }

    private static String makePlaceholders(int count) {
        if (count < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(count * 2 - 1);
            sb.append("?");
            for (int i = 1; i < count; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<StoredObject> CREATOR = new Creator<StoredObject>() {
        @Override
        public StoredObject createFromParcel(Parcel source) {
            return new StoredObject(source.readBundle());
        }

        @Override
        public StoredObject[] newArray(int size) {
            return new StoredObject[size];
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
