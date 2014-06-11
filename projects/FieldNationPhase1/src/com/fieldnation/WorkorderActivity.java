package com.fieldnation;

import java.text.ParseException;

import com.fieldnation.R;
import com.fieldnation.auth.FutureWaitAsyncTask;
import com.fieldnation.service.rpc.WorkorderRpc;
import com.fieldnation.webapi.AccessToken;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class WorkorderActivity extends ActionBarActivity {
	private static final String TAG = "WorkorderActivity";

	private static final int RPC_GET_REQUESTED = 1;
	// UI
	private ActionBarDrawerToggle _drawerToggle;
	private DrawerLayout _drawerLayout;
	private NotificationActionBarView _notificationsView;
	private MessagesActionBarView _messagesView;
	private ViewPager _viewPager;

	private Fragment[] _fragments;

	// Data
	private GlobalState _gs;
	private AccessToken _accessToken;
	private PagerAdapter _pagerAdapter;
	private WorkorderRpc _workorderRpc;

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
		setContentView(R.layout.activity_workorder);

		_gs = (GlobalState) getApplicationContext();

		_accountManager = AccountManager.get(this);

		// TODO present wait dialog
		buildTabs();
		buildDrawer();
		getAuthorization();
	}

	private void buildTabs() {
		_viewPager = (ViewPager) findViewById(R.id.content_viewpager);

		_fragments = new Fragment[4];

		_fragments[0] = new WorkorderListFragment().setDisplayType(WorkorderListFragment.TYPE_REQUESTED);
		_fragments[1] = new WorkorderListFragment().setDisplayType(WorkorderListFragment.TYPE_ASSIGNED);
		_fragments[2] = new WorkorderListFragment().setDisplayType(WorkorderListFragment.TYPE_COMPLETED);
		_fragments[3] = new WorkorderListFragment().setDisplayType(WorkorderListFragment.TYPE_CANCELED);

		ActionBar actionbar = getSupportActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeButtonEnabled(true);

		ActionBar.Tab tab1 = actionbar.newTab().setText("In Progress");
		ActionBar.Tab tab2 = actionbar.newTab().setText("Assigned");
		ActionBar.Tab tab3 = actionbar.newTab().setText("Completed");
		ActionBar.Tab tab4 = actionbar.newTab().setText("Canceled");

		tab1.setTabListener(_tabListener);
		tab2.setTabListener(_tabListener);
		tab3.setTabListener(_tabListener);
		tab4.setTabListener(_tabListener);

		actionbar.addTab(tab1);
		actionbar.addTab(tab2);
		actionbar.addTab(tab3);
		actionbar.addTab(tab4);

		_pagerAdapter = new ScreenSlitdePagerAdapter(getSupportFragmentManager());

		_viewPager.setAdapter(_pagerAdapter);
		_viewPager.setOnPageChangeListener(_viewPager_onChange);
	}

	private void buildDrawer() {
		_drawerLayout = (DrawerLayout) findViewById(R.id.container);
		_drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout, R.drawable.ic_navigation_drawer, R.string.launcher_open, R.string.launcher_open) {

			@Override
			public void onDrawerStateChanged(int newState) {
				if (newState != 0) {
					getSupportActionBar().setNavigationMode(
							ActionBar.NAVIGATION_MODE_STANDARD);
					supportInvalidateOptionsMenu();
				}
				super.onDrawerStateChanged(newState);
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				if (slideOffset == 0.0) {
					getSupportActionBar().setNavigationMode(
							ActionBar.NAVIGATION_MODE_TABS);
					supportInvalidateOptionsMenu();
				}
				super.onDrawerSlide(drawerView, slideOffset);
			}
		};

		_drawerLayout.setDrawerListener(_drawerToggle);

	}

	private void getAuthorization() {
		Account[] accounts = _accountManager.getAccountsByType(Constants.FIELD_NATION_ACCOUNT_TYPE);
		Log.v(TAG, "Found accounts: " + accounts.length);
		AccountManagerFuture<Bundle> future = null;
		if (accounts.length == 0) {
			future = _accountManager.addAccount(
					Constants.FIELD_NATION_ACCOUNT_TYPE, null, null, null,
					this, amc, new Handler());

		} else if (accounts.length == 1) {
			future = _accountManager.getAuthToken(accounts[0],
					Constants.FIELD_NATION_ACCOUNT_TYPE, null, this, amc,
					new Handler());
		} else {
			// TODO, ANDR-10 present a picker for the account

			future = _accountManager.getAuthToken(accounts[0],
					Constants.FIELD_NATION_ACCOUNT_TYPE, null, this, amc,
					new Handler());

		}
		if (future != null) {
			// new
			// FutureWaitAsyncTask(_futureWaitAsyncTaskListener).execute(future);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		// pull out connections to our custom views
		_notificationsView = (NotificationActionBarView) MenuItemCompat.getActionView(menu.findItem(R.id.notifications_menuitem));
		_notificationsView.setCount(10);

		_messagesView = (MessagesActionBarView) MenuItemCompat.getActionView(menu.findItem(R.id.messages_menuitem));
		_messagesView.setCount(100);

		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		_drawerToggle.syncState();
	}

	@Override
	protected void onStop() {
		// TODO Method Stub: onStop()
		Log.v(TAG, "Method Stub: onStop()");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Method Stub: onDestroy()
		Log.v(TAG, "Method Stub: onDestroy()");
		super.onDestroy();
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

	private class ScreenSlitdePagerAdapter extends FragmentStatePagerAdapter {

		public ScreenSlitdePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int postition) {
			return _fragments[postition];
		}

		@Override
		public int getCount() {
			return _fragments.length;
		}

	}

	private ViewPager.SimpleOnPageChangeListener _viewPager_onChange = new ViewPager.SimpleOnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			getSupportActionBar().setSelectedNavigationItem(position);
		};
	};

	private TabListener _tabListener = new TabListener() {

		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		}

		@Override
		public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
			_viewPager.setCurrentItem(arg0.getPosition(), true);
		}

		@Override
		public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		}
	};

	// the next two events are related to authentication
	private FutureWaitAsyncTask.Listener _futureWaitAsyncTaskListener = new FutureWaitAsyncTask.Listener() {
		@Override
		public void onFail(Exception ex) {
			// TODO Method Stub: onFail()
			System.out.println("Method Stub: onFail()");

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

						_workorderRpc = new WorkorderRpc(WorkorderActivity.this, _accessToken, _rpcReciever);
						startService(_workorderRpc.getRequested(
								RPC_GET_REQUESTED, 1));
					}

				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

	};

	private AccountManagerCallback<Bundle> amc = new AccountManagerCallback<Bundle>() {
		@Override
		public void run(AccountManagerFuture<Bundle> future) {
			Log.v(TAG, "amc");
			_amc_future = new FutureWaitAsyncTask(_futureWaitAsyncTaskListener);
			_amc_future.execute(future);
			Log.v(TAG, "/amc");
		}
	};

	// handles responses from the RPC service
	private ResultReceiver _rpcReciever = new ResultReceiver(new Handler()) {
		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			// TODO Method Stub: onReceiveResult()
			Log.v(TAG, "Method Stub: onReceiveResult()");
			super.onReceiveResult(resultCode, resultData);
		};
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
