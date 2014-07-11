package com.fieldnation.rpc.server;

import com.fieldnation.webapi.OAuth;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * A really simple and dumb cache
 * 
 * @author michael.carver
 * 
 */
public class DataCache {
	private static final String TAG = "rpc.server.DataCache";

	public static DataCacheNode query(Context context, OAuth at, Bundle bundle) {
		String hashdata = hashBundle(at, bundle);
		DataCacheNode node = DataCacheNode.get(context, hashdata);
		if (node != null) {
			Log.v(TAG, "hit!");
			return node;
		}
		Log.v(TAG, "miss!");
		return null;
	}

	public static void store(Context context, OAuth at, Bundle source, byte[] responseData, int responseCode) {
		String hashdata = hashBundle(at, source);

		DataCacheNode.put(context, hashdata, responseData, responseCode);
	}

	private static String hashBundle(OAuth at, Bundle bundle) {
		String hashdata = "";

		hashdata = bundle.getString("METHOD");
		hashdata += "." + at.getAccessToken();
		hashdata += "." + getIfThere(bundle, "PARAM_METHOD");
		hashdata += "." + getIfThere(bundle, "PARAM_PATH");
		hashdata += "." + getIfThere(bundle, "PARAM_OPTIONS");
		hashdata += "." + getIfThere(bundle, "PARAM_CONTENT_TYPE");

		if (bundle.containsKey("PARAM_DATA")) {
			hashdata += "." + new String(bundle.getByteArray("PARAM_DATA"));
		}

		return hashdata;
	}

	private static String getIfThere(Bundle bundle, String param) {
		if (bundle.containsKey(param) && bundle.get(param) != null)
			return bundle.getString(param);
		return "";
	}

}
