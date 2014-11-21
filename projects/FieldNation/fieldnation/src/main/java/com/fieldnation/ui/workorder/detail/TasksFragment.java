package com.fieldnation.ui.workorder.detail;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.FileHelper;
import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.CustomField;
import com.fieldnation.data.workorder.Document;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.ShipmentTracking;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.AppPickerPackage;
import com.fieldnation.ui.GPSLocationService;
import com.fieldnation.ui.SignatureActivity;
import com.fieldnation.ui.dialog.AppPickerDialog;
import com.fieldnation.ui.dialog.ClosingNotesDialog;
import com.fieldnation.ui.dialog.ConfirmDialog;
import com.fieldnation.ui.dialog.CustomFieldDialog;
import com.fieldnation.ui.dialog.DeviceCountDialog;
import com.fieldnation.ui.dialog.ShipmentAddDialog;
import com.fieldnation.ui.dialog.TaskShipmentAddDialog;
import com.fieldnation.ui.dialog.TermsDialog;
import com.fieldnation.ui.dialog.WorkLogDialog;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


public class TasksFragment extends WorkorderFragment {
    private static final String TAG = "ui.workorder.detail.TasksFragment";

    // Activity result codes
    private static final int RESULT_CODE_BASE = 200;
    private static final int RESULT_CODE_SEND_EMAIL = RESULT_CODE_BASE + 1;
    private static final int RESULT_CODE_SIGNATURE = RESULT_CODE_BASE + 2;
    private static final int RESULT_CODE_GET_ATTACHMENT = RESULT_CODE_BASE + 3;
    private static final int RESULT_CODE_GET_CAMERA_PIC = RESULT_CODE_BASE + 4;

    // tags to put in signature to link the signature back to me
    private static final String SIGNATURE_WORKORDERID = "ui.workorder.detail.TasksFragment:SIGNATURE_WORKORDERID";
    private static final String SIGNATURE_TASKID = "ui.workorder.detail.TasksFragment:SIGNATURE_TASKID";

    // Web request result codes
    private static final int WEB_CHANGED = 1;
    private static final int WEB_GET_TASKS = 2;
    private static final int WEB_SEND_DELIVERABLE = 3;

    // saved state keys
    private static final String STATE_WORKORDER = "ui.workorder.detail.TasksFragment:STATE_WORKORDER";
    private static final String STATE_AUTHTOKEN = "ui.workorder.detail.TasksFragment:STATE_AUTHTOKEN";
    private static final String STATE_USERNAME = "ui.workorder.detail.TasksFragment:STATE_USERNAME";
    private static final String STATE_TASKS = "ui.workorder.detail.TasksFragment:STATE_TASKS";
    private static final String STATE_CURRENT_TASK = "ui.workorder.detail.TasksFragment:STATE_CURRENT_TASK";

    // UI
    private ActionBarTopView _topBar;
    private TimeLoggedView _timeLogged;
    private TaskListView _taskList;
    private CustomFieldListView _customFields;
    private ShipmentView _shipments;
    private ClosingNotesView _closingNotes;

    // Dialogs
    private ClosingNotesDialog _closingDialog;
    private TaskShipmentAddDialog _taskShipmentAddDialog;
    private ShipmentAddDialog _shipmentAddDialog;
    private AppPickerDialog _appDialog;
    private ConfirmDialog _confirmDialog;
    private DeviceCountDialog _deviceCountDialog;
    private CustomFieldDialog _customFieldDialog;
    private WorkLogDialog _worklogDialog;
    private TermsDialog _termsDialog;

    // Data
    private GlobalState _gs;
    private WorkorderService _service;

    private String _authToken;
    private String _username;
    private Workorder _workorder;
    private List<Task> _tasks = null;
    private Task _currentTask;
    private SecureRandom _rand = new SecureRandom();
    private GPSLocationService _gPSLocationService;

    /*-*************************************-*/
    /*-				LifeCycle				-*/
    /*-*************************************-*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workorder_tasks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(TAG, "TasksFragment onViewCreated");

        _gs = (GlobalState) view.getContext().getApplicationContext();

        _shipments = (ShipmentView) view.findViewById(R.id.shipment_view);
        _shipments.setListener(_shipments_listener);

        _taskList = (TaskListView) view.findViewById(R.id.scope_view);
        _taskList.setTaskListViewListener(_taskListView_listener);

        _timeLogged = (TimeLoggedView) view.findViewById(R.id.timelogged_view);
        _timeLogged.setListener(_timeLoggedView_listener);

        _closingNotes = (ClosingNotesView) view.findViewById(R.id.closingnotes_view);
        _closingNotes.setListener(_clockingNotesView_listener);

        _topBar = (ActionBarTopView) view.findViewById(R.id.topaction_view);
        _topBar.setListener(_actionBarTop_listener);

        _customFields = (CustomFieldListView) view.findViewById(R.id.customfields_view);
        _customFields.setListener(_customFields_listener);

        _closingDialog = ClosingNotesDialog.getInstance(getFragmentManager(), TAG);
        _closingDialog.setListener(_closingNotes_onOk);

        _deviceCountDialog = DeviceCountDialog.getInstance(getFragmentManager(), TAG);
        _deviceCountDialog.setListener(_deviceCountListener);

        _customFieldDialog = CustomFieldDialog.getInstance(getFragmentManager(), TAG);
        _customFieldDialog.setListener(_customFieldDialog_listener);

        _appDialog = AppPickerDialog.getInstance(getFragmentManager(), TAG);
        _appDialog.setListener(_appdialog_listener);

        _taskShipmentAddDialog = TaskShipmentAddDialog.getInstance(getFragmentManager(), TAG);
        _taskShipmentAddDialog.setListener(taskShipmentAddDialog_listener);

        _shipmentAddDialog = ShipmentAddDialog.getInstance(getFragmentManager(), TAG);
        _shipmentAddDialog.setListener(_shipmentAddDialog_listener);

        _confirmDialog = ConfirmDialog.getInstance(getFragmentManager(), TAG);
        _confirmDialog.setListener(_confirmDialog_listener);

        _worklogDialog = WorkLogDialog.getInstance(getFragmentManager(), TAG);
        _worklogDialog.setListener(_worklogDialog_listener);

        _termsDialog = TermsDialog.getInstance(getFragmentManager(), TAG);

        if (savedInstanceState == null) {
            _gs.requestAuthentication(_authClient);
        } else {
            if (savedInstanceState.containsKey(STATE_WORKORDER)) {
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);
            }
            if (savedInstanceState.containsKey(STATE_AUTHTOKEN)) {
                _authToken = savedInstanceState.getString(STATE_AUTHTOKEN);
            }
            if (savedInstanceState.containsKey(STATE_USERNAME)) {
                _username = savedInstanceState.getString(STATE_USERNAME);
            }
            if (savedInstanceState.containsKey(STATE_TASKS)) {
                Parcelable[] tasks = savedInstanceState.getParcelableArray(STATE_TASKS);
                _tasks = new LinkedList<Task>();
                for (int i = 0; i < tasks.length; i++) {
                    _tasks.add((Task) tasks[i]);
                }
                _taskList.setData(_workorder, _tasks);
            }
            if (savedInstanceState.containsKey(STATE_CURRENT_TASK)) {
                _currentTask = savedInstanceState.getParcelable(STATE_CURRENT_TASK);
            }
            if (_authToken != null && _username != null) {
                _service = new WorkorderService(view.getContext(), _username, _authToken, _resultReceiver);
            } else {
                _gs.requestAuthentication(_authClient);
            }
        }

        _gPSLocationService = new GPSLocationService(getActivity());
        // GPS settings dialog should only be displayed if the GPS is failing
        if (_gPSLocationService.isGooglePlayServicesAvailable() && !_gPSLocationService.isGpsEnabled()) {
            _gPSLocationService.showSettingsAlert(view.getContext());
        }

        configureUi();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        _appDialog.addIntent(getActivity().getPackageManager(), intent, "Get Content");

        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            _appDialog.addIntent(getActivity().getPackageManager(), intent, "Take Picture");

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_authToken != null) {
            outState.putString(STATE_AUTHTOKEN, _authToken);
        }
        if (_username != null) {
            outState.putString(STATE_USERNAME, _username);
        }
        if (_workorder != null) {
            outState.putParcelable(STATE_WORKORDER, _workorder);
        }
        if (_tasks != null && _tasks.size() > 0) {
            Task[] tasks = new Task[_tasks.size()];
            for (int i = 0; i < _tasks.size(); i++) {
                tasks[i] = _tasks.get(i);
            }
            outState.putParcelableArray(STATE_TASKS, tasks);
        }

        if (_currentTask != null) {
            outState.putParcelable(STATE_CURRENT_TASK, _currentTask);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void update() {
        configureUi();
    }

    @Override
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        configureUi();
        requestData();
    }

    private void configureUi() {
        if (_workorder == null)
            return;

        if (_shipments != null && _timeLogged != null) {
            WorkorderStatus status = _workorder.getStatus().getWorkorderStatus();
            if (status.ordinal() < WorkorderStatus.ASSIGNED.ordinal()) {
                _timeLogged.setVisibility(View.GONE);
                _shipments.setVisibility(View.GONE);
                _closingNotes.setVisibility(View.GONE);
            } else {
                _shipments.setVisibility(View.VISIBLE);
                _timeLogged.setVisibility(View.VISIBLE);
                _closingNotes.setVisibility(View.VISIBLE);
            }
        }

        if (_shipments != null)
            _shipments.setWorkorder(_workorder);

        if (_timeLogged != null)
            _timeLogged.setWorkorder(_workorder);

        if (_closingNotes != null)
            _closingNotes.setWorkorder(_workorder);


        if (_topBar != null)
            _topBar.setWorkorder(_workorder);

        if (_customFields != null) {
            _customFields.setData(_workorder, _workorder.getCustomFields());
        }
        setLoading(false);
    }

    private void requestData() {
        if (_service == null)
            return;

        if (_workorder == null)
            return;

        _gs.startService(_service.getTasks(WEB_GET_TASKS, _workorder.getWorkorderId(), false));
        setLoading(true);
    }

    private PendingIntent getNotificationIntent() {
        Intent intent = new Intent(_gs, WorkorderActivity.class);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB,
                WorkorderActivity.TAB_TASKS);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID,
                _workorder.getWorkorderId());

        return PendingIntent.getActivity(_gs, _rand.nextInt(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult() resultCode= " + resultCode);

        if (requestCode == RESULT_CODE_SIGNATURE && resultCode == Activity.RESULT_OK) {
            byte[] json_vector = data.getExtras().getByteArray(SignatureActivity.INTENT_KEY_BITMAP);
            String name = data.getExtras().getString(SignatureActivity.INTENT_KEY_NAME);
            String arrival = data.getExtras().getString(SignatureActivity.INTENT_KEY_ARRIVAL);
            String depart = data.getExtras().getString(SignatureActivity.INTENT_KEY_DEPARTURE);
            int taskid = data.getExtras().getInt(SIGNATURE_TASKID);
            long workorderid = data.getExtras().getLong(SIGNATURE_WORKORDERID);

            getActivity().startService(
                    _service.completeSignatureTask(resultCode, workorderid, taskid, arrival, depart, name, json_vector));
            setLoading(true);
        } else if (requestCode == RESULT_CODE_GET_ATTACHMENT || requestCode == RESULT_CODE_GET_CAMERA_PIC) {
            _gs.startService(_service.uploadDeliverable(
                    WEB_SEND_DELIVERABLE, _workorder.getWorkorderId(),
                    _currentTask.getSlotId(), data, getNotificationIntent()));
            // todo notify task view that the file is uploading

        }
    }

    private void showClosingNotesDialog() {
        _closingDialog.show(_workorder.getClosingNotes());
    }

    /*-*************************************-*/
    /*-				UI Events				-*/
    /*-*************************************-*/

    private WorkLogDialog.Listener _worklogDialog_listener = new WorkLogDialog.Listener() {
        @Override
        public void onOk(LoggedWork loggedWork, Calendar start, Calendar end, int deviceCount) {
            if (loggedWork == null) {
                if (deviceCount <= 0) {
                    getActivity().startService(_service.logTime(WEB_CHANGED, _workorder.getWorkorderId(), start.getTimeInMillis(),
                            end.getTimeInMillis()));
                } else {
                    getActivity().startService(_service.logTime(WEB_CHANGED, _workorder.getWorkorderId(), start.getTimeInMillis(),
                            end.getTimeInMillis(), deviceCount));
                }
            } else {
                if (deviceCount <= 0) {
                    getActivity().startService(
                            _service.updateLogTime(WEB_CHANGED, _workorder.getWorkorderId(),
                                    loggedWork.getLoggedHoursId(), start.getTimeInMillis(), end.getTimeInMillis()));
                } else {
                    getActivity().startService(
                            _service.updateLogTime(WEB_CHANGED, _workorder.getWorkorderId(),
                                    loggedWork.getLoggedHoursId(), start.getTimeInMillis(), end.getTimeInMillis(), deviceCount));
                }
            }
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }
    };

    private TimeLoggedView.Listener _timeLoggedView_listener = new TimeLoggedView.Listener() {
        @Override
        public void addWorklog(boolean showdevice) {
            _worklogDialog.show("Add Worklog", null, showdevice);
        }

        @Override
        public void editWorklog(Workorder workorder, LoggedWork loggedWork, boolean showDeviceCount) {
            _worklogDialog.show("Add Worklog", loggedWork, showDeviceCount);
        }
    };

    /*-************************************-*/
    /*-         ActionBarTopView           -*/
    /*-************************************-*/

    private ActionBarTopView.Listener _actionBarTop_listener = new ActionBarTopView.Listener() {
        @Override
        public void onComplete() {
            getActivity().startService(_service.complete(WEB_CHANGED, _workorder.getWorkorderId()));
            setLoading(true);
        }

        @Override
        public void onCheckOut() {
            Pay pay = _workorder.getPay();
            if (pay != null && pay.isPerDeviceRate()) {
                _deviceCountDialog.show(_workorder, pay.getMaxDevice());
            } else {
                if (_gPSLocationService.isGooglePlayServicesAvailable() && _gPSLocationService.isLocationServiceEnabled() && _gPSLocationService.isGpsEnabled()) {
                    try {
                        Location location = _gPSLocationService.getLocation();
                        double lat = location.getLatitude();
                        double log = location.getLongitude();
                        getActivity().startService(_service.checkout(WEB_CHANGED, _workorder.getWorkorderId(), lat, log));
                        setLoading(true);
                    } catch (Exception e) {
                        _gPSLocationService.showSettingsOffAlert(getView().getContext());
                    }
                } else {
                    _gPSLocationService.showCheckInOutAlert(getView().getContext());
                    getActivity().startService(
                            _service.checkout(WEB_CHANGED, _workorder.getWorkorderId()));
                    setLoading(true);
                }
            }
        }

        @Override
        public void onCheckIn() {
            if (_gPSLocationService.isGooglePlayServicesAvailable() && _gPSLocationService.isLocationServiceEnabled() && _gPSLocationService.isGpsEnabled()) {
                try {
                    Location location = _gPSLocationService.getLocation();
                    double lat = location.getLatitude();
                    double log = location.getLongitude();
                    getActivity().startService(_service.checkin(WEB_CHANGED, _workorder.getWorkorderId(), lat, log));
                    setLoading(true);
                } catch (Exception e) {
                    _gPSLocationService.showSettingsOffAlert(getView().getContext());
                }
            } else {
                _gPSLocationService.showCheckInOutAlert(getView().getContext());
                getActivity().startService(
                        _service.checkin(WEB_CHANGED, _workorder.getWorkorderId()));
                setLoading(true);
            }
        }

        @Override
        public void onAcknowledge() {
            getActivity().startService(_service.acknowledgeHold(WEB_CHANGED, _workorder.getWorkorderId()));
            setLoading(true);
        }

        @Override
        public void onConfirm() {
            _confirmDialog.show(_workorder, _workorder.getSchedule());
        }

        @Override
        public void onEnterClosingNotes() {
            showClosingNotesDialog();
        }
    };


    /*-*******************************-*/
    /*-         Time Logged           -*/
    /*-*******************************-*/

    /*-*************************-*/
    /*-         Tasks           -*/
    /*-*************************-*/
    private TaskListView.Listener _taskListView_listener = new TaskListView.Listener() {
        @Override
        public void onTaskClick(Task task) {
            switch (task.getTaskType()) {
                case CHECKIN:
                    if (_gPSLocationService.isGooglePlayServicesAvailable() && _gPSLocationService.isLocationServiceEnabled() && _gPSLocationService.isGpsEnabled()) {
                        try {
                            Location location = _gPSLocationService.getLocation();
                            double lat = location.getLatitude();
                            double log = location.getLongitude();
                            getActivity().startService(_service.checkin(WEB_CHANGED, _workorder.getWorkorderId(), lat, log));
                            setLoading(true);
                        } catch (Exception e) {
                            _gPSLocationService.showSettingsOffAlert(getView().getContext());
                        }
                    } else {
                        _gPSLocationService.showCheckInOutAlert(getView().getContext());
                        getActivity().startService(_service.checkin(WEB_CHANGED, _workorder.getWorkorderId()));
                        setLoading(true);
                    }

                    break;
                case CHECKOUT:
                    Pay pay = _workorder.getPay();
                    if (pay != null && pay.isPerDeviceRate()) {
                        _deviceCountDialog.show(_workorder, pay.getMaxDevice());
                    } else {
                        if (_gPSLocationService.isGooglePlayServicesAvailable() && _gPSLocationService.isLocationServiceEnabled() && _gPSLocationService.isGpsEnabled()) {
                            try {
                                Location location = _gPSLocationService.getLocation();
                                double lat = location.getLatitude();
                                double log = location.getLongitude();
                                getActivity().startService(_service.checkout(WEB_CHANGED, _workorder.getWorkorderId(), lat, log));
                                setLoading(true);
                            } catch (Exception e) {
                                _gPSLocationService.showSettingsOffAlert(getView().getContext());
                            }
                        } else {
                            _gPSLocationService.showCheckInOutAlert(getView().getContext());
                            getActivity().startService(
                                    _service.checkout(WEB_CHANGED, _workorder.getWorkorderId()));
                            setLoading(true);
                        }
                    }
                    break;
                case CLOSE_OUT_NOTES:
                    showClosingNotesDialog();
                    break;
                case CONFIRM_ASSIGNMENT:
                    _confirmDialog.show(_workorder, _workorder.getSchedule());
                    break;
                case CUSTOM_FIELD:
                    if (task.getCompleted())
                        return;
                    // TODO, get custom field info, preset dialog
                    break;
                case DOWNLOAD:
                    Integer _identifier = task.getIdentifier();
                    Log.v(TAG, "_identifier: " + _identifier);
                    Document[] docs = _workorder.getDocuments();
                    if (docs != null && docs.length > 0) {
                        for (int i = 0; i < docs.length; i++) {
                            Document doc = docs[i];
                            Log.v(TAG, "docid: " + doc.getDocumentId());
                            if (doc.getDocumentId().equals(_identifier)) {
                                // task completed here
                                if (!task.getCompleted()) {
                                    getActivity().startService(
                                            _service.completeTask(WEB_CHANGED, _workorder.getWorkorderId(),
                                                    task.getTaskId()));
                                    setLoading(true);
                                }

                                FileHelper.viewOrDownloadFile(getActivity(), doc.getFilePath(),
                                        doc.getFileName(), doc.getFileType());

                                break;
                            }
                        } // end for
                    }

                    break;
                case EMAIL: {
                    String email = task.getEmailAddress();
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + email));
                    startActivityForResult(intent, RESULT_CODE_SEND_EMAIL);

                    if (!task.getCompleted()) {
                        getActivity().startService(
                                _service.completeTask(WEB_CHANGED, _workorder.getWorkorderId(), task.getTaskId()));
                        setLoading(true);
                    }
                    break;
                }
                case PHONE:
                    try {
                        if (task.getPhoneNumber() != null) {
                            if (!task.getCompleted()) {
                                getActivity().startService(
                                        _service.completeTask(WEB_CHANGED, _workorder.getWorkorderId(), task.getTaskId()));
                                setLoading(true);
                            }

                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            String phNum = "tel:" + task.getPhoneNumber();
                            callIntent.setData(Uri.parse(phNum));
                            startActivity(callIntent);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    break;
                case SHIPMENT_TRACKING:
                    ShipmentTracking[] shipments = _workorder.getShipmentTracking();
                    if (shipments == null) {
                        _shipmentAddDialog.show(R.string.add_shipment, task.getTaskId());
                    } else {
                        _taskShipmentAddDialog.show("Assign/Add New", _workorder, task.getTaskId());
                    }

                    break;
                case SIGNATURE: {
                    _currentTask = task;
                    Intent intent = new Intent(getActivity(), SignatureActivity.class);
                    try {
                        if (!misc.isEmptyOrNull(_workorder.getCheckInOutInfo().getCheckInTime()))
                            intent.putExtra(SignatureActivity.INTENT_KEY_ARRIVAL,
                                    _workorder.getCheckInOutInfo().getCheckInTime());
                    } catch (Exception ex) {
                    }
                    try {
                        if (!misc.isEmptyOrNull(_workorder.getCheckInOutInfo().getCheckOutTime()))
                            intent.putExtra(SignatureActivity.INTENT_KEY_DEPARTURE,
                                    _workorder.getCheckInOutInfo().getCheckOutTime());
                    } catch (Exception ex) {
                    }
                    try {
                        if (!misc.isEmptyOrNull(task.getDescription())) {
                            intent.putExtra(SignatureActivity.INTENT_KEY_NAME, task.getDescription());
                        }
                    } catch (Exception ex) {
                    }

                    intent.putExtra(SIGNATURE_TASKID, task.getTaskId());
                    intent.putExtra(SIGNATURE_WORKORDERID, _workorder.getWorkorderId());

                    startActivityForResult(intent, RESULT_CODE_SIGNATURE);
                    break;
                }
                case UPLOAD_FILE: {
                    _currentTask = task;
                    _appDialog.show();
                    break;
                }
                case UPLOAD_PICTURE: {
                    _currentTask = task;
                    _appDialog.show();
                    break;
                }
                case UNIQUE_TASK:
                    if (task.getCompleted())
                        return;
                    getActivity().startService(
                            _service.completeTask(WEB_CHANGED, _workorder.getWorkorderId(), task.getTaskId()));
                    setLoading(true);
                    break;
                default:
                    break;
            }
        }
    };

    private TaskShipmentAddDialog.Listener taskShipmentAddDialog_listener = new TaskShipmentAddDialog.Listener() {
        @Override
        public void onDelete(Workorder workorder, int shipmentId) {
            getActivity().startService(_service.deleteShipment(WEB_CHANGED, workorder.getWorkorderId(), shipmentId));
            setLoading(true);
        }

        @Override
        public void onAssign(Workorder workorder, int shipmentId, long taskId) {
            // @TODO
            Log.v(TAG, "Method Stub: onAssign()" + shipmentId + "=" + taskId);
            getActivity().startService(
                    _service.completeShipmentTask(WEB_CHANGED, workorder.getWorkorderId(), shipmentId, taskId));
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onAddShipmentDetails(Workorder workorder, String description, boolean shipToSite, String carrier,
                                         String trackingId) {
            getActivity().startService(
                    _service.addShipmentDetails(WEB_CHANGED, workorder.getWorkorderId(), description, shipToSite,
                            carrier, null, trackingId));
            setLoading(true);
        }

        @Override
        public void onAddShipmentDetails(Workorder workorder, String description, boolean shipToSite, String carrier,
                                         String trackingId, long taskId) {
            getActivity().startService(
                    _service.addShipmentDetails(WEB_CHANGED, workorder.getWorkorderId(), description, shipToSite,
                            carrier, null, trackingId, taskId));
            setLoading(true);
        }
    };

    /*-*********************************-*/
    /*-         Custom Fields           -*/
    /*-*********************************-*/
    private CustomFieldRowView.Listener _customFields_listener = new CustomFieldRowView.Listener() {
        @Override
        public void onClick(CustomFieldRowView view, CustomField field) {
            _customFieldDialog.show(field);
        }
    };

    private CustomFieldDialog.Listener _customFieldDialog_listener = new CustomFieldDialog.Listener() {
        @Override
        public void onOk(CustomField field, String value) {
            getActivity().startService(
                    _service.setCustomField(WEB_CHANGED, _workorder.getWorkorderId(), field.getCustomLabelId(), value));
            setLoading(true);
        }
    };

    /*-*****************************-*/
    /*-         Shipments           -*/
    /*-*****************************-*/
    private ShipmentAddDialog.Listener _shipmentAddDialog_listener = new ShipmentAddDialog.Listener() {
        @Override
        public void onOk(String trackingId, String carrier, String description, boolean shipToSite) {

        }

        @Override
        public void onOk(String trackingId, String carrier, String description, boolean shipToSite, long taskId) {
            getActivity().startService(
                    _service.addShipmentDetails(WEB_CHANGED, _workorder.getWorkorderId(), description, shipToSite,
                            carrier, null, trackingId, taskId));
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }
    };

    private ShipmentView.Listener _shipments_listener = new ShipmentView.Listener() {

        @Override
        public void addShipment() {
            _shipmentAddDialog.show("Add Shipment", 0);
        }

        @Override
        public void onDelete(Workorder workorder, int shipmentId) {
            getActivity().startService(_service.deleteShipment(WEB_CHANGED, workorder.getWorkorderId(), shipmentId));
            setLoading(true);
        }

        @Override
        public void onAssign(Workorder workorder, int shipmentId) {
            // TODO STUB .onAssign()
            Log.v(TAG, "STUB .onAssign()");
        }


//        @Override
//        public void onAddShipmentDetails(Workorder workorder, String description, boolean shipToSite, String carrier,
//                                         String trackingId) {
//            getActivity().startService(
//                    _service.addShipmentDetails(WEB_CHANGED, workorder.getWorkorderId(), description, shipToSite,
//                            carrier, null, trackingId));
//            Log.v(TAG, "Method Stub: onAddShipmentDetails()");
//        }
//
//        @Override
//        public void onAddShipmentDetails(Workorder workorder, String description, boolean shipToSite, String carrier,
//                                         String trackingId, long taskId) {
//            getActivity().startService(
//                    _service.addShipmentDetails(WEB_CHANGED, workorder.getWorkorderId(), description, shipToSite,
//                            carrier, null, trackingId, taskId));
//        }
    };

    /*-*********************************-*/
    /*-         Closing Notes           -*/
    /*-*********************************-*/
    private ClosingNotesDialog.Listener _closingNotes_onOk = new ClosingNotesDialog.Listener() {
        @Override
        public void onOk(String message) {
            Log.v(TAG, "On Ok");
            getActivity().startService(
                    _service.closingNotes(WEB_CHANGED, _workorder.getWorkorderId(), message));
            setLoading(true);

        }

        @Override
        public void onCancel() {
        }
    };

    private ClosingNotesView.Listener _clockingNotesView_listener = new ClosingNotesView.Listener() {
        @Override
        public void onChangeClosingNotes(String closingNotes) {
            showClosingNotesDialog();
        }
    };

    /*-*****************************-*/
    /*-         MISC Events         -*/
    /*-*****************************-*/

    private AppPickerDialog.Listener _appdialog_listener = new AppPickerDialog.Listener() {

        @Override
        public void onClick(AppPickerPackage pack) {
            Intent src = pack.intent;

            ResolveInfo info = pack.resolveInfo;

            src.setComponent(new ComponentName(
                    info.activityInfo.applicationInfo.packageName,
                    info.activityInfo.name));

            if (src.getAction().equals(Intent.ACTION_GET_CONTENT)) {
                startActivityForResult(src, RESULT_CODE_GET_ATTACHMENT);
            } else {
                startActivityForResult(src, RESULT_CODE_GET_CAMERA_PIC);
            }
        }
    };


    private ConfirmDialog.Listener _confirmDialog_listener = new ConfirmDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String startDate, long durationMilliseconds) {
            try {
                long end = durationMilliseconds + ISO8601.toUtc(startDate);
                Intent intent = _service.confirmAssignment(WEB_CHANGED, _workorder.getWorkorderId(),
                        startDate, ISO8601.fromUTC(end));
                getActivity().startService(intent);
                setLoading(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onCancel(Workorder workorder) {
        }

        @Override
        public void termsOnClick(Workorder workorder) {
            _termsDialog.show();
            // TODO STUB .termsOnClick()
            Log.v(TAG, "STUB .termsOnClick()");

        }
    };

    private DeviceCountDialog.Listener _deviceCountListener = new DeviceCountDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, int count) {
            if (_gPSLocationService.isGooglePlayServicesAvailable() && _gPSLocationService.isLocationServiceEnabled() && _gPSLocationService.isGpsEnabled()) {
                try {
                    Location location = _gPSLocationService.getLocation();
                    double lat = location.getLatitude();
                    double log = location.getLongitude();
                    getActivity().startService(_service.checkout(WEB_CHANGED, _workorder.getWorkorderId(), count, lat, log));
                    setLoading(true);
                } catch (Exception e) {
                    _gPSLocationService.showSettingsOffAlert(getView().getContext());
                }

            } else {
                _gPSLocationService.showCheckInOutAlert(getView().getContext());
                getActivity().startService(
                        _service.checkout(WEB_CHANGED, _workorder.getWorkorderId(), count));
                setLoading(true);
            }
        }
    };

    /*-*************************************-*/
    /*-				WEB Events				-*/
    /*-*************************************-*/

    private AuthenticationClient _authClient = new AuthenticationClient() {
        @Override
        public void onAuthentication(String username, String authToken) {
            try {
                _username = username;
                _authToken = authToken;
                _service = new WorkorderService(getActivity(), username, authToken, _resultReceiver);
                requestData();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onAuthenticationFailed(Exception ex) {
            _gs.requestAuthenticationDelayed(_authClient);
        }

        @Override
        public GlobalState getGlobalState() {
            return _gs;
        }
    };

    private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {

            if (resultCode == WEB_CHANGED || resultCode == WEB_SEND_DELIVERABLE) {
                _workorder.dispatchOnChange();

			/*-			Tasks			-*/
            } else if (resultCode == WEB_GET_TASKS) {
                // TODO populate
                String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));
                try {
                    JsonArray array = new JsonArray(data);
                    _tasks = new LinkedList<Task>();
                    for (int i = 0; i < array.size(); i++) {
                        try {
                            _tasks.add(Task.fromJson(array.getJsonObject(i)));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                _taskList.setData(_workorder, _tasks);
                setLoading(false);
            } else {
                setLoading(false);
            }
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            if (_service != null) {
                _gs.invalidateAuthToken(_service.getAuthToken());
            }
            _gs.requestAuthenticationDelayed(_authClient);
            _username = null;
            _authToken = null;
            setLoading(false);
            Toast.makeText(getActivity(), "Could not complete request", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void doAction(Bundle bundle) {
        // do nothing
    }

}
