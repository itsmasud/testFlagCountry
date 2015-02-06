package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fieldnation.R;

import static android.view.View.OnClickListener;

/**
 * Created by michael.carver on 2/5/2015.
 */
public class LocationDialog extends DialogFragmentBase {
    private static final String TAG = "ui.dialog.LocationDialog";

    //Ui
    private Button _okButton;
    private Button _cancelButton;
    private TextView _bodyTextView;

    // Data
    private boolean _hard;
    private boolean _buttonPressed = false;
    private Listener _listener;


    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public static LocationDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, LocationDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_location, container);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        _bodyTextView = (TextView) v.findViewById(R.id.body_textview);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (_hard)
            _bodyTextView.setText(R.string.dialog_location_hard);
        else
            _bodyTextView.setText(R.string.dialog_location_soft);
    }

    public void show(boolean hard, Listener listener) {
        _listener = listener;
        _hard = hard;
        super.show();
    }

    private OnClickListener _ok_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            _buttonPressed = true;
            dismiss();
            if (_listener != null)
                _listener.onOk();
        }
    };

    private OnClickListener _cancel_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            _buttonPressed = true;
            dismiss();
            if (_listener != null)
                _listener.onCancel();
        }
    };

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (_listener != null && !_buttonPressed)
            _listener.onDismiss();

        _buttonPressed = false;
    }

    public interface Listener {
        public void onOk();

        public void onCancel();

        public void onDismiss();
    }
}

