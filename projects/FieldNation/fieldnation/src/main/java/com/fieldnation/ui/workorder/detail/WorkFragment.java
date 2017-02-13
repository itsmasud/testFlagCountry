package com.fieldnation.ui.workorder.detail;

import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.data.workorder.Discount;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fngps.GpsLocationService;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.MemUtils;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.GpsTrackingService;
import com.fieldnation.service.activityresult.ActivityResultConstants;
import com.fieldnation.service.data.filecache.FileCacheClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.service.data.v2.workorder.WorkOrderClient;
import com.fieldnation.service.data.workorder.ReportProblemType;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.AppPickerPackage;
import com.fieldnation.ui.OverScrollView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.SignOffActivity;
import com.fieldnation.ui.SignatureListView;
import com.fieldnation.ui.dialog.AppPickerDialog;
import com.fieldnation.ui.dialog.ClosingNotesDialog;
import com.fieldnation.ui.dialog.CounterOfferDialog;
import com.fieldnation.ui.dialog.CustomFieldDialog;
import com.fieldnation.ui.dialog.DeclineDialog;
import com.fieldnation.ui.dialog.DiscountDialog;
import com.fieldnation.ui.dialog.ExpenseDialog;
import com.fieldnation.ui.dialog.ExpiresDialog;
import com.fieldnation.ui.dialog.LocationDialog;
import com.fieldnation.ui.dialog.OneButtonDialog;
import com.fieldnation.ui.dialog.PayDialog;
import com.fieldnation.ui.dialog.PhotoUploadDialog;
import com.fieldnation.ui.dialog.ShipmentAddDialog;
import com.fieldnation.ui.dialog.TaskShipmentAddDialog;
import com.fieldnation.ui.dialog.TermsDialog;
import com.fieldnation.ui.dialog.TermsScrollingDialog;
import com.fieldnation.ui.dialog.TwoButtonDialog;
import com.fieldnation.ui.dialog.WorkLogDialog;
import com.fieldnation.ui.dialog.v2.AcceptBundleDialog;
import com.fieldnation.ui.dialog.v2.MarkCompleteDialog;
import com.fieldnation.ui.dialog.v2.ReportProblemDialog;
import com.fieldnation.ui.workorder.WorkOrderActivity;
import com.fieldnation.ui.workorder.WorkorderBundleDetailActivity;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Expense;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.CheckInOutDialog;
import com.fieldnation.v2.ui.dialog.EtaDialog;
import com.fieldnation.v2.ui.dialog.MarkIncompleteWarningDialog;
import com.fieldnation.v2.ui.dialog.WithdrawRequestDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public class WorkFragment extends WorkorderFragment {
    private static final String TAG = "WorkFragment";

    // Dialog tags
    private static final String DIALOG_CHECK_IN_CHECK_OUT = TAG + ".checkInOutDialog";
    private static final String DIALOG_REPORT_PROBLEM = TAG + ".reportProblemDialog";
    private static final String DIALOG_ETA = TAG + ".etaDialog";
    private static final String DIALOG_WITHDRAW = TAG + ".withdrawRequestDialog";
    private static final String DIALOG_CANCEL_WARNING = TAG + ".cancelWarningDialog";
    private static final String DIALOG_RUNNING_LATE = TAG + ".runningLateDialogLegacy";
    private static final String DIALOG_MARK_COMPLETE = TAG + ".markCompleteDialog";
    private static final String DIALOG_MARK_INCOMPLETE = TAG + ".markIncompleteDialog";
    private static final String DIALOG_RATE_BUYER_YESNO = TAG + ".rateBuyerYesNoDialog";

    // saved state keys
    private static final String STATE_WORKORDER = "WorkFragment:STATE_WORKORDER";
    private static final String STATE_TASKS = "WorkFragment:STATE_TASKS";
    private static final String STATE_CURRENT_TASK = "WorkFragment:STATE_CURRENT_TASK";
    private static final String STATE_SIGNATURES = "WorkFragment:STATE_SIGNATURES";
    private static final String STATE_DEVICE_COUNT = "WorkFragment:STATE_DEVICE_COUNT";
    private static final String STATE_SCANNED_IMAGE_PATH = "WorkFragment:STATE_SCANNED_IMAGE_PATH";
    private static final String STATE_TEMP_FILE = "WorkFragment:STATE_TEMP_FILE";
    private static final String STATE_TEMP_URI = "WorkFragment:STATE_TEMP_URI";

    // UI
    private Button _testButton;
    private OverScrollView _scrollView;
    private ActionBarTopView _topBar;
    private WorkSummaryView _sumView;
    private CompanySummaryView _companySummaryView;
    private ScheduleSummaryView _scheduleView;
    private LocationView _locView;
    private ContactListView _contactListView;
    private ExpectedPaymentView _exView;
    private TextView _bundleWarningTextView;
    private TimeLogListView _timeLogged;
    private TaskListView _taskList;
    private CustomFieldListView _customFields;
    private ShipmentListView _shipments;
    private SignatureListView _signatureView;
    private ClosingNotesView _closingNotes;
    private PaymentView _payView;
    private CounterOfferSummaryView _coSummaryView;
    private ExpenseListLayout _expenseListView;
    private DiscountListLayout _discountListView;
    private RefreshView _refreshView;

    // Dialogs
    private AppPickerDialog _appDialog;
    private ClosingNotesDialog _closingDialog;
    private CounterOfferDialog _counterOfferDialog;
    private CustomFieldDialog _customFieldDialog;
    private DeclineDialog _declineDialog;
    private DiscountDialog _discountDialog;
    private ExpenseDialog _expenseDialog;
    private ShipmentAddDialog _shipmentAddDialog;
    private TaskShipmentAddDialog _taskShipmentAddDialog;
    private TermsDialog _termsDialog;
    private TermsScrollingDialog _termsScrollingDialog;
    private WorkLogDialog _worklogDialog;
    private LocationDialog _locationDialog;
    private OneButtonDialog _locationLoadingDialog;
    private PayDialog _payDialog;
    private TwoButtonDialog _yesNoDialog;
    private ReportProblemDialog _reportProblemDialog;
    private PhotoUploadDialog _photoUploadDialog;

    // Data
    private WorkordersWebApi _workOrderApi;
    private FileCacheClient _fileCacheClient;
    private WorkordersWebApi _workorderApi;
    private File _tempFile;
    private Uri _tempUri;
    private GpsLocationService _gpsLocationService;
    //TODO    private List<Signature> _signatures = null;
    //TODO    private List<Task> _tasks = null;
    //TODO    private Task _currentTask;
    private WorkOrder _workOrder;
    private int _deviceCount = -1;
    private String _scannedImagePath;

    private final List<Runnable> _untilAdded = new LinkedList<>();

	/*-*************************************-*/
    /*-				LifeCycle				-*/
    /*-*************************************-*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_TEMP_FILE)) {
                _tempFile = new File(savedInstanceState.getString(STATE_TEMP_FILE));
            }

            if (savedInstanceState.containsKey(STATE_TEMP_URI)) {
                _tempUri = savedInstanceState.getParcelable(STATE_TEMP_URI);
            }
        }
        return inflater.inflate(R.layout.fragment_workorder_work, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.v(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        _testButton = (Button) view.findViewById(R.id.test_button);
        _testButton.setOnClickListener(_test_onClick);

        _topBar = (ActionBarTopView) view.findViewById(R.id.actiontop_view);
        _topBar.setListener(_actionbartop_listener);

        _sumView = (WorkSummaryView) view.findViewById(R.id.summary_view);
        _sumView.setListener(_summaryView_listener);

        _companySummaryView = (CompanySummaryView) view.findViewById(R.id.companySummary_view);

        _contactListView = (ContactListView) view.findViewById(R.id.contactList_view);

        _locView = (LocationView) view.findViewById(R.id.location_view);
        _scheduleView = (ScheduleSummaryView) view.findViewById(R.id.schedule_view);

        _payView = (PaymentView) view.findViewById(R.id.payment_view);
// TODO        _payView.setListener(_paymentView_listener);

        _coSummaryView = (CounterOfferSummaryView) view.findViewById(R.id.counterOfferSummary_view);
        _coSummaryView.setListener(_coSummary_listener);

        _expenseListView = (ExpenseListLayout) view.findViewById(R.id.expenseListLayout_view);
        _expenseListView.setListener(_expenseListView_listener);

        _discountListView = (DiscountListLayout) view.findViewById(R.id.discountListLayout_view);
        _discountListView.setListener(_discountListView_listener);

        _exView = (ExpectedPaymentView) view.findViewById(R.id.expected_pay_view);

        _bundleWarningTextView = (TextView) view.findViewById(R.id.bundlewarning2_textview);
        _bundleWarningTextView.setOnClickListener(_bundle_onClick);

        _refreshView = (RefreshView) view.findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _scrollView = (OverScrollView) view.findViewById(R.id.scroll_view);
        _scrollView.setOnOverScrollListener(_refreshView);

        _shipments = (ShipmentListView) view.findViewById(R.id.shipment_view);
// TODO        _shipments.setListener(_shipments_listener);

        _taskList = (TaskListView) view.findViewById(R.id.scope_view);
// TODO        _taskList.setTaskListViewListener(_taskListView_listener);

        _timeLogged = (TimeLogListView) view.findViewById(R.id.timelogged_view);
// TODO        _timeLogged.setListener(_timeLoggedView_listener);

        _closingNotes = (ClosingNotesView) view.findViewById(R.id.closingnotes_view);
        _closingNotes.setListener(_clockingNotesView_listener);

        _customFields = (CustomFieldListView) view.findViewById(R.id.customfields_view);
// TODO        _customFields.setListener(_customFields_listener);

        _signatureView = (SignatureListView) view.findViewById(R.id.signature_view);
// TODO        _signatureView.setListener(_signaturelist_listener);

        _payDialog = PayDialog.getInstance(getFragmentManager(), TAG);
// TODO        _payDialog.setListener(_payDialog_listener);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDER)) {
                _workOrder = savedInstanceState.getParcelable(STATE_WORKORDER);
            }
            if (savedInstanceState.containsKey(STATE_TASKS)) {
                Parcelable[] tasks = savedInstanceState.getParcelableArray(STATE_TASKS);
/*
TODO                _tasks = new LinkedList<>();
                for (Parcelable task : tasks) {
                    _tasks.add((Task) task);
                }
                _taskList.setData(_workorder, _tasks);
*/
            }
            if (savedInstanceState.containsKey(STATE_CURRENT_TASK)) {
// TODO                _currentTask = savedInstanceState.getParcelable(STATE_CURRENT_TASK);
            }
            if (savedInstanceState.containsKey(STATE_SIGNATURES)) {
                Parcelable[] sigs = savedInstanceState.getParcelableArray(STATE_SIGNATURES);
/*
TODO                _signatures = new LinkedList<>();
                for (Parcelable sig : sigs) {
                    _signatures.add((Signature) sig);
                }
*/
            }
            if (savedInstanceState.containsKey(STATE_DEVICE_COUNT)) {
                _deviceCount = savedInstanceState.getInt(STATE_DEVICE_COUNT);
            }
            if (savedInstanceState.containsKey(STATE_SCANNED_IMAGE_PATH)) {
                _scannedImagePath = savedInstanceState.getString(STATE_SCANNED_IMAGE_PATH);
            }
        }

        populateUi();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_workOrder != null) {
            outState.putParcelable(STATE_WORKORDER, _workOrder);
        }
/*
TODO         if (_tasks != null && _tasks.size() > 0) {
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
*/
        if (_deviceCount > -1)
            outState.putInt(STATE_DEVICE_COUNT, _deviceCount);

/*
TODO        if (_currentTask != null)
            outState.putParcelable(STATE_CURRENT_TASK, _currentTask);
*/

        if (_scannedImagePath != null)
            outState.putString(STATE_SCANNED_IMAGE_PATH, _scannedImagePath);

        if (_tempFile != null)
            outState.putString(STATE_TEMP_FILE, _tempFile.getAbsolutePath());

        if (_tempUri != null)
            outState.putParcelable(STATE_TEMP_URI, _tempUri);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        _appDialog.addIntent(getActivity().getPackageManager(), intent, "Get Content");

        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            _appDialog.addIntent(getActivity().getPackageManager(), intent, "Take Picture");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        Log.v(TAG, "onAttach");
        super.onAttach(activity);
        _appDialog = AppPickerDialog.getInstance(getFragmentManager(), TAG);
        _closingDialog = ClosingNotesDialog.getInstance(getFragmentManager(), TAG);
        _counterOfferDialog = CounterOfferDialog.getInstance(getFragmentManager(), TAG);
        _customFieldDialog = CustomFieldDialog.getInstance(getFragmentManager(), TAG);
        _declineDialog = DeclineDialog.getInstance(getFragmentManager(), TAG);
//        _deviceCountDialog = DeviceCountDialog.getInstance(getFragmentManager(), TAG);
        _discountDialog = DiscountDialog.getInstance(getFragmentManager(), TAG);
        _expenseDialog = ExpenseDialog.getInstance(getFragmentManager(), TAG);
        _locationDialog = LocationDialog.getInstance(getFragmentManager(), TAG);
        _locationLoadingDialog = OneButtonDialog.getInstance(getFragmentManager(), TAG);
        _shipmentAddDialog = ShipmentAddDialog.getInstance(getFragmentManager(), TAG);
        _taskShipmentAddDialog = TaskShipmentAddDialog.getInstance(getFragmentManager(), TAG);
        _termsDialog = TermsDialog.getInstance(getFragmentManager(), TAG);
        _termsScrollingDialog = TermsScrollingDialog.getInstance(getFragmentManager(), TAG);
        _yesNoDialog = TwoButtonDialog.getInstance(getFragmentManager(), TAG);
        _worklogDialog = WorkLogDialog.getInstance(getFragmentManager(), TAG);
        _photoUploadDialog = PhotoUploadDialog.getInstance(getFragmentManager(), TAG);
        _payDialog = PayDialog.getInstance(getFragmentManager(), TAG);

        _locationLoadingDialog.setData(getString(R.string.dialog_location_loading_title),
                getString(R.string.dialog_location_loading_body),
                getString(R.string.dialog_location_loading_button),
                _locationLoadingDialog_listener);

        _closingDialog.setListener(_closingNotes_onOk);
// TODO        _counterOfferDialog.setListener(_counterOffer_listener);
        _declineDialog.setListener(_declineDialog_listener);
        _discountDialog.setListener(_discountDialog_listener);
        _expenseDialog.setListener(_expenseDialog_listener);
// TODO        _customFieldDialog.setListener(_customFieldDialog_listener);
        _appDialog.setListener(_appdialog_listener);
// TODO        _taskShipmentAddDialog.setListener(taskShipmentAddDialog_listener);
        _shipmentAddDialog.setListener(_shipmentAddDialog_listener);
// TODO        _worklogDialog.setListener(_worklogDialog_listener);
        _photoUploadDialog.setListener(_photoUploadDialog_listener);
// TODO        _payDialog.setListener(_payDialog_listener);

        CheckInOutDialog.addOnCheckInListener(DIALOG_CHECK_IN_CHECK_OUT, _checkInOutDialog_onCheckIn);
        CheckInOutDialog.addOnCheckOutListener(DIALOG_CHECK_IN_CHECK_OUT, _checkInOutDialog_onCheckOut);
        EtaDialog.addOnRequestedListener(DIALOG_ETA, _etaDialog_onRequested);
        EtaDialog.addOnAcceptedListener(DIALOG_ETA, _etaDialog_onAccepted);
        EtaDialog.addOnConfirmedListener(DIALOG_ETA, _etaDialog_onConfirmed);
        ReportProblemDialog.addOnSendListener(DIALOG_REPORT_PROBLEM, _reportProblemDialog_onSend);
        WithdrawRequestDialog.addOnWithdrawListener(DIALOG_WITHDRAW, _withdrawRequestDialog_onWithdraw);
        MarkCompleteDialog.addOnContinueClickListener(DIALOG_MARK_COMPLETE, _markCompleteDialog_onContinue);
        MarkCompleteDialog.addOnSignatureClickListener(DIALOG_MARK_COMPLETE, _markCompleteDialog_onSignature);
        MarkIncompleteWarningDialog.addOnMarkIncompleteListener(DIALOG_MARK_INCOMPLETE, _markIncompleteDialog_markIncomplete);

        _workorderApi = new WorkordersWebApi(_workOrderApi_listener);
        _workorderApi.connect(App.get());

        _fileCacheClient = new FileCacheClient(_fileCacheClient_listener);
        _fileCacheClient.connect(App.get());

        _gpsLocationService = new GpsLocationService(getActivity());

        while (_untilAdded.size() > 0) {
            _untilAdded.remove(0).run();
        }
    }

    @Override
    public void onDetach() {
        Log.v(TAG, "onDetach");

        CheckInOutDialog.removeOnCheckInListener(DIALOG_CHECK_IN_CHECK_OUT, _checkInOutDialog_onCheckIn);
        CheckInOutDialog.removeOnCheckOutListener(DIALOG_CHECK_IN_CHECK_OUT, _checkInOutDialog_onCheckOut);
        EtaDialog.removeOnRequestedListener(DIALOG_ETA, _etaDialog_onRequested);
        EtaDialog.removeOnAcceptedListener(DIALOG_ETA, _etaDialog_onAccepted);
        EtaDialog.removeOnConfirmedListener(DIALOG_ETA, _etaDialog_onConfirmed);
        ReportProblemDialog.removeOnSendListener(DIALOG_REPORT_PROBLEM, _reportProblemDialog_onSend);
        WithdrawRequestDialog.removeOnWithdrawListener(DIALOG_WITHDRAW, _withdrawRequestDialog_onWithdraw);
        MarkCompleteDialog.removeOnContinueClickListener(DIALOG_MARK_COMPLETE, _markCompleteDialog_onContinue);
        MarkCompleteDialog.removeOnSignatureClickListener(DIALOG_MARK_COMPLETE, _markCompleteDialog_onSignature);
        MarkIncompleteWarningDialog.removeOnMarkIncompleteListener(DIALOG_MARK_INCOMPLETE, _markIncompleteDialog_markIncomplete);

        if (_workorderApi != null && _workorderApi.isConnected())
            _workorderApi.disconnect(App.get());

        if (_fileCacheClient != null && _fileCacheClient.isConnected())
            _fileCacheClient.disconnect(App.get());

        if (_workorderApi != null && _workorderApi.isConnected())
            _workorderApi.disconnect(App.get());

        super.onDetach();
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause");
        if (_gpsLocationService != null && _gpsLocationService.isRunning()) {
            _gpsLocationService.stopLocationUpdates();
        }
        super.onPause();
    }

    @Override
    public void update() {
//        Tracker.screen(App.get(), ScreenName.workOrderDetailsWork());
    }

    @Override
    public void setWorkorder(WorkOrder workOrder) {
        Log.v(TAG, "setWorkorder");
        _workOrder = workOrder;
        subscribeData();
        requestTasks();
        populateUi();
    }

/*
TODO     private void setTasks(List<Task> tasks) {
        _tasks = tasks;
        _taskList.setData(_workorder, tasks);
        setLoading(false);
    }
*/

    private void populateUi() {
        misc.hideKeyboard(getView());

        if (_workOrder == null)
            return;

        if (getActivity() == null)
            return;

        setLoading(true);

        if (_sumView != null) {
            Stopwatch watch = new Stopwatch(true);
            _sumView.setWorkOrder(_workOrder);
            //Log.v(TAG, "_sumView time: " + watch.finish());
        }

        if (_companySummaryView != null) {
            Stopwatch watch = new Stopwatch(true);
            _companySummaryView.setWorkOrder(_workOrder);
            //Log.v(TAG, "_companySummaryView time: " + watch.finish());
        }

        if (_locView != null) {
            Stopwatch watch = new Stopwatch(true);
            _locView.setWorkOrder(_workOrder);
            //Log.v(TAG, "_locView time: " + watch.finish());
        }

        if (_scheduleView != null) {
            Stopwatch watch = new Stopwatch(true);
            _scheduleView.setWorkOrder(_workOrder);
            //Log.v(TAG, "_scheduleView time: " + watch.finish());
        }

        if (_contactListView != null) {
            Stopwatch watch = new Stopwatch(true);
            _contactListView.setWorkOrder(_workOrder);
            //Log.v(TAG, "_contactListView time: " + watch.finish());
        }

        if (_payView != null) {
            Stopwatch watch = new Stopwatch(true);
            _payView.setWorkOrder(_workOrder);
            //Log.v(TAG, "_payView time: " + watch.finish());
        }

        if (_coSummaryView != null) {
            Stopwatch watch = new Stopwatch(true);
//TODO            _coSummaryView.setData(_workOrder);
            //Log.v(TAG, "_coSummaryView time: " + watch.finish());
        }

        if (_expenseListView != null) {
            Stopwatch watch = new Stopwatch(true);
            _expenseListView.setWorkOrder(_workOrder);
            //Log.v(TAG, "_expenseListView time: " + watch.finish());
        }

        if (_discountListView != null) {
            Stopwatch watch = new Stopwatch(true);
            _discountListView.setWorkOrder(_workOrder);
            //Log.v(TAG, "_discountListView time: " + watch.finish());
        }

        if (_topBar != null) {
            Stopwatch watch = new Stopwatch(true);
//TODO            _topBar.setWorkorder(_workOrder);
            //Log.v(TAG, "_topBar time: " + watch.finish());
        }

        if (_exView != null) {
            Stopwatch watch = new Stopwatch(true);
//TODO            _exView.setWorkorder(_workOrder);
            //Log.v(TAG, "_exView time: " + watch.finish());
        }

        if (_shipments != null && _timeLogged != null) {
            Stopwatch watch = new Stopwatch(true);
/*
TODO            WorkorderStatus status = _workOrder.getStatus().getWorkorderStatus();
            if (status.ordinal() < WorkorderStatus.ASSIGNED.ordinal()) {
                _timeLogged.setVisibility(View.GONE);
                _shipments.setVisibility(View.GONE);
                _closingNotes.setVisibility(View.GONE);
            } else {
                _shipments.setVisibility(View.VISIBLE);
                _timeLogged.setVisibility(View.VISIBLE);
                _closingNotes.setVisibility(View.VISIBLE);
            }
*/
            //Log.v(TAG, "_shipments time: " + watch.finish());
        }

        if (_shipments != null) {
            Stopwatch watch = new Stopwatch(true);
//TODO            _shipments.setWorkorder(_workOrder);
            //Log.v(TAG, "_shipments time: " + watch.finish());
        }

        if (_timeLogged != null) {
            Stopwatch watch = new Stopwatch(true);
//TODO            _timeLogged.setWorkorder(_workOrder);
            //Log.v(TAG, "_timeLogged time: " + watch.finish());
        }

        if (_closingNotes != null) {
            Stopwatch watch = new Stopwatch(true);
//TODO            _closingNotes.setWorkorder(_workOrder);
            //Log.v(TAG, "_closingNotes time: " + watch.finish());
        }

        if (_customFields != null) {
            Stopwatch watch = new Stopwatch(true);
//TODO            _customFields.setData(_workOrder);
            //Log.v(TAG, "_customFields time: " + watch.finish());
        }

        if (_signatureView != null) {
            Stopwatch watch = new Stopwatch(true);
//TODO            _signatureView.setWorkorder(_workOrder);
            //Log.v(TAG, "_signatureView time: " + watch.finish());
        }

        setLoading(false);

        if (_bundleWarningTextView != null) {
            Stopwatch watch = new Stopwatch(true);
            if (_workOrder.getBundle() != null && _workOrder.getBundle().getId() != null && _workOrder.getBundle().getId() > 0) {
                _bundleWarningTextView.setVisibility(View.VISIBLE);
            } else {
                _bundleWarningTextView.setVisibility(View.GONE);
            }
            //Log.v(TAG, "_bundleWarningTextView time: " + watch.finish());
        }

        if (getArguments() != null) {
            if (getArguments().containsKey(WorkOrderActivity.INTENT_FIELD_ACTION)
                    && getArguments().getString(WorkOrderActivity.INTENT_FIELD_ACTION)
                    .equals(WorkOrderActivity.ACTION_CONFIRM)) {

                EtaDialog.show(App.get(), DIALOG_ETA, _workOrder.getWorkOrderId(), _workOrder.getSchedule(), EtaDialog.PARAM_DIALOG_TYPE_CONFIRM);
                getArguments().remove(WorkOrderActivity.INTENT_FIELD_ACTION);
            }
        }
    }

    private void requestWorkorder() {
        if (_workOrder == null)
            return;

        Log.v(TAG, "getData.startRefreshing");
        setLoading(true);
        WorkordersWebApi.getWorkOrder(App.get(), _workOrder.getWorkOrderId(), false);
    }

    private void requestTasks() {
        if (_workOrder == null)
            return;

        if (getActivity() == null)
            return;

// TODO        WorkorderClient.listTasks(App.get(), _workOrder.getWorkOrderId(), false);
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

    /*-*********************************************-*/
    /*-				Check In Process				-*/
    /*-*********************************************-*/

    private void startCheckin() {
        // everything is awsome. checkin
/*
TODO        _gpsLocationService.setListener(_gps_checkInListener);
        if (!_gpsLocationService.isLocationServicesEnabled()) {
            _locationDialog.show(_workorder.getIsGpsRequired(), _locationDialog_checkInListener);
        } else if (_gpsLocationService.hasLocation()) {
            doCheckin();
        } else if (_gpsLocationService.isRunning()) {
            _locationLoadingDialog.show();
        } else if (_gpsLocationService.isLocationServicesEnabled()) {
            _locationLoadingDialog.show();
            _gpsLocationService.startLocation();
        } else {
            // location is disabled, or failed. ask for them to be enabled
            Log.v(TAG, "Should not be here");
        }
*/
//        setLoading(true);
    }

    private void doCheckin() {
//        setLoading(true);
        _gpsLocationService.setListener(null);
        if (_gpsLocationService.hasLocation()) {
            CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_CHECK_OUT, _workOrder, _gpsLocationService.getLocation(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_IN);
        } else {
            CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_CHECK_OUT, _workOrder, CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_IN);

        }
    }

    private final CheckInOutDialog.OnCheckInListener _checkInOutDialog_onCheckIn = new CheckInOutDialog.OnCheckInListener() {
        @Override
        public void onCheckIn(long workOrderId) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CHECK_IN, WorkOrderTracker.Action.CHECK_IN, (int) workOrderId);
        }
    };

    private final LocationDialog.Listener _locationDialog_checkInListener = new LocationDialog.Listener() {
        @Override
        public void onOk() {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, ActivityResultConstants.RESULT_CODE_ENABLE_GPS_CHECKIN);
        }

        @Override
        public void onNotNow() {
            doCheckin();
            setLoading(false);
        }

        @Override
        public void onCancel() {
            setLoading(false);
        }
    };

    private final GpsLocationService.Listener _gps_checkInListener = new GpsLocationService.Listener() {
        @Override
        public void onLocation(Location location) {
            Log.v(TAG, "_gps_checkInListener.onLocation");
            startCheckin();
            if (_locationLoadingDialog != null && _locationLoadingDialog.isVisible()) {
                _locationLoadingDialog.dismiss();
            }
        }
    };

    /*-*********************************************-*/
    /*-				Check Out Process				-*/
    /*-*********************************************-*/

    private void startCheckOut() {
/*
TODO        _gpsLocationService.setListener(_gps_checkOutListener);
        if (!_gpsLocationService.isLocationServicesEnabled()) {
            _locationDialog.show(_workorder.getIsGpsRequired(), _locationDialog_checkOutListener);
        } else if (_gpsLocationService.hasLocation()) {
            doCheckOut();
        } else if (_gpsLocationService.isRunning()) {
            _locationLoadingDialog.show();
        } else if (_gpsLocationService.isLocationServicesEnabled()) {
            _locationLoadingDialog.show();
            _gpsLocationService.startLocation();
        } else {
            // location is disabled, or failed. ask for them to be enabled
            Log.v(TAG, "Should not be here");
        }
*/
//        setLoading(true);
    }

    private void doCheckOut() {
//        setLoading(true);

        Pay pay = _workOrder.getPay();
        if (pay != null && pay.getType().equals("device")) {
            _deviceCount = pay.getRange().getMax().intValue();
        }

        _gpsLocationService.setListener(null);
        if (_gpsLocationService.hasLocation()) {
            if (_deviceCount > -1) {
                CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_CHECK_OUT, _workOrder, _gpsLocationService.getLocation(), _deviceCount, CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            } else {
                CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_CHECK_OUT, _workOrder, _gpsLocationService.getLocation(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            }
        } else {
            if (_deviceCount > -1) {
                CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_CHECK_OUT, _workOrder, _deviceCount, CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            } else {
                CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_CHECK_OUT, _workOrder, CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            }
        }
    }

    private final CheckInOutDialog.OnCheckOutListener _checkInOutDialog_onCheckOut = new CheckInOutDialog.OnCheckOutListener() {
        @Override
        public void onCheckOut(long workOrderId) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CHECK_OUT, WorkOrderTracker.Action.CHECK_OUT, (int) workOrderId);
        }
    };

    private final LocationDialog.Listener _locationDialog_checkOutListener = new LocationDialog.Listener() {
        @Override
        public void onOk() {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, ActivityResultConstants.RESULT_CODE_ENABLE_GPS_CHECKOUT);
        }

        @Override
        public void onNotNow() {
            doCheckOut();
            setLoading(false);
        }

        @Override
        public void onCancel() {
            setLoading(false);
        }
    };

    private final GpsLocationService.Listener _gps_checkOutListener = new GpsLocationService.Listener() {
        @Override
        public void onLocation(Location location) {
            Log.v(TAG, "_gps_checkOutListener.onLocation");
            startCheckOut();
            if (_locationLoadingDialog != null && _locationLoadingDialog.isVisible()) {
                _locationLoadingDialog.dismiss();
            }
        }
    };

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        Log.v(TAG, "WorkFragment#onActivityResult");
        if (!isAdded()) {
            Log.v(TAG, "onActivityResult -> try later");
            _untilAdded.add(new Runnable() {
                @Override
                public void run() {
                    onActivityResult(requestCode, resultCode, data);
                }
            });
            return;
        }

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            Log.e(TAG, "onActivityResult: result not null");
            String content = result.getContents();

            if (content == null) {
                Log.e(TAG, "onActivityResult: no image path");
            } else {
                _scannedImagePath = result.getBarcodeImagePath();
                _shipmentAddDialog.setTrackingId(content);
                _shipmentAddDialog.setSelectedCarrier(misc.getCarrierId(content));
            }
        }

        try {
            Log.v(TAG, "onActivityResult() resultCode= " + resultCode);
            Log.v(TAG, "onActivityResult() requestCode= " + requestCode);

            if ((requestCode == ActivityResultConstants.RESULT_CODE_GET_ATTACHMENT_WORK
                    || requestCode == ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_WORK)
                    && resultCode == Activity.RESULT_OK) {

                _fileCacheClient.subDeliverableCache();

                setLoading(true);

                if (data == null) {
                    Log.e(TAG, "uploading an image using camera");
                    _tempUri = null;
                    _photoUploadDialog.show(_workOrder.getWorkOrderId(), _tempFile.getName());
                    _photoUploadDialog.setPhoto(MemUtils.getMemoryEfficientBitmap(_tempFile.toString(), 400));
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        ClipData clipData = data.getClipData();

                        if (clipData != null) {
                            int count = clipData.getItemCount();
                            Intent intent = new Intent();
                            Uri uri = null;

                            if (count == 1) {
                                _tempUri = data.getData();
                                _tempFile = null;
                                _photoUploadDialog.show(_workOrder.getWorkOrderId(), FileUtils.getFileNameFromUri(App.get(), data.getData()));
                                FileCacheClient.cacheDeliverableUpload(App.get(), data.getData());
                            } else {
                                for (int i = 0; i < count; ++i) {
                                    uri = clipData.getItemAt(i).getUri();
                                    if (uri != null) {
// TODO                                        WorkorderClient.uploadDeliverable(App.get(), _workOrder.getWorkOrderId(), _currentTask.getSlotId(), intent.setData(uri));
                                    }
                                }
                            }
                        } else {
                            Log.v(TAG, "Single local/ non-local file upload");
                            _tempUri = data.getData();
                            _tempFile = null;
                            _photoUploadDialog.show(_workOrder.getWorkOrderId(), FileUtils.getFileNameFromUri(App.get(), data.getData()));
                            FileCacheClient.cacheDeliverableUpload(App.get(), data.getData());
                        }
                    } else {
                        Log.v(TAG, "Android version is pre-4.3");
                        _tempUri = data.getData();
                        _tempFile = null;
                        _photoUploadDialog.show(_workOrder.getWorkOrderId(), FileUtils.getFileNameFromUri(App.get(), data.getData()));
                        FileCacheClient.cacheDeliverableUpload(App.get(), data.getData());
                    }
                }

            } else if (requestCode == ActivityResultConstants.RESULT_CODE_GET_SIGNATURE && resultCode == Activity.RESULT_OK) {
                requestWorkorder();
/*
TODO                if (App.get().getProfile().canRequestWorkOnMarketplace() && !_workOrder.isW2Workorder() && _workorder.getBuyerRatingInfo().getRatingId() == null) {
                    RateBuyerYesNoDialog.show(App.get(), DIALOG_RATE_BUYER_YESNO, _workorder, _workorder.getCompanyName());
                }
*/
            } else if (requestCode == ActivityResultConstants.RESULT_CODE_ENABLE_GPS_CHECKIN) {
                startCheckin();
            } else if (requestCode == ActivityResultConstants.RESULT_CODE_ENABLE_GPS_CHECKOUT) {
                startCheckOut();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.logException(ex);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onActivityResult(requestCode, resultCode, data);
                }
            }, 100);
        }
    }

    /*-*********************************************-*/
    /*-				Main View Listeners				-*/
    /*-*********************************************-*/
    private final View.OnClickListener _test_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    private final ActionBarTopView.Listener _actionbartop_listener = new ActionBarTopView.Listener() {
        @Override
        public void onCheckOut() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CHECK_OUT,
                    null, _workOrder.getWorkOrderId());

            startCheckOut();
        }

        @Override
        public void onCheckIn() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CHECK_IN,
                    null, _workOrder.getWorkOrderId());

            startCheckin();
        }

        @Override
        public void onCheckInAgain() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CHECK_IN_AGAIN,
                    null, _workOrder.getWorkOrderId());

            startCheckin();
        }

        @Override
        public void onAcknowledgeHold() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.ACKNOWLEDGE_HOLD,
                    WorkOrderTracker.Action.ACKNOWLEDGE_HOLD, _workOrder.getWorkOrderId());

            WorkorderClient.actionAcknowledgeHold(App.get(), _workOrder.getWorkOrderId());
            setLoading(true);
        }

        @Override
        public void onMarkIncomplete() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.MARK_INCOMPLETE,
                    null, _workOrder.getWorkOrderId());

            MarkIncompleteWarningDialog.show(App.get(), DIALOG_MARK_INCOMPLETE, _workOrder.getWorkOrderId());
        }

        @Override
        public void onViewPayment() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.VIEW_PAYMENT, null, _workOrder.getWorkOrderId());
/*
TODO            if (_workorder.getPaymentId() != null) {
                PaymentDetailActivity.startNew(App.get(), _workorder.getPaymentId());
            } else {
                PaymentListActivity.startNew(App.get());
            }
*/
        }

        @Override
        public void onReportProblem() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.REPORT_PROBLEM, null, _workOrder.getWorkOrderId());

            ReportProblemDialog.show(App.get(), DIALOG_REPORT_PROBLEM, _workOrder.getWorkOrderId());
        }

        @Override
        public void onMyWay() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.ON_MY_WAY, WorkOrderTracker.Action.ON_MY_WAY, _workOrder.getWorkOrderId());

            if (_gpsLocationService != null && _gpsLocationService.hasLocation() && _gpsLocationService.getLocation() != null) {
                Location location = _gpsLocationService.getLocation();
                WorkOrderClient.actionOnMyWay(App.get(), _workOrder.getWorkOrderId(), location.getLatitude(), location.getLongitude());
            } else {
                WorkOrderClient.actionOnMyWay(App.get(), _workOrder.getWorkOrderId(), null, null);
            }

            try {
                GpsTrackingService.start(App.get(), System.currentTimeMillis() + 3600000); // 1 hours
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNotInterested() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.NOT_INTERESTED, null, _workOrder.getWorkOrderId());

            _declineDialog.show();
        }

        @Override
        public void onRequest() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.REQUEST, null, _workOrder.getWorkOrderId());

            if (_workOrder.getBundle() != null && _workOrder.getBundle().getId() != null && _workOrder.getBundle().getId() > 0) {
                // Todo track bundles... although we don't allow this anymore
                AcceptBundleDialog.show(App.get(), DIALOG_CANCEL_WARNING, _workOrder.getBundle().getId(),
                        _workOrder.getBundle().getMetadata().getTotal(), _workOrder.getWorkOrderId(), AcceptBundleDialog.TYPE_REQUEST);
            } else {
                EtaDialog.show(App.get(), DIALOG_ETA, _workOrder.getWorkOrderId(), _workOrder.getSchedule(), EtaDialog.PARAM_DIALOG_TYPE_REQUEST);
            }
        }

        @Override
        public void onConfirmAssignment() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CONFIRM,
                    null, _workOrder.getWorkOrderId());

            if (_workOrder.getBundle() != null && _workOrder.getBundle().getId() != null && _workOrder.getBundle().getId() > 0) {
                // Todo track bundles... although we don't allow this anymore
                AcceptBundleDialog.show(App.get(), DIALOG_CANCEL_WARNING, _workOrder.getBundle().getId(),
                        _workOrder.getBundle().getMetadata().getTotal(), _workOrder.getWorkOrderId(), AcceptBundleDialog.TYPE_ACCEPT);
            } else {
                EtaDialog.show(App.get(), DIALOG_ETA, _workOrder.getWorkOrderId(),
                        _workOrder.getSchedule(), EtaDialog.PARAM_DIALOG_TYPE_ACCEPT);
            }
        }

        @Override
        public void onWithdraw() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.WITHDRAW, null, _workOrder.getWorkOrderId());

            WithdrawRequestDialog.show(App.get(), DIALOG_WITHDRAW, _workOrder.getWorkOrderId());
        }

        @Override
        public void onViewCounter() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.VIEW_COUNTER_OFFER, null, _workOrder.getWorkOrderId());
// TODO            _counterOfferDialog.show(_workorder);
        }

        @Override
        public void onReadyToGo() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.READY_TO_GO, WorkOrderTracker.Action.READY_TO_GO, _workOrder.getWorkOrderId());

            WorkorderClient.actionReadyToGo(App.get(), _workOrder.getWorkOrderId());
        }

        @Override
        public void onConfirm() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CONFIRM,
                    null, _workOrder.getWorkOrderId());

            EtaDialog.show(App.get(), DIALOG_ETA, _workOrder.getWorkOrderId(),
                    _workOrder.getSchedule(), EtaDialog.PARAM_DIALOG_TYPE_CONFIRM);
        }

        @Override
        public void onEnterClosingNotes() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CLOSING_NOTES,
                    null, _workOrder.getWorkOrderId());

            showClosingNotesDialog();
        }

        @Override
        public void onMarkComplete() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.MARK_COMPlETE,
                    null, _workOrder.getWorkOrderId());

// TODO            MarkCompleteDialog.show(App.get(), DIALOG_MARK_COMPLETE, _workorder);
        }
    };

    private final WorkSummaryView.Listener _summaryView_listener = new WorkSummaryView.Listener() {
        @Override
        public void showConfidentialInfo(String body) {
            WorkOrderTracker.onDescriptionModalEvent(App.get(), WorkOrderTracker.ModalType.CONFIDENTIAL_INFORMATION);
            _termsScrollingDialog.show(getString(R.string.dialog_confidential_information_title), body);
        }

        @Override
        public void showCustomerPolicies(String body) {
            WorkOrderTracker.onDescriptionModalEvent(App.get(), WorkOrderTracker.ModalType.CUSTOMER_POLICIES);
            _termsScrollingDialog.show(getString(R.string.dialog_policy_title), body);
        }

        @Override
        public void showStandardInstructions(String body) {
            WorkOrderTracker.onDescriptionModalEvent(App.get(), WorkOrderTracker.ModalType.STANDARD_INSTRUCTIONS);
            _termsScrollingDialog.show(getString(R.string.dialog_standard_instruction_title), body);
        }
    };

/*
 TODO private final TimeLogListView.Listener _timeLoggedView_listener = new TimeLogListView.Listener() {
        @Override
        public void addWorklog(boolean showdevice) {
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED);
            _worklogDialog.show(getString(R.string.dialog_delete_add_worklog_title), null, showdevice);
        }

        @Override
        public void editWorklog(Workorder workorder, LoggedWork loggedWork, boolean showDeviceCount) {
            WorkOrderTracker.onEditEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED);
            _worklogDialog.show(getString(R.string.dialog_delete_add_worklog_title), loggedWork, showDeviceCount);
        }

        @Override
        public void deleteWorklog(Workorder workorder, LoggedWork loggedWork) {
            final long workorderID = workorder.getWorkorderId();
            final long loggedHoursID = loggedWork.getLoggedHoursId();

            _yesNoDialog.setData(getString(R.string.dialog_delete_worklog_title),
                    getString(R.string.dialog_delete_worklog_body), getString(R.string.btn_yes), getString(R.string.btn_no),
                    new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
                            WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED);
                            WorkorderClient.deleteTimeLog(App.get(), workorderID,
                                    loggedHoursID);
                            setLoading(true);

                        }

                        @Override
                        public void onNegative() {
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
            _yesNoDialog.show();
        }
    };
*/

/*
TODO    private final TaskListView.Listener _taskListView_listener = new TaskListView.Listener() {
        @Override
        public void onCheckin(Task task) {
            startCheckin();
        }

        @Override
        public void onCheckout(Task task) {
            startCheckOut();
        }

        @Override
        public void onCloseOutNotes(Task task) {
            showClosingNotesDialog();
        }

        @Override
        public void onConfirmAssignment(Task task) {
            EtaDialog.show(App.get(), DIALOG_ETA, _workOrder.getWorkOrderId(),
                    _workorder.getScheduleV2(), EtaDialog.PARAM_DIALOG_TYPE_CONFIRM);
        }

        @Override
        public void onCustomField(Task task) {
            for (CustomField cf : _workorder.getCustomFields()) {
                // do not remove the casting here!
                if ((long) cf.getCustomLabelId() == (long) task.getCustomField()) {
                    _customFieldDialog.show(cf);
                    break;
                }
            }
        }

        @Override
        public void onDownload(Task task) {
            Integer _identifier = task.getIdentifier();
            Log.v(TAG, "_identifier: " + _identifier);
            Document[] docs = _workorder.getDocuments();
            if (docs != null && docs.length > 0) {
                for (Document doc : docs) {
                    if (doc != null && doc.getDocumentId() != null && doc.getDocumentId().equals(_identifier)) {
                        Log.v(TAG, "docid: " + doc.getDocumentId());
                        // task completed here
                        if (!task.getCompleted()) {
                            WorkorderClient.actionCompleteTask(App.get(),
                                    _workOrder.getWorkOrderId(), task.getTaskId());
                        }

                        FileHelper.viewOrDownloadFile(getActivity(), doc.getFilePath(),
                                doc.getFileName(), doc.getFileType());
                        break;
                    }
                }
            }
        }

        @Override
        public void onEmail(Task task) {
            String email = task.getEmailAddress();
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + email));
            startActivityForResult(intent, ActivityResultConstants.RESULT_CODE_SEND_EMAIL);

            if (!task.getCompleted()) {
                WorkorderClient.actionCompleteTask(App.get(),
                        _workOrder.getWorkOrderId(), task.getTaskId());
            }
            setLoading(true);
        }

        @Override
        public void onPhone(Task task) {
            if (!task.getCompleted()) {
                WorkorderClient.actionCompleteTask(App.get(),
                        _workOrder.getWorkOrderId(), task.getTaskId());
                setLoading(true);
            }
            try {
                if (task.getPhoneNumber() != null) {
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

                    if (!TextUtils.isEmpty(task.getPhoneNumber()) && android.util.Patterns.PHONE.matcher(task.getPhoneNumber()).matches()) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        String phNum = "tel:" + task.getPhoneNumber();
                        callIntent.setData(Uri.parse(phNum));
                        startActivity(callIntent);
                        setLoading(true);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(R.string.dialog_no_number_message);
                        builder.setTitle(R.string.dialog_no_number_title);
                        builder.setPositiveButton(R.string.btn_ok, null);
                        builder.show();
                    }

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.dialog_no_number_message);
                    builder.setTitle(R.string.dialog_no_number_title);
                    builder.setPositiveButton(R.string.btn_ok, null);
                    builder.show();
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onShipment(Task task) {
            ShipmentTracking[] shipments = _workorder.getShipmentTracking();
            if (shipments == null) {
                _shipmentAddDialog.show(getText(R.string.dialog_shipment_title), task);
            } else {
                _taskShipmentAddDialog.show(getString(R.string.dialog_task_shipment_title), _workorder, task);
            }
        }

        @Override
        public void onSignature(Task task) {
            _currentTask = task;
            SignOffActivity.startSignOff(getActivity(), _workorder, task.getTaskId());
            setLoading(true);
        }

        @Override
        public void onUploadFile(Task task) {
            _currentTask = task;
            _appDialog.show();
        }

        @Override
        public void onUploadPicture(Task task) {
            _currentTask = task;
            _appDialog.show();
        }

        @Override
        public void onUniqueTask(Task task) {
            if (task.getCompleted())
                return;
            WorkorderClient.actionCompleteTask(App.get(),
                    _workOrder.getWorkOrderId(), task.getTaskId());
            setLoading(true);
        }
    };
*/

/*
TODO    private final CustomFieldRowView.Listener _customFields_listener = new CustomFieldRowView.Listener() {
        @Override
        public void onClick(CustomFieldRowView view, CustomField field) {
            _customFieldDialog.show(field);
        }
    };
*/

/*
TODO    private final ShipmentListView.Listener _shipments_listener = new ShipmentListView.Listener() {

        @Override
        public void addShipment() {
            _shipmentAddDialog.show(getString(R.string.dialog_shipment_title), null);
        }

        @Override
        public void onDelete(Workorder workorder, final ShipmentTracking shipment) {
            if ((long) shipment.getUserId() != (long) App.getProfileId()) {
                ToastClient.toast(App.get(), R.string.toast_cant_delete_shipment_permission, Toast.LENGTH_LONG);
                return;
            }

            _yesNoDialog.setData(getString(R.string.dialog_delete_shipment_title),
                    getString(R.string.dialog_delete_shipment_body), getString(R.string.btn_yes), getString(R.string.btn_no),
                    new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
                            WorkorderClient.deleteShipment(App.get(),
                                    _workOrder.getWorkOrderId(), shipment.getWorkorderShipmentId());
                        }

                        @Override
                        public void onNegative() {
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
            _yesNoDialog.show();
        }

        @Override
        public void onAssign(Workorder workorder, ShipmentTracking shipment) {
            // TODO STUB .onAssign()
            Log.v(TAG, "STUB .onAssign()");
            // TODO present a picker of the tasks that this can be assigned too
        }
    };
*/

    private final ClosingNotesView.Listener _clockingNotesView_listener = new ClosingNotesView.Listener() {
        @Override
        public void onChangeClosingNotes(String closingNotes) {
            WorkOrderTracker.onEditEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.CLOSING_NOTES);
            showClosingNotesDialog();
        }
    };

/*
TODO    private final SignatureListView.Listener _signaturelist_listener = new SignatureListView.Listener() {
        @Override
        public void addSignature() {
            SignOffActivity.startSignOff(getActivity(), _workOrder);
            setLoading(true);
        }

        @Override
        public void signatureOnClick(SignatureCardView view, Signature signature) {
            SignatureDisplayActivity.startIntent(getActivity(), signature.getSignatureId(), _workorder);
            setLoading(true);
        }

        @Override
        public boolean signatureOnLongClick(SignatureCardView view, final Signature signature) {
            _yesNoDialog.setData(getString(R.string.dialog_delete_signature_title),
                    getString(R.string.dialog_delete_signature_body), getString(R.string.btn_yes), getString(R.string.btn_no),
                    new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
                            WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SIGNATURES);
                            WorkorderClient.deleteSignature(App.get(),
                                    _workOrder.getWorkOrderId(), signature.getSignatureId());
                        }

                        @Override
                        public void onNegative() {
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
            _yesNoDialog.show();
            return true;
        }
    };
*/

/*
TODO    private final PaymentView.Listener _paymentView_listener = new PaymentView.Listener() {
        @Override
        public void onCounterOffer(Workorder workorder) {
            _counterOfferDialog.show(_workorder);
        }

        @Override
        public void onRequestNewPay(Workorder workorder) {
            // TODO add analytics
            Log.e(TAG, "Inside _paymentView_listener.onRequestNewPay()");
            if (workorder.getIncreaseRequestInfo() != null && workorder.getIncreaseRequestInfo().getPay() != null) {
                _payDialog.show(workorder.getIncreaseRequestInfo().getPay(), true);
            } else {
                _payDialog.show(workorder.getPay(), true);
            }

        }

        @Override
        public void onShowTerms(Workorder workorder) {
            _termsDialog.show(getString(R.string.dialog_terms_title),
                    getString(R.string.dialog_terms_body));
        }
    };
*/

    private final CounterOfferSummaryView.Listener _coSummary_listener = new CounterOfferSummaryView.Listener() {
        @Override
        public void onCounterOffer() {
// TODO            _counterOfferDialog.show(_workorder);
        }
    };


    private final ExpenseListLayout.Listener _expenseListView_listener = new ExpenseListLayout.Listener() {
        @Override
        public void addExpense() {
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.EXPENSES);
            _expenseDialog.show(true);
        }

        @Override
        public void expenseOnClick(Expense expense) {
            //TODO expenseOnClick
        }

        @Override
        public void expenseLongClick(final Expense expense) {
            _yesNoDialog.setData(getString(R.string.dialog_delete_expense_title),
                    getString(R.string.dialog_delete_expense_body), getString(R.string.btn_yes), getString(R.string.btn_no),
                    new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
                            WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.EXPENSES);
                            WorkorderClient.deleteExpense(App.get(),
                                    _workOrder.getWorkOrderId(), expense.getId());
                        }

                        @Override
                        public void onNegative() {
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
            _yesNoDialog.show();
        }
    };


    private final DiscountListLayout.Listener _discountListView_listener = new DiscountListLayout.Listener() {
        @Override
        public void addDiscount() {
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.DISCOUNTS);
            _discountDialog.show(getString(R.string.dialog_add_discount_title));
        }

        @Override
        public void discountOnClick(Discount discount) {
            // TODO discountOnClick
        }

        @Override
        public void discountLongClick(final Discount discount) {
            _yesNoDialog.setData(getString(R.string.dialog_delete_discount_title),
                    getString(R.string.dialog_delete_discount_body), getString(R.string.btn_yes), getString(R.string.btn_no),
                    new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
                            WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.DISCOUNTS);
                            WorkorderClient.deleteDiscount(App.get(),
                                    _workOrder.getWorkOrderId(), discount.getDiscountId());
                        }

                        @Override
                        public void onNegative() {
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
            _yesNoDialog.show();
        }
    };


    private final View.OnClickListener _bundle_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkorderBundleDetailActivity.startNew(App.get(), _workOrder.getWorkOrderId(), _workOrder.getBundle().getId());
            setLoading(true);
        }
    };

    /*-*********************************-*/
    /*-				Dialogs				-*/
    /*-*********************************-*/
    private void showClosingNotesDialog() {
/*
TODO        if (_workorder.canChangeClosingNotes())
            _closingDialog.show(_workorder.getClosingNotes());
*/
    }

    private final AppPickerDialog.Listener _appdialog_listener = new AppPickerDialog.Listener() {

        @Override
        public void onClick(AppPickerPackage pack) {
            Intent src = pack.intent;

            ResolveInfo info = pack.resolveInfo;

            src.setComponent(new ComponentName(
                    info.activityInfo.applicationInfo.packageName,
                    info.activityInfo.name));

            if (src.getAction().equals(Intent.ACTION_GET_CONTENT)) {
                Log.v(TAG, "onClick: " + src.toString());
                startActivityForResult(src, ActivityResultConstants.RESULT_CODE_GET_ATTACHMENT_WORK);
            } else {
                File temppath = new File(App.get().getTempFolder() + "/IMAGE-"
                        + misc.longToHex(System.currentTimeMillis(), 8) + ".png");
                _tempFile = temppath;
                src.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temppath));
                startActivityForResult(src, ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_WORK);
            }
            setLoading(true);
        }
    };

    private final OneButtonDialog.Listener _locationLoadingDialog_listener = new OneButtonDialog.Listener() {
        @Override
        public void onButtonClick() {
            _gpsLocationService.stopLocationUpdates();
            setLoading(false);
        }

        @Override
        public void onCancel() {
            setLoading(false);
        }
    };

    private final ClosingNotesDialog.Listener _closingNotes_onOk = new ClosingNotesDialog.Listener() {
        @Override
        public void onOk(String message) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CLOSING_NOTES, WorkOrderTracker.Action.CLOSING_NOTES, _workOrder.getWorkOrderId());
            WorkOrderTracker.onEditEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.CLOSING_NOTES);
            WorkorderClient.actionSetClosingNotes(App.get(), _workOrder.getWorkOrderId(), message);
            WorkordersWebApi.getWorkOrder(App.get(), _workOrder.getWorkOrderId(), false);
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }
    };

/*
TODO    private final ConfirmDialog.Listener _confirmListener = new ConfirmDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String startDate, long durationMilliseconds) {
            try {
                long end = durationMilliseconds + ISO8601.toUtc(startDate);
                WorkorderClient.actionAcceptAssignment(App.get(),
                        _workOrder.getWorkOrderId(), startDate, ISO8601.fromUTC(end), null, false);
                setLoading(true);

            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onCancel(Workorder workorder) {
        }

        @Override
        public void termsOnClick(Workorder workorder) {
            _termsDialog.show(getString(R.string.dialog_terms_title), getString(R.string.dialog_terms_body));
        }
    };
*/

/*
TODO    private final CounterOfferDialog.Listener _counterOffer_listener = new CounterOfferDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String reason, boolean expires,
                         int expirationInSeconds, Pay pay, Schedule schedule, Expense[] expenses) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.COUNTER_OFFER, WorkOrderTracker.Action.COUNTER_OFFER, workorder.getWorkorderId());
            WorkorderClient.actionCounterOffer(App.get(), workorder.getWorkorderId(), expires,
                    reason, expirationInSeconds, pay, schedule, expenses);
            setLoading(true);
        }
    };
*/

/*
TODO    private final CustomFieldDialog.Listener _customFieldDialog_listener = new CustomFieldDialog.Listener() {
        @Override
        public void onOk(CustomField field, String value) {
            WorkorderClient.actionCustomField(App.get(), _workOrder.getWorkOrderId(),
                    field.getCustomLabelId(), value);
            setLoading(true);
        }
    };
*/

    private final DeclineDialog.Listener _declineDialog_listener = new DeclineDialog.Listener() {
        @Override
        public void onOk() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.NOT_INTERESTED, WorkOrderTracker.Action.NOT_INTERESTED, _workOrder.getWorkOrderId());
            WorkOrderClient.actionDecline(App.get(), _workOrder.getWorkOrderId(), -1, null);
        }

        @Override
        public void onOk(boolean blockBuyer, int blockingReasonId, String blockingExplanation) {
            WorkOrderClient.actionDecline(App.get(), _workOrder.getWorkOrderId(), -1, null);
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.NOT_INTERESTED, WorkOrderTracker.Action.NOT_INTERESTED, _workOrder.getWorkOrderId());

            if (blockBuyer) {
                ProfileClient.actionBlockCompany(App.get(),
                        App.get().getProfile().getUserId(),
                        _workOrder.getCompany().getId(),
                        blockingReasonId, blockingExplanation);
            }
        }

        @Override
        public void onOk(boolean blockBuyer, int declineReasonId, String declineExplanation, int blockingReasonId, String blockingExplanation) {
            WorkOrderClient.actionDecline(App.get(), _workOrder.getWorkOrderId(), declineReasonId, declineExplanation);
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.NOT_INTERESTED, WorkOrderTracker.Action.NOT_INTERESTED, _workOrder.getWorkOrderId());
            if (blockBuyer) {
                ProfileClient.actionBlockCompany(App.get(),
                        App.get().getProfile().getUserId(),
                        _workOrder.getCompany().getId(), blockingReasonId, blockingExplanation);
            }

            getActivity().finish();
        }

        @Override
        public void onOk(int declineReasonId, String declineExplanation) {
            WorkOrderClient.actionDecline(App.get(), _workOrder.getWorkOrderId(), declineReasonId, declineExplanation);
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.NOT_INTERESTED, WorkOrderTracker.Action.NOT_INTERESTED, _workOrder.getWorkOrderId());
        }

        @Override
        public void onCancel() {
        }
    };

    private final DiscountDialog.Listener _discountDialog_listener = new DiscountDialog.Listener() {
        @Override
        public void onOk(String description, double amount) {
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.DISCOUNTS);
            WorkorderClient.createDiscount(App.get(), _workOrder.getWorkOrderId(),
                    description, amount);
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }
    };

    private final EtaDialog.OnRequestedListener _etaDialog_onRequested = new EtaDialog.OnRequestedListener() {
        @Override
        public void onRequested(int workOrderId) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.REQUEST, WorkOrderTracker.Action.REQUEST, workOrderId);
        }
    };

    private final EtaDialog.OnAcceptedListener _etaDialog_onAccepted = new EtaDialog.OnAcceptedListener() {
        @Override
        public void onAccepted(int workOrderId) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.ACCEPT_WORK, WorkOrderTracker.Action.ACCEPT_WORK, workOrderId);
        }
    };

    private final EtaDialog.OnConfirmedListener _etaDialog_onConfirmed = new EtaDialog.OnConfirmedListener() {
        @Override
        public void onConfirmed(int workOrderId) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CONFIRM, WorkOrderTracker.Action.CONFIRM, workOrderId);
        }
    };

    private final ExpenseDialog.Listener _expenseDialog_listener = new ExpenseDialog.Listener() {
        @Override
        public void onOk(String description, double amount, ExpenseCategory category) {
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.EXPENSES);
            WorkorderClient.createExpense(App.get(), _workOrder.getWorkOrderId(), description,
                    amount, category);
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }
    };

    private final ExpiresDialog.Listener _expiresDialog_listener = new ExpiresDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String dateTime) {
            long seconds = -1;
            if (dateTime != null) {
                try {
                    seconds = (ISO8601.toUtc(dateTime) - System.currentTimeMillis()) / 1000;
                } catch (ParseException e) {
                    Log.v(TAG, e);
                }
            }

            WorkorderClient.actionRequest(App.get(), _workOrder.getWorkOrderId(), seconds);
            setLoading(true);
        }
    };


    private final MarkCompleteDialog.OnSignatureClickListener _markCompleteDialog_onSignature = new MarkCompleteDialog.OnSignatureClickListener() {
        @Override
        public void onSignatureClick() {
            new AsyncTaskEx<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... params) {
                    try {
                        Context context = (Context) params[0];
                        WorkOrder workOrder = (WorkOrder) params[1];

                        Intent intent = new Intent(context, SignOffActivity.class);
// TODO                        intent.putExtra(SignOffActivity.INTENT_PARAM_WORKORDER, workOrder);
                        intent.putExtra(SignOffActivity.INTENT_COMPLETE_WORKORDER, true);
                        startActivityForResult(intent, ActivityResultConstants.RESULT_CODE_GET_SIGNATURE);
                        return null;
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                        ToastClient.toast(App.get(), "Could not start signature collection. Please try again.", Toast.LENGTH_LONG);
                    }
                    return null;
                }
            }.executeEx(getActivity(), _workOrder);
        }
    };

    private final MarkCompleteDialog.OnContinueClickListener _markCompleteDialog_onContinue = new MarkCompleteDialog.OnContinueClickListener() {
        @Override
        public void onContinueClick() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.MARK_COMPlETE, WorkOrderTracker.Action.MARK_COMPLETE, _workOrder.getWorkOrderId());
            WorkorderClient.actionComplete(App.get(), _workOrder.getWorkOrderId());
            setLoading(true);
        }
    };

    private final MarkIncompleteWarningDialog.OnMarkIncompleteListener _markIncompleteDialog_markIncomplete = new MarkIncompleteWarningDialog.OnMarkIncompleteListener() {
        @Override
        public void onMarkIncomplete(long workOrderId) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.MARK_INCOMPLETE, WorkOrderTracker.Action.MARK_INCOMPLETE, _workOrder.getWorkOrderId());
            WorkorderClient.actionIncomplete(App.get(), _workOrder.getWorkOrderId());
            setLoading(true);
        }
    };

/*
TODO    private final PayDialog.Listener _payDialog_listener = new PayDialog.Listener() {
        @Override
        public void onComplete(Pay pay, String explanation) {
            // TODO analytics
            WorkorderClient.actionChangePay(App.get(), _workOrder.getWorkOrderId(),
                    pay, explanation);

            populateUi();
        }

        @Override
        public void onNothing() {
        }
    };
*/

    private final PhotoUploadDialog.Listener _photoUploadDialog_listener = new PhotoUploadDialog.Listener() {
        @Override
        public void onOk(long workOrderId, String filename, String photoDescription) {
            Log.e(TAG, "uploading an image using camera");
/*
TODO            if (_tempFile != null) {
                WorkorderClient.uploadDeliverable(App.get(), workOrderId, _currentTask.getSlotId(),
                        filename, _tempFile.getAbsolutePath(), photoDescription);
            } else if (_tempUri != null) {
                WorkorderClient.uploadDeliverable(App.get(), workOrderId, _currentTask.getSlotId(),
                        filename, _tempUri, photoDescription);
            }
*/
        }

        @Override
        public void onImageClick() {
            Intent intent;
            if (_tempUri == null) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(_tempFile), "image/*");

            } else {
                intent = new Intent(Intent.ACTION_VIEW, _tempUri);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            try {
                if (App.get().getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    App.get().startActivity(intent);
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            requestWorkorder();
        }
    };

    private final ReportProblemDialog.OnSendListener _reportProblemDialog_onSend = new ReportProblemDialog.OnSendListener() {
        @Override
        public void onSend(long workorderId, String explanation, ReportProblemType type) {
            if (_workOrder.getWorkOrderId() == workorderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.REPORT_PROBLEM, WorkOrderTracker.Action.REPORT_PROBLEM, workorderId);
        }
    };

    private final ShipmentAddDialog.Listener _shipmentAddDialog_listener = new ShipmentAddDialog.Listener() {
        @Override
        public void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite) {
/*
TODO            if (_scannedImagePath != null) {
                final UploadSlot[] slots = _workorder.getUploadSlots();
                if (slots == null) return;
                for (UploadSlot uploadSlot : slots) {
                    if (uploadSlot.getSlotName().equalsIgnoreCase("misc")) {
                        String fileName = _scannedImagePath.substring(_scannedImagePath.lastIndexOf(File.separator) + 1, _scannedImagePath.length());
                        WorkorderClient.uploadDeliverable(App.get(), _workOrder.getWorkOrderId(),
                                uploadSlot.getSlotId(), fileName, _scannedImagePath);
                        _scannedImagePath = null;
                    }
                }
            }
*/

            WorkorderClient.createShipment(App.get(), _workOrder.getWorkOrderId(), description, shipToSite,
                    carrier, carrierName, trackingId);
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SHIPMENTS);
            setLoading(true);
        }

        @Override
        public void onOk(String trackingId, String carrier, String carrierName, String description,
                         boolean shipToSite, long taskId) {
            Log.v(TAG, "ShipmentAddDialog#onOk");

/*
TODO            if (_scannedImagePath != null) {
                final UploadSlot[] slots = _workorder.getUploadSlots();
                if (slots == null) return;
                for (UploadSlot uploadSlot : slots) {
                    if (uploadSlot.getSlotName().equalsIgnoreCase("misc")) {
                        String fileName = _scannedImagePath.substring(_scannedImagePath.lastIndexOf(File.separator) + 1, _scannedImagePath.length());
                        WorkorderClient.uploadDeliverable(App.get(), _workOrder.getWorkOrderId(),
                                uploadSlot.getSlotId(), fileName, _scannedImagePath);
                        _scannedImagePath = null;
                    }
                }
            }
*/

            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SHIPMENTS);
            WorkorderClient.createShipment(App.get(), _workOrder.getWorkOrderId(), description, shipToSite,
                    carrier, carrierName, trackingId, taskId);
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onScan() {
            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setPrompt(getString(R.string.dialog_scan_barcode_title));
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        }
    };

/*
TODO    private final TaskShipmentAddDialog.Listener taskShipmentAddDialog_listener = new TaskShipmentAddDialog.Listener() {
        @Override
        public void onDelete(Workorder workorder, ShipmentTracking shipment) {
            WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SHIPMENTS);
            WorkorderClient.deleteShipment(App.get(), workorder.getWorkorderId(), shipment.getWorkorderShipmentId());
            setLoading(true);
        }

        @Override
        public void onAssign(Workorder workorder, int shipmentId, long taskId) {
            WorkorderClient.actionCompleteShipmentTask(App.get(), workorder.getWorkorderId(), shipmentId, taskId);
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onAddShipmentDetails(Workorder workorder, String trackingId, String carrier, String carrierName, String description, boolean shipToSite) {
            WorkOrderTracker.onEditEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SHIPMENTS);
            WorkorderClient.actionSetShipmentDetails(App.get(), workorder.getWorkorderId(), description,
                    shipToSite, carrier, carrierName, trackingId);
            setLoading(true);
        }

        @Override
        public void onAddShipmentDetails(Workorder workorder, String trackingId, String carrier, String carrierName, String description, boolean shipToSite, long taskId) {
            WorkOrderTracker.onEditEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SHIPMENTS);
            WorkorderClient.actionSetShipmentDetails(App.get(), workorder.getWorkorderId(), description,
                    shipToSite, carrier, carrierName, trackingId, taskId);
            setLoading(true);
        }

        @Override
        public void onScan() {
            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setPrompt(getString(R.string.dialog_scan_barcode_title));
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        }

        @Override
        public void onAddShipment(ShipmentTracking shipment, Task task) {
            _shipmentAddDialog.show(getText(R.string.dialog_shipment_title), shipment.getName(), task);
        }

        @Override
        public void onAddShipment(Task task) {
            _shipmentAddDialog.show(getText(R.string.dialog_shipment_title), task);
        }

    };
*/

    private final WithdrawRequestDialog.OnWithdrawListener _withdrawRequestDialog_onWithdraw = new WithdrawRequestDialog.OnWithdrawListener() {
        @Override
        public void onWithdraw(long workOrderId) {
            if (_workOrder.getWorkOrderId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.WITHDRAW, WorkOrderTracker.Action.WITHDRAW, workOrderId);
        }
    };

/*
TODO    private final WorkLogDialog.Listener _worklogDialog_listener = new WorkLogDialog.Listener() {
        @Override
        public void onOk(LoggedWork loggedWork, Calendar start, Calendar end, int deviceCount) {
            if (loggedWork == null) {
                WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED);
                if (deviceCount <= 0) {
                    WorkorderClient.addTimeLog(App.get(), _workOrder.getWorkOrderId(),
                            start.getTimeInMillis(), end.getTimeInMillis());
                } else {
                    WorkorderClient.addTimeLog(App.get(), _workOrder.getWorkOrderId(),
                            start.getTimeInMillis(), end.getTimeInMillis(), deviceCount);
                }
            } else {
                WorkOrderTracker.onEditEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED);
                if (deviceCount <= 0) {
                    WorkorderClient.updateTimeLog(App.get(), _workOrder.getWorkOrderId(),
                            loggedWork.getLoggedHoursId(), start.getTimeInMillis(), end.getTimeInMillis());
                } else {
                    WorkorderClient.updateTimeLog(App.get(), _workOrder.getWorkOrderId(),
                            loggedWork.getLoggedHoursId(), start.getTimeInMillis(), end.getTimeInMillis(), deviceCount);
                }
            }
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }
    };
*/

    /*-*****************************-*/
    /*-				Web				-*/
    /*-*****************************-*/

    private void subscribeData() {
/*
TODO        if (_workorder == null)
            return;

        if (_workorderClient == null)
            return;

        if (!_workorderClient.isConnected())
            return;

        _workorderClient.subListTasks(_workOrder.getWorkOrderId(), false);
*/
    }

    private final FileCacheClient.Listener _fileCacheClient_listener = new FileCacheClient.Listener() {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDeliverableCacheEnd(Uri uri, String filename) {
            _tempUri = uri;
            _tempFile = null;
            _photoUploadDialog.setPhoto(MemUtils.getMemoryEfficientBitmap(filename, 400));
        }
    };

    private final WorkordersWebApi.Listener _workOrderApi_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            subscribeData();
        }

/*
        @Override
TODO        public void onTaskList(long workorderId, List<Task> tasks, boolean failed) {
            setTasks(tasks);
        }
*/
    };
}


