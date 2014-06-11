package com.fieldnation;

import com.fieldnation.webapi.AccessToken;

import android.accounts.AccountManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

public class MarketActivity extends BaseActivity {
	private static final String TAG = "MarketActivity";

	// UI
	private ActionBarDrawerToggle _drawerToggle;
	private DrawerLayout _drawerLayout;

	// Data

	// Services

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_market);
		setTitle(R.string.market_title);

		buildDrawer();

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeButtonEnabled(true);

	}

	private void buildDrawer() {
		_drawerLayout = (DrawerLayout) findViewById(R.id.container);
		_drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout, R.drawable.ic_navigation_drawer, R.string.launcher_open, R.string.launcher_open) {

			// @Override
			// public void onDrawerStateChanged(int newState) {
			// if (newState != 0) {
			// supportInvalidateOptionsMenu();
			// }
			// super.onDrawerStateChanged(newState);
			// }
			//
			// @Override
			// public void onDrawerSlide(View drawerView, float slideOffset) {
			// if (slideOffset == 0.0) {
			// supportInvalidateOptionsMenu();
			// }
			// super.onDrawerSlide(drawerView, slideOffset);
			// }
		};

		_drawerLayout.setDrawerListener(_drawerToggle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

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
