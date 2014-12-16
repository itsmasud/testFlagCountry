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

    // Data
    private Account _account = null;
    private boolean _authenticating = false;
    private boolean _removing = false;
    private String _authToken;
    private String _username;

    // Services
    private AccountManager _accountManager;

    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        subscribeAuthCommand(this, 0, TAG, _topicReceiver);

        if (!_authenticating)
            getAccount();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        TopicService.delete(this, 0, TAG);
        super.onDestroy();
    }

    private void requestAuthTokenFromAccountManager() {
        AccountManagerFuture<Bundle> future = _accountManager.getAuthToken(_account, getAccoutnType(), null,
                null, null, null);
        new FutureWaitAsyncTask(_futureWaitAsyncTaskListener).execute(future);
    }

    private String getAccoutnType() {
        return getString(R.string.accounttype);
    }

    private void getAccount() {
        Log.v(TAG, "getAccount()");
        if (_authenticating)
            return;

        if (_removing)
            return;

        Log.v(TAG, "getAccount() not authenticating");

        _authenticating = true;
        if (_accountManager == null)
            _accountManager = AccountManager.get(this);

        Account[] accounts = _accountManager.getAccountsByType(getAccoutnType());
        Log.v(TAG, "Found accounts: " + accounts.length);
        AccountManagerFuture<Bundle> future = null;

        if (accounts.length == 0) {
            future = _accountManager.addAccount(getAccoutnType(), null, null, null, null, null, new Handler());
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

    /*-*****************************-*/
    /*-             Events          -*/
    /*-*****************************-*/


    private TopicReceiver _topicReceiver = new TopicReceiver(new Handler()) {
        @Override
        public void onRegister(int resultCode, String topicId) {
        }

        @Override
        public void onUnregister(int resultCode, String topicId) {
        }

        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            String type = parcel.getString(BUNDLE_PARAM_TYPE);

            if (BUNDLE_PARAM_TYPE_INVALID.equals(type)) {
                if (!_removing) {
                    _accountManager.invalidateAuthToken(getAccoutnType(), _authToken);
                    _username = null;
                    _authToken = null;
                    dispatchAuthInvalid(AuthTopicService.this);
                    requestAuthentication(AuthTopicService.this);
                }
            } else if (BUNDLE_PARAM_TYPE_REQUEST.equals(type)) {
                if (_account != null && !_removing) {
                    requestAuthTokenFromAccountManager();
                } else if (!_authenticating) {
                    if (_username != null && _authToken != null) {
                        dispatchAuthComplete(AuthTopicService.this, _username, _authToken);
                    } else {
                        getAccount();
                    }
                }
            } else if (BUNDLE_PARAM_TYPE_REMOVE.equals(type)) {
                if (!_removing && _account != null) {
                    _removing = true;
                    _authenticating = false;
                    AccountManagerFuture<Boolean> future = _accountManager.removeAccount(_account, null, null);
                    new FutureWaitAsyncTask(_futureWaitAsyncTaskListener).execute(future);
                    _account = null;
                    getAccount();
                }
            } else if (BUNDLE_PARAM_TYPE_COMPLETE.equals(type)) {
                _authenticating = false;
                getAccount();
            }
        }
    };


    private FutureWaitAsyncTask.Listener _futureWaitAsyncTaskListener = new FutureWaitAsyncTask.Listener() {
        @Override
        public void onComplete(Object result) {
            if (result instanceof Bundle && ((Bundle) result).containsKey("intent")) {
                Log.v(TAG, "FutureWaitAsyncTask intent");
                Bundle bundle = (Bundle) result;
                Intent intent = bundle.getParcelable("intent");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else if (!_removing) {
                Log.v(TAG, "FutureWaitAsyncTask not removing");
                Bundle bundle = (Bundle) result;
                Log.v(TAG, "_futureWaitAsyncTaskListener.onComplete()");
                String tokenString = bundle.getString("authtoken");

                // auth is complete
                _authenticating = false;

                // if however, data invalid, need to ask again.
                if (tokenString == null) {
                    if (bundle.containsKey("accountType") && bundle.containsKey("authAccount")) {
                        Log.v(TAG, "FutureWaitAsyncTask, getAccount");
                        getAccount();
                    } else {
                        // todo.. not sure
                        Log.v(TAG, "FutureWaitAsyncTask, should not be here");
                    }
                } else {
                    Log.v(TAG, "FutureWaitAsyncTask, dispatch account");
                    _authToken = tokenString;
                    _username = bundle.getString("authAccount");
                    dispatchAuthComplete(AuthTopicService.this, _username, tokenString);
                }
            } else if (_removing) {
                Log.v(TAG, "FutureWaitAsyncTask removing");
                _removing = false;
                dispatchAuthInvalid(AuthTopicService.this);
            }
        }

        @Override
        public void onFail(Exception ex) {
            dispatchAuthFailed(AuthTopicService.this);
            _authenticating = false;
            _removing = false;
        }
    };

    /*-********************************-*/
    /*-             Topic API          -*/
    /*-********************************-*/
    public static void startService(Context context) {
        Intent intent = new Intent(context, AuthTopicService.class);
        context.startService(intent);
    }

    // function for client to subscribe to authentication events
    public static void subscribeAuthState(Context context, int resultCode, String tag, TopicReceiver topicReceiver) {
        TopicService.registerListener(context, resultCode, tag, TOPIC_AUTH_STATE, topicReceiver);
    }

    private static void subscribeAuthCommand(Context context, int resultCode, String tag, TopicReceiver topicReceiver) {
        TopicService.registerListener(context, resultCode, tag, TOPIC_AUTH_COMMAND, topicReceiver);
    }

    // internal
    private static void dispatchAuthFailed(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_FAILED);

        TopicService.dispatchTopic(context, TOPIC_AUTH_STATE, bundle);
    }

    // internal
    private static void dispatchAuthInvalid(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_INVALID);

        TopicService.dispatchTopic(context, TOPIC_AUTH_STATE, bundle);
    }

    public static void dispatchAuthComplete(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_COMPLETE);

        TopicService.dispatchTopic(context, TOPIC_AUTH_COMMAND, bundle);
    }


    // internal
    private static void dispatchAuthComplete(Context context, String username, String authToken) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_COMPLETE);
        bundle.putString(BUNDLE_PARAM_AUTH_TOKEN, authToken);
        bundle.putString(BUNDLE_PARAM_USERNAME, username);

        TopicService.dispatchTopic(context, TOPIC_AUTH_STATE, bundle);
    }

    //external
    public static void requestAuthRemove(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_REMOVE);

        TopicService.dispatchTopic(context, TOPIC_AUTH_COMMAND, bundle);
    }

    // external
    public static void requestAuthentication(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_REQUEST);

        TopicService.dispatchTopic(context, TOPIC_AUTH_COMMAND, bundle);
    }

    public static void requestAuthInvalid(Context context) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PARAM_TYPE, BUNDLE_PARAM_TYPE_INVALID);

        TopicService.dispatchTopic(context, TOPIC_AUTH_COMMAND, bundle);
    }

}
