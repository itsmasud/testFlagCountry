package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.ExpectedPayment;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.utils.misc;

public class ExpectedPaymentView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "ui.workorder.detail.ExpectedPaymentView";

    // UI
    private TextView _laborTextView;
    private TextView _expensesTextView;
    private TextView _discountsTextView;
    private TextView _expectedTotalTextView;
    private TextView _fnPercentTextView;
    private TextView _feeTextView;
    private TextView _payTextView;
    private TextView _payStatusTextView;

    // Data
    private Workorder _workorder;

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
        _discountsTextView = (TextView) findViewById(R.id.discounts_textview);
        _expectedTotalTextView = (TextView) findViewById(R.id.expectedtotal_textview);
        _fnPercentTextView = (TextView) findViewById(R.id.fieldnationpercentage_textview);
        _feeTextView = (TextView) findViewById(R.id.expectedfee_textview);
        _payTextView = (TextView) findViewById(R.id.expectedpay_textview);
        _payStatusTextView = (TextView) findViewById(R.id.paystatus_textview);

        populateUi();
    }

	/*-*************************************-*/
    /*-				Mutators				-*/
    /*-*************************************-*/

    @Override
    public void setWorkorder(Workorder workorder, boolean isCached) {
        _workorder = workorder;
        populateUi();
    }

    private void populateUi() {
        if (_payStatusTextView == null)
            return;

        if (_workorder == null) {
            setVisibility(View.GONE);
            return;
        }

        setVisibility(View.VISIBLE);

        ExpectedPayment pay = _workorder.getExpectedPayment();

        if (pay == null) {
            this.setVisibility(GONE);
            return;
        } else if (_workorder.getPay() == null) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }


        WorkorderStatus status = _workorder.getStatus().getWorkorderStatus();

        if (status == WorkorderStatus.AVAILABLE) {
            this.setVisibility(GONE);
            return;
        }

        _laborTextView.setText(misc.toCurrency(pay.getLaborEarned()));
        _expensesTextView.setText(misc.toCurrency(pay.getExpensesApproved()));
        _discountsTextView.setText(misc.toCurrency(pay.getDiscounts()));
        _expectedTotalTextView.setText(misc.toCurrency(pay.getExpectedTotal()));
        _feeTextView.setText(misc.toCurrency(pay.getExpectedFee()));
        _payTextView.setText(misc.toCurrency(pay.getExpectedAmount()));
        _payStatusTextView.setText(misc.capitalize(pay.getPaymentStatus()));
        if (pay.getFnFeePercentage() != null) {
            _fnPercentTextView.setText(String.format(getContext().getString(R.string.fieldnation_expected_fee_percentage), pay.getFnFeePercentage()));
        } else {
            _fnPercentTextView.setText(String.format(getContext().getString(R.string.fieldnation_expected_fee_percentage), 10.0));
        }
    }
}
