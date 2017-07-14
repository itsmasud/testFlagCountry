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
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
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
    private WorkordersWebApi _workOrderApi;
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

        _iconView = findViewById(R.id.icon_view);
        _descriptionTextView = findViewById(R.id.description_textview);
        _progressBar = findViewById(R.id.progress_view);

        setOnClickListener(_checkbox_onClick);

        populateUi();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (_workOrderApi != null) _workOrderApi.disconnect(App.get());
        _workOrderApi = null;

        super.onDetachedFromWindow();
    }

    public void setOnTaskClickListener(Listener listener) {
        _listener = listener;
    }

    public void setData(WorkOrder workOrder, Task task) {
        _task = task;
        _workOrder = workOrder;

        if (_task.getAttachments().getId() != null) {
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

            // custom fields
        } else if (_task.getType().getId() == 7) {
            _progressBar.setVisibility(GONE);
            _descriptionTextView.setText(_task.getDescription());
        } else if (_task.getType().getId() == 8) {
            _descriptionTextView.setText("Call " + _task.getPhone() + "\n" + _task.getLabel());
        } else if (_task.getType().getId() == 9) {
            _descriptionTextView.setText("Email " + _task.getEmail() + "\n" + _task.getLabel());
        } else if (_task.getType().getId() == 10) {
            _descriptionTextView.setText("Complete Task\n" + _task.getLabel());

            // normals
        } else {
            _progressBar.setVisibility(GONE);
            if (misc.isEmptyOrNull(_task.getLabel()) || _task.getLabel().equals(_task.getType().getName()))
                _descriptionTextView.setText(_task.getType().getName());
            else
                _descriptionTextView.setText(_task.getType().getName() + "\n" + _task.getLabel());
        }
        updateCheckBox();
    }

    private void updateCheckBox() {
        if (_task != null
                && (_task.getActionsSet().contains(Task.ActionsEnum.EDIT)
                || _task.getActionsSet().contains(Task.ActionsEnum.COMPLETE))) {
            _descriptionTextView.setTextColor(getResources().getColor(R.color.fn_dark_text));
            setEnabled(true);
        } else {
            _descriptionTextView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
            setEnabled(false);
        }

        if (_task.getStatus() != null
                && _task.getStatus().equals(Task.StatusEnum.COMPLETE)) {
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

        if (_workOrderApi == null || !_workOrderApi.isConnected()) {
            _workOrderApi = new WorkordersWebApi(_workOrderApi_listener);
            _workOrderApi.connect(App.get());
        }
    }

    private final View.OnClickListener _checkbox_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateCheckBox();

            if (_listener != null) {
                _listener.onTaskClick(_task);
            }
        }
    };

    private final WorkordersWebApi.Listener _workOrderApi_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            if (_workOrderApi != null) _workOrderApi.subWorkordersWebApi();
        }

        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.equals("addAttachment");
        }

        @Override
        public void onQueued(TransactionParams transactionParams, String methodName) {
            if (!methodName.equals("addAttachment"))
                return;
            Log.v(TAG, "onQueued");
            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");

                if (folderId == _task.getAttachments().getId()) {
                    _uploadingFiles.add(name);
                    _uploadingProgress.put(name, UploadedDocumentView.PROGRESS_QUEUED);
                    populateUi();
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onPaused(TransactionParams transactionParams, String methodName) {
            if (!methodName.equals("addAttachment"))
                return;
            Log.v(TAG, "onPaused");
            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");

                if (folderId == _task.getAttachments().getId()) {
                    _uploadingFiles.add(name);
                    _uploadingProgress.put(name, UploadedDocumentView.PROGRESS_PAUSED);
                    populateUi();
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onProgress(TransactionParams transactionParams, String methodName, long pos, long size, long time) {
            if (!methodName.equals("addAttachment"))
                return;

            Log.v(TAG, "onProgress");
            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");

                if (folderId == _task.getAttachments().getId()) {
                    Double percent = pos * 1.0 / size;
                    Log.v(TAG, "onProgress(" + folderId + "," + name + "," + (pos * 100 / size) + "," + (int) (time / percent));
                    _uploadingFiles.add(name);
                    _uploadingProgress.put(name, (int) (pos * 100 / size));
                    populateUi();
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (!methodName.equals("addAttachment"))
                return;

            Log.v(TAG, "onComplete");
            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");

                if (folderId == _task.getAttachments().getId()) {
                    _uploadingFiles.remove(name);
                    _uploadingProgress.remove(name);
                    populateUi();
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    public interface Listener {
        void onTaskClick(Task task);
    }
}