package com.fieldnation.authserver.db;

import java.security.MessageDigest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteFullException;
import android.util.Log;

import com.fieldnation.utils.misc;

/**
 * These are database backed persistent objects. All changes made to them will
 * be saved as the changes are made.
 * 
 * @author michael.carver
 * 
 */
public class AuthCache {
	private static final String TAG = "auth.db.AuthCache";

	private Context _context;
	private long _id;
	private String _username;
	private String _password = "";
	private String _sessionHash = "";
	private long _sessionExpiry = 0;
	private String _oAuthBlob = "";
	private String _requestBlob = "";

	private AuthCache(Context context, Cursor src) {
		_context = context;
		_id = src.getLong(0);
		_username = src.getString(AuthCacheSqlHelper.Column.USERNAME.getIndex());
		_password = src.getString(AuthCacheSqlHelper.Column.PASSWORD.getIndex());
		_sessionHash = src.getString(AuthCacheSqlHelper.Column.SESSION_HASH.getIndex());
		_oAuthBlob = src.getString(AuthCacheSqlHelper.Column.OAUTH_BLOB.getIndex());
		_requestBlob = src.getString(AuthCacheSqlHelper.Column.REQUEST_BLOB.getIndex());
		_sessionExpiry = src.getLong(AuthCacheSqlHelper.Column.SESSION_EXPIRY.getIndex());

		Log.v(TAG, _username);
		Log.v(TAG, _requestBlob);
		Log.v(TAG, _oAuthBlob);
	}

	private AuthCache(Context context, String username, String password) {
		_context = context;
		_username = username;
		_password = password;
	}

	/*-			Password		-*/
	public void setPassword(String password) {
		_password = password;
		save();
	}

	public String getPassword() {
		return _password;
	}

	public boolean validatePassword(String password) {
		try {
			return _password.equals(password);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/*-				Sessions				-*/
	public boolean validateSessionHash(String sessionHash) {
		if (_sessionHash.equals("")) {
			Log.v(TAG, "no session");
			return false;
		}

		if (_sessionExpiry < System.currentTimeMillis()) {
			Log.v(TAG, "session expired");
			invalidateSession();
			return false;
		}

		Log.d(TAG, _sessionHash);

		return _sessionHash.equals(sessionHash);
	}

	public void invalidateSession() {
		_sessionHash = "";
		_sessionExpiry = 0;
		save();
	}

	public String startSession(String password, long expiryInSeconds) {
		if (!validatePassword(password))
			return null;

		_sessionHash = generateSessionHash(password);
		// TODO, need a better clock than this, this can be thwarted by manually
		// setting the clock on a phone
		_sessionExpiry = System.currentTimeMillis() + expiryInSeconds * 1000;
		save();

		return _sessionHash;
	}

	/*-			AuthBlob		-*/
	public void setOAuthBlob(String oAuthBlob) {
		_oAuthBlob = oAuthBlob;
		save();
	}

	public String getOAuthBlob() {
		return _oAuthBlob;
	}

	/*-			RequestBlob		-*/
	public void setRequestBlob(String requestBlob) {
		_requestBlob = requestBlob;
		save();
	}

	public String getRequestBlob() {
		return _requestBlob;
	}

	/*-			Hashing Utility		-*/
	private String generateSessionHash(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			// TODO, salt this hash
			return misc.getHex(md.digest(password.getBytes()));
		} catch (Exception ex) {
			// TODO should never happen
			System.exit(1);
			return null;
		}
	}

	/*-			Database wrapper		-*/
	private void save() {
		save(_context, this);
	}

	/*-*****************************************-*/
	/*-				Accessor Stuff				-*/
	/*-*****************************************-*/

	public static AuthCache get(Context context, String username) {
		AuthCacheSqlHelper helper = new AuthCacheSqlHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			Cursor cursor = db.query(AuthCacheSqlHelper.TABLE_NAME,
					AuthCacheSqlHelper.getColumnNames(),
					AuthCacheSqlHelper.Column.USERNAME + "=?",
					new String[] { username }, null, null, null);

			try {
				if (cursor.moveToFirst()) {
					return new AuthCache(context, cursor);
				}

				return null;
			} finally {
				cursor.close();
			}
		} finally {
			helper.close();
		}
	}

	private static AuthCache get(Context context, SQLiteDatabase db, long id) {
		Cursor cursor = db.query(AuthCacheSqlHelper.TABLE_NAME,
				AuthCacheSqlHelper.getColumnNames(),
				AuthCacheSqlHelper.Column.ID + "=" + id, null, null, null, null);

		try {
			if (cursor.moveToFirst()) {
				return new AuthCache(context, cursor);
			}

			return null;
		} finally {
			cursor.close();
		}
	}

	public static AuthCache create(Context context, String username,
			String password) {
		AuthCacheSqlHelper helper = new AuthCacheSqlHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			AuthCache authCache = null;

			authCache = get(context, username);

			if (authCache != null) {
				return null;
			}

			authCache = new AuthCache(context, username, password);

			ContentValues values = new ContentValues();
			values.put(AuthCacheSqlHelper.Column.USERNAME.getName(),
					authCache._username);
			values.put(AuthCacheSqlHelper.Column.PASSWORD.getName(),
					authCache._password);
			values.put(AuthCacheSqlHelper.Column.OAUTH_BLOB.getName(),
					authCache._oAuthBlob);
			values.put(AuthCacheSqlHelper.Column.SESSION_HASH.getName(),
					authCache._sessionHash);
			values.put(AuthCacheSqlHelper.Column.REQUEST_BLOB.getName(),
					authCache._requestBlob);
			values.put(AuthCacheSqlHelper.Column.SESSION_EXPIRY.getName(),
					authCache._sessionExpiry);

			return get(context, db,
					db.insert(AuthCacheSqlHelper.TABLE_NAME, null, values));
		} finally {
			helper.close();
		}
	}

	private static void save(Context context, AuthCache authCache) {
		AuthCacheSqlHelper helper = new AuthCacheSqlHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put(AuthCacheSqlHelper.Column.USERNAME.getName(),
					authCache._username);
			values.put(AuthCacheSqlHelper.Column.PASSWORD.getName(),
					authCache._password);
			values.put(AuthCacheSqlHelper.Column.OAUTH_BLOB.getName(),
					authCache._oAuthBlob);
			values.put(AuthCacheSqlHelper.Column.SESSION_HASH.getName(),
					authCache._sessionHash);
			values.put(AuthCacheSqlHelper.Column.REQUEST_BLOB.getName(),
					authCache._requestBlob);
			values.put(AuthCacheSqlHelper.Column.SESSION_EXPIRY.getName(),
					authCache._sessionExpiry);

			db.update(AuthCacheSqlHelper.TABLE_NAME, values,
					AuthCacheSqlHelper.Column.ID + "=" + authCache._id, null);
		} finally {
			helper.close();
		}
	}
}
