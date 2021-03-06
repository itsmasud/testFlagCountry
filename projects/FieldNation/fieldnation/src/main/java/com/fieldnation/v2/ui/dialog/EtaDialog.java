package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.ui.ApatheticOnMenuItemClickListener;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.dialog.DatePickerDialog;
import com.fieldnation.ui.dialog.TimePickerDialog;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Assignee;
import com.fieldnation.v2.data.model.Date;
import com.fieldnation.v2.data.model.ETA;
import com.fieldnation.v2.data.model.ETAStatus;
import com.fieldnation.v2.data.model.Request;
import com.fieldnation.v2.data.model.Schedule;
import com.fieldnation.v2.data.model.ScheduleServiceWindow;
import com.fieldnation.v2.data.model.User;
import com.fieldnation.v2.ui.ListItemTwoVertView;

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

    // Params
    public static final String PARAM_DIALOG_TYPE_ACCEPT = "accept";
    public static final String PARAM_DIALOG_TYPE_MASS_ACCEPT = "massAccept";
    public static final String PARAM_DIALOG_TYPE_REQUEST = "request";
    public static final String PARAM_DIALOG_TYPE_ADD = "add";
    public static final String PARAM_DIALOG_TYPE_EDIT = "edit";

    private final static int MIN_JOB_DURATION = 900000;
    //    private final static int MIN_EXPIRING_DURATION = 900000;
    private final static int INVALID_NUMBER = -1;
    private final int ONE_DAY = 86400000;

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;

    private TextView _termsWarningTextView;
    private RelativeLayout _expirationLayout;
    private HintSpinner _expireSpinner;

    private ListItemTwoVertView _titleView;

    private TextView _etaSwitchLabel;
    private Switch _etaSwitch;
    private View _etaSwitchDivider;

    private RelativeLayout _etaLayout;
    private Button _etaStartDateButton;
    private Button _etaStartTimeButton;
    private Button _durationButton;
    private EditText _noteEditText;

    private RefreshView _refreshView;

    // Dialogs
    private DatePickerDialog _etaStartDatePicker;
    private TimePickerDialog _etaStartTimePicker;

    // Passed data
    private String _dialogType;
    private int _workOrderId;
    private Schedule _woSchedule;
    private ETA _woEta;

    // User data
    private Calendar _etaStart;
    private long _durationMilliseconds = INVALID_NUMBER;
    private long _expiringDurationSeconds = INVALID_NUMBER;
    private boolean _isSwitchOn = true;
    private int _currentPosition = INVALID_NUMBER;
    private int[] _durations;
    private boolean _isDateSet = false;
    private boolean _isTimeSet = false;
    private boolean _isDirty = false;


    /*-*************************************-*/
    /*-             Life cycle              -*/
    /*-*************************************-*/
    public EtaDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        _etaStart = Calendar.getInstance();
        _etaStart.set(Calendar.SECOND, 0);

        View v = inflater.inflate(R.layout.dialog_v2_eta, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.inflateMenu(R.menu.dialog);

        _finishMenu = _toolbar.findViewById(R.id.primary_menu);

        _termsWarningTextView = v.findViewById(R.id.termswarning_textview);

        // Expiration stuff
        _expirationLayout = v.findViewById(R.id.request_layout); // expiration layout
        _expireSpinner = v.findViewById(R.id.expire_duration_spinner);

        // schedule description
        _titleView = v.findViewById(R.id.title);

        // ETA layout
        _etaSwitchLabel = v.findViewById(R.id.switchLabel_textview);
        _etaSwitch = v.findViewById(R.id.eta_switch);
        _etaSwitchDivider = v.findViewById(R.id.etaSwitch_divider);
        _etaLayout = v.findViewById(R.id.eta_layout);
        _etaStartDateButton = v.findViewById(R.id.etaStartDate_button);
        _etaStartTimeButton = v.findViewById(R.id.etaStartTime_button);
        _durationButton = v.findViewById(R.id.duration_button);
        _noteEditText = v.findViewById(R.id.note_edittext);

        _refreshView = v.findViewById(R.id.refresh_view);

        _durations = _expirationLayout.getContext().getResources().getIntArray(R.array.request_expire_duration_values);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");

        _workOrdersApi.sub();

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

        _expireSpinner.setOnItemSelectedListener(_expireSpinner_selected);
        HintArrayAdapter adapter = HintArrayAdapter.createFromResources(_expirationLayout.getContext(), R.array.request_expire_duration_titles, R.layout.view_request_expiry_spinner_item);
        adapter.setDropDownViewResource(android.support.design.R.layout.support_simple_spinner_dropdown_item);
        _expireSpinner.setAdapter(adapter);

        _etaSwitch.setOnCheckedChangeListener(_switch_onChange);

        _etaStartDateButton.setOnClickListener(_etaStartDate_onClick);
        _etaStartTimeButton.setOnClickListener(_etaStartTime_onClick);
        _durationButton.setOnClickListener(_duration_onClick);

        DurationPickerDialog.addOnOkListener(UID_DURATION_DIALOG, _durationPickerDialog_onOk);

        _termsWarningTextView.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public void onPause() {
        _workOrdersApi.unsub();

        super.onPause();
        DurationPickerDialog.removeOnOkListener(UID_DURATION_DIALOG, _durationPickerDialog_onOk);
    }

    @Override
    public void show(Bundle params, boolean animate) {
        Log.v(TAG, "Show");
        _workOrderId = params.getInt("workOrderId");
        _dialogType = params.getString("dialogType");
        _woSchedule = params.getParcelable("schedule");
        _woEta = params.getParcelable("eta");

        super.show(params, animate);

        try {
            if (_woEta.getStatus().getName() != null
                    && _woEta.getStatus().getName() != ETAStatus.NameEnum.UNCONFIRMED
                    && _woEta.getStart().getUtc() != null) {
                _etaStart = _woEta.getStart().getCalendar();
                _isTimeSet = true;
                _isDateSet = true;
            } else if (_woSchedule.getServiceWindow().getStart().getUtc() != null) {
                _etaStart = _woSchedule.getServiceWindow().getStart().getCalendar();
                _isTimeSet = true;
                _isDateSet = true;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        populateUi();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        outState.putInt("currentPosition", _currentPosition);

//        if (_expiringDurationSeconds != INVALID_NUMBER)
//            outState.putLong("expiringDurationSeconds", _expiringDurationSeconds);

        // ETA stuff
        if (_durationMilliseconds != INVALID_NUMBER)
            outState.putLong("durationMilliseconds", _durationMilliseconds);

        if (!misc.isEmptyOrNull(_noteEditText.getText().toString().trim()))
            outState.putString("noteEditText", _noteEditText.getText().toString().trim());

        outState.putBoolean("etaSwitch", _etaSwitch.isChecked());
        outState.putSerializable("etaStart", _etaStart);
        outState.putBoolean("isDateSet", _isDateSet);
        outState.putBoolean("isTimeSet", _isTimeSet);
        outState.putBoolean("isDirty", _isDirty);

        super.onSaveDialogState(outState);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        Log.v(TAG, "onRestoreDialogState");
        if (savedState.containsKey("currentPosition"))
            _currentPosition = savedState.getInt("currentPosition");

        // ETA stuff
        if (savedState.containsKey("durationMilliseconds"))
            _durationMilliseconds = savedState.getLong("durationMilliseconds");

        if (savedState.containsKey("noteEditText"))
            _noteEditText.setText(savedState.getString("noteEditText"));

        if (savedState.containsKey("etaSwitch"))
            _isSwitchOn = savedState.getBoolean("etaSwitch");

        if (savedState.containsKey("etaStart"))
            _etaStart = (Calendar) savedState.getSerializable("etaStart");

        if (savedState.containsKey("isDateSet"))
            _isDateSet = savedState.getBoolean("isDateSet");
        if (savedState.containsKey("isTimeSet"))
            _isTimeSet = savedState.getBoolean("isTimeSet");
        if (savedState.containsKey("isDirty"))
            _isDirty = savedState.getBoolean("isDirty");

        super.onRestoreDialogState(savedState);

        // UI
        populateUi();
    }

    /*-********************************************-*/
    /*-             Internal Mutators              -*/
    /*-********************************************-*/
    private void populateUi() {
        getView().setEnabled(false);
        if (misc.isEmptyOrNull(_dialogType))
            return;

        if (_woSchedule == null || _woEta == null) {
            return;
        }
        getView().setEnabled(true);

        // Wod request work, Woc Request work
        if (_dialogType.equals(PARAM_DIALOG_TYPE_REQUEST)) {
            _toolbar.setTitle("Request " + _workOrderId);
            _finishMenu.setText(App.get().getString(R.string.btn_request));

            _expirationLayout.setVisibility(View.VISIBLE);
            _etaSwitch.setVisibility(View.VISIBLE);
            _etaSwitchDivider.setVisibility(View.VISIBLE);
            _etaSwitchLabel.setVisibility(View.VISIBLE);
            _etaSwitch.setChecked(_isSwitchOn);
            _etaLayout.setVisibility(_isSwitchOn ? View.VISIBLE : View.GONE);

            SpannableString spanned = new SpannableString("By requesting this work order, I understand and agree to the Buyer's work order terms, the Standard Work Order Terms and Conditions and the Provider Quality Assurance Policy. I also understand that I am committing myself to complete this work order at the designated date and time and that failure to do so can result in non-payment or deactivation from the platform.");
            spanned.setSpan(_standardTerms_onClick, 91, 131, spanned.getSpanFlags(_standardTerms_onClick));
            spanned.setSpan(_pqap_onClick, 140, 173, spanned.getSpanFlags(_pqap_onClick));
            _termsWarningTextView.setText(spanned);
            _termsWarningTextView.setVisibility(View.VISIBLE);

            // Woc accept route, Wod Accept route (onSetEta)
        } else if (_dialogType.equals(PARAM_DIALOG_TYPE_ACCEPT)
                || _dialogType.equals(PARAM_DIALOG_TYPE_MASS_ACCEPT)) {
            _toolbar.setTitle("Accept " + _workOrderId);
            _finishMenu.setText(App.get().getString(R.string.btn_accept));
            _expirationLayout.setVisibility(View.GONE);

            _etaSwitch.setOnCheckedChangeListener(null);
            _etaSwitch.setChecked(true);
            _etaSwitch.setOnCheckedChangeListener(_switch_onChange);
            _etaSwitchLabel.setVisibility(View.GONE);
            _etaSwitch.setVisibility(View.GONE);
            _etaSwitchDivider.setVisibility(View.GONE);
            _etaLayout.setVisibility(View.VISIBLE);

            SpannableString spanned = new SpannableString("By accepting this work order, I understand and agree to the Buyer's work order terms, the Standard Work Order Terms and Conditions and the Provider Quality Assurance Policy. I also understand that I am committing myself to complete this work order at the designated date and time and that failure to do so can result in non-payment or deactivation from the platform.");
            spanned.setSpan(_standardTerms_onClick, 90, 130, spanned.getSpanFlags(_standardTerms_onClick));
            spanned.setSpan(_pqap_onClick, 139, 172, spanned.getSpanFlags(_pqap_onClick));
            _termsWarningTextView.setText(spanned);
            _termsWarningTextView.setVisibility(View.VISIBLE);

/*
            // old confirm, work order start, confirm task
        } else if (_dialogType.equals(PARAM_DIALOG_TYPE_CONFIRM)) {
            _toolbar.setTitle("Confirm " + _workOrderId);
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
            _toolbar.setTitle("Set ETA " + _workOrderId);
            _finishMenu.setText(App.get().getString(R.string.btn_submit));
            _expirationLayout.setVisibility(View.GONE);

            _etaSwitch.setOnCheckedChangeListener(null);
            _etaSwitch.setChecked(true);
            _etaSwitch.setOnCheckedChangeListener(_switch_onChange);
            _etaSwitchLabel.setVisibility(View.GONE);
            _etaSwitch.setVisibility(View.GONE);
            _etaSwitchDivider.setVisibility(View.GONE);
            _etaLayout.setVisibility(View.VISIBLE);
            _termsWarningTextView.setVisibility(View.GONE);

            // from WoD. change the eta
        } else if (_dialogType.equals(PARAM_DIALOG_TYPE_EDIT)) {
            _toolbar.setTitle(R.string.dialog_eta_title);
            _finishMenu.setText(App.get().getString(R.string.btn_submit));

            _expirationLayout.setVisibility(View.GONE);

            _etaSwitch.setOnCheckedChangeListener(null);
            _etaSwitch.setChecked(true);
            _etaSwitch.setOnCheckedChangeListener(_switch_onChange);
            _etaSwitch.setVisibility(View.GONE);
            _etaSwitchDivider.setVisibility(View.GONE);
            _etaSwitchLabel.setVisibility(View.GONE);
            _etaLayout.setVisibility(View.VISIBLE);
            _termsWarningTextView.setVisibility(View.GONE);
        }

        final String scheduleDisplayText = getScheduleDisplayText();
        if (scheduleDisplayText == null) {
            _titleView.set("Work Order Schedule", "");
        } else
            _titleView.set("Work Order Schedule", scheduleDisplayText);

        _etaStartDateButton.setText(DateUtils.formatDateReallyLongV2(_etaStart));
        _etaStartTimeButton.setText(DateUtils.formatTimeLong(_etaStart));

        if (_woSchedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.EXACT) {
            _etaStartDateButton.setEnabled(false);
            _etaStartTimeButton.setEnabled(false);
        } else {
            _etaStartDateButton.setEnabled(true);
            _etaStartTimeButton.setEnabled(true);
        }

        try {
            if (_durationMilliseconds == INVALID_NUMBER) {
                if (_woEta.getStatus().getName() != null
                        && _woEta.getStatus().getName() != ETAStatus.NameEnum.UNCONFIRMED
                        && _woEta.getHourEstimate() != null
                        && _woEta.getHourEstimate() > 0
                        && _dialogType.equals(PARAM_DIALOG_TYPE_EDIT)) {
                    _durationMilliseconds = (long) (_woEta.getHourEstimate() * 60 * 60 * 1000);
                }
            }
        } catch (Exception e) {
        }
        if (_durationMilliseconds == INVALID_NUMBER) {
            _durationButton.setText("");
        } else {
            _durationButton.setText(misc.convertMsToHuman(_durationMilliseconds));
        }

        if (_currentPosition != INVALID_NUMBER) {
            _expiringDurationSeconds = _durations[_currentPosition];
            _expireSpinner.setSelection(_currentPosition);
        } else _expireSpinner.setSelection(0);

        _finishMenu.setEnabled(canEnableSubmitButton());
    }

    private boolean canEnableSubmitButton() {
        if (!_isDirty)
            return false;

        if (!_etaSwitch.isChecked())
            return true;

        if (_woSchedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.EXACT && _durationMilliseconds != INVALID_NUMBER)
            return true;

        if ((_woSchedule.getServiceWindow().getMode() != ScheduleServiceWindow.ModeEnum.EXACT) && _isDateSet && _isTimeSet && _durationMilliseconds != INVALID_NUMBER)
            return true;

        return false;
    }

    private boolean isValidEta(final Calendar arrival) {
        if (_woSchedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.EXACT) {
            return true;
        } else if (_woSchedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.HOURS) {
            return isWithinBusinessHours(arrival, _woSchedule);
        } else if (_woSchedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.BETWEEN) {
            return isWithinRange(arrival, _woSchedule);
        }
        return true;
    }

    private static boolean isWithinBusinessHours(final Calendar eta, final Schedule schedule) {
        // make a copy so we don't mess it up
        Calendar arrival = (Calendar) eta.clone();
        try {
            if (passesMidnight(schedule)) {
                Calendar scal = schedule.getServiceWindow().getStart().getCalendar();
                Calendar ecal = schedule.getServiceWindow().getEnd().getCalendar();

                if (DateUtils.isBeforeDay(arrival, scal) || DateUtils.isAfterDay(arrival, ecal))
                    return false;

                // move to first day
                arrival.set(Calendar.DAY_OF_MONTH, scal.get(Calendar.DAY_OF_MONTH));
                ecal.set(Calendar.DAY_OF_MONTH, scal.get(Calendar.DAY_OF_MONTH));

                // if too early, then bump a day
                return !(arrival.getTimeInMillis() < scal.getTimeInMillis()
                        && arrival.getTimeInMillis() >= ecal.getTimeInMillis());

            } else {
                Calendar scal = schedule.getServiceWindow().getStart().getCalendar();
                Calendar ecal = schedule.getServiceWindow().getEnd().getCalendar();

                if (arrival.getTimeInMillis() < scal.getTimeInMillis()
                        || arrival.getTimeInMillis() > ecal.getTimeInMillis()) {
                    return false;
                }
                // arrival is within the start and end days, constrain check to the day of

                scal.set(Calendar.DAY_OF_MONTH, arrival.get(Calendar.DAY_OF_MONTH));
                ecal.set(Calendar.DAY_OF_MONTH, arrival.get(Calendar.DAY_OF_MONTH));

                if (arrival.getTimeInMillis() >= scal.getTimeInMillis()
                        && arrival.getTimeInMillis() < ecal.getTimeInMillis())
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
        if (_woSchedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.EXACT)
            return false;

        try {
            // is business or range
            if (_woSchedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.BETWEEN) {
                //Calendar scal = _schedule.getServiceWindow().getStart().getCalendar();
                Calendar ecal = _woSchedule.getServiceWindow().getEnd().getCalendar();

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

            if (_woSchedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.HOURS) {
                Calendar sCal = _woSchedule.getServiceWindow().getStart().getCalendar();
                Calendar eCal = _woSchedule.getServiceWindow().getEnd().getCalendar();

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
                        sdf1.format(eCal.getTime()),
                        DateUtils.getDeviceTimezone(eCal));

                return dateTimeString;

            } else if (_woSchedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.BETWEEN) {
                Calendar sCal = _woSchedule.getServiceWindow().getStart().getCalendar();
                Calendar eCal = _woSchedule.getServiceWindow().getEnd().getCalendar();

                SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy @ hh:mma", Locale.getDefault());
                sdf.setDateFormatSymbols(symbols);

                dateTimeString = App.get().getString(R.string.schedule_open_range_format2,
                        sdf.format(sCal.getTime()),
                        sdf.format(eCal.getTime()),
                        DateUtils.getDeviceTimezone(eCal));

                return dateTimeString;

            } else if (_woSchedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.EXACT) {
                Calendar sCal = _woSchedule.getServiceWindow().getStart().getCalendar();

                SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy @ hh:mma", Locale.getDefault());
                sdf.setDateFormatSymbols(symbols);
                dateTimeString = sdf.format(sCal.getTime()) + DateUtils.getDeviceTimezone(sCal);

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
    private final ClickableSpan _standardTerms_onClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://app.fieldnation.com/legal/?a=workorder"));
            ActivityClient.startActivity(intent);
        }
    };

    private final ClickableSpan _pqap_onClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://app.fieldnation.com/legal/?a=qualityassurance"));
            ActivityClient.startActivity(intent);
        }
    };

    private final CompoundButton.OnCheckedChangeListener _switch_onChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            _isSwitchOn = isChecked;
            _isDirty = true;
            misc.hideKeyboard(buttonView);
            populateUi();
        }
    };

    private final View.OnClickListener _etaStartDate_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
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
                _isDateSet = true;
                _isDirty = true;
                populateUi();
            } else {
                // the time field might need to be cleared.
                try {
                    test = _woSchedule.getServiceWindow().getStart().getCalendar();
                    test.set(year, monthOfYear, dayOfMonth);
                    if (isValidEta(test)) {
                        ToastClient.toast(App.get(), R.string.toast_pick_time_within_schedule, Toast.LENGTH_SHORT);
                        _etaStart = test;
                        _isDateSet = true;
                        _isTimeSet = false;
                        _isDirty = true;
                        populateUi();
                        _etaStartTimeButton.setText("");
                    } else {
                        test = _woSchedule.getServiceWindow().getEnd().getCalendar();
                        test.set(year, monthOfYear, dayOfMonth);
                        if (isValidEta(test)) {
                            ToastClient.toast(App.get(), R.string.toast_pick_time_within_schedule, Toast.LENGTH_SHORT);
                            _etaStart = test;
                            _isDateSet = true;
                            _isTimeSet = false;
                            _isDirty = true;
                            populateUi();
                            _etaStartTimeButton.setText("");
                        } else {
                            ToastClient.toast(App.get(), R.string.toast_pick_time_within_schedule, Toast.LENGTH_SHORT);
                        }
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
        }
    };

    private final View.OnClickListener _etaStartTime_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            _etaStartTimePicker.show();
        }
    };

    private final TimePickerDialog.OnTimeSetListener _etaStartTime_onSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar test = (Calendar) _etaStart.clone();
            test.set(test.get(Calendar.YEAR), test.get(Calendar.MONTH),
                    test.get(Calendar.DAY_OF_MONTH), hourOfDay, minute, 0);

            if (isValidEta(test)) {
                _etaStart = test;
                _isTimeSet = true;
                _isDirty = true;
                populateUi();
            } else {
                ToastClient.toast(App.get(), R.string.toast_pick_time_within_schedule, Toast.LENGTH_SHORT);
                // TODO, pop the dialog again?
            }
        }
    };

    private final View.OnClickListener _duration_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
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

            if (_woSchedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.EXACT) {
                _durationMilliseconds = milliseconds;
                _isDirty = true;
                populateUi();
            } else {
                Calendar test = Calendar.getInstance();
                test.setTimeInMillis(_etaStart.getTimeInMillis() + milliseconds);

                if (isValidEta(test)) {
                    _durationMilliseconds = milliseconds;
                    _isDirty = true;
                    populateUi();
                } else {
                    ToastClient.toast(App.get(), R.string.toast_pick_duration_within_range, Toast.LENGTH_LONG);
                }
            }
        }
    };

    private final AdapterView.OnItemSelectedListener _expireSpinner_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                _currentPosition = INVALID_NUMBER;
                return;
            }
            _currentPosition = position;
            _expiringDurationSeconds = _durations[position];
            _isDirty = true;
            populateUi();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            _currentPosition = INVALID_NUMBER;
            _isDirty = true;
            populateUi();
        }
    };

    private final View.OnClickListener _toolbar_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            dismiss(true);
        }
    };

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new ApatheticOnMenuItemClickListener() {
        @Override
        public boolean onSingleMenuItemClick(MenuItem item) {
            // only do eta checks if switch is on
            if (_etaSwitch.isChecked()) {
                if (!isValidEta(_etaStart)) {
                    ToastClient.toast(App.get(), R.string.toast_invalid_eta, Toast.LENGTH_SHORT);
                    return true;
                }

                if (_durationMilliseconds == INVALID_NUMBER) {
                    ToastClient.toast(App.get(), R.string.toast_job_duration_empty, Toast.LENGTH_LONG);
                    return true;
                }
            }

            if (_dialogType.equals(PARAM_DIALOG_TYPE_MASS_ACCEPT)) {
                try {
                    ETA eta = new ETA();
                    eta.workOrderId(_workOrderId);
                    eta.setStart(new Date(_etaStart));
                    eta.end(new Date(_etaStart.getTimeInMillis() + _durationMilliseconds));
                    eta.setUser(new User().id((int) App.getProfileId()));
                    eta.setMode(_woEta.getMode());
                    eta.setHourEstimate(_durationMilliseconds / 3600000.0);
                    eta.setNotes(_noteEditText.getText().toString().trim());

                    _onBundleEtaDispatcher.dispatch(getUid(), _workOrderId, eta);
                    dismiss(true);
                    return true;
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }

            try {
                switch (_dialogType) {
                    case PARAM_DIALOG_TYPE_REQUEST: {
                        WorkOrderTracker.onActionButtonEvent(App.get(), App.get().analActionTitle,
                                WorkOrderTracker.ActionButton.REQUEST, WorkOrderTracker.Action.REQUEST,
                                _workOrderId);

                        AppMessagingClient.setLoading(true);

                        Request request = new Request();
                        request.setNotes(_noteEditText.getText().toString().trim());
                        if (_currentPosition != INVALID_NUMBER)
                            request.setExpires(new Date(System.currentTimeMillis() + _expiringDurationSeconds * 1000));

                        if (_etaSwitch.isChecked()) {
                            ETA eta = new ETA();
                            eta.setStart(new Date(_etaStart));
                            eta.end(new Date(_etaStart.getTimeInMillis() + _durationMilliseconds));
                            eta.setUser(new User().id((int) App.getProfileId()));
                            eta.setMode(_woEta.getMode());
                            eta.setHourEstimate(_durationMilliseconds / 3600000.0);
                            eta.setNotes(_noteEditText.getText().toString().trim());
                            request.setEta(eta);
                        }

                        SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
                        uiContext.page += " - Eta Dialog";
                        WorkordersWebApi.request(App.get(), _workOrderId, request, uiContext);
                        break;
                    }

                    case PARAM_DIALOG_TYPE_ACCEPT: {
                        WorkOrderTracker.onActionButtonEvent(App.get(), App.get().analActionTitle,
                                WorkOrderTracker.ActionButton.ACCEPT_WORK, WorkOrderTracker.Action.ACCEPT_WORK,
                                _workOrderId);

                        AppMessagingClient.setLoading(true);

                        Assignee assignee = new Assignee();
                        assignee.setUser(new User().id((int) App.getProfileId()));

                        SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
                        uiContext.page += " - Eta Dialog";

                        WorkordersWebApi.assignUser(App.get(), _workOrderId, assignee, uiContext);
                        break;
                    }
                    case PARAM_DIALOG_TYPE_ADD:  // add eta
                    case PARAM_DIALOG_TYPE_EDIT: {
                        WorkOrderTracker.onActionButtonEvent(App.get(), App.get().analActionTitle,
                                WorkOrderTracker.ActionButton.ETA, WorkOrderTracker.Action.ETA, _workOrderId);

                        AppMessagingClient.setLoading(true);

                        ETA eta = new ETA();
                        eta.setStart(new Date(_etaStart));
                        eta.end(new Date(_etaStart.getTimeInMillis() + _durationMilliseconds));
                        eta.setUser(new User().id((int) App.getProfileId()));
                        eta.setMode(_woEta.getMode());
                        eta.setHourEstimate(_durationMilliseconds / 3600000.0);
                        eta.setNotes(_noteEditText.getText().toString().trim());

                        SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
                        uiContext.page += " - Eta Dialog";

                        WorkordersWebApi.updateETA(App.get(), _workOrderId, eta, uiContext);
                        break;
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            _isDirty = false;
            return true;
        }
    };

    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName) {
            if (transactionParams.getMethodParamInt("workOrderId") == null
                    || transactionParams.getMethodParamInt("workOrderId") != _workOrderId)
                return false;

            return methodName.equals("assignUser")
                    || methodName.contains("updateETA")
                    || methodName.contains("request"); // TODO this might not work here
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (methodName.equals("assignUser")) {
                if (success) {
                    // TODO this might not work
                    try {
                        ETA eta = new ETA();
                        eta.setStart(new Date(_etaStart));
                        eta.end(new Date(_etaStart.getTimeInMillis() + _durationMilliseconds));
                        eta.setUser(new User().id((int) App.getProfileId()));
                        eta.setHourEstimate(_durationMilliseconds / 3600000.0);
                        eta.setNotes(_noteEditText.getText().toString().trim());

                        SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
                        uiContext.page += " - Eta Dialog";

                        WorkordersWebApi.updateETA(App.get(), _workOrderId, eta, uiContext);
                        // TODO dismiss(true);
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                }
            }

            // Add/edit accept rout "assign user"
            if (methodName.contains("updateETA") && success) {
                Log.v(TAG, "updateETA");
                switch (_dialogType) {
                    case PARAM_DIALOG_TYPE_ACCEPT:
                        ToastClient.toast(App.get(), "Work Order Accepted", Toast.LENGTH_LONG);
                        break;
                    case PARAM_DIALOG_TYPE_ADD:
                        ToastClient.toast(App.get(), "ETA created successfully.", Toast.LENGTH_LONG);
                        break;
                    case PARAM_DIALOG_TYPE_EDIT:
                        ToastClient.toast(App.get(), "ETA updated successfully.", Toast.LENGTH_LONG);
                        break;
                }
                _refreshView.refreshComplete();
                dismiss(true);
            }

            if (methodName.contains("request") && success) {
                Log.v(TAG, "request");
                ToastClient.toast(App.get(), "Work Order requested", Toast.LENGTH_LONG);
                _refreshView.refreshComplete();
                dismiss(true);
            }

            if (!success) {
                _refreshView.refreshComplete();
            }
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    public static void show(Context context, String uid, int workOrderId, Schedule schedule, ETA eta, String dialogType) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putParcelable("schedule", schedule);
        params.putParcelable("eta", eta);
        params.putString("dialogType", dialogType);
        Controller.show(context, uid, EtaDialog.class, params);
    }

    /*-****************************-*/
    /*-         Bundle Eta         -*/
    /*-****************************-*/
    public interface OnBundleEtaListener {
        void onBundleEta(int workOrderId, ETA eta);
    }

    private static KeyedDispatcher<OnBundleEtaListener> _onBundleEtaDispatcher = new KeyedDispatcher<OnBundleEtaListener>() {
        @Override
        public void onDispatch(OnBundleEtaListener listener, Object... parameters) {
            listener.onBundleEta((Integer) parameters[0], (ETA) parameters[1]);
        }
    };

    public static void addOnBundleEtaListener(String uid, OnBundleEtaListener onBundleEtaListener) {
        _onBundleEtaDispatcher.add(uid, onBundleEtaListener);
    }

    public static void removeOnBundleEtaListener(String uid, OnBundleEtaListener onBundleEtaListener) {
        _onBundleEtaDispatcher.remove(uid, onBundleEtaListener);
    }

    public static void removeAllOnBundleEtaListener(String uid) {
        _onBundleEtaDispatcher.removeAll(uid);
    }
}
