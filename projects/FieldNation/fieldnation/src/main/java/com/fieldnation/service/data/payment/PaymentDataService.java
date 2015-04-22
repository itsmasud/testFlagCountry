package com.fieldnation.service.data.payment;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.fieldnation.Log;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.transaction.Priority;
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
        StoredObject obj = StoredObject.get(context, PSO_PAYMENT_GET_ALL, page + "");
        if (obj != null) {
            PaymentDataDispatch.allPage(context, page, obj.getData());
        }

        if (obj == null || (obj.getLastUpdated() + 30000 < System.currentTimeMillis())) {
            try {
                WebTransactionBuilder.builder(context)
                        .priority(Priority.HIGH)
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
        }
    }

    private void getPayment(Context context, Intent intent) {
        long paymentId = intent.getLongExtra(PARAM_ID, 0);
        StoredObject obj = StoredObject.get(context, PSO_PAYMENT_GET, paymentId + "");
        if (obj != null) {
            PaymentDataDispatch.payment(context, paymentId, obj.getData());
        }

        if (obj == null || (obj.getLastUpdated() + 30000 < System.currentTimeMillis())) {
            try {
                WebTransactionBuilder.builder(context)
                        .priority(Priority.HIGH)
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
