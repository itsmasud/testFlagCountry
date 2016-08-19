package com.fieldnation.ui.payment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.accounting.Fee;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.fntools.misc;

public class PaymentFeeView extends RelativeLayout {
    private static final String TAG = "PaymentFeeView";

    // UI
    private TextView _titleTextView;
    private TextView _paymentTextView;

    /*-*****************************-*/
    /*-			Life cycle			-*/
    /*-*****************************-*/
    public PaymentFeeView(Context context) {
        super(context);
        init();
    }

    public PaymentFeeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaymentFeeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_payment_card_other, this);

        if (isInEditMode())
            return;

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _paymentTextView = (TextView) findViewById(R.id.payment_textview);
    }

    public void setWorkorder(Payment payment, Fee fee) {
        _titleTextView.setText(fee.getDescription());
        _paymentTextView.setText(misc.toCurrency(fee.getAmount()));
    }
}
