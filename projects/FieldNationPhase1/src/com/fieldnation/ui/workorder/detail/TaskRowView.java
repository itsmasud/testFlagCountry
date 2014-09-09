package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Task;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class TaskRowView extends RelativeLayout {
	private static final String TAG = "ui.workorder.detail.TaskRowView";

	// Ui
	private CheckBox _checkbox;

	// Data
	private Task _task;

	public TaskRowView(Context context) {
		super(context);
		init();
	}

	public TaskRowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TaskRowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_wd_task_row, this);

		if (isInEditMode())
			return;
		_checkbox = (CheckBox) findViewById(R.id.checkbox);
	}

	public void setTask(Task task) {
		_task = task;

		_checkbox.setText(task.getDescription());
	}

}
