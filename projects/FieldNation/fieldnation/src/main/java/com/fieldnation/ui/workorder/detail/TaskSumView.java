package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.Workorder;

public class TaskSumView extends RelativeLayout implements WorkorderRenderer {
    private static final String TAG = "ui.workorder.detail.TaskSumView";

    // UI
    private TextView _taskCountTextView;
    private LinearLayout _contentLayout;
    private RelativeLayout _loadingLayout;
    private TextView _viewTasksTextView;

    // Data
    private Workorder _workorder;
    private Listener _listener;

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
        _viewTasksTextView = (TextView) findViewById(R.id.viewtasks_textview);
        _viewTasksTextView.setOnClickListener(_viewtasks_onClick);

        setVisibility(View.GONE);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    public void setWorkorder(Workorder workorder, boolean isCached) {
        _workorder = workorder;

        Task[] tasks = workorder.getTasks();

        if (tasks != null && tasks.length > 0) {
            _taskCountTextView.setText(tasks.length + " ");
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }
    }

    private View.OnClickListener _viewtasks_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onShowTasksTab();
        }
    };

    public interface Listener {
        public void onShowTasksTab();
    }
}