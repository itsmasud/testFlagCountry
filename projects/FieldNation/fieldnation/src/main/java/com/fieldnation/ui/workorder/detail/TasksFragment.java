package com.fieldnation.ui.workorder.detail;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.FileHelper;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthTopicReceiver;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.data.workorder.CustomField;
import com.fieldnation.data.workorder.Document;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.ShipmentTracking;
import com.fieldnation.data.workorder.Signature;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.topics.TopicService;
import com.fieldnation.ui.AppPickerPackage;
import com.fieldnation.ui.GpsLocationService;
import com.fieldnation.ui.OverScrollView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.SignOffActivity;
import com.fieldnation.ui.SignatureDisplayActivity;
import com.fieldnation.ui.SignatureListView;
import com.fieldnation.ui.SignatureTileView;
import com.fieldnation.ui.dialog.AppPickerDialog;
import com.fieldnation.ui.dialog.ClosingNotesDialog;
import com.fieldnation.ui.dialog.ConfirmDialog;
import com.fieldnation.ui.dialog.CustomFieldDialog;
import com.fieldnation.ui.dialog.DeviceCountDialog;
import com.fieldnation.ui.dialog.MarkCompleteDialog;
import com.fieldnation.ui.dialog.ShipmentAddDialog;
import com.fieldnation.ui.dialog.TaskShipmentAddDialog;
import com.fieldnation.ui.dialog.TermsDialog;
import com.fieldnation.ui.dialog.WorkLogDialog;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.utils.ISO8601;

import java.io.File;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


public class TasksFragment extends WorkorderFragment {
    private static final String TAG = "ui.workorder.detail.TasksFragment";

    // Activity result codes
    private static final int RESULT_CODE_BASE = 200;
    private static final int RESULT_CODE_SEND_EMAIL = RESULT_CODE_BASE + 1;
    private static final int RESULT_CODE_GET_ATTACHMENT = RESULT_CODE_BASE + 3;
    private static final int RESULT_CODE_GET_CAMERA_PIC = RESULT_CODE_BASE + 4;
    private static final int RESULT_CODE_GET_SIGNATURE = RESULT_CODE_BASE + 5;

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
    private static final String STATE_SIGNATURES = "ui.workorder.detail.TasksFragment:STATE_SIGNATURES";

    // UI
    private ActionBarTopView _topBar;
    private TimeLoggedView _timeLogged;
    private TaskListView _taskList;
    private CustomFieldListView _customFields;
    private ShipmentView _shipments;
    private ClosingNotesView _closingNotes;
    private RefreshView _refreshView;
    private OverScrollView _scrollView;
    private SignatureListView _signatureView;

    // Dialogs
    private AppPickerDialog _appDialog;
    private ClosingNotesDialog _closingDialog;
    private ConfirmDialog _confirmDialog;
    private CustomFieldDialog _customFieldDialog;
    private DeviceCountDialog _deviceCountDialog;
    private MarkCompleteDialog _markCompleteDialog;
    private ShipmentAddDialog _shipmentAddDialog;
    private TaskShipmentAddDialog _taskShipmentAddDialog;
    private TermsDialog _termsDialog;
    private WorkLogDialog _worklogDialog;

    // Data
    private Context _context;
    private WorkorderService _service;

    private String _authToken;
    private String _username;
    private Workorder _workorder;
    private List<Task> _tasks = null;
    private Task _currentTask;
    private SecureRandom _rand = new SecureRandom();
    private GpsLocationService _gPSLocationService;
    private boolean _isCached = true;
    private List<Signature> _signatures = null;
    private File _tempFile;

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

        _refreshView = (RefreshView) view.findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _scrollView = (OverScrollView) view.findViewById(R.id.scroll_view);
        _scrollView.setOnOverScrollListener(_refreshView);

        _signatureView = (SignatureListView) view.findViewById(R.id.signature_view);
        _signatureView.setListener(_signaturelist_listener);

        _closingDialog = ClosingNotesDialog.getInstance(getFragmentManager(), TAG);
        _closingDialog.setListener(_closingNotes_onOk);

        _deviceCountDialog = DeviceCountDialog.getInstance(getFragmentManager(), TAG);
//        _deviceCountDialog.setListener(_deviceCountListener);

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

        _markCompleteDialog = MarkCompleteDialog.getInstance(getFragmentManager(), TAG);
        _markCompleteDialog.setListener(_markCompleteDialog_listener);

        if (savedInstanceState != null) {
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
                _tasks = new LinkedList<>();
                for (int i = 0; i < tasks.length; i++) {
                    _tasks.add((Task) tasks[i]);
                }
                _taskList.setData(_workorder, _tasks, true);
            }
            if (savedInstanceState.containsKey(STATE_CURRENT_TASK)) {
                _currentTask = savedInstanceState.getParcelable(STATE_CURRENT_TASK);
            }
            if (savedInstanceState.containsKey(STATE_SIGNATURES)) {
                Parcelable[] sigs = savedInstanceState.getParcelableArray(STATE_SIGNATURES);
                _signatures = new LinkedList<>();
                for (int i = 0; i < sigs.length; i++) {
                    _signatures.add((Signature) sigs[i]);
                }
            }
            if (_authToken != null && _username != null) {
                _service = new WorkorderService(view.getContext(), _username, _authToken, _resultReceiver);
            }
        }

//        _gPSLocationService = new GPSLocationService(getActivity());
        // GPS settings dialog should only be displayed if the GPS is failing
/*
        if (_gPSLocationService.isGooglePlayServicesAvailable() && !_gPSLocationService.isGpsEnabled()) {
            _gPSLocationService.showSettingsAlert(view.getContext());
        }
*/

        configureUi();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        _context = getActivity().getApplicationContext();

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        _appDialog.addIntent(_context.getPackageManager(), intent, "Get Content");

        if (_context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            _appDialog.addIntent(_context.getPackageManager(), intent, "Take Picture");
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
        if (_signatures != null && _signatures.size() > 0) {
            Signature[] sigs = new Signature[_signatures.size()];
            for (int i = 0; i < _signatures.size(); i++) {
                sigs[i] = _signatures.get(i);
            }
            outState.putParcelableArray(STATE_SIGNATURES, sigs);
        }

        if (_currentTask != null) {
            outState.putParcelable(STATE_CURRENT_TASK, _currentTask);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        _context = getActivity().getApplicationContext();
        AuthTopicService.subscribeAuthState(_context, 0, TAG, _authReceiver);
    }

    @Override
    public void onPause() {
        TopicService.delete(_context, TAG);
        setLoading(false);
        super.onPause();
    }

    @Override
    public void update() {
        //configureUi();
    }

    @Override
    public void setLoading(boolean isLoading) {
        if (_refreshView != null) {
            if (isLoading) {
                _refreshView.startRefreshing();
            } else {
                _refreshView.refreshComplete();
            }
        }
    }

    @Override
    public void setWorkorder(Workorder workorder, boolean isCached) {
        _workorder = workorder;
        _isCached = isCached;
        configureUi();
        requestData(true);
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
            _shipments.setWorkorder(_workorder, _isCached);

        if (_timeLogged != null)
            _timeLogged.setWorkorder(_workorder, _isCached);

        if (_closingNotes != null)
            _closingNotes.setWorkorder(_workorder, _isCached);


        if (_topBar != null)
            _topBar.setWorkorder(_workorder, _isCached);

        if (_customFields != null) {
            _customFields.setData(_workorder, _workorder.getCustomFields(), _isCached);
        }

        if (_signatureView != null) {
            _signatureView.setWorkorder(_workorder, _isCached);
        }
    }

    private void requestData(boolean allowCache) {
        requestTaskData(allowCache);
    }

    private void requestTaskData(boolean allowCache) {
        if (_service == null)
            return;

        if (_workorder == null)
            return;

        setLoading(true);
        _context.startService(_service.getTasks(WEB_GET_TASKS, _workorder.getWorkorderId(), allowCache));
    }

    private void setTaskData(List<Task> tasks, boolean isCached) {
        _tasks = tasks;
        _taskList.setData(_workorder, tasks, isCached);

        if (isCached) {
            requestTaskData(false);
            setLoading(true);
        } else {
            setLoading(false);
        }
    }

    private PendingIntent getNotificationIntent() {
        Intent intent = new Intent(_context, WorkorderActivity.class);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB,
                WorkorderActivity.TAB_DETAILS);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID,
                _workorder.getWorkorderId());

        return PendingIntent.getActivity(_context, _rand.nextInt(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult() resultCode= " + resultCode);

        if ((requestCode == RESULT_CODE_GET_ATTACHMENT || requestCode == RESULT_CODE_GET_CAMERA_PIC)
                && resultCode == Activity.RESULT_OK) {

            if (data == null) {
                getActivity().startService(_service.uploadDeliverable(WEB_SEND_DELIVERABLE,
                        _workorder.getWorkorderId(), _currentTask.getSlotId(),
                        _tempFile.getAbsolutePath(), getNotificationIntent()));
            } else {
                getActivity().startService(_service.uploadDeliverable(
                        WEB_SEND_DELIVERABLE, _workorder.getWorkorderId(),
                        _currentTask.getSlotId(), data, getNotificationIntent()));
            }
        } else if (requestCode == RESULT_CODE_GET_SIGNATURE && resultCode == Activity.RESULT_OK) {
            _context.startService(
                    _service.complete(WEB_CHANGED, _workorder.getWorkorderId()));
        }
    }

    private void showClosingNotesDialog() {
        _closingDialog.show(_workorder.getClosingNotes());
    }

    /*-*************************************-*/
    /*-				UI Events				-*/
    /*-*************************************-*/
    private RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            if (_workorder != null)
                _workorder.dispatchOnChange();
        }
    };

    private MarkCompleteDialog.Listener _markCompleteDialog_listener = new MarkCompleteDialog.Listener() {
        @Override
        public void onSignatureClick() {
            new AsyncTaskEx<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... params) {
                    Context context = (Context) params[0];
                    Workorder workorder = (Workorder) params[1];

                    Intent intent = new Intent(context, SignOffActivity.class);
                    intent.putExtra(SignOffActivity.INTENT_PARAM_WORKORDER, workorder);
                    intent.putExtra(SignOffActivity.INTENT_COMPLETE_WORKORDER, true);
                    startActivityForResult(intent, RESULT_CODE_GET_SIGNATURE);
                    return null;
                }
            }.executeEx(getActivity(), _workorder);
        }

        @Override
        public void onContinueClick() {
            _context.startService(
                    _service.complete(WEB_CHANGED, _workorder.getWorkorderId()));
        }
    };

    private WorkLogDialog.Listener _worklogDialog_listener = new WorkLogDialog.Listener() {
        @Override
        public void onOk(LoggedWork loggedWork, Calendar start, Calendar end, int deviceCount) {
            if (loggedWork == null) {
                if (deviceCount <= 0) {
                    _context.startService(_service.logTime(WEB_CHANGED, _workorder.getWorkorderId(), start.getTimeInMillis(),
                            end.getTimeInMillis()));
                } else {
                    _context.startService(_service.logTime(WEB_CHANGED, _workorder.getWorkorderId(), start.getTimeInMillis(),
                            end.getTimeInMillis(), deviceCount));
                }
            } else {
                if (deviceCount <= 0) {
                    _context.startService(
                            _service.updateLogTime(WEB_CHANGED, _workorder.getWorkorderId(),
                                    loggedWork.getLoggedHoursId(), start.getTimeInMillis(), end.getTimeInMillis()));
                } else {
                    _context.startService(
                            _service.updateLogTime(WEB_CHANGED, _workorder.getWorkorderId(),
                                    loggedWork.getLoggedHoursId(), start.getTimeInMillis(), end.getTimeInMillis(), deviceCount));
                }
            }
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

        @Override
        public void deleteWorklog(Workorder workorder, LoggedWork loggedWork) {
            _context.startService(
                    _service.deleteLogTime(WEB_CHANGED,
                            workorder.getWorkorderId(), loggedWork.getLoggedHoursId()));
            _refreshView.startRefreshing();
        }
    };

    /*-************************************-*/
    /*-         ActionBarTopView           -*/
    /*-************************************-*/

    private ActionBarTopView.Listener _actionBarTop_listener = new ActionBarTopView.Listener() {
        @Override
        public void onComplete() {
            _markCompleteDialog.show(_workorder);
        }

        @Override
        public void onCheckOut() {
            Pay pay = _workorder.getPay();
            if (pay != null && pay.isPerDeviceRate()) {
                _deviceCountDialog.show(_workorder, pay.getMaxDevice());
            } else {
//                if (_gPSLocationService.isGooglePlayServicesAvailable() && _gPSLocationService.isLocationServiceEnabled() && _gPSLocationService.isGpsEnabled()) {
//                    try {
//                        Location location = _gPSLocationService.getLocation();
//                        double lat = location.getLatitude();
//                        double log = location.getLongitude();
////                        _context.startService(_service.checkout(WEB_CHANGED, _workorder.getWorkorderId(), lat, log));
//                    } catch (Exception e) {
//                        _gPSLocationService.showSettingsOffAlert(getView().getContext());
//                    }
//                } else {
//                    _gPSLocationService.showCheckInOutAlert(getView().getContext());
//                    _context.startService(
//                            _service.checkout(WEB_CHANGED, _workorder.getWorkorderId()));
//                }
            }
        }

        @Override
        public void onCheckIn() {
//            if (_gPSLocationService.isGooglePlayServicesAvailable() && _gPSLocationService.isLocationServiceEnabled() && _gPSLocationService.isGpsEnabled()) {
//                try {
//                    Location location = _gPSLocationService.getLocation();
//                    double lat = location.getLatitude();
//                    double log = location.getLongitude();
////                    _context.startService(_service.checkin(WEB_CHANGED, _workorder.getWorkorderId(), lat, log));
//                } catch (Exception e) {
//                    _gPSLocationService.showSettingsOffAlert(getView().getContext());
//                }
//            } else {
//                _gPSLocationService.showCheckInOutAlert(getView().getContext());
//                _context.startService(
//                        _service.checkin(WEB_CHANGED, _workorder.getWorkorderId()));
//            }
        }

        @Override
        public void onAcknowledge() {
            _context.startService(
                    _service.acknowledgeHold(WEB_CHANGED, _workorder.getWorkorderId()));
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
        public void onTaskClick(Task task) {
            switch (task.getTaskType()) {
                case CHECKIN:
//                    if (_gPSLocationService.isGooglePlayServicesAvailable() && _gPSLocationService.isLocationServiceEnabled() && _gPSLocationService.isGpsEnabled()) {
//                        try {
//                            Location location = _gPSLocationService.getLocation();
//                            double lat = location.getLatitude();
//                            double log = location.getLongitude();
////                            _context.startService(_service.checkin(WEB_CHANGED, _workorder.getWorkorderId(), lat, log));
//                        } catch (Exception e) {
//                            _gPSLocationService.showSettingsOffAlert(getView().getContext());
//                        }
//                    } else {
//                        _gPSLocationService.showCheckInOutAlert(getView().getContext());
//                        _context.startService(_service.checkin(WEB_CHANGED, _workorder.getWorkorderId()));
//                    }

                    break;
                case CHECKOUT:
                    Pay pay = _workorder.getPay();
                    if (pay != null && pay.isPerDeviceRate()) {
                        _deviceCountDialog.show(_workorder, pay.getMaxDevice());
                    } else {
//                        if (_gPSLocationService.isGooglePlayServicesAvailable() && _gPSLocationService.isLocationServiceEnabled() && _gPSLocationService.isGpsEnabled()) {
//                            try {
//                                Location location = _gPSLocationService.getLocation();
//                                double lat = location.getLatitude();
//                                double log = location.getLongitude();
////                                _context.startService(_service.checkout(WEB_CHANGED, _workorder.getWorkorderId(), lat, log));
//                            } catch (Exception e) {
//                                _gPSLocationService.showSettingsOffAlert(getView().getContext());
//                            }
//                        } else {
//                            _gPSLocationService.showCheckInOutAlert(getView().getContext());
//                            _context.startService(
//                                    _service.checkout(WEB_CHANGED, _workorder.getWorkorderId()));
//                        }
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
                                    _context.startService(
                                            _service.completeTask(WEB_CHANGED, _workorder.getWorkorderId(),
                                                    task.getTaskId()));
                                }

                                FileHelper.viewOrDownloadFile(_context, doc.getFilePath(),
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
                        _context.startService(
                                _service.completeTask(WEB_CHANGED, _workorder.getWorkorderId(), task.getTaskId()));
                    }
                    break;
                }
                case PHONE:
                    try {
                        if (task.getPhoneNumber() != null) {
                            if (!task.getCompleted()) {
                                _context.startService(
                                        _service.completeTask(WEB_CHANGED, _workorder.getWorkorderId(), task.getTaskId()));
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
                        _shipmentAddDialog.show(getText(R.string.dialog_shipment_title), task.getTaskId());
                    } else {
                        _taskShipmentAddDialog.show("Assign/Add New", _workorder, task.getTaskId());
                    }

                    break;
                case SIGNATURE: {
                    _currentTask = task;
                    SignOffActivity.startSignOff(getActivity(), _workorder, task.getTaskId());
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
                    _context.startService(
                            _service.completeTask(WEB_CHANGED, _workorder.getWorkorderId(), task.getTaskId()));
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onCheckin(Task task) {

        }

        @Override
        public void onCheckout(Task task) {

        }

        @Override
        public void onCloseOutNotes(Task task) {

        }

        @Override
        public void onConfirmAssignment(Task task) {

        }

        @Override
        public void onCustomField(Task task) {

        }

        @Override
        public void onDownload(Task task) {

        }

        @Override
        public void onEmail(Task task) {

        }

        @Override
        public void onPhone(Task task) {

        }

        @Override
        public void onShipment(Task task) {

        }

        @Override
        public void onSignature(Task task) {

        }

        @Override
        public void onUploadFile(Task task) {

        }

        @Override
        public void onUploadPicture(Task task) {

        }

        @Override
        public void onUniqueTask(Task task) {

        }
    };

    private TaskShipmentAddDialog.Listener taskShipmentAddDialog_listener = new TaskShipmentAddDialog.Listener() {
        @Override
        public void onDelete(Workorder workorder, int shipmentId) {
            _context.startService(_service.deleteShipment(WEB_CHANGED, workorder.getWorkorderId(), shipmentId));
        }

        @Override
        public void onAssign(Workorder workorder, int shipmentId, long taskId) {
            // @TODO
            Log.v(TAG, "Method Stub: onAssign()" + shipmentId + "=" + taskId);
            _context.startService(
                    _service.completeShipmentTask(WEB_CHANGED, workorder.getWorkorderId(), shipmentId, taskId));
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onAddShipmentDetails(Workorder workorder, String trackingId, String carrier, String carrierName, String description, boolean shipToSite) {
            _context.startService(
                    _service.addShipmentDetails(WEB_CHANGED, workorder.getWorkorderId(), description, shipToSite, carrier, carrierName, trackingId));
        }

        @Override
        public void onAddShipmentDetails(Workorder workorder, String trackingId, String carrier, String carrierName, String description, boolean shipToSite, long taskId) {
            _context.startService(
                    _service.addShipmentDetails(WEB_CHANGED, workorder.getWorkorderId(), description, shipToSite,
                            carrier, carrierName, trackingId, taskId));
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
            _context.startService(
                    _service.setCustomField(WEB_CHANGED, _workorder.getWorkorderId(), field.getCustomLabelId(), value));
        }
    };

    /*-*****************************-*/
    /*-         Shipments           -*/
    /*-*****************************-*/
    private ShipmentAddDialog.Listener _shipmentAddDialog_listener = new ShipmentAddDialog.Listener() {
        @Override
        public void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite) {
            _context.startService(
                    _service.addShipmentDetails(WEB_CHANGED, _workorder.getWorkorderId(), description, shipToSite,
                            carrier, carrierName, trackingId));
        }

        @Override
        public void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite, long taskId) {
            _context.startService(
                    _service.addShipmentDetails(WEB_CHANGED, _workorder.getWorkorderId(), description, shipToSite,
                            carrier, carrierName, trackingId, taskId));
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
            _context.startService(_service.deleteShipment(WEB_CHANGED, workorder.getWorkorderId(), shipmentId));
        }

        @Override
        public void onAssign(Workorder workorder, int shipmentId) {
            // TODO STUB .onAssign()
            Log.v(TAG, "STUB .onAssign()");
        }


//        @Override
//        public void onAddShipmentDetails(Workorder workorder, String description, boolean shipToSite, String carrier,
//                                         String trackingId) {
//            _context.startService(
//                    _service.addShipmentDetails(WEB_CHANGED, workorder.getWorkorderId(), description, shipToSite,
//                            carrier, null, trackingId));
//            Log.v(TAG, "Method Stub: onAddShipmentDetails()");
//        }
//
//        @Override
//        public void onAddShipmentDetails(Workorder workorder, String description, boolean shipToSite, String carrier,
//                                         String trackingId, long taskId) {
//            _context.startService(
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
            _context.startService(
                    _service.closingNotes(WEB_CHANGED, _workorder.getWorkorderId(), message));

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

    private SignatureListView.Listener _signaturelist_listener = new SignatureListView.Listener() {
        @Override
        public void addSignature() {
            SignOffActivity.startSignOff(getActivity(), _workorder);
            setLoading(true);
        }

        @Override
        public void signatureOnClick(SignatureTileView view, Signature signature) {
            SignatureDisplayActivity.startIntent(getActivity(), signature.getSignatureId(), _workorder);
            setLoading(true);
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
                String packageName = _context.getPackageName();
                File externalPath = Environment.getExternalStorageDirectory();
                new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/temp").mkdirs();
                File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/temp/IMAGE-" + System.currentTimeMillis() + ".png");
                _tempFile = temppath;
                src.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temppath));
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
                _context.startService(intent);
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
        }
    };

/*
    private DeviceCountDialog.Listener _deviceCountListener = new DeviceCountDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, int count) {
//            if (_gPSLocationService.isGooglePlayServicesAvailable() && _gPSLocationService.isLocationServiceEnabled() && _gPSLocationService.isGpsEnabled()) {
//                try {
//                    Location location = _gPSLocationService.getLocation();
//                    double lat = location.getLatitude();
//                    double log = location.getLongitude();
////                    _context.startService(_service.checkout(WEB_CHANGED, _workorder.getWorkorderId(), count, lat, log));
//                } catch (Exception e) {
//                    _gPSLocationService.showSettingsOffAlert(getView().getContext());
//                }
//
//            } else {
//                _gPSLocationService.showCheckInOutAlert(getView().getContext());
//                _context.startService(
//                        _service.checkout(WEB_CHANGED, _workorder.getWorkorderId(), count));
//            }
        }
    };
*/

    /*-*************************************-*/
    /*-				WEB Events				-*/
    /*-*************************************-*/

    private AuthTopicReceiver _authReceiver = new AuthTopicReceiver(new Handler()) {
        @Override
        public void onAuthentication(String username, String authToken, boolean isNew) {
            if (_service == null || isNew) {
                _username = username;
                _authToken = authToken;
                if (_context != null)
                    _service = new WorkorderService(_context, username, authToken, _resultReceiver);
                requestData(true);
            }
        }

        @Override
        public void onAuthenticationFailed(boolean networkDown) {
            _service = null;
        }

        @Override
        public void onAuthenticationInvalidated() {
            _service = null;
        }

        @Override
        public void onRegister(int resultCode, String topicId) {
            AuthTopicService.requestAuthentication(_context);
        }
    };

    private class TaskParseAsyncTask extends AsyncTaskEx<Bundle, Object, List<Task>> {
        private boolean cached = false;

        @Override
        protected List<Task> doInBackground(Bundle... params) {
            Bundle resultData = params[0];
            String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));
            cached = resultData.getBoolean(WebServiceConstants.KEY_RESPONSE_CACHED);
            List<Task> tasks = new LinkedList<Task>();
            try {
                JsonArray array = new JsonArray(data);

                for (int i = 0; i < array.size(); i++) {
                    try {
                        tasks.add(Task.fromJson(array.getJsonObject(i)));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return tasks;
        }

        @Override
        protected void onPostExecute(List<Task> tasks) {
            super.onPostExecute(tasks);
            setTaskData(tasks, cached);
        }
    }

    private WebResultReceiver _resultReceiver = new WebResultReceiver(new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {

            if (resultCode == WEB_CHANGED || resultCode == WEB_SEND_DELIVERABLE) {
                _workorder.dispatchOnChange();

			/*-			Tasks			-*/
            } else if (resultCode == WEB_GET_TASKS) {
                new TaskParseAsyncTask().executeEx(resultData);
            } else {
            }
        }

        @Override
        public Context getContext() {
            return TasksFragment.this._context;
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            AuthTopicService.requestAuthInvalid(_context);
            _username = null;
            _authToken = null;
            _service = null;
            Toast.makeText(_context, "Could not complete request", Toast.LENGTH_LONG).show();
        }
    };

}
