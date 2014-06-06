package com.fieldnation.auth;

import com.fieldnation.R;
import com.fieldnation.service.rpc.AuthRpc;
import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Authenticator extends AbstractAccountAuthenticator {
	private Context _context;

	public Authenticator(Context context) {
		super(context);
		_context = context;
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response,
			String accountType, String authTokenType,
			String[] requiredFeatures, Bundle options)
			throws NetworkErrorException {

		Intent intent = new Intent(_context, AuthActivity.class);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
				response);

		Bundle result = new Bundle();
		result.putParcelable(AccountManager.KEY_INTENT, intent);

		return result;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response,
			String accountType) {
		// TODO Auto-generated method stub
		System.out.println("Method Stub: editProperties()");
		return null;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response,
			Account account, Bundle options) throws NetworkErrorException {
		// TODO Auto-generated method stub
		System.out.println("Method Stub: confirmCredentials()");
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		AccountManager am = AccountManager.get(_context);
		String password = am.getPassword(account);
		String hostname = _context.getString(R.string.fn_hostname);
		String grantType = _context.getString(R.string.fn_grant_type);
		String clientId = _context.getString(R.string.fn_client_id);
		String clientSecret = _context.getString(R.string.fn_client_secret);

		Intent intent = AuthRpc.makeIntent(_context, response, hostname,
				grantType, clientId, clientSecret, account.name, password);

		_context.startService(intent);
		return null;

		// Bundle result = new Bundle();
		//
		// try {
		// AccessToken at = AuthHelper.getToken(_context, account.name,
		// password);
		//
		// result.putString(AccountManager.KEY_AUTHTOKEN, at.toString());
		// result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
		// result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
		// } catch (MalformedURLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// return result;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		// TODO Auto-generated method stub
		System.out.println("Method Stub: getAuthTokenLabel()");
		return null;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		// TODO Auto-generated method stub
		System.out.println("Method Stub: updateCredentials()");
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
			Account account, String[] features) throws NetworkErrorException {
		// TODO Auto-generated method stub
		System.out.println("Method Stub: hasFeatures()");
		return null;
	}

}
