package com.fieldnation;

public class DelayedCall<Tag> extends AsyncTaskEx<Long, Void, Object> {
	private static final String TAG = "DelayedCall";

	private Listener<Tag> _listener;
	private Tag _tag;
	private boolean _finished = false;
	private boolean _canceled = false;

	public void execute(long timeInMilliseconds, Listener<Tag> listener, Tag tag) {
		_listener = listener;
		_tag = tag;
		super.executeEx(timeInMilliseconds);
	}

	public void finish() {
		_finished = true;
		if (_listener != null) {
			_listener.onComplete(this, _tag);
		}
	}

	public void cancel() {
		_canceled = true;
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
		if (_finished || _canceled)
			return;

		if (_listener != null) {
			_listener.onComplete(this, _tag);
		}
		super.onPostExecute(result);
	}

	public interface Listener<Tag> {
		public void onComplete(DelayedCall<Tag> delayedCall, Tag Tag);
	}
}
