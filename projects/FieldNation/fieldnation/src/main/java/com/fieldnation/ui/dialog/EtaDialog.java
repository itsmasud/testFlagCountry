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
import java.util.Locale;

public class EtaDialog extends DialogFragmentBase {
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

            if (savedInstanceState.containsKey(STATE_EXPIRATION_DURATION))
                _expiringDurationMilliseconds = savedInstanceState.getLong(STATE_EXPIRATION_DURATION);

            if (savedInstanceState.containsKey(STATE_DIALOG_STYLE))
                _dialogStyle = savedInstanceState.getString(STATE_DIALOG_STYLE);

            if (savedInstanceState.containsKey(STATE_ETA_START_DATE))
                _etaMilliseconds = savedInstanceState.getLong(STATE_ETA_START_DATE);

            if (savedInstanceState.containsKey(STATE_ETA_SWITCH))
                _isEtaSwitchState = savedInstanceState.getBoolean(STATE_ETA_SWITCH);

            if (savedInstanceState.containsKey(STATE_ETA_START_DATE_SET))
                _isDateSet = savedInstanceState.getBoolean(STATE_ETA_START_DATE_SET);

            if (savedInstanceState.containsKey(STATE_ETA_START_TIME_SET))
                _isTimeSet = savedInstanceState.getBoolean(STATE_ETA_START_TIME_SET);

            if (savedInstanceState.containsKey(STATE_NOTE))
                _note = savedInstanceState.getString(STATE_NOTE);
        }
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
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


        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (_clear) {
            if (_dialogStyle.equals(DIALOG_STYLE_REQUEST)) {
                _switchOnclick_listener.onCheckedChanged(_etaSwitch, false);
                _etaSwitch.setChecked(false);
            }
            populateEtaLayout();
            _clear = false;

        } else {
            _schedule = _workorder.getSchedule();

            setExpiringDuration(_expiringDurationMilliseconds);

            if (_dialogStyle.equals(DIALOG_STYLE_REQUEST)) {
                _switchOnclick_listener.onCheckedChanged(_etaSwitch, _isEtaSwitchState);
                _etaSwitch.setChecked(_isEtaSwitchState);
            }
            populateEtaLayout();

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
        _durationDialog.setListener(_jobDuration_listener);

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


    public void show(Workorder workorder, String dialogStyle) {
        _workorder = workorder;
        _dialogStyle = dialogStyle;
        _schedule = workorder.getSchedule();
        _clear = true;

        super.show();
    }


    private void populateUi() {
        if (_schedule == null)
            return;

        if (misc.isEmptyOrNull(_dialogStyle))
            return;

        if (_dialogStyle.equals(DIALOG_STYLE_REQUEST)) {
            _okButton.setText(getString(R.string.btn_submit));
            _titleTextView.setText("Request " + _workorder.getWorkorderId());
            _requestLayout.setVisibility(View.VISIBLE);
            _etaSwitch.setVisibility(View.VISIBLE);

        } else if (_dialogStyle.equals(DIALOG_STYLE_CONFIRM)) {
            _okButton.setText(getString(R.string.btn_confirm));
            _titleTextView.setText("Confirm " + _workorder.getWorkorderId());
            _requestLayout.setVisibility(View.GONE);
            _etaSwitch.setVisibility(View.GONE);
            _etaLayout.setVisibility(View.VISIBLE);

        } else if (_dialogStyle.equals(DIALOG_STYLE_EDIT)) {
            _okButton.setText(getString(R.string.btn_save));
            _titleTextView.setText(getString(R.string.dialog_eta_title));
            _requestLayout.setVisibility(View.GONE);
            _etaSwitch.setVisibility(View.GONE);
            _etaLayout.setVisibility(View.VISIBLE);

        } else if (_dialogStyle.equals(DIALOG_STYLE_ACCEPT)) {
            _okButton.setText(getString(R.string.btn_accept));
            _titleTextView.setText("Accept " + _workorder.getWorkorderId());
            _requestLayout.setVisibility(View.GONE);
            _etaSwitch.setVisibility(View.GONE);
            _etaLayout.setVisibility(View.VISIBLE);

        }

        final String scheduleDisplayText = getScheduleDisplayText();
        if (scheduleDisplayText == null) {
            _scheduleTextView.setVisibility(View.GONE);
        } else
            _scheduleTextView.setText(getScheduleDisplayText());

        _noteEditText.post(new Runnable() {
            @Override
            public void run() {
                misc.hideKeyboard(_noteEditText);
            }
        });

//        populateEtaLayout();

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

        if (_dialogStyle.equals(DIALOG_STYLE_EDIT)) {

            if ((_estimatedSchedule = _workorder.getEstimatedSchedule()) == null) return;

            try {
                if (_clear) {
                    _etaStartDateTimeCalendar = ISO8601.toCalendar(_estimatedSchedule.getStartTime());
                    _etaMilliseconds = _etaStartDateTimeCalendar.getTimeInMillis();

                    if (!misc.isEmptyOrNull(_estimatedSchedule.getNote())) {
                        _noteEditText.setText(_estimatedSchedule.getNote());
                    }

                    if (_estimatedSchedule.getDuration() > 0)
                        _durationMilliseconds = (long) (_estimatedSchedule.getDuration() * 3600000);

                } else {

                    if (_etaMilliseconds == INVALID_NUMBER)
                        _etaStartDateTimeCalendar = ISO8601.toCalendar(_estimatedSchedule.getStartTime());
                    else
                        _etaStartDateTimeCalendar.setTimeInMillis(_etaMilliseconds);

                    if (!misc.isEmptyOrNull(_note)) {
                        _noteEditText.setText(_note);
                    } else {
                        _noteEditText.setText("");
                        _noteEditText.setHint(getString(R.string.hint_text_optional_details));
                    }
                }
                _etaStartDateButton.setText(DateUtils.formatDateReallyLongV2(_etaStartDateTimeCalendar));
                _etaStartTimeButton.setText(DateUtils.formatTimeLong(ISO8601.toCalendar(_estimatedSchedule.getStartTime())));

                setDuration(_durationMilliseconds > INVALID_NUMBER ? _durationMilliseconds : MIN_JOB_DURATION);

                if (_estimatedSchedule.getType() == Schedule.Type.EXACT) {
                    _etaStartDateButton.setEnabled(false);
                    _etaStartTimeButton.setEnabled(false);
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        } else {

            try {
                _scheduledStartDateTimeCalendar = ISO8601.toCalendar(_schedule.getStartTime());

                if (_clear) {
                    _etaStartDateTimeCalendar = _scheduledStartDateTimeCalendar;
                    _etaMilliseconds = _etaStartDateTimeCalendar.getTimeInMillis();
                    _etaStartDateButton.setText(DateUtils.formatDateReallyLongV2(_scheduledStartDateTimeCalendar));
                    _etaStartTimeButton.setText(DateUtils.formatTimeLong(_scheduledStartDateTimeCalendar));
                    setDuration(MIN_JOB_DURATION);
                    _noteEditText.setText("");
                    _noteEditText.setHint(getString(R.string.hint_text_optional_details));

                } else {

                    if (_etaMilliseconds == INVALID_NUMBER)
                        _etaStartDateTimeCalendar = _scheduledStartDateTimeCalendar;
                    else
                        _etaStartDateTimeCalendar.setTimeInMillis(_etaMilliseconds);

                    _etaStartDateButton.setText(DateUtils.formatDateReallyLongV2(_etaStartDateTimeCalendar));
                    _etaStartTimeButton.setText(DateUtils.formatTimeLong(_etaStartDateTimeCalendar));
                    setDuration(_durationMilliseconds > INVALID_NUMBER ? _durationMilliseconds : MIN_JOB_DURATION);

                    if (!misc.isEmptyOrNull(_note)) {
                        _noteEditText.setText(_note);
                    } else {
                        _noteEditText.setText("");
                        _noteEditText.setHint(getString(R.string.hint_text_optional_details));
                    }


                }

                if (_schedule.getType() == Schedule.Type.EXACT) {
                    _etaStartDateButton.setEnabled(false);
                    _etaStartTimeButton.setEnabled(false);
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _clear = false;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        _clear = false;
    }

    @Override
    public void dismiss() {
        _clear = true;
        super.dismiss();
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

                ToastClient.toast(App.get(), getString(R.string.toast_pick_date_time_between_schedule), Toast.LENGTH_LONG);
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
                ToastClient.toast(App.get(), getString(R.string.toast_pick_date_time_between_schedule), Toast.LENGTH_LONG);
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
                ToastClient.toast(App.get(), getString(R.string.toast_pick_date_time_between_schedule), Toast.LENGTH_LONG);
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
            ToastClient.toast(App.get(), getString(R.string.toast_previous_date_not_allowed), Toast.LENGTH_LONG);
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


	/*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/

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


    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener == null) return;

            if (!isValidDate() || !isValidTime()) return;

            if (_dialogStyle.equals(DIALOG_STYLE_REQUEST)) {
                if (_etaSwitch.isChecked()) {
                    _listener.onRequest(_workorder, _expiringDurationMilliseconds, ISO8601.fromCalendar(_etaStartDateTimeCalendar), _durationMilliseconds, _noteEditText.getText().toString().trim());
                } else {
                    _listener.onRequest(_workorder, _expiringDurationMilliseconds);
                }

            } else if (_dialogStyle.equals(DIALOG_STYLE_EDIT)) {
                _listener.onConfirmEta(_workorder, ISO8601.fromCalendar(_etaStartDateTimeCalendar), _durationMilliseconds, _noteEditText.getText().toString().trim(), true);
            } else {
                _listener.onConfirmEta(_workorder, ISO8601.fromCalendar(_etaStartDateTimeCalendar), _durationMilliseconds, _noteEditText.getText().toString().trim(), false);
            }

            dismiss();
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

    private final DurationDialog.Listener _jobDuration_listener = new DurationDialog.Listener() {
        @Override
        public void onOk(long timeMilliseconds) {
            if (timeMilliseconds < MIN_JOB_DURATION) {
                setDuration(MIN_JOB_DURATION);
                ToastClient.toast(App.get(), getString(R.string.toast_minimum_job_duration), Toast.LENGTH_LONG);
                return;
            }
            long tempJobDuration = _durationMilliseconds;

            _durationMilliseconds = timeMilliseconds;

            if (isValidDate() && isValidTime()) {
                _durationButton.setText(misc.convertMsToHuman(timeMilliseconds));
            } else {
                _durationMilliseconds = tempJobDuration;
                setDuration(_durationMilliseconds);
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
            _isEtaSwitchState = isChecked;

            if (isChecked) {
                _etaLayout.setVisibility(View.VISIBLE);
            } else {
                _etaLayout.setVisibility(View.GONE);
            }
        }
    };


    public interface Listener {

        void onRequest(Workorder workorder, long expirationMilliseconds);

        void onRequest(Workorder workorder, long expirationMilliseconds, String startDate, long durationMilliseconds, String note);

        void onConfirmEta(Workorder workorder, String startDate, long durationMilliseconds, String note, boolean isEditEta);

        void onCancel(Workorder workorder);

    }

}
