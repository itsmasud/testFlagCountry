package com.fieldnation.rpc.server;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.common.WebServiceConstants;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class HttpPostFileRunnable extends HttpRunnable implements WebServiceConstants {
	private static final String TAG = "rpc.server.HttpPostFileRunnable";

	public HttpPostFileRunnable(Context context, Intent intent, OAuth at) {
		super(context, intent, at);
	}

	@Override
	public void run() {
		Bundle bundle = _intent.getExtras();
		String path = bundle.getString(KEY_PARAM_PATH);
		String options = bundle.getString(KEY_PARAM_OPTIONS);
		byte[] data = bundle.getByteArray(KEY_PARAM_DATA);
		String fieldName = bundle.getString(KEY_FILE_FIELD_NAME);
		String filename = bundle.getString(KEY_FILE_NAME);
		String fieldMapString = bundle.getString(KEY_FIELD_MAP);
		Map<String, String> fields = null;

		if (bundle.containsKey(KEY_PARAM_CALLBACK)) {
			ResultReceiver rr = bundle.getParcelable(KEY_PARAM_CALLBACK);

			if (fieldMapString != null) {
				try {
					JsonObject obj = new JsonObject(fieldMapString);
					fields = new HashMap<String, String>();

					Enumeration<String> keys = obj.keys();
					while (keys.hasMoreElements()) {
						String key = keys.nextElement();
						fields.put(key, obj.getString(key));
					}

				} catch (Exception ex) {

				}
			}

			Ws ws = new Ws(_auth);
			Result result = null;
			try {
				result = ws.httpPostFile(path, options, fieldName, filename, data, fields);

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

			} catch (Exception ex) {
				Log.v(TAG, "web request fail");
				bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_UNKNOWN);
				bundle.putString(KEY_RESPONSE_ERROR, ex.getMessage());
				if (result != null) {
					bundle.putLong(KEY_RESPONSE_CODE, result.getResponseCode());
				}
			}

			rr.send(bundle.getInt(KEY_RESULT_CODE), bundle);
		}
	}
}
