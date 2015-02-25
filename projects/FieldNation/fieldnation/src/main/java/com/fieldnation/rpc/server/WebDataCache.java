package com.fieldnation.rpc.server;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.Log;


/**
 * A really simple and dumb cache
 * 
 * @author michael.carver
 * 
 */
public class WebDataCache {
	private static final String TAG = "rpc.server.WebDataCache";

	public static WebDataCacheNode query(Context context, AuthToken at, Bundle bundle) {
		String hashdata = hashBundle(at, bundle);
		WebDataCacheNode node = WebDataCacheNode.get(context, hashdata);
		if (node != null) {
			Log.v(TAG, "hit!");
			return node;
		}
		Log.v(TAG, "miss!");
		return null;
	}

	public static void store(Context context, AuthToken at, Bundle source, byte[] responseData, int responseCode) {
		String hashdata = hashBundle(at, source);

		WebDataCacheNode.put(context, hashdata, responseData, responseCode);
	}

	private static String hashBundle(AuthToken at, Bundle bundle) {
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
