package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.WorkLogDialog;

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
		// TODO need to do something
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
