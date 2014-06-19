package com.fieldnation.service.rpc;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.authserver.db.AuthCache;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.DataService;
import com.fieldnation.webapi.OAuth;
import com.fieldnation.webapi.Result;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class AuthRpc extends RpcInterface {

	private final static String TAG = "service.rpc.AuthRpc";

	public static final String ACTION_NAME = "auth";

	public static final String METHOD_REFRESH_TOKEN = "refreshToken";
	public static final String METHOD_GET_OAUTH_TOKEN = "getOauthToken";

	public static final String KEY_RESULT_CODE = "RESULT_CODE";
	public static final String KEY_PARAM_RESULT_RECEIVER = "PARAM_RESULT_RECEIVER";
	public static final String KEY_PARAM_ACCOUNT_AUTHENTICATOR_RESPONSE = "PARAM_ACCOUNT_AUTHENTICATOR_RESPONSE";
	public static final String KEY_PARAM_PASSWORD = "PARAM_PASSWORD";
	public static final String KEY_PARAM_CLIENT_SECRET = "PARAM_CLIENT_SECRET";
	public static final String KEY_PARAM_CLIENT_ID = "PARAM_CLIENT_ID";
	public static final String KEY_PARAM_GRANT_TYPE = "PARAM_GRANT_TYPE";
	public static final String KEY_PARAM_PATH = "PARAM_PATH";
	public static final String KEY_PARAM_HOSTNAME = "PARAM_HOSTNAME";
	public static final String KEY_PARAM_USERNAME = "PARAM_USERNAME";
	public static final String KEY_PARAM_ACCESS_TOKEN = "PARAM_ACCESS_TOKEN";
	public static final String KEY_METHOD = "METHOD";

	private GlobalState _gs;

	public AuthRpc(HashMap<String, RpcInterface> map) {
		super(map, ACTION_NAME);
	}

	@Override
	public void execute(Context context, Intent intent) {
		_gs = (GlobalState) context.getApplicationContext();
		String method = intent.getStringExtra(KEY_METHOD);

		if (METHOD_GET_OAUTH_TOKEN.equals(method)) {
			getOauthToken(context, intent);
		} else if (METHOD_REFRESH_TOKEN.equals(method)) {
			refreshToken(context, intent);
		}

	}

	private void refreshToken(Context context, Intent intent) {
		String accessToken = intent.getStringExtra(KEY_PARAM_ACCESS_TOKEN);
		String username = intent.getStringExtra(KEY_PARAM_USERNAME);

		AuthCache ac = AuthCache.get(context, username);
		if (ac == null)
			return;

		if (!ac.validateSessionHash(accessToken)) {
			return;
		}

		try {
			JsonObject reqBlob = new JsonObject(ac.getRequestBlob());

			OAuth auth = OAuth.authServer(reqBlob.getString("hostname"),
					reqBlob.getString("grantType"),
					reqBlob.getString("clientId"),
					reqBlob.getString("clientSecret"), username,
					ac.getPassword());

			ac.setOAuthBlob(auth.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private void getOauthToken(Context context, Intent intent) {
		String errorMessage = null;

		Bundle bundle = intent.getExtras();

		String hostname = bundle.getString(KEY_PARAM_HOSTNAME);
		String path = bundle.getString(KEY_PARAM_PATH);
		String grantType = bundle.getString(KEY_PARAM_GRANT_TYPE);
		String clientId = bundle.getString(KEY_PARAM_CLIENT_ID);
		String clientSecret = bundle.getString(KEY_PARAM_CLIENT_SECRET);
		String username = bundle.getString(KEY_PARAM_USERNAME);
		String password = bundle.getString(KEY_PARAM_PASSWORD);

		String requestBlob = "";
		try {
			JsonObject json = new JsonObject();
			json.put("hostname", hostname);
			json.put("grantType", grantType);
			json.put("clientId", clientId);
			json.put("clientSecret", clientSecret);
			json.put("path", path);

			requestBlob = json.toString();
		} catch (Exception ex) {
			// TODO should never happen
			ex.printStackTrace();
		}

		OAuth at = null;
		String accessToken = null;
		try {
			at = OAuth.authServer(hostname, path, grantType, clientId,
					clientSecret, username, password);

			Log.v(TAG, at.toString());

		} catch (Exception ex) {
			Log.v(TAG, ex.getMessage());
			if (ex.getMessage().startsWith("No authentication challenges found")) {
				errorMessage = context.getString(R.string.login_error_invalid_remote_creds);
			}
			// could not get the token... need to figure out why
			ex.printStackTrace();
		}

		// auth against server failed, try to do some local work
		if (at == null) {
			Log.d(TAG, "Authentication failed, trying local");
			// get local stuff
			AuthCache authCache = AuthCache.get(context, username);
			if (authCache == null) {
				Log.d(TAG, "User not in database");
				if (errorMessage == null)
					errorMessage = context.getString(R.string.login_error_not_found_in_db);
			} else {
				if (authCache.validatePassword(password)) {
					// valid user... generate session
					accessToken = authCache.startSession(password, 3600);
				} else {
					if (errorMessage == null)
						errorMessage = context.getString(R.string.login_error_invalid_local_creds);
					Log.d(TAG, "Invalid local creds");
				}
			}
		} else {
			Log.d(TAG, "Saving user");
			// store to local
			AuthCache authCache = AuthCache.get(context, username);
			if (authCache == null) {
				authCache = AuthCache.create(context, username, password);
			} else {
				authCache.setPassword(password);
			}
			authCache.setRequestBlob(requestBlob);
			authCache.setOAuthBlob(at.toString());
			accessToken = authCache.startSession(password, 3600);
		}

		if (errorMessage == null)
			errorMessage = context.getString(R.string.login_error_no_error);

		if (bundle.containsKey(KEY_PARAM_ACCOUNT_AUTHENTICATOR_RESPONSE)) {
			AccountAuthenticatorResponse aar = (AccountAuthenticatorResponse) bundle.getParcelable(KEY_PARAM_ACCOUNT_AUTHENTICATOR_RESPONSE);

			Bundle resultBundle = new Bundle();

			if (accessToken != null) {
				resultBundle.putString(AccountManager.KEY_AUTHTOKEN,
						accessToken);
				resultBundle.putString(AccountManager.KEY_ACCOUNT_NAME,
						username);
				resultBundle.putString(AccountManager.KEY_ACCOUNT_TYPE,
						_gs.accountType);
			} else {
				resultBundle.putString(AccountManager.KEY_AUTH_FAILED_MESSAGE,
						errorMessage);
			}
			aar.onResult(resultBundle);
		}

		if (bundle.containsKey(KEY_PARAM_RESULT_RECEIVER)) {
			ResultReceiver rr = bundle.getParcelable(KEY_PARAM_RESULT_RECEIVER);

			Bundle response = new Bundle();

			response.putString("error", errorMessage);
			if (at != null) {
				response.putString("authtoken", accessToken);
			}

			rr.send(bundle.getInt(KEY_RESULT_CODE), response);
		}
	}

	public static Intent authenticateWeb(Context context,
			AccountAuthenticatorResponse response, String hostname,
			String grantType, String clientId, String clientSecret,
			String username, String password) {

		Intent intent = new Intent(context, DataService.class);
		intent.setAction(DataService.ACTION_RPC);
		intent.putExtra(DataService.KEY_SERVICE, ACTION_NAME);
		intent.putExtra(KEY_METHOD, METHOD_GET_OAUTH_TOKEN);
		intent.putExtra(KEY_PARAM_HOSTNAME, hostname);
		intent.putExtra(KEY_PARAM_PATH, "/authentication/api/oauth/token");
		intent.putExtra(KEY_PARAM_GRANT_TYPE, grantType);
		intent.putExtra(KEY_PARAM_CLIENT_ID, clientId);
		intent.putExtra(KEY_PARAM_CLIENT_SECRET, clientSecret);
		intent.putExtra(KEY_PARAM_USERNAME, username);
		intent.putExtra(KEY_PARAM_PASSWORD, password);

		intent.putExtra(KEY_PARAM_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

		return intent;
	}

	public static Intent authenticateWeb(Context context,
			ResultReceiver callback, int resultCode, String hostname,
			String grantType, String clientId, String clientSecret,
			String username, String password) {

		Intent intent = new Intent(context, DataService.class);
		intent.setAction(DataService.ACTION_RPC);
		intent.putExtra(DataService.KEY_SERVICE, ACTION_NAME);
		intent.putExtra(KEY_METHOD, METHOD_GET_OAUTH_TOKEN);
		intent.putExtra(KEY_PARAM_HOSTNAME, hostname);
		intent.putExtra(KEY_PARAM_PATH, "/authentication/api/oauth/token");
		intent.putExtra(KEY_PARAM_GRANT_TYPE, grantType);
		intent.putExtra(KEY_PARAM_CLIENT_ID, clientId);
		intent.putExtra(KEY_PARAM_CLIENT_SECRET, clientSecret);
		intent.putExtra(KEY_PARAM_USERNAME, username);
		intent.putExtra(KEY_PARAM_PASSWORD, password);

		intent.putExtra(KEY_PARAM_RESULT_RECEIVER, callback);
		intent.putExtra(KEY_RESULT_CODE, resultCode);

		return intent;
	}

	public static Intent refreshToken(Context context, String username,
			String accessToken) {
		Intent intent = new Intent(context, DataService.class);
		intent.setAction(DataService.ACTION_RPC);
		intent.putExtra(DataService.KEY_SERVICE, ACTION_NAME);
		intent.putExtra(KEY_METHOD, METHOD_REFRESH_TOKEN);
		intent.putExtra(KEY_PARAM_ACCESS_TOKEN, accessToken);
		intent.putExtra(KEY_PARAM_USERNAME, username);

		return intent;
	}
}
