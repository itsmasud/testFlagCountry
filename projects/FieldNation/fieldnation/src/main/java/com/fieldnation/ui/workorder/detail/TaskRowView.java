package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.TaskType;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;

public class TaskRowView extends RelativeLayout {
    private static final String TAG = "ui.workorder.detail.TaskRowView";

    // Ui
    private CheckBox _checkbox;

    // Data
    private Workorder _workorder;
    private Task _task;
    private Listener _listener = null;

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
        _checkbox.setOnClickListener(_checkbox_onClick);
    }

    public void setData(Workorder workorder, Task task) {
        _task = task;
        _workorder = workorder;

        _checkbox.setEnabled(_workorder.canModify());

        TaskType type = task.getTaskType();

        if (misc.isEmptyOrNull(task.getDescription())) {
            _checkbox.setText(type.getDisplay(getContext()));
        } else {
            _checkbox.setText(type.getDisplay(getContext()) + "\n" + task.getDescription());
        }

        _checkbox.setChecked(task.getCompleted());
    }

    public void setOnTaskClickListener(Listener listener) {
        _listener = listener;
    }

    private View.OnClickListener _checkbox_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _checkbox.setChecked(_task.getCompleted());

            if (_listener != null) {
                _listener.onTaskClick(_task);
            }
        }
    };

    public interface Listener {
        public void onTaskClick(Task task);
    }

}
