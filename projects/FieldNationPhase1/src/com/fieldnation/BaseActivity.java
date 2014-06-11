package com.fieldnation;

import java.text.ParseException;

import com.fieldnation.auth.FutureWaitAsyncTask;
import com.fieldnation.webapi.AccessToken;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;

public abstract class BaseActivity extends ActionBarActivity {
	private static final String TAG = "BaseActivity";

	// UI
	NotificationActionBarView _notificationsView;
	MessagesActionBarView _messagesView;

	// Data
	GlobalState _gs;
	AccessToken _accessToken;

	// Other
	private FutureWaitAsyncTask _amc_future;

	// Services
	private AccountManager _accountManager;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		_gs = (GlobalState) getApplicationContext();

		getAuthorization();
	}

	private void getAuthorization() {
		if (_accountManager == null)
			_accountManager = AccountManager.get(this);

		Account[] accounts = _accountManager.getAccountsByType(Constants.FIELD_NATION_ACCOUNT_TYPE);
		Log.v(TAG, "Found accounts: " + accounts.length);
		AccountManagerFuture<Bundle> future = null;
		if (accounts.length == 0) {
			future = _accountManager.addAccount(
					Constants.FIELD_NATION_ACCOUNT_TYPE, null, null, null,
					this, _amc, new Handler());

		} else if (accounts.length == 1) {
			future = _accountManager.getAuthToken(accounts[0],
					Constants.FIELD_NATION_ACCOUNT_TYPE, null, this, _amc,
					new Handler());
		} else {
			// TODO, ANDR-10 present a picker for the account
			future = _accountManager.getAuthToken(accounts[0],
					Constants.FIELD_NATION_ACCOUNT_TYPE, null, this, _amc,
					new Handler());

		}
		if (future != null) {
			// new
			// FutureWaitAsyncTask(_futureWaitAsyncTaskListener).execute(future);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		// pull out connections to our custom views
		_notificationsView = (NotificationActionBarView) MenuItemCompat.getActionView(menu.findItem(R.id.notifications_menuitem));
		_notificationsView.setCount(10);

		_messagesView = (MessagesActionBarView) MenuItemCompat.getActionView(menu.findItem(R.id.messages_menuitem));
		_messagesView.setCount(100);

		return true;

	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private AccountManagerCallback<Bundle> _amc = new AccountManagerCallback<Bundle>() {
		@Override
		public void run(AccountManagerFuture<Bundle> future) {
			_amc_future = new FutureWaitAsyncTask(_futureWaitAsyncTaskListener);
			_amc_future.execute(future);
		}
	};

	private FutureWaitAsyncTask.Listener _futureWaitAsyncTaskListener = new FutureWaitAsyncTask.Listener() {
		@Override
		public void onFail(Exception ex) {
			onFailedAuthToken(ex);
		}

		@Override
		public void onComplete(Bundle bundle) {
			try {
				String tokenString = bundle.getString("authtoken");

				if (tokenString == null) {
					if (bundle.containsKey("accountType") && bundle.containsKey("authAccount")) {
						getAuthorization();
					}
				} else {
					_accessToken = new AccessToken(tokenString);

					if (_accessToken.isExpired()) {
						_accountManager.invalidateAuthToken(
								Constants.FIELD_NATION_ACCOUNT_TYPE,
								tokenString);
						getAuthorization();
					} else {
						_gs.accessToken = _accessToken;
						onHaveAuthToken(_accessToken);
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	};

	/*-*********************************************-*/
	/*-				Abstract Methods				-*/
	/*-*********************************************-*/
	public abstract void onHaveAuthToken(AccessToken accessToken);

	public abstract void onFailedAuthToken(Exception ex);
}
