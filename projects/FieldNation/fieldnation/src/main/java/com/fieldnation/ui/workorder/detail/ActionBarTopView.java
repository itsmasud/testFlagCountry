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
import com.fieldnation.data.v2.actions.Action;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderSubstatus;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.dialog.v2.RunningLateDialogLegacy;
import com.fieldnation.ui.workorder.WorkorderBundleDetailActivity;

public class ActionBarTopView extends LinearLayout {
    private static final String TAG = "ActionBarTopView";

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
    private Workorder _workorder;
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

        setVisibility(View.GONE);
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

    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;

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
        if (App.isNcns()) {
            populateButtonsNCNS();
        } else {
            populateButtons();
        }
    }

    private void populateButtons() {
        Log.v(TAG, "populateButtons");
        WorkorderSubstatus substatus = _workorder.getWorkorderSubstatus();

        switch (substatus) {
            case AVAILABLE:
                inflate();
                if (_workorder.isBundle()) {
                    _rightWhiteButton.setVisibility(VISIBLE);
                    _rightWhiteButton.setText(R.string.btn_view_bundle);
                    _rightWhiteButton.setOnClickListener(_viewBundle_onClick);
                } else {
                    // not interested, request
                    _leftWhiteButton.setVisibility(VISIBLE);
                    _leftWhiteButton.setText(R.string.btn_not_interested);
                    _leftWhiteButton.setOnClickListener(_notInterested_onClick);
                    _rightWhiteButton.setVisibility(VISIBLE);
                    _rightWhiteButton.setText(R.string.btn_request);
                    _rightWhiteButton.setOnClickListener(_request_onClick);
                }
                setVisibility(View.VISIBLE);
                break;
            case ROUTED:
                inflate();
                if (_workorder.isBundle()) {
                    _rightWhiteButton.setVisibility(VISIBLE);
                    _rightWhiteButton.setText(R.string.btn_view_bundle);
                    _rightWhiteButton.setOnClickListener(_viewBundle_onClick);
                } else {
                    // not interested, accept work
                    _leftWhiteButton.setVisibility(VISIBLE);
                    _leftWhiteButton.setText(R.string.btn_not_interested);
                    _leftWhiteButton.setOnClickListener(_notInterested_onClick);
                    _rightOrangeButton.setVisibility(VISIBLE);
                    _rightOrangeButton.setText(R.string.btn_accept);
                    _rightOrangeButton.setOnClickListener(_confirmAssignment_onClick);
                }
                setVisibility(View.VISIBLE);
                break;
            case COUNTEROFFERED:
            case REQUESTED:
                inflate();
                // withdraw/withdraw request
                _leftWhiteButton.setVisibility(VISIBLE);
                _leftWhiteButton.setText(R.string.btn_withdraw_request);
                _leftWhiteButton.setOnClickListener(_withdraw_onClick);

                // if provider has countered then View Counter
                if (_workorder.getIsCounter()) {
                    _rightWhiteButton.setVisibility(VISIBLE);
                    _rightWhiteButton.setText(R.string.btn_view_counter);
                    _rightWhiteButton.setOnClickListener(_viewCounter_onClick);
                }
                setVisibility(View.VISIBLE);
                break;
            case CONFIRMED:
                inflate();
                // Ready-To-Go if needed
                if (_workorder.getNeedsReadyToGo()) {
                    _leftWhiteButton.setVisibility(VISIBLE);
                    _leftWhiteButton.setText(R.string.btn_report_a_problem);
                    _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

                    _rightGreenButton.setVisibility(VISIBLE);
                    _rightGreenButton.setText(R.string.btn_ready_to_go);
                    _rightGreenButton.setOnClickListener(_readyToGo_onClick);
                } else {
                    _leftWhiteButton.setVisibility(VISIBLE);
                    _leftWhiteButton.setText(R.string.btn_report_a_problem);
                    _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

                    _rightWhiteButton.setVisibility(VISIBLE);
                    _rightWhiteButton.setText(R.string.btn_check_in);
                    _rightWhiteButton.setOnClickListener(_checkin_onClick);
                }
                setVisibility(View.VISIBLE);
                break;
            case UNCONFIRMED:
                inflate();
                // Confirm
                _leftWhiteButton.setVisibility(VISIBLE);
                _leftWhiteButton.setText(R.string.btn_report_a_problem);
                _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

                _rightOrangeButton.setVisibility(VISIBLE);
                _rightOrangeButton.setText(R.string.btn_confirm);
                _rightOrangeButton.setOnClickListener(_confirm_onClick);
                setVisibility(View.VISIBLE);
                break;
            case CHECKEDOUT:
                inflate();
                // if everything is done except closing notes then closing notes
                if (_workorder.canComplete()) {
                    // check in, or check in again
                    _leftWhiteButton.setVisibility(VISIBLE);
                    if (_workorder.getIsWorkPerformed()) {
                        _leftWhiteButton.setText(R.string.btn_check_in_again);
                    } else {
                        _leftWhiteButton.setText(R.string.btn_check_in);
                    }
                    _leftWhiteButton.setOnClickListener(_checkin_onClick);

                    _rightOrangeButton.setVisibility(VISIBLE);
                    _rightOrangeButton.setText(R.string.btn_mark_completed);
                    _rightOrangeButton.setOnClickListener(_markComplete_onClick);

                    // else if everything is done, Mark Complete
                } else if (_workorder.areTasksComplete()
                        && misc.isEmptyOrNull(_workorder.getClosingNotes())
                        && _workorder.canChangeClosingNotes()) {
                    // check in, or check in again
                    _leftWhiteButton.setVisibility(VISIBLE);
                    if (_workorder.getIsWorkPerformed()) {
                        _leftWhiteButton.setText(R.string.btn_check_in_again);
                    } else {
                        _leftWhiteButton.setText(R.string.btn_check_in);
                    }
                    _leftWhiteButton.setOnClickListener(_checkin_onClick);

                    _rightOrangeButton.setVisibility(VISIBLE);
                    _rightOrangeButton.setText(R.string.btn_closing_notes);
                    _rightOrangeButton.setOnClickListener(_closing_onClick);
                } else {
                    _leftWhiteButton.setVisibility(VISIBLE);
                    _leftWhiteButton.setText(R.string.btn_report_a_problem);
                    _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

                    // check in, or check in again
                    _rightWhiteButton.setVisibility(VISIBLE);
                    if (_workorder.getIsWorkPerformed()) {
                        _rightWhiteButton.setText(R.string.btn_check_in_again);
                        _rightWhiteButton.setOnClickListener(_checkinAgain_onClick);
                    } else {
                        _rightWhiteButton.setText(R.string.btn_check_in);
                        _rightWhiteButton.setOnClickListener(_checkin_onClick);
                    }

                }

                setVisibility(View.VISIBLE);
                break;
            case CHECKEDIN:
                inflate();
                _leftWhiteButton.setVisibility(VISIBLE);
                _leftWhiteButton.setText(R.string.btn_report_a_problem);
                _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

                // Check out
                _rightGreenButton.setVisibility(VISIBLE);
                _rightGreenButton.setText(R.string.btn_check_out);
                _rightGreenButton.setOnClickListener(_checkout_onClick);
                setVisibility(View.VISIBLE);
                break;
            case ONHOLD_ACKNOWLEDGED:
                // nothing
                inflate();
                _leftWhiteButton.setVisibility(VISIBLE);
                _leftWhiteButton.setText(R.string.btn_report_a_problem);
                _leftWhiteButton.setOnClickListener(_reportProblem_onClick);
                setVisibility(View.VISIBLE);

                break;
            case ONHOLD_UNACKNOWLEDGED:
                inflate();
                _leftWhiteButton.setVisibility(VISIBLE);
                _leftWhiteButton.setText(R.string.btn_report_a_problem);
                _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

                // ack hold
                _rightOrangeButton.setVisibility(VISIBLE);
                _rightOrangeButton.setText(R.string.btn_acknowledge_hold);
                _rightOrangeButton.setOnClickListener(_acknowledge_onClick);
                setVisibility(View.VISIBLE);
                break;
            case PENDINGREVIEW: // marked completed
                inflate();
                _leftWhiteButton.setVisibility(VISIBLE);
                _leftWhiteButton.setText(R.string.btn_report_a_problem);
                _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

                // mark incomplete
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText(R.string.btn_mark_incomplete);
                _rightWhiteButton.setOnClickListener(_markIncomplete_onClick);
                setVisibility(View.VISIBLE);
                break;
            case APPROVED_PROCESSINGPAYMENT:
            case INREVIEW:
                // nothing
                inflate();
                _leftWhiteButton.setVisibility(VISIBLE);
                _leftWhiteButton.setText(R.string.btn_report_a_problem);
                _leftWhiteButton.setOnClickListener(_reportProblem_onClick);
                setVisibility(View.VISIBLE);
                break;
            case PAID: // completed
                inflate();
                if (_workorder.getPay() != null && _workorder.getPay().hidePay()) {
                    _rightWhiteButton.setVisibility(VISIBLE);
                    _rightWhiteButton.setText(R.string.btn_report_a_problem);
                    _rightWhiteButton.setOnClickListener(_reportProblem_onClick);
                } else {
                    _leftWhiteButton.setVisibility(VISIBLE);
                    _leftWhiteButton.setText(R.string.btn_report_a_problem);
                    _leftWhiteButton.setOnClickListener(_reportProblem_onClick);

                    // view payment
                    _rightWhiteButton.setVisibility(VISIBLE);
                    _rightWhiteButton.setText(R.string.btn_payments);
                    _rightWhiteButton.setOnClickListener(_viewPayment_onClick);
                    setVisibility(View.VISIBLE);
                }
                break;
            case CANCELED_LATEFEEPROCESSING:
            case CANCELED:
                // nothing
                break;
            case CANCELED_LATEFEEPAID:
                if (_workorder.getPay() != null && !_workorder.getPay().hidePay()) {
                    inflate();
                    // view fee
                    _rightWhiteButton.setVisibility(VISIBLE);
                    _rightWhiteButton.setText(R.string.btn_fees);
                    _rightWhiteButton.setOnClickListener(_viewPayment_onClick);
                    setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    private void populateButtonsNCNS() {
        Log.v(TAG, "populateButtonsNCNS");
        // Primary actions
        if (_workorder.getPrimaryActions() != null && _workorder.getPrimaryActions().length > 0) {

            Action[] actions = _workorder.getPrimaryActions();
            if (actions != null) {
                inflate();
                for (Action action : actions) {
                    if (populateButton(_rightWhiteButton, action)) {
                        setVisibility(VISIBLE);
                        break;
                    }
                }
            }
        }

        if (_workorder.getSecondaryActions() != null && _workorder.getSecondaryActions().length > 0) {
            Action[] actions = _workorder.getSecondaryActions();
            if (actions != null) {
                inflate();
                for (Action action : actions) {
                    if (populateButton(_leftWhiteButton, action)) {
                        setVisibility(VISIBLE);
                        break;
                    }
                }
            }
        }
    }

    private boolean populateButton(Button button, Action action) {
        switch (action.getType()) {
            case ACCEPT:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_confirmAssignment_onClick);
                button.setText("ACCEPT");
                break;
            case CONFIRM:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_confirm_onClick);
                button.setText("CONFIRM");
                break;
            case ON_MY_WAY:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_onMyWay_onClick);
                button.setText(R.string.btn_on_my_way);
                break;
            case READY:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_readyToGo_onClick);
                button.setText("CONFIRM");
                break;
            case READY_TO_GO:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_readyToGo_onClick);
                button.setText("READY");
                break;
            case REPORT_PROBLEM:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_reportProblem_onClick);
                button.setText(R.string.btn_report_problem);
                break;
            case REQUEST:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_request_onClick);
                button.setText("REQUEST");
                break;
            case VIEW_BUNDLE:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_viewBundle_onClick);
                button.setText("VIEW BUNDLE (" + _workorder.getBundleCount() + ")");
                break;
            case ACK_HOLD:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_acknowledge_onClick);
                button.setText("ACKNOWLEDGE HOLD");
                break;
            case WITHDRAW:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_withdraw_onClick);
                button.setText("WITHDRAW");
                break;
            case CHECK_IN:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_checkin_onClick);
                button.setText("CHECK IN");
                break;
            case CHECK_OUT:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_checkout_onClick);
                button.setText("CHECK OUT");
                break;
            case MARK_INCOMPLETE:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_markIncomplete_onClick);
                button.setText("MARK INCOMPLETE");
                break;

            case MARK_COMPLETE:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_markComplete_onClick);
                button.setText("MARK COMPLETE");
                break;
            case VIEW_PAYMENT:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_viewPayment_onClick);
                button.setText("VIEW PAYMENT");
                break;
//            case ACK_UPDATE:
//                break;
            case DECLINE:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_notInterested_onClick);
                button.setText(R.string.btn_not_interested);
                break;
//            case MAP:
//                break;
//            case MESSAGE:
//                break;
//            case NOT_SUPPORTED:
//                break;
//            case PHONE:
//                break;
            case RUNNING_LATE:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_runningLate_onClick);
                button.setText("RUNNING LATE");
                break;
//            case VIEW:
//                break;
            default:
                return false;
        }
        return true;
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
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.REPORT_PROBLEM, _workorder.getWorkorderId());
            if (_listener != null) {
                _listener.onReportProblem();
            }
        }
    };

    private final View.OnClickListener _notInterested_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.NOT_INTERESTED, _workorder.getWorkorderId());
            if (_listener != null) {
                _listener.onNotInterested();
            }
        }
    };

    private final View.OnClickListener _onMyWay_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.ON_MY_WAY, _workorder.getWorkorderId());
            if (_listener != null)
                _listener.onMyWay();
        }
    };

    private final View.OnClickListener _viewBundle_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.VIEW_BUNDLE, _workorder.getWorkorderId());
            WorkorderBundleDetailActivity.startNew(App.get(), _workorder.getWorkorderId(), _workorder.getBundleId());
        }
    };

    private final View.OnClickListener _request_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.REQUEST, _workorder.getWorkorderId());
            if (_listener != null) {
                _listener.onRequest();
            }
        }
    };

    private final View.OnClickListener _runningLate_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.RUNNING_LATE, _workorder.getWorkorderId());
            RunningLateDialogLegacy.Controller.show(App.get(), _workorder);
        }
    };

    private final View.OnClickListener _confirmAssignment_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CONFIRM, _workorder.getWorkorderId());
            if (_listener != null) {
                _listener.onConfirmAssignment();
            }
        }
    };

    private final View.OnClickListener _withdraw_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.WITHDRAW, _workorder.getWorkorderId());
            if (_listener != null) {
                _listener.onWithdraw();
            }
        }
    };

    private final View.OnClickListener _viewCounter_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.VIEW_COUNTER_OFFER, _workorder.getWorkorderId());
            if (_listener != null) {
                _listener.onViewCounter();
            }
        }
    };

    private final View.OnClickListener _readyToGo_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.READY_TO_GO, _workorder.getWorkorderId());
            if (_listener != null) {
                _listener.onReadyToGo();
            }
        }
    };

    private final View.OnClickListener _markComplete_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.MARK_COMPlETE, _workorder.getWorkorderId());
            if (_listener != null) {
                _listener.onMarkComplete();
            }
        }
    };

    private final View.OnClickListener _closing_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CLOSING_NOTES, _workorder.getWorkorderId());
            if (_listener != null) {
                _listener.onEnterClosingNotes();
            }
        }
    };

    private final View.OnClickListener _confirm_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CONFIRM, _workorder.getWorkorderId());
            if (_listener != null)
                _listener.onConfirm();
        }
    };

    private final View.OnClickListener _checkin_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CHECK_IN, _workorder.getWorkorderId());
            if (_listener != null)
                _listener.onCheckIn();
        }
    };

    private final View.OnClickListener _checkinAgain_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CHECK_IN_AGAIN, _workorder.getWorkorderId());
            if (_listener != null)
                _listener.onCheckIn();
        }
    };

    private final View.OnClickListener _checkout_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CHECK_OUT, _workorder.getWorkorderId());
            if (_listener != null)
                _listener.onCheckOut();
        }
    };

    private final View.OnClickListener _acknowledge_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.ACKNOWLEDGE_HOLD, _workorder.getWorkorderId());
            if (_listener != null)
                _listener.onAcknowledgeHold();
        }
    };

    private final View.OnClickListener _markIncomplete_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.MARK_INCOMPLETE, _workorder.getWorkorderId());
            if (_listener != null)
                _listener.onMarkIncomplete();
        }
    };

    private final View.OnClickListener _viewPayment_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.VIEW_PAYMENT, _workorder.getWorkorderId());
            if (_listener != null)
                _listener.onViewPayment();
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

        void onEnterClosingNotes();

        void onMarkComplete();

        void onCheckOut();

        void onAcknowledgeHold();

        void onMarkIncomplete();

        void onViewPayment();

        void onReportProblem();

        void onMyWay();
    }
}
