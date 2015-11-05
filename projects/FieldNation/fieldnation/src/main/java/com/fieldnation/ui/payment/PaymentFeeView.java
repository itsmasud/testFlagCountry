package com.fieldnation.ui.payment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.accounting.Fee;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.utils.misc;

public class PaymentFeeView extends RelativeLayout {
    private static final String TAG = "ui.payment.PaymentFeeView";
    // UI
    // status
    private View _statusView;
    private TextView _statusTextView;
    // bundle
    private IconFontTextView _bundleImageView;

    // center panel
    // title
    private TextView _titleTextView;
    // items
    private TextView _clientNameTextView;
    private TextView _distanceTextView;
    private TextView _whenTextView;

    // right panel
    private TextView _paymentTextView;
    private TextView _basisTextView;

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

        // connect UI
        // main content
        // status
        _statusView = findViewById(R.id.status_view);
        _statusTextView = (TextView) findViewById(R.id.status_textview);

        // bundle
        _bundleImageView = (IconFontTextView) findViewById(R.id.bundle_imageview);

        // center panel
        // title box
        _titleTextView = (TextView) findViewById(R.id.title_textview);
        // items
        _clientNameTextView = (TextView) findViewById(R.id.clientname_textview);
        _distanceTextView = (TextView) findViewById(R.id.distance_textview);
        _distanceTextView.setVisibility(GONE);
        _whenTextView = (TextView) findViewById(R.id.when_textview);

        // todo need to pick a button, and hide the others
        findViewById(R.id.action_button_green).setVisibility(GONE);
        findViewById(R.id.action_button_white).setVisibility(GONE);
        findViewById(R.id.action_button_orange).setVisibility(GONE);

        _paymentTextView = (TextView) findViewById(R.id.payment_textview);
        _basisTextView = (TextView) findViewById(R.id.basis_textview);

        findViewById(R.id.location_textview).setVisibility(View.GONE);
        findViewById(R.id.workorderid_textview).setVisibility(View.GONE);

        setIsBundle(false);

        _statusView.setBackgroundResource(R.drawable.card_status_green);
        _statusTextView.setTextColor(getContext().getResources().getColor(R.color.fn_white_text));

    }

    private void setIsBundle(boolean isBundle) {
        if (isBundle) {
            _bundleImageView.setVisibility(VISIBLE);
        } else {
            _bundleImageView.setVisibility(GONE);
        }
    }

    public void setWorkorder(Payment payment, Fee fee) {
        // _titleTextView.setText(fee.get);
        _titleTextView.setText("Fee [" + fee.getWorkorderId() + "]");
        // _clientNameTextView.setText(fee.getClientName());
        // _titleTextView.setVisibility(GONE);
        _clientNameTextView.setVisibility(GONE);

        try {
            // if (fee.getEndTime() != null) {
            // String when = "";
            // Calendar cal = ISO8601.toCalendar(wo.getEndTime());
            //
            // when = misc.formatDate(cal);
            //
            // _whenTextView.setVisibility(VISIBLE);
            // _whenTextView.setText(when);
            // } else {
            // _whenTextView.setVisibility(GONE);
            // }
        } catch (Exception ex) {
            // Log.v(TAG, ex);
            _whenTextView.setVisibility(GONE);
        }
        _whenTextView.setVisibility(GONE);

        if (payment.getStatus().equals("paid")) {
            _statusView.setBackgroundResource(R.drawable.card_status_black);
        } else {
            _statusView.setBackgroundResource(R.drawable.card_status_green);
        }

        String paymethod = misc.capitalize(payment.getPayMethod().replaceAll("_", " "));
        _basisTextView.setText(paymethod);
        _paymentTextView.setText(misc.toCurrency(fee.getAmount()).substring(1));
        _statusTextView.setText(misc.capitalize(payment.getStatus()));
    }

}
