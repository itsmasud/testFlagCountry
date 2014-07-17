package com.fieldnation.ui.market;

import com.fieldnation.R;
import com.fieldnation.R.id;
import com.fieldnation.R.layout;
import com.fieldnation.R.string;
import com.fieldnation.ui.DrawerActivity;
import com.fieldnation.ui.ListViewEx;
import com.fieldnation.ui.PagingListAdapter;
import com.fieldnation.ui.ListViewEx.OnRefreshListener;
import com.fieldnation.ui.PagingListAdapter.Listener;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.ui.workorder.WorkorderListAdapter;

import android.os.Bundle;

/**
 * Displays all the work orders in the market that are available to this user
 * 
 * @author michael.carver
 * 
 */
public class MarketActivity extends DrawerActivity {
	private static final String TAG = "ui.market.MarketActivity";

	// UI
	private ListViewEx _listView;

	// Data
	private WorkorderListAdapter _adapter;

	// Services

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemlist);
		setTitle(R.string.market_title);

		_listView = (ListViewEx) findViewById(R.id.items_listview);
		_listView.setOnRefreshListener(_listView_onRefreshListener);

		addActionBarAndDrawer(R.id.container);

	}

	@Override
	protected void onResume() {
		_listView.setAdapter(getListAdapter());
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (_adapter != null) {
			_adapter.onStop();
			_adapter = null;
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private WorkorderListAdapter.Listener _workorderAdapter_listener = new WorkorderListAdapter.Listener() {

		@Override
		public void onLoading() {
		}

		@Override
		public void onLoadComplete() {
			_listView.onRefreshComplete();
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
			if (_adapter == null) {
				_adapter = new WorkorderListAdapter(this, WorkorderDataSelector.AVAILABLE);
				_adapter.setLoadingListener(_workorderAdapter_listener);
			}

			if (!_adapter.isViable()) {
				_adapter = new WorkorderListAdapter(this, WorkorderDataSelector.AVAILABLE);
				_adapter.setLoadingListener(_workorderAdapter_listener);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		return _adapter;

	}
}
