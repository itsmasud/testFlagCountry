package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.fieldnation.data.v2.Range;
import com.fieldnation.data.v2.Schedule;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.dialog.DatePickerDialog;
import com.fieldnation.ui.dialog.TimePickerDialog;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Michael on 10/11/2016.
 */

public class EtaDialog extends FullScreenDialog {
    private static final String TAG = "EtaDialog";

    // Dialog Uids
    private static final String UID_EXIPRY_DIALOG = TAG + ".ExpiryDialog";
    private static final String UID_DURATION_DIALOG = TAG + ".DurationDialog";

    // State
    private static final String STATE_DURATION = "STATE_DURATION";
    private static final String STATE_EXPIRATION_DURATION = "STATE_EXPIRATION_DURATION";
    private static final String STATE_NOTE = "STATE_NOTE";
    private static final String STATE_ETA_SWITCH = "STATE_ETA_SWITCH";
    private static final String STATE_ETA_CALENDAR = "STATE_ETA_CALENDAR";

    // Params
    public static final String PARAM_DIALOG_TYPE = "type";
    public static final String PARAM_DIALOG_TYPE_ACCEPT = "accept";
    public static final String PARAM_DIALOG_TYPE_REQUEST = "request";
    public static final String PARAM_DIALOG_TYPE_CONFIRM = "confirm";
    public static final String PARAM_DIALOG_TYPE_EDIT = "edit";
    public static final String PARAM_WORK_ORDER_ID = "workOrderId";
    public static final String PARAM_SCHEDULE = "schedule";

    private final static int MIN_JOB_DURATION = 900000;
    private final static int MIN_EXPIRING_DURATION = 900000;
    private final static int INVALID_NUMBER = -1;
    private final int ONE_DAY = 86400000;

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;

    private TextView _termsWarningTextView;
    private RelativeLayout _expirationLayout;
    private Button _expirationButton;

    private TextView _scheduleTextView;

    private TextView _etaSwitchLabel;
    private Switch _etaSwitch;

    private RelativeLayout _etaLayout;
    private Button _etaStartDateButton;
    private Button _etaStartTimeButton;
    private Button _durationButton;
    private EditText _noteEditText;

    // Dialogs
    private DatePickerDialog _etaStartDatePicker;
    private TimePickerDialog _etaStartTimePicker;
    private DurationDialog.Controller _durationDialog;
    private DurationDialog.Controller _expiryDialog;

    // Passed data
    private String _dialogType;
    private Schedule _schedule;
    private long _workOrderId;

    // User data
    private Calendar _etaStart;
    private long _durationMilliseconds = INVALID_NUMBER;
    private long _expiringDurationMilliseconds = INVALID_NUMBER;
    private boolean _isSwitchOn = true;

    /*-*************************************-*/
    /*-             Life cycle              -*/
    /*-*************************************-*/
    public EtaDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        _etaStart = Calendar.getInstance();

        View v = inflater.inflate(R.layout.dialog_v2_eta, container, false);

        _toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.inflateMenu(R.menu.dialog);

        _finishMenu = (ActionMenuItemView) _toolbar.findViewById(R.id.primary_menu);

        _termsWarningTextView = (TextView) v.findViewById(R.id.termswarning_textview);

        // Expiration stuff
        _expirationLayout = (RelativeLayout) v.findViewById(R.id.request_layout); // expiration layout
        _expirationButton = (Button) v.findViewById(R.id.expiration_button);

        // schedule description
        _scheduleTextView = (TextView) v.findViewById(R.id.schedule_textview);

        // ETA layout
        _etaSwitchLabel = (TextView) v.findViewById(R.id.switchLabel_textview);
        _etaSwitch = (Switch) v.findViewById(R.id.eta_switch);
        _etaLayout = (RelativeLayout) v.findViewById(R.id.eta_layout);
        _etaStartDateButton = (Button) v.findViewById(R.id.etaStartDate_button);
        _etaStartTimeButton = (Button) v.findViewById(R.id.etaStartTime_button);
        _durationButton = (Button) v.findViewById(R.id.duration_button);
        _noteEditText = (EditText) v.findViewById(R.id.note_edittext);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Dialog setup, start them off with today
        _etaStartTimePicker = new TimePickerDialog(_expirationLayout.getContext(), _etaStartTime_onSet,
                _etaStart.get(Calendar.HOUR_OF_DAY),
                _etaStart.get(Calendar.MINUTE), false);

        _etaStartDatePicker = new DatePickerDialog(_expirationLayout.getContext(), _etaStartDate_onSet,
                _etaStart.get(Calendar.YEAR),
                _etaStart.get(Calendar.MONTH),
                _etaStart.get(Calendar.DAY_OF_MONTH));

        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _expirationButton.setOnClickListener(_expiringButton_onClick);

        _etaSwitch.setOnCheckedChangeListener(_switch_onChange);

        _etaStartDateButton.setOnClickListener(_etaStartDate_onClick);
        _etaStartTimeButton.setOnClickListener(_etaStartTime_onClick);
        _durationButton.setOnClickListener(_duration_onClick);

        _durationDialog = new DurationDialog.Controller(App.get(), UID_DURATION_DIALOG);
        _durationDialog.setListener(_durationDialog_listener);

        _expiryDialog = new DurationDialog.Controller(App.get(), UID_EXIPRY_DIALOG);
        _expiryDialog.setListener(_expiryDialog_listener);

        _termsWarningTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (_durationDialog != null) _durationDialog.disconnect(App.get());
        if (_expiryDialog != null) _expiryDialog.disconnect(App.get());
    }

    @Override
    public void show(Bundle params, boolean animate) {
        Log.v(TAG, "Show");
        _schedule = params.getParcelable(PARAM_SCHEDULE);
        _dialogType = params.getString(PARAM_DIALOG_TYPE);
        _workOrderId = params.getLong(PARAM_WORK_ORDER_ID);

        try {
            if (_schedule.getEstimate() != null && _schedule.getEstimate().getArrival() != null) {
                _etaStart = ISO8601.toCalendar(_schedule.getEstimate().getArrival());
            } else if (_schedule.getExact() != null) {
                _etaStart = ISO8601.toCalendar(_schedule.getExact());
            } else {
                _etaStart = ISO8601.toCalendar(_schedule.getRange().getBegin());
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        super.show(params, animate);

        populateUi();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_expiringDurationMilliseconds != INVALID_NUMBER)
            outState.putLong(STATE_EXPIRATION_DURATION, _expiringDurationMilliseconds);

        // ETA stuff
        if (_durationMilliseconds != INVALID_NUMBER)
            outState.putLong(STATE_DURATION, _durationMilliseconds);

        if (!misc.isEmptyOrNull(_noteEditText.getText().toString().trim()))
            outState.putString(STATE_NOTE, _noteEditText.getText().toString().trim());

        outState.putBoolean(STATE_ETA_SWITCH, _etaSwitch.isChecked());
        outState.putSerializable(STATE_ETA_CALENDAR, _etaStart);

        super.onSaveDialogState(outState);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState.containsKey(STATE_EXPIRATION_DURATION))
            _expiringDurationMilliseconds = savedState.getLong(STATE_EXPIRATION_DURATION);

        // ETA stuff
        if (savedState.containsKey(STATE_DURATION))
            _durationMilliseconds = savedState.getLong(STATE_DURATION);

        if (savedState.containsKey(STATE_NOTE))
            _noteEditText.setText(savedState.getString(STATE_NOTE));

        if (savedState.containsKey(STATE_ETA_SWITCH))
            _isSwitchOn = savedState.getBoolean(STATE_ETA_SWITCH);

        if (savedState.containsKey(STATE_ETA_CALENDAR))
            _etaStart = (Calendar) savedState.getSerializable(STATE_ETA_CALENDAR);

        super.onRestoreDialogState(savedState);

        // UI
        populateUi();
    }

    /*-********************************************-*/
    /*-             Internal Mutators              -*/
    /*-********************************************-*/
    private void populateUi() {
        if (misc.isEmptyOrNull(_dialogType))
            return;

        if (_dialogType.equals(PARAM_DIALOG_TYPE_REQUEST)) {
            _toolbar.setTitle("Request " + _workOrderId);
            _finishMenu.setTitle(App.get().getString(R.string.btn_submit));

            _expirationLayout.setVisibility(View.VISIBLE);
            _etaSwitch.setVisibility(View.VISIBLE);
            _etaSwitchLabel.setVisibility(View.VISIBLE);
            _etaSwitch.setChecked(_isSwitchOn);
            _etaLayout.setVisibility(_isSwitchOn ? View.VISIBLE : View.GONE);

            SpannableString spanned = new SpannableString("By requesting this work order you are agreeing to our Work Order Terms and Conditions");
            spanned.setSpan(_terms_onClick, 54, 85, spanned.getSpanFlags(_terms_onClick));
            _termsWarningTextView.setText(spanned);
            _termsWarningTextView.setVisibility(View.VISIBLE);

        } else if (_dialogType.equals(PARAM_DIALOG_TYPE_CONFIRM)) {
            _toolbar.setTitle("Confirm " + _workOrderId);
            _finishMenu.setTitle(App.get().getString(R.string.btn_confirm));
            _expirationLayout.setVisibility(View.GONE);

            _etaSwitch.setChecked(true);
            _etaSwitchLabel.setVisibility(View.GONE);
            _etaSwitch.setVisibility(View.GONE);
            _etaLayout.setVisibility(View.VISIBLE);
            _termsWarningTextView.setVisibility(View.GONE);

        } else if (_dialogType.equals(PARAM_DIALOG_TYPE_EDIT)) {
            _toolbar.setTitle(R.string.dialog_eta_title);
            _finishMenu.setTitle(App.get().getString(R.string.btn_save));
            _expirationLayout.setVisibility(View.GONE);

            _etaSwitch.setChecked(true);
            _etaSwitch.setVisibility(View.GONE);
            _etaSwitchLabel.setVisibility(View.GONE);
            _etaLayout.setVisibility(View.VISIBLE);
            _termsWarningTextView.setVisibility(View.GONE);

        } else if (_dialogType.equals(PARAM_DIALOG_TYPE_ACCEPT)) {
            _toolbar.setTitle("Accept " + _workOrderId);
            _finishMenu.setTitle(App.get().getString(R.string.btn_accept));
            _expirationLayout.setVisibility(View.GONE);

            _etaSwitch.setChecked(true);
            _etaSwitchLabel.setVisibility(View.GONE);
            _etaSwitch.setVisibility(View.GONE);
            _etaLayout.setVisibility(View.VISIBLE);

            SpannableString spanned = new SpannableString("By accepting this work order you are agreeing to our Work Order Terms and Conditions");
            spanned.setSpan(_terms_onClick, 53, 84, spanned.getSpanFlags(_terms_onClick));
            _termsWarningTextView.setText(spanned);
            _termsWarningTextView.setVisibility(View.VISIBLE);
        }

        final String scheduleDisplayText = getScheduleDisplayText();
        if (scheduleDisplayText == null) {
            _scheduleTextView.setVisibility(View.GONE);
        } else
            _scheduleTextView.setText(scheduleDisplayText);

        _etaStartDateButton.setText(DateUtils.formatDateReallyLongV2(_etaStart));
        _etaStartTimeButton.setText(DateUtils.formatTimeLong(_etaStart));

        if (_schedule.getRange() == null) {
            _etaStartDateButton.setEnabled(false);
            _etaStartTimeButton.setEnabled(false);
        } else {
            _etaStartDateButton.setEnabled(true);
            _etaStartTimeButton.setEnabled(true);
        }

        try {
            if (_durationMilliseconds == INVALID_NUMBER) {
//                if (_schedule.getRange() != null) {
//                    if (_schedule.getRange().getType() == Range.Type.BUSINESS
//                            || _schedule.getRange().getType() == Range.Type.RANGE) {
//                        _durationMilliseconds = ISO8601.toUtc(_schedule.getRange().getEnd())
//                                - ISO8601.toUtc(_schedule.getRange().getBegin());
//                        while (_durationMilliseconds > 60 * 60 * 1000 * 24)
//                            _durationMilliseconds = _durationMilliseconds - 60 * 60 * 1000 * 24;
//                    }
//                }
                if (_schedule.getEstimate() != null && _schedule.getEstimate().getDuration() != null) {
                    _durationMilliseconds = (long) (_schedule.getEstimate().getDuration() * 60 * 60 * 1000);
                }
            }
        } catch (Exception e) {
        }

//        if (_durationMilliseconds == INVALID_NUMBER) {
//            _durationMilliseconds = 60 * 60 * 1000; // 1 hr
//        }
        _durationButton.setText(misc.convertMsToHuman(_durationMilliseconds));


        if (_expiringDurationMilliseconds == INVALID_NUMBER) {
            _expirationButton.setText(R.string.btn_never);
        } else {
            _expirationButton.setText(misc.convertMsToHuman(_expiringDurationMilliseconds));
        }
    }

    private boolean isValidEta(final Calendar arrival) {
        if (_schedule.getExact() != null)
            return true;
        else if (_schedule.getRange().getType() == Range.Type.BUSINESS) {
            return isWithinBusinessHours(arrival, _schedule);
        } else if (_schedule.getRange().getType() == Range.Type.RANGE) {
            return isWithinRange(arrival, _schedule);
        }
        return true;
    }

    private static boolean isWithinBusinessHours(final Calendar eta, final Schedule schedule) {
        // make a copy so we don't mess it up
        Calendar arrival = (Calendar) eta.clone();
        try {
            // strategy: test if arrival is within the range at all. If it is,
            // then constrain the check to within a single day, and see if the time falls within that day
            if (passesMidnight(schedule)) {
                Calendar scal = ISO8601.toCalendar(schedule.getRange().getBegin());
                Calendar ecal = ISO8601.toCalendar(schedule.getRange().getEnd());
                ecal.add(Calendar.DAY_OF_MONTH, 1);

                if (arrival.getTimeInMillis() < scal.getTimeInMillis() || arrival.getTimeInMillis() > ecal.getTimeInMillis()) {
                    return false;
                }

                // move to first day
                arrival.set(Calendar.DAY_OF_MONTH, scal.get(Calendar.DAY_OF_MONTH));
                // if too early, then bump a day
                if (arrival.getTimeInMillis() < scal.getTimeInMillis()) {
                    arrival.add(Calendar.DAY_OF_MONTH, 1);
                    // move ecal to the same day. check if arrival is within the end time
                    ecal.set(Calendar.DAY_OF_MONTH, arrival.get(Calendar.DAY_OF_MONTH));
                    if (arrival.getTimeInMillis() <= ecal.getTimeInMillis())
                        return true;
                } else {
                    return true;
                }

            } else {
                Calendar scal = ISO8601.toCalendar(schedule.getRange().getBegin());
                Calendar ecal = ISO8601.toCalendar(schedule.getRange().getEnd());

                if (scal.getTimeInMillis() > arrival.getTimeInMillis() || ecal.getTimeInMillis() < arrival.getTimeInMillis()) {
                    return false;
                }
                // arrival is within the start and end days, constrain check to the day of

                scal.set(Calendar.DAY_OF_MONTH, arrival.get(Calendar.DAY_OF_MONTH));
                ecal.set(Calendar.DAY_OF_MONTH, arrival.get(Calendar.DAY_OF_MONTH));

                if (scal.getTimeInMillis() <= arrival.getTimeInMillis() && ecal.getTimeInMillis() >= arrival.getTimeInMillis())
                    return true;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return false;
    }

    private static boolean passesMidnight(final Schedule schedule) {
        if (!misc.isEmptyOrNull(schedule.getExact()))
            return false;

        try {
            // is business or range
            if (schedule.getRange().getType() == Range.Type.BUSINESS) {
                Calendar scal = ISO8601.toCalendar(schedule.getRange().getBegin());
                Calendar ecal = ISO8601.toCalendar(schedule.getRange().getEnd());

                // end time is earlier in the day than start. that means it crosses midnight
                return scal.get(Calendar.HOUR_OF_DAY) >= ecal.get(Calendar.HOUR_OF_DAY);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return false;
    }


    private boolean isLastDaySelected(final Calendar eta) {
        if (!misc.isEmptyOrNull(_schedule.getExact()))
            return false;

        try {
            // is business or range
            if (_schedule.getRange().getType() == Range.Type.RANGE) {
                Calendar scal = ISO8601.toCalendar(_schedule.getRange().getBegin());
                Calendar ecal = ISO8601.toCalendar(_schedule.getRange().getEnd());

                // if the last day selected
                return eta.get(Calendar.DAY_OF_MONTH) == ecal.get(Calendar.DAY_OF_MONTH);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return false;
    }


    private static boolean isWithinRange(final Calendar arrival, final Schedule schedule) {
        try {
            Calendar scal = ISO8601.toCalendar(schedule.getRange().getBegin());
            Calendar ecal = ISO8601.toCalendar(schedule.getRange().getEnd());

            // if arrival is anytime between the range, then we're good
            if (arrival.getTimeInMillis() >= scal.getTimeInMillis() && arrival.getTimeInMillis() <= ecal.getTimeInMillis()) {
                return true;
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return false;
    }

    private String getScheduleDisplayText() {
        String dateTimeString = "";
        try {
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
            symbols.setAmPmStrings(App.get().getResources().getStringArray(R.array.schedule_capital_case_am_pm_array));

            if (_schedule.getRange() != null && _schedule.getRange().getType() == Range.Type.BUSINESS) {

                Calendar sCal = ISO8601.toCalendar(_schedule.getRange().getBegin());
                Calendar eCal = ISO8601.toCalendar(_schedule.getRange().getEnd());

                SimpleDateFormat sdf1 = new SimpleDateFormat("E, MMM dd", Locale.getDefault());
                sdf1.setDateFormatSymbols(symbols);
                SimpleDateFormat sdf2 = new SimpleDateFormat("E, MMM dd, yyyy", Locale.getDefault());
                sdf2.setDateFormatSymbols(symbols);

                dateTimeString = App.get().getString(R.string.schedule_business_hours_format1,
                        sdf1.format(sCal.getTime()),
                        sdf2.format(eCal.getTime()));

                sdf1 = new SimpleDateFormat("hh:mma", Locale.getDefault());
                sdf1.setDateFormatSymbols(symbols);


                dateTimeString += App.get().getString(R.string.schedule_business_hours_format3,
                        sdf1.format(sCal.getTime()),
                        sdf1.format(eCal.getTime()));

                return dateTimeString;

            } else if (_schedule.getRange() != null && _schedule.getRange().getType() == Range.Type.RANGE) {
                Calendar sCal = ISO8601.toCalendar(_schedule.getRange().getBegin());
                Calendar eCal = ISO8601.toCalendar(_schedule.getRange().getEnd());

                SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy @ hh:mma", Locale.getDefault());
                sdf.setDateFormatSymbols(symbols);

                dateTimeString = App.get().getString(R.string.schedule_open_range_format2,
                        sdf.format(sCal.getTime()),
                        sdf.format(eCal.getTime()));

                return dateTimeString;

            } else { //if (schedule.getType() == Schedule.Type.EXACT) {
                Calendar sCal = ISO8601.toCalendar(_schedule.getExact());

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

    /*-*************************************-*/
    /*-             Ui Events               -*/
    /*-*************************************-*/
    private final ClickableSpan _terms_onClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            OneButtonDialog.Controller.show(App.get(), null, R.string.dialog_terms_title,
                    R.string.dialog_terms_body, R.string.btn_ok, true);
        }
    };

    private final CompoundButton.OnCheckedChangeListener _switch_onChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                _etaLayout.setVisibility(View.VISIBLE);
            } else {
                _etaLayout.setVisibility(View.GONE);
            }
        }
    };

    private final View.OnClickListener _etaStartDate_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _etaStartDatePicker.show();
        }
    };

    private final DatePickerDialog.OnDateSetListener _etaStartDate_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar test = (Calendar) _etaStart.clone();
            test.set(year, monthOfYear, dayOfMonth);

            if (isValidEta(test)) {
                _etaStart = test;
                populateUi();
            } else {
                // the time field might need to be cleared.
                try {
                    test = ISO8601.toCalendar(_schedule.getRange().getBegin());
                    test.set(year, monthOfYear, dayOfMonth);
                    if (isValidEta(test)) {
                        ToastClient.toast(App.get(), "Please select a time within the schedule", Toast.LENGTH_SHORT);
                        _etaStart = test;
                        populateUi();
                        _etaStartTimeButton.setText("");
                    } else {
                        test = ISO8601.toCalendar(_schedule.getRange().getEnd());
                        test.set(year, monthOfYear, dayOfMonth);
                        if (isValidEta(test)) {
                            ToastClient.toast(App.get(), "Please select a time within the schedule", Toast.LENGTH_SHORT);
                            _etaStart = test;
                            populateUi();
                            _etaStartTimeButton.setText("");
                        } else {
                            ToastClient.toast(App.get(), "Please select a date within the schedule", Toast.LENGTH_SHORT);
                        }
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
        }
    };

    private final View.OnClickListener _etaStartTime_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _etaStartTimePicker.show();
        }
    };

    private final TimePickerDialog.OnTimeSetListener _etaStartTime_onSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar test = (Calendar) _etaStart.clone();
            test.set(test.get(Calendar.YEAR), test.get(Calendar.MONTH),
                    test.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

            if (isValidEta(test)) {
                _etaStart = test;
                populateUi();
            } else {
                ToastClient.toast(App.get(), "Please select a time within the schedule", Toast.LENGTH_SHORT);
                // TODO, pop the dialog again?
            }
        }
    };

    private final View.OnClickListener _duration_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            misc.hideKeyboard(_noteEditText);
            DurationDialog.Controller.show(App.get(), UID_DURATION_DIALOG);
        }
    };

    private final DurationDialog.ControllerListener _durationDialog_listener = new DurationDialog.ControllerListener() {
        @Override
        public void onOk(long milliseconds) {
            Log.v(TAG, "_durationDialog_listener.onOk");
            if (milliseconds < MIN_JOB_DURATION) {
                ToastClient.toast(App.get(), R.string.toast_minimum_job_duration, Toast.LENGTH_LONG);
                return;
            }

            if (_schedule.getExact() != null) {
                _durationMilliseconds = milliseconds;
                populateUi();
            } else {
                Calendar test = Calendar.getInstance();
                test.setTimeInMillis(_etaStart.getTimeInMillis() + milliseconds);

                if (isValidEta(test)) {
                    _durationMilliseconds = milliseconds;
                    populateUi();
                } else {
                    ToastClient.toast(App.get(), "Please select a duration within the range", Toast.LENGTH_LONG);
                }
            }
        }

        @Override
        public void onCancel() {
        }
    };

    private final View.OnClickListener _expiringButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DurationDialog.Controller.show(App.get(), UID_EXIPRY_DIALOG);
        }
    };

    private final DurationDialog.ControllerListener _expiryDialog_listener = new DurationDialog.ControllerListener() {
        @Override
        public void onOk(long milliseconds) {
            Log.v(TAG, "_expiryDialog_listener.onOk");
            if (milliseconds < MIN_EXPIRING_DURATION) {
                ToastClient.toast(App.get(), R.string.toast_minimum_expiring_duration, Toast.LENGTH_LONG);
                return;
            }
            _expiringDurationMilliseconds = milliseconds;
            populateUi();
        }

        @Override
        public void onCancel() {
        }
    };

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // only do eta checks if switch is on
            if (_etaSwitch.isChecked()) {
                if (!isValidEta(_etaStart)) {
                    ToastClient.toast(App.get(), "Please enter a valid eta.", Toast.LENGTH_SHORT);
                    return true;
                }

                if (_durationMilliseconds == INVALID_NUMBER) {
                    ToastClient.toast(App.get(), R.string.toast_job_duration_empty, Toast.LENGTH_LONG);
                    return true;
                }
            }

            try {
                if (_dialogType.equals(PARAM_DIALOG_TYPE_REQUEST)) {
                    if (_etaSwitch.isChecked()) {
                        String startDate = ISO8601.fromCalendar(_etaStart);
                        WorkorderClient.actionRequest(
                                App.get(),
                                _workOrderId,
                                _expiringDurationMilliseconds,
                                startDate,
                                ISO8601.getEndDate(startDate, _durationMilliseconds),
                                _noteEditText.getText().toString().trim());
                    } else {
                        WorkorderClient.actionRequest(
                                App.get(),
                                _workOrderId,
                                _expiringDurationMilliseconds,
                                null, null,
                                _noteEditText.getText().toString().trim());
                    }
                    dismiss(true);
                } else {
                    if (_etaSwitch.isChecked()) {
                        String startDate = ISO8601.fromCalendar(_etaStart);
                        WorkorderClient.actionConfirmAssignment(
                                App.get(),
                                _workOrderId,
                                startDate,
                                ISO8601.getEndDate(startDate, _durationMilliseconds),
                                _noteEditText.getText().toString().trim(),
                                _dialogType.equals(PARAM_DIALOG_TYPE_EDIT));
                    } else {
                        WorkorderClient.actionConfirmAssignment(
                                App.get(),
                                _workOrderId,
                                null, null,
                                _noteEditText.getText().toString().trim(),
                                _dialogType.equals(PARAM_DIALOG_TYPE_EDIT));
                    }
                    dismiss(true);
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return true;
        }
    };

    public abstract static class Controller extends com.fieldnation.fndialog.Controller {

        public Controller(Context context, String uid) {
            super(context, EtaDialog.class, uid);
        }

        public static void show(Context context, long workOrderId, Schedule schedule, String dialogType) {
            Bundle params = new Bundle();
            params.putParcelable(PARAM_SCHEDULE, schedule);
            params.putLong(PARAM_WORK_ORDER_ID, workOrderId);
            params.putString(PARAM_DIALOG_TYPE, dialogType);
            show(context, null, EtaDialog.class, params);
        }
    }
}