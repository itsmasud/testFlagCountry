package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.ExpectedPayment;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.utils.misc;

public class ExpectedPaymentView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "ExpectedPaymentView";

    // UI
    private TextView _laborTextView;
    private TextView _expensesTextView;
    private TextView _discountsTextView;
    private TextView _expectedTotalTextView;
    private TextView _feePercentTextView;
    private TextView _feeTextView;
    private TextView _insurancePercentTextView;
    private TextView _insuraceFeeTextView;
    private TextView _totalTextView;
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
    public void setWorkorder(Workorder workorder) {
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
        } else if (_workorder.getPay() == null || _workorder.getPay().hidePay()) {
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
        _feeTextView.setText(misc.toCurrency(pay.getExpectedServiceFee()));
        _totalTextView.setText(misc.toCurrency(pay.getExpectedAmount()));
        _payStatusTextView.setText(misc.capitalize(pay.getPaymentStatus()));
        if (pay.getFnFeePercentage() != null) {
            _feePercentTextView.setText(String.format(getContext().getString(R.string.fieldnation_expected_fee_percentage), pay.getFnFeePercentage()));
        } else {
            _feePercentTextView.setText(String.format(getContext().getString(R.string.fieldnation_expected_fee_percentage), 10.0));
        }

        if ((int) (double) (pay.getExpectedInsuranceFee() * 100) == 0) {
            _insuraceFeeTextView.setVisibility(GONE);
            _insurancePercentTextView.setVisibility(GONE);
        } else {
            Profile profile = App.get().getProfile();
            _insuraceFeeTextView.setVisibility(VISIBLE);
            _insurancePercentTextView.setVisibility(VISIBLE);
            _insuraceFeeTextView.setText(misc.toCurrency(pay.getExpectedInsuranceFee()));
            try {
                _insurancePercentTextView.setText(String.format(getContext().getString(R.string.fieldnation_expected_insurance_percentage), profile.insurancePercent()));
            } catch (Exception ex) {
                _insurancePercentTextView.setText(String.format(getContext().getString(R.string.fieldnation_expected_insurance_percentage), 1.3F));
            }
        }
    }
}
