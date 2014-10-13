package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.Workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TaskSumView extends RelativeLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.TaskSumView";

	// UI
	private TextView _taskCountTextView;
	private LinearLayout _contentLayout;
	private RelativeLayout _loadingLayout;

	// Data
	private Workorder _workorder;

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

		_contentLayout = (LinearLayout) findViewById(R.id.content_layout);
		_loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);
		_taskCountTextView = (TextView) findViewById(R.id.taskcount_textview);
	}

	private void setLoading(boolean isLoading) {
		if (isLoading) {
			_contentLayout.setVisibility(View.GONE);
			_loadingLayout.setVisibility(View.VISIBLE);
		} else {
			_contentLayout.setVisibility(View.VISIBLE);
			_loadingLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		_workorder.addListener(_workorder_listener);

		Task[] tasks = workorder.getTasks();

		if (tasks != null) {
			_taskCountTextView.setText(tasks.length + " ");
		} else {
			_taskCountTextView.setText("0 ");
		}
		setLoading(false);
	}

	private Workorder.Listener _workorder_listener = new Workorder.Listener() {

		@Override
		public void onChange(Workorder workorder) {
			setLoading(true);
		}
	};
}