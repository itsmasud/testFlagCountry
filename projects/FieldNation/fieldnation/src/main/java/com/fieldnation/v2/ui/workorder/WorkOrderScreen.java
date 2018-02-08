package com.fieldnation.v2.ui.workorder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.BuildConfig;
import com.fieldnation.R;
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.contexts.SpStackContext;
import com.fieldnation.analytics.contexts.SpStatusContext;
import com.fieldnation.analytics.contexts.SpTracingContext;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.analytics.contexts.SpWorkOrderContext;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fnactivityresult.ActivityResultConstants;
import com.fieldnation.fnactivityresult.ActivityResultListener;
import com.fieldnation.fnanalytics.EventContext;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fngps.SimpleGps;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.FileUtils;
import com.fieldnation.service.GpsTrackingService;
import com.fieldnation.service.data.documents.DocumentClient;
import com.fieldnation.service.data.documents.DocumentConstants;
import com.fieldnation.ui.NestedScrollView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.SignOffActivity;
import com.fieldnation.ui.menu.MessagesMenuButton;
import com.fieldnation.ui.menu.MoreMenuButton;
import com.fieldnation.ui.payment.PaymentListActivity;
import com.fieldnation.ui.workorder.detail.CounterOfferSummaryView;
import com.fieldnation.ui.workorder.detail.ScheduleSummaryView;
import com.fieldnation.ui.workorder.detail.WorkSummaryView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.CheckInOut;
import com.fieldnation.v2.data.model.Condition;
import com.fieldnation.v2.data.model.Coords;
import com.fieldnation.v2.data.model.Date;
import com.fieldnation.v2.data.model.ETA;
import com.fieldnation.v2.data.model.ETAStatus;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.PayIncrease;
import com.fieldnation.v2.data.model.ProblemType;
import com.fieldnation.v2.data.model.Problems;
import com.fieldnation.v2.data.model.Requests;
import com.fieldnation.v2.data.model.Route;
import com.fieldnation.v2.data.model.Signature;
import com.fieldnation.v2.data.model.TimeLog;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.data.model.WorkOrderRatingsBuyer;
import com.fieldnation.v2.ui.dialog.AttachedFoldersDialog;
import com.fieldnation.v2.ui.dialog.ChatDialog;
import com.fieldnation.v2.ui.dialog.CheckInOutDialog;
import com.fieldnation.v2.ui.dialog.ClosingNotesDialog;
import com.fieldnation.v2.ui.dialog.CounterOfferDialog;
import com.fieldnation.v2.ui.dialog.DeclineDialog;
import com.fieldnation.v2.ui.dialog.DiscountDialog;
import com.fieldnation.v2.ui.dialog.EtaDialog;
import com.fieldnation.v2.ui.dialog.ExpenseDialog;
import com.fieldnation.v2.ui.dialog.HoldReviewDialog;
import com.fieldnation.v2.ui.dialog.MarkCompleteDialog;
import com.fieldnation.v2.ui.dialog.MarkIncompleteWarningDialog;
import com.fieldnation.v2.ui.dialog.PayDialog;
import com.fieldnation.v2.ui.dialog.RateBuyerDialog;
import com.fieldnation.v2.ui.dialog.RateBuyerYesNoDialog;
import com.fieldnation.v2.ui.dialog.ReportProblemDialog;
import com.fieldnation.v2.ui.dialog.RequestBundleDialog;
import com.fieldnation.v2.ui.dialog.RunningLateDialog;
import com.fieldnation.v2.ui.dialog.ShipmentAddDialog;
import com.fieldnation.v2.ui.dialog.TwoButtonDialog;
import com.fieldnation.v2.ui.dialog.WithdrawRequestDialog;
import com.fieldnation.v2.ui.dialog.WorkLogDialog;

import java.io.File;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class WorkOrderScreen extends RelativeLayout implements UUIDView {
    private static final String TAG = "WorkOrderScreen";

    // Dialog tags
    private static final String DIALOG_CANCEL_WARNING = TAG + ".cancelWarningDialog";
    private static final String DIALOG_MARK_COMPLETE = TAG + ".markCompleteDialog";
    private static final String DIALOG_RATE_BUYER_YESNO = TAG + ".rateBuyerYesNoDialog";
    private static final String DIALOG_REPORT_PROBLEM = TAG + ".reportProblemDialog";
    private static final String DIALOG_RUNNING_LATE = TAG + ".runningLateDialogLegacy";
    private static final String DIALOG_WITHDRAW = TAG + ".withdrawRequestDialog";
    private static final String DIALOG_WORKLOG = TAG + ".worklogDialog";
    private static final String DIALOG_PAY = TAG + ".payDialog";
    private static final String DIALOG_HOLD_REVIEW = TAG + ".holdReviewDialog";
    private static final String DIALOG_DELETE_WORKLOG = TAG + ".deleteWorkLogDialog";
    private static final String DIALOG_DELETE_SIGNATURE = TAG + ".deleteSignatureDialog";
    private static final String DIALOG_RATE_YESNO = TAG + ".rateBuyerYesNoDialog";
    private static final String DIALOG_ATTACHED_FOLDERS = TAG + ".attachedFoldersDialog";

    // saved state keys
    private static final String STATE_DEVICE_COUNT = "WorkFragment:STATE_DEVICE_COUNT";
    private static final String STATE_SCANNED_IMAGE_PATH = "WorkFragment:STATE_SCANNED_IMAGE_PATH";

    // UI
    private WorkOrderHeaderView _headerView;
    private Toolbar _toolbar;
    private Button _toolbarActionButton;
    private Button _testButton;
    private NestedScrollView _scrollView;
    private ActionBarTopView _topBar;
    private FailedUploadsView _failedUploads;
    private ProblemSummaryView _problemSummaryView;
    private BuyerCustomFieldView _buyerCustomFieldView;
    private WorkSummaryView _sumView;
    private ScheduleSummaryView _scheduleView;
    private LocationView _locView;
    private RequestNewPayView _requestNewPayView;
    private RefreshView _refreshView;
    private List<WorkOrderRenderer> _renderers = new LinkedList<>();
    private WodBottomSheetView _bottomsheetView;
    private MessagesMenuButton _messagesMenuButton;
    private MoreMenuButton _moreMenuButton;
    private PopupMenu _morePopup;
    private CounterOfferSummaryView _counterOfferSummaryView;

    private AdditionalInfoSectionView _additionalInfoSectionView;
    private PaymentSectionView _paymentSectionView;
    private TaskSectionView _taskSectionView;
    private BuyerInfoSectionView _buyerInfoSectionView;

    // Data
    private WorkOrder _workOrder;
    private int _deviceCount = -1;
    private String _scannedImagePath;
    private Location _currentLocation;
    private boolean _locationFailed = false;
    private int _workOrderId;
    private SimpleGps _simpleGps;
    private String _myUUID;

    /*-*************************************-*/
    /*-				LifeCycle				-*/
    /*-*************************************-*/
    public WorkOrderScreen(Context context) {
        super(context);
        init();
    }

    public WorkOrderScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WorkOrderScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.screen_work_order_details, this, true);

        if (isInEditMode()) return;

        _renderers.clear();

        _toolbar = findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);

        _testButton = findViewById(R.id.test_button);
        _testButton.setOnClickListener(_test_onClick);

        _headerView = findViewById(R.id.header_view);
        _renderers.add(_headerView);

        _topBar = findViewById(R.id.actiontop_view);
        _topBar.setListener(_actionbartop_listener);
        _renderers.add(_topBar);

        _sumView = findViewById(R.id.summary_view);
        _renderers.add(_sumView);

        _failedUploads = findViewById(R.id.failedUploads_view);
        _renderers.add(_failedUploads);

        _problemSummaryView = findViewById(R.id.problemsummary_view);
        _renderers.add(_problemSummaryView);

        _buyerCustomFieldView = findViewById(R.id.buyerCustomField_view);
        _renderers.add(_buyerCustomFieldView);

        _buyerInfoSectionView = findViewById(R.id.buyerInfoSectionView);
        _renderers.add(_buyerInfoSectionView);

        _counterOfferSummaryView = findViewById(R.id.counterOfferSummary_view);
        _renderers.add(_counterOfferSummaryView);

        _locView = findViewById(R.id.location_view);
        _renderers.add(_locView);

        _scheduleView = findViewById(R.id.schedule_view);
        _renderers.add(_scheduleView);

        _requestNewPayView = findViewById(R.id.requestNewPay_view);
        _renderers.add(_requestNewPayView);

        _paymentSectionView = findViewById(R.id.paymentSectionView);
        _renderers.add(_paymentSectionView);

        _additionalInfoSectionView = findViewById(R.id.additionalInfoSeciontView);
        _renderers.add(_additionalInfoSectionView);

        _refreshView = findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _scrollView = findViewById(R.id.scroll_view);
        _scrollView.setOnOverScrollListener(_refreshView);

        _taskSectionView = findViewById(R.id.taskSectionView);
        _renderers.add(_taskSectionView);

        _bottomsheetView = findViewById(R.id.bottomsheet_view);
        _bottomsheetView.setListener(_bottomsheetView_listener);
        _renderers.add(_bottomsheetView);

        if (!BuildConfig.DEBUG || BuildConfig.FLAVOR.contains("ncns"))
            _testButton.setVisibility(View.GONE);


        post(new Runnable() {
            @Override
            public void run() {
                _refreshView.startRefreshing();
            }
        });

        populateUi();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle outState = new Bundle();
        outState.putParcelable("super", super.onSaveInstanceState());

        outState.putInt("workOrderId", _workOrderId);
        if (_deviceCount > -1)
            outState.putInt(STATE_DEVICE_COUNT, _deviceCount);

        if (_scannedImagePath != null)
            outState.putString(STATE_SCANNED_IMAGE_PATH, _scannedImagePath);

        return outState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle savedInstanceState = (Bundle) state;

        super.onRestoreInstanceState(savedInstanceState.getParcelable("super"));

        if (savedInstanceState != null) {
            _workOrderId = savedInstanceState.getInt("workOrderId");
            if (savedInstanceState.containsKey(STATE_DEVICE_COUNT)) {
                _deviceCount = savedInstanceState.getInt(STATE_DEVICE_COUNT);
            }
            if (savedInstanceState.containsKey(STATE_SCANNED_IMAGE_PATH)) {
                _scannedImagePath = savedInstanceState.getString(STATE_SCANNED_IMAGE_PATH);
            }
        }
    }

    public void onStart() {
        Log.v(TAG, "onStart");

        _toolbar.getMenu().clear();
        _toolbar.inflateMenu(R.menu.wod);
        _toolbar.setTitle("WO LOADING...");
        _toolbar.setNavigationOnClickListener(_toolbarNavigation_listener);

        _messagesMenuButton = _toolbar.findViewById(R.id.messages_menu);
        _messagesMenuButton.setOnClickListener(_messagesMenuButton_onClick);

        _moreMenuButton = _toolbar.findViewById(R.id.more_menu);
        _moreMenuButton.setOnClickListener(_moreMenuButton_onClick);

        _morePopup = new PopupMenu(getContext(), _moreMenuButton);
        _morePopup.setOnMenuItemClickListener(_morePopup_onClick);

        App.get().getSpUiContext().page(WorkOrderTracker.Tab.DETAILS.name());
        _workOrderApi.sub();

        ReportProblemDialog.addOnSendListener(DIALOG_REPORT_PROBLEM, _reportProblemDialog_onSend);
        WithdrawRequestDialog.addOnWithdrawListener(DIALOG_WITHDRAW, _withdrawRequestDialog_onWithdraw);
        MarkCompleteDialog.addOnContinueClickListener(DIALOG_MARK_COMPLETE, _markCompleteDialog_onContinue);
        MarkCompleteDialog.addOnSignatureClickListener(DIALOG_MARK_COMPLETE, _markCompleteDialog_onSignature);
        WorkLogDialog.addOnOkListener(DIALOG_WORKLOG, _worklogDialog_listener);
        PayDialog.addOnCompleteListener(DIALOG_PAY, _payDialog_onComplete);
        HoldReviewDialog.addOnAcknowledgeListener(DIALOG_HOLD_REVIEW, _holdReviewDialog_onAcknowledge);
        HoldReviewDialog.addOnCancelListener(DIALOG_HOLD_REVIEW, _holdReviewDialog_onCancel);
        TwoButtonDialog.addOnPrimaryListener(DIALOG_DELETE_WORKLOG, _twoButtonDialog_deleteWorkLog);
        TwoButtonDialog.addOnPrimaryListener(DIALOG_DELETE_SIGNATURE, _twoButtonDialog_deleteSignature);

        new SimpleGps(App.get()).updateListener(_simpleGps_listener).numUpdates(1).start(App.get());

        _simpleGps = new SimpleGps(App.get())
                .updateListener(_simpleGps_listener)
                .numUpdates(1)
                .start(App.get());
    }

    public void onResume() {
        _documentClient.sub();
    }

    public void onPause() {
        _documentClient.unsub();
    }

    public void onStop() {
        Log.v(TAG, "onStop");
        ReportProblemDialog.removeOnSendListener(DIALOG_REPORT_PROBLEM, _reportProblemDialog_onSend);
        WithdrawRequestDialog.removeOnWithdrawListener(DIALOG_WITHDRAW, _withdrawRequestDialog_onWithdraw);
        MarkCompleteDialog.removeOnContinueClickListener(DIALOG_MARK_COMPLETE, _markCompleteDialog_onContinue);
        MarkCompleteDialog.removeOnSignatureClickListener(DIALOG_MARK_COMPLETE, _markCompleteDialog_onSignature);
        WorkLogDialog.removeOnOkListener(DIALOG_WORKLOG, _worklogDialog_listener);
        PayDialog.removeOnCompleteListener(DIALOG_PAY, _payDialog_onComplete);
        HoldReviewDialog.removeOnAcknowledgeListener(DIALOG_HOLD_REVIEW, _holdReviewDialog_onAcknowledge);
        HoldReviewDialog.removeOnCancelListener(DIALOG_HOLD_REVIEW, _holdReviewDialog_onCancel);
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_DELETE_WORKLOG, _twoButtonDialog_deleteWorkLog);
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_DELETE_SIGNATURE, _twoButtonDialog_deleteSignature);

        _workOrderApi.unsub();
        if (_simpleGps != null && _simpleGps.isRunning()) _simpleGps.stop();
        if (_activityResultListener != null) _activityResultListener.unsub();
    }


    public boolean onBackPressed() {
        return _bottomsheetView.onBackPressed();
    }

    public void setWorkOrder(WorkOrder workOrder) {
        //Log.v(TAG, "setWorkOrder");
        _workOrder = workOrder;
        _workOrderId = workOrder.getId();
        populateUi();
    }

    public void setUUID(String uuid) {
        _myUUID = uuid;

        for (WorkOrderRenderer workOrderRenderer : _renderers) {
            if (workOrderRenderer instanceof UUIDView) {
                ((UUIDView) workOrderRenderer).setUUID(_myUUID);
            }
        }
    }

    private void populateUi() {
        //misc.hideKeyboard(this);

        if (_workOrder == null)
            return;

        if (_morePopup == null)
            return;

        _toolbar.setTitle("WO " + _workOrderId);

        _activityResultListener.sub();

        setLoading(true);

        for (WorkOrderRenderer renderer : _renderers) {
            renderer.setWorkOrder(_workOrder);
        }

        setLoading(false);

        Menu menu = _morePopup.getMenu();
        menu.clear();

        if (_workOrder.getEta().getActionsSet().contains(ETA.ActionsEnum.RUNNING_LATE)) {
            menu.add(0, 0, 300, "Running Late");
        }
        if (_workOrder.getProblems().getActionsSet().contains(Problems.ActionsEnum.ADD)) {
            menu.add(0, 1, 300, "Report A Problem");
        }
        if (!(_workOrder.getBundle().getId() > 0) && (_workOrder.getRoutes().getUserRoute().getActionsSet().contains(Route.ActionsEnum.ACCEPT)
                || _workOrder.getRequests().getActionsSet().contains(Requests.ActionsEnum.ADD))) {
            menu.add(0, 2, 300, "Not Interested");
        }
        if (_workOrder.getActionsSet().contains(WorkOrder.ActionsEnum.PRINT)) {
            menu.add(0, 3, 300, "Print");
        }
        if (_workOrder.getRatings().getBuyer().getActionsSet().contains(WorkOrderRatingsBuyer.ActionsEnum.ADD)) {
            menu.add(0, 4, 300, "Rate Buyer");
        }

        menu.add(0, 5, 300, "Send Debug Log");

        if (menu.size() == 0) {
            _moreMenuButton.setVisibility(GONE);
        } else {
            _moreMenuButton.setVisibility(VISIBLE);
        }
    }

    private void requestWorkorder() {
        if (_workOrder == null)
            return;

        Log.v(TAG, "getData.startRefreshing");
        setLoading(true);
        WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, false, false);
    }

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
    private void doCheckin() {
        App.get().analActionTitle = null;
        CheckInOutDialog.show(App.get(), null, _myUUID, _workOrderId,
                _workOrder.getTimeLogs(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_IN);
    }

    /*-*********************************************-*/
    /*-				Check Out Process				-*/
    /*-*********************************************-*/
    private void doCheckOut() {
//        setLoading(true);

        Pay pay = _workOrder.getPay();
        if (pay.getType() == Pay.TypeEnum.DEVICE
                && pay.getBase().getUnits() != null) {
            _deviceCount = pay.getBase().getUnits().intValue();
        } else {
            _deviceCount = -1;
        }

        App.get().analActionTitle = null;

        if (_deviceCount > -1) {
            CheckInOutDialog.show(App.get(), null, _myUUID, _workOrderId,
                    _workOrder.getTimeLogs(), _deviceCount, CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
        } else {
            CheckInOutDialog.show(App.get(), null, _myUUID, _workOrderId,
                    _workOrder.getTimeLogs(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnClickListener _toolbarNavigation_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppMessagingClient.finishActivity();
        }
    };

    private final SimpleGps.Listener _simpleGps_listener = new SimpleGps.Listener() {
        @Override
        public void onLocation(SimpleGps simpleGps, Location location) {
            _currentLocation = location;
            _locationFailed = false;
            simpleGps.stop();
        }

        @Override
        public void onFail(SimpleGps simpleGps) {
            _locationFailed = true;
            simpleGps.stop();
        }

        @Override
        public void onPermissionDenied(SimpleGps simpleGps) {
            _locationFailed = true;
            simpleGps.stop();
        }
    };

    private final ActivityResultListener _activityResultListener = new ActivityResultListener() {
        @Override
        public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
            Log.v(TAG, "WorkFragment#onActivityResult");
            try {
                Log.v(TAG, "onActivityResult() resultCode= " + resultCode);
                Log.v(TAG, "onActivityResult() requestCode= " + requestCode);

                if (requestCode == ActivityResultConstants.RESULT_CODE_GET_SIGNATURE && resultCode == Activity.RESULT_OK) {
                    Log.v(TAG, "signature response");
                    requestWorkorder();

                    if (App.getProfile().canRequestWorkOnMarketplace() && !_workOrder.getW2()) {
                        RateBuyerYesNoDialog.show(App.get(), DIALOG_RATE_BUYER_YESNO, _workOrderId, _workOrder.getCompany(), _workOrder.getLocation());
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return true;
        }
    };

    /*-*********************************************-*/
    /*-				Main View Listeners				-*/
    /*-*********************************************-*/
    private final View.OnClickListener _test_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CounterOfferDialog.show(App.get(), _workOrderId, false);
        }
    };

    private final View.OnClickListener _messagesMenuButton_onClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            ChatDialog.show(App.get(), _workOrderId);
        }
    };

    private final View.OnClickListener _moreMenuButton_onClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            _morePopup.show();
        }
    };

    private final PopupMenu.OnMenuItemClickListener _morePopup_onClick = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case 0: // running late
                    WorkOrderTracker.onActionButtonEvent(
                            App.get(), WorkOrderTracker.ActionButton.RUNNING_LATE, null, _workOrder.getId());

                    RunningLateDialog.show(App.get(), DIALOG_RUNNING_LATE, _workOrder.getId(),
                            _workOrder.getEta(), _workOrder.getSchedule(), _workOrder.getContacts(),
                            _workOrder.getTitle());
                    break;
                case 1: // report a problem
                    WorkOrderTracker.onActionButtonEvent(
                            App.get(), WorkOrderTracker.ActionButton.REPORT_PROBLEM, null, _workOrderId);

                    ReportProblemDialog.show(App.get(), DIALOG_REPORT_PROBLEM, _workOrderId, _workOrder.getProblems());
                    break;
                case 2: // not interested
                    WorkOrderTracker.onActionButtonEvent(
                            App.get(), WorkOrderTracker.ActionButton.NOT_INTERESTED, null, _workOrderId);

                    if (_workOrder.getBundle().getId() != null && _workOrder.getBundle().getId() > 0) {
                        DeclineDialog.show(
                                App.get(), null, _workOrder.getBundle().getMetadata().getTotal(),
                                _workOrderId, _workOrder.getCompany().getId());
                    } else {
                        DeclineDialog.show(App.get(), null, _workOrderId, _workOrder.getCompany().getId(), true);
                    }
                    break;
                case 3: { // print
                    String url = "https://" + getContext().getString(R.string.web_fn_hostname) + "/marketplace/wo_print.php?workorder_id=" + _workOrderId;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    ActivityClient.startActivity(intent);
                    break;
                }
                case 4: // rate buyer
                    RateBuyerDialog.show(App.get(), null, _workOrderId, _workOrder.getCompany(), _workOrder.getLocation());
                    break;
                case 5: { // send debug log
                    File tempfile = DebugUtils.dumpLogcat(getContext(), (BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_FLAVOR_NAME).trim());
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"apps@fieldnation.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Android Log " + (BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_FLAVOR_NAME).trim());
                    intent.putExtra(Intent.EXTRA_TEXT, "Tell me about the problem you are having.");
                    intent.putExtra(Intent.EXTRA_STREAM, App.getUriFromFile(tempfile));
                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                        getContext().startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), R.string.no_email_app_is_available, Toast.LENGTH_LONG).show();
                    }
                    break;
                }
            }

            return true;
        }
    };

    private final ActionBarTopView.Listener _actionbartop_listener = new ActionBarTopView.Listener() {
        @Override
        public void onCheckOut() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CHECK_OUT,
                    null,
                    _workOrderId,
                    new EventContext[]{
                            new SpTracingContext(new UUIDGroup(null, _myUUID)),
                            new SpWorkOrderContext.Builder().workOrderId(_workOrderId).build(),
                            new SpStackContext(DebugUtils.getStackTraceElement()),
                            new SpStatusContext(SpStatusContext.Status.INFO, "Work Order Screen")
                    }
            );
            doCheckOut();
        }

        @Override
        public void onCheckIn() {
            WorkOrderTracker.onActionButtonEvent(
                    App.get(),
                    WorkOrderTracker.ActionButton.CHECK_IN,
                    null,
                    _workOrderId,
                    new EventContext[]{
                            new SpTracingContext(new UUIDGroup(null, _myUUID)),
                            new SpWorkOrderContext.Builder().workOrderId(_workOrderId).build(),
                            new SpStackContext(DebugUtils.getStackTraceElement()),
                            new SpStatusContext(SpStatusContext.Status.INFO, "Work Order Screen")
                    }
            );
            doCheckin();
        }

        @Override
        public void onCheckInAgain() {
            WorkOrderTracker.onActionButtonEvent(
                    App.get(),
                    WorkOrderTracker.ActionButton.CHECK_IN_AGAIN,
                    null,
                    _workOrderId,
                    new EventContext[]{
                            new SpTracingContext(new UUIDGroup(null, _myUUID)),
                            new SpWorkOrderContext.Builder().workOrderId(_workOrderId).build(),
                            new SpStackContext(DebugUtils.getStackTraceElement()),
                            new SpStatusContext(SpStatusContext.Status.INFO, "Work Order Screen")
                    }
            );
            doCheckin();
        }

        @Override
        public void onAcknowledgeHold() {
            setLoading(true);
            HoldReviewDialog.show(App.get(), DIALOG_HOLD_REVIEW, _workOrderId, _workOrder.getHolds());
        }

        @Override
        public void onMarkIncomplete() {
            WorkOrderTracker.onActionButtonEvent(
                    App.get(), WorkOrderTracker.ActionButton.MARK_INCOMPLETE, null, _workOrderId);
            App.get().analActionTitle = null;
            MarkIncompleteWarningDialog.show(App.get(), null, _workOrderId);
        }

        @Override
        public void onViewPayment() {
            WorkOrderTracker.onActionButtonEvent(
                    App.get(), WorkOrderTracker.ActionButton.VIEW_PAYMENT, null, _workOrderId);
            PaymentListActivity.startNew(App.get());
        }

        @Override
        public void onReportProblem() {
            WorkOrderTracker.onActionButtonEvent(
                    App.get(), WorkOrderTracker.ActionButton.REPORT_PROBLEM, null, _workOrderId);

            ReportProblemDialog.show(App.get(), DIALOG_REPORT_PROBLEM, _workOrderId, _workOrder.getProblems());
        }

        @Override
        public void onMyWay() {
            if (!App.get().isLocationEnabled()) {
                ToastClient.snackbar(
                        App.get(),
                        getResources().getString(R.string.snackbar_location_disabled),
                        "LOCATION SETTINGS",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                ActivityClient.startActivityForResult(intent, ActivityResultConstants.RESULT_CODE_ENABLE_GPS);
                            }
                        },
                        Snackbar.LENGTH_INDEFINITE);
            }

            WorkOrderTracker.onActionButtonEvent(
                    App.get(), WorkOrderTracker.ActionButton.ON_MY_WAY, WorkOrderTracker.Action.ON_MY_WAY, _workOrderId);
            try {
                ETAStatus etaStatus = new ETAStatus().name(ETAStatus.NameEnum.ONMYWAY);

                ETA eta = new ETA();
                eta.status(etaStatus);

                if (_currentLocation != null)
                    eta.condition(new Condition()
                            .coords(new Coords(_currentLocation.getLatitude(), _currentLocation.getLongitude())));

                WorkordersWebApi.updateETA(App.get(), _workOrderId, eta, App.get().getSpUiContext());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            try {
                GpsTrackingService.start(App.get(), System.currentTimeMillis() + 7200000); // 2 hours
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            setLoading(true);
        }

        @Override
        public void onEta() {
            App.get().analActionTitle = null;
            EtaDialog.show(App.get(), null, _workOrderId, _workOrder.getSchedule(),
                    _workOrder.getEta(), EtaDialog.PARAM_DIALOG_TYPE_ADD);
        }

        @Override
        public void onNotInterested() {
            WorkOrderTracker.onActionButtonEvent(
                    App.get(), WorkOrderTracker.ActionButton.NOT_INTERESTED, null, _workOrderId);

            if (_workOrder.getBundle().getId() != null && _workOrder.getBundle().getId() > 0) {
                DeclineDialog.show(
                        App.get(), null, _workOrder.getBundle().getMetadata().getTotal(),
                        _workOrderId, _workOrder.getCompany().getId());
            } else {
                DeclineDialog.show(App.get(), null, _workOrderId, _workOrder.getCompany().getId(), true);
            }
        }

        @Override
        public void onRequest() {
            WorkOrderTracker.onActionButtonEvent(
                    App.get(), WorkOrderTracker.ActionButton.REQUEST, null, _workOrderId);

            if (_workOrder.getBundle().getId() != null && _workOrder.getBundle().getId() > 0) {
                // Todo track bundles... although we don't allow this anymore
                RequestBundleDialog.show(
                        App.get(), DIALOG_CANCEL_WARNING, _workOrder.getBundle().getId(),
                        _workOrder.getBundle().getMetadata().getTotal(), _workOrderId, RequestBundleDialog.TYPE_REQUEST);
            } else {
                App.get().analActionTitle = null;
                EtaDialog.show(App.get(), null, _workOrderId, _workOrder.getSchedule(),
                        _workOrder.getEta(), EtaDialog.PARAM_DIALOG_TYPE_REQUEST);
            }
        }

        @Override
        public void onAccept() {
            WorkOrderTracker.onActionButtonEvent(
                    App.get(), WorkOrderTracker.ActionButton.CONFIRM, null, _workOrderId);

            if (_workOrder.getBundle().getId() != null && _workOrder.getBundle().getId() > 0) {
                // Todo track bundles... although we don't allow this anymore
                RequestBundleDialog.show(
                        App.get(), DIALOG_CANCEL_WARNING, _workOrder.getBundle().getId(),
                        _workOrder.getBundle().getMetadata().getTotal(), _workOrderId, RequestBundleDialog.TYPE_ACCEPT);
            } else {
                App.get().analActionTitle = null;
                EtaDialog.show(App.get(), null, _workOrderId, _workOrder.getSchedule(),
                        _workOrder.getEta(), EtaDialog.PARAM_DIALOG_TYPE_ACCEPT);
            }
        }

        @Override
        public void onWithdraw() {
            WorkOrderTracker.onActionButtonEvent(
                    App.get(), WorkOrderTracker.ActionButton.WITHDRAW, null, _workOrderId);

            WithdrawRequestDialog.show(
                    App.get(), DIALOG_WITHDRAW, _workOrderId, 0, _workOrder.getRequests().getOpenRequest().getId());
        }

        @Override
        public void onReadyToGo() {
            WorkOrderTracker.onActionButtonEvent(
                    App.get(), WorkOrderTracker.ActionButton.READY_TO_GO, WorkOrderTracker.Action.READY_TO_GO, _workOrderId);

            try {
                ETA eta = new ETA()
                        .status(new ETAStatus()
                                .name(ETAStatus.NameEnum.READYTOGO));

                WorkordersWebApi.updateETA(App.get(), _workOrderId, eta, App.get().getSpUiContext());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            setLoading(true);
        }

        @Override
        public void onConfirm() {
            WorkOrderTracker.onActionButtonEvent(
                    App.get(), WorkOrderTracker.ActionButton.CONFIRM, null, _workOrderId);

            try {
                ETA eta = new ETA()
                        .status(new ETAStatus()
                                .name(ETAStatus.NameEnum.CONFIRMED));
                WorkordersWebApi.updateETA(App.get(), _workOrderId, eta, App.get().getSpUiContext());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            setLoading(true);
        }

        @Override
        public void onEnterClosingNotes() {
            WorkOrderTracker.onActionButtonEvent(
                    App.get(), WorkOrderTracker.ActionButton.CLOSING_NOTES, null, _workOrderId);

            if (_workOrder.getActionsSet().contains(WorkOrder.ActionsEnum.CLOSING_NOTES))
                ClosingNotesDialog.show(App.get(), _workOrderId, _workOrder.getClosingNotes());
        }

        @Override
        public void onMarkComplete() {
            WorkOrderTracker.onActionButtonEvent(
                    App.get(), WorkOrderTracker.ActionButton.MARK_COMPlETE, null, _workOrderId);

            MarkCompleteDialog.show(App.get(), DIALOG_MARK_COMPLETE, _workOrder.getSignatures().getResults().length > 0);
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _twoButtonDialog_deleteWorkLog = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED);
            WorkordersWebApi.deleteTimeLog(App.get(), _workOrderId, ((TimeLog) extraData).getId(), App.get().getSpUiContext());
            setLoading(true);
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _twoButtonDialog_deleteSignature = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SIGNATURES);
            WorkordersWebApi.deleteSignature(App.get(), _workOrderId, ((Signature) extraData).getId(), App.get().getSpUiContext());
        }
    };

    /*-*********************************-*/
    /*-				Dialogs				-*/
    /*-*********************************-*/
    private final MarkCompleteDialog.OnSignatureClickListener _markCompleteDialog_onSignature = new MarkCompleteDialog.OnSignatureClickListener() {
        @Override
        public void onSignatureClick() {
            new AsyncTaskEx<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... params) {
                    try {
                        Log.v(TAG, "onSignatureClick ");
                        WorkOrder workOrder = (WorkOrder) params[0];

                        SignOffActivity.startSignOff(App.get(), workOrder.getId(), true);
                        return null;
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                        ToastClient.toast(App.get(), "Could not start signature collection. Please try again.", Toast.LENGTH_LONG);
                    }
                    return null;
                }
            }.executeEx(_workOrder);
        }
    };

    private final MarkCompleteDialog.OnContinueClickListener _markCompleteDialog_onContinue = new MarkCompleteDialog.OnContinueClickListener() {
        @Override
        public void onContinueClick() {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.MARK_COMPlETE, WorkOrderTracker.Action.MARK_COMPLETE, _workOrderId);

            SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
            uiContext.page += " - Mark Complete Dialog";
            WorkordersWebApi.completeWorkOrder(App.get(), _workOrderId, uiContext);
            setLoading(true);

            if (App.getProfile().canRequestWorkOnMarketplace() && !_workOrder.getW2()) {
                RateBuyerYesNoDialog.show(App.get(), DIALOG_RATE_YESNO, _workOrderId, _workOrder.getCompany(), _workOrder.getLocation());
            }
        }
    };

    private final PayDialog.OnCompleteListener _payDialog_onComplete = new PayDialog.OnCompleteListener() {
        @Override
        public void onComplete(Pay pay, String explanation) {
            try {
                SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
                uiContext.page += " - Pay Dialog";

                AppMessagingClient.setLoading(true);

                WorkordersWebApi.addIncrease(App.get(), _workOrderId,
                        new PayIncrease().pay(pay).description(explanation), uiContext);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final HoldReviewDialog.OnAcknowledgeListener _holdReviewDialog_onAcknowledge = new HoldReviewDialog.OnAcknowledgeListener() {
        @Override
        public void onAcknowledge(int workOrderId) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.ACKNOWLEDGE_HOLD,
                    WorkOrderTracker.Action.ACKNOWLEDGE_HOLD, _workOrderId);

        }
    };

    private final HoldReviewDialog.OnCancelListener _holdReviewDialog_onCancel = new HoldReviewDialog.OnCancelListener() {
        @Override
        public void onCancel() {
            setLoading(false);
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
        public void onSend(int workorderId, String explanation, ProblemType type) {
            if (_workOrderId == workorderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.REPORT_PROBLEM, WorkOrderTracker.Action.REPORT_PROBLEM, workorderId);
        }
    };

    private final WithdrawRequestDialog.OnWithdrawListener _withdrawRequestDialog_onWithdraw = new WithdrawRequestDialog.OnWithdrawListener() {
        @Override
        public void onWithdraw(int workOrderId) {
            if (_workOrderId == workOrderId)
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
                WorkOrderTracker.onAddEvent(
                        App.get(),
                        WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED,
                        new EventContext[]{
                                new SpTracingContext(new UUIDGroup(null, _myUUID)),
                                new SpWorkOrderContext.Builder().workOrderId(_workOrderId).build(),
                                new SpStackContext(DebugUtils.getStackTraceElement()),
                                new SpStatusContext(SpStatusContext.Status.INFO, "Work Order Screen")
                        }
                );
                WorkordersWebApi.addTimeLog(App.get(), new UUIDGroup(_myUUID, UUID.randomUUID().toString()),
                        _workOrderId, newTimeLog, App.get().getSpUiContext());

            } else {
                WorkOrderTracker.onEditEvent(
                        App.get(),
                        WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED,
                        new EventContext[]{
                                new SpTracingContext(new UUIDGroup(null, _myUUID)),
                                new SpWorkOrderContext.Builder().workOrderId(_workOrderId).build(),
                                new SpStackContext(DebugUtils.getStackTraceElement()),
                                new SpStatusContext(SpStatusContext.Status.INFO, "Work Order Screen")
                        }
                );
                WorkordersWebApi.updateTimeLog(App.get(), new UUIDGroup(_myUUID, UUID.randomUUID().toString()), _workOrderId, timeLog.getId(), newTimeLog, App.get().getSpUiContext());
            }
            setLoading(true);
        }
    };

    private final WodBottomSheetView.Listener _bottomsheetView_listener = new WodBottomSheetView.Listener() {
        @Override
        public void addCounterOffer() {
            CounterOfferDialog.show(App.get(), _workOrderId, false);
        }

        @Override
        public void addRequestNewPay() {
            Log.v(TAG, "Inside _paymentView_listener.onRequestNewPay()");
            if (_workOrder.getPay().getIncreases().getLastIncrease() != null) {
                PayDialog.show(App.get(), DIALOG_PAY, R.string.request_new_pay, R.string.btn_submit, _workOrder.getPay().getIncreases().getLastIncrease().getPay(), true);
            } else {
                PayDialog.show(App.get(), DIALOG_PAY, R.string.request_new_pay, R.string.btn_submit, _workOrder.getPay(), true);
            }
        }

        @Override
        public void addTimeLog() {
            WorkOrderTracker.onAddEvent(
                    App.get(),
                    WorkOrderTracker.WorkOrderDetailsSection.TIME_LOGGED,
                    new EventContext[]{
                            new SpTracingContext(new UUIDGroup(null, _myUUID)),
                            new SpWorkOrderContext.Builder().workOrderId(_workOrderId).build(),
                            new SpStackContext(DebugUtils.getStackTraceElement()),
                            new SpStatusContext(SpStatusContext.Status.INFO, "Work Order Screen")
                    }
            );
            if (_workOrder.getPay() != null && _workOrder.getPay().getType() != null)
                WorkLogDialog.show(App.get(), DIALOG_WORKLOG, _myUUID, null, _workOrder.getPay().getType() == Pay.TypeEnum.DEVICE);
            else
                WorkLogDialog.show(App.get(), DIALOG_WORKLOG, _myUUID, null, false);
        }

        @Override
        public void addExpense() {
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.EXPENSES);
            ExpenseDialog.show(App.get(), null, _workOrderId, false, true);
        }

        @Override
        public void addDiscount() {
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.DISCOUNTS);
            DiscountDialog.show(App.get(), null, _workOrderId, getContext().getString(R.string.dialog_add_discount_title));
        }

        @Override
        public void addSignature() {
            SignOffActivity.startSignOff(App.get(), _workOrderId);
            setLoading(true);
        }

        @Override
        public void addShipment() {
            ShipmentAddDialog.show(App.get(), null, _workOrderId,
                    _workOrder.getAttachments(), App.get().getString(R.string.dialog_shipment_title), null, null);
        }

        @Override
        public void addAttachment() {
            UUIDGroup uuid = new UUIDGroup(null, _myUUID);
            Tracker.event(App.get(), new CustomEvent.Builder()
                    .addContext(new SpTracingContext(uuid))
                    .addContext(new SpWorkOrderContext.Builder().workOrderId(_workOrderId).build())
                    .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                    .addContext(new SpStatusContext(SpStatusContext.Status.INFO, "WoD BottomSheet Add Attachment"))
                    .build());

            AttachedFoldersDialog.show(App.get(), DIALOG_ATTACHED_FOLDERS, _myUUID, _workOrderId);
        }
    };

    /*-*****************************-*/
    /*-				Web				-*/
    /*-*****************************-*/
    private final DocumentClient _documentClient = new DocumentClient() {
        @Override
        public boolean processDownload(long documentId) {
            return true;
        }

        @Override
        public void onDownload(long documentId, File file, int state, boolean isSync) {
            Log.v(TAG, "DocumentClient.onDownload");
            if (file == null || state == DocumentConstants.PARAM_STATE_START) {
                if (state == DocumentConstants.PARAM_STATE_FINISH)
                    ToastClient.toast(App.get(), R.string.could_not_download_file, Toast.LENGTH_SHORT);
                return;
            }

            if (isSync)
                return;

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(App.getUriFromFile(file),
                        FileUtils.guessContentTypeFromName(file.getName()));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (intent.resolveActivity(App.get().getPackageManager()) != null) {
                    ActivityClient.startActivity(intent);
                } else {
                    String name = file.getName();
                    name = name.substring(name.indexOf("_") + 1);

                    final Intent folderIntent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(App.getUriFromFile(new File(App.get().getDownloadsFolder())), "resource/folder");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    if (folderIntent.resolveActivity(App.get().getPackageManager()) != null) {
                        ToastClient.snackbar(App.get(), "Can not open " + name + ", placed in downloads folder", "View", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityClient.startActivity(folderIntent);
                            }
                        }, Snackbar.LENGTH_LONG);
                    } else {
                        ToastClient.toast(App.get(), "Can not open " + name + ", placed in downloads folder", Toast.LENGTH_LONG);
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final WorkordersWebApi _workOrderApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName) {
            if (transactionParams.getMethodParamInt("workOrderId") == null
                    || transactionParams.getMethodParamInt("workOrderId") != _workOrderId)
                return false;

            return methodName.contains("TimeLog");
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (methodName.contains("TimeLog") && !success) {
                Log.v(TAG, "onWorkordersWebApi");
                setLoading(false);
            }
            return false;
        }
    };
}