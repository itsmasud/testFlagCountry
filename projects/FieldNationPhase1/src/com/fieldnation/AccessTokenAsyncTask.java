package com.fieldnation;

import com.fieldnation.webapi.AccessToken;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Runs an authentication request against field nation's server
 * 
 * @author michael.carver
 * 
 */
public class AccessTokenAsyncTask extends AsyncTask<String, Void, Object> {
	private AccessTokenAsyncTaskListener _listener;

	public AccessTokenAsyncTask(AccessTokenAsyncTaskListener listener) {
		super();
		_listener = listener;
	}

	public void execute(Context context, String username, String password) {
		String hostname = context.getString(R.string.fn_hostname);
		String grantType = context.getString(R.string.fn_grant_type);
		String clientId = context.getString(R.string.fn_client_id);
		String clientSecret = context.getString(R.string.fn_client_secret);

		// build the query
		execute(hostname, grantType, clientId, clientSecret, username, password);
	}

	@Override
	protected Object doInBackground(String... params) {
		// Execute the query, return the AT if success, otherwise return the
		// exception
		try {
			return new AccessToken(params[0], params[1], params[2], params[3],
					params[4], params[5]);

		} catch (Exception ex) {
			return ex;
		}
	}

	@Override
	protected void onPostExecute(Object result) {
		if (_listener == null)
			return;

		if (result instanceof AccessToken)
			_listener.onComplete((AccessToken) result);
		else
			_listener.onFail((Exception) result);
	}

}
