package com.fieldnation.service.transaction;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJson;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.auth.OAuth;
import com.fieldnation.utils.misc;

import java.net.UnknownHostException;

/**
 * Created by Michael Carver on 2/27/2015.
 * <p/>
 * this class does two things.
 * 1) Accepts transactions from the rest of the application
 * 2) processes transactions from the queue until they are complete
 */
public class WebTransactionService extends Service implements WebTransactionConstants {
    private static final String TAG = "WebTransactionService";

    private OAuth _auth;
    private AuthTopicClient _authTopicClient;
    private static final int MAX_THREAD_COUNT = 10;
    private static int THREAD_COUNT = 0;
    private boolean _isAuthenticated = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");

        _authTopicClient = new AuthTopicClient(_authTopic_listener);
        _authTopicClient.connect(this);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        _authTopicClient.disconnect(this);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        // get transaction and transforms from intent, push into the database
        // kick off the next transaction if not running
        if (intent != null && intent.getExtras() != null) {
            try {
                Bundle extras = intent.getExtras();

                if (extras.containsKey(PARAM_KEY) && WebTransaction.keyExists(this, extras.getString(PARAM_KEY))) {
                    Log.v(TAG, "Duplicate key: " + extras.getString(PARAM_KEY));
                    // TODO, need to send a response!?
                    return START_STICKY;
                }

                WebTransaction transaction = WebTransaction.put(this,
                        WebTransaction.Priority.values()[extras.getInt(PARAM_PRIORITY)],
                        extras.getString(PARAM_KEY),
                        extras.getBoolean(PARAM_USE_AUTH),
                        extras.getByteArray(PARAM_REQUEST),
                        extras.getString(PARAM_HANDLER_NAME),
                        extras.getByteArray(PARAM_HANDLER_PARAMS));

                if (extras.containsKey(PARAM_TRANSFORM_LIST) && extras.get(PARAM_TRANSFORM_LIST) != null) {
                    Parcelable[] transforms = extras.getParcelableArray(PARAM_TRANSFORM_LIST);
                    for (int i = 0; i < transforms.length; i++) {
                        Bundle transform = (Bundle) transforms[i];
                        Transform.put(this, transaction.getId(), transform);
                    }
                }

                transaction.setState(WebTransaction.State.IDLE);
                transaction.save(this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        startTransaction();

        return START_STICKY;
    }

    private final AuthTopicClient.Listener _authTopic_listener = new AuthTopicClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "AuthTopicClient.onConnected");
            _authTopicClient.registerAuthState();
            _authTopicClient.registerAuthState();
        }

        @Override
        public void onAuthenticated(OAuth oauth) {
            Log.v(TAG, "AuthTopicClient.onAuthenticated");
            _auth = oauth;
            Log.v(TAG, _auth.toJson().display());
            _isAuthenticated = true;
            startTransaction();
        }

        @Override
        public void onNotAuthenticated() {
            _isAuthenticated = false;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startTransaction() {
        Log.v(TAG, "startTransaction");

        if (_auth == null || !_isAuthenticated)
            return;

        synchronized (TAG) {
            if (THREAD_COUNT >= MAX_THREAD_COUNT) {
                Log.v(TAG, "startTransaction leaving: " + THREAD_COUNT);
                return;
            }
        }

        WebTransaction next = WebTransaction.getNext(this);
        if (next == null) {
            return;
        }

        if (next.getKey() != null)
            Log.v(TAG, next.getKey());

        new AsyncTaskEx<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                synchronized (TAG) {
                    THREAD_COUNT++;
                }
                Context context = (Context) params[0];
                WebTransaction trans = (WebTransaction) params[1];
                OAuth auth = (OAuth) params[2];

                // at some point call the web service
                JsonObject request = trans.getRequest().copy();
                try {
                    if (trans.useAuth()) {
                        if (!request.has(HttpJsonBuilder.PARAM_WEB_HOST)) {
                            request.put(HttpJsonBuilder.PARAM_WEB_HOST, auth.getHost());
                        }
                        request.put(HttpJsonBuilder.PARAM_WEB_PROTOCOL, "https");
                        auth.applyToRequest(request);
                    }
                    Log.v(TAG, request.display());
                    HttpResult result = HttpJson.run(context, request);

                    try {
                        Log.v(TAG, result.getResponseCode() + "");
                        Log.v(TAG, result.getResponseMessage());
                        Log.v(TAG, result.getResultsAsString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    if (result.getResultsAsString().equals("You must provide a valid OAuth token to make a request")) {
                        Log.v(TAG, "Reauth");
                        _isAuthenticated = false;
                        AuthTopicClient.dispatchInvalidateCommand(context);
                        requeue(context, trans);
                        AuthTopicClient.dispatchRequestCommand(context);
                        return null;
                    } else if (result.getResponseCode() == 400) {
                        // Bad request
                        // need to report this

                        // need to re-auth?
                        Thread.sleep(5000);
                        requeue(context, trans);
                        AuthTopicClient.dispatchRequestCommand(context);
                    } else if (result.getResponseCode() == 401) {
                        Log.v(TAG, "Reauth");
                        _isAuthenticated = false;
                        AuthTopicClient.dispatchInvalidateCommand(context);
                        requeue(context, trans);
                        AuthTopicClient.dispatchRequestCommand(context);
                        return null;
                    } else if (result.getResponseCode() == 404) {
                        Thread.sleep(5000);
                        requeue(context, trans);
                        AuthTopicClient.dispatchRequestCommand(context);
                        return null;
                    } else if (result.getResponseCode() == 502) {
                        Thread.sleep(5000);
                        requeue(context, trans);
                        AuthTopicClient.dispatchRequestCommand(context);
                        return null;
                    }

                    String handler = trans.getHandlerName();
                    if (!misc.isEmptyOrNull(handler)) {
                        WebTransactionHandler.Result wresult = WebTransactionHandler.completeTransaction(
                                context,
                                handler,
                                trans,
                                result);

                        switch (wresult) {
                            case ERROR:
                                WebTransaction.delete(context, trans.getId());
                                Transform.deleteTransaction(context, trans.getId());
                                startTransaction();
                                break;
                            case FINISH:
                                WebTransaction.delete(context, trans.getId());
                                Transform.deleteTransaction(context, trans.getId());
                                startTransaction();
                                break;
                            case REQUEUE:
                                requeue(context, trans);
                                startTransaction();
                                break;
                        }
                    }
                    return null;
                } catch (UnknownHostException ex) {
                    // probably offline
                    ex.printStackTrace();
                    requeue(context, trans);
                    return null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    requeue(context, trans);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Object o) {
                synchronized (TAG) {
                    THREAD_COUNT--;
                }
            }
        }.executeEx(WebTransactionService.this, next, _auth);
    }

    private void finishTransaction(WebTransaction trans) {
        Log.v(TAG, "finishTransaction(" + trans.getId() + ")");
    }

    private static final void requeue(Context context, WebTransaction trans) {
        trans.setState(WebTransaction.State.IDLE);
        trans.save(context);

    }
}
