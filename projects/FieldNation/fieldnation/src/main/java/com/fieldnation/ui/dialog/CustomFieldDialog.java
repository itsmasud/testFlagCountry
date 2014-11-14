package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.CustomField;
import com.fieldnation.utils.misc;

/**
 * Created by michael.carver on 10/29/2014.
 */
public class CustomFieldDialog extends DialogFragmentBase {
    private static final String TAG = "ui.dialog.CustomFieldDialog";

    // State
    private static final String STATE_CUSTOM_FIELD = "CustomFieldDialog:STATE_CUSTOM_FIELD";

    // UI
    private EditText _textEditText;
    private Button _dateTimeButton;
    private Spinner _spinner;
    private RelativeLayout _tipLayout;
    private TextView _tipTextView;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private CustomField _customField;
    private Listener _listener;


    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static CustomFieldDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, CustomFieldDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        if (savedInstanceState != null) {
            //TODO if it doesn't contain this, then we are in an error state
            if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD))
                _customField = savedInstanceState.getParcelable(STATE_CUSTOM_FIELD);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_customField != null)
            outState.putParcelable(STATE_CUSTOM_FIELD, _customField);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_custom_field, container);

        _textEditText = (EditText) v.findViewById(R.id.text_edittext);
        Log.v(TAG, "onCreateView() _textEditText = " + _textEditText.toString());

        _dateTimeButton = (Button) v.findViewById(R.id.datetime_button);
        _dateTimeButton.setOnClickListener(_dateTime_onClick);

        _spinner = (Spinner) v.findViewById(R.id.spinner);

        _tipLayout = (RelativeLayout) v.findViewById(R.id.tip_layout);

        _tipTextView = (TextView) v.findViewById(R.id.tip_textview);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(CustomField customField) {
        _customField = customField;

        show();
    }

    private void populateUi() {
        if (_textEditText == null || _dateTimeButton == null || _spinner == null ||
                _tipLayout == null || _tipTextView == null || _customField == null)
            return;

        if (getDialog() != null) {
            getDialog().setTitle(_customField.getLabel());
        }

        CustomField.FieldType type = _customField.getFieldType();

        _textEditText.setVisibility(View.GONE);
        _dateTimeButton.setVisibility(View.GONE);
        _spinner.setVisibility(View.GONE);
        _tipLayout.setVisibility(View.GONE);

        if (!misc.isEmptyOrNull(_customField.getTip())) {
            _tipLayout.setVisibility(View.VISIBLE);
            if (!misc.isEmptyOrNull(_customField.getCustomFieldFormat())) {
                _tipTextView.setText(_customField.getTip() + "\n(Format: " + _customField.getCustomFieldFormat() + ")");
            } else {
                _tipTextView.setText(_customField.getTip());
            }
        }

        switch (type) {
            case DATE:
            case DATETIME:
            case TIME:
                //_dateTimeButton.setVisibility(View.VISIBLE);
            case TEXT:
            case NUMBER:
            case PHONE:
                _textEditText.setVisibility(View.VISIBLE);
                _textEditText.getEditableText().clear();
                //_textEditText.setText("", TextView.BufferType.EDITABLE);
                if (!misc.isEmptyOrNull(_customField.getValue())) {
                    _textEditText.setText(_customField.getValue(), TextView.BufferType.EDITABLE);
                }
                break;
            case LIST:
                _spinner.setVisibility(View.VISIBLE);
                if (_customField.getPredefinedValues() != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_dropdown_item,
                            _customField.getPredefinedValues());

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    _spinner.setAdapter(adapter);
                    if (!misc.isEmptyOrNull(_customField.getValue())) {
                        String val = _customField.getValue();
                        String[] values = _customField.getPredefinedValues();

                        for (int i = 0; i < values.length; i++) {
                            if (val.equals(values[i])) {
                                _spinner.setSelection(i);
                                break;
                            }
                        }
                    }
                }
                break;
        }
    }

    private View.OnClickListener _dateTime_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO need to get date/time
        }
    };

    private View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            switch (_customField.getFieldType()) {
                case LIST:
                    _listener.onOk(_customField, (String) _spinner.getSelectedItem());
                    break;
            }
            Log.v(TAG, "_ok_onClick _textEditText = " + _textEditText.toString());
            _listener.onOk(_customField, _textEditText.getText().toString());
        }
    };

    private View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };


    public interface Listener {
        public void onOk(CustomField field, String value);
    }
}
