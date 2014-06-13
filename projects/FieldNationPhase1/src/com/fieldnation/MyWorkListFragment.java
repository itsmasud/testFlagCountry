package com.fieldnation;

import com.fieldnation.R;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyWorkListFragment extends Fragment {
	public static final String TYPE_REQUESTED = "getRequested";
	public static final String TYPE_AVAILABLE = "getAvailable";
	public static final String TYPE_PENDING_APPROVAL = "getPendingApproval";
	public static final String TYPE_ASSIGNED = "getAssigned";
	public static final String TYPE_COMPLETED = "getCompleted";
	public static final String TYPE_CANCELED = "getCanceled";

	// UI
	private ListView _workordersListView;
	private ProgressBar _loadingProgressBar;
	private TextView _noDataTextView;

	// Data
	private WorkorderListAdapter _listAdapter;
	private String _displayType = TYPE_REQUESTED;
	private boolean _hasData = false;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public MyWorkListFragment setDisplayType(String displayType) {
		_displayType = displayType;
		return this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			_listAdapter = new WorkorderListAdapter(getActivity(), _displayType);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		_hasData = false;
		_listAdapter.registerDataSetObserver(_listAdapter_observer);
		_listAdapter.update();
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
		_workordersListView = (ListView) view.findViewById(R.id.workorders_listview);
		_workordersListView.setDivider(null);

		_workordersListView.setAdapter(_listAdapter);

	}

	@Override
	public void onStart() {
		super.onStart();
		updateUi();
	}

	@Override
	public void onPause() {
		super.onPause();

		if (_listAdapter != null) {
			_listAdapter.onStop();
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

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
	private void updateUi() {
		if (_loadingProgressBar != null) {
			if (_hasData) {
				_loadingProgressBar.setVisibility(View.GONE);
				if (_listAdapter != null) {
					if (_listAdapter.getCount() == 0) {
						_noDataTextView.setVisibility(View.VISIBLE);
					} else {
						_noDataTextView.setVisibility(View.GONE);
					}
				}
			} else {
				_loadingProgressBar.setVisibility(View.VISIBLE);
			}
		}
	}
}
