package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fieldnation.R;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 11/2/17.
 */

public class PaymentSectionView extends LinearLayout implements WorkOrderRenderer, UUIDView {
    private static final String TAG = "PaymentSectionView";

    private List<WorkOrderRenderer> _renderers = new LinkedList<>();
    private WorkOrder _workOrder;
    private String _uiUUID;

    public PaymentSectionView(Context context) {
        super(context);
        init();
    }

    public PaymentSectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaymentSectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_section_payments, this, true);

        if (isInEditMode()) return;

        _renderers.add((WorkOrderRenderer) findViewById(R.id.payment_view));
        _renderers.add((WorkOrderRenderer) findViewById(R.id.timelogSummary_view));
        _renderers.add((WorkOrderRenderer) findViewById(R.id.counterOfferSummary_view));
        _renderers.add((WorkOrderRenderer) findViewById(R.id.expensesSummaryView));
        _renderers.add((WorkOrderRenderer) findViewById(R.id.discountSummaryView));
        _renderers.add((WorkOrderRenderer) findViewById(R.id.expected_pay_view));
    }

    @Override
    public void setUUID(String uuid) {
        _uiUUID = uuid;

        for (WorkOrderRenderer workOrderRenderer : _renderers) {
            if (workOrderRenderer instanceof UUIDView) {
                ((UUIDView) workOrderRenderer).setUUID(_uiUUID);
            }
        }

        populateUi();
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        for (WorkOrderRenderer workOrderRenderer : _renderers) {
            workOrderRenderer.setWorkOrder(workOrder);
        }
        populateUi();
    }

    private void populateUi() {
        setVisibility(VISIBLE);

        boolean visible = false;
        for (WorkOrderRenderer workOrderRenderer : _renderers) {
            if (((View) workOrderRenderer).getVisibility() == VISIBLE) {
                visible = true;
                return;
            }
        }

        if (!visible) {
            setVisibility(GONE);
        }
    }
}
