package com.fieldnation.service.objectstore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.GlobalState;
import com.fieldnation.service.objectstore.ObjectStoreSqlHelper.Column;
import com.fieldnation.utils.misc;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 2/26/2015.
 */
public class StoredObject implements Parcelable, ObjectStoreConstants {

    private long _id;
    private String _objKey;
    private String _objName;
    private boolean _isFile;
    private byte[] _metaData;
    private byte[] _data;
    private File _file;

    StoredObject(Cursor cursor) {
        _id = cursor.getLong(Column.ID.getIndex());
        _objKey = cursor.getString(Column.OBJ_KEY.getIndex());
        _objName = cursor.getString(Column.OBJ_NAME.getIndex());
        _isFile = cursor.getInt(Column.IS_FILE.getIndex()) == 1;
        _metaData = cursor.getBlob(Column.META_DATA.getIndex());
        if (_isFile) {
            _file = new File(new String(cursor.getBlob(Column.DATA.getIndex())));
        } else {
            _data = cursor.getBlob(Column.DATA.getIndex());
        }
    }

    public StoredObject(Bundle bundle) {
        _id = bundle.getLong(PARAM_ID);
        _objKey = bundle.getString(PARAM_OBJECT_KEY);
        _objName = bundle.getString(PARAM_OBJECT_TYPE);
        _isFile = bundle.getBoolean(PARAM_IS_FILE);
        _metaData = bundle.getByteArray(PARAM_META_DATA);
        if (_isFile)
            _file = (File) bundle.getSerializable(PARAM_FILE);
        else
            _data = bundle.getByteArray(PARAM_DATA);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_ID, _id);
        bundle.putString(PARAM_OBJECT_KEY, _objKey);
        bundle.putString(PARAM_OBJECT_TYPE, _objName);
        bundle.putBoolean(PARAM_IS_FILE, _isFile);
        bundle.putByteArray(PARAM_META_DATA, _metaData);
        if (_isFile)
            bundle.putSerializable(PARAM_FILE, _file);
        else
            bundle.putByteArray(PARAM_DATA, _data);

        return bundle;
    }

    public long getId() {
        return _id;
    }

    public String getObjKey() {
        return _objKey;
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

    public byte[] getMetaData() {
        return _metaData;
    }

    public void setMetaData(byte[] metaData) {
        _metaData = metaData;
    }

    StoredObject save(Context context) {
        return put(context, this);
    }

    /*-*****************************************-*/
    /*-             Database interface          -*/
    /*-*****************************************-*/

    /**
     * Gets an object based on the global id
     *
     * @param context android context object
     * @param id      the global id of the object
     * @return the object. Null if there was an error.
     */
    static StoredObject get(Context context, long id) {
        ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(context);
        StoredObject obj = null;
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = db.query(
                        ObjectStoreSqlHelper.TABLE_NAME,
                        ObjectStoreSqlHelper.getColumnNames(),
                        Column.ID + "=?",
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


    /**
     * Finds the objectType with the matching key and returns it.
     *
     * @param context        Android application context
     * @param objectTypeName name of the object type
     * @param objectKey      the object key
     * @return the object, null if there was an error.
     */
    static StoredObject get(Context context, String objectTypeName, String objectKey) {
        ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(context);
        StoredObject obj = null;
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = db.query(
                        ObjectStoreSqlHelper.TABLE_NAME,
                        ObjectStoreSqlHelper.getColumnNames(),
                        Column.OBJ_NAME + "=? AND " + Column.OBJ_KEY + "=?",
                        new String[]{objectTypeName, objectKey},
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

    /**
     * updates the object's data in the database
     * If the object is a file object, it will copy the file data into the file store
     *
     * @param context Android application context
     * @param obj     the object to save
     * @return the updated object
     */
    static StoredObject put(Context context, StoredObject obj) {
        ContentValues v = new ContentValues();
        v.put(Column.OBJ_NAME.getName(), obj._objName);
        v.put(Column.OBJ_KEY.getName(), obj._objKey);
        v.put(Column.IS_FILE.getName(), obj._isFile);
        if (obj._isFile) {
            // this is a file object. check that it's in the file store, if not, then copy it in.
            String appFileStore = GlobalState.getContext().getStoragePath() + "/FileStore";
            String filepath = appFileStore + "/os" + obj._id + ".dat";

            // file isn't in the store
            if (!filepath.equals(obj._file.getAbsolutePath())) {
                new File(appFileStore).mkdirs();
                File dest = new File(filepath);

                // clear the way.
                if (dest.exists())
                    dest.delete();

                // try to copy
                boolean copySuccess = false;
                try {
                    copySuccess = misc.copyFile(obj._file, dest);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (copySuccess) {
                    // success, update entry to point to the file store
                    v.put(Column.DATA.getName(), dest.getAbsolutePath());
                } else {
                    // failed, delete atempt, return error
                    dest.delete();
                    return null;
                }
            } else {
                // already in store, do nothing.
                v.put(Column.DATA.getName(), obj._file.getAbsolutePath());
            }

        } else {
            v.put(Column.DATA.getName(), obj._data);
        }
        v.put(Column.META_DATA.getName(), obj._metaData);

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

    static StoredObject put(Context context, String objectTypeName, String objectKey, byte[] metaData, File file) {
        ContentValues v = new ContentValues();
        v.put(Column.OBJ_NAME.getName(), objectTypeName);
        v.put(Column.OBJ_KEY.getName(), objectKey);
        v.put(Column.IS_FILE.getName(), true);
        v.put(Column.META_DATA.getName(), metaData);

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
        if (id != -1) {
            // copy the file to the file store
            String appFileStore = GlobalState.getContext().getStoragePath() + "/FileStore";
            new File(appFileStore).mkdirs();
            File dest = new File(appFileStore + "/os" + id + ".dat");

            if (dest.exists())
                dest.delete();

            boolean copySuccess = false;
            try {
                copySuccess = misc.copyFile(file, dest);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (!copySuccess) {
                delete(context, id);
                dest.delete();
            } else {
                StoredObject result = get(context, id);
                result.setFile(dest);
                result.save(context);
                return result;
            }
        }

        return null;
    }

    static StoredObject put(Context context, String objectTypeName, String objectKey, byte[] metaData, byte[] data) {
        ContentValues v = new ContentValues();
        v.put(Column.OBJ_NAME.getName(), objectTypeName);
        v.put(Column.OBJ_KEY.getName(), objectKey);
        v.put(Column.IS_FILE.getName(), false);
        v.put(Column.DATA.getName(), data);
        v.put(Column.META_DATA.getName(), metaData);

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

    static List<StoredObject> list(Context context, String objectTypeName) {
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

    static List<StoredObject> list(Context context, String objectTypeName, String[] keys) {
        List<StoredObject> list = new LinkedList<>();

        if (keys == null || keys.length == 0)
            return list;

        ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(context);
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                String[] param = new String[keys.length + 1];
                param[0] = objectTypeName;

                for (int i = 0; i < keys.length; i++) {
                    param[i + 1] = keys[i];
                }

                Cursor cursor = db.query(
                        ObjectStoreSqlHelper.TABLE_NAME,
                        ObjectStoreSqlHelper.getColumnNames(),
                        Column.OBJ_NAME + "=? AND " + Column.OBJ_KEY + " IN (" + makePlaceholders(keys.length) + ")",
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

    static boolean delete(Context context, String objectTypeName, String objkey) {
        ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(context);
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                return db.delete(
                        ObjectStoreSqlHelper.TABLE_NAME,
                        Column.OBJ_NAME + "=? AND " + Column.OBJ_KEY + "=?",
                        new String[]{objectTypeName, objkey}) > 0;
            } finally {
                db.close();
            }
        } finally {
            helper.close();
        }
    }

    static boolean delete(Context context, long id) {
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
