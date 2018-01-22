package com.fieldnation.service.tracker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fieldnation.App;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.analytics.contexts.SpFileContext;
import com.fieldnation.analytics.contexts.SpStackContext;
import com.fieldnation.analytics.contexts.SpStatusContext;
import com.fieldnation.analytics.contexts.SpTracingContext;
import com.fieldnation.analytics.contexts.SpWorkOrderContext;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionSystem;
import com.fieldnation.v2.data.listener.TransactionParams;

import java.util.List;

/**
 * Created by mc on 1/19/18.
 */

public class RetryService extends Service {
    private static final String TAG = "RetryService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || intent.getExtras() == null || !intent.hasExtra("workOrderId"))
            return super.onStartCommand(intent, flags, startId);

        List<WebTransaction> list = WebTransaction.getZombies();
        int workOrderId = intent.getIntExtra("workOrderId", 0);
        if (workOrderId == 0)
            return super.onStartCommand(intent, flags, startId);

        for (WebTransaction webTransaction : list) {
            try {

                UUIDGroup uuidGroup = webTransaction.getUUID();

                TransactionParams transactionParams = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));
                JsonObject methodParams = new JsonObject(transactionParams.methodParams);
                int transWorkOrderId = methodParams.getInt("workOrderId");
                if (transWorkOrderId != workOrderId)
                    continue;

                StoredObject so = StoredObject.get(App.get(), methodParams.getLong("storedObjectId"));

                Tracker.event(App.get(),
                        new SimpleEvent.Builder()
                                .category("RetryService")
                                .label(TAG)
                                .action("start")
                                .addContext(new SpTracingContext(uuidGroup))
                                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                                .addContext(new SpStatusContext(SpStatusContext.Status.START, "RetryService"))
                                .addContext(new SpFileContext.Builder().name(methodParams.getString("attachment.file.name")).size((int) so.size()).build())
                                .addContext(new SpWorkOrderContext.Builder().workOrderId(workOrderId).build())
                                .build());

                webTransaction.setTryCount(-1); // -1 cause +1 in requeue()
                webTransaction.requeue(0);
                WebTransactionSystem.getInstance();

                Tracker.event(App.get(),
                        new SimpleEvent.Builder()
                                .category("RetryService")
                                .label(TAG)
                                .action("finish")
                                .addContext(new SpTracingContext(uuidGroup))
                                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                                .addContext(new SpStatusContext(SpStatusContext.Status.COMPLETE, "RetryService"))
                                .addContext(new SpFileContext.Builder().name(methodParams.getString("attachment.file.name")).size((int) so.size()).build())
                                .addContext(new SpWorkOrderContext.Builder().workOrderId(workOrderId).build())
                                .build());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
