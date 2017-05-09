package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.PayIncreases;
import com.fieldnation.v2.data.model.Requests;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

public class PaymentView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "ui.workorder.detail.PaymentView";

    // UI
    // TODO need to grab the description views at the top
    private TextView _payTextView;
    private TextView _termsTextView;
    private Button _actionButton;
    private ViewStub _requestNewPayStub;
    private RequestNewPayTile _requestNewPayTile = null;

    // Data
    private WorkOrder _workOrder;
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

        _requestNewPayStub = (ViewStub) findViewById(R.id.requestNewPayTile_viewstub);

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
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        refresh();
    }

    private RequestNewPayTile getRequestNewPayTile() {
        if (_requestNewPayTile == null) {
            _requestNewPayTile = (RequestNewPayTile) _requestNewPayStub.inflate();
            _requestNewPayTile.setOnClickListener(_requestNewPay_onClick);
        }
        return _requestNewPayTile;
    }

    private void refresh() {
        if (_actionButton == null)
            return;


        if (_workOrder.getPay() != null
                && _workOrder.getPay().getIncreases() != null
                && _workOrder.getPay().getIncreases().getActionsSet().contains(PayIncreases.ActionsEnum.ADD)) {
            getRequestNewPayTile().setVisibility(VISIBLE);
            getRequestNewPayTile().setData(_workOrder);

        } else {
            getRequestNewPayTile().setVisibility(GONE);
        }

        Pay pay = _workOrder.getPay();
        if (pay != null  /* TODO && !pay.hidePay() */) {
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

        // can counter offer
        if (_workOrder.getRequests() != null
                && _workOrder.getRequests().getActionsSet() != null
                && _workOrder.getRequests().getActionsSet().contains(Requests.ActionsEnum.COUNTER_OFFER)) {
            _actionButton.setText(R.string.btn_counter_offer);

            // can request pay increase
        } else if (_workOrder.getPay() != null
                && _workOrder.getPay().getIncreases() != null
                && _workOrder.getPay().getIncreases().getActionsSet() != null
                && _workOrder.getPay().getIncreases().getActionsSet().contains(PayIncreases.ActionsEnum.ADD)) {
            _actionButton.setText(R.string.btn_request_new_pay);

            // counter offers disabled and in the marketplace
        } else if (!(_workOrder.getRequests() != null
                && _workOrder.getRequests().getActionsSet() != null
                && _workOrder.getRequests().getActionsSet().contains(Requests.ActionsEnum.COUNTER_OFFER))
                && _workOrder.getRequests().getCounterOffer() == null
                && (_workOrder.getStatus().getId() == 2 || _workOrder.getStatus().getId() == 9)) {
            _actionButton.setEnabled(false);
            _actionButton.setText(R.string.btn_counter_disabled);
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
                if (_workOrder.getRequests() != null
                        && _workOrder.getRequests().getActionsSet() != null
                        && _workOrder.getRequests().getActionsSet().contains(Requests.ActionsEnum.COUNTER_OFFER)
                        && _workOrder.getRequests() != null
                        && _workOrder.getRequests().getCounterOffer() == null) {
                    _listener.onCounterOffer(_workOrder);
                } else if (_workOrder.getPay() != null
                        && _workOrder.getPay().getIncreases() != null
                        && _workOrder.getPay().getIncreases().getActionsSet() != null
                        && _workOrder.getPay().getIncreases().getActionsSet().contains(PayIncreases.ActionsEnum.ADD)) {
                    _listener.onRequestNewPay(_workOrder);
                }
            }
        }
    };

    private final View.OnClickListener _terms_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onShowTerms(_workOrder);
        }
    };

    private final View.OnClickListener _requestNewPay_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_workOrder.getPay() != null
                    && _workOrder.getPay().getIncreases() != null
                    && _workOrder.getPay().getIncreases().getActionsSet() != null
                    && _workOrder.getPay().getIncreases().getActionsSet().contains(PayIncreases.ActionsEnum.ADD)) {
                if (_listener != null) {
                    _listener.onRequestNewPay(_workOrder);
                }
            }
        }
    };

    public interface Listener {
        void onCounterOffer(WorkOrder workOrder);

        void onRequestNewPay(WorkOrder workOrder);

        void onShowTerms(WorkOrder workOrder);
    }
}
