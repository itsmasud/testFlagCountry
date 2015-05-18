package com.fieldnation.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.data.accounting.Workorder;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.util.Calendar;

public class PaymentWorkorderView extends RelativeLayout {
    private static final String TAG = "ui.payment.PaymentWorkorderView";
    // UI
    // status
    private View _statusView;
    private TextView _statusTextView;
    // bundle
    private IconFontTextView _bundleIconFont;

    // center panel
    // title
    private TextView _titleTextView;
    // items
    private TextView _clientNameTextView;
    private TextView _distanceTextView;
    private TextView _whenTextView;
    private TextView _workorderIdTextView;

    // right panel
    private TextView _paymentTextView;
    private TextView _basisTextView;
    private Button _actionGreenButton;
    private Button _actionGrayButton;
    private Button _actionOrangeButton;

    // Data
    private Workorder _workorder;

    public PaymentWorkorderView(Context context) {
        this(context, null, -1);
    }

    public PaymentWorkorderView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public PaymentWorkorderView(Context context, AttributeSet attrs, int defStyle) {
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
        _bundleIconFont = (IconFontTextView) findViewById(R.id.bundle_imageview);

        // center panel
        // title box
        _titleTextView = (TextView) findViewById(R.id.title_textview);
        // items
        _clientNameTextView = (TextView) findViewById(R.id.clientname_textview);
        _distanceTextView = (TextView) findViewById(R.id.distance_textview);
        _distanceTextView.setVisibility(GONE);
        _whenTextView = (TextView) findViewById(R.id.when_textview);

        // todo need to pick a button, and hide the others
        _actionGreenButton = (Button) findViewById(R.id.action_button_green);
        _actionGreenButton.setVisibility(GONE);

        _actionGrayButton = (Button) findViewById(R.id.action_button_gray);
        _actionGrayButton.setVisibility(GONE);

        _actionOrangeButton = (Button) findViewById(R.id.action_button_orange);
        _actionOrangeButton.setVisibility(GONE);

        _paymentTextView = (TextView) findViewById(R.id.payment_textview);
        _basisTextView = (TextView) findViewById(R.id.basis_textview);

        findViewById(R.id.location_textview).setVisibility(View.GONE);
        _workorderIdTextView = (TextView) findViewById(R.id.workorderid_textview);

        setIsBundle(false);

        _statusView.setBackgroundResource(R.drawable.card_status_green);
        _statusTextView.setTextColor(getContext().getResources().getColor(R.color.fn_white_text));
        setOnClickListener(_this_onClick);
    }

    private void setIsBundle(boolean isBundle) {
        if (isBundle) {
            _bundleIconFont.setVisibility(VISIBLE);
        } else {
            _bundleIconFont.setVisibility(GONE);
        }
    }

    public void setWorkorder(Payment payment, Workorder wo) {
        _workorder = wo;
        _titleTextView.setText(wo.getTitle());
        _clientNameTextView.setText(wo.getClientName());

        try {
            if (wo.getEndTime() != null) {
                String when = "";
                Calendar cal = ISO8601.toCalendar(wo.getEndTime());

                when = misc.formatDate(cal);

                _whenTextView.setVisibility(VISIBLE);
                _whenTextView.setText(when);
            } else {
                _whenTextView.setVisibility(GONE);
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            _whenTextView.setVisibility(GONE);
        }
        if (payment.getStatus().equals("paid")) {
            _statusView.setBackgroundResource(R.drawable.card_status_black);
        } else {
            _statusView.setBackgroundResource(R.drawable.card_status_green);
        }

        String paymethod = misc.capitalize(payment.getPayMethod().replaceAll("_", " "));
        _basisTextView.setText(paymethod);
        _paymentTextView.setText(misc.toCurrency(wo.getAmount()).substring(1));
        _statusTextView.setText(misc.capitalize(payment.getStatus()));
        _workorderIdTextView.setText("ID: " + _workorder.getWorkorderId());
    }

    private View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), WorkorderActivity.class);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, _workorder.getWorkorderId());
            getContext().startActivity(intent);
        }
    };

}
