package com.fieldnation.ui.workorder.detail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.documents.DocumentClient;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.ui.SignOffActivity;
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

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class TaskRowView extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("TaskRowView");

    // Ui
    private IconFontTextView _iconView;
    private TextView _descriptionTextView;
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

        setOnClickListener(_click_listener);

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
//            updateCheckBox();
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






    private final View.OnClickListener _click_listener =new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            WorkOrderTracker.onTaskEvent(App.get(), _task.getType(), _workOrder.getId());

            updateCheckBox();

            switch (_task.getType().getId()) {

                case 1:
                    App.get().analActionTitle = null;
                    EtaDialog.show(App.get(), null, _workOrder.getId(), _workOrder.getSchedule(),
                            _workOrder.getEta(), EtaDialog.PARAM_DIALOG_TYPE_ADD);
                    break;
                case 2:
                    showClosingNotesDialog();
                    break;
                case 3: // check in
                    // TODO
                    doCheckin();

                    break;
                case 4: // check out
                    doCheckOut();
                    break;
                case 5: // upload file
                    startAppPickerDialog();
                    break;
                case 6: // upload picture
                    startAppPickerDialog();
                    break;
                case 7: // custom field
                    CustomFieldDialog.show(App.get(), null, _workOrder.getId(), _task.getCustomField());
                    break;
                case 8: // phone
                    if (_task.getStatus() != null && !_task.getStatus().equals(Task.StatusEnum.COMPLETE))
                        try {
                            WorkordersWebApi.updateTask(App.get(), _workOrder.getId(), _task.getId(), new Task().status(Task.StatusEnum.COMPLETE), App.get().getSpUiContext());
                        } catch (Exception ex) {
                            Log.v(TAG, ex);
                        }

                    try {
                        if (_task.getPhone() != null) {
                            // Todo, need to figure out if there is a phone number here
//                    Spannable test = new SpannableString(task.getPhoneNumber());
//                    Linkify.addLinks(test, Linkify.PHONE_NUMBERS);
//                    if (test.getSpans(0, test.length(), URLSpan.class).length == 0) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                        builder.setMessage(R.string.dialog_no_number_message);
//                        builder.setTitle(R.string.dialog_no_number_title);
//                        builder.setPositiveButton(R.string.btn_ok, null);
//                        builder.show();
//
//                    } else {
//                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                        String phNum = "tel:" + task.getPhoneNumber();
//                        callIntent.setData(Uri.parse(phNum));
//                        startActivity(callIntent);
//                        setLoading(true);
//                    }

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
                case 9: // email
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
                case 10: // unique task
                    if (_task.getStatus() != null && _task.getStatus().equals(Task.StatusEnum.COMPLETE))
                        return;

                    try {
                        WorkordersWebApi.updateTask(App.get(), _workOrder.getId(), _task.getId(), new Task().status(Task.StatusEnum.COMPLETE), App.get().getSpUiContext());
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
//                    setLoading(true);
                    break;
                case 11:
                    SignOffActivity.startSignOff(App.get(), _workOrder.getId(), _task.getId());
//                    setLoading(true);
                    break;
                case 12:
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
                case 13:
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