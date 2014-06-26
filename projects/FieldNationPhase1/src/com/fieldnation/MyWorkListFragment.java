package com.fieldnation;

import com.fieldnation.R;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyWorkListFragment extends Fragment {
	private static final String TAG = "MyWorkListFragment";

	// UI
	private ListViewEx _workordersListView;

	// Data
	private WorkorderListAdapter _listAdapter;
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

		Log.v(TAG,
				"onCreate: " + MyWorkListFragment.this.toString() + "/" + _displayView.getCall());

		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("_displayView")) {
				Log.v(TAG, "Restoring state");
				_displayView = WorkorderDataSelector.fromName(savedInstanceState.getString("_displayView"));
			}
		}
		Log.v(TAG, "Display Type: " + _displayView.getCall());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_mywork_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_workordersListView = (ListViewEx) view.findViewById(R.id.workorders_listview);
		_workordersListView.setDivider(null);
		_workordersListView.setOnRefreshListener(_listView_onRefreshListener);
		_workordersListView.setAdapter(getListAdapter());
	}

	@Override
	public void onStart() {
		super.onStart();
		// update();
	}

	@Override
	public void onPause() {
		super.onPause();

		WorkorderListAdapter adapter = getListAdapter();
		if (adapter != null) {
			Log.v(TAG, "onPause");
			adapter.onStop();
			adapter = null;
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
	private WorkorderListAdapter.Listener _workorderAdapter_listener = new WorkorderListAdapter.Listener() {

		@Override
		public void onLoading() {
			// TODO Method Stub: onLoading()
			Log.v(TAG, "Method Stub: onLoading()");
		}

		@Override
		public void onLoadComplete() {
			_workordersListView.onRefreshComplete();
		}
	};

	private ListViewEx.OnRefreshListener _listView_onRefreshListener = new ListViewEx.OnRefreshListener() {

		@Override
		public void onRefresh() {
			_listAdapter.update(false);
		}
	};

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	public void update() {
		// TODO method stub update()
		Log.v(TAG, "update() stub");
	}

	private WorkorderListAdapter getListAdapter() {
		if (this.getActivity() == null)
			return null;
		try {
			if (_listAdapter == null) {
				_listAdapter = new WorkorderListAdapter(this.getActivity(),
						_displayView);
				_listAdapter.setLoadingListener(_workorderAdapter_listener);
			}

			if (!_listAdapter.isViable()) {
				_listAdapter = new WorkorderListAdapter(this.getActivity(),
						_displayView);
				_listAdapter.setLoadingListener(_workorderAdapter_listener);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		return _listAdapter;
	}
}
