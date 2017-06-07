package com.fieldnation.v2.ui.workorder;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.BuildConfig;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.GpsTrackingService;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.activityresult.ActivityResultConstants;
import com.fieldnation.service.data.gmaps.Position;
import com.fieldnation.ui.IconFontButton;
import com.fieldnation.ui.ncns.ConfirmActivity;
import com.fieldnation.ui.payment.PaymentListActivity;
import com.fieldnation.ui.workorder.BundleDetailActivity;
import com.fieldnation.ui.workorder.WorkOrderActivity;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Acknowledgment;
import com.fieldnation.v2.data.model.Bundle;
import com.fieldnation.v2.data.model.Condition;
import com.fieldnation.v2.data.model.Contact;
import com.fieldnation.v2.data.model.Coords;
import com.fieldnation.v2.data.model.ETA;
import com.fieldnation.v2.data.model.ETAStatus;
import com.fieldnation.v2.data.model.Hold;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.Problem;
import com.fieldnation.v2.data.model.ProblemType;
import com.fieldnation.v2.data.model.Problems;
import com.fieldnation.v2.data.model.Request;
import com.fieldnation.v2.data.model.Requests;
import com.fieldnation.v2.data.model.Route;
import com.fieldnation.v2.data.model.ScheduleServiceWindow;
import com.fieldnation.v2.data.model.TimeLog;
import com.fieldnation.v2.data.model.TimeLogs;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.CheckInOutDialog;
import com.fieldnation.v2.ui.dialog.DeclineDialog;
import com.fieldnation.v2.ui.dialog.EtaDialog;
import com.fieldnation.v2.ui.dialog.HoldReviewDialog;
import com.fieldnation.v2.ui.dialog.MarkIncompleteWarningDialog;
import com.fieldnation.v2.ui.dialog.ReportProblemDialog;
import com.fieldnation.v2.ui.dialog.RunningLateDialog;
import com.fieldnation.v2.ui.dialog.WithdrawRequestDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Michael on 7/26/2016.
 */
public class WorkOrderCard extends RelativeLayout {
    private static final String TAG = "WorkOrderCard";

    // Dialog UIDs
    private static final String DIALOG_REPORT_PROBLEM = TAG + ".reportProblemDialog";
    private static final String DIALOG_MARK_INCOMPLETE_WARNING = TAG + ".markIncompleteWarningDialog";
    private static final String DIALOG_CHECK_IN_OUT = TAG + ".checkInOutDialog";
    private static final String DIALOG_ETA = TAG + ".etaDialog";
    private static final String DIALOG_DECLINE = TAG + ".declineDialog";
    private static final String DIALOG_WITHDRAW_REQUEST = TAG + ".withdrawRequestDialog";
    private static final String DIALOG_RUNNING_LATE = TAG + ".runningLateDialog";
    private static final String DIALOG_MARK_INCOMPLETE = TAG + ".markIncompleteWarningDialog";
    private static final String DIALOG_HOLD_REVIEW = TAG + ".holdReviewDialog";

    // Ui
    private View _warningBarView;
    private TextView _amountTextView;
    private TextView _payTypeTextView;
    private TextView _workTypeTextView;
    private TextView _titleTextView;
    private TextView _dateTextView;
    private TextView _timeTextView;
    private TextView _hyphenTextView;
    private TextView _date2TextView;
    private TextView _time2TextView;
    private TextView _locationTextView;
    private TextView _distanceTextView;
    private IconFontButton[] _secondaryButtons = new IconFontButton[4];
    private Button _primaryButton;
    private Button _testButton;

    // Data
    private WorkOrder _workOrder;
    private Location _location;
    private String _savedSearchTitle;
    private OnActionListener _onActionListener;

    public WorkOrderCard(Context context) {
        super(context);
        init();
    }

    public WorkOrderCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WorkOrderCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_work_order_card, this);

        if (isInEditMode())
            return;

        _warningBarView = findViewById(R.id.warninginbar_view);
        _amountTextView = (TextView) findViewById(R.id.amount_textview);
        _payTypeTextView = (TextView) findViewById(R.id.paytype_textview);
        _workTypeTextView = (TextView) findViewById(R.id.worktype_textview);
        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _dateTextView = (TextView) findViewById(R.id.date_textview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _hyphenTextView = (TextView) findViewById(R.id.hyphen_textview);
        _time2TextView = (TextView) findViewById(R.id.time2_textview);
        _date2TextView = (TextView) findViewById(R.id.date2_textview);
        _locationTextView = (TextView) findViewById(R.id.location_textview);
        _distanceTextView = (TextView) findViewById(R.id.distance_textview);

        _secondaryButtons[0] = (IconFontButton) findViewById(R.id.secondary1_button);
        _secondaryButtons[1] = (IconFontButton) findViewById(R.id.secondary2_button);
        _secondaryButtons[2] = (IconFontButton) findViewById(R.id.secondary3_button);
        _secondaryButtons[3] = (IconFontButton) findViewById(R.id.secondary4_button);

        _primaryButton = (Button) findViewById(R.id.primary_button);

        _testButton = (Button) findViewById(R.id.test_button);
        _testButton.setOnClickListener(_test_onClick);

        // Just in case we forget to hide this button when building a release version
        if (!BuildConfig.DEBUG || BuildConfig.FLAVOR.contains("ncns"))
            _testButton.setVisibility(GONE);

        DeclineDialog.addOnDeclinedListener(DIALOG_DECLINE, _declineDialog_onDeclined);
        CheckInOutDialog.addOnCheckInListener(DIALOG_CHECK_IN_OUT, _checkInOutDialog_onCheckIn);
        CheckInOutDialog.addOnCheckOutListener(DIALOG_CHECK_IN_OUT, _checkInOutDialog_onCheckOut);
        EtaDialog.addOnRequestedListener(DIALOG_ETA, _etaDialog_onRequested);
        EtaDialog.addOnAcceptedListener(DIALOG_ETA, _etaDialog_onAccepted);
        EtaDialog.addOnEtaListener(DIALOG_ETA, _etaDialog_onEta);
        MarkIncompleteWarningDialog.addOnMarkIncompleteListener(DIALOG_MARK_INCOMPLETE_WARNING, _markIncompleteWarningDialog_onMarkIncomplete);
        ReportProblemDialog.addOnSendListener(DIALOG_REPORT_PROBLEM, _reportProblemDialog_onSend);
        RunningLateDialog.addOnSendListener(DIALOG_RUNNING_LATE, _runningLateDialog_onSend);
        WithdrawRequestDialog.addOnWithdrawListener(DIALOG_WITHDRAW_REQUEST, _withdrawRequestDialog_onWithdraw);
        HoldReviewDialog.addOnAcknowledgeListener(DIALOG_HOLD_REVIEW, _holdReviewDialog_onAcknowledge);
        HoldReviewDialog.addOnCancelListener(DIALOG_HOLD_REVIEW, _holdReviewDialog_onCancel);

        setOnClickListener(_this_onClick);
    }

    @Override
    protected void onDetachedFromWindow() {
        DeclineDialog.removeOnDeclinedListener(DIALOG_DECLINE, _declineDialog_onDeclined);
        CheckInOutDialog.removeOnCheckInListener(DIALOG_CHECK_IN_OUT, _checkInOutDialog_onCheckIn);
        CheckInOutDialog.removeOnCheckOutListener(DIALOG_CHECK_IN_OUT, _checkInOutDialog_onCheckOut);
        EtaDialog.removeOnRequestedListener(DIALOG_ETA, _etaDialog_onRequested);
        EtaDialog.removeOnAcceptedListener(DIALOG_ETA, _etaDialog_onAccepted);
        EtaDialog.removeOnEtaListener(DIALOG_ETA, _etaDialog_onEta);
        MarkIncompleteWarningDialog.removeOnMarkIncompleteListener(DIALOG_MARK_INCOMPLETE_WARNING, _markIncompleteWarningDialog_onMarkIncomplete);
        ReportProblemDialog.removeOnSendListener(DIALOG_REPORT_PROBLEM, _reportProblemDialog_onSend);
        RunningLateDialog.removeOnSendListener(DIALOG_RUNNING_LATE, _runningLateDialog_onSend);
        WithdrawRequestDialog.removeOnWithdrawListener(DIALOG_WITHDRAW_REQUEST, _withdrawRequestDialog_onWithdraw);
        HoldReviewDialog.removeOnAcknowledgeListener(DIALOG_HOLD_REVIEW, _holdReviewDialog_onAcknowledge);
        HoldReviewDialog.removeOnCancelListener(DIALOG_HOLD_REVIEW, _holdReviewDialog_onCancel);

        super.onDetachedFromWindow();
    }

    public void setData(WorkOrder workOrder, Location location, String savedSearchTitle) {
        _workOrder = workOrder;
        _location = location;
        _savedSearchTitle = savedSearchTitle;

        populateUi();
    }

    public WorkOrder getWorkOrder() {
        return _workOrder;
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_primaryButton == null)
            return;

        _titleTextView.setText(_workOrder.getId() + " | " + _workOrder.getTitle());
        if (_workOrder.getTypeOfWork() != null && !misc.isEmptyOrNull(_workOrder.getTypeOfWork().getName())) {
            _workTypeTextView.setText(_workOrder.getTypeOfWork().getName().toUpperCase());
        } else {
            _workTypeTextView.setText("");
        }

        setWarning(false);

        if (_workOrder.getProblems() != null
                && _workOrder.getProblems().getResults() != null
                && _workOrder.getProblems().getResults().length > 0) {
            for (Problem problem : _workOrder.getProblems().getResults()) {
                if (problem != null
                        && problem.getActionsSet().contains(Problem.ActionsEnum.RESOLVE)) {
                    setWarning(true);
                    break;
                }
            }
        }

        if (_workOrder.getHolds() != null
                && _workOrder.getHolds().getResults() != null
                && _workOrder.getHolds().getResults().length > 0) {
            Hold[] holds = _workOrder.getHolds().getResults();
            for (Hold hold : holds) {
                if (hold.getAcknowledgment().getStatus() != Acknowledgment.StatusEnum.ACKNOWLEDGED) {
                    setWarning(true);
                    break;
                }
            }
        }

        populateLocation();
        populatePay();
        populateTime();
        populateButtons();
    }

    private void setWarning(boolean warning) {
        if (warning) {
            _warningBarView.setVisibility(VISIBLE);
            _amountTextView.setTextColor(getResources().getColor(R.color.fn_white_text));
            _payTypeTextView.setTextColor(getResources().getColor(R.color.fn_white_text));
            _workTypeTextView.setTextColor(getResources().getColor(R.color.fn_white_text));
        } else {
            _warningBarView.setVisibility(GONE);
            _amountTextView.setTextColor(getResources().getColor(R.color.fn_gray_dark));
            _payTypeTextView.setTextColor(getResources().getColor(R.color.fn_gray_light));
            _workTypeTextView.setTextColor(getResources().getColor(R.color.fn_gray_light));
        }
    }

    private void populateTime() {
        _timeTextView.setVisibility(VISIBLE);
        _dateTextView.setVisibility(VISIBLE);
        _hyphenTextView.setVisibility(GONE);
        _time2TextView.setVisibility(GONE);
        _date2TextView.setVisibility(GONE);

        if (_workOrder.getSchedule() != null) {
            if (_workOrder.getEta() != null
                    && _workOrder.getEta().getStatus() != null
                    && _workOrder.getEta().getStatus().getName() != null
                    && _workOrder.getEta().getStatus().getName() != ETAStatus.NameEnum.UNCONFIRMED
                    && _workOrder.getEta().getStart() != null) {
                // estimated
                try {
                    Calendar cal = _workOrder.getEta().getStart().getCalendar();
                    _timeTextView.setText(new SimpleDateFormat("h:mm a", Locale.getDefault()).format(cal.getTime()));
                    _dateTextView.setText(new SimpleDateFormat("MMM d", Locale.getDefault()).format(cal.getTime()));
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                    _timeTextView.setVisibility(GONE);
                    _dateTextView.setVisibility(GONE);
                }

                // exact
            } else if (_workOrder.getSchedule().getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.EXACT) {
                try {
                    Calendar cal = _workOrder.getSchedule().getServiceWindow().getStart().getCalendar();
                    _timeTextView.setText(new SimpleDateFormat("h:mm a", Locale.getDefault()).format(cal.getTime()));
                    _dateTextView.setText(new SimpleDateFormat("MMM d", Locale.getDefault()).format(cal.getTime()));
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                    _timeTextView.setVisibility(GONE);
                    _dateTextView.setVisibility(GONE);
                }

                // range
            } else if (_workOrder.getSchedule().getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.HOURS) {
                // business
                try {
                    Calendar scal = _workOrder.getSchedule().getServiceWindow().getStart().getCalendar();
                    Calendar ecal = _workOrder.getSchedule().getServiceWindow().getEnd().getCalendar();
                    _timeTextView.setText(new SimpleDateFormat("h:mm a", Locale.getDefault()).format(scal.getTime())
                            + " - " + new SimpleDateFormat("h:mm a", Locale.getDefault()).format(ecal.getTime()));
                    _dateTextView.setText(new SimpleDateFormat("MMM d", Locale.getDefault()).format(scal.getTime())
                            + " - " + new SimpleDateFormat("d", Locale.getDefault()).format(ecal.getTime()));
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                    _timeTextView.setVisibility(GONE);
                    _dateTextView.setVisibility(GONE);
                }

            } else if (_workOrder.getSchedule().getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.BETWEEN) {
                // normal range
                try {
                    Calendar scal = _workOrder.getSchedule().getServiceWindow().getStart().getCalendar();
                    Calendar ecal = _workOrder.getSchedule().getServiceWindow().getEnd().getCalendar();
                    _timeTextView.setText(new SimpleDateFormat("h:mm a", Locale.getDefault()).format(scal.getTime()));
                    _dateTextView.setText(new SimpleDateFormat("MMM d", Locale.getDefault()).format(scal.getTime()));
                    _time2TextView.setText(new SimpleDateFormat("h:mm a", Locale.getDefault()).format(ecal.getTime()));
                    _date2TextView.setText(new SimpleDateFormat("MMM d", Locale.getDefault()).format(ecal.getTime()));
                    _hyphenTextView.setVisibility(VISIBLE);
                    _time2TextView.setVisibility(VISIBLE);
                    _date2TextView.setVisibility(VISIBLE);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                    _timeTextView.setVisibility(GONE);
                    _dateTextView.setVisibility(GONE);
                }

            } else {
                _timeTextView.setText("");
            }
        }
    }

    private void populateLocation() {
        com.fieldnation.v2.data.model.Location location = _workOrder.getLocation();
        if (location == null || location.getMode() == com.fieldnation.v2.data.model.Location.ModeEnum.REMOTE) {
            _locationTextView.setText(R.string.remote_work);
            _distanceTextView.setVisibility(GONE);
        } else {
            if (location.getCoordinates() == null || _location == null) {
                _locationTextView.setText(location.getCityState());
                _distanceTextView.setVisibility(GONE);
            } else {
                try {
                    Position siteLoc = new Position(location.getCoordinates().getLatitude(), location.getCoordinates().getLongitude());
                    Position myLoc = new Position(_location.getLatitude(), _location.getLongitude());
                    _locationTextView.setText(location.getCityState());
                    _distanceTextView.setVisibility(VISIBLE);
                    _distanceTextView.setText(myLoc.distanceTo(siteLoc) + " mi");
                } catch (Exception ex) {
                    _locationTextView.setText(location.getCityState());
                    _distanceTextView.setVisibility(GONE);
                }
            }
        }
    }

    private void populatePay() {
        Pay pay = _workOrder.getPay();

        if (pay == null || pay.getType() == null) {
            _payTypeTextView.setVisibility(INVISIBLE);
            _amountTextView.setVisibility(INVISIBLE);
            return;
        }

        _payTypeTextView.setVisibility(VISIBLE);
        _amountTextView.setVisibility(VISIBLE);

        try {
            switch (pay.getType()) {
                case FIXED:
                    _amountTextView.setText(misc.toShortCurrency(pay.getBase().getAmount()));
                    _payTypeTextView.setText(getResources().getString(R.string.payment_type_fixed));
                    break;
                case HOURLY:
                    _amountTextView.setText(misc.toShortCurrency(pay.getBase().getAmount()));
                    _payTypeTextView.setText(getResources().getString(R.string.payment_type_hourly, pay.getBase().getUnits().intValue()));
                    break;
                case BLENDED:
                    _amountTextView.setText(misc.toShortCurrency(pay.getBase().getAmount()));
                    _payTypeTextView.setText(getResources().getString(R.string.payment_type_blended, pay.getAdditional().getAmount().intValue(), pay.getAdditional().getUnits().intValue()));
                    break;
                case DEVICE:
                    _amountTextView.setText(misc.toShortCurrency(pay.getBase().getAmount()));
                    _payTypeTextView.setText(getResources().getString(R.string.payment_type_device, pay.getBase().getUnits().intValue()));
                    break;
                default:
                    _payTypeTextView.setVisibility(INVISIBLE);
                    _amountTextView.setVisibility(INVISIBLE);
                    break;
            }
        } catch (Exception ex) {
            _payTypeTextView.setVisibility(INVISIBLE);
            _amountTextView.setVisibility(INVISIBLE);
        }

        try {
            if (!misc.isEmptyOrNull(_workOrder.getStatus().getName())) {
                switch (_workOrder.getStatus().getName().toLowerCase()) {
                    case "approved":
                        _amountTextView.setText(misc.toShortCurrency(pay.getTotal()));
                        _payTypeTextView.setText("APPROVED");
                        _amountTextView.setVisibility(VISIBLE);
                        _payTypeTextView.setVisibility(VISIBLE);
                        break;
                    case "paid":
                        _amountTextView.setText(misc.toShortCurrency(pay.getTotal()));
                        _payTypeTextView.setText("PAID");
                        _amountTextView.setVisibility(VISIBLE);
                        _payTypeTextView.setVisibility(VISIBLE);
                        break;
                    case "work done":
                        _amountTextView.setText(misc.toShortCurrency(pay.getTotal()));
                        _payTypeTextView.setText("IN REVIEW");
                        _amountTextView.setVisibility(VISIBLE);
                        _payTypeTextView.setVisibility(VISIBLE);
                        break;
                    default:
                        // Log.v(TAG, "break!");
                        break;
                }
            }
        } catch (Exception ex) {
        }
    }

    private void populateButtons() {
        _primaryButton.setVisibility(GONE);
        populatePrimaryButton(_primaryButton);

        for (Button button : _secondaryButtons) {
            button.setVisibility(GONE);
        }
        populateSecondaryButtons();
    }

    private void populatePrimaryButton(Button button) {
        // Order of operations
        // check_out
        // check_in
        // set eta
        // ready (NCNS confirm)
        // on my way
        // ack hold
        // mark incomplete
        // view_bundle
        // accept
        // request
        // withdraw

        button.setEnabled(true);

        if (false) {

            // ack hold/
        } else if (_workOrder.isOnHold() && !_workOrder.areHoldsAcknowledged()) {
            button.setVisibility(VISIBLE);
            button.setOnClickListener(_ackHold_onClick);
            button.setText(R.string.btn_review_hold);

            // is on hold
        } else if (_workOrder.isOnHold()) {
            button.setVisibility(VISIBLE);
            button.setText(R.string.btn_on_hold);
            button.setEnabled(false);

            // set eta
        } else if (_workOrder.getEta() != null
                && _workOrder.getEta().getActionsSet().contains(ETA.ActionsEnum.ADD)) {
            button.setVisibility(VISIBLE);
            button.setOnClickListener(_eta_onClick);
            button.setText(R.string.btn_set_eta);

        } else if (_workOrder.getEta() != null
                && _workOrder.getEta().getActionsSet().contains(ETA.ActionsEnum.MARK_READY_TO_GO)) {
            button.setVisibility(VISIBLE);
            button.setOnClickListener(_readyToGo_onClick);
            button.setText(R.string.btn_ready_to_go);

            // confirm
        } else if (_workOrder.getEta() != null
                && _workOrder.getEta().getActionsSet().contains(ETA.ActionsEnum.CONFIRM)) {
            button.setVisibility(VISIBLE);
            button.setOnClickListener(_confirm_onClick);
            button.setText(R.string.btn_confirm);

            // on my way
        } else if (_workOrder.getEta() != null
                && _workOrder.getEta().getActionsSet().contains(ETA.ActionsEnum.ON_MY_WAY)) {
            button.setVisibility(VISIBLE);
            button.setOnClickListener(_onMyWay_onClick);
            button.setText(R.string.btn_on_my_way);


            // check_out
        } else if (_workOrder.getTimeLogs() != null
                && _workOrder.getTimeLogs().getOpenTimeLog() != null
                && _workOrder.getTimeLogs().getOpenTimeLog().getActionsSet().contains(TimeLog.ActionsEnum.EDIT)) {
            button.setVisibility(VISIBLE);
            button.setOnClickListener(_checkOut_onClick);
            button.setText(R.string.btn_check_out);

            // mark complete
//        } else if (workOrderActions.contains(WorkOrder.ActionsEnum.MARK_COMPLETE)) {
//            button.setVisibility(VISIBLE);
//            button.setOnClickListener(_complete_onClick);
//            button.setText(R.string.btn_complete);

            // check_in
        } else if (_workOrder.getTimeLogs() != null
                && _workOrder.getTimeLogs().getActionsSet().contains(TimeLogs.ActionsEnum.ADD)) {
            button.setVisibility(VISIBLE);
            if (_workOrder.getTimeLogs().getMetadata().getTotal() >= 1) {
                button.setText(R.string.btn_check_in_again);
                button.setOnClickListener(_checkInAgain_onClick);
            } else {
                button.setText(R.string.btn_check_in);
                button.setOnClickListener(_checkIn_onClick);
            }

            // mark incomplete
        } else if (_workOrder.getActionsSet() != null
                && _workOrder.getActionsSet().contains(WorkOrder.ActionsEnum.INCOMPLETE)) {
            button.setVisibility(VISIBLE);
            button.setOnClickListener(_incomplete_onClick);
            button.setText(R.string.btn_incomplete);

            // view_bundle
        } else if (_workOrder.getBundle() != null
                && _workOrder.getBundle().getActionsSet().contains(Bundle.ActionsEnum.VIEW)) {
            button.setVisibility(VISIBLE);
            button.setOnClickListener(_viewBundle_onClick);
            button.setText(getResources().getString(R.string.btn_view_bundle_num,
                    _workOrder.getBundle().getMetadata().getTotal() + 1));

            // accept
        } else if (_workOrder.getRoutes() != null
                && _workOrder.getRoutes().getUserRoute() != null
                && _workOrder.getRoutes().getUserRoute().getActionsSet().contains(Route.ActionsEnum.ACCEPT)) {
            button.setVisibility(VISIBLE);
            button.setOnClickListener(_accept_onClick);
            button.setText(R.string.btn_accept);

            // request
        } else if (_workOrder.getRequests() != null
                && _workOrder.getRequests().getActionsSet().contains(Requests.ActionsEnum.ADD)) {
            button.setVisibility(VISIBLE);
            button.setOnClickListener(_request_onClick);
            button.setText(R.string.btn_request);

            // withdraw
        } else if (_workOrder.getRequests() != null
                && _workOrder.getRequests().getOpenRequest() != null
                && _workOrder.getRequests().getOpenRequest().getActionsSet().contains(Request.ActionsEnum.DELETE)) {
            button.setVisibility(VISIBLE);
            button.setOnClickListener(_withdraw_onClick);
            button.setText(R.string.btn_withdraw);

        } else if (_workOrder.getStatus().getId() == 6) {
            button.setVisibility(VISIBLE);
            button.setOnClickListener(_viewPayment_onClick);
            button.setText(R.string.btn_view_payment);

        } else if (_workOrder.getStatus().getId() == 7) {
            button.setVisibility(VISIBLE);
            button.setOnClickListener(_viewPayment_onClick);
            button.setText(R.string.btn_fees);
        }
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        _onActionListener = onActionListener;
    }

    // other icons
    // phone-solid
    // map-location-solid
    // chat-solid
    // circle-x-solid
    // problem-solid
    // time-issue-solid
    private void populateSecondaryButtons() {
        int buttonId = 0;
        Button button = _secondaryButtons[buttonId];

        boolean isBundle = _workOrder.getBundle() != null && _workOrder.getBundle().getId() != null && _workOrder.getBundle().getId() != 0;

        // decline
        if (!isBundle
                && _workOrder.getRequests() != null
                && (_workOrder.getRequests().getActionsSet().contains(Requests.ActionsEnum.ADD)
                || _workOrder.getRequests().getActionsSet().contains(Requests.ActionsEnum.COUNTER_OFFER))) {
            button.setVisibility(VISIBLE);
            button.setText(R.string.icon_circle_x_solid);
            button.setOnClickListener(_decline_onClick);
            buttonId++;
            if (buttonId >= _secondaryButtons.length) return;
            button = _secondaryButtons[buttonId];
        }

        // running late
        if (_workOrder.getEta() != null
                && _workOrder.getEta().getActionsSet().contains(ETA.ActionsEnum.RUNNING_LATE)) {
            button.setVisibility(VISIBLE);
            button.setText(R.string.icon_time_issue_solid);
            button.setOnClickListener(_runningLate_onClick);
            buttonId++;
            if (buttonId >= _secondaryButtons.length) return;
            button = _secondaryButtons[buttonId];
        }

        // report a problem
        if (_workOrder.getProblems() != null
                && _workOrder.getProblems().getActionsSet().contains(Problems.ActionsEnum.ADD)) {
            button.setVisibility(VISIBLE);
            button.setText(R.string.icon_problem_solid);
            button.setOnClickListener(_reportProblem_onClick);
            buttonId++;
            if (buttonId >= _secondaryButtons.length) return;
            button = _secondaryButtons[buttonId];
        }

        // phone
        if (_workOrder.getContacts() != null
                && _workOrder.getContacts().getResults() != null
                && _workOrder.getContacts().getResults().length > 0) {
            button.setVisibility(VISIBLE);
            button.setText(R.string.icon_phone_solid);
            button.setOnClickListener(_phone_onClick);
            buttonId++;
            if (buttonId >= _secondaryButtons.length) return;
            button = _secondaryButtons[buttonId];
        }

        // message
        if (_workOrder.getActionsSet() != null
                && _workOrder.getActionsSet().contains(WorkOrder.ActionsEnum.MESSAGING)) {
            button.setVisibility(VISIBLE);
            button.setText(R.string.icon_chat_solid);
            button.setOnClickListener(_message_onClick);
            buttonId++;
            if (buttonId >= _secondaryButtons.length) return;
            button = _secondaryButtons[buttonId];
        }

        // map
        if (_workOrder.getLocation() != null
                && _workOrder.getLocation().getCoordinates() != null) {
            button.setVisibility(VISIBLE);
            button.setText(R.string.icon_map_location_solid);
            button.setOnClickListener(_map_onClick);
            buttonId++;
            if (buttonId >= _secondaryButtons.length) return;
            button = _secondaryButtons[buttonId];
        }
    }

    private final OnClickListener _viewPayment_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search",
                    WorkOrderTracker.ActionButton.VIEW_PAYMENT, null, _workOrder.getId());
            PaymentListActivity.startNew(App.get());
        }
    };

    private final OnClickListener _incomplete_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search",
                    WorkOrderTracker.ActionButton.MARK_INCOMPLETE, null, _workOrder.getId());

            MarkIncompleteWarningDialog.show(App.get(), DIALOG_MARK_INCOMPLETE, _workOrder.getId());
        }
    };

    private final MarkIncompleteWarningDialog.OnMarkIncompleteListener _markIncompleteWarningDialog_onMarkIncomplete = new MarkIncompleteWarningDialog.OnMarkIncompleteListener() {
        @Override
        public void onMarkIncomplete(long workOrderId) {
            if (_onActionListener != null) _onActionListener.onAction();

            if (_workOrder.getId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search",
                        WorkOrderTracker.ActionButton.MARK_INCOMPLETE, WorkOrderTracker.Action.MARK_INCOMPLETE, _workOrder.getId());
        }
    };

    private final OnClickListener _ackHold_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_onActionListener != null) _onActionListener.onAction();

            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.ACKNOWLEDGE_HOLD, WorkOrderTracker.Action.ACKNOWLEDGE_HOLD, _workOrder.getId());
            HoldReviewDialog.show(App.get(), DIALOG_HOLD_REVIEW, _workOrder);

        }
    };

    private final View.OnClickListener _checkInAgain_onClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.CHECK_IN_AGAIN, null, _workOrder.getId());
            if (_workOrder.getPay() != null
                    && _workOrder.getPay().getType() == Pay.TypeEnum.DEVICE
                    && _workOrder.getPay().getBase() != null
                    && _workOrder.getPay().getBase().getUnits() != null) {
                CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_OUT, _workOrder, _location,
                        _workOrder.getPay().getBase().getUnits().intValue(),
                        CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_IN);
            } else {
                CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_OUT, _workOrder, _location,
                        CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_IN);
            }
        }
    };

    private final OnClickListener _checkIn_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.CHECK_IN, null, _workOrder.getId());
            if (_workOrder.getPay() != null
                    && _workOrder.getPay().getType() == Pay.TypeEnum.DEVICE
                    && _workOrder.getPay().getBase() != null
                    && _workOrder.getPay().getBase().getUnits() != null) {
                CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_OUT, _workOrder, _location,
                        _workOrder.getPay().getBase().getUnits().intValue(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_IN);
            } else {
                CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_OUT, _workOrder, _location,
                        CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_IN);
            }
        }
    };

    private final CheckInOutDialog.OnCheckInListener _checkInOutDialog_onCheckIn = new CheckInOutDialog.OnCheckInListener() {
        @Override
        public void onCheckIn(int workOrderId) {
            if (_onActionListener != null) _onActionListener.onAction();

            if (_workOrder.getId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.CHECK_IN, WorkOrderTracker.Action.CHECK_IN, workOrderId);
        }
    };

    private final OnClickListener _checkOut_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.CHECK_OUT, null, _workOrder.getId());
            if (_workOrder.getPay() != null
                    && _workOrder.getPay().getType() == Pay.TypeEnum.DEVICE
                    && _workOrder.getPay().getBase() != null
                    && _workOrder.getPay().getBase().getUnits() != null) {
                CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_OUT, _workOrder, _location,
                        _workOrder.getPay().getBase().getUnits().intValue(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            } else {
                CheckInOutDialog.show(App.get(), DIALOG_CHECK_IN_OUT, _workOrder, _location,
                        CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            }
        }
    };

    private final CheckInOutDialog.OnCheckOutListener _checkInOutDialog_onCheckOut = new CheckInOutDialog.OnCheckOutListener() {
        @Override
        public void onCheckOut(int workOrderId) {
            if (_onActionListener != null) _onActionListener.onAction();

            if (_workOrder.getId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.CHECK_OUT, WorkOrderTracker.Action.CHECK_OUT, workOrderId);
        }
    };

    private final OnClickListener _request_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.REQUEST, null, _workOrder.getId());
            EtaDialog.show(App.get(), DIALOG_ETA, _workOrder);
        }
    };

    private final EtaDialog.OnRequestedListener _etaDialog_onRequested = new EtaDialog.OnRequestedListener() {
        @Override
        public void onRequested(int workOrderId) {
            if (_onActionListener != null) _onActionListener.onAction();

            if (_workOrder.getId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.REQUEST, WorkOrderTracker.Action.REQUEST, workOrderId);
        }
    };

    private final OnClickListener _accept_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.ACCEPT_WORK, null, _workOrder.getId());
            EtaDialog.show(App.get(), DIALOG_ETA, _workOrder);
        }
    };

    private final EtaDialog.OnAcceptedListener _etaDialog_onAccepted = new EtaDialog.OnAcceptedListener() {
        @Override
        public void onAccepted(int workOrderId) {
            if (_onActionListener != null) _onActionListener.onAction();

            if (_workOrder.getId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.ACCEPT_WORK, WorkOrderTracker.Action.ACCEPT_WORK, workOrderId);
        }
    };

    private final OnClickListener _confirm_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_onActionListener != null) _onActionListener.onAction();

            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.CONFIRM, null, _workOrder.getId());
            try {
                ETA eta = new ETA()
                        .status(new ETAStatus()
                                .name(ETAStatus.NameEnum.CONFIRMED));

                WorkordersWebApi.updateETA(App.get(), _workOrder.getId(), eta, App.get().getSpUiContext());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final OnClickListener _eta_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.ETA, null, _workOrder.getId());
            EtaDialog.show(App.get(), DIALOG_ETA, _workOrder);
        }
    };

    private final EtaDialog.OnEtaListener _etaDialog_onEta = new EtaDialog.OnEtaListener() {
        @Override
        public void onEta(int workOrderId) {
            if (_onActionListener != null) _onActionListener.onAction();

            if (_workOrder.getId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.ETA, WorkOrderTracker.Action.ETA, workOrderId);
        }
    };

    private final OnClickListener _decline_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.NOT_INTERESTED, null, _workOrder.getId());
            DeclineDialog.show(App.get(), DIALOG_DECLINE, _workOrder.getId(), _workOrder.getCompany().getId());
        }
    };

    private final DeclineDialog.OnDeclinedListener _declineDialog_onDeclined = new DeclineDialog.OnDeclinedListener() {
        @Override
        public void onDeclined(long workOrderId) {
            if (_onActionListener != null) _onActionListener.onAction();

            if (_workOrder.getId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.NOT_INTERESTED, WorkOrderTracker.Action.NOT_INTERESTED, workOrderId);
        }
    };

    private final OnClickListener _onMyWay_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_onActionListener != null) _onActionListener.onAction();

            if (!App.get().isLocationEnabled()) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                PendingIntent PI = PendingIntent.getActivity(App.get(), ActivityResultConstants.RESULT_CODE_ENABLE_GPS, intent, PendingIntent.FLAG_ONE_SHOT);
                ToastClient.snackbar(App.get(), "We would like to use your location to provide more accurate status information to the buyer.", "LOCATION SETTINGS", PI, Snackbar.LENGTH_INDEFINITE);
            }

            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.ON_MY_WAY, WorkOrderTracker.Action.ON_MY_WAY, _workOrder.getId());
            try {
                ETAStatus etaStatus = new ETAStatus().name(ETAStatus.NameEnum.ONMYWAY);

                ETA eta = new ETA();
                eta.status(etaStatus);

                if (_location != null)
                    eta.condition(new Condition()
                            .coords(new Coords(_location.getLatitude(), _location.getLongitude())));

                WorkordersWebApi.updateETA(App.get(), _workOrder.getId(), eta, App.get().getSpUiContext());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            try {
                GpsTrackingService.start(App.get(), System.currentTimeMillis() + 7200000); // 2 hours
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final OnClickListener _viewBundle_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.VIEW_BUNDLE, null, _workOrder.getId());
            BundleDetailActivity.startNew(App.get(), _workOrder.getBundle().getId());
        }
    };

    private final OnClickListener _readyToGo_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_onActionListener != null) _onActionListener.onAction();

            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.READY_TO_GO, WorkOrderTracker.Action.READY_TO_GO, _workOrder.getId());
            try {
                ETA eta = new ETA()
                        .status(new ETAStatus()
                                .name(ETAStatus.NameEnum.READYTOGO));

                WorkordersWebApi.updateETA(App.get(), _workOrder.getId(), eta, App.get().getSpUiContext());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final OnClickListener _reportProblem_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.REPORT_PROBLEM, null, _workOrder.getId());
            ReportProblemDialog.show(App.get(), DIALOG_REPORT_PROBLEM, _workOrder);
        }
    };

    private final ReportProblemDialog.OnSendListener _reportProblemDialog_onSend = new ReportProblemDialog.OnSendListener() {
        @Override
        public void onSend(int workOrderId, String explanation, ProblemType type) {
            if (_onActionListener != null) _onActionListener.onAction();

            if (_workOrder.getId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.REPORT_PROBLEM, WorkOrderTracker.Action.REPORT_PROBLEM, workOrderId);
        }
    };

    private final OnClickListener _phone_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_onActionListener != null) _onActionListener.onAction();

            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.CALL_BUYER, null, _workOrder.getId());
            try {
                Contact contact = _workOrder.getContacts().getResults()[0];
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contact.getPhone()));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(callIntent);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final OnClickListener _withdraw_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.WITHDRAW, null, _workOrder.getId());
            WithdrawRequestDialog.show(App.get(), DIALOG_WITHDRAW_REQUEST, _workOrder.getId(), 0, _workOrder.getRequests().getOpenRequest().getId());
        }
    };

    private final WithdrawRequestDialog.OnWithdrawListener _withdrawRequestDialog_onWithdraw = new WithdrawRequestDialog.OnWithdrawListener() {
        @Override
        public void onWithdraw(int workOrderId) {
            if (_onActionListener != null) _onActionListener.onAction();

            if (_workOrder.getId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.WITHDRAW, WorkOrderTracker.Action.WITHDRAW, (int) workOrderId);
        }
    };

    private final OnClickListener _message_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.VIEW_MESSAGES, null, _workOrder.getId());
            WorkOrderActivity.startNew(App.get(), _workOrder.getId(), WorkOrderActivity.TAB_MESSAGE);
        }
    };

    private final OnClickListener _runningLate_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.RUNNING_LATE, null, _workOrder.getId());
            RunningLateDialog.show(App.get(), DIALOG_RUNNING_LATE, _workOrder);
        }
    };

    private final RunningLateDialog.OnSendListener _runningLateDialog_onSend = new RunningLateDialog.OnSendListener() {
        @Override
        public void onSend(int workOrderId) {
            if (_onActionListener != null) _onActionListener.onAction();

            if (_workOrder.getId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.RUNNING_LATE, WorkOrderTracker.Action.RUNNING_LATE, (int) workOrderId);
        }
    };

    private final HoldReviewDialog.OnAcknowledgeListener _holdReviewDialog_onAcknowledge = new HoldReviewDialog.OnAcknowledgeListener() {
        @Override
        public void onAcknowledge(int workOrderId) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.ACKNOWLEDGE_HOLD,
                    WorkOrderTracker.Action.ACKNOWLEDGE_HOLD, _workOrder.getId());

        }
    };

    private final HoldReviewDialog.OnCancelListener _holdReviewDialog_onCancel = new HoldReviewDialog.OnCancelListener() {
        @Override
        public void onCancel() {
        }
    };

    private final OnClickListener _map_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.DIRECTIONS, null, _workOrder.getId());
            if (_workOrder != null) {
                com.fieldnation.v2.data.model.Location location = _workOrder.getLocation();

                if (location != null) {
                    try {
                        String _fullAddress = misc.escapeForURL(location.getFullAddressOneLine());
                        String _uriString = "geo:0,0?q=" + _fullAddress;
                        Uri _uri = Uri.parse(_uriString);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(_uri);
                        ActivityResultClient.startActivity(App.get(), intent);
                    } catch (Exception e) {
                        Log.v(TAG, e);
                        ToastClient.toast(App.get(), "Could not start map", Toast.LENGTH_SHORT);
                    }
                }
            }
        }
    };

    private final OnClickListener _test_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ConfirmActivity.startNew(App.get());
            //_onMyWay_onClick.onClick(v);
        }
    };

    private final OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityResultClient.startActivity(App.get(),
                    WorkOrderActivity.makeIntentShow(App.get(), _workOrder.getId()),
                    R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
        }
    };

    public interface OnActionListener {
        void onAction();
    }
}