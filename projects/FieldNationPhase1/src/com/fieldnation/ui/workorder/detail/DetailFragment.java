package com.fieldnation.ui.workorder.detail;

import java.text.ParseException;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.AdditionalExpense;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.dialog.ExpiresDialog;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.utils.ISO8601;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DetailFragment extends WorkorderFragment {
	private static final String TAG = "ui.workorder.detail.DetailFragment";

	private static final int RESULT_CHANGE = 5;

	// UI
	private SummaryView _sumView;
	private LocationView _locView;
	private ScheduleSummaryView _scheduleView;
	private PaymentView _payView;
	private TaskSumView _taskView;
	private ActionView _actionView;
	private ActionBarTopView _topBar;
	private ExpiresDialog _expiresDialog;

	// Data
	private Workorder _workorder;
	private GlobalState _gs;
	private WorkorderService _service;

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
		_gs = (GlobalState) getActivity().getApplicationContext();
		_gs.requestAuthentication(_authClient);

		_sumView = (SummaryView) view.findViewById(R.id.summary_view);

		_taskView = (TaskSumView) view.findViewById(R.id.tasksum_view);
		_taskView.setListener(_taskSum_listener);

		_locView = (LocationView) view.findViewById(R.id.location_view);
		_scheduleView = (ScheduleSummaryView) view.findViewById(R.id.schedule_view);

		_payView = (PaymentView) view.findViewById(R.id.payment_view);
		_payView.showDetails(true);
		_payView.setListener(_paymentView_listener);

		_actionView = (ActionView) view.findViewById(R.id.action_view);
		_actionView.setListener(_actionView_listener);

		_topBar = (ActionBarTopView) view.findViewById(R.id.actiontop_view);
		_topBar.setListener(_actionbartop_listener);

		_expiresDialog = new ExpiresDialog(getActivity());

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

		if (_topBar != null) {
			_topBar.setWorkorder(_workorder);
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

	private TaskSumView.Listener _taskSum_listener = new TaskSumView.Listener() {
		@Override
		public void onShowTasksTab() {
			// TODO Method Stub: onShowTasksTab()
			Log.v(TAG, "Method Stub: onShowTasksTab()");

		}
	};

	private ActionBarTopView.Listener _actionbartop_listener = new ActionBarTopView.Listener() {
		@Override
		public void onComplete() {
			getActivity().startService(_service.complete(RESULT_CHANGE, _workorder.getWorkorderId()));
		}

		@Override
		public void onCheckOut() {
			getActivity().startService(_service.checkout(RESULT_CHANGE, _workorder.getWorkorderId()));
		}

		@Override
		public void onCheckIn() {
			getActivity().startService(_service.checkin(RESULT_CHANGE, _workorder.getWorkorderId()));
		}

		@Override
		public void onAcknowledge() {
			getActivity().startService(_service.acknowledgeHold(RESULT_CHANGE, _workorder.getWorkorderId()));
		}
	};

	private PaymentView.Listener _paymentView_listener = new PaymentView.Listener() {

		@Override
		public void onDeleteExpense(Workorder workorder, AdditionalExpense expense) {
			getActivity().startService(
					_service.deleteExpense(RESULT_CHANGE, _workorder.getWorkorderId(), expense.getExpenseId()));
		}

		@Override
		public void onDeleteDiscount(Workorder workorder, int discountId) {
			getActivity().startService(_service.deleteDiscount(RESULT_CHANGE, _workorder.getWorkorderId(), discountId));
		}

		@Override
		public void onAddExpense(Workorder workorder, String description, double amount, ExpenseCategory category) {
			getActivity().startService(
					_service.addExpense(RESULT_CHANGE, workorder.getWorkorderId(), description, amount, category));
		}

		@Override
		public void onAddDiscount(Workorder workorder, Double amount, String description) {
			getActivity().startService(
					_service.addDiscount(RESULT_CHANGE, _workorder.getWorkorderId(), amount, description));
		}
	};

	private ActionView.Listener _actionView_listener = new ActionView.Listener() {

		@Override
		public void onRequest(Workorder workorder) {
			_expiresDialog.show(getFragmentManager(), _expiresDialog_listener);
		}

		@Override
		public void onNotInterested(Workorder workorder) {
			getActivity().startService(_service.decline(RESULT_CHANGE, _workorder.getWorkorderId()));
		}

		@Override
		public void onConfirmAssignment(Workorder workorder) {
			// TODO dialog to ask for start/end time
			getActivity().startService(_service.confirmAssignment(RESULT_CHANGE, workorder.getWorkorderId(), 0, 0));
		}

		@Override
		public void onComplete(Workorder workorder) {
			getActivity().startService(_service.complete(RESULT_CHANGE, workorder.getWorkorderId()));
		}
	};

	private ExpiresDialog.Listener _expiresDialog_listener = new ExpiresDialog.Listener() {
		@Override
		public void onOk(String dateTime) {
			try {
				long seconds;
				seconds = (ISO8601.toUtc(dateTime) - System.currentTimeMillis()) / 1000;
				getActivity().startService(_service.request(RESULT_CHANGE, _workorder.getWorkorderId(), seconds));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private AuthenticationClient _authClient = new AuthenticationClient() {

		@Override
		public void onAuthenticationFailed(Exception ex) {
			_gs.requestAuthenticationDelayed(_authClient);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			_service = new WorkorderService(getActivity(), username, authToken, _resultReceiver);
		}

		@Override
		public GlobalState getGlobalState() {
			return _gs;
		}
	};

	private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			if (resultCode == RESULT_CHANGE) {
				_workorder.dispatchOnChange();
			}
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			if (_service != null) {
				_gs.invalidateAuthToken(_service.getAuthToken());
			}
			_gs.requestAuthenticationDelayed(_authClient);
			// TODO, toast failure, put ui in wait mode
		}
	};
}
