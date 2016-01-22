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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.service.toast.ToastClient;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class PayDialog extends DialogFragmentBase {
    private static String TAG = "PayDialog";

    // State
    private static final String STATE_MODE = "STATE_MODE";
    private static final String STATE_PAY = "STATE_PAY";

    // Modes
    private static final int MODE_FIXED = 0;
    private static final int MODE_HOURLY = 1;
    private static final int MODE_PER_DEVICE = 2;
    private static final int MODE_BLENDED = 3;

    // UI
    private MaterialBetterSpinner _typeSpinner;

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

    private TextInputLayout _explanationLayout;
    private EditText _explanationEditText;

    private Button _okButton;
    private Button _cancelButton;

    // Data
    private Pay _pay;
    private Listener _listener;
    private boolean _showExplanation;
    private int _mode = MODE_FIXED;


    // Payable & Hours
    private static double MINIMUM_ACCUMULATED_PAYABLE_AMOUNT = 20;
    private double _fixedAmount;
    private double _hourlyRateAmount;
    private double _maxHours;
    private double _deviceRate;
    private int _maxDevices;
    private double blendedHourlyAmount;
    private double _blendedMaxHours;
    private double _extraHourly;
    private double _extraMaxHours;
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

        _typeSpinner = (MaterialBetterSpinner) v.findViewById(R.id.type_spinner);
        _typeSpinner.setOnItemClickListener(_type_selected);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.pay_types,
                R.layout.view_spinner_item);
        adapter.setDropDownViewResource(
                android.support.design.R.layout.support_simple_spinner_dropdown_item);
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

    @Override
    public void reset() {
        super.reset();
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

    private Double getAmount(String _amount) {
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

    private Integer getNumberOfDevice(String _noStr) {
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
                    _fixedAmount = getAmount(_fixedEditText.getText().toString());
                    return _fixedAmount >= MINIMUM_ACCUMULATED_PAYABLE_AMOUNT ? true : false;
                case MODE_HOURLY:
                    _hourlyRateAmount = getAmount((_hourlyRateEditText.getText().toString()));
                    _maxHours = getAmount((_maxHoursEditText.getText().toString()));
                    return _hourlyRateAmount * _maxHours >= MINIMUM_ACCUMULATED_PAYABLE_AMOUNT ? true : false;
                case MODE_PER_DEVICE:
                    _deviceRate = getAmount((_deviceRateEditText.getText().toString()));
                    _maxDevices = getNumberOfDevice((_maxDevicesEditText.getText().toString()));
                    return _deviceRate * _maxDevices >= MINIMUM_ACCUMULATED_PAYABLE_AMOUNT ? true : false;
                case MODE_BLENDED:
                    blendedHourlyAmount = getAmount((_blendedHourlyEditText.getText().toString()));
                    _blendedMaxHours = getAmount((_blendedMaxHoursEditText.getText().toString()));
                    _extraHourly = getAmount((_extraHourlyEditText.getText().toString()));
                    _extraMaxHours = getAmount((_extraMaxHoursEditText.getText().toString()));
                    return (blendedHourlyAmount * _blendedMaxHours) + (_extraHourly * _extraMaxHours) >= MINIMUM_ACCUMULATED_PAYABLE_AMOUNT ? true : false;
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
//        _fixedLayout.setVisibility(View.GONE);
//        _hourlyLayout.setVisibility(View.GONE);
//        _devicesLayout.setVisibility(View.GONE);
//        _blendedLayout.setVisibility(View.GONE);
    }

    private void setMode(int mode) {
        _typeSpinner.setSelection(mode);
        clearUi();
        _mode = mode;
        switch (mode) {
            case MODE_FIXED:
                _fixedLayout.setVisibility(View.VISIBLE);
                _hourlyLayout.setVisibility(View.GONE);
                _devicesLayout.setVisibility(View.GONE);
                _blendedLayout.setVisibility(View.GONE);
                break;
            case MODE_HOURLY:
                _hourlyLayout.setVisibility(View.VISIBLE);
                _fixedLayout.setVisibility(View.GONE);
                _devicesLayout.setVisibility(View.GONE);
                _blendedLayout.setVisibility(View.GONE);
                break;
            case MODE_PER_DEVICE:
                _devicesLayout.setVisibility(View.VISIBLE);
                _fixedLayout.setVisibility(View.GONE);
                _hourlyLayout.setVisibility(View.GONE);
                _blendedLayout.setVisibility(View.GONE);
                break;
            case MODE_BLENDED:
                _blendedLayout.setVisibility(View.VISIBLE);
                _fixedLayout.setVisibility(View.GONE);
                _hourlyLayout.setVisibility(View.GONE);
                _devicesLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void populateUi() {
        if (_pay == null)
            return;

        if (_showExplanation) {
            _explanationLayout.setVisibility(View.VISIBLE);
        } else {
            _explanationLayout.setVisibility(View.GONE);
        }

        if (_pay.isBlendedRate()) {
            setMode(MODE_BLENDED);
            _blendedHourlyEditText.setText(_pay.getBlendedStartRate() + "");
            _blendedMaxHoursEditText.setText(_pay.getBlendedFirstHours() + "");
            _extraHourlyEditText.setText(_pay.getBlendedAdditionalRate() + "");
            _extraMaxHoursEditText.setText(_pay.getBlendedAdditionalHours() + "");
            _blendedHourlyEditText.requestFocus();
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

    private final AdapterView.OnItemClickListener _type_selected = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            setMode(position);
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
                ToastClient.toast(App.get(), "Please enter a value greater than $0.10.", Toast.LENGTH_SHORT);
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
