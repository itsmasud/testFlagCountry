package com.fieldnation.ui.workorder.detail;

import java.util.Arrays;
import java.util.List;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TaskListView extends RelativeLayout {
	private static final String TAG = "ui.workorder.detail.TaskListView";

	// UI
	private TextView _preVisistTextView;
	private LinearLayout _preVisistList;
	private LinearLayout _onSiteLayout;
	private LinearLayout _onSiteList;
	private LinearLayout _postVisitLayout;
	private LinearLayout _postVisitList;
	private TextView _noDataTextView;

	// Data
	private List<Task> _tasks;
	private Listener _listener;
    private Workorder _workorder;
    private Integer[] woStatus = {5, 6, 7}; //work order status approved, paid, canceled

	public TaskListView(Context context) {
		super(context);
		init();
	}

	public TaskListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TaskListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_wd_task_list, this);

		if (isInEditMode())
			return;

		_preVisistTextView = (TextView) findViewById(R.id.previsit_textview);
		_preVisistList = (LinearLayout) findViewById(R.id.previsit_list);
		_onSiteLayout = (LinearLayout) findViewById(R.id.onsite_layout);
		_onSiteList = (LinearLayout) findViewById(R.id.onsite_list);
		_postVisitLayout = (LinearLayout) findViewById(R.id.postvisit_layout);
		_postVisitList = (LinearLayout) findViewById(R.id.postvisit_list);
		_noDataTextView = (TextView) findViewById(R.id.nodata_textview);

	}

	public void setTaskListViewListener(TaskListView.Listener l) {
		_listener = l;
	}

	public void setTaskList(List<Task> tasks) {
		_tasks = tasks;

		populateUi();
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
		_onSiteList.removeAllViews();
		_postVisitList.removeAllViews();
		if (nocategories) {
			_preVisistTextView.setVisibility(View.GONE);
			_onSiteLayout.setVisibility(View.GONE);
			_postVisitLayout.setVisibility(View.GONE);

			for (int i = 0; i < _tasks.size(); i++) {
				Task task = _tasks.get(i);

				TaskRowView row = new TaskRowView(getContext());
				row.setTask(task);

                //if work order completed or canceled then hide/disable any controls actions
                if(_workorder != null && !Arrays.asList(woStatus).contains(_workorder.getStatusId())) {
                    row.setOnTaskClickListener(_task_onClick);
                }

				_preVisistList.addView(row);
			}
		} else {
			_preVisistTextView.setVisibility(View.VISIBLE);
			_onSiteLayout.setVisibility(View.VISIBLE);
			_postVisitLayout.setVisibility(View.VISIBLE);
			for (int i = 0; i < _tasks.size(); i++) {
				Task task = _tasks.get(i);

				TaskRowView row = new TaskRowView(getContext());
				row.setTask(task);

                //if work order completed or canceled then hide/disable any controls actions
                if(_workorder != null && !Arrays.asList(woStatus).contains(_workorder.getStatusId())) {
                    row.setOnTaskClickListener(_task_onClick);
                }

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

    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
    }

	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/
	private TaskRowView.Listener _task_onClick = new TaskRowView.Listener() {
		@Override
		public void onTaskClick(Task task) {
			if (_listener != null) {
				_listener.onTaskClick(task);
			}
		}
	};

	public interface Listener {
		public void onTaskClick(Task task);
	}
}
