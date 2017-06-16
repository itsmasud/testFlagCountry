package com.fieldnation.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.PayAdditional;
import com.fieldnation.v2.data.model.PayBase;

/**
 * Created by shoaib.ahmed on 30/5/2017.
 */
public class PaymentCoView extends RelativeLayout {
    private static String TAG = "PaymentCoView";


    private static final double MINIMUM_ACCUMULATED_PAYABLE_AMOUNT = 20;
    private static final double MINIMUM_PAYABLE_AMOUNT = 1.0;

    // Modes
    private static final int MODE_FIXED = 0;
    private static final int MODE_HOURLY = 1;
    private static final int MODE_PER_DEVICE = 2;
    private static final int MODE_BLENDED = 3;

    // UI
    private TextView _payTypeTitleTextView;
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

    // Data
    private int _mode = -1;

    public PaymentCoView(Context context) {
        super(context);
        init();
    }

    public PaymentCoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaymentCoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Log.e(TAG, "init");
        LayoutInflater.from(getContext()).inflate(R.layout.view_co_pay_new, this);

        if (isInEditMode())
            return;

        _payTypeTitleTextView = (TextView) findViewById(R.id.pay_type_title_textview);

        _typeSpinner = (HintSpinner) findViewById(R.id.type_spinner);
        _typeSpinner.setOnItemSelectedListener(_type_selected);
        getTypeSpinner();

        // fixed
        _fixedLayout = (LinearLayout) findViewById(R.id.fixed_layout);
        _fixedEditText = (EditText) findViewById(R.id.fixed_edittext);

        // hourly
        _hourlyLayout = (LinearLayout) findViewById(R.id.hourly_layout);
        _hourlyRateEditText = (EditText) findViewById(R.id.hourlyrate_edittext);
        _maxHoursEditText = (EditText) findViewById(R.id.maxhours_edittext);

        // per device
        _devicesLayout = (LinearLayout) findViewById(R.id.devices_layout);
        _deviceRateEditText = (EditText) findViewById(R.id.devicerate_edittext);
        _maxDevicesEditText = (EditText) findViewById(R.id.maxdevices_edittext);

        // blended
        _blendedLayout = (LinearLayout) findViewById(R.id.blended_layout);
        _blendedFixedRateEditText = (EditText) findViewById(R.id.blendedFixedRate_edittext);
        _blendedFixedMaxHoursEditText = (EditText) findViewById(R.id.blendedFixedMaxHours_edittext);
        _extraHourlyEditText = (EditText) findViewById(R.id.extrahours_edittext);
        _extraMaxHoursEditText = (EditText) findViewById(R.id.extramaxhours_edittext);

        populateUi();
    }


    private HintSpinner getTypeSpinner() {
        if (_typeSpinner != null && _typeSpinner.getAdapter() == null) {
            HintArrayAdapter adapter = HintArrayAdapter.createFromResources(
                    _typeSpinner.getContext(),
                    R.array.pay_types,
                    R.layout.view_spinner_item_gray);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _typeSpinner.setAdapter(adapter);
        }
        return _typeSpinner;
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
            return getDouble(_noStr).intValue();
        }
    }

    public boolean isValidAmount() {
        try {
            switch (_mode) {
                case MODE_FIXED:
                    return getDouble(_fixedEditText.getText().toString()) >= MINIMUM_PAYABLE_AMOUNT;
                case MODE_HOURLY:
                    double hourlyRateAmount = getDouble((_hourlyRateEditText.getText().toString()));
                    return hourlyRateAmount >= MINIMUM_PAYABLE_AMOUNT;
                case MODE_PER_DEVICE:
                    double deviceRate = getDouble((_deviceRateEditText.getText().toString()));
                    return deviceRate >= MINIMUM_PAYABLE_AMOUNT;
                case MODE_BLENDED:
                    double blendedFixedRate = getDouble((_blendedFixedRateEditText.getText().toString()));
//                    double blendedMaxHours = getDouble((_blendedFixedMaxHoursEditText.getText().toString()));
                    double extraHourly = getDouble((_extraHourlyEditText.getText().toString()));
                    return blendedFixedRate >= MINIMUM_PAYABLE_AMOUNT && extraHourly >= MINIMUM_PAYABLE_AMOUNT;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }


    public boolean isValidTotalAmount() {
        try {
            switch (_mode) {
                case MODE_FIXED:
                    return getDouble(_fixedEditText.getText().toString()) > MINIMUM_ACCUMULATED_PAYABLE_AMOUNT;
                case MODE_HOURLY:
                    double hourlyRateAmount = getDouble((_hourlyRateEditText.getText().toString()));
                    double maxHours = getDouble((_maxHoursEditText.getText().toString()));
                    return hourlyRateAmount * maxHours > MINIMUM_ACCUMULATED_PAYABLE_AMOUNT;
                case MODE_PER_DEVICE:
                    double deviceRate = getDouble((_deviceRateEditText.getText().toString()));
                    int maxDevices = getInteger((_maxDevicesEditText.getText().toString()));
                    return deviceRate * maxDevices > MINIMUM_ACCUMULATED_PAYABLE_AMOUNT;
                case MODE_BLENDED:
                    double blendedFixedRate = getDouble((_blendedFixedRateEditText.getText().toString()));
//                    double blendedMaxHours = getDouble((_blendedFixedMaxHoursEditText.getText().toString()));
                    double extraHourly = getDouble((_extraHourlyEditText.getText().toString()));
                    double extraMaxHours = getDouble((_extraMaxHoursEditText.getText().toString()));
                    return blendedFixedRate + (extraHourly * extraMaxHours) > MINIMUM_ACCUMULATED_PAYABLE_AMOUNT;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }


    public boolean isValidPay() {
        if (_mode == -1) return false;

//        try {
//            switch (_mode) {
//                case MODE_FIXED:
//                    if (misc.isEmptyOrNull(_fixedEditText.getText().toString())) return false;
//                    break;
//                case MODE_HOURLY:
//                    if (misc.isEmptyOrNull(_hourlyRateEditText.getText().toString()) || misc.isEmptyOrNull(_maxHoursEditText.getText().toString()))
//                        return false;
//                    break;
//                case MODE_PER_DEVICE:
//                    if (misc.isEmptyOrNull(_deviceRateEditText.getText().toString()) || misc.isEmptyOrNull(_maxDevicesEditText.getText().toString()))
//                        return false;
//                    break;
//                case MODE_BLENDED:
//                    if (misc.isEmptyOrNull(_blendedFixedRateEditText.getText().toString())
//                            || misc.isEmptyOrNull(_blendedFixedMaxHoursEditText.getText().toString())
//                            || misc.isEmptyOrNull(_extraHourlyEditText.getText().toString())
//                            || misc.isEmptyOrNull(_extraMaxHoursEditText.getText().toString()))
//                        return false;
//
//                    break;
//            }
//        } catch (Exception ex) {
//            Log.v(TAG, ex);
//        }
        return true;
    }

    public Pay getPay() {
        Pay pay = new Pay();
        try {
            switch (_mode) {
                case MODE_FIXED:
                    pay.setType(Pay.TypeEnum.FIXED);
                    pay.setBase(new PayBase()
                            .amount(misc.isEmptyOrNull(_fixedEditText.getText().toString()) ? 0.0 : Double.parseDouble(_fixedEditText.getText().toString()))
                            .units(1.0));
                    break;
                case MODE_HOURLY:
                    pay.setType(Pay.TypeEnum.HOURLY);
                    pay.setBase(new PayBase()
                            .amount(misc.isEmptyOrNull(_hourlyRateEditText.getText().toString()) ? 0.0 : Double.parseDouble(_hourlyRateEditText.getText().toString()))
                            .units(misc.isEmptyOrNull(_maxHoursEditText.getText().toString()) ? 0.0 : Double.parseDouble(_maxHoursEditText.getText().toString())));
                    break;
                case MODE_PER_DEVICE:
                    pay.setType(Pay.TypeEnum.DEVICE);
                    pay.setBase(new PayBase()
                            .amount(misc.isEmptyOrNull(_deviceRateEditText.getText().toString()) ? 0.0 : Double.parseDouble(_deviceRateEditText.getText().toString()))
                            .units(misc.isEmptyOrNull(_maxDevicesEditText.getText().toString()) ? 0.0 : Double.parseDouble(_maxDevicesEditText.getText().toString())));
                    break;
                case MODE_BLENDED:
                    pay.setType(Pay.TypeEnum.BLENDED);
                    pay.setBase(new PayBase()
                            .amount(misc.isEmptyOrNull(_blendedFixedRateEditText.getText().toString()) ? 0.0 : Double.parseDouble(_blendedFixedRateEditText.getText().toString()))
                            .units(misc.isEmptyOrNull(_blendedFixedMaxHoursEditText.getText().toString()) ? 0.0 : Double.parseDouble(_blendedFixedMaxHoursEditText.getText().toString())));
                    pay.setAdditional(new PayAdditional()
                            .amount(misc.isEmptyOrNull(_extraHourlyEditText.getText().toString()) ? 0.0 : Double.parseDouble(_extraHourlyEditText.getText().toString()))
                            .units(misc.isEmptyOrNull(_extraMaxHoursEditText.getText().toString()) ? 0.0 : Double.parseDouble(_extraMaxHoursEditText.getText().toString())));
                    break;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return pay;
    }

    private void setMode(int mode) {
        _mode = mode;

        switch (mode) {
            case MODE_FIXED:
                _blendedLayout.setVisibility(View.GONE);
                _devicesLayout.setVisibility(View.GONE);
                _fixedLayout.setVisibility(View.VISIBLE);
                _hourlyLayout.setVisibility(View.GONE);
                _payTypeTitleTextView.setVisibility(VISIBLE);
                break;
            case MODE_HOURLY:
                _blendedLayout.setVisibility(View.GONE);
                _devicesLayout.setVisibility(View.GONE);
                _fixedLayout.setVisibility(View.GONE);
                _hourlyLayout.setVisibility(View.VISIBLE);
                _payTypeTitleTextView.setVisibility(VISIBLE);
                break;
            case MODE_PER_DEVICE:
                _blendedLayout.setVisibility(View.GONE);
                _devicesLayout.setVisibility(View.VISIBLE);
                _fixedLayout.setVisibility(View.GONE);
                _hourlyLayout.setVisibility(View.GONE);
                _payTypeTitleTextView.setVisibility(VISIBLE);
                break;
            case MODE_BLENDED:
                _blendedLayout.setVisibility(View.VISIBLE);
                _devicesLayout.setVisibility(View.GONE);
                _fixedLayout.setVisibility(View.GONE);
                _hourlyLayout.setVisibility(View.GONE);
                _payTypeTitleTextView.setVisibility(VISIBLE);
                break;
            case -1:
                _blendedLayout.setVisibility(View.GONE);
                _devicesLayout.setVisibility(View.GONE);
                _fixedLayout.setVisibility(View.GONE);
                _hourlyLayout.setVisibility(View.GONE);
                _payTypeTitleTextView.setVisibility(GONE);


        }
    }

    private void populateUi() {
        setMode(_mode);
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
            _mode = -1;
        }
    };
}
