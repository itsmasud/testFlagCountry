package com.fieldnation.fnstore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.ObjectStoreSqlHelper.Column;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.misc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Michael Carver on 2/26/2015.
 */
public class StoredObject implements Parcelable, ObjectStoreConstants {
    private static final String TAG = "StoredObject";

    private long _id;
    private long _profileId;
    private String _objKey;
    private String _objName;
    private long _lastupdated;
    private boolean _isFile;
    private byte[] _data;
    private File _file;
    private boolean _expires;
    private byte[] _hash;

    private static final Object WRITE_PAUSE = new Object();
    private static final Set<Long> WRITING = new HashSet<>();

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
            } else {
                _data = cursor.getBlob(Column.DATA.getIndex());
            }
        }
        _hash = cursor.getBlob(Column.HASH.getIndex());
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
        _hash = bundle.getByteArray(PARAM_HASH);
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

        bundle.putByteArray(PARAM_HASH, _hash);
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

    public boolean isUri() {
        return _isFile;
    }

    public Uri getUri() {
        return Uri.fromFile(_file);
    }

    public byte[] getHash() {
        return _hash;
    }

    public String getHashHex() {
        return misc.getHex(_hash);
    }

    public long size() {
        if (_file != null)
            return _file.length();

        if (_data != null)
            return _data.length;

        return -1;
    }

    public void setFile(File file, byte[] hash) {
        _isFile = true;
        _file = file;
        _hash = hash;
    }

    public byte[] getData() {
        return _data;
    }

    public void setData(byte[] data) {
        _isFile = false;
        _data = data;

        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            sha1.update(data);
            _hash = sha1.digest();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

    }

    public boolean expires() {
        return _expires;
    }

    public StoredObject save(Context context) {
//        Log.v(TAG, "save");
        return put(context, this);
    }

    @Override
    public String toString() {
        return "[StoredObject id:" + _id + ", profileId:" + _profileId + ", name:" + _objName + ", key:" + _objKey + "]";
    }

    /*-*********************************************-*/
    /*-             Database interface              -*/
    /*-*********************************************-*/
    private static void threadSleep() {
//        try {
//            Thread.sleep(30000);
//        } catch (Exception ex) {
//            Log.v(TAG, ex);
//        }
    }

    private static void addWriting(long id) {
        Log.v(TAG, "addWriting " + id);
        //Log.v(TAG, DebugUtils.getStackTrace(new Exception()));
        synchronized (TAG) {
            WRITING.add(id);
        }
    }

    private static boolean isWriting(long id) {
        Log.v(TAG, "isWriting " + id);
        //Log.v(TAG, DebugUtils.getStackTrace(new Exception()));
        synchronized (TAG) {
            return WRITING.contains(id);
        }
    }

    private static void removeWriting(long id) {
        Log.v(TAG, "removeWriting " + id);
        //Log.v(TAG, DebugUtils.getStackTrace(new Exception()));

        synchronized (TAG) {
            WRITING.remove(id);
        }
        synchronized (WRITE_PAUSE) {
            WRITE_PAUSE.notifyAll();
        }
    }

    private static void waitWriting(long id) {
        Log.v(TAG, "waitWriting " + id);
        //Log.v(TAG, DebugUtils.getStackTrace(new Exception()));
        Stopwatch stopwatch = new Stopwatch(true);
        while (isWriting(id)) {
            synchronized (WRITE_PAUSE) {
                try {
                    WRITE_PAUSE.wait(10000);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
        }
        Log.v(TAG, "waitWriting " + id + " time: " + stopwatch.finish());
    }


    /**
     * Gets an object based on the global id
     *
     * @param id the global id of the object
     * @return the object. Null if there was an error.
     */
    public static StoredObject get(Context context, long id) {
        return get(context, id, true);
    }


    /**
     * Gets an object based on the global id
     *
     * @param id the global id of the object
     * @return the object. Null if there was an error.
     */
    public static StoredObject get(Context context, long id, boolean allowWaiting) {
        //Log.v(TAG, "get(" + id + ")");
        StoredObject obj = null;

        if (allowWaiting)
            waitWriting(id);

        synchronized (TAG) {
            ObjectStoreSqlHelper helper = ObjectStoreSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.query(
                            ObjectStoreSqlHelper.TABLE_NAME,
                            ObjectStoreSqlHelper.getColumnNames(),
                            Column.ID + "=?",
                            new String[]{id + ""},
                            null, null, null, "1");

                    if (cursor.moveToFirst()) {
                        obj = new StoredObject(cursor);
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

    public static StoredObject get(Context context, long profileId, String objectTypeName, long objectKey) {
        return get(context, profileId, objectTypeName, objectKey + "");
    }

    /**
     * Finds the objectType with the matching key and returns it.
     *
     * @param objectTypeName name of the object type
     * @param objectKey      the object key
     * @return the object, null if there was an error.
     */
    public static StoredObject get(Context context, long profileId, String objectTypeName, String objectKey) {
        try {
            Log.v(TAG, "get(" + profileId + ", " + objectTypeName + ", " + objectKey + ")");
            // Log.v(TAG, "get(" + objectTypeName + "/" + objectKey + ")");
            StoredObject obj = null;
            synchronized (TAG) {
                ObjectStoreSqlHelper helper = ObjectStoreSqlHelper.getInstance(context);
                SQLiteDatabase db = helper.getReadableDatabase();
                try {
                    Cursor cursor = null;
                    try {
                        cursor = db.query(
                                ObjectStoreSqlHelper.TABLE_NAME,
                                ObjectStoreSqlHelper.getColumnNames(),
                                Column.PROFILE_ID + "=? AND "
                                        + Column.OBJ_NAME + "=? AND "
                                        + Column.OBJ_KEY + "=?",
                                new String[]{profileId + "", objectTypeName, objectKey},
                                null, null, null, "1");

                        if (cursor.moveToFirst()) {
                            obj = new StoredObject(cursor);
                        }
                    } finally {
                        if (cursor != null) cursor.close();
                    }
                } finally {
                    if (db != null) db.close();
                }
            }
            if (obj != null && isWriting(obj._id)) {
                Log.v(TAG, "get redo, still writing...");
                waitWriting(obj._id);

                return get(context, profileId, objectTypeName, objectKey);
            }

            return obj;
        } catch (Exception ex) {
            Log.logException(ex);
            return null;
        }
    }

    /**
     * updates the object's data in the database
     * If the object is a file object, it will copy the file data into the file store
     *
     * @param obj the object to save
     * @return the updated object
     */
    public static StoredObject put(Context context, StoredObject obj) {
        Log.v(TAG, "put(" + obj + ")");
        ContentValues v = new ContentValues();
        v.put(Column.PROFILE_ID.getName(), obj._profileId);
        v.put(Column.OBJ_NAME.getName(), obj._objName);
        v.put(Column.OBJ_KEY.getName(), obj._objKey);
        v.put(Column.LAST_UPDATED.getName(), System.currentTimeMillis());
        v.put(Column.IS_FILE.getName(), obj._isFile);
        v.put(Column.EXPIRES.getName(), obj._expires);
        v.put(Column.HASH.getName(), obj._hash);
        if (obj._isFile) {
            v.put(Column.DATA.getName(), obj._file.getAbsolutePath().getBytes());
        } else {
            v.put(Column.DATA.getName(), obj._data);
        }

        boolean success = false;
        synchronized (TAG) {
            addWriting(obj._id);
            ObjectStoreSqlHelper helper = ObjectStoreSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                success = db.update(ObjectStoreSqlHelper.TABLE_NAME, v, Column.ID + "=" + obj._id, null) > 0;
            } finally {
                if (db != null) db.close();
            }
        }

        threadSleep(); // for testing

        if (success) {
            StoredObject result = get(context, obj._id, false);
            removeWriting(obj._id);
            return result;
        } else {
            return null;
        }
    }

    public static StoredObject put(Context context, long profileId, String objectTypeName, long objectKey, Uri uri, String filename) {
        return put(context, profileId, objectTypeName, objectKey + "", uri, filename, true);
    }

    public static StoredObject put(Context context, long profileId, String objectTypeName, long objectKey, Uri uri, String filename, boolean expires) {
        return put(context, profileId, objectTypeName, objectKey + "", uri, filename, expires);
    }

    public static StoredObject put(Context context, long profileId, String objectTypeName, String objectKey, Uri uri, String filename) {
        return put(context, profileId, objectTypeName, objectKey, uri, filename, true);
    }

    public static StoredObject put(Context context, long profileId, String objectTypeName, String objectKey, Uri uri, String filename, boolean expires) {
        Log.v(TAG, "put(" + profileId + ", " + objectTypeName + ", " + objectKey + ", " + uri + ", " + filename + ", " + expires + ")");
        try {
            return put(context, profileId, objectTypeName, objectKey, context.getContentResolver().openInputStream(uri), filename, expires);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static StoredObject put(Context context, long profileId, String objectTypeName, long objectKey, InputStream inputStream, String filename) {
        return put(context, profileId, objectTypeName, objectKey + "", inputStream, filename, true);
    }

    public static StoredObject put(Context context, long profileId, String objectTypeName, long objectKey, InputStream inputStream, String filename, boolean expires) {
        return put(context, profileId, objectTypeName, objectKey + "", inputStream, filename, expires);
    }

    public static StoredObject put(Context context, long profileId, String objectTypeName, String objectKey, InputStream inputStream, String filename) {
        return put(context, profileId, objectTypeName, objectKey, inputStream, filename, true);
    }

    public static StoredObject put(Context context, long profileId, String objectTypeName, String objectKey, InputStream inputStream, String filename, boolean expires) {
        long id = -1;
        Log.v(TAG, "put(" + profileId + ", " + objectTypeName + ", " + objectKey + ", InputStream, " + filename + ", " + expires + ")");
        // Log.v(TAG, "put2(" + objectTypeName + "/" + objectKey + ", " + file.getAbsolutePath() + ")");
        StoredObject result = get(context, profileId, objectTypeName, objectKey);
        if (result != null) {
            delete(context, result);
        }

        ContentValues v = new ContentValues();
        v.put(Column.PROFILE_ID.getName(), profileId);
        v.put(Column.OBJ_NAME.getName(), objectTypeName);
        v.put(Column.OBJ_KEY.getName(), objectKey);
        v.put(Column.LAST_UPDATED.getName(), System.currentTimeMillis());
        v.put(Column.IS_FILE.getName(), true);
        v.put(Column.EXPIRES.getName(), expires ? 1 : 0);

        synchronized (TAG) {
            ObjectStoreSqlHelper helper = ObjectStoreSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                id = db.insert(ObjectStoreSqlHelper.TABLE_NAME, null, v);
                if (id != -1)
                    addWriting(id);
            } finally {
                if (db != null) db.close();
            }
        }

        threadSleep(); // for testing

        if (id != -1) {
            // Log.v(TAG, "put2, copy file, " + id);
            // copy the file to the file store
            String appFileStore = getStoragePath(context) + "/FileStore";
            new File(appFileStore).mkdirs();
            File dest = new File(appFileStore + "/" + id + "_" + filename);

            if (dest.exists())
                dest.delete();

            byte[] hash = null;
            try {
                hash = FileUtils.writeStream(inputStream, dest);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

            if (hash == null) {
                Log.v(TAG, "put2, copy failed");
                delete(context, id);
                dest.delete();
                removeWriting(id);
            } else {
                Log.v(TAG, "put2, copy success");
                result = get(context, id, false);
                result.setFile(dest, hash);
                result = result.save(context);
                removeWriting(id);
                return result;
            }
        }
        return null;
    }

    public static StoredObject put(Context context, long profileId, String objectTypeName, long objectKey, byte[] data, String filename) {
        return put(context, profileId, objectTypeName, objectKey + "", data, filename, true);
    }

    public static StoredObject put(Context context, long profileId, String objectTypeName, long objectKey, byte[] data, String filename, boolean expires) {
        return put(context, profileId, objectTypeName, objectKey + "", data, filename, expires);
    }

    public static StoredObject put(Context context, long profileId, String objectTypeName, String objectKey, byte[] data, String filename) {
        return put(context, profileId, objectTypeName, objectKey, data, filename, true);
    }

    public static StoredObject put(Context context, long profileId, String objectTypeName, String objectKey, byte[] data, String filename, boolean expires) {
        Log.v(TAG, "put(" + profileId + ", " + objectTypeName + ", " + objectKey + ", byte[] data, " + filename + ", " + expires + ")");
        // Log.v(TAG, "put2(" + objectTypeName + "/" + objectKey + ", " + file.getAbsolutePath() + ")");
        StoredObject result = get(context, profileId, objectTypeName, objectKey);
        if (result != null) {
            delete(context, result);
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
            ObjectStoreSqlHelper helper = ObjectStoreSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                id = db.insert(ObjectStoreSqlHelper.TABLE_NAME, null, v);
                if (id != -1)
                    addWriting(id);
            } finally {
                if (db != null) db.close();
            }
        }

        threadSleep(); // for testing

        if (id != -1) {
//            Log.v(TAG, "put2, copy file, " + id);
            // copy the file to the file store
            String appFileStore = getStoragePath(context) + "/FileStore";
            new File(appFileStore).mkdirs();
            File dest = new File(appFileStore + "/" + id + "_" + filename);

            if (dest.exists())
                dest.delete();

            byte[] hash = null;
            FileOutputStream fout = null;
            try {
                fout = new FileOutputStream(dest);
                try {
                    MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
                    sha1.reset();
                    sha1.update(data, 0, data.length);
                    hash = sha1.digest();
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
                fout.write(data);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            } finally {
                if (fout != null) {
                    try {
                        fout.close();
                    } catch (Exception ex) {
                    }
                    fout = null;
                }
            }

            if (hash == null) {
                Log.v(TAG, "put2, copy failed");
                delete(context, id);
                dest.delete();
                removeWriting(id);
            } else {
                Log.v(TAG, "put2, copy success");
                result = get(context, id, false);
                result.setFile(dest, hash);
                result = result.save(context);
                removeWriting(id);
                return result;
            }
        }
        return null;
    }

    public static StoredObject put(Context context, long profileId, String objectTypeName, long objectKey, byte[] data) {
        return put(context, profileId, objectTypeName, objectKey + "", data, true);
    }

    public static StoredObject put(Context context, long profileId, String objectTypeName, long objectKey, byte[] data, boolean expires) {
        return put(context, profileId, objectTypeName, objectKey + "", data, expires);
    }

    public static StoredObject put(Context context, long profileId, String objectTypeName, String objectKey, byte[] data) {
        return put(context, profileId, objectTypeName, objectKey, data, true);
    }

    public static StoredObject put(Context context, long profileId, String objectTypeName, String objectKey, byte[] data, boolean expires) {
        Log.v(TAG, "put(" + profileId + ", " + objectTypeName + ", " + objectKey + ", byte[] data, " + expires + ")");
        // Log.v(TAG, "put3(" + objectTypeName + "/" + objectKey + ")");
        StoredObject result = get(context, profileId, objectTypeName, objectKey);

        if (result != null) {
            result.setData(data);
            result = result.save(context);
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

        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            sha1.update(data, 0, data.length);
            v.put(Column.HASH.getName(), sha1.digest());
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        long id = -1;
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = ObjectStoreSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                id = db.insert(ObjectStoreSqlHelper.TABLE_NAME, null, v);
                if (id != -1)
                    addWriting(id);
            } finally {
                if (db != null) db.close();
            }
        }

        threadSleep(); // for testing

        if (id != -1) {
            result = get(context, id, false);
            removeWriting(id);
            return result;
        }

        return null;
    }

    public static List<String> objectTypes(Context context, long profileId) {
        Log.v(TAG, "list(" + profileId + ")");
        List<StoredObject> list = new LinkedList<>();

        synchronized (TAG) {
            ObjectStoreSqlHelper helper = ObjectStoreSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.query(
                            ObjectStoreSqlHelper.TABLE_NAME,
                            ObjectStoreSqlHelper.getColumnNames(),
                            Column.PROFILE_ID + "=?",
                            new String[]{profileId + ""},
                            null, null, null);

                    while (cursor.moveToNext()) {
                        list.add(new StoredObject(cursor));
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }

        Set<String> set = new HashSet<>();
        for (StoredObject obj : list) {
            if (isWriting(obj._id)) {
                obj = get(context, obj._id);
            }
            set.add(obj.getObjName());
        }

        for (String str : set) {
            Log.v(TAG, str);
        }

        return new LinkedList<>(set);
    }

    public static List<StoredObject> list(Context context) {
        Log.v(TAG, "list()");
        List<StoredObject> list = new LinkedList<>();

        synchronized (TAG) {
            ObjectStoreSqlHelper helper = ObjectStoreSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.query(
                            ObjectStoreSqlHelper.TABLE_NAME,
                            ObjectStoreSqlHelper.getColumnNames(),
                            null, null, null, null, null);

                    while (cursor.moveToNext()) {
                        list.add(new StoredObject(cursor));
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }

        List<StoredObject> newList = new LinkedList<>();
        for (StoredObject obj : list) {
            if (isWriting(obj._id)) {
                obj = get(context, obj._id);
            }
            newList.add(obj);
        }

        return newList;
    }

    public static List<StoredObject> list(Context context, long profileId, String objectTypeName) {
        Log.v(TAG, "list(" + profileId + ", " + objectTypeName + ")");
        List<StoredObject> list = new LinkedList<>();

        synchronized (TAG) {
            ObjectStoreSqlHelper helper = ObjectStoreSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.query(
                            ObjectStoreSqlHelper.TABLE_NAME,
                            ObjectStoreSqlHelper.getColumnNames(),
                            Column.PROFILE_ID + "=? AND " + Column.OBJ_NAME + "=?",
                            new String[]{profileId + "", objectTypeName},
                            null, null, null);

                    while (cursor.moveToNext()) {
                        list.add(new StoredObject(cursor));
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }

        List<StoredObject> newList = new LinkedList<>();
        for (StoredObject obj : list) {
            if (isWriting(obj._id)) {
                obj = get(context, obj._id);
            }
            newList.add(obj);
        }

        return newList;
    }

    public static List<StoredObject> list(Context context, long profileId, String objectTypeName, String[] keys) {
        Log.v(TAG, "list(" + profileId + ", " + objectTypeName + ", String[] keys)");
        List<StoredObject> list = new LinkedList<>();

        if (keys == null || keys.length == 0)
            return list;

        synchronized (TAG) {
            ObjectStoreSqlHelper helper = ObjectStoreSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                String[] param = new String[keys.length + 2];
                param[0] = profileId + "";
                param[1] = objectTypeName;

                System.arraycopy(keys, 0, param, 1, keys.length);

                Cursor cursor = null;
                try {
                    cursor = db.query(
                            ObjectStoreSqlHelper.TABLE_NAME,
                            ObjectStoreSqlHelper.getColumnNames(),
                            Column.PROFILE_ID + "=? AND "
                                    + Column.OBJ_NAME + "=? AND "
                                    + Column.OBJ_KEY
                                    + " IN (" + makePlaceholders(keys.length) + ")",
                            param,
                            null, null, null);
                    while (cursor.moveToNext()) {
                        list.add(new StoredObject(cursor));
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }

        List<StoredObject> newList = new LinkedList<>();
        for (StoredObject obj : list) {
            if (isWriting(obj._id)) {
                obj = get(context, obj._id);
            }
            newList.add(obj);
        }

        return list;
    }

    public static void flush(Context context, long deathAge) {
        Log.v(TAG, "flush(" + deathAge + ")");
        List<StoredObject> list = new LinkedList<>();
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = ObjectStoreSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = null;
                try {
                    cursor = db.query(
                            ObjectStoreSqlHelper.TABLE_NAME,
                            ObjectStoreSqlHelper.getColumnNames(),
                            Column.LAST_UPDATED + " < ? AND " + Column.EXPIRES + " = ?",
                            new String[]{(System.currentTimeMillis() - deathAge) + "", "1"},
                            null, null, null);

                    while (cursor.moveToNext()) {
                        list.add(new StoredObject(cursor));
                    }
                } catch (IllegalStateException e) {
                    // Do nothing, all delete of what we have so far
                } finally {
                    if (cursor != null) cursor.close();
                }
            } finally {
                if (db != null) db.close();
            }
        }
        delete(context, list);
    }

    public static boolean flushAllOfType(Context context, String objectTypeName) {
        Stopwatch stopwatch = new Stopwatch(true);
        boolean success = false;
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = ObjectStoreSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                success = db.delete(
                        ObjectStoreSqlHelper.TABLE_NAME,
                        Column.OBJ_NAME + " = ?",
                        new String[]{objectTypeName}) > 0;
            } finally {
                if (db != null) db.close();
            }
        }
        Log.v(TAG, "flushAllOfType " + objectTypeName + " " + stopwatch.finish() + "ms");
        return success;
    }

    public static boolean delete(Context context, long profileId, String objectTypeName, long objkey) {
        return delete(context, profileId, objectTypeName, objkey + "");
    }

    public static boolean delete(Context context, long profileId, String objectTypeName, String objkey) {
        Log.v(TAG, "delete(" + profileId + ", " + objectTypeName + ", " + objkey + ")");
        StoredObject obj = get(context, profileId, objectTypeName, objkey);
        if (obj != null && obj._isFile && obj._file != null) {
            obj._file.delete();
        }

        boolean success = false;
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = ObjectStoreSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                success = db.delete(
                        ObjectStoreSqlHelper.TABLE_NAME,
                        Column.PROFILE_ID + "=? AND "
                                + Column.OBJ_NAME + "=? AND "
                                + Column.OBJ_KEY + "=?",
                        new String[]{profileId + "", objectTypeName, objkey}) > 0;
            } finally {
                if (db != null) db.close();
            }
        }
        return success;
    }

    public static boolean delete(Context context, long id) {
        Log.v(TAG, "delete(" + id + ")");
        StoredObject obj = get(context, id);
        if (obj != null && obj._isFile && obj._file != null) {
            obj._file.delete();
        }

        boolean success = false;
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = ObjectStoreSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                success = db.delete(
                        ObjectStoreSqlHelper.TABLE_NAME,
                        Column.ID + "=?",
                        new String[]{id + ""}) > 0;
            } finally {
                if (db != null) db.close();
            }
        }
        return success;
    }

    public static boolean delete(Context context, StoredObject obj) {
        Log.v(TAG, "delete(" + obj + ")");
        if (obj != null && obj._file != null && obj._isFile) {
            obj._file.delete();
        }

        boolean success = false;
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = ObjectStoreSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                success = db.delete(
                        ObjectStoreSqlHelper.TABLE_NAME,
                        Column.ID + "=?",
                        new String[]{obj.getId() + ""}) > 0;
            } finally {
                if (db != null) db.close();
            }
        }
        return success;
    }

    public static boolean delete(Context context, List<StoredObject> list) {
        if (list.size() == 0)
            return true;

        Log.v(TAG, "delete(" + list.size() + ")");

        for (StoredObject obj : list) {
            if (obj != null && obj._isFile && obj._file != null) {
                obj._file.delete();
            }
        }
        Stopwatch stopwatch = new Stopwatch(true);
        synchronized (TAG) {
            ObjectStoreSqlHelper helper = ObjectStoreSqlHelper.getInstance(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.beginTransaction();
            try {
                for (int i = 0; i < list.size(); i++) {
                    db.delete(
                            ObjectStoreSqlHelper.TABLE_NAME,
                            Column.ID + " = " + list.get(i).getId(), null);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
                db.close();
            }
        }
        Log.v(TAG, "Delete time: " + stopwatch.finish());
        return true;
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

    public static String getStoragePath(Context context) {
        File externalPath = Environment.getExternalStorageDirectory();
        String packageName = context.getPackageName();
        File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName);
        temppath.mkdirs();
        return temppath.getAbsolutePath();
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<StoredObject> CREATOR = new Creator<StoredObject>() {
        @Override
        public StoredObject createFromParcel(Parcel source) {
            return new StoredObject(source.readBundle(getClass().getClassLoader()));
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
