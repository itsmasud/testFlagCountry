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
	private ClosingNotesView _closingNotes;
	private View[] _separators;

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
		_timeLogged.setFragmentManager(getFragmentManager());
		_closingNotes = (ClosingNotesView) view.findViewById(R.id.closingnotes_view);

		_separators = new View[3];

		_separators[0] = view.findViewById(R.id.sep1);
		_separators[1] = view.findViewById(R.id.sep2);
		_separators[2] = view.findViewById(R.id.sep3);

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

		if (_closingNotes != null)
			_closingNotes.setWorkorder(_workorder);

		if (_shipments != null && _timeLogged != null) {
			WorkorderStatus status = _workorder.getStatus().getWorkorderStatus();
			if (status.ordinal() < WorkorderStatus.ASSIGNED.ordinal()) {
				_timeLogged.setVisibility(View.GONE);
				_separators[0].setVisibility(View.GONE);
				_separators[1].setVisibility(View.GONE);
				_shipments.setVisibility(View.GONE);
				_separators[2].setVisibility(View.GONE);
				_closingNotes.setVisibility(View.GONE);
			} else {
				_shipments.setVisibility(View.VISIBLE);
				_separators[0].setVisibility(View.VISIBLE);
				_separators[1].setVisibility(View.VISIBLE);
				_timeLogged.setVisibility(View.VISIBLE);
				_separators[2].setVisibility(View.VISIBLE);
				_closingNotes.setVisibility(View.VISIBLE);
			}
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

}
