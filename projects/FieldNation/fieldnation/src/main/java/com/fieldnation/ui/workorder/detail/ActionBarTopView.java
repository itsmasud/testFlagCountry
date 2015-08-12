package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;

public class ActionBarTopView extends RelativeLayout {
    private static final String TAG = "ActionBarTopView";

    // Ui
    private Button _checkinButton;
    private Button _checkoutButton;
    private Button _acknowledgeButton;
    private Button _completeButton;
    private Button _incompleteButton;
    private Button _confirmButton;
    private Button _closingNotesButton;

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

        _checkinButton = (Button) findViewById(R.id.checkin_button);
        _checkinButton.setOnClickListener(_checkin_onClick);
        _checkoutButton = (Button) findViewById(R.id.checkout_button);
        _checkoutButton.setOnClickListener(_checkout_onClick);
        _acknowledgeButton = (Button) findViewById(R.id.acknowledge_button);
        _acknowledgeButton.setOnClickListener(_acknowledge_onClick);
        _completeButton = (Button) findViewById(R.id.complete_button);
        _completeButton.setOnClickListener(_complete_onClick);
        _incompleteButton = (Button) findViewById(R.id.incomplete_button);
        _incompleteButton.setOnClickListener(_incomplete_onClick);
        _confirmButton = (Button) findViewById(R.id.confirm_button);
        _confirmButton.setOnClickListener(_confirm_onClick);
        _closingNotesButton = (Button) findViewById(R.id.closingnotes_button);
        _closingNotesButton.setOnClickListener(_closing_onClick);

        setVisibility(View.GONE);
    }

    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;

        _checkinButton.setVisibility(View.GONE);
        _checkoutButton.setVisibility(View.GONE);
        _acknowledgeButton.setVisibility(View.GONE);
        _completeButton.setVisibility(View.GONE);
        _incompleteButton.setVisibility(View.GONE);
        _confirmButton.setVisibility(View.GONE);
        _closingNotesButton.setVisibility(View.GONE);
        _completeButton.setEnabled(true);
        _completeButton.setEnabled(true);

        if (_workorder.canCheckout()) {
            _checkoutButton.setVisibility(View.VISIBLE);
            setVisibility(VISIBLE);
        }
        if (_workorder.canCheckin()) {
            _checkinButton.setVisibility(View.VISIBLE);
            setVisibility(VISIBLE);
        }
        if (_workorder.canAckHold()) {
            _acknowledgeButton.setVisibility(View.VISIBLE);
            setVisibility(VISIBLE);
        }
        if (_workorder.canConfirm()) {
            _confirmButton.setVisibility(View.VISIBLE);
            setVisibility(VISIBLE);
        }

        if (_workorder.canComplete()) {
            _completeButton.setVisibility(View.VISIBLE);
        } else if (_workorder.areTasksComplete()
                && misc.isEmptyOrNull(_workorder.getClosingNotes())
                && _workorder.canChangeClosingNotes()) {
            _closingNotesButton.setVisibility(View.VISIBLE);
        }

        if (_workorder.canIncomplete()) {
            _incompleteButton.setVisibility(View.VISIBLE);
            setVisibility(VISIBLE);
        }
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
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
                _listener.onAcknowledge();
        }
    };

    private final View.OnClickListener _complete_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onComplete();
        }
    };

    private final View.OnClickListener _incomplete_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onIncomplete();
        }
    };

    public interface Listener {
        void onCheckIn();

        void onCheckOut();

        void onAcknowledge();

        void onComplete();

        void onIncomplete();

        void onConfirm();

        void onEnterClosingNotes();
    }
}
