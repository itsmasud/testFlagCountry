package com.fieldnation;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

public abstract class AsyncTaskEx<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

	public AsyncTask<Params, Progress, Result> executeEx(Params... params) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return executeHoneyComb(params);
		} else {
			return super.execute(params);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private AsyncTask<Params, Progress, Result> executeHoneyComb(Params... params) {
		return super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
	}

}
