package com.fieldnation.service.rpc;

import java.util.HashMap;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.db.User;
import com.fieldnation.auth.db.UserDataSource;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.DataService;
import com.fieldnation.webapi.OAuth;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class AuthRpc extends RpcInterface {
	private final static String TAG = "service.rpc.AuthRpc";

	private GlobalState _gs;

	public AuthRpc(HashMap<String, RpcInterface> map) {
		super(map, "auth");
	}

	@Override
	public void execute(Context context, Intent intent) {
		_gs = (GlobalState) context.getApplicationContext();
		String errorMessage = null;
		try {
			Bundle bundle = intent.getExtras();

			String hostname = bundle.getString("PARAM_HOSTNAME");
			String path = bundle.getString("PARAM_PATH");
			String grantType = bundle.getString("PARAM_GRANT_TYPE");
			String clientId = bundle.getString("PARAM_CLIENT_ID");
			String clientSecret = bundle.getString("PARAM_CLIENT_SECRET");
			String username = bundle.getString("PARAM_USERNAME");
			String password = bundle.getString("PARAM_PASSWORD");

			OAuth at = null;
			try {
				at = OAuth.authServer(hostname, path, grantType, clientId,
						clientSecret, username, password);
				Log.v(TAG, at.toString());

			} catch (Exception ex) {
				Log.v(TAG, ex.getMessage());
				if (ex.getMessage().startsWith(
						"No authentication challenges found")) {
					errorMessage = context.getString(R.string.login_error_invalid_remote_creds);
				}
				// could not get the token... need to figure out why
				ex.printStackTrace();
			}

			// auth against server failed, try to do some local work
			if (at == null) {
				Log.d(TAG, "Authentication failed, trying local");
				// get local stuff
				UserDataSource ds = new UserDataSource(context);
				ds.open();
				User user = ds.get(username);
				if (user == null) {
					Log.d(TAG, "User not in database");
					if (errorMessage == null)
						errorMessage = context.getString(R.string.login_error_not_found_in_db);
				} else {

					if (user.validatePassword(password)) {
						// valid user... generate local oauth
						String authToken = user.createAuthToken(3600);
						long expiresIn = 3600;
						long expiresOn = user.getExpirationDate();

						JsonObject json = new JsonObject();

						json.put("access_token", authToken);
						json.put("expires_in", expiresIn);
						json.put("expires_on", expiresOn);
						json.put("is_local", true);

						at = OAuth.fromJson(json);
					} else {
						if (errorMessage == null)
							errorMessage = context.getString(R.string.login_error_invalid_local_creds);
						Log.d(TAG, "Invalid local creds");
					}
				}
				ds.close();
			} else {
				Log.d(TAG, "Saving user");
				// store to local
				UserDataSource ds = new UserDataSource(context);
				ds.open();
				User user = ds.get(username);
				if (user == null) {
					user = ds.get(ds.create(username, password));
				} else {
					user.setPassword(password);
				}
				ds.save(user);
				ds.close();
			}

			if (errorMessage == null)
				errorMessage = context.getString(R.string.login_error_no_error);

			if (bundle.containsKey("PARAM_ACCOUNT_AUTHENTICATOR_RESPONSE")) {
				AccountAuthenticatorResponse aar = (AccountAuthenticatorResponse) bundle.getParcelable("PARAM_ACCOUNT_AUTHENTICATOR_RESPONSE");

				Bundle result = new Bundle();
				result.putString("ACTION", "RPC_getOauthToken");

				if (at != null) {
					result.putString(AccountManager.KEY_AUTHTOKEN,
							at.toString());
					result.putString(AccountManager.KEY_ACCOUNT_NAME, username);
					result.putString(AccountManager.KEY_ACCOUNT_TYPE,
							_gs.accountType);
				} else {
					result.putString(AccountManager.KEY_AUTH_FAILED_MESSAGE,
							errorMessage);
				}
				aar.onResult(result);
			}

			if (bundle.containsKey("PARAM_RESULT_RECEIVER")) {
				ResultReceiver rr = bundle.getParcelable("PARAM_RESULT_RECEIVER");

				Bundle response = new Bundle();

				response.putString("ACTION", "RPC_getOauthToken");
				response.putString("error", errorMessage);
				if (at != null) {
					response.putString("authtoken", at.toString());
				}

				rr.send(bundle.getInt("RESULT_CODE"), response);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Intent makeIntent(Context context,
			AccountAuthenticatorResponse response, String hostname,
			String grantType, String clientId, String clientSecret,
			String username, String password) {

		Intent intent = new Intent(context, DataService.class);
		intent.setAction("RPC");
		intent.putExtra("SERVICE", "auth");
		intent.putExtra("METHOD", "getOauthToken");
		intent.putExtra("PARAM_HOSTNAME", hostname);
		intent.putExtra("PARAM_PATH", "/authentication/api/oauth/token");
		intent.putExtra("PARAM_GRANT_TYPE", grantType);
		intent.putExtra("PARAM_CLIENT_ID", clientId);
		intent.putExtra("PARAM_CLIENT_SECRET", clientSecret);
		intent.putExtra("PARAM_USERNAME", username);
		intent.putExtra("PARAM_PASSWORD", password);

		intent.putExtra("PARAM_ACCOUNT_AUTHENTICATOR_RESPONSE", response);

		return intent;
	}

	public static Intent makeIntent(Context context, ResultReceiver callback,
			int resultCode, String hostname, String grantType, String clientId,
			String clientSecret, String username, String password) {

		Intent intent = new Intent(context, DataService.class);
		intent.setAction("RPC");
		intent.putExtra("SERVICE", "auth");
		intent.putExtra("METHOD", "getOauthToken");
		intent.putExtra("PARAM_HOSTNAME", hostname);
		intent.putExtra("PARAM_PATH", "/authentication/api/oauth/token");
		intent.putExtra("PARAM_GRANT_TYPE", grantType);
		intent.putExtra("PARAM_CLIENT_ID", clientId);
		intent.putExtra("PARAM_CLIENT_SECRET", clientSecret);
		intent.putExtra("PARAM_USERNAME", username);
		intent.putExtra("PARAM_PASSWORD", password);

		intent.putExtra("PARAM_RESULT_RECEIVER", callback);
		intent.putExtra("RESULT_CODE", resultCode);

		return intent;
	}
}
