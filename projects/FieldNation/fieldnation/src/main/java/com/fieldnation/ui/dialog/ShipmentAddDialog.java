package com.fieldnation.ui.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.R;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.fntools.misc;

public class ShipmentAddDialog extends DialogFragmentBase {
    private static final String TAG = "ShipmentAddDialog";

    // State
    private static final String STATE_TASKID = "STATE_TASKID";
    private static final String STATE_TITLE = "STATE_TITLE";
    private static final String STATE_CARRIER_SELECTION = "STATE_CARRIER_SELECTION";
    private static final String STATE_DIRECTION_SELECTION = "STATE_DIRECTION_SELECTION";

    private static final int RESULT_CODE_BARCODE_SCAN = 0;

    // UI
    private TextView _titleTextView;
    private EditText _trackingIdEditText;
    private Button _scanButton;
    private HintSpinner _carrierSpinner;
    private EditText _carrierEditText;
    private TextInputLayout _carrierLayout;
    private EditText _descriptionEditText;
    private HintSpinner _directionSpinner;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private Listener _listener;
    private String _title = null;
    private long _taskId = -1;
    private boolean _clear = false;
    private int _carrierPosition = -1;
    private int _directionPosition = -1;

    // Modes
    private static final int CARRIER_FEDEX = 0;
    private static final int CARRIER_UPS = 1;
    private static final int CARRIER_USPS = 2;
    private static final int CARRIER_OTHER = 3;

    private CharSequence[] _carriers = null;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public static ShipmentAddDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, ShipmentAddDialog.class);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult");
        if (requestCode == RESULT_CODE_BARCODE_SCAN) {
            if (resultCode == Activity.RESULT_OK) {
                Log.v(TAG, "requestCode");
                _trackingIdEditText.setText(data.getStringExtra("SCAN_RESULT"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_titleTextView != null && !misc.isEmptyOrNull(_titleTextView.getText().toString()))
            outState.putString(STATE_TITLE, _titleTextView.getText().toString());

        if (_taskId != -1)
            outState.putLong(STATE_TASKID, _taskId);

        if (_carrierPosition != -1)
            outState.putInt(STATE_CARRIER_SELECTION, _carrierPosition);

        if (_directionPosition != -1)
            outState.putInt(STATE_DIRECTION_SELECTION, _directionPosition);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_add_shipment, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);

        _trackingIdEditText = (EditText) v.findViewById(R.id.trackingid_edittext);
        _trackingIdEditText.setOnEditorActionListener(_onEditor);

        _scanButton = (Button) v.findViewById(R.id.scanBarcode_button);
        _scanButton.setOnClickListener(_scan_onClick);

        _carrierSpinner = (HintSpinner) v.findViewById(R.id.carrier_spinner);
        _carrierSpinner.setOnItemSelectedListener(_carrier_selected);

        _carrierEditText = (EditText) v.findViewById(R.id.carrier_edittext);
        _carrierEditText.setOnEditorActionListener(_onEditor);

        _carrierLayout = (TextInputLayout) v.findViewById(R.id.carrier_layout);

        _descriptionEditText = (EditText) v.findViewById(R.id.description_edittext);
        _descriptionEditText.setOnEditorActionListener(_onEditor);

        _directionSpinner = (HintSpinner) v.findViewById(R.id.direction_spinner);
        _directionSpinner.setOnItemSelectedListener(_direction_selected);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_okButton_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        _carriers = getResources().getTextArray(R.array.carrier_list);

        return v;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_TASKID))
                _taskId = savedInstanceState.getLong(STATE_TASKID);

            if (savedInstanceState.containsKey(STATE_TITLE)) {
                _title = savedInstanceState.getString(STATE_TITLE);
                _titleTextView.setText(_title);
            }

            if (savedInstanceState.containsKey(STATE_CARRIER_SELECTION)) {
                _carrierPosition = savedInstanceState.getInt(STATE_CARRIER_SELECTION);
                getCarrierSpinner().setSelection(_carrierPosition);
                switchCarrierTextEdit();
            } else {
                getCarrierSpinner().clearSelection();
            }

            if (savedInstanceState.containsKey(STATE_DIRECTION_SELECTION)) {
                _directionPosition = savedInstanceState.getInt(STATE_DIRECTION_SELECTION);
                getDirectionSpinner().setSelection(_directionPosition);
            } else {
                getDirectionSpinner().clearSelection();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (_title != null) {
            _titleTextView.setText(_title);
        }

        if (_clear) {
            _clear = false;
            getCarrierSpinner().setHint(getResources().getString(R.string.dialog_shipment_career_spinner_default_text));
            getCarrierSpinner().clearSelection();
            getDirectionSpinner().setHint(getResources().getString(R.string.dialog_shipment_direction_spinner_default_text));
            getDirectionSpinner().clearSelection();
            _directionPosition = -1;
            _carrierPosition = -1;
            _carrierEditText.setText("");
            _descriptionEditText.setText("");
            _trackingIdEditText.setText("");
        }

        populateUi();
    }

    public void show(CharSequence title, long taskId) {
        _title = (String) title;
        _taskId = taskId;
        _clear = true;
        show();
    }

    private void populateUi() {
        if (_cancelButton == null)
            return;

        switchCarrierTextEdit();

        getCarrierSpinner();
        getDirectionSpinner();
    }

    private void switchCarrierTextEdit() {
        switch (_carrierPosition) {
            case CARRIER_OTHER:
                _carrierLayout.setVisibility(View.VISIBLE);
                break;
            case CARRIER_FEDEX:
            case CARRIER_UPS:
            case CARRIER_USPS:
            default:
                _carrierLayout.setVisibility(View.GONE);
                break;
        }
    }

    public void setSelectedCarrier(final int carrierId) {
        _carrierPosition = carrierId;
        populateUi();
    }

    public HintSpinner getCarrierSpinner() {
        if (_carrierSpinner != null && _carrierSpinner.getAdapter() == null) {
            HintArrayAdapter adapter = HintArrayAdapter.createFromResources(
                    getActivity(),
                    R.array.carrier_list,
                    R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _carrierSpinner.setAdapter(adapter);
        }
        return _carrierSpinner;
    }

    public HintSpinner getDirectionSpinner() {
        if (_directionSpinner != null && _directionSpinner.getAdapter() == null) {
            HintArrayAdapter adapter = HintArrayAdapter.createFromResources(
                    getActivity(),
                    R.array.direction_list,
                    R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _directionSpinner.setAdapter(adapter);
        }
        return _directionSpinner;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _clear = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        _clear = true;
    }

    @Override
    public void dismiss() {
        _clear = true;
        super.dismiss();
    }


    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setTrackingId(String trackingId) {
        _trackingIdEditText.setText(trackingId);
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final TextView.OnEditorActionListener _onEditor = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;

            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (v == _trackingIdEditText) {
                    if (_carrierLayout.getVisibility() == View.VISIBLE) {
                        _carrierEditText.requestFocus();
                        handled = true;
                    } else {
                        _descriptionEditText.requestFocus();
                        handled = true;
                    }
                } else if (v == _carrierEditText) {
                    _descriptionEditText.requestFocus();
                    handled = true;
                }
            }

            return handled;
        }
    };

    private final AdapterView.OnItemSelectedListener _carrier_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _carrierPosition = position;
            switchCarrierTextEdit();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            _carrierPosition = -1;
            switchCarrierTextEdit();
        }
    };

    private final AdapterView.OnItemSelectedListener _direction_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _directionPosition = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            _directionPosition = -1;
        }
    };

    private final View.OnClickListener _okButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (misc.isEmptyOrNull(_trackingIdEditText.getText().toString())) {
                ToastClient.toast(App.get(), getString(R.string.toast_missing_traking_number), Toast.LENGTH_SHORT);
                return;
            }

            if (_carrierPosition == -1) {
                ToastClient.toast(App.get(), getString(R.string.toast_carrier_not_selected), Toast.LENGTH_SHORT);
                return;
            }

            if (misc.isEmptyOrNull(_descriptionEditText.getText().toString())) {
                ToastClient.toast(App.get(), getString(R.string.toast_missing_description), Toast.LENGTH_SHORT);
                return;
            }

            if (_directionPosition == -1) {
                ToastClient.toast(App.get(), getString(R.string.toast_direction_not_selected), Toast.LENGTH_SHORT);
                return;
            }

            if (_carrierPosition != CARRIER_OTHER && _carrierPosition != -1) {
                if (_carrierPosition == CARRIER_UPS && misc.getCarrierId(_trackingIdEditText.getText().toString()) != CARRIER_UPS) {
                    ToastClient.toast(App.get(), String.format(getString(R.string.toast_invalid_tracking_number), _carriers[_carrierPosition]), Toast.LENGTH_SHORT);
                    return;
                }
                if (_carrierPosition == CARRIER_FEDEX && misc.getCarrierId(_trackingIdEditText.getText().toString()) != CARRIER_FEDEX) {
                    ToastClient.toast(App.get(), String.format(getString(R.string.toast_invalid_tracking_number), _carriers[_carrierPosition]), Toast.LENGTH_SHORT);
                    return;
                }

                if (_carrierPosition == CARRIER_USPS && misc.getCarrierId(_trackingIdEditText.getText().toString()) != CARRIER_USPS) {
                    ToastClient.toast(App.get(), String.format(getString(R.string.toast_invalid_tracking_number), _carriers[_carrierPosition]), Toast.LENGTH_SHORT);
                    return;
                }
            } else {
                if (_carrierPosition == CARRIER_OTHER && misc.isEmptyOrNull(_carrierEditText.getText().toString())) {
                    ToastClient.toast(App.get(), getString(R.string.toast_missing_carrier_name), Toast.LENGTH_SHORT);
                    return;
                }
            }

            if (_listener != null) {
                if (_taskId != 0) {
                    if (_carrierPosition == CARRIER_OTHER) {
                        _listener.onOk(
                                _trackingIdEditText.getText().toString(),
                                "Other",
                                _carrierEditText.getText().toString(),
                                _descriptionEditText.getText().toString(),
                                _directionPosition == 0,
                                _taskId);
                    } else {
                        _listener.onOk(
                                _trackingIdEditText.getText().toString(),
                                _carriers[_carrierPosition].toString(),
                                null,
                                _descriptionEditText.getText().toString(),
                                _directionPosition == 0,
                                _taskId);
                    }

                } else {
                    if (_carrierPosition == CARRIER_OTHER) {
                        _listener.onOk(
                                _trackingIdEditText.getText().toString(),
                                "Other",
                                _carrierEditText.getText().toString(),
                                _descriptionEditText.getText().toString(),
                                _directionPosition == 0);
                    } else {
                        _listener.onOk(
                                _trackingIdEditText.getText().toString(),
                                _carriers[_carrierPosition].toString(),
                                null,
                                _descriptionEditText.getText().toString(),
                                _directionPosition == 0);
                    }
                }
            }
            ShipmentAddDialog.this.dismiss();
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private final View.OnClickListener _scan_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onScan();
        }
    };

    public interface Listener {
        void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite, long taskId);

        void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite);

        void onCancel();

        void onScan();
    }


}
