package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.fieldnation.R;

public class ShipmentAddDialog extends DialogFragmentBase {
    private static final String TAG = "ui.dialog.ShipmentAddDialog";

    // State
    private static final String STATE_TASKID = "STATE_TASKID";
    private static final String STATE_TITLE = "STATE_TITLE";

    // UI
    private TextView _titleTextView;
    private EditText _trackingIdEditText;
    private Spinner _carrierSpinner;
    private ArrayAdapter<CharSequence> _carrierAdapter;
    private EditText _carrierEditText;
    private EditText _descriptionEditText;
    private RadioButton _shipToSiteRadio;
    private Button _okButton;
    private Button _cancelButton;
    private LinearLayout _carrierNameLayout;

    // Data
    private Listener _listener;
    private long _taskId = 0;
    private String _title;
    private boolean _clear = false;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public static ShipmentAddDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, ShipmentAddDialog.class);
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

        _carrierSpinner = (Spinner) v.findViewById(R.id.carrier_spinner);
        _carrierSpinner.setOnItemSelectedListener(_carrier_selected);

        _carrierAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.carrier_list,
                android.R.layout.simple_spinner_item);
        _carrierAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _carrierSpinner.setAdapter(_carrierAdapter);

        _carrierNameLayout = (LinearLayout) v.findViewById(R.id.carriername_layout);
        _carrierEditText = (EditText) v.findViewById(R.id.carrier_edittext);
        _carrierEditText.setOnEditorActionListener(_onEditor);
        _descriptionEditText = (EditText) v.findViewById(R.id.description_edittext);
        _descriptionEditText.setOnEditorActionListener(_onEditor);
        _shipToSiteRadio = (RadioButton) v.findViewById(R.id.shiptosite_radio);

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

    public void show(int titleResId, long taskId) {
        show(getActivity().getText(titleResId), taskId);
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
    private TextView.OnEditorActionListener _onEditor = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;

            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (v == _trackingIdEditText) {
                    if (_carrierNameLayout.getVisibility() == View.VISIBLE) {
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

    private AdapterView.OnItemSelectedListener _carrier_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if ("Other".equals(_carrierSpinner.getSelectedItem().toString())) {
                _carrierNameLayout.setVisibility(View.VISIBLE);
            } else {
                _carrierNameLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private View.OnClickListener _okButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO validate input
            if (_listener != null) {
                if (_taskId != 0) {
                    if ("Other".equals(_carrierSpinner.getSelectedItem().toString())) {
                        _listener.onOk(
                                _trackingIdEditText.getText().toString(),
                                "Other",
                                _carrierEditText.getText().toString(),
                                _descriptionEditText.getText().toString(),
                                _shipToSiteRadio.isChecked(),
                                _taskId);
                    } else {
                        _listener.onOk(
                                _trackingIdEditText.getText().toString(),
                                _carrierSpinner.getSelectedItem().toString(),
                                null,
                                _descriptionEditText.getText().toString(),
                                _shipToSiteRadio.isChecked(),
                                _taskId);
                    }

                } else {
                    if ("Other".equals(_carrierSpinner.getSelectedItem().toString())) {
                        _listener.onOk(
                                _trackingIdEditText.getText().toString(),
                                "Other",
                                _carrierEditText.getText().toString(),
                                _descriptionEditText.getText().toString(),
                                _shipToSiteRadio.isChecked());
                    } else {
                        _listener.onOk(
                                _trackingIdEditText.getText().toString(),
                                _carrierSpinner.getSelectedItem().toString(),
                                null,
                                _descriptionEditText.getText().toString(),
                                _shipToSiteRadio.isChecked());
                    }
                }
            }
            ShipmentAddDialog.this.dismiss();
        }
    };

    private View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    public interface Listener {
        public void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite, long taskId);

        public void onOk(String trackingId, String carrier, String carrierName, String description, boolean shipToSite);

        public void onCancel();
    }


}
