package com.fieldnation.auth;

import android.accounts.AccountManagerFuture;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class FutureWaitAsyncTask extends AsyncTask<Object, Void, Object> {
	private static final String TAG = "auth.FutureWaitAsyncTask";

	private Listener _listener;

	public FutureWaitAsyncTask(Listener listener) {
		super();
		_listener = listener;
		Log.v(TAG, "Constructed");
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
		Log.v(TAG, "doInBackground");

		@SuppressWarnings("unchecked")
		AccountManagerFuture<Bundle> future = (AccountManagerFuture<Bundle>) params[0];
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

	public interface Listener {
		public void onComplete(Bundle bundle);

		public void onFail(Exception ex);
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Method Stub: finalize()
		Log.v(TAG, "Method Stub: finalize()");
		super.finalize();
	}

}
