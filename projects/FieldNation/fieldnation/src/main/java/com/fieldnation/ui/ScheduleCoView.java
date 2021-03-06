package com.fieldnation.ui;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.ui.dialog.DatePickerDialog;
import com.fieldnation.ui.dialog.TimePickerDialog;
import com.fieldnation.v2.data.model.Date;
import com.fieldnation.v2.data.model.Schedule;
import com.fieldnation.v2.data.model.ScheduleServiceWindow;

import java.util.Calendar;

/**
 * Created by shoaib.ahmed on 30/5/2017.
 */
public class ScheduleCoView extends RelativeLayout {
    private static final String TAG = "ScheduleCoViewNew";

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
    private TextView _scheduleTypeTitleTextview;
    private HintSpinner _typeSpinner;
    private LinearLayout _rangeLayout;
    private Button _startDateButton;
    private Button _endDateButton;
    private LinearLayout _exactLayout;
    private Button _dateTimeButton;

    private DatePickerDialog _datePicker;
    private TimePickerDialog _timePicker;

    // Data
    private int _mode = -1;
    private Calendar _startCal;
    private Calendar _endCal;
    private boolean _startIsSet = false;
    private boolean _endIsSet = false;
    private final Handler _handler = new Handler();

    public ScheduleCoView(Context context) {
        super(context);
        init();
    }

    public ScheduleCoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScheduleCoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_co_schedule_new, this);

        if (isInEditMode())
            return;

        _scheduleTypeTitleTextview = findViewById(R.id.schedule_type_title_textview);
        _typeSpinner = findViewById(R.id.type_spinner);
        _rangeLayout = findViewById(R.id.range_layout);
        getTypeSpinner();

        _exactLayout = findViewById(R.id.exact_layout);
        _startDateButton = findViewById(R.id.start_date_button);
        _endDateButton = findViewById(R.id.end_date_button);
        _dateTimeButton = findViewById(R.id.date_time_button);

        initializeTimePicker();

        _startCal = Calendar.getInstance();
        _startCal.set(Calendar.SECOND, 0);
        _endCal = Calendar.getInstance();
        _endCal.set(Calendar.SECOND, 0);

        _typeSpinner.setOnItemSelectedListener(_type_selected);
        _startDateButton.setOnClickListener(_startDateButton_onClick);
        _endDateButton.setOnClickListener(_endDateButton_onClick);
        _dateTimeButton.setOnClickListener(_dateTimeButton_onClick);

        populateUi();
    }

    private HintSpinner getTypeSpinner() {
        if (_typeSpinner != null && _typeSpinner.getAdapter() == null) {
            HintArrayAdapter adapter = HintArrayAdapter.createFromResources(
                    _typeSpinner.getContext(),
                    R.array.schedule_types,
                    R.layout.view_spinner_item_gray);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _typeSpinner.setAdapter(adapter);
        }
        return _typeSpinner;
    }


    public Schedule getSchedule() {
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

    public void setSchedule(Schedule schedule) {
        switch (schedule.getServiceWindow().getMode()) {
            case HOURS:
            case BETWEEN:
                _mode = MODE_RANGE;
                try {
                    _startCal = schedule.getServiceWindow().getStart().getCalendar();
                    _endCal = schedule.getServiceWindow().getEnd().getCalendar();
                    _startIsSet = true;
                    _endIsSet = true;
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
                _typeSpinner.setSelection(_mode);
                //setMode(_mode);
                break;
            case EXACT:
                _mode = MODE_EXACT;
                try {
                    _startCal = schedule.getServiceWindow().getStart().getCalendar();
                    _startIsSet = true;
                    _endIsSet = true;
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
                _typeSpinner.setSelection(_mode);
                //setMode(_mode);
                break;
        }
    }

    private void populateUi() {
        if (_typeSpinner == null)
            return;
        setMode(_mode);
    }

    private void setMode(int mode) {
        _mode = mode;

        switch (_mode) {
            case MODE_EXACT:
                _scheduleTypeTitleTextview.setVisibility(VISIBLE);
                _rangeLayout.setVisibility(View.GONE);
                _exactLayout.setVisibility(View.VISIBLE);
                _dateTimeButton.setText(DateUtils.formatDateTimeLong(_startCal));
                break;
            case MODE_RANGE:
                _scheduleTypeTitleTextview.setVisibility(VISIBLE);
                _rangeLayout.setVisibility(View.VISIBLE);
                _exactLayout.setVisibility(View.GONE);

                _startDateButton.setText(DateUtils.formatDateTimeLong(_startCal));
                if (_startCal.getTimeInMillis() > _endCal.getTimeInMillis()) {
                    _endCal.setTimeInMillis(_startCal.getTimeInMillis() + 86400000);
                }
                _endDateButton.setText(DateUtils.formatDateTimeLong(_endCal));

                break;
            case -1:
                _rangeLayout.setVisibility(View.GONE);
                _exactLayout.setVisibility(View.GONE);
                _scheduleTypeTitleTextview.setVisibility(GONE);

        }
    }

    public boolean isValidSchedule() {
        return _mode != -1;
    }

    private void initializeTimePicker() {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, 0);
        _datePicker = new DatePickerDialog(getContext(), _date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        _timePicker = new TimePickerDialog(getContext(), _time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
    }

    /*-*****************************-*/
    /*-			UI Events			-*/
    /*-*****************************-*/
    private final DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String tag = (String) _datePicker.getTag();
            if (tag == null)
                return;

            if (tag.equals("start")) {
                _startCal.set(year, monthOfYear, dayOfMonth);
                if (DateUtils.isBeforeToday(_startCal)) {
                    ToastClient.toast(App.get(), R.string.toast_previous_date_not_allowed, Toast.LENGTH_LONG);
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
                    ToastClient.toast(App.get(), R.string.toast_previous_date_not_allowed, Toast.LENGTH_LONG);
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
            if (tag == null) return;

            if (tag.equals("start")) {
                _startCal.set(_startCal.get(Calendar.YEAR), _startCal.get(Calendar.MONTH),
                        _startCal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute, 0);

                // truncate milliseconds to seconds
                if (_startCal.getTimeInMillis() / 1000 < System.currentTimeMillis() / 1000) {
                    ToastClient.toast(App.get(), R.string.toast_previous_time_not_allowed, Toast.LENGTH_LONG);
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
                    if (_startCal.after(_endCal)) {
                        ToastClient.toast(App.get(), R.string.toast_pick_start_time_before_end, Toast.LENGTH_SHORT);
                    }
                    _startDateButton.setText(DateUtils.formatDateTimeLong(_startCal));
                }

            } else if (tag.equals("end")) {
                _endCal.set(_endCal.get(Calendar.YEAR), _endCal.get(Calendar.MONTH),
                        _endCal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute, 0);

                // truncate milliseconds to seconds
                if (_endCal.getTimeInMillis() / 1000 < System.currentTimeMillis() / 1000) {
                    ToastClient.toast(App.get(), R.string.toast_previous_time_not_allowed, Toast.LENGTH_LONG);
                    _handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            _timePicker.setTag("end");
                            _timePicker.show();
                        }
                    }, 100);
                    return;
                }

                if (_startCal.after(_endCal)) {
                    ToastClient.toast(App.get(), R.string.toast_pick_end_time_after_start, Toast.LENGTH_SHORT);
                }

                _endIsSet = true;
                _endDateButton.setText(DateUtils.formatDateTimeLong(_endCal));
            }
            initializeTimePicker();
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

    private final View.OnClickListener _dateTimeButton_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            _datePicker.setTag("start");
            _datePicker.show();
        }
    };

    private final View.OnClickListener _startDateButton_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            _datePicker.setTag("start");
            _datePicker.show();
        }
    };

    private final View.OnClickListener _endDateButton_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            _datePicker.setTag("end");
            _datePicker.show();
        }
    };


}
