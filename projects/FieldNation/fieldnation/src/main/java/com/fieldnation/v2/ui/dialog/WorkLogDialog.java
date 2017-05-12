package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.ui.dialog.DatePickerDialog;
import com.fieldnation.ui.dialog.TimePickerDialog;
import com.fieldnation.v2.data.model.TimeLog;

import java.text.ParseException;
import java.util.Calendar;

public class WorkLogDialog extends SimpleDialog {
    private static final String TAG = "WorkLogDialog";

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
    private TimeLog _timeLog;
    private boolean _showDevicesCount = false;

    // Data
    private Calendar _startCalendar;
    private Calendar _endCalendar;
    private boolean _startIsSet = false;
    private boolean _endIsSet = false;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public WorkLogDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_add_worklog, container, false);

        _startButton = (Button) v.findViewById(R.id.start_spinner);
        _endButton = (Button) v.findViewById(R.id.end_spinner);

        _devicesLayout = (LinearLayout) v.findViewById(R.id.devices_layout);
        _devicesEditText = (EditText) v.findViewById(R.id.devices_edittext);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        final Calendar c = Calendar.getInstance();
        _datePicker = new DatePickerDialog(getView().getContext(), _date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        _timePicker = new TimePickerDialog(getView().getContext(), _time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);

        _startCalendar = Calendar.getInstance();
        _endCalendar = Calendar.getInstance();

        _startButton.setOnClickListener(_start_onClick);
        _endButton.setOnClickListener(_end_onClick);
        _okButton.setOnClickListener(_ok_onClick);
        _cancelButton.setOnClickListener(_cancel_onClick);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);
        _timeLog = payload.getParcelable("timeLog");
        _showDevicesCount = payload.getBoolean("showDeviceCount");

        populateUi();
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        _startIsSet = savedState.getBoolean("startIsSet");
        _endIsSet = savedState.getBoolean("endIsSet");
        _startCalendar.setTimeInMillis(savedState.getLong("startTime"));
        _endCalendar.setTimeInMillis(savedState.getLong("endTime"));

        super.onRestoreDialogState(savedState);

        populateUi();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        super.onSaveDialogState(outState);

        outState.putBoolean("startIsSet", _startIsSet);
        outState.putBoolean("endIsSet", _endIsSet);
        outState.putLong("startTime", _startCalendar.getTimeInMillis());
        outState.putLong("endTime", _endCalendar.getTimeInMillis());
    }

    private void populateUi() {
        if (_devicesLayout == null)
            return;

        if (_showDevicesCount) {
            _devicesLayout.setVisibility(View.VISIBLE);
        } else {
            _devicesLayout.setVisibility(View.GONE);
        }

        try {
            if (!_startIsSet && _timeLog != null && _timeLog.getIn() != null && _timeLog.getIn().getCreated() != null) {
                _startCalendar = _timeLog.getIn().getCreated().getCalendar();
                _startIsSet = true;
            }

            if (_startIsSet)
                _startButton.setText(DateUtils.formatDateTime(_startCalendar, false));
        } catch (ParseException ex) {
            Log.v(TAG, ex);
        }

        try {
            if (!_endIsSet && _timeLog != null && _timeLog.getOut() != null && _timeLog.getOut().getCreated() != null) {
                _endCalendar = _timeLog.getOut().getCreated().getCalendar();
                _endIsSet = true;
            }

            if (_endIsSet)
                _endButton.setText(DateUtils.formatDateTime(_endCalendar, false));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        try {
            if (_timeLog != null && _timeLog.getDevices() != null)
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
                dismiss(true);
                _onOkDispatcher.dispatch(getUid(), _timeLog, _startCalendar, _endCalendar, deviceCount);
            }
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
            _onCancelDispatcher.dispatch(getUid());
        }
    };

    public static void show(Context context, String uid, TimeLog timeLog, boolean showDeviceCount) {
        Bundle params = new Bundle();
        params.putParcelable("timeLog", timeLog);
        params.putBoolean("showDeviceCount", showDeviceCount);
        Controller.show(context, uid, WorkLogDialog.class, params);
    }

    /*-**********************-*/
    /*-         Ok           -*/
    /*-**********************-*/
    public interface OnOkListener {
        void onOk(TimeLog timeLog, Calendar start, Calendar end, int deviceCount);
    }

    private static KeyedDispatcher<WorkLogDialog.OnOkListener> _onOkDispatcher = new KeyedDispatcher<WorkLogDialog.OnOkListener>() {
        @Override
        public void onDispatch(WorkLogDialog.OnOkListener listener, Object... parameters) {
            listener.onOk((TimeLog) parameters[0], (Calendar) parameters[1], (Calendar) parameters[2], (Integer) parameters[3]);
        }
    };

    public static void addOnOkListener(String uid, WorkLogDialog.OnOkListener onOkListener) {
        _onOkDispatcher.add(uid, onOkListener);
    }

    public static void removeOnOkListener(String uid, WorkLogDialog.OnOkListener onOkListener) {
        _onOkDispatcher.remove(uid, onOkListener);
    }

    public static void removeAllOnOkListener(String uid) {
        _onOkDispatcher.removeAll(uid);
    }

    /*-**************************-*/
    /*-         Cancel           -*/
    /*-**************************-*/
    public interface OnCancelListener {
        void onCancel();
    }

    private static KeyedDispatcher<WorkLogDialog.OnCancelListener> _onCancelDispatcher = new KeyedDispatcher<WorkLogDialog.OnCancelListener>() {
        @Override
        public void onDispatch(WorkLogDialog.OnCancelListener listener, Object... parameters) {
            listener.onCancel();
        }
    };

    public static void addOnCancelListener(String uid, WorkLogDialog.OnCancelListener onCancelListener) {
        _onCancelDispatcher.add(uid, onCancelListener);
    }

    public static void removeOnCancelListener(String uid, WorkLogDialog.OnCancelListener onCancelListener) {
        _onCancelDispatcher.remove(uid, onCancelListener);
    }

    public static void removeAllOnCancelListener(String uid) {
        _onCancelDispatcher.removeAll(uid);
    }

}
