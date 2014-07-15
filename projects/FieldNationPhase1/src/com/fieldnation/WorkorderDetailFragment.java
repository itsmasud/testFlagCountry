package com.fieldnation;

import com.fieldnation.data.workorder.Workorder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.ScrollingTabContainerView.TabView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class WorkorderDetailFragment extends WorkorderFragment {
	private static final String TAG = "WorkorderDetailFragment";

	// UI
	private WorkorderDetailSumView _sumView;
	private WorkorderDetailLocationView _locView;
	private WorkorderDetailScheduleView _scheduleView;

	// Data
	private Workorder _workorder;

	/*-*************************************-*/
	/*-				LifeCycle				-*/
	/*-*************************************-*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Method Stub: onCreate()
		Log.v(TAG, "Method Stub: onCreate()");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_workorder_detail, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_sumView = (WorkorderDetailSumView) view.findViewById(R.id.detailsum_view);
		_locView = (WorkorderDetailLocationView) view.findViewById(R.id.location_view);
		_scheduleView = (WorkorderDetailScheduleView) view.findViewById(R.id.schedule_view);

		if (_workorder != null) {
			setWorkorder(_workorder);
		}
	}

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/

	public void update() {
	}

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;

		if (_sumView != null) {
			_sumView.setWorkorder(_workorder);
		}
		if (_locView != null) {
			_locView.setWorkorder(_workorder);
		}
		if (_scheduleView != null) {
			_scheduleView.setWorkorder(_workorder);
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

}
