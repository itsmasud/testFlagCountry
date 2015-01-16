package com.fieldnation.rpc.server;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.server.AuthCache;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.common.AuthServiceConstants;

import java.net.UnknownHostException;
import java.util.HashMap;

public class AuthRpc extends RpcInterface implements AuthServiceConstants {
    private final static String TAG = "rpc.server.AuthRpc";

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
            // } else if (METHOD_REFRESH_TOKEN.equals(method)) {
            // refreshToken(context, intent);
        }

    }

    // private void refreshToken(Context context, Intent intent) {
    // String accessToken = intent.getStringExtra(KEY_PARAM_ACCESS_TOKEN);
    // String username = intent.getStringExtra(KEY_PARAM_USERNAME);
    //
    // AuthCache ac = AuthCache.get(context, username);
    // if (ac == null)
    // return;
    //
    // if (!ac.validateSessionHash(accessToken)) {
    // return;
    // }
    //
    // AccountManager am = AccountManager.get(context);
    //
    // Account[] accounts = am.getAccountsByType(_gs.accountType);
    //
    // Account account = null;
    //
    // for (int i = 0; i < accounts.length; i++) {
    // if (accounts[i].name.equals(username)) {
    // account = accounts[i];
    // break;
    // }
    // }
    // String password = am.getPassword(account);
    //
    // Log.d(TAG, password);
    //
    // try {
    // JsonObject reqBlob = new JsonObject(ac.getRequestBlob());
    //
    // OAuth auth = OAuth.authServer(reqBlob.getString("hostname"),
    // reqBlob.getString("grantType"),
    // reqBlob.getString("clientId"),
    // reqBlob.getString("clientSecret"), username, password);
    //
    // ac.setOAuthBlob(auth.toString());
    // } catch (Exception e) {
    // e.printStackTrace();
    // return;
    // }
    // }

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
            at = OAuth.authServer(hostname, path, grantType, clientId, clientSecret, username, password);

            Log.v(TAG, at.toString());

        } catch (UnknownHostException ex) {
            // TODO this means that the connection is down
            errorMessage = ex.getMessage();
        } catch (Exception ex) {
            // Log.v(TAG, ex.getMessage());
            if (ex.getMessage() != null) {
                if (ex.getMessage().startsWith("No authentication challenges found")) {
                    errorMessage = context.getString(R.string.login_error_invalid_remote_creds);
                }
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
            if (at.hasError()) {
                if (at.getErrorType().equals("invalid_client")) {
                    errorMessage = context.getString(R.string.login_error_update_app);
                } else {
                    errorMessage = at.getErrorDescription();
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
        }

        if (errorMessage == null)
            errorMessage = context.getString(R.string.login_error_no_error);

        if (bundle.containsKey(KEY_PARAM_ACCOUNT_AUTHENTICATOR_RESPONSE)) {
            AccountAuthenticatorResponse aar = (AccountAuthenticatorResponse) bundle.getParcelable(KEY_PARAM_ACCOUNT_AUTHENTICATOR_RESPONSE);

            Bundle resultBundle = new Bundle();

            if (accessToken != null) {
                resultBundle.putString(AccountManager.KEY_AUTHTOKEN, accessToken);
                resultBundle.putString(AccountManager.KEY_ACCOUNT_NAME, username);
                resultBundle.putString(AccountManager.KEY_ACCOUNT_TYPE, context.getString(R.string.accounttype));
            } else {
                resultBundle.putString(AccountManager.KEY_AUTH_FAILED_MESSAGE, errorMessage);
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

}
