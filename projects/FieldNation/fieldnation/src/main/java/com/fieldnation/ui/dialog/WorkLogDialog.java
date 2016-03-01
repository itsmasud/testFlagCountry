package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.util.Calendar;

public class WorkLogDialog extends DialogFragmentBase {
    private static final String TAG = "WorkLogDialog";

    // State
    private static final String STATE_TITLE = "STATE_TITLE";
    private static final String STATE_LOGGEDWORK = "STATE_LOGGED_WORK";
    private static final String STATE_DEVICES_COUNT = "STATE_DEVICES_COUNT";
    private static final String STATE_START_DATE = "STATE_START_DATE";
    private static final String STATE_END_DATE = "STATE_END_DATE";

    // UI
    private Button _startButton;
    private Button _endButton;
    private LinearLayout _devicesLayout;
    private EditText _devicesEditText;
    private Button _okButton;
    private Button _cancelButton;
    private DatePickerDialog _datePicker;
    private TimePickerDialog _timePicker;

    // Data State
    private String _title;
    private LoggedWork _loggedWork;
    private boolean _showDevicesCount = false;

    // Data
    private Listener _listener;
    private Calendar _startCalendar;
    private Calendar _endCalendar;
    private boolean _startIsSet = false;
    private boolean _endIsSet = false;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public static WorkLogDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, WorkLogDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_TITLE))
                _title = savedInstanceState.getString(STATE_TITLE);

            if (savedInstanceState.containsKey(STATE_DEVICES_COUNT))
                _showDevicesCount = savedInstanceState.getBoolean(STATE_DEVICES_COUNT);

            if (savedInstanceState.containsKey(STATE_LOGGEDWORK))
                _loggedWork = savedInstanceState.getParcelable(STATE_LOGGEDWORK);

            if (savedInstanceState.containsKey(STATE_START_DATE))
                _startButton.setText(savedInstanceState.getString(STATE_START_DATE));

            if (savedInstanceState.containsKey(STATE_END_DATE))
                _endButton.setText(savedInstanceState.getString(STATE_END_DATE));
        }
        super.onViewStateRestored(savedInstanceState);

        populateUi();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_DEVICES_COUNT, _showDevicesCount);

        if (_title != null)
            outState.putString(STATE_TITLE, _title);

        if (_loggedWork != null)
            outState.putParcelable(STATE_LOGGEDWORK, _loggedWork);

        if (_startButton != null && !misc.isEmptyOrNull(_startButton.getText().toString()))
            outState.putString(STATE_START_DATE, _startButton.getText().toString());

        if (_endButton != null && !misc.isEmptyOrNull(_endButton.getText().toString()))
            outState.putString(STATE_END_DATE, _endButton.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_add_worklog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        _startButton = (Button) v.findViewById(R.id.start_spinner);
        _startButton.setOnClickListener(_start_onClick);

        _endButton = (Button) v.findViewById(R.id.end_spinner);
        _endButton.setOnClickListener(_end_onClick);

        _devicesLayout = (LinearLayout) v.findViewById(R.id.devices_layout);
        _devicesEditText = (EditText) v.findViewById(R.id.devices_edittext);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        final Calendar c = Calendar.getInstance();
        _datePicker = DatePickerDialog.newInstance(_date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        _datePicker.setCloseOnSingleTapDay(true);
        _timePicker = TimePickerDialog.newInstance(_time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false, false);

        _startCalendar = Calendar.getInstance();
        _endCalendar = Calendar.getInstance();

        return v;
    }

    @Override
    public void init() {
        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(CharSequence title, LoggedWork loggedWork, boolean showDeviceCount) {
        _startIsSet = false;
        _endIsSet = false;
        _loggedWork = loggedWork;
        _showDevicesCount = showDeviceCount;
        _title = (String) title;

        super.show();
    }

    private void populateUi() {
        if (_devicesLayout == null)
            return;

        if (_showDevicesCount) {
            _devicesLayout.setVisibility(View.VISIBLE);
        } else {
            _devicesLayout.setVisibility(View.GONE);
        }

        if (_loggedWork == null)
            return;

        try {
            String startDate = _loggedWork.getStartDate();
            _startCalendar = ISO8601.toCalendar(startDate);
            _startButton.setText(misc.formatDateTime(_startCalendar, false));
        } catch (ParseException ex) {
            Log.v(TAG, ex);
        }

        try {
            String endDate = _loggedWork.getEndDate();
            _endCalendar = ISO8601.toCalendar(endDate);
            _endButton.setText(misc.formatDateTime(_endCalendar, false));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        try {
            _devicesEditText.setText(_loggedWork.getNoOfDevices().toString());
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnClickListener _start_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _datePicker.show(_fm, "start");
        }
    };

    private final View.OnClickListener _end_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _datePicker.show(_fm, "end");
        }
    };

    private final DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
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

    private final TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {

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

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int deviceCount = -1;
            try {
                deviceCount = Integer.parseInt(_devicesEditText.getText().toString());
            } catch (Exception ex) {
            }

            if ((_startIsSet && _endIsSet)
                    || (_loggedWork != null && (_startIsSet || _endIsSet))
                    || (_loggedWork != null && _showDevicesCount && deviceCount != _loggedWork.getNoOfDevices())) {
                WorkLogDialog.this.dismiss();
                if (_listener != null) {
                    _listener.onOk(_loggedWork, _startCalendar, _endCalendar, deviceCount);
                }
            }
        }
    };
    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null)
                _listener.onCancel();
        }
    };

    public interface Listener {
        void onOk(LoggedWork loggedWork, Calendar start, Calendar end, int deviceCount);

        void onCancel();
    }
}
