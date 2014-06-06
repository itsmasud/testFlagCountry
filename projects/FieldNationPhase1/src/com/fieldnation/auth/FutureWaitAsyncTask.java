package com.fieldnation.auth;

import android.accounts.AccountManagerFuture;
import android.os.AsyncTask;
import android.os.Bundle;

public class FutureWaitAsyncTask extends
		AsyncTask<AccountManagerFuture<Bundle>, Void, Object> {

	private FutureWaitAsyncTaskListener _listener;

	public FutureWaitAsyncTask(FutureWaitAsyncTaskListener listener) {
		super();
		_listener = listener;
	}

	@Override
	protected Object doInBackground(AccountManagerFuture<Bundle>... params) {
		AccountManagerFuture<Bundle> future = params[0];
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
}
