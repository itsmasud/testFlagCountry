package com.fieldnation.v2.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.analytics.AnswersWrapper;
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.analytics.contexts.SpStackContext;
import com.fieldnation.analytics.contexts.SpStatusContext;
import com.fieldnation.analytics.contexts.SpTracingContext;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fnactivityresult.ActivityResultConstants;
import com.fieldnation.fnanalytics.EventContext;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.service.data.documents.DocumentClient;
import com.fieldnation.service.data.documents.DocumentConstants;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.SignOffActivity;
import com.fieldnation.v2.data.client.AttachmentHelper;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.Shipment;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.TaskTypeEnum;
import com.fieldnation.v2.ui.TasksAdapter;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Shoaib on 09/10/17.
 */

public class TasksDialog extends FullScreenDialog {
    private static final String TAG = "TasksDialog";

    // Dialog
    private static final String DIALOG_GET_FILE = TAG + ".getFileDialog";
    private static final String DIALOG_TASK_SHIPMENT_ADD = TAG + ".taskShipmentAddDialog";


    // Params
    private static final String PARAM_WORK_ORDER_ID = "workOrderId";
    private static final String PARAM_DIALOG_TITLE = "dialogTitle";
    private static final String PARAM_GROUP_ID = "groupId";

    // Ui
    private Toolbar _toolbar;
    private OverScrollRecyclerView _list;

    // Data
    private int _workOrderId = 0;
    private WorkOrder _workOrder;
    private String _groupId;
    private String _dialogTitle;
    private final TasksAdapter _adapter = new TasksAdapter();
    private Task _currentTask;
    private String _uiUUID = null;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public TasksDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_v2_toolbar_recycle, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);

        _list = v.findViewById(R.id.list);
        _list.setItemAnimator(new DefaultItemAnimator());
        _list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        GetFileDialog.addOnFileListener(DIALOG_GET_FILE, _getFile_onFile);
        TaskShipmentAddDialog.addOnAddShipmentListener(DIALOG_TASK_SHIPMENT_ADD, _taskShipmentAddDialog_onAdd);
        TaskShipmentAddDialog.addOnDeleteListener(DIALOG_TASK_SHIPMENT_ADD, _taskShipmentAddDialog_onDelete);

        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        _list.setAdapter(_adapter);
        _adapter.setListener(_taskClick_listener);
    }

    @Override
    public void onStop() {
        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpTracingContext(new UUIDGroup(null, _uiUUID)))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.COMPLETE, "Tasks Dialog"))
                .build());

        super.onStop();
        GetFileDialog.removeOnFileListener(DIALOG_GET_FILE, _getFile_onFile);
        TaskShipmentAddDialog.removeOnAddShipmentListener(DIALOG_TASK_SHIPMENT_ADD, _taskShipmentAddDialog_onAdd);
        TaskShipmentAddDialog.removeOnDeleteListener(DIALOG_TASK_SHIPMENT_ADD, _taskShipmentAddDialog_onDelete);
    }

    @Override
    public void onResume() {
        super.onResume();

        _workOrdersApi.sub();
        _documentClient.sub();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);
        _workOrderId = params.getInt(PARAM_WORK_ORDER_ID);
        _dialogTitle = params.getString(PARAM_DIALOG_TITLE);
        _groupId = params.getString(PARAM_GROUP_ID);
        _uiUUID = params.getString("uiUUID");

        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpTracingContext(new UUIDGroup(null, _uiUUID)))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.START, "Tasks Dialog"))
                .build());

        populateUi();

        AppMessagingClient.setLoading(true);
        WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, true, false);
        populateUi();
    }

    private void populateUi() {
        if (_list == null) return;

        if (_workOrder == null) return;

        _toolbar.setTitle(_dialogTitle);

        _adapter.setData(_workOrder.getTasks(), _groupId);
    }

    @Override
    public void onPause() {
        _workOrdersApi.unsub();
        _documentClient.unsub();
        super.onPause();
    }

    public TaskTypeEnum getType(Task task) {
        return TaskTypeEnum.fromTypeId(task.getType().getId());
    }

    private boolean checkMedia() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /*-*********************************************-*/
    /*-				Closing notes Process			-*/
    /*-*********************************************-*/

    private void showClosingNotesDialog() {
        if (_workOrder.getActionsSet().contains(WorkOrder.ActionsEnum.CLOSING_NOTES))
            ClosingNotesDialog.show(App.get(), _workOrder.getId(), _workOrder.getClosingNotes());
    }

    /*-*********************************************-*/
    /*-				Check In Process				-*/
    /*-*********************************************-*/
    private void doCheckin() {
        App.get().analActionTitle = null;
        CheckInOutDialog.show(App.get(), null, _uiUUID, _workOrder.getId(),
                _workOrder.getTimeLogs(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_IN);
    }

    /*-*********************************************-*/
    /*-				Check Out Process				-*/
    /*-*********************************************-*/
    private void doCheckOut() {
        int deviceCount = -1;

        Pay pay = _workOrder.getPay();
        if (pay.getType() == Pay.TypeEnum.DEVICE
                && pay.getBase().getUnits() != null) {
            deviceCount = pay.getBase().getUnits().intValue();
        }

        App.get().analActionTitle = null;

        if (deviceCount > -1) {
            CheckInOutDialog.show(App.get(), null, _uiUUID, _workOrder.getId(),
                    _workOrder.getTimeLogs(), deviceCount, CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
        } else {
            CheckInOutDialog.show(App.get(), null, _uiUUID, _workOrder.getId(),
                    _workOrder.getTimeLogs(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
        }
    }

    private void startAppPickerDialog() {
        if (checkMedia()) {
            UUIDGroup uuid = new UUIDGroup(null, _uiUUID);

            Tracker.event(App.get(), new CustomEvent.Builder()
                    .addContext(new SpTracingContext(uuid))
                    .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                    .addContext(new SpStatusContext(SpStatusContext.Status.INFO, "Tasks Dialog, start get file"))
                    .build());

            GetFileDialog.show(App.get(), DIALOG_GET_FILE, uuid.uuid);
        } else {
            ToastClient.toast(App.get(), R.string.toast_external_storage_needed, Toast.LENGTH_LONG);
        }
    }

    /*-*********************************-*/
    /*-				Dialogs				-*/
    /*-*********************************-*/
    private final GetFileDialog.OnFileListener _getFile_onFile = new GetFileDialog.OnFileListener() {
        @Override
        public void onFile(List<GetFileDialog.UriIntent> fileResult) {
            Log.v(TAG, "onFile");
            if (fileResult.size() == 0)
                return;

            if (fileResult.size() == 1) {
                GetFileDialog.UriIntent fui = fileResult.get(0);
                if (fui.uri != null) {
                    PhotoUploadDialog.show(App.get(), null, fui.uuid, _workOrder.getId(), _currentTask, FileUtils.getFileNameFromUri(App.get(), fui.uri), fui.uri);
                } else {
                    Tracker.event(App.get(), new CustomEvent.Builder()
                            .addContext(new SpTracingContext(fui.uuid))
                            .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                            .addContext(new SpStatusContext(SpStatusContext.Status.FAIL, "Tasks Dialog, no uri"))
                            .build());
                }
                return;
            }

            for (GetFileDialog.UriIntent fui : fileResult) {
                Tracker.event(App.get(),
                        new SimpleEvent.Builder()
                                .tag(AnswersWrapper.TAG)
                                .category("AttachmentUpload")
                                .label("WorkOrderScreen - multiple")
                                .action("start")
                                .addContext(new SpTracingContext(fui.uuid))
                                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                                .addContext(new SpStatusContext(SpStatusContext.Status.INFO, "Tasks Dialog Upload"))
                                .build());

                try {
                    Attachment attachment = new Attachment();
                    attachment.folderId(_currentTask.getAttachments().getId());
                    AttachmentHelper.addAttachment(App.get(), fui.uuid, _workOrder.getId(), attachment, fui.intent);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
        }
    };

    private final TaskShipmentAddDialog.OnAddShipmentListener _taskShipmentAddDialog_onAdd = new TaskShipmentAddDialog.OnAddShipmentListener() {
        @Override
        public void onAddShipment(int workOrderId, Shipment shipment, Task task) {
            ShipmentAddDialog.show(App.get(), null, workOrderId,
                    _workOrder.getAttachments(),
                    getContext().getString(R.string.dialog_shipment_title),
                    shipment == null ? "" : shipment.getName(), task);
        }
    };

    private final TaskShipmentAddDialog.OnDeleteListener _taskShipmentAddDialog_onDelete = new TaskShipmentAddDialog.OnDeleteListener() {
        @Override
        public void onDelete(int workOrderId, Shipment shipment) {
            WorkOrderTracker.onDeleteEvent(
                    App.get(),
                    WorkOrderTracker.WorkOrderDetailsSection.SHIPMENTS,
                    new EventContext[]{
                            new SpTracingContext(new UUIDGroup(null, _uiUUID)),
                            new SpStackContext(DebugUtils.getStackTraceElement()),
                            new SpStatusContext(SpStatusContext.Status.INFO, "Tasks Dialog")
                    }
            );

            SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
            uiContext.page += " - Task Shipment Add Dialog";
            WorkordersWebApi.deleteShipment(App.get(), workOrderId, shipment.getId(), uiContext);
        }
    };

    /*-*****************************-*/
    /*-		      Events			-*/
    /*-*****************************-*/

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss(true);
        }
    };

    private final TasksAdapter.Listener _taskClick_listener = new TasksAdapter.Listener() {
        @Override
        public void onTaskClick(View view, Task task) {
            WorkOrderTracker.onTaskEvent(
                    App.get(),
                    task.getType(),
                    _workOrder.getId(),
                    new EventContext[]{
                            new SpTracingContext(new UUIDGroup(null, _uiUUID)),
                            new SpStackContext(DebugUtils.getStackTraceElement()),
                            new SpStatusContext(SpStatusContext.Status.INFO, "Tasks Dialog")
                    });

            switch (getType(task)) {

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
                    _currentTask = task;
                    startAppPickerDialog();
                    break;

                case UPLOAD_PICTURE: // upload picture
                    _currentTask = task;
                    startAppPickerDialog();
                    break;

                case CUSTOM_FIELD: // custom field
                    if (task.getCustomField().getId() == null) {
                        break;
                    }
                    CustomFieldDialog.show(App.get(), null, _workOrder.getId(), task.getCustomField());
                    break;

                case PHONE: // phone
                    try {
                        WorkordersWebApi.updateTask(App.get(), _workOrder.getId(), task.getId(), new Task().status(Task.StatusEnum.COMPLETE), App.get().getSpUiContext());
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }

                    try {
                        if (task.getPhone() != null) {
                            if (!TextUtils.isEmpty(task.getPhone()) && android.util.Patterns.PHONE.matcher(task.getPhone()).matches()) {
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                String phNum = "tel:" + task.getPhone();
                                callIntent.setData(Uri.parse(phNum));
                                ActivityClient.startActivity(callIntent);
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
                    String email = task.getEmail();
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + email));
                    ActivityClient.startActivityForResult(intent, ActivityResultConstants.RESULT_CODE_SEND_EMAIL);

                    try {
                        WorkordersWebApi.updateTask(App.get(), _workOrder.getId(), task.getId(), new Task().status(Task.StatusEnum.COMPLETE), App.get().getSpUiContext());
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                    break;

                case UNIQUE_TASK: // unique task
                    if (task.getStatus() != null && task.getStatus().equals(Task.StatusEnum.COMPLETE))
                        return;

                    try {
                        AppMessagingClient.setLoading(true);
                        WorkordersWebApi.updateTask(App.get(), _workOrder.getId(), task.getId(), new Task().status(Task.StatusEnum.COMPLETE), App.get().getSpUiContext());
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                    break;

                case SIGNATURE: // signature
                    SignOffActivity.startSignOff(App.get(), _workOrder.getId(), task.getId());
                    break;

                case SHIPMENT: // shipment
                    List<Shipment> shipments = new LinkedList();
                    for (Shipment shipment : _workOrder.getShipments().getResults()) {
                        if (shipment.getDirection().equals(Shipment.DirectionEnum.FROM_SITE))
                            shipments.add(shipment);
                    }

                    if (shipments.size() == 0) {
                        ShipmentAddDialog.show(App.get(), null, _workOrder.getId(),
                                _workOrder.getAttachments(), App.get().getString(R.string.dialog_task_shipment_title), null, task);
                    } else {
                        TaskShipmentAddDialog.show(App.get(), DIALOG_TASK_SHIPMENT_ADD, _workOrder.getId(),
                                _workOrder.getShipments(), App.get().getString(R.string.dialog_task_shipment_title), task);
                    }
                    break;

                case DOWNLOAD:
                    Attachment attachment = task.getAttachment();
                    if (attachment.getId() != null) {
                        Log.v(TAG, "attachmentid: " + attachment.getId());
                        if (task.getStatus() != null && !task.getStatus().equals(Task.StatusEnum.COMPLETE)) {
                            try {
                                WorkordersWebApi.updateTask(App.get(), _workOrder.getId(), task.getId(), new Task().status(Task.StatusEnum.COMPLETE), App.get().getSpUiContext());
                            } catch (Exception ex) {
                                Log.v(TAG, ex);
                            }
                        }
                        _adapter.downloadStart(attachment.getId());

                        DocumentClient.downloadDocument(App.get(), attachment.getId(),
                                attachment.getFile().getLink(), attachment.getFile().getName(), false);
                    }
                    break;
            }
        }
    };

    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.toLowerCase().contains("attachment")
                    || methodName.equals("getWorkOrder")
                    || methodName.equals("updateTask");
        }

        @Override
        public void onQueued(TransactionParams transactionParams, String methodName) {
            Log.v(TAG, "WorkordersWebApi.onQueued");

            if (!methodName.equals("addAttachment"))
                return;

            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");
                _adapter.uploadStart(transactionParams);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            populateUi();
        }

        @Override
        public void onStart(TransactionParams transactionParams, String methodName) {
            Log.v(TAG, "WorkordersWebApi.onStart");
            if (!methodName.equals("addAttachment"))
                return;

            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");
                _adapter.uploadProgress(transactionParams, 0);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            populateUi();
        }

        @Override
        public void onPaused(TransactionParams transactionParams, String methodName) {
            Log.v(TAG, "WorkordersWebApi.onPaused");
            if (!methodName.equals("addAttachment"))
                return;

            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");
                _adapter.uploadProgress(transactionParams, -1);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            populateUi();
        }

        @Override
        public void onProgress(TransactionParams transactionParams, String methodName, long pos, long size, long time) {
            Log.v(TAG, "WorkordersWebApi.onProgress");
            if (!methodName.equals("addAttachment"))
                return;

            try {
                JsonObject obj = new JsonObject(transactionParams.methodParams);
                String name = obj.getString("attachment.file.name");
                int folderId = obj.getInt("attachment.folder_id");

                Double percent = pos * 1.0 / size;
                Log.v(TAG, "onProgress(" + folderId + "," + name + "," + (pos * 100 / size) + "," + (int) (time / percent));

                if (pos == size) {
                    AppMessagingClient.setLoading(true);
                    _adapter.uploadStop(transactionParams);
                    populateUi();
                } else {
                    _adapter.uploadProgress(transactionParams, (int) (pos * 100 / size));
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            populateUi();
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            Log.v(TAG, "WorkordersWebApi.onComplete");

            if (successObject != null && (methodName.equals("getWorkOrder"))) {
                WorkOrder workOrder = (WorkOrder) successObject;
                if (success) {
                    _workOrder = workOrder;
                    populateUi();
                    AppMessagingClient.setLoading(false);
                }

            } else if (methodName.equals("updateTask")) {
                WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, true, false);
                AppMessagingClient.setLoading(true);

            } else if (methodName.equals("addAttachment")) {
                try {
                    JsonObject obj = new JsonObject(transactionParams.methodParams);
                    String name = obj.getString("attachment.file.name");
                    int folderId = obj.getInt("attachment.folder_id");
                    _adapter.uploadStop(transactionParams);
                    AppMessagingClient.setLoading(true);
                    WorkordersWebApi.getAttachments(App.get(), _workOrderId, false, false);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }

            } else if (successObject != null && methodName.equals("getAttachments")) {
//                folders = (AttachmentFolders) successObject;
                populateUi();
                AppMessagingClient.setLoading(false);
            }
        }
    };

    private final DocumentClient _documentClient = new DocumentClient() {
        @Override
        public boolean processDownload(long documentId) {
            return true;
        }

        @Override
        public void onDownload(long documentId, File file, int state) {
            Log.v(TAG, "DocumentClient.onDownload");
            if (file == null || state == DocumentConstants.PARAM_STATE_START) {
                if (state == DocumentConstants.PARAM_STATE_FINISH)
                    ToastClient.toast(App.get(), R.string.could_not_download_file, Toast.LENGTH_SHORT);
                return;
            }
            _adapter.downloadComplete((int) documentId);
        }
    };

    /**
     * @param context
     * @param uid
     * @param workOrderId
     * @param groupId
     * @param dialogTitle
     */
    public static void show(Context context, String uid, String uiUUID, int workOrderId, String groupId, String dialogTitle) {
        Bundle params = new Bundle();
        params.putInt(PARAM_WORK_ORDER_ID, workOrderId);
        params.putString(PARAM_GROUP_ID, groupId);
        params.putString(PARAM_DIALOG_TITLE, dialogTitle);
        params.putString("uiUUID", uiUUID);

        Controller.show(context, uid, TasksDialog.class, params);
    }

}
