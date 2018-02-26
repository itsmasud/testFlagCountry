package com.fieldnation.v2.ui.workorder;

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
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.ui.workorder.BundleDetailActivity;
import com.fieldnation.v2.data.model.Bundle;
import com.fieldnation.v2.data.model.ETA;
import com.fieldnation.v2.data.model.Request;
import com.fieldnation.v2.data.model.Requests;
import com.fieldnation.v2.data.model.Route;
import com.fieldnation.v2.data.model.TimeLog;
import com.fieldnation.v2.data.model.TimeLogs;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.RunningLateDialog;
import com.fieldnation.v2.ui.dialog.TwoButtonDialog;

import java.util.Set;

public class ActionBarTopView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "ActionBarTopView";

    // Dalog UIDs
    private static final String DIALOG_RUNNING_LATE = TAG + ".runningLateDialog";

    // Ui
    private Button _rightGreenButton;

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

        _rightGreenButton = findViewById(R.id.rightGreen_button);

        setVisibility(View.GONE);
        _inflated = true;
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;

        if (_inflated) {
            _rightGreenButton.setVisibility(View.GONE);
        }
        setVisibility(View.GONE);
        populateButtons();
    }

    private void populateButtons() {
        //Log.v(TAG, "populateButtons");
        Set<WorkOrder.ActionsEnum> workOrderActions = _workOrder.getActionsSet();
        Set<TimeLogs.ActionsEnum> timeLogsActions = _workOrder.getTimeLogs().getActionsSet();

        if (false) {

            // ack hold
        } else if (_workOrder.getHolds().isOnHold()) {
            inflate();

            _rightGreenButton.setVisibility(VISIBLE);
            _rightGreenButton.setOnClickListener(_acknowledge_onClick);
            if (_workOrder.getHolds().areHoldsAcknowledged()) {
                _rightGreenButton.setText(R.string.btn_on_hold);
                _rightGreenButton.setEnabled(false);
//                _rightGreenButton.setBackgroundColor(R.drawable.btn_bg_gray);
            } else {
                _rightGreenButton.setText(R.string.btn_review_hold);
                _rightGreenButton.setEnabled(true);
            }
            setVisibility(View.VISIBLE);
            return;

        } else if (_workOrder.getEta().getActionsSet().contains(ETA.ActionsEnum.ADD)) {
            inflate();
            _rightGreenButton.setVisibility(VISIBLE);
            _rightGreenButton.setOnClickListener(_eta_onClick);
            _rightGreenButton.setText(R.string.btn_set_eta);
            setVisibility(View.VISIBLE);
            _rightGreenButton.setEnabled(true);

        } else if (_workOrder.getEta().getActionsSet().contains(ETA.ActionsEnum.MARK_READY_TO_GO)) {
            inflate();
            _rightGreenButton.setVisibility(VISIBLE);
            _rightGreenButton.setOnClickListener(_readyToGo_onClick);
            _rightGreenButton.setText(R.string.btn_ready_to_go);
            setVisibility(View.VISIBLE);
            _rightGreenButton.setEnabled(true);

            // confirm
        } else if (_workOrder.getEta().getActionsSet().contains(ETA.ActionsEnum.CONFIRM)) {
            inflate();
            _rightGreenButton.setVisibility(VISIBLE);
            _rightGreenButton.setOnClickListener(_confirm_onClick);
            _rightGreenButton.setText(R.string.btn_confirm);
            setVisibility(View.VISIBLE);
            _rightGreenButton.setEnabled(true);

            // on my way
        } else if (_workOrder.getEta().getActionsSet().contains(ETA.ActionsEnum.ON_MY_WAY)) {
            inflate();
            _rightGreenButton.setVisibility(VISIBLE);
            _rightGreenButton.setOnClickListener(_onMyWay_onClick);
            _rightGreenButton.setText(R.string.btn_on_my_way);
            setVisibility(View.VISIBLE);
            _rightGreenButton.setEnabled(true);

            // check_out
        } else if (_workOrder.getTimeLogs().getOpenTimeLog().getActionsSet().contains(TimeLog.ActionsEnum.EDIT)) {
            inflate();
            _rightGreenButton.setVisibility(VISIBLE);
            _rightGreenButton.setOnClickListener(_checkout_onClick);
            _rightGreenButton.setText(R.string.btn_check_out);
            setVisibility(View.VISIBLE);
            _rightGreenButton.setEnabled(true);

            // Mark complete
        } else if (workOrderActions.contains(WorkOrder.ActionsEnum.COMPLETE)) {
            inflate();
            _rightGreenButton.setVisibility(VISIBLE);
            _rightGreenButton.setOnClickListener(_markComplete_onClick);
            _rightGreenButton.setText(R.string.btn_complete);
            setVisibility(View.VISIBLE);
            _rightGreenButton.setEnabled(true);

            // check_in
        } else if (timeLogsActions.contains(TimeLogs.ActionsEnum.ADD)) {
            inflate();
            _rightGreenButton.setVisibility(VISIBLE);
            if (_workOrder.getTimeLogs().getMetadata().getTotal() > 1) {
                _rightGreenButton.setText(R.string.btn_check_in_again);
                _rightGreenButton.setOnClickListener(_checkinAgain_onClick);
                _rightGreenButton.setEnabled(true);
            } else {
                _rightGreenButton.setText(R.string.btn_check_in);
                _rightGreenButton.setOnClickListener(_checkin_onClick);
                _rightGreenButton.setEnabled(true);
            }
            setVisibility(View.VISIBLE);

            // mark incomplete
        } else if (workOrderActions.contains(WorkOrder.ActionsEnum.INCOMPLETE)) {
            inflate();
            _rightGreenButton.setVisibility(VISIBLE);
            _rightGreenButton.setOnClickListener(_markIncomplete_onClick);
            _rightGreenButton.setText(R.string.btn_incomplete);
            setVisibility(View.VISIBLE);
            _rightGreenButton.setEnabled(true);

            // view_bundle
        } else if (_workOrder.getBundle().getActionsSet().contains(Bundle.ActionsEnum.VIEW)) {
            inflate();
            _rightGreenButton.setVisibility(VISIBLE);
            _rightGreenButton.setOnClickListener(_viewBundle_onClick);
            _rightGreenButton.setText(getResources().getString(R.string.btn_view_bundle_num,
                    _workOrder.getBundle().getMetadata().getTotal() + 1));
            setVisibility(View.VISIBLE);
            _rightGreenButton.setEnabled(true);

            // accept
        } else if (_workOrder.getRoutes().getUserRoute().getActionsSet().contains(Route.ActionsEnum.ACCEPT)) {
            inflate();
/*
            _leftGreenButton.setVisibility(VISIBLE);
            _leftGreenButton.setOnClickListener(_notInterested_onClick);
            _leftGreenButton.setText(R.string.btn_not_interested);
*/

            _rightGreenButton.setVisibility(VISIBLE);
            _rightGreenButton.setOnClickListener(_accept_onClick);
            _rightGreenButton.setText(R.string.btn_accept);
            setVisibility(View.VISIBLE);
            _rightGreenButton.setEnabled(true);

            // request
        } else if (_workOrder.getRequests().getActionsSet().contains(Requests.ActionsEnum.ADD)) {
            inflate();
/*
            _leftGreenButton.setVisibility(VISIBLE);
            _leftGreenButton.setOnClickListener(_notInterested_onClick);
            _leftGreenButton.setText(R.string.btn_not_interested);
*/

            _rightGreenButton.setVisibility(VISIBLE);
            _rightGreenButton.setOnClickListener(_request_onClick);
            _rightGreenButton.setText(R.string.btn_request);
            setVisibility(View.VISIBLE);
            _rightGreenButton.setEnabled(true);

            // withdraw
        } else if (_workOrder.getRequests().getOpenRequest().getActionsSet().contains(Request.ActionsEnum.DELETE)) {
            inflate();

            _rightGreenButton.setVisibility(VISIBLE);
            _rightGreenButton.setOnClickListener(_withdraw_onClick);
            _rightGreenButton.setText(R.string.btn_withdraw);

            setVisibility(View.VISIBLE);

            // View payments
        } else if (_workOrder.getStatus().getId() == 6) {
            inflate();
            _rightGreenButton.setVisibility(VISIBLE);
            _rightGreenButton.setText(R.string.btn_payments);
            _rightGreenButton.setOnClickListener(_viewPayment_onClick);
            setVisibility(View.VISIBLE);
            _rightGreenButton.setEnabled(true);

            // View fees
        } else if (_workOrder.getStatus().getId() == 7) {
            inflate();
            _rightGreenButton.setVisibility(VISIBLE);
            _rightGreenButton.setText(R.string.btn_fees);
            _rightGreenButton.setOnClickListener(_viewPayment_onClick);
            setVisibility(View.VISIBLE);
            _rightGreenButton.setEnabled(true);

        } else if (timeLogsActions.contains(TimeLogs.ActionsEnum.ADD)
                && workOrderActions.contains(WorkOrder.ActionsEnum.CLOSING_NOTES)) {
            inflate();
/*
            _leftGreenButton.setVisibility(VISIBLE);
            if (_workOrder.getTimeLogs().getMetadata().getTotal() > 1) {
                _leftGreenButton.setText(R.string.btn_check_in_again);
                _leftGreenButton.setOnClickListener(_checkinAgain_onClick);
            } else {
                _leftGreenButton.setText(R.string.btn_check_in);
                _leftGreenButton.setOnClickListener(_checkin_onClick);
            }
*/

            _rightGreenButton.setVisibility(VISIBLE);
            _rightGreenButton.setOnClickListener(_closing_onClick);
            _rightGreenButton.setText(R.string.btn_closing_notes);
            setVisibility(View.VISIBLE);
            _rightGreenButton.setEnabled(true);
        }

        if (App.get().getOfflineState() == App.OfflineState.SYNC
                || App.get().getOfflineState() == App.OfflineState.OFFLINE
                || App.get().getOfflineState() == App.OfflineState.UPLOADING) {
            _rightGreenButton.setEnabled(true);
            _rightGreenButton.setOnClickListener(_disable_onClick);
            _rightGreenButton.setTextColor(getResources().getColor(R.color.fn_dark_text));
            _rightGreenButton.setBackgroundDrawable(_rightGreenButton.getResources().getDrawable(R.drawable.btn_bg_gray_normal));
        }
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final View.OnClickListener _reportProblem_onClick = new View.OnClickListener() {
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

    private final View.OnClickListener _onMyWay_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onMyWay();
        }
    };

    private final View.OnClickListener _viewBundle_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.VIEW_BUNDLE,
                    null, _workOrder.getId());

            BundleDetailActivity.startNew(App.get(), _workOrder.getBundle().getId());
        }
    };

    private final View.OnClickListener _request_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onRequest();
        }
    };

    private final View.OnClickListener _runningLate_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.RUNNING_LATE,
                    null, _workOrder.getId());

            RunningLateDialog.show(App.get(), DIALOG_RUNNING_LATE, _workOrder.getId(), _workOrder.getEta(),
                    _workOrder.getSchedule(), _workOrder.getContacts(), _workOrder.getTitle());
        }
    };

    private final RunningLateDialog.OnSendListener _runningLateDialog_onSend = new RunningLateDialog.OnSendListener() {
        @Override
        public void onSend(int workOrderId) {
            if (_workOrder.getId() == workOrderId)
                WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.RUNNING_LATE,
                        WorkOrderTracker.Action.RUNNING_LATE, workOrderId);
        }
    };

    private final OnClickListener _eta_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            if (_listener != null) _listener.onEta();
        }
    };

    private final View.OnClickListener _accept_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            if (_listener != null) _listener.onAccept();
        }
    };

    private final View.OnClickListener _withdraw_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) _listener.onWithdraw();
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

    private final View.OnClickListener _checkin_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            if (_listener != null) _listener.onCheckIn();
        }
    };

    private final View.OnClickListener _checkinAgain_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            if (_listener != null) _listener.onCheckInAgain();
        }
    };

    private final View.OnClickListener _checkout_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
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

    private final View.OnClickListener _viewPayment_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            if (_listener != null) _listener.onViewPayment();
        }
    };

    private final View.OnClickListener _disable_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TwoButtonDialog.show(App.get(), null, getResources().getString(R.string.not_available),
                    getResources().getString(R.string.not_available_body_text),
                    getResources().getString(R.string.btn_close), null, true, null);
        }
    };


    public interface Listener {
        void onNotInterested();

        void onRequest();

        void onWithdraw();

        void onAccept();

        void onConfirm();

        void onEta();

        void onReadyToGo();

        void onMyWay();

        void onReportProblem();

        void onAcknowledgeHold();

        void onCheckIn();

        void onCheckInAgain();

        void onCheckOut();

        void onEnterClosingNotes();

        void onMarkComplete();

        void onMarkIncomplete();

        void onViewPayment();
    }
}