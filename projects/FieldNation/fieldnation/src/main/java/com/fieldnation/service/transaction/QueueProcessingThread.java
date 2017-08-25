package com.fieldnation.service.transaction;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
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
                webTransaction = WebTransactionSystem.TRANSACTION_QUEUE.remove(0);
            }
        }
        if (webTransaction == null)
            return false;

        try {
            if (webTransaction.getKey() != null && WebTransaction.keyExists(webTransaction.getKey())) {
                Log.v(TAG, "processIntent end duplicate " + webTransaction.getKey());
                return true;
            }
            //Log.v(TAG, "processIntent saving transaction");
            webTransaction.setState(WebTransaction.State.IDLE);
            webTransaction.save();

            String listenerName = webTransaction.getListenerName();
            if (!misc.isEmptyOrNull(listenerName)) {
                WebTransactionDispatcher.queued(App.get(), listenerName, webTransaction);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return true;
    }
}
