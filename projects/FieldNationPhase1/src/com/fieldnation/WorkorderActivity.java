package com.fieldnation;

import java.text.ParseException;

import com.fieldnation.auth.FutureWaitAsyncTask;
import com.fieldnation.auth.FutureWaitAsyncTaskListener;
import com.fieldnation.service.rpc.WorkorderGetRequestedRpc;
import com.fieldnation.webapi.AccessToken;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.Tab;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
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

public class WorkorderActivity extends ActionBarActivity {
	// UI
	private ActionBarDrawerToggle _drawerToggle;
	private DrawerLayout _drawerLayout;
	private NotificationActionBarView _notificationsView;

	// Data
	private AccessToken _accessToken;
	
	// Other
	private FutureWaitAsyncTask _futureWaitAsyncTask;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workorder);

		buildTabs();
		buildDrawer();
		getAuthorization();
	}

	private void buildTabs() {
		ActionBar actionbar = getSupportActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeButtonEnabled(true);

		ActionBar.Tab tab1 = actionbar.newTab().setText("In Progress");
		ActionBar.Tab tab2 = actionbar.newTab().setText("Assigned");
		ActionBar.Tab tab3 = actionbar.newTab().setText("Completed");
		ActionBar.Tab tab4 = actionbar.newTab().setText("Cancelled");

		// TODO add the other fragments
		tab1.setTabListener(_tabListener);
		tab2.setTabListener(new FragmentSwaperTabListener(
				new WorkorderAssignedFragment(), R.id.workorder_content));
		tab3.setTabListener(_tabListener);
		tab4.setTabListener(_tabListener);

		// place the tabs
		actionbar.addTab(tab1);
		actionbar.addTab(tab2);
		actionbar.addTab(tab3);
		actionbar.addTab(tab4);
	}

	private void buildDrawer() {
		_drawerLayout = (DrawerLayout) findViewById(R.id.container);
		_drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout,
				R.drawable.ic_navigation_drawer, R.string.launcher_open,
				R.string.launcher_open) {

			@Override
			public void onDrawerStateChanged(int newState) {
				if (newState != 0) {
					getSupportActionBar().setNavigationMode(
							ActionBar.NAVIGATION_MODE_STANDARD);
				}
				super.onDrawerStateChanged(newState);
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				if (slideOffset == 0.0) {
					getSupportActionBar().setNavigationMode(
							ActionBar.NAVIGATION_MODE_TABS);
				}
				System.out
						.println("Method Stub: onDrawerSlide()" + slideOffset);
				super.onDrawerSlide(drawerView, slideOffset);
			}
		};

		_drawerLayout.setDrawerListener(_drawerToggle);

	}

	private void getAuthorization() {
		_futureWaitAsyncTask = new FutureWaitAsyncTask(
				_futureWaitAsyncTaskListener);

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

	// TODO remove
	private TabListener _tabListener = new TabListener() {
		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
			// TODO Auto-generated method stub
			System.out.println("Method Stub: onTabUnselected()");

		}

		@Override
		public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
			// TODO Auto-generated method stub
			System.out.println("Method Stub: onTabSelected()");

		}

		@Override
		public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
			// TODO Auto-generated method stub
			System.out.println("Method Stub: onTabReselected()");

		}
	};

	// the next two events are related to authentication
	private FutureWaitAsyncTaskListener _futureWaitAsyncTaskListener = new FutureWaitAsyncTaskListener() {
		@Override
		public void onFail(Exception ex) {
			// TODO Auto-generated method stub
			System.out.println("Method Stub: onFail()");
		}

		@Override
		public void onComplete(Bundle bundle) {
			try {
				String tokenString = bundle.getString("authtoken");

				if (tokenString == null) {
					if (bundle.containsKey("accountType")
							&& bundle.containsKey("jacobfaketech")) {
						getAuthorization();
					}
				} else {
					_accessToken = new AccessToken(tokenString);

					startService(WorkorderGetRequestedRpc.makeIntent(
							WorkorderActivity.this, _rpcReciever, 0,
							_accessToken, 0));

				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	};

	private AccountManagerCallback<Bundle> amc = new AccountManagerCallback<Bundle>() {
		@Override
		public void run(AccountManagerFuture<Bundle> future) {
			_futureWaitAsyncTask.execute(future);
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
