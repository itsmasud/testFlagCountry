package com.fieldnation.service.transaction;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import com.fieldnation.auth.client.AuthTopicReceiver;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.webclient.WebClient;
import com.fieldnation.service.transaction.handlers.WebTransactionHandler;

import java.text.ParseException;

/**
 * Created by Michael Carver on 2/27/2015.
 * <p/>
 * this class does two things.
 * 1) Accepts transactions from the rest of the application
 * 2) processes transactions from the queue until they are complete
 */
public class WebTransactionService extends Service implements WebTransactionConstants {
    private static final String TAG = "service.transaction.TransactionService";

    private WebTransaction _currentTransaction = null;
    private WebClient _webService = null;

    @Override
    public void onCreate() {
        super.onCreate();

        AuthTopicService.subscribeAuthState(this, 0, TAG, _topicReceiver);
    }

    @Override
    public void onDestroy() {
        AuthTopicService.unsubscribeAuthState(this, TAG);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // get transaction and transforms from intent, push into the database
        // kick off the next transaction if not running
        if (intent != null && intent.getExtras() != null) {
            try {
                Bundle extras = intent.getExtras();
                WebTransaction transaction = WebTransaction.put(this,
                        WebTransaction.Priority.values()[extras.getInt(PARAM_PRIORITY)],
                        extras.getString(PARAM_KEY),
                        new JsonObject(extras.getByteArray(PARAM_REQUEST)),
                        extras.getString(PARAM_HANDLER_NAME),
                        extras.getByteArray(PARAM_HANDLER_PARAMS));
                if (extras.containsKey(PARAM_TRANSFORM_LIST) && extras.get(PARAM_TRANSFORM_LIST) != null) {
                    Bundle[] transforms = (Bundle[]) extras.getParcelableArray(PARAM_TRANSFORM_LIST);
                    for (int i = 0; i < transforms.length; i++) {
                        Bundle transform = transforms[i];
                        Transform.put(this, transaction.getId(), transform);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (_currentTransaction == null) {
            startTransaction();
        }

        return START_STICKY;
    }

    private final AuthTopicReceiver _topicReceiver = new AuthTopicReceiver(new Handler()) {
        @Override
        public void onAuthentication(String username, String authToken, boolean isNew) {
            if (_webService == null || isNew) {
                _webService = new WebClient(WebTransactionService.this, username, authToken, _webReceiver);
                if (_currentTransaction == null) {
                    startTransaction();
                }
            }
        }

        @Override
        public void onAuthenticationFailed(boolean networkDown) {
        }

        @Override
        public void onAuthenticationInvalidated() {
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void startTransaction() {
        if (_webService == null)
            return;

        _currentTransaction = WebTransaction.getNext(this);
        if (_currentTransaction == null) {
            stopSelf();
            return;
        }

        // at some point call the web service
        JsonObject request = _currentTransaction.getRequest();
        startService(
                _webService.http(0, request, false));
    }

    private WebResultReceiver _webReceiver = new WebResultReceiver(new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            // yay!
            String handler = _currentTransaction.getHandlerName();
            WebTransactionHandler.completeTransaction(
                    WebTransactionService.this,
                    _transactionListener,
                    handler,
                    _currentTransaction,
                    resultData);
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);

            // something bad happened. should either retry, or set up an alert to try again later.
        }

        @Override
        public Context getContext() {
            return WebTransactionService.this;
        }

    };

    private final WebTransactionHandler.Listener _transactionListener = new WebTransactionHandler.Listener() {
        @Override
        public void onComplete() {
            // finish up transaction
            WebTransaction.delete(WebTransactionService.this, _currentTransaction.getId());
            // fire off the next one
            startTransaction();
        }
    };

    /**
     * Parses a URL option string and adds the access_token to it.
     *
     * @param authToken
     * @param params
     * @return
     * @throws java.text.ParseException
     */
    public String applyAuthTokenToUrlParams(String authToken, String params) throws ParseException {
        if (params == null || params.equals("")) {
            return "?access_token=" + authToken;
        } else if (params.startsWith("?")) { // if options already specified
            return "?access_token=" + authToken + "&" + params.substring(1);
        }
        throw new ParseException("Options must be nothing, or start with '?'. Got: " + params, 0);
    }

}
