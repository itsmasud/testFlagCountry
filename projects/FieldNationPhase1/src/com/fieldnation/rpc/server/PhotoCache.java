package com.fieldnation.rpc.server;

import android.content.Context;
import android.util.Log;

public class PhotoCache {
	private static final String TAG = "rpc.server.PhotoCache";

	public static PhotoCacheNode query(Context context, String url) {
		PhotoCacheNode node = PhotoCacheNode.get(context, url);
		if (node != null) {
			Log.v(TAG, "hit!");
			return node;
		}
		Log.v(TAG, "miss!");
		return null;
	}

	public static void store(Context context, String url, byte[] photoData) {
		PhotoCacheNode.put(context, url, photoData);
	}

}
