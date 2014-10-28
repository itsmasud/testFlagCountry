package com.fieldnation.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class AcceptBundleWorkroder extends Dialog {
    private static final String TAG = "ui.workorder.detail.AcceptBundleWorkroder";

    // UI
    private TextView _acceptWOText;
    private TextView _viewBundle;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private Listener _listener;

    /*-*****************************-*/
    /*-			Life Cycle			-*/
    /*-*****************************-*/

    public AcceptBundleWorkroder(Context context) {
        super(context);
        setContentView(R.layout.dialog_accept_bundle_workorder);

        _acceptWOText = (TextView) findViewById(R.id.accept_description);
        _viewBundle = (TextView) findViewById(R.id.view_bundle);
        _viewBundle.setOnClickListener(_viewBundle_onClick);
        _okButton = (Button) findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);
        _cancelButton = (Button) findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        setTitle(R.string.activity_bundle_detail);
    }

    public void show(Workorder _workorder) {
        if(_workorder == null)
            return;

        _acceptWOText.setText("This workorder is part of a bundle of "+_workorder.getBundleId().toString()+" workorders. By accepting this workorder you are accepting all of them.");
        //@TODO

        show();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private View.OnClickListener _viewBundle_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            //@TODO
            if (_listener != null) {
                _listener.onOk();
            }
        }
    };

    private View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            //@TODO
            if (_listener != null) {
                _listener.onOk();
            }
        }
    };

    private View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null) {
                _listener.onCancel();
            }
        }
    };

    public interface Listener {
        //@TODO
        public void onOk();

        public void onCancel();

    }
}
