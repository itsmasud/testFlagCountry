package com.fieldnation.rpc.server;

import com.fieldnation.rpc.server.PhotoCacheSqlHelper.Column;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PhotoCacheNode {
	private static final String TAG = "rpc.server.PhotoCacheNode";
	private Context _context;
	private long _id;
	private long _lastViewed;
	private String _url;
	private byte[] _photoData;

	private PhotoCacheNode(Context context, Cursor cursor) {
		_context = context;
		_id = cursor.getLong(Column.ID.getIndex());
		_lastViewed = cursor.getLong(Column.LAST_READ.getIndex());
		_url = cursor.getString(Column.URL.getIndex());
		_photoData = cursor.getBlob(Column.PHOTO_DATA.getIndex());
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

		// save
	}

	public byte[] getPhotoData() {
		return _photoData;
	}

	public String getUrl() {
		return _url;
	}

	public void save() {
		save(this);
	}

	public static PhotoCacheNode get(Context context, String url) {
		PhotoCacheSqlHelper helper = new PhotoCacheSqlHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();

		try {
			Cursor cursor = db.query(PhotoCacheSqlHelper.TABLE_NAME, DataCacheSqlHelper.getColumnNames(),
					Column.URL + "=?", new String[] { url }, null, null, null);
			try {
				if (cursor.moveToFirst()) {
					PhotoCacheNode node = new PhotoCacheNode(context, cursor);

					node.setLastViewed(System.currentTimeMillis());

					return node;
				}

				return null;
			} finally {
				cursor.close();
			}

		} finally {
			helper.close();
		}
	}

	public static void put(Context context, String url, byte[] photoData) {
		PhotoCacheSqlHelper helper = new PhotoCacheSqlHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			PhotoCacheNode node = get(context, url);

			if (node != null) {
				node._lastViewed = System.currentTimeMillis();
				node._photoData = photoData;

				node.save();
				return;
			}

			ContentValues values = new ContentValues();
			values.put(Column.LAST_READ.getName(), System.currentTimeMillis());
			values.put(Column.PHOTO_DATA.getName(), photoData);
			values.put(Column.URL.getName(), url);

			db.insert(PhotoCacheSqlHelper.TABLE_NAME, null, values);
		} finally {
			helper.close();
		}
	}

	public static void save(PhotoCacheNode node) {
		PhotoCacheSqlHelper helper = new PhotoCacheSqlHelper(node._context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put(Column.LAST_READ.getName(), node._lastViewed);
			values.put(Column.PHOTO_DATA.getName(), node._photoData);
			values.put(Column.URL.getName(), node._url);
			db.update(PhotoCacheSqlHelper.TABLE_NAME, values, Column.ID + "=" + node._id, null);
		} finally {
			helper.close();
		}
	}

	public static void delete(Context context, long id) {
		PhotoCacheSqlHelper helper = new PhotoCacheSqlHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			db.delete(PhotoCacheSqlHelper.TABLE_NAME, Column.ID + "=" + id, null);
		} finally {
			helper.close();
		}
	}

	public static void flush(Context context) {
		PhotoCacheSqlHelper helper = new PhotoCacheSqlHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			Log.v(TAG,
					"Flushed " + db.delete(PhotoCacheSqlHelper.TABLE_NAME,
							Column.LAST_READ + "<" + (System.currentTimeMillis() - 604800000), null));
		} finally {
			helper.close();
		}

	}
}
