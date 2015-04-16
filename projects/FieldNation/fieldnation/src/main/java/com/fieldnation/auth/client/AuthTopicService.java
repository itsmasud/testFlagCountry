package com.fieldnation.auth.client;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;

import com.fieldnation.FutureWaitAsyncTask;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.topics.TopicReceiver;
import com.fieldnation.topics.TopicService;
import com.fieldnation.topics.Topics;

/**
 * Created by Michael on 12/15/2014.
 */
public class AuthTopicService extends Service {
    private static final String TAG = "auth.client.AuthTopicService";

    // Topics
    public static final String TOPIC_AUTH_STATE = TAG + ":TOPIC_AUTH_STATE";
    public static final String TOPIC_AUTH_COMMAND = TAG + ":TOPIC_AUTH_COMMAND";
    public static final String TOPIC_AUTH_STARTUP = TAG + ":TOPIC_AUTH_STARTUP";

    // Params
    public static final String BUNDLE_PARAM_AUTH_TOKEN = "BUNDLE_PARAM_AUTH_TOKEN";
    public static final String BUNDLE_PARAM_USERNAME = "BUNDLE_PARAM_USERNAME";

    // Types
    public static final String BUNDLE_PARAM_TYPE = "BUNDLE_PARAM_TYPE";
    public static final String BUNDLE_PARAM_TYPE_REQUEST = "BUNDLE_PARAM_TYPE_REQUEST";
    public static final String BUNDLE_PARAM_TYPE_INVALID = "BUNDLE_PARAM_TYPE_INVALID";
    public static final String BUNDLE_PARAM_TYPE_FAILED = "BUNDLE_PARAM_TYPE_FAILED";
    public static final String BUNDLE_PARAM_TYPE_COMPLETE = "BUNDLE_PARAM_TYPE_COMPLETE";
    public static final String BUNDLE_PARAM_TYPE_REMOVE = "BUNDLE_PARAM_TYPE_REMOVE";
    public static final String BUNDLE_PARAM_TYPE_CANCELLED = "BUNDLE_PARAM_TYPE_CANCELLED";
    public static final String BUNDLE_PARAM_TYPE_NO_NETWORK = "BUNDLE_PARAM_TYPE_NO_NETWORK";
    public static final String BUNDLE_PARAM_TYPE_NEED_PASSWORD = "BUNDLE_PARAM_TYPE_NEED_PASSWORD";


    private static final int STATE_NOT_AUTHENTICATED = 0;
    private static final int STATE_AUTHENTICATING = 1;
    private static final int STATE_AUTHENTICATED = 2;
    private static final int STATE_REMOVING = 3;

    // Data
    private Account _account = null;
    private String _authToken;
    private String _username;
    private int _state;

    private boolean _isNetworkDown = false;
    private boolean _isStarted = false;
    private boolean _isShuttingDown = false;

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
        if (intent == null) {
            return START_STICKY;
        }
        if (!_isStarted) {
            subscribeAuthCommand(this, 0, TAG, _topicReceiver);
            TopicService.registerListener(this, 0, TAG + ":SYSTEM", Topics.TOPIC_SHUTDOWN, _topics);
            TopicService.registerListener(this, 0, TAG + ":SYSTEM", Topics.TOPIC_NETWORK_DOWN, _topics);
            TopicService.registerListener(this, 0, TAG + ":SYSTEM", Topics.TOPIC_NETWORK_UP, _topics);
            _isStarted = true;
            //requestAuthentication(this);
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
        TopicService.delete(this, TAG);
        TopicService.delete(this, TAG + ":SYSTEM");
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            super.onTaskRemoved(rootIntent);

        _isShuttingDown = true;
    }

    private String getAccoutnType() {
        return getString(R.string.auth_account_type);
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

    /*-*************************-*/
    /*-         Topics          -*/
    /*-*************************-*/

    private final TopicReceiver _topics = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            Log.v(TAG, "STATE: " + getStateString());
            Log.v(TAG, "_topics:" + topicId);
            if (Topics.TOPIC_NETWORK_DOWN.equals(topicId)) {
                _isNetworkDown = true;
                setState(STATE_NOT_AUTHENTICATED);
                requestAuthentication(AuthTopicService.this);
            } else if (Topics.TOPIC_NETWORK_UP.equals(topicId) && _isNetworkDown) {
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


    /*-*****************************-*/
    /*-             Events          -*/
    /*-*****************************-*/


    private void handleInvalid() {
        if (_state == STATE_AUTHENTICATED) {
            setState(STATE_NOT_AUTHENTICATED);
            _accountManager.invalidateAuthToken(getAccoutnType(), _authToken);
            _username = null;
            _authToken = null;
            dispatchAuthInvalid(AuthTopicService.this);
            requestAuthentication(AuthTopicService.this);
        }
    }

    private void handleRequest() {
        if (_isNetworkDown) {
            Log.v(TAG, "handleRequest._isNetworkDown");
//            dispatchNoNetwork(this);
            return;
        }

        if (_state == STATE_AUTHENTICATED) {
            Log.v(TAG, "handleRequest STATE_AUTHENTICATED");
            if (_authToken == null) {
                Log.v(TAG, "handleRequest requestAuthTokenFromAccountManager");
                requestAuthTokenFromAccountManager();
            } else {
                Log.v(TAG, "handleRequest dispatchAuthComplete");
                dispatchAuthComplete(AuthTopicService.this, _username, _authToken);
            }
        } else if (_state == STATE_NOT_AUTHENTICATED) {
            Log.v(TAG, "handleRequest STATE_NOT_AUTHENTICATED");
            getAccount();
        } else {
            Log.v(TAG, "handleRequest NA :" + _state);
        }
    }

    private void requestAuthTokenFromAccountManager() {
        setState(STATE_AUTHENTICATING);
        AccountManagerFuture<Bundle> future = _accountManager.getAuthToken(
                _account, getAccoutnType(), null, null, null, null);
        new FutureWaitAsyncTask(_futureWaitAsyncTaskListener).execute(future);
    }

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

        Account[] accounts = _accountManager.getAccountsByType(getAccoutnType());
        Log.v(TAG, "Found accounts: " + accounts.length);
        AccountManagerFuture<Bundle> future = null;

        if (accounts.length == 0) {
            future = _accountManager.addAccount(
                    getAccoutnType(), null, null, null, null, null, new Handler());

        } else if (accounts.length >= 1) {
            //TODO  always gets the first account, should probbaly present a picker of some kind
            _account = accounts[0];
        }

        if (future != null) {
            // new
            Log.v(TAG, "got future");
            new FutureWaitAsyncTask(_futureWaitAsyncTaskListener).execute(future);
        }

        if (_account != null) {
            Log.v(TAG, "got account");
            requestAuthTokenFromAccountManager();
        }
    }

    private void handleRemove() {
        if (_state == STATE_AUTHENTICATED) {
            setState(STATE_REMOVING);
            AccountManagerFuture<Boolean> future = _accountManager.removeAccount(_account, null, null);
            new FutureWaitAsyncTask(_futureWaitAsyncTaskListener).execute(future);
            _account = null;
        } else if (_state == STATE_NOT_AUTHENTICATED) {
            dispatchAuthInvalid(this);
        } else {
            //TODO probably need a workaround for this
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    handleRemove();
                }
            }, 1000);
        }
    }

    /**
     * called when the auth service is complete
     */
    private void handleComplete() {
        setState(STATE_AUTHENTICATING);
        getAccount();
    }

    private void handleCancelled() {
        setState(STATE_NOT_AUTHENTICATED);
        _account = null;
        //dispatchAuthInvalid(AuthTopicService.this);
    }

    private final FutureWaitAsyncTask.Listener _futureWaitAsyncTaskListener = new FutureWaitAsyncTask.Listener() {
        @Override
        public void onComplete(Object result) {
            if (_isShuttingDown)
                return;

            if (result instanceof Bundle && ((Bundle) result).containsKey(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)) {
                setState(STATE_AUTHENTICATING);
                Log.v(TAG, "FutureWaitAsyncTask intent");
                Bundle bundle = (Bundle) result;
                dispatchNeedUsernameAndPassword(AuthTopicService.this, bundle.getParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE));
                //startActivity(intent);

            } else if (_state == STATE_AUTHENTICATED || _state == STATE_AUTHENTICATING) {
                Log.v(TAG, "FutureWaitAsyncTask not removing");
                Bundle bundle = (Bundle) result;
                Log.v(TAG, "_futureWaitAsyncTaskListener.onComplete()");
                String tokenString = bundle.getString("authtoken");

                // auth is complete
                // if however, data invalid, need to ask again.
                if (tokenString == null) {
                    if (bundle.containsKey("accountType") && bundle.containsKey("authAccount")) {
                        Log.v(TAG, "FutureWaitAsyncTask, getAccount");
                        setState(STATE_AUTHENTICATING);
                        getAccount();
                    } else {
                        Log.v(TAG, "FutureWaitAsyncTask, auth failure");
                        // todo.. not sure
                        setState(STATE_NOT_AUTHENTICATED);
                        if (bundle.containsKey(AccountManager.KEY_AUTH_FAILED_MESSAGE)
                                && getString(R.string.login_error_update_app).equals(bundle.getString(AccountManager.KEY_AUTH_FAILED_MESSAGE))) {
                            Topics.dispatchNeedUpdate(AuthTopicService.this);
                        }
                    }
                } else {
                    Log.v(TAG, "FutureWaitAsyncTask, dispatch account");
                    _authToken = tokenString;
                    _username = bundle.getString("authAccount");
                    setState(STATE_AUTHENTICATED);
                    dispatchAuthComplete(AuthTopicService.this, _username, tokenString);
                }
            } else if (_state == STATE_REMOVING) {
                Log.v(TAG, "FutureWaitAsyncTask removing");
                setState(STATE_NOT_AUTHENTICATED);
                dispatchAuthInvalid(AuthTopicService.this);
                requestAuthentication(AuthTopicService.this);
            } else {
                Log.v(TAG, "FutureWaitAsyncTask unknown " + _state);
            }
        }

        @Override
        public void onFail(Exception ex) {
            if (_isShuttingDown)
                return;

            setState(STATE_NOT_AUTHENTICATED);
            dispatchAuthFailed(AuthTopicService.this);
        }
    };

    /*-********************************-*/
    /*-             Topic API          -*/
    /*-********************************-*/
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

/*
    private static void dispatchNoNetwork(Context context) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_NO_NETWORK);

        TopicService.dispatchTopic(context, TOPIC_AUTH_STATE, bundle);
    }
*/

    // internal
    private static void dispatchAuthFailed(Context context) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_FAILED);

        TopicService.dispatchTopic(context, TOPIC_AUTH_STATE, bundle);
    }

    // internal
    private static void dispatchAuthInvalid(Context context) {
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

    public static void dispatchAuthComplete(Context context) {
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
    private static void dispatchAuthComplete(Context context, String username, String authToken) {
        if (context == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_COMPLETE);
        bundle.putString(BUNDLE_PARAM_AUTH_TOKEN, authToken);
        bundle.putString(BUNDLE_PARAM_USERNAME, username);

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

}
