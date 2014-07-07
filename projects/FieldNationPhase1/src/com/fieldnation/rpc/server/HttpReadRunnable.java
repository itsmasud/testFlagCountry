package com.fieldnation.rpc.server;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.webapi.OAuth;
import com.fieldnation.webapi.Result;
import com.fieldnation.webapi.Ws;

public class HttpReadRunnable extends HttpRunnable implements WebServiceConstants {
	private static final String TAG = "HttpReadRunnable";

	public HttpReadRunnable(Context context, Intent intent, OAuth at) {
		super(context, intent, at);
	}

	@Override
	public void run() {
		Bundle bundle = _intent.getExtras();
		String method = bundle.getString(KEY_PARAM_METHOD);
		String path = bundle.getString(KEY_PARAM_PATH);
		String options = bundle.getString(KEY_PARAM_OPTIONS);
		String contentType = bundle.getString(KEY_PARAM_CONTENT_TYPE);
		boolean allowCache = bundle.getBoolean(KEY_ALLOW_CACHE);

		Log.v(TAG, "doHttpRead");
		if (bundle.containsKey(KEY_PARAM_CALLBACK)) {
			ResultReceiver rr = bundle.getParcelable(KEY_PARAM_CALLBACK);

			DataCacheNode cachedData = null;

			cachedData = DataCache.query(_context, _auth, bundle);

			if (allowCache && cachedData != null) {
				Log.v(TAG, "Cached Response");
				bundle.putByteArray(KEY_RESPONSE_DATA,
						cachedData.getResponseData());
				bundle.putInt(KEY_RESPONSE_CODE, cachedData.getResponseCode());
				bundle.putBoolean(KEY_RESPONSE_CACHED, true);
				bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_NONE);
			} else {
				Log.v(TAG, "Atempting web request");
				Ws ws = new Ws(_auth);
				try {
					Result result = ws.httpRead(method, path, options,
							contentType);

					try {
						// happy path
						bundle.putByteArray(KEY_RESPONSE_DATA,
								result.getResultsAsByteArray());
						bundle.putInt(KEY_RESPONSE_CODE,
								result.getResponseCode());
						bundle.putBoolean(KEY_RESPONSE_CACHED, false);
						bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_NONE);
						DataCache.store(_context, _auth, bundle,
								bundle.getByteArray(KEY_RESPONSE_DATA),
								bundle.getInt(KEY_RESPONSE_CODE));
						Log.v(TAG, "web request success");
					} catch (Exception ex) {
						try {
							// unhappy, but http error
							bundle.putInt(KEY_RESPONSE_CODE,
									result.getResponseCode());
							bundle.putString(KEY_RESPONSE_ERROR_TYPE,
									ERROR_HTTP_ERROR);
							bundle.putString(KEY_RESPONSE_ERROR,
									result.getResponseMessage());
						} catch (Exception ex1) {
							// sad path
							bundle.putString(KEY_RESPONSE_ERROR_TYPE,
									ERROR_UNKNOWN);
							bundle.putString(KEY_RESPONSE_ERROR,
									ex1.getMessage());

						}
					}

				} catch (Exception ex) {
					Log.v(TAG, "web request fail");
					bundle.putString(KEY_RESPONSE_ERROR_TYPE,
							ERROR_NETWORK_ERROR);
					bundle.putString(KEY_RESPONSE_ERROR, ex.getMessage());
				}
			}
			rr.send(bundle.getInt(KEY_RESULT_CODE), bundle);
		}
	}

}
