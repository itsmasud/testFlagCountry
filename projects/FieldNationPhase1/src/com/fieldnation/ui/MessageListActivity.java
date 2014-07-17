package com.fieldnation.ui;

import com.fieldnation.R;
import com.fieldnation.R.id;
import com.fieldnation.R.layout;

import android.os.Bundle;

public class MessageListActivity extends BaseActivity {
	private static final String TAG = "ui.MessageListActivity";

	// UI
	private ListViewEx _listView;

	// Data
	private MessagesListAdapter _adapter;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemlist);

		_listView = (ListViewEx) findViewById(R.id.items_listview);
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
	private MessagesListAdapter.Listener _adapter_listener = new MessagesListAdapter.Listener() {

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

	private MessagesListAdapter getListAdapter() {
		try {
			if (_adapter == null) {
				_adapter = new MessagesListAdapter(this);
				_adapter.setLoadingListener(_adapter_listener);
			}

			if (!_adapter.isViable()) {
				_adapter = new MessagesListAdapter(this);
				_adapter.setLoadingListener(_adapter_listener);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		return _adapter;

	}

}
