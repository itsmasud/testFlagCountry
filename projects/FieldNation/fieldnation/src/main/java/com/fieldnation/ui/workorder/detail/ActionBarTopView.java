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

public class ActionBarTopView extends RelativeLayout {
    private static final String TAG = "ActionBarTopView";

    // Ui
    private Button _leftWhiteButton;
    private Button _leftGreenButton;
    private Button _leftOrangeButton;
    private Button _rightWhiteButton;
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
        _rightWhiteButton = (Button) findViewById(R.id.rightWhite_button);
        _rightGreenButton = (Button) findViewById(R.id.rightGreen_button);
        _rightOrangeButton = (Button) findViewById(R.id.rightOrange_button);

        setVisibility(View.GONE);
    }

    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;

        _leftWhiteButton.setVisibility(View.GONE);
        _leftGreenButton.setVisibility(View.GONE);
        _leftOrangeButton.setVisibility(View.GONE);
        _rightWhiteButton.setVisibility(View.GONE);
        _rightGreenButton.setVisibility(View.GONE);
        _rightOrangeButton.setVisibility(View.GONE);
        setVisibility(View.GONE);

        WorkorderSubstatus substatus = _workorder.getWorkorderSubstatus();

        switch (substatus) {
            case AVAILABLE:
                // not interested
                // request
                setVisibility(View.VISIBLE);
                break;
            case ROUTED:
                // not interested, accept work
                setVisibility(View.VISIBLE);
                break;
            case REQUESTED:
                // withdraw/withdraw request
                // if provider has countered then View Counter
                setVisibility(View.VISIBLE);
                break;
            case CONFIRMED:
                // Ready-To-Go if needed
                setVisibility(View.VISIBLE);
                break;
            case UNCONFIRMED:
                // Confirm
                setVisibility(View.VISIBLE);
                break;
            case CHECKEDOUT:
                // check in, or check in again
                // if everything is done except closing notes then closing notes
                // else if everything is done, Mark Complete
                setVisibility(View.VISIBLE);
                break;
            case CHECKEDIN:
                // Check out
                setVisibility(View.VISIBLE);
                break;
            case ONHOLD_ACKNOWLEDGED:
                // nothing
                break;
            case ONHOLD_UNACKNOWLEDGED:
                // ack hold
                setVisibility(View.VISIBLE);
                break;
            case PENDINGREVIEW: // marked completed
                // mark incomplete
                setVisibility(View.VISIBLE);
                break;
            case INREVIEW:
                // nothing
                break;
            case PAID: // completed
                // view payment
                setVisibility(View.VISIBLE);
                break;
            case CANCELED:
                // nothing
                break;
            case CANCELED_LATEFEEPAID:
                // view fee
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
