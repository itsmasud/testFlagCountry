package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;

import java.util.Calendar;

public class EtaDialog extends DialogFragmentBase {
    private static final String TAG = "EtaDialog";

    // State
    private static final String STATE_DURATION = "STATE_DURATION";
    private static final String STATE_SCHEDULE = "STATE_SCHEDULE";
    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_TAC_ACCEPT = "STATE_TAC_ACCEPT";

    private final static int MIN_JOB_DURATION = 900000;

    // Ui
    private TextView _titleTextView;
    private Button _expirationButton;
    private Button _cancelButton;
    private Button _okButton;
    private DatePickerDialog _datePicker;
    private TimePickerDialog _requestTimePicker;
    private RelativeLayout _requestLayout;
    private RelativeLayout _confirmLayout;

    private RelativeLayout _startDateLayout;
    private Button _startDateButton;
    private Button _durationButton;
    private TextView _scheduleTextView;
    private CheckBox _tacCheckBox;
    private Button _tacButton;

    private Switch _etaSwitch;
    private Button _etaDurationButton;


    //    private DatePickerDialog _datePicker;
    private TimePickerDialog _confirmTimePicker;
    private DurationDialog _durationDialog;


    // Data
    private Calendar _calendar;
    private boolean _isDateSet;
    private Listener _listener;
    private Workorder _workorder;
    private Schedule _schedule;
    private String _okButtonText;
    private String _title;
    private boolean _isRequest = false;
    private boolean _isConfirm = false;
    private final Handler _handler = new Handler();

    private Calendar _startCalendar;
    private long _durationMilliseconds = -1;
    private boolean _tacAccept = false;


    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public static EtaDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, EtaDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDER))
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);

            if (savedInstanceState.containsKey(STATE_DURATION))
                _durationMilliseconds = savedInstanceState.getLong(STATE_DURATION);

            if (savedInstanceState.containsKey(STATE_SCHEDULE))
                _schedule = savedInstanceState.getParcelable(STATE_SCHEDULE);

            if (savedInstanceState.containsKey(STATE_TAC_ACCEPT))
                _tacAccept = savedInstanceState.getBoolean(STATE_TAC_ACCEPT);

        }
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);

        outState.putLong(STATE_DURATION, _durationMilliseconds);
        outState.putBoolean(STATE_TAC_ACCEPT, _tacAccept);

        if (_schedule != null)
            outState.putParcelable(STATE_SCHEDULE, _schedule);


        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        populateUi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_eta, container, false);

        _requestLayout = (RelativeLayout) v.findViewById(R.id.request_layout);
        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _expirationButton = (Button) v.findViewById(R.id.expiration_button);
        _expirationButton.setOnClickListener(_expiration_onClick);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);
        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);


        _confirmLayout = (RelativeLayout) v.findViewById(R.id.confirm_layout);

        final Calendar c = Calendar.getInstance();
        _datePicker = new DatePickerDialog(getActivity(), _date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        _requestTimePicker = new TimePickerDialog(getActivity(), _requestTime_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);

        _calendar = Calendar.getInstance();

        // confirm related
        _startDateLayout = (RelativeLayout) v.findViewById(R.id.startDate_layout);

        _startDateButton = (Button) v.findViewById(R.id.startDate_button);
        _startDateButton.setOnClickListener(_startDate_onClick);

        _durationButton = (Button) v.findViewById(R.id.duration_button);
        _durationButton.setOnClickListener(_duration_onClick);

        _scheduleTextView = (TextView) v.findViewById(R.id.schedule_textview);

        _tacButton = (Button) v.findViewById(R.id.tac_button);
        _tacButton.setOnClickListener(_terms_onClick);

        _tacCheckBox = (CheckBox) v.findViewById(R.id.tac_checkbox);
        _tacCheckBox.setOnCheckedChangeListener(_tacCheck_change);
//        final Calendar c = Calendar.getInstance();
        _datePicker = new DatePickerDialog(getActivity(), _date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        _confirmTimePicker = new TimePickerDialog(getActivity(), _confirmTime_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);

        _startCalendar = Calendar.getInstance();

        _durationDialog = DurationDialog.getInstance(_fm, TAG);
        _durationDialog.setListener(_duration_listener);


        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(Workorder workorder, String titleInitial, String okButtonText) {
        _isDateSet = false;
        _workorder = workorder;
        _okButtonText = okButtonText;
        _title = titleInitial + workorder.getWorkorderId();
        _isRequest = true;

        super.show();
    }


    public void show(Workorder workorder, Schedule schedule, String titleInitial, String okButtonText) {
        _schedule = schedule;
        _workorder = workorder;

        _okButtonText = okButtonText;
        _title = titleInitial + workorder.getWorkorderId();
        _isConfirm = true;

        super.show();
    }


    private void populateUi() {
        if (!misc.isEmptyOrNull(_title))
            _titleTextView.setText(_title);

        if (!misc.isEmptyOrNull(_okButtonText))
            _okButton.setText(_okButtonText);

        if (_isRequest && !_isConfirm) {
            _requestLayout.setVisibility(View.VISIBLE);
            _confirmLayout.setVisibility(View.GONE);
        } else {
            _requestLayout.setVisibility(View.GONE);
            _confirmLayout.setVisibility(View.VISIBLE);
        }


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

    private void setDuration(long timeMilliseconds) {
        _durationMilliseconds = timeMilliseconds;
        _durationButton.setText(misc.convertMsToHuman(_durationMilliseconds));
    }





	/*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/

    private final DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _calendar.set(year, monthOfYear, dayOfMonth);
            if (DateUtils.isBeforeToday(_calendar)) {
                ToastClient.toast(App.get(), getString(R.string.toast_previous_date_not_allowed), Toast.LENGTH_LONG);
                _handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        _datePicker.show();
                    }
                }, 100);
            } else {
                _requestTimePicker.show();
            }
        }
    };

    private final TimePickerDialog.OnTimeSetListener _requestTime_onSet = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            _calendar.set(_calendar.get(Calendar.YEAR), _calendar.get(Calendar.MONTH),
                    _calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

            // truncate milliseconds to seconds
            if (_calendar.getTimeInMillis() / 1000 < System.currentTimeMillis() / 1000) {
                ToastClient.toast(App.get(), getString(R.string.toast_previous_time_not_allowed), Toast.LENGTH_LONG);
                _handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        _requestTimePicker.show();
                    }
                }, 100);
                return;
            }

            _expirationButton.setText(DateUtils.formatDateTimeLong(_calendar));
            _isDateSet = true;
        }
    };

    private final View.OnClickListener _expiration_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _datePicker.show();
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
            if (_listener != null && _isRequest) {
                if (_isDateSet) {
                    _listener.onOk(_workorder, ISO8601.fromCalendar(_calendar));
                } else {
                    _listener.onOk(_workorder, null);
                }
            }

            if (_listener != null && _isConfirm) {
                if (!_tacAccept) {
                    ToastClient.toast(App.get(), R.string.please_accept_the_terms_and_conditions_to_continue, Toast.LENGTH_LONG);
                    return;
                }
                if (_listener != null) {
                    _listener.onOk(_workorder, ISO8601.fromCalendar(_startCalendar), _durationMilliseconds);
                }
            }

            dismiss();
        }
    };

    // confirm things
    private final TimePickerDialog.OnTimeSetListener _confirmTime_onSet = new TimePickerDialog.OnTimeSetListener() {

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
                        _requestTimePicker.show();
                    } else {
                        _datePicker.show();
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
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
    void onOk(Workorder workorder, String dateTime);

    void onOk(Workorder workorder, String startDate, long durationMilliseconds);

    void onCancel(Workorder workorder);

    void termsOnClick(Workorder workorder);
}

}
