package com.fieldnation.ui.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.fntools.misc;

/**
 * Created by michael.carver on 11/5/2014.
 */
public class PaymentCoView extends RelativeLayout {
    private static final String TAG = "PaymentCoView";

    // Ui
    private TextView _statusTextView;
    private TextView _body1TextView;
    private TextView _body2TextView;
    private Button _clearButton;
    private Button _changeButton;

    // Data
    private boolean _isCounterOffer;
    private Pay _pay;
    private Listener _listener;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/

    public PaymentCoView(Context context) {
        super(context);
        init();
    }

    public PaymentCoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaymentCoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_co_payment_tile, this);

        if (isInEditMode())
            return;

        _statusTextView = (TextView) findViewById(R.id.status_textview);
        _body1TextView = (TextView) findViewById(R.id.body1_textview);
        _body2TextView = (TextView) findViewById(R.id.body2_textview);

        _clearButton = (Button) findViewById(R.id.clear_button);
        _clearButton.setOnClickListener(_clear_onClick);
        _changeButton = (Button) findViewById(R.id.change_button);
        _changeButton.setOnClickListener(_change_onClick);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setPay(Pay pay, boolean isCounterOffer) {
        _pay = pay;
        _isCounterOffer = isCounterOffer;
        populateUi();
    }

    public Pay getPay() {
        return _pay;
    }

    private void populateUi() {
        if (_pay == null)
            return;

        _body2TextView.setVisibility(View.GONE);

        if (_pay.isFixedRate()) {
            _body1TextView.setText("Fixed pay of " + misc.toCurrencyTrim(_pay.getFixedAmount()));
        }

        // blended
        if (_pay.isBlendedRate()) {
            _body2TextView.setVisibility(View.VISIBLE);
            _body1TextView.setText("Blended " + misc.toCurrencyTrim(_pay.getBlendedStartRate()) + " for the first " + _pay.getBlendedFirstHours() + " hours");
            _body2TextView.setText(misc.toCurrencyTrim(_pay.getBlendedAdditionalRate()) + "/hour for up to " + _pay.getBlendedAdditionalHours() + " hours");
        }

        // hourly
        if (_pay.isHourlyRate()) {
            _body1TextView.setText("Hourly pay, " + misc.toCurrencyTrim(_pay.getPerHour()) + "/hour for up to " + _pay.getMaxHour() + " hours");
        }

        // per device
        if (_pay.isPerDeviceRate()) {
            _body1TextView.setText(misc.toCurrencyTrim(_pay.getPerDevice()) + "/device for up to " + _pay.getMaxDevice() + " devices");
        }

        if (_isCounterOffer) {
            _statusTextView.setText("Your Offer");
        } else {
            _statusTextView.setText("Buyer's Offer");
        }
    }

    /*-*************************-*/
    /*-         Events          -*/
    /*-*************************-*/
    private final View.OnClickListener _clear_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onClearClick();
        }
    };

    private final View.OnClickListener _change_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onChangeClick(_pay);
        }
    };

    public interface Listener {
        void onClearClick();

        void onChangeClick(Pay pay);
    }
}
