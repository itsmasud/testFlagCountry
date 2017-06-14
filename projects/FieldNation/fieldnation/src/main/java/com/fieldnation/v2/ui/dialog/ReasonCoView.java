package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnactivityresult.ActivityResultClient;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;

/**
 * Created by michael.carver on 11/6/2014.
 */
public class ReasonCoView extends RelativeLayout {
    private static final String TAG = "ReasonCoView";

    // Ui
    private EditText _requestReasonEditText;
    private CheckBox _expiresCheckBox;
    private HintSpinner _expireDurationSpinner;
    private TextView _termsWarningTextView;

    // Data
    private Listener _listener;
    private int _currentPosition = 1;
    private int[] _durations;

    public ReasonCoView(Context context) {
        super(context);
        init();
    }

    public ReasonCoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReasonCoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Log.v(TAG, "init");
        LayoutInflater.from(getContext()).inflate(R.layout.view_co_reasons_tile, this);

        if (isInEditMode())
            return;

        _requestReasonEditText = (EditText) findViewById(R.id.request_reason_edittext);

        _expiresCheckBox = (CheckBox) findViewById(R.id.expires_checkbox);
        _expiresCheckBox.setOnClickListener(_expires_onClick);

        _expireDurationSpinner = (HintSpinner) findViewById(R.id.expire_duration_spinner);
        _expireDurationSpinner.setOnItemSelectedListener(_expireSpinner_selected);
        HintArrayAdapter adapter = HintArrayAdapter.createFromResources(getContext(),
                R.array.expire_duration_titles,
                R.layout.view_counter_offer_reason_spinner_item);
        adapter.setDropDownViewResource(android.support.design.R.layout.support_simple_spinner_dropdown_item);
        _expireDurationSpinner.setAdapter(adapter);
        _expireDurationSpinner.setSelection(1);

        _termsWarningTextView = (TextView) findViewById(R.id.termswarning_textview);
        _termsWarningTextView.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString spanned = new SpannableString("By countering this work order, I understand and agree to the Buyer's work order terms, the Standard Work Order Terms and Conditions and the Provider Quality Assurance Policy. I also understand that I am committing myself to complete this work order at the designated date and time and that failure to do so can result in non-payment or deactivation from the platform.");
        spanned.setSpan(_standardTerms_onClick, 91, 131, spanned.getSpanFlags(_standardTerms_onClick));
        spanned.setSpan(_pqap_onClick, 140, 173, spanned.getSpanFlags(_pqap_onClick));
        _termsWarningTextView.setText(spanned);

        _durations = getContext().getResources().getIntArray(R.array.expire_duration_values);
    }

    public String getReason() {
        Log.v(TAG, "getReason");
        return _requestReasonEditText.getText().toString();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setCounterOffer(String reason, long expires) {
        Log.v(TAG, "setCounterOffer( " + reason + ", " + expires + ")");

        if (reason != null)
            _requestReasonEditText.setText(reason);


        int index = 1;
        // find the nearest duration
        if (expires > 0) {
            long durationSeconds = Math.abs(expires - System.currentTimeMillis()) / 1000;

            long diff = Integer.MAX_VALUE;

            for (int i = 0; i < _durations.length; i++) {
                if (diff > Math.abs(durationSeconds - _durations[i])) {
                    index = i;
                    diff = Math.abs(durationSeconds - _durations[i]);
                }
            }
            _expireDurationSpinner.setSelection(index);
            _expiresCheckBox.setChecked(true);
        } else {
            _expiresCheckBox.setChecked(false);
        }
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final View.OnClickListener _expires_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "_expires_onClick");

            if (_listener != null) {
                if (_expiresCheckBox.isChecked())
                    _listener.onExpirationChange(System.currentTimeMillis() + _durations[_currentPosition] * 1000);
                else
                    _listener.onExpirationChange(0);
            }
        }
    };

    private final AdapterView.OnItemSelectedListener _expireSpinner_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.v(TAG, "_expireSpinner_selected.onItemSelected");

            if (position != _currentPosition) {
                _currentPosition = position;

                _expiresCheckBox.setChecked(true);
                if (_listener != null) {
                    if (_expiresCheckBox.isChecked())
                        _listener.onExpirationChange(System.currentTimeMillis() + (_durations[_currentPosition] * 1000));
                    else
                        _listener.onExpirationChange(0);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Log.v(TAG, "_expireSpinner_selected.onNothingSelected");
            _currentPosition = 1;
            if (_listener != null) {
                if (_expiresCheckBox.isChecked())
                    _listener.onExpirationChange(System.currentTimeMillis() + (_durations[_currentPosition] * 1000));
                else
                    _listener.onExpirationChange(0);
            }
        }
    };

    private final ClickableSpan _standardTerms_onClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://app.fieldnation.com/legal/?a=workorder"));
            ActivityResultClient.startActivity(App.get(), intent);
        }
    };

    private final ClickableSpan _pqap_onClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://app.fieldnation.com/legal/?a=qualityassurance"));
            ActivityResultClient.startActivity(App.get(), intent);
        }
    };

    public interface Listener {
        void onExpirationChange(long expires);
    }
}