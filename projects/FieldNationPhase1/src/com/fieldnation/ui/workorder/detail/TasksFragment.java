package com.fieldnation.ui.workorder.detail;

import java.util.LinkedList;
import java.util.List;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.SignatureActivity;
import com.fieldnation.ui.workorder.WorkorderFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TasksFragment extends WorkorderFragment {
	private static final String TAG = "ui.workorder.detail.TasksFragment";

	// Activity result codes
	private static final int RESULT_CODE_BASE = 200;
	private static final int RESULT_CODE_SEND_EMAIL = RESULT_CODE_BASE + 1;
	private static final int RESULT_CODE_SIGNATURE = RESULT_CODE_BASE + 2;

	// Web request result codes
	private static final int WEB_CHANGED = 1;
	private static final int WEB_GET_TASKS = 2;

	// UI
	private ShipmentView _shipments;
	private TaskListView _taskList;
	private TimeLoggedView _timeLogged;
	private ClosingNotesView _closingNotes;
	private View[] _separators;
	private ClosingNotesDialog _closingDialog;

	// Data
	private GlobalState _gs;
	private Workorder _workorder;
	private WorkorderService _service;
	private List<Task> _tasks = null;

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

		_gs = (GlobalState) view.getContext().getApplicationContext();
		_gs.requestAuthentication(_authClient);

		_shipments = (ShipmentView) view.findViewById(R.id.shipment_view);
		_taskList = (TaskListView) view.findViewById(R.id.scope_view);
		_timeLogged = (TimeLoggedView) view.findViewById(R.id.timelogged_view);
		_timeLogged.setFragmentManager(getFragmentManager());
		_closingNotes = (ClosingNotesView) view.findViewById(R.id.closingnotes_view);

		_separators = new View[3];

		_separators[0] = view.findViewById(R.id.sep1);
		_separators[1] = view.findViewById(R.id.sep2);
		_separators[2] = view.findViewById(R.id.sep3);

		_closingDialog = new ClosingNotesDialog(view.getContext());

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
		requestData();
	}

	private void configureUi() {
		if (_workorder == null)
			return;

		if (_shipments != null)
			_shipments.setWorkorder(_workorder);

		if (_taskList != null) {
			_taskList.setTaskListViewListener(_taskListView_listener);
		}

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

	private void requestData() {
		if (_service == null)
			return;

		if (_workorder == null)
			return;

		_gs.startService(_service.getTasks(WEB_GET_TASKS, _workorder.getWorkorderId(), false));
	}

	/*-*************************************-*/
	/*-				UI Events				-*/
	/*-*************************************-*/
	private TaskListView.Listener _taskListView_listener = new TaskListView.Listener() {

		@Override
		public void onTaskClick(Task task) {
			switch (task.getTaskType()) {
			case CHECKIN:
				if (task.getCompleted())
					return;

				getActivity().startService(
						_service.checkin(WEB_CHANGED, _workorder.getWorkorderId(), System.currentTimeMillis()));
				break;
			case CHECKOUT:
				if (task.getCompleted())
					return;
				getActivity().startService(
						_service.checkout(WEB_CHANGED, _workorder.getWorkorderId(), System.currentTimeMillis()));
				break;
			case CLOSE_OUT_NOTES:
				if (task.getCompleted())
					return;
				_closingDialog.show(_workorder.getClosingNotes(), _closingNotes_onOk);
				break;
			case CONFIRM_ASSIGNMENT:
				if (task.getCompleted())
					return;
				// TODO get start time + duration from user
				// _service.confirmAssignment(WEB_CONFIRM_ASSIGNMENT,
				// _workorder.getWorkorderId(), startTimeMilliseconds,
				// endTimeMilliseconds)
				break;
			case CUSTOM_FIELD:
				if (task.getCompleted())
					return;
				// TODO, get custom field info, preset dialog
				break;
			case DOWNLOAD:
				if (task.getCompleted())
					return;
				// TODO, download file and display it
				break;
			case EMAIL: {
				String email = task.getEmailAddress();
				Intent intent = new Intent(Intent.ACTION_SENDTO);
				intent.setData(Uri.parse("mailto:" + email));
				startActivityForResult(intent, RESULT_CODE_SEND_EMAIL);

				// TODO, mark this task as complete
				break;
			}
			case PHONE:
				// TODO start up the phone app
				break;
			case SHIPMENT_TRACKING:
				// TODO send to shipment section
				break;
			case SIGNATURE: {
				// TODO bring up signature library
				Intent intent = new Intent(getActivity(), SignatureActivity.class);
				try {
					intent.putExtra(SignatureActivity.RESULT_KEY_ARRIVAL,
							_workorder.getCheckInOutInfo().getCheckInTime());
				} catch (Exception ex) {
				}
				try {
					intent.putExtra(SignatureActivity.RESULT_KEY_DEPARTURE,
							_workorder.getCheckInOutInfo().getCheckOutTime());
				} catch (Exception ex) {
				}
				try {
					intent.putExtra(SignatureActivity.RESULT_KEY_NAME, _workorder.getManagerName());
				} catch (Exception ex) {
				}
				startActivityForResult(intent, RESULT_CODE_SIGNATURE);
				break;
			}
			case UPLOAD_FILE:
				// TODO send to attachments tab
				break;
			case UPLOAD_PICTURE:
				// TODO send to attachments tab
				break;
			default:
				break;
			}
		}
	};

	private ClosingNotesDialog.Listener _closingNotes_onOk = new ClosingNotesDialog.Listener() {
		@Override
		public void onOk(String message) {
			getActivity().startService(_service.closingNotes(WEB_CHANGED, _workorder.getWorkorderId(), message));
		}
	};

	/*-*************************************-*/
	/*-				WEB Events				-*/
	/*-*************************************-*/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == RESULT_CODE_SIGNATURE && resultCode == Activity.RESULT_OK) {
			byte[] imagedata = data.getExtras().getByteArray(SignatureActivity.RESULT_KEY_BITMAP);

			Log.v(TAG, "BP");

			// TODO, do something with the bitmap.
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private AuthenticationClient _authClient = new AuthenticationClient() {
		@Override
		public void onAuthentication(String username, String authToken) {
			_service = new WorkorderService(getActivity(), username, authToken, _resultReceiver);
			requestData();
		}

		@Override
		public void onAuthenticationFailed(Exception ex) {
			_gs.requestAuthenticationDelayed(_authClient);
		}

		@Override
		public GlobalState getGlobalState() {
			return _gs;
		}
	};

	private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {

			if (resultCode == WEB_CHANGED) {
				_workorder.dispatchOnChange();
			}

			/*-			Tasks			-*/
			if (resultCode == WEB_GET_TASKS) {
				// TODO populate
				String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));
				Log.v(TAG, data);
				try {
					JsonArray array = new JsonArray(data);
					_tasks = new LinkedList<Task>();
					for (int i = 0; i < array.size(); i++) {
						try {
							_tasks.add(Task.fromJson(array.getJsonObject(i)));
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				_taskList.setTaskList(_tasks);
			}
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			if (_service != null) {
				_gs.invalidateAuthToken(_service.getAuthToken());
			}
			_gs.requestAuthenticationDelayed(_authClient);
		}
	};

}
