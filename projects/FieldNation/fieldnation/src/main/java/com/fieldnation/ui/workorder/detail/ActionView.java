package com.fieldnation.ui.workorder.detail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.data.workorder.WorkorderSubstatus;

public class ActionView extends RelativeLayout implements WorkorderRenderer {
    private static final String TAG = "ui.workorder.detail.ActionView";

    // UI
    private Button _requestButton;
    private Button _notInterestedButton;
    private Button _counterOfferButton;
    private Button _completeButton;
    private LinearLayout _buttonLayout;
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

        _buttonLayout = (LinearLayout) findViewById(R.id.button_layout);
        _progressBar = (ProgressBar) findViewById(R.id.progressBar);

        setLoading(true);
        setVisibility(View.GONE);
    }

    private void setLoading(boolean isLoading) {
        // if (isLoading) {
        // _progressBar.setVisibility(View.VISIBLE);
        // _buttonLayout.setVisibility(View.GONE);
        // } else {
        // _progressBar.setVisibility(View.GONE);
        // _buttonLayout.setVisibility(View.VISIBLE);
        // }
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    public void setWorkorder(Workorder workorder) {
        Log.v(TAG, "Method Stub: setWorkorder()");

        _workorder = workorder;

        WorkorderStatus stat = workorder.getStatus().getWorkorderStatus();

        if (stat == WorkorderStatus.CANCELLED || stat == WorkorderStatus.COMPLETED) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }

        _requestButton.setVisibility(View.GONE);
        _notInterestedButton.setVisibility(View.GONE);
        _counterOfferButton.setVisibility(View.GONE);

        switch (stat) {
            case ASSIGNED:
                buildStatusAssigned();
                break;
            case AVAILABLE:
                buildStatusAvailable();
                break;
            case CANCELLED:
                buildStatusCancelled();
                break;
            case COMPLETED:
            case APPROVED:
            case PAID:
                buildStatusCompleted();
                break;
            case INPROGRESS:
                buildStatusInProgress();
                break;
            default:
                break;

        }

        if (_workorder.canComplete()) {
            _completeButton.setVisibility(View.VISIBLE);
        } else {
            _completeButton.setVisibility(View.GONE);
        }
        setVisibility(View.VISIBLE);
    }

    private void buildStatusAssigned() {
    }

    private void buildStatusAvailable() {
        WorkorderSubstatus substatus = _workorder.getStatus().getWorkorderSubstatus();

        _requestButton.setVisibility(View.VISIBLE);
        _notInterestedButton.setVisibility(View.VISIBLE);
        _counterOfferButton.setVisibility(View.VISIBLE);

        if (substatus == WorkorderSubstatus.ROUTED) {
            _requestButton.setText(R.string.accept_work);
        } else if (substatus == WorkorderSubstatus.REQUESTED || substatus == WorkorderSubstatus.COUNTEROFFERED) {
            _requestButton.setVisibility(View.GONE);
        } else {
            _requestButton.setText(R.string.request_work);
        }
    }

    private void buildStatusInProgress() {
    }

    private void buildStatusCancelled() {
    }

    private void buildStatusCompleted() {
    }

	/*-*************************-*/
    /*-			Events			-*/
	/*-*************************-*/

    private View.OnClickListener _request_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setLoading(true);
            if (_listener != null) {
                if (_workorder.getStatus().getWorkorderSubstatus() == WorkorderSubstatus.ROUTED) {
                    //_listener.onConfirmAssignment(_workorder);
                    _listener.onAcceptWorkorder(_workorder);
                } else {
                    _listener.onRequest(_workorder);
                }
            }
        }
    };

    private View.OnClickListener _notInterested_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setLoading(true);
            if (_listener != null)
                _listener.onNotInterested(_workorder);
        }
    };

    private View.OnClickListener _complete_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setLoading(true);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(
                    "Are you sure you want to mark this work order as complete?")
                    .setPositiveButton("Yes", _complete_dialog)
                    .setNegativeButton("No", null).show();
        }
    };

    private DialogInterface.OnClickListener _complete_dialog = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (_listener != null)
                _listener.onComplete(_workorder);
        }
    };


    private View.OnClickListener _counteroffer_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), CounterOfferActivity.class);
            intent.putExtra(CounterOfferActivity.INTENT_WORKORDER, _workorder);
            getContext().startActivity(intent);
        }
    };

    public interface Listener {
        public void onComplete(Workorder workorder);

        public void onNotInterested(Workorder workorder);

        public void onConfirmAssignment(Workorder workorder);

        public void onAcceptWorkorder(Workorder workorder);

        public void onRequest(Workorder workorder);
    }

}
