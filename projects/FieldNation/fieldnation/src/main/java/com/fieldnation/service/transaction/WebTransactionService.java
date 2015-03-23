package com.fieldnation.service.transaction;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJson;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.data.oauth.OAuth;

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
    private static int THREAD_COUNT = 0;

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
                WebTransaction transaction = WebTransaction.put(this,
                        WebTransaction.Priority.values()[extras.getInt(PARAM_PRIORITY)],
                        extras.getString(PARAM_KEY),
                        extras.getBoolean(PARAM_USE_AUTH),
                        new JsonObject(extras.getByteArray(PARAM_REQUEST)),
                        extras.getString(PARAM_HANDLER_NAME),
                        extras.getByteArray(PARAM_HANDLER_PARAMS));
/*
                if (extras.containsKey(PARAM_TRANSFORM_LIST) && extras.get(PARAM_TRANSFORM_LIST) != null) {
                    Bundle[] transforms = (Bundle[]) extras.getParcelableArray(PARAM_TRANSFORM_LIST);
                    for (int i = 0; i < transforms.length; i++) {
                        Bundle transform = transforms[i];
                        Transform.put(this, transaction.getId(), transform);
                    }
                }
*/
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
        }

        @Override
        public void onAuthenticated(OAuth oauth) {
            Log.v(TAG, "AuthTopicClient.onAuthenticated");
            _auth = oauth;
            Log.v(TAG, _auth.toJson().display());
            startTransaction();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startTransaction() {
        Log.v(TAG, "startTransaction");

        if (_auth == null)
            return;

        WebTransaction next = WebTransaction.getNext(this);
        if (next == null) {
            return;
        }
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
                JsonObject request = trans.getRequest();
                try {
                    if (trans.useAuth()) {
                        if (!request.has(HttpJsonBuilder.PARAM_WEB_HOST)) {
                            request.put(HttpJsonBuilder.PARAM_WEB_HOST, auth.getHost());
                        }
                        request.put(HttpJsonBuilder.PARAM_WEB_PROTOCOL, "https");
                        auth.applyToRequest(request);
                    }
                    HttpResult result = HttpJson.run(context, request);

                    String handler = trans.getHandlerName();
                    WebTransactionHandler.completeTransaction(
                            context,
                            _transactionListener,
                            handler,
                            trans,
                            result);
                    return null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    trans.setState(WebTransaction.State.IDLE);
                    return null;
                }
            }

        }.executeEx(WebTransactionService.this, next, _auth);
    }

    private void finishTransaction() {
        synchronized (TAG) {
            THREAD_COUNT--;
            if (THREAD_COUNT == 0)
                stopSelf();
        }
    }

    private final WebTransactionHandler.Listener _transactionListener = new WebTransactionHandler.Listener() {
        @Override
        public void onComplete(WebTransaction trans) {
            Log.v(TAG, "_transactionListener.onComplete");
            // finish up transaction
            WebTransaction.delete(WebTransactionService.this, trans.getId());
            // fire off the next one
            startTransaction();
            finishTransaction();
        }

        @Override
        public void requeue(WebTransaction trans) {
            trans.setState(WebTransaction.State.IDLE);
            trans.save(WebTransactionService.this);

            startTransaction();
            finishTransaction();
        }

        @Override
        public void onError(WebTransaction trans) {
            Log.v(TAG, "_transactionListener.onError");
            // finish up transaction
/*
            WebTransaction.delete(WebTransactionService.this, trans.getId());
            _currentTransaction = null;
*/
            // fire off the next one
            startTransaction();
            finishTransaction();
        }
    };
}
