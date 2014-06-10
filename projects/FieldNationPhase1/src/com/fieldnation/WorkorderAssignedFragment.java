package com.fieldnation;

import com.fieldnation.R;
import com.fieldnation.service.rpc.WorkorderRpc;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

public class WorkorderAssignedFragment extends Fragment {
	private static final int RPC_GET_ASSIGNED = 1;
	// UI
	private ListView _workordersListView;
	private ProgressBar _loadingProgressBar;

	// Data
	private GlobalState _gs;
	private WorkorderAssignedAdapter _listAdapter;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.fragment_wo_assigned, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_gs = (GlobalState) getActivity().getApplicationContext();

		_loadingProgressBar = (ProgressBar) view
				.findViewById(R.id.loading_progressbar);
		_workordersListView = (ListView) view
				.findViewById(R.id.workorders_listview);
		_workordersListView.setDivider(null);

	}

	@Override
	public void onStart() {
		super.onStart();

		_listAdapter = new WorkorderAssignedAdapter(getActivity());

		_workordersListView.setAdapter(_listAdapter);
		_listAdapter.registerDataSetObserver(_listAdapter_observer);
		_listAdapter.update();
		_loadingProgressBar.setVisibility(View.VISIBLE);
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private DataSetObserver _listAdapter_observer = new DataSetObserver() {
		@Override
		public void onChanged() {
			_loadingProgressBar.setVisibility(View.GONE);
			super.onChanged();
		}
	};
	private ResultReceiver _rpcReceiver = new ResultReceiver(new Handler()) {
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			System.out.println("Method Stub: onCreateView()");
		};
	};

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	private void getWorkorders() {
		WorkorderRpc.getAssigned(this.getActivity(), _rpcReceiver,
				RPC_GET_ASSIGNED, _gs.accessToken, 0);
	}
}
