package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.TermsDialog;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

import java.util.LinkedList;
import java.util.List;

public class PaymentView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "PaymentView";

    private List<WorkOrderRenderer> _renderers = new LinkedList<>();

    // UI
    private TextView _payTextView;
    private TextView _termsTextView;

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

        _payTextView = findViewById(R.id.pay_textview);
        _termsTextView = findViewById(R.id.terms_textview);
        _renderers.add((WorkOrderRenderer) findViewById(R.id.penalty_summary_view));
        _renderers.add((WorkOrderRenderer) findViewById(R.id.bonus_summary_view));
        _renderers.add((WorkOrderRenderer) findViewById(R.id.fnServiceFeeSummaryView));
        _renderers.add((WorkOrderRenderer) findViewById(R.id.insurance_summary_view));

        _termsTextView.setOnClickListener(_terms_onClick);

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
        if (_termsTextView == null)
            return;

        Pay pay = _workOrder.getPay();

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
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnClickListener _terms_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TermsDialog.show(App.get(), null, getContext().getString(R.string.dialog_terms_title), getContext().getString(R.string.dialog_terms_body));
        }
    };
}
