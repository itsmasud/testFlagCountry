package com.fieldnation.rpc.client;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

import com.fieldnation.auth.server.Authenticator;
import com.fieldnation.rpc.common.AuthServiceConstants;
import com.fieldnation.rpc.common.DataServiceConstants;
import com.fieldnation.rpc.server.AuthRpc;
import com.fieldnation.rpc.server.DataService;

/**
 * Provides intents that will call the authentication services.
 * 
 * @author michael.carver
 * 
 */
public class AuthService implements AuthServiceConstants {
	private Context _context;

	public AuthService(Context conetxt) {
		_context = conetxt;
	}

	/**
	 * This is usually called by the {@link AccountManager} via the
	 * {@link Authenticator}
	 * 
	 * @see Authenticator
	 * @see AccountManager
	 * @see com.fieldnation.auth.server.AuthService
	 * @see AuthRpc
	 * 
	 * @param response
	 * @param hostname
	 * @param grantType
	 * @param clientId
	 * @param clientSecret
	 * @param username
	 * @param password
	 * @return
	 */
	public Intent authenticateWeb(AccountAuthenticatorResponse response,
			String hostname, String grantType, String clientId,
			String clientSecret, String username, String password) {

		Intent intent = new Intent(_context, DataService.class);
		intent.setAction(DataServiceConstants.ACTION_RPC);
		intent.putExtra(DataServiceConstants.KEY_SERVICE, ACTION_NAME);
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

	/**
	 * This should be called by a normal application to authenticate.
	 * 
	 * @param callback
	 * @param resultCode
	 * @param hostname
	 * @param grantType
	 * @param clientId
	 * @param clientSecret
	 * @param username
	 * @param password
	 * @return
	 */
	public Intent authenticateWeb(ResultReceiver callback, int resultCode,
			String hostname, String grantType, String clientId,
			String clientSecret, String username, String password) {

		Intent intent = new Intent(_context, DataService.class);
		intent.setAction(DataServiceConstants.ACTION_RPC);
		intent.putExtra(DataServiceConstants.KEY_SERVICE, ACTION_NAME);
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

}
