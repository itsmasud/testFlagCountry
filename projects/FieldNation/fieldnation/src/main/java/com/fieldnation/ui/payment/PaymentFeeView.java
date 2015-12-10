package com.fieldnation.ui.payment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.accounting.Fee;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.utils.misc;

public class PaymentFeeView extends RelativeLayout {
    private static final String TAG = "PaymentFeeView";

    // UI
    private IconFontTextView _bundleIconFontView;
    private TextView _titleTextView;
    private TextView _priceTextView;
    private TextView _stateTextView;

    // Data
    public PaymentFeeView(Context context) {
        this(context, null, -1);
    }

    public PaymentFeeView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public PaymentFeeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(getContext()).inflate(R.layout.view_workorder_card, this);

        if (isInEditMode())
            return;

        _bundleIconFontView = (IconFontTextView) findViewById(R.id.bundle_iconFont);
        _titleTextView = (TextView) findViewById(R.id.title_textview);
        findViewById(R.id.companyName_textview).setVisibility(GONE);
        findViewById(R.id.workorderId_textview).setVisibility(GONE);
        findViewById(R.id.time_textview).setVisibility(INVISIBLE);
        _priceTextView = (TextView) findViewById(R.id.price_textview);
        findViewById(R.id.extra_textview).setVisibility(INVISIBLE);
        _stateTextView = (TextView) findViewById(R.id.status_textview);

        findViewById(R.id.left_button).setVisibility(GONE);
        findViewById(R.id.rightWhite_button).setVisibility(GONE);
        findViewById(R.id.rightGreen_button).setVisibility(GONE);
        findViewById(R.id.rightOrange_button).setVisibility(GONE);

        setIsBundle(false);
    }

    private void setIsBundle(boolean isBundle) {
        if (isBundle) {
            _bundleIconFontView.setVisibility(VISIBLE);
        } else {
            _bundleIconFontView.setVisibility(GONE);
        }
    }

    public void setWorkorder(Payment payment, Fee fee) {
        _titleTextView.setText("Fee [" + fee.getWorkorderId() + "]");

        if (!misc.isEmptyOrNull(payment.getPayMethod())) {
            String paymethod = misc.capitalize(payment.getPayMethod().replaceAll("_", " "));
            _stateTextView.setText(paymethod + "\n" + payment.getStatus());
            _stateTextView.setVisibility(VISIBLE);
        } else {
            _stateTextView.setText(payment.getStatus());
            _stateTextView.setVisibility(VISIBLE);
        }
        _priceTextView.setText(misc.toCurrency(fee.getAmount()).substring(1));
    }
}
