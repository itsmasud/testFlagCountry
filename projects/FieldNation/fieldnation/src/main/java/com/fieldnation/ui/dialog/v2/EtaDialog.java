package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.dialog.DatePickerDialog;
import com.fieldnation.ui.dialog.DurationDialog;
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
    private Button _expirationButton;
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

        _etaStartDateTimeCalendar = Calendar.getInstance();

        _requestLayout = (RelativeLayout) v.findViewById(R.id.request_layout);
        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _expirationButton = (Button) v.findViewById(R.id.expiration_button);


        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _okButton = (Button) v.findViewById(R.id.ok_button);

        _etaStartDateButton = (Button) v.findViewById(R.id.etaStartDate_button);

        _durationButton = (Button) v.findViewById(R.id.duration_button);
        _durationButton.setOnClickListener(_duration_onClick);

        _scheduleTextView = (TextView) v.findViewById(R.id.schedule_textview);

        _etaStartDatePicker = new DatePickerDialog(context, _etaStartDate_onSet, _etaStartDateTimeCalendar.get(Calendar.YEAR), _etaStartDateTimeCalendar.get(Calendar.MONTH), _etaStartDateTimeCalendar.get(Calendar.DAY_OF_MONTH));
        _etaStartTimePicker = new TimePickerDialog(context, _etaStartTime_onSet, _etaStartDateTimeCalendar.get(Calendar.HOUR_OF_DAY), _etaStartDateTimeCalendar.get(Calendar.MINUTE), false);

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

        return v;
    }

    @Override
    public void onAdded() {
        super.onAdded();
        _expirationButton.setOnClickListener(_expiringButton_onClick);

        _cancelButton.setOnClickListener(_cancel_onClick);
        _okButton.setOnClickListener(_ok_onClick);
        _etaStartDateButton.setOnClickListener(_etaStartDate_onClick);

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
}
