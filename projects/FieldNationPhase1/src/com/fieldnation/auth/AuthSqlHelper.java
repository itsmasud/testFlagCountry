package com.fieldnation.auth;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AuthSqlHelper extends SQLiteOpenHelper {

	public AuthSqlHelper(Context context) {
		super(context, "auth.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query = "";
		query += "CREATE TABLE users (";
		query += "_id integer primary key autoincrement,";
		query += "username text not null,";
		query += "accountType text not null,";
		query += "securityHash text not null,";
		query += "authToken text not null,";
		query += "authExipresOn number not null);";
		db.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS users; ");
	    onCreate(db);
	}

}
