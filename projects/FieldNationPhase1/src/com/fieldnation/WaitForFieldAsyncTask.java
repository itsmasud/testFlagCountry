package com.fieldnation;

import java.lang.reflect.Field;

import android.os.AsyncTask;

public class WaitForFieldAsyncTask extends AsyncTask<Object, Void, Object> {
	private Listener _listener;

	public WaitForFieldAsyncTask(Listener listener) {
		super();

		_listener = listener;
	}

	public void execute(Object object, String fieldName) {
		super.execute(object, fieldName);
	}

	@Override
	protected Object doInBackground(Object... params) {
		Object obj = params[0];
		String fieldName = (String) params[1];
		try {
			Field field = obj.getClass().getField(fieldName);

			while (field.get(obj) == null) {
				Thread.sleep(100);
			}

			return field.get(obj);
		} catch (Exception ex) {
			return ex;
		}
	}

	@Override
	protected void onPostExecute(Object result) {
		if (result instanceof Exception) {
			_listener.onFail((Exception) result);
		} else {
			_listener.onSuccess(result);
		}

	}

	public interface Listener {

		public void onFail(Exception ex);

		public void onSuccess(Object value);
	}

}
