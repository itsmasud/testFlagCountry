package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class ConfirmDialog extends DialogFragmentBase {
    private static final String TAG = "ui.dialog.ConfirmDialog";

    // State
    private static final String STATE_DURATION = "STATE_DURATION";
    private static final String STATE_SCHEDULE = "STATE_SCHEDULE";
    private static final String STATE_WORKORDER = "STATE_WORKORDER";

    // Ui
    private Button _startDateButton;
    private Button _durationButton;
    private Button _okButton;
    private Button _cancelButton;
    private TextView _schedule1TextView;
    private TextView _schedule2TextView;
    private CheckBox _tacCheckBox;
    private Button _tacButton;

    private DatePickerDialog _datePicker;
    private TimePickerDialog _timePicker;
    private DurationDialog _durationDialog;
    // Data
    private Listener _listener;
    private Calendar _startCalendar;
    private long _durationMilliseconds;
    private Schedule _schedule;
    private Workorder _workorder;

    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/
    public static ConfirmDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, ConfirmDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_DURATION))
                _durationMilliseconds = savedInstanceState.getLong(STATE_DURATION);

            if (savedInstanceState.containsKey(STATE_SCHEDULE))
                _schedule = savedInstanceState.getParcelable(STATE_SCHEDULE);

            if (savedInstanceState.containsKey(STATE_WORKORDER))
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);
        }

        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(STATE_DURATION, _durationMilliseconds);

        if (_schedule != null)
            outState.putParcelable(STATE_SCHEDULE, _schedule);

        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_confirm, container, false);

        _startDateButton = (Button) v.findViewById(R.id.start_date_button);
        _startDateButton.setOnClickListener(_startDate_onClick);

        _durationButton = (Button) v.findViewById(R.id.duration_button);
        _durationButton.setOnClickListener(_duration_onClick);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);
        _okButton.setEnabled(false);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        _schedule1TextView = (TextView) v.findViewById(R.id.schedule1_textview);
        _schedule2TextView = (TextView) v.findViewById(R.id.schedule2_textview);

        _tacButton = (Button) v.findViewById(R.id.tac_button);
        _tacButton.setOnClickListener(_terms_onClick);

        _tacCheckBox = (CheckBox) v.findViewById(R.id.tac_checkbox);
        _tacCheckBox.setOnCheckedChangeListener(_tacCheck_change);
        final Calendar c = Calendar.getInstance();
        _datePicker = DatePickerDialog.newInstance(_date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        _datePicker.setCloseOnSingleTapDay(true);
        _timePicker = TimePickerDialog.newInstance(_time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                false, false);

        _startCalendar = Calendar.getInstance();

        _durationDialog = DurationDialog.getInstance(_fm, TAG);
        _durationDialog.setListener(_duration_listener);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

//    public void setDuration(long timeMilliseconds) {
//        _durationMilliseconds = timeMilliseconds;
//        _durationButton.setText(misc.convertMsToHuman(_durationMilliseconds));
//    }

//    public void setTime(Calendar time) {
//        try {
//            _startCalendar.set(time.get(Calendar.YEAR), time.get(Calendar.MONTH),
//                    time.get(Calendar.DAY_OF_MONTH), time.get(Calendar.HOUR_OF_DAY),
//                    time.get(Calendar.MINUTE));
//
//            long start = ISO8601.toUtc(_schedule.getStartTime());
//            long end = ISO8601.toUtc(_schedule.getEndTime());
//
//            long input = _startCalendar.getTimeInMillis();
//
//            if (input < start || input > end) {
//                Toast.makeText(getActivity(), "Arrival time is out of range. Please try again", Toast.LENGTH_LONG).show();
//                _startCalendar = ISO8601.toCalendar(_schedule.getStartTime());
//            }
//
//            _startDateButton.setText(misc.formatDateTimeLong(_startCalendar));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }


    public void show(Workorder workorder, Schedule schedule) {
        _schedule = schedule;
        _workorder = workorder;

        super.show();
    }


    private void populateUi() {
        if (_schedule == null)
            return;

        if (_schedule1TextView == null)
            return;

        if (_schedule.isExact()) {
            _schedule2TextView.setVisibility(View.GONE);
            try {
                Calendar cal = ISO8601.toCalendar(_schedule.getStartTime());
                String dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + misc.formatDateLong(cal);
                String time = misc.formatTime(cal, false) + " " + cal.getTimeZone().getDisplayName(false,
                        TimeZone.SHORT);

                _schedule1TextView.setText("You will need to arrive exactly on " + dayDate + " at " + time + ".");

                _startCalendar = cal;
                _startDateButton.setText(misc.formatDateTimeLong(_startCalendar));
                _startDateButton.setEnabled(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            _schedule2TextView.setVisibility(View.GONE);
            try {
                Calendar cal = ISO8601.toCalendar(_schedule.getStartTime());
                String dayDate;
                String time = "";

                dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + misc.formatDateLong(cal);
                time = misc.formatTime(cal, false);

                String msg = "You will need to arrive between\n\t" + dayDate + " at " + time + " and\n\t";

                Calendar cal2 = ISO8601.toCalendar(_schedule.getEndTime());

                // same day
                if (cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
                    time = misc.formatTime(cal2, false) + " " + cal2.getTimeZone().getDisplayName(false, TimeZone.SHORT);
                    msg += time + ".";

                } else {
                    dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal2.getTime()) + " " + misc.formatDateLong(cal2);
                    time = misc.formatTime(cal2, false) + " " + cal2.getTimeZone().getDisplayName(false, TimeZone.SHORT);
                    msg += dayDate + " at " + time + ".";
                }

                _schedule1TextView.setText(msg);

                _startCalendar = cal;
                _startDateButton.setText(misc.formatDateTimeLong(_startCalendar));
                _startDateButton.setEnabled(true);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    /*-*****************************-*/
    /*-				Events			-*/
    /*-*****************************-*/
    private DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            _startCalendar.set(year, month, day);
            _timePicker.show(_fm, datePickerDialog.getTag());
        }
    };

    private TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute) {
            try {
                _startCalendar.set(_startCalendar.get(Calendar.YEAR), _startCalendar.get(Calendar.MONTH),
                        _startCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

                long start = ISO8601.toUtc(_schedule.getStartTime());
                long end = ISO8601.toUtc(_schedule.getEndTime());

                long input = _startCalendar.getTimeInMillis();

                if (input < start || input > end) {
                    Toast.makeText(getActivity(), "Arrival time is out of range. Please try again", Toast.LENGTH_LONG).show();
                    _startCalendar = ISO8601.toCalendar(_schedule.getStartTime());
                }

                _startDateButton.setText(misc.formatDateTimeLong(_startCalendar));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener _tacCheck_change = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            _okButton.setEnabled(isChecked);
        }
    };

    private View.OnClickListener _terms_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.termsOnClick(_workorder);
        }
    };

    private View.OnClickListener _startDate_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Calendar start = ISO8601.toCalendar(_schedule.getStartTime());

                if (!_schedule.isExact()) {
                    Calendar stop = ISO8601.toCalendar(_schedule.getEndTime());

                    if (start.get(Calendar.YEAR) == stop.get(Calendar.YEAR)
                            && start.get(Calendar.DAY_OF_YEAR) == stop.get(Calendar.DAY_OF_YEAR)) {
                        _timePicker.show(_fm, TAG);
                    } else {
                        _datePicker.show(_fm, TAG);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
                _listener.onOk(_workorder, ISO8601.fromCalendar(_startCalendar), _durationMilliseconds);
            }
            dismiss();
        }
    };
    private View.OnClickListener _duration_onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            _durationDialog.show();
        }
    };
    private DurationDialog.Listener _duration_listener = new DurationDialog.Listener() {

        @Override
        public void onOk(long timeMilliseconds) {
            _durationMilliseconds = timeMilliseconds;
            _durationButton.setText(misc.convertMsToHuman(_durationMilliseconds));
        }

        @Override
        public void onCancel() {
        }
    };

    public interface Listener {
        public void onOk(Workorder workorder, String startDate, long durationMilliseconds);

        public void onCancel(Workorder workorder);

        public void termsOnClick(Workorder workorder);
    }

}