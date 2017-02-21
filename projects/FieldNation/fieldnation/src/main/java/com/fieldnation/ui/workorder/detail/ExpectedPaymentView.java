package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
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
    private TextView _insuraceFeeTextView;
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
        _insuraceFeeTextView = (TextView) findViewById(R.id.insuranceFee_textview);
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

/*
TODO        ExpectedPayment expectedPayment = _workOrder.getExpectedPayment();

        if (expectedPayment == null) {
            this.setVisibility(GONE);
            return;
        } else if (_workOrder.getPay() == null || _workOrder.getPay().hidePay()) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }

        WorkorderStatus status = _workOrder.getStatus().getWorkorderStatus();
        if (status == WorkorderStatus.AVAILABLE) {
            this.setVisibility(GONE);
            return;
        }

        _laborTextView.setText(misc.toCurrency(expectedPayment.getLaborEarned()));
        _expensesTextView.setText(misc.toCurrency(expectedPayment.getExpensesApproved()));
        _discountsTextView.setText(misc.toCurrency(expectedPayment.getDiscounts()));
        _expectedTotalTextView.setText(misc.toCurrency(expectedPayment.getExpectedTotal()));
        _feeTextView.setText(misc.toCurrency(expectedPayment.getExpectedServiceFee()));
        _totalTextView.setText(misc.toCurrency(expectedPayment.getExpectedAmount()));
        _payStatusTextView.setText(misc.capitalize(expectedPayment.getPaymentStatus()));

        if (expectedPayment.getFnFeePercentage() != null) {
            _feePercentTextView.setText(String.format(getContext().getString(R.string.fieldnation_expected_fee_percentage), expectedPayment.getFnFeePercentage()));
        } else {
            _feePercentTextView.setText(String.format(getContext().getString(R.string.fieldnation_expected_fee_percentage), 10.0));
        }

        if ((int) (double) (expectedPayment.getExpectedInsuranceFee() * 100) == 0) {
            _insuraceFeeTextView.setVisibility(GONE);
            _insurancePercentTextView.setVisibility(GONE);
        } else {
            Profile profile = App.get().getProfile();
            _insuraceFeeTextView.setVisibility(VISIBLE);
            _insurancePercentTextView.setVisibility(VISIBLE);
            _insuraceFeeTextView.setText(misc.toCurrency(expectedPayment.getExpectedInsuranceFee()));
            try {
                _insurancePercentTextView.setText(String.format(getContext().getString(R.string.fieldnation_expected_insurance_percentage), profile.insurancePercent()));
            } catch (Exception ex) {
                _insurancePercentTextView.setText(String.format(getContext().getString(R.string.fieldnation_expected_insurance_percentage), 1.3F));
            }
        }

        Penalty[] penaltyList = expectedPayment.getPenalties();
        double penalties = 0.0;
        if (penaltyList != null) {
            for (Penalty info : penaltyList) {
                penalties += info.getAmount();
            }
        }
        _penaltyTextView.setText(misc.toCurrency(penalties));

        Bonus[] bonusList = expectedPayment.getBonuses();
        double bonuses = 0.0;
        if (bonusList != null) {
            for (Bonus info : bonusList) {
                bonuses += info.getAmount();
            }
        }
        _bonusTextView.setText(misc.toCurrency(bonuses));
*/
    }
}
