package com.fieldnation.service.data.payment;

import android.content.Intent;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.MSService;
import com.fieldnation.service.objectstore.StoredObject;

/**
 * Created by Michael Carver on 3/27/2015.
 */
public class PaymentService extends MSService implements PaymentConstants {
    private static final String TAG = "PaymentDataService";

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public int getMaxWorkerCount() {
        return 2;
    }

    @Override
    public void processIntent(Intent intent) {
        if (intent.hasExtra(PARAM_ACTION)) {
            String action = intent.getStringExtra(PARAM_ACTION);
            if (action.equals(PARAM_ACTION_LIST)) {
                list(intent);
            } else if (action.equals(PARAM_ACTION_GET)) {
                get(intent);
            }
        }
    }

    private void list(Intent intent) {
        int page = intent.getIntExtra(PARAM_PAGE, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);
        boolean allowCache = intent.getBooleanExtra(PARAM_ALLOW_CACHE, true);

        StoredObject obj = null;
        if (allowCache) {
            obj = StoredObject.get(App.getProfileId(), PSO_PAYMENT_LIST, page + "");
            if (obj != null) {
                try {
                    PaymentDispatch.list(this, page, new JsonArray(obj.getData()), false, isSync, true);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
        }

        if (isSync || obj == null || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            PaymentTransactionBuilder.list(this, page, isSync);
        }
    }

    private void get(Intent intent) {
        long paymentId = intent.getLongExtra(PARAM_PAYMENT_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(App.getProfileId(), PSO_PAYMENT, paymentId);
        if (obj != null) {
            try {
                PaymentDispatch.get(this, paymentId, new JsonObject(obj.getData()), false, isSync);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        if (isSync || obj == null || (obj.getLastUpdated() + CALL_BOUNCE_TIMER < System.currentTimeMillis())) {
            PaymentTransactionBuilder.get(this, paymentId, isSync);
        }
    }

}
