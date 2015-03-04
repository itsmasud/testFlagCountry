package com.fieldnation.service.transaction;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import com.fieldnation.rpc.common.WebResultReceiver;

/**
 * Created by Michael Carver on 2/27/2015.
 * <p/>
 * this class does two things.
 * 1) Accepts transactions from the rest of the application
 * 2) processes transactions from the queue until they are complete
 */
public class TransactionService extends Service implements TransactionConstants {

    private Transaction _currentTransaction = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // get transaction and transforms from intent, push into the database
        // kick off the next transaction if not running

        if (_currentTransaction == null) {
            startTransaction();
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startTransaction() {
        _currentTransaction = Transaction.getNext(this);
        if (_currentTransaction == null) {
            stopSelf();
            return;
        }
        // at some point call the web service
    }

    private WebResultReceiver _webReceiver = new WebResultReceiver(new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            // yay!
            String handler = _currentTransaction.getHandlerName();
            // get handler object
            // pass transaction and result data to handler
            // finish up transaction
            Transaction.delete(TransactionService.this, _currentTransaction.getId());

            // fire off the next one
            startTransaction();
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);

            // something bad happened. should either retry, or set up an alert to try again later.
        }

        @Override
        public Context getContext() {
            return TransactionService.this;
        }

    };
}
