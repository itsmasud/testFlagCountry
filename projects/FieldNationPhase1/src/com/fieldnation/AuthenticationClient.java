package com.fieldnation;

import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * <p>
 * This class should be implemented by any other class that needs to get web
 * service authentication.
 * </p>
 * 
 * <p>
 * TODO, this class will fail silently. It needs to be updated to do something
 * intelligent on failure
 * </p>
 * 
 * @see GlobalState
 * @see AuthenticationServer
 * 
 * @author michael.carver
 * 
 */
public abstract class AuthenticationClient {
	private static final String TAG = "AuthenticationClient";
	private GlobalState _gs;

	public AuthenticationClient(Context context) {
		_gs = (GlobalState) context.getApplicationContext();
	}

	/**
	 * Waits for the future to finish. When finished it will check for an
	 * authentication token. If not found, then it requests authentication
	 * again. If found, then it calls onAuthentication()
	 * 
	 * @param future
	 */
	public void waitForFuture(AccountManagerFuture<Bundle> future) {
		new FutureWaitAsyncTask(_futureWaitAsyncTaskListener).execute(future);
	}

	/**
	 * Waits for the field of the passed object to become non-null. When that
	 * event happens, then the client asks for authentication again.
	 * 
	 * @param obj
	 *            the object to monitor.
	 * @param fieldname
	 *            the name of the field to monitor.
	 */
	public void waitForObject(Object obj, String fieldname) {
		new WaitForFieldAsyncTask(_waitForAccessToken_listener).execute(obj,
				fieldname);
	}

	private WaitForFieldAsyncTask.Listener _waitForAccessToken_listener = new WaitForFieldAsyncTask.Listener() {
		@Override
		public void onSuccess(Object value) {
			_gs.requestAuthentication(AuthenticationClient.this);
			// TODO Method Stub: onFail()
			Log.v(TAG, "Method Stub: onSuccess()");
		}

		@Override
		public void onFail(Exception ex) {
			// TODO Method Stub: onFail()
			Log.v(TAG, "Method Stub: onFail()");
		}
	};

	private FutureWaitAsyncTask.Listener _futureWaitAsyncTaskListener = new FutureWaitAsyncTask.Listener() {
		@Override
		public void onFail(Exception ex) {
			Log.e(TAG, "_futureWaitAsyncTaskListener.onFail");
		}

		@Override
		public void onComplete(Bundle bundle) {
			String tokenString = bundle.getString("authtoken");

			if (tokenString == null) {
				if (bundle.containsKey("accountType") && bundle.containsKey("authAccount")) {
					_gs.requestAuthentication(AuthenticationClient.this);
				}
			} else {
				onAuthentication(bundle.getString("authAccount"), tokenString);
			}
		}
	};

	public abstract void onAuthentication(String username, String authToken);

}
