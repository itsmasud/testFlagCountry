package com.fieldnation.service.data.payment;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.fieldnation.ThreadManager;
import com.fieldnation.UniqueTag;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.MSService;
import com.fieldnation.service.objectstore.StoredObject;

import java.util.List;

/**
 * Created by Michael Carver on 3/27/2015.
 */
public class PaymentService extends MSService implements PaymentConstants {
    private static final String TAG = "PaymentDataService";

    @Override
    public int getMaxWorkerCount() {
        return 2;
    }

    @Override
    public WorkerThread getNewWorker(ThreadManager manager, List<Intent> intents) {
        return new MyWorkerThread(manager, this, intents);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class MyWorkerThread extends WorkerThread {
        private String TAG = UniqueTag.makeTag("PaymentDataServiceThread");
        private Context _context;

        public MyWorkerThread(ThreadManager manager, Context context, List<Intent> intents) {
            super(manager, intents);
            setName(TAG);
            _context = context;
        }

        @Override
        public void processIntent(Intent intent) {
            String action = intent.getStringExtra(PARAM_ACTION);
            if (action.equals(PARAM_ACTION_LIST)) {
                list(_context, intent);
            } else if (action.equals(PARAM_ACTION_GET)) {
                get(_context, intent);
            }
        }
    }

    private void list(Context context, Intent intent) {
        int page = intent.getIntExtra(PARAM_PAGE, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(context, PSO_PAYMENT_LIST, page + "");
        if (obj != null) {
            try {
                PaymentDispatch.list(context, page, new JsonArray(obj.getData()), false, isSync);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (isSync || obj == null || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            PaymentTransactionBuilder.list(context, page, isSync);
        }
    }

    private void get(Context context, Intent intent) {
        long paymentId = intent.getLongExtra(PARAM_PAYMENT_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(context, PSO_PAYMENT, paymentId);
        if (obj != null) {
            try {
                PaymentDispatch.get(context, paymentId, new JsonObject(obj.getData()), false, isSync);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (isSync || obj == null || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            PaymentTransactionBuilder.get(context, paymentId, isSync);
        }
    }

}
