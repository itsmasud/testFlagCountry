package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class ExpiresDialog extends DialogFragmentBase {
    private static final String TAG = "ui.dialog.ExpiresDialog";

    // State
    private static final String STATE_WORKORDER = "STATE_WORKORDER";

    // Ui
    private Button _expirationButton;
    private Button _cancelButton;
    private Button _okButton;
    private DatePickerDialog _datePicker;
    private TimePickerDialog _timePicker;

    // Data
    private Calendar _calendar;
    private boolean _isDateSet;
    private Listener _listener;
    private Workorder _workorder;

    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public static ExpiresDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, ExpiresDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDER))
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);
        }
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_expiration, container, false);

        _expirationButton = (Button) v.findViewById(R.id.expiration_button);
        _expirationButton.setOnClickListener(_expiration_onClick);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);
        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        final Calendar c = Calendar.getInstance();
        _datePicker = DatePickerDialog.newInstance(_date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        _datePicker.setCloseOnSingleTapDay(true);
        _timePicker = TimePickerDialog.newInstance(_time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                false, false);

        _calendar = Calendar.getInstance();

        //getDialog().setTitle("Request Workorder");

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }


    public void show(Workorder workorder) {
        _isDateSet = false;
        _workorder = workorder;

        super.show();
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
                    _listener.onOk(_workorder, null);
                } else {
                    _listener.onOk(_workorder, ISO8601.fromCalendar(_calendar));
                }
            }
            dismiss();
        }
    };

    public interface Listener {
        public void onOk(Workorder workorder, String dateTime);
    }

}
