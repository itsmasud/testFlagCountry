package com.fieldnation;

import java.text.ParseException;

import com.fieldnation.auth.AuthWaitAsyncTask;
import com.fieldnation.auth.AuthWaitAsyncTaskListener;
import com.fieldnation.service.rpc.WorkorderGetRequestedRpc;
import com.fieldnation.webapi.AccessToken;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity {
	private ActionBarDrawerToggle _drawerToggle;
	private AuthWaitAsyncTask _authWaitAsyncTask;
	private DrawerLayout _drawerLayout;

	private NotificationActionBarView _notificationsView;

	private AccessToken _accessToken;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_drawerLayout = (DrawerLayout) findViewById(R.id.container);

		// TODO, build into another class... or a method
		_drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout,
				R.drawable.abc_ic_menu_moreoverflow_normal_holo_light,
				R.string.launcher_open, R.string.launcher_open);
		// {
		// @Override
		// public void onDrawerClosed(View drawerView) {
		// super.onDrawerClosed(drawerView);
		// getSupportActionBar().setTitle(
		// getResources().getString(R.string.app_name));
		// }
		//
		// @Override
		// public void onDrawerOpened(View drawerView) {
		// super.onDrawerOpened(drawerView);
		// getSupportActionBar().setTitle(
		// getResources().getString(R.string.launcher_open));
		// }
		// };

		_drawerLayout.setDrawerListener(_drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		// TODO, build into a utility class
		_authWaitAsyncTask = new AuthWaitAsyncTask(_authWaitAsyncTaskListener);

		AccountManager am = AccountManager.get(this);
		Account[] accounts = am
				.getAccountsByType(Constants.FIELD_NATION_ACCOUNT_TYPE);
		if (accounts.length == 0) {
			am.addAccount(Constants.FIELD_NATION_ACCOUNT_TYPE,
					Constants.FIELD_NATION_ACCOUNT_TYPE, null, new Bundle(),
					this, amc, null);
		} else {
			// TODO, present a picker for the account
			am.getAuthToken(accounts[0], Constants.FIELD_NATION_ACCOUNT_TYPE,
					new Bundle(), this, amc, null);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		// pull out connections to our custom views
		_notificationsView = (NotificationActionBarView) MenuItemCompat
				.getActionView(menu.findItem(R.id.notifications_menuitem));
		_notificationsView.setCount(10);

		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		_drawerToggle.syncState();
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		System.out.println("Method Stub: onRestoreInstanceState()");
		super.onRestoreInstanceState(savedInstanceState);
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

	// the next two events are related to authentication
	private AuthWaitAsyncTaskListener _authWaitAsyncTaskListener = new AuthWaitAsyncTaskListener() {
		@Override
		public void onFail(Exception ex) {
			// TODO Auto-generated method stub
			System.out.println("Method Stub: onFail()");
		}

		@Override
		public void onComplete(Bundle bundle) {
			try {
				_accessToken = new AccessToken(
						bundle.getString("JSON_ACCESS_TOKEN"));
				WorkorderGetRequestedRpc.sendRpc(MainActivity.this,
						_rpcReciever, 0, _accessToken, 0);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	};

	private AccountManagerCallback<Bundle> amc = new AccountManagerCallback<Bundle>() {
		@Override
		public void run(AccountManagerFuture<Bundle> future) {
			_authWaitAsyncTask.execute(future);
		}
	};

	// handles responses from the RPC service
	private ResultReceiver _rpcReciever = new ResultReceiver(new Handler()) {
		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			// TODO Auto-generated method stub
			System.out.println("Method Stub: onReceiveResult()");
			super.onReceiveResult(resultCode, resultData);
		}
	};

	// when a menu item is selected
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		if (_drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		_drawerToggle.onConfigurationChanged(newConfig);
	}

}
