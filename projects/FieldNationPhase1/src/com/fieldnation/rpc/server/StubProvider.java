package com.fieldnation.rpc.server;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class StubProvider extends ContentProvider {
	private static final String TAG = "service.StubProvider";

	@Override
	public boolean onCreate() {
		// TODO Method Stub: onCreate()
		Log.v(TAG, "Method Stub: onCreate()");
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Method Stub: query()
		Log.v(TAG, "Method Stub: query()");
		return null;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Method Stub: getType()
		Log.v(TAG, "Method Stub: getType()");
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Method Stub: insert()
		Log.v(TAG, "Method Stub: insert()");
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Method Stub: delete()
		Log.v(TAG, "Method Stub: delete()");
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Method Stub: update()
		Log.v(TAG, "Method Stub: update()");
		return 0;
	}

}
