package com.fieldnation;

import android.util.Log;
import android.os.Bundle;

public class MessageListActivity extends DrawerActivity {
	private static final String TAG = "MessageListActivity";

	// UI
	private ListViewEx _messageListView;

	// Data
	private MessagesListAdapter _listAdapter;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_list);

		_messageListView = (ListViewEx) findViewById(R.id.messages_listview);
		_messageListView.setOnRefreshListener(_listView_onRefreshListener);

		addActionBarAndDrawer(R.id.container);

		_messageListView.setAdapter(getListAdapter());
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
	private MessagesListAdapter.Listener _adapter_listener = new MessagesListAdapter.Listener() {

		@Override
		public void onLoading() {
			// TODO Method Stub: onLoading()
			Log.v(TAG, "Method Stub: onLoading()");

		}

		@Override
		public void onLoadComplete() {
			_messageListView.onRefreshComplete();
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
			if (_listAdapter == null) {
				_listAdapter = new MessagesListAdapter(this);
				_listAdapter.setLoadingListener(_adapter_listener);
			}

			if (!_listAdapter.isViable()) {
				_listAdapter = new MessagesListAdapter(this);
				_listAdapter.setLoadingListener(_adapter_listener);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		return _listAdapter;

	}

}
