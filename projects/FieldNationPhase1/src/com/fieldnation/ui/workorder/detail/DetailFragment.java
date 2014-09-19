package com.fieldnation.ui.workorder.detail;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.workorder.WorkorderFragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceResponse;

public class DetailFragment extends WorkorderFragment {
	private static final String TAG = "ui.workorder.detail.DetailFragment";

	// UI
	private SummaryView _sumView;
	private LocationView _locView;
	private ScheduleSummaryView _scheduleView;
	private PaymentView _payView;
	private TaskSumView _taskView;
	private ActionView _actionView;

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

		_sumView = (SummaryView) view.findViewById(R.id.summary_view);
		_taskView = (TaskSumView) view.findViewById(R.id.tasksum_view);
		_locView = (LocationView) view.findViewById(R.id.location_view);
		_scheduleView = (ScheduleSummaryView) view.findViewById(R.id.schedule_view);
		_payView = (PaymentView) view.findViewById(R.id.payment_view);
		_payView.showDetails(false);
		_actionView = (ActionView) view.findViewById(R.id.action_view);

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

		if (_taskView != null) {
			_taskView.setWorkorder(_workorder);
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

		if (_actionView != null) {
			_actionView.setWorkorder(_workorder);
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

}
