package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.PayModifier;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

public class ExpectedPaymentView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "ExpectedPaymentView";

    // UI
    private TextView _laborTextView;
    private TextView _expensesTextView;
    private TextView _bonusTextView;
    private TextView _penaltyTextView;
    private TextView _discountsTextView;
    private TextView _expectedTotalTextView;
    private TextView _feePercentTextView;
    private TextView _feeTextView;
    private TextView _insurancePercentTextView;
    private TextView _insuranceFeeTextView;
    private TextView _totalTextView;
    private TextView _payStatusTextView;

    // Data
    private WorkOrder _workOrder;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public ExpectedPaymentView(Context context) {
        super(context);
        init();
    }

    public ExpectedPaymentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_expected_payment, this);

        if (isInEditMode())
            return;

        _laborTextView = (TextView) findViewById(R.id.labor_textview);
        _expensesTextView = (TextView) findViewById(R.id.expenses_textview);
        _bonusTextView = (TextView) findViewById(R.id.bonus_textview);
        _penaltyTextView = (TextView) findViewById(R.id.penalty_textview);
        _discountsTextView = (TextView) findViewById(R.id.discounts_textview);
        _expectedTotalTextView = (TextView) findViewById(R.id.expectedtotal_textview);

        _feePercentTextView = (TextView) findViewById(R.id.feePercentage_textview);
        _feeTextView = (TextView) findViewById(R.id.fee_textview);

        _insurancePercentTextView = (TextView) findViewById(R.id.insurancePercentage_textview);
        _insuranceFeeTextView = (TextView) findViewById(R.id.insuranceFee_textview);
        _totalTextView = (TextView) findViewById(R.id.total_textview);
        _payStatusTextView = (TextView) findViewById(R.id.paystatus_textview);

        populateUi();
    }

    /*-*************************************-*/
    /*-				Mutators				-*/
    /*-*************************************-*/
    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        Log.e(TAG, "populateUi");

        if (_payStatusTextView == null)
            return;

        if (_workOrder == null) {
            setVisibility(View.GONE);
            return;
        }

        setVisibility(View.VISIBLE);

        Pay pay = _workOrder.getPay();

        if (pay == null) {
            this.setVisibility(GONE);
            return;
        } else if (_workOrder.getPay() == null /* TODO || _workOrder.getPay().hidePay()*/) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }

        if (_workOrder.getStatus().getId() == 2 || _workOrder.getStatus().getId() == 9) {
            setVisibility(GONE);
            return;
        }

        Double expectedSum = pay.getLaborSum();

        // Labor
        _laborTextView.setText(misc.toCurrency(pay.getLaborSum()));

        // Expenses approved
        _expensesTextView.setText(misc.toCurrency(pay.getExpenses().getSum().getCharged()));
        expectedSum += pay.getExpenses().getSum().getCharged();

        // Discounts
        _discountsTextView.setText(misc.toCurrency(pay.getDiscounts().getSum().getAll()));
        expectedSum -= pay.getDiscounts().getSum().getAll();

        // Bonus
        _bonusTextView.setText(misc.toCurrency(pay.getBonuses().getSum().getCharged()));
        expectedSum += pay.getBonuses().getSum().getCharged();

        // Penalty
        _penaltyTextView.setText(misc.toCurrency(pay.getPenalties().getSum().getCharged()));
        expectedSum -= pay.getPenalties().getSum().getCharged();

        // Expected Total
        _expectedTotalTextView.setText(misc.toCurrency(expectedSum));

        // Insurance and Field Nation fees
        _feePercentTextView.setVisibility(GONE);
        _feeTextView.setVisibility(GONE);
        _insurancePercentTextView.setVisibility(GONE);
        _insuranceFeeTextView.setVisibility(GONE);
        PayModifier[] fees = pay.getFees();
        for (PayModifier fee : fees) {
            if (fee.getName().equals("provider")) {
                _feeTextView.setText(misc.toCurrency(fee.getAmount()));
                _feePercentTextView.setText(String.format(
                        getContext().getString(R.string.fieldnation_expected_fee_percentage),
                        (float) (fee.getModifier() * 100)));

                _feeTextView.setVisibility(VISIBLE);
                _feePercentTextView.setVisibility(VISIBLE);
            } else if (fee.getName().equals("insurance")) {
                _insuranceFeeTextView.setText(misc.toCurrency(fee.getAmount()));
                _insurancePercentTextView.setText(String.format(
                        getContext().getString(R.string.fieldnation_expected_insurance_percentage),
                        (float) (fee.getModifier() * 100)));

                _insuranceFeeTextView.setVisibility(VISIBLE);
                _insurancePercentTextView.setVisibility(VISIBLE);
            }
        }

        _totalTextView.setText(misc.toCurrency(pay.getTotal()));

        if (_workOrder.getStatus().getId() == 5) {
            _payStatusTextView.setText("Pending");
        } else if (pay.getPayment() != null
                && pay.getPayment().getCharged() != null
                && pay.getPayment().getCharged()) {
            _payStatusTextView.setText("Paid");
        } else {
            _payStatusTextView.setText("Unpaid");
        }
    }
}