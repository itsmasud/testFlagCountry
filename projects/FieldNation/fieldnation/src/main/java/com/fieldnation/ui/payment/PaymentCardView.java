package com.fieldnation.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import org.w3c.dom.Text;

import java.util.Calendar;

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

    private View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), PaymentDetailActivity.class);
            intent.putExtra(PaymentDetailActivity.INTENT_KEY_PAYMENT_ID, _paymentInfo.getPaymentId());
            getContext().startActivity(intent);
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
            if (_paymentInfo.getFees() != null && _paymentInfo.getFees().length == 1) {
                _subTitleTextView.setText(getResources().getString(
                        R.string.number_workorders_and_one_fee, _paymentInfo.getWorkorders().length));
            } else if (_paymentInfo.getFees() != null && _paymentInfo.getFees().length > 0) {
                _subTitleTextView.setText(getResources().getString(
                        R.string.number_workorders_and_x_fees,
                        _paymentInfo.getWorkorders().length,
                        _paymentInfo.getFees().length));
            } else {
                _subTitleTextView.setText(getResources().getString(
                        R.string.number_workorders_and_x_fees,
                        _paymentInfo.getWorkorders().length,
                        0));
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
            } else {
                _iconView.setTextColor(getResources().getColor(R.color.fn_yellow));
                _iconView.setText(R.string.icon_icon_circle_pending);
            }
            _titleTextView.setText(status);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        // pay_method
        try {
            String method = _paymentInfo.getPayMethod().toLowerCase().replaceAll("_", " ");
            method = misc.capitalize(method);
            _payTypeTextView.setText(method);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            _payTypeTextView.setText("");
        }
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
