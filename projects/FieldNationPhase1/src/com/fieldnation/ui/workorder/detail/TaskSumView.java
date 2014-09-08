package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class TaskSumView extends RelativeLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.TaskSumView";

	public TaskSumView(Context context) {
		super(context);
		init();
	}

	public TaskSumView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TaskSumView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_wd_task_sum, this);

		if (isInEditMode())
			return;
	}

	@Override
	public void setWorkorder(Workorder workorder) {
		// TODO Method Stub: setWorkorder()
		Log.v(TAG, "Method Stub: setWorkorder()");

	}
}