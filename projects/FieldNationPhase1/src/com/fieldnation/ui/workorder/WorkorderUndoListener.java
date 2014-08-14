package com.fieldnation.ui.workorder;

import java.util.LinkedList;
import java.util.List;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import com.cocosw.undobar.UndoBarController.AdvancedUndoListener;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.CancelCategory;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;

public class WorkorderUndoListener implements AdvancedUndoListener {
	private static final String TAG = "ui.workorder.WorkorderSummaryAdvancedUndoListener";
	private WorkorderService _service;
	private List<Workorder> _workorders;
	private Context _context;
	private Listener _listener;
	private List<Workorder> _success = new LinkedList<Workorder>();
	private List<Workorder> _failed = new LinkedList<Workorder>();

	public WorkorderUndoListener(List<Workorder> workorders, Context context, String username, String authToken,
			Listener listener) {
		_workorders = workorders;
		_service = new WorkorderService(context, username, authToken, _resultReciever);
		_context = context.getApplicationContext();
		_listener = listener;
	}

	@Override
	public void onUndo(Parcelable token) {
		if (_listener != null) {
			_listener.onUndo();
		}
	}

	@Override
	public void onHide(Parcelable token) {
		for (int i = 0; i < _workorders.size(); i++) {
			Workorder workorder = _workorders.get(i);

			switch (workorder.getNotInterestedAction()) {
			case Workorder.NOT_INTERESTED_ACTION_DECLINE:
				_context.startService(_service.decline(i, workorder.getWorkorderId()));

				break;
			case Workorder.NOT_INTERESTED_ACTION_WITHDRAW_REQUEST:
				_context.startService(_service.withdrawRequest(i, workorder.getWorkorderId()));

				break;
			case Workorder.NOT_INTERESTED_ACTION_CANCEL_ASSIGNMENT:
				// TODO, get reason input from user
				_context.startService(_service.cancelAssignment(i, workorder.getWorkorderId(), CancelCategory.OTHER,
						"UI Not implemented"));
				break;
			}
		}
	}

	@Override
	public void onClear() {
		for (int i = 0; i < _workorders.size(); i++) {
			Workorder workorder = _workorders.get(i);

			switch (workorder.getNotInterestedAction()) {
			case Workorder.NOT_INTERESTED_ACTION_DECLINE:
				_context.startService(_service.decline(i, workorder.getWorkorderId()));

				break;
			case Workorder.NOT_INTERESTED_ACTION_WITHDRAW_REQUEST:
				_context.startService(_service.withdrawRequest(i, workorder.getWorkorderId()));

				break;
			case Workorder.NOT_INTERESTED_ACTION_CANCEL_ASSIGNMENT:
				// TODO, get reason input from user
				_context.startService(_service.cancelAssignment(i, workorder.getWorkorderId(), CancelCategory.OTHER,
						"UI Not implemented"));
				break;
			}
		}
	}

	private WebServiceResultReceiver _resultReciever = new WebServiceResultReceiver(new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			_success.add(_workorders.get(resultCode));

			if (_success.size() + _failed.size() == _workorders.size()) {
				if (_listener != null) {
					_listener.onComplete(_success, _failed);
				}
			}
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			_failed.add(_workorders.get(resultCode));

			if (_success.size() + _failed.size() == _workorders.size()) {
				if (_listener != null) {
					_listener.onComplete(_success, _failed);
				}
			}
		}
	};

	public interface Listener {
		public void onComplete(List<Workorder> success, List<Workorder> failed);

		public void onUndo();
	}
}