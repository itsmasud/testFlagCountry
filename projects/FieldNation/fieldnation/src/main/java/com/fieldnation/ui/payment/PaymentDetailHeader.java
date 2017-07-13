package com.fieldnation.ui.payment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;

import java.util.Calendar;

/**
 * Created by mc on 7/13/17.
 */

public class PaymentDetailHeader extends LinearLayout {
    private static final String TAG = "PaymentDetailHeader";

    // Ui
    private TextView _titleTextView;
    private TextView _paymentTextView;
    private TextView _timeTextView;

    // Data
    private Payment _paid;

    public PaymentDetailHeader(Context context) {
        super(context);
        init();
    }

    public PaymentDetailHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaymentDetailHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_payments_detail_header, this);

        if (isInEditMode())
            return;

        _titleTextView = findViewById(R.id.title_textview);
        _paymentTextView = findViewById(R.id.payment_textview);
        _timeTextView = findViewById(R.id.time_textview);
    }

    public void setPayment(Payment payment) {
        _paid = payment;

        populateUi();
    }

    private void populateUi() {
        if (_timeTextView == null)
            return;

        if (_paid == null)
            return;

        try {
            if (_paid.getDatePaid() != null) {
                String when = "";
                Calendar cal = ISO8601.toCalendar(_paid.getDatePaid());

                when = DateUtils.formatDate(cal);

                _timeTextView.setVisibility(View.VISIBLE);
                _timeTextView.setText(when);
            } else {
                _timeTextView.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            _timeTextView.setVisibility(View.INVISIBLE);
        }

        _paymentTextView.setText(misc.toCurrency(_paid.getAmount()));
        _titleTextView.setText(_paid.getPayMethod());

    }
}
