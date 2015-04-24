package com.fieldnation.service.data.payment;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.MSService;
import com.fieldnation.service.objectstore.StoredObject;

import java.util.List;

/**
 * Created by Michael Carver on 3/27/2015.
 */
public class PaymentDataService extends MSService implements PaymentConstants {
    private static final String TAG = "PaymentDataService";

    @Override
    public int getMaxWorkerCount() {
        return 1;
    }

    @Override
    public WorkerThread getNewWorker(List<Intent> intents) {
        return new MyWorkerThread(this, intents);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class MyWorkerThread extends WorkerThread {
        private Context _context;

        public MyWorkerThread(Context context, List<Intent> intents) {
            super("MyWorkerThread", intents);
            _context = context;
        }

        @Override
        public void processIntent(Intent intent) {
            String action = intent.getStringExtra(PARAM_ACTION);
            if (action.equals(PARAM_ACTION_GET_ALL)) {
                getAll(_context, intent);
            } else if (action.equals(PARAM_ACTION_PAYMENT)) {
                getPayment(_context, intent);
            }
        }
    }

    private void getAll(Context context, Intent intent) {
        int page = intent.getIntExtra(PARAM_PAGE, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(context, PSO_PAYMENT_GET_ALL, page + "");
        if (obj != null) {
            try {
                PaymentDataDispatch.allPage(context, page, new JsonArray(obj.getData()), isSync);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (isSync || obj == null || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            PaymentTransactionBuilder.getAll(context, page, isSync);
        }
    }

    private void getPayment(Context context, Intent intent) {
        long paymentId = intent.getLongExtra(PARAM_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(context, PSO_PAYMENT_GET, paymentId);
        if (obj != null) {
            try {
                PaymentDataDispatch.payment(context, paymentId, new JsonObject(obj.getData()), isSync);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (isSync || obj == null || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            PaymentTransactionBuilder.getPayment(context, paymentId, isSync);
        }
    }

}
