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
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.service.data.workorder.WorkorderConstants;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.ui.RefreshView;
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
    public static final String PARAM_CALENDAR = "PARAM_CALENDAR";

    private final static int INVALID_NUMBER = -1;

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;

    private RefreshView _refreshView;
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

    // Services
    private WorkorderClient _workorderClient;

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
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.inflateMenu(R.menu.dialog);

        _finishMenu = (ActionMenuItemView) _toolbar.findViewById(R.id.primary_menu);

        _refreshView = (RefreshView) v.findViewById(R.id.refresh_view);

        _deviceNumberLayout = v.findViewById(R.id.deviceNumber_layout);
        _startTimeTextView = (TextView) v.findViewById(R.id.startTime_textview);
        _startDateButton = (Button) v.findViewById(R.id.startDate_button);
        _startTimeButton = (Button) v.findViewById(R.id.startTime_button);
        _spinner = (HintSpinner) v.findViewById(R.id.spinner);

        return v;
    }

    private void setLoading(boolean loading) {
        if (loading) {
            _toolbar.setEnabled(false);
            _refreshView.startRefreshing();
            _startDateButton.setEnabled(false);
            _startTimeButton.setEnabled(false);
            _spinner.setEnabled(false);
        } else {
            _toolbar.setEnabled(true);
            _refreshView.refreshComplete();
            _startDateButton.setEnabled(true);
            _startTimeButton.setEnabled(true);
            _spinner.setEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Dialog setup, start them off with today
        _startTimePicker = new TimePickerDialog(_startTimeButton.getContext(), _startTime_onSet,
                _startCalendar.get(Calendar.HOUR_OF_DAY),
                _startCalendar.get(Calendar.MINUTE), false);

        _startDatePicker = new DatePickerDialog(_startDateButton.getContext(), _startDate_onSet,
                _startCalendar.get(Calendar.YEAR),
                _startCalendar.get(Calendar.MONTH),
                _startCalendar.get(Calendar.DAY_OF_MONTH));

        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _startDateButton.setOnClickListener(startDate_onClick);
        _startTimeButton.setOnClickListener(startTime_onClick);
        _spinner.setOnItemSelectedListener(_spinner_selected);

    }

    @Override
    public void onPause() {
        if (_workorderClient != null && _workorderClient.isConnected())
            _workorderClient.disconnect(App.get());

        super.onPause();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        Log.v(TAG, "Show");
        _dialogType = params.getString(PARAM_DIALOG_TYPE);
        _workorderId = params.getLong(PARAM_WORK_ORDER_ID);

        if (params.containsKey(PARAM_LOCATION))
            _location = params.getParcelable(PARAM_LOCATION);

        if (params.containsKey(PARAM_MAX_DEVICE_NUMBER) &&
                _dialogType.equals(CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT)) {
            _deviceNumberLayout.setVisibility(View.VISIBLE);
            _maxDevice = params.getInt(PARAM_MAX_DEVICE_NUMBER);
            getSpinner();
        } else {
            _deviceNumberLayout.setVisibility(View.GONE);
        }

        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(App.get());

        super.show(params, animate);

        populateUi();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_expiringDurationMilliseconds != INVALID_NUMBER)
            outState.putLong(STATE_EXPIRATION_DURATION, _expiringDurationMilliseconds);

        if (_durationMilliseconds != INVALID_NUMBER)
            outState.putLong(STATE_DURATION, _durationMilliseconds);

        if (_startCalendar != null)
            outState.putSerializable(PARAM_CALENDAR, _startCalendar);

        super.onSaveDialogState(outState);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState.containsKey(STATE_EXPIRATION_DURATION))
            _expiringDurationMilliseconds = savedState.getLong(STATE_EXPIRATION_DURATION);

        if (savedState.containsKey(STATE_DURATION))
            _durationMilliseconds = savedState.getLong(STATE_DURATION);

        if (savedState.containsKey(PARAM_CALENDAR))
            _startCalendar = (Calendar) savedState.getSerializable(PARAM_CALENDAR);

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

        if (_spinner == null)
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

            setLoading(true);
            if (_maxDevice != INVALID_NUMBER && _itemSelectedPosition == INVALID_NUMBER) {
                _itemSelectedPosition = 0;
            }

            if (_dialogType.equals(PARAM_DIALOG_TYPE_CHECK_IN)) {
                if (_location != null)
                    WorkorderClient.actionCheckin(App.get(), _workorderId, ISO8601.fromCalendar(_startCalendar), _location);
                else
                    WorkorderClient.actionCheckin(App.get(), _workorderId, ISO8601.fromCalendar(_startCalendar));

                _onCheckInDispatcher.dispatch(getUid(), _workorderId);

            } else if (_dialogType.equals(PARAM_DIALOG_TYPE_CHECK_OUT)) {
                if (_location != null) {
                    if (_itemSelectedPosition >= INVALID_NUMBER)
                        WorkorderClient.actionCheckout(App.get(), _workorderId, ISO8601.fromCalendar(_startCalendar), _itemSelectedPosition, _location);
                    else
                        WorkorderClient.actionCheckout(App.get(), _workorderId, ISO8601.fromCalendar(_startCalendar), _location);

                } else if (_itemSelectedPosition >= INVALID_NUMBER) {
                    if (_location != null)
                        WorkorderClient.actionCheckout(App.get(), _workorderId, ISO8601.fromCalendar(_startCalendar), _itemSelectedPosition, _location);
                    else
                        WorkorderClient.actionCheckout(App.get(), _workorderId, ISO8601.fromCalendar(_startCalendar), _itemSelectedPosition);
                } else
                    WorkorderClient.actionCheckout(App.get(), _workorderId, ISO8601.fromCalendar(_startCalendar));

                _onCheckOutDispatcher.dispatch(getUid(), _workorderId);
            }

            return true;
        }
    };

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

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            _workorderClient.subActions();
        }

        @Override
        public void onAction(long workorderId, String action, boolean failed) {
            if (workorderId != _workorderId)
                return;

            if (action.equals("checkin") || action.equals("checkout"))
                return;

            Log.v(TAG, "_workorderClient_listener.onAction()");
            _workorderClient.clearTopic(WorkorderConstants.TOPIC_ID_ACTION_COMPLETE);
            setLoading(false);
            if (failed) {
            } else {
                dismiss(true);
            }
        }
    };

    // with location
    public static void show(Context context, String uid, long workorderId, Location location, String dialogType) {
        Bundle params = new Bundle();
        params.putLong(PARAM_WORK_ORDER_ID, workorderId);
        params.putParcelable(PARAM_LOCATION, location);
        params.putString(PARAM_DIALOG_TYPE, dialogType);
        Controller.show(context, uid, CheckInOutDialog.class, params);
    }

    // with location + max device
    public static void show(Context context, String uid, long workorderId, Location location, int maxDevice, String dialogType) {
        Bundle params = new Bundle();
        params.putLong(PARAM_WORK_ORDER_ID, workorderId);
        params.putParcelable(PARAM_LOCATION, location);
        params.putInt(PARAM_MAX_DEVICE_NUMBER, maxDevice);
        params.putString(PARAM_DIALOG_TYPE, dialogType);
        Controller.show(context, uid, CheckInOutDialog.class, params);
    }

    // with max device but no location
    public static void show(Context context, String uid, long workorderId, int maxDevice, String dialogType) {
        Bundle params = new Bundle();
        params.putLong(PARAM_WORK_ORDER_ID, workorderId);
        params.putString(PARAM_DIALOG_TYPE, dialogType);
        params.putInt(PARAM_MAX_DEVICE_NUMBER, maxDevice);
        Controller.show(context, uid, CheckInOutDialog.class, params);
    }

    public static void show(Context context, String uid, long workorderId, String dialogType) {
        Bundle params = new Bundle();
        params.putLong(PARAM_WORK_ORDER_ID, workorderId);
        params.putString(PARAM_DIALOG_TYPE, dialogType);
        Controller.show(context, uid, CheckInOutDialog.class, params);
    }

    public static void dismiss(Context context) {
        Controller.dismiss(context, DIALOG_CHECK_IN_CHECK_OUT);
    }

    /*-****************************-*/
    /*-         Check In           -*/
    /*-****************************-*/
    public interface OnCheckInListener {
        void onCheckIn(long workOrderId);
    }

    private static KeyedDispatcher<OnCheckInListener> _onCheckInDispatcher = new KeyedDispatcher<OnCheckInListener>() {
        @Override
        public void onDispatch(OnCheckInListener listener, Object... parameters) {
            listener.onCheckIn((Long) parameters[0]);
        }
    };

    public static void addOnCheckInListener(String uid, OnCheckInListener onCheckInListener) {
        _onCheckInDispatcher.add(uid, onCheckInListener);
    }

    public static void removeOnCheckInListener(String uid, OnCheckInListener onCheckInListener) {
        _onCheckInDispatcher.remove(uid, onCheckInListener);
    }

    public static void removeAllOnCheckInListener(String uid) {
        _onCheckInDispatcher.removeAll(uid);
    }

    /*-*****************************-*/
    /*-         Check Out           -*/
    /*-*****************************-*/
    public interface OnCheckOutListener {
        void onCheckOut(long workOrderId);
    }

    private static KeyedDispatcher<OnCheckOutListener> _onCheckOutDispatcher = new KeyedDispatcher<OnCheckOutListener>() {
        @Override
        public void onDispatch(OnCheckOutListener listener, Object... parameters) {
            listener.onCheckOut((Long) parameters[0]);
        }
    };

    public static void addOnCheckOutListener(String uid, OnCheckOutListener onCheckOutListener) {
        _onCheckOutDispatcher.add(uid, onCheckOutListener);
    }

    public static void removeOnCheckOutListener(String uid, OnCheckOutListener onCheckOutListener) {
        _onCheckOutDispatcher.remove(uid, onCheckOutListener);
    }

    public static void removeAllOnCheckOutListener(String uid) {
        _onCheckOutDispatcher.removeAll(uid);
    }


}