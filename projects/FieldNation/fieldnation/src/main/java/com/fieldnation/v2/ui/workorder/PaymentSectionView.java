package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.ui.workorder.detail.CounterOfferSummaryView;
import com.fieldnation.ui.workorder.detail.PaymentView;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.CounterOfferDialog;
import com.fieldnation.v2.ui.dialog.TermsDialog;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 11/2/17.
 */

public class PaymentSectionView extends LinearLayout implements WorkOrderRenderer, UUIDView {
    private static final String TAG = "PaymentSectionView";

    private static final String DIALOG_TERMS = TAG + ".termsDialog";

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

        View v = null;
        _renderers.add((WorkOrderRenderer) findViewById(R.id.requestNewPay_view));

        PaymentView paymentView = findViewById(R.id.payment_view);
        paymentView.setListener(_paymentView_listener);
        _renderers.add(paymentView);

        _renderers.add((WorkOrderRenderer) findViewById(R.id.timelogSummary_view));

        CounterOfferSummaryView counterOfferSummaryView = findViewById(R.id.counterOfferSummary_view);
        counterOfferSummaryView.setListener(_coSummary_listener);
        _renderers.add(counterOfferSummaryView);

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
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        for (WorkOrderRenderer workOrderRenderer : _renderers) {
            workOrderRenderer.setWorkOrder(workOrder);
        }

        boolean visible = false;
        for (WorkOrderRenderer workOrderRenderer : _renderers) {
            if (((View) workOrderRenderer).getVisibility() == VISIBLE) {
                visible = true;
                return;
            }
        }

        if (visible) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }

    private final PaymentView.Listener _paymentView_listener = new PaymentView.Listener() {
        @Override
        public void onShowTerms(WorkOrder workOrder) {
            TermsDialog.show(App.get(), DIALOG_TERMS, getContext().getString(R.string.dialog_terms_title), getContext().getString(R.string.dialog_terms_body));
        }
    };

    private final CounterOfferSummaryView.Listener _coSummary_listener = new CounterOfferSummaryView.Listener() {
        @Override
        public void onCounterOffer() {
            CounterOfferDialog.show(App.get(), _workOrder.getId(), _workOrder.getPay(), _workOrder.getSchedule());
        }
    };
}
