package com.fieldnation.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.fieldnation.R;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.util.Calendar;

public class WorkLogDialog extends Dialog {
    private static final String TAG = "ui.WorkLogDialog";

    // UI
    private Button _startButton;
    private Button _endButton;
    private LinearLayout _devicesLayout;
    private EditText _devicesEditText;
    private Button _okButton;
    private Button _cancelButton;
    private DatePickerDialog _datePicker;
    private TimePickerDialog _timePicker;

    // Data
    private FragmentManager _fm;
    private Calendar _startCalendar;
    private Calendar _endCalendar;
    private boolean _startIsSet = false;
    private boolean _endIsSet = false;
    private Listener _listener;
    private LoggedWork _loggedWork;
    private boolean _showDevicesCount;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public WorkLogDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_add_worklog);

        _startButton = (Button) findViewById(R.id.start_spinner);
        _startButton.setOnClickListener(_start_onClick);

        _endButton = (Button) findViewById(R.id.end_spinner);
        _endButton.setOnClickListener(_end_onClick);

        _devicesLayout = (LinearLayout) findViewById(R.id.devices_layout);
        _devicesEditText = (EditText) findViewById(R.id.devices_edittext);

        _okButton = (Button) findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        _cancelButton = (Button) findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);


        final Calendar c = Calendar.getInstance();
        _datePicker = DatePickerDialog.newInstance(_date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        _datePicker.setCloseOnSingleTapDay(true);
        _timePicker = TimePickerDialog.newInstance(_time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                false, false);

        _startCalendar = Calendar.getInstance();
        _endCalendar = Calendar.getInstance();
    }

    public void show(FragmentManager fm, int titleResId, LoggedWork loggedWork, boolean showDeviceCount,
                     Listener listener) {
        show(fm, getContext().getText(titleResId),loggedWork, showDeviceCount, listener);
    }

    public void show(FragmentManager fm, CharSequence title, LoggedWork loggedWork, boolean showDeviceCount,
                     Listener listener) {
        _startIsSet = false;
        _endIsSet = false;
        _listener = listener;
        _loggedWork = loggedWork;

        _fm = fm;
        _showDevicesCount = showDeviceCount;
        setTitle(title);

        if (showDeviceCount) {
            _devicesLayout.setVisibility(View.VISIBLE);
        } else {
            _devicesLayout.setVisibility(View.GONE);
        }

        if (_loggedWork != null) {
            try {
                String startDate = _loggedWork.getStartDate();
                _startButton.setText(misc.formatDateTime(ISO8601.toCalendar(startDate), false));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            try {
                String endDate = _loggedWork.getEndDate();
                _endButton.setText(misc.formatDateTime(ISO8601.toCalendar(endDate), false));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                _devicesEditText.setText(_loggedWork.getNoOfDevices().toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        show();
    }


    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private View.OnClickListener _start_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _datePicker.show(_fm, "start");
        }
    };

    private View.OnClickListener _end_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _datePicker.show(_fm, "end");
        }
    };

    private DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            String tag = datePickerDialog.getTag();
            if (tag.equals("start")) {
                _startCalendar.set(year, month, day);
            } else if (tag.equals("end")) {
                _endCalendar.set(year, month, day);
            }

            _timePicker.show(_fm, datePickerDialog.getTag());
        }
    };

    private TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute) {
            String tag = view.getTag();
            if (tag.equals("start")) {
                _startCalendar.set(_startCalendar.get(Calendar.YEAR), _startCalendar.get(Calendar.MONTH),
                        _startCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                _startIsSet = true;
                _startButton.setText(misc.formatDateTime(_startCalendar, false));
            } else if (tag.equals("end")) {
                _endCalendar.set(_endCalendar.get(Calendar.YEAR), _endCalendar.get(Calendar.MONTH),
                        _endCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                _endIsSet = true;
                _endButton.setText(misc.formatDateTime(_endCalendar, false));
            }
        }
    };

    private View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int deviceCount = -1;
            try {
                deviceCount = Integer.parseInt(_devicesEditText.getText().toString());
            } catch (Exception ex) {
            }

            if ((_startIsSet && _endIsSet) || (_loggedWork != null && (_startIsSet || _endIsSet)) || (_loggedWork != null && _showDevicesCount && deviceCount != _loggedWork.getNoOfDevices())) {
                WorkLogDialog.this.dismiss();
                if (_listener != null) {
                    _listener.onOk(_startCalendar, _endCalendar, deviceCount);
                }
            }
        }
    };
    private View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null)
                _listener.onCancel();
        }
    };

    public interface Listener {
        public void onOk(Calendar start, Calendar end, int deviceCount);

        public void onCancel();
    }
}
