package com.fieldnation.rpc.server;

import java.util.Hashtable;

import com.fieldnation.webapi.OAuth;

import android.os.Bundle;
import android.util.Log;

/**
 * A really simple and dumb cache
 * 
 * @author michael.carver
 * 
 */
public class DataCache {
	private static final String TAG = "service.DataCache";

	private static Hashtable<String, Bundle> _cache = new Hashtable<String, Bundle>();

	public static Bundle query(OAuth at, Bundle bundle) {
		String hashdata = hashBundle(at, bundle);
		if (_cache.containsKey(hashdata)) {
			Log.v(TAG, "hit!");
			return _cache.get(hashdata);
		}
		Log.v(TAG, "miss!");
		return null;
	}

	public static void store(OAuth at, Bundle source, Bundle result) {
		String hashdata = hashBundle(at, source);

		_cache.put(hashdata, result);
	}

	private static String hashBundle(OAuth at, Bundle bundle) {
		String hashdata = bundle.getString("METHOD");

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
