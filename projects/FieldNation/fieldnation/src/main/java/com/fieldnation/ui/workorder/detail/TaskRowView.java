package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.TaskType;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;

public class TaskRowView extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("ui.workorder.detail.TaskRowView");

    // Ui
    private CheckBox _checkbox;

    // Data
    private Workorder _workorder;
    private Task _task;
    private Listener _listener = null;
    private String _uploadUrl;

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


        populateUi();
    }

    @Override
    protected void finalize() throws Throwable {
//        TopicService.delete(getContext(), TAG);

        super.finalize();
    }

    public void setData(Workorder workorder, Task task) {
        _task = task;
        _workorder = workorder;

        if (_task.getSlotId() != null) {
            _uploadUrl = _workorder.getWorkorderId() + "/deliverables/" + _task.getSlotId();
//            Topics.subscribeFileUpload(getContext(), TAG, _uploadReceiver);
        }


        populateUi();
    }

    private void populateUi() {
        if (_checkbox == null)
            return;
        if (_workorder == null)
            return;

        _checkbox.setEnabled(_workorder.canModifyTasks());

        TaskType type = _task.getTaskType();

        if (misc.isEmptyOrNull(_task.getDescription())) {
            _checkbox.setText(type.getDisplay(getContext()));
        } else {
            _checkbox.setText(type.getDisplay(getContext()) + "\n" + _task.getDescription());
        }

        _checkbox.setChecked(_task.getCompleted());

    }


    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/

/*
    private FileUploadTopicReceiver _uploadReceiver = new FileUploadTopicReceiver(new Handler()) {
        @Override
        public void onStart(String url, String filename) {
            if (_task != null && _workorder != null) {
                if (url.contains(_uploadUrl)) {
                    Log.v(TAG, "This task is uploading a file..." + url);
                    TaskType type = _task.getTaskType();
                    _checkbox.setText(type.getDisplay(getContext()) + "\nUploading: " + filename);
                }
            }
        }

        @Override
        public void onFinish(String url, String filename) {
            if (_task != null && _workorder != null) {
                if (url.contains(_uploadUrl)) {
                    Log.v(TAG, "This task is uploading a file..." + url);
                    TaskType type = _task.getTaskType();
                    _checkbox.setText(type.getDisplay(getContext()) + "\n" + filename);
                }
            }
        }

        @Override
        public void onError(String url, String filename, String message) {
            if (_task != null && _workorder != null) {
                if (url.contains(_uploadUrl)) {
                    Log.v(TAG, "This task is uploading a file..." + url);
                    TaskType type = _task.getTaskType();
                    _checkbox.setText(type.getDisplay(getContext()) + "\nFailed: " + filename);
                }
            }
        }
    };
*/

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
