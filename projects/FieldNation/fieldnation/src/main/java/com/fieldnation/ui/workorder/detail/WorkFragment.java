package com.fieldnation.ui.workorder.detail;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.FileHelper;
import com.fieldnation.R;
import com.fieldnation.analytics.ScreenName;
import com.fieldnation.data.workorder.CustomField;
import com.fieldnation.data.workorder.Discount;
import com.fieldnation.data.workorder.Document;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.ShipmentTracking;
import com.fieldnation.data.workorder.Signature;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fngps.GpsLocationService;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.MemUtils;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.activityresult.ActivityResultConstants;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.service.data.v2.workorder.WorkOrderClient;
import com.fieldnation.service.data.workorder.ReportProblemType;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.AppPickerPackage;
import com.fieldnation.ui.OverScrollView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.SignOffActivity;
import com.fieldnation.ui.SignatureCardView;
import com.fieldnation.ui.SignatureDisplayActivity;
import com.fieldnation.ui.SignatureListView;
import com.fieldnation.ui.dialog.AppPickerDialog;
import com.fieldnation.ui.dialog.ClosingNotesDialog;
import com.fieldnation.ui.dialog.ConfirmDialog;
import com.fieldnation.ui.dialog.CounterOfferDialog;
import com.fieldnation.ui.dialog.CustomFieldDialog;
import com.fieldnation.ui.dialog.DeclineDialog;
import com.fieldnation.ui.dialog.DiscountDialog;
import com.fieldnation.ui.dialog.ExpenseDialog;
import com.fieldnation.ui.dialog.ExpiresDialog;
import com.fieldnation.ui.dialog.LocationDialog;
import com.fieldnation.ui.dialog.MarkCompleteDialog;
import com.fieldnation.ui.dialog.MarkIncompleteDialog;
import com.fieldnation.ui.dialog.OneButtonDialog;
import com.fieldnation.ui.dialog.PayDialog;
import com.fieldnation.ui.dialog.PhotoUploadDialog;
import com.fieldnation.ui.dialog.RateBuyerModal;
import com.fieldnation.ui.dialog.ReportProblemDialog;
import com.fieldnation.ui.dialog.ShipmentAddDialog;
import com.fieldnation.ui.dialog.TaskShipmentAddDialog;
import com.fieldnation.ui.dialog.TermsDialog;
import com.fieldnation.ui.dialog.TermsScrollingDialog;
import com.fieldnation.ui.dialog.TwoButtonDialog;
import com.fieldnation.ui.dialog.WorkLogDialog;
import com.fieldnation.ui.dialog.v2.AcceptBundleDialog;
import com.fieldnation.ui.dialog.v2.CheckInOutDialog;
import com.fieldnation.ui.dialog.v2.EtaDialog;
import com.fieldnation.ui.payment.PaymentDetailActivity;
import com.fieldnation.ui.payment.PaymentListActivity;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderBundleDetailActivity;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class WorkFragment extends WorkorderFragment {
    private static final String TAG = "WorkFragment";

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
    private MarkCompleteDialog _markCompleteDialog;
    private ShipmentAddDialog _shipmentAddDialog;
    private TaskShipmentAddDialog _taskShipmentAddDialog;
    private TermsDialog _termsDialog;
    private TermsScrollingDialog _termsScrollingDialog;
    private WorkLogDialog _worklogDialog;
    private LocationDialog _locationDialog;
    private OneButtonDialog _locationLoadingDialog;
    private PayDialog _payDialog;
    private TwoButtonDialog _yesNoDialog;
    private MarkIncompleteDialog _markIncompleteDialog;
    private ReportProblemDialog _reportProblemDialog;
    private PhotoUploadDialog _photoUploadDialog;
    private RateBuyerModal _rateBuyerModal;


    // Data
    private WorkorderClient _workorderClient;
    private File _tempFile;
    private Uri _tempUri;
    private GpsLocationService _gpsLocationService;
    private List<Signature> _signatures = null;
    private List<Task> _tasks = null;
    private Task _currentTask;
    private Workorder _workorder;
    private int _deviceCount = -1;
    private String _scannedImagePath;

    private final List<Runnable> _untilAdded = new LinkedList<>();

	/*-*************************************-*/
    /*-				LifeCycle				-*/
    /*-*************************************-*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        super.onViewCreated(view, savedInstanceState);

        _topBar = (ActionBarTopView) view.findViewById(R.id.actiontop_view);
        _topBar.setListener(_actionbartop_listener);

        _sumView = (WorkSummaryView) view.findViewById(R.id.summary_view);
        _sumView.setListener(_summaryView_listener);

        _companySummaryView = (CompanySummaryView) view.findViewById(R.id.companySummary_view);

        _contactListView = (ContactListView) view.findViewById(R.id.contactList_view);

        _locView = (LocationView) view.findViewById(R.id.location_view);
        _scheduleView = (ScheduleSummaryView) view.findViewById(R.id.schedule_view);

        _payView = (PaymentView) view.findViewById(R.id.payment_view);
        _payView.setListener(_paymentView_listener);

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
        _shipments.setListener(_shipments_listener);

        _taskList = (TaskListView) view.findViewById(R.id.scope_view);
        _taskList.setTaskListViewListener(_taskListView_listener);

        _timeLogged = (TimeLogListView) view.findViewById(R.id.timelogged_view);
        _timeLogged.setListener(_timeLoggedView_listener);

        _closingNotes = (ClosingNotesView) view.findViewById(R.id.closingnotes_view);
        _closingNotes.setListener(_clockingNotesView_listener);

        _customFields = (CustomFieldListView) view.findViewById(R.id.customfields_view);
        _customFields.setListener(_customFields_listener);

        _signatureView = (SignatureListView) view.findViewById(R.id.signature_view);
        _signatureView.setListener(_signaturelist_listener);

        _payDialog = PayDialog.getInstance(getFragmentManager(), TAG);
        _payDialog.setListener(_payDialog_listener);


        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDER)) {
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);
            }
            if (savedInstanceState.containsKey(STATE_TASKS)) {
                Parcelable[] tasks = savedInstanceState.getParcelableArray(STATE_TASKS);
                _tasks = new LinkedList<>();
                for (Parcelable task : tasks) {
                    _tasks.add((Task) task);
                }
                _taskList.setData(_workorder, _tasks);
            }
            if (savedInstanceState.containsKey(STATE_CURRENT_TASK)) {
                _currentTask = savedInstanceState.getParcelable(STATE_CURRENT_TASK);
            }
            if (savedInstanceState.containsKey(STATE_SIGNATURES)) {
                Parcelable[] sigs = savedInstanceState.getParcelableArray(STATE_SIGNATURES);
                _signatures = new LinkedList<>();
                for (Parcelable sig : sigs) {
                    _signatures.add((Signature) sig);
                }
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
        if (_deviceCount > -1)
            outState.putInt(STATE_DEVICE_COUNT, _deviceCount);

        if (_currentTask != null)
            outState.putParcelable(STATE_CURRENT_TASK, _currentTask);

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
        _markCompleteDialog = MarkCompleteDialog.getInstance(getFragmentManager(), TAG);
        _markIncompleteDialog = MarkIncompleteDialog.getInstance(getFragmentManager(), TAG);
        _reportProblemDialog = ReportProblemDialog.getInstance(getFragmentManager(), TAG);
        _shipmentAddDialog = ShipmentAddDialog.getInstance(getFragmentManager(), TAG);
        _taskShipmentAddDialog = TaskShipmentAddDialog.getInstance(getFragmentManager(), TAG);
        _termsDialog = TermsDialog.getInstance(getFragmentManager(), TAG);
        _termsScrollingDialog = TermsScrollingDialog.getInstance(getFragmentManager(), TAG);
        _yesNoDialog = TwoButtonDialog.getInstance(getFragmentManager(), TAG);
        _worklogDialog = WorkLogDialog.getInstance(getFragmentManager(), TAG);
        _photoUploadDialog = PhotoUploadDialog.getInstance(getFragmentManager(), TAG);
        _payDialog = PayDialog.getInstance(getFragmentManager(), TAG);
        _rateBuyerModal = RateBuyerModal.getInstance(getFragmentManager(), TAG);


        _locationLoadingDialog.setData(getString(R.string.dialog_location_loading_title),
                getString(R.string.dialog_location_loading_body),
                getString(R.string.dialog_location_loading_button),
                _locationLoadingDialog_listener);

        _closingDialog.setListener(_closingNotes_onOk);
        _counterOfferDialog.setListener(_counterOffer_listener);
        _declineDialog.setListener(_declineDialog_listener);
        _discountDialog.setListener(_discountDialog_listener);
        _expenseDialog.setListener(_expenseDialog_listener);
        _customFieldDialog.setListener(_customFieldDialog_listener);
        _appDialog.setListener(_appdialog_listener);
        _taskShipmentAddDialog.setListener(taskShipmentAddDialog_listener);
        _shipmentAddDialog.setListener(_shipmentAddDialog_listener);
        _worklogDialog.setListener(_worklogDialog_listener);
        _markCompleteDialog.setListener(_markCompleteDialog_listener);
        _markIncompleteDialog.setListener(_markIncompleteDialog_listener);
        _reportProblemDialog.setListener(_reportProblem_listener);
        _photoUploadDialog.setListener(_photoUploadDialog_listener);
        _payDialog.setListener(_payDialog_listener);

        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(App.get());

        _gpsLocationService = new GpsLocationService(getActivity());

        while (_untilAdded.size() > 0) {
            _untilAdded.remove(0).run();
        }
    }

    @Override
    public void onDetach() {
        if (_workorderClient != null && _workorderClient.isConnected())
            _workorderClient.disconnect(App.get());

        super.onDetach();
    }

    @Override
    public void onPause() {
        _gpsLocationService.stopLocationUpdates();
        super.onPause();
    }

    @Override
    public void update() {
        Tracker.screen(App.get(), ScreenName.workOrderDetailsWork());
    }

    @Override
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        subscribeData();
        requestTasks();
        populateUi();
    }

    private void setTasks(List<Task> tasks) {
        _tasks = tasks;
        _taskList.setData(_workorder, tasks);
        setLoading(false);
    }

    private void populateUi() {
        misc.hideKeyboard(getView());

        if (_workorder == null)
            return;

        if (getActivity() == null)
            return;
        if (_sumView != null) {
            Stopwatch watch = new Stopwatch(true);
            _sumView.setWorkorder(_workorder);
            Log.v(TAG, "_sumView time: " + watch.finish());
        }

        if (_companySummaryView != null) {
            Stopwatch watch = new Stopwatch(true);
            _companySummaryView.setWorkorder(_workorder);
            Log.v(TAG, "_companySummaryView time: " + watch.finish());
        }

        if (_locView != null) {
            Stopwatch watch = new Stopwatch(true);
            _locView.setWorkorder(_workorder);
            Log.v(TAG, "_locView time: " + watch.finish());
        }

        if (_scheduleView != null) {
            Stopwatch watch = new Stopwatch(true);
            _scheduleView.setWorkorder(_workorder);
            Log.v(TAG, "_scheduleView time: " + watch.finish());
        }

        if (_contactListView != null) {
            Stopwatch watch = new Stopwatch(true);
            _contactListView.setWorkorder(_workorder);
            Log.v(TAG, "_contactListView time: " + watch.finish());
        }

        if (_payView != null) {
            Stopwatch watch = new Stopwatch(true);
            _payView.setWorkorder(_workorder);
            Log.v(TAG, "_payView time: " + watch.finish());
        }

        if (_coSummaryView != null) {
            Stopwatch watch = new Stopwatch(true);
            _coSummaryView.setData(_workorder);
            Log.v(TAG, "_coSummaryView time: " + watch.finish());
        }

        if (_expenseListView != null) {
            Stopwatch watch = new Stopwatch(true);
            _expenseListView.setWorkorder(_workorder);
            Log.v(TAG, "_expenseListView time: " + watch.finish());
        }

        if (_discountListView != null) {
            Stopwatch watch = new Stopwatch(true);
            _discountListView.setWorkorder(_workorder);
            Log.v(TAG, "_discountListView time: " + watch.finish());
        }

        if (_topBar != null) {
            Stopwatch watch = new Stopwatch(true);
            _topBar.setWorkorder(_workorder);
            Log.v(TAG, "_topBar time: " + watch.finish());
        }

        if (_exView != null) {
            Stopwatch watch = new Stopwatch(true);
            _exView.setWorkorder(_workorder);
            Log.v(TAG, "_exView time: " + watch.finish());
        }

        if (_shipments != null && _timeLogged != null) {
            Stopwatch watch = new Stopwatch(true);
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
            Log.v(TAG, "_shipments time: " + watch.finish());
        }

        if (_shipments != null) {
            Stopwatch watch = new Stopwatch(true);
            _shipments.setWorkorder(_workorder);
            Log.v(TAG, "_shipments time: " + watch.finish());
        }

        if (_timeLogged != null) {
            Stopwatch watch = new Stopwatch(true);
            _timeLogged.setWorkorder(_workorder);
            Log.v(TAG, "_timeLogged time: " + watch.finish());
        }

        if (_closingNotes != null) {
            Stopwatch watch = new Stopwatch(true);
            _closingNotes.setWorkorder(_workorder);
            Log.v(TAG, "_closingNotes time: " + watch.finish());
        }

        if (_topBar != null) {
            Stopwatch watch = new Stopwatch(true);
            _topBar.setWorkorder(_workorder);
            Log.v(TAG, "_topBar time: " + watch.finish());
        }

        if (_customFields != null) {
            Stopwatch watch = new Stopwatch(true);
            _customFields.setData(_workorder, _workorder.getCustomFields());
            Log.v(TAG, "_customFields time: " + watch.finish());
        }

        if (_signatureView != null) {
            Stopwatch watch = new Stopwatch(true);
            _signatureView.setWorkorder(_workorder);
            Log.v(TAG, "_signatureView time: " + watch.finish());
        }

        setLoading(false);

        if (_bundleWarningTextView != null) {
            Stopwatch watch = new Stopwatch(true);
            if (_workorder.getBundleId() != null && _workorder.getBundleId() > 0) {
                _bundleWarningTextView.setVisibility(View.VISIBLE);
            } else {
                _bundleWarningTextView.setVisibility(View.GONE);
            }
            Log.v(TAG, "_bundleWarningTextView time: " + watch.finish());
        }

        if (getArguments() != null) {
            if (getArguments().containsKey(WorkorderActivity.INTENT_FIELD_ACTION)
                    && getArguments().getString(WorkorderActivity.INTENT_FIELD_ACTION)
                    .equals(WorkorderActivity.ACTION_CONFIRM)) {

                EtaDialog.Controller.show(App.get(), _workorder.getWorkorderId(),
                        _workorder.getScheduleV2(), EtaDialog.PARAM_DIALOG_TYPE_CONFIRM);
                getArguments().remove(WorkorderActivity.INTENT_FIELD_ACTION);
            }
        }
    }

    private void requestWorkorder() {
        if (_workorder == null)
            return;

        Log.v(TAG, "getData.startRefreshing");
        setLoading(true);
        _workorder.dispatchOnChange();
    }

    private void requestTasks() {
        if (_workorder == null)
            return;

        if (getActivity() == null)
            return;

        WorkorderClient.listTasks(App.get(), _workorder.getWorkorderId(), false);
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

    private void showClosingNotesDialog() {
        if (_workorder.canChangeClosingNotes())
            _closingDialog.show(_workorder.getClosingNotes());
    }

    private void startCheckin() {
        // everything is awsome. checkin
        _gpsLocationService.setListener(_gps_checkInListener);
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
        setLoading(true);
    }

    private void startCheckOut() {
        _gpsLocationService.setListener(_gps_checkOutListener);
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
        setLoading(true);
    }

    private void doCheckin() {
//        setLoading(true);
        _gpsLocationService.setListener(null);
        if (_gpsLocationService.hasLocation()) {
            CheckInOutDialog.Controller.show(App.get(), _workorder.getWorkorderId(), _gpsLocationService.getLocation(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_IN);
        } else {
            CheckInOutDialog.Controller.show(App.get(), _workorder.getWorkorderId(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_IN);

        }
    }

    private void doCheckOut() {
//        setLoading(true);

        Pay pay = _workorder.getPay();
        if (pay != null && pay.isPerDeviceRate()) {
            _deviceCount = pay.getMaxDevice();
        }

        _gpsLocationService.setListener(null);
        if (_gpsLocationService.hasLocation()) {
            if (_deviceCount > -1) {
                CheckInOutDialog.Controller.show(App.get(), _workorder.getWorkorderId(), _gpsLocationService.getLocation(), _deviceCount, CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            } else {
                CheckInOutDialog.Controller.show(App.get(), _workorder.getWorkorderId(), _gpsLocationService.getLocation(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            }
        } else {
            if (_deviceCount > -1) {
                CheckInOutDialog.Controller.show(App.get(), _workorder.getWorkorderId(), _deviceCount, CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            } else {
                CheckInOutDialog.Controller.show(App.get(), _workorder.getWorkorderId(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            }
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final ReportProblemDialog.Listener _reportProblem_listener = new ReportProblemDialog.Listener() {
        @Override
        public void onReportAProblem(String explanation, ReportProblemType type) {
            WorkorderClient.actionReportProblem(App.get(), _workorder.getWorkorderId(), explanation, type);
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

                _workorderClient.subDeliverableCache();

                setLoading(true);

                if (data == null) {
                    Log.e(TAG, "uploading an image using camera");
                    _tempUri = null;
                    _photoUploadDialog.show(_tempFile.getName());
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
                                _photoUploadDialog.show(FileUtils.getFileNameFromUri(App.get(), data.getData()));
                                WorkorderClient.cacheDeliverableUpload(App.get(), data.getData());
                            } else {
                                for (int i = 0; i < count; ++i) {
                                    uri = clipData.getItemAt(i).getUri();
                                    if (uri != null) {
                                        WorkorderClient.uploadDeliverable(App.get(), _workorder.getWorkorderId(),
                                                _currentTask.getSlotId(), intent.setData(uri));
                                    }
                                }
                            }
                        } else {
                            Log.v(TAG, "Single local/ non-local file upload");
                            _tempUri = data.getData();
                            _tempFile = null;
                            _photoUploadDialog.show(FileUtils.getFileNameFromUri(App.get(), data.getData()));
                            WorkorderClient.cacheDeliverableUpload(App.get(), data.getData());
                        }
                    } else {
                        Log.v(TAG, "Android version is pre-4.3");
                        _tempUri = data.getData();
                        _tempFile = null;
                        _photoUploadDialog.show(FileUtils.getFileNameFromUri(App.get(), data.getData()));
                        WorkorderClient.cacheDeliverableUpload(App.get(), data.getData());
                    }
                }

            } else if (requestCode == ActivityResultConstants.RESULT_CODE_GET_SIGNATURE && resultCode == Activity.RESULT_OK) {
                requestWorkorder();
                if (App.get().getProfile().canRequestWorkOnMarketplace() && !_workorder.isW2Workorder() && _workorder.getBuyerRatingInfo().getRatingId() == null) {
                    _rateBuyerModal.show(_workorder);
                }
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
    /*-				Dialog Listeners				-*/
    /*-*********************************************-*/
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

    private final ClosingNotesDialog.Listener _closingNotes_onOk = new ClosingNotesDialog.Listener() {
        @Override
        public void onOk(String message) {
            WorkorderClient.actionSetClosingNotes(App.get(), _workorder.getWorkorderId(), message);
            _workorder.dispatchOnChange();
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }
    };

    private final ConfirmDialog.Listener _confirmListener = new ConfirmDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String startDate, long durationMilliseconds) {
            try {
                long end = durationMilliseconds + ISO8601.toUtc(startDate);
                WorkorderClient.actionConfirmAssignment(App.get(),
                        _workorder.getWorkorderId(), startDate, ISO8601.fromUTC(end), null, false);
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

    private final CounterOfferDialog.Listener _counterOffer_listener = new CounterOfferDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String reason, boolean expires,
                         int expirationInSeconds, Pay pay, Schedule schedule, Expense[] expenses) {
            WorkorderClient.actionCounterOffer(App.get(), workorder.getWorkorderId(), expires,
                    reason, expirationInSeconds, pay, schedule, expenses);
            setLoading(true);
        }
    };

    private final CustomFieldDialog.Listener _customFieldDialog_listener = new CustomFieldDialog.Listener() {
        @Override
        public void onOk(CustomField field, String value) {
            WorkorderClient.actionCustomField(App.get(), _workorder.getWorkorderId(),
                    field.getCustomLabelId(), value);
            setLoading(true);
        }
    };

    private final DeclineDialog.Listener _declineDialog_listener = new DeclineDialog.Listener() {
        @Override
        public void onOk() {
            WorkOrderClient.actionDecline(App.get(), _workorder.getWorkorderId(), -1, null);
        }

        @Override
        public void onOk(boolean blockBuyer, int blockingReasonId, String blockingExplanation) {
            WorkOrderClient.actionDecline(App.get(), _workorder.getWorkorderId(), -1, null);

            if (blockBuyer) {
                ProfileClient.actionBlockCompany(App.get(),
                        App.get().getProfile().getUserId(),
                        _workorder.getCompanyId(), blockingReasonId, blockingExplanation);
            }
        }

        @Override
        public void onOk(boolean blockBuyer, int declineReasonId, String declineExplanation, int blockingReasonId, String blockingExplanation) {
            WorkOrderClient.actionDecline(App.get(), _workorder.getWorkorderId(), declineReasonId, declineExplanation);
            if (blockBuyer) {
                ProfileClient.actionBlockCompany(App.get(),
                        App.get().getProfile().getUserId(),
                        _workorder.getCompanyId(), blockingReasonId, blockingExplanation);
            }

            getActivity().finish();
        }

        @Override
        public void onOk(int declineReasonId, String declineExplanation) {
            WorkOrderClient.actionDecline(App.get(), _workorder.getWorkorderId(), declineReasonId, declineExplanation);
        }

        @Override
        public void onCancel() {
        }
    };

    private final DiscountDialog.Listener _discountDialog_listener = new DiscountDialog.Listener() {
        @Override
        public void onOk(String description, double amount) {
            WorkorderClient.createDiscount(App.get(), _workorder.getWorkorderId(),
                    description, amount);
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }
    };

    private final ExpenseDialog.Listener _expenseDialog_listener = new ExpenseDialog.Listener() {
        @Override
        public void onOk(String description, double amount, ExpenseCategory category) {
            WorkorderClient.createExpense(App.get(), _workorder.getWorkorderId(), description,
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

            WorkorderClient.actionRequest(App.get(), _workorder.getWorkorderId(), seconds);
            setLoading(true);
        }
    };

    private final MarkCompleteDialog.Listener _markCompleteDialog_listener = new MarkCompleteDialog.Listener() {
        @Override
        public void onSignatureClick() {
            new AsyncTaskEx<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... params) {
                    try {
                        Context context = (Context) params[0];
                        Workorder workorder = (Workorder) params[1];

                        Intent intent = new Intent(context, SignOffActivity.class);
                        intent.putExtra(SignOffActivity.INTENT_PARAM_WORKORDER, workorder);
                        intent.putExtra(SignOffActivity.INTENT_COMPLETE_WORKORDER, true);
                        startActivityForResult(intent, ActivityResultConstants.RESULT_CODE_GET_SIGNATURE);
                        return null;
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                        ToastClient.toast(App.get(), "Could not start signature collection. Please try again.", Toast.LENGTH_LONG);
                    }
                    return null;
                }
            }.executeEx(getActivity(), _workorder);
        }

        @Override
        public void onContinueClick() {
            WorkorderClient.actionComplete(App.get(), _workorder.getWorkorderId());
            setLoading(true);
        }
    };


    private final MarkIncompleteDialog.Listener _markIncompleteDialog_listener = new MarkIncompleteDialog.Listener() {

        // TODO: I am not pretty sure about the following method
        @Override
        public void onContinueClick() {
            WorkorderClient.actionIncomplete(App.get(), _workorder.getWorkorderId());
            setLoading(true);
        }
    };


    private final ShipmentAddDialog.Listener _shipmentAddDialog_listener = new ShipmentAddDialog.Listener() {
        @Override
        public void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite) {
            if (_scannedImagePath != null) {
                final UploadSlot[] slots = _workorder.getUploadSlots();
                if (slots == null) return;
                for (UploadSlot uploadSlot : slots) {
                    if (uploadSlot.getSlotName().equalsIgnoreCase("misc")) {
                        String fileName = _scannedImagePath.substring(_scannedImagePath.lastIndexOf(File.separator) + 1, _scannedImagePath.length());
                        WorkorderClient.uploadDeliverable(App.get(), _workorder.getWorkorderId(),
                                uploadSlot.getSlotId(), fileName, _scannedImagePath);
                        _scannedImagePath = null;
                    }
                }
            }

            WorkorderClient.createShipment(App.get(), _workorder.getWorkorderId(), description, shipToSite,
                    carrier, carrierName, trackingId);
            setLoading(true);
        }

        @Override
        public void onOk(String trackingId, String carrier, String carrierName, String description,
                         boolean shipToSite, long taskId) {
            Log.v(TAG, "ShipmentAddDialog#onOk");
            WorkorderClient.createShipment(App.get(), _workorder.getWorkorderId(), description, shipToSite,
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

    private final TaskShipmentAddDialog.Listener taskShipmentAddDialog_listener = new TaskShipmentAddDialog.Listener() {
        @Override
        public void onDelete(Workorder workorder, ShipmentTracking shipment) {
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
            WorkorderClient.actionSetShipmentDetails(App.get(), workorder.getWorkorderId(), description,
                    shipToSite, carrier, carrierName, trackingId);
            setLoading(true);
        }

        @Override
        public void onAddShipmentDetails(Workorder workorder, String trackingId, String carrier, String carrierName, String description, boolean shipToSite, long taskId) {
            WorkorderClient.actionSetShipmentDetails(App.get(), workorder.getWorkorderId(), description,
                    shipToSite, carrier, carrierName, trackingId, taskId);
            setLoading(true);
        }
    };

    private final WorkLogDialog.Listener _worklogDialog_listener = new WorkLogDialog.Listener() {
        @Override
        public void onOk(LoggedWork loggedWork, Calendar start, Calendar end, int deviceCount) {
            if (loggedWork == null) {
                if (deviceCount <= 0) {
                    WorkorderClient.addTimeLog(App.get(), _workorder.getWorkorderId(),
                            start.getTimeInMillis(), end.getTimeInMillis());
                } else {
                    WorkorderClient.addTimeLog(App.get(), _workorder.getWorkorderId(),
                            start.getTimeInMillis(), end.getTimeInMillis(), deviceCount);
                }
            } else {
                if (deviceCount <= 0) {
                    WorkorderClient.updateTimeLog(App.get(), _workorder.getWorkorderId(),
                            loggedWork.getLoggedHoursId(), start.getTimeInMillis(), end.getTimeInMillis());
                } else {
                    WorkorderClient.updateTimeLog(App.get(), _workorder.getWorkorderId(),
                            loggedWork.getLoggedHoursId(), start.getTimeInMillis(), end.getTimeInMillis(), deviceCount);
                }
            }
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }
    };

    /*-*****************************************-*/
    /*-				View Listeners				-*/
    /*-*****************************************-*/
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

    private final ActionBarTopView.Listener _actionbartop_listener = new ActionBarTopView.Listener() {
        @Override
        public void onCheckOut() {
            startCheckOut();
        }

        @Override
        public void onAcknowledgeHold() {
            WorkorderClient.actionAcknowledgeHold(App.get(), _workorder.getWorkorderId());

            setLoading(true);
        }

        @Override
        public void onMarkIncomplete() {
            _markIncompleteDialog.show(_workorder);
        }

        @Override
        public void onViewPayment() {
            if (_workorder.getPaymentId() != null) {
                PaymentDetailActivity.startNew(App.get(), _workorder.getPaymentId());
            } else {
                PaymentListActivity.startNew(App.get());
            }
        }

        @Override
        public void onReportProblem() {
            _reportProblemDialog.show(_workorder);
        }

        @Override
        public void onCheckIn() {
            Log.v(TAG, "onCheckIn");
            startCheckin();
        }

        @Override
        public void onNotInterested() {
            _declineDialog.show();
        }

        @Override
        public void onRequest() {
            if (_workorder.isBundle()) {
                AcceptBundleDialog.Controller.show(App.get(), _workorder.getBundleId(),
                        _workorder.getBundleCount(), _workorder.getWorkorderId(), AcceptBundleDialog.TYPE_REQUEST);
            } else {
                EtaDialog.Controller.show(App.get(), _workorder.getWorkorderId(),
                        _workorder.getScheduleV2(), EtaDialog.PARAM_DIALOG_TYPE_REQUEST);
            }
        }

        @Override
        public void onConfirmAssignment() {
            if (_workorder.isBundle()) {
                AcceptBundleDialog.Controller.show(App.get(), _workorder.getBundleId(),
                        _workorder.getBundleCount(), _workorder.getWorkorderId(), AcceptBundleDialog.TYPE_ACCEPT);
            } else {
                EtaDialog.Controller.show(App.get(), _workorder.getWorkorderId(),
                        _workorder.getScheduleV2(), EtaDialog.PARAM_DIALOG_TYPE_ACCEPT);
            }
        }

        @Override
        public void onWithdraw() {
            WorkorderClient.actionWithdrawRequest(App.get(), _workorder.getWorkorderId());
        }

        @Override
        public void onViewCounter() {
            _counterOfferDialog.show(_workorder);
        }

        @Override
        public void onReadyToGo() {
            WorkorderClient.actionReadyToGo(App.get(), _workorder.getWorkorderId());
        }

        @Override
        public void onConfirm() {
            EtaDialog.Controller.show(App.get(), _workorder.getWorkorderId(),
                    _workorder.getScheduleV2(), EtaDialog.PARAM_DIALOG_TYPE_CONFIRM);
        }

        @Override
        public void onEnterClosingNotes() {
            showClosingNotesDialog();
        }

        @Override
        public void onMarkComplete() {
            _markCompleteDialog.show(_workorder);
        }
    };

    private final ClosingNotesView.Listener _clockingNotesView_listener = new ClosingNotesView.Listener() {
        @Override
        public void onChangeClosingNotes(String closingNotes) {
            showClosingNotesDialog();
        }
    };

    private final CustomFieldRowView.Listener _customFields_listener = new CustomFieldRowView.Listener() {
        @Override
        public void onClick(CustomFieldRowView view, CustomField field) {
            _customFieldDialog.show(field);
        }
    };

    private final PaymentView.Listener _paymentView_listener = new PaymentView.Listener() {
        @Override
        public void onCounterOffer(Workorder workorder) {
            _counterOfferDialog.show(_workorder);
        }

        @Override
        public void onRequestNewPay(Workorder workorder) {
            // TODO show request new pay dialog
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

    private final CounterOfferSummaryView.Listener _coSummary_listener = new CounterOfferSummaryView.Listener() {
        @Override
        public void onCounterOffer() {
            _counterOfferDialog.show(_workorder);
        }
    };

    private final ExpenseListLayout.Listener _expenseListView_listener = new ExpenseListLayout.Listener() {
        @Override
        public void addExpense() {
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
                            WorkorderClient.deleteExpense(App.get(),
                                    _workorder.getWorkorderId(), expense.getExpenseId());
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
                            WorkorderClient.deleteDiscount(App.get(),
                                    _workorder.getWorkorderId(), discount.getDiscountId());
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

    private final RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            requestWorkorder();
        }
    };

    private final ShipmentListView.Listener _shipments_listener = new ShipmentListView.Listener() {

        @Override
        public void addShipment() {
            _shipmentAddDialog.show(getString(R.string.dialog_shipment_title), 0);
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
                                    _workorder.getWorkorderId(), shipment.getWorkorderShipmentId());
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

    private final SignatureListView.Listener _signaturelist_listener = new SignatureListView.Listener() {
        @Override
        public void addSignature() {
            SignOffActivity.startSignOff(getActivity(), _workorder);
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
                            WorkorderClient.deleteSignature(App.get(),
                                    _workorder.getWorkorderId(), signature.getSignatureId());
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


    private final PayDialog.Listener _payDialog_listener = new PayDialog.Listener() {
        @Override
        public void onComplete(Pay pay, String explanation) {
            WorkorderClient.actionChangePay(App.get(), _workorder.getWorkorderId(),
                    pay, explanation);

            populateUi();
        }

        @Override
        public void onNothing() {
        }
    };

    private final WorkSummaryView.Listener _summaryView_listener = new WorkSummaryView.Listener() {
        @Override
        public void showConfidentialInfo(String body) {
            _termsScrollingDialog.show(getString(R.string.dialog_confidential_information_title), body);
        }

        @Override
        public void showCustomerPolicies(String body) {
            _termsScrollingDialog.show(getString(R.string.dialog_policy_title), body);
        }

        @Override
        public void showStandardInstructions(String body) {
            _termsDialog.show(getString(R.string.dialog_standard_instruction_title), body);
        }
    };

    private final TaskListView.Listener _taskListView_listener = new TaskListView.Listener() {
        @Override
        public void onCheckin(Task task) {
            startCheckin();
        }

        @Override
        public void onCheckout(Task task) {
            Pay pay = _workorder.getPay();
            if (pay != null && pay.isPerDeviceRate()) {
//                _deviceCountDialog.show(_workorder, pay.getMaxDevice());
                setLoading(true);
            } else {
                startCheckOut();
            }
        }

        @Override
        public void onCloseOutNotes(Task task) {
            showClosingNotesDialog();
        }

        @Override
        public void onConfirmAssignment(Task task) {
            EtaDialog.Controller.show(App.get(), _workorder.getWorkorderId(),
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
                    Log.v(TAG, "docid: " + doc.getDocumentId());
                    if (doc.getDocumentId().equals(_identifier)) {
                        // task completed here
                        if (!task.getCompleted()) {
                            WorkorderClient.actionCompleteTask(App.get(),
                                    _workorder.getWorkorderId(), task.getTaskId());
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
                        _workorder.getWorkorderId(), task.getTaskId());
            }
            setLoading(true);
        }

        @Override
        public void onPhone(Task task) {
            if (!task.getCompleted()) {
                WorkorderClient.actionCompleteTask(App.get(),
                        _workorder.getWorkorderId(), task.getTaskId());
                setLoading(true);
            }
            try {
                if (task.getPhoneNumber() != null) {
                    // Todo, need to figure out if there is a phone number here
                    Spannable test = new SpannableString(task.getPhoneNumber());
                    Linkify.addLinks(test, Linkify.PHONE_NUMBERS);
                    if (test.getSpans(0, test.length(), URLSpan.class).length == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(R.string.dialog_no_number_message);
                        builder.setTitle(R.string.dialog_no_number_title);
                        builder.setPositiveButton(R.string.btn_ok, null);
                        builder.show();

                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        String phNum = "tel:" + task.getPhoneNumber();
                        callIntent.setData(Uri.parse(phNum));
                        startActivity(callIntent);
                        setLoading(true);
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
                _shipmentAddDialog.show(getText(R.string.dialog_shipment_title), task.getTaskId());
            } else {
                _taskShipmentAddDialog.show(getString(R.string.dialog_task_shipment_title), _workorder, task.getTaskId());
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
                    _workorder.getWorkorderId(), task.getTaskId());
            setLoading(true);
        }
    };

    private final TimeLogListView.Listener _timeLoggedView_listener = new TimeLogListView.Listener() {
        @Override
        public void addWorklog(boolean showdevice) {
            _worklogDialog.show(getString(R.string.dialog_delete_add_worklog_title), null, showdevice);
        }

        @Override
        public void editWorklog(Workorder workorder, LoggedWork loggedWork, boolean showDeviceCount) {
            _worklogDialog.show(getString(R.string.dialog_delete_add_worklog_title), loggedWork, showDeviceCount);
        }

        @Override
        public void deleteWorklog(Workorder workorder, LoggedWork loggedWork) {
//            WorkorderClient.deleteTimeLog(GlobalState.getContext(), workorder.getWorkorderId(),
//                    loggedWork.getLoggedHoursId());
//            setLoading(true);

            final long workorderID = workorder.getWorkorderId();
            final long loggedHoursID = loggedWork.getLoggedHoursId();

            _yesNoDialog.setData(getString(R.string.dialog_delete_worklog_title),
                    getString(R.string.dialog_delete_worklog_body), getString(R.string.btn_yes), getString(R.string.btn_no),
                    new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
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

    private final View.OnClickListener _bundle_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkorderBundleDetailActivity.startNew(App.get(), _workorder.getWorkorderId(), _workorder.getBundleId());
            setLoading(true);
        }
    };

    /*-*****************************-*/
    /*-				Web				-*/
    /*-*****************************-*/

    private void subscribeData() {
        if (_workorder == null)
            return;

        if (_workorderClient == null)
            return;

        if (!_workorderClient.isConnected())
            return;

        _workorderClient.subListTasks(_workorder.getWorkorderId(), false);
    }

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            subscribeData();
            _workorderClient.subDeliverableUpload();
        }

        @Override
        public void onTaskList(long workorderId, List<Task> tasks, boolean failed) {
            setTasks(tasks);
        }

        @Override
        public void onDeliverableCacheEnd(Uri uri, String filename) {
            _tempUri = uri;
            _tempFile = null;
            _photoUploadDialog.setPhoto(MemUtils.getMemoryEfficientBitmap(filename, 400));
        }
    };

    private final PhotoUploadDialog.Listener _photoUploadDialog_listener = new PhotoUploadDialog.Listener() {
        @Override
        public void onOk(String filename, String photoDescription) {
            Log.e(TAG, "uploading an image using camera");
            if (_tempFile != null) {
                WorkorderClient.uploadDeliverable(App.get(), _workorder.getWorkorderId(),
                        _currentTask.getSlotId(), filename, _tempFile.getAbsolutePath(), photoDescription);
            } else if (_tempUri != null) {
                WorkorderClient.uploadDeliverable(App.get(), _workorder.getWorkorderId(),
                        _currentTask.getSlotId(), filename, _tempUri, photoDescription);
            }
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
}


