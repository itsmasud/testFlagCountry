package com.fieldnation.ui.ncns;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.v2.data.client.GetWorkOrdersOptions;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.WorkOrders;

/**
 * Created by mc on 5/19/17.
 */

public class RemindMeService extends Service {
    private static final String TAG = "RemindMeService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        _workOrdersApi.sub();
        GetWorkOrdersOptions opts = new GetWorkOrdersOptions();
        opts.setPerPage(25);
        opts.setList("workorders_assignments");
        opts.setFFlightboardTomorrow(true);
        opts.setPage(1);

        WorkordersWebApi.getWorkOrders(App.get(), opts, false, false);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.equals("getWorkOrders");
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            Log.v(TAG, "onComplete");

            if (methodName.equals("getWorkOrders")
                    && success
                    && successObject != null
                    && successObject instanceof WorkOrders) {
                Log.v(TAG, "onComplete getWorkOrders");

                WorkOrders workOrders = (WorkOrders) successObject;

                if (!"workorders_assignments".equals(workOrders.getMetadata().getList())) {
                    return;
                }

                if (workOrders.getMetadata().getTotal() != null
                        && workOrders.getMetadata().getTotal() > 0) {
                    Log.v(TAG, "onComplete setNeedsConfirmation");
                    App.get().setNeedsConfirmation(true);
                }

                _workOrdersApi.unsub();
                stopSelf();
            }
        }
    };
}