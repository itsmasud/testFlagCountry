package com.fieldnation.service.data.payment;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.fieldnation.Log;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.topics.TopicService;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionBuilder;

/**
 * Created by Michael Carver on 3/27/2015.
 */
public class PaymentDataService extends Service implements PaymentConstants {
    private static final String TAG = "PaymentDataService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        if (intent != null && intent.getExtras() != null) {
            new Thread(new WorkerRunnable(this, intent)).start();
        }
        return START_STICKY;
    }

    private void getAll(Context context, Intent intent) {
        int page = intent.getIntExtra(PARAM_PAGE, 0);
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(PaymentTransactionHandler.class)
                    .handlerParams(
                            PaymentTransactionHandler.generateGetAllParams(page)
                    )
                    .key("PaymentGetAll" + page)
                    .useAuth()
                    .request(
                            new HttpJsonBuilder()
                                    .method("GET")
                                    .path("/api/rest/v1/accounting/payment-queue/all")
                                    .urlParams("?page=" + page)
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        StoredObject obj = StoredObject.get(context, PSO_PAYMENT_GET_ALL, page + "");
        if (obj != null) {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_ALL);
            bundle.putInt(PARAM_PAGE, page);
            bundle.putByteArray(PARAM_DATA, obj.getData());
            TopicService.dispatchEvent(context, TOPIC_ID_GET_ALL, bundle, true);
        }
    }

    private void getPayment(Context context, Intent intent) {
        long paymentId = intent.getLongExtra(PARAM_ID, 0);
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(PaymentTransactionHandler.class)
                    .handlerParams(PaymentTransactionHandler.generatePaymentParams(paymentId))
                    .key("GetPayment" + paymentId)
                    .useAuth()
                    .request(
                            new HttpJsonBuilder()
                                    .method("GET")
                                    .path("/api/rest/v1/accounting/payment-queue/" + paymentId)
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        StoredObject obj = StoredObject.get(context, PSO_PAYMENT_GET, paymentId + "");
        if (obj != null) {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_ACTION, PARAM_ACTION_PAYMENT);
            bundle.putLong(PARAM_ID, paymentId);
            bundle.putByteArray(PARAM_DATA, obj.getData());
            TopicService.dispatchEvent(context, TOPIC_ID_PAYMENT, bundle, true);
        }
    }

    private class WorkerRunnable implements Runnable {
        private Context _context;
        private Intent _intent;

        WorkerRunnable(Context context, Intent intent) {
            _context = context;
            _intent = intent;
        }

        @Override
        public void run() {
            String action = _intent.getStringExtra(PARAM_ACTION);
            if (action.equals(PARAM_ACTION_GET_ALL)) {
                getAll(_context, _intent);
            } else if (action.equals(PARAM_ACTION_PAYMENT)) {
                getPayment(_context, _intent);
            }
        }
    }
}
