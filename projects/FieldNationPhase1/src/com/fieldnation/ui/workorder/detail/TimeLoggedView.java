package com.fieldnation.ui.workorder.detail;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.ui.WorkLogDialog;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TimeLoggedView extends RelativeLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.TimeLoggedView";

	// Ui
	private LinearLayout _logList;
	private TextView _totalTimeTextView;
	private LinearLayout _addLogLinearLayout;
	private TextView _noTimeTextView;
	private WorkLogDialog _dialog;

	// Data
	private Workorder _workorder;
	private GlobalState _gs;
	private WorkorderService _service;

	public TimeLoggedView(Context context) {
		super(context);
		init();
	}

	public TimeLoggedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TimeLoggedView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_wd_time_logged, this);

		if (isInEditMode())
			return;
		_logList = (LinearLayout) findViewById(R.id.log_list);
		_totalTimeTextView = (TextView) findViewById(R.id.totaltime_textview);
		_noTimeTextView = (TextView) findViewById(R.id.notime_textview);
		_addLogLinearLayout = (LinearLayout) findViewById(R.id.addlog_linearlayout);
		_addLogLinearLayout.setOnClickListener(_addLog_onClick);

	}

	public void setFragmentManager(FragmentManager _fm) {
		_dialog = new WorkLogDialog(getContext());
		_dialog.setFragmentManager(_fm);
	}

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;

		LoggedWork[] logs = _workorder.getLoggedWork();

		if (logs == null || logs.length == 0) {
			_noTimeTextView.setVisibility(View.VISIBLE);
			return;
		}
		_noTimeTextView.setVisibility(View.GONE);

		_logList.removeAllViews();
		for (int i = 0; i < logs.length; i++) {
			LoggedWork log = logs[i];
			ScheduleDetailView v = new ScheduleDetailView(getContext());
			v.setLoggedWork(log);
			_logList.addView(v);

			// TODO hook up edit button onclick
		}
	}

	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/

	private View.OnClickListener _addLog_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_dialog.show();
		}
	};
}
