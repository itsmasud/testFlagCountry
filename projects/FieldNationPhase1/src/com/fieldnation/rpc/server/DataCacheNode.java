package com.fieldnation.rpc.server;

import com.fieldnation.rpc.server.DataCacheSqlHelper.Column;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataCacheNode {
	private static final String TAG = "rpc.server.DataCacheNode";
	private static final long EXPIRATION_TIMEOUT = 60 * 60 * 1000; // 1 hour
	private Context _context;
	private long _id;
	private long _expiresOn;
	private String _key;
	private byte[] _responseData;
	private int _responseCode;

	private DataCacheNode(Context context, Cursor cursor) {
		_context = context;
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

	public static DataCacheNode get(Context context, String key) {
		DataCacheSqlHelper helper = new DataCacheSqlHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();

		try {
			Cursor cursor = db.query(DataCacheSqlHelper.TABLE_NAME, DataCacheSqlHelper.getColumnNames(),
					DataCacheSqlHelper.Column.KEY + "=?", new String[] { key }, null, null, null);
			try {
				if (cursor.moveToFirst()) {
					// hit
					DataCacheNode node = new DataCacheNode(context, cursor);

					// expired
					if (node.getExpiresOn() < System.currentTimeMillis()) {
						delete(context, node.getId());
						return null;
					}
					// ok
					return node;
				}
				// miss
				return null;
			} finally {
				cursor.close();
			}
		} finally {
			helper.close();
		}
	}

	public static void put(Context context, String key, byte[] responseData, int responseCode) {
		DataCacheSqlHelper helper = new DataCacheSqlHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			DataCacheNode node = get(context, key);

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

			db.insert(DataCacheSqlHelper.TABLE_NAME, null, values);
		} finally {
			helper.close();
		}
	}

	public static void save(DataCacheNode node) {
		DataCacheSqlHelper helper = new DataCacheSqlHelper(node._context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put(Column.EXIPES_ON.getName(), node._expiresOn);
			values.put(Column.KEY.getName(), node._key);
			values.put(Column.RESPONSE_DATA.getName(), node._responseData);
			values.put(Column.RESPONSE_CODE.getName(), node._responseCode);

			db.update(DataCacheSqlHelper.TABLE_NAME, values, Column.ID + "=" + node._id, null);
		} finally {
			helper.close();
		}
		;
	}

	public static void delete(Context context, long id) {
		DataCacheSqlHelper helper = new DataCacheSqlHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			db.delete(DataCacheSqlHelper.TABLE_NAME, Column.ID + "=" + id, null);
		} finally {
			helper.close();
		}

	}

	public static void flush(Context context) {
		DataCacheSqlHelper helper = new DataCacheSqlHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			Log.v(TAG,
					"Flushed " + db.delete(DataCacheSqlHelper.TABLE_NAME,
							Column.EXIPES_ON + "<" + System.currentTimeMillis(), null) + " cached data");
		} finally {
			helper.close();
		}
	}
}
