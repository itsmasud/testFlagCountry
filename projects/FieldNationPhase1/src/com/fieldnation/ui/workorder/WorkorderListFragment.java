package com.fieldnation.ui.workorder;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;

import eu.erikw.PullToRefreshListView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WorkorderListFragment extends Fragment {
	private static final String TAG = "ui.workorder.MyWorkListFragment";

	// UI
	private PullToRefreshListView _listView;
	private SmoothProgressBar _loadingBar;

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
		return inflater.inflate(R.layout.fragment_workorder_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_listView = (PullToRefreshListView) view.findViewById(R.id.workorders_listview);
		_listView.setDivider(null);
		_listView.setOnRefreshListener(_listView_onRefreshListener);

		_loadingBar = (SmoothProgressBar) view.findViewById(R.id.loading_progress);
		// _loadingBar.setSmoothProgressDrawableInterpolator(new
		// AccelerateDecelerateInterpolator());
		// _loadingBar.setSmoothProgressDrawableColors(getActivity().getResources().getIntArray(R.array.loading_bar_colors));
		// _loadingBar.setSmoothProgressDrawableMirrorMode(true);
		// _loadingBar.setSmoothProgressDrawableReversed(true);
		// _loadingBar.setSmoothProgressDrawableSeparatorLength(0);
		// _loadingBar.setSmoothProgressDrawableSpeed(2.0F);
		// _loadingBar.setSmoothProgressDrawableProgressiveStartSpeed(2.0F);
		// _loadingBar.setSmoothProgressDrawableProgressiveStopSpeed(2.0F);
		// _loadingBar.setSmoothProgressDrawableStrokeWidth(8F);
		// _loadingBar.setSmoothProgressDrawableSectionsCount(1);
		_loadingBar.setSmoothProgressDrawableCallbacks(_progressCallback);
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
	private SmoothProgressDrawable.Callbacks _progressCallback = new SmoothProgressDrawable.Callbacks() {

		@Override
		public void onStop() {
			_loadingBar.setVisibility(View.GONE);
		}

		@Override
		public void onStart() {
			_loadingBar.setVisibility(View.VISIBLE);
		}

	};

	private WorkorderListAdapter.Listener<Workorder> _workorderAdapter_listener = new WorkorderListAdapter.Listener<Workorder>() {

		@Override
		public void onLoading() {
			_listView.setRefreshing();
			_loadingBar.progressiveStart();
		}

		@Override
		public void onLoadComplete() {
			_listView.onRefreshComplete();
			_loadingBar.progressiveStop();
		}
	};

	private PullToRefreshListView.OnRefreshListener _listView_onRefreshListener = new PullToRefreshListView.OnRefreshListener() {
		@Override
		public void onRefresh() {
			_adapter.update(false);
			_loadingBar.progressiveStart();
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
