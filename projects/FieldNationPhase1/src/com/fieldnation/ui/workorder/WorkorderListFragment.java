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

		((ActionBarActivity) getActivity()).startSupportActionMode(_actionMode_Callback);
	}

	@Override
	public void onStart() {
		super.onStart();
		// update();
	}

	@Override
	public void onResume() {
		Log.v(TAG, "onResume");
		_listView.setAdapter(getAdapter());
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
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
	private ActionMode.Callback _actionMode_Callback = new ActionMode.Callback() {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.workorder_card, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
			// TODO Method Stub: onPrepareActionMode()
			Log.v(TAG, "Method Stub: onPrepareActionMode()");
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode arg0) {
			// TODO Method Stub: onDestroyActionMode()
			Log.v(TAG, "Method Stub: onDestroyActionMode()");

		}

		@Override
		public boolean onActionItemClicked(ActionMode arg0, MenuItem arg1) {
			// TODO Method Stub: onActionItemClicked()
			Log.v(TAG, "Method Stub: onActionItemClicked()");
			return false;
		}
	};

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
