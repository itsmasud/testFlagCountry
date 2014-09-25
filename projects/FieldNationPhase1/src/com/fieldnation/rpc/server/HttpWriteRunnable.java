package com.fieldnation.rpc.server;

import com.fieldnation.rpc.common.WebServiceConstants;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class HttpWriteRunnable extends HttpRunnable implements WebServiceConstants {
	private static final String TAG = "rpc.server.HttpWriteRunnable";

	public HttpWriteRunnable(Context context, Intent intent, OAuth at) {
		super(context, intent, at);
	}

	@Override
	public void run() {
		Bundle bundle = _intent.getExtras();
		String method = bundle.getString(KEY_PARAM_METHOD);
		String path = bundle.getString(KEY_PARAM_PATH);
		String options = bundle.getString(KEY_PARAM_OPTIONS);
		String contentType = bundle.getString(KEY_PARAM_CONTENT_TYPE);
		byte[] data = bundle.getByteArray(KEY_PARAM_DATA);
		boolean allowCache = bundle.getBoolean(KEY_ALLOW_CACHE);

		if (path.contains("messages/new")) {
			Log.v(TAG, "BP");
		}

		if (bundle.containsKey(KEY_PARAM_CALLBACK)) {
			ResultReceiver rr = bundle.getParcelable(KEY_PARAM_CALLBACK);

			DataCacheNode cachedData = null;

			if (allowCache)
				cachedData = DataCache.query(_context, _auth, bundle);

			if (cachedData != null) {
				bundle.putByteArray(KEY_RESPONSE_DATA, cachedData.getResponseData());
				bundle.putInt(KEY_RESPONSE_CODE, cachedData.getResponseCode());
				bundle.putBoolean(KEY_RESPONSE_CACHED, true);
			} else {
				Ws ws = new Ws(_auth);
				Result result = null;
				try {
					result = ws.httpReadWrite(method, path, options, data, contentType);

					if (result.getResponseCode() / 100 != 2) {
						Log.v(TAG, "Error response: " + result.getResponseCode());
						bundle.putInt(KEY_RESPONSE_CODE, result.getResponseCode());
						bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_HTTP_ERROR);
						bundle.putString(KEY_RESPONSE_ERROR, result.getResponseMessage());

					} else {
						try {
							// happy path
							bundle.putByteArray(KEY_RESPONSE_DATA, result.getResultsAsByteArray());
							bundle.putInt(KEY_RESPONSE_CODE, result.getResponseCode());
							bundle.putBoolean(KEY_RESPONSE_CACHED, false);
							bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_NONE);
							DataCache.store(_context, _auth, bundle, bundle.getByteArray(KEY_RESPONSE_DATA),
									bundle.getInt(KEY_RESPONSE_CODE));
							Log.v(TAG, "web request success");
						} catch (Exception ex) {
							try {
								// unhappy, but http error
								bundle.putInt(KEY_RESPONSE_CODE, result.getResponseCode());
								bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_HTTP_ERROR);
								bundle.putString(KEY_RESPONSE_ERROR, result.getResponseMessage());
							} catch (Exception ex1) {
								// sad path
								bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_UNKNOWN);
								bundle.putString(KEY_RESPONSE_ERROR, ex1.getMessage());
							}
						}
					}

				} catch (Exception ex) {
					Log.v(TAG, "web request fail");
					bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_UNKNOWN);
					bundle.putString(KEY_RESPONSE_ERROR, ex.getMessage());
					if (result != null) {
						bundle.putLong(KEY_RESPONSE_CODE, result.getResponseCode());
					}
				}
			}

			rr.send(bundle.getInt(KEY_RESULT_CODE), bundle);
		}
	}

}
