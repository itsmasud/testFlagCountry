package com.fieldnation.rpc.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.HashMap;

import com.fieldnation.auth.server.AuthCache;
import com.fieldnation.auth.server.AuthCacheSqlHelper;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.webapi.OAuth;
import com.fieldnation.webapi.Result;
import com.fieldnation.webapi.Ws;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class WebRpc extends RpcInterface implements WebServiceConstants {
	private static final String TAG = "service.rpc.WebRpc";

	public WebRpc(HashMap<String, RpcInterface> map) {
		super(map, ACTION_NAME);
	}

	@Override
	public void execute(Context context, Intent intent) {
		String method = intent.getStringExtra(KEY_METHOD);
		String authToken = intent.getStringExtra(KEY_PARAM_AUTH_TOKEN);
		String username = intent.getStringExtra(KEY_PARAM_USERNAME);
		String errorMessage = null;
		String errorType = ERROR_NONE;

		Log.v(TAG, "username " + username);

		OAuth auth = null;

		// look up user in the cache
		AuthCache authCache = AuthCache.get(context, username);

		// not found
		if (authCache == null) {
			errorType = ERROR_USER_NOT_FOUND;
			errorMessage = "Could not find the user.";
		} else {
			// validate session
			Log.v(TAG, authToken);
			if (authCache.validateSessionHash(authToken)) {
				try {
					auth = OAuth.fromCache(authCache.getOAuthBlob());
				} catch (ParseException e) {
					// this is handled below when we check for auth != null
					e.printStackTrace();
				}
			} else {
				errorType = ERROR_SESSION_INVALID;
				errorMessage = "The authtoken is invalid or has expired.";
			}
		}

		if (auth != null) {
			if (METHOD_HTTP_READ.equals(method)) {
				doHttpRead(context, intent, auth);
			} else if (METHOD_HTTP_WRITE.equals(method)) {
				doHttpWrite(context, intent, auth);
			}
		} else {
			doHttpError(context, intent, errorType, errorMessage);
		}
	}

	private void doHttpError(Context context, Intent intent, String errorType,
			String message) {
		Bundle bundle = intent.getExtras();

		if (bundle.containsKey(KEY_PARAM_CALLBACK)) {
			ResultReceiver rr = bundle.getParcelable(KEY_PARAM_CALLBACK);

			bundle.putString(KEY_RESPONSE_ERROR_TYPE, errorType);
			bundle.putString(KEY_RESPONSE_ERROR, message);
			rr.send(bundle.getInt(KEY_RESULT_CODE), bundle);
		}
	}

	private void doHttpRead(Context context, Intent intent, OAuth at) {
		Bundle bundle = intent.getExtras();
		String method = bundle.getString(KEY_PARAM_METHOD);
		String path = bundle.getString(KEY_PARAM_PATH);
		String options = bundle.getString(KEY_PARAM_OPTIONS);
		String contentType = bundle.getString(KEY_PARAM_CONTENT_TYPE);
		boolean allowCache = bundle.getBoolean(KEY_ALLOW_CACHE);

		Log.v(TAG, "doHttpRead");
		if (bundle.containsKey(KEY_PARAM_CALLBACK)) {
			ResultReceiver rr = bundle.getParcelable(KEY_PARAM_CALLBACK);

			Bundle cachedData = null;

			cachedData = DataCache.query(at, bundle);

			if (allowCache && cachedData != null) {
				Log.v(TAG, "Cached Response");
				bundle.putByteArray(KEY_RESPONSE_DATA,
						cachedData.getByteArray(KEY_RESPONSE_DATA));
				bundle.putInt(KEY_RESPONSE_CODE,
						cachedData.getInt(KEY_RESPONSE_CODE));
				bundle.putBoolean(KEY_RESPONSE_CACHED, true);
				bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_NONE);
			} else {
				Log.v(TAG, "Atempting web request");
				Ws ws = new Ws(at);
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
						DataCache.store(at, bundle, bundle);
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

	private void doHttpWrite(Context context, Intent intent, OAuth at) {
		Bundle bundle = intent.getExtras();
		String method = bundle.getString(KEY_PARAM_METHOD);
		String path = bundle.getString(KEY_PARAM_PATH);
		String options = bundle.getString(KEY_PARAM_OPTIONS);
		String contentType = bundle.getString(KEY_PARAM_CONTENT_TYPE);
		byte[] data = bundle.getByteArray(KEY_PARAM_DATA);
		boolean allowCache = bundle.getBoolean(KEY_ALLOW_CACHE);

		if (bundle.containsKey(KEY_PARAM_CALLBACK)) {
			ResultReceiver rr = bundle.getParcelable(KEY_PARAM_CALLBACK);

			Bundle cachedData = null;

			if (allowCache)
				cachedData = DataCache.query(at, bundle);

			if (cachedData != null) {
				bundle.putByteArray(KEY_RESPONSE_DATA,
						cachedData.getByteArray(KEY_RESPONSE_DATA));
				bundle.putInt(KEY_RESPONSE_CODE,
						cachedData.getInt(KEY_RESPONSE_CODE));
				bundle.putBoolean(KEY_RESPONSE_CACHED, true);
			} else {
				Ws ws = new Ws(at);
				Result result = null;
				try {
					result = ws.httpWrite(method, path, options, data,
							contentType);

					try {
						// happy path
						bundle.putByteArray(KEY_RESPONSE_DATA,
								result.getResultsAsByteArray());
						bundle.putInt(KEY_RESPONSE_CODE,
								result.getResponseCode());
						bundle.putBoolean(KEY_RESPONSE_CACHED, false);
						bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_NONE);
						DataCache.store(at, bundle, bundle);
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
					bundle.putString(KEY_RESPONSE_ERROR_TYPE, ERROR_UNKNOWN);
					bundle.putString(KEY_RESPONSE_ERROR, ex.getMessage());
					if (result != null) {
						bundle.putLong(KEY_RESPONSE_CODE,
								result.getResponseCode());
					}
				}
			}

			rr.send(bundle.getInt(KEY_RESULT_CODE), bundle);
		}

	}
}
