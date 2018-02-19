package com.fieldnation.service.transaction;

import com.fieldnation.App;
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.contexts.SpStackContext;
import com.fieldnation.analytics.contexts.SpStatusContext;
import com.fieldnation.analytics.contexts.SpTracingContext;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.ThreadManager;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;

/**
 * Created by mc on 8/25/17.
 */
class QueueProcessingThread extends ThreadManager.ManagedThread {
    private final String TAG = UniqueTag.makeTag("QueueProcessingThread");

    public QueueProcessingThread(ThreadManager manager) {
        super(manager);
        setName(TAG);
        start();
    }

    @Override
    public boolean doWork() {
        WebTransaction webTransaction = null;
        synchronized (WebTransactionSystem.TRANSACTION_QUEUE) {
            if (WebTransactionSystem.TRANSACTION_QUEUE.size() > 0) {
                Log.v(TAG, "TRANSACTION_QUEUE " + WebTransactionSystem.TRANSACTION_QUEUE.size());
                webTransaction = WebTransactionSystem.TRANSACTION_QUEUE.remove(0);
            }
        }
        if (webTransaction == null)
            return false;

        if (webTransaction.getType() == WebTransaction.Type.CRAWLER && App.get().getOfflineState() == App.OfflineState.NORMAL) {
            Log.v(TAG, "Throwing out transaction");
            return true;
        }

        try {
            if (webTransaction.getKey() != null) {
                WebTransaction dbWt = WebTransaction.get(webTransaction.getKey());
                if (dbWt != null
                        && (dbWt.getType() == webTransaction.getType())
                        && (dbWt.isWifiRequired() == webTransaction.isWifiRequired())) {
                    Log.v(TAG, "processIntent end duplicate " + webTransaction.getKey());
                    return true;
                }
            }

            if (webTransaction.getUUID() != null) {
                Tracker.event(App.get(), new CustomEvent.Builder()
                        .addContext(new SpTracingContext(webTransaction.getUUID()))
                        .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                        .addContext(new SpStatusContext(SpStatusContext.Status.START, "Queue Processing"))
                        .build());
            }

            //Log.v(TAG, "processIntent saving transaction");
            webTransaction.setState(WebTransaction.State.IDLE);
            webTransaction.save();

            String listenerName = webTransaction.getListenerName();
            if (!misc.isEmptyOrNull(listenerName)) {
                WebTransactionDispatcher.queued(App.get(), listenerName, webTransaction);
            }

            if (webTransaction.getUUID() != null) {
                Tracker.event(App.get(), new CustomEvent.Builder()
                        .addContext(new SpTracingContext(webTransaction.getUUID()))
                        .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                        .addContext(new SpStatusContext(SpStatusContext.Status.COMPLETE, "Queue Processing"))
                        .build());
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return true;
    }
}
