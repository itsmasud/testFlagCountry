package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Pay;

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

        _body1TextView.setVisibility(VISIBLE);
        _body2TextView.setVisibility(View.GONE);

        try {
            switch (_pay.getType()) {
                case BLENDED:
                    _body2TextView.setVisibility(View.VISIBLE);
                    _body1TextView.setText("Blended " + misc.toCurrencyTrim(_pay.getBase().getAmount()) + " for the first " + _pay.getBase().getUnits() + " hours");
                    _body2TextView.setText(misc.toCurrencyTrim(_pay.getAdditional().getAmount()) + "/hour for up to " + _pay.getAdditional().getUnits() + " hours");
                    break;
                case DEVICE:
                    _body1TextView.setText(misc.toCurrencyTrim(_pay.getBase().getAmount()) + "/device for up to " + _pay.getBase().getUnits() + " devices");
                    break;
                case FIXED:
                    _body1TextView.setText("Fixed pay of " + misc.toCurrencyTrim(_pay.getBase().getAmount()));
                    break;
                case HOURLY:
                    _body1TextView.setText("Hourly pay, " + misc.toCurrencyTrim(_pay.getBase().getAmount()) + "/hour for up to " + _pay.getBase().getUnits() + " hours");
                    break;
            }

            _statusTextView.setVisibility(VISIBLE);
            if (_isCounterOffer) {
                _statusTextView.setText("Your Offer");
            } else {
                _statusTextView.setText("Buyer's Offer");
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            _statusTextView.setVisibility(GONE);
            _body1TextView.setVisibility(GONE);
            _body2TextView.setVisibility(GONE);
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
