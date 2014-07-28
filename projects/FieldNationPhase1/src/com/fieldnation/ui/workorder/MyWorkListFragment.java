package com.fieldnation.ui.workorder;

import com.cocosw.undobar.UndoBarController;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.ui.ListViewEx;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyWorkListFragment extends Fragment {
	private static final String TAG = "ui.workorder.MyWorkListFragment";

	// UI
	private ListViewEx _listView;

	// Data
	private WorkorderListAdapter _adapter;
	private WorkorderDataSelector _displayView = WorkorderDataSelector.AVAILABLE;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public MyWorkListFragment setDisplayType(WorkorderDataSelector displayView) {
		_displayView = displayView;
		return this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.v(TAG, "onCreate: " + MyWorkListFragment.this.toString() + "/" + _displayView.getCall());

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

		_listView = (ListViewEx) view.findViewById(R.id.workorders_listview);
		_listView.setDivider(null);
		_listView.setOnRefreshListener(_listView_onRefreshListener);
	}

	@Override
	public void onStart() {
		super.onStart();
		// update();
	}

	@Override
	public void onResume() {
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

	private WorkorderListAdapter.Listener<Workorder> _workorderAdapter_listener = new WorkorderListAdapter.Listener<Workorder>() {

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
			_adapter.update(false);
		}
	};

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	public void update() {
		// TODO method stub update()
		Log.v(TAG, "update() stub");
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
