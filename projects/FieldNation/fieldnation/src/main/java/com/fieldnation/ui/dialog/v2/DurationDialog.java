package com.fieldnation.ui.dialog.v2;

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
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fntoast.ToastClient;
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

        _dayTextView = (TextView) v.findViewById(R.id.day_textview);
        _hourTextView = (TextView) v.findViewById(R.id.hour_textview);
        _minTextView = (TextView) v.findViewById(R.id.min_textview);

        _numberPad = new Button[10];
        _numberPad[0] = (Button) v.findViewById(R.id.button0);
        _numberPad[1] = (Button) v.findViewById(R.id.button1);
        _numberPad[2] = (Button) v.findViewById(R.id.button2);
        _numberPad[3] = (Button) v.findViewById(R.id.button3);
        _numberPad[4] = (Button) v.findViewById(R.id.button4);
        _numberPad[5] = (Button) v.findViewById(R.id.button5);
        _numberPad[6] = (Button) v.findViewById(R.id.button6);
        _numberPad[7] = (Button) v.findViewById(R.id.button7);
        _numberPad[8] = (Button) v.findViewById(R.id.button8);
        _numberPad[9] = (Button) v.findViewById(R.id.button9);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _deleteButton = (IconFontButton) v.findViewById(R.id.delete_imageview);

        return v;
    }

    @Override
    public void onAdded() {
        super.onAdded();

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

                Bundle response = new Bundle();
                response.putInt(PARAM_RESULT, PARAM_RESULT_OK);
                response.putLong(PARAM_VALUE, seconds * 1000);
                onResult(response);


                dismiss(true);
            } catch (Exception ex) {
                ToastClient.toast(App.get(), "Invalid number, please try again.", Toast.LENGTH_LONG);
            }
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle response = new Bundle();
            response.putInt(PARAM_RESULT, PARAM_RESULT_CANCEL);
            onResult(response);
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

    public static class Controller extends com.fieldnation.fndialog.Controller {
        public Controller(Context context, String uid) {
            super(context, DurationDialog.class, uid);
        }

        public static void show(Context context, String uid) {
            show(context, uid, DurationDialog.class, null);
        }
    }

    public static abstract class ControllerListener implements com.fieldnation.fndialog.Controller.Listener {
        @Override
        public void onComplete(Bundle response) {
            switch (response.getInt(PARAM_RESULT)) {
                case PARAM_RESULT_OK:
                    onOk(response.getLong(PARAM_VALUE));
                    break;
                case PARAM_RESULT_CANCEL:
                    onCancel();
                    break;
            }
        }

        public abstract void onOk(long milliseconds);

        public abstract void onCancel();
    }
}
