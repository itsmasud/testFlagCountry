package com.fieldnation.v2.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.AnswersWrapper;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.workorder.detail.UploadedDocumentView;
import com.fieldnation.v2.data.client.AttachmentHelper;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.GetFileDialog;
import com.fieldnation.v2.ui.dialog.PhotoUploadDialog;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shoaib on 6/10/17.
 */

public class TaskRowView extends RelativeLayout {
    private static final String TAG = UniqueTag.makeTag("TaskRowView");

    // Ui
    private TextView _keyTextView;
    private TextView _valueTextView;
    private TextView _rightValueTextView;
    private ProgressBar _progressBar;

    // Data
    private WorkOrder _workOrder;
    private Task _task;

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

    public TaskRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_task_row, this);

        if (isInEditMode())
            return;

        _keyTextView = findViewById(R.id.key);
        _valueTextView = findViewById(R.id.value);
        _rightValueTextView = findViewById(R.id.value_right);
        _progressBar = findViewById(R.id.progressBar);

        populateUi();
    }

    @Override
    protected void onDetachedFromWindow() {
        _workOrderApi.unsub();
        super.onDetachedFromWindow();
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
        if (_keyTextView == null)
            return;

        if (_workOrder == null)
            return;

        if (_task == null)
            return;

        if (_uploadingFiles.size() > 0) {

            if (_uploadingFiles.size() == 1) {
                _keyTextView.setText(_task.getType().getName() + "\nUploading: " + _uploadingFiles.iterator().next());
            } else if (_uploadingFiles.size() > 1) {
                _keyTextView.setText(_task.getType().getName() + "\nUploading " + _uploadingFiles.size() + " files");
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

        } else {
            _progressBar.setVisibility(GONE);
            drawRowByType(getType(_task));
        }
        updateView();
    }


    private String getFormattedTime(Calendar cal) {
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        symbols.setAmPmStrings(getResources().getStringArray(R.array.schedule_capital_case_am_pm_array));

        SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy @ hh:mm a", Locale.getDefault());
        sdf.setDateFormatSymbols(symbols);

        return sdf.format(cal.getTime()) + DateUtils.getDeviceTimezone();
    }

    public TaskTypeEnum getType(Task task) {
        return TaskTypeEnum.fromTypeId(task.getType().getId());
    }

    private void drawRowByType(TaskTypeEnum taskType) {
        try {
            switch (taskType) {
                case SET_ETA: // set eta
                    _keyTextView.setText(_task.getType().getName());
                    _valueTextView.setVisibility(GONE);
                    if (_task.getEta().getStart().getUtc() == null) {
                        _rightValueTextView.setVisibility(GONE);
                    } else {
                        _rightValueTextView.setVisibility(VISIBLE);
                        _rightValueTextView.setText(getFormattedTime(_task.getEta().getStart().getCalendar()));
                    }
                    break;
                case CLOSING_NOTES: // closing notes
                    _keyTextView.setText(_task.getLabel());
                    if (misc.isEmptyOrNull(_task.getClosingNotes()))
                        _valueTextView.setVisibility(GONE);
                    else {
                        _valueTextView.setText(_task.getClosingNotes());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    _rightValueTextView.setVisibility(GONE);
                    break;
                case CHECK_IN: // check in
                    _keyTextView.setText(_task.getLabel());
                    _valueTextView.setVisibility(GONE);
                    if (_task.getTimeLog().getIn().getCreated().getUtc() == null) {
                        _rightValueTextView.setVisibility(GONE);
                    } else {
                        _rightValueTextView.setVisibility(VISIBLE);
                        _rightValueTextView.setText(getFormattedTime(_task.getTimeLog().getIn().getCreated().getCalendar()));
                    }
                    break;
                case CHECK_OUT: // check out
                    _keyTextView.setText(_task.getLabel());
                    _valueTextView.setVisibility(GONE);
                    if (_task.getTimeLog().getOut().getCreated().getUtc() == null) {
                        _rightValueTextView.setVisibility(GONE);
                    } else {
                        _rightValueTextView.setVisibility(VISIBLE);
                        _rightValueTextView.setText(getFormattedTime(_task.getTimeLog().getOut().getCreated().getCalendar()));
                    }
                    break;
                case UPLOAD_FILE: // upload file
                    _keyTextView.setText(_task.getType().getName());
                    _rightValueTextView.setVisibility(GONE);

                    if (misc.isEmptyOrNull(_task.getLabel())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
                case UPLOAD_PICTURE: // upload picture
                    _keyTextView.setText(_task.getType().getName());
                    _rightValueTextView.setVisibility(GONE);

                    if (misc.isEmptyOrNull(_task.getLabel())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
                case CUSTOM_FIELD: // custom field
                    _keyTextView.setText(_task.getLabel());
                    _rightValueTextView.setVisibility(GONE);

                    if (misc.isEmptyOrNull(_task.getDescription())) { // TODO need to add task.getDescriptions().getFirst(). Talk with Kamrul vai
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getDescription());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
                case PHONE: // phone
                    _keyTextView.setText("Call " + _task.getPhone());
                    _rightValueTextView.setVisibility(GONE);

                    if (misc.isEmptyOrNull(_task.getLabel())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
                case EMAIL: // email
                    _keyTextView.setText("Email " + _task.getEmail());
                    _rightValueTextView.setVisibility(GONE);
                    if (misc.isEmptyOrNull(_task.getLabel())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
                case UNIQUE_TASK: // unique task
                    _keyTextView.setText("Complete tasks");
                    _rightValueTextView.setVisibility(GONE);
                    if (misc.isEmptyOrNull(_task.getLabel())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
                case SIGNATURE: // signature
                    _keyTextView.setText(_task.getType().getName());
                    _rightValueTextView.setVisibility(GONE);
                    if (misc.isEmptyOrNull(_task.getLabel())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
                case SHIPMENT: // shipment
                    _keyTextView.setText(_task.getType().getName());
                    _rightValueTextView.setVisibility(GONE);
                    if (misc.isEmptyOrNull(_task.getLabel())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
                case DOWNLOAD:
                    _keyTextView.setText(_task.getType().getName());
                    _rightValueTextView.setVisibility(GONE);
                    if (misc.isEmptyOrNull(_task.getLabel())) {
                        _valueTextView.setVisibility(GONE);
                    } else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
            }
        } catch (Exception e) {
            Log.v(TAG, e);
        }
    }

    private void updateView() {
        if (_task != null
                && (_task.getActionsSet().contains(Task.ActionsEnum.EDIT)
                || _task.getActionsSet().contains(Task.ActionsEnum.COMPLETE))) {
            _keyTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.fn_dark_text));
            setEnabled(true);
        } else {
            _keyTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.fn_light_text_50));
            setEnabled(false);
        }
    }


    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private void subscribeUpload() {
        if (_workOrder == null)
            return;

        _workOrderApi.sub();
    }

    private final WorkordersWebApi _workOrderApi = new WorkordersWebApi() {
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

//    private final View.OnClickListener _click_listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
////            if (_listener == null)
////                return;
//
//            WorkOrderTracker.onTaskEvent(App.get(), _task.getType(), _workOrder.getId());
//            updateView();
////            _listener.onTaskClick(_task);
//
//        }
//    };

//    public interface Listener {
//        void onTaskClick(Task task);
//    }
//

}
