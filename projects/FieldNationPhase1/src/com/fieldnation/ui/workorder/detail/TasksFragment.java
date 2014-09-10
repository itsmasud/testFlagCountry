package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.ui.workorder.WorkorderFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TasksFragment extends WorkorderFragment {
	private static final String TAG = "ui.workorder.detail.TasksFragment";

	// UI
	private ShipmentView _shipments;
	private ScopeOfWorkView _scope;
	private TimeLoggedView _timeLogged;

	// Data
	private Workorder _workorder;

	/*-*************************************-*/
	/*-				LifeCycle				-*/
	/*-*************************************-*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_workorder_tasks, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_shipments = (ShipmentView) view.findViewById(R.id.shipment_view);
		_scope = (ScopeOfWorkView) view.findViewById(R.id.scope_view);
		_timeLogged = (TimeLoggedView) view.findViewById(R.id.timelogged_view);

		configureUi();
	}

	@Override
	public void update() {
		configureUi();
	}

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		configureUi();
	}

	private void configureUi() {
		if (_workorder == null)
			return;

		if (_shipments != null)
			_shipments.setWorkorder(_workorder);

		if (_scope != null)
			_scope.setWorkorder(_workorder);

		if (_timeLogged != null)
			_timeLogged.setWorkorder(_workorder);

		WorkorderStatus status = _workorder.getStatus().getWorkorderStatus();
		if (status.ordinal() <= WorkorderStatus.ASSIGNED.ordinal()) {
			_shipments.setVisibility(View.GONE);
			_timeLogged.setVisibility(View.GONE);
		} else {
			_shipments.setVisibility(View.VISIBLE);
			_timeLogged.setVisibility(View.VISIBLE);
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

}
