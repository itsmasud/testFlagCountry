package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.ui.dialog.DatePickerDialog;
import com.fieldnation.ui.dialog.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by Shoaib on 10/28/2016.
 */

public class CheckInOutDialog extends FullScreenDialog {
    private static final String TAG = "CheckInOutDialog";

    // Dialog Uids
    private static final String DIALOG_CHECK_IN_CHECK_OUT = "DIALOG_CHECK_IN_CHECK_OUT";

    // State
    private static final String STATE_DURATION = "STATE_DURATION";
    private static final String STATE_EXPIRATION_DURATION = "STATE_EXPIRATION_DURATION";

    // Params
    public static final String PARAM_DIALOG_TYPE = "type";
    public static final String PARAM_DIALOG_TYPE_CHECK_IN = "checkin";
    public static final String PARAM_DIALOG_TYPE_CHECK_OUT = "checkout";
    public static final String PARAM_WORK_ORDER_ID = "workOrderId";
    public static final String PARAM_LOCATION = "location";
    public static final String PARAM_MAX_DEVICE_NUMBER = "maxnumber";

    private final static int INVALID_NUMBER = -1;

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;

    private Button _startDateButton;
    private Button _startTimeButton;
    private TextView _startTimeTextView;
    private View _deviceNumberLayout;
    private HintSpinner _spinner;


    // Dialogs
    private DatePickerDialog _startDatePicker;
    private TimePickerDialog _startTimePicker;

    // Passed data
    private String _dialogType;
    private Location _location;
    private long _workorderId;
    private int _maxDevice;

    // User data
    private Calendar _startCalendar;
    private long _durationMilliseconds = INVALID_NUMBER;
    private long _expiringDurationMilliseconds = INVALID_NUMBER;
    private int _itemSelectedPosition;


    /*-*************************************-*/
    /*-             Life cycle              -*/
    /*-*************************************-*/
    public CheckInOutDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        _startCalendar = Calendar.getInstance();

        View v = inflater.inflate(R.layout.dialog_v2_check_in_out, container, false);

        _toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        _deviceNumberLayout = v.findViewById(R.id.deviceNumber_layout);
        _startTimeTextView = (TextView) v.findViewById(R.id.startTime_textview);
        _startDateButton = (Button) v.findViewById(R.id.startDate_button);
        _startTimeButton = (Button) v.findViewById(R.id.startTime_button);
        _spinner = (HintSpinner) v.findViewById(R.id.spinner);

        return v;
    }

    @Override
    public void onAdded() {
        super.onAdded();
        // Dialog setup, start them off with today
        _startTimePicker = new TimePickerDialog(_startTimeButton.getContext(), _startTime_onSet,
                _startCalendar.get(Calendar.HOUR_OF_DAY),
                _startCalendar.get(Calendar.MINUTE), false);

        _startDatePicker = new DatePickerDialog(_startDateButton.getContext(), _startDate_onSet,
                _startCalendar.get(Calendar.YEAR),
                _startCalendar.get(Calendar.MONTH),
                _startCalendar.get(Calendar.DAY_OF_MONTH));

        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        _toolbar.inflateMenu(R.menu.dialog);
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _finishMenu = (ActionMenuItemView) _toolbar.findViewById(R.id.primary_menu);

        _startDateButton.setOnClickListener(startDate_onClick);
        _startTimeButton.setOnClickListener(startTime_onClick);
        _spinner.setOnItemSelectedListener(_spinner_selected);

    }

    @Override
    public void onRemoved() {
        super.onRemoved();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        Log.v(TAG, "Show");
        _dialogType = params.getString(PARAM_DIALOG_TYPE);
        _workorderId = params.getLong(PARAM_WORK_ORDER_ID);

        if (params.containsKey(PARAM_LOCATION))
            _location = params.getParcelable(PARAM_LOCATION);

        if (params.containsKey(PARAM_MAX_DEVICE_NUMBER)) {
            _deviceNumberLayout.setVisibility(View.VISIBLE);
            _maxDevice = params.getInt(PARAM_MAX_DEVICE_NUMBER);
            getSpinner();
        } else {
            _deviceNumberLayout.setVisibility(View.GONE);
        }

        super.show(params, animate);

        populateUi();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_expiringDurationMilliseconds != INVALID_NUMBER)
            outState.putLong(STATE_EXPIRATION_DURATION, _expiringDurationMilliseconds);

        if (_durationMilliseconds != INVALID_NUMBER)
            outState.putLong(STATE_DURATION, _durationMilliseconds);

        super.onSaveDialogState(outState);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState.containsKey(STATE_EXPIRATION_DURATION))
            _expiringDurationMilliseconds = savedState.getLong(STATE_EXPIRATION_DURATION);

        if (savedState.containsKey(STATE_DURATION))
            _durationMilliseconds = savedState.getLong(STATE_DURATION);

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

        if (_dialogType.equals(PARAM_DIALOG_TYPE_CHECK_IN)) {
            _toolbar.setTitle(getView().getResources().getString(R.string.title_check_in));
            _startTimeTextView.setText(getView().getResources().getString(R.string.start_time));
        } else if (_dialogType.equals(PARAM_DIALOG_TYPE_CHECK_OUT)) {
            _toolbar.setTitle(getView().getResources().getString(R.string.title_check_out));
            _startTimeTextView.setText(getView().getResources().getString(R.string.end_time));
        }
        _finishMenu.setTitle(App.get().getString(R.string.btn_submit));

        _startDateButton.setText(DateUtils.formatDateReallyLongV2(_startCalendar));
        _startTimeButton.setText(DateUtils.formatTimeLong(_startCalendar));

    }

    public HintSpinner getSpinner() {
        if (_spinner != null && _spinner.getAdapter() == null) {

            String[] deviceArray = null;
            if (_maxDevice >= 0) {
                deviceArray = new String[_maxDevice + 1];
                for (int i = 0; i <= _maxDevice; i++) {
                    deviceArray[i] = String.valueOf(i);
                }
            }

            HintArrayAdapter adapter = HintArrayAdapter.createFromArray(
                    _spinner.getContext(),
                    deviceArray,
                    R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _spinner.setAdapter(adapter);
        }
        return _spinner;
    }


    /*-*************************************-*/
    /*-             Ui Events               -*/
    /*-*************************************-*/
    private final View.OnClickListener startDate_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _startDatePicker.show();
        }
    };

    private final DatePickerDialog.OnDateSetListener _startDate_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Calendar test = (Calendar) _startCalendar.clone();
            test.set(year, monthOfYear, dayOfMonth);

            if (test.getTimeInMillis() > System.currentTimeMillis()) {
                ToastClient.toast(App.get(), getView().getResources().getString(R.string.toast_future_datetime_not_allowed), Toast.LENGTH_SHORT);
                _startDatePicker.show();

            } else {
                _startCalendar = test;
                populateUi();
            }


        }
    };

    private final View.OnClickListener startTime_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _startTimePicker.show();
        }
    };

    private final TimePickerDialog.OnTimeSetListener _startTime_onSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar test = (Calendar) _startCalendar.clone();
            test.set(test.get(Calendar.YEAR), test.get(Calendar.MONTH),
                    test.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

            if (test.getTimeInMillis() > System.currentTimeMillis()) {
                ToastClient.toast(App.get(), getView().getResources().getString(R.string.toast_future_datetime_not_allowed), Toast.LENGTH_SHORT);
                _startTimePicker.show();
            } else {
                _startCalendar = test;
                populateUi();
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

            if (_maxDevice != INVALID_NUMBER && _itemSelectedPosition == INVALID_NUMBER) {
                _itemSelectedPosition = 0;
            }

            if (_dialogType.equals(PARAM_DIALOG_TYPE_CHECK_IN)) {
                if (_location != null)
                    onCheckin(_workorderId, ISO8601.fromCalendar(_startCalendar), _location);
                else onCheckin(_workorderId, ISO8601.fromCalendar(_startCalendar));

            } else if (_dialogType.equals(PARAM_DIALOG_TYPE_CHECK_OUT)) {
                if (_location != null) {
                    if (_itemSelectedPosition >= INVALID_NUMBER)
                        onCheckout(_workorderId, ISO8601.fromCalendar(_startCalendar), _itemSelectedPosition, _location);
                    else onCheckout(_workorderId, ISO8601.fromCalendar(_startCalendar), _location);

                } else if (_itemSelectedPosition >= INVALID_NUMBER) {
                    if (_location != null)
                        onCheckout(_workorderId, ISO8601.fromCalendar(_startCalendar), _itemSelectedPosition, _location);
                    else
                        onCheckout(_workorderId, ISO8601.fromCalendar(_startCalendar), _itemSelectedPosition);

                } else
                    onCheckout(_workorderId, ISO8601.fromCalendar(_startCalendar));
            }
//            dismiss(true);
            return true;
        }
    };


    private static void onCheckin(long workOrderId, String startDate, Location location) {
        try {
            WorkorderClient.actionCheckin(App.get(), workOrderId, startDate, location);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    private static void onCheckin(long workOrderId, String startDate) {
        try {
            WorkorderClient.actionCheckin(App.get(), workOrderId, startDate);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    private static void onCheckout(long workOrderId, String startDate, int maxDevice, Location location) {
        try {
            WorkorderClient.actionCheckout(App.get(), workOrderId, startDate, maxDevice, location);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    private static void onCheckout(long workOrderId, String startDate, int maxDevice) {
        try {
            WorkorderClient.actionCheckout(App.get(), workOrderId, startDate, maxDevice);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    private static void onCheckout(long workOrderId, String startDate, Location location) {
        try {
            WorkorderClient.actionCheckout(App.get(), workOrderId, startDate, location);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    private static void onCheckout(long workOrderId, String startDate) {
        try {
            WorkorderClient.actionCheckout(App.get(), workOrderId, startDate);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }


    private final AdapterView.OnItemSelectedListener _spinner_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _itemSelectedPosition = position;
            Log.v(TAG, "onItemSelected");
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Log.v(TAG, "onNothingSelected");
        }
    };


    public abstract static class Controller extends com.fieldnation.fndialog.Controller {

        public Controller(Context context, String uid) {
            super(context, CheckInOutDialog.class, uid);
        }

        // with location
        public static void show(Context context, String uid, long workorderId, Location location, String dialogType) {
            Bundle params = new Bundle();
            params.putLong(PARAM_WORK_ORDER_ID, workorderId);
            params.putParcelable(PARAM_LOCATION, location);
            params.putString(PARAM_DIALOG_TYPE, dialogType);
            show(context, uid, CheckInOutDialog.class, params);
        }

        // with location + max device
        public static void show(Context context, String uid, long workorderId, Location location, int maxDevice, String dialogType) {
            Bundle params = new Bundle();
            params.putLong(PARAM_WORK_ORDER_ID, workorderId);
            params.putParcelable(PARAM_LOCATION, location);
            params.putInt(PARAM_MAX_DEVICE_NUMBER, maxDevice);
            params.putString(PARAM_DIALOG_TYPE, dialogType);
            show(context, uid, CheckInOutDialog.class, params);
        }

        // with max device but no location
        public static void show(Context context, String uid, long workorderId, int maxDevice, String dialogType) {
            Bundle params = new Bundle();
            params.putLong(PARAM_WORK_ORDER_ID, workorderId);
            params.putString(PARAM_DIALOG_TYPE, dialogType);
            params.putInt(PARAM_MAX_DEVICE_NUMBER, maxDevice);
            show(context, uid, CheckInOutDialog.class, params);
        }

        public static void show(Context context, String uid, long workorderId, String dialogType) {
            Bundle params = new Bundle();
            params.putLong(PARAM_WORK_ORDER_ID, workorderId);
            params.putString(PARAM_DIALOG_TYPE, dialogType);
            show(context, uid, CheckInOutDialog.class, params);
        }

        public static void dismiss(Context context) {
            dismiss(context, DIALOG_CHECK_IN_CHECK_OUT);
        }
    }
}