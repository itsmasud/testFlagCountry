package com.fieldnation;

import java.text.ParseException;

import com.fieldnation.authserver.FutureWaitAsyncTask;
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

/**
 * This is the base of all the activities in this project. It provides
 * authentication and applies the action bars
 * 
 * @author michael.carver
 * 
 */
public abstract class BaseActivity extends ActionBarActivity {
	private static final String TAG = "BaseActivity";

	// UI
	NotificationActionBarView _notificationsView;
	MessagesActionBarView _messagesView;

	// Data
	GlobalState _gs;
	private Account _account;
	private boolean _authenticating = false;

	// Services
	private AccountManager _accountManager;

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
	}

	final public void getAccount() {
		if (_authenticating)
			return;

		_authenticating = true;
		if (_accountManager == null)
			_accountManager = AccountManager.get(this);

		Account[] accounts = _accountManager.getAccountsByType(_gs.accountType);
		Log.v(TAG, "Found accounts: " + accounts.length);
		AccountManagerFuture<Bundle> future = null;
		if (accounts.length == 0) {
			future = _accountManager.addAccount(_gs.accountType, null, null,
					null, this, null, new Handler());

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
	private AuthenticationServer authServer = new AuthenticationServer() {

		@Override
		public void requestAuthentication(AuthenticationClient client) {
			// TODO Method Stub: requestAuthentication()
			Log.v(TAG, "Method Stub: requestAuthentication()");
			if (_account == null) {
				Log.v(TAG, "Method Stub: requestAuthentication()1");
				client.waitForObject(BaseActivity.this, "_acccount");
			} else {
				Log.v(TAG, "Method Stub: requestAuthentication()2");
				AccountManagerFuture<Bundle> future = _accountManager.getAuthToken(
						_account, _gs.accountType, null, BaseActivity.this,
						null, new Handler());
				client.waitForFuture(future);
			}
		}

		@Override
		public void invalidateToken(String token) {
			Log.v(TAG, "invalidateToken(" + token + ")");
			_accountManager.invalidateAuthToken(_gs.accountType, token);
		}
	};

	// Menu
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

	// Authentication
	// private AccountManagerCallback<Bundle> _amc = new
	// AccountManagerCallback<Bundle>() {
	// @Override
	// public void run(AccountManagerFuture<Bundle> future) {
	// Log.v(TAG, "_amc.run()");
	// new FutureWaitAsyncTask(_futureWaitAsyncTaskListener).execute(future);
	// }
	// };

	private FutureWaitAsyncTask.Listener _futureWaitAsyncTaskListener = new FutureWaitAsyncTask.Listener() {
		@Override
		public void onFail(Exception ex) {
			// TODO stub
			Log.v(TAG, "_futureWaitAsyncTaskListener.onFail()");
		}

		@Override
		public void onComplete(Bundle bundle) {
			String tokenString = bundle.getString("authtoken");

			if (tokenString == null) {
				if (bundle.containsKey("accountType") && bundle.containsKey("authAccount")) {
					getAccount();
				}
			}
		}
	};

}
