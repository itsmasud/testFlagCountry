package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.WorkorderFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TasksFragment extends WorkorderFragment {
	private static final String TAG = "ui.workorder.detail.TasksFragment";

	// UI
	private PrereqView _prereqs;
	private ShipmentView _shipments;
	private ScopeOfWorkView _scope;

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

		_prereqs = (PrereqView) view.findViewById(R.id.prereq_view);
		_shipments = (ShipmentView) view.findViewById(R.id.shipment_view);
		_scope = (ScopeOfWorkView) view.findViewById(R.id.scope_view);

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

		if (_prereqs == null || _shipments == null)
			return;

		if (_prereqs != null)
			_prereqs.setWorkorder(_workorder);

		if (_shipments != null)
			_shipments.setWorkorder(_workorder);

		if (_scope != null)
			_scope.setWorkorder(_workorder);
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

}
