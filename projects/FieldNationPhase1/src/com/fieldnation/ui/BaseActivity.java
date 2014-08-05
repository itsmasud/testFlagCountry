package com.fieldnation.ui;

import com.fieldnation.FutureWaitAsyncTask;
import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.auth.client.AuthenticationServer;
import com.fieldnation.rpc.server.ClockReceiver;
import com.fieldnation.ui.market.MarketActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This is the base of all the activities in this project. It provides
 * authentication and sets up the action bars.
 * 
 * @author michael.carver
 * 
 */
public abstract class BaseActivity extends ActionBarActivity {
	private static final String TAG = "ui.BaseActivity";

	// UI
	NotificationActionBarView _notificationsView;
	MessagesActionBarView _messagesView;

	// Data
	protected GlobalState _gs;
	private Account _account = null;
	private boolean _authenticating = false;
	private boolean _removing = false;

	// Services
	private AccountManager _accountManager;
	private Handler _handler = new Handler();

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		_gs = (GlobalState) getApplicationContext();
		_gs.setAuthenticationServer(authServer);

		ClockReceiver.registerClock(this);

		getAccount();

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeButtonEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		_notificationsView = (NotificationActionBarView) MenuItemCompat.getActionView(menu.findItem(R.id.notifications_menuitem));
		// _notificationsView.setCount(10);

		_messagesView = (MessagesActionBarView) MenuItemCompat.getActionView(menu.findItem(R.id.messages_menuitem));

		return true;

	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

	/**
	 * Implements the AuthenticationServer interface.
	 */
	private AuthenticationServer authServer = new AuthenticationServer() {

		@Override
		public void requestAuthentication(AuthenticationClient client) {
			Log.v(TAG, "requestAuthentication()");
			if (_removing) {
				client.waitForTime(10000);
				return;
			}

			if (_account == null) {
				Log.v(TAG, "requestAuthentication() no account");
				client.waitForObject(BaseActivity.this, "_acccount");
				if (!_authenticating) {
					getAccount();
				}
			} else {
				Log.v(TAG, "requestAuthentication() asking for account token");
				AccountManagerFuture<Bundle> future = _accountManager.getAuthToken(_account, _gs.accountType, null,
						BaseActivity.this, null, _handler);
				client.waitForFuture(future);
			}
		}

		@Override
		public void invalidateToken(String token) {
			Log.v(TAG, "invalidateToken(" + token + ")");
			if (_removing) {
				return;
			}
			_accountManager.invalidateAuthToken(_gs.accountType, token);
		}

		@Override
		public void removeAccount(AuthenticationClient client) {
			if (_removing)
				return;

			_removing = true;
			_authenticating = false;
			AccountManagerFuture<Boolean> future = _accountManager.removeAccount(_account, null, _handler);
			new FutureWaitAsyncTask(_futureWaitAsyncTaskListener).execute(future);
			_account = null;
		}
	};

	// Menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent gohome = new Intent(this, MarketActivity.class);
			startActivity(gohome);
			return true;
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private FutureWaitAsyncTask.Listener _futureWaitAsyncTaskListener = new FutureWaitAsyncTask.Listener() {
		@Override
		public void onFail(Exception ex) {
			// TODO stub onFail()
			Log.v(TAG, "_futureWaitAsyncTaskListener.onFail()");
			_authenticating = false;
			_removing = false;
		}

		@Override
		public void onComplete(Object result) {
			if (!_removing) {
				Bundle bundle = (Bundle) result;
				Log.v(TAG, "_futureWaitAsyncTaskListener.onComplete()");
				String tokenString = bundle.getString("authtoken");

				_authenticating = false;

				if (tokenString == null) {
					if (bundle.containsKey("accountType") && bundle.containsKey("authAccount")) {
						getAccount();
					}
				}
			} else if (_removing) {
				_removing = false;
				// restart the app to clear the backstack, and anything stored
				// in the UI.
				Intent activity = new Intent(BaseActivity.this, MarketActivity.class);
				PendingIntent pendingIntent = PendingIntent.getActivity(BaseActivity.this, 1, activity,
						PendingIntent.FLAG_CANCEL_CURRENT);
				AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
				System.exit(0);
			}

		}
	};

	private void getAccount() {
		Log.v(TAG, "getAccount()");
		if (_authenticating)
			return;

		if (_removing)
			return;

		Log.v(TAG, "getAccount() not authenticating");

		_authenticating = true;
		if (_accountManager == null)
			_accountManager = AccountManager.get(this);

		Account[] accounts = _accountManager.getAccountsByType(_gs.accountType);
		Log.v(TAG, "Found accounts: " + accounts.length);
		AccountManagerFuture<Bundle> future = null;
		if (accounts.length == 0) {
			future = _accountManager.addAccount(_gs.accountType, null, null, null, this, null, new Handler());

		} else if (accounts.length == 1) {
			_account = accounts[0];
		} else {
			// TODO, ANDR-10 present a picker for the account
			_account = accounts[0];
		}
		if (future != null) {
			// new
			Log.v(TAG, "got future");
			new FutureWaitAsyncTask(_futureWaitAsyncTaskListener).execute(future);
		}

	}

}
