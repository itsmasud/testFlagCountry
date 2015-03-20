package com.fieldnation.service.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;

import com.fieldnation.FutureWaitAsyncTask;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.service.data.oauth.OAuth;
import com.fieldnation.ui.AuthActivity;

/**
 * Created by Michael on 12/15/2014.
 */
public class AuthTopicService extends Service implements AuthTopicConstants {
    private static final String TAG = "AuthTopicService";

    // Data
    private boolean _isStarted = false;
    private Account _account = null;
    private OAuth _authToken = null;
    private AuthState _state = null;
    private AuthTopicClient _authTopicClient;

    // Services
    private AccountManager _accountManager;

    static {
        Log.v(TAG, "STATIC!");
    }

    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/

    public AuthTopicService() {
        super();
        Log.v(TAG, "Construct");
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate");
        super.onCreate();
        _state = null;
        setState(AuthState.NOT_AUTHENTICATED);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        if (!_isStarted) {
            _authTopicClient = new AuthTopicClient(_authClientListener);
            _authTopicClient.connect(this);
            _isStarted = true;
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        _authTopicClient.disconnect(this);
        super.onDestroy();
    }

    private void setState(AuthState state) {
        if (_state == null || state != _state || state == AuthState.AUTHENTICATED) {
            _state = state;
            Log.v(TAG, state.name());
            if (_state == AuthState.AUTHENTICATED) {
                AuthTopicClient.dispatchAuthenticated(this, _authToken);
            } else {
                AuthTopicClient.dispatchAuthState(this, _state);
            }
        }
    }

    private final AuthTopicClient.Listener _authClientListener = new AuthTopicClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "onConnected");
            _authTopicClient.registerInvalidateCommand();
            _authTopicClient.registerRemoveCommand();
            _authTopicClient.registerRequestCommand();
            _authTopicClient.registerAccountAddedCommand();
        }

        @Override
        public void onCommandInvalidate() {
            Log.v(TAG, "onCommandInvalidate");
            invalidateToken();
        }

        @Override
        public void onCommandRemove() {
            Log.v(TAG, "onCommandRemove");
            removeAccount();
        }

        @Override
        public void onCommandRequest() {
            Log.v(TAG, "onCommandRequest");
            requestToken();
        }

        @Override
        public void onCommandAddedAccount() {
            Log.v(TAG, "onCommandAddedAccount");
            getAccount();
        }
    };

    /*-*****************************-*/
    /*-         Commands            -*/
    /*-*****************************-*/
    private String getAccountType() {
        return getString(R.string.auth_account_type);
    }

    private void invalidateToken() {
        Log.v(TAG, "invalidateToken");
        if (_state == AuthState.AUTHENTICATED) {
            setState(AuthState.NOT_AUTHENTICATED);
            _accountManager.invalidateAuthToken(getAccountType(), _authToken.getAccessToken());
            _authToken.delete(this);
            _authToken = null;
        }
    }

    private void requestToken() {
        Log.v(TAG, "requestToken");
        if (_state == AuthState.AUTHENTICATING) {
            Log.v(TAG, "requestToken do nothing");
            // TODO, what do we do?
        } else {
            if (_authToken != null) {
                Log.v(TAG, "requestToken have auth");
                setState(AuthState.AUTHENTICATED);
            } else if (_account != null) {
                Log.v(TAG, "requestToken have account");
                // don't have an auth token, but we are authenticated, so ask AOS for the token
                requestAuthTokenFromAccountManager();
            } else {
                Log.v(TAG, "requestToken need account");
                setState(AuthState.AUTHENTICATING);
                getAccount();
            }
        }
    }

    private void removeAccount() {
        Log.v(TAG, "removeAccount");
        if (_state == AuthState.AUTHENTICATED) {
            setState(AuthState.REMOVING);
            AccountManagerFuture<Boolean> future = _accountManager.removeAccount(_account, null, null);
            new FutureWaitAsyncTask(_futureWaitAsync_remove).execute(future);
            _account = null;
        } else if (_state == AuthState.NOT_AUTHENTICATED) {
            Log.v(TAG, "removeAccount do nothing");
        } else if (_state == AuthState.AUTHENTICATING) {
            // retry later if authenticating
            Log.v(TAG, "removeAccount retry later");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    removeAccount();
                }
            }, 1000);
        }
    }

    /*-*********************************-*/
    /*-         Internal Stuff          -*/
    /*-*********************************-*/
    private void onAppIsOld() {
        Log.v(TAG, "onAppIsOld");
        GlobalTopicClient.dispatchUpdateApp(this);
    }

    private void onNeedUserNameAndPassword(Parcelable authenticatorResponse) {
        Log.v(TAG, "onNeedUserNameAndPassword");
        Intent intent = new Intent(this, AuthActivity.class);

        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, authenticatorResponse);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * Requests auth from account manager
     */
    private void requestAuthTokenFromAccountManager() {
        Log.v(TAG, "requestAuthTokenFromAccountManager");
        setState(AuthState.AUTHENTICATING);
        AccountManagerFuture<Bundle> future = _accountManager.getAuthToken(
                _account, getAccountType(), null, null, null, null);
        new FutureWaitAsyncTask(_futureWaitAsync_token).execute(future);
    }

    /**
     * Gets the account from AOS
     */
    private void getAccount() {
        Log.v(TAG, "getAccount()");
        if (_state == AuthState.REMOVING) {
            Log.v(TAG, "removing, quit");
            return;
        }

        setState(AuthState.AUTHENTICATING);
        if (_accountManager == null)
            _accountManager = AccountManager.get(this);

        Account[] accounts = _accountManager.getAccountsByType(getAccountType());
        Log.v(TAG, "Found accounts: " + accounts.length);
        AccountManagerFuture<Bundle> future = null;

        // no accounts, ask account manager to add one
        if (accounts.length == 0) {
            future = _accountManager.addAccount(
                    getAccountType(), null, null, null, null, null, new Handler());
            new FutureWaitAsyncTask(_futureWaitAsync_account).execute(future);

        } else if (accounts.length >= 1) {
            //TODO  always gets the first account, should probbaly present a picker of some kind
            // have an account, now ask for the auth token
            _account = accounts[0];
            requestAuthTokenFromAccountManager();
        }
    }

    private final FutureWaitAsyncTask.Listener _futureWaitAsync_remove = new FutureWaitAsyncTask.Listener() {
        @Override
        public void onComplete(Object result) {
            Log.v(TAG, "_futureWaitAsync_remove.onComplete");
            setState(AuthState.NOT_AUTHENTICATED);
        }

        @Override
        public void onFail(Exception ex) {
            Log.v(TAG, "_futureWaitAsync_remove.onFail");
            setState(AuthState.NOT_AUTHENTICATED);
        }
    };

    private final FutureWaitAsyncTask.Listener _futureWaitAsync_account = new FutureWaitAsyncTask.Listener() {
        @Override
        public void onComplete(Object result) {
            Log.v(TAG, "_futureWaitAsync_account.onComplete");
            boolean isAuthenticatorResponse = false;

            if (result instanceof Bundle) {
                isAuthenticatorResponse = ((Bundle) result).containsKey(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
            }

            if (isAuthenticatorResponse) {
                Log.v(TAG, "isAuthenticatorResponse");
                setState(AuthState.AUTHENTICATING);
                Bundle bundle = (Bundle) result;
                // need to get username and password
                onNeedUserNameAndPassword(bundle.getParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE));
            } else {
                // TODO oh shit!
                Log.v(TAG, "NOT isAuthenticatorResponse!");
                setState(AuthState.NOT_AUTHENTICATED);
            }
        }

        @Override
        public void onFail(Exception ex) {
            Log.v(TAG, "_futureWaitAsync_account.onFail");
            setState(AuthState.NOT_AUTHENTICATED);
        }
    };

    private final FutureWaitAsyncTask.Listener _futureWaitAsync_token = new FutureWaitAsyncTask.Listener() {
        @Override
        public void onComplete(Object result) {
            Log.v(TAG, "_futureWaitAsync_token.onComplete");
            Bundle bundle = (Bundle) result;
            Log.v(TAG, bundle.toString());
            // auth is complete
            // if however, data invalid, need to ask again.
            if (!bundle.containsKey(AccountManager.KEY_AUTHTOKEN)) {
                Log.v(TAG, "no token string");
                if (bundle.containsKey(AccountManager.KEY_ACCOUNT_TYPE) && bundle.containsKey(AccountManager.KEY_ACCOUNT_NAME)) {
                    // account has been added, but no key yet
                    Log.v(TAG, "no account, requesting");
                    setState(AuthState.AUTHENTICATING);
                    getAccount();
                } else {
                    Log.v(TAG, "must have been an auth failure");
                    // todo.. not sure
                    setState(AuthState.NOT_AUTHENTICATED);
                    if (bundle.containsKey(AccountManager.KEY_AUTH_FAILED_MESSAGE)
                            && getString(R.string.login_error_update_app).equals(bundle.getString(AccountManager.KEY_AUTH_FAILED_MESSAGE))) {
                        // account fail, check if app is too old
                        onAppIsOld();
                    } else {
                    }
                }
            } else {
                // have token string, get the full token
                Log.v(TAG, "have token");
                _authToken = OAuth.lookup(AuthTopicService.this, bundle.getString(AccountManager.KEY_ACCOUNT_NAME));
                Log.v(TAG, _authToken.toJson().display());
                setState(AuthState.AUTHENTICATED);
            }
        }

        @Override
        public void onFail(Exception ex) {
            Log.v(TAG, "_futureWaitAsync_token.onFail");
            setState(AuthState.NOT_AUTHENTICATED);
        }
    };
}
