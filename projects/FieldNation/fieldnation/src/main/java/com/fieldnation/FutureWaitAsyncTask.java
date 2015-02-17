package com.fieldnation;

import android.accounts.AccountManagerFuture;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;


/**
 * Waits for an {@link AccountManagerFuture} to resolve.
 * 
 * @author michael.carver
 * 
 */
public class FutureWaitAsyncTask extends AsyncTask<Object, Void, Object> {
	private static final String TAG = "FutureWaitAsyncTask";

	private Listener _listener;

	public FutureWaitAsyncTask(Listener listener) {
		super();
		_listener = listener;
	}

	public void execute(AccountManagerFuture<?> future) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			executeHoneyComb(future);
		} else {
			super.execute(future);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void executeHoneyComb(AccountManagerFuture<?> future) {
		super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, future);
	}

	@Override
	protected Object doInBackground(Object... params) {
		AccountManagerFuture<?> future = (AccountManagerFuture<?>) params[0];
		Log.v(TAG, "Waiting for result.");
		try {
			return future.getResult();
		} catch (Exception ex) {
			ex.printStackTrace();
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
	 * 
	 */
	public interface Listener {
		public void onComplete(Object result);

		public void onFail(Exception ex);
	}
}
