package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.ui.dialog.DatePickerDialog;
import com.fieldnation.ui.dialog.TimePickerDialog;
import com.fieldnation.v2.data.model.Date;
import com.fieldnation.v2.data.model.Schedule;
import com.fieldnation.v2.data.model.ScheduleServiceWindow;

import java.util.Calendar;

public class ScheduleDialog extends SimpleDialog {
    private static final String TAG = "ScheduleDialog";

    // State
    private static final String STATE_SCHEDULE = "STATE_SCHEDULE";
    private static final String STATE_SPINNER = "STATE_SPINNER";
    private static final String STATE_FIXED_DATETIME = "STATE_FIXED_DATETIME";
    private static final String STATE_RANGE_DATETIME_START = "STATE_RANGE_DATETIME_START";
    private static final String STATE_RANGE_DATETIME_END = "STATE_RANGE_DATETIME_END";

    // Modes
    private static final int MODE_RANGE = 0;
    private static final int MODE_EXACT = 1;

    // UI
    private HintSpinner _typeSpinner;
    private LinearLayout _rangeLayout;
    private Button _startDateButton;
    private Button _endDateButton;
    private LinearLayout _exactLayout;
    private Button _dateTimeButton;
    private Button _cancelButton;
    private Button _okButton;

    private DatePickerDialog _datePicker;
    private TimePickerDialog _timePicker;

    // Data
    private int _mode = 1;
    private Schedule _sched;
    private Calendar _startCal;
    private Calendar _endCal;
    private boolean _startIsSet = false;
    private boolean _endIsSet = false;
    private String _stateFixedDateTime;
    private String _stateRangeStartDateTime;
    private String _stateRangeEndDateTime;
    private final Handler _handler = new Handler();

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public ScheduleDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_schedule, container, false);

        _typeSpinner = (HintSpinner) v.findViewById(R.id.type_spinner);
        _rangeLayout = (LinearLayout) v.findViewById(R.id.range_layout);
        _exactLayout = (LinearLayout) v.findViewById(R.id.exact_layout);
        _startDateButton = (Button) v.findViewById(R.id.start_date_button);
        _endDateButton = (Button) v.findViewById(R.id.end_date_button);
        _dateTimeButton = (Button) v.findViewById(R.id.date_time_button);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _okButton = (Button) v.findViewById(R.id.ok_button);

        final Calendar c = Calendar.getInstance();
        _datePicker = new DatePickerDialog(context, _date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        _timePicker = new TimePickerDialog(context, _time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);

        _startCal = Calendar.getInstance();
        _endCal = Calendar.getInstance();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _typeSpinner.setOnItemSelectedListener(_type_selected);
        _startDateButton.setOnClickListener(_startDateButton_onClick);
        _endDateButton.setOnClickListener(_endDateButton_onClick);
        _dateTimeButton.setOnClickListener(_dateTimeButton_onClick);
        _cancelButton.setOnClickListener(_cancelButton_onClick);
        _okButton.setOnClickListener(_okButton_onClick);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        _sched = payload.getParcelable("schedule");

        super.show(payload, animate);

        populateUi();
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState == null) {
            return;
        }
        if (savedState.containsKey(STATE_SCHEDULE)) {
            _sched = savedState.getParcelable(STATE_SCHEDULE);
        }
        if (savedState.containsKey(STATE_SPINNER)) {
            _mode = savedState.getInt(STATE_SPINNER);
        }
        if (savedState.containsKey(STATE_FIXED_DATETIME)) {
            _stateFixedDateTime = savedState.getString(STATE_FIXED_DATETIME);
        }
        if (savedState.containsKey(STATE_RANGE_DATETIME_START)) {
            _stateRangeStartDateTime = savedState.getString(STATE_RANGE_DATETIME_START);
        }
        if (savedState.containsKey(STATE_RANGE_DATETIME_END)) {
            _stateRangeEndDateTime = savedState.getString(STATE_RANGE_DATETIME_END);
        }
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_sched != null) {
            outState.putParcelable(STATE_SCHEDULE, _sched);
        }

        outState.putInt(STATE_SPINNER, _mode);

        if (_dateTimeButton != null && !misc.isEmptyOrNull(_dateTimeButton.getText().toString())) {
            outState.putString(STATE_FIXED_DATETIME, _dateTimeButton.getText().toString());
            Log.e(TAG, "STATE_FIXED_DATETIME: " + _dateTimeButton.getText().toString());
        }

        if (_startDateButton != null && !misc.isEmptyOrNull(_startDateButton.getText().toString())) {
            outState.putString(STATE_RANGE_DATETIME_START, _startDateButton.getText().toString());
        }

        if (_endDateButton != null && !misc.isEmptyOrNull(_endDateButton.getText().toString())) {
            outState.putString(STATE_RANGE_DATETIME_END, _endDateButton.getText().toString());
        }
    }

    private Schedule makeSchedule() {
        Schedule schedule = new Schedule();
        try {
            switch (_mode) {
                case MODE_EXACT:
                    schedule.setServiceWindow(new ScheduleServiceWindow()
                            .mode(ScheduleServiceWindow.ModeEnum.EXACT)
                            .start(new Date(_startCal)));
                    return schedule;
                case MODE_RANGE:
                    schedule.setServiceWindow(new ScheduleServiceWindow()
                            .mode(ScheduleServiceWindow.ModeEnum.BETWEEN)
                            .start(new Date(_startCal))
                            .end(new Date(_endCal)));
                    return schedule;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    private void populateUi() {
        Log.e(TAG, "populateUi");
        if (_typeSpinner == null)
            return;

        HintArrayAdapter adapter = HintArrayAdapter.createFromResources(
                _typeSpinner.getContext(),
                R.array.schedule_types,
                R.layout.view_spinner_item);

        adapter.setDropDownViewResource(
                android.support.design.R.layout.support_simple_spinner_dropdown_item);

        _typeSpinner.setAdapter(adapter);

        try {
            _startCal = _sched.getServiceWindow().getStart().getCalendar();
        } catch (Exception e) {
            Log.v(TAG, e);
        }
        try {
            _endCal = _sched.getServiceWindow().getEnd().getCalendar();
        } catch (Exception e) {
            Log.v(TAG, e);
        }
        setMode(_mode);


//        if (_sched != null) {
//            if (_sched.isExact()) {
//                setMode(MODE_EXACT);
//            } else {
//                setMode(MODE_RANGE);
//            }
//        }
    }

    private void setMode(int mode) {
        _mode = mode;

        _typeSpinner.setSelection(_mode);

        switch (_mode) {
            case MODE_EXACT:
                _rangeLayout.setVisibility(View.GONE);
                _exactLayout.setVisibility(View.VISIBLE);
                if (misc.isEmptyOrNull(_stateFixedDateTime)) {
                    _dateTimeButton.setText(DateUtils.formatDateTimeLong(_startCal));
                } else {
                    _dateTimeButton.setText(_stateFixedDateTime);
                }
                break;
            case MODE_RANGE:
                _rangeLayout.setVisibility(View.VISIBLE);
                _exactLayout.setVisibility(View.GONE);

                if (misc.isEmptyOrNull(_stateFixedDateTime)) {
                    _startDateButton.setText(DateUtils.formatDateTimeLong(_startCal));
                } else {
                    _startDateButton.setText(_stateRangeStartDateTime);
                }
                if (misc.isEmptyOrNull(_stateRangeEndDateTime)) {
                    _endDateButton.setText(DateUtils.formatDateTimeLong(_endCal));
                } else {
                    _endDateButton.setText(_stateRangeEndDateTime);
                }

                break;
        }
    }

    /*-*****************************-*/
    /*-			UI Events			-*/
    /*-*****************************-*/
    private final DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String tag = (String) _datePicker.getTag();
            if (tag.equals("start")) {
                _startCal.set(year, monthOfYear, dayOfMonth);
                if (DateUtils.isBeforeToday(_startCal)) {
                    ToastClient.toast(App.get(), App.get().getString(R.string.toast_previous_date_not_allowed), Toast.LENGTH_LONG);
                    _handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            _datePicker.setTag("start");
                            _datePicker.show();
                        }
                    }, 100);
                } else {
                    _timePicker.setTag("start");
                    _timePicker.show();
                }

            } else if (tag.equals("end")) {
                _endCal.set(year, monthOfYear, dayOfMonth);
                if (DateUtils.isBeforeToday(_endCal)) {
                    ToastClient.toast(App.get(), App.get().getString(R.string.toast_previous_date_not_allowed), Toast.LENGTH_LONG);
                    _handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            _datePicker.setTag("end");
                            _datePicker.show();
                        }
                    }, 100);
                } else {
                    _timePicker.setTag("end");
                    _timePicker.show();
                }
            }
        }
    };

    private final TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker v, int hourOfDay, int minute) {
            String tag = (String) _timePicker.getTag();
            if (tag.equals("start")) {
                _startCal.set(_startCal.get(Calendar.YEAR), _startCal.get(Calendar.MONTH),
                        _startCal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

                // truncate milliseconds to seconds
                if (_startCal.getTimeInMillis() / 1000 < System.currentTimeMillis() / 1000) {
                    ToastClient.toast(App.get(), App.get().getString(R.string.toast_previous_time_not_allowed), Toast.LENGTH_LONG);
                    _handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            _timePicker.setTag("start");
                            _timePicker.show();
                        }
                    }, 100);
                    return;
                }
                _startIsSet = true;

                if (_mode == MODE_EXACT) {
                    _dateTimeButton.setText(DateUtils.formatDateTimeLong(_startCal));
                } else {
                    _startDateButton.setText(DateUtils.formatDateTimeLong(_startCal));
                }

            } else if (tag.equals("end")) {
                _endCal.set(_endCal.get(Calendar.YEAR), _endCal.get(Calendar.MONTH),
                        _endCal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

                // truncate milliseconds to seconds
                if (_endCal.getTimeInMillis() / 1000 < System.currentTimeMillis() / 1000) {
                    ToastClient.toast(App.get(), App.get().getString(R.string.toast_previous_time_not_allowed), Toast.LENGTH_LONG);
                    _handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            _timePicker.setTag("end");
                            _timePicker.show();
                        }
                    }, 100);
                    return;
                }

                _endIsSet = true;
                _endDateButton.setText(DateUtils.formatDateTimeLong(_endCal));
            }
        }
    };

    private final AdapterView.OnItemSelectedListener _type_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setMode(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final View.OnClickListener _okButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_startIsSet || _endIsSet) {
                dismiss(true);
                _onCompleteDispatcher.dispatch(getUid(), makeSchedule());
            } else {
                ToastClient.toast(App.get(), R.string.toast_change_schedule_or_cancel, Toast.LENGTH_LONG);
            }
        }
    };
    private final View.OnClickListener _cancelButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
            _onCancelDispatcher.dispatch(getUid());
        }
    };

    private final View.OnClickListener _dateTimeButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _datePicker.setTag("start");
            _datePicker.show();
        }
    };

    private final View.OnClickListener _startDateButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _datePicker.setTag("start");
            _datePicker.show();
        }
    };

    private final View.OnClickListener _endDateButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _datePicker.setTag("end");
            _datePicker.show();
        }
    };

    public static void show(Context context, String uid, Schedule schedule) {
        Bundle params = new Bundle();
        params.putParcelable("schedule", schedule);

        Controller.show(context, uid, ScheduleDialog.class, params);
    }

    /*-****************************-*/
    /*-         Complete           -*/
    /*-****************************-*/
    public interface OnCompleteListener {
        void onComplete(Schedule schedule);
    }

    private static KeyedDispatcher<OnCompleteListener> _onCompleteDispatcher = new KeyedDispatcher<OnCompleteListener>() {
        @Override
        public void onDispatch(OnCompleteListener listener, Object... parameters) {
            listener.onComplete((Schedule) parameters[0]);
        }
    };

    public static void addOnCompleteListener(String uid, OnCompleteListener onCompleteListener) {
        _onCompleteDispatcher.add(uid, onCompleteListener);
    }

    public static void removeOnCompleteListener(String uid, OnCompleteListener onCompleteListener) {
        _onCompleteDispatcher.remove(uid, onCompleteListener);
    }

    public static void removeAllOnCompleteListener(String uid) {
        _onCompleteDispatcher.removeAll(uid);
    }

    /*-**************************-*/
    /*-         Cancel           -*/
    /*-**************************-*/
    public interface OnCancelListener {
        void onCancel();
    }

    private static KeyedDispatcher<OnCancelListener> _onCancelDispatcher = new KeyedDispatcher<OnCancelListener>() {
        @Override
        public void onDispatch(OnCancelListener listener, Object... parameters) {
            listener.onCancel();
        }
    };

    public static void addOnCancelListener(String uid, OnCancelListener onCancelListener) {
        _onCancelDispatcher.add(uid, onCancelListener);
    }

    public static void removeOnCancelListener(String uid, OnCancelListener onCancelListener) {
        _onCancelDispatcher.remove(uid, onCancelListener);
    }

    public static void removeAllOnCancelListener(String uid) {
        _onCancelDispatcher.removeAll(uid);
    }
}
