package com.fieldnation;

import android.accounts.AccountManagerFuture;
import android.os.AsyncTask;
import android.os.Build;


/**
 * Waits for an {@link AccountManagerFuture} to resolve.
 *
 * @author michael.carver
 */
public class FutureWaitAsyncTask extends AsyncTask<Object, Void, Object> {
    private static final String TAG = "FutureWaitAsyncTask";

    private final Listener _listener;

    public FutureWaitAsyncTask(Listener listener) {
        super();
        _listener = listener;
    }

    public void execute(AccountManagerFuture<?> future) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, future);
        } else {
            super.execute(future);
        }
    }

    @Override
    protected Object doInBackground(Object... params) {
        AccountManagerFuture<?> future = (AccountManagerFuture<?>) params[0];
        Log.v(TAG, "Waiting for result.");
        try {
            return future.getResult();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return ex;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);

        if (_listener == null)
            return;

        if (result instanceof Exception) {
            _listener.onFail((Exception) result);
        } else {
            _listener.onComplete(result);
        }
    }

    /**
     * Called when a future either completes, or an error happens.
     *
     * @author michael.carver
     */
    public interface Listener {
        void onComplete(Object result);

        void onFail(Exception ex);
    }
}
