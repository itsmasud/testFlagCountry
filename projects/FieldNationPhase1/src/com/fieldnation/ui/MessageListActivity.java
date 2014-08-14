package com.fieldnation.ui;

import com.fieldnation.R;
import com.fieldnation.data.profile.Message;
import com.fieldnation.ui.workorder.WorkorderActivity;

import eu.erikw.PullToRefreshListView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

public class MessageListActivity extends BaseActivity {
	private static final String TAG = "ui.MessageListActivity";

	// UI
	private PullToRefreshListView _listView;
	private SmoothProgressBar _loadingProgress;
	private TextView _hidden_workorder_id;

	// Data
	private MessagesListAdapter _adapter;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemlist_nodrawer);

		_listView = (PullToRefreshListView) findViewById(R.id.items_listview);
		_listView.setOnRefreshListener(_listView_onRefreshListener);
		_listView.setOnItemClickListener(_listView_ItemClickListener);
		
		_loadingProgress = (SmoothProgressBar) findViewById(R.id.loading_progress);
		_loadingProgress.setSmoothProgressDrawableCallbacks(_loadingCallback);
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
	private SmoothProgressDrawable.Callbacks _loadingCallback = new SmoothProgressDrawable.Callbacks() {
		@Override
		public void onStop() {
			_loadingProgress.setVisibility(View.GONE);
		}

		@Override
		public void onStart() {
			_loadingProgress.setVisibility(View.VISIBLE);
		}
	};

	private MessagesListAdapter.Listener<Message> _adapter_listener = new MessagesListAdapter.Listener<Message>() {

		@Override
		public void onLoading() {
			_listView.setRefreshing();
			_loadingProgress.progressiveStart();
		}

		@Override
		public void onLoadComplete() {
			_listView.onRefreshComplete();
			_loadingProgress.progressiveStop();
		}
	};

	private PullToRefreshListView.OnRefreshListener _listView_onRefreshListener = new PullToRefreshListView.OnRefreshListener() {
		@Override
		public void onRefresh() {
			getListAdapter().update(false);
			_loadingProgress.progressiveStart();
		}
	};
	
	private PullToRefreshListView.OnItemClickListener _listView_ItemClickListener = new PullToRefreshListView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> a, View v, int position, long id) {			
			_hidden_workorder_id = (TextView) v.findViewById(R.id.hidden_workorder_id);
			Long _workorder_id = Long.parseLong(_hidden_workorder_id.getText().toString());
						
			Intent intent = new Intent(v.getContext(), WorkorderActivity.class);			
			intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, _workorder_id);
			v.getContext().startActivity(intent);
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

	@Override
	public void onRefresh() {
		getListAdapter().update(false);
		_loadingProgress.progressiveStart();
	}

}
