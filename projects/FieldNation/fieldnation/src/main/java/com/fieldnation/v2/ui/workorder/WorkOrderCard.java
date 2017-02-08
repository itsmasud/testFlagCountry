package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
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
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.data.gmaps.Position;
import com.fieldnation.service.data.workorder.ReportProblemType;
import com.fieldnation.ui.IconFontButton;
import com.fieldnation.ui.dialog.v2.ReportProblemDialog;
import com.fieldnation.ui.dialog.v2.RunningLateDialog;
import com.fieldnation.ui.dialog.v2.WithdrawRequestDialog;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderBundleDetailActivity;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.ScheduleServiceWindow;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.CheckInOutDialog;
import com.fieldnation.v2.ui.dialog.DeclineDialog;
import com.fieldnation.v2.ui.dialog.EtaDialog;
import com.fieldnation.v2.ui.dialog.MarkIncompleteWarningDialog;

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

    // Ui
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
        if (!BuildConfig.DEBUG)
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

        _titleTextView.setText(_workOrder.getWorkOrderId() + " | " + _workOrder.getTitle());
        if (_workOrder.getTypeOfWork() != null && !misc.isEmptyOrNull(_workOrder.getTypeOfWork().getName())) {
            _workTypeTextView.setText(_workOrder.getTypeOfWork().getName().toUpperCase());
        } else {
            _workTypeTextView.setText("");
        }

        populateLocation();
        populatePay();
        populateTime();
        populateButtons();
    }

    private void populateTime() {
        _timeTextView.setVisibility(VISIBLE);
        _dateTextView.setVisibility(VISIBLE);
        _hyphenTextView.setVisibility(GONE);
        _time2TextView.setVisibility(GONE);
        _date2TextView.setVisibility(GONE);

        if (_workOrder.getSchedule() != null) {
            if (_workOrder.getSchedule().getEta() != null && _workOrder.getSchedule().getEta().getStart() != null) {
                // estimated
                try {
                    Calendar cal = _workOrder.getSchedule().getEta().getStart().getCalendar();
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

        switch (pay.getType()) {
            case "fixed":
                _amountTextView.setText(misc.toShortCurrency(pay.getBase().getAmount()));
                _payTypeTextView.setText(getResources().getString(R.string.payment_type_fixed));
                break;
            case "hourly":
                _amountTextView.setText(misc.toShortCurrency(pay.getBase().getAmount()));
                _payTypeTextView.setText(getResources().getString(R.string.payment_type_hourly, pay.getBase().getUnits().intValue()));
                break;
            case "blended":
                _amountTextView.setText(misc.toShortCurrency(pay.getBase().getAmount()));
                _payTypeTextView.setText(getResources().getString(R.string.payment_type_blended, pay.getAdditional().getAmount().intValue(), pay.getAdditional().getUnits().intValue()));
                break;
            case "device":
                _amountTextView.setText(misc.toShortCurrency(pay.getBase().getAmount()));
                _payTypeTextView.setText(getResources().getString(R.string.payment_type_device, pay.getBase().getUnits().intValue()));
                break;
            default:
                _payTypeTextView.setVisibility(INVISIBLE);
                _amountTextView.setVisibility(INVISIBLE);
                break;
        }

        if (!misc.isEmptyOrNull(_workOrder.getStatus().getName())) {
            switch (_workOrder.getStatus().getName().toLowerCase()) {
                case "approved":
                    _amountTextView.setVisibility(VISIBLE);
                    _payTypeTextView.setVisibility(VISIBLE);
                    _amountTextView.setText(misc.toShortCurrency(pay.getTotal()));
                    _payTypeTextView.setText("APPROVED");
                    break;
                case "paid":
                    _amountTextView.setVisibility(VISIBLE);
                    _payTypeTextView.setVisibility(VISIBLE);
                    _amountTextView.setText(misc.toShortCurrency(pay.getTotal()));
                    _payTypeTextView.setText("PAID");
                    break;
                case "work done":
                    _amountTextView.setVisibility(VISIBLE);
                    _payTypeTextView.setVisibility(VISIBLE);
                    // we use the payment value set above, because total isn't set at this stage
                    _payTypeTextView.setText("IN REVIEW");
                    break;
                default:
                    Log.v(TAG, "break!");
                    break;
            }
        }
    }

    private void populateButtons() {
        WorkOrder.ActionsEnum[] actions = _workOrder.getSortedActions();

        _primaryButton.setVisibility(GONE);

        for (Button button : _secondaryButtons) {
            button.setVisibility(GONE);
        }

        if (actions == null || actions.length == 0) {
            return;
        }

        // Primary actions
        for (WorkOrder.ActionsEnum action : actions) {
            if (action != null && populatePrimaryButton(_primaryButton, action))
                break;
        }

        int i = 0; // action index
        int j = 0; // button index
        // assign supported actions to buttons until no more actions or no more buttons
        while (i < actions.length && j < _secondaryButtons.length) {
            WorkOrder.ActionsEnum action = actions[i];

            // only if the action has been assigned do we move to the next button
            if (action != null && populateSecondaryButton(_secondaryButtons[j], action)) {
                j++;
            }
            i++;
        }
    }

    private boolean populatePrimaryButton(Button button, WorkOrder.ActionsEnum action) {
        switch (action) {
//            case ON_MY_WAY:
//                button.setVisibility(VISIBLE);
//                button.setOnClickListener(_onMyWay_onClick);
//                button.setText(R.string.btn_on_my_way);
//                break;
//            case VIEW_BUNDLE:
//                button.setVisibility(VISIBLE);
//                button.setOnClickListener(_viewBundle_onClick);
//                button.setText("VIEW BUNDLE (" + _workOrder.getBundle().getCount() + ")");
//                break;
//            case MARK_COMPLETE:
//                button.setVisibility(VISIBLE);
//                button.setOnClickListener(_complete_onClick);
//                button.setText("COMPLETE");
//                break;
//            // don't have a payment id in the current data structure
//            case VIEW_PAYMENT:
//                button.setVisibility(VISIBLE);
//                button.setOnClickListener(_viewPayment_onClick);
//                button.setText("VIEW PAYMENT");
//                break;

//            case READY_TO_GO:
//                button.setVisibility(VISIBLE);
//                button.setOnClickListener(_readyToGo_onClick);
//                button.setText(R.string.btn_ready);
//                break;
//            case ACCEPT:
//                button.setVisibility(VISIBLE);
//                button.setOnClickListener(_accept_onClick);
//                button.setText(R.string.btn_accept);
//                break;
//            case CONFIRM:
//                button.setVisibility(VISIBLE);
//                button.setOnClickListener(_confirm_onClick);
//                button.setText(R.string.btn_confirm);
//                break;
//            case ACKNOWLEDGE:
//                button.setVisibility(VISIBLE);
//                button.setOnClickListener(_ackHold_onClick);
//                button.setText("ACKNOWLEDGE HOLD");
//                break;
//            case WITHDRAW_REQUEST:
//                button.setVisibility(VISIBLE);
//                button.setOnClickListener(_withdraw_onClick);
//                button.setText("WITHDRAW");
//                break;
//            case MARK_INCOMPLETE:
//                button.setVisibility(VISIBLE);
//                button.setOnClickListener(_incomplete_onClick);
//                button.setText("INCOMPLETE");
//                break;

            case REPORT_A_PROBLEM:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_reportProblem_onClick);
                button.setText(R.string.btn_report_problem);
                break;
            case REQUEST:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_request_onClick);
                button.setText(R.string.btn_request);
                break;
            case CHECK_IN:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_checkIn_onClick);
                button.setText("CHECK IN");
                break;
            case CHECK_OUT:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_checkOut_onClick);
                button.setText("CHECK OUT");
                break;
            case ETA:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_eta_onClick);
                button.setText("SET ETA");
                break;
            default:
                return false;
        }
        return true;
    }

    // other icons
    // phone-solid
    // map-location-solid
    // chat-solid
    // circle-x-solid
    // problem-solid
    // time-issue-solid
    private boolean populateSecondaryButton(IconFontButton button, WorkOrder.ActionsEnum action) {
        switch (action) {
//            case DECLINE:
//                button.setVisibility(VISIBLE);
//                button.setText(R.string.icon_circle_x_solid);
//                button.setOnClickListener(_decline_onClick);
//                break;
//            case RUNNING_LATE:
//                button.setVisibility(VISIBLE);
//                button.setText(R.string.icon_time_issue_solid);
//                button.setOnClickListener(_runningLate_onClick);
//                break;
//            case PHONE:
//                button.setVisibility(VISIBLE);
//                button.setText(R.string.icon_phone_solid);
//                button.setOnClickListener(_phone_onClick);
//                break;
//            case MAP:
//                button.setVisibility(VISIBLE);
//                button.setText(R.string.icon_map_location_solid);
//                button.setOnClickListener(_map_onClick);
//                break;

            case REPORT_A_PROBLEM:
                button.setVisibility(VISIBLE);
                button.setText(R.string.icon_problem_solid);
                button.setOnClickListener(_reportProblem_onClick);
                break;
            case MESSAGING:
                button.setVisibility(VISIBLE);
                button.setText(R.string.icon_chat_solid);
                button.setOnClickListener(_message_onClick);
                break;
            default:
                button.setVisibility(GONE);
                return false;
        }
        return true;
    }

    private final OnClickListener _incomplete_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search",
                    WorkOrderTracker.ActionButton.MARK_INCOMPLETE, null, _workOrder.getWorkOrderId());

            MarkIncompleteWarningDialog.show(App.get(), DIALOG_MARK_INCOMPLETE, _workOrder.getWorkOrderId());
        }
    };

    private final MarkIncompleteWarningDialog.OnMarkIncompleteListener _markIncompleteWarningDialog_onMarkIncomplete = new MarkIncompleteWarningDialog.OnMarkIncompleteListener() {
        @Override
        public void onMarkIncomplete(long workOrderId) {
            if (_workOrder.getWorkOrderId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search",
                        WorkOrderTracker.ActionButton.MARK_INCOMPLETE, WorkOrderTracker.Action.MARK_INCOMPLETE, _workOrder.getWorkOrderId());
        }
    };

    private final OnClickListener _ackHold_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.ACKNOWLEDGE_HOLD, WorkOrderTracker.Action.ACKNOWLEDGE_HOLD, _workOrder.getWorkOrderId());
            // TODO WorkorderClient.actionAcknowledgeHold(App.get(), _workOrder.getWorkOrderId());
        }
    };

    private final OnClickListener _checkIn_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.CHECK_IN, null, _workOrder.getWorkOrderId());
            if (_workOrder.getPay() != null && _workOrder.getPay().getType().equals("device")) {
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
        public void onCheckIn(long workOrderId) {
            if (_workOrder.getWorkOrderId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.CHECK_IN, WorkOrderTracker.Action.CHECK_IN, (int) workOrderId);
        }
    };

    private final OnClickListener _checkOut_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.CHECK_OUT, null, _workOrder.getWorkOrderId());
            if (_workOrder.getPay() != null && _workOrder.getPay().getType().equals("device")) {
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
        public void onCheckOut(long workOrderId) {
            if (_workOrder.getWorkOrderId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.CHECK_OUT, WorkOrderTracker.Action.CHECK_OUT, (int) workOrderId);
        }
    };

    private final OnClickListener _request_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.REQUEST, null, _workOrder.getWorkOrderId());
            EtaDialog.show(App.get(), DIALOG_ETA, _workOrder.getWorkOrderId(), _workOrder.getSchedule(), EtaDialog.PARAM_DIALOG_TYPE_REQUEST);
        }
    };

    private final EtaDialog.OnRequestedListener _etaDialog_onRequested = new EtaDialog.OnRequestedListener() {
        @Override
        public void onRequested(int workOrderId) {
            if (_workOrder.getWorkOrderId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.REQUEST, WorkOrderTracker.Action.REQUEST, (int) workOrderId);
        }
    };

    private final OnClickListener _accept_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.ACCEPT_WORK, null, _workOrder.getWorkOrderId());
            EtaDialog.show(App.get(), DIALOG_ETA, _workOrder.getWorkOrderId(), _workOrder.getSchedule(), EtaDialog.PARAM_DIALOG_TYPE_ACCEPT);
        }
    };

    private final EtaDialog.OnAcceptedListener _etaDialog_onAccepted = new EtaDialog.OnAcceptedListener() {
        @Override
        public void onAccepted(int workOrderId) {
            if (_workOrder.getWorkOrderId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.ACCEPT_WORK, WorkOrderTracker.Action.ACCEPT_WORK, (int) workOrderId);
        }
    };

    private final OnClickListener _confirm_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.CONFIRM, null, _workOrder.getWorkOrderId());
            WorkordersWebApi.confirm(App.get(), _workOrder.getWorkOrderId());
        }
    };

    private final OnClickListener _eta_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.ETA, null, _workOrder.getWorkOrderId());
            EtaDialog.show(App.get(), DIALOG_ETA, _workOrder.getWorkOrderId(), _workOrder.getSchedule(), EtaDialog.PARAM_DIALOG_TYPE_ETA);
        }
    };

    private final EtaDialog.OnEtaListener _etaDialog_onEta = new EtaDialog.OnEtaListener() {
        @Override
        public void onEta(int workOrderId) {
            if (_workOrder.getWorkOrderId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.ETA, WorkOrderTracker.Action.ETA, workOrderId);
        }
    };

    private final OnClickListener _decline_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.NOT_INTERESTED, null, _workOrder.getWorkOrderId());
            DeclineDialog.show(App.get(), DIALOG_DECLINE, _workOrder.getWorkOrderId(), _workOrder.getCompany().getId());
        }
    };

    private final DeclineDialog.OnDeclinedListener _declineDialog_onDeclined = new DeclineDialog.OnDeclinedListener() {
        @Override
        public void onDeclined(long workOrderId) {
            if (_workOrder.getWorkOrderId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.NOT_INTERESTED, WorkOrderTracker.Action.NOT_INTERESTED, workOrderId);
        }
    };

/*
    private final OnClickListener _onMyWay_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.ON_MY_WAY, WorkOrderTracker.Action.ON_MY_WAY, _workOrder.getWorkOrderId());
            if (_location != null)
                WorkOrderClient.actionOnMyWay(App.get(), _workOrder.getWorkOrderId(), _location.getLatitude(), _location.getLongitude());
            else
                WorkOrderClient.actionOnMyWay(App.get(), _workOrder.getWorkOrderId(), null, null);

            try {
                GpsTrackingService.start(App.get(), System.currentTimeMillis() + 3600000); // 1 hours
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
*/

    private final OnClickListener _viewBundle_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.VIEW_BUNDLE, null, _workOrder.getWorkOrderId());
            WorkorderBundleDetailActivity.startNew(App.get(), _workOrder.getWorkOrderId(), _workOrder.getBundle().getId());
        }
    };


    private final OnClickListener _readyToGo_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.READY_TO_GO, WorkOrderTracker.Action.READY_TO_GO, _workOrder.getWorkOrderId());
            // TODO WorkorderClient.actionReadyToGo(App.get(), _workOrder.getWorkOrderId());
        }
    };


    private final OnClickListener _reportProblem_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.REPORT_PROBLEM, null, _workOrder.getWorkOrderId());
            ReportProblemDialog.show(App.get(), DIALOG_REPORT_PROBLEM, _workOrder.getWorkOrderId());
        }
    };

    private final ReportProblemDialog.OnSendListener _reportProblemDialog_onSend = new ReportProblemDialog.OnSendListener() {
        @Override
        public void onSend(long workorderId, String explanation, ReportProblemType type) {
            if (_workOrder.getWorkOrderId() == workorderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.REPORT_PROBLEM, WorkOrderTracker.Action.REPORT_PROBLEM, (int) workorderId);
        }
    };


/*
    private final OnClickListener _phone_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.CALL_BUYER, null, _workOrder.getWorkOrderId());
            try {
                Contact contact = _workOrder.getContacts()[0];
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(callIntent);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };
*/

    private final OnClickListener _withdraw_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.WITHDRAW, null, _workOrder.getWorkOrderId());
            WithdrawRequestDialog.show(App.get(), DIALOG_WITHDRAW_REQUEST, _workOrder.getWorkOrderId());
        }
    };

    private final WithdrawRequestDialog.OnWithdrawListener _withdrawRequestDialog_onWithdraw = new WithdrawRequestDialog.OnWithdrawListener() {
        @Override
        public void onWithdraw(long workOrderId) {
            if (_workOrder.getWorkOrderId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.WITHDRAW, WorkOrderTracker.Action.WITHDRAW, (int) workOrderId);
        }
    };

    private final OnClickListener _message_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.VIEW_MESSAGES, null, _workOrder.getWorkOrderId());
            WorkorderActivity.startNew(App.get(), _workOrder.getWorkOrderId(), WorkorderActivity.TAB_MESSAGE);
        }
    };

/*
    private final OnClickListener _runningLate_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.RUNNING_LATE, null, _workOrder.getWorkOrderId());
            RunningLateDialog.show(App.get(), DIALOG_RUNNING_LATE, _workOrder);
        }
    };
*/

    private final RunningLateDialog.OnSendListener _runningLateDialog_onSend = new RunningLateDialog.OnSendListener() {
        @Override
        public void onSend(long workOrderId) {
            if (_workOrder.getWorkOrderId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.RUNNING_LATE, WorkOrderTracker.Action.RUNNING_LATE, (int) workOrderId);
        }
    };

    private final OnClickListener _map_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), _savedSearchTitle + " Saved Search", WorkOrderTracker.ActionButton.DIRECTIONS, null, _workOrder.getWorkOrderId());
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
            WorkordersWebApi.getAssignee(App.get(), _workOrder.getWorkOrderId(), false);
        }
    };


    private final OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityResultClient.startActivity(
                    App.get(),
                    WorkorderActivity.makeIntentShow(App.get(), _workOrder.getWorkOrderId()),
                    R.anim.activity_slide_in_right,
                    R.anim.activity_slide_out_left);
        }
    };
}