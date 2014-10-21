package com.fieldnation.ui.workorder.detail;

import java.util.Calendar;

import com.fieldnation.R;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.dialog.WorkLogDialog;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
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
	private FragmentManager _fm;

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

		_dialog = new WorkLogDialog(getContext());
	}

	public void setFragmentManager(FragmentManager fm) {
		_fm = fm;
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
			
			// hook up edit button
			v.setWorkorder(_workorder);
			v.setFragmentManager(_fm);
		}		
	}

	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/

	private View.OnClickListener _addLog_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_dialog.show(_fm, "Add Worklog", false, false, _worklogdialog_listener);
		}
	};

	private WorkLogDialog.Listener _worklogdialog_listener = new WorkLogDialog.Listener() {

		@Override
		public void onOk(Calendar start, Calendar end, int deviceCount, boolean isOnSiteTime) {
			// TODO Method Stub: onOk()
			Log.v(TAG, "Method Stub: onOk()");
		}
		
		@Override
		public void onOk(Calendar start, Calendar end, int deviceCount, boolean isOnSiteTime, Integer workorderHoursId) {
			// TODO Method Stub: onOk()
			Log.v(TAG, "Method Stub: onOk()");
		}

		@Override
		public void onCancel() {
			// TODO Method Stub: onCancel()
			Log.v(TAG, "Method Stub: onCancel()");
		}
	};
}
