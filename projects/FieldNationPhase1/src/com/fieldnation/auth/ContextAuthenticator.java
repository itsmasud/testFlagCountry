package com.fieldnation.auth;

import java.text.ParseException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.os.Bundle;

import com.fieldnation.Constants;
import com.fieldnation.webapi.AccessToken;

public class ContextAuthenticator {
	private FutureWaitAsyncTask _authWaitAsyncTask;

	public ContextAuthenticator() {

	}

	public void getAuthToken(Context context) {
		// TODO, build into a utility class
		_authWaitAsyncTask = new FutureWaitAsyncTask(_authWaitAsyncTaskListener);

		AccountManager am = AccountManager.get(context);
		Account[] accounts = am
				.getAccountsByType(Constants.FIELD_NATION_ACCOUNT_TYPE);
		if (accounts.length == 0) {
			// Do nothing
		} else {
			AccountManagerFuture<Bundle> future = am.getAuthToken(accounts[0],
					Constants.FIELD_NATION_ACCOUNT_TYPE, new Bundle(), null,
					null, null);

			_authWaitAsyncTask.execute(future);
		}
	}

	// the next two events are related to authentication
	private FutureWaitAsyncTaskListener _authWaitAsyncTaskListener = new FutureWaitAsyncTaskListener() {
		@Override
		public void onFail(Exception ex) {
			// TODO Auto-generated method stub
			System.out.println("Method Stub: onFail()");
		}

		@Override
		public void onComplete(Bundle bundle) {
			try {
				AccessToken accessToken = new AccessToken(
						bundle.getString("JSON_ACCESS_TOKEN"));

				// TODO pass up to caller
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	};

}
