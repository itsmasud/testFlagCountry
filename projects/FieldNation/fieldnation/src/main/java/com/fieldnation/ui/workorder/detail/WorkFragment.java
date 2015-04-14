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

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.FileHelper;
import com.fieldnation.GlobalState;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthTopicReceiver;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.data.workorder.CustomField;
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
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.topics.GaTopic;
import com.fieldnation.topics.TopicService;
import com.fieldnation.ui.AppPickerPackage;
import com.fieldnation.ui.GpsLocationService;
import com.fieldnation.ui.OverScrollView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.SignOffActivity;
import com.fieldnation.ui.SignatureDisplayActivity;
import com.fieldnation.ui.SignatureListView;
import com.fieldnation.ui.SignatureTileView;
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
    private static final String TAG = "ui.workorder.detail.WorkFragment";

    // Activity result codes
    private static final int RESULT_CODE_BASE = 200;
    private static final int RESULT_CODE_SEND_EMAIL = RESULT_CODE_BASE + 1;
    private static final int RESULT_CODE_GET_ATTACHMENT = RESULT_CODE_BASE + 3;
    private static final int RESULT_CODE_GET_CAMERA_PIC = RESULT_CODE_BASE + 4;
    private static final int RESULT_CODE_GET_SIGNATURE = RESULT_CODE_BASE + 5;
    private static final int RESULT_CODE_ENABLE_GPS_CHECKIN = RESULT_CODE_BASE + 6;
    private static final int RESULT_CODE_ENABLE_GPS_CHECKOUT = RESULT_CODE_BASE + 7;

    // Web request result codes
    private static final int WEB_CHANGED = 1;
    private static final int WEB_GET_TASKS = 2;
    private static final int WEB_SEND_DELIVERABLE = 3;
    private static final int WEB_COMPLETE_WORKORDER = 4;
    private static final int WEB_NOTHING = 5;

    // saved state keys
    private static final String STATE_WORKORDER = "ui.workorder.detail.WorkFragment:STATE_WORKORDER";
    private static final String STATE_AUTHTOKEN = "ui.workorder.detail.WorkFragment:STATE_AUTHTOKEN";
    private static final String STATE_USERNAME = "ui.workorder.detail.WorkFragment:STATE_USERNAME";
    private static final String STATE_TASKS = "ui.workorder.detail.WorkFragment:STATE_TASKS";
    private static final String STATE_CURRENT_TASK = "ui.workorder.detail.WorkFragment:STATE_CURRENT_TASK";
    private static final String STATE_SIGNATURES = "ui.workorder.detail.WorkFragment:STATE_SIGNATURES";
    private static final String STATE_DEVICE_COUNT = "ui.workorder.detail.WorkFragment:STATE_DEVICE_COUNT";


    // UI
    private OverScrollView _scrollView;
    private ActionBarTopView _topBar;
    private SummaryView _sumView;
    private LocationView _locView;
    private ScheduleSummaryView _scheduleView;
    private ExpectedPaymentView _exView;
    private TextView _bundleWarningTextView;
    private TimeLoggedView _timeLogged;
    private TaskListView _taskList;
    private CustomFieldListView _customFields;
    private ShipmentView _shipments;
    private SignatureListView _signatureView;
    private ClosingNotesView _closingNotes;
    private PaymentView _payView;
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

    // Data
    private WorkorderService _service;
    private ProfileService _profileService;

    private boolean _isCached = true;
    private File _tempFile;
    private GpsLocationService _gpsLocationService;
    private List<Signature> _signatures = null;
    private List<Task> _tasks = null;
    private SecureRandom _rand = new SecureRandom();
    private String _authToken;
    private String _username;
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

        _sumView = (SummaryView) view.findViewById(R.id.summary_view);
        _sumView.setListener(_summaryView_listener);

        _locView = (LocationView) view.findViewById(R.id.location_view);
        _scheduleView = (ScheduleSummaryView) view
                .findViewById(R.id.schedule_view);

        _payView = (PaymentView) view.findViewById(R.id.payment_view);
        _payView.showDetails(true);
        _payView.setListener(_paymentView_listener);

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

        _shipments = (ShipmentView) view.findViewById(R.id.shipment_view);
        _shipments.setListener(_shipments_listener);

        _taskList = (TaskListView) view.findViewById(R.id.scope_view);
        _taskList.setTaskListViewListener(_taskListView_listener);

        _timeLogged = (TimeLoggedView) view.findViewById(R.id.timelogged_view);
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
            if (savedInstanceState.containsKey(STATE_DEVICE_COUNT)) {
                _deviceCount = savedInstanceState.getInt(STATE_DEVICE_COUNT);
            }
            if (_authToken != null && _username != null) {
                _service = new WorkorderService(view.getContext(), _username, _authToken, _resultReceiver);
                _profileService = new ProfileService(view.getContext(), _username, _authToken, _resultReceiver);
            }
        }

        populateUi(true);
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
        _appDialog.addIntent(GlobalState.getContext().getPackageManager(), intent, "Get Content");

        if (GlobalState.getContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            _appDialog.addIntent(GlobalState.getContext().getPackageManager(), intent, "Take Picture");
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        AuthTopicService.subscribeAuthState(GlobalState.getContext(), 0, TAG, _authReceiver);

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

        _locationLoadingDialog.setData(getString(R.string.dialog_location_loading_title),
                getString(R.string.dialog_location_loading_body),
                getString(R.string.dialog_location_loading_button),
                _locationLoadingDialog_listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        AuthTopicService.subscribeAuthState(GlobalState.getContext(), 0, TAG, _authReceiver);

        _gpsLocationService = new GpsLocationService(GlobalState.getContext());
    }

    @Override
    public void onPause() {
        TopicService.delete(GlobalState.getContext(), TAG);
        _gpsLocationService.stopLocationUpdates();
        super.onPause();
    }

    @Override
    public void update() {
    }

    @Override
    public void setWorkorder(Workorder workorder, boolean isCached) {
        _workorder = workorder;
        requestTasks(true);
        populateUi(isCached);
    }

    private void setTasks(List<Task> tasks, boolean isCached) {
        _tasks = tasks;
        _taskList.setData(_workorder, tasks, isCached);
        if (isCached) {
            requestTasks(false);
            setLoading(true);
        } else {
            setLoading(false);
        }
    }

    private void populateUi(boolean isCached) {
        if (_workorder == null)
            return;

        if (_sumView != null) {
            _sumView.setWorkorder(_workorder, isCached);
        }

        if (_locView != null) {
            _locView.setWorkorder(_workorder, isCached);
        }

        if (_scheduleView != null) {
            _scheduleView.setWorkorder(_workorder, isCached);
        }

        if (_payView != null) {
            _payView.setWorkorder(_workorder, isCached);
        }

        if (_actionView != null) {
            _actionView.setWorkorder(_workorder, isCached);
        }

        if (_topBar != null) {
            _topBar.setWorkorder(_workorder, isCached);
        }

        if (_exView != null) {
            _exView.setWorkorder(_workorder, isCached);
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

        if (!isCached)
            setLoading(false);

        if (_bundleWarningTextView != null) {
            if (_workorder.getBundleId() != null && _workorder.getBundleId() > 0) {
                _bundleWarningTextView.setVisibility(View.VISIBLE);
                _bundleWarningTextView.setText(String.format(getString(R.string.workorder_bundle_warning), _workorder.getBundleCount()));
            } else {
                _bundleWarningTextView.setVisibility(View.GONE);
            }
        }
    }

    private void requestWorkorder(boolean allowCache) {
        if (_workorder == null)
            return;

        Log.v(TAG, "getData.startRefreshing");
        setLoading(true);
        _workorder.dispatchOnChange();
    }

    private void requestTasks(boolean allowCache) {
        if (_workorder == null)
            return;

        if (_service == null)
            return;

        GlobalState.getContext().startService(_service.getTasks(WEB_GET_TASKS, _workorder.getWorkorderId(), allowCache));
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
        Intent intent = new Intent(GlobalState.getContext(), WorkorderActivity.class);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB,
                WorkorderActivity.TAB_DETAILS);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID,
                _workorder.getWorkorderId());

        return PendingIntent.getActivity(GlobalState.getContext(), _rand.nextInt(), intent,
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
        if (_gpsLocationService == null || _service == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startCheckin();
                }
            }, 500);
            return;
        }

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
        if (_gpsLocationService == null || _service == null || GlobalState.getContext() == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startCheckOut();
                }
            }, 500);
            return;
        }
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
        GaTopic.dispatchEvent(GlobalState.getContext(), "WorkorderActivity", GaTopic.ACTION_CHECKIN, "WorkFragment", 1);
        if (_gpsLocationService.hasLocation()) {
            GlobalState.getContext().startService(
                    _service.checkin(WEB_CHANGED, _workorder.getWorkorderId(), _gpsLocationService.getLocation()));
        } else {
            GlobalState.getContext().startService(
                    _service.checkin(WEB_CHANGED, _workorder.getWorkorderId()));
        }

    }

    private void doCheckOut() {
        setLoading(true);
        _gpsLocationService.setListener(null);
        GaTopic.dispatchEvent(GlobalState.getContext(), "WorkorderActivity", GaTopic.ACTION_CHECKOUT, "WorkFragment", 1);
        if (_gpsLocationService.hasLocation()) {
            if (_deviceCount > -1) {
                GlobalState.getContext().startService(
                        _service.checkout(WEB_CHANGED, _workorder.getWorkorderId(), _deviceCount, _gpsLocationService.getLocation()));
            } else {
                GlobalState.getContext().startService(
                        _service.checkout(WEB_CHANGED, _workorder.getWorkorderId(), _gpsLocationService.getLocation()));
            }
        } else {
            if (_deviceCount > -1) {
                GlobalState.getContext().startService(
                        _service.checkout(WEB_CHANGED, _workorder.getWorkorderId(), _deviceCount));
            } else {
                GlobalState.getContext().startService(
                        _service.checkout(WEB_CHANGED, _workorder.getWorkorderId()));
            }
        }

    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private OneButtonDialog.Listener _locationLoadingDialog_listener = new OneButtonDialog.Listener() {
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
                GlobalState.getContext().startService(_service.uploadDeliverable(WEB_SEND_DELIVERABLE,
                        _workorder.getWorkorderId(), _currentTask.getSlotId(),
                        _tempFile.getAbsolutePath(), getNotificationIntent()));
            } else {
                GlobalState.getContext().startService(_service.uploadDeliverable(
                        WEB_SEND_DELIVERABLE, _workorder.getWorkorderId(),
                        _currentTask.getSlotId(), data, getNotificationIntent()));
            }
        } else if (requestCode == RESULT_CODE_GET_SIGNATURE && resultCode == Activity.RESULT_OK) {
            GlobalState gs = GlobalState.getContext();
            if (gs.shouldShowReviewDialog()) {
                showReviewDialog();
                gs.setShownReviewDialog();
                requestWorkorder(false);
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
    private AcceptBundleDialog.Listener _acceptBundleDialogConfirmListener = new AcceptBundleDialog.Listener() {

        @Override
        public void onOk(Workorder workorder) {
            _confirmDialog.show(_workorder, workorder.getSchedule());
        }
    };

    private AcceptBundleDialog.Listener _acceptBundleDialogExpiresListener = new AcceptBundleDialog.Listener() {
        @Override
        public void onOk(Workorder workorder) {
            _expiresDialog.show(workorder);
        }
    };

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
                String packageName = GlobalState.getContext().getPackageName();
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

    private ClosingNotesDialog.Listener _closingNotes_onOk = new ClosingNotesDialog.Listener() {
        @Override
        public void onOk(String message) {
            GlobalState.getContext().startService(_service.closingNotes(WEB_CHANGED, _workorder.getWorkorderId(), message));
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }
    };

    private ConfirmDialog.Listener _confirmListener = new ConfirmDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String startDate, long durationMilliseconds) {
            try {
                GaTopic.dispatchEvent(GlobalState.getContext(), "WorkorderActivity", GaTopic.ACTION_CONFIRM_ASSIGN, "WorkFragment", 1);
                long end = durationMilliseconds + ISO8601.toUtc(startDate);
                GlobalState.getContext().startService(_service.confirmAssignment(WEB_CHANGED,
                        _workorder.getWorkorderId(), startDate, ISO8601.fromUTC(end)));

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
        }

    };

    private CounterOfferDialog.Listener _counterOffer_listener = new CounterOfferDialog.Listener() {
        @Override
        public void onOk(Workorder workorder, String reason, boolean expires, int expirationInSeconds,
                         Pay pay, Schedule schedule, Expense[] expenses) {
            GaTopic.dispatchEvent(GlobalState.getContext(), "WorkorderActivity", GaTopic.ACTION_COUNTER, "WorkFragment", 1);
            GlobalState.getContext().startService(
                    _service.setCounterOffer(WEB_CHANGED, workorder.getWorkorderId(), expires, reason,
                            expirationInSeconds, pay, schedule, expenses));
            setLoading(true);
        }
    };

    private CustomFieldDialog.Listener _customFieldDialog_listener = new CustomFieldDialog.Listener() {
        @Override
        public void onOk(CustomField field, String value) {
            GlobalState.getContext().startService(
                    _service.setCustomField(WEB_CHANGED, _workorder.getWorkorderId(), field.getCustomLabelId(), value));
            setLoading(true);
        }
    };

    private DeclineDialog.Listener _declineDialog_listener = new DeclineDialog.Listener() {
        @Override
        public void onOk(boolean blockBuyer, int reasonId, String details) {
            GlobalState.getContext().startService(_service.decline(WEB_CHANGED, _workorder.getWorkorderId()));
            if (blockBuyer) {
                GlobalState gs = GlobalState.getContext();
                gs.startService(
                        _profileService.addBlockedCompany(WEB_NOTHING, gs.getProfile().getUserId(), _workorder.getCompanyId(), reasonId, details));
            }
            if (getActivity() != null)
                getActivity().finish();
        }

        @Override
        public void onCancel() {
        }
    };

    private DeviceCountDialog.Listener _deviceCountListener = new DeviceCountDialog.Listener() {
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

    private DiscountDialog.Listener _discountDialog_listener = new DiscountDialog.Listener() {
        @Override
        public void onOk(String description, double amount) {
            GlobalState.getContext().startService(
                    _service.addDiscount(WEB_CHANGED,
                            _workorder.getWorkorderId(), amount, description));
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }
    };

    private ExpenseDialog.Listener _expenseDialog_listener = new ExpenseDialog.Listener() {
        @Override
        public void onOk(String description, double amount, ExpenseCategory category) {
            GlobalState.getContext().startService(
                    _service.addExpense(WEB_CHANGED, _workorder.getWorkorderId(),
                            description, amount, category));
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }
    };

    private ExpiresDialog.Listener _expiresDialog_listener = new ExpiresDialog.Listener() {
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
            GaTopic.dispatchEvent(GlobalState.getContext(), "WorkorderActivity", GaTopic.ACTION_REQUEST_WORK, "WorkFragment", 1);
            GlobalState.getContext().startService(
                    _service.request(WEB_CHANGED,
                            _workorder.getWorkorderId(), seconds));
            setLoading(true);
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
            }.executeEx(GlobalState.getContext(), _workorder);
        }

        @Override
        public void onContinueClick() {
            GaTopic.dispatchEvent(GlobalState.getContext(), "WorkorderActivity", GaTopic.ACTION_COMPLETE_WORK, "WorkFragment", 1);
            try {
                GaTopic.dispatchEvent(GlobalState.getContext(), "WorkorderActivity",
                        GaTopic.ACTION_COMPLETE_FN_EARNED, "WorkFragment",
                        (long) (_workorder.getExpectedPayment().getExpectedFee() * 100));
            } catch (Exception ex) {
                // I don't expect this to ever fail, but it could. just a safe guard.
                ex.printStackTrace();
            }
            try {
                GaTopic.dispatchEvent(GlobalState.getContext(), "WorkorderActivity",
                        GaTopic.ACTION_COMPLETE_FN_EARNED_GROSS, "WorkFragment",
                        (long) (_workorder.getExpectedPayment().getExpectedTotal() * 100));
            } catch (Exception ex) {
                // I don't expect this to ever fail, but it could. just a safe guard.
                ex.printStackTrace();
            }

            GlobalState.getContext().startService(
                    _service.complete(WEB_COMPLETE_WORKORDER, _workorder.getWorkorderId()));
            setLoading(true);
        }
    };

    private ShipmentAddDialog.Listener _shipmentAddDialog_listener = new ShipmentAddDialog.Listener() {
        @Override
        public void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite) {
            GlobalState.getContext().startService(
                    _service.addShipmentDetails(WEB_CHANGED, _workorder.getWorkorderId(), description, shipToSite,
                            carrier, carrierName, trackingId));
            setLoading(true);
        }

        @Override
        public void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite, long taskId) {
            GlobalState.getContext().startService(
                    _service.addShipmentDetails(WEB_CHANGED, _workorder.getWorkorderId(), description, shipToSite,
                            carrier, carrierName, trackingId, taskId));
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }
    };

    private TaskShipmentAddDialog.Listener taskShipmentAddDialog_listener = new TaskShipmentAddDialog.Listener() {
        @Override
        public void onDelete(Workorder workorder, int shipmentId) {
            GlobalState.getContext().startService(_service.deleteShipment(WEB_CHANGED, workorder.getWorkorderId(), shipmentId));
            setLoading(true);
        }

        @Override
        public void onAssign(Workorder workorder, int shipmentId, long taskId) {
            Log.v(TAG, "Method Stub: onAssign()" + shipmentId + "=" + taskId);
            GlobalState.getContext().startService(
                    _service.completeShipmentTask(WEB_CHANGED, workorder.getWorkorderId(), shipmentId, taskId));
            setLoading(true);
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onAddShipmentDetails(Workorder workorder, String trackingId, String carrier, String carrierName, String description, boolean shipToSite) {
            GlobalState.getContext().startService(
                    _service.addShipmentDetails(WEB_CHANGED, workorder.getWorkorderId(), description,
                            shipToSite, carrier, carrierName, trackingId));
            setLoading(true);
        }

        @Override
        public void onAddShipmentDetails(Workorder workorder, String trackingId, String carrier, String carrierName, String description, boolean shipToSite, long taskId) {
            GlobalState.getContext().startService(
                    _service.addShipmentDetails(WEB_CHANGED, workorder.getWorkorderId(), description,
                            shipToSite, carrier, carrierName, trackingId, taskId));
            setLoading(true);
        }
    };

    private WorkLogDialog.Listener _worklogDialog_listener = new WorkLogDialog.Listener() {
        @Override
        public void onOk(LoggedWork loggedWork, Calendar start, Calendar end, int deviceCount) {
            if (loggedWork == null) {
                if (deviceCount <= 0) {
                    GlobalState.getContext().startService(
                            _service.logTime(WEB_CHANGED, _workorder.getWorkorderId(), start.getTimeInMillis(),
                                    end.getTimeInMillis()));
                } else {
                    GlobalState.getContext().startService(
                            _service.logTime(WEB_CHANGED, _workorder.getWorkorderId(), start.getTimeInMillis(),
                                    end.getTimeInMillis(), deviceCount));
                }
            } else {
                if (deviceCount <= 0) {
                    GlobalState.getContext().startService(
                            _service.updateLogTime(WEB_CHANGED, _workorder.getWorkorderId(),
                                    loggedWork.getLoggedHoursId(), start.getTimeInMillis(), end.getTimeInMillis()));
                } else {
                    GlobalState.getContext().startService(
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

    /*-*****************************************-*/
    /*-				View Listeners				-*/
    /*-*****************************************-*/
    private LocationDialog.Listener _locationDialog_checkInListener = new LocationDialog.Listener() {
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

    private LocationDialog.Listener _locationDialog_checkOutListener = new LocationDialog.Listener() {
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

    private ActionBarTopView.Listener _actionbartop_listener = new ActionBarTopView.Listener() {
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
            startCheckin();
        }

        @Override
        public void onAcknowledge() {
            GlobalState.getContext().startService(
                    _service.acknowledgeHold(WEB_CHANGED, _workorder.getWorkorderId()));
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

    private ActionView.Listener _actionView_listener = new ActionView.Listener() {

        @Override
        public void onRequest(Workorder workorder) {
            if (workorder.isBundle()) {
                _acceptBundleWOExpiresDialog.show(workorder);
            } else {
                _expiresDialog.show(workorder);
            }
        }

        @Override
        public void onWithdrawRequest(Workorder workorder) {
            getActivity().startService(
                    _service.withdrawRequest(WEB_CHANGED, workorder.getWorkorderId()));
        }

        @Override
        public void onShowCounterOfferDialog(Workorder workorder) {
            _counterOfferDialog.show(workorder);
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

    private ClosingNotesView.Listener _clockingNotesView_listener = new ClosingNotesView.Listener() {
        @Override
        public void onChangeClosingNotes(String closingNotes) {
            showClosingNotesDialog();
        }
    };

    private CustomFieldRowView.Listener _customFields_listener = new CustomFieldRowView.Listener() {
        @Override
        public void onClick(CustomFieldRowView view, CustomField field) {
            _customFieldDialog.show(field);
        }
    };

    private PaymentView.Listener _paymentView_listener = new PaymentView.Listener() {

        @Override
        public void onDeleteExpense(Workorder workorder,
                                    Expense expense) {
            GlobalState.getContext().startService(_service.deleteExpense(WEB_CHANGED,
                    _workorder.getWorkorderId(),
                    expense.getExpenseId()));
            setLoading(true);
        }

        @Override
        public void onShowAddDiscountDialog() {
            _discountDialog.show("Add Discount");
        }

        @Override
        public void onShowAddExpenseDialog() {
            _expenseDialog.show(true);
        }

        @Override
        public void onShowCounterOfferDialog() {
            _counterOfferDialog.show(_workorder);
        }

        @Override
        public void onShowTerms() {
            _termsDialog.show();
        }

        @Override
        public void onDeleteDiscount(Workorder workorder, int discountId) {
            GlobalState.getContext().startService(
                    _service.deleteDiscount(WEB_CHANGED,
                            _workorder.getWorkorderId(), discountId));
            setLoading(true);
        }
    };

    private RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            requestWorkorder(false);
        }
    };

    private ShipmentView.Listener _shipments_listener = new ShipmentView.Listener() {

        @Override
        public void addShipment() {
            _shipmentAddDialog.show("Add Shipment", 0);
        }

        @Override
        public void onDelete(Workorder workorder, int shipmentId) {
            GlobalState.getContext().startService(_service.deleteShipment(WEB_CHANGED, workorder.getWorkorderId(), shipmentId));
            setLoading(true);
        }

        @Override
        public void onAssign(Workorder workorder, int shipmentId) {
            // TODO STUB .onAssign()
            Log.v(TAG, "STUB .onAssign()");
            // TODO present a picker of the tasks that this can be assigned too
        }
    };

    private SignatureListView.Listener _signaturelist_listener = new SignatureListView.Listener() {
        @Override
        public void addSignature() {
            SignOffActivity.startSignOff(GlobalState.getContext(), _workorder);
            setLoading(true);
        }

        @Override
        public void signatureOnClick(SignatureTileView view, Signature signature) {
            SignatureDisplayActivity.startIntent(GlobalState.getContext(), signature.getSignatureId(), _workorder);
            setLoading(true);
        }
    };

    private SummaryView.Listener _summaryView_listener = new SummaryView.Listener() {
        @Override
        public void showConfidentialInfo(String body) {
            _termsDialog.show("Confidential Information", body);
        }

        @Override
        public void showCustomerPolicies(String body) {
            _termsDialog.show("Policies And Procedures", body);
        }
    };

    private TaskListView.Listener _taskListView_listener = new TaskListView.Listener() {
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
                            GlobalState.getContext().startService(
                                    _service.completeTask(WEB_CHANGED, _workorder.getWorkorderId(),
                                            task.getTaskId()));
                        }

                        FileHelper.viewOrDownloadFile(GlobalState.getContext(), doc.getFilePath(),
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
                GlobalState.getContext().startService(
                        _service.completeTask(WEB_CHANGED, _workorder.getWorkorderId(), task.getTaskId()));
            }
            setLoading(true);
        }

        @Override
        public void onPhone(Task task) {
            if (!task.getCompleted()) {
                GlobalState.getContext().startService(
                        _service.completeTask(WEB_CHANGED, _workorder.getWorkorderId(), task.getTaskId()));
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
            SignOffActivity.startSignOff(GlobalState.getContext(), _workorder, task.getTaskId());
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
            GlobalState.getContext().startService(
                    _service.completeTask(WEB_CHANGED, _workorder.getWorkorderId(), task.getTaskId()));
            setLoading(true);
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
            GlobalState.getContext().startService(
                    _service.deleteLogTime(WEB_CHANGED,
                            workorder.getWorkorderId(), loggedWork.getLoggedHoursId()));
            setLoading(true);
        }
    };

    private View.OnClickListener _bundle_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(GlobalState.getContext(), WorkorderBundleDetailActivity.class);
            intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_WORKORDER_ID, _workorder.getWorkorderId());
            intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_BUNDLE_ID, _workorder.getBundleId());
            getActivity().startActivity(intent);
            setLoading(true);
        }
    };

    /*-*****************************-*/
    /*-				Web				-*/
    /*-*****************************-*/
    private AuthTopicReceiver _authReceiver = new AuthTopicReceiver(new Handler()) {
        @Override
        public void onAuthentication(String username, String authToken, boolean isNew) {
            if (_service == null || isNew) {
                _username = username;
                _authToken = authToken;
                _service = new WorkorderService(GlobalState.getContext(), username, authToken, _resultReceiver);
                _profileService = new ProfileService(GlobalState.getContext(), username, authToken, _resultReceiver);
                requestWorkorder(true);
            }
        }

        @Override
        public void onAuthenticationFailed(boolean networkDown) {
            _service = null;
            _profileService = null;
        }

        @Override
        public void onAuthenticationInvalidated() {
            _service = null;
            _profileService = null;
        }

        @Override
        public void onRegister(int resultCode, String topicId) {
            AuthTopicService.requestAuthentication(GlobalState.getContext());
        }
    };

    private WebResultReceiver _resultReceiver = new WebResultReceiver(
            new Handler()) {

        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            if (resultCode == WEB_CHANGED || resultCode == WEB_SEND_DELIVERABLE) {
                requestWorkorder(false);
            } else if (resultCode == WEB_GET_TASKS) {
                new TaskParseAsyncTask().executeEx(resultData);
            } else if (resultCode == WEB_COMPLETE_WORKORDER) {
                GlobalState gs = GlobalState.getContext();
                gs.setCompletedWorkorder();

                if (gs.shouldShowReviewDialog()) {
                    showReviewDialog();
                    gs.setShownReviewDialog();
                }
                requestWorkorder(false);
            }
        }

        @Override
        public Context getContext() {
            return GlobalState.getContext();
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            _username = null;
            _authToken = null;
            _service = null;
            _profileService = null;
            AuthTopicService.requestAuthInvalid(GlobalState.getContext());
            try {
                Toast.makeText(GlobalState.getContext(), new String(resultData.getByteArray(KEY_RESPONSE_DATA)), Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    Toast.makeText(GlobalState.getContext(), R.string.toast_could_not_complete_request, Toast.LENGTH_LONG).show();
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                }
            }
        }
    };

    private class TaskParseAsyncTask extends AsyncTaskEx<Bundle, Object, List<Task>> {
        private boolean cached = false;

        @Override
        protected List<Task> doInBackground(Bundle... params) {
            Bundle resultData = params[0];
            String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));
            cached = resultData.getBoolean(WebServiceConstants.KEY_RESPONSE_CACHED);
            List<Task> tasks = new LinkedList<>();
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
            setTasks(tasks, cached);
        }
    }
}
