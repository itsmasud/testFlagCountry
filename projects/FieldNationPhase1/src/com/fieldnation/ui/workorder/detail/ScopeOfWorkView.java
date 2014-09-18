package com.fieldnation.ui.workorder.detail;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScopeOfWorkView extends RelativeLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.ScopeOfWorkView";

	private int WEB_GET_TASKS = 1;
	private int WEB_CHANGED = 2;

	// UI
	private TextView _preVisistTextView;
	private LinearLayout _preVisistList;
	private LinearLayout _onSiteLayout;
	private LinearLayout _onSiteList;
	private LinearLayout _postVisitLayout;
	private LinearLayout _postVisitList;
	private TextView _noDataTextView;
	private ClosingNotesDialog _closingDialog;

	// Data
	private GlobalState _gs;
	private WorkorderService _service;
	private Workorder _workorder;
	private List<Task> _tasks;

	public ScopeOfWorkView(Context context) {
		super(context);
		init();
	}

	public ScopeOfWorkView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ScopeOfWorkView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_wd_scope, this);

		if (isInEditMode())
			return;

		_gs = (GlobalState) getContext().getApplicationContext();
		_gs.requestAuthentication(_authClient);

		_preVisistTextView = (TextView) findViewById(R.id.previsit_textview);
		_preVisistList = (LinearLayout) findViewById(R.id.previsit_list);
		_onSiteLayout = (LinearLayout) findViewById(R.id.onsite_layout);
		_onSiteList = (LinearLayout) findViewById(R.id.onsite_list);
		_postVisitLayout = (LinearLayout) findViewById(R.id.postvisit_layout);
		_postVisitList = (LinearLayout) findViewById(R.id.postvisit_list);
		_noDataTextView = (TextView) findViewById(R.id.nodata_textview);

		_closingDialog = new ClosingNotesDialog(getContext());
	}

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;

		requestData();
	}

	private void requestData() {
		if (_service == null)
			return;

		if (_workorder == null)
			return;

		_gs.startService(_service.getTasks(WEB_GET_TASKS, _workorder.getWorkorderId(), false));
	}

	private void populateUi() {
		if (_tasks == null)
			return;

		if (_tasks.size() == 0) {
			_noDataTextView.setVisibility(View.VISIBLE);
			return;
		} else {
			_noDataTextView.setVisibility(View.GONE);
		}

		boolean nocategories = misc.isEmptyOrNull(_tasks.get(0).getStage()) || "any".equals(_tasks.get(0).getStage());

		_preVisistList.removeAllViews();
		_onSiteLayout.removeAllViews();
		_postVisitLayout.removeAllViews();
		if (nocategories) {
			_onSiteLayout.setVisibility(View.GONE);
			_postVisitLayout.setVisibility(View.GONE);

			for (int i = 0; i < _tasks.size(); i++) {
				Task task = _tasks.get(i);

				TaskRowView row = new TaskRowView(getContext());
				row.setTask(task);
				row.setOnTaskClickListener(_task_onClick);

				_preVisistList.addView(row);
			}
		} else {
			_preVisistTextView.setVisibility(View.VISIBLE);
			_onSiteLayout.setVisibility(View.GONE);
			_postVisitLayout.setVisibility(View.GONE);
			for (int i = 0; i < _tasks.size(); i++) {
				Task task = _tasks.get(i);

				TaskRowView row = new TaskRowView(getContext());
				row.setTask(task);
				row.setOnTaskClickListener(_task_onClick);
				if ("prep".equals(task.getStage())) {
					_preVisistList.addView(row);
				} else if ("onsite".equals(task.getStage())) {
					_onSiteList.addView(row);
				} else if ("post".equals(task.getStage())) {
					_postVisitList.addView(row);
				}
			}
		}
	}

	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/
	private TaskRowView.Listener _task_onClick = new TaskRowView.Listener() {

		@Override
		public void onTaskClick(Task task) {
			switch (task.getTaskType()) {
			case CHECKIN:
				getContext().startService(
						_service.checkin(WEB_CHANGED, _workorder.getWorkorderId(), System.currentTimeMillis()));
				break;
			case CHECKOUT:
				getContext().startService(
						_service.checkout(WEB_CHANGED, _workorder.getWorkorderId(), System.currentTimeMillis()));
				break;
			case CLOSE_OUT_NOTES:
				_closingDialog.show(_workorder.getClosingNotes(), _closingNotes_onOk);
				break;
			case CONFIRM_ASSIGNMENT:
				// TODO get start time + durration from user
				// _service.confirmAssignment(WEB_CONFIRM_ASSIGNMENT,
				// _workorder.getWorkorderId(), startTimeMilliseconds,
				// endTimeMilliseconds)
				break;
			case CUSTOM_FIELD:
				// TODO, get custom field info, preset dialog
				break;
			case DOWNLOAD:
				// TODO, download file and display it
				break;
			case EMAIL:
				// TODO get e-mail address and send to email client
				break;
			case PHONE:
				// TODO start up the phone app
				break;
			case SHPIMENT_TRACKING:
				// TODO send to shipment section
				break;
			case SIGNATURE:
				// TODO bring up signature library
				break;
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
			getContext().startService(_service.closingNotes(WEB_CHANGED, _workorder.getWorkorderId(), message));
		}
	};

	private AuthenticationClient _authClient = new AuthenticationClient() {
		@Override
		public void onAuthentication(String username, String authToken) {
			_service = new WorkorderService(getContext(), username, authToken, _resultReceiver);
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

			if (resultCode == WEB_GET_TASKS) {
				// TODO populate
				String data = new String(resultData.getByteArray(WorkorderService.KEY_RESPONSE_DATA));
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

				populateUi();
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
