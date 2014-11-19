package com.fieldnation.ui.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.util.Calendar;

/**
 * Created by michael.carver on 11/6/2014.
 */
public class ReasonCoView extends RelativeLayout {
    private static final String TAG = "ui.dialog.ReasonCoView";

    // Ui
    private EditText _requestReasonEditText;
    private CheckBox _expiresCheckBox;
    private Button _expiresButton;
    private CheckBox _tacCheckBox;
    private Button _tacButton;

    // Data
    private Listener _listener;
    private String _reason;
    private boolean _expires;
    private Calendar _expirationDate;
    private boolean _tacAccepted = false;

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
        _expiresCheckBox.setOnCheckedChangeListener(_expires_onChange);

        _expiresButton = (Button) findViewById(R.id.expires_button);
        _expiresButton.setOnClickListener(_expiresButton_onClick);

        _tacCheckBox = (CheckBox) findViewById(R.id.tac_checkbox);
        _tacCheckBox.setOnCheckedChangeListener(_tac_onChange);

        _tacButton = (Button) findViewById(R.id.tac_button);
        _tacButton.setOnClickListener(_tac_onClick);

        populateUi();
    }

    public String getReason() {
        return _requestReasonEditText.getText().toString();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setCounterOffer(String reason, boolean expires, String expirationDate) {
        _reason = reason;
        _expires = expires;

        try {
            if (expirationDate != null)
                _expirationDate = ISO8601.toCalendar(expirationDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        _expiresCheckBox.setChecked(_expires);
        if (_expires) {
            _expiresButton.setVisibility(View.VISIBLE);
        } else {
            _expiresButton.setVisibility(View.GONE);
        }
        if (_expirationDate != null) {
            _expiresButton.setText(misc.formatDateTime(_expirationDate, false));
        }

    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private CompoundButton.OnCheckedChangeListener _expires_onChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            _expires = isChecked;

            if (_expires && _listener != null)
                _listener.showDateTimePicker();

            populateUi();
        }
    };

    private CompoundButton.OnCheckedChangeListener _tac_onChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            _tacAccepted = isChecked;
            if (_listener != null)
                _listener.onTacChange(isChecked);
            populateUi();
        }
    };

    private View.OnClickListener _expiresButton_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.showDateTimePicker();
        }
    };

    private OnClickListener _tac_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onTacClick();
        }
    };

    public interface Listener {
        public void onTacClick();

        public void onTacChange(boolean isChecked);

        public void showDateTimePicker();
    }
}
