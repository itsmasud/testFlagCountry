package com.fieldnation.service.objectstore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.service.objectstore.ObjectStoreSqlHelper.Column;
import com.fieldnation.utils.misc;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Michael Carver on 2/26/2015.
 */
public class StoredObject implements Parcelable, ObjectStoreConstants {
    private static final String TAG = "StoredObject";

    private static final Random RAND = new Random(System.currentTimeMillis());

    private long _id;
    private long _profileId;
    private String _objKey;
    private String _objName;
    private long _lastupdated;
    private boolean _isFile;
    private byte[] _data;
    private File _file;
    private boolean _expires;

    StoredObject(Cursor cursor) {
        updateFromDatabase(cursor);
    }

    private void updateFromDatabase(Cursor cursor) {
        _id = cursor.getLong(Column.ID.getIndex());
        _profileId = cursor.getLong(Column.PROFILE_ID.getIndex());
        _objKey = cursor.getString(Column.OBJ_KEY.getIndex());
        _objName = cursor.getString(Column.OBJ_NAME.getIndex());
        _lastupdated = cursor.getLong(Column.LAST_UPDATED.getIndex());
        _isFile = cursor.getInt(Column.IS_FILE.getIndex()) == 1;
        _expires = cursor.getInt(Column.EXPIRES.getIndex()) == 1;
        if (!cursor.isNull(Column.DATA.getIndex())) {
            if (_isFile) {
                _file = new File(new String(cursor.getBlob(Column.DATA.getIndex())));
//                Log.v(TAG, "updateFromDatabase isFile, " + _file.getAbsolutePath());
            } else {
                _data = cursor.getBlob(Column.DATA.getIndex());
            }
        }
    }

    public StoredObject(Bundle bundle) {
        _id = bundle.getLong(PARAM_ID);
        _profileId = bundle.getLong(PARAM_PROFILE_ID);
        _objKey = bundle.getString(PARAM_OBJECT_KEY);
        _objName = bundle.getString(PARAM_OBJECT_TYPE);
        _lastupdated = bundle.getLong(PARAM_LAST_UPDATED);
        _isFile = bundle.getBoolean(PARAM_IS_FILE);
        _expires = bundle.getBoolean(PARAM_EXPIRES);
        if (_isFile)
            _file = (File) bundle.getSerializable(PARAM_FILE);
        else
            _data = bundle.getByteArray(PARAM_DATA);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_ID, _id);
        bundle.putLong(PARAM_PROFILE_ID, _profileId);
        bundle.putString(PARAM_OBJECT_KEY, _objKey);
        bundle.putString(PARAM_OBJECT_TYPE, _objName);
        bundle.putLong(PARAM_LAST_UPDATED, _lastupdated);
        bundle.putBoolean(PARAM_IS_FILE, _isFile);
        bundle.putBoolean(PARAM_EXPIRES, _expires);
        if (_isFile && _file != null)
            bundle.putSerializable(PARAM_FILE, _file);
        else if (_data != null)
            bundle.putByteArray(PARAM_DATA, _data);

        return bundle;
    }

    public long getId() {
        return _id;
    }

    public long getProfileId() {
        return _profileId;
    }

    public String getObjKey() {
        return _objKey;
    }

    public String getObjName() {
        return _objName;
    }

    public long getLastUpdated() {
        return _lastupdated;
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

    public boolean expires() {
        return _expires;
    }

    public StoredObject save() {
//        Log.v(TAG, "save");
        return put(this);
    }

    @Override
    public String toString() {
        return "[StoredObject id:" + _id + ", profileId:" + _profileId + ",  name:" + _objName + ", key:" + _objKey + "]";
    }

    /*-*****************************************-*/
    /*-             Database interface          -*/
    /*-*****************************************-*/

    /**
     * Gets an object based on the global id
     *
     * @param id the global id of the object
     * @return the object. Null if there was an error.
     */
    public static StoredObject get(long id) {
        Log.v(TAG, "get(" + id + ")");
        StoredObject obj = null;
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(App.get());
            try {
                SQLiteDatabase db = helper.getReadableDatabase();
                try {
                    Cursor cursor = db.query(
                            ObjectStoreSqlHelper.TABLE_NAME,
                            ObjectStoreSqlHelper.getColumnNames(),
                            Column.ID + "=?",
                            new String[]{id + ""},
                            null, null, null, "1");

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
        }
        return obj;
    }


    public static StoredObject get(long profileId, String objectTypeName, long objectKey) {
        return get(profileId, objectTypeName, objectKey + "");
    }

    /**
     * Finds the objectType with the matching key and returns it.
     *
     * @param objectTypeName name of the object type
     * @param objectKey      the object key
     * @return the object, null if there was an error.
     */
    public static StoredObject get(long profileId, String objectTypeName, String objectKey) {
        Log.v(TAG, "get(" + profileId + ", " + objectTypeName + ", " + objectKey + ")");
        // Log.v(TAG, "get(" + objectTypeName + "/" + objectKey + ")");
        StoredObject obj = null;
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(App.get());
            try {
                SQLiteDatabase db = helper.getReadableDatabase();
                try {
                    Cursor cursor = db.query(
                            ObjectStoreSqlHelper.TABLE_NAME,
                            ObjectStoreSqlHelper.getColumnNames(),
                            Column.PROFILE_ID + "=? AND "
                                    + Column.OBJ_NAME + "=? AND "
                                    + Column.OBJ_KEY + "=?",
                            new String[]{profileId + "", objectTypeName, objectKey},
                            null, null, null, "1");

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
        }
        return obj;
    }

    /**
     * updates the object's data in the database
     * If the object is a file object, it will copy the file data into the file store
     *
     * @param obj the object to save
     * @return the updated object
     */
    public static StoredObject put(StoredObject obj) {
        Log.v(TAG, "put(" + obj + ")");
        // Log.v(TAG, "put1(" + obj._id + ", " + obj._objName + "/" + obj._objKey + ");");
        ContentValues v = new ContentValues();
        v.put(Column.PROFILE_ID.getName(), obj._profileId);
        v.put(Column.OBJ_NAME.getName(), obj._objName);
        v.put(Column.OBJ_KEY.getName(), obj._objKey);
        v.put(Column.LAST_UPDATED.getName(), System.currentTimeMillis());
        v.put(Column.IS_FILE.getName(), obj._isFile);
        v.put(Column.EXPIRES.getName(), obj._expires);
        if (obj._isFile) {
            v.put(Column.DATA.getName(), obj._file.getAbsolutePath().getBytes());
        } else {
            v.put(Column.DATA.getName(), obj._data);
        }

        boolean success = false;
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(App.get());
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
        }
        if (success) {
            StoredObject result = get(obj._id);
            return result;
        } else {
            return null;
        }
    }

    public static StoredObject put(long profileId, String objectTypeName, long objectKey, File file, String filename) {
        return put(profileId, objectTypeName, objectKey + "", file, filename, true);
    }

    public static StoredObject put(long profileId, String objectTypeName, long objectKey, File file, String filename, boolean expires) {
        return put(profileId, objectTypeName, objectKey + "", file, filename, expires);
    }

    public static StoredObject put(long profileId, String objectTypeName, String objectKey, File file, String filename) {
        return put(profileId, objectTypeName, objectKey, file, filename, true);
    }

    public static StoredObject put(long profileId, String objectTypeName, String objectKey, File file, String filename, boolean expires) {
        Log.v(TAG, "put(" + profileId + ", " + objectTypeName + ", " + objectKey + ", " + file + ", " + filename + ", " + expires + ")");
        // Log.v(TAG, "put2(" + objectTypeName + "/" + objectKey + ", " + file.getAbsolutePath() + ")");
        StoredObject result = get(profileId, objectTypeName, objectKey);
        if (result != null) {
            result.delete(result);
        }

        ContentValues v = new ContentValues();
        v.put(Column.PROFILE_ID.getName(), profileId);
        v.put(Column.OBJ_NAME.getName(), objectTypeName);
        v.put(Column.OBJ_KEY.getName(), objectKey);
        v.put(Column.LAST_UPDATED.getName(), System.currentTimeMillis());
        v.put(Column.IS_FILE.getName(), true);
        v.put(Column.EXPIRES.getName(), expires ? 1 : 0);

        long id = -1;
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(App.get());
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
        }
        if (id != -1) {
//            Log.v(TAG, "put2, copy file, " + id);
            // copy the file to the file store
            String appFileStore = App.get().getStoragePath() + "/FileStore";
            new File(appFileStore).mkdirs();
            File dest = new File(appFileStore + "/" + id + "_" + filename);

            if (dest.exists())
                dest.delete();

            boolean copySuccess = false;
            try {
                copySuccess = misc.copyFile(file, dest);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (!copySuccess) {
//                Log.v(TAG, "put2, copy failed");
                delete(id);
                dest.delete();
            } else {
//                Log.v(TAG, "put2, copy success");
                result = get(id);
                result.setFile(dest);
                result = result.save();
                return result;
            }
        }

        return null;
    }

    public static StoredObject put(long profileId, String objectTypeName, long objectKey, byte[] data, String filename) {
        return put(profileId, objectTypeName, objectKey + "", data, filename, true);
    }

    public static StoredObject put(long profileId, String objectTypeName, long objectKey, byte[] data, String filename, boolean expires) {
        return put(profileId, objectTypeName, objectKey + "", data, filename, expires);
    }

    public static StoredObject put(long profileId, String objectTypeName, String objectKey, byte[] data, String filename) {
        return put(profileId, objectTypeName, objectKey, data, filename, true);
    }

    public static StoredObject put(long profileId, String objectTypeName, String objectKey, byte[] data, String filename, boolean expires) {
        Log.v(TAG, "put(" + profileId + ", " + objectTypeName + ", " + objectKey + ", byte[] data, " + filename + ", " + expires + ")");
        // Log.v(TAG, "put2(" + objectTypeName + "/" + objectKey + ", " + file.getAbsolutePath() + ")");
        StoredObject result = get(profileId, objectTypeName, objectKey);
        if (result != null) {
            result.delete(result);
        }

        ContentValues v = new ContentValues();
        v.put(Column.PROFILE_ID.getName(), profileId);
        v.put(Column.OBJ_NAME.getName(), objectTypeName);
        v.put(Column.OBJ_KEY.getName(), objectKey);
        v.put(Column.LAST_UPDATED.getName(), System.currentTimeMillis());
        v.put(Column.IS_FILE.getName(), true);
        v.put(Column.EXPIRES.getName(), expires ? 1 : 0);

        long id = -1;
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(App.get());
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
        }
        if (id != -1) {
//            Log.v(TAG, "put2, copy file, " + id);
            // copy the file to the file store
            String appFileStore = App.get().getStoragePath() + "/FileStore";
            new File(appFileStore).mkdirs();
            File dest = new File(appFileStore + "/" + id + "_" + filename);

            if (dest.exists())
                dest.delete();

            boolean copySuccess = false;
            try {
                FileOutputStream fout = new FileOutputStream(dest);
                fout.write(data);
                fout.close();
                copySuccess = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (!copySuccess) {
//                Log.v(TAG, "put2, copy failed");
                delete(id);
                dest.delete();
            } else {
//                Log.v(TAG, "put2, copy success");
                result = get(id);
                result.setFile(dest);
                result = result.save();
                return result;
            }
        }

        return null;
    }

    public static StoredObject put(long profileId, String objectTypeName, long objectKey, byte[] data) {
        return put(profileId, objectTypeName, objectKey + "", data, true);
    }

    public static StoredObject put(long profileId, String objectTypeName, long objectKey, byte[] data, boolean expires) {
        return put(profileId, objectTypeName, objectKey + "", data, expires);
    }

    public static StoredObject put(long profileId, String objectTypeName, String objectKey, byte[] data) {
        return put(profileId, objectTypeName, objectKey, data, true);
    }

    public static StoredObject put(long profileId, String objectTypeName, String objectKey, byte[] data, boolean expires) {
        Log.v(TAG, "put(" + profileId + ", " + objectTypeName + ", " + objectKey + ", byte[] data, " + expires + ")");
        // Log.v(TAG, "put3(" + objectTypeName + "/" + objectKey + ")");
        StoredObject result = get(profileId, objectTypeName, objectKey);

        if (result != null) {
            result.setData(data);
            result = result.save();
            return result;
        }

        ContentValues v = new ContentValues();
        v.put(Column.PROFILE_ID.getName(), profileId);
        v.put(Column.OBJ_NAME.getName(), objectTypeName);
        v.put(Column.OBJ_KEY.getName(), objectKey);
        v.put(Column.LAST_UPDATED.getName(), System.currentTimeMillis());
        v.put(Column.IS_FILE.getName(), false);
        v.put(Column.DATA.getName(), data);
        v.put(Column.EXPIRES.getName(), expires ? 1 : 0);

        long id = -1;
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(App.get());
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
        }
        if (id != -1) {
            result = get(id);
            return result;
        }

        return null;
    }

    public static List<StoredObject> list(long profileId, String objectTypeName) {
        Log.v(TAG, "list(" + profileId + ", " + objectTypeName + ")");
        List<StoredObject> list = new LinkedList<>();

        synchronized (TAG) {
            ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(App.get());
            try {
                SQLiteDatabase db = helper.getReadableDatabase();
                try {
                    Cursor cursor = db.query(
                            ObjectStoreSqlHelper.TABLE_NAME,
                            ObjectStoreSqlHelper.getColumnNames(),
                            Column.PROFILE_ID + "=? AND " + Column.OBJ_NAME + "=?",
                            new String[]{profileId + "", objectTypeName},
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
        }
        return list;
    }

    public static List<StoredObject> list(long profileId, String objectTypeName, String[] keys) {
        Log.v(TAG, "list(" + profileId + ", " + objectTypeName + ", String[] keys)");
        List<StoredObject> list = new LinkedList<>();

        if (keys == null || keys.length == 0)
            return list;

        synchronized (TAG) {
            ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(App.get());
            try {
                SQLiteDatabase db = helper.getReadableDatabase();
                try {
                    String[] param = new String[keys.length + 2];
                    param[0] = profileId + "";
                    param[1] = objectTypeName;

                    System.arraycopy(keys, 0, param, 1, keys.length);

                    Cursor cursor = db.query(
                            ObjectStoreSqlHelper.TABLE_NAME,
                            ObjectStoreSqlHelper.getColumnNames(),
                            Column.PROFILE_ID + "=? AND "
                                    + Column.OBJ_NAME + "=? AND "
                                    + Column.OBJ_KEY
                                    + " IN (" + makePlaceholders(keys.length) + ")",
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
        }
        return list;
    }

    public static void flush(long deathAge) {
        Log.v(TAG, "flush(" + deathAge + ")");
        List<StoredObject> list = new LinkedList<>();
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(App.get());
            try {
                SQLiteDatabase db = helper.getReadableDatabase();
                try {
                    Cursor cursor = db.query(
                            ObjectStoreSqlHelper.TABLE_NAME,
                            ObjectStoreSqlHelper.getColumnNames(),
                            Column.LAST_UPDATED + " < ? AND " + Column.EXPIRES + " = ?",
                            new String[]{(System.currentTimeMillis() - deathAge) + "", "1"},
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
        }

        for (int i = 0; i < list.size(); i++) {
            StoredObject obj = list.get(i);
            delete(obj);
        }
    }

    public static boolean delete(long profileId, String objectTypeName, long objkey) {
        return delete(profileId, objectTypeName, objkey + "");
    }

    public static boolean delete(long profileId, String objectTypeName, String objkey) {
        Log.v(TAG, "delete(" + profileId + ", " + objectTypeName + ", " + objkey + ")");
        StoredObject obj = get(profileId, objectTypeName, objkey);
        if (obj != null && obj._isFile) {
            obj._file.delete();
        }

        boolean success = false;
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(App.get());
            try {
                SQLiteDatabase db = helper.getWritableDatabase();
                try {
                    success = db.delete(
                            ObjectStoreSqlHelper.TABLE_NAME,
                            Column.PROFILE_ID + "=? AND "
                                    + Column.OBJ_NAME + "=? AND "
                                    + Column.OBJ_KEY + "=?",
                            new String[]{profileId + "", objectTypeName, objkey}) > 0;
                } finally {
                    db.close();
                }
            } finally {
                helper.close();
            }
        }
        return success;
    }

    public static boolean delete(long id) {
        Log.v(TAG, "delete(" + id + ")");
        StoredObject obj = get(id);
        if (obj != null && obj._isFile) {
            obj._file.delete();
        }

        boolean success = false;
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(App.get());
            try {
                SQLiteDatabase db = helper.getWritableDatabase();
                try {
                    success = db.delete(
                            ObjectStoreSqlHelper.TABLE_NAME,
                            Column.ID + "=?",
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

    public static boolean delete(StoredObject obj) {
        Log.v(TAG, "delete(" + obj + ")");
        if (obj != null && obj.getFile() != null && obj.isFile()) {
            obj.getFile().delete();
        }

        boolean success = false;
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = new ObjectStoreSqlHelper(App.get());
            try {
                SQLiteDatabase db = helper.getWritableDatabase();
                try {
                    success = db.delete(
                            ObjectStoreSqlHelper.TABLE_NAME,
                            Column.ID + "=?",
                            new String[]{obj.getId() + ""}) > 0;
                } finally {
                    db.close();
                }
            } finally {
                helper.close();
            }
        }
        return success;
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
