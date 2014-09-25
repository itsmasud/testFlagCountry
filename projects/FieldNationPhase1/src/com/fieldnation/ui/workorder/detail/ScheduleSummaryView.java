package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

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
