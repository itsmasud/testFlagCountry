package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
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
    private static final String STATE_ETA_SWITCH = "STATE_ETA_SWITCH";
    private static final String STATE_NOTE = "STATE_NOTE";

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

    private RelativeLayout _expirationLayout;
    private Button _expirationButton;

    private TextView _scheduleTextView;

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

    // Data
    private final Handler _handler = new Handler();

    // Passed data
    private String _dialogType;
    private Schedule _schedule;
    private long _workOrderId;

    // User data
    private Calendar _etaStart;
    private long _durationMilliseconds = INVALID_NUMBER;
    private long _expiringDurationMilliseconds = INVALID_NUMBER;

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

        // Expiration stuff
        _expirationLayout = (RelativeLayout) v.findViewById(R.id.request_layout); // expiration layout
        _expirationButton = (Button) v.findViewById(R.id.expiration_button);

        // schedule description
        _scheduleTextView = (TextView) v.findViewById(R.id.schedule_textview);

        _etaSwitch = (Switch) v.findViewById(R.id.enableEta_switch);
        // ETA layout
        _etaLayout = (RelativeLayout) v.findViewById(R.id.eta_layout);
        _etaStartDateButton = (Button) v.findViewById(R.id.etaStartDate_button);
        _etaStartTimeButton = (Button) v.findViewById(R.id.etaStartTime_button);
        _durationButton = (Button) v.findViewById(R.id.duration_button);
        _noteEditText = (EditText) v.findViewById(R.id.note_edittext);

        // Dialog setup, start them off with today
        _etaStartTimePicker = new TimePickerDialog(context, _etaStartTime_onSet,
                _etaStart.get(Calendar.HOUR_OF_DAY),
                _etaStart.get(Calendar.MINUTE), false);

        _etaStartDatePicker = new DatePickerDialog(context, _etaStartDate_onSet,
                _etaStart.get(Calendar.YEAR),
                _etaStart.get(Calendar.MONTH),
                _etaStart.get(Calendar.DAY_OF_MONTH));
        return v;
    }

    @Override
    public void onAdded() {
        super.onAdded();
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        _toolbar.inflateMenu(R.menu.dialog);
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _finishMenu = (ActionMenuItemView) _toolbar.findViewById(R.id.primary_menu);

        _expirationButton.setOnClickListener(_expiringButton_onClick);

        _etaSwitch.setOnCheckedChangeListener(_switchOnclick_listener);
        _etaStartDateButton.setOnClickListener(_etaStartDate_onClick);
        _etaStartTimeButton.setOnClickListener(_etaStartTime_onClick);
        _durationButton.setOnClickListener(_duration_onClick);

        _durationDialog = new DurationDialog.Controller(App.get(), UID_DURATION_DIALOG);
        _durationDialog.setListener(_durationDialog_listener);

        _expiryDialog = new DurationDialog.Controller(App.get(), UID_EXIPRY_DIALOG);
        _expiryDialog.setListener(_expiryDialog_listener);
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        if (_durationDialog != null) _durationDialog.disconnect(App.get());
        if (_expiryDialog != null) _expiryDialog.disconnect(App.get());
    }

    @Override
    public void show(Bundle params, boolean animate) {
        _schedule = params.getParcelable(PARAM_SCHEDULE);
        _dialogType = params.getString(PARAM_DIALOG_TYPE);
        _workOrderId = params.getLong(PARAM_WORK_ORDER_ID);

        super.show(params, animate);

        if (_schedule.getRange() == null) {
            _etaStartDateButton.setEnabled(false);
            _etaStartTimeButton.setEnabled(false);
        }

        populateUi();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_expiringDurationMilliseconds != INVALID_NUMBER)
            outState.putLong(STATE_EXPIRATION_DURATION, _expiringDurationMilliseconds);

        // ETA stuff
        outState.putBoolean(STATE_ETA_SWITCH, _etaSwitch.isChecked());

        if (_durationMilliseconds != INVALID_NUMBER)
            outState.putLong(STATE_DURATION, _durationMilliseconds);

        if (!misc.isEmptyOrNull(_noteEditText.getText().toString().trim()))
            outState.putString(STATE_NOTE, _noteEditText.getText().toString().trim());

        super.onSaveDialogState(outState);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState.containsKey(STATE_EXPIRATION_DURATION))
            _expiringDurationMilliseconds = savedState.getLong(STATE_EXPIRATION_DURATION);

        // ETA stuff
        if (savedState.containsKey(STATE_ETA_SWITCH)) {
            boolean s = savedState.getBoolean(STATE_ETA_SWITCH);
            _etaSwitch.setChecked(s);
            _switchOnclick_listener.onCheckedChanged(_etaSwitch, s);
        }

        if (savedState.containsKey(STATE_DURATION))
            _durationMilliseconds = savedState.getLong(STATE_DURATION);

        if (savedState.containsKey(STATE_NOTE))
            _noteEditText.setText(savedState.getString(STATE_NOTE));

        super.onRestoreDialogState(savedState);

        // Restore some UI
        populateEta();
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

        } else if (_dialogType.equals(PARAM_DIALOG_TYPE_CONFIRM)) {
            _toolbar.setTitle("Confirm " + _workOrderId);
            _finishMenu.setTitle(App.get().getString(R.string.btn_confirm));
            _expirationLayout.setVisibility(View.GONE);
            _etaSwitch.setVisibility(View.GONE);
            _etaLayout.setVisibility(View.VISIBLE);

        } else if (_dialogType.equals(PARAM_DIALOG_TYPE_EDIT)) {
            _toolbar.setTitle(R.string.dialog_eta_title);
            _finishMenu.setTitle(App.get().getString(R.string.btn_save));
            _expirationLayout.setVisibility(View.GONE);
            _etaSwitch.setVisibility(View.GONE);
            _etaLayout.setVisibility(View.VISIBLE);

        } else if (_dialogType.equals(PARAM_DIALOG_TYPE_ACCEPT)) {
            _toolbar.setTitle("Accept " + _workOrderId);
            _finishMenu.setTitle(App.get().getString(R.string.btn_accept));
            _expirationLayout.setVisibility(View.GONE);
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
    }

    private void populateEta() {
        setExpiringDuration(_expiringDurationMilliseconds);

        _etaStartDateButton.setText(DateUtils.formatDateReallyLongV2(_etaStart));
        _etaStartTimeButton.setText(DateUtils.formatTimeLong(_etaStart));
        _durationButton.setText(misc.convertMsToHuman(_durationMilliseconds));
    }

    private boolean passesMidnight() {
        if (!misc.isEmptyOrNull(_schedule.getExact()))
            return false;

        try {
            // is business or range
            if (_schedule.getRange().getType() == Range.Type.BUSINESS) {
                Calendar scal = ISO8601.toCalendar(_schedule.getRange().getBegin());
                Calendar ecal = ISO8601.toCalendar(_schedule.getRange().getEnd());

                // end time is earlier in the day than start. that means it crosses midnight
                return scal.get(Calendar.HOUR_OF_DAY) >= ecal.get(Calendar.HOUR_OF_DAY);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return false;
    }

    // a valid date is defined as
    // not in the past
    //
    //
    private boolean isValidDay() {
        // clamp start time
        Calendar etaStart = DateUtils.clearTime(_etaStart);

        if (isSelectedEtaBeforeToday()) {
            ToastClient.toast(App.get(), R.string.toast_previous_date_not_allowed, Toast.LENGTH_LONG);
            _handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    _etaStartDatePicker.show();
                }
            }, 100);
            return false;
        }

        // is exact work order
        if (!misc.isEmptyOrNull(_schedule.getExact()))
            return true;

        boolean result = false;
        try {
            // start is already in the future. just need to clamp it to end date
            if (_schedule.getRange().getType() == Range.Type.BUSINESS) {
                Calendar ecal = DateUtils.clearTime(ISO8601.toCalendar(_schedule.getRange().getEnd()));
                // if passes midnight, then add 1 day
                if (passesMidnight()) {
                    ecal.add(Calendar.DAY_OF_YEAR, 1);
                }
                // return true if eta start day <= end day
                result = etaStart.getTimeInMillis() <= ecal.getTimeInMillis();
            } else { // RANGE
                // assume the date range doesn't care about midnight
                // return true if eta day <= end day
                Calendar ecal = DateUtils.clearTime(ISO8601.toCalendar(_schedule.getRange().getEnd()));
                result = etaStart.getTimeInMillis() <= ecal.getTimeInMillis();

            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (!result) {
            ToastClient.toast(App.get(), R.string.toast_pick_date_time_between_schedule, Toast.LENGTH_LONG);
            _handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    _etaStartDatePicker.show();
                }
            }, 100);

        }
        return result;
    }

    private boolean isValidTime() {
        // in the past, not valid
        if (isSelectedEtaBeforeToday()) {
            ToastClient.toast(App.get(), R.string.toast_previous_date_not_allowed, Toast.LENGTH_LONG);
            _handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    _etaStartDatePicker.show();
                }
            }, 100);
            return false;
        }

        // exact time, can't change. so it's valid
        if (!misc.isEmptyOrNull(_schedule.getExact()))
            return true;

        boolean result = false;
        // we don't need to worry about dates here, just the time part
        Calendar etaStart = Calendar.getInstance();
        etaStart.setTimeInMillis(_etaStart.getTimeInMillis());
        Calendar etaEnd = Calendar.getInstance();
        etaEnd.setTimeInMillis(etaStart.getTimeInMillis() + _durationMilliseconds);
        try {
            // if business hours
            if (_schedule.getRange().getType() == Range.Type.BUSINESS) {
                Calendar scal = ISO8601.toCalendar(_schedule.getRange().getBegin());
                Calendar ecal = ISO8601.toCalendar(_schedule.getRange().getEnd());
                // if passes midnight, then
                if (passesMidnight()) {
                    // if etaStart time > end time and less than start time... then it is within the section that is not allowed. This is an easier test!
                    result = !(etaStart.get(Calendar.HOUR_OF_DAY) > ecal.get(Calendar.HOUR_OF_DAY)
                            && etaEnd.get(Calendar.HOUR_OF_DAY) < scal.get(Calendar.HOUR_OF_DAY));
                } else {
                    result = etaStart.get(Calendar.HOUR_OF_DAY) > scal.get(Calendar.HOUR_OF_DAY)
                            && etaEnd.get(Calendar.HOUR_OF_DAY) < ecal.get(Calendar.HOUR_OF_DAY);
                }
            } else {
                // TODO else handle range...
                result = true;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (!result) {
            ToastClient.toast(App.get(), R.string.toast_pick_date_time_between_schedule, Toast.LENGTH_LONG);
            _handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    _etaStartTimePicker.show();
                }
            }, 100);
        }
        return result;
    }

    private boolean isSelectedEtaBeforeToday() {
        // is the date in the past?
        if (DateUtils.isBeforeToday(_etaStart)) {
            return true;
        }
        return false;
    }

    private void setDuration(long timeMilliseconds) {
        if (timeMilliseconds == INVALID_NUMBER) {
            _durationButton.setText("");
        } else {
            _durationMilliseconds = timeMilliseconds;
            _durationButton.setText(misc.convertMsToHuman(_durationMilliseconds));
        }
    }

    private void setExpiringDuration(long timeMilliseconds) {
        if (timeMilliseconds == INVALID_NUMBER) {
            _expirationButton.setText(R.string.btn_never);
        } else {
            _expiringDurationMilliseconds = timeMilliseconds;
            _expirationButton.setText(misc.convertMsToHuman(_expiringDurationMilliseconds));
        }
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
    private Switch.OnCheckedChangeListener _switchOnclick_listener = new Switch.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                _etaLayout.setVisibility(View.VISIBLE);
            } else {
                _etaLayout.setVisibility(View.GONE);
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
            _etaStart.set(_etaStart.get(Calendar.YEAR), _etaStart.get(Calendar.MONTH),
                    _etaStart.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

            if (isValidTime()) {
                _etaStartTimeButton.setText(DateUtils.formatTimeLong(_etaStart));
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
            _etaStart.set(year, monthOfYear, dayOfMonth);

            if (isSelectedEtaBeforeToday()) {
                ToastClient.toast(App.get(), R.string.toast_previous_date_not_allowed, Toast.LENGTH_LONG);
                _handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        _etaStartDatePicker.show();
                    }
                }, 100);
                return;
            }

            if (isValidDay()) {
                _etaStartDateButton.setText(DateUtils.formatDateReallyLongV2(_etaStart));
            }
        }
    };

    private final View.OnClickListener _duration_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DurationDialog.Controller.show(App.get(), UID_DURATION_DIALOG);
        }
    };

    private final DurationDialog.ControllerListener _durationDialog_listener = new DurationDialog.ControllerListener() {
        @Override
        public void onOk(long milliseconds) {
            if (milliseconds < MIN_JOB_DURATION) {
                ToastClient.toast(App.get(), R.string.toast_minimum_job_duration, Toast.LENGTH_LONG);
                return;
            }
            long tempJobDuration = _durationMilliseconds;

            _durationMilliseconds = milliseconds;

            if (isValidDay() && isValidTime()) {
                _durationButton.setText(misc.convertMsToHuman(milliseconds));
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
            DurationDialog.Controller.show(App.get(), UID_EXIPRY_DIALOG);
        }
    };

    private final DurationDialog.ControllerListener _expiryDialog_listener = new DurationDialog.ControllerListener() {
        @Override
        public void onOk(long milliseconds) {
            if (milliseconds < MIN_EXPIRING_DURATION) {
                setExpiringDuration(INVALID_NUMBER);
                ToastClient.toast(App.get(), R.string.toast_minimum_expiring_duration, Toast.LENGTH_LONG);
                return;
            }
            _expiringDurationMilliseconds = milliseconds;
            _expirationButton.setText(misc.convertMsToHuman(milliseconds));
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
            if (!isValidDay() || !isValidTime())
                return true;

            if (_durationMilliseconds == INVALID_NUMBER) {
                ToastClient.toast(App.get(), R.string.toast_job_duration_empty, Toast.LENGTH_LONG);
                return true;
            }

            if (_dialogType.equals(PARAM_DIALOG_TYPE_REQUEST)) {
                if (_etaSwitch.isChecked()) {
                    onRequest(_workOrderId, _expiringDurationMilliseconds,
                            ISO8601.fromCalendar(_etaStart), _durationMilliseconds,
                            _noteEditText.getText().toString().trim());
                } else {
                    onRequest(_workOrderId, _expiringDurationMilliseconds);
                }

            } else if (_dialogType.equals(PARAM_DIALOG_TYPE_EDIT)) {
                onConfirmEta(_workOrderId, ISO8601.fromCalendar(_etaStart),
                        _durationMilliseconds, _noteEditText.getText().toString().trim(), true);
            } else {
                onConfirmEta(_workOrderId, ISO8601.fromCalendar(_etaStart),
                        _durationMilliseconds, _noteEditText.getText().toString().trim(), false);
            }
            return true;
        }
    };

    private void onRequest(long workOrderId, long expirationMilliseconds) {
        try {
            long seconds = -1;
            if (expirationMilliseconds > 0) {
                seconds = expirationMilliseconds / 1000;
            }
            WorkorderClient.actionRequest(App.get(), workOrderId, seconds);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        dismiss(true);
    }

    private void onRequest(long workOrderId, long expirationMilliseconds, String startDate, long durationMilliseconds, String note) {
        try {
            long seconds = -1;
            if (expirationMilliseconds > 0) {
                seconds = expirationMilliseconds / 1000;
            }

            // request the workorder
            WorkorderClient.actionRequest(App.get(), workOrderId, seconds, startDate, ISO8601.getEndDate(startDate, durationMilliseconds), note);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        dismiss(true);
    }

    private void onConfirmEta(long workOrderId, String startDate, long durationMilliseconds, String note, boolean isEditEta) {
        //set loading mode
        try {
            WorkorderClient.actionConfirmAssignment(App.get(),
                    workOrderId, startDate, ISO8601.getEndDate(startDate, durationMilliseconds), note, isEditEta);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        dismiss(true);
    }

    public abstract static class Controller extends com.fieldnation.fndialog.Controller {

        public Controller(Context context, String uid) {
            super(context, EtaDialog.class, uid);
        }

        public static void show(Context context, String uid, long workOrderId, Schedule schedule, String dialogType) {
            Bundle params = new Bundle();
            params.putParcelable(PARAM_SCHEDULE, schedule);
            params.putLong(PARAM_WORK_ORDER_ID, workOrderId);
            params.putString(PARAM_DIALOG_TYPE, dialogType);
            show(context, uid, EtaDialog.class, params);
        }
    }
}