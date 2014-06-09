package com.fieldnation.service.rpc;

import java.util.HashMap;

import com.fieldnation.service.DataService;
import com.fieldnation.webapi.AccessToken;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

public class AuthRpc extends RpcInterface {

	public AuthRpc(HashMap<String, RpcInterface> map) {
		super(map, "getOauthToken");
	}

	@Override
	public void execute(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();

			String hostname = bundle.getString("PARAM_HOSTNAME");
			String path = bundle.getString("PARAM_PATH");
			String grantType = bundle.getString("PARAM_GRANT_TYPE");
			String clientId = bundle.getString("PARAM_CLIENT_ID");
			String clientSecret = bundle.getString("PARAM_CLIENT_SECRET");
			String username = bundle.getString("PARAM_USERNAME");
			String password = bundle.getString("PARAM_PASSWORD");

			AccessToken at = null;
			try {
				at = new AccessToken(hostname, path, grantType, clientId,
						clientSecret, username, password);
			} catch (Exception ex) {
				// could not get the token... need to figure out why
				ex.printStackTrace();
			}
			// TODO, ANDR-11 if failed, generate local auth token

			if (bundle.containsKey("PARAM_ACCOUNT_AUTHENTICATOR_RESPONSE")) {
				AccountAuthenticatorResponse aar = (AccountAuthenticatorResponse) bundle
						.getParcelable("PARAM_ACCOUNT_AUTHENTICATOR_RESPONSE");

				Bundle result = new Bundle();
				result.putString("ACTION", "RPC_getOauthToken");
				result.putString(AccountManager.KEY_AUTHTOKEN, at.toString());
				result.putString(AccountManager.KEY_ACCOUNT_NAME, username);
				result.putString(AccountManager.KEY_ACCOUNT_TYPE, "fieldnation");
				aar.onResult(result);
			}

			if (bundle.containsKey("PARAM_RESULT_RECEIVER")) {
				ResultReceiver rr = bundle
						.getParcelable("PARAM_RESULT_RECEIVER");

				Bundle response = new Bundle();

				response.putString("ACTION", "RPC_getOauthToken");
				response.putString("authtoken", at.toString());

				rr.send(bundle.getInt("RESULT_CODE"), response);
			}

			// TODO Auto-generated method stub
			System.out.println("Method Stub: execute()");
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
