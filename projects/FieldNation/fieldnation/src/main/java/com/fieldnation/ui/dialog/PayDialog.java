package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Pay;

public class PayDialog extends DialogFragmentBase {
    private static String TAG = "ui.dialog.PayDialog";

    // State
    private static final String STATE_MODE = "STATE_MODE";
    private static final String STATE_PAY = "STATE_PAY";

    // Modes
    private static final int MODE_FIXED = 0;
    private static final int MODE_HOURLY = 1;
    private static final int MODE_PER_DEVICE = 2;
    private static final int MODE_BLENDED = 3;

    // UI
    private Spinner _typeSpinner;

    private LinearLayout _fixedLayout;
    private EditText _fixedEditText;

    private LinearLayout _hourlyLayout;
    private EditText _hourlyRateEditText;
    private EditText _maxHoursEditText;

    private LinearLayout _devicesLayout;
    private EditText _deviceRateEditText;
    private EditText _maxDevicesEditText;

    private LinearLayout _blendedLayout;
    private EditText _blendedHourlyEditText;
    private EditText _blendedMaxHoursEditText;
    private EditText _extraHourlyEditText;
    private EditText _extraMaxHoursEditText;

    private Button _okButton;
    private Button _cancelButton;

    // Data
    private Pay _pay;
    private Listener _listener;
    private int _mode = MODE_FIXED;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public static PayDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, PayDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_PAY))
                _pay = savedInstanceState.getParcelable(STATE_PAY);

            if (savedInstanceState.containsKey(STATE_MODE))
                _mode = savedInstanceState.getInt(STATE_MODE);
        }
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_pay != null) {
            outState.putParcelable(STATE_PAY, _pay);
        }

        outState.putInt(STATE_MODE, _mode);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_pay, container, false);

        _typeSpinner = (Spinner) v.findViewById(R.id.type_spinner);
        _typeSpinner.setOnItemSelectedListener(_type_selected);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.pay_types,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _typeSpinner.setAdapter(adapter);

        // fixed
        _fixedLayout = (LinearLayout) v.findViewById(R.id.fixed_layout);
        _fixedEditText = (EditText) v.findViewById(R.id.fixed_edittext);

        // hourly
        _hourlyLayout = (LinearLayout) v.findViewById(R.id.hourly_layout);
        _hourlyRateEditText = (EditText) v.findViewById(R.id.hourlyrate_edittext);
        _maxHoursEditText = (EditText) v.findViewById(R.id.maxhours_edittext);

        // per device
        _devicesLayout = (LinearLayout) v.findViewById(R.id.devices_layout);
        _deviceRateEditText = (EditText) v.findViewById(R.id.devicerate_edittext);
        _maxDevicesEditText = (EditText) v.findViewById(R.id.maxdevices_edittext);

        // blended
        _blendedLayout = (LinearLayout) v.findViewById(R.id.blended_layout);
        _blendedHourlyEditText = (EditText) v.findViewById(R.id.blendedhourlyrate_edittext);
        _blendedMaxHoursEditText = (EditText) v.findViewById(R.id.blendedmaxhours_edittext);
        _extraHourlyEditText = (EditText) v.findViewById(R.id.extrahours_edittext);
        _extraMaxHoursEditText = (EditText) v.findViewById(R.id.extramaxhours_edittext);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void init() {
        super.init();
        populateUi();
    }

    @Override
    public void reset() {
        super.reset();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(Pay pay) {
        _pay = pay;
        super.show();
    }

    private Pay makePay() {
        Pay pay = null;
        switch (_mode) {
            case MODE_FIXED:
                return new Pay(Double.parseDouble(_fixedEditText.getText().toString()));
            case MODE_HOURLY:
                return new Pay(Double.parseDouble(_hourlyRateEditText.getText().toString()),
                        Double.parseDouble(_maxHoursEditText.getText().toString()));
            case MODE_PER_DEVICE:
                return new Pay(Double.parseDouble(_deviceRateEditText.getText().toString()),
                        Integer.parseInt(_maxDevicesEditText.getText().toString()));
            case MODE_BLENDED:
                return new Pay(Double.parseDouble(_blendedHourlyEditText.getText().toString()),
                        Double.parseDouble(_blendedMaxHoursEditText.getText().toString()),
                        Double.parseDouble(_extraHourlyEditText.getText().toString()),
                        Double.parseDouble(_extraMaxHoursEditText.getText().toString()));
        }
        return pay;
    }

    private void clearUi() {
        _fixedLayout.setVisibility(View.GONE);
        _hourlyLayout.setVisibility(View.GONE);
        _devicesLayout.setVisibility(View.GONE);
        _blendedLayout.setVisibility(View.GONE);
    }

    private void setMode(int mode) {
        _typeSpinner.setSelection(mode);
        clearUi();
        _mode = mode;
        switch (mode) {
            case MODE_FIXED:
                _fixedLayout.setVisibility(View.VISIBLE);
                break;
            case MODE_HOURLY:
                _hourlyLayout.setVisibility(View.VISIBLE);
                break;
            case MODE_PER_DEVICE:
                _devicesLayout.setVisibility(View.VISIBLE);
                break;
            case MODE_BLENDED:
                _blendedLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void populateUi() {
        if (_pay == null)
            return;

        if (_pay.isBlendedRate()) {
            setMode(MODE_BLENDED);
            _blendedHourlyEditText.setText(_pay.getBlendedStartRate() + "");
            _blendedMaxHoursEditText.setText(_pay.getBlendedFirstHours() + "");
            _extraHourlyEditText.setText(_pay.getBlendedAdditionalRate() + "");
            _extraMaxHoursEditText.setText(_pay.getBlendedAdditionalHours() + "");
        } else if (_pay.isFixedRate()) {
            setMode(MODE_FIXED);
            _fixedEditText.setText(_pay.getFixedAmount() + "");
        } else if (_pay.isHourlyRate()) {
            setMode(MODE_HOURLY);
            _hourlyRateEditText.setText(_pay.getPerHour() + "");
            _maxHoursEditText.setText(_pay.getMaxHour() + "");
        } else if (_pay.isPerDeviceRate()) {
            setMode(MODE_PER_DEVICE);
            _deviceRateEditText.setText(_pay.getPerDevice() + "");
            _maxDevicesEditText.setText(_pay.getMaxDevice() + "");
        }
    }

	/*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/

    private AdapterView.OnItemSelectedListener _type_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setMode(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null)
                _listener.onNothing();
        }
    };

    private View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener == null)
                return;

            _listener.onComplete(makePay());
        }
    };

    public interface Listener {
        public void onComplete(Pay pay);

        public void onNothing();
    }

}
