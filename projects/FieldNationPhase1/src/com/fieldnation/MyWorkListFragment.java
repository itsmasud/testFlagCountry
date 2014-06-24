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
	private ProgressBar _loadingProgressBar;
	private TextView _noDataTextView;
	private ImageButton _refreshButton;
	private ProgressBar _overscrollBarLeft;
	private ProgressBar _overscrollBarRight;

	// Data
	private WorkorderListAdapter _listAdapter;
	private DataSelector _displayView = DataSelector.AVAILABLE;
	private boolean _hasData = false;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public MyWorkListFragment setDisplayType(DataSelector displayView) {
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
				_displayView = DataSelector.fromName(savedInstanceState.getString("_displayView"));
			}
			if (savedInstanceState.containsKey("_hasData")) {
				Log.v(TAG, "Restoring state");
				_hasData = savedInstanceState.getBoolean("_hasData");
			}
		}

		Log.v(TAG, "Display Type: " + _displayView.getCall());
		_hasData = false;
		// getListAdapter().update(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_mywork_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_noDataTextView = (TextView) view.findViewById(R.id.nodata_textview);

		_loadingProgressBar = (ProgressBar) view.findViewById(R.id.loading_progressbar);
		_workordersListView = (ListViewEx) view.findViewById(R.id.workorders_listview);
		_workordersListView.setOnOverScrollListener(_listView_onOverScroll);
		_workordersListView.setDivider(null);

		_workordersListView.setAdapter(getListAdapter());

		_refreshButton = (ImageButton) view.findViewById(R.id.refresh_button);
		_refreshButton.setOnClickListener(_refresh_onClick);

		_overscrollBarLeft = (ProgressBar) view.findViewById(R.id.overscrollleft_progressBar);
		_overscrollBarRight = (ProgressBar) view.findViewById(R.id.overscrollright_progressBar);

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
			adapter.onStop();
			adapter = null;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("_displayView", _displayView.name());
		outState.putBoolean("_hasData", _hasData);
		super.onSaveInstanceState(outState);
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private int _lastOffset = 0;
	private ListViewEx.OnOverscrollListener _listView_onOverScroll = new ListViewEx.OnOverscrollListener() {
		@Override
		public void onOverScroll(int offset) {
			offset = Math.abs(offset);
			_overscrollBarLeft.setProgress(offset);
			_overscrollBarRight.setProgress(offset);

			if (_lastOffset >= 200 && offset == 0) {
				update();
			}
			_lastOffset = offset;
		}
	};

	private View.OnClickListener _refresh_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.v(TAG,
					"_refresh_onClick: " + MyWorkListFragment.this.toString() + "/" + _displayView.getCall());
			update();
		}
	};

	private DataSetObserver _listAdapter_observer = new DataSetObserver() {
		@Override
		public void onChanged() {
			_hasData = true;
			updateUi();
			super.onChanged();
		}
	};

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	public void update() {
		_hasData = false;
		updateUi();
		getListAdapter().update(false);
	}

	private void updateUi() {
		if (_loadingProgressBar != null) {
			if (_hasData) {
				_loadingProgressBar.setVisibility(View.GONE);
				WorkorderListAdapter adapter = getListAdapter();
				if (adapter != null) {
					if (adapter.getCount() == 0) {
						_noDataTextView.setVisibility(View.VISIBLE);
						_refreshButton.setVisibility(View.VISIBLE);
					} else {
						_noDataTextView.setVisibility(View.GONE);
						_refreshButton.setVisibility(View.GONE);
					}
				}
			} else {
				_noDataTextView.setVisibility(View.GONE);
				_refreshButton.setVisibility(View.GONE);
				_loadingProgressBar.setVisibility(View.VISIBLE);
			}
		}
	}

	private WorkorderListAdapter getListAdapter() {
		if (this.getActivity() == null)
			return null;
		try {
			if (_listAdapter == null) {
				_listAdapter = new WorkorderListAdapter(this.getActivity(),
						_displayView);
				_listAdapter.registerDataSetObserver(_listAdapter_observer);
			}

			if (!_listAdapter.isViable()) {
				_listAdapter = new WorkorderListAdapter(this.getActivity(),
						_displayView);
				_listAdapter.registerDataSetObserver(_listAdapter_observer);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		return _listAdapter;
	}
}
