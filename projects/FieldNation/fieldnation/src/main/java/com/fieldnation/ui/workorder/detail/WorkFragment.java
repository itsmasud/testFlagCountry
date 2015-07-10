package com.fieldnation.ui.workorder.detail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.FileHelper;
import com.fieldnation.GlobalState;
import com.fieldnation.GoogleAnalyticsTopicClient;
import com.fieldnation.GpsLocationService;
import com.fieldnation.Log;
import com.fieldnation.R;
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
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.AppPickerPackage;
import com.fieldnation.ui.OverScrollView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.SignOffActivity;
import com.fieldnation.ui.SignatureCardView;
import com.fieldnation.ui.SignatureDisplayActivity;
import com.fieldnation.ui.SignatureListView;
import com.fieldnation.ui.dialog.AcceptBundleDialog;
import com.fieldnation.ui.dialog.AppPickerDialog;
import com.fieldnation.ui.dialog.ClosingNotesDialog;
import com.fieldnation.ui.dialog.ConfirmDialog;
import com.fieldnation.ui.dialog.CounterOfferDialog;
import com.fieldnation.ui.dialog.CustomFieldDialog;
import com.fieldnation.ui.dialog.DeclineDialog;
import com.fieldnation.ui.dialog.DeviceCountDialog;
import com.fieldnation.ui.dialog.DiscountDialog;
import com.fieldnation.ui.dialog.ExpenseDialog;
import com.fieldnation.ui.dialog.ExpiresDialog;
import com.fieldnation.ui.dialog.LocationDialog;
import com.fieldnation.ui.dialog.MarkCompleteDialog;
import com.fieldnation.ui.dialog.OneButtonDialog;
import com.fieldnation.ui.dialog.ShipmentAddDialog;
import com.fieldnation.ui.dialog.TaskShipmentAddDialog;
import com.fieldnation.ui.dialog.TermsDialog;
import com.fieldnation.ui.dialog.TwoButtonDialog;
import com.fieldnation.ui.dialog.WorkLogDialog;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderBundleDetailActivity;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.utils.ISO8601;

import java.io.File;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class WorkFragment extends WorkorderFragment {
    private static final String TAG = "WorkFragment";

    // Activity result codes
    private static final int RESULT_CODE_BASE = 200;
    private static final int RESULT_CODE_SEND_EMAIL = RESULT_CODE_BASE + 1;
    private static final int RESULT_CODE_GET_ATTACHMENT = RESULT_CODE_BASE + 3;
    private static final int RESULT_CODE_GET_CAMERA_PIC = RESULT_CODE_BASE + 4;
    private static final int RESULT_CODE_GET_SIGNATURE = RESULT_CODE_BASE + 5;
    private static final int RESULT_CODE_ENABLE_GPS_CHECKIN = RESULT_CODE_BASE + 6;
    private static final int RESULT_CODE_ENABLE_GPS_CHECKOUT = RESULT_CODE_BASE + 7;

    // saved state keys
    private static final String STATE_WORKORDER = "WorkFragment:STATE_WORKORDER";
    private static final String STATE_TASKS = "WorkFragment:STATE_TASKS";
    private static final String STATE_CURRENT_TASK = "WorkFragment:STATE_CURRENT_TASK";
    private static final String STATE_SIGNATURES = "WorkFragment:STATE_SIGNATURES";
    private static final String STATE_DEVICE_COUNT = "WorkFragment:STATE_DEVICE_COUNT";

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
    private ActionView _actionView;
    private RefreshView _refreshView;


    // Dialogs
    private AcceptBundleDialog _acceptBundleWOConfirmDialog;
    private AcceptBundleDialog _acceptBundleWOExpiresDialog;
    private AppPickerDialog _appDialog;
    private ClosingNotesDialog _closingDialog;
    private ConfirmDialog _confirmDialog;
    private CounterOfferDialog _counterOfferDialog;
    private CustomFieldDialog _customFieldDialog;
    private DeclineDialog _declineDialog;
    private DeviceCountDialog _deviceCountDialog;
    private DiscountDialog _discountDialog;
    private ExpenseDialog _expenseDialog;
    private ExpiresDialog _expiresDialog;
    private MarkCompleteDialog _markCompleteDialog;
    private ShipmentAddDialog _shipmentAddDialog;
    private TaskShipmentAddDialog _taskShipmentAddDialog;
    private TermsDialog _termsDialog;
    private WorkLogDialog _worklogDialog;
    private LocationDialog _locationDialog;
    private OneButtonDialog _locationLoadingDialog;
    private TwoButtonDialog _yesNoDialog;

    // Data
    private WorkorderClient _workorderClient;
    private ProfileClient _profileClient;

    private File _tempFile;
    private GpsLocationService _gpsLocationService;
    private List<Signature> _signatures = null;
    private List<Task> _tasks = null;
    private SecureRandom _rand = new SecureRandom();
    private Task _currentTask;
    private Workorder _workorder;
    private int _deviceCount = -1;


	/*-*************************************-*/
    /*-				LifeCycle				-*/
    /*-*************************************-*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workorder_work, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _sumView = (WorkSummaryView) view.findViewById(R.id.summary_view);
        _sumView.setListener(_summaryView_listener);

        _companySummaryView = (CompanySummaryView) view.findViewById(R.id.companySummary_view);

        _contactListView = (ContactListView) view.findViewById(R.id.contactList_view);

        _locView = (LocationView) view.findViewById(R.id.location_view);
        _scheduleView = (ScheduleSummaryView) view
                .findViewById(R.id.schedule_view);

        _payView = (PaymentView) view.findViewById(R.id.payment_view);
        _payView.setListener(_paymentView_listener);

        _coSummaryView = (CounterOfferSummaryView) view.findViewById(R.id.counterOfferSummary_view);

        _expenseListView = (ExpenseListLayout) view.findViewById(R.id.expenseListLayout_view);
        _expenseListView.setListener(_expenseListView_listener);

        _discountListView = (DiscountListLayout) view.findViewById(R.id.discountListLayout_view);
        _discountListView.setListener(_discountListView_listener);

        _actionView = (ActionView) view.findViewById(R.id.action_view);
        _actionView.setListener(_actionView_listener);

        _topBar = (ActionBarTopView) view.findViewById(R.id.actiontop_view);
        _topBar.setListener(_actionbartop_listener);

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
        if (_deviceCount > -1) {
            outState.putInt(STATE_DEVICE_COUNT, _deviceCount);
        }

        if (_currentTask != null) {
            outState.putParcelable(STATE_CURRENT_TASK, _currentTask);
        }

        super.onSaveInstanceState(outState);
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(activity);
        _profileClient = new ProfileClient(_profileClient_listener);
        _profileClient.connect(activity);

        _gpsLocationService = new GpsLocationService(getActivity());

        _acceptBundleWOConfirmDialog = AcceptBundleDialog.getInstance(getFragmentManager(), TAG + "._acceptBundleWOConfirmDialog");
        _acceptBundleWOExpiresDialog = AcceptBundleDialog.getInstance(getFragmentManager(), TAG + "._acceptBundleWOExpiresDialog");
        _appDialog = AppPickerDialog.getInstance(getFragmentManager(), TAG);
        _closingDialog = ClosingNotesDialog.getInstance(getFragmentManager(), TAG);
        _confirmDialog = ConfirmDialog.getInstance(getFragmentManager(), TAG);
        _counterOfferDialog = CounterOfferDialog.getInstance(getFragmentManager(), TAG);
        _customFieldDialog = CustomFieldDialog.getInstance(getFragmentManager(), TAG);
        _declineDialog = DeclineDialog.getInstance(getFragmentManager(), TAG);
        _deviceCountDialog = DeviceCountDialog.getInstance(getFragmentManager(), TAG);
        _discountDialog = DiscountDialog.getInstance(getFragmentManager(), TAG);
        _expenseDialog = ExpenseDialog.getInstance(getFragmentManager(), TAG);
        _expiresDialog = ExpiresDialog.getInstance(getFragmentManager(), TAG);
        _locationDialog = LocationDialog.getInstance(getFragmentManager(), TAG);
        _locationLoadingDialog = OneButtonDialog.getInstance(getFragmentManager(), TAG);
        _markCompleteDialog = MarkCompleteDialog.getInstance(getFragmentManager(), TAG);
        _shipmentAddDialog = ShipmentAddDialog.getInstance(getFragmentManager(), TAG);
        _taskShipmentAddDialog = TaskShipmentAddDialog.getInstance(getFragmentManager(), TAG);
        _termsDialog = TermsDialog.getInstance(getFragmentManager(), TAG);
        _worklogDialog = WorkLogDialog.getInstance(getFragmentManager(), TAG);
        _yesNoDialog = TwoButtonDialog.getInstance(getFragmentManager(), TAG);

        _locationLoadingDialog.setData(getString(R.string.dialog_location_loading_title),
                getString(R.string.dialog_location_loading_body),
                getString(R.string.dialog_location_loading_button),
                _locationLoadingDialog_listener);

        _deviceCountDialog.setListener(_deviceCountListener);
        _acceptBundleWOConfirmDialog.setListener(_acceptBundleDialogConfirmListener);
        _acceptBundleWOExpiresDialog.setListener(_acceptBundleDialogExpiresListener);
        _closingDialog.setListener(_closingNotes_onOk);
        _confirmDialog.setListener(_confirmListener);
        _counterOfferDialog.setListener(_counterOffer_listener);
        _declineDialog.setListener(_declineDialog_listener);
        _discountDialog.setListener(_discountDialog_listener);
        _expenseDialog.setListener(_expenseDialog_listener);
        _expiresDialog.setListener(_expiresDialog_listener);
        _customFieldDialog.setListener(_customFieldDialog_listener);
        _appDialog.setListener(_appdialog_listener);
        _taskShipmentAddDialog.setListener(taskShipmentAddDialog_listener);
        _shipmentAddDialog.setListener(_shipmentAddDialog_listener);
        _worklogDialog.setListener(_worklogDialog_listener);
        _markCompleteDialog.setListener(_markCompleteDialog_listener);
    }

    @Override
    public void onDetach() {
        _workorderClient.disconnect(getActivity());
        _workorderClient = null;
        _profileClient.disconnect(getActivity());
        _profileClient = null;
        super.onDetach();
    }


    @Override
    public void onPause() {
        _gpsLocationService.stopLocationUpdates();
        super.onPause();
    }

    @Override
    public void update() {
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
        if (_workorder == null)
            return;

        if (getActivity() == null)
            return;

        if (_sumView != null) {
            _sumView.setWorkorder(_workorder);
        }

        if (_companySummaryView != null) {
            _companySummaryView.setWorkorder(_workorder);
        }

        if (_locView != null) {
            _locView.setWorkorder(_workorder);
        }

        if (_scheduleView != null) {
            _scheduleView.setWorkorder(_workorder);
        }

        if (_contactListView != null) {
            _contactListView.setWorkorder(_workorder);
        }

        if (_payView != null) {
            _payView.setWorkorder(_workorder);
        }

        if (_coSummaryView != null) {
            _coSummaryView.setData(_workorder);
        }

        if (_expenseListView != null) {
            _expenseListView.setWorkorder(_workorder);
        }

        if (_discountListView != null) {
            _discountListView.setWorkorder(_workorder);
        }

        if (_actionView != null) {
            _actionView.setWorkorder(_workorder);
        }

        if (_topBar != null) {
            _topBar.setWorkorder(_workorder);
        }

        if (_exView != null) {
            _exView.setWorkorder(_workorder);
        }

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

        if (_signatureView != null) {
            _signatureView.setWorkorder(_workorder);
        }

        setLoading(false);

        if (_bundleWarningTextView != null) {
            if (_workorder.getBundleId() != null && _workorder.getBundleId() > 0) {
                _bundleWarningTextView.setVisibility(View.VISIBLE);
            } else {
                _bundleWarningTextView.setVisibility(View.GONE);
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

        WorkorderClient.listTasks(getActivity(), _workorder.getWorkorderId(), false);
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

    private PendingIntent getNotificationIntent() {
        Intent intent = new Intent(getActivity(), WorkorderActivity.class);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB,
                WorkorderActivity.TAB_DETAILS);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID,
                _workorder.getWorkorderId());

        return PendingIntent.getActivity(getActivity(), _rand.nextInt(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void showClosingNotesDialog() {
        if (_workorder.canChangeClosingNotes())
            _closingDialog.show(_workorder.getClosingNotes());
    }

    private void showReviewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_review_message);
        builder.setTitle(R.string.dialog_review_title);
        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri marketUri = Uri.parse("market://details?id=com.fieldnation.android");
                startActivity(new Intent(Intent.ACTION_VIEW).setData(marketUri));
            }
        });
        builder.setNegativeButton(R.string.btn_no_thanks, null);
        builder.create().show();
    }

    private void startCheckin() {
        Log.v(TAG, "startCheckin");
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
        Log.v(TAG, "startCheckOut");
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
        setLoading(true);
        _gpsLocationService.setListener(null);
        GoogleAnalyticsTopicClient.dispatchEvent(getActivity(), "WorkorderActivity", GoogleAnalyticsTopicClient.EventAction.CHECKIN, "WorkFragment", 1);
        if (_gpsLocationService.hasLocation()) {
            WorkorderClient.actionCheckin(getActivity(), _workorder.getWorkorderId(),
                    _gpsLocationService.getLocation());
        } else {
            WorkorderClient.actionCheckin(getActivity(), _workorder.getWorkorderId());
        }
    }

    private void doCheckOut() {
        setLoading(true);
        _gpsLocationService.setListener(null);
        GoogleAnalyticsTopicClient.dispatchEvent(getActivity(), "WorkorderActivity",
                GoogleAnalyticsTopicClient.EventAction.CHECKOUT, "WorkFragment", 1);
        if (_gpsLocationService.hasLocation()) {
            if (_deviceCount > -1) {
                WorkorderClient.actionCheckout(getActivity(), _workorder.getWorkorderId(),
                        _deviceCount, _gpsLocationService.getLocation());
            } else {
                WorkorderClient.actionCheckout(getActivity(), _workorder.getWorkorderId(),
                        _gpsLocationService.getLocation());
            }
        } else {
            if (_deviceCount > -1) {
                WorkorderClient.actionCheckout(getActivity(), _workorder.getWorkorderId(),
                        _deviceCount);
            } else {
                WorkorderClient.actionCheckout(getActivity(), _workorder.getWorkorderId());
            }
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
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
            _locationLoadingDialog.dismiss();
        }
    };
    private final GpsLocationService.Listener _gps_checkOutListener = new GpsLocationService.Listener() {
        @Override
        public void onLocation(Location location) {
            Log.v(TAG, "_gps_checkOutListener.onLocation");
            startCheckOut();
            _locationLoadingDialog.dismiss();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult() resultCode= " + resultCode);

        if ((requestCode == RESULT_CODE_GET_ATTACHMENT || requestCode == RESULT_CODE_GET_CAMERA_PIC)
                && resultCode == Activity.RESULT_OK) {

            if (data == null) {
                WorkorderClient.uploadDeliverable(getActivity(),
                        _workorder.getWorkorderId(), _currentTask.getSlotId(), _tempFile.getName(),
                        _tempFile.getAbsolutePath());
            } else {
                WorkorderClient.uploadDeliverable(getActivity(),
                        _workorder.getWorkorderId(), _currentTask.getSlotId(), data);
            }
        } else if (requestCode == RESULT_CODE_GET_SIGNATURE && resultCode == Activity.RESULT_OK) {
            GlobalState gs = (GlobalState) getActivity().getApplication();
            if (gs.shouldShowReviewDialog()) {
                showReviewDialog();
                gs.setShownReviewDialog();
                requestWorkorder();
            }
        } else if (requestCode == RESULT_CODE_ENABLE_GPS_CHECKIN) {
            startCheckin();
        } else if (requestCode == RESULT_CODE_ENABLE_GPS_CHECKOUT) {
            startCheckOut();
        }
    }


    /*-*********************************************-*/
    /*-				Dialog Listeners				-*/
    /*-*********************************************-*/
    private final AcceptBundleDialog.Listener _acceptBundleDialogConfirmListener = new AcceptBundleDialog.Listener() {

        @Override
        public void onOk(Workorder workorder) {
            _confirmDialog.show(_workorder, workorder.getSchedule());
        }
    };

    private final AcceptBundleDialog.Listener _acceptBundleDialogExpiresListener = new AcceptBundleDialog.Listener() {
        @Override
        public void onOk(Workorder workorder) {
            _expiresDialog.show(workorder);
        }
    };

    private final AppPickerDialog.Listener _appdialog_listener = new AppPickerDialog.Listener() {

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
                String packageName = getActivity().getPackageName();
                File externalPath = Environment.getExternalStorageDirectory();
                new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/temp").mkdirs();
                File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/temp/IMAGE-" + System.currentTimeMillis() + ".png");
                _tempFile = temppath;
                src.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temppath));
                startActivityForResult(src, RESULT_CODE_GET_CAMERA_PIC);
            }
            setLoading(true);
        }
    };

    private final ClosingNotesDialog.Listener _closingNotes_onOk = new ClosingNotesDialog.Listener() {
        @Override
        public void onOk(String message) {
            WorkorderClient.actionSetClosingNotes(getActivity(), _workorder.getWorkorderId(), message);
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
                GoogleAnalyticsTopicClient.dispatchEvent(getActivity(), "WorkorderActivity",
                        GoogleAnalyticsTopicClient.EventAction.CONFIRM_ASSIGN, "WorkFragment", 1);
                long end = durationMilliseconds + ISO8601.toUtc(startDate);
                WorkorderClient.actionConfirmAssignment(getActivity(),
                        _workorder.getWorkorderId(), startDate, ISO8601.fromUTC(end));
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
            _termsDialog.show(getString(R.string.dialog_terms_title), getString(R.string.dialog_terms_body));
        }
    };

    private final CounterOfferDialog.Listener _counterOffer_listener = new CounterOfferDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String reason, boolean expires,
                         int expirationInSeconds, Pay pay, Schedule schedule, Expense[] expenses) {
            GoogleAnalyticsTopicClient.dispatchEvent(getActivity(), "WorkorderActivity",
                    GoogleAnalyticsTopicClient.EventAction.COUNTER, "WorkFragment", 1);

            WorkorderClient.actionCounterOffer(getActivity(), workorder.getWorkorderId(), expires,
                    reason, expirationInSeconds, pay, schedule, expenses);
            setLoading(true);
        }
    };

    private final CustomFieldDialog.Listener _customFieldDialog_listener = new CustomFieldDialog.Listener() {
        @Override
        public void onOk(CustomField field, String value) {
            WorkorderClient.actionCustomField(getActivity(), _workorder.getWorkorderId(),
                    field.getCustomLabelId(), value);
            setLoading(true);
        }
    };

    private final DeclineDialog.Listener _declineDialog_listener = new DeclineDialog.Listener() {
        @Override
        public void onOk(boolean blockBuyer, int reasonId, String details) {
            WorkorderClient.actionDecline(getActivity(), _workorder.getWorkorderId());
            if (blockBuyer) {
                ProfileClient.actionBlockCompany(getActivity(),
                        GlobalState.getContext().getProfile().getUserId(),
                        _workorder.getCompanyId(), reasonId, details);
            }

            getActivity().finish();
        }

        @Override
        public void onCancel() {
        }
    };

    private final DeviceCountDialog.Listener _deviceCountListener = new DeviceCountDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, int count) {
            _deviceCount = count;
            startCheckOut();
        }

        @Override
        public void onCancel() {
            setLoading(false);
        }
    };

    private final DiscountDialog.Listener _discountDialog_listener = new DiscountDialog.Listener() {
        @Override
        public void onOk(String description, double amount) {
            WorkorderClient.createDiscount(getActivity(), _workorder.getWorkorderId(),
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
            WorkorderClient.createExpense(getActivity(), _workorder.getWorkorderId(), description,
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
                    e.printStackTrace();
                }
            }

            GoogleAnalyticsTopicClient.dispatchEvent(getActivity(), "WorkorderActivity",
                    GoogleAnalyticsTopicClient.EventAction.REQUEST_WORK, "WorkFragment", 1);
            WorkorderClient.actionRequest(getActivity(), _workorder.getWorkorderId(), seconds);
            setLoading(true);

        }
    };

    private final MarkCompleteDialog.Listener _markCompleteDialog_listener = new MarkCompleteDialog.Listener() {
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
            GoogleAnalyticsTopicClient.dispatchEvent(getActivity(), "WorkorderActivity",
                    GoogleAnalyticsTopicClient.EventAction.COMPLETE_WORK, "WorkFragment", 1);
            try {
                GoogleAnalyticsTopicClient.dispatchEvent(getActivity(), "WorkorderActivity",
                        GoogleAnalyticsTopicClient.EventAction.COMPLETE_FN_EARNED, "WorkFragment",
                        (long) (_workorder.getExpectedPayment().getExpectedFee() * 100));
            } catch (Exception ex) {
                // I don't expect this to ever fail, but it could. just a safe guard.
                ex.printStackTrace();
            }
            try {
                GoogleAnalyticsTopicClient.dispatchEvent(getActivity(), "WorkorderActivity",
                        GoogleAnalyticsTopicClient.EventAction.COMPLETE_FN_EARNED_GROSS, "WorkFragment",
                        (long) (_workorder.getExpectedPayment().getExpectedTotal() * 100));
            } catch (Exception ex) {
                // I don't expect this to ever fail, but it could. just a safe guard.
                ex.printStackTrace();
            }

            WorkorderClient.actionComplete(getActivity(), _workorder.getWorkorderId());
            GlobalState.getContext().setCompletedWorkorder();

            setLoading(true);
        }
    };

    private final ShipmentAddDialog.Listener _shipmentAddDialog_listener = new ShipmentAddDialog.Listener() {
        @Override
        public void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite) {
            WorkorderClient.createShipment(getActivity(), _workorder.getWorkorderId(), description, shipToSite,
                    carrier, carrierName, trackingId);
            setLoading(true);
        }

        @Override
        public void onOk(String trackingId, String carrier, String carrierName, String description,
                         boolean shipToSite, long taskId) {
            WorkorderClient.createShipment(getActivity(), _workorder.getWorkorderId(), description, shipToSite,
                    carrier, carrierName, trackingId, taskId);
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }
    };

    private final TaskShipmentAddDialog.Listener taskShipmentAddDialog_listener = new TaskShipmentAddDialog.Listener() {
        @Override
        public void onDelete(Workorder workorder, int shipmentId) {
            WorkorderClient.deleteShipment(getActivity(), workorder.getWorkorderId(), shipmentId);
            setLoading(true);
        }

        @Override
        public void onAssign(Workorder workorder, int shipmentId, long taskId) {
            WorkorderClient.actionCompleteShipmentTask(getActivity(), workorder.getWorkorderId(), shipmentId, taskId);
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onAddShipmentDetails(Workorder workorder, String trackingId, String carrier, String carrierName, String description, boolean shipToSite) {
            WorkorderClient.actionSetShipmentDetails(getActivity(), workorder.getWorkorderId(), description,
                    shipToSite, carrier, carrierName, trackingId);
            setLoading(true);
        }

        @Override
        public void onAddShipmentDetails(Workorder workorder, String trackingId, String carrier, String carrierName, String description, boolean shipToSite, long taskId) {
            WorkorderClient.actionSetShipmentDetails(getActivity(), workorder.getWorkorderId(), description,
                    shipToSite, carrier, carrierName, trackingId, taskId);
            setLoading(true);
        }
    };

    private final WorkLogDialog.Listener _worklogDialog_listener = new WorkLogDialog.Listener() {
        @Override
        public void onOk(LoggedWork loggedWork, Calendar start, Calendar end, int deviceCount) {
            if (loggedWork == null) {
                if (deviceCount <= 0) {
                    WorkorderClient.addTimeLog(GlobalState.getContext(), _workorder.getWorkorderId(),
                            start.getTimeInMillis(), end.getTimeInMillis());
                } else {
                    WorkorderClient.addTimeLog(GlobalState.getContext(), _workorder.getWorkorderId(),
                            start.getTimeInMillis(), end.getTimeInMillis(), deviceCount);
                }
            } else {
                if (deviceCount <= 0) {
                    WorkorderClient.updateTimeLog(GlobalState.getContext(), _workorder.getWorkorderId(),
                            loggedWork.getLoggedHoursId(), start.getTimeInMillis(), end.getTimeInMillis());
                } else {
                    WorkorderClient.updateTimeLog(GlobalState.getContext(), _workorder.getWorkorderId(),
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
            startActivityForResult(intent, RESULT_CODE_ENABLE_GPS_CHECKIN);
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
            startActivityForResult(intent, RESULT_CODE_ENABLE_GPS_CHECKOUT);
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
        public void onComplete() {
            _markCompleteDialog.show(_workorder);
        }

        @Override
        public void onCheckOut() {
            Pay pay = _workorder.getPay();
            if (pay != null && pay.isPerDeviceRate()) {
                _deviceCountDialog.show(_workorder, pay.getMaxDevice());
                setLoading(true);
            } else {
                startCheckOut();
            }
        }

        @Override
        public void onCheckIn() {
            Log.v(TAG, "onCheckIn");
            startCheckin();
        }

        @Override
        public void onAcknowledge() {
            WorkorderClient.actionAcknowledgeHold(GlobalState.getContext(), _workorder.getWorkorderId());

            setLoading(true);
        }

        @Override
        public void onConfirm() {
            if (_workorder.isBundle()) {
                _acceptBundleWOConfirmDialog.show(_workorder);
            } else {
                _confirmDialog.show(_workorder, _workorder.getSchedule());
            }
        }

        @Override
        public void onEnterClosingNotes() {
            showClosingNotesDialog();
        }
    };

    private final ActionView.Listener _actionView_listener = new ActionView.Listener() {

        @Override
        public void onRequest(Workorder workorder) {
            if (workorder.isBundle()) {
                _acceptBundleWOExpiresDialog.show(workorder);
            } else {
                _expiresDialog.show(workorder);
            }
        }

        @Override
        public void onShowCounterOfferDialog(Workorder workorder) {
            _counterOfferDialog.show(workorder);
        }

        @Override
        public void onWithdrawRequest(Workorder workorder) {
            WorkorderClient.actionWithdrawRequest(GlobalState.getContext(), workorder.getWorkorderId());
        }

        @Override
        public void onNotInterested(Workorder workorder) {
            _declineDialog.show();
        }

        @Override
        public void onConfirmAssignment(Workorder workorder) {
            if (workorder.isBundle()) {
                _acceptBundleWOConfirmDialog.show(workorder);
            } else {
                _confirmDialog.show(_workorder, workorder.getSchedule());
            }
        }

        @Override
        public void onComplete(Workorder workorder) {
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
        }

        @Override
        public void onShowTerms(Workorder workorder) {
            _termsDialog.show(getString(R.string.dialog_terms_title),
                    getString(R.string.dialog_terms_body));
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
            _yesNoDialog.setData("Delete Expense",
                    "Are you sure you want to delete this expense?", "YES", "NO",
                    new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
                            WorkorderClient.deleteExpense(GlobalState.getContext(),
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
            _discountDialog.show("Add Discount");
        }

        @Override
        public void discountOnClick(Discount discount) {
            // TODO discountOnClick
        }

        @Override
        public void discountLongClick(final Discount discount) {
            _yesNoDialog.setData("Delete Discount",
                    "Are you sure you want to delete this discount?", "YES", "NO",
                    new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
                            WorkorderClient.deleteDiscount(GlobalState.getContext(),
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
            _shipmentAddDialog.show("Add Shipment", 0);
        }

        @Override
        public void onDelete(Workorder workorder, int shipmentId) {
            WorkorderClient.deleteShipment(GlobalState.getContext(), workorder.getWorkorderId(), shipmentId);
            setLoading(true);
        }

        @Override
        public void onAssign(Workorder workorder, int shipmentId) {
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
            _yesNoDialog.setData("Delete Signature",
                    "Are you sure you want to delete this signature?", "YES", "NO",
                    new TwoButtonDialog.Listener() {
                        @Override
                        public void onPositive() {
                            WorkorderClient.deleteSignature(GlobalState.getContext(),
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

    private final WorkSummaryView.Listener _summaryView_listener = new WorkSummaryView.Listener() {
        @Override
        public void showConfidentialInfo(String body) {
            _termsDialog.show("Confidential Information", body);
        }

        @Override
        public void showCustomerPolicies(String body) {
            _termsDialog.show("Policies And Procedures", body);
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
                _deviceCountDialog.show(_workorder, pay.getMaxDevice());
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
            _confirmDialog.show(_workorder, _workorder.getSchedule());
        }

        @Override
        public void onCustomField(Task task) {
            if (task.getCompleted())
                return;
            // TODO, get custom field info, preset dialog
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
                            WorkorderClient.actionCompleteTask(GlobalState.getContext(),
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
            startActivityForResult(intent, RESULT_CODE_SEND_EMAIL);

            if (!task.getCompleted()) {
                WorkorderClient.actionCompleteTask(GlobalState.getContext(),
                        _workorder.getWorkorderId(), task.getTaskId());
            }
            setLoading(true);
        }

        @Override
        public void onPhone(Task task) {
            if (!task.getCompleted()) {
                WorkorderClient.actionCompleteTask(GlobalState.getContext(),
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
                ex.printStackTrace();
            }
        }

        @Override
        public void onShipment(Task task) {
            ShipmentTracking[] shipments = _workorder.getShipmentTracking();
            if (shipments == null) {
                _shipmentAddDialog.show(getText(R.string.dialog_shipment_title), task.getTaskId());
            } else {
                _taskShipmentAddDialog.show("Assign/Add New", _workorder, task.getTaskId());
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
            WorkorderClient.actionCompleteTask(GlobalState.getContext(),
                    _workorder.getWorkorderId(), task.getTaskId());
            setLoading(true);
        }
    };

    private final TimeLogListView.Listener _timeLoggedView_listener = new TimeLogListView.Listener() {
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
            WorkorderClient.deleteTimeLog(GlobalState.getContext(), workorder.getWorkorderId(),
                    loggedWork.getLoggedHoursId());
            setLoading(true);
        }
    };

    private final View.OnClickListener _bundle_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), WorkorderBundleDetailActivity.class);
            intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_WORKORDER_ID, _workorder.getWorkorderId());
            intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_BUNDLE_ID, _workorder.getBundleId());
            getActivity().startActivity(intent);
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
        }

        @Override
        public void onTaskList(long workorderId, List<Task> tasks, boolean failed) {
            setTasks(tasks);
        }
    };

    private final ProfileClient.Listener _profileClient_listener = new ProfileClient.Listener() {
        @Override
        public void onConnected() {
            _profileClient.subActions();
        }

        @Override
        public void onAction(long profileId, String action, boolean failed) {
            //TODO _profileClient_listener.onAction
            // TODO ... do something!
            Log.v(TAG, "_profileClient_listener.onAction");
        }
    };
}
