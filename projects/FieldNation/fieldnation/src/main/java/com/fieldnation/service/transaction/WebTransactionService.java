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
import com.fieldnation.rpc.webclient.WebService;
import com.fieldnation.service.objectstore.StoredObject;

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
    private WebService _webService = null;

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
                WebTransaction.put(this,
                        WebTransaction.Priority.values()[extras.getInt(PARAM_PRIORITY)],
                        extras.getString(PARAM_KEY),
                        new JsonObject(extras.getByteArray(PARAM_META)),
                        extras.getLong(PARAM_STORED_OBJECT_ID, -1),
                        extras.getString(PARAM_HANDLER_NAME));
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
                _webService = new WebService(WebTransactionService.this, username, authToken, _webReceiver);
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
        StoredObject so = null;
        if (_currentTransaction.getStoedObjectId() != -1) {
            so = StoredObject.get(this, _currentTransaction.getStoedObjectId());
        }

        JsonObject meta = _currentTransaction.getMeta();
        if (so == null) {
            try {
                _webService.httpRead(0,
                        meta.getString(PARAM_WEB_METHOD),
                        meta.getString(PARAM_WEB_PATH),
                        meta.getString(PARAM_WEB_OPTIONS), false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (so.isFile()) {
            try {
                _webService.httpPostFile(0,
                        meta.getString(PARAM_WEB_PATH),
                        meta.getString(PARAM_WEB_OPTIONS),
                        so.getFile().getName(),
                        so.getFile().getAbsolutePath(),
                        null,
                        meta.getString(PARAM_WEB_CONTENT_TYPE));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                _webService.httpWrite(0,
                        meta.getString(PARAM_WEB_METHOD),
                        meta.getString(PARAM_WEB_PATH),
                        meta.getString(PARAM_WEB_OPTIONS),
                        so.getData(),
                        meta.getString(PARAM_WEB_CONTENT_TYPE), false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
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
}
