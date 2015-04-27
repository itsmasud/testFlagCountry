package com.fieldnation.service.transaction;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.UniqueTag;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJson;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.auth.OAuth;
import com.fieldnation.utils.misc;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

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
    private boolean _isAuthenticated = false;
    private List<WorkerThread> _threads = new LinkedList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");

        _authTopicClient = new AuthTopicClient(_authTopic_listener);
        _authTopicClient.connect(this);

        for (int i = 0; i < MAX_THREAD_COUNT; i++) {
            _threads.add(new WorkerThread(this));
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        _authTopicClient.disconnect(this);

        for (int i = 0; i < _threads.size(); i++) {
            _threads.get(i).finish();
        }

        for (int i = 0; i < _threads.size(); i++) {
            WorkerThread thread = _threads.get(i);

            try {
                thread.join(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (thread.isAlive()) {
                thread.interrupt();
            }
        }

        _threads.clear();

        super.onDestroy();
    }

    private void setAuth(OAuth auth) {
        synchronized (TAG) {
            _auth = auth;
        }
    }

    private OAuth getAuth() {
        synchronized (TAG) {
            return _auth;
        }
    }

    private boolean allowSync() {
        // TODO need to calculate by collecting config information and compare to phone state
        return true;
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
                        Priority.values()[extras.getInt(PARAM_PRIORITY)],
                        extras.getString(PARAM_KEY),
                        extras.getBoolean(PARAM_USE_AUTH),
                        extras.getBoolean(PARAM_IS_SYNC),
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
            setAuth(oauth);
            _isAuthenticated = true;
        }

        @Override
        public void onNotAuthenticated() {
            _isAuthenticated = false;
        }
    };

    class WorkerThread extends Thread {
        private String TAG = UniqueTag.makeTag("WorkerThread");
        private Context context;
        private boolean _running = true;

        public WorkerThread(Context context) {
            super();
            setName(TAG);
            this.context = context;
            start();
        }

        private void sleep() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void finish() {
            _running = false;
        }

        @Override
        public void run() {
            Log.v(TAG, "start");
            while (_running) {

                boolean allowSync = allowSync();

                WebTransaction trans = WebTransaction.getNext(context, allowSync);

                if (trans == null) {
                    Log.v(TAG, "skip no transaction");
                    sleep();
                    continue;
                }

                // at some point call the web service
                JsonObject request = trans.getRequest().copy();
                try {
                    if (trans.useAuth()) {
                        OAuth auth = getAuth();
                        if (auth == null || !_isAuthenticated) {
                            Log.v(TAG, "skip no auth");
                            trans.requeue(context);
                            sleep();
                            continue;
                        }

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
                        trans.requeue(context);
                        AuthTopicClient.dispatchRequestCommand(context);
                        continue;
                    } else if (result.getResponseCode() == 400) {
                        // Bad request
                        // need to report this

                        // need to re-auth?
                        Thread.sleep(5000);
                        trans.requeue(context);
                        AuthTopicClient.dispatchRequestCommand(context);
                    } else if (result.getResponseCode() == 401) {
                        Log.v(TAG, "Reauth");
                        _isAuthenticated = false;
                        AuthTopicClient.dispatchInvalidateCommand(context);
                        trans.requeue(context);
                        AuthTopicClient.dispatchRequestCommand(context);
                        continue;
                    } else if (result.getResponseCode() == 404) {
                        Thread.sleep(5000);
                        trans.requeue(context);
                        AuthTopicClient.dispatchRequestCommand(context);
                        continue;
                    } else if (result.getResponseCode() == 502) {
                        Thread.sleep(5000);
                        trans.requeue(context);
                        AuthTopicClient.dispatchRequestCommand(context);
                        continue;
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
                                break;
                            case FINISH:
                                WebTransaction.delete(context, trans.getId());
                                Transform.deleteTransaction(context, trans.getId());
                                break;
                            case REQUEUE:
                                trans.requeue(context);
                                break;
                        }
                    }
                    continue;
                } catch (UnknownHostException ex) {
                    // probably offline
                    ex.printStackTrace();
                    trans.requeue(context);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    trans.requeue(context);
                }
            }
            Log.v(TAG, "finish");
        }
    }
}
