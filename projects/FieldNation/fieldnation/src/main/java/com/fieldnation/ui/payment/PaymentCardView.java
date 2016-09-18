package com.fieldnation.ui.payment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.IconFontTextView;

public class PaymentCardView extends RelativeLayout {
    private static final String TAG = "PaymentCardView";

    // UI
    private IconFontTextView _iconView;
    private TextView _titleTextView;
    private TextView _subTitleTextView;
    private TextView _paymentTextView;
    private TextView _payTypeTextView;

    // Data
    private Payment _paymentInfo;

    /*-*****************************-*/
    /*-			Life cycle			-*/
    /*-*****************************-*/
    public PaymentCardView(Context context) {
        super(context);
        init();
    }

    public PaymentCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaymentCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_payment_card, this);

        if (isInEditMode())
            return;

        _iconView = (IconFontTextView) findViewById(R.id.icon_view);
        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _subTitleTextView = (TextView) findViewById(R.id.subtitle_textview);
        _paymentTextView = (TextView) findViewById(R.id.payment_textview);
        _payTypeTextView = (TextView) findViewById(R.id.paytype_textview);

        setOnClickListener(_this_onClick);
    }

    private final View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PaymentDetailActivity.startNew(App.get(), _paymentInfo.getPaymentId());
        }
    };

    public void setData(Payment paymentInfo) {
        _paymentInfo = paymentInfo;
        refresh();
    }

    /**
     * repopulates the ui
     */
    private void refresh() {
        // amount
        try {
            _paymentTextView.setText(misc.toCurrency(_paymentInfo.getAmount()));
        } catch (Exception ex) {
            Log.v(TAG, ex);
            _paymentTextView.setText("NA");
        }
        try {
            // TODO create string resources.
            if (_paymentInfo.getFees() != null && _paymentInfo.getFees().length > 0) {
                _subTitleTextView.setText(getResources().getString(R.string.num_workorders_and_payments,
                        getResources().getQuantityString(
                                R.plurals.num_workorders,
                                _paymentInfo.getWorkorders().length,
                                _paymentInfo.getWorkorders().length),
                        getResources().getQuantityString(
                                R.plurals.num_other_payments,
                                _paymentInfo.getFees().length,
                                _paymentInfo.getFees().length)));
            } else {
                _subTitleTextView.setText(getResources().getQuantityString(
                        R.plurals.num_workorders,
                        _paymentInfo.getWorkorders().length,
                        _paymentInfo.getWorkorders().length));
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            _subTitleTextView.setText("");
        }

        // status
        try {
            String status = misc.capitalize(_paymentInfo.getStatus());

            if (status.toLowerCase().equals("paid")) {
                _iconView.setTextColor(getResources().getColor(R.color.fn_accent_color));
                _iconView.setText(R.string.icon_circle_check);
                _payTypeTextView.setText(DateUtils.formatDate(ISO8601.toCalendar(_paymentInfo.getDatePaid())));
            } else {
                _payTypeTextView.setText(R.string.pending);
                _iconView.setTextColor(getResources().getColor(R.color.fn_yellow));
                _iconView.setText(R.string.icon_circle_pending);
            }
            _titleTextView.setText(status);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        // pay_method
/*        try {
            String method = _paymentInfo.getPayMethod();
            method = misc.capitalize(method);
            _payTypeTextView.setText(method);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            _payTypeTextView.setText("");
        }*/
        // payment_id
        try {
            _titleTextView.setText(getResources().getString(R.string.payment_id_x, _paymentInfo.getPaymentId()));
        } catch (Exception ex) {
            Log.v(TAG, ex);
            _titleTextView.setText(getResources().getString(R.string.payment_id_na));
        }

        // date_paid
/*
        try {
            String d = _paymentInfo.getDatePaid();
            Calendar cal = ISO8601.toCalendar(d);

            _dateTextView.setText(getContext().getString(R.string.estimated) + " " + misc.formatDate(cal));
        } catch (Exception ex) {
            Log.v(TAG, ex);
            _dateTextView.setText("");
        }
*/
    }
}
