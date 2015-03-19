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

    private static final int STATE_NOT_AUTHENTICATED = 0;
    private static final int STATE_AUTHENTICATING = 1;
    private static final int STATE_AUTHENTICATED = 2;
    private static final int STATE_REMOVING = 3;

    // Data
    private boolean _isStarted = false;
    private Account _account = null;
    private OAuth _authToken = null;
    private int _state;
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
        setState(STATE_NOT_AUTHENTICATED);
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate");
        super.onCreate();
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

    private void setState(int state) {
        _state = state;
        Log.v(TAG, getStateString());
    }

    private String getStateString() {
        switch (_state) {
            case STATE_AUTHENTICATED:
                return "STATE_AUTHENTICATED";
            case STATE_AUTHENTICATING:
                return "STATE_AUTHENTICATING";
            case STATE_NOT_AUTHENTICATED:
                return "STATE_NOT_AUTHENTICATED";
            case STATE_REMOVING:
                return "STATE_REMOVING";
        }
        return "";
    }

    private final AuthTopicClient.Listener _authClientListener = new AuthTopicClient.Listener() {
        @Override
        public void onConnected() {
            _authTopicClient.registerRequestAuth();
            _authTopicClient.registerInvalidAuth();
            _authTopicClient.registerFailedAuth();
        }

        @Override
        public void onAuthRequested() {
            requestToken();
        }

        @Override
        public void onAuthFailed() {
        }

        @Override
        public void onAuthInvalid() {
            invalidateToken();
        }
    };

    /*-*****************************-*/
    /*-         Commands            -*/
    /*-*****************************-*/
    private String getAccountType() {
        return getString(R.string.auth_account_type);
    }

    private void invalidateToken() {
        if (_state == STATE_AUTHENTICATED) {
            setState(STATE_NOT_AUTHENTICATED);
            _accountManager.invalidateAuthToken(getAccountType(), _authToken.getAccessToken());
            _authToken.delete(this);
            _authToken = null;
        }
    }

    private void requestToken() {
        if (_state == STATE_AUTHENTICATING) {
            // TODO, what do we do?
        } else {
            Log.v(TAG, "handleRequest STATE_AUTHENTICATED");
            if (_authToken != null) {
                Log.v(TAG, "handleRequest onAuthComplete");
                onAuthComplete(_authToken);
            } else if (_account != null) {
                Log.v(TAG, "handleRequest requestAuthTokenFromAccountManager");
                // don't have an auth token, but we are authenticated, so ask AOS for the token
                requestAuthTokenFromAccountManager();
            } else {
                setState(STATE_NOT_AUTHENTICATED);
                getAccount();
            }
        }
    }

    private void removeAccount() {
        if (_state == STATE_AUTHENTICATED) {
            setState(STATE_REMOVING);
            AccountManagerFuture<Boolean> future = _accountManager.removeAccount(_account, null, null);
            new FutureWaitAsyncTask(_futureWaitAsync_remove).execute(future);
            _account = null;
        } else if (_state == STATE_NOT_AUTHENTICATED) {
            onAuthInvalid();
        } else {
            //TODO probably need a workaround for this
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    removeAccount();
                }
            }, 1000);
        }
    }

    /**
     * called when the auth service is complete
     */
    private void accountCreated() {
        setState(STATE_AUTHENTICATING);
        getAccount();
    }

    private void cancelAuth() {
        setState(STATE_NOT_AUTHENTICATED);
        _account = null;
        //onAuthInvalid(AuthTopicService.this);
    }

    /*-*********************************-*/
    /*-         Internal Stuff          -*/
    /*-*********************************-*/
    private void onAuthComplete(OAuth oauth) {
        AuthTopicClient.dispatchHaveAuth(this, oauth);
    }

    private void onAppIsOld() {
        GlobalTopicClient.dispatchUpdateApp(this);
    }

    private void onAuthInvalid() {
        AuthTopicClient.dispatchInvalidAuth(this);
    }

    private void onAuthFailed() {
        AuthTopicClient.dispatchFailedAuth(this);
    }

    private void onNeedUserNameAndPassword(Parcelable authenticatorResponse) {
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
        setState(STATE_AUTHENTICATING);
        AccountManagerFuture<Bundle> future = _accountManager.getAuthToken(
                _account, getAccountType(), null, null, null, null);
        new FutureWaitAsyncTask(_futureWaitAsync_token).execute(future);
    }

    /**
     * Gets the account from AOS
     */
    private void getAccount() {
        Log.v(TAG, "getAccount()");
        if (_state == STATE_REMOVING) {
            Log.v(TAG, "removing");
            return;
        }

        Log.v(TAG, "getAccount() not authenticating");

        setState(STATE_AUTHENTICATING);
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
            Log.v(TAG, "FutureWaitAsyncTask unknown " + _state);
            setState(STATE_NOT_AUTHENTICATED);
        }

        @Override
        public void onFail(Exception ex) {

        }
    };

    private final FutureWaitAsyncTask.Listener _futureWaitAsync_account = new FutureWaitAsyncTask.Listener() {
        @Override
        public void onComplete(Object result) {
            boolean isAuthenticatorResponse = false;

            if (result instanceof Bundle) {
                isAuthenticatorResponse = ((Bundle) result).containsKey(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
            }

            if (isAuthenticatorResponse) {
                setState(STATE_AUTHENTICATING);
                Log.v(TAG, "FutureWaitAsyncTask intent");
                Bundle bundle = (Bundle) result;
                // need to get username and password
                onNeedUserNameAndPassword(bundle.getParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE));
                //startActivity(intent);
            } else {
                // TODO oh shit!
            }
        }

        @Override
        public void onFail(Exception ex) {

        }
    };

    private final FutureWaitAsyncTask.Listener _futureWaitAsync_token = new FutureWaitAsyncTask.Listener() {
        @Override
        public void onComplete(Object result) {
            Log.v(TAG, "FutureWaitAsyncTask not removing");
            Bundle bundle = (Bundle) result;
            Log.v(TAG, "_futureWaitAsyncTaskListener.onComplete()");
            String tokenString = bundle.getString(AccountManager.KEY_AUTHTOKEN);

            // auth is complete
            // if however, data invalid, need to ask again.
            if (tokenString == null) {
                if (bundle.containsKey(AccountManager.KEY_ACCOUNT_TYPE) && bundle.containsKey(AccountManager.KEY_ACCOUNT_NAME)) {
                    // account has been added, but no key yet
                    Log.v(TAG, "FutureWaitAsyncTask, getAccount");
                    setState(STATE_AUTHENTICATING);
                    getAccount();
                } else {
                    Log.v(TAG, "FutureWaitAsyncTask, auth failure");
                    // todo.. not sure
                    setState(STATE_NOT_AUTHENTICATED);
                    if (bundle.containsKey(AccountManager.KEY_AUTH_FAILED_MESSAGE)
                            && getString(R.string.login_error_update_app).equals(bundle.getString(AccountManager.KEY_AUTH_FAILED_MESSAGE))) {
                        // account fail, check if app is too old
                        onAppIsOld();
                    } else {
                        onAuthInvalid();
                    }
                }
            } else {
                // have token string, get the full token
                Log.v(TAG, "FutureWaitAsyncTask, dispatch account");
                _authToken = bundle.getParcelable(OAuth.KEY_OAUTH);
                setState(STATE_AUTHENTICATED);
                onAuthComplete(_authToken);
            }
        }

        @Override
        public void onFail(Exception ex) {

        }
    };


    private final FutureWaitAsyncTask.Listener _futureWaitAsyncTaskListener2 = new FutureWaitAsyncTask.Listener() {
        @Override
        public void onComplete(Object result) {
            boolean isAuthenticatorResponse = false;

            if (result instanceof Bundle) {
                isAuthenticatorResponse = ((Bundle) result).containsKey(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
            }

            if (isAuthenticatorResponse) {
                setState(STATE_AUTHENTICATING);
                Log.v(TAG, "FutureWaitAsyncTask intent");
                Bundle bundle = (Bundle) result;
                // need to get username and password
                onNeedUserNameAndPassword(bundle.getParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE));
                //startActivity(intent);

            } else if (_state == STATE_AUTHENTICATED || _state == STATE_AUTHENTICATING) {
            } else {
            }
        }

        @Override
        public void onFail(Exception ex) {
            setState(STATE_NOT_AUTHENTICATED);
            onAuthFailed();
        }
    };

    /*-*****************************-*/
    /*-         Old Stuff           -*/
    /*-*****************************-*/
    /*-*************************-*/
    /*-         Topics          -*/
    /*-*************************-*/
/*
    private final TopicReceiver _topics = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            Log.v(TAG, "STATE: " + getStateString());
            Log.v(TAG, "_topics:" + topicId);
            if (Topics.TOPIC_NETWORK_DOWN.equals(topicId)) {
                _isNetworkDown = true;
                setState(STATE_NOT_AUTHENTICATED);
//                dispatchNoNetwork(AuthTopicService.this);
            } else if (Topics.TOPIC_NETWORK_UP.equals(topicId)) {
                _isNetworkDown = false;
                setState(STATE_NOT_AUTHENTICATED);
                requestAuthentication(AuthTopicService.this);
            } else if (Topics.TOPIC_SHUTDOWN.equals(topicId)) {
                setState(STATE_NOT_AUTHENTICATED);
                _account = null;
                _accountManager = null;
                _isShuttingDown = true;
                stopSelf();
            }
        }
    };
*/

/*
    private final TopicReceiver _topicReceiver = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            Log.v(TAG, "STATE: " + getStateString());
            if (_isShuttingDown) {
                return;
            }
            String type = parcel.getString(BUNDLE_PARAM_TYPE);
            Log.v(TAG, "Type: " + type);

            if (_isNetworkDown) {
                Log.v(TAG, "Network Down");
//                dispatchNoNetwork(AuthTopicService.this);
                return;
            }

            if (BUNDLE_PARAM_TYPE_INVALID.equals(type)) {
                handleInvalid();
            } else if (BUNDLE_PARAM_TYPE_REQUEST.equals(type)) {
                handleRequest();
            } else if (BUNDLE_PARAM_TYPE_REMOVE.equals(type)) {
                handleRemove();
            } else if (BUNDLE_PARAM_TYPE_COMPLETE.equals(type)) {
                handleComplete();
            } else if (BUNDLE_PARAM_TYPE_CANCELLED.equals(type)) {
                handleCancelled();
            }
        }
    };
*/


    /*-*****************************-*/
    /*-             Events          -*/
    /*-*****************************-*/


    /*-********************************-*/
    /*-             Topic API          -*/
    /*-********************************-*/
/*
    private static void startService(Context context) {
        if (context == null)
            return;

        Intent intent = new Intent(context, AuthTopicService.class);
        context.startService(intent);
    }

    // function for client to subscribe to authentication events
    public static void subscribeAuthState(Context context, int resultCode, String tag, TopicReceiver topicReceiver) {
        if (context == null)
            return;

        startService(context);
        TopicService.registerListener(context, resultCode, tag, TOPIC_AUTH_STATE, topicReceiver);
    }

    public static void unsubscribeAuthState(Context context, String tag) {
        if (context == null)
            return;

        startService(context);
        TopicService.unRegisterListener(context, 0, tag, TOPIC_AUTH_STATE);
    }

    private static void subscribeAuthCommand(Context context, int resultCode, String tag, TopicReceiver topicReceiver) {
        if (context == null)
            return;

        TopicService.registerListener(context, resultCode, tag, TOPIC_AUTH_COMMAND, topicReceiver);
    }

*/
/*
    private static void dispatchNoNetwork(Context context) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_NO_NETWORK);

        TopicService.dispatchTopic(context, TOPIC_AUTH_STATE, bundle);
    }
*//*


    // internal
    private static void dispatchAuthFailed(Context context) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_FAILED);

        TopicService.dispatchTopic(context, TOPIC_AUTH_STATE, bundle);
    }

    // internal
    private static void onAuthInvalid(Context context) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_INVALID);

        TopicService.dispatchTopic(context, TOPIC_AUTH_STATE, bundle);
    }
    public static void subscribeNeedUsernameAndPassword(Context context, String tag, TopicReceiver topicReceiver) {
        TopicService.registerListener(context, 0, tag, TOPIC_AUTH_STARTUP, topicReceiver);
    }
    public static void dispatchNeedUsernameAndPassword(Context context, Parcelable parcel) {
        Log.v(TAG, "dispatchNeedUsernameAndPassword");
        if (context == null)
            return;

        startService(context);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_NEED_PASSWORD);
        bundle.putParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, parcel);

        TopicService.dispatchTopic(context, TOPIC_AUTH_STARTUP, bundle, true);
    }

    public static void dispatchGettingUsernameAndPassword(Context context) {
        Log.v(TAG, "dispatchGettingUsernameAndPassword");

        if (context == null)
            return;

        startService(context);

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_NEED_PASSWORD);

        TopicService.dispatchTopic(context, TOPIC_AUTH_STARTUP, bundle, true);
    }

    public static void onAuthComplete(Context context) {
        if (context == null)
            return;

        startService(context);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_COMPLETE);

        TopicService.dispatchTopic(context, TOPIC_AUTH_COMMAND, bundle);
    }

    public static void dispatchAuthCancelled(Context context) {
        if (context == null)
            return;

        startService(context);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_CANCELLED);

        TopicService.dispatchTopic(context, TOPIC_AUTH_COMMAND, bundle);
    }

    // internal
    private static void onAuthComplete(Context context, OAuth authToken) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_COMPLETE);
        bundle.putParcelable(BUNDLE_PARAM_AUTH_TOKEN, authToken);

        TopicService.dispatchTopic(context, TOPIC_AUTH_STATE, bundle);
    }

    //external
    public static void requestAuthRemove(Context context) {
        if (context == null)
            return;

        startService(context);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_REMOVE);

        TopicService.dispatchTopic(context, TOPIC_AUTH_COMMAND, bundle);
    }

    // external
    public static void requestAuthentication(Context context) {
        if (context == null)
            return;

        startService(context);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_REQUEST);

        TopicService.dispatchTopic(context, TOPIC_AUTH_COMMAND, bundle);
    }

    public static void requestAuthInvalid(Context context) {
        if (context == null)
            return;

        startService(context);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_INVALID);

        TopicService.dispatchTopic(context, TOPIC_AUTH_COMMAND, bundle);
    }
*/

}
