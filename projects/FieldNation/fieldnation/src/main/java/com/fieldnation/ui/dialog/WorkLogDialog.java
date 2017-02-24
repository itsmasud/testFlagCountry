package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.fieldnation.R;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.TimeLog;

import java.text.ParseException;
import java.util.Calendar;

public class WorkLogDialog extends DialogFragmentBase {
    private static final String TAG = "WorkLogDialog";

    // State
    private static final String STATE_TITLE = "STATE_TITLE";
    private static final String STATE_TIMELOG = "STATE_LOGGED_WORK";
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
    private TimeLog _timeLog;
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

            if (savedInstanceState.containsKey(STATE_TIMELOG))
                _timeLog = savedInstanceState.getParcelable(STATE_TIMELOG);

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

        if (_timeLog != null)
            outState.putParcelable(STATE_TIMELOG, _timeLog);

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
        _datePicker = new DatePickerDialog(getActivity(), _date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        _timePicker = new TimePickerDialog(getActivity(), _time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);

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

    public void show(CharSequence title, TimeLog timeLog, boolean showDeviceCount) {
        _startIsSet = false;
        _endIsSet = false;
        _timeLog = timeLog;
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

        if (_timeLog == null)
            return;

        try {
            _startCalendar = _timeLog.getIn().getCreated().getCalendar();
            _startButton.setText(DateUtils.formatDateTime(_startCalendar, false));
        } catch (ParseException ex) {
            Log.v(TAG, ex);
        }

        try {
            _endCalendar = _timeLog.getOut().getCreated().getCalendar();
            _endButton.setText(DateUtils.formatDateTime(_endCalendar, false));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        try {
            _devicesEditText.setText(_timeLog.getDevices().toString());
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
            _datePicker.setTag("start");
            _datePicker.show();
        }
    };

    private final View.OnClickListener _end_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _datePicker.setTag("end");
            _datePicker.show();
        }
    };

    private final DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String tag = (String) _datePicker.getTag();
            if (tag.equals("start")) {
                _startCalendar.set(year, monthOfYear, dayOfMonth);
            } else if (tag.equals("end")) {
                _endCalendar.set(year, monthOfYear, dayOfMonth);
            }

            _timePicker.setTag(_datePicker.getTag());
            _timePicker.show();
        }
    };

    private final TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String tag = (String) _timePicker.getTag();
            if (tag.equals("start")) {
                _startCalendar.set(_startCalendar.get(Calendar.YEAR), _startCalendar.get(Calendar.MONTH),
                        _startCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                _startIsSet = true;
                _startButton.setText(DateUtils.formatDateTime(_startCalendar, false));
            } else if (tag.equals("end")) {
                _endCalendar.set(_endCalendar.get(Calendar.YEAR), _endCalendar.get(Calendar.MONTH),
                        _endCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                _endIsSet = true;
                _endButton.setText(DateUtils.formatDateTime(_endCalendar, false));
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
                    || (_timeLog != null && (_startIsSet || _endIsSet))
                    || (_timeLog != null && _showDevicesCount && deviceCount != _timeLog.getDevices())) {
                WorkLogDialog.this.dismiss();
                if (_listener != null) {
                    _listener.onOk(_timeLog, _startCalendar, _endCalendar, deviceCount);
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
        void onOk(TimeLog timeLog, Calendar start, Calendar end, int deviceCount);

        void onCancel();
    }
}
