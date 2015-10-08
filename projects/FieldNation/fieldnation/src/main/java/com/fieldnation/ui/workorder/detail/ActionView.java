package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;

public class ActionView extends RelativeLayout implements WorkorderRenderer {
    private static final String TAG = "ActionView";

    // UI
    private Button _requestButton;
    private Button _notInterestedButton;
    private Button _counterOfferButton;
    private Button _completeButton;
    private Button _withdrawRequestButton;
    private ProgressBar _progressBar;

    // DATA
    private Workorder _workorder;
    private Listener _listener;

    /*-*****************************-*/
    /*-			Life Cycle			-*/
    /*-*****************************-*/
    public ActionView(Context context) {
        super(context);
        init();
    }

    public ActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ActionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_actions, this);

        if (isInEditMode())
            return;

        _requestButton = (Button) findViewById(R.id.request_button);
        _requestButton.setOnClickListener(_request_onClick);

        _notInterestedButton = (Button) findViewById(R.id.not_interested_button);
        _notInterestedButton.setOnClickListener(_notInterested_onClick);

        _counterOfferButton = (Button) findViewById(R.id.counteroffer_button);
        _counterOfferButton.setOnClickListener(_counteroffer_onClick);

        _completeButton = (Button) findViewById(R.id.complete_button);
        _completeButton.setOnClickListener(_complete_onClick);

        _progressBar = (ProgressBar) findViewById(R.id.progressBar);

        _withdrawRequestButton = (Button) findViewById(R.id.withdraw_request_button);
        _withdrawRequestButton.setOnClickListener(_withdraw_onClick);

        setVisibility(View.GONE);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    public void setWorkorder(Workorder workorder) {
        Log.v(TAG, "Method Stub: setWorkorder()");

        _workorder = workorder;

        _requestButton.setVisibility(View.GONE);
        _notInterestedButton.setVisibility(View.GONE);
        _counterOfferButton.setVisibility(View.GONE);

        // request button
        if (_workorder.canAcceptWork()) {
            _requestButton.setText(R.string.btn_accept);
            _requestButton.setVisibility(View.VISIBLE);
        } else if (_workorder.canRequest()) {
            _requestButton.setText(R.string.btn_request);
            _requestButton.setVisibility(View.VISIBLE);
        } else {
            _requestButton.setVisibility(View.GONE);
        }

        // counter offer button
        if (_workorder.canCounterOffer()) {
            _counterOfferButton.setVisibility(View.VISIBLE);
            if (_workorder.getCounterOfferInfo() == null) {
                _counterOfferButton.setText(R.string.btn_counter);
            } else {
                _counterOfferButton.setText(R.string.btn_view_counter);
            }
        }

        // complete button
        if (_workorder.canComplete()) {
            _completeButton.setVisibility(View.VISIBLE);
        } else {
            _completeButton.setVisibility(View.GONE);
        }

        if (_workorder.canDeclineWork()) {
            _notInterestedButton.setVisibility(View.VISIBLE);
        } else {
            _notInterestedButton.setVisibility(View.GONE);
        }

        if (_workorder.canWithdrawRequest()) {
            _withdrawRequestButton.setVisibility(View.VISIBLE);
        } else {
            _withdrawRequestButton.setVisibility(View.GONE);
        }

        if (_requestButton.getVisibility() == VISIBLE
                || _notInterestedButton.getVisibility() == VISIBLE
                || _counterOfferButton.getVisibility() == VISIBLE
                || _completeButton.getVisibility() == VISIBLE
                || _withdrawRequestButton.getVisibility() == VISIBLE) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final OnClickListener _withdraw_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                if (_workorder.canWithdrawRequest()) {
                    _listener.onWithdrawRequest(_workorder);
                }
            }
        }
    };

    private final View.OnClickListener _request_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                if (_workorder.canAcceptWork()) {
                    _listener.onConfirmAssignment(_workorder);
                } else if (_workorder.canConfirm()) {
                    _listener.onConfirmAssignment(_workorder);
                } else {
                    _listener.onRequest(_workorder);
                }
            }
        }
    };

    private final View.OnClickListener _notInterested_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onNotInterested(_workorder);
        }
    };

    private final View.OnClickListener _complete_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onComplete(_workorder);
        }
    };

    private final View.OnClickListener _counteroffer_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onShowCounterOfferDialog(_workorder);
        }
    };

    public interface Listener {
        void onComplete(Workorder workorder);

        void onNotInterested(Workorder workorder);

        void onConfirmAssignment(Workorder workorder);

        void onRequest(Workorder workorder);

        void onShowCounterOfferDialog(Workorder workorder);

        void onWithdrawRequest(Workorder workorder);
    }

}
