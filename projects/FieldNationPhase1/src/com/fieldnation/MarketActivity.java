package com.fieldnation;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
public class MarketActivity extends DrawerActivity {
	private static final String TAG = "MarketActivity";

	// UI
	private ListViewEx _workordersListView;

	// Data
	private WorkorderListAdapter _listAdapter;

	// Services

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_market);
		setTitle(R.string.market_title);

		_workordersListView = (ListViewEx) findViewById(R.id.workorder_listview);
		_workordersListView.setOnRefreshListener(_listView_onRefreshListener);

		addActionBarAndDrawer(R.id.container);

		_workordersListView.setAdapter(getListAdapter());
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (_listAdapter != null) {
			_listAdapter.onStop();
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private WorkorderListAdapter.Listener _workorderAdapter_listener = new WorkorderListAdapter.Listener() {

		@Override
		public void onLoading() {
			// TODO Method Stub: onLoading()
			Log.v(TAG, "Method Stub: onLoading()");

		}

		@Override
		public void onLoadComplete() {
			_workordersListView.onRefreshComplete();
		}
	};

	private ListViewEx.OnRefreshListener _listView_onRefreshListener = new ListViewEx.OnRefreshListener() {

		@Override
		public void onRefresh() {
			getListAdapter().update(false);
		}
	};

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	private WorkorderListAdapter getListAdapter() {
		try {
			if (_listAdapter == null) {
				_listAdapter = new WorkorderListAdapter(this,
						WorkorderDataSelector.AVAILABLE);
				_listAdapter.setLoadingListener(_workorderAdapter_listener);
			}

			if (!_listAdapter.isViable()) {
				_listAdapter = new WorkorderListAdapter(this,
						WorkorderDataSelector.AVAILABLE);
				_listAdapter.setLoadingListener(_workorderAdapter_listener);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		return _listAdapter;

	}
}
