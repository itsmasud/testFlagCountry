package com.fieldnation.service.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.OnAccountsUpdateListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.FutureWaitAsyncTask;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.service.data.profile.ProfileClient;

import java.util.List;

/**
 * Created by Michael on 12/15/2014.
 */
public class AuthSystem implements AuthTopicConstants {
    private static final String TAG = UniqueTag.makeTag("AuthSystem");

    // Data
    private Account _account = null;
    private OAuth _authToken = null;
    private AuthState _state = null;

    // Services
    private AccountManager _accountManager;

    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    private static AuthSystem _instance = null;

    public static AuthSystem start() {
        if (_instance == null)
            _instance = new AuthSystem();

        return _instance;
    }

    public static void stop() {
        if (_instance != null) {
            _instance.shutdown();
        }
    }

    private AuthSystem() {
        _authClient.subInvalidateCommand();
        _authClient.subRemoveCommand();
        _authClient.subRequestCommand();
        _authClient.subAccountAddedCommand();

        _state = null;
        setState(AuthState.NOT_AUTHENTICATED);
    }

    public void shutdown() {
        Log.v(TAG, "onDestroy");
        _authClient.unsubInvalidateCommand();
        _authClient.unsubRemoveCommand();
        _authClient.unsubRequestCommand();
        _authClient.unsubAccountAddedCommand();

        //setState(AuthState.NOT_AUTHENTICATED);
        if (_accountManager != null) {
            _accountManager.removeOnAccountsUpdatedListener(_accounts_updateListener);
        }

        _instance = null;
    }

    private void setState(AuthState state) {
        if (_state == null || state != _state || state == AuthState.AUTHENTICATED) {
            _state = state;
            Log.v(TAG, state.name());
            if (_state == AuthState.AUTHENTICATED) {
                AuthClient.authenticated(_authToken);
            } else {
                AuthClient.authStateChange(_state);
            }
        }
    }

    private final AuthClient _authClient = new AuthClient() {
        @Override
        public void onCommandInvalidate() {
            Log.v(TAG, "onCommandInvalidate");
            if (_authToken != null)
                invalidateToken(_authToken.getAccessToken());
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
        Log.v(TAG, "getAccountType");
        return App.get().getString(R.string.auth_account_type);
    }

    private void invalidateToken(String token) {
        Log.v(TAG, "invalidateToken");
        if (_state == AuthState.AUTHENTICATED) {
            setState(AuthState.NOT_AUTHENTICATED);
            _accountManager.invalidateAuthToken(getAccountType(), token);
            _authToken.delete();
            _authToken = null;
        }
    }

    private void requestToken() {
        Log.v(TAG, "requestToken");
        if (_state == AuthState.AUTHENTICATING) {
            Log.v(TAG, "requestToken do nothing");
            // TODO, what do we do?
        } else {
            if (_authToken != null && _authToken.getAccessToken() != null) {
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
        if (_account == null)
            return;
        Log.v(TAG, "removeAccount 2");
        OAuth.flushAll();
        App.logout();

        if (_state == AuthState.AUTHENTICATED) {
            setState(AuthState.REMOVING);
            AccountManagerFuture<Boolean> future = _accountManager.removeAccount(_account, null, null);
            new FutureWaitAsyncTask(_futureWaitAsync_remove).execute(future);
//            GlobalTopicClient.profileInvalid(App.get());
//            AuthTopicClient.invalidateCommand(App.get());
            _account = null;
            _authToken = null;
        } else if (_state == AuthState.NOT_AUTHENTICATED) {
            Log.v(TAG, "removeAccount do nothing");
        } else if (_state == AuthState.AUTHENTICATING) {
            // retry later if authenticating
            Log.v(TAG, "removeAccount retry later");
            setState(AuthState.REMOVING);
            AccountManagerFuture<Boolean> future = _accountManager.removeAccount(_account, null, null);
            new FutureWaitAsyncTask(_futureWaitAsync_remove).execute(future);
            _account = null;
            _authToken = null;
//            GlobalTopicClient.profileInvalid(App.get());
//            AuthTopicClient.invalidateCommand(App.get());

        }
    }

    /*-*********************************-*/
    /*-         Internal Stuff          -*/
    /*-*********************************-*/
    private void onAppIsOld() {
        Log.v(TAG, "onAppIsOld");
        AppMessagingClient.updateApp();
    }

    private void onNeedUserNameAndPassword(Parcelable authenticatorResponse) {
        Log.v(TAG, "onNeedUserNameAndPassword");
        // toggling cause we may never get a response from the UI here, but we don't want
        // to notify eveybody that we're not authed.
        _state = AuthState.NOT_AUTHENTICATED;
        Log.v(TAG, "NOT_AUTHENTICATED");
        AuthClient.needUsernameAndPassword(authenticatorResponse);
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
        if (_accountManager == null) {
            _accountManager = AccountManager.get(App.get());
            _accountManager.addOnAccountsUpdatedListener(_accounts_updateListener, new Handler(), false);
        }

        Account[] accounts = _accountManager.getAccountsByType(getAccountType());
        Log.v(TAG, "Found accounts: " + accounts.length);

        // no accounts, ask account manager to add one
        if (accounts.length == 0) {
            AccountManagerFuture<Bundle> future = _accountManager.addAccount(
                    getAccountType(), null, null, null, null, null, new Handler());
            new FutureWaitAsyncTask(_futureWaitAsync_account).execute(future);

        } else if (accounts.length >= 1) {
            //TODO  always gets the first account, should probably present a picker of some kind
            // have an account, now ask for the auth token
            _account = accounts[0];
            requestAuthTokenFromAccountManager();
        }
    }

    /*-****************************-*/
    /*-         Callbacks          -*/
    /*-****************************-*/
    private final OnAccountsUpdateListener _accounts_updateListener = new OnAccountsUpdateListener() {

        @Override
        public void onAccountsUpdated(Account[] accounts) {
            Log.v(TAG, "onAccountsUpdated");
            List<OAuth> auths = OAuth.list();
            String type = getAccountType();
            if (auths == null || auths.size() == 0)
                return;

            for (int j = 0; j < auths.size(); j++) {
                OAuth auth = auths.get(j);
                boolean match = false;
                for (Account account : accounts) {
                    if (account.type.equals(type)) {
                        if (auth.getUsername().equals(account.name)) {
                            match = true;
                            break;
                        }
                    }
                }

                if (!match) {
                    // what now?
                    auth.delete();
                    _authToken = null;
                    _account = null;
                    setState(AuthState.NOT_AUTHENTICATED);
                    requestToken();
                }
            }
        }
    };

    private final FutureWaitAsyncTask.Listener _futureWaitAsync_remove = new FutureWaitAsyncTask.Listener() {
        @Override
        public void onComplete(Object result) {
            Log.v(TAG, "_futureWaitAsync_remove.onComplete");
            setState(AuthState.NOT_AUTHENTICATED);
            AuthClient.requestCommand();
        }

        @Override
        public void onFail(Exception ex) {
            Log.v(TAG, "_futureWaitAsync_remove.onFail");
            setState(AuthState.NOT_AUTHENTICATED);
            AuthClient.requestCommand();
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

            if (result == null) {
                Log.v(TAG, "token response is null");
                onFail(null);
                return;
            }

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
                            && App.get().getString(R.string.login_error_update_app).equals(bundle.getString(AccountManager.KEY_AUTH_FAILED_MESSAGE))) {
                        // account fail, check if app is too old
                        onAppIsOld();
                    } else {
                        Log.v(TAG, "!KEY_AUTH_FAILED_MESSAGE");
                    }
                }
            } else {
                // have token string, get the full token
                Log.v(TAG, "have token");
                _authToken = OAuth.lookup(bundle.getString(AccountManager.KEY_ACCOUNT_NAME));

                if (_authToken == null || _authToken.getAccessToken() == null) {
                    _account = null;
                    _accountManager.invalidateAuthToken(getAccountType(), bundle.getString(AccountManager.KEY_AUTHTOKEN));
                    setState(AuthState.NOT_AUTHENTICATED);
                    _authToken.delete();
                    _authToken = null;
                    return;
                }

                Log.v(TAG, _authToken.toJson().display());
                ProfileClient.get(App.get());
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
