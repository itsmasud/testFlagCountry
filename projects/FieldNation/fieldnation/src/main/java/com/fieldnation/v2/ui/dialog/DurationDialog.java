package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.IconFontButton;

/**
 * Created by Michael on 10/11/2016.
 */

public class DurationDialog extends SimpleDialog {
    private static final String TAG = "DurationDialog";

    // Params
    private static final String PARAM_RESULT = "result";
    private static final int PARAM_RESULT_OK = 0;
    private static final int PARAM_RESULT_CANCEL = 1;
    private static final String PARAM_VALUE = "value";

    // State
    private static final String STATE_NUMBER = "STATE_NUMBER";

    // Ui
    private TextView _dayTextView;
    private TextView _hourTextView;
    private TextView _minTextView;

    private Button[] _numberPad;

    private Button _okButton;
    private Button _cancelButton;
    private IconFontButton _deleteButton;

    // Data
    private String _number = "";


    public DurationDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_duration, container, false);

        _dayTextView = v.findViewById(R.id.day_textview);
        _hourTextView = v.findViewById(R.id.hour_textview);
        _minTextView = v.findViewById(R.id.min_textview);

        _numberPad = new Button[10];
        _numberPad[0] = v.findViewById(R.id.button0);
        _numberPad[1] = v.findViewById(R.id.button1);
        _numberPad[2] = v.findViewById(R.id.button2);
        _numberPad[3] = v.findViewById(R.id.button3);
        _numberPad[4] = v.findViewById(R.id.button4);
        _numberPad[5] = v.findViewById(R.id.button5);
        _numberPad[6] = v.findViewById(R.id.button6);
        _numberPad[7] = v.findViewById(R.id.button7);
        _numberPad[8] = v.findViewById(R.id.button8);
        _numberPad[9] = v.findViewById(R.id.button9);

        _okButton = v.findViewById(R.id.ok_button);
        _cancelButton = v.findViewById(R.id.cancel_button);
        _deleteButton = v.findViewById(R.id.delete_imageview);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        _numberPad[0].setOnClickListener(_number_onClick);
        _numberPad[1].setOnClickListener(_number_onClick);
        _numberPad[2].setOnClickListener(_number_onClick);
        _numberPad[3].setOnClickListener(_number_onClick);
        _numberPad[4].setOnClickListener(_number_onClick);
        _numberPad[5].setOnClickListener(_number_onClick);
        _numberPad[6].setOnClickListener(_number_onClick);
        _numberPad[7].setOnClickListener(_number_onClick);
        _numberPad[8].setOnClickListener(_number_onClick);
        _numberPad[9].setOnClickListener(_number_onClick);
        _okButton.setOnClickListener(_ok_onClick);
        _cancelButton.setOnClickListener(_cancel_onClick);
        _deleteButton.setOnClickListener(_delete_onClick);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState.containsKey(STATE_NUMBER))
            _number = savedState.getString(STATE_NUMBER);
        super.onRestoreDialogState(savedState);
        populateUi();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_number != null)
            outState.putString(STATE_NUMBER, _number);
        super.onSaveDialogState(outState);
    }

    private void populateUi() {
        if (_dayTextView == null)
            return;

        while (_number.length() > 0 && _number.charAt(0) == '0')
            _number = _number.substring(1);

        String padded = _number;

        if (padded.length() < 6)
            padded = misc.repeat("0", 6 - _number.length()) + padded;

        _dayTextView.setText(padded.substring(0, 2));
        _hourTextView.setText(padded.substring(2, 4));
        _minTextView.setText(padded.substring(4, 6));
    }


    private final View.OnClickListener _number_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String tag = (String) v.getTag();
            if (_number.length() < 6) {
                _number += tag;
                populateUi();
            }
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                long seconds = Long.parseLong(_dayTextView.getText() + "") * 86400;
                seconds += Long.parseLong(_hourTextView.getText() + "") * 3600;
                seconds += Long.parseLong(_minTextView.getText() + "") * 60;

                _onOkDispatcher.dispatch(getUid(), seconds * 1000);

                dismiss(true);
            } catch (Exception ex) {
                ToastClient.toast(App.get(), "Invalid number, please try again.", Toast.LENGTH_LONG);
            }
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _onCanceledDispatcher.dispatch(getUid());
            dismiss(true);
        }
    };

    private final View.OnClickListener _delete_onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (_number.length() > 0) {
                _number = _number.substring(0, _number.length() - 1);
                populateUi();
            }
        }
    };

    public static void show(Context context, String uid) {
        Controller.show(context, uid, DurationDialog.class, null);
    }

    /*-*******************************-*/
    /*-         Ok Listener           -*/
    /*-*******************************-*/
    public interface OnOkListener {
        void onOk(long milliseconds);
    }

    private static KeyedDispatcher<OnOkListener> _onOkDispatcher = new KeyedDispatcher<OnOkListener>() {
        @Override
        public void onDispatch(OnOkListener listener, Object... parameters) {
            listener.onOk((Long) parameters[0]);
        }
    };

    public static void addOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.add(uid, onOkListener);
    }

    public static void removeOnOkListener(String uid, OnOkListener onOkListener) {
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

    private static KeyedDispatcher<OnCanceledListener> _onCanceledDispatcher = new KeyedDispatcher<OnCanceledListener>() {
        @Override
        public void onDispatch(OnCanceledListener listener, Object... parameters) {
            listener.onCanceled();
        }
    };

    public static void addOnCanceledListener(String uid, OnCanceledListener onCanceledListener) {
        _onCanceledDispatcher.add(uid, onCanceledListener);
    }

    public static void removeOnCanceledListener(String uid, OnCanceledListener onCanceledListener) {
        _onCanceledDispatcher.remove(uid, onCanceledListener);
    }

    public static void removeAllOnCanceledListener(String uid) {
        _onCanceledDispatcher.removeAll(uid);
    }

}
