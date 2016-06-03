package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderSubstatus;
import com.fieldnation.utils.misc;

public class PaymentView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "ui.workorder.detail.PaymentView";

    // UI
    // TODO need to grab the description views at the top
    private TextView _payTextView;
    private TextView _termsTextView;
    private Button _actionButton;
    private RequestNewPayTile _requestNewPayTile;

    // Data
    private Workorder _workorder;
    private Listener _listener;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public PaymentView(Context context) {
        super(context);
        init();
    }

    public PaymentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_payment, this);

        if (isInEditMode())
            return;

        _requestNewPayTile = (RequestNewPayTile) findViewById(R.id.requestNewPayTile);

        _payTextView = (TextView) findViewById(R.id.pay_textview);
        _termsTextView = (TextView) findViewById(R.id.terms_textview);
        _termsTextView.setOnClickListener(_terms_onClick);

        _actionButton = (Button) findViewById(R.id.action_button);
        _actionButton.setOnClickListener(_action_onClick);

        setVisibility(View.GONE);
    }

    /*-*************************************-*/
    /*-				Mutators				-*/
    /*-*************************************-*/
    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        refresh();
    }

    private void refresh() {
        if (_requestNewPayTile == null)
            return;

        if (_workorder.getIncreaseRequestInfo() != null) {
            _requestNewPayTile.setVisibility(VISIBLE);
            _requestNewPayTile.setData(_workorder);
        } else {
            _requestNewPayTile.setVisibility(GONE);
        }

        Pay pay = _workorder.getPay();
        if (pay != null && !pay.hidePay()) {
            _termsTextView.setVisibility(VISIBLE);
            String[] paytext = pay.toDisplayStringLong();
            String data = "";

            if (paytext[0] != null) {
                data = paytext[0];
            }

            if (paytext[1] != null) {
                data += "\n" + paytext[1];
            }

            if (misc.isEmptyOrNull(data)) {
                setVisibility(GONE);
                return;
            }

            _payTextView.setText(data);
            setVisibility(View.VISIBLE);
        } else {
            _payTextView.setVisibility(GONE);
            _termsTextView.setVisibility(GONE);
            setVisibility(View.GONE);
            return;
        }

        _actionButton.setVisibility(VISIBLE);
        _actionButton.setEnabled(true);
        if (_workorder.canCounterOffer() && _workorder.getCounterOfferInfo() == null) {
            _actionButton.setText("COUNTER OFFER");
        } else if (_workorder.canRequestPayIncrease()) {
            _actionButton.setText("REQUEST NEW PAY");
        } else if (!_workorder.canCounterOffer() &&
                (_workorder.getWorkorderSubstatus() == WorkorderSubstatus.ROUTED
                        || _workorder.getWorkorderSubstatus() == WorkorderSubstatus.REQUESTED)) {
            _actionButton.setEnabled(false);
            _actionButton.setText("COUNTER DISABLED");
        } else {
            _actionButton.setVisibility(GONE);
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnClickListener _action_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                if (_workorder.canCounterOffer() && _workorder.getCounterOfferInfo() == null) {
                    _listener.onCounterOffer(_workorder);
                } else if (_workorder.canRequestPayIncrease()) {
                    _listener.onRequestNewPay(_workorder);
                }
            }
        }
    };

    private final View.OnClickListener _terms_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onShowTerms(_workorder);
        }
    };

    public interface Listener {
        void onCounterOffer(Workorder workorder);

        void onRequestNewPay(Workorder workorder);

        void onShowTerms(Workorder workorder);
    }
}
