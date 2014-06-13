package com.fieldnation;

import java.text.ParseException;

import com.fieldnation.auth.FutureWaitAsyncTask;
import com.fieldnation.service.ClockReceiver;
import com.fieldnation.webapi.OAuth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public abstract class BaseActivity extends ActionBarActivity {
	private static final String TAG = "BaseActivity";

	// UI
	NotificationActionBarView _notificationsView;
	MessagesActionBarView _messagesView;

	// Data
	GlobalState _gs;
	OAuth _accessToken;

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

		ClockReceiver.registerClock(this);

		getAuthorization();
	}

	private void getAuthorization() {
		if (_accountManager == null)
			_accountManager = AccountManager.get(this);

		Account[] accounts = _accountManager.getAccountsByType(_gs.accountType);
		Log.v(TAG, "Found accounts: " + accounts.length);
		AccountManagerFuture<Bundle> future = null;
		if (accounts.length == 0) {
			future = _accountManager.addAccount(_gs.accountType, null, null,
					null, this, _amc, new Handler());

		} else if (accounts.length == 1) {
			future = _accountManager.getAuthToken(accounts[0], _gs.accountType,
					null, this, _amc, new Handler());
		} else {
			// TODO, ANDR-10 present a picker for the account
			future = _accountManager.getAuthToken(accounts[0], _gs.accountType,
					null, this, _amc, new Handler());

		}
		if (future != null) {
			// new
			new FutureWaitAsyncTask(_futureWaitAsyncTaskListener).execute(future);
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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

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
					_accessToken = OAuth.fromString(tokenString);

					if (_accessToken.isExpired()) {
						_accountManager.invalidateAuthToken(_gs.accountType,
								tokenString);
						getAuthorization();
					} else {
						_gs.oAuth = _accessToken;
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
	public abstract void onHaveAuthToken(OAuth oAuth);

	public abstract void onFailedAuthToken(Exception ex);
}
