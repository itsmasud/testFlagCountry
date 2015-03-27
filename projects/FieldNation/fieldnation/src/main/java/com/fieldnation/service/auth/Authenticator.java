package com.fieldnation.service.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;

import com.fieldnation.Log;
import com.fieldnation.R;

/**
 * The OS will call this when authenticating a user. It is mostly a wrapper for
 * the AuthRpc
 *
 * @author michael.carver
 */
public class Authenticator extends AbstractAccountAuthenticator {
    private static final String TAG = "auth.server.Authenticator";
    private Context _context;

    public Authenticator(Context context) {
        super(context.getApplicationContext());
        _context = context.getApplicationContext();
        Log.v(TAG, "Constructor");
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType,
                             String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Log.v(TAG, "addAccount");
        Bundle result = new Bundle();
        result.putParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        return result;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType,
                               Bundle options) throws NetworkErrorException {
        Log.v(TAG, "getAuthToken");
        AccountManager am = AccountManager.get(_context);
        String password = am.getPassword(account);
        String hostname = _context.getString(R.string.web_fn_hostname);
        String grantType = _context.getString(R.string.auth_fn_grant_type);
        String clientId = _context.getString(R.string.auth_fn_client_id);
        String clientSecret = _context.getString(R.string.auth_fn_client_secret);

        OAuth auth = OAuth.lookup(_context, account.name);

        try {
            if (auth == null) {
                auth = OAuth.authenticate(_context, hostname, "/authentication/api/oauth/token", grantType, clientId, clientSecret, account.name, password);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Bundle result = new Bundle();
        if (auth != null) {
            result.putString(AccountManager.KEY_AUTHTOKEN, auth.getAccessToken());
            result.putString(AccountManager.KEY_ACCOUNT_NAME, auth.getUsername());
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, _context.getString(R.string.auth_account_type));
        } else {
            result.putString(AccountManager.KEY_AUTH_FAILED_MESSAGE, "Could not get Auth token");
        }
        return result;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        Log.v(TAG, "Method Stub: editProperties()");
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        Log.v(TAG, "Method Stub: confirmCredentials()");
        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        Log.v(TAG, "Method Stub: getAuthTokenLabel()");
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType,
                                    Bundle options) throws NetworkErrorException {
        Log.v(TAG, "Method Stub: updateCredentials()");
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        Log.v(TAG, "Method Stub: hasFeatures()");
        return null;
    }

}
