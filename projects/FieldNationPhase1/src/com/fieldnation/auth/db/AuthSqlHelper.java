package com.fieldnation.auth.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AuthSqlHelper extends SQLiteOpenHelper {

	public static final String TABLE_NAME = "users";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_USERNAME = "username";
	public static final String COLUMN_SECURITY_HASH = "securityHash";
	public static final String COLUMN_AUTH_TOKEN = "authToken";
	public static final String COLUMN_AUTH_EXPIRY = "authExipresOn";
	public static final String[] COLUMNS = { COLUMN_ID, COLUMN_USERNAME, COLUMN_SECURITY_HASH, COLUMN_AUTH_TOKEN, COLUMN_AUTH_EXPIRY };

	public AuthSqlHelper(Context context) {
		super(context, "auth.db", null, 3);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query = "";
		query += "CREATE TABLE users (";
		query += "_id integer primary key autoincrement,";
		query += "username text not null,";
		query += "securityHash text not null,";
		query += "authToken text not null,";
		query += "authExipresOn number not null";
		query += ");";
		db.execSQL(query);
		db.execSQL("CREATE INDEX users_username on users (username);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS users; ");
		onCreate(db);
	}

}
