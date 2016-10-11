package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.dialog.DatePickerDialog;
import com.fieldnation.ui.dialog.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by Michael on 10/11/2016.
 */

public class EtaDialog extends FullScreenDialog {
    private static final String TAG = "EtaDialog";

    // State
    private static final String STATE_DIALOG_STYLE = "STATE_DIALOG_STYLE";
    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_DURATION = "STATE_DURATION";
    private static final String STATE_EXPIRATION_DURATION = "STATE_EXPIRATION_DURATION";
    private static final String STATE_ETA_SWITCH = "STATE_ETA_SWITCH";
    private static final String STATE_ETA_START_DATE = "STATE_ETA_START_DATE";
    private static final String STATE_ETA_START_DATE_SET = "STATE_ETA_START_DATE_SET";
    private static final String STATE_ETA_START_TIME_SET = "STATE_ETA_START_TIME_SET";
    private static final String STATE_NOTE = "STATE_NOTE";

    public static final String DIALOG_STYLE_ACCEPT = "DIALOG_STYLE_ACCEPT";
    public static final String DIALOG_STYLE_REQUEST = "DIALOG_STYLE_REQUEST";
    public static final String DIALOG_STYLE_CONFIRM = "DIALOG_STYLE_CONFIRM";
    public static final String DIALOG_STYLE_EDIT = "DIALOG_STYLE_EDIT";

    private final static int MIN_JOB_DURATION = 900000;
    private final static int MIN_EXPIRING_DURATION = 900000;
    private final static int INVALID_NUMBER = -1;
    private final int ONE_DAY = 86400000;

    // Ui
    private Toolbar _toolbar;
    private Button _expirationButton;
    private RelativeLayout _requestLayout;
    private TextView _scheduleTextView;
    private RelativeLayout _etaLayout;
    private Switch _etaSwitch;
    private Button _etaStartDateButton;
    private Button _etaStartTimeButton;
    private Button _durationButton;
    private EditText _noteEditText;

    // Dialogs
    private DatePickerDialog _etaStartDatePicker;
    private TimePickerDialog _etaStartTimePicker;
    private DurationDialog.Controller _durationDialog;
    private DurationDialog.Controller _expiryDialog;

    // Data
    private Calendar _scheduledStartDateTimeCalendar;
    private Calendar _etaStartDateTimeCalendar;

    private Workorder _workorder;
    private Schedule _schedule;
    private Schedule _estimatedSchedule;
    private boolean _isEtaSwitchState = false;
    private String _dialogStyle;
    private String _note;
    private boolean _isDateSet = false;
    private boolean _isTimeSet = false;
    private final Handler _handler = new Handler();

    private long _etaMilliseconds = INVALID_NUMBER;
    private long _durationMilliseconds = INVALID_NUMBER;
    private long _expiringDurationMilliseconds = INVALID_NUMBER;
    private boolean _clear = false;
    private boolean _hasMidnightSupport = false;

    public EtaDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_eta, container, false);

        _toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        _etaStartDateTimeCalendar = Calendar.getInstance();

        _requestLayout = (RelativeLayout) v.findViewById(R.id.request_layout);
        _expirationButton = (Button) v.findViewById(R.id.expiration_button);

        _etaStartDateButton = (Button) v.findViewById(R.id.etaStartDate_button);

        _durationButton = (Button) v.findViewById(R.id.duration_button);

        _scheduleTextView = (TextView) v.findViewById(R.id.schedule_textview);

        _etaStartTimePicker = new TimePickerDialog(context, _etaStartTime_onSet, _etaStartDateTimeCalendar.get(Calendar.HOUR_OF_DAY), _etaStartDateTimeCalendar.get(Calendar.MINUTE), false);
        _etaStartDatePicker = new DatePickerDialog(context, _etaStartDate_onSet, _etaStartDateTimeCalendar.get(Calendar.YEAR), _etaStartDateTimeCalendar.get(Calendar.MONTH), _etaStartDateTimeCalendar.get(Calendar.DAY_OF_MONTH));

        _durationDialog = new DurationDialog.Controller(App.get(), TAG + ".DurationDialog");
        _durationDialog.setListener(_durationDialog_listener);

        _expiryDialog = new DurationDialog.Controller(App.get(), TAG + ".ExpiryDialog");
        _expiryDialog.setListener(_expiryDialog_listener);

        _etaLayout = (RelativeLayout) v.findViewById(R.id.eta_layout);
        _etaSwitch = (Switch) v.findViewById(R.id.enableEta_switch);
        _etaStartTimeButton = (Button) v.findViewById(R.id.etaStartTime_button);
        _noteEditText = (EditText) v.findViewById(R.id.note_edittext);

        return v;
    }

    @Override
    public void onAdded() {
        super.onAdded();
        _expirationButton.setOnClickListener(_expiringButton_onClick);
        _cancelButton.setOnClickListener(_cancel_onClick);
        _okButton.setOnClickListener(_ok_onClick);
        _etaStartDateButton.setOnClickListener(_etaStartDate_onClick);
        _durationButton.setOnClickListener(_duration_onClick);
        _etaSwitch.setOnCheckedChangeListener(_switchOnclick_listener);
        _etaStartTimeButton.setOnClickListener(_etaStartTime_onClick);
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);

        if (_durationMilliseconds != INVALID_NUMBER)
            outState.putLong(STATE_DURATION, _durationMilliseconds);

        if (_expiringDurationMilliseconds != INVALID_NUMBER)
            outState.putLong(STATE_EXPIRATION_DURATION, _expiringDurationMilliseconds);

        if (_etaMilliseconds != INVALID_NUMBER)
            outState.putLong(STATE_ETA_START_DATE, _etaMilliseconds);

        outState.putBoolean(STATE_ETA_SWITCH, _etaSwitch.isChecked());
        outState.putBoolean(STATE_ETA_START_DATE_SET, _isDateSet);
        outState.putBoolean(STATE_ETA_START_TIME_SET, _isTimeSet);

        if (!misc.isEmptyOrNull(_dialogStyle))
            outState.putString(STATE_DIALOG_STYLE, _dialogStyle);

        if (!misc.isEmptyOrNull(_noteEditText.getText().toString().trim()))
            outState.putString(STATE_NOTE, _noteEditText.getText().toString().trim());

        super.onSaveDialogState(outState);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState.containsKey(STATE_WORKORDER))
            _workorder = savedState.getParcelable(STATE_WORKORDER);

        if (savedState.containsKey(STATE_DURATION))
            _durationMilliseconds = savedState.getLong(STATE_DURATION);

        if (savedState.containsKey(STATE_EXPIRATION_DURATION))
            _expiringDurationMilliseconds = savedState.getLong(STATE_EXPIRATION_DURATION);

        if (savedState.containsKey(STATE_DIALOG_STYLE))
            _dialogStyle = savedState.getString(STATE_DIALOG_STYLE);

        if (savedState.containsKey(STATE_ETA_START_DATE))
            _etaMilliseconds = savedState.getLong(STATE_ETA_START_DATE);

        if (savedState.containsKey(STATE_ETA_SWITCH))
            _isEtaSwitchState = savedState.getBoolean(STATE_ETA_SWITCH);

        if (savedState.containsKey(STATE_ETA_START_DATE_SET))
            _isDateSet = savedState.getBoolean(STATE_ETA_START_DATE_SET);

        if (savedState.containsKey(STATE_ETA_START_TIME_SET))
            _isTimeSet = savedState.getBoolean(STATE_ETA_START_TIME_SET);

        if (savedState.containsKey(STATE_NOTE))
            _note = savedState.getString(STATE_NOTE);
        super.onRestoreDialogState(savedState);
    }

    private boolean isValidDate() {
        if (isSelectedEtaPastDay()) return false;
        if (_schedule.getType() == Schedule.Type.EXACT) return true;

        try {
            _etaStartDateTimeCalendar = DateUtils.ceilUptoMinutes(_etaStartDateTimeCalendar);
            Calendar scheduleStartDateCalendar = ISO8601.toCalendar(_schedule.getStartTime());
            Calendar scheduleEndDateCalendar = ISO8601.toCalendar(_schedule.getEndTime());
            Calendar etaEndDateCalendar = Calendar.getInstance();
            etaEndDateCalendar.set(_etaStartDateTimeCalendar.get(Calendar.YEAR),
                    _etaStartDateTimeCalendar.get(Calendar.MONTH),
                    _etaStartDateTimeCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

            // midnight support
            if (_schedule.getType() == Schedule.Type.BUSINESS_HOURS &&
                    scheduleStartDateCalendar.get(Calendar.HOUR_OF_DAY) >= scheduleEndDateCalendar.get(Calendar.HOUR_OF_DAY)) {
                _hasMidnightSupport = true;
                scheduleEndDateCalendar.setTimeInMillis(scheduleEndDateCalendar.getTimeInMillis() + ONE_DAY);
            } else {
                _hasMidnightSupport = false;
            }

            if ((scheduleStartDateCalendar.getTimeInMillis() > etaEndDateCalendar.getTimeInMillis())
                    || (scheduleEndDateCalendar.getTimeInMillis() < etaEndDateCalendar.getTimeInMillis())) {

                // midnight support
                if (_hasMidnightSupport && DateUtils.isSameDay(scheduleEndDateCalendar, _etaStartDateTimeCalendar)) {
                    return true;
                }

                ToastClient.toast(App.get(), R.string.toast_pick_date_time_between_schedule, Toast.LENGTH_LONG);
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

        return true;
    }

    private boolean isValidTime() {
        if (isSelectedEtaPastDay()) return false;

        if (_schedule.getType() == Schedule.Type.EXACT) return true;

        try {
            _etaStartDateTimeCalendar = DateUtils.ceilUptoMinutes(_etaStartDateTimeCalendar);
            Calendar scheduleStartDateCalendar = DateUtils.ceilUptoMinutes(ISO8601.toCalendar(_schedule.getStartTime()));
            Calendar scheduleEndDateCalendar = DateUtils.ceilUptoMinutes(ISO8601.toCalendar(_schedule.getEndTime()));

            Calendar startTimeCalendar = DateUtils.ceilUptoMinutes(Calendar.getInstance());
            Calendar endHourCalendar = DateUtils.ceilUptoMinutes(Calendar.getInstance());
            Calendar endTimeCalendar = DateUtils.ceilUptoMinutes(Calendar.getInstance());

            if (_schedule.getType() == Schedule.Type.BUSINESS_HOURS) {

                startTimeCalendar.set(
                        _etaStartDateTimeCalendar.get(Calendar.YEAR),
                        _etaStartDateTimeCalendar.get(Calendar.MONTH),
                        _etaStartDateTimeCalendar.get(Calendar.DAY_OF_MONTH),
                        scheduleStartDateCalendar.get(Calendar.HOUR_OF_DAY),
                        scheduleStartDateCalendar.get(Calendar.MINUTE));
                startTimeCalendar = DateUtils.ceilUptoMinutes(startTimeCalendar);

                endTimeCalendar.setTimeInMillis(_etaStartDateTimeCalendar.getTimeInMillis() + _durationMilliseconds);
                endTimeCalendar = DateUtils.ceilUptoMinutes(endTimeCalendar);

                endHourCalendar.set(
                        _etaStartDateTimeCalendar.get(Calendar.YEAR),
                        _etaStartDateTimeCalendar.get(Calendar.MONTH),
                        _etaStartDateTimeCalendar.get(Calendar.DAY_OF_MONTH),
                        scheduleEndDateCalendar.get(Calendar.HOUR_OF_DAY),
                        scheduleEndDateCalendar.get(Calendar.MINUTE));
                endHourCalendar = DateUtils.ceilUptoMinutes(endHourCalendar);

                // midnight support
                if (scheduleStartDateCalendar.get(Calendar.HOUR_OF_DAY) >= scheduleEndDateCalendar.get(Calendar.HOUR_OF_DAY)) {
                    _hasMidnightSupport = true;
                    scheduleEndDateCalendar.setTimeInMillis(scheduleEndDateCalendar.getTimeInMillis() + ONE_DAY);
                    scheduleEndDateCalendar = DateUtils.ceilUptoMinutes(scheduleEndDateCalendar);

                    Calendar calNight12am = Calendar.getInstance();
                    Calendar calDay12pm = Calendar.getInstance();

                    calNight12am.set(_etaStartDateTimeCalendar.get(Calendar.YEAR),
                            _etaStartDateTimeCalendar.get(Calendar.MONTH),
                            _etaStartDateTimeCalendar.get(Calendar.DAY_OF_MONTH), 0, 0);
                    calNight12am = DateUtils.ceilUptoMinutes(calNight12am);
                    calDay12pm.set(_etaStartDateTimeCalendar.get(Calendar.YEAR),
                            _etaStartDateTimeCalendar.get(Calendar.MONTH),
                            _etaStartDateTimeCalendar.get(Calendar.DAY_OF_MONTH), 12, 0);
                    calDay12pm = DateUtils.ceilUptoMinutes(calDay12pm);

                    if ((_etaStartDateTimeCalendar.getTimeInMillis() >= calNight12am.getTimeInMillis())
                            && _etaStartDateTimeCalendar.getTimeInMillis() < calDay12pm.getTimeInMillis()) {
                        startTimeCalendar.setTimeInMillis(endHourCalendar.getTimeInMillis() - ONE_DAY);
                        startTimeCalendar = DateUtils.ceilUptoMinutes(startTimeCalendar);
                    }

                    if (_etaStartDateTimeCalendar.getTimeInMillis() > calDay12pm.getTimeInMillis()) {
                        endHourCalendar.setTimeInMillis(endHourCalendar.getTimeInMillis() + ONE_DAY);
                        endHourCalendar = DateUtils.ceilUptoMinutes(endHourCalendar);
                    }

                } else {
                    _hasMidnightSupport = false;
                }


            } else if (_schedule.getType() == Schedule.Type.OPEN_RAGE) {
                startTimeCalendar = DateUtils.ceilUptoMinutes(scheduleStartDateCalendar);
                endTimeCalendar.setTimeInMillis(_etaStartDateTimeCalendar.getTimeInMillis() + _durationMilliseconds);
                endTimeCalendar = DateUtils.ceilUptoMinutes(endTimeCalendar);
                endHourCalendar = DateUtils.ceilUptoMinutes(scheduleEndDateCalendar);
            }
//                Log.e(TAG, "start time validation starts");
//                Log.v(TAG, "startTimeCalendar: " + DateUtils.formatDateTimeLong(startTimeCalendar));

            if ((startTimeCalendar.getTimeInMillis() < scheduleStartDateCalendar.getTimeInMillis()) ||
                    (startTimeCalendar.getTimeInMillis() > _etaStartDateTimeCalendar.getTimeInMillis())) {
                ToastClient.toast(App.get(), R.string.toast_pick_date_time_between_schedule, Toast.LENGTH_LONG);
                _handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        _etaStartTimePicker.show();
                    }
                }, 100);
                return false;
            }

            if ((endTimeCalendar.getTimeInMillis() > scheduleEndDateCalendar.getTimeInMillis())
                    || (endTimeCalendar.getTimeInMillis() > endHourCalendar.getTimeInMillis())) {
                ToastClient.toast(App.get(), R.string.toast_pick_date_time_between_schedule, Toast.LENGTH_LONG);
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


        return true;
    }

    private boolean isSelectedEtaPastDay() {
        if (DateUtils.isBeforeToday(_etaStartDateTimeCalendar)) {
            ToastClient.toast(App.get(), R.string.toast_previous_date_not_allowed, Toast.LENGTH_LONG);
            _handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    _etaStartDatePicker.show();
                }
            }, 100);
            return true;
        }
        return false;
    }

    private final View.OnClickListener _expiringButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DurationDialog.Controller.show(App.get(), TAG + ".ExpiryDialog");
        }
    };

    private final DatePickerDialog.OnDateSetListener _etaStartDate_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar tempEtaDateTimeCal = DateUtils.ceilUptoMinutes(_etaStartDateTimeCalendar);
            _etaStartDateTimeCalendar.set(year, monthOfYear, dayOfMonth);
            _etaStartDateTimeCalendar = DateUtils.ceilUptoMinutes(_etaStartDateTimeCalendar);

            if (isSelectedEtaPastDay()) {
                _etaStartDateTimeCalendar = tempEtaDateTimeCal;
                return;
            }

            if (isValidDate()) {
                _isDateSet = true;
                _etaMilliseconds = _etaStartDateTimeCalendar.getTimeInMillis();
                _etaStartDateButton.setText(DateUtils.formatDateReallyLongV2(_etaStartDateTimeCalendar));
            } else {
                _etaStartDateTimeCalendar = tempEtaDateTimeCal;
            }
        }
    };


    private final TimePickerDialog.OnTimeSetListener _etaStartTime_onSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar tempEtaCal = DateUtils.ceilUptoMinutes(_etaStartDateTimeCalendar);
            _etaStartDateTimeCalendar.set(_etaStartDateTimeCalendar.get(Calendar.YEAR), _etaStartDateTimeCalendar.get(Calendar.MONTH),
                    _etaStartDateTimeCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

            if (isValidTime()) {
                _isTimeSet = true;
                _etaMilliseconds = _etaStartDateTimeCalendar.getTimeInMillis();
                _etaStartTimeButton.setText(DateUtils.formatTimeLong(_etaStartDateTimeCalendar));
            } else {
                _etaStartDateTimeCalendar = tempEtaCal;
            }

        }
    };

    private final DurationDialog.ControllerListener _durationDialog_listener = new DurationDialog.ControllerListener() {
        @Override
        public void onOk(long milliseconds) {

        }

        @Override
        public void onCancel() {

        }
    };

    private final DurationDialog.ControllerListener _expiryDialog_listener = new DurationDialog.ControllerListener() {
        @Override
        public void onOk(long milliseconds) {

        }

        @Override
        public void onCancel() {

        }
    };

}
