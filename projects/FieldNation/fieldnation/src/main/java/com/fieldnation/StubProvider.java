package com.fieldnation;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.fieldnation.Log;


public class StubProvider extends ContentProvider {
	private static final String TAG = "rpc.server.StubProvider";

	@Override
	public boolean onCreate() {
		Log.v(TAG, "Method Stub: onCreate()");
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.v(TAG, "Method Stub: query()");
		return null;
	}

	@Override
	public String getType(Uri uri) {
		Log.v(TAG, "Method Stub: getType()");
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.v(TAG, "Method Stub: insert()");
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.v(TAG, "Method Stub: delete()");
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		Log.v(TAG, "Method Stub: update()");
		return 0;
	}

}
