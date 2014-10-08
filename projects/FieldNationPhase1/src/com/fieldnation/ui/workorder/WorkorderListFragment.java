package com.fieldnation.ui.workorder;

import java.util.LinkedList;
import java.util.List;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.State;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WorkorderListFragment extends Fragment {
	private static final String TAG = "ui.workorder.WorkorderListFragment";

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
		_listView.setStateListener(_listview_onPullListener);

		_loadingBar = (SmoothProgressBar) view.findViewById(R.id.loading_progress);
		_loadingBar.setSmoothProgressDrawableCallbacks(_progressCallback);
		_loadingBar.setMax(100);

		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("_displayView")) {
				Log.v(TAG, "Restoring state");
				_displayView = WorkorderDataSelector.fromName(savedInstanceState.getString("_displayView"));
			}

			if (savedInstanceState.containsKey("WORKORDERS")) {
				Parcelable[] works = savedInstanceState.getParcelableArray("WORKORDERS");

				if (works != null && works.length > 0) {
					List<Workorder> work = new LinkedList<Workorder>();
					for (int i = 0; i < works.length; i++) {
						work.add((Workorder) works[i]);
					}
					try {
						_adapter = new WorkorderListAdapter(this.getActivity(), _displayView, work);
						_adapter.setLoadingListener(_workorderAdapter_listener);
						_loadingBar.setVisibility(View.GONE);
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
			}
		}
		Log.v(TAG, "Display Type: " + _displayView.getCall());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("_displayView", _displayView.name());

		if (_adapter != null) {
			List<Workorder> work = _adapter.getObjects();
			if (work != null && work.size() > 0) {
				Workorder[] works = new Workorder[work.size()];
				for (int i = 0; i < work.size(); i++) {
					works[i] = work.get(i);
				}
				outState.putParcelableArray("WORKORDERS", works);
			}
		}

		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		Log.v(TAG, "onStart");
		if (_listView != null && getAdapter() != null && _listView.getAdapter() == null) {
			_listView.setAdapter(getAdapter());
		}
		super.onStart();
	}

	@Override
	public void onPause() {
		Log.v(TAG, "onPause()");
		if (getAdapter() != null) {
			getAdapter().onPause();
		}
		super.onPause();
	}

	// @Override
	// public void onStop() {
	// Log.v(TAG, "Method Stub: onStop()");
	// super.onStop();
	// if (getAdapter() != null) {
	// getAdapter().onStop();
	// _adapter = null;
	// }
	// }

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
			// _adapter.update(false);
			// _loadingBar.setIndeterminate(true);
			// _loadingBar.progressiveStart();
		}
	};

	private PullToRefreshListView.StateListener _listview_onPullListener = new PullToRefreshListView.StateListener() {
		@Override
		public void onPull(int pullPercent) {
			if (_listView.getState() == PullToRefreshListView.State.PULL_TO_REFRESH) {
				float sep = 4f - 4 * Math.abs(pullPercent) / 100f;
				if (sep < 0)
					sep = 0f;
				_loadingBar.setSmoothProgressDrawableSpeed(sep);
			}
		}

		@Override
		public void onStopPull() {
			_loadingBar.setSmoothProgressDrawableSpeed(2f);
			_loadingBar.setSmoothProgressDrawableReversed(true);
			_loadingBar.setSmoothProgressDrawableSectionsCount(1);
			_loadingBar.progressiveStop();
			_loadingBar.setVisibility(View.GONE);
		}

		@Override
		public void onStateChange(State state) {
			if (state == State.RELEASE_TO_REFRESH) {
				if (getAdapter() != null)
					getAdapter().update(false);
				_loadingBar.progressiveStart();
			}
		}

		@Override
		public void onStartPull() {
			_loadingBar.setSmoothProgressDrawableSectionsCount(1);
			_loadingBar.setSmoothProgressDrawableReversed(true);
			_loadingBar.progressiveStart();
		}

	};

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	public void update() {
		if (getAdapter() != null)
			getAdapter().update(false);
		_listView.setRefreshing();
	}

	public void hiding() {
		if (getAdapter() != null)
			getAdapter().onStop();
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
