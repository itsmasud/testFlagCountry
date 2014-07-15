package com.fieldnation;

import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WorkorderDetailScheduleView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "WorkorderDetailScheduleView";

	// UI

	// Data
	private Workorder _workorder;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public WorkorderDetailScheduleView(Context context) {
		this(context, null);
	}

	public WorkorderDetailScheduleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_workorder_detail_schedule, this);

		if (isInEditMode())
			return;

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
