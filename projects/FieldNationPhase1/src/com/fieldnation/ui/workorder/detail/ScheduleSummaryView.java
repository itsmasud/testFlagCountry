package com.fieldnation.ui.workorder.detail;

import java.util.Calendar;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.WorkLogDialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleSummaryView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.ScheduleSummaryView";

	private static final int WEB_SUBMIT_WORKLOG = 1;

	// UI
	private TextView _arriveTimeTextView;

	// Data
	private Workorder _workorder;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public ScheduleSummaryView(Context context) {
		super(context);
		init();
	}

	public ScheduleSummaryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_wd_schedule_summary, this);

		if (isInEditMode())
			return;

		_arriveTimeTextView = (TextView) findViewById(R.id.arrivetime_view);
	}

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		refresh();
	}

	private void refresh() {

	}

}
