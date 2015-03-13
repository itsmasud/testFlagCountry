package com.fieldnation.service.transaction;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJson;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.rpc.server.auth.AuthTopicReceiver;
import com.fieldnation.rpc.server.auth.AuthTopicService;
import com.fieldnation.rpc.server.auth.OAuth;

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
    private OAuth _auth;

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
                        extras.getBoolean(PARAM_USE_AUTH),
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
        public void onAuthentication(OAuth auth, boolean isNew) {
            _auth = auth;
            if (_currentTransaction == null) {
                startTransaction();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void startTransaction() {
        _currentTransaction = WebTransaction.getNext(this);
        if (_currentTransaction == null) {
            stopSelf();
            return;
        }

        // at some point call the web service
        JsonObject request = _currentTransaction.getRequest();
        try {
            if (_currentTransaction.useAuth()) {
                if (!request.has(HttpJsonBuilder.PARAM_WEB_HOST)) {
                    request.put(HttpJsonBuilder.PARAM_WEB_HOST, _auth.getHost());
                }
                _auth.applyToRequest(request);
            }
            HttpResult result = HttpJson.run(this, request);


            String handler = _currentTransaction.getHandlerName();
            WebTransactionHandler.completeTransaction(
                    WebTransactionService.this,
                    _transactionListener,
                    handler,
                    _currentTransaction,
                    result);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private final WebTransactionHandler.Listener _transactionListener = new WebTransactionHandler.Listener() {
        @Override
        public void onComplete() {
            // finish up transaction
            WebTransaction.delete(WebTransactionService.this, _currentTransaction.getId());
            _currentTransaction = null;
            // fire off the next one
            startTransaction();
        }

        @Override
        public void onError() {
            // finish up transaction
            WebTransaction.delete(WebTransactionService.this, _currentTransaction.getId());
            _currentTransaction = null;
            // fire off the next one
            startTransaction();
        }
    };
}
