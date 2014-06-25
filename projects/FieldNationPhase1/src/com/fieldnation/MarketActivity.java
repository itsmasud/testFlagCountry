package com.fieldnation;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
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
public class MarketActivity extends DrawerActivity {
	private static final String TAG = "MarketActivity";

	// UI
	private ListViewEx _workordersListView;

	// Data
	private WorkorderListAdapter _woAdapter;

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

		try {
			_woAdapter = new WorkorderListAdapter(MarketActivity.this,
					DataSelector.AVAILABLE);
			_woAdapter.setLoadingListener(_workorderAdapter_listener);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		_workordersListView.setAdapter(_woAdapter);
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
			_woAdapter.update(false);
		}
	};

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/

}
