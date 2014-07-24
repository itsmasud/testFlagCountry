package com.fieldnation.data.workorder;

import com.fieldnation.GlobalState;
import com.fieldnation.auth.client.AuthenticationClient;

import android.content.Context;
import android.util.Log;

public class ExpenseCategories {
	private static final String TAG = "data.workorder.ExpenseCategories";

	private GlobalState _gs;

	public ExpenseCategories(Context context) {
		_gs = (GlobalState) context.getApplicationContext();
		_gs.requestAuthentication(_authclient);
	}

	private AuthenticationClient _authclient = new AuthenticationClient() {

		@Override
		public void onAuthenticationFailed(Exception ex) {
			// TODO Method Stub: onAuthenticationFailed()
			Log.v(TAG, "Method Stub: onAuthenticationFailed()");

		}

		@Override
		public void onAuthentication(String username, String authToken) {
			// TODO Method Stub: onAuthentication()
			Log.v(TAG, "Method Stub: onAuthentication()");

		}

		@Override
		public GlobalState getGlobalState() {
			// TODO Method Stub: getGlobalState()
			Log.v(TAG, "Method Stub: getGlobalState()");
			return null;
		}
	};
}
