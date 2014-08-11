package com.fieldnation.ui.payment;

import com.fieldnation.R;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.json.JsonObject;
import com.fieldnation.ui.DrawerActivity;
import com.fieldnation.ui.PagingListAdapter;

import eu.erikw.PullToRefreshListView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;
import android.os.Bundle;
import android.view.View;

public class PaymentListActivity extends DrawerActivity {
	private static final String TAG = "ui.payment.PaymentListActivity";

	// UI
	private PullToRefreshListView _listView;
	private SmoothProgressBar _loadingProgress;

	// Data
	private PaymentListAdapter _adapter;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemlist);

		_listView = (PullToRefreshListView) findViewById(R.id.items_listview);
		_listView.setOnRefreshListener(_listView_onRefreshListener);

		_loadingProgress = (SmoothProgressBar) findViewById(R.id.loading_progress);
		_loadingProgress.setSmoothProgressDrawableCallbacks(_loadingCallback);

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

	private PagingListAdapter.Listener<Payment> _adapter_listener = new PagingListAdapter.Listener<Payment>() {

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
			getAdapter().update(false);
			_loadingProgress.progressiveStart();
		}
	};

	@Override
	public void onRefresh() {
		getAdapter().update(false);
		_loadingProgress.progressiveStart();
	}

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	private PagingListAdapter<Payment> getAdapter() {
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
