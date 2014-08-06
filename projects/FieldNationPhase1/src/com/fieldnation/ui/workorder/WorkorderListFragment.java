package com.fieldnation.ui.workorder;

import com.cocosw.undobar.UndoBarController;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;

import eu.erikw.PullToRefreshListView;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class WorkorderListFragment extends Fragment {
	private static final String TAG = "ui.workorder.MyWorkListFragment";

	// UI
	private PullToRefreshListView _listView;

	// Data
	private WorkorderListAdapter _adapter;
	private WorkorderDataSelector _displayView = WorkorderDataSelector.AVAILABLE;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public WorkorderListFragment setDisplayType(WorkorderDataSelector displayView) {
		_displayView = displayView;
		return this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.v(TAG, "onCreate: " + WorkorderListFragment.this.toString() + "/" + _displayView.getCall());

		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("_displayView")) {
				Log.v(TAG, "Restoring state");
				_displayView = WorkorderDataSelector.fromName(savedInstanceState.getString("_displayView"));
			}
		}
		Log.v(TAG, "Display Type: " + _displayView.getCall());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_mywork_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_listView = (PullToRefreshListView) view.findViewById(R.id.workorders_listview);
		_listView.setDivider(null);
		_listView.setOnRefreshListener(_listView_onRefreshListener);
	}

	@Override
	public void onStart() {
		Log.v(TAG, "onStart");
		if (_listView != null) {
			_listView.setAdapter(getAdapter());
		}
		super.onStart();
	}

	@Override
	public void onPause() {
		Log.v(TAG, "onPause()");
		if (_adapter != null) {
			_adapter.onPause();
		}
		super.onPause();
	}

	@Override
	public void onStop() {
		Log.v(TAG, "Method Stub: onStop()");
		super.onStop();
		if (_adapter != null) {
			_adapter.onStop();
			_adapter = null;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("_displayView", _displayView.name());
		super.onSaveInstanceState(outState);
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

	private WorkorderListAdapter.Listener<Workorder> _workorderAdapter_listener = new WorkorderListAdapter.Listener<Workorder>() {

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
			_adapter.update(false);
		}
	};

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	public void update() {
		getAdapter().update(false);
		_listView.setRefreshing();
	}

	public void hiding() {
		_adapter.onStop();
	}

	private WorkorderListAdapter getAdapter() {
		if (this.getActivity() == null)
			return null;
		try {
			if (_adapter == null) {
				_adapter = new WorkorderListAdapter(this.getActivity(), _displayView);
				_adapter.setLoadingListener(_workorderAdapter_listener);
			}

			if (!_adapter.isViable()) {
				_adapter = new WorkorderListAdapter(this.getActivity(), _displayView);
				_adapter.setLoadingListener(_workorderAdapter_listener);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		return _adapter;
	}
}
