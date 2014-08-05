package com.fieldnation.ui;

import com.fieldnation.R;
import com.fieldnation.data.profile.Notification;

import eu.erikw.PullToRefreshListView;
import android.os.Bundle;

public class NotificationListActivity extends BaseActivity {
	private static final String TAG = "ui.NotificationListActivity";

	// UI
	private PullToRefreshListView _listView;

	// Data
	private NotificationListAdapter _adapter;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemlist);

		_listView = (PullToRefreshListView) findViewById(R.id.items_listview);
		_listView.setOnRefreshListener(_listView_onRefreshListener);

		// addActionBarAndDrawer(R.id.container);

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
	private NotificationListAdapter.Listener<Notification> _adapter_listener = new NotificationListAdapter.Listener<Notification>() {

		@Override
		public void onLoading() {
			_listView.setRefreshing();
		}

		@Override
		public void onLoadComplete() {
			_listView.onRefreshComplete();
		}
	};

	private PullToRefreshListView.OnRefreshListener _listView_onRefreshListener = new PullToRefreshListView.OnRefreshListener() {

		@Override
		public void onRefresh() {
			getListAdapter().update(false);
		}
	};

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/

	private NotificationListAdapter getListAdapter() {
		try {
			if (_adapter == null) {
				_adapter = new NotificationListAdapter(this);
				_adapter.setLoadingListener(_adapter_listener);
			}

			if (!_adapter.isViable()) {
				_adapter = new NotificationListAdapter(this);
				_adapter.setLoadingListener(_adapter_listener);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		return _adapter;

	}

}
