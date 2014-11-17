package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;

/**
 * Created by michael.carver on 10/28/2014.
 */
public class DeviceCountDialog extends DialogFragmentBase {
    private static final String TAG = "ui.dialog.DeviceCountDialog";

    // State
    private static final String MAX_COUNT = "STATE_MAX_COUNT";

    // UI
    private NumberPicker _devicePicker;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private Listener _listener;
    private Workorder _workorder;
    private int _maxCount;


    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    // grabs the dialog from the fragment stack if it already exists
    public static DeviceCountDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, DeviceCountDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            _maxCount = savedInstanceState.getInt(MAX_COUNT, 0);
        }
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(MAX_COUNT, _maxCount);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_device_count, container, false);

        _devicePicker = (NumberPicker) v.findViewById(R.id.devices_picker);
        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

//        getDialog().setTitle(R.string.device_count);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateUi();
    }

    public void show(Workorder workorder, int maxDeviceCount) {
        _maxCount = maxDeviceCount;
        _workorder = workorder;
        show();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_devicePicker == null)
            return;

        _devicePicker.setMaxValue(_maxCount);
    }

    /*-*************************-*/
    /*-         Events          -*/
    /*-*************************-*/
    private View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null)
                _listener.onOk(_workorder, _devicePicker.getValue());
        }
    };

    private View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    public interface Listener {
        public void onOk(Workorder workorder, int count);
    }
}
