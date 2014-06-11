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

public class MyWorkActivity extends BaseActivity {
	private static final String TAG = "MyWorkActivity";

	// UI
	private ActionBarDrawerToggle _drawerToggle;
	private DrawerLayout _drawerLayout;
	private ViewPager _viewPager;

	private Fragment[] _fragments;

	// Data
	private PagerAdapter _pagerAdapter;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mywork);

		_gs = (GlobalState) getApplicationContext();

		buildTabs();
		buildDrawer();
	}

	private void buildTabs() {
		_viewPager = (ViewPager) findViewById(R.id.content_viewpager);

		_fragments = new Fragment[4];

		_fragments[0] = new MyWorkListFragment().setDisplayType(MyWorkListFragment.TYPE_REQUESTED);
		_fragments[1] = new MyWorkListFragment().setDisplayType(MyWorkListFragment.TYPE_ASSIGNED);
		_fragments[2] = new MyWorkListFragment().setDisplayType(MyWorkListFragment.TYPE_COMPLETED);
		_fragments[3] = new MyWorkListFragment().setDisplayType(MyWorkListFragment.TYPE_CANCELED);

		ActionBar actionbar = getSupportActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeButtonEnabled(true);

		ActionBar.Tab tab1 = actionbar.newTab().setText(
				getString(R.string.my_work_category1));
		ActionBar.Tab tab2 = actionbar.newTab().setText(
				getString(R.string.my_work_category2));
		ActionBar.Tab tab3 = actionbar.newTab().setText(
				getString(R.string.my_work_category3));
		ActionBar.Tab tab4 = actionbar.newTab().setText(
				getString(R.string.my_work_category4));

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

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		_drawerToggle.syncState();
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

	// swaps fragments on a pager transition
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

	// sync set actionbar tabs on page viewer change
	private ViewPager.SimpleOnPageChangeListener _viewPager_onChange = new ViewPager.SimpleOnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			getSupportActionBar().setSelectedNavigationItem(position);
		};
	};

	// sync pageviewer based on tab selection
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

	// when a menu item is selected
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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

	@Override
	public void onHaveAuthToken(AccessToken accessToken) {
		// TODO Method Stub: onHaveAuthToken()
		Log.v(TAG, "Method Stub: onHaveAuthToken()");

	}

	@Override
	public void onFailedAuthToken(Exception ex) {
		// TODO Method Stub: onFailedAuthToken()
		Log.v(TAG, "Method Stub: onFailedAuthToken()");

	}

}
