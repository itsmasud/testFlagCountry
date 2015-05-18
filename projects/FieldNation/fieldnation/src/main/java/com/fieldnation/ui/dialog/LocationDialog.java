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
    private static final String TAG = "LocationDialog";

    //Ui
    private Button _okButton;
    private Button _notNowButton;
    private TextView _bodyTextView;

    // Data
    private boolean _hard;
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

        _notNowButton = (Button) v.findViewById(R.id.notnow_button);
        _notNowButton.setOnClickListener(_notNow_onClick);

        _bodyTextView = (TextView) v.findViewById(R.id.body_textview);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (_hard) {
            _bodyTextView.setText(R.string.dialog_location_hard);
            _notNowButton.setText(R.string.btn_cancel);
        } else {
            _bodyTextView.setText(R.string.dialog_location_soft);
            _notNowButton.setText(R.string.btn_not_now);
        }
    }

    public void setData(boolean hard, Listener listener) {
        _hard = hard;
        _listener = listener;

        if (_bodyTextView != null)
            onResume();
    }

    public void show(boolean hard, Listener listener) {
        _listener = listener;
        _hard = hard;
        super.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

    private final OnClickListener _ok_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null)
                _listener.onOk();
        }
    };

    private final OnClickListener _notNow_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null) {
                if (_hard)
                    _listener.onCancel();
                else
                    _listener.onNotNow();
            }
        }
    };

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (_listener != null)
            _listener.onCancel();
    }

    public interface Listener {
        void onOk();

        void onNotNow();

        void onCancel();
    }
}

