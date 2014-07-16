package com.fieldnation.ui.workorder;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.cocosw.undobar.UndoBarController.AdvancedUndoListener;
import com.fieldnation.R;
import com.fieldnation.R.string;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.workorder.WorkorderSummaryView.Listener;

class WorkorderSummaryAdvancedUndoListener implements AdvancedUndoListener {
	private static final String TAG = "WorkorderSummaryAdvancedUndoListener";
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
	}

	private WebServiceResultReceiver _resultReciever = new WebServiceResultReceiver(new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			if (_listener != null)
				_listener.notifyDataSetChanged();
			Toast.makeText(_context, R.string.success, Toast.LENGTH_LONG).show();

		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			Log.v(TAG, errorType);
			Log.v(TAG, resultData.toString());
			Toast.makeText(_context, R.string.try_again, Toast.LENGTH_LONG).show();
		}
	};
}