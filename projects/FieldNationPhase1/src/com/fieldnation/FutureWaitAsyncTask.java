package com.fieldnation;

import android.accounts.AccountManagerFuture;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

/**
 * Waits for an {@link AccountManagerFuture} to resolve.
 * 
 * @author michael.carver
 * 
 */
public class FutureWaitAsyncTask extends AsyncTask<Object, Void, Object> {
	private static final String TAG = "auth.server.FutureWaitAsyncTask";

	private Listener _listener;

	public FutureWaitAsyncTask(Listener listener) {
		super();
		_listener = listener;
	}

	public void execute(AccountManagerFuture<Bundle> future) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			executeHoneyComb(future);
		} else {
			super.execute(future);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void executeHoneyComb(AccountManagerFuture<Bundle> future) {
		super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, future);
	}

	@Override
	protected Object doInBackground(Object... params) {
		@SuppressWarnings("unchecked")
		AccountManagerFuture<Bundle> future = (AccountManagerFuture<Bundle>) params[0];
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

		if (result instanceof Bundle) {
			_listener.onComplete((Bundle) result);
		} else {
			_listener.onFail((Exception) result);
		}

	}

	/**
	 * Called when a future either completes, or an error happens.
	 * 
	 * @author michael.carver
	 * 
	 */
	public interface Listener {
		public void onComplete(Bundle bundle);

		public void onFail(Exception ex);
	}
}
