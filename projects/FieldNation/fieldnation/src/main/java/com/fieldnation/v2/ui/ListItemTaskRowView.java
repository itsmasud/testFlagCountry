package com.fieldnation.v2.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fnactivityresult.ActivityResultConstants;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.documents.DocumentClient;
import com.fieldnation.ui.SignOffActivity;
import com.fieldnation.ui.workorder.detail.UploadedDocumentView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.Shipment;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.CheckInOutDialog;
import com.fieldnation.v2.ui.dialog.ClosingNotesDialog;
import com.fieldnation.v2.ui.dialog.CustomFieldDialog;
import com.fieldnation.v2.ui.dialog.EtaDialog;
import com.fieldnation.v2.ui.dialog.GetFileDialog;
import com.fieldnation.v2.ui.dialog.ShipmentAddDialog;
import com.fieldnation.v2.ui.dialog.TaskShipmentAddDialog;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shoaib on 6/10/17.
 */

public class ListItemTaskRowView extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("ListItemTaskRowView");

    // Ui
    private TextView _keyTextView;
    private TextView _valueTextView;
    private TextView _rightValueTextView;
    private ProgressBar _progressBar;

    // Data
    private String _key;
    private String _value;
    private String _action;
    private boolean _progressVisible = false;
    private int _progress = -1;

    private WorkOrder _workOrder;
    private Task _task;

    private final HashSet<String> _uploadingFiles = new HashSet<>();
    private final Hashtable<String, Integer> _uploadingProgress = new Hashtable<>();


    public ListItemTaskRowView(Context context) {
        super(context);
        init();
    }

    public ListItemTaskRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListItemTaskRowView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        setOnClickListener(_click_listener);

        populateUi();
    }

//    public void setActionVisible(boolean visible) {
//        _actionVisible = visible;
//        populateUi();
//    }
//
//    public void setActionString(String action) {
//        _action = action;
//
//        populateUi();
//    }
//
//    public void setProgressVisible(boolean visible) {
//        _progressVisible = visible;
//        populateUi();
//    }
//
//    public void setProgress(int progress) {
//        _progress = progress;
//        populateUi();
//    }
//
//    public void setOnActionClickedListener(OnActionClickListener onActionClickedListener) {
//        _actionOnclickListener = onActionClickedListener;
//    }
//
//    public void set(String key, String value) {
//        _key = key;
//        _value = value;
//        populateUi();
//    }

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
            drawRowByType(getType());
        }
        updateView();
    }


    private String getFormattedTime(Calendar cal) {
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        symbols.setAmPmStrings(getResources().getStringArray(R.array.schedule_small_case_am_pm_array));

        SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy @ hh:mma", Locale.getDefault());
        sdf.setDateFormatSymbols(symbols);

        return sdf.format(cal.getTime()) + DateUtils.getDeviceTimezone();

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

                    if (misc.isEmptyOrNull(_task.getLabel())){
                        _valueTextView.setVisibility(GONE);
                    }else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
                case UPLOAD_PICTURE: // upload picture
                    _keyTextView.setText(_task.getType().getName());
                    _rightValueTextView.setVisibility(GONE);

                    if (misc.isEmptyOrNull(_task.getLabel())){
                        _valueTextView.setVisibility(GONE);
                    }else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
                case CUSTOM_FIELD: // custom field
                    _progressBar.setVisibility(GONE);
                    _keyTextView.setText(_task.getLabel());
                    _rightValueTextView.setVisibility(GONE);

                    if (misc.isEmptyOrNull(_task.getDescription())){ // TODO need to add task.getDescriptions().getFirst(). Talk with Kamrul vai
                        _valueTextView.setVisibility(GONE);
                    }else {
                        _valueTextView.setText(_task.getDescription());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
                case PHONE: // phone
                    _keyTextView.setText("Call " + _task.getPhone());
                    _rightValueTextView.setVisibility(GONE);

                    if (misc.isEmptyOrNull(_task.getLabel())){
                        _valueTextView.setVisibility(GONE);
                    }else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
                case EMAIL: // email
                    _keyTextView.setText("Email " + _task.getEmail());
                    _rightValueTextView.setVisibility(GONE);
                    if (misc.isEmptyOrNull(_task.getLabel())){
                        _valueTextView.setVisibility(GONE);
                    }else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
                case UNIQUE_TASK: // unique task
                    _keyTextView.setText("Complete tasks");
                    _rightValueTextView.setVisibility(GONE);
                    if (misc.isEmptyOrNull(_task.getLabel())){
                        _valueTextView.setVisibility(GONE);
                    }else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
                case SIGNATURE: // signature
                    _keyTextView.setText(_task.getType().getName());
                    _rightValueTextView.setVisibility(GONE);
                    if (misc.isEmptyOrNull(_task.getLabel())){
                        _valueTextView.setVisibility(GONE);
                    }else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
                case SHIPMENT: // shipment
                    _keyTextView.setText(_task.getType().getName());
                    _rightValueTextView.setVisibility(GONE);
                    if (misc.isEmptyOrNull(_task.getLabel())){
                        _valueTextView.setVisibility(GONE);
                    }else {
                        _valueTextView.setText(_task.getLabel());
                        _valueTextView.setVisibility(VISIBLE);
                    }
                    break;
                case DOWNLOAD:
                    _keyTextView.setText(_task.getType().getName());
                    _rightValueTextView.setVisibility(GONE);
                    if (misc.isEmptyOrNull(_task.getLabel())){
                        _valueTextView.setVisibility(GONE);
                    }else {
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


    /*-*********************************************-*/
    /*-				Closing notes Process			-*/
    /*-*********************************************-*/

    private void showClosingNotesDialog() {
        if (_workOrder.getActionsSet().contains(WorkOrder.ActionsEnum.CLOSING_NOTES))
            ClosingNotesDialog.show(App.get(), null, _workOrder.getId(), _workOrder.getClosingNotes());
    }

    /*-*********************************************-*/
    /*-				Check In Process				-*/
    /*-*********************************************-*/
    private void doCheckin() {
        App.get().analActionTitle = null;
        CheckInOutDialog.show(App.get(), null, _workOrder.getId(),
                _workOrder.getTimeLogs(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_IN);
    }

    /*-*********************************************-*/
    /*-				Check Out Process				-*/
    /*-*********************************************-*/
    private void doCheckOut() {
//        setLoading(true);
        int deviceCount = -1;

        Pay pay = _workOrder.getPay();
        if (pay.getType() == Pay.TypeEnum.DEVICE
                && pay.getBase().getUnits() != null) {
            deviceCount = pay.getBase().getUnits().intValue();
        }

        App.get().analActionTitle = null;

        if (deviceCount > -1) {
            CheckInOutDialog.show(App.get(), null, _workOrder.getId(),
                    _workOrder.getTimeLogs(), deviceCount, CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
        } else {
            CheckInOutDialog.show(App.get(), null, _workOrder.getId(),
                    _workOrder.getTimeLogs(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
        }
    }

    private void startAppPickerDialog() {
        GetFileDialog.show(App.get(), null);
    }

    public TaskTypeEnum getType() {
        return TaskTypeEnum.fromTypeId(_task.getType().getId());
    }

    private enum TaskTypeEnum {
        SET_ETA(1),
        CLOSING_NOTES(2),
        CHECK_IN(3),
        CHECK_OUT(4),
        UPLOAD_FILE(5),
        UPLOAD_PICTURE(6),
        CUSTOM_FIELD(7),
        PHONE(8),
        EMAIL(9),
        UNIQUE_TASK(10),
        SIGNATURE(11),
        SHIPMENT(12),
        DOWNLOAD(13),
        NOT_SUPPORTED(14);

        private int value;

        TaskTypeEnum(int value) {
            this.value = value;
        }

        private int getValue() {
            return this.value;
        }

        private static TaskTypeEnum fromTypeId(int id) {
            for (int i = 0; i < values().length; i++) {
                if (values()[i].value == id)
                    return values()[i];
            }
            return NOT_SUPPORTED;
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

//    private final View.OnClickListener _checkbox_onClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            updateView();
//
//            if (_listener != null) {
//                _listener.onTaskClick(_task);
//            }
//        }
//    };

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

    private final View.OnClickListener _click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            WorkOrderTracker.onTaskEvent(App.get(), _task.getType(), _workOrder.getId());

            updateView();

            switch (getType()) {

                case SET_ETA: // set eta
                    App.get().analActionTitle = null;
                    EtaDialog.show(App.get(), null, _workOrder.getId(), _workOrder.getSchedule(),
                            _workOrder.getEta(), EtaDialog.PARAM_DIALOG_TYPE_ADD);
                    break;
                case CLOSING_NOTES: // closing notes
                    showClosingNotesDialog();
                    break;
                case CHECK_IN: // check in
                    doCheckin();
                    break;
                case CHECK_OUT: // check out
                    doCheckOut();
                    break;
                case UPLOAD_FILE: // upload file
                    startAppPickerDialog();
                    break;
                case UPLOAD_PICTURE: // upload picture
                    startAppPickerDialog();
                    break;
                case CUSTOM_FIELD: // custom field
                    CustomFieldDialog.show(App.get(), null, _workOrder.getId(), _task.getCustomField());
                    break;
                case PHONE: // phone
                    if (_task.getStatus() != null && !_task.getStatus().equals(Task.StatusEnum.COMPLETE))
                        try {
                            WorkordersWebApi.updateTask(App.get(), _workOrder.getId(), _task.getId(), new Task().status(Task.StatusEnum.COMPLETE), App.get().getSpUiContext());
                        } catch (Exception ex) {
                            Log.v(TAG, ex);
                        }

                    try {
                        if (_task.getPhone() != null) {
                            if (!TextUtils.isEmpty(_task.getPhone()) && android.util.Patterns.PHONE.matcher(_task.getPhone()).matches()) {
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                String phNum = "tel:" + _task.getPhone();
                                callIntent.setData(Uri.parse(phNum));
                                ActivityClient.startActivity(callIntent);
//                                setLoading(true);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage(R.string.dialog_no_number_message);
                                builder.setTitle(R.string.dialog_no_number_title);
                                builder.setPositiveButton(R.string.btn_ok, null);
                                builder.show();
                            }

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage(R.string.dialog_no_number_message);
                            builder.setTitle(R.string.dialog_no_number_title);
                            builder.setPositiveButton(R.string.btn_ok, null);
                            builder.show();
                        }
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                    break;
                case EMAIL: // email
                    String email = _task.getEmail();
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + email));
                    ActivityClient.startActivityForResult(intent, ActivityResultConstants.RESULT_CODE_SEND_EMAIL);

                    try {
                        WorkordersWebApi.updateTask(App.get(), _workOrder.getId(), _task.getId(), new Task().status(Task.StatusEnum.COMPLETE), App.get().getSpUiContext());
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
//                    setLoading(true);
                    break;
                case UNIQUE_TASK: // unique task
                    if (_task.getStatus() != null && _task.getStatus().equals(Task.StatusEnum.COMPLETE))
                        return;

                    try {
                        WorkordersWebApi.updateTask(App.get(), _workOrder.getId(), _task.getId(), new Task().status(Task.StatusEnum.COMPLETE), App.get().getSpUiContext());
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
//                    setLoading(true);
                    break;
                case SIGNATURE: // signature
                    SignOffActivity.startSignOff(App.get(), _workOrder.getId(), _task.getId());
//                    setLoading(true);
                    break;
                case SHIPMENT: // shipment
                    List<Shipment> shipments = new LinkedList();
                    for (Shipment shipment : _workOrder.getShipments().getResults()) {
                        if (shipment.getDirection().equals(Shipment.DirectionEnum.FROM_SITE))
                            shipments.add(shipment);
                    }

                    if (shipments.size() == 0) {
                        ShipmentAddDialog.show(App.get(), null, _workOrder.getId(),
                                _workOrder.getAttachments(), getContext().getString(R.string.dialog_task_shipment_title), null, _task);
                    } else {
                        TaskShipmentAddDialog.show(App.get(), null, _workOrder.getId(),
                                _workOrder.getShipments(), getContext().getString(R.string.dialog_task_shipment_title), _task);
                    }
                    break;
                case DOWNLOAD:
                    Attachment doc = _task.getAttachment();
                    if (doc.getId() != null) {
                        Log.v(TAG, "docid: " + doc.getId());
                        if (_task.getStatus() != null && !_task.getStatus().equals(Task.StatusEnum.COMPLETE)) {
                            try {
                                WorkordersWebApi.updateTask(App.get(), _workOrder.getId(), _task.getId(), new Task().status(Task.StatusEnum.COMPLETE), App.get().getSpUiContext());
                            } catch (Exception ex) {
                                Log.v(TAG, ex);
                            }
                        }
                        DocumentClient.downloadDocument(App.get(), doc.getId(),
                                doc.getFile().getLink(), doc.getFile().getName(), false);
                    }
                    break;
            }
        }
    };
}
