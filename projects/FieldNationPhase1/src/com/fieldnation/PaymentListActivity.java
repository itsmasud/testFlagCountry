package com.fieldnation;

import android.os.Bundle;
import android.util.Log;

public class PaymentListActivity extends DrawerActivity {
	private static final String TAG = "PaymentListActivity";

	// UI
	private ListViewEx _listView;

	// Data
	private PaymentListAdapter _adapter;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemlist);

		_listView = (ListViewEx) findViewById(R.id.items_listview);
		_listView.setOnRefreshListener(_listView_onRefreshListener);

		addActionBarAndDrawer(R.id.container);

	}

	@Override
	protected void onResume() {
		_listView.setAdapter(getAdapter());
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
	private PagingListAdapter.Listener _adapter_listener = new PagingListAdapter.Listener() {

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
			getAdapter().update(false);
		}
	};

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	private PagingListAdapter getAdapter() {
		try {
			if (_adapter == null) {
				_adapter = new PaymentListAdapter(this);
				_adapter.setLoadingListener(_adapter_listener);
			}

			if (!_adapter.isViable()) {
				_adapter = new PaymentListAdapter(this);
				_adapter.setLoadingListener(_adapter_listener);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		return _adapter;

	}

}
