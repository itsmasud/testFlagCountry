package com.fieldnation.v2.ui.dialog;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.ui.dialog.DatePickerDialog;
import com.fieldnation.ui.dialog.TimePickerDialog;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Assignee;
import com.fieldnation.v2.data.model.Date;
import com.fieldnation.v2.data.model.ETA;
import com.fieldnation.v2.data.model.Request;
import com.fieldnation.v2.data.model.Requests;
import com.fieldnation.v2.data.model.Route;
import com.fieldnation.v2.data.model.Schedule;
import com.fieldnation.v2.data.model.ScheduleServiceWindow;
import com.fieldnation.v2.data.model.User;
import com.fieldnation.v2.data.model.WorkOrder;

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
    private static final String UID_DURATION_DIALOG = TAG + ".DurationPickerDialog";

    // State
    private static final String STATE_DURATION = "STATE_DURATION";
    private static final String STATE_SPINNER_POSITION = "STATE_SPINNER_POSITION";
    private static final String STATE_NOTE = "STATE_NOTE";
    private static final String STATE_ETA_SWITCH = "STATE_ETA_SWITCH";
    private static final String STATE_ETA_CALENDAR = "STATE_ETA_CALENDAR";

    // Params
    //public static final String PARAM_DIALOG_TYPE = "type";
    private static final String PARAM_DIALOG_TYPE_ACCEPT = "accept";
    private static final String PARAM_DIALOG_TYPE_REQUEST = "request";
    //public static final String PARAM_DIALOG_TYPE_CONFIRM = "confirm";
    private static final String PARAM_DIALOG_TYPE_ADD = "add";
    private static final String PARAM_DIALOG_TYPE_EDIT = "edit";
    private static final String PARAM_WORKORDER = "workOrder";

    private final static int MIN_JOB_DURATION = 900000;
    //    private final static int MIN_EXPIRING_DURATION = 900000;
    private final static int INVALID_NUMBER = -1;
    private final int ONE_DAY = 86400000;

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;

    private TextView _termsWarningTextView;
    private RelativeLayout _expirationLayout;
    private CheckBox _expiresCheckBox;
    private HintSpinner _expireSpinner;

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

    // Passed data
    private String _dialogType;
    private WorkOrder _workOrder;

    // User data
    private Calendar _etaStart;
    private long _durationMilliseconds = INVALID_NUMBER;
    private long _expiringDurationSeconds = INVALID_NUMBER;
    private boolean _isSwitchOn = true;
    private int _currentPosition = 1;
    private int[] _durations;
    private WorkordersWebApi _workOrderClient;


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
        _expiresCheckBox = (CheckBox) v.findViewById(R.id.expires_checkbox);
        _expireSpinner = (HintSpinner) v.findViewById(R.id.expire_duration_spinner);

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

        _durations = _expirationLayout.getContext().getResources().getIntArray(R.array.expire_duration_values);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");

        _workOrderClient = new WorkordersWebApi(_workOrderClient_listener);
        _workOrderClient.connect(App.get());

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

        _expiresCheckBox.setOnClickListener(_expires_onClick);
        _expireSpinner.setOnItemSelectedListener(_expireSpinner_selected);
        HintArrayAdapter adapter = HintArrayAdapter.createFromResources(_expirationLayout.getContext(), R.array.expire_duration_titles, R.layout.view_counter_offer_reason_spinner_item);
        adapter.setDropDownViewResource(android.support.design.R.layout.support_simple_spinner_dropdown_item);
        _expireSpinner.setAdapter(adapter);

        _etaSwitch.setOnCheckedChangeListener(_switch_onChange);

        _etaStartDateButton.setOnClickListener(_etaStartDate_onClick);
        _etaStartTimeButton.setOnClickListener(_etaStartTime_onClick);
        _durationButton.setOnClickListener(_duration_onClick);

        DurationPickerDialog.addOnOkListener(UID_DURATION_DIALOG, _durationPickerDialog_onOk);

        _termsWarningTextView.setMovementMethod(LinkMovementMethod.getInstance());

        if (_currentPosition != INVALID_NUMBER) {
            _expiringDurationSeconds = _durations[_currentPosition];
            _expireSpinner.setSelection(_currentPosition);
        }

    }

    @Override
    public void onPause() {
        if (_workOrderClient != null && _workOrderClient.isConnected())
            _workOrderClient.disconnect(App.get());

        super.onPause();
        DurationPickerDialog.removeOnOkListener(UID_DURATION_DIALOG, _durationPickerDialog_onOk);
    }

    @Override
    public void show(Bundle params, boolean animate) {
        Log.v(TAG, "Show");
        _workOrder = params.getParcelable(PARAM_WORKORDER);

        if (_workOrder.getRequests() != null
                && _workOrder.getRequests().getActionsSet().contains(Requests.ActionsEnum.ADD)) {
            _dialogType = PARAM_DIALOG_TYPE_REQUEST;
        } else if (_workOrder.getRoutes() != null
                && _workOrder.getRoutes().getUserRoute() != null
                && _workOrder.getRoutes().getUserRoute().getActionsSet().contains(Route.ActionsEnum.ACCEPT)) {
            _dialogType = PARAM_DIALOG_TYPE_ACCEPT;
        } else if (_workOrder.getEta() != null
                && _workOrder.getEta().getActionsSet().contains(ETA.ActionsEnum.ADD)) {
            _dialogType = PARAM_DIALOG_TYPE_ADD;
        } else if (_workOrder.getEta() != null
                && _workOrder.getEta().getActionsSet().contains(ETA.ActionsEnum.EDIT)) {
            _dialogType = PARAM_DIALOG_TYPE_EDIT;
        }

        try {
            if (_workOrder.getEta() != null && _workOrder.getEta().getStart() != null) {
                _etaStart = _workOrder.getEta().getStart().getCalendar();
            } else if (_workOrder.getSchedule() != null
                    && _workOrder.getSchedule().getServiceWindow() != null
                    && _workOrder.getSchedule().getServiceWindow().getStart() != null) {
                _etaStart = _workOrder.getSchedule().getServiceWindow().getStart().getCalendar();
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        super.show(params, animate);

        populateUi();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        outState.putInt(STATE_SPINNER_POSITION, _currentPosition);

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
        Log.v(TAG, "onRestoreDialogState");
        if (savedState.containsKey(STATE_SPINNER_POSITION))
            _currentPosition = savedState.getInt(STATE_SPINNER_POSITION);

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

        // Wod request work, Woc Request work
        if (_dialogType.equals(PARAM_DIALOG_TYPE_REQUEST)) {
            _toolbar.setTitle("Request " + _workOrder.getId());
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

            // Woc accept route, Wod Accept route (onSetEta)
        } else if (_dialogType.equals(PARAM_DIALOG_TYPE_ACCEPT)) {
            _toolbar.setTitle("Accept " + _workOrder.getId());
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

/*
            // old confirm, work order start, confirm task
        } else if (_dialogType.equals(PARAM_DIALOG_TYPE_CONFIRM)) {
            _toolbar.setTitle("Confirm " + _workOrder.getId());
            _finishMenu.setTitle(App.get().getString(R.string.btn_confirm));
            _expirationLayout.setVisibility(View.GONE);

            _etaSwitch.setChecked(true);
            _etaSwitchLabel.setVisibility(View.GONE);
            _etaSwitch.setVisibility(View.GONE);
            _etaLayout.setVisibility(View.VISIBLE);
            _termsWarningTextView.setVisibility(View.GONE);
*/

            // Add eta from WoC
        } else if (_dialogType.equals(PARAM_DIALOG_TYPE_ADD)) {
            _toolbar.setTitle("Set ETA " + _workOrder.getId());
            _finishMenu.setTitle(App.get().getString(R.string.btn_submit));
            _expirationLayout.setVisibility(View.GONE);

            _etaSwitch.setChecked(true);
            _etaSwitchLabel.setVisibility(View.GONE);
            _etaSwitch.setVisibility(View.GONE);
            _etaLayout.setVisibility(View.VISIBLE);
            _termsWarningTextView.setVisibility(View.GONE);

            // from WoD. change the eta
        } else if (_dialogType.equals(PARAM_DIALOG_TYPE_EDIT)) {
            _toolbar.setTitle(R.string.dialog_eta_title);
            _finishMenu.setTitle(App.get().getString(R.string.btn_save));
            _expirationLayout.setVisibility(View.GONE);

            _etaSwitch.setChecked(true);
            _etaSwitch.setVisibility(View.GONE);
            _etaSwitchLabel.setVisibility(View.GONE);
            _etaLayout.setVisibility(View.VISIBLE);
            _termsWarningTextView.setVisibility(View.GONE);

        }

        final String scheduleDisplayText = getScheduleDisplayText();
        if (scheduleDisplayText == null) {
            _scheduleTextView.setVisibility(View.GONE);
        } else
            _scheduleTextView.setText(scheduleDisplayText);

        _etaStartDateButton.setText(DateUtils.formatDateReallyLongV2(_etaStart));
        _etaStartTimeButton.setText(DateUtils.formatTimeLong(_etaStart));

        if (_workOrder.getSchedule().getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.EXACT) {
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
                if (_workOrder.getEta() != null && _workOrder.getEta().getHourEstimate() != null) {
                    _durationMilliseconds = (long) (_workOrder.getEta().getHourEstimate() * 60 * 60 * 1000);
                }
            }
        } catch (Exception e) {
        }
        _durationButton.setText(misc.convertMsToHuman(_durationMilliseconds));

//        if (_expiringDurationSeconds == INVALID_NUMBER) {
//            _expirationButton.setText(R.string.btn_never);
//        } else {
//            _expirationButton.setText(misc.convertMsToHuman(_expiringDurationSeconds));
//        }
    }

    private boolean isValidEta(final Calendar arrival) {
        if (_workOrder.getSchedule().getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.EXACT) {
            return true;
        } else if (_workOrder.getSchedule().getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.HOURS) {
            return isWithinBusinessHours(arrival, _workOrder.getSchedule());
        } else if (_workOrder.getSchedule().getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.BETWEEN) {
            return isWithinRange(arrival, _workOrder.getSchedule());
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
                Calendar scal = schedule.getServiceWindow().getStart().getCalendar();
                Calendar ecal = schedule.getServiceWindow().getEnd().getCalendar();
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
                Calendar scal = schedule.getServiceWindow().getStart().getCalendar();
                Calendar ecal = schedule.getServiceWindow().getEnd().getCalendar();

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
        if (schedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.EXACT) {
            return false;
        }

        try {
            // is business or range
            if (schedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.HOURS) {
                Calendar scal = schedule.getServiceWindow().getStart().getCalendar();
                Calendar ecal = schedule.getServiceWindow().getEnd().getCalendar();

                // end time is earlier in the day than start. that means it crosses midnight
                return scal.get(Calendar.HOUR_OF_DAY) >= ecal.get(Calendar.HOUR_OF_DAY);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return false;
    }


    private boolean isLastDaySelected(final Calendar eta) {
        if (_workOrder.getSchedule().getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.EXACT)
            return false;

        try {
            // is business or range
            if (_workOrder.getSchedule().getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.BETWEEN) {
                //Calendar scal = _schedule.getServiceWindow().getStart().getCalendar();
                Calendar ecal = _workOrder.getSchedule().getServiceWindow().getEnd().getCalendar();

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
            Calendar scal = schedule.getServiceWindow().getStart().getCalendar();
            Calendar ecal = schedule.getServiceWindow().getEnd().getCalendar();

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

            if (_workOrder.getSchedule().getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.HOURS) {
                Calendar sCal = _workOrder.getSchedule().getServiceWindow().getStart().getCalendar();
                Calendar eCal = _workOrder.getSchedule().getServiceWindow().getEnd().getCalendar();

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

            } else if (_workOrder.getSchedule().getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.BETWEEN) {
                Calendar sCal = _workOrder.getSchedule().getServiceWindow().getStart().getCalendar();
                Calendar eCal = _workOrder.getSchedule().getServiceWindow().getEnd().getCalendar();

                SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy @ hh:mma", Locale.getDefault());
                sdf.setDateFormatSymbols(symbols);

                dateTimeString = App.get().getString(R.string.schedule_open_range_format2,
                        sdf.format(sCal.getTime()),
                        sdf.format(eCal.getTime()));

                return dateTimeString;

            } else { //if (schedule.getType() == Schedule.Type.EXACT) {
                Calendar sCal = _workOrder.getSchedule().getServiceWindow().getStart().getCalendar();

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
            OneButtonDialog.show(App.get(), getUid() + ".oneButtonDialog", R.string.dialog_terms_title,
                    R.string.dialog_terms_body, R.string.btn_ok, true);
        }
    };

    private final CompoundButton.OnCheckedChangeListener _switch_onChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                _isSwitchOn = true;
                _etaLayout.setVisibility(View.VISIBLE);
            } else {
                _isSwitchOn = false;
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
                    test = _workOrder.getSchedule().getServiceWindow().getStart().getCalendar();
                    test.set(year, monthOfYear, dayOfMonth);
                    if (isValidEta(test)) {
                        ToastClient.toast(App.get(), "Please select a time within the schedule", Toast.LENGTH_SHORT);
                        _etaStart = test;
                        populateUi();
                        _etaStartTimeButton.setText("");
                    } else {
                        test = _workOrder.getSchedule().getServiceWindow().getEnd().getCalendar();
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
            DurationPickerDialog.show(App.get(), UID_DURATION_DIALOG);

        }
    };

    private final DurationPickerDialog.OnOkListener _durationPickerDialog_onOk = new DurationPickerDialog.OnOkListener() {
        @Override
        public void onOk(long milliseconds) {
            Log.v(TAG, "milliseconds: " + milliseconds);

            if (milliseconds < MIN_JOB_DURATION) {
                ToastClient.toast(App.get(), R.string.toast_minimum_job_duration, Toast.LENGTH_LONG);
                return;
            }

            if (_workOrder.getSchedule().getServiceWindow().getMode() != ScheduleServiceWindow.ModeEnum.EXACT) {
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
    };

//    private final View.OnClickListener _expiringButton_onClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            DurationPickerDialog.show(App.get(), UID_EXIPRY_DIALOG);
//        }
//    };

    private final AdapterView.OnItemSelectedListener _expireSpinner_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _currentPosition = position;
            _expiringDurationSeconds = _durations[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            _currentPosition = 1;
        }
    };

    private final View.OnClickListener _expires_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!_expiresCheckBox.isChecked()) {
                _expiringDurationSeconds = INVALID_NUMBER;
            }
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
                switch (_dialogType) {
                    case PARAM_DIALOG_TYPE_REQUEST: {
                        _onRequestedDispatcher.dispatch(getUid(), _workOrder.getId());

                        Request request = new Request();
                        request.setNotes(_noteEditText.getText().toString().trim());
                        if (_expiresCheckBox.isChecked())
                            request.setExpires(new Date(System.currentTimeMillis() + _expiringDurationSeconds * 1000));

                        if (_etaSwitch.isChecked()) {
                            ETA eta = new ETA();
                            eta.setStart(new Date(_etaStart));
                            eta.end(new Date(_etaStart.getTimeInMillis() + _durationMilliseconds * 1000));
                            eta.setUser(new User().id((int) App.getProfileId()));
                            eta.setHourEstimate(_durationMilliseconds / 3600.0);
                            request.setEta(eta);
                        }
                        WorkordersWebApi.request(App.get(), _workOrder.getId(), request);

                        dismiss(true);
                        break;
                    }

                    case PARAM_DIALOG_TYPE_ACCEPT: {
                        _onAcceptedDispatcher.dispatch(getUid(), _workOrder.getId());

                        Assignee assignee = new Assignee();
                        assignee.setUser(new User().id((int) App.getProfileId()));
                        WorkordersWebApi.assignUser(App.get(), _workOrder.getId(), assignee);

                        break;
                    }
/*
                    case PARAM_DIALOG_TYPE_CONFIRM:{
                        _onConfirmedDispatcher.dispatch(getUid(), _workOrder.getId());

                        if (_etaSwitch.isChecked()) {
                            String startDate = ISO8601.fromCalendar(_etaStart);
                            WorkorderClient.actionConfirm(
                                    App.get(),
                                    _workOrder.getId(),
                                    startDate,
                                    ISO8601.getEndDate(startDate, _durationMilliseconds),
                                    _noteEditText.getText().toString().trim());
                        } else {
                            WorkorderClient.actionConfirm(
                                    App.get(),
                                    _workOrder.getId(),
                                    null, null,
                                    _noteEditText.getText().toString().trim());
                        }
                        dismiss(true);
                        break;
                        }
*/

                    case PARAM_DIALOG_TYPE_ADD:  // add eta
                    case PARAM_DIALOG_TYPE_EDIT: {
                        _onEtaDispatcher.dispatch(getUid(), _workOrder.getId());
                        ETA eta = new ETA();
                        eta.setStart(new Date(_etaStart));
                        eta.end(new Date(_etaStart.getTimeInMillis() + _durationMilliseconds * 1000));
                        eta.setUser(new User().id((int) App.getProfileId()));
                        eta.setHourEstimate(_durationMilliseconds / 3600.0);
                        WorkordersWebApi.updateETA(App.get(), _workOrder.getId(), eta);
                        dismiss(true);
                        break;
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return true;
        }
    };

    private final WorkordersWebApi.Listener _workOrderClient_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            _workOrderClient.subWorkordersWebApi();
        }

        @Override
        public void onComplete(String methodName, Object successObject, boolean success, Object failObject) {
            if (methodName.equals("assignUser")) {
                if (success) {
                    // TODO this might not work
                    try {
                        ETA eta = new ETA();
                        eta.setStart(new Date(_etaStart));
                        eta.end(new Date(_etaStart.getTimeInMillis() + _durationMilliseconds * 1000));
                        eta.setUser(new User().id((int) App.getProfileId()));
                        WorkordersWebApi.updateETA(App.get(), _workOrder.getId(), eta);
                        eta.setHourEstimate(_durationMilliseconds / 3600.0);
                        dismiss(true);
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                }
            }
        }
    };

    public static void show(Context context, String uid, WorkOrder workOrder) {
        Bundle params = new Bundle();
        params.putParcelable(PARAM_WORKORDER, workOrder);
        Controller.show(context, uid, EtaDialog.class, params);
    }

    /*-*****************************-*/
    /*-         Requested           -*/
    /*-*****************************-*/
    public interface OnRequestedListener {
        void onRequested(int workOrderid);
    }

    private static KeyedDispatcher<OnRequestedListener> _onRequestedDispatcher = new KeyedDispatcher<OnRequestedListener>() {
        @Override
        public void onDispatch(OnRequestedListener listener, Object... parameters) {
            listener.onRequested((int) parameters[0]);
        }
    };

    public static void addOnRequestedListener(String uid, OnRequestedListener onRequestedListener) {
        _onRequestedDispatcher.add(uid, onRequestedListener);
    }

    public static void removeOnRequestedListener(String uid, OnRequestedListener onRequestedListener) {
        _onRequestedDispatcher.remove(uid, onRequestedListener);
    }

    public static void removeAllOnRequestedListener(String uid) {
        _onRequestedDispatcher.removeAll(uid);
    }

    /*-****************************-*/
    /*-         Accepted           -*/
    /*-****************************-*/
    public interface OnAcceptedListener {
        void onAccepted(int workOrderid);
    }

    private static KeyedDispatcher<OnAcceptedListener> _onAcceptedDispatcher = new KeyedDispatcher<OnAcceptedListener>() {
        @Override
        public void onDispatch(OnAcceptedListener listener, Object... parameters) {
            listener.onAccepted((int) parameters[0]);
        }
    };

    public static void addOnAcceptedListener(String uid, OnAcceptedListener onAcceptedListener) {
        _onAcceptedDispatcher.add(uid, onAcceptedListener);
    }

    public static void removeOnAcceptedListener(String uid, OnAcceptedListener onAcceptedListener) {
        _onAcceptedDispatcher.remove(uid, onAcceptedListener);
    }

    public static void removeAllOnAcceptedListener(String uid) {
        _onAcceptedDispatcher.removeAll(uid);
    }

    /*-*****************************-*/
    /*-         Confirmed           -*/
    /*-*****************************-*/
    public interface OnConfirmedListener {
        void onConfirmed(int workOrderid);
    }

    private static KeyedDispatcher<OnConfirmedListener> _onConfirmedDispatcher = new KeyedDispatcher<OnConfirmedListener>() {
        @Override
        public void onDispatch(OnConfirmedListener listener, Object... parameters) {
            listener.onConfirmed((int) parameters[0]);
        }
    };

    public static void addOnConfirmedListener(String uid, OnConfirmedListener onConfirmedListener) {
        _onConfirmedDispatcher.add(uid, onConfirmedListener);
    }

    public static void removeOnConfirmedListener(String uid, OnConfirmedListener onConfirmedListener) {
        _onConfirmedDispatcher.remove(uid, onConfirmedListener);
    }

    public static void removeAllOnConfirmedListener(String uid) {
        _onConfirmedDispatcher.removeAll(uid);
    }

    /*-***********************-*/
    /*-         Eta           -*/
    /*-***********************-*/
    public interface OnEtaListener {
        void onEta(int workOrderid);
    }

    private static KeyedDispatcher<OnEtaListener> _onEtaDispatcher = new KeyedDispatcher<OnEtaListener>() {
        @Override
        public void onDispatch(OnEtaListener listener, Object... parameters) {
            listener.onEta((int) parameters[0]);
        }
    };

    public static void addOnEtaListener(String uid, OnEtaListener onEtaListener) {
        _onEtaDispatcher.add(uid, onEtaListener);
    }

    public static void removeOnEtaListener(String uid, OnEtaListener onEtaListener) {
        _onEtaDispatcher.remove(uid, onEtaListener);
    }

    public static void removeAllOnEtaListener(String uid) {
        _onEtaDispatcher.removeAll(uid);
    }
}