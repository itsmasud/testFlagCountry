package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.TaskType;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.utils.misc;

public class TaskRowView extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("TaskRowView");

    // Ui
    private IconFontTextView _iconView;
    private TextView _descriptionTextView;

    // Data
    private Workorder _workorder;
    private WorkorderClient _workorderClient;
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

        _iconView = (IconFontTextView) findViewById(R.id.icon_view);
        _descriptionTextView = (TextView) findViewById(R.id.description_textview);

        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(App.get());

        setOnClickListener(_checkbox_onClick);

        populateUi();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (_workorderClient != null && _workorderClient.isConnected())
            _workorderClient.disconnect(App.get());
        _workorderClient = null;
        super.onDetachedFromWindow();
    }

    public void setOnTaskClickListener(Listener listener) {
        _listener = listener;
    }

    public void setData(Workorder workorder, Task task) {
        _task = task;
        _workorder = workorder;

        if (_task.getSlotId() != null) {
            subscribeUpload();
        }

        populateUi();
    }

    private void populateUi() {
        if (_iconView == null)
            return;

        if (_workorder == null)
            return;

        setEnabled(_workorder.canModifyTasks());

        if (_workorder.canModifyTasks()) {
            _descriptionTextView.setTextColor(getResources().getColor(R.color.fn_dark_text));
        } else {
            _descriptionTextView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
        }


        TaskType type = _task.getTaskType();

        if (misc.isEmptyOrNull(_task.getDescription())) {
            _descriptionTextView.setText(type.getDisplay(getContext()));
        } else {
            _descriptionTextView.setText(type.getDisplay(getContext()) + "\n" + _task.getDescription());
        }
        updateCheckBox();
    }

    private void updateCheckBox() {
        if (_workorder.canModifyTasks()) {
            // set enabled
            if (_task.getCompleted()) {
                _iconView.setTextColor(getResources().getColor(R.color.fn_accent_color));
                _iconView.setText(R.string.icon_task_done);
            } else {
                _iconView.setTextColor(getResources().getColor(R.color.fn_light_text));
                _iconView.setText(R.string.icon_task);
            }
        } else {
            if (_task.getCompleted()) {
                _iconView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
                _iconView.setText(R.string.icon_task_done);
            } else {
                _iconView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
                _iconView.setText(R.string.icon_task);
            }
        }
    }

    private void subscribeUpload() {
        if (_workorder == null)
            return;

        if (_task == null || _task.getSlotId() == null)
            return;

        if (_workorderClient == null)
            return;

        if (!_workorderClient.isConnected())
            return;


        _workorderClient.subDeliverableUpload(_workorder.getWorkorderId(), _task.getSlotId());
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            subscribeUpload();
        }

        @Override
        public void onUploadDeliverable(long workorderId, long slotId, String filename, boolean isComplete, boolean failed) {
            TaskType type = _task.getTaskType();

            if (!isComplete) {
                _descriptionTextView.setText(type.getDisplay(getContext()) + "\nUploading: " + filename);
            } else {
                _descriptionTextView.setText(type.getDisplay(getContext()) + "\n" + filename);
            }
        }
    };


    private final View.OnClickListener _checkbox_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateCheckBox();

            if (_listener != null) {
                _listener.onTaskClick(_task);
            }
        }
    };

    public interface Listener {
        void onTaskClick(Task task);
    }

}
