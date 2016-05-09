package com.fieldnation.ui.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.ui.FnSpinner;
import com.fieldnation.utils.misc;

public class ShipmentAddDialog extends DialogFragmentBase {
    private static final String TAG = "ShipmentAddDialog";

    // State
    private static final String STATE_TASKID = "STATE_TASKID";
    private static final String STATE_TITLE = "STATE_TITLE";
    private static final String STATE_CAREER_SELECTION = "STATE_CAREER_SELECTION";
    private static final String STATE_DIRECTION_SELECTION = "STATE_DIRECTION_SELECTION";

    private static final int RESULT_CODE_BARCODE_SCAN = 0;

    // UI
    private TextView _titleTextView;
    private EditText _trackingIdEditText;
    private Button _scanButton;
    private FnSpinner _carrierSpinner;
    private EditText _carrierEditText;
    private TextInputLayout _carrierLayout;
    private EditText _descriptionEditText;
    private FnSpinner _directionSpinner;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private Listener _listener;
    private long _taskId = 0;
    private String _title;
    private boolean _clear = false;
    private int _selectedPosition_careerSpinner = -1;
    private int _selectedPosition_directionSpinner = -1;

    // Modes
    private static final int CARRIER_FEDEX = 0;
    private static final int CARRIER_UPS = 1;
    private static final int CARRIER_USPS = 2;
    private static final int CARRIER_OTHER = 3;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public static ShipmentAddDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, ShipmentAddDialog.class);
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
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_TASKID))
                _taskId = savedInstanceState.getLong(STATE_TASKID);

            if (savedInstanceState.containsKey(STATE_TITLE))
                _title = savedInstanceState.getString(STATE_TITLE);

            if (savedInstanceState.containsKey(STATE_CAREER_SELECTION))
                _selectedPosition_careerSpinner = savedInstanceState.getInt(STATE_CAREER_SELECTION);

            if (savedInstanceState.containsKey(STATE_DIRECTION_SELECTION))
                _selectedPosition_directionSpinner = savedInstanceState.getInt(STATE_DIRECTION_SELECTION);

        }
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_title != null)
            outState.putString(STATE_TITLE, _title);

        if (_taskId != 0)
            outState.putLong(STATE_TASKID, _taskId);

        if (_selectedPosition_careerSpinner != -1)
            outState.putInt(STATE_CAREER_SELECTION, _selectedPosition_careerSpinner);

        if (_selectedPosition_directionSpinner != -1)
            outState.putInt(STATE_DIRECTION_SELECTION, _selectedPosition_directionSpinner);

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

        _carrierSpinner = (FnSpinner) v.findViewById(R.id.carrier_spinner);
        _carrierSpinner.setOnItemClickListener(_carrier_selected);

        _carrierEditText = (EditText) v.findViewById(R.id.carrier_edittext);
        _carrierEditText.setOnEditorActionListener(_onEditor);

        _carrierLayout = (TextInputLayout) v.findViewById(R.id.carrier_layout);

        _descriptionEditText = (EditText) v.findViewById(R.id.description_edittext);
        _descriptionEditText.setOnEditorActionListener(_onEditor);

        _directionSpinner = (FnSpinner) v.findViewById(R.id.direction_spinner);
        _directionSpinner.setOnItemClickListener(_direction_selected);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_okButton_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        return v;
    }

    private void setCarieerSelection(final int selectedCarrier) {
        _selectedPosition_careerSpinner = selectedCarrier;
        _carrierSpinner.setListSelection(_selectedPosition_careerSpinner);

        switch (selectedCarrier) {
            case CARRIER_FEDEX:
                _carrierLayout.setVisibility(View.GONE);
                break;
            case CARRIER_UPS:
                _carrierLayout.setVisibility(View.GONE);
                break;
            case CARRIER_USPS:
                _carrierLayout.setVisibility(View.GONE);
                break;
            case CARRIER_OTHER:
                _carrierLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void populateSpinners() {
        getCarrierSpinner();
        getDirectionSpinner();
    }

    public FnSpinner getCarrierSpinner() {
        if (_carrierSpinner != null && _carrierSpinner.getAdapter() == null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.carrier_list,
                    R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _carrierSpinner.setAdapter(adapter);
        }
        return _carrierSpinner;
    }

    public FnSpinner getDirectionSpinner() {
        if (_directionSpinner != null && _directionSpinner.getAdapter() == null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.direction_list,
                    R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _directionSpinner.setAdapter(adapter);
        }

        return _directionSpinner;
    }

    @Override
    public void onResume() {
        super.onResume();

        populateSpinners();

        if (_title != null)
            _titleTextView.setText(_title);

        if (_clear) {
            _clear = false;
            _carrierSpinner.setText(getResources().getString(R.string.dialog_shipment_career_spinner_default_text));
            _directionSpinner.setText(getResources().getString(R.string.dialog_shipment_direction_spinner_default_text));
            _carrierEditText.setText("");
            _descriptionEditText.setText("");
            _trackingIdEditText.setText("");
        } else if (_selectedPosition_careerSpinner != -1) {
            _carrier_selected.onItemClick(null, null, _selectedPosition_careerSpinner, 0);
        }
        }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.v(TAG, "onDestroy");
        _clear = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
//        Log.v(TAG, "onDismiss");
        _clear = true;
    }

    @Override
    public void dismiss() {
//        Log.v(TAG, "dismiss");
        _clear = true;
        super.dismiss();
    }


    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setTrackingId(String trackingId) {
        _trackingIdEditText.setText(trackingId);
    }

    public void setSelectedCarrier(final String carrierName) {
        try {
            for (int i = 0; i < getCarrierSpinner().getAdapter().getCount(); i++) {
                if (getCarrierSpinner().getAdapter().getItem(i).equals(carrierName)) {
                    _carrier_selected.onItemClick(null, null, i, 0);
                    break;
                }
            }
        } catch (Exception ex) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setSelectedCarrier(carrierName);
                }
            }, 100);
        }
    }

    public void show(CharSequence title, long taskId) {
        _title = (String) title;
        _taskId = taskId;
        _clear = true;
        show();
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

    private final AdapterView.OnItemClickListener _carrier_selected = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            setCarieerSelection(position);
        }

    };

    private final AdapterView.OnItemClickListener _direction_selected = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            _selectedPosition_directionSpinner = position;
        }
    };

    private final View.OnClickListener _okButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (misc.isEmptyOrNull(_trackingIdEditText.getText().toString())) {
                ToastClient.toast(App.get(), getString(R.string.toast_missing_traking_number), Toast.LENGTH_SHORT);
                return;
            }

            if (getString(R.string.dialog_shipment_career_spinner_default_text).equals(_carrierSpinner.getText().toString())) {
                ToastClient.toast(App.get(), getString(R.string.toast_carrier_not_selected), Toast.LENGTH_SHORT);
                return;
            }

            if (misc.isEmptyOrNull(_descriptionEditText.getText().toString())) {
                ToastClient.toast(App.get(), getString(R.string.toast_missing_description), Toast.LENGTH_SHORT);
                return;
            }

            if (getString(R.string.dialog_shipment_direction_spinner_default_text).equals(_directionSpinner.getText().toString())) {
                ToastClient.toast(App.get(), getString(R.string.toast_direction_not_selected), Toast.LENGTH_SHORT);
                return;
            }

            if (!"Other".equals(getCarrierSpinner().getText().toString())) {
                final String carrier = _carrierSpinner.getText().toString();
                if ("UPS".equals(carrier) && !"UPS".equals(misc.getCarrierName(_trackingIdEditText.getText().toString()))) {
                    ToastClient.toast(App.get(), String.format(getString(R.string.toast_invalid_tracking_number), carrier), Toast.LENGTH_SHORT);
                    return;
                }
                if ("Fedex".equals(carrier) && !"Fedex".equals(misc.getCarrierName(_trackingIdEditText.getText().toString()))) {
                    ToastClient.toast(App.get(), String.format(getString(R.string.toast_invalid_tracking_number), carrier), Toast.LENGTH_SHORT);
                    return;
                }

                if ("USPS".equals(carrier) && !"USPS".equals(misc.getCarrierName(_trackingIdEditText.getText().toString()))) {
                    ToastClient.toast(App.get(), String.format(getString(R.string.toast_invalid_tracking_number), carrier), Toast.LENGTH_SHORT);
                    return;
                }
            } else {
                if (misc.isEmptyOrNull(_carrierEditText.getText().toString())) {
                    ToastClient.toast(App.get(), getString(R.string.toast_missing_carrier_name), Toast.LENGTH_SHORT);
                    return;
                }
            }

            if (_listener != null) {
                if (_taskId != 0) {
                    if ("Other".equals(getCarrierSpinner().getText().toString())) {
                        _listener.onOk(
                                _trackingIdEditText.getText().toString(),
                                "Other",
                                _carrierEditText.getText().toString(),
                                _descriptionEditText.getText().toString(),
                                _selectedPosition_directionSpinner == 0,
                                _taskId);
                    } else {
                        _listener.onOk(
                                _trackingIdEditText.getText().toString(),
                                getCarrierSpinner().getText().toString(),
                                null,
                                _descriptionEditText.getText().toString(),
                                _selectedPosition_directionSpinner == 0,
                                _taskId);
                    }

                } else {
                    if ("Other".equals(getCarrierSpinner().getText().toString())) {
                        _listener.onOk(
                                _trackingIdEditText.getText().toString(),
                                "Other",
                                _carrierEditText.getText().toString(),
                                _descriptionEditText.getText().toString(),
                                _selectedPosition_directionSpinner == 0);
                    } else {
                        _listener.onOk(
                                _trackingIdEditText.getText().toString(),
                                getCarrierSpinner().getText().toString(),
                                null,
                                _descriptionEditText.getText().toString(),
                                _selectedPosition_directionSpinner == 0);
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
