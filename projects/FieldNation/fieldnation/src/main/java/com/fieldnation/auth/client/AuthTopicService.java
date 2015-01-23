package com.fieldnation.auth.client;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.fieldnation.FutureWaitAsyncTask;
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

    // Services
    private AccountManager _accountManager;

    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/

    public AuthTopicService() {
        super();
        setState(STATE_NOT_AUTHENTICATED);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!_isStarted) {
            subscribeAuthCommand(this, 0, TAG, _topicReceiver);
            TopicService.registerListener(this, 0, TAG + ":SYSTEM", Topics.TOPIC_SHUTDOWN, _topics);
            TopicService.registerListener(this, 0, TAG + ":SYSTEM", Topics.TOPIC_NETWORK_DOWN, _topics);
            TopicService.registerListener(this, 0, TAG + ":SYSTEM", Topics.TOPIC_NETWORK_UP, _topics);
            _isStarted = true;
            requestAuthentication(this);
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        TopicService.delete(this, TAG);
        TopicService.delete(this, TAG + ":SHUTDOWN");
        super.onDestroy();
    }

    private String getAccoutnType() {
        return getString(R.string.auth_account_type);
    }

    private void setState(int state) {
        _state = state;
        switch (_state) {
            case STATE_AUTHENTICATED:
                Log.v(TAG, "STATE_AUTHENTICATED");
                break;
            case STATE_AUTHENTICATING:
                Log.v(TAG, "STATE_AUTHENTICATING");
                break;
            case STATE_NOT_AUTHENTICATED:
                Log.v(TAG, "STATE_NOT_AUTHENTICATED");
                break;
            case STATE_REMOVING:
                Log.v(TAG, "STATE_REMOVING");
                break;
        }
    }

    /*-*************************-*/
    /*-         Topics          -*/
    /*-*************************-*/

    private final TopicReceiver _topics = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
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
                dispatchAuthInvalid(AuthTopicService.this);
            }
        }
    };

    private final TopicReceiver _topicReceiver = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            String type = parcel.getString(BUNDLE_PARAM_TYPE);
            Log.v(TAG, "Type: " + type);

            if (_isNetworkDown) {
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
            if (result instanceof Bundle && ((Bundle) result).containsKey("intent")) {
                setState(STATE_AUTHENTICATING);
                Log.v(TAG, "FutureWaitAsyncTask intent");
                Bundle bundle = (Bundle) result;
                Intent intent = bundle.getParcelable("intent");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

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
