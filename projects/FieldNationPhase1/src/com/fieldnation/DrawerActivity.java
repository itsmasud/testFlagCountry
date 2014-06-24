package com.fieldnation;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

public class DrawerActivity extends BaseActivity {
	private final static String TAG = "DrawerActivity";
	// UI
	private ActionBarDrawerToggle _drawerToggle = null;
	private DrawerLayout _drawerLayout;

	public void addActionBarAndDrawer(int drawerId) {
		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeButtonEnabled(true);
		actionbar.setHomeAsUpIndicator(R.drawable.ic_navigation_drawer);

		_drawerLayout = (DrawerLayout) findViewById(drawerId);
		_drawerToggle = getActionBarDrawerToggle();

		_drawerLayout.setDrawerListener(_drawerToggle);
	}

	public final ActionBarDrawerToggle getActionBarDrawerToggle() {
		if (_drawerToggle == null) {
			_drawerToggle = createActionBarDrawerToggle(_drawerLayout);
		}

		return _drawerToggle;
	}

	public ActionBarDrawerToggle createActionBarDrawerToggle(
			DrawerLayout drawerLayout) {
		return new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_navigation_drawer, R.string.launcher_open,
				R.string.launcher_open);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		_drawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
