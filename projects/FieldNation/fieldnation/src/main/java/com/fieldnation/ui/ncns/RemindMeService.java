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

    private WorkordersWebApi _workOrdersApi;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        _workOrdersApi = new WorkordersWebApi(_workOrdersApi_listener);
        _workOrdersApi.connect(App.get());
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final WorkordersWebApi.Listener _workOrdersApi_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "onConnected");
            _workOrdersApi.subWorkordersWebApi();

            GetWorkOrdersOptions opts = new GetWorkOrdersOptions();
            opts.setPerPage(65);
            opts.setList("workorders_assignments");
            opts.setFFlightboardTomorrow(true);
            opts.setPage(1);

            WorkordersWebApi.getWorkOrders(App.get(), opts, false, false);
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

                if (workOrders.getMetadata() != null
                        && workOrders.getMetadata().getTotal() != null
                        && workOrders.getMetadata().getTotal() > 0) {
                    Log.v(TAG, "onComplete setNeedsConfirmation");
                    App.get().setNeedsConfirmation(true);
                }

                if (_workOrdersApi != null) _workOrdersApi.disconnect(App.get());
                stopSelf();
            }
        }
    };
}