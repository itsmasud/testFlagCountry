package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.workorder.WorkorderBundleDetailActivity;
import com.fieldnation.v2.data.model.Request;
import com.fieldnation.v2.data.model.Requests;
import com.fieldnation.v2.data.model.Schedule;
import com.fieldnation.v2.data.model.TimeLogs;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.RunningLateDialog;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

import java.util.Set;

public class ActionBarTopView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "ActionBarTopView";

    // Dalog UIDs
    private static final String DIALOG_RUNNING_LATE = TAG + ".runningLateDialog";

    // Ui
    private Button _leftWhiteButton;
    private Button _leftGrayButton;
    private Button _leftGreenButton;
    private Button _leftOrangeButton;
    private Button _rightWhiteButton;
    private Button _rightGrayButton;
    private Button _rightGreenButton;
    private Button _rightOrangeButton;

    // Data
    private Listener _listener;
    private WorkOrder _workOrder;
    private boolean _inflated = false;

    public ActionBarTopView(Context context) {
        super(context);
        init();
    }

    public ActionBarTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ActionBarTopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (isInEditMode())
            return;

        RunningLateDialog.addOnSendListener(DIALOG_RUNNING_LATE, _runningLateDialog_onSend);

        setVisibility(View.GONE);
    }

    @Override
    protected void onDetachedFromWindow() {
        RunningLateDialog.removeOnSendListener(DIALOG_RUNNING_LATE, _runningLateDialog_onSend);

        super.onDetachedFromWindow();
    }

    private void inflate() {
        if (_inflated)
            return;

        LayoutInflater.from(getContext()).inflate(R.layout.view_action_bar_top, this);

        _leftWhiteButton = (Button) findViewById(R.id.leftWhite_button);
        _leftGreenButton = (Button) findViewById(R.id.leftGreen_button);
        _leftOrangeButton = (Button) findViewById(R.id.leftOrange_button);
        _leftGrayButton = (Button) findViewById(R.id.leftGray_button);
        _rightWhiteButton = (Button) findViewById(R.id.rightWhite_button);
        _rightGreenButton = (Button) findViewById(R.id.rightGreen_button);
        _rightOrangeButton = (Button) findViewById(R.id.rightOrange_button);
        _rightGrayButton = (Button) findViewById(R.id.rightGray_button);

        setVisibility(View.GONE);
        _inflated = true;
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;

        if (_inflated) {
            _leftWhiteButton.setVisibility(View.GONE);
            _leftGreenButton.setVisibility(View.GONE);
            _leftOrangeButton.setVisibility(View.GONE);
            _leftGrayButton.setVisibility(View.GONE);
            _rightWhiteButton.setVisibility(View.GONE);
            _rightGreenButton.setVisibility(View.GONE);
            _rightOrangeButton.setVisibility(View.GONE);
            _rightGrayButton.setVisibility(View.GONE);
        }
        setVisibility(View.GONE);
        populateButtons();
    }

    private void populateButtons() {
        Log.v(TAG, "populateButtons");

        Set<WorkOrder.ActionsEnum> workOrderActions = _workOrder.getActionsSet();
        Set<Schedule.ActionsEnum> scheduleActions = _workOrder.getSchedule().getActionsSet();
        Set<TimeLogs.ActionsEnum> timeLogsActions = _workOrder.getTimeLogs().getActionsSet();

        if (false) {

            // set eta
        } else if (scheduleActions.contains(Schedule.ActionsEnum.ETA)
                && _workOrder.getSchedule().getEta().getUser().getId() == 0) {
            inflate();
            _leftWhiteButton.setVisibility(VISIBLE);
            _leftWhiteButton.setText(R.string.btn_report_a_problem);
            _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

            _rightWhiteButton.setVisibility(VISIBLE);
            _rightWhiteButton.setOnClickListener(_eta_onClick);
            _rightWhiteButton.setText(R.string.btn_set_eta);
            setVisibility(View.VISIBLE);

            // ready (NCNS confirm)
        } else if (workOrderActions.contains(WorkOrder.ActionsEnum.CONFIRM)) {
            inflate();
            _leftWhiteButton.setVisibility(VISIBLE);
            _leftWhiteButton.setText(R.string.btn_report_a_problem);
            _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

            _rightWhiteButton.setVisibility(VISIBLE);
            _rightWhiteButton.setOnClickListener(_readyToGo_onClick);
            _rightWhiteButton.setText(R.string.btn_confirm);
            setVisibility(View.VISIBLE);

            // on my way
//            inflate();
//            _leftWhiteButton.setVisibility(VISIBLE);
//            _leftWhiteButton.setText(R.string.btn_report_a_problem);
//            _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

//            _rightWhiteButton.setVisibility(VISIBLE);
//            _rightWhiteButton.setOnClickListener(_onMyWay_onClick);
//            _rightWhiteButton.setText(R.string.btn_on_my_way);
//            setVisibility(View.VISIBLE);

            // ack hold/
//            inflate();
//            _leftWhiteButton.setVisibility(VISIBLE);
//            _leftWhiteButton.setText(R.string.btn_report_a_problem);
//            _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

//            _rightWhiteButton.setVisibility(VISIBLE);
//            _rightWhiteButton.setOnClickListener(_ackHold_onClick);
//            _rightWhiteButton.setText(R.string.btn_acknowledge_hold);
//            setVisibility(View.VISIBLE);

            // check_out
        } else if (timeLogsActions.contains(TimeLogs.ActionsEnum.EDIT)
                && _workOrder.getTimeLogs().getOpenTimeLog() != null) {
            inflate();
            _leftWhiteButton.setVisibility(VISIBLE);
            _leftWhiteButton.setText(R.string.btn_report_a_problem);
            _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

            _rightWhiteButton.setVisibility(VISIBLE);
            _rightWhiteButton.setOnClickListener(_checkout_onClick);
            _rightWhiteButton.setText(R.string.btn_check_out);
            setVisibility(View.VISIBLE);

            // check_in
        } else if (timeLogsActions.contains(TimeLogs.ActionsEnum.ADD)) {
            inflate();
            _leftWhiteButton.setVisibility(VISIBLE);
            _leftWhiteButton.setText(R.string.btn_report_a_problem);
            _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

            _rightWhiteButton.setVisibility(VISIBLE);
            _rightWhiteButton.setOnClickListener(_checkin_onClick);
            _rightWhiteButton.setText(R.string.btn_check_in);
            setVisibility(View.VISIBLE);

            // mark incomplete
        } else if (workOrderActions.contains(WorkOrder.ActionsEnum.MARK_INCOMPLETE)) {
            inflate();
            _leftWhiteButton.setVisibility(VISIBLE);
            _leftWhiteButton.setText(R.string.btn_report_a_problem);
            _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

            _rightWhiteButton.setVisibility(VISIBLE);
            _rightWhiteButton.setOnClickListener(_markIncomplete_onClick);
            _rightWhiteButton.setText(R.string.btn_incomplete);
            setVisibility(View.VISIBLE);

            // view_bundle
        } else if (_workOrder.getBundle() != null
                && _workOrder.getBundle().getId() != null
                && _workOrder.getBundle().getId() > 0) {
            inflate();
            _rightWhiteButton.setVisibility(VISIBLE);
            _rightWhiteButton.setOnClickListener(_viewBundle_onClick);
            _rightWhiteButton.setText(getResources().getString(R.string.btn_view_bundle_num,
                    _workOrder.getBundle().getMetadata().getTotal()));
            setVisibility(View.VISIBLE);

            // accept
//            inflate();
//            _leftWhiteButton.setVisibility(VISIBLE);
//            _leftWhiteButton.setOnClickListener(_notInterested_onClick);
//            _leftWhiteButton.setText(R.string.btn_not_interested);

//            _rightWhiteButton.setVisibility(VISIBLE);
//            _rightWhiteButton.setOnClickListener(_accept_onClick);
//            _rightWhiteButton.setText(R.string.btn_accept);
//            setVisibility(View.VISIBLE);

            // request
        } else if (_workOrder.getRequests() != null
                && _workOrder.getRequests().getActionsSet().contains(Requests.ActionsEnum.ADD)) {
            inflate();
            _leftWhiteButton.setVisibility(VISIBLE);
            _leftWhiteButton.setOnClickListener(_notInterested_onClick);
            _leftWhiteButton.setText(R.string.btn_not_interested);

            _rightWhiteButton.setVisibility(VISIBLE);
            _rightWhiteButton.setOnClickListener(_request_onClick);
            _rightWhiteButton.setText(R.string.btn_request);
            setVisibility(View.VISIBLE);

            // withdraw
        } else if (_workOrder.getRequests() != null
                && _workOrder.getRequests().getOpenRequest() != null
                && _workOrder.getRequests().getOpenRequest().getActionsSet().contains(Request.ActionsEnum.REMOVE)) {
            inflate();
            _leftWhiteButton.setVisibility(VISIBLE);
            _leftWhiteButton.setOnClickListener(_withdraw_onClick);
            _leftWhiteButton.setText(R.string.btn_withdraw);

            if (_workOrder.getRequests() != null && _workOrder.getRequests().getCounterOffer() != null) {
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText(R.string.btn_view_counter);
                _rightWhiteButton.setOnClickListener(_viewCounter_onClick);
            }
            setVisibility(View.VISIBLE);

//        } else if (workOrderActions.contains(WorkOrder.ActionsEnum.MARK_COMPLETE)) {
//            inflate();
//            _leftWhiteButton.setVisibility(VISIBLE);
//            _leftWhiteButton.setText(R.string.btn_report_a_problem);
//            _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

//            _rightWhiteButton.setVisibility(VISIBLE);
//            _rightWhiteButton.setOnClickListener(_complete_onClick);
//            _rightWhiteButton.setText(R.string.btn_complete);
//            setVisibility(View.VISIBLE);

//              View payments
//            inflate();
//            _leftWhiteButton.setVisibility(VISIBLE);
//            _leftWhiteButton.setText(R.string.btn_report_a_problem);
//            _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

//            _rightWhiteButton.setVisibility(VISIBLE);
//            _rightWhiteButton.setText(R.string.btn_payments);
//            _rightWhiteButton.setOnClickListener(_viewPayment_onClick);
//            setVisibility(View.VISIBLE);

//              View fees
//            inflate();
//            _leftWhiteButton.setVisibility(VISIBLE);
//            _leftWhiteButton.setText(R.string.btn_report_a_problem);
//            _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

//            _rightWhiteButton.setVisibility(VISIBLE);
//            _rightWhiteButton.setText(R.string.btn_fees);
//            _rightWhiteButton.setOnClickListener(_viewPayment_onClick);
//            setVisibility(View.VISIBLE);

        } else if (timeLogsActions.contains(TimeLogs.ActionsEnum.ADD)
                && workOrderActions.contains(WorkOrder.ActionsEnum.CLOSING_NOTES)) {
            inflate();
            // TODO figure out the check in again logic
            _leftWhiteButton.setVisibility(VISIBLE);
            _leftWhiteButton.setText(R.string.btn_check_in);
            _leftWhiteButton.setOnClickListener(_checkin_onClick);

            _rightWhiteButton.setVisibility(VISIBLE);
            _rightWhiteButton.setOnClickListener(_closing_onClick);
            _rightWhiteButton.setText(R.string.btn_closing_notes);
            setVisibility(View.VISIBLE);
        }
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final View.OnClickListener _reportProblem_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onReportProblem();
            }
        }
    };

    private final View.OnClickListener _notInterested_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onNotInterested();
            }
        }
    };

    private final View.OnClickListener _onMyWay_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onMyWay();
        }
    };

    private final View.OnClickListener _viewBundle_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.VIEW_BUNDLE,
                    null, _workOrder.getWorkOrderId().intValue());

            WorkorderBundleDetailActivity.startNew(App.get(), _workOrder.getWorkOrderId(), _workOrder.getBundle().getId());
        }
    };

    private final View.OnClickListener _request_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onRequest();
            }
        }
    };

    private final View.OnClickListener _runningLate_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.RUNNING_LATE,
                    null, _workOrder.getWorkOrderId());

            RunningLateDialog.show(App.get(), DIALOG_RUNNING_LATE, _workOrder);
        }
    };

    private final RunningLateDialog.OnSendListener _runningLateDialog_onSend = new RunningLateDialog.OnSendListener() {
        @Override
        public void onSend(long workOrderId) {
            if (_workOrder.getWorkOrderId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.RUNNING_LATE,
                        WorkOrderTracker.Action.RUNNING_LATE, (int) workOrderId);
        }
    };

    private final OnClickListener _eta_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) _listener.onEta();
        }
    };

    private final View.OnClickListener _confirmAssignment_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) _listener.onConfirmAssignment();
        }
    };

    private final View.OnClickListener _withdraw_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) _listener.onWithdraw();
        }
    };

    private final View.OnClickListener _viewCounter_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) _listener.onViewCounter();
        }
    };

    private final View.OnClickListener _readyToGo_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) _listener.onReadyToGo();
        }
    };

    private final View.OnClickListener _markComplete_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) _listener.onMarkComplete();
        }
    };

    private final View.OnClickListener _closing_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) _listener.onEnterClosingNotes();
        }
    };

    private final View.OnClickListener _confirm_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) _listener.onConfirm();
        }
    };

    private final View.OnClickListener _checkin_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) _listener.onCheckIn();
        }
    };

    private final View.OnClickListener _checkinAgain_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) _listener.onCheckInAgain();
        }
    };

    private final View.OnClickListener _checkout_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) _listener.onCheckOut();
        }
    };

    private final View.OnClickListener _acknowledge_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) _listener.onAcknowledgeHold();
        }
    };

    private final View.OnClickListener _markIncomplete_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) _listener.onMarkIncomplete();
        }
    };

    private final View.OnClickListener _viewPayment_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) _listener.onViewPayment();
        }
    };

    public interface Listener {
        void onNotInterested();

        void onRequest();

        void onConfirmAssignment();

        void onWithdraw();

        void onViewCounter();

        void onReadyToGo();

        void onConfirm();

        void onCheckIn();

        void onCheckInAgain();

        void onEnterClosingNotes();

        void onMarkComplete();

        void onCheckOut();

        void onAcknowledgeHold();

        void onMarkIncomplete();

        void onViewPayment();

        void onReportProblem();

        void onMyWay();

        void onEta();
    }
}