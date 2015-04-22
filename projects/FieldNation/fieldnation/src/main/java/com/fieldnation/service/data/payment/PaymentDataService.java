package com.fieldnation.service.data.payment;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.Log;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.objectstore.StoredObject;

/**
 * Created by Michael Carver on 3/27/2015.
 */
public class PaymentDataService extends Service implements PaymentConstants {
    private static final String TAG = "PaymentDataService";

    private static final Object LOCK = new Object();
    private static int COUNT = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        if (intent != null && intent.getExtras() != null) {
            new AsyncTaskEx<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... params) {
                    Context context = (Context) params[0];
                    Intent intent = (Intent) params[1];

                    synchronized (LOCK) {
                        COUNT++;
                    }
                    String action = intent.getStringExtra(PARAM_ACTION);
                    if (action.equals(PARAM_ACTION_GET_ALL)) {
                        getAll(context, intent);
                    } else if (action.equals(PARAM_ACTION_PAYMENT)) {
                        getPayment(context, intent);
                    }
                    synchronized (LOCK) {
                        COUNT--;
                        if (COUNT == 0) {
                            stopSelf();
                        }
                    }
                    return null;
                }
            }.executeEx(this, intent);
        }
        return START_STICKY;
    }


    private void getAll(Context context, Intent intent) {
        int page = intent.getIntExtra(PARAM_PAGE, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(context, PSO_PAYMENT_GET_ALL, page + "");
        if (obj != null) {
            try {
                PaymentDataDispatch.allPage(context, page, new JsonArray(obj.getData()));
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
                PaymentDataDispatch.payment(context, paymentId, new JsonObject(obj.getData()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (isSync || obj == null || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            PaymentTransactionBuilder.getPayment(context, paymentId, isSync);
        }
    }

}
