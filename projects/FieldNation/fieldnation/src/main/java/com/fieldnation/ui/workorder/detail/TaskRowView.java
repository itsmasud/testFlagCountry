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
import com.fieldnation.data.workorder.CustomField;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.TaskType;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.IconFontTextView;

import java.util.HashSet;
import java.util.Hashtable;

public class TaskRowView extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("TaskRowView");

    // Ui
    private IconFontTextView _iconView;
    private TextView _descriptionTextView;
    private ProgressBar _progressBar;

    // Data
    private Workorder _workorder;
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

    public void setData(Workorder workorder, Task task) {
        _task = task;
        _workorder = workorder;

        if (_task.getSlotId() != null) {
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

        if (_workorder == null)
            return;

        setEnabled(_workorder.canModifyTasks());

        if (_workorder.canModifyTasks()) {
            _descriptionTextView.setTextColor(getResources().getColor(R.color.fn_dark_text));
        } else {
            _descriptionTextView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
        }


        TaskType type = _task.getTaskType();

        if (_uploadingFiles.size() > 0) {

            if (_uploadingFiles.size() == 1) {
                _descriptionTextView.setText(type.getDisplay(getContext()) + "\nUploading: " + _uploadingFiles.iterator().next());
            } else if (_uploadingFiles.size() > 1) {
                _descriptionTextView.setText(type.getDisplay(getContext()) + "\nUploading " + _uploadingFiles.size() + " files");
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

        } else if (misc.isEmptyOrNull(_task.getDescription())) {
            _progressBar.setVisibility(GONE);

            boolean isDescriptionSet = false;

            if (_workorder.getCustomFields() != null) {
                for (CustomField cf : _workorder.getCustomFields()) {
                    // do not remove the casting here!
                    if (_task.getCustomField() != null && (long) cf.getCustomLabelId() == (long) _task.getCustomField()) {
                        _descriptionTextView.setText(type.getDisplay(getContext()) + ": " + cf.getLabel());
                        isDescriptionSet = true;
                        break;
                    }
                }
            }
            if (!isDescriptionSet) {
                _descriptionTextView.setText(type.getDisplay(getContext()));
            }
        } else {
            _progressBar.setVisibility(GONE);
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

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
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
        _workorderClient.subDeliverableProgress(_workorder.getWorkorderId(), _task.getSlotId());
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
