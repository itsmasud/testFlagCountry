package com.fieldnation.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import com.fieldnation.R;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class ExpiresDialog extends Dialog {
    private static final String TAG = "ui.dialog.ExpiresDialog";

    // Ui
    private Button _expirationButton;
    private Button _cancelButton;
    private Button _okButton;
    private DatePickerDialog _datePicker;
    private TimePickerDialog _timePicker;

    // Data
    private FragmentManager _fm;
    private Calendar _calendar;
    private boolean _isDateSet;
    private Listener _listener;

    public ExpiresDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_expiration);

        _expirationButton = (Button) findViewById(R.id.expiration_button);
        _expirationButton.setOnClickListener(_expiration_onClick);
        _cancelButton = (Button) findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);
        _okButton = (Button) findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        final Calendar c = Calendar.getInstance();
        _datePicker = DatePickerDialog.newInstance(_date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        _datePicker.setCloseOnSingleTapDay(true);
        _timePicker = TimePickerDialog.newInstance(_time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                false, false);

        _calendar = Calendar.getInstance();

        setTitle("When does the offer expire?");
    }

    public void show(FragmentManager fm, Listener listener) {
        _fm = fm;
        _listener = listener;
        _isDateSet = false;
        _expirationButton.setText("Never");
        show();
    }

	/*-*************************-*/
    /*-			Events			-*/
	/*-*************************-*/

    private DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            _calendar.set(year, month, day);

            _timePicker.show(_fm, null);
        }
    };

    private TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute) {
            _calendar.set(_calendar.get(Calendar.YEAR), _calendar.get(Calendar.MONTH),
                    _calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

            _expirationButton.setText(misc.formatDateTimeLong(_calendar));
            _isDateSet = true;
        }
    };

    private View.OnClickListener _expiration_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _datePicker.show(_fm, null);
        }
    };

    private View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                if (_isDateSet) {
                    _listener.onOk(null);
                } else {
                    _listener.onOk(ISO8601.fromCalendar(_calendar));
                }
            }
            dismiss();
        }
    };

    public interface Listener {
        public void onOk(String dateTime);
    }

}
