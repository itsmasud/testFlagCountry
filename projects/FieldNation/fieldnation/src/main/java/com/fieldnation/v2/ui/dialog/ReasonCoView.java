package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fieldnation.R;
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
    private CheckBox _tacCheckBox;
    private Button _tacButton;

    // Data
    private Listener _listener;
    private String _reason;
    private long _expires;
    private boolean _tacAccepted = false;
    private boolean _reset = false;
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
        LayoutInflater.from(getContext()).inflate(R.layout.view_co_reasons_tile, this);

        if (isInEditMode())
            return;

        _requestReasonEditText = (EditText) findViewById(R.id.request_reason_edittext);

        _expiresCheckBox = (CheckBox) findViewById(R.id.expires_checkbox);
        _expiresCheckBox.setOnClickListener(_expires_onClick);

        _expireDurationSpinner = (HintSpinner) findViewById(R.id.expire_duration_spinner);
        _expireDurationSpinner.setOnItemSelectedListener(_expireSpinner_selected);
        HintArrayAdapter adapter = HintArrayAdapter.createFromResources(getContext(), R.array.expire_duration_titles,
                R.layout.view_counter_offer_reason_spinner_item);
        adapter.setDropDownViewResource(android.support.design.R.layout.support_simple_spinner_dropdown_item);
        _expireDurationSpinner.setAdapter(adapter);

        _tacCheckBox = (CheckBox) findViewById(R.id.tac_checkbox);
        _tacCheckBox.setOnClickListener(_tacCheck_onClick);

        _tacButton = (Button) findViewById(R.id.tac_button);
        _tacButton.setOnClickListener(_tac_onClick);

        _durations = getContext().getResources().getIntArray(R.array.expire_duration_values);

        populateUi();
    }

    public String getReason() {
        return _requestReasonEditText.getText().toString();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setCounterOffer(String reason, long expires) {
        _reset = true;
        _reason = reason;
        _expires = expires;

        int index = 0;
        // find the nearest duration
        if (expires > 0) {
            long durationSecounds = Math.abs(expires - System.currentTimeMillis()) / 1000;

            long diff = Integer.MAX_VALUE;

            for (int i = 0; i < _durations.length; i++) {
                if (diff > Math.abs(durationSecounds - _durations[i])) {
                    index = i;
                    diff = Math.abs(durationSecounds - _durations[i]);
                }
            }
        }
        _expireDurationSpinner.setSelection(index);

        populateUi();
    }

    private void populateUi() {
        if (_tacButton == null)
            return;

        // reason
        if (_reason != null) {
            _requestReasonEditText.setText(_reason);
        }

        // expiration stuff
        _expiresCheckBox.setChecked(_expires > 0);

        if (_reset) {
            _reset = false;
            _tacCheckBox.setChecked(false);
        }
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final View.OnClickListener _expires_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_expiresCheckBox.isChecked() && _listener != null) {
                _listener.onExpirationChange(System.currentTimeMillis() + _durations[_currentPosition] * 1000);
            }
        }
    };

    private final AdapterView.OnItemSelectedListener _expireSpinner_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _currentPosition = position;

            if (_listener != null) {
                _listener.onExpirationChange(System.currentTimeMillis() + (_durations[_currentPosition] * 1000));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            _currentPosition = 1;
            if (_listener != null) {
                _listener.onExpirationChange(System.currentTimeMillis() + (_durations[_currentPosition] * 1000));
            }
        }
    };

    private final View.OnClickListener _tacCheck_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            _tacAccepted = _tacCheckBox.isChecked();
            if (_listener != null)
                _listener.onTacChange(_tacAccepted);
        }
    };

    private final View.OnClickListener _tac_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onTacClick();
        }
    };

    public interface Listener {
        void onTacClick();

        void onTacChange(boolean isChecked);

        void onExpirationChange(long expires);
    }
}