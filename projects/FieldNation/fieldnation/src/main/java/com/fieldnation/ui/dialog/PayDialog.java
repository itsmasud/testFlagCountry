package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;

public class PayDialog extends DialogFragmentBase {
    private static String TAG = "PayDialog";

    private static final double MINIMUM_ACCUMULATED_PAYABLE_AMOUNT = 20;

    // State
    private static final String STATE_MODE = "STATE_MODE";
    private static final String STATE_SHOW_EXPLANATION = "STATE_SHOW_EXPLANATION";
    private static final String STATE_EXPLANATION = "STATE_EXPLANATION";

    private static final String STATE_FIXED = "STATE_FIXED";

    private static final String STATE_HOURLY_RATE = "STATE_HOURLY_RATE";
    private static final String STATE_HOURLY_MAX = "STATE_MAX_HOURS";

    private static final String STATE_DEVICES_RATE = "STATE_DEVICES_RATE";
    private static final String STATE_DEVICES_MAX = "STATE_DEVICES_MAX";

    private static final String STATE_BLENDED_HOURLY = "STATE_BLENDED_HOURLY";
    private static final String STATE_BLENDED_HOURLY_MAX = "STATE_BLENDED_HOURLY_MAX";
    private static final String STATE_BLENDED_XHOURLY = "STATE_BLENDED_XHOURLY";
    private static final String STATE_BLENDED_XHOURLY_MAX = "STATE_BLENDED_XHOURLY_MAX";

    // Modes
    private static final int MODE_FIXED = 0;
    private static final int MODE_HOURLY = 1;
    private static final int MODE_PER_DEVICE = 2;
    private static final int MODE_BLENDED = 3;

    // UI
    private HintSpinner _typeSpinner;

    private LinearLayout _fixedLayout;
    private EditText _fixedEditText;

    private LinearLayout _hourlyLayout;
    private EditText _hourlyRateEditText;
    private EditText _maxHoursEditText;

    private LinearLayout _devicesLayout;
    private EditText _deviceRateEditText;
    private EditText _maxDevicesEditText;

    private LinearLayout _blendedLayout;
    private EditText _blendedFixedRateEditText;
    private EditText _blendedFixedMaxHoursEditText;
    private EditText _extraHourlyEditText;
    private EditText _extraMaxHoursEditText;

    private TextInputLayout _explanationLayout;
    private EditText _explanationEditText;

    private Button _okButton;
    private Button _cancelButton;

    // Data
    private Pay _pay;
    private Listener _listener;
    private boolean _showExplanation;
    private int _mode = MODE_FIXED;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public static PayDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, PayDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_MODE))
                _mode = savedInstanceState.getInt(STATE_MODE);

            if (savedInstanceState.containsKey(STATE_SHOW_EXPLANATION))
                _showExplanation = savedInstanceState.getBoolean(STATE_SHOW_EXPLANATION);

            if (savedInstanceState.containsKey(STATE_EXPLANATION))
                _explanationEditText.setText(savedInstanceState.getString(STATE_EXPLANATION));

            if (savedInstanceState.containsKey(STATE_FIXED))
                _fixedEditText.setText(savedInstanceState.getString(STATE_FIXED));

            if (savedInstanceState.containsKey(STATE_HOURLY_RATE))
                _hourlyRateEditText.setText(savedInstanceState.getString(STATE_HOURLY_RATE));

            if (savedInstanceState.containsKey(STATE_HOURLY_MAX))
                _maxHoursEditText.setText(savedInstanceState.getString(STATE_HOURLY_MAX));

            if (savedInstanceState.containsKey(STATE_DEVICES_RATE))
                _deviceRateEditText.setText(savedInstanceState.getString(STATE_DEVICES_RATE));

            if (savedInstanceState.containsKey(STATE_DEVICES_MAX))
                _maxDevicesEditText.setText(savedInstanceState.getString(STATE_DEVICES_MAX));

            if (savedInstanceState.containsKey(STATE_BLENDED_HOURLY))
                _blendedFixedRateEditText.setText(savedInstanceState.getString(STATE_BLENDED_HOURLY));

            if (savedInstanceState.containsKey(STATE_BLENDED_HOURLY_MAX))
                _blendedFixedMaxHoursEditText.setText(savedInstanceState.getString(STATE_BLENDED_HOURLY_MAX));

            if (savedInstanceState.containsKey(STATE_BLENDED_XHOURLY))
                _extraHourlyEditText.setText(savedInstanceState.getString(STATE_BLENDED_XHOURLY));

            if (savedInstanceState.containsKey(STATE_BLENDED_XHOURLY_MAX))
                _extraMaxHoursEditText.setText(savedInstanceState.getString(STATE_BLENDED_XHOURLY_MAX));
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_MODE, _mode);
        outState.putBoolean(STATE_SHOW_EXPLANATION, _showExplanation);

        if (_explanationEditText != null && _explanationEditText.getText() != null)
            outState.putString(STATE_EXPLANATION, _explanationEditText.getText().toString());

        if (_fixedEditText != null && !misc.isEmptyOrNull(_fixedEditText.getText().toString()))
            outState.putString(STATE_FIXED, _fixedEditText.getText().toString());

        if (_hourlyRateEditText != null && !misc.isEmptyOrNull(_hourlyRateEditText.getText().toString()))
            outState.putString(STATE_HOURLY_RATE, _hourlyRateEditText.getText().toString());

        if (_maxHoursEditText != null && !misc.isEmptyOrNull(_maxHoursEditText.getText().toString()))
            outState.putString(STATE_HOURLY_MAX, _maxHoursEditText.getText().toString());

        if (_deviceRateEditText != null && !misc.isEmptyOrNull(_deviceRateEditText.getText().toString()))
            outState.putString(STATE_DEVICES_RATE, _deviceRateEditText.getText().toString());

        if (_maxDevicesEditText != null && !misc.isEmptyOrNull(_maxDevicesEditText.getText().toString()))
            outState.putString(STATE_DEVICES_MAX, _maxDevicesEditText.getText().toString());

        if (_blendedFixedRateEditText != null && !misc.isEmptyOrNull(_blendedFixedRateEditText.getText().toString()))
            outState.putString(STATE_BLENDED_HOURLY, _blendedFixedRateEditText.getText().toString());

        if (_blendedFixedMaxHoursEditText != null && !misc.isEmptyOrNull(_blendedFixedMaxHoursEditText.getText().toString()))
            outState.putString(STATE_BLENDED_HOURLY_MAX, _blendedFixedMaxHoursEditText.getText().toString());

        if (_extraHourlyEditText != null && !misc.isEmptyOrNull(_extraHourlyEditText.getText().toString()))
            outState.putString(STATE_BLENDED_XHOURLY, _extraHourlyEditText.getText().toString());

        if (_extraMaxHoursEditText != null && !misc.isEmptyOrNull(_extraMaxHoursEditText.getText().toString()))
            outState.putString(STATE_BLENDED_XHOURLY_MAX, _extraMaxHoursEditText.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_pay, container, false);

        _typeSpinner = (HintSpinner) v.findViewById(R.id.type_spinner);
        _typeSpinner.setOnItemSelectedListener(_type_selected);

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
        _blendedFixedRateEditText = (EditText) v.findViewById(R.id.blendedFixedRate_edittext);
        _blendedFixedMaxHoursEditText = (EditText) v.findViewById(R.id.blendedFixedMaxHours_edittext);
        _extraHourlyEditText = (EditText) v.findViewById(R.id.extrahours_edittext);
        _extraMaxHoursEditText = (EditText) v.findViewById(R.id.extramaxhours_edittext);

        _explanationLayout = (TextInputLayout) v.findViewById(R.id.explanation_layout);
        _explanationEditText = (EditText) v.findViewById(R.id.explanation_edittext);

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

    private HintSpinner getTypeSpinner() {
        if (_typeSpinner != null && _typeSpinner.getAdapter() == null) {
            HintArrayAdapter adapter = HintArrayAdapter.createFromResources(
                    _typeSpinner.getContext(),
                    R.array.pay_types,
                    R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _typeSpinner.setAdapter(adapter);
        }
        return _typeSpinner;
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(Pay pay, boolean showExplanation) {
        _pay = pay;
        _showExplanation = showExplanation;
        super.show();
    }

    public void show(Pay pay) {
        _showExplanation = false;
        _pay = pay;
        super.show();
    }

    private Double getDouble(String _amount) {
        try {
            double amount = Double.parseDouble(_amount);
            if ((int) (amount * 100) < 10) {
                return 0.0;
            } else {
                return amount;
            }
        } catch (Exception ex) {
            return 0.0;
        }
    }

    private Integer getInteger(String _noStr) {
        try {
            int _no = Integer.parseInt(_noStr);
            return _no;
        } catch (Exception ex) {
            return 0;
        }
    }

    private boolean isValidAmount() {
        try {
            switch (_mode) {
                case MODE_FIXED:
                    return getDouble(_fixedEditText.getText().toString()) >= MINIMUM_ACCUMULATED_PAYABLE_AMOUNT;
                case MODE_HOURLY:
                    double hourlyRateAmount = getDouble((_hourlyRateEditText.getText().toString()));
                    double maxHours = getDouble((_maxHoursEditText.getText().toString()));
                    return hourlyRateAmount * maxHours >= MINIMUM_ACCUMULATED_PAYABLE_AMOUNT;
                case MODE_PER_DEVICE:
                    double deviceRate = getDouble((_deviceRateEditText.getText().toString()));
                    int maxDevices = getInteger((_maxDevicesEditText.getText().toString()));
                    return deviceRate * maxDevices >= MINIMUM_ACCUMULATED_PAYABLE_AMOUNT;
                case MODE_BLENDED:
                    double blendedFixedRate = getDouble((_blendedFixedRateEditText.getText().toString()));
//                    double blendedMaxHours = getDouble((_blendedFixedMaxHoursEditText.getText().toString()));
                    double extraHourly = getDouble((_extraHourlyEditText.getText().toString()));
                    double extraMaxHours = getDouble((_extraMaxHoursEditText.getText().toString()));
                    return blendedFixedRate  + (extraHourly * extraMaxHours) >= MINIMUM_ACCUMULATED_PAYABLE_AMOUNT;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }


    private Pay makePay() {
        Pay pay = null;
        switch (_mode) {
            case MODE_FIXED:
                pay = new Pay(Double.parseDouble(_fixedEditText.getText().toString()));
                break;
            case MODE_HOURLY:
                pay = new Pay(Double.parseDouble(_hourlyRateEditText.getText().toString()),
                        Double.parseDouble(_maxHoursEditText.getText().toString()));
                break;
            case MODE_PER_DEVICE:
                pay = new Pay(Double.parseDouble(_deviceRateEditText.getText().toString()),
                        Integer.parseInt(_maxDevicesEditText.getText().toString()));
                break;
            case MODE_BLENDED:
                pay = new Pay(Double.parseDouble(_blendedFixedRateEditText.getText().toString()),
                        Double.parseDouble(_blendedFixedMaxHoursEditText.getText().toString()),
                        Double.parseDouble(_extraHourlyEditText.getText().toString()),
                        Double.parseDouble(_extraMaxHoursEditText.getText().toString()));
                break;
        }
        if (pay != null
                && _explanationEditText != null
                && !misc.isEmptyOrNull(_explanationEditText.getText().toString())) {
            pay.setDescription(_explanationEditText.getText().toString());
        }
        return pay;
    }

    private void setMode(int mode) {
        getTypeSpinner().setSelection(mode);
        _mode = mode;
        switch (mode) {
            case MODE_FIXED:
                _blendedLayout.setVisibility(View.GONE);
                _devicesLayout.setVisibility(View.GONE);
                _fixedLayout.setVisibility(View.VISIBLE);
                _hourlyLayout.setVisibility(View.GONE);
                break;
            case MODE_HOURLY:
                _blendedLayout.setVisibility(View.GONE);
                _devicesLayout.setVisibility(View.GONE);
                _fixedLayout.setVisibility(View.GONE);
                _hourlyLayout.setVisibility(View.VISIBLE);
                break;
            case MODE_PER_DEVICE:
                _blendedLayout.setVisibility(View.GONE);
                _devicesLayout.setVisibility(View.VISIBLE);
                _fixedLayout.setVisibility(View.GONE);
                _hourlyLayout.setVisibility(View.GONE);
                break;
            case MODE_BLENDED:
                _blendedLayout.setVisibility(View.VISIBLE);
                _devicesLayout.setVisibility(View.GONE);
                _fixedLayout.setVisibility(View.GONE);
                _hourlyLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void populateUi() {
        if (_showExplanation) {
            _explanationLayout.setVisibility(View.VISIBLE);
        } else {
            _explanationLayout.setVisibility(View.GONE);
        }

        setMode(_mode);

        if (_pay == null)
            return;

        if (_pay.isBlendedRate()) {
            setMode(MODE_BLENDED);
            _blendedFixedRateEditText.setText(_pay.getBlendedStartRate() + "");
            _blendedFixedMaxHoursEditText.setText(_pay.getBlendedFirstHours() + "");
            _extraHourlyEditText.setText(_pay.getBlendedAdditionalRate() + "");
            _extraMaxHoursEditText.setText(_pay.getBlendedAdditionalHours() + "");
            _blendedFixedRateEditText.requestFocus();

        } else if (_pay.isFixedRate()) {
            setMode(MODE_FIXED);
            _fixedEditText.setText((_pay.getFixedAmount() + "").trim());
            _fixedEditText.requestFocus();

        } else if (_pay.isHourlyRate()) {
            setMode(MODE_HOURLY);
            _hourlyRateEditText.setText((_pay.getPerHour() + "").trim());
            _maxHoursEditText.setText((_pay.getMaxHour() + "").trim());
            _hourlyRateEditText.requestFocus();

        } else if (_pay.isPerDeviceRate()) {
            setMode(MODE_PER_DEVICE);
            _deviceRateEditText.setText((_pay.getPerDevice() + "").trim());
            _maxDevicesEditText.setText((_pay.getMaxDevice() + "").trim());
            _deviceRateEditText.requestFocus();
        }
        _explanationEditText.setText(_pay.getDescription());
    }

    @Override
    public void onResume() {
        super.onResume();

        populateUi();
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final AdapterView.OnItemSelectedListener _type_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setMode(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null)
                _listener.onNothing();
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isValidAmount()) {
                ToastClient.toast(App.get(), getResources().getString(R.string.toast_minimum_accumulated_payable_amount), Toast.LENGTH_SHORT);
                return;
            }

            if (_listener == null)
                return;

            try {
                _listener.onComplete(makePay(), _explanationEditText.getText().toString());
            } catch (Exception ex) {
                ToastClient.toast(App.get(), R.string.please_enter_a_value_greater_than, Toast.LENGTH_SHORT);
                return;
            }
            dismiss();
        }
    };

    public interface Listener {
        void onComplete(Pay pay, String explanation);

        void onNothing();
    }

}
