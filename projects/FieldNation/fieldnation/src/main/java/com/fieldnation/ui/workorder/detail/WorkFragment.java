package com.fieldnation.ui.workorder.detail;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fngps.SimpleGps;
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
import com.fieldnation.ui.OverScrollView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.SignOffActivity;
import com.fieldnation.ui.SignatureCardView;
import com.fieldnation.ui.SignatureDisplayActivity;
import com.fieldnation.ui.SignatureListView;
import com.fieldnation.ui.dialog.DeclineDialog;
import com.fieldnation.ui.dialog.ExpiresDialog;
import com.fieldnation.ui.dialog.PhotoUploadDialog;
import com.fieldnation.ui.dialog.TermsScrollingDialog;
import com.fieldnation.ui.dialog.TwoButtonDialog;
import com.fieldnation.ui.dialog.v2.AcceptBundleDialog;
import com.fieldnation.ui.dialog.v2.ReportProblemDialog;
import com.fieldnation.ui.workorder.WorkOrderActivity;
import com.fieldnation.ui.workorder.WorkorderBundleDetailActivity;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.CheckInOut;
import com.fieldnation.v2.data.model.CustomField;
import com.fieldnation.v2.data.model.Date;
import com.fieldnation.v2.data.model.Expense;
import com.fieldnation.v2.data.model.ExpenseCategory;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.PayIncrease;
import com.fieldnation.v2.data.model.PayModifier;
import com.fieldnation.v2.data.model.Request;
import com.fieldnation.v2.data.model.Schedule;
import com.fieldnation.v2.data.model.Shipment;
import com.fieldnation.v2.data.model.ShipmentCarrier;
import com.fieldnation.v2.data.model.Signature;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.TimeLog;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.AppPickerIntent;
import com.fieldnation.v2.ui.dialog.AppPickerDialog;
import com.fieldnation.v2.ui.dialog.CheckInOutDialog;
import com.fieldnation.v2.ui.dialog.ClosingNotesDialog;
import com.fieldnation.v2.ui.dialog.CounterOfferDialog;
import com.fieldnation.v2.ui.dialog.CustomFieldDialog;
import com.fieldnation.v2.ui.dialog.DiscountDialog;
import com.fieldnation.v2.ui.dialog.EtaDialog;
import com.fieldnation.v2.ui.dialog.ExpenseDialog;
import com.fieldnation.v2.ui.dialog.LocationDialog;
import com.fieldnation.v2.ui.dialog.MarkCompleteDialog;
import com.fieldnation.v2.ui.dialog.MarkIncompleteWarningDialog;
import com.fieldnation.v2.ui.dialog.OneButtonDialog;
import com.fieldnation.v2.ui.dialog.PayDialog;
import com.fieldnation.v2.ui.dialog.ShipmentAddDialog;
import com.fieldnation.v2.ui.dialog.TaskShipmentAddDialog;
import com.fieldnation.v2.ui.dialog.TermsDialog;
import com.fieldnation.v2.ui.dialog.WithdrawRequestDialog;
import com.fieldnation.v2.ui.dialog.WorkLogDialog;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class WorkFragment extends WorkorderFragment {
    private static final String TAG = "WorkFragment";

    // Dialog tags
    private static final String DIALOG_APP_PICKER_DIALOG = TAG + ".appPickerDialog";
    private static final String DIALOG_CANCEL_WARNING = TAG + ".cancelWarningDialog";
    private static final String DIALOG_CHECK_IN_CHECK_OUT = TAG + ".checkInOutDialog";
    private static final String DIALOG_CLOSING_NOTES = TAG + ".closingNotesDialog";
    private static final String DIALOG_COUNTER_OFFER = TAG + ".counterOfferDialog";
    private static final String DIALOG_CUSTOM_FIELD = TAG + ".customFieldDialog";
    private static final String DIALOG_DISCOUNT = TAG + ".discountDialog";
    private static final String DIALOG_ETA = TAG + ".etaDialog";
    private static final String DIALOG_EXPENSE = TAG + ".expenseDialog";
    private static final String DIALOG_LOCATION_DIALOG_CHECK_IN = TAG + ".locationDialogCheckIn";
    private static final String DIALOG_LOCATION_DIALOG_CHECK_OUT = TAG + ".locationDialogCheckOut";
    private static final String DIALOG_LOCATION_LOADING = TAG + ".locationLoadingDialog";
    private static final String DIALOG_MARK_COMPLETE = TAG + ".markCompleteDialog";
    private static final String DIALOG_MARK_INCOMPLETE = TAG + ".markIncompleteDialog";
    private static final String DIALOG_PAY = TAG + ".payDialog";
    private static final String DIALOG_RATE_BUYER_YESNO = TAG + ".rateBuyerYesNoDialog";
    private static final String DIALOG_REPORT_PROBLEM = TAG + ".reportProblemDialog";
    private static final String DIALOG_RUNNING_LATE = TAG + ".runningLateDialogLegacy";
    private static final String DIALOG_SHIPMENT_ADD = TAG + ".shipmentAddDialog";
    private static final String DIALOG_TASK_SHIPMENT_ADD = TAG + ".taskShipmentAddDialog";
    private static final String DIALOG_TERMS = TAG + ".termsDialog";
    private static final String DIALOG_WITHDRAW = TAG + ".withdrawRequestDialog";
    private static final String DIALOG_WORKLOG = TAG + ".worklogDialog";

    // saved state keys
    private static final String STATE_WORKORDER = "WorkFragment:STATE_WORKORDER";
    private static final String STATE_CURRENT_TASK = "WorkFragment:STATE_CURRENT_TASK";
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
    private List<WorkOrderRenderer> _renderers = new LinkedList<>();

    // Dialogs
    private DeclineDialog _declineDialog;
    private TermsScrollingDialog _termsScrollingDialog;
    private TwoButtonDialog _yesNoDialog;
    private ReportProblemDialog _reportProblemDialog;
    private PhotoUploadDialog _photoUploadDialog;

    // Data
    private WorkordersWebApi _workOrderApi;
    private FileCacheClient _fileCacheClient;
    private WorkordersWebApi _workorderApi;
    private File _tempFile;
    private Uri _tempUri;
    private WorkOrder _workOrder;
    private int _deviceCount = -1;
    private String _scannedImagePath;
    private Location _currentLocation;
    private boolean _locationFailed = false;

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

        _renderers.clear();

        _testButton = (Button) view.findViewById(R.id.test_button);
        _testButton.setOnClickListener(_test_onClick);

        _topBar = (ActionBarTopView) view.findViewById(R.id.actiontop_view);
        _topBar.setListener(_actionbartop_listener);
        _renderers.add(_topBar);

        _sumView = (WorkSummaryView) view.findViewById(R.id.summary_view);
        _sumView.setListener(_summaryView_listener);
        _renderers.add(_sumView);

        _companySummaryView = (CompanySummaryView) view.findViewById(R.id.companySummary_view);
        _renderers.add(_companySummaryView);

        _contactListView = (ContactListView) view.findViewById(R.id.contactList_view);
        _renderers.add(_contactListView);

        _locView = (LocationView) view.findViewById(R.id.location_view);
        _renderers.add(_locView);

        _scheduleView = (ScheduleSummaryView) view.findViewById(R.id.schedule_view);
        _renderers.add(_scheduleView);

        _payView = (PaymentView) view.findViewById(R.id.payment_view);
        _payView.setListener(_paymentView_listener);
        _renderers.add(_payView);

        _coSummaryView = (CounterOfferSummaryView) view.findViewById(R.id.counterOfferSummary_view);
        _coSummaryView.setListener(_coSummary_listener);
        _renderers.add(_coSummaryView);

        _expenseListView = (ExpenseListLayout) view.findViewById(R.id.expenseListLayout_view);
        _expenseListView.setListener(_expenseListView_listener);
        _renderers.add(_expenseListView);

        _discountListView = (DiscountListLayout) view.findViewById(R.id.discountListLayout_view);
        _discountListView.setListener(_discountListView_listener);
        _renderers.add(_discountListView);

        _exView = (ExpectedPaymentView) view.findViewById(R.id.expected_pay_view);
        _renderers.add(_exView);

        _bundleWarningTextView = (TextView) view.findViewById(R.id.bundlewarning2_textview);
        _bundleWarningTextView.setOnClickListener(_bundle_onClick);

        _refreshView = (RefreshView) view.findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _scrollView = (OverScrollView) view.findViewById(R.id.scroll_view);
        _scrollView.setOnOverScrollListener(_refreshView);

        _shipments = (ShipmentListView) view.findViewById(R.id.shipment_view);
        _shipments.setListener(_shipments_listener);
        _renderers.add(_shipments);

        _taskList = (TaskListView) view.findViewById(R.id.scope_view);
// TODO        _taskList.setTaskListViewListener(_taskListView_listener);
// TODO        _renderers.add(_taskList);

        _timeLogged = (TimeLogListView) view.findViewById(R.id.timelogged_view);
        _timeLogged.setListener(_timeLoggedView_listener);
        _renderers.add(_timeLogged);

        _closingNotes = (ClosingNotesView) view.findViewById(R.id.closingnotes_view);
        _closingNotes.setListener(_closingNotesView_listener);
        _renderers.add(_closingNotes);

        _customFields = (CustomFieldListView) view.findViewById(R.id.customfields_view);
        _customFields.setListener(_customFields_listener);
        _renderers.add(_customFields);

        _signatureView = (SignatureListView) view.findViewById(R.id.signature_view);
        _signatureView.setListener(_signatureList_listener);
        _renderers.add(_signatureView);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDER)) {
                _workOrder = savedInstanceState.getParcelable(STATE_WORKORDER);
            }
            if (savedInstanceState.containsKey(STATE_CURRENT_TASK)) {
// TODO                _currentTask = savedInstanceState.getParcelable(STATE_CURRENT_TASK);
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
    }

    private void startAppPickerDialog() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        AppPickerIntent intent1 = new AppPickerIntent(intent, "Get Content");

        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            AppPickerIntent intent2 = new AppPickerIntent(intent, "Take Picture");
            AppPickerDialog.show(App.get(), DIALOG_APP_PICKER_DIALOG, new AppPickerIntent[]{intent1, intent2});
        } else {
            AppPickerDialog.show(App.get(), DIALOG_APP_PICKER_DIALOG, new AppPickerIntent[]{intent1});
        }
    }

    @Override
    public void onAttach(Activity activity) {
        Log.v(TAG, "onAttach");
        super.onAttach(activity);
        _declineDialog = DeclineDialog.getInstance(getFragmentManager(), TAG);
//        _deviceCountDialog = DeviceCountDialog.getInstance(getFragmentManager(), TAG);
        _termsScrollingDialog = TermsScrollingDialog.getInstance(getFragmentManager(), TAG);
        _yesNoDialog = TwoButtonDialog.getInstance(getFragmentManager(), TAG);
        _photoUploadDialog = PhotoUploadDialog.getInstance(getFragmentManager(), TAG);
        _declineDialog.setListener(_declineDialog_listener);
// TODO        _taskShipmentAddDialog.setListener(taskShipmentAddDialog_listener);
        _photoUploadDialog.setListener(_photoUploadDialog_listener);

        AppPickerDialog.addOnOkListener(DIALOG_APP_PICKER_DIALOG, _appPicker_onOk);
        CheckInOutDialog.addOnCheckInListener(DIALOG_CHECK_IN_CHECK_OUT, _checkInOutDialog_onCheckIn);
        CheckInOutDialog.addOnCheckOutListener(DIALOG_CHECK_IN_CHECK_OUT, _checkInOutDialog_onCheckOut);
        ClosingNotesDialog.addOnOkListener(DIALOG_CLOSING_NOTES, _closingNotes_onOk);
        CounterOfferDialog.addOnOkListener(DIALOG_COUNTER_OFFER, _counterOfferDialog_onOk);
        CustomFieldDialog.addOnOkListener(DIALOG_CUSTOM_FIELD, _customfieldDialog_onOk);
        DiscountDialog.addOnOkListener(DIALOG_DISCOUNT, _discountDialog_onOk);
        EtaDialog.addOnRequestedListener(DIALOG_ETA, _etaDialog_onRequested);
        EtaDialog.addOnAcceptedListener(DIALOG_ETA, _etaDialog_onAccepted);
        EtaDialog.addOnConfirmedListener(DIALOG_ETA, _etaDialog_onConfirmed);
        ExpenseDialog.addOnOkListener(DIALOG_EXPENSE, _expenseDialog_onOk);
        ReportProblemDialog.addOnSendListener(DIALOG_REPORT_PROBLEM, _reportProblemDialog_onSend);
        ShipmentAddDialog.addOnOkListener(DIALOG_SHIPMENT_ADD, _shipmentAddDialog_onOk);
        TaskShipmentAddDialog.addOnAddShipmentListener(DIALOG_TASK_SHIPMENT_ADD, _taskShipmentAddDialog_onAdd);
        TaskShipmentAddDialog.addOnDeleteListener(DIALOG_TASK_SHIPMENT_ADD, _taskShipmentAddDialog_onDelete);
        WithdrawRequestDialog.addOnWithdrawListener(DIALOG_WITHDRAW, _withdrawRequestDialog_onWithdraw);
        MarkCompleteDialog.addOnContinueClickListener(DIALOG_MARK_COMPLETE, _markCompleteDialog_onContinue);
        MarkCompleteDialog.addOnSignatureClickListener(DIALOG_MARK_COMPLETE, _markCompleteDialog_onSignature);
        MarkIncompleteWarningDialog.addOnMarkIncompleteListener(DIALOG_MARK_INCOMPLETE, _markIncompleteDialog_markIncomplete);
        WorkLogDialog.addOnOkListener(DIALOG_WORKLOG, _worklogDialog_listener);

        OneButtonDialog.addOnPrimaryListener(DIALOG_LOCATION_LOADING, _locationLoadingDialog_onOk);
        OneButtonDialog.addOnCanceledListener(DIALOG_LOCATION_LOADING, _locationLoadingDialog_onCancel);

        LocationDialog.addOnOkListener(DIALOG_LOCATION_DIALOG_CHECK_IN, _locationDialog_onOkCheckIn);
        LocationDialog.addOnCancelListener(DIALOG_LOCATION_DIALOG_CHECK_IN, _locationDialog_onCancelCheckIn);
        LocationDialog.addOnNotNowListener(DIALOG_LOCATION_DIALOG_CHECK_IN, _locationDialog_onNotNowCheckIn);

        LocationDialog.addOnOkListener(DIALOG_LOCATION_DIALOG_CHECK_OUT, _locationDialog_onOkCheckOut);
        LocationDialog.addOnCancelListener(DIALOG_LOCATION_DIALOG_CHECK_OUT, _locationDialog_onCancelCheckOut);
        LocationDialog.addOnNotNowListener(DIALOG_LOCATION_DIALOG_CHECK_OUT, _locationDialog_onNotNowCheckOut);

        PayDialog.addOnCompleteListener(DIALOG_PAY, _payDialog_onComplete);


        _workorderApi = new WorkordersWebApi(_workOrderApi_listener);
        _workorderApi.connect(App.get());

        _fileCacheClient = new FileCacheClient(_fileCacheClient_listener);
        _fileCacheClient.connect(App.get());

        while (_untilAdded.size() > 0) {
            _untilAdded.remove(0).run();
        }
    }

    @Override
    public void onDetach() {
        Log.v(TAG, "onDetach");

        AppPickerDialog.removeOnOkListener(DIALOG_APP_PICKER_DIALOG, _appPicker_onOk);
        CheckInOutDialog.removeOnCheckInListener(DIALOG_CHECK_IN_CHECK_OUT, _checkInOutDialog_onCheckIn);
        CheckInOutDialog.removeOnCheckOutListener(DIALOG_CHECK_IN_CHECK_OUT, _checkInOutDialog_onCheckOut);
        ClosingNotesDialog.removeOnOkListener(DIALOG_CLOSING_NOTES, _closingNotes_onOk);
        CounterOfferDialog.removeOnOkListener(DIALOG_COUNTER_OFFER, _counterOfferDialog_onOk);
        CustomFieldDialog.removeOnOkListener(DIALOG_CUSTOM_FIELD, _customfieldDialog_onOk);
        DiscountDialog.removeOnOkListener(DIALOG_DISCOUNT, _discountDialog_onOk);
        EtaDialog.removeOnRequestedListener(DIALOG_ETA, _etaDialog_onRequested);
        EtaDialog.removeOnAcceptedListener(DIALOG_ETA, _etaDialog_onAccepted);
        EtaDialog.removeOnConfirmedListener(DIALOG_ETA, _etaDialog_onConfirmed);
        ExpenseDialog.removeOnOkListener(DIALOG_EXPENSE, _expenseDialog_onOk);
        ReportProblemDialog.removeOnSendListener(DIALOG_REPORT_PROBLEM, _reportProblemDialog_onSend);
        ShipmentAddDialog.removeOnOkListener(DIALOG_SHIPMENT_ADD, _shipmentAddDialog_onOk);
        TaskShipmentAddDialog.removeOnAddShipmentListener(DIALOG_TASK_SHIPMENT_ADD, _taskShipmentAddDialog_onAdd);
        TaskShipmentAddDialog.removeOnDeleteListener(DIALOG_TASK_SHIPMENT_ADD, _taskShipmentAddDialog_onDelete);
        WithdrawRequestDialog.removeOnWithdrawListener(DIALOG_WITHDRAW, _withdrawRequestDialog_onWithdraw);
        MarkCompleteDialog.removeOnContinueClickListener(DIALOG_MARK_COMPLETE, _markCompleteDialog_onContinue);
        MarkCompleteDialog.removeOnSignatureClickListener(DIALOG_MARK_COMPLETE, _markCompleteDialog_onSignature);
        MarkIncompleteWarningDialog.removeOnMarkIncompleteListener(DIALOG_MARK_INCOMPLETE, _markIncompleteDialog_markIncomplete);
        WorkLogDialog.removeOnOkListener(DIALOG_WORKLOG, _worklogDialog_listener);

        OneButtonDialog.removeOnPrimaryListener(DIALOG_LOCATION_LOADING, _locationLoadingDialog_onOk);
        OneButtonDialog.removeOnCanceledListener(DIALOG_LOCATION_LOADING, _locationLoadingDialog_onCancel);

        LocationDialog.removeOnOkListener(DIALOG_LOCATION_DIALOG_CHECK_IN, _locationDialog_onOkCheckIn);
        LocationDialog.removeOnCancelListener(DIALOG_LOCATION_DIALOG_CHECK_IN, _locationDialog_onCancelCheckIn);
        LocationDialog.removeOnNotNowListener(DIALOG_LOCATION_DIALOG_CHECK_IN, _locationDialog_onNotNowCheckIn);

        LocationDialog.removeOnOkListener(DIALOG_LOCATION_DIALOG_CHECK_OUT, _locationDialog_onOkCheckOut);
        LocationDialog.removeOnCancelListener(DIALOG_LOCATION_DIALOG_CHECK_OUT, _locationDialog_onCancelCheckOut);
        LocationDialog.removeOnNotNowListener(DIALOG_LOCATION_DIALOG_CHECK_OUT, _locationDialog_onNotNowCheckOut);

        PayDialog.removeOnCompleteListener(DIALOG_PAY, _payDialog_onComplete);

        if (_workorderApi != null && _workorderApi.isConnected())
            _workorderApi.disconnect(App.get());

        if (_fileCacheClient != null && _fileCacheClient.isConnected())
            _fileCacheClient.disconnect(App.get());

        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();

        new SimpleGps(App.get()).updateListener(_simpleGps_listener).numUpdates(1).start(App.get());
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause");
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

        for (WorkOrderRenderer renderer : _renderers) {
            renderer.setWorkOrder(_workOrder);
        }

        setLoading(false);

        if (_bundleWarningTextView != null) {
            Stopwatch watch = new Stopwatch(true);
            if (_workOrder.getBundle() != null && _workOrder.getBundle().getId() != null && _workOrder.getBundle().getId() > 0) {
                _bundleWarningTextView.setVisibility(View.VISIBLE);
            } else {
                _bundleWarningTextView.setVisibility(View.GONE);
            }
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
    private final Runnable _startCheckIn = new Runnable() {
        @Override
        public void run() {
            if (_currentLocation == null && !_locationFailed) {
                OneButtonDialog.show(App.get(), DIALOG_LOCATION_LOADING,
                        R.string.dialog_location_loading_title, R.string.dialog_location_loading_body,
                        R.string.dialog_location_loading_button, true);
            } else if (_currentLocation != null) {
                doCheckin();
            } else if (_locationFailed) {

            }
            setLoading(true);
        }
    };


    private void doCheckin() {
//        setLoading(true);
        if (_currentLocation != null) {
            CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_CHECK_OUT, _workOrder, _currentLocation, CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_IN);
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

    private final LocationDialog.OnOkListener _locationDialog_onOkCheckIn = new LocationDialog.OnOkListener() {
        @Override
        public void onOk() {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, ActivityResultConstants.RESULT_CODE_ENABLE_GPS_CHECKIN);
        }
    };

    private final LocationDialog.OnNotNowListener _locationDialog_onNotNowCheckIn = new LocationDialog.OnNotNowListener() {
        @Override
        public void onNotNow() {
            doCheckin();
            setLoading(false);
        }
    };

    private final LocationDialog.OnCancelListener _locationDialog_onCancelCheckIn = new LocationDialog.OnCancelListener() {
        @Override
        public void onCancel() {
            setLoading(false);
        }
    };

    /*-*********************************************-*/
    /*-				Check Out Process				-*/
    /*-*********************************************-*/
    private final Runnable _startCheckOut = new Runnable() {
        @Override
        public void run() {
            if (_currentLocation == null && !_locationFailed) {
                // waiting for response
                OneButtonDialog.show(App.get(), DIALOG_LOCATION_LOADING,
                        R.string.dialog_location_loading_title, R.string.dialog_location_loading_body,
                        R.string.dialog_location_loading_button, true);
                _gpsRunnable = _startCheckOut;
            } else if (_currentLocation != null) {
                // have location
                doCheckOut();
            } else if (_locationFailed) {
                // location failed
            }
            setLoading(true);
        }
    };

    private void doCheckOut() {
//        setLoading(true);

        Pay pay = _workOrder.getPay();
        if (pay != null && pay.getType().equals("device")) {
            _deviceCount = pay.getRange().getMax().intValue();
        }

        if (_currentLocation != null) {
            if (_deviceCount > -1) {
                CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_CHECK_OUT, _workOrder, _currentLocation, _deviceCount, CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            } else {
                CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_CHECK_OUT, _workOrder, _currentLocation, CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
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

    private final LocationDialog.OnOkListener _locationDialog_onOkCheckOut = new LocationDialog.OnOkListener() {
        @Override
        public void onOk() {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, ActivityResultConstants.RESULT_CODE_ENABLE_GPS_CHECKOUT);
        }
    };

    private final LocationDialog.OnNotNowListener _locationDialog_onNotNowCheckOut = new LocationDialog.OnNotNowListener() {
        @Override
        public void onNotNow() {
            doCheckOut();
            setLoading(false);
        }
    };

    private final LocationDialog.OnCancelListener _locationDialog_onCancelCheckOut = new LocationDialog.OnCancelListener() {
        @Override
        public void onCancel() {
            setLoading(false);
        }
    };

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private Runnable _gpsRunnable = null;

    private final SimpleGps.Listener _simpleGps_listener = new SimpleGps.Listener() {
        @Override
        public void onLocation(SimpleGps simpleGps, Location location) {
            simpleGps.stop();
            _currentLocation = location;
            _locationFailed = false;
            if (_gpsRunnable != null) _gpsRunnable.run();
            Controller.dismiss(App.get(), DIALOG_LOCATION_LOADING);
        }

        @Override
        public void onFail(SimpleGps simpleGps) {
            _locationFailed = true;
            simpleGps.stop();
            if (_gpsRunnable != null) _gpsRunnable.run();
            Controller.dismiss(App.get(), DIALOG_LOCATION_LOADING);
        }
    };

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
                _startCheckIn.run();
            } else if (requestCode == ActivityResultConstants.RESULT_CODE_ENABLE_GPS_CHECKOUT) {
                _startCheckOut.run();
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
            CounterOfferDialog.show(App.get(), DIALOG_COUNTER_OFFER, _workOrder);
        }
    };

    private final ActionBarTopView.Listener _actionbartop_listener = new ActionBarTopView.Listener() {
        @Override
        public void onCheckOut() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CHECK_OUT,
                    null, _workOrder.getWorkOrderId());

            _startCheckOut.run();
        }

        @Override
        public void onCheckIn() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CHECK_IN,
                    null, _workOrder.getWorkOrderId());

            _startCheckIn.run();
        }

        @Override
        public void onCheckInAgain() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CHECK_IN_AGAIN,
                    null, _workOrder.getWorkOrderId());

            _startCheckIn.run();
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

            if (_currentLocation != null) {
                WorkOrderClient.actionOnMyWay(App.get(), _workOrder.getWorkOrderId(), _currentLocation.getLatitude(), _currentLocation.getLongitude());
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
        public void onEta() {

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

            WithdrawRequestDialog.show(App.get(), DIALOG_WITHDRAW, _workOrder.getWorkOrderId(), _workOrder.getRequests().getOpenRequest().getId());
        }

        @Override
        public void onViewCounter() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.VIEW_COUNTER_OFFER, null, _workOrder.getWorkOrderId());
            CounterOfferDialog.show(App.get(), DIALOG_COUNTER_OFFER, _workOrder);
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

            MarkCompleteDialog.show(App.get(), DIALOG_MARK_COMPLETE, _workOrder);
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


    private final TimeLogListView.Listener _timeLoggedView_listener = new TimeLogListView.Listener() {
        @Override
        public void addWorklog(boolean showdevice) {
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED);
            WorkLogDialog.show(App.get(), DIALOG_WORKLOG, null, showdevice);
        }

        @Override
        public void editWorklog(WorkOrder workOrder, TimeLog timeLog, boolean showDeviceCount) {
            WorkOrderTracker.onEditEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED);
            WorkLogDialog.show(App.get(), DIALOG_WORKLOG, timeLog, showDeviceCount);
        }

        @Override
        public void deleteWorklog(WorkOrder workOrder, TimeLog timeLog) {
            final long workorderID = workOrder.getWorkOrderId();
            final long timeLogId = timeLog.getId();

            _yesNoDialog.setData(getString(R.string.dialog_delete_worklog_title),
                    getString(R.string.dialog_delete_worklog_body), getString(R.string.btn_yes), getString(R.string.btn_no),
                    new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
                            WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED);
                            WorkordersWebApi.removeTimeLog(App.get(), _workOrder.getWorkOrderId(), (int) timeLogId);
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
            startAppPickerDialog();
        }

        @Override
        public void onUploadPicture(Task task) {
            _currentTask = task;
            startAppPickerDialog();
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

    private final CustomFieldRowView.Listener _customFields_listener = new CustomFieldRowView.Listener() {
        @Override
        public void onClick(CustomFieldRowView view, CustomField field) {
            CustomFieldDialog.show(App.get(), DIALOG_CUSTOM_FIELD, field);
        }
    };

    private final ShipmentListView.Listener _shipments_listener = new ShipmentListView.Listener() {

        @Override
        public void addShipment() {
            ShipmentAddDialog.show(App.get(), DIALOG_SHIPMENT_ADD, _workOrder, getString(R.string.dialog_shipment_title), null, null);
        }

        @Override
        public void onDelete(WorkOrder workOrder, final Shipment shipment) {
            if ((long) shipment.getUser().getId() != (long) App.getProfileId()) {
                ToastClient.toast(App.get(), R.string.toast_cant_delete_shipment_permission, Toast.LENGTH_LONG);
                return;
            }

            _yesNoDialog.setData(getString(R.string.dialog_delete_shipment_title),
                    getString(R.string.dialog_delete_shipment_body), getString(R.string.btn_yes), getString(R.string.btn_no),
                    new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
                            WorkorderClient.deleteShipment(App.get(),
                                    _workOrder.getWorkOrderId(), shipment.getId());
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
        public void onAssign(WorkOrder workOrder, Shipment shipment) {
            // TODO STUB .onAssign()
            Log.v(TAG, "STUB .onAssign()");
            // TODO present a picker of the tasks that this can be assigned too
        }
    };

    private final ClosingNotesView.Listener _closingNotesView_listener = new ClosingNotesView.Listener() {
        @Override
        public void onChangeClosingNotes(String closingNotes) {
            WorkOrderTracker.onEditEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.CLOSING_NOTES);
            showClosingNotesDialog();
        }
    };

    private final SignatureListView.Listener _signatureList_listener = new SignatureListView.Listener() {
        @Override
        public void addSignature() {
            SignOffActivity.startSignOff(getActivity(), _workOrder);
            setLoading(true);
        }

        @Override
        public void signatureOnClick(SignatureCardView view, Signature signature) {
            SignatureDisplayActivity.startIntent(getActivity(), signature.getId(), _workOrder);
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
                            WorkordersWebApi.deleteSignature(App.get(), _workOrder.getWorkOrderId(), signature.getId());
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


    private final PaymentView.Listener _paymentView_listener = new PaymentView.Listener() {
        @Override
        public void onCounterOffer(WorkOrder workOrder) {
            CounterOfferDialog.show(App.get(), DIALOG_COUNTER_OFFER, workOrder);
        }

        @Override
        public void onRequestNewPay(WorkOrder workOrder) {
            // TODO add analytics
            Log.e(TAG, "Inside _paymentView_listener.onRequestNewPay()");
            if (_workOrder.getPay() != null
                    && _workOrder.getPay().getIncreases() != null
                    && _workOrder.getPay().getIncreases().getLastIncrease() != null) {
                PayDialog.show(App.get(), DIALOG_PAY, _workOrder.getPay().getIncreases().getLastIncrease().getPay(), true);
            } else {
                PayDialog.show(App.get(), DIALOG_PAY, _workOrder.getPay(), true);
            }
        }

        @Override
        public void onShowTerms(WorkOrder workOrder) {
            TermsDialog.show(App.get(), DIALOG_TERMS, getString(R.string.dialog_terms_title), getString(R.string.dialog_terms_body));
        }
    };


    private final CounterOfferSummaryView.Listener _coSummary_listener = new CounterOfferSummaryView.Listener() {
        @Override
        public void onCounterOffer() {
            CounterOfferDialog.show(App.get(), DIALOG_COUNTER_OFFER, _workOrder);
        }
    };


    private final ExpenseListLayout.Listener _expenseListView_listener = new ExpenseListLayout.Listener() {
        @Override
        public void addExpense() {
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.EXPENSES);
            ExpenseDialog.show(App.get(), DIALOG_EXPENSE, true);
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
                            WorkordersWebApi.deleteExpense(App.get(), _workOrder.getWorkOrderId(), expense.getId());
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
            DiscountDialog.show(App.get(), DIALOG_DISCOUNT, getString(R.string.dialog_add_discount_title));
        }

        @Override
        public void discountOnClick(PayModifier discount) {
            // TODO discountOnClick
        }

        @Override
        public void discountLongClick(final PayModifier discount) {
            _yesNoDialog.setData(getString(R.string.dialog_delete_discount_title),
                    getString(R.string.dialog_delete_discount_body), getString(R.string.btn_yes), getString(R.string.btn_no),
                    new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
                            WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.DISCOUNTS);
                            WorkordersWebApi.removeDiscount(App.get(), _workOrder.getWorkOrderId(), discount.getId());
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
        if (_workOrder.getActionsSet().contains(WorkOrder.ActionsEnum.CLOSING_NOTES))
            ClosingNotesDialog.show(App.get(), DIALOG_CLOSING_NOTES, _workOrder.getWorkOrderId(), _workOrder.getClosingNotes());
    }

    private final AppPickerDialog.OnOkListener _appPicker_onOk = new AppPickerDialog.OnOkListener() {
        @Override
        public void onOk(Intent pack) {
            if (pack.getAction().equals(Intent.ACTION_GET_CONTENT)) {
                Log.v(TAG, "onClick: " + pack.toString());
                startActivityForResult(pack, ActivityResultConstants.RESULT_CODE_GET_ATTACHMENT_WORK);
            } else {
                File temppath = new File(App.get().getTempFolder() + "/IMAGE-"
                        + misc.longToHex(System.currentTimeMillis(), 8) + ".png");
                _tempFile = temppath;
                pack.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temppath));
                startActivityForResult(pack, ActivityResultConstants.RESULT_CODE_GET_CAMERA_PIC_WORK);
            }
            setLoading(true);
        }
    };

    private final OneButtonDialog.OnPrimaryListener _locationLoadingDialog_onOk = new OneButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary() {
            setLoading(false);
        }
    };

    private final OneButtonDialog.OnCanceledListener _locationLoadingDialog_onCancel = new OneButtonDialog.OnCanceledListener() {
        @Override
        public void onCanceled() {
            setLoading(false);
        }
    };

    private final ClosingNotesDialog.OnOkListener _closingNotes_onOk = new ClosingNotesDialog.OnOkListener() {
        @Override
        public void onOk(String message) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CLOSING_NOTES, WorkOrderTracker.Action.CLOSING_NOTES, _workOrder.getWorkOrderId());
            WorkOrderTracker.onEditEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.CLOSING_NOTES);
            WorkordersWebApi.getWorkOrder(App.get(), _workOrder.getWorkOrderId(), false);
            setLoading(true);
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
            TermsDialog.show(App.get(), DIALOG_TERMS, getString(R.string.dialog_terms_title), getString(R.string.dialog_terms_body));
        }
    };
*/

    private final CounterOfferDialog.OnOkListener _counterOfferDialog_onOk = new CounterOfferDialog.OnOkListener() {
        @Override
        public void onOk(WorkOrder workorder, String reason, long expires, Pay pay, Schedule schedule,
                         Expense[] expenses) {

            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.COUNTER_OFFER,
                    WorkOrderTracker.Action.COUNTER_OFFER, workorder.getWorkOrderId());

            try {
                Request request = new Request();
                request.counter(true);

                if (!misc.isEmptyOrNull(reason))
                    request.counterNotes(reason);

                if (pay != null)
                    request.pay(pay);

                if (schedule != null)
                    request.schedule(schedule);

                if (expenses != null)
                    request.expenses(expenses);

                if (expires > 0)
                    request.expires(new Date(expires));

                WorkordersWebApi.request(App.get(), workorder.getWorkOrderId(), request);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            setLoading(true);
        }
    };

    private final CustomFieldDialog.OnOkListener _customfieldDialog_onOk = new CustomFieldDialog.OnOkListener() {
        @Override
        public void onOk(CustomField field, String value) {
            try {
                CustomField cf = new CustomField();
                cf.setValue(value);
                WorkordersWebApi.updateCustomField(App.get(), _workOrder.getWorkOrderId(), field.getId(), cf);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            setLoading(true);
        }
    };

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


    private final DiscountDialog.OnOkListener _discountDialog_onOk = new DiscountDialog.OnOkListener() {
        @Override
        public void onOk(String description, double amount) {
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.DISCOUNTS);
            try {
                PayModifier discount = new PayModifier();
                discount.setAmount(amount);
                discount.setDescription(description);
                WorkordersWebApi.addDiscount(App.get(), _workOrder.getWorkOrderId(), discount);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            setLoading(true);
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


    private final ExpenseDialog.OnOkListener _expenseDialog_onOk = new ExpenseDialog.OnOkListener() {
        @Override
        public void onOk(String description, double amount, ExpenseCategory category) {
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.EXPENSES);
            try {
                Expense expense = new Expense();
                expense.description(description);
                expense.amount(amount);
                expense.category(category);
                WorkordersWebApi.addExpense(App.get(), _workOrder.getWorkOrderId(), expense);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            setLoading(true);
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
            WorkordersWebApi.completeWorkOrder(App.get(), _workOrder.getWorkOrderId());
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

    private final PayDialog.OnCompleteListener _payDialog_onComplete = new PayDialog.OnCompleteListener() {
        @Override
        public void onComplete(Pay pay, String explanation) {
            try {
                WorkordersWebApi.addIncrease(App.get(), _workOrder.getWorkOrderId(),
                        new PayIncrease().pay(pay).description(explanation));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            populateUi();
        }
    };

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

    private final ShipmentAddDialog.OnOkListener _shipmentAddDialog_onOk = new ShipmentAddDialog.OnOkListener() {
        @Override
        public void onOk(String trackingId, ShipmentCarrier.NameEnum carrier, String carrierName, String description, Shipment.DirectionEnum direction, int taskId) {
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SHIPMENTS);

            try {
                ShipmentCarrier shipmentCarrier = new ShipmentCarrier();
                shipmentCarrier.name(carrier);
                if (carrier == ShipmentCarrier.NameEnum.OTHER)
                    shipmentCarrier.other(carrierName);
                shipmentCarrier.tracking(trackingId);

                Shipment shipment = new Shipment();
                shipment.carrier(shipmentCarrier);
                shipment.name(description);
                shipment.direction(direction);

                WorkordersWebApi.addShipment(App.get(), _workOrder.getWorkOrderId(), shipment);

            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            setLoading(true);
        }
    };

    private final TaskShipmentAddDialog.OnAddShipmentListener _taskShipmentAddDialog_onAdd = new TaskShipmentAddDialog.OnAddShipmentListener() {
        @Override
        public void onAddShipment(WorkOrder workorder, Shipment shipment, Task task) {
            ShipmentAddDialog.show(App.get(), DIALOG_SHIPMENT_ADD, workorder,
                    getString(R.string.dialog_shipment_title),
                    shipment == null ? "" : shipment.getName(), task);
        }
    };

    private final TaskShipmentAddDialog.OnDeleteListener _taskShipmentAddDialog_onDelete = new TaskShipmentAddDialog.OnDeleteListener() {
        @Override
        public void onDelete(WorkOrder workorder, Shipment shipment) {
            WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SHIPMENTS);
            WorkorderClient.deleteShipment(App.get(), workorder.getWorkOrderId(), shipment.getId());
            setLoading(true);
        }
    };

    private final WithdrawRequestDialog.OnWithdrawListener _withdrawRequestDialog_onWithdraw = new WithdrawRequestDialog.OnWithdrawListener() {
        @Override
        public void onWithdraw(long workOrderId) {
            if (_workOrder.getWorkOrderId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.WITHDRAW, WorkOrderTracker.Action.WITHDRAW, workOrderId);
        }
    };


    private final WorkLogDialog.OnOkListener _worklogDialog_listener = new WorkLogDialog.OnOkListener() {
        @Override
        public void onOk(TimeLog timeLog, Calendar start, Calendar end, int deviceCount) {
            TimeLog newTimeLog = new TimeLog();
            try {
                newTimeLog.in(new CheckInOut().created(new Date(start)));
                newTimeLog.out(new CheckInOut().created(new Date(end)));
                if (deviceCount > 0)
                    newTimeLog.devices((double) deviceCount);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (timeLog == null) {
                WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED);
                WorkordersWebApi.addTimeLog(App.get(), _workOrder.getWorkOrderId(), newTimeLog);

            } else {
                WorkOrderTracker.onEditEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED);
                WorkordersWebApi.updateTimeLog(App.get(), _workOrder.getWorkOrderId(), timeLog.getId(), newTimeLog);
            }
            setLoading(true);
        }
    };


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


