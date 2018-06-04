package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.ListItemTwoHorizTwoVertView;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

import java.util.LinkedList;
import java.util.List;

public class PaymentView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "PaymentView";

    private List<WorkOrderRenderer> _renderers = new LinkedList<>();

    // UI
    private ListItemTwoHorizTwoVertView _payView;

    // Data
    private WorkOrder _workOrder;

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

        _payView = findViewById(R.id.pay_summary_view);
        _payView.setAlertVisible(false);
        _payView.setTitleEllipse(false);
        _renderers.add((WorkOrderRenderer) findViewById(R.id.penalty_summary_view));
        _renderers.add((WorkOrderRenderer) findViewById(R.id.bonus_summary_view));
        _renderers.add((WorkOrderRenderer) findViewById(R.id.insurance_summary_view));
        _renderers.add((WorkOrderRenderer) findViewById(R.id.fnServiceFeeSummaryView));
        _renderers.add((WorkOrderRenderer) findViewById(R.id.workers_comp_summary_view));

        setVisibility(View.GONE);
    }

    /*-*************************************-*/
    /*-				Mutators				-*/
    /*-*************************************-*/
    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        for (WorkOrderRenderer workOrderRenderer : _renderers) {
            workOrderRenderer.setWorkOrder(workOrder);
        }

        refresh();
    }

    private void refresh() {
        setVisibility(View.VISIBLE);

        if (_workOrder == null
                || _workOrder.getPay() == null
                || _workOrder.getPay().getType() == null) {
            _payView.setVisibility(GONE);
            return;
        }

        _payView.set(getPayTypeString(), getPayInfoString(), getPayAmountString(), null);
    }

    private String getPayTypeString() {
        if (_workOrder == null || _workOrder.getPay() == null || _workOrder.getPay().getType() == null)
            return null;

        final Pay.TypeEnum type = _workOrder.getPay().getType();
        String payTypeText = null;
        switch (type) {
            case FIXED:
                payTypeText = "Fixed";
                break;
            case HOURLY:
                payTypeText = "Hourly";
                break;
            case DEVICE:
                payTypeText = "Device";
                break;
            case BLENDED:
                payTypeText = "Blended (Fixed + Hourly)";
                break;
        }
        return payTypeText;
    }

    private String getPayInfoString() {
        if (_workOrder == null || _workOrder.getPay() == null || _workOrder.getPay().getType() == null)
            return null;

        final Pay pay = _workOrder.getPay();
        final Pay.TypeEnum type = _workOrder.getPay().getType();
        String payInfoText = null;
        switch (type) {
            case FIXED:
                break;
            case HOURLY:
                payInfoText = "For up to " + misc.to1Decimal(pay.getBase().getUnits()) + " hrs";
                break;
            case DEVICE:
                payInfoText = "For up to " + misc.to1Decimal(pay.getBase().getUnits()) + " devices";
                break;
            case BLENDED:
                payInfoText = misc.toCurrency(pay.getBase().getAmount()) + " for first " + misc.to1Decimal(pay.getBase().getUnits()) + " hrs + " +
                        misc.toCurrency(pay.getAdditional().getAmount()) + " for up to " + misc.to1Decimal(pay.getAdditional().getUnits()) + " hrs";
                break;
        }
        return payInfoText;
    }

    private String getPayAmountString() {
        if (_workOrder == null || _workOrder.getPay() == null || _workOrder.getPay().getType() == null)
            return null;

        final Pay pay = _workOrder.getPay();
        final Pay.TypeEnum type = _workOrder.getPay().getType();
        String text = null;
        switch (type) {
            case FIXED:
                text = misc.toCurrency(pay.getBase().getAmount());
                break;
            case HOURLY:
                text = misc.toCurrency(pay.getBase().getAmount()) + "/hr";
                break;
            case DEVICE:
                text = misc.toCurrency(pay.getBase().getAmount()) + "/device";
                break;
            case BLENDED:
                text = misc.toCurrency(pay.getBase().getAmount() + pay.getAdditional().getAmount() * pay.getAdditional().getUnits());
                break;
        }
        return text;
    }
}
