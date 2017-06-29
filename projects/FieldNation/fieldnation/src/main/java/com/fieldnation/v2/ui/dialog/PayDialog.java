package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.PayAdditional;
import com.fieldnation.v2.data.model.PayBase;

public class PayDialog extends FullScreenDialog {
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
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;
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

    // Data
    private Pay _pay;
    private boolean _showExplanation;
    private int _mode = MODE_FIXED;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public PayDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_pay, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.inflateMenu(R.menu.dialog);
        _toolbar.setTitle(R.string.change_pay);

        _finishMenu = _toolbar.findViewById(R.id.primary_menu);
        //_finishMenu.setTitle(App.get().getString(R.string.btn_submit));
        _finishMenu.setText(R.string.btn_ok);

        _typeSpinner = v.findViewById(R.id.type_spinner);

        // fixed
        _fixedLayout = v.findViewById(R.id.fixed_layout);
        _fixedEditText = v.findViewById(R.id.fixed_edittext);

        // hourly
        _hourlyLayout = v.findViewById(R.id.hourly_layout);
        _hourlyRateEditText = v.findViewById(R.id.hourlyrate_edittext);
        _maxHoursEditText = v.findViewById(R.id.maxhours_edittext);

        // per device
        _devicesLayout = v.findViewById(R.id.devices_layout);
        _deviceRateEditText = v.findViewById(R.id.devicerate_edittext);
        _maxDevicesEditText = v.findViewById(R.id.maxdevices_edittext);

        // blended
        _blendedLayout = v.findViewById(R.id.blended_layout);
        _blendedFixedRateEditText = v.findViewById(R.id.blendedFixedRate_edittext);
        _blendedFixedMaxHoursEditText = v.findViewById(R.id.blendedFixedMaxHours_edittext);
        _extraHourlyEditText = v.findViewById(R.id.extrahours_edittext);
        _extraMaxHoursEditText = v.findViewById(R.id.extramaxhours_edittext);

        _explanationLayout = v.findViewById(R.id.explanation_layout);
        _explanationEditText = v.findViewById(R.id.explanation_edittext);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _typeSpinner.setOnItemSelectedListener(_type_selected);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        _pay = payload.getParcelable("pay");
        _showExplanation = payload.getBoolean("showExplanation");

        super.show(payload, animate);

        populateUi();
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState != null) {
            if (savedState.containsKey(STATE_MODE))
                _mode = savedState.getInt(STATE_MODE);

            if (savedState.containsKey(STATE_SHOW_EXPLANATION))
                _showExplanation = savedState.getBoolean(STATE_SHOW_EXPLANATION);

            if (savedState.containsKey(STATE_EXPLANATION))
                _explanationEditText.setText(savedState.getString(STATE_EXPLANATION));

            if (savedState.containsKey(STATE_FIXED))
                _fixedEditText.setText(savedState.getString(STATE_FIXED));

            if (savedState.containsKey(STATE_HOURLY_RATE))
                _hourlyRateEditText.setText(savedState.getString(STATE_HOURLY_RATE));

            if (savedState.containsKey(STATE_HOURLY_MAX))
                _maxHoursEditText.setText(savedState.getString(STATE_HOURLY_MAX));

            if (savedState.containsKey(STATE_DEVICES_RATE))
                _deviceRateEditText.setText(savedState.getString(STATE_DEVICES_RATE));

            if (savedState.containsKey(STATE_DEVICES_MAX))
                _maxDevicesEditText.setText(savedState.getString(STATE_DEVICES_MAX));

            if (savedState.containsKey(STATE_BLENDED_HOURLY))
                _blendedFixedRateEditText.setText(savedState.getString(STATE_BLENDED_HOURLY));

            if (savedState.containsKey(STATE_BLENDED_HOURLY_MAX))
                _blendedFixedMaxHoursEditText.setText(savedState.getString(STATE_BLENDED_HOURLY_MAX));

            if (savedState.containsKey(STATE_BLENDED_XHOURLY))
                _extraHourlyEditText.setText(savedState.getString(STATE_BLENDED_XHOURLY));

            if (savedState.containsKey(STATE_BLENDED_XHOURLY_MAX))
                _extraMaxHoursEditText.setText(savedState.getString(STATE_BLENDED_XHOURLY_MAX));
        }
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
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
                    return blendedFixedRate + (extraHourly * extraMaxHours) >= MINIMUM_ACCUMULATED_PAYABLE_AMOUNT;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    private Pay makePay() {
        Pay pay = new Pay();
        try {
            switch (_mode) {
                case MODE_FIXED:
                    pay.setType(Pay.TypeEnum.FIXED);
                    pay.setBase(new PayBase()
                            .amount(Double.parseDouble(_fixedEditText.getText().toString()))
                            .units(1.0));
                    break;
                case MODE_HOURLY:
                    pay.setType(Pay.TypeEnum.HOURLY);
                    pay.setBase(new PayBase()
                            .amount(Double.parseDouble(_hourlyRateEditText.getText().toString()))
                            .units(Double.parseDouble(_maxHoursEditText.getText().toString())));
                    break;
                case MODE_PER_DEVICE:
                    pay.setType(Pay.TypeEnum.DEVICE);
                    pay.setBase(new PayBase()
                            .amount(Double.parseDouble(_deviceRateEditText.getText().toString()))
                            .units(Double.parseDouble(_maxDevicesEditText.getText().toString())));
                    break;
                case MODE_BLENDED:
                    pay.setType(Pay.TypeEnum.BLENDED);
                    pay.setBase(new PayBase()
                            .amount(Double.parseDouble(_blendedFixedRateEditText.getText().toString()))
                            .units(Double.parseDouble(_blendedFixedMaxHoursEditText.getText().toString())));
                    pay.setAdditional(new PayAdditional()
                            .amount(Double.parseDouble(_extraHourlyEditText.getText().toString()))
                            .units(Double.parseDouble(_extraMaxHoursEditText.getText().toString())));
                    break;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
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

        switch (_pay.getType()) {
            case BLENDED:
                setMode(MODE_BLENDED);
                _blendedFixedRateEditText.setText(_pay.getBase().getAmount() + "");
                _blendedFixedMaxHoursEditText.setText(_pay.getBase().getUnits() + "");
                _extraHourlyEditText.setText(_pay.getAdditional().getAmount() + "");
                _extraMaxHoursEditText.setText(_pay.getAdditional().getUnits() + "");
                _blendedFixedRateEditText.requestFocus();
                break;
            case DEVICE:
                setMode(MODE_PER_DEVICE);
                _deviceRateEditText.setText((_pay.getBase().getAmount() + "").trim());
                _maxDevicesEditText.setText((_pay.getBase().getUnits() + "").trim());
                _deviceRateEditText.requestFocus();
                break;
            case FIXED:
                setMode(MODE_FIXED);
                _fixedEditText.setText((_pay.getBase().getAmount() + "").trim());
                _fixedEditText.requestFocus();
                break;
            case HOURLY:
                setMode(MODE_HOURLY);
                _hourlyRateEditText.setText((_pay.getBase().getAmount() + "").trim());
                _maxHoursEditText.setText((_pay.getBase().getUnits() + "").trim());
                _hourlyRateEditText.requestFocus();
                break;
        }
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

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss(true);
            _onNothingDispatcher.dispatch(getUid());
        }
    };

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (!isValidAmount()) {
                ToastClient.toast(App.get(), App.get().getString(R.string.toast_minimum_accumulated_payable_amount), Toast.LENGTH_SHORT);
                return false;
            }

            try {
                _onCompleteDispatcher.dispatch(getUid(), makePay(), _explanationEditText.getText().toString());
            } catch (Exception ex) {
                ToastClient.toast(App.get(), R.string.please_enter_a_value_greater_than, Toast.LENGTH_SHORT);
                return false;
            }
            dismiss(true);
            return true;
        }
    };

    public static void show(Context context, String uid, Pay pay, boolean showExplanation) {
        Bundle params = new Bundle();
        params.putParcelable("pay", pay);
        params.putBoolean("showExplanation", showExplanation);

        Controller.show(context, uid, PayDialog.class, params);
    }

    /*-****************************-*/
    /*-         Complete           -*/
    /*-****************************-*/
    public interface OnCompleteListener {
        void onComplete(Pay pay, String explanation);
    }

    private static KeyedDispatcher<OnCompleteListener> _onCompleteDispatcher = new KeyedDispatcher<OnCompleteListener>() {
        @Override
        public void onDispatch(OnCompleteListener listener, Object... parameters) {
            listener.onComplete((Pay) parameters[0], (String) parameters[1]);
        }
    };

    public static void addOnCompleteListener(String uid, OnCompleteListener onCompleteListener) {
        _onCompleteDispatcher.add(uid, onCompleteListener);
    }

    public static void removeOnCompleteListener(String uid, OnCompleteListener onCompleteListener) {
        _onCompleteDispatcher.remove(uid, onCompleteListener);
    }

    public static void removeAllOnCompleteListener(String uid) {
        _onCompleteDispatcher.removeAll(uid);
    }

    /*-***************************-*/
    /*-         Nothing           -*/
    /*-***************************-*/
    public interface OnNothingListener {
        void onNothing();
    }

    private static KeyedDispatcher<OnNothingListener> _onNothingDispatcher = new KeyedDispatcher<OnNothingListener>() {
        @Override
        public void onDispatch(OnNothingListener listener, Object... parameters) {
            listener.onNothing();
        }
    };

    public static void addOnNothingListener(String uid, OnNothingListener onNothingListener) {
        _onNothingDispatcher.add(uid, onNothingListener);
    }

    public static void removeOnNothingListener(String uid, OnNothingListener onNothingListener) {
        _onNothingDispatcher.remove(uid, onNothingListener);
    }

    public static void removeAllOnNothingListener(String uid) {
        _onNothingDispatcher.removeAll(uid);
    }
}
