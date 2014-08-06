package com.fieldnation.ui.workorder;

import java.util.List;
import java.util.Set;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.cocosw.undobar.UndoBarController.AdvancedUndoListener;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.CancelCategory;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.workorder.WorkorderCardView.Listener;

public class WorkorderSummaryAdvancedUndoListener implements AdvancedUndoListener {
	private static final String TAG = "ui.workorder.WorkorderSummaryAdvancedUndoListener";
	private WorkorderService _service;
	private List<Workorder> _workorders;
	private Context _context;

	public WorkorderSummaryAdvancedUndoListener(List<Workorder> workorders, Context context, String username,
			String authToken) {
		_workorders = workorders;
		_service = new WorkorderService(context, username, authToken, _resultReciever);
		_context = context.getApplicationContext();
	}

	@Override
	public void onUndo(Parcelable token) {
	}

	@Override
	public void onHide(Parcelable token) {
		for (int i = 0; i < _workorders.size(); i++) {
			Workorder workorder = _workorders.get(i);

			switch (workorder.getNotInterestedAction()) {
			case Workorder.NOT_INTERESTED_ACTION_DECLINE:
				_context.startService(_service.decline(Workorder.NOT_INTERESTED_ACTION_DECLINE,
						workorder.getWorkorderId()));

				break;
			case Workorder.NOT_INTERESTED_ACTION_WITHDRAW_REQUEST:
				_context.startService(_service.withdrawRequest(Workorder.NOT_INTERESTED_ACTION_WITHDRAW_REQUEST,
						workorder.getWorkorderId()));

				break;
			case Workorder.NOT_INTERESTED_ACTION_CANCEL_ASSIGNMENT:
				// TODO, get reason input from user
				_context.startService(_service.cancelAssignment(Workorder.NOT_INTERESTED_ACTION_CANCEL_ASSIGNMENT,
						workorder.getWorkorderId(), CancelCategory.OTHER, "UI Not implemented"));
				break;
			}
		}
	}

	@Override
	public void onClear() {
	}

	private WebServiceResultReceiver _resultReciever = new WebServiceResultReceiver(new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
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