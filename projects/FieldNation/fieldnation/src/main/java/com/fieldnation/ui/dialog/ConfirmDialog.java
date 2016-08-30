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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;

import java.util.Calendar;

public class ConfirmDialog extends DialogFragmentBase {
    private static final String TAG = "ConfirmDialog";

    // State
    private static final String STATE_DURATION = "STATE_DURATION";
    private static final String STATE_SCHEDULE = "STATE_SCHEDULE";
    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_TAC_ACCEPT = "STATE_TAC_ACCEPT";

    private final static int MIN_JOB_DURATION = 900000;
    // Ui
    private LinearLayout _startDateLayout;
    private Button _startDateButton;
    private Button _durationButton;
    private Button _okButton;
    private Button _cancelButton;
    private TextView _scheduleTextView;
    private CheckBox _tacCheckBox;
    private Button _tacButton;

    private DatePickerDialog _datePicker;
    private TimePickerDialog _timePicker;
    private DurationDialog _durationDialog;

    // Data
    private Listener _listener;
    private Calendar _startCalendar;
    private long _durationMilliseconds = -1;
    private Schedule _schedule;
    private Workorder _workorder;
    private boolean _tacAccept = false;

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

            if (savedInstanceState.containsKey(STATE_TAC_ACCEPT))
                _tacAccept = savedInstanceState.getBoolean(STATE_TAC_ACCEPT);
        }

        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(STATE_DURATION, _durationMilliseconds);
        outState.putBoolean(STATE_TAC_ACCEPT, _tacAccept);

        if (_schedule != null)
            outState.putParcelable(STATE_SCHEDULE, _schedule);

        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_confirm, container, false);

        _startDateLayout = (LinearLayout) v.findViewById(R.id.startDate_layout);

        _startDateButton = (Button) v.findViewById(R.id.startDate_button);
        _startDateButton.setOnClickListener(_startDate_onClick);

        _durationButton = (Button) v.findViewById(R.id.duration_button);
        _durationButton.setOnClickListener(_duration_onClick);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        _scheduleTextView = (TextView) v.findViewById(R.id.schedule_textview);

        _tacButton = (Button) v.findViewById(R.id.tac_button);
        _tacButton.setOnClickListener(_terms_onClick);

        _tacCheckBox = (CheckBox) v.findViewById(R.id.tac_checkbox);
        _tacCheckBox.setOnCheckedChangeListener(_tacCheck_change);
        final Calendar c = Calendar.getInstance();
        _datePicker = new DatePickerDialog(getActivity(), _date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        _timePicker = new TimePickerDialog(getActivity(), _time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);

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

    private void setDuration(long timeMilliseconds) {
        _durationMilliseconds = timeMilliseconds;
        _durationButton.setText(misc.convertMsToHuman(_durationMilliseconds));
    }

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
//            Log.v(TAG, ex);
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

        if (_scheduleTextView == null)
            return;

        _tacCheckBox.setChecked(_tacAccept);

        String display = _schedule.getDisplayString(false);
        _scheduleTextView.setText(display);
        setDuration(_durationMilliseconds > -1 ? _durationMilliseconds : MIN_JOB_DURATION);
        if (_schedule.isExact()) {
            try {
                _startCalendar = ISO8601.toCalendar(_schedule.getStartTime());
                _startDateLayout.setVisibility(View.GONE);
                setDuration(_durationMilliseconds > -1 ? _durationMilliseconds : MIN_JOB_DURATION);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        } else {
            try {
                Calendar cal = ISO8601.toCalendar(_schedule.getStartTime());
                Calendar cal2 = ISO8601.toCalendar(_schedule.getEndTime());
                _startCalendar = cal;
                _startDateButton.setText(DateUtils.formatDateTimeLong(_startCalendar));
                _startDateLayout.setVisibility(View.VISIBLE);
                setDuration(_durationMilliseconds > -1 ? _durationMilliseconds : cal2.getTimeInMillis() - cal.getTimeInMillis());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    }

    /*-*****************************-*/
    /*-				Events			-*/
    /*-*****************************-*/

    private final DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _startCalendar.set(year, monthOfYear, dayOfMonth);
            _timePicker.show();
        }
    };

    private final TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
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

                _startDateButton.setText(DateUtils.formatDateTimeLong(_startCalendar));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

    };

    private final CompoundButton.OnCheckedChangeListener _tacCheck_change = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            _tacAccept = isChecked;
        }
    };

    private final View.OnClickListener _terms_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.termsOnClick(_workorder);
        }
    };

    private final View.OnClickListener _startDate_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Calendar start = ISO8601.toCalendar(_schedule.getStartTime());

                if (!_schedule.isExact()) {
                    Calendar stop = ISO8601.toCalendar(_schedule.getEndTime());

                    if (start.get(Calendar.YEAR) == stop.get(Calendar.YEAR)
                            && start.get(Calendar.DAY_OF_YEAR) == stop.get(Calendar.DAY_OF_YEAR)) {
                        _timePicker.show();
                    } else {
                        _datePicker.show();
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!_tacAccept) {
                ToastClient.toast(App.get(), R.string.please_accept_the_terms_and_conditions_to_continue, Toast.LENGTH_LONG);
                return;
            }
            if (_listener != null) {
                _listener.onOk(_workorder, ISO8601.fromCalendar(_startCalendar), _durationMilliseconds);
            }
            dismiss();
        }
    };

    private final View.OnClickListener _duration_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _durationDialog.show(_durationMilliseconds);
        }
    };

    private final DurationDialog.Listener _duration_listener = new DurationDialog.Listener() {
        @Override
        public void onOk(long timeMilliseconds) {
            if (timeMilliseconds < MIN_JOB_DURATION) {
                setDuration(MIN_JOB_DURATION);
                ToastClient.toast(App.get(), getString(R.string.toast_minimum_job_duration), Toast.LENGTH_LONG);
                return;
            }
            _durationMilliseconds = timeMilliseconds;
            _durationButton.setText(misc.convertMsToHuman(_durationMilliseconds));
        }

        @Override
        public void onCancel() {
        }
    };

    public interface Listener {
        void onOk(Workorder workorder, String startDate, long durationMilliseconds);

        void onCancel(Workorder workorder);

        void termsOnClick(Workorder workorder);
    }
}