package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.WorkorderFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DetailFragment extends WorkorderFragment {
	private static final String TAG = "ui.workorder.detail.DetailFragment";

	// UI
	private SummaryView _sumView;
	private LocationView _locView;
	private ScheduleView _scheduleView;
	private PaymentView _payView;
	private ClosingNotesView _closingNotes;
	private ExpectedPaymentView _expectedPaymentView;

	// Data
	private Workorder _workorder;

	/*-*************************************-*/
	/*-				LifeCycle				-*/
	/*-*************************************-*/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_workorder_detail, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_sumView = (SummaryView) view.findViewById(R.id.detailsum_view);
		_locView = (LocationView) view.findViewById(R.id.location_view);
		_scheduleView = (ScheduleView) view.findViewById(R.id.schedule_view);
		_payView = (PaymentView) view.findViewById(R.id.payment_view);
		_closingNotes = (ClosingNotesView) view.findViewById(R.id.closingnotes_view);
		_expectedPaymentView = (ExpectedPaymentView) view.findViewById(R.id.expected_view);

		_scheduleView.setFragmentManager(getFragmentManager());

		if (_workorder != null) {
			setWorkorder(_workorder);
		}
	}

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/

	@Override
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

		if (_payView != null) {
			_payView.setWorkorder(_workorder);
		}

		if (_closingNotes != null) {
			_closingNotes.setWorkorder(_workorder);
		}

		if (_expectedPaymentView != null) {
			_expectedPaymentView.setWorkorder(_workorder);
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

}
