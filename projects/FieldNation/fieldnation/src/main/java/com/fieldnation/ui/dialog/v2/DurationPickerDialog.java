package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.ui.FnNumberPicker;
import com.fieldnation.ui.KeyedDispatcher;

/**
 * Created by Shoaib on 1/1/17.
 */

public class DurationPickerDialog extends SimpleDialog {
    private static final String TAG = "DurationPickerDialog";

    // State
    private static final String STATE_HOUR = "STATE_HOUR";
    private static final String STATE_MINUTE = "STATE_MINUTE";

    private static final int DEFAULT_MINUTE_SELECTION = 1;

    // Ui
    private FnNumberPicker _hourFnNumberPicker;
    private FnNumberPicker _minuteFnNumberPicker;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    String[] _minuteDisplayedValues;
    private int _selectedHour = 0;
    private int _selectedMinute = DEFAULT_MINUTE_SELECTION;


    public DurationPickerDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_duration_picker, container, false);

        _hourFnNumberPicker = (FnNumberPicker) v.findViewById(R.id.hourNumberPicker);
        _minuteFnNumberPicker = (FnNumberPicker) v.findViewById(R.id.minuteNumberumberPicker);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _okButton = (Button) v.findViewById(R.id.ok_button);

        return v;
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume");
        _cancelButton.setOnClickListener(_cancel_onClick);
        _okButton.setOnClickListener(_ok_onClick);
        _hourFnNumberPicker.setOnValueChangedListener(_hourPicker_listener);
        _minuteFnNumberPicker.setOnValueChangedListener(_minutePicker_listener);
        populateUi();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        outState.putInt(STATE_HOUR, _hourFnNumberPicker.getValue());
        outState.putSerializable(STATE_MINUTE, _minuteFnNumberPicker.getValue());
        super.onSaveDialogState(outState);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        Log.e(TAG, "onRestoreDialogState");
        if (savedState.containsKey(STATE_HOUR))
            _selectedHour = savedState.getInt(STATE_HOUR);

        if (savedState.containsKey(STATE_MINUTE))
            _selectedMinute = savedState.getInt(STATE_MINUTE);
        super.onRestoreDialogState(savedState);
    }


    @Override
    public void cancel() {
        super.cancel();
        _onCanceledDispatcher.dispatch(getUid());
    }


    private void populateUi() {
        if (_okButton == null)
            return;

        _hourFnNumberPicker.setDisplayedValuesAs(0, 23, 1);
        _hourFnNumberPicker.setTextSize(16);
        _hourFnNumberPicker.setValue(_selectedHour);
        _minuteFnNumberPicker.setDisplayedValuesAs(0, 59, 15);
        _minuteFnNumberPicker.setValue(_selectedMinute);
        _minuteFnNumberPicker.setTextSize(16);
        _minuteDisplayedValues = _minuteFnNumberPicker.getDisplayedValues();

    }

    /*-*****************************-*/
    /*-             Events          -*/
    /*-*****************************-*/
    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                long seconds = Long.parseLong(String.valueOf(_hourFnNumberPicker.getValue())) * 3600;
                seconds += Long.parseLong(_minuteDisplayedValues[_minuteFnNumberPicker.getValue()]) * 60;
                _onOkDispatcher.dispatch(getUid(), seconds * 1000);

                dismiss(true);
            } catch (Exception ex) {
                ToastClient.toast(App.get(), "Invalid number, please try again.", Toast.LENGTH_LONG);
            }

        }
    };

    private final NumberPicker.OnValueChangeListener _hourPicker_listener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            if (newVal == 0) {
                _minuteFnNumberPicker.setValue(DEFAULT_MINUTE_SELECTION);
            }
        }
    };

    private final NumberPicker.OnValueChangeListener _minutePicker_listener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            if (_hourFnNumberPicker.getValue() == 0 && newVal == 0) {
                _minuteFnNumberPicker.setValue(DEFAULT_MINUTE_SELECTION);
            }
        }
    };

    public static void show(Context context, String uid) {
        Log.e(TAG, "show");
        Controller.show(context, uid, DurationPickerDialog.class, null);
    }

    /*-*******************************-*/
    /*-         Ok Listener           -*/
    /*-*******************************-*/
    public interface OnOkListener {
        void onOk(long milliseconds);
    }

    private static KeyedDispatcher<DurationPickerDialog.OnOkListener> _onOkDispatcher = new KeyedDispatcher<DurationPickerDialog.OnOkListener>() {
        @Override
        public void onDispatch(DurationPickerDialog.OnOkListener listener, Object... parameters) {
            listener.onOk((Long) parameters[0]);
        }
    };

    public static void addOnOkListener(String uid, DurationPickerDialog.OnOkListener onOkListener) {
        _onOkDispatcher.add(uid, onOkListener);
    }

    public static void removeOnOkListener(String uid, DurationPickerDialog.OnOkListener onOkListener) {
        _onOkDispatcher.remove(uid, onOkListener);
    }

    public static void removeAllOnOkListener(String uid) {
        _onOkDispatcher.removeAll(uid);
    }

    /*-*************************************-*/
    /*-         Canceled Listener           -*/
    /*-*************************************-*/
    public interface OnCanceledListener {
        void onCanceled();
    }

    private static KeyedDispatcher<DurationPickerDialog.OnCanceledListener> _onCanceledDispatcher = new KeyedDispatcher<DurationPickerDialog.OnCanceledListener>() {
        @Override
        public void onDispatch(DurationPickerDialog.OnCanceledListener listener, Object... parameters) {
            listener.onCanceled();
        }
    };

    public static void addOnCanceledListener(String uid, DurationPickerDialog.OnCanceledListener onCanceledListener) {
        _onCanceledDispatcher.add(uid, onCanceledListener);
    }

    public static void removeOnCanceledListener(String uid, DurationPickerDialog.OnCanceledListener onCanceledListener) {
        _onCanceledDispatcher.remove(uid, onCanceledListener);
    }

    public static void removeAllOnCanceledListener(String uid) {
        _onCanceledDispatcher.removeAll(uid);
    }
}
