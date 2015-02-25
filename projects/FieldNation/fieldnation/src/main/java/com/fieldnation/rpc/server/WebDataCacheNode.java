package com.fieldnation.rpc.server;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.fieldnation.Log;
import com.fieldnation.rpc.server.WebDataCacheSqlHelper.Column;

public class WebDataCacheNode {
    private static final String TAG = "rpc.server.WebDataCacheNode";
    private static final long EXPIRATION_TIMEOUT = 60 * 60 * 1000; // 1 hour
    private Context _context;
    private long _id;
    private long _expiresOn;
    private String _key;
    private byte[] _responseData;
    private int _responseCode;

    private WebDataCacheNode(Context context, Cursor cursor) {
        _context = context.getApplicationContext();
        _id = cursor.getLong(Column.ID.getIndex());
        _expiresOn = cursor.getLong(Column.EXIPES_ON.getIndex());
        _key = cursor.getString(Column.KEY.getIndex());
        _responseData = cursor.getBlob(Column.RESPONSE_DATA.getIndex());
        _responseCode = cursor.getInt(Column.RESPONSE_CODE.getIndex());
    }

    public long getId() {
        return _id;
    }

    public long getExpiresOn() {
        return _expiresOn;
    }

    public byte[] getResponseData() {
        return _responseData;
    }

    public int getResponseCode() {
        return _responseCode;
    }

    public void save() {
        save(this);
    }

    public static WebDataCacheNode get(Context context, String key) {
        WebDataCacheSqlHelper helper = new WebDataCacheSqlHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        WebDataCacheNode node = null;
        try {
            Cursor cursor = db.query(WebDataCacheSqlHelper.TABLE_NAME, WebDataCacheSqlHelper.getColumnNames(),
                    WebDataCacheSqlHelper.Column.KEY + "=?", new String[]{key}, null, null, null);
            try {
                if (cursor.moveToFirst()) {
                    // hit
                    node = new WebDataCacheNode(context, cursor);
                }
            } finally {
                cursor.close();
            }
        } finally {
            helper.close();
        }
        if (node == null)
            return null;

        // expired
        if (node.getExpiresOn() < System.currentTimeMillis()) {
            delete(context, node.getId());
            return null;
        }

        return node;
    }

    public static void put(Context context, String key, byte[] responseData, int responseCode) {
        WebDataCacheNode node = get(context, key);
        if (node != null) {
            node._expiresOn = System.currentTimeMillis() + EXPIRATION_TIMEOUT;
            node._responseData = responseData;
            node._responseCode = responseCode;

            node.save();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(Column.EXIPES_ON.getName(), System.currentTimeMillis() + EXPIRATION_TIMEOUT);
        values.put(Column.KEY.getName(), key);
        values.put(Column.RESPONSE_DATA.getName(), responseData);
        values.put(Column.RESPONSE_CODE.getName(), responseCode);

        WebDataCacheSqlHelper helper = new WebDataCacheSqlHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.insert(WebDataCacheSqlHelper.TABLE_NAME, null, values);
        } finally {
            helper.close();
        }
    }

    public static void save(WebDataCacheNode node) {
        ContentValues values = new ContentValues();
        values.put(Column.EXIPES_ON.getName(), node._expiresOn);
        values.put(Column.KEY.getName(), node._key);
        values.put(Column.RESPONSE_DATA.getName(), node._responseData);
        values.put(Column.RESPONSE_CODE.getName(), node._responseCode);

        WebDataCacheSqlHelper helper = new WebDataCacheSqlHelper(node._context);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.update(WebDataCacheSqlHelper.TABLE_NAME, values, Column.ID + "=" + node._id, null);
        } finally {
            helper.close();
        }
    }

    public static void delete(Context context, long id) {
        WebDataCacheSqlHelper helper = new WebDataCacheSqlHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.delete(WebDataCacheSqlHelper.TABLE_NAME, Column.ID + "=" + id, null);
        } finally {
            helper.close();
        }

    }

    public static void flush(Context context) {
        WebDataCacheSqlHelper helper = new WebDataCacheSqlHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            Log.v(TAG,
                    "Flushed " + db.delete(WebDataCacheSqlHelper.TABLE_NAME,
                            Column.EXIPES_ON + "<" + System.currentTimeMillis(), null) + " cached data");
        } finally {
            helper.close();
        }
    }
}
