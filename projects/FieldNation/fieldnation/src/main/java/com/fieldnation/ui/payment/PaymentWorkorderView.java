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
    private static final String TAG = "PaymentWorkorderView";

    // UI
    private IconFontTextView _bundleIconFontView;
    private TextView _titleTextView;
    private TextView _companyNameTextView;
    private TextView _workorderIdTextView;
    private TextView _timeTextView;
    private TextView _priceTextView;
    private TextView _extraTextView;
    private TextView _stateTextView;
    private Button _leftButton;
    private Button _rightWhiteButton;
    private Button _rightOrangeButton;
    private Button _rightGreenButton;

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

        _bundleIconFontView = (IconFontTextView) findViewById(R.id.bundle_iconFont);
        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _companyNameTextView = (TextView) findViewById(R.id.companyName_textview);
        _workorderIdTextView = (TextView) findViewById(R.id.workorderId_textview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _priceTextView = (TextView) findViewById(R.id.price_textview);
        _extraTextView = (TextView) findViewById(R.id.extra_textview);
        _stateTextView = (TextView) findViewById(R.id.status_textview);

        findViewById(R.id.left_button).setVisibility(GONE);
        findViewById(R.id.rightWhite_button).setVisibility(GONE);
        findViewById(R.id.rightGreen_button).setVisibility(GONE);
        findViewById(R.id.rightOrange_button).setVisibility(GONE);

        setIsBundle(false);
/*
        findViewById(R.id.location_textview).setVisibility(View.GONE);
        _statusView.setBackgroundResource(R.drawable.card_status_green);
        _statusTextView.setTextColor(getContext().getResources().getColor(R.color.fn_white_text));
*/
        setOnClickListener(_this_onClick);
    }

    private void setIsBundle(boolean isBundle) {
        if (isBundle) {
            _bundleIconFontView.setVisibility(VISIBLE);
        } else {
            _bundleIconFontView.setVisibility(GONE);
        }
    }

    public void setWorkorder(Payment payment, Workorder wo) {
        _workorder = wo;
        _titleTextView.setText(wo.getTitle());
        _companyNameTextView.setText(wo.getClientName());

        try {
            if (wo.getEndTime() != null) {
                String when = "";
                Calendar cal = ISO8601.toCalendar(wo.getEndTime());

                when = misc.formatDate(cal);

                _timeTextView.setVisibility(VISIBLE);
                _timeTextView.setText(when);
            } else {
                _timeTextView.setVisibility(INVISIBLE);
            }
        } catch (Exception ex) {
            // Log.v(TAG, ex);
            _timeTextView.setVisibility(INVISIBLE);
        }

        if (payment.getPayMethod() != null) {
            String paymethod = misc.capitalize(payment.getPayMethod().replaceAll("_", " "));
            _stateTextView.setText(paymethod);
        } else {
            _stateTextView.setVisibility(View.INVISIBLE);
        }
        _priceTextView.setText(misc.toCurrency(wo.getAmount()));
        _stateTextView.setText(misc.capitalize(payment.getStatus()));
        _workorderIdTextView.setText("WO ID: " + _workorder.getWorkorderId());
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
