package com.fieldnation.ui.dialog;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by michael.carver on 11/6/2014.
 */
public class ReasonCoView extends RelativeLayout {
    private static final String TAG = "ReasonCoView";

    // Ui
    private EditText _requestReasonEditText;
    private CheckBox _expiresCheckBox;
    private Button _expiresButton;
    private CheckBox _tacCheckBox;
    private Button _tacButton;

    // Dialogs
    private DatePickerDialog _datePicker;
    private TimePickerDialog _timePicker;

    // Data
    private FragmentManager _fm;
    private Listener _listener;
    private String _reason;
    private boolean _expires;
    private Calendar _expirationDate;
    private boolean _tacAccepted = false;
    private boolean _reset = false;
    private Calendar _pickerCal;

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

        _expiresButton = (Button) findViewById(R.id.expires_button);
        _expiresButton.setOnClickListener(_expiresButton_onClick);

        _tacCheckBox = (CheckBox) findViewById(R.id.tac_checkbox);
        _tacCheckBox.setOnClickListener(_tacCheck_onClick);

        _tacButton = (Button) findViewById(R.id.tac_button);
        _tacButton.setOnClickListener(_tac_onClick);

        _pickerCal = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        _datePicker = DatePickerDialog.newInstance(_date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        _datePicker.setCloseOnSingleTapDay(true);
        _timePicker = TimePickerDialog.newInstance(_time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                false, false);

        populateUi();
    }

    public String getReason() {
        return _requestReasonEditText.getText().toString();
    }

    public String getExpiration() {
        try {
            return ISO8601.fromCalendar(_expirationDate);
        } catch (Exception ex) {
            return null;
        }
    }

    public void setListener(FragmentManager fm, Listener listener) {
        _listener = listener;
        _fm = fm;
    }

    public void setCounterOffer(String reason, boolean expires, String expirationDate) {
        _reset = true;
        _reason = reason;
        _expires = expires;

        try {
            if (expirationDate != null)
                _expirationDate = ISO8601.toCalendar(expirationDate);
        } catch (Exception e) {
            Log.v(TAG, e);
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
            _expiresCheckBox.setChecked(true);
            _expiresButton.setVisibility(View.VISIBLE);
        } else {
            _expiresCheckBox.setChecked(false);
            _expiresButton.setVisibility(View.GONE);
        }
        if (_expirationDate != null) {
            _expiresButton.setText(misc.formatDateTime(_expirationDate, false));
        }

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
            if (_datePicker.isAdded())
                return;

            _expires = _expiresCheckBox.isChecked();

            if (_expires) {
                _expiresCheckBox.setChecked(false);
                _datePicker.show(_fm, TAG);
            } else if (_listener != null) {
                _listener.onExpirationChange(false, null);
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

    private final View.OnClickListener _expiresButton_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_datePicker.isAdded())
                return;

            if (_listener != null)
                _datePicker.show(_fm, TAG);
        }
    };

    private final View.OnClickListener _tac_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onTacClick();
        }
    };

    private final DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            _pickerCal.set(year, month, day);

            if (!_timePicker.isAdded())
                _timePicker.show(_fm, datePickerDialog.getTag());
        }
    };

    private final TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute) {
            String tag = view.getTag();
            _pickerCal.set(_pickerCal.get(Calendar.YEAR), _pickerCal.get(Calendar.MONTH),
                    _pickerCal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

            _expirationDate = (Calendar) _pickerCal.clone();
            _expires = true;
            if (_listener != null)
                _listener.onExpirationChange(_expires, ISO8601.fromCalendar(_expirationDate));
            populateUi();
        }
    };


    public interface Listener {
        void onTacClick();

        void onTacChange(boolean isChecked);

        void onExpirationChange(boolean expires, String date);
    }
}
