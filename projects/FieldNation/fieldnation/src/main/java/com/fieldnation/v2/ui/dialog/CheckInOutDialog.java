package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.view.menu.ActionMenuItemView;
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
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.contexts.SpStackContext;
import com.fieldnation.analytics.contexts.SpStatusContext;
import com.fieldnation.analytics.contexts.SpTracingContext;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fnactivityresult.ActivityResultConstants;
import com.fieldnation.fnanalytics.EventContext;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fngps.SimpleGps;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.GpsTrackingService;
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.ui.ApatheticOnMenuItemClickListener;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.dialog.DatePickerDialog;
import com.fieldnation.ui.dialog.TimePickerDialog;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.CheckInOut;
import com.fieldnation.v2.data.model.Coords;
import com.fieldnation.v2.data.model.Date;
import com.fieldnation.v2.data.model.TimeLog;
import com.fieldnation.v2.data.model.TimeLogs;

import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Shoaib on 10/28/2016.
 */

public class CheckInOutDialog extends FullScreenDialog {
    private static final String TAG = "CheckInOutDialog";

    // Dialog Uids
    private static final String DIALOG_CHECK_IN_CHECK_OUT = "DIALOG_CHECK_IN_CHECK_OUT";

    // State
    private static final String STATE_SPINNER_POSITION = "CheckInOutDialog:STATE_SPINNER_POSITION";

    // Params
    public static final String PARAM_DIALOG_TYPE_CHECK_IN = "checkin";
    public static final String PARAM_DIALOG_TYPE_CHECK_OUT = "checkout";

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
    private int _workOrderId;
    private int _maxDevice;
    private TimeLogs _timeLogs;
    private String _uiUUID = null;

    // User data
    private Calendar _startCalendar;
    private long _durationMilliseconds = INVALID_NUMBER;
    private long _expiringDurationMilliseconds = INVALID_NUMBER;
    private int _itemSelectedPosition = INVALID_NUMBER;

    // Services
    private SimpleGps _simpleGps;

    /*-*************************************-*/
    /*-             Life cycle              -*/
    /*-*************************************-*/
    public CheckInOutDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        _startCalendar = Calendar.getInstance();
        _startCalendar.set(Calendar.SECOND, 0);

        View v = inflater.inflate(R.layout.dialog_v2_check_in_out, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.inflateMenu(R.menu.dialog);

        _finishMenu = _toolbar.findViewById(R.id.primary_menu);

        _refreshView = v.findViewById(R.id.refresh_view);

        _deviceNumberLayout = v.findViewById(R.id.deviceNumber_layout);
        _startTimeTextView = v.findViewById(R.id.startTime_textview);
        _startDateButton = v.findViewById(R.id.startDate_button);
        _startTimeButton = v.findViewById(R.id.startTime_button);
        _spinner = v.findViewById(R.id.spinner);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _simpleGps = new SimpleGps(App.get())
                .updateListener(_gps_listener)
                .priority(SimpleGps.Priority.HIGHEST)
                .start(App.get());
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

        _workOrderApi.sub();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        Log.v(TAG, "Show");

        _dialogType = params.getString("dialogType");
        _workOrderId = params.getInt("workOrderId");
        _timeLogs = params.getParcelable("timeLogs");
        _uiUUID = params.getString("uiUUID");

        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpTracingContext(new UUIDGroup(null, _uiUUID)))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.START, "Check In Out Dialog"))
                .build());

        if (params.containsKey("deviceCount") &&
                _dialogType.equals(CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT)) {
            _deviceNumberLayout.setVisibility(View.VISIBLE);
            _maxDevice = params.getInt("deviceCount");
            getSpinner();
        } else {
            _deviceNumberLayout.setVisibility(View.GONE);
        }

        super.show(params, animate);

        populateUi();
    }

    private final SimpleGps.Listener _gps_listener = new SimpleGps.Listener() {
        @Override
        public void onLocation(SimpleGps simpleGps, Location location) {
            _location = location;
            _simpleGps.stop();
        }

        @Override
        public void onFail(SimpleGps simpleGps) {
            ToastClient.toast(App.get(), R.string.could_not_get_gps_location, Toast.LENGTH_LONG);
        }

        @Override
        public void onPermissionDenied(SimpleGps simpleGps) {
        }
    };

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState.containsKey("expiringDurationMilliseconds"))
            _expiringDurationMilliseconds = savedState.getLong("expiringDurationMilliseconds");

        if (savedState.containsKey("durationMilliseconds"))
            _durationMilliseconds = savedState.getLong("durationMilliseconds");

        if (savedState.containsKey("startCalendar"))
            _startCalendar = (Calendar) savedState.getSerializable("startCalendar");

        if (savedState.containsKey(STATE_SPINNER_POSITION))
            _itemSelectedPosition = savedState.getInt(STATE_SPINNER_POSITION);

        super.onRestoreDialogState(savedState);

        // UI
        populateUi();
    }

    @Override
    public void onPause() {
        _workOrderApi.unsub();
        super.onPause();
    }

    @Override
    public void onStop() {
        Tracker.event(App.get(), new CustomEvent.Builder()
                .addContext(new SpTracingContext(new UUIDGroup(null, _uiUUID)))
                .addContext(new SpStackContext(DebugUtils.getStackTraceElement()))
                .addContext(new SpStatusContext(SpStatusContext.Status.COMPLETE, "Check In Out Dialog"))
                .build());

        if (_simpleGps != null && _simpleGps.isRunning())
            _simpleGps.stop();
        super.onStop();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_expiringDurationMilliseconds != INVALID_NUMBER)
            outState.putLong("expiringDurationMilliseconds", _expiringDurationMilliseconds);

        if (_durationMilliseconds != INVALID_NUMBER)
            outState.putLong("durationMilliseconds", _durationMilliseconds);

        if (_startCalendar != null)
            outState.putSerializable("startCalendar", _startCalendar);

        if (_itemSelectedPosition != INVALID_NUMBER)
            outState.putInt(STATE_SPINNER_POSITION, _itemSelectedPosition);

        super.onSaveDialogState(outState);
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

    /*-********************************************-*/
    /*-             Internal Mutators              -*/
    /*-********************************************-*/
    private void populateUi() {
        getView().setEnabled(false);
        if (_timeLogs == null)
            return;

        if (misc.isEmptyOrNull(_dialogType))
            return;

        if (_spinner == null)
            return;
        getView().setEnabled(true);

        if (!App.get().isLocationEnabled()) {
            ToastClient.snackbar(App.get(), getView().getResources().getString(R.string.snackbar_location_disabled), "LOCATION SETTINGS", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    ActivityClient.startActivityForResult(intent, ActivityResultConstants.RESULT_CODE_ENABLE_GPS);
                }
            }, Snackbar.LENGTH_INDEFINITE);
        }

        if (_dialogType.equals(PARAM_DIALOG_TYPE_CHECK_IN)) {
            _toolbar.setTitle(getView().getResources().getString(R.string.title_check_in));
            _startTimeTextView.setText(getView().getResources().getString(R.string.start_time));
        } else if (_dialogType.equals(PARAM_DIALOG_TYPE_CHECK_OUT)) {
            _toolbar.setTitle(getView().getResources().getString(R.string.title_check_out));
            _startTimeTextView.setText(getView().getResources().getString(R.string.end_time));
        }
        _finishMenu.setText(App.get().getString(R.string.btn_submit));

        _startDateButton.setText(DateUtils.formatDateReallyLongV2(_startCalendar));
        _startTimeButton.setText(DateUtils.formatTimeLong(_startCalendar));
        if (_spinner != null && _itemSelectedPosition != INVALID_NUMBER)
            _spinner.setSelection(_itemSelectedPosition);
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
    private final View.OnClickListener startDate_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            _startDatePicker.show();
        }
    };

    private final DatePickerDialog.OnDateSetListener _startDate_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar test = (Calendar) _startCalendar.clone();
            test.set(year, monthOfYear, dayOfMonth);

            if (test.getTimeInMillis() > System.currentTimeMillis()) {
                ToastClient.toast(App.get(), R.string.toast_future_datetime_not_allowed, Toast.LENGTH_SHORT);
                _startDatePicker.show();

            } else {
                _startCalendar = test;
                populateUi();
            }
        }
    };

    private final View.OnClickListener startTime_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            _startTimePicker.show();
        }
    };

    private final TimePickerDialog.OnTimeSetListener _startTime_onSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar test = (Calendar) _startCalendar.clone();
            test.set(test.get(Calendar.YEAR), test.get(Calendar.MONTH),
                    test.get(Calendar.DAY_OF_MONTH), hourOfDay, minute, 0);

            if (test.getTimeInMillis() > System.currentTimeMillis()) {
                ToastClient.toast(App.get(), R.string.toast_future_datetime_not_allowed, Toast.LENGTH_SHORT);
                _startTimePicker.show();
            } else {
                _startCalendar = test;
                populateUi();
            }
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
            try {
                setLoading(true);
                if (_maxDevice != INVALID_NUMBER && _itemSelectedPosition == INVALID_NUMBER) {
                    _itemSelectedPosition = 0;
                }

                if (_dialogType.equals(PARAM_DIALOG_TYPE_CHECK_IN)) {
                    CheckInOut cio = new CheckInOut();
                    cio.created(new Date(_startCalendar));
                    if (_location != null) {
                        cio.coords(new Coords(_location));
                    }

                    SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
                    uiContext.page += "- Check In Dialog";

                    AppMessagingClient.setLoading(true);

                    WorkordersWebApi.addTimeLog(App.get(), new UUIDGroup(_uiUUID, UUID.randomUUID().toString()), _workOrderId, new TimeLog().in(cio), uiContext);

                    GpsTrackingService.stop(App.get());

                    WorkOrderTracker.onActionButtonEvent(
                            App.get(),
                            App.get().analActionTitle,
                            WorkOrderTracker.ActionButton.CHECK_IN,
                            WorkOrderTracker.Action.CHECK_IN,
                            _workOrderId,
                            new EventContext[]{
                                    new SpTracingContext(new UUIDGroup(null, _uiUUID)),
                                    new SpStackContext(DebugUtils.getStackTraceElement()),
                                    new SpStatusContext(SpStatusContext.Status.INFO, "Check In Out Dialog"),
                            }
                    );

                } else if (_dialogType.equals(PARAM_DIALOG_TYPE_CHECK_OUT)) {
                    boolean callMade = false;
                    CheckInOut cio = new CheckInOut();
                    cio.created(new Date(_startCalendar));
                    if (_location != null) {
                        cio.coords(new Coords(_location));
                    }

                    for (TimeLog timeLog : _timeLogs.getResults()) {
                        if (timeLog.getStatus() == TimeLog.StatusEnum.CHECKED_IN) {
                            if (_itemSelectedPosition > INVALID_NUMBER) {
                                timeLog.devices((double) _itemSelectedPosition);
                                if (timeLog.getIn().getCreated().getCalendar().after(_startCalendar)) {
                                    ToastClient.toast(App.get(), "Check Out Failed. Check your mobile date and time.", Toast.LENGTH_SHORT);
                                    setLoading(false);
                                    return false;
                                }

                            }
                            timeLog.out(cio);
                            SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
                            uiContext.page += " - Check Out Dialog";
                            Log.e(TAG, "check out: " + timeLog.getJson());

                            AppMessagingClient.setLoading(true);

                            WorkordersWebApi.updateTimeLog(App.get(), new UUIDGroup(_uiUUID, UUID.randomUUID().toString()),
                                    _workOrderId, timeLog.getId(), timeLog, uiContext);
                            callMade = true;
                            break;
                        }
                    }

                    if (callMade) {
                        Log.v(TAG, "break!");
                    }

                    WorkOrderTracker.onActionButtonEvent(
                            App.get(),
                            App.get().analActionTitle,
                            WorkOrderTracker.ActionButton.CHECK_OUT,
                            WorkOrderTracker.Action.CHECK_OUT,
                            _workOrderId,
                            new EventContext[]{
                                    new SpTracingContext(new UUIDGroup(null, _uiUUID)),
                                    new SpStackContext(DebugUtils.getStackTraceElement()),
                                    new SpStatusContext(SpStatusContext.Status.INFO, "Check In Out Dialog"),
                            }
                    );
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
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

    private final WorkordersWebApi _workOrderApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.equals("addTimeLog")
                    || methodName.equals("updateTimeLog");
        }

        @Override
        public void onMessage(String address, Object message) {
            PigeonRoost.clearAddressCacheAll(address);
            super.onMessage(address, message);
        }

        @Override
        public boolean onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (methodName.equals("addTimeLog") || methodName.equals("updateTimeLog")) {
                setLoading(false);
                if (success) {
                    dismiss(true);
                }
            }

            if (!success) {
                if (methodName.equals("addTimeLog")) {
                    ToastClient.toast(App.get(), "Check In Failed, Please Try Again Later", Toast.LENGTH_SHORT);
                } else if (methodName.equals("updateTimeLog")) {
                    ToastClient.toast(App.get(), "Check Out Failed, Please Try Again Later", Toast.LENGTH_SHORT);
                }
            }
            return super.onComplete(transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    // with max device
    public static void show(Context context, String uid, String uiUUID, int workOrderId, TimeLogs timeLogs, int maxDevice, String dialogType) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putString("dialogType", dialogType);
        params.putParcelable("timeLogs", timeLogs);
        params.putInt("deviceCount", maxDevice);
        params.putString("uiUUID", uiUUID);

        Controller.show(context, uid, CheckInOutDialog.class, params);
    }

    public static void show(Context context, String uid, String uiUUID, int workOrderId, TimeLogs timeLogs, String dialogType) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putParcelable("timeLogs", timeLogs);
        params.putString("dialogType", dialogType);
        params.putString("uiUUID", uiUUID);

        Controller.show(context, uid, CheckInOutDialog.class, params);
    }

    public static void dismiss(Context context) {
        Controller.dismiss(context, DIALOG_CHECK_IN_CHECK_OUT);
    }

    /*-**************************-*/
    /*-         Cancel           -*/
    /*-**************************-*/
    public interface OnCancelListener {
        void onCancel();
    }

    private static KeyedDispatcher<CheckInOutDialog.OnCancelListener> _onCancelDispatcher = new KeyedDispatcher<CheckInOutDialog.OnCancelListener>() {
        @Override
        public void onDispatch(CheckInOutDialog.OnCancelListener listener, Object... parameters) {
            listener.onCancel();
        }
    };

    public static void addOnCancelListener(String uid, CheckInOutDialog.OnCancelListener onCancelListener) {
        _onCancelDispatcher.add(uid, onCancelListener);
    }

    public static void removeOnCancelListener(String uid, CheckInOutDialog.OnCancelListener onCancelListener) {
        _onCancelDispatcher.remove(uid, onCancelListener);
    }

    public static void removeAllOnCancelListener(String uid) {
        _onCancelDispatcher.removeAll(uid);
    }

}