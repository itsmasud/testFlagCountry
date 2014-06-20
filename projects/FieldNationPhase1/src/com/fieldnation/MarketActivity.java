package com.fieldnation;

import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Displays all the work orders in the market that are available to this user
 * 
 * @author michael.carver
 * 
 */
public class MarketActivity extends BaseActivity {
	private static final String TAG = "MarketActivity";

	// UI
	private ActionBarDrawerToggle _drawerToggle;
	private DrawerLayout _drawerLayout;
	private ListView _workordersListView;
	private ProgressBar _loadingProgressBar;
	private TextView _noDataTextView;
	private ImageButton _refreshButton;

	// Data
	private WorkorderListAdapter _woAdapter;
	private boolean _hasData = false;

	// Services

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_market);
		setTitle(R.string.market_title);

		_workordersListView = (ListView) findViewById(R.id.workorder_listview);
		_loadingProgressBar = (ProgressBar) findViewById(R.id.loading_progressbar);
		_noDataTextView = (TextView) findViewById(R.id.nodata_textview);
		_refreshButton = (ImageButton) findViewById(R.id.refresh_button);
		_refreshButton.setOnClickListener(_refresh_onClick);

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setHomeButtonEnabled(true);
		actionbar.setHomeAsUpIndicator(R.drawable.ic_navigation_drawer);

		buildDrawer();

		try {
			_woAdapter = new WorkorderListAdapter(MarketActivity.this,
					WorkorderListAdapter.TYPE_AVAILABLE);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		_woAdapter.registerDataSetObserver(_listAdapter_observer);
		_workordersListView.setAdapter(_woAdapter);
		_woAdapter.update(true);

	}

	private void buildDrawer() {
		_drawerLayout = (DrawerLayout) findViewById(R.id.container);
		_drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout,
				R.drawable.ic_navigation_drawer, R.string.launcher_open,
				R.string.launcher_open);

		_drawerLayout.setDrawerListener(_drawerToggle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		_drawerToggle.syncState();
	}

	@Override
	protected void onStart() {
		super.onStart();
		updateUi();
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (_woAdapter != null) {
			_woAdapter.onStop();
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _refresh_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_hasData = false;
			updateUi();
			_woAdapter.update(false);
		}
	};

	// when a menu item is selected
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

	private DataSetObserver _listAdapter_observer = new DataSetObserver() {
		@Override
		public void onChanged() {
			_hasData = true;
			updateUi();
			super.onChanged();
		}
	};

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	private void updateUi() {
		if (_loadingProgressBar != null) {
			if (_hasData) {
				_loadingProgressBar.setVisibility(View.GONE);
				if (_woAdapter != null) {
					if (_woAdapter.getCount() == 0) {
						_noDataTextView.setVisibility(View.VISIBLE);
						_refreshButton.setVisibility(View.VISIBLE);
					} else {
						_noDataTextView.setVisibility(View.GONE);
						_refreshButton.setVisibility(View.GONE);
					}
				}
			} else {
				_noDataTextView.setVisibility(View.GONE);
				_refreshButton.setVisibility(View.GONE);
				_loadingProgressBar.setVisibility(View.VISIBLE);
			}
		}
	}

}
