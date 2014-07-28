package com.fieldnation;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

public class DelayedCall<Tag> extends AsyncTaskEx<Long, Void, Object> {
	private static final String TAG = "DelayedCall";

	private Listener<Tag> _listener;
	private Tag _tag;

	public void execute(long timeInMilliseconds, Listener<Tag> listener, Tag tag) {
		_listener = listener;
		_tag = tag;
		super.executeEx(timeInMilliseconds);
	}

	@Override
	protected Object doInBackground(Long... params) {

		try {
			Thread.sleep(params[0]);
		} catch (InterruptedException e) {
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {

		if (_listener != null) {
			_listener.onComplete(this, _tag);
		}
		super.onPostExecute(result);
	}

	public interface Listener<Tag> {
		public void onComplete(DelayedCall<Tag> delayedCall, Tag Tag);
	}
}
