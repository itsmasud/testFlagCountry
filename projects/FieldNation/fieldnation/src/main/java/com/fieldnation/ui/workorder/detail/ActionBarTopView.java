package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderSubstatus;
import com.fieldnation.utils.misc;

public class ActionBarTopView extends RelativeLayout {
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
        LayoutInflater.from(getContext()).inflate(R.layout.view_action_bar_top, this);

        if (isInEditMode())
            return;

        _leftWhiteButton = (Button) findViewById(R.id.leftWhite_button);
        _leftGreenButton = (Button) findViewById(R.id.leftGreen_button);
        _leftOrangeButton = (Button) findViewById(R.id.leftOrange_button);
        _leftGrayButton = (Button) findViewById(R.id.leftGray_button);
        _rightWhiteButton = (Button) findViewById(R.id.rightWhite_button);
        _rightGreenButton = (Button) findViewById(R.id.rightGreen_button);
        _rightOrangeButton = (Button) findViewById(R.id.rightOrange_button);
        _rightGrayButton = (Button) findViewById(R.id.rightGray_button);

        setVisibility(View.GONE);
    }

    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;

        _leftWhiteButton.setVisibility(View.GONE);
        _leftGreenButton.setVisibility(View.GONE);
        _leftOrangeButton.setVisibility(View.GONE);
        _leftGrayButton.setVisibility(View.GONE);
        _rightWhiteButton.setVisibility(View.GONE);
        _rightGreenButton.setVisibility(View.GONE);
        _rightOrangeButton.setVisibility(View.GONE);
        _rightGrayButton.setVisibility(View.GONE);
        setVisibility(View.GONE);

        WorkorderSubstatus substatus = _workorder.getWorkorderSubstatus();

        switch (substatus) {
            case AVAILABLE:
                // not interested
                // request
                _leftWhiteButton.setVisibility(VISIBLE);
                _leftWhiteButton.setText(R.string.btn_not_interested);
                _leftWhiteButton.setOnClickListener(_notInterested_onClick);
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText(R.string.btn_request);
                _rightWhiteButton.setOnClickListener(_request_onClick);
                setVisibility(View.VISIBLE);
                break;
            case ROUTED:
                // not interested, accept work
                _leftWhiteButton.setVisibility(VISIBLE);
                _leftWhiteButton.setText(R.string.btn_not_interested);
                _leftWhiteButton.setOnClickListener(_notInterested_onClick);
                _rightOrangeButton.setVisibility(VISIBLE);
                _rightOrangeButton.setText(R.string.btn_accept);
                _rightOrangeButton.setOnClickListener(_confirmAssignment_onClick);
                setVisibility(View.VISIBLE);
                break;
            case COUNTEROFFERED:
            case REQUESTED:
                // withdraw/withdraw request
                _leftGrayButton.setVisibility(VISIBLE);
                _leftGrayButton.setText(R.string.btn_withdraw_request);
                _leftGrayButton.setOnClickListener(_withdraw_onClick);

                // if provider has countered then View Counter
                if (_workorder.getIsCounter()) {
                    _rightWhiteButton.setVisibility(VISIBLE);
                    _rightWhiteButton.setText(R.string.btn_view_counter);
                    _rightWhiteButton.setOnClickListener(_viewCounter_onClick);
                }
                setVisibility(View.VISIBLE);
                break;
            case CONFIRMED:
                // Ready-To-Go if needed
                if (_workorder.getNeedsReadyToGo()) {
                    _rightGreenButton.setVisibility(VISIBLE);
                    _rightGreenButton.setText(R.string.btn_ready_to_go);
                    _rightGreenButton.setOnClickListener(_readyToGo_onClick);
                } else {
                    _leftWhiteButton.setVisibility(VISIBLE);
                    _leftWhiteButton.setText(R.string.btn_check_in);
                    _leftWhiteButton.setOnClickListener(_checkin_onClick);
                }
                setVisibility(View.VISIBLE);
                break;
            case UNCONFIRMED:
                // Confirm
                _rightOrangeButton.setVisibility(VISIBLE);
                _rightOrangeButton.setText(R.string.btn_confirm);
                _rightOrangeButton.setOnClickListener(_confirm_onClick);
                setVisibility(View.VISIBLE);
                break;
            case CHECKEDOUT:
                // check in, or check in again
                _leftWhiteButton.setVisibility(VISIBLE);
                if (_workorder.getIsWorkPerformed()) {
                    _leftWhiteButton.setText(R.string.btn_check_in_again);
                } else {
                    _leftWhiteButton.setText(R.string.btn_check_in);
                }
                _leftWhiteButton.setOnClickListener(_checkin_onClick);

                // if everything is done except closing notes then closing notes
                if (_workorder.canComplete()) {
//                    _rightOrangeButton.setVisibility(VISIBLE);
//                    _rightOrangeButton.setText(R.string.btn_mark_completed);
//                    _rightOrangeButton.setOnClickListener(_markComplete_onClick);

                    // else if everything is done, Mark Complete
                } else if (_workorder.areTasksComplete()
                        && misc.isEmptyOrNull(_workorder.getClosingNotes())
                        && _workorder.canChangeClosingNotes()) {
                    _rightWhiteButton.setVisibility(VISIBLE);
                    _rightWhiteButton.setText(R.string.btn_closing_notes);
                    _rightWhiteButton.setOnClickListener(_closing_onClick);
                }

                setVisibility(View.VISIBLE);
                break;
            case CHECKEDIN:
                // Check out
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText(R.string.btn_check_out);
                _rightWhiteButton.setOnClickListener(_checkout_onClick);
                setVisibility(View.VISIBLE);
                break;
            case ONHOLD_ACKNOWLEDGED:
                // nothing
                break;
            case ONHOLD_UNACKNOWLEDGED:
                // ack hold
                _rightOrangeButton.setVisibility(VISIBLE);
                _rightOrangeButton.setText(R.string.btn_acknowledge_hold);
                _rightOrangeButton.setOnClickListener(_acknowledge_onClick);
                setVisibility(View.VISIBLE);
                break;
            case PENDINGREVIEW: // marked completed
                // mark incomplete
//                _rightGrayButton.setVisibility(VISIBLE);
//                _rightGrayButton.setText(R.string.btn_mark_incomplete);
//                _rightGrayButton.setOnClickListener(_markIncomplete_onClick);
//                setVisibility(View.VISIBLE);
                break;
            case APPROVED_PROCESSINGPAYMENT:
            case INREVIEW:
                // nothing
                break;
            case PAID: // completed
                // view payment
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText(R.string.btn_payments);
                _rightWhiteButton.setOnClickListener(_viewPayment_onClick);
                setVisibility(View.VISIBLE);
                break;
            case CANCELED_LATEFEEPROCESSING:
            case CANCELED:
                // nothing
                break;
            case CANCELED_LATEFEEPAID:
                // view fee
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText(R.string.btn_fees);
                _rightWhiteButton.setOnClickListener(_viewPayment_onClick);
                setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final View.OnClickListener _notInterested_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onNotInterested();
            }
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

    private final View.OnClickListener _confirmAssignment_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onConfirmAssignment();
            }
        }
    };

    private final View.OnClickListener _withdraw_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onWithdraw();
            }
        }
    };

    private final View.OnClickListener _viewCounter_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onViewCounter();
            }
        }
    };

    private final View.OnClickListener _readyToGo_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onReadyToGo();
            }
        }
    };

    private final View.OnClickListener _markComplete_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onMarkComplete();
            }
        }
    };

    private final View.OnClickListener _closing_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onEnterClosingNotes();
            }
        }
    };
    private final View.OnClickListener _confirm_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onConfirm();
        }
    };
    private final View.OnClickListener _checkin_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onCheckIn();
        }
    };

    private final View.OnClickListener _checkout_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onCheckOut();
        }
    };

    private final View.OnClickListener _acknowledge_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onAcknowledgeHold();
        }
    };

    private final View.OnClickListener _markIncomplete_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onMarkIncomplete();
        }
    };

    private final View.OnClickListener _viewPayment_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
    }
}
