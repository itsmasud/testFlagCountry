package com.fieldnation.auth.db;

import java.security.NoSuchAlgorithmException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * An API that creates, and persists user objects
 * 
 * @author michael.carver
 * 
 */
public class UserDataSource {
	private AuthSqlHelper _helper;
	private SQLiteDatabase _db;

	public UserDataSource(Context context) {
		_helper = new AuthSqlHelper(context);
	}

	/**
	 * Opens the database connection
	 */
	public void open() {
		_db = _helper.getWritableDatabase();
	}

	/**
	 * Closes the connection to the database. Once called this object cannot be
	 * re-opened
	 */
	public void close() {
		_helper.close();
	}

	/**
	 * Looks up a user based on their username
	 * 
	 * @param username
	 * @return
	 */
	public User get(String username) {
		Cursor cursor = _db.query(AuthSqlHelper.TABLE_NAME,
				AuthSqlHelper.COLUMNS, AuthSqlHelper.COLUMN_USERNAME + "=?",
				new String[] { username }, null, null, null);

		try {
			if (cursor.moveToFirst()) {
				return new User(cursor);
			}

			return null;
		} finally {
			cursor.close();
		}
	}

	/**
	 * Looks up a user based on their ID
	 * 
	 * @param id
	 * @return
	 */
	public User get(long id) {
		Cursor cursor = _db.query(AuthSqlHelper.TABLE_NAME,
				AuthSqlHelper.COLUMNS, AuthSqlHelper.COLUMN_ID + "=" + id,
				null, null, null, null);

		try {
			if (cursor.moveToFirst()) {
				return new User(cursor);
			}

			return null;
		} finally {
			cursor.close();
		}
	}

	/**
	 * Creates the user and returns the user id
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public long create(String username, String password) throws Exception {
		User user = null;

		user = get(username);

		if (user != null) {
			throw new Exception("User already exists");
		}

		user = new User(username, password);

		ContentValues values = new ContentValues();
		values.put(AuthSqlHelper.COLUMN_USERNAME, user._username);
		values.put(AuthSqlHelper.COLUMN_SECURITY_HASH, user._securityHash);
		values.put(AuthSqlHelper.COLUMN_AUTH_TOKEN, user._authToken);
		values.put(AuthSqlHelper.COLUMN_AUTH_EXPIRY, user._authExpiresOn);

		return _db.insert(AuthSqlHelper.TABLE_NAME, null, values);
	}

	/**
	 * Saves the user to the database
	 * 
	 * @param data
	 */
	public void save(User data) {
		ContentValues values = new ContentValues();
		values.put(AuthSqlHelper.COLUMN_USERNAME, data._username);
		values.put(AuthSqlHelper.COLUMN_SECURITY_HASH, data._securityHash);
		values.put(AuthSqlHelper.COLUMN_AUTH_TOKEN, data._authToken);
		values.put(AuthSqlHelper.COLUMN_AUTH_EXPIRY, data._authExpiresOn);

		_db.update(AuthSqlHelper.TABLE_NAME, values, "_id=" + data._id, null);
	}
}
