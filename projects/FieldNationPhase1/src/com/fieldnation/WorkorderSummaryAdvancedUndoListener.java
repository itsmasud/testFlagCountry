package com.fieldnation;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;

import com.cocosw.undobar.UndoBarController.AdvancedUndoListener;
import com.fieldnation.WorkorderSummaryView.Listener;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;

class WorkorderSummaryAdvancedUndoListener implements AdvancedUndoListener {
	/**
	 * 
	 */
	private WorkorderService _service;
	private Listener _listener;
	private Workorder _workorder;
	private int _notInterestedAction;
	private Context _context;

	public WorkorderSummaryAdvancedUndoListener(Workorder workorder, Context context, String username,
			String authToken, Listener listener, int notInterestedAction) {
		_service = new WorkorderService(context, username, authToken, _resultReciever);
		_listener = listener;
		_workorder = workorder;
		_notInterestedAction = notInterestedAction;
		_context = context;
	}

	@Override
	public void onUndo(Parcelable token) {
		if (_listener != null) {
			_listener.cancelRemove(_workorder);
		}
	}

	@Override
	public void onHide(Parcelable token) {
		switch (_notInterestedAction) {
		case WorkorderSummaryView.NOT_INTERESTED_ACTION_DECLINE:
			_context.startService(_service.decline(WorkorderSummaryView.NOT_INTERESTED_ACTION_DECLINE,
					_workorder.getWorkorderId()));

			break;
		case WorkorderSummaryView.NOT_INTERESTED_ACTION_WITHDRAW_REQUEST:
			_context.startService(_service.withdrawRequest(WorkorderSummaryView.NOT_INTERESTED_ACTION_WITHDRAW_REQUEST,
					_workorder.getWorkorderId()));

			break;
		case WorkorderSummaryView.NOT_INTERESTED_ACTION_CANCEL_ASSIGNMENT:
			// TODO, get reason input from user
			_context.startService(_service.cancelAssignment(
					WorkorderSummaryView.NOT_INTERESTED_ACTION_CANCEL_ASSIGNMENT, _workorder.getWorkorderId(), 1,
					"Cause I said So"));
			break;
		}
	}

	@Override
	public void onClear() {
		// TODO Method Stub: onClear()
		Log.v(WorkorderSummaryView.TAG, "Method Stub: onClear()");

	}

	private WebServiceResultReceiver _resultReciever = new WebServiceResultReceiver(new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			// TODO Method Stub: onSuccess()
			Log.v(WorkorderSummaryView.TAG, "Method Stub: onSuccess()");

		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			// TODO Method Stub: onError()
			Log.v(WorkorderSummaryView.TAG, "Method Stub: onError()");

		}
	};
}