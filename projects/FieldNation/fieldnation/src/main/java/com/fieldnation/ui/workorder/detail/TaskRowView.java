package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.HashSet;
import java.util.Hashtable;

public class TaskRowView extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("TaskRowView");

    // Ui
    private IconFontTextView _iconView;
    private TextView _descriptionTextView;
    private ProgressBar _progressBar;

    // Data
    private WorkOrder _workOrder;
    private WorkorderClient _workorderClient;
    private Task _task;
    private Listener _listener = null;

    private final HashSet<String> _uploadingFiles = new HashSet<>();
    private final Hashtable<String, Integer> _uploadingProgress = new Hashtable<>();

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
        _progressBar = (ProgressBar) findViewById(R.id.progress_view);

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

    public void setData(WorkOrder workOrder, Task task) {
        _task = task;
        _workOrder = workOrder;

        if (_task.getAttachments() != null && _task.getAttachments().getId() != null) {
            subscribeUpload();
        }

        populateUi();
    }

    public void setProgress(Integer progress) {
        if (_progressBar == null)
            return;

        _progressBar.setVisibility(VISIBLE);
        if (progress == null) {
            _progressBar.setIndeterminate(true);
            return;
        }

        _progressBar.setIndeterminate(false);
        _progressBar.setMax(100);
        _progressBar.setProgress(progress);
    }

    private void populateUi() {
        if (_iconView == null)
            return;

        if (_workOrder == null)
            return;

        if (_task == null)
            return;

        if (_uploadingFiles.size() > 0) {

            if (_uploadingFiles.size() == 1) {
                _descriptionTextView.setText(_task.getType().getName() + "\nUploading: " + _uploadingFiles.iterator().next());
            } else if (_uploadingFiles.size() > 1) {
                _descriptionTextView.setText(_task.getType().getName() + "\nUploading " + _uploadingFiles.size() + " files");
            }

            int progress = 0;
            for (Integer val : _uploadingProgress.values()) {
                progress += val;
            }

            if (_uploadingProgress.size() > 0) {
                int pos = progress / _uploadingProgress.size();

                if (pos == 100) {
                    _progressBar.setIndeterminate(true);
                    _progressBar.setVisibility(VISIBLE);
                } else {
                    _progressBar.setIndeterminate(false);
                    _progressBar.setProgress(progress / _uploadingProgress.size());
                    _progressBar.setVisibility(VISIBLE);
                }
            } else {
                _progressBar.setIndeterminate(true);
                _progressBar.setVisibility(VISIBLE);
            }

        }

        else if (_task.getCustomField() != null) {
            _progressBar.setVisibility(GONE);

            boolean isDescriptionSet = false;
            if (_task.getCustomField() != null
                    && _task.getCustomField().getName() != null) {
                // do not remove the casting here!
                _descriptionTextView.setText(_task.getType().getName() + ": " + _task.getCustomField().getName());
                isDescriptionSet = true;
            }

            if (!isDescriptionSet) {
                _descriptionTextView.setText(_task.getType().getName());
            }

        } else {
            _progressBar.setVisibility(GONE);
            String description = (_task.getType().getId() == 1 || _task.getType().getId() == 2
                    || _task.getType().getId() == 3 || _task.getType().getId() == 4)
                    ? _task.getType().getName() : _task.getType().getName() + "\n" + _task.getLabel();
            _descriptionTextView.setText(description);
        }
        updateCheckBox();
    }

    private void updateCheckBox() {
        if (_task != null && _task.getActionsSet() != null
                && (_task.getActionsSet().contains(Task.ActionsEnum.EDIT)
                || _task.getActionsSet().contains(Task.ActionsEnum.COMPLETE)
                || _task.getActionsSet().contains(Task.ActionsEnum.INCOMPLETE))) {
            _descriptionTextView.setTextColor(getResources().getColor(R.color.fn_dark_text));
            setEnabled(true);
        } else {
            _descriptionTextView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
            setEnabled(false);
        }

        if (_task.getStatus() != null && _task.getStatus().equals(Task.StatusEnum.COMPLETE)) {
            _iconView.setTextColor(getResources().getColor(R.color.fn_accent_color));
            _iconView.setText(R.string.icon_task_done);
        } else {
            _iconView.setTextColor(getResources().getColor(R.color.fn_light_text));
            _iconView.setText(R.string.icon_task);
        }

    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private void subscribeUpload() {
        if (_workOrder == null)
            return;

        if (_task == null || _task.getAttachments() == null || _task.getAttachments().getId() == null)
            return;

        if (_workorderClient == null)
            return;

        if (!_workorderClient.isConnected())
            return;

        _workorderClient.subDeliverableUpload(_workOrder.getId(), _task.getAttachments().getId());
        _workorderClient.subDeliverableProgress(_workOrder.getId(), _task.getAttachments().getId());
    }

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            subscribeUpload();
        }

        @Override
        public void onUploadDeliverable(long workorderId, long slotId, String filename, boolean isComplete, boolean failed) {
            if (failed || isComplete) {
                _uploadingFiles.remove(filename);
                _uploadingProgress.put(filename, 100);

                if (_uploadingFiles.size() == 0)
                    _uploadingProgress.clear();
            } else {
                _uploadingFiles.add(filename);
                if (!_uploadingProgress.containsKey(filename))
                    _uploadingProgress.put(filename, 0);
            }
            populateUi();
        }

        @Override
        public void onUploadDeliverableProgress(long workorderId, long slotId, String filename, long pos, long size, long time) {
            Double percent = pos * 1.0 / size;
            Log.v(TAG, "onUploadDeliverableProgress(" + workorderId + "," + slotId + "," + filename + "," + (pos * 100 / size) + "," + (int) (time / percent));
            int prog = (int) (pos * 100 / size);
            _uploadingProgress.put(filename, prog);
            populateUi();
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