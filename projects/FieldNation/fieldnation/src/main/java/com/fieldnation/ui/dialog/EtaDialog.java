package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EtaDialog extends DialogFragmentBase {
    private static final String TAG = "EtaDialog";

    // State
    private static final String STATE_DURATION = "STATE_DURATION";
    private static final String STATE_SCHEDULE = "STATE_SCHEDULE";
    private static final String STATE_WORKORDER = "STATE_WORKORDER";

    private final static int MIN_JOB_DURATION = 900000;
    private final static int MIN_EXPIRING_DURATION = 900000;
    private final static int INVALID_NUMBER = -1;

    // Ui
    private TextView _titleTextView;
    private Button _expirationButton;
    private Button _cancelButton;
    private Button _okButton;
    private RelativeLayout _requestLayout;

    private TextView _scheduleTextView;

    private RelativeLayout _etaLayout;
    private Switch _etaSwitch;
    private Button _etaStartDateButton;
    private Button _etaStartTimeButton;
    private Button _durationButton;
    private EditText _noteEditText;


    private DatePickerDialog _etaStartDatePicker;
    private TimePickerDialog _etaStartTimePicker;
    private DurationDialog _durationDialog;
    private DurationDialog _expiryDialog;


    // Data
    private Calendar _scheduledStartDateTimeCalendar;
    private Calendar _etaStartDateTimeCalendar;

    private Listener _listener;
    private Workorder _workorder;
    private Schedule _schedule;
    private boolean _isRequest = false;
    private boolean _isConfirm = false;
    private boolean _isEdit = false;
    private boolean _isEtaEnabled = false;
    private final Handler _handler = new Handler();

    private long _durationMilliseconds = INVALID_NUMBER;
    private long _expiringDurationMilliseconds = INVALID_NUMBER;
    private boolean _clear = false;


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

        }
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);

        outState.putLong(STATE_DURATION, _durationMilliseconds);

        if (_schedule != null)
            outState.putParcelable(STATE_SCHEDULE, _schedule);


        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (_clear) {
            _clear = false;
        }


        populateUi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_eta, container, false);

        _etaStartDateTimeCalendar = Calendar.getInstance();

        _requestLayout = (RelativeLayout) v.findViewById(R.id.request_layout);
        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _expirationButton = (Button) v.findViewById(R.id.expiration_button);
        _expirationButton.setOnClickListener(_expiringButton_onClick);


        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);
        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        _etaStartDateButton = (Button) v.findViewById(R.id.etaStartDate_button);
        _etaStartDateButton.setOnClickListener(_etaStartDate_onClick);

        _durationButton = (Button) v.findViewById(R.id.duration_button);
        _durationButton.setOnClickListener(_duration_onClick);

        _scheduleTextView = (TextView) v.findViewById(R.id.schedule_textview);

        _etaStartDatePicker = new DatePickerDialog(getActivity(), _etaStartDate_onSet, _etaStartDateTimeCalendar.get(Calendar.YEAR), _etaStartDateTimeCalendar.get(Calendar.MONTH), _etaStartDateTimeCalendar.get(Calendar.DAY_OF_MONTH));
        _etaStartTimePicker = new TimePickerDialog(getActivity(), _etaStartTime_onSet, _etaStartDateTimeCalendar.get(Calendar.HOUR_OF_DAY), _etaStartDateTimeCalendar.get(Calendar.MINUTE), false);

        _durationDialog = DurationDialog.getInstance(_fm, TAG);
        _durationDialog.setListener(_duration_listener);

        _expiryDialog = DurationDialog.getInstance(_fm, TAG);
        _expiryDialog.setListener(_expiry_listener);


        _etaLayout = (RelativeLayout) v.findViewById(R.id.eta_layout);
        _etaSwitch = (Switch) v.findViewById(R.id.enableEta_switch);
        _etaSwitch.setOnCheckedChangeListener(_switchOnclick_listener);
        _etaStartTimeButton = (Button) v.findViewById(R.id.etaStartTime_button);
        _etaStartTimeButton.setOnClickListener(_etaStartTime_onClick);
        _noteEditText = (EditText) v.findViewById(R.id.note_edittext);


        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }


    public void show(Workorder workorder, boolean isRequest, boolean isConfirm, boolean isEdit) {
        _workorder = workorder;
        _isRequest = isRequest;
        _isConfirm = isConfirm;
        _isEdit = isEdit;
        _schedule = workorder.getSchedule();
        _clear = true;

        super.show();
    }


    private void populateUi() {
        if (_schedule == null)
            return;

        if (_isRequest) {
            _okButton.setText(getString(R.string.btn_submit));
            _titleTextView.setText("Request " + _workorder.getWorkorderId());
            _requestLayout.setVisibility(View.VISIBLE);
            _switchOnclick_listener.onCheckedChanged(_etaSwitch, false);
            _etaSwitch.setVisibility(View.VISIBLE);
            _etaSwitch.setChecked(false);

        } else if (_isConfirm) {
            _okButton.setText(getString(R.string.btn_confirm));
            _titleTextView.setText("Confirm " + _workorder.getWorkorderId());
            _requestLayout.setVisibility(View.GONE);
            _etaSwitch.setVisibility(View.GONE);
            _switchOnclick_listener.onCheckedChanged(_etaSwitch, true);
            _isEtaEnabled = true;
        } else if (_isEdit) {
            _okButton.setText(getString(R.string.btn_save));
            _titleTextView.setText(getString(R.string.dialog_eta_title));
            _requestLayout.setVisibility(View.GONE);
            _etaSwitch.setVisibility(View.GONE);
            _switchOnclick_listener.onCheckedChanged(_etaSwitch, true);
            _isEtaEnabled = true;
        }

        final String scheduleDisplayText = getScheduleDisplayText();
        if (scheduleDisplayText == null) {
            _scheduleTextView.setVisibility(View.GONE);
        } else
            _scheduleTextView.setText(getScheduleDisplayText());
        populateEtaLayout();
    }

    private void setDuration(long timeMilliseconds) {
        _durationMilliseconds = timeMilliseconds;
        _durationButton.setText(misc.convertMsToHuman(_durationMilliseconds));
    }

    private void setExpiringDuration(long timeMilliseconds) {
        if (timeMilliseconds == INVALID_NUMBER) {
            _expirationButton.setText(getString(R.string.btn_never));
            return;
        }

        _expiringDurationMilliseconds = timeMilliseconds;
        _expirationButton.setText(misc.convertMsToHuman(_expiringDurationMilliseconds));
    }


    private String getScheduleDisplayText() {

        String dateTimeString;
        try {
            Calendar sCal = null;
            Calendar eCal = null;

            try {
                if (_schedule != null && !misc.isEmptyOrNull(_schedule.getStartTime()))
                    sCal = ISO8601.toCalendar(_schedule.getStartTime());
            } catch (Exception ex) {
            }

            try {
                if (_schedule != null && !misc.isEmptyOrNull(_schedule.getEndTime()))
                    eCal = ISO8601.toCalendar(_schedule.getEndTime());
            } catch (Exception ex) {
            }

            if (sCal == null) {
                return null;
            }

            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
            symbols.setAmPmStrings(getResources().getStringArray(R.array.schedule_capital_case_am_pm_array));

            if (_schedule.getType() == Schedule.Type.BUSINESS_HOURS) {

                SimpleDateFormat sdf1 = new SimpleDateFormat("E, MMM dd", Locale.getDefault());
                sdf1.setDateFormatSymbols(symbols);
                SimpleDateFormat sdf2 = new SimpleDateFormat("E, MMM dd, yyyy", Locale.getDefault());
                sdf2.setDateFormatSymbols(symbols);

                dateTimeString = getString(R.string.schedule_business_hours_format1,
                        sdf1.format(sCal.getTime()),
                        sdf2.format(eCal.getTime()));

                sdf1 = new SimpleDateFormat("hh:mma", Locale.getDefault());
                sdf1.setDateFormatSymbols(symbols);


                dateTimeString += getString(R.string.schedule_business_hours_format3,
                        sdf1.format(sCal.getTime()),
                        sdf1.format(eCal.getTime()));

                return dateTimeString;

            } else if (_schedule.getType() == Schedule.Type.OPEN_RAGE) {

                SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy @ hh:mma", Locale.getDefault());
                sdf.setDateFormatSymbols(symbols);

                dateTimeString = getString(R.string.schedule_open_range_format2,
                        sdf.format(sCal.getTime()),
                        sdf.format(eCal.getTime()));

                return dateTimeString;

            } else { //if (schedule.getType() == Schedule.Type.EXACT) {

                SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy @ hh:mma", Locale.getDefault());
                sdf.setDateFormatSymbols(symbols);
                dateTimeString = sdf.format(sCal.getTime());

                return dateTimeString;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }


    private void populateEtaLayout() {

        if (_isRequest || _isConfirm) {
            try {
                _scheduledStartDateTimeCalendar = _etaStartDateTimeCalendar = ISO8601.toCalendar(_schedule.getStartTime());
                _etaStartDateButton.setText(DateUtils.formatDateReallyLongV2(_scheduledStartDateTimeCalendar));

                _etaStartTimeButton.setText(DateUtils.formatTimeLong(_scheduledStartDateTimeCalendar));

                setDuration(_durationMilliseconds > INVALID_NUMBER ? _durationMilliseconds : MIN_JOB_DURATION);

                if (_schedule.getType() == Schedule.Type.EXACT) {
                    _etaStartDateButton.setEnabled(false);
                    _etaStartTimeButton.setEnabled(false);
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        } else if (_isEdit) {

            final Schedule estimatedSchedule;
            if ((estimatedSchedule = _workorder.getEstimatedSchedule()) == null) return;

            try {
                _etaStartDateTimeCalendar = ISO8601.toCalendar(estimatedSchedule.getStartTime());
                _etaStartDateButton.setText(DateUtils.formatDateReallyLongV2(_etaStartDateTimeCalendar));
                _etaStartTimeButton.setText(DateUtils.formatTimeLong(ISO8601.toCalendar(estimatedSchedule.getStartTime())));
                _noteEditText.setText(estimatedSchedule.getNote());

                setDuration(_durationMilliseconds > INVALID_NUMBER ? _durationMilliseconds : MIN_JOB_DURATION);

                if (estimatedSchedule.getType() == Schedule.Type.EXACT) {
                    _etaStartDateButton.setEnabled(false);
                    _etaStartDateButton.setEnabled(false);
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _clear = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        _clear = true;
    }

    @Override
    public void dismiss() {
        _clear = true;
        super.dismiss();
    }


    private boolean isValidDate() {

        if (_schedule.getType() == Schedule.Type.OPEN_RAGE
                || _schedule.getType() == Schedule.Type.BUSINESS_HOURS) {
            try {
                Calendar scheduleStartDateCalendar = ISO8601.toCalendar(_schedule.getStartTime());
                Calendar scheduleEndDateCalendar = ISO8601.toCalendar(_schedule.getEndTime());
                Calendar etaEndDateTimeCalendar = GregorianCalendar.getInstance();
                etaEndDateTimeCalendar.setTimeInMillis(_etaStartDateTimeCalendar.getTimeInMillis() + _durationMilliseconds);

                if ((scheduleStartDateCalendar.getTimeInMillis() > etaEndDateTimeCalendar.getTimeInMillis())
                        || (scheduleEndDateCalendar.getTimeInMillis() < etaEndDateTimeCalendar.getTimeInMillis())) {
                    _etaStartDateTimeCalendar = scheduleStartDateCalendar;
                    _etaStartDateButton.setText(DateUtils.formatDateReallyLongV2(scheduleStartDateCalendar));
                    _etaStartTimeButton.setText(DateUtils.formatTimeLong(scheduleStartDateCalendar));

                    ToastClient.toast(App.get(), getString(R.string.toast_choose_date_between_schedule), Toast.LENGTH_LONG);
                    _handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            _etaStartDatePicker.show();
                        }
                    }, 100);
                    return false;
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

        }

        return true;
    }

    private boolean isValidTime() {

        if (_schedule.getType() == Schedule.Type.OPEN_RAGE || _schedule.getType() == Schedule.Type.BUSINESS_HOURS) {
            try {
                Calendar scheduleStartDateCalendar = ISO8601.toCalendar(_schedule.getStartTime());
                Calendar scheduleEndDateCalendar = ISO8601.toCalendar(_schedule.getEndTime());

                Calendar startTimeCalendar = Calendar.getInstance();
                Calendar endTimeCalendar = Calendar.getInstance();

                if (_schedule.getType() == Schedule.Type.BUSINESS_HOURS) {
                    startTimeCalendar.set(
                            _etaStartDateTimeCalendar.get(Calendar.YEAR),
                            _etaStartDateTimeCalendar.get(Calendar.MONTH),
                            _etaStartDateTimeCalendar.get(Calendar.DAY_OF_MONTH),
                            scheduleStartDateCalendar.get(Calendar.HOUR_OF_DAY),
                            scheduleStartDateCalendar.get(Calendar.MINUTE));

                    endTimeCalendar.set(
                            _etaStartDateTimeCalendar.get(Calendar.YEAR),
                            _etaStartDateTimeCalendar.get(Calendar.MONTH),
                            _etaStartDateTimeCalendar.get(Calendar.DAY_OF_MONTH),
                            scheduleEndDateCalendar.get(Calendar.HOUR_OF_DAY),
                            scheduleEndDateCalendar.get(Calendar.MINUTE));

                } else if (_schedule.getType() == Schedule.Type.OPEN_RAGE) {
                    startTimeCalendar.set(
                            scheduleStartDateCalendar.get(Calendar.YEAR),
                            scheduleStartDateCalendar.get(Calendar.MONTH),
                            scheduleStartDateCalendar.get(Calendar.DAY_OF_MONTH),
                            scheduleStartDateCalendar.get(Calendar.HOUR_OF_DAY),
                            scheduleStartDateCalendar.get(Calendar.MINUTE));

                    endTimeCalendar.set(
                            _etaStartDateTimeCalendar.get(Calendar.YEAR),
                            _etaStartDateTimeCalendar.get(Calendar.MONTH),
                            _etaStartDateTimeCalendar.get(Calendar.DAY_OF_MONTH),
                            _etaStartDateTimeCalendar.get(Calendar.HOUR_OF_DAY),
                            _etaStartDateTimeCalendar.get(Calendar.MINUTE));
                }


                if ((startTimeCalendar.getTimeInMillis() < scheduleStartDateCalendar.getTimeInMillis()) ||
                        (startTimeCalendar.getTimeInMillis() > _etaStartDateTimeCalendar.getTimeInMillis()
                                && DateUtils.returnMinutes(startTimeCalendar.getTimeInMillis()) != DateUtils.returnMinutes(_etaStartDateTimeCalendar.getTimeInMillis()))) {

                    _etaStartTimeButton.setText(DateUtils.formatTimeLong(scheduleStartDateCalendar));

                    _etaStartDateTimeCalendar.set(_etaStartDateTimeCalendar.get(Calendar.YEAR), _etaStartDateTimeCalendar.get(Calendar.MONTH),
                            _etaStartDateTimeCalendar.get(Calendar.DAY_OF_MONTH), scheduleStartDateCalendar.get(Calendar.HOUR_OF_DAY), scheduleStartDateCalendar.get(Calendar.MINUTE));

                    ToastClient.toast(App.get(), getString(R.string.toast_choose_time_between_schedule), Toast.LENGTH_LONG);
                    _handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            _etaStartTimePicker.show();
                        }
                    }, 100);
                    return false;
                }


                long endTimeWithDuration = endTimeCalendar.getTimeInMillis() + _durationMilliseconds;

                if (endTimeWithDuration > scheduleEndDateCalendar.getTimeInMillis()
                        && (DateUtils.returnMinutes(endTimeWithDuration) != DateUtils.returnMinutes(scheduleEndDateCalendar.getTimeInMillis()))) {
                    _etaStartTimeButton.setText(DateUtils.formatTimeLong(scheduleStartDateCalendar));

                    _etaStartDateTimeCalendar.set(_etaStartDateTimeCalendar.get(Calendar.YEAR), _etaStartDateTimeCalendar.get(Calendar.MONTH),
                            _etaStartDateTimeCalendar.get(Calendar.DAY_OF_MONTH), scheduleStartDateCalendar.get(Calendar.HOUR_OF_DAY), scheduleStartDateCalendar.get(Calendar.MINUTE));

                    ToastClient.toast(App.get(), getString(R.string.toast_choose_time_between_schedule), Toast.LENGTH_LONG);
                    _handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            _etaStartTimePicker.show();
                        }
                    }, 100);
                    return false;
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

        }
        return true;
    }




	/*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/


    private final DatePickerDialog.OnDateSetListener _etaStartDate_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            _etaStartDateTimeCalendar.set(year, monthOfYear, dayOfMonth);

            if (DateUtils.isBeforeToday(_etaStartDateTimeCalendar)) {
                ToastClient.toast(App.get(), getString(R.string.toast_previous_date_not_allowed), Toast.LENGTH_LONG);
                _handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        _etaStartDatePicker.show();
                    }
                }, 100);
                return;
            }

            if (isValidDate()) {
                _etaStartDateButton.setText(DateUtils.formatDateReallyLongV2(_etaStartDateTimeCalendar));
            }

        }
    };


    private final TimePickerDialog.OnTimeSetListener _etaStartTime_onSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            _etaStartDateTimeCalendar.set(_etaStartDateTimeCalendar.get(Calendar.YEAR), _etaStartDateTimeCalendar.get(Calendar.MONTH),
                    _etaStartDateTimeCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

            if (isValidTime())
                _etaStartTimeButton.setText(DateUtils.formatTimeLong(_etaStartDateTimeCalendar));

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

            if (_listener != null && _isConfirm && _isEtaEnabled && isValidDate() && isValidTime()) {
//                Log.e(TAG, "_isConfirm");
                _listener.onConfirmEta(_workorder, ISO8601.fromCalendar(_etaStartDateTimeCalendar), _durationMilliseconds, _noteEditText.getText().toString().trim());
            }

            if (_listener != null && _isEtaEnabled && isValidDate() && isValidTime()) {
//                Log.e(TAG, "_isEtaEnabled");
                _listener.onConfirmEta(_workorder, ISO8601.fromCalendar(_etaStartDateTimeCalendar), _durationMilliseconds,  _noteEditText.getText().toString().trim());
            }

            if (_listener != null && _isRequest) {
//                Log.e(TAG, "_isRequest");
                _listener.onRequest(_workorder, _expiringDurationMilliseconds);
            }

            dismiss();
        }
    };

    private final TimePickerDialog.OnTimeSetListener _confirmTime_onSet = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            try {
                _etaStartDateTimeCalendar.set(_etaStartDateTimeCalendar.get(Calendar.YEAR), _etaStartDateTimeCalendar.get(Calendar.MONTH),
                        _etaStartDateTimeCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

                long start = ISO8601.toUtc(_schedule.getStartTime());
                long end = ISO8601.toUtc(_schedule.getEndTime());

                long input = _etaStartDateTimeCalendar.getTimeInMillis();

                if (input < start || input > end) {
                    Toast.makeText(getActivity(), "Arrival time is out of range. Please try again", Toast.LENGTH_LONG).show();
                    _etaStartDateTimeCalendar = ISO8601.toCalendar(_schedule.getStartTime());
                }

                _etaStartDateButton.setText(DateUtils.formatDateReallyLongV2(_etaStartDateTimeCalendar));

            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

    };

    private final View.OnClickListener _etaStartDate_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _etaStartDatePicker.show();
        }
    };

    private final View.OnClickListener _etaStartTime_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _etaStartTimePicker.show();
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

            if (isValidDate() && isValidTime()) {
                _durationMilliseconds = timeMilliseconds;
                _durationButton.setText(misc.convertMsToHuman(timeMilliseconds));
            }
        }

        @Override
        public void onCancel() {
        }
    };


    private final View.OnClickListener _expiringButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _expiryDialog.show(_expiringDurationMilliseconds);
        }
    };

    private final DurationDialog.Listener _expiry_listener = new DurationDialog.Listener() {
        @Override
        public void onOk(long timeMilliseconds) {
            if (timeMilliseconds < MIN_EXPIRING_DURATION) {
                setExpiringDuration(INVALID_NUMBER);
                ToastClient.toast(App.get(), getString(R.string.toast_minimum_expiring_duration), Toast.LENGTH_LONG);
                return;
            }
            _expiringDurationMilliseconds = timeMilliseconds;
            _expirationButton.setText(misc.convertMsToHuman(timeMilliseconds));
        }

        @Override
        public void onCancel() {
        }
    };

    private Switch.OnCheckedChangeListener _switchOnclick_listener = new Switch.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked) {
                _etaLayout.setVisibility(View.VISIBLE);
                _isEtaEnabled = true;
            } else {
                _etaLayout.setVisibility(View.GONE);
                _isEtaEnabled = false;
            }
        }
    };


    public interface Listener {

        void onRequest(Workorder workorder, long milliseconds);

        void onConfirmEta(Workorder workorder, String startDate, long durationMilliseconds, String note);

        void onCancel(Workorder workorder);

    }

}
