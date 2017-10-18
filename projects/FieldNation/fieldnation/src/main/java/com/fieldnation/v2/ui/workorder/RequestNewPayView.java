package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.PayIncrease;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.PayView;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

/**
 * Created by Michael on 6/3/2016.
 */
public class RequestNewPayView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "RequestNewPayView";

    // Ui
    private TextView _statusTextView;
    private PayView _payView;

    // Data
    private WorkOrder _workOrder;

    public RequestNewPayView(Context context) {
        super(context);
        init();
    }

    public RequestNewPayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RequestNewPayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_request_pay, this);

        if (isInEditMode())
            return;

        _statusTextView = findViewById(R.id.requestPayStatus_textview);
        _payView = findViewById(R.id.payView);

        populateUi();
    }

    private void populateUi() {
        setVisibility(GONE);
        if (_workOrder == null)
            return;

        if (_payView == null)
            return;

        PayIncrease increase = _workOrder.getPay().getIncreases().getLastIncrease();
        if (increase == null
                || increase.getPay().getBase().getAmount() == null
                || increase.getPay().getBase().getUnits() == null) {
            return;
        }

        Pay pay = increase.getPay();

        _payView.set(pay);
        if (increase.getStatus() != null) {
            switch (increase.getStatus()) {
                case PENDING: // pending
                    setVisibility(VISIBLE);
                    _statusTextView.setText("PENDING");
                    _statusTextView.setTextColor(getResources().getColor(R.color.fn_light_text));
                    break;
                case ACCEPTED: // approved
                    setVisibility(VISIBLE);
                    _statusTextView.setText("APPROVED");
                    _statusTextView.setTextColor(getResources().getColor(R.color.fn_green));
                    break;
                case DENIED: // denied
                    setVisibility(VISIBLE);
                    _statusTextView.setText("DENIED");
                    _statusTextView.setTextColor(getResources().getColor(R.color.fn_red));
                    break;
                default:
                    setVisibility(GONE);
                    break;
            }
        }
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

}
