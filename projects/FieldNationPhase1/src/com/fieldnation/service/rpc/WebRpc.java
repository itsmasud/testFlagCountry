package com.fieldnation.service.rpc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.HashMap;

import com.fieldnation.auth.db.AuthSqlHelper;
import com.fieldnation.auth.db.User;
import com.fieldnation.auth.db.UserDataSource;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.DataCache;
import com.fieldnation.webapi.OAuth;
import com.fieldnation.webapi.Result;
import com.fieldnation.webapi.Ws;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class WebRpc extends RpcInterface {
	private static final String TAG = "service.rpc.WebRpc";

	public WebRpc(HashMap<String, RpcInterface> map) {
		super(map, "web_request");
	}

	@Override
	public void execute(Context context, Intent intent) {
		try {
			String method = intent.getStringExtra("METHOD");
			String accessToken = intent.getStringExtra("PARAM_ACCESS_TOKEN");
			String username = intent.getStringExtra("PARAM_USERNAME");
			String errorMessage = null;

			Log.v(TAG, "username " + username);

			UserDataSource ds = new UserDataSource(context).open();
			OAuth auth = null;
			try {
				User user = ds.get(username);

				if (user.isAuthTokenValid(accessToken)) {
					JsonObject json = new JsonObject(user.getAuthBlob());
					try {
						// TODO, this sucks bad, need to cache stuff like this
						// when
						// possible
						auth = OAuth.authServer(json.getString("hostname"),
								json.getString("grantType"),
								json.getString("clientId"),
								json.getString("clientSecret"), username,
								user.getPassword());
					} catch (Exception e) {
						e.printStackTrace();
						// TODO, make into something user friendly.
						errorMessage = e.getMessage();
					}
				} else {
					errorMessage = "Invalid Token";
				}
			} finally {
				ds.close();
			}

			if (auth != null) {
				if ("httpread".equals(method)) {
					doHttpRead(context, intent, auth);
				} else if ("httpwrite".equals(method)) {
					doHttpWrite(context, intent, auth);
				}
			} else {
				doHttpError(context, intent, errorMessage);
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			doHttpError(context, intent, e.getMessage());
		}
	}

	private void doHttpError(Context context, Intent intent, String message) {
		Bundle bundle = intent.getExtras();

		if (bundle.containsKey("PARAM_CALLBACK")) {
			ResultReceiver rr = bundle.getParcelable("PARAM_CALLBACK");

			bundle.putString("RESPONSE_ERROR", message);

			rr.send(bundle.getInt("RESULT_CODE"), bundle);
		}

	}

	private void doHttpRead(Context context, Intent intent, OAuth at) {
		Bundle bundle = intent.getExtras();
		String method = bundle.getString("PARAM_METHOD");
		String path = bundle.getString("PARAM_PATH");
		String options = bundle.getString("PARAM_OPTIONS");
		String contentType = bundle.getString("PARAM_CONTENT_TYPE");
		boolean allowCache = bundle.getBoolean("ALLOW_CACHE");

		Log.v(TAG, "doHttpRead");
		if (bundle.containsKey("PARAM_CALLBACK")) {
			ResultReceiver rr = bundle.getParcelable("PARAM_CALLBACK");

			Bundle cachedData = null;

			cachedData = DataCache.query(at, bundle);

			if (allowCache && cachedData != null) {
				Log.v(TAG, "Cached Response");
				bundle.putByteArray("RESPONSE_DATA",
						cachedData.getByteArray("RESPONSE_DATA"));
				bundle.putInt("RESPONSE_CODE",
						cachedData.getInt("RESPONSE_CODE"));
				bundle.putBoolean("RESPONSE_CACHED", true);
			} else {
				Log.v(TAG, "Atempting web request");
				Ws ws = new Ws(at);
				try {
					Result result = ws.httpRead(method, path, options,
							contentType);

					bundle.putByteArray("RESPONSE_DATA",
							result.getResultsAsByteArray());
					bundle.putInt("RESPONSE_CODE",
							result.getUrlConnection().getResponseCode());
					bundle.putBoolean("RESPONSE_CACHED", false);

					DataCache.store(at, bundle, bundle);
					Log.v(TAG, "web request success");
				} catch (Exception ex) {
					Log.v(TAG, "web request fail");
					bundle.putString("RESPONSE_ERROR", ex.getMessage());
				}
			}
			rr.send(bundle.getInt("RESULT_CODE"), bundle);
		}

	}

	private void doHttpWrite(Context context, Intent intent, OAuth at) {
		Bundle bundle = intent.getExtras();
		String method = bundle.getString("PARAM_METHOD");
		String path = bundle.getString("PARAM_PATH");
		String options = bundle.getString("PARAM_OPTIONS");
		String contentType = bundle.getString("PARAM_CONTENT_TYPE");
		byte[] data = bundle.getByteArray("PARAM_DATA");
		boolean allowCache = bundle.getBoolean("ALLOW_CACHE");

		if (bundle.containsKey("PARAM_CALLBACK")) {
			ResultReceiver rr = bundle.getParcelable("PARAM_CALLBACK");

			Bundle cachedData = null;

			if (allowCache)
				cachedData = DataCache.query(at, bundle);

			if (cachedData != null) {
				bundle.putByteArray("RESPONSE_DATA",
						cachedData.getByteArray("RESPONSE_DATA"));
				bundle.putInt("RESPONSE_CODE",
						cachedData.getInt("RESPONSE_CODE"));
				bundle.putBoolean("RESPONSE_CACHED", true);
			} else {
				Ws ws = new Ws(at);
				try {
					Result result = ws.httpWrite(method, path, options, data,
							contentType);

					bundle.putByteArray("RESPONSE_DATA",
							result.getResultsAsByteArray());
					bundle.putInt("RESPONSE_CODE",
							result.getUrlConnection().getResponseCode());
					bundle.putBoolean("RESPONSE_CACHED", false);
				} catch (Exception ex) {
					bundle.putString("RESPONSE_ERROR", ex.getMessage());
				}
				DataCache.store(at, bundle, bundle);
			}

			rr.send(bundle.getInt("RESULT_CODE"), bundle);
		}

	}
}
