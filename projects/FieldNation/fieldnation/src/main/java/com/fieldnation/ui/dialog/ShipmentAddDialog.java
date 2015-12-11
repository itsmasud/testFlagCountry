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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.utils.misc;
import com.journeyapps.barcodescanner.CaptureActivity;

public class ShipmentAddDialog extends DialogFragmentBase {
    private static final String TAG = "ShipmentAddDialog";

    // State
    private static final String STATE_TASKID = "STATE_TASKID";
    private static final String STATE_TITLE = "STATE_TITLE";

    private static final int RESULT_CODE_BARCODE_SCAN = 0;

    // UI
    private TextView _titleTextView;
    private EditText _trackingIdEditText;
    private Button _scanButton;
    private Spinner _carrierSpinner;
    private EditText _carrierEditText;
    private TextInputLayout _carrierLayout;
    private EditText _descriptionEditText;
    private Spinner _directionSpinner;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private Listener _listener;
    private long _taskId = 0;
    private String _title;
    private boolean _clear = false;
    private ArrayAdapter<CharSequence> _carrierAdapter;
    private ArrayAdapter<CharSequence> _directionAdapter;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public static ShipmentAddDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, ShipmentAddDialog.class);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult");
        if (requestCode == RESULT_CODE_BARCODE_SCAN) {
            if (resultCode == Activity.RESULT_OK) {
                Log.v(TAG, "requestCode");
                _trackingIdEditText.setText(data.getStringExtra("SCAN_RESULT"));
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_TASKID))
                _taskId = savedInstanceState.getLong(STATE_TASKID);

            if (savedInstanceState.containsKey(STATE_TITLE))
                _title = savedInstanceState.getString(STATE_TITLE);
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

        _carrierSpinner = (Spinner) v.findViewById(R.id.carrier_spinner);
        _carrierSpinner.setOnItemSelectedListener(_carrier_selected);

        _carrierAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.carrier_list,
                R.layout.view_spinner_item);

        _carrierAdapter.setDropDownViewResource(
                android.support.design.R.layout.support_simple_spinner_dropdown_item);

        _carrierSpinner.setAdapter(_carrierAdapter);

        _carrierEditText = (EditText) v.findViewById(R.id.carrier_edittext);
        _carrierEditText.setOnEditorActionListener(_onEditor);

        _carrierLayout = (TextInputLayout) v.findViewById(R.id.carrier_layout);

        _descriptionEditText = (EditText) v.findViewById(R.id.description_edittext);
        _descriptionEditText.setOnEditorActionListener(_onEditor);

        _directionSpinner = (Spinner) v.findViewById(R.id.direction_spinner);

        _directionAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.direction_list,
                R.layout.view_spinner_item);

        _directionAdapter.setDropDownViewResource(
                android.support.design.R.layout.support_simple_spinner_dropdown_item);

        _directionSpinner.setAdapter(_directionAdapter);


        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_okButton_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (_title != null)
            _titleTextView.setText(_title);

        if (_clear) {
            _clear = false;
            _carrierEditText.setText("");
            _descriptionEditText.setText("");
            _trackingIdEditText.setText("");
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }


    public void setListener(Listener listener) {
        _listener = listener;
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

    private final AdapterView.OnItemSelectedListener _carrier_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if ("Other".equals(_carrierSpinner.getSelectedItem().toString())) {
                _carrierLayout.setVisibility(View.VISIBLE);
            } else {
                _carrierLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final View.OnClickListener _okButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (misc.isEmptyOrNull(_trackingIdEditText.getText().toString())) {
                ToastClient.toast(App.get(), "Missing tracking number", Toast.LENGTH_SHORT);
                return;
            }

            if (misc.isEmptyOrNull(_descriptionEditText.getText().toString())) {
                ToastClient.toast(App.get(), "Missing description", Toast.LENGTH_SHORT);
                return;
            }

            if (_listener != null) {
                if (_taskId != 0) {
                    if ("Other".equals(_carrierSpinner.getSelectedItem().toString())) {
                        _listener.onOk(
                                _trackingIdEditText.getText().toString(),
                                "Other",
                                _carrierEditText.getText().toString(),
                                _descriptionEditText.getText().toString(),
                                _directionSpinner.getSelectedItemPosition() == 0,
                                _taskId);
                    } else {
                        _listener.onOk(
                                _trackingIdEditText.getText().toString(),
                                _carrierSpinner.getSelectedItem().toString(),
                                null,
                                _descriptionEditText.getText().toString(),
                                _directionSpinner.getSelectedItemPosition() == 0,
                                _taskId);
                    }

                } else {
                    if ("Other".equals(_carrierSpinner.getSelectedItem().toString())) {
                        _listener.onOk(
                                _trackingIdEditText.getText().toString(),
                                "Other",
                                _carrierEditText.getText().toString(),
                                _descriptionEditText.getText().toString(),
                                _directionSpinner.getSelectedItemPosition() == 0);
                    } else {
                        _listener.onOk(
                                _trackingIdEditText.getText().toString(),
                                _carrierSpinner.getSelectedItem().toString(),
                                null,
                                _descriptionEditText.getText().toString(),
                                _directionSpinner.getSelectedItemPosition() == 0);
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
            Intent intent = new Intent(getActivity(), CaptureActivity.class);
            intent.setAction("com.google.zxing.client.android.SCAN");
            intent.putExtra("SAVE_HISTORY", false);
            startActivityForResult(intent, RESULT_CODE_BARCODE_SCAN);
        }
    };


    public interface Listener {
        void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite, long taskId);

        void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite);

        void onCancel();
    }


}
