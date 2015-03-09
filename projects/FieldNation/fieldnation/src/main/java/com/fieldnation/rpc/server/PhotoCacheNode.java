package com.fieldnation.rpc.server;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fieldnation.Log;
import com.fieldnation.rpc.server.PhotoCacheSqlHelper.Column;

import java.io.ByteArrayOutputStream;

public class PhotoCacheNode {
    private static final String TAG = "rpc.server.PhotoCacheNode";

    private static final long ONE_WEEK = 604800000;

//    private static final Hashtable<String, PhotoCacheNode> _cache = new Hashtable<String, PhotoCacheNode>();

    private Context _context;
    private long _id;
    private long _lastViewed;
    private String _url;
    private Bitmap _photoData;
    private Bitmap _circleData;

    private PhotoCacheNode(Context context, Cursor cursor) {
        _context = context.getApplicationContext();
        _id = cursor.getLong(Column.ID.getIndex());
        _lastViewed = cursor.getLong(Column.LAST_READ.getIndex());
        _url = cursor.getString(Column.URL.getIndex());

        byte[] data = cursor.getBlob(Column.PHOTO_DATA.getIndex());
        _photoData = BitmapFactory.decodeByteArray(data, 0, data.length);

        data = cursor.getBlob(Column.CIRCLE_DATA.getIndex());
        _circleData = BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public Context getContext() {
        return _context;
    }

    public long getId() {
        return _id;
    }

    public long getLastViewed() {
        return _lastViewed;
    }

    public void setLastViewed(long utcMilliseconds) {
        _lastViewed = utcMilliseconds;
        save();
    }

    public Bitmap getPhotoData() {
        return _photoData;
    }

    public Bitmap getCircleData() {
        return _circleData;
    }

    public String getUrl() {
        return _url;
    }

    public void save() {
        save(this);
    }

    public static PhotoCacheNode get(Context context, String url) {
        if (url == null)
            return null;

        PhotoCacheSqlHelper helper = new PhotoCacheSqlHelper(context);
        PhotoCacheNode node = null;
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            try {
                Cursor cursor = db.query(PhotoCacheSqlHelper.TABLE_NAME, PhotoCacheSqlHelper.getColumnNames(),
                        Column.URL + "=?", new String[]{url}, null, null, null);
                try {
                    if (cursor.moveToFirst()) {
                        node = new PhotoCacheNode(context, cursor);
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
        if (node != null) {
            node.setLastViewed(System.currentTimeMillis());
        }
        return node;
    }

    public static void put(Context context, String url, Bitmap photoData, Bitmap circleData) {
        PhotoCacheNode node = get(context, url);

        if (node != null) {
            node._lastViewed = System.currentTimeMillis();
            node._photoData = photoData;
            node._circleData = circleData;

            node.save();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(Column.LAST_READ.getName(), System.currentTimeMillis());
        values.put(Column.URL.getName(), url);

        ByteArrayOutputStream bout;
        try {
            bout = new ByteArrayOutputStream();
            photoData.compress(Bitmap.CompressFormat.PNG, 100, bout);
            bout.flush();
            values.put(Column.PHOTO_DATA.getName(), bout.toByteArray());
            bout.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            bout = new ByteArrayOutputStream();
            circleData.compress(Bitmap.CompressFormat.PNG, 100, bout);
            bout.flush();
            values.put(Column.CIRCLE_DATA.getName(), bout.toByteArray());
            bout.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        PhotoCacheSqlHelper helper = new PhotoCacheSqlHelper(context);
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                db.insert(PhotoCacheSqlHelper.TABLE_NAME, null, values);
            } finally {
                db.close();
            }
        } finally {
            helper.close();
        }
    }

    public static void save(PhotoCacheNode node) {
        ContentValues values = new ContentValues();
        values.put(Column.LAST_READ.getName(), node._lastViewed);
        values.put(Column.URL.getName(), node._url);

        ByteArrayOutputStream bout;
        try {
            bout = new ByteArrayOutputStream();
            node._photoData.compress(Bitmap.CompressFormat.PNG, 100, bout);
            bout.flush();
            values.put(Column.PHOTO_DATA.getName(), bout.toByteArray());
            bout.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            bout = new ByteArrayOutputStream();
            node._circleData.compress(Bitmap.CompressFormat.PNG, 100, bout);
            bout.flush();
            values.put(Column.CIRCLE_DATA.getName(), bout.toByteArray());
            bout.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        PhotoCacheSqlHelper helper = new PhotoCacheSqlHelper(node._context);
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                db.update(PhotoCacheSqlHelper.TABLE_NAME, values, Column.ID + "=" + node._id, null);
            } finally {
                db.close();
            }
        } finally {
            helper.close();
        }
    }

    public static void delete(Context context, long id) {
//        _cache.clear();
        PhotoCacheSqlHelper helper = new PhotoCacheSqlHelper(context);
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                db.delete(PhotoCacheSqlHelper.TABLE_NAME, Column.ID + "=" + id, null);
            } finally {
                db.close();
            }
        } finally {
            helper.close();
        }
    }

    public static void flush(Context context) {
//        _cache.clear();
        PhotoCacheSqlHelper helper = new PhotoCacheSqlHelper(context);
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                Log.v(TAG,
                        "Flushed " + db.delete(PhotoCacheSqlHelper.TABLE_NAME,
                                Column.LAST_READ + "<" + (System.currentTimeMillis() - ONE_WEEK), null));
            } finally {
                db.close();
            }
        } finally {
            helper.close();
        }
    }
}
