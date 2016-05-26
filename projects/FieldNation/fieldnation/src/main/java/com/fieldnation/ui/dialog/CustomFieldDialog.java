package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.CustomField;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.utils.DateUtils;
import com.fieldnation.utils.misc;

import java.util.Calendar;

/**
 * Created by michael.carver on 10/29/2014.
 */
public class CustomFieldDialog extends DialogFragmentBase {
    private static final String TAG = "CustomFieldDialog";

    // State
    private static final String STATE_CUSTOM_FIELD = "CustomFieldDialog:STATE_CUSTOM_FIELD";
    private static final String STATE_CUSTOM_FIELD_DATE = "CustomFieldDialog:STATE_CUSTOM_FIELD_DATE";
    private static final String STATE_CUSTOM_FIELD_DATETIME = "CustomFieldDialog:STATE_CUSTOM_FIELD_DATETIME";
    private static final String STATE_CUSTOM_FIELD_TIME = "CustomFieldDialog:STATE_CUSTOM_FIELD_TIME";
    private static final String STATE_CUSTOM_FIELD_TEXT = "CustomFieldDialog:STATE_CUSTOM_FIELD_TEXT";
    private static final String STATE_CUSTOM_FIELD_NUMBER = "CustomFieldDialog:STATE_CUSTOM_FIELD_NUMBER";
    private static final String STATE_CUSTOM_FIELD_PHONE_NUMBER = "CustomFieldDialog:STATE_CUSTOM_FIELD_PHONE_NUMBER";

    // UI
    private TextView _titleTextView;
    private EditText _textEditText;
    private Button _dateTimeButton;
    private LinearLayout _spinnerLayout;
    private HintSpinner _spinner;
    private TextView _tipTextView;
    private Button _okButton;
    private Button _cancelButton;

    // Dialogs
    private DatePickerDialog _datePicker;
    private TimePickerDialog _timePicker;

    // Data
    private CustomField _customField;
    private Listener _listener;
    private Calendar _pickerCal;
    private Calendar _expirationDate;
    private int _itemSelectedPosition;
    private String _customFieldDateData;
    private String _customFieldDateTimeData;
    private String _customFieldTimeData;
    private String _customFieldTextData;
    private String _customFieldNumberData;
    private String _customFieldPhoneNumberData;
    private boolean _clear = false;


    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static CustomFieldDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, CustomFieldDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        //setRetainInstance(true);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD))
                _customField = savedInstanceState.getParcelable(STATE_CUSTOM_FIELD);

            if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD_DATE))
                _customFieldDateData = savedInstanceState.getString(STATE_CUSTOM_FIELD_DATE);

            if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD_DATETIME))
                _customFieldDateTimeData = savedInstanceState.getString(STATE_CUSTOM_FIELD_DATETIME);

            if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD_TIME))
                _customFieldTimeData = savedInstanceState.getString(STATE_CUSTOM_FIELD_TIME);

            if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD_TEXT))
                _customFieldTextData = savedInstanceState.getString(STATE_CUSTOM_FIELD_TEXT);

            if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD_NUMBER))
                _customFieldNumberData = savedInstanceState.getString(STATE_CUSTOM_FIELD_NUMBER);

            if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD_PHONE_NUMBER))
                _customFieldPhoneNumberData = savedInstanceState.getString(STATE_CUSTOM_FIELD_PHONE_NUMBER);


        }
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onViewStateRestored");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD)) {
                _customField = savedInstanceState.getParcelable(STATE_CUSTOM_FIELD);
            }


            CustomField.FieldType type = _customField.getFieldType();
            switch (type) {
                case DATE:
                    if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD_DATE)) {
                        _customFieldDateData = savedInstanceState.getString(STATE_CUSTOM_FIELD_DATE);
                        _textEditText.setText(_customFieldDateData);
                    }
                    break;
                case DATETIME:
                    if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD_DATETIME)) {
                        _customFieldDateTimeData = savedInstanceState.getString(STATE_CUSTOM_FIELD_DATETIME);
                        _textEditText.setText(_customFieldDateTimeData);
                    }
                    break;
                case TIME:
                    if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD_TIME)) {
                        _customFieldTimeData = savedInstanceState.getString(STATE_CUSTOM_FIELD_TIME);
                        _textEditText.setText(_customFieldTimeData);
                    }
                    break;
                case TEXT:
                    if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD_TEXT)) {
                        _customFieldTextData = savedInstanceState.getString(STATE_CUSTOM_FIELD_TEXT);
                        _textEditText.setText(_customFieldTextData);
                    }
                    break;
                case NUMBER:
                    Log.e(TAG, "onViewStateRestored: case NUMBER");

                    if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD_NUMBER)) {
                        _customFieldNumberData = savedInstanceState.getString(STATE_CUSTOM_FIELD_NUMBER);
                        _textEditText.setText(_customFieldNumberData);
                    }
                    break;
                case PHONE:
                    if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD_PHONE_NUMBER)) {
                        _customFieldTextData = savedInstanceState.getString(STATE_CUSTOM_FIELD_PHONE_NUMBER);
                        _textEditText.setText(_customFieldPhoneNumberData);
                    }
                    break;
            }

        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        if (_customField != null)
            outState.putParcelable(STATE_CUSTOM_FIELD, _customField);

        CustomField.FieldType type = _customField.getFieldType();
        switch (type) {
            case DATE:
                if (_textEditText != null && !misc.isEmptyOrNull(_textEditText.getText().toString())) {
                    _customFieldDateData = _textEditText.getText().toString();
                    outState.putString(STATE_CUSTOM_FIELD_DATE, _customFieldDateData);
                }
                break;
            case DATETIME:
                if (_textEditText != null && !misc.isEmptyOrNull(_textEditText.getText().toString())) {
                    _customFieldDateTimeData = _textEditText.getText().toString();
                    outState.putString(STATE_CUSTOM_FIELD_DATETIME, _customFieldDateTimeData);
                }
                break;
            case TIME:
                if (_textEditText != null && !misc.isEmptyOrNull(_textEditText.getText().toString())) {
                    _customFieldTimeData = _textEditText.getText().toString();
                    outState.putString(STATE_CUSTOM_FIELD_TIME, _customFieldTimeData);
                }
                break;
            case TEXT:
                if (_textEditText != null && !misc.isEmptyOrNull(_textEditText.getText().toString())) {
                    _customFieldTextData = _textEditText.getText().toString();
                    outState.putString(STATE_CUSTOM_FIELD_TEXT, _customFieldTextData);
                }
                break;
            case NUMBER:
                if (_textEditText != null && !misc.isEmptyOrNull(_textEditText.getText().toString())) {
                    _customFieldNumberData = _textEditText.getText().toString();
                    outState.putString(STATE_CUSTOM_FIELD_NUMBER, _customFieldNumberData);
                }
                break;
            case PHONE:
                if (_textEditText != null && !misc.isEmptyOrNull(_textEditText.getText().toString())) {
                    _customFieldPhoneNumberData = _textEditText.getText().toString();
                    outState.putString(STATE_CUSTOM_FIELD_PHONE_NUMBER, _customFieldPhoneNumberData);
                }
                break;
        }
//        Log.e(TAG, "_customFieldTextData: " + _customFieldTextData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_custom_field, container);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);

        _textEditText = (EditText) v.findViewById(R.id.text_edittext);
        _textEditText.addTextChangedListener(_textEditText_watcherListener);

        _dateTimeButton = (Button) v.findViewById(R.id.datetime_button);
        _dateTimeButton.setOnClickListener(_dateTime_onClick);

        _spinnerLayout = (LinearLayout) v.findViewById(R.id.spinner_layout);
        _spinner = (HintSpinner) v.findViewById(R.id.spinner);
        _spinner.setOnItemSelectedListener(_spinner_selected);

        _tipTextView = (TextView) v.findViewById(R.id.tip_textview);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);
        _okButton.setEnabled(false);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void reset() {
        Log.v(TAG, "reset");
        super.reset();
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();

        if (_clear) {
            _spinner.clearSelection();
        }

        populateUi();
    }

    @Override
    public void dismiss() {
//        Log.e(TAG, "dismiss");
        _clear = true;
        super.dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
//        Log.e(TAG, "onDismiss");
        super.onDismiss(dialog);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(CustomField customField) {
        _customField = customField;
        super.show();
    }

    private void populateUi() {
        if (_textEditText == null || _dateTimeButton == null || _spinner == null ||
                _tipTextView == null || _customField == null)
            return;

        _titleTextView.setText(_customField.getLabel());

        CustomField.FieldType type = _customField.getFieldType();


        _textEditText.setVisibility(View.GONE);
        _dateTimeButton.setVisibility(View.GONE);
        _spinnerLayout.setVisibility(View.GONE);
        _tipTextView.setVisibility(View.GONE);

        if (!misc.isEmptyOrNull(_customField.getTip())) {
            _tipTextView.setVisibility(View.VISIBLE);
            if (!misc.isEmptyOrNull(_customField.getCustomFieldFormat())) {
                _tipTextView.setText(_customField.getTip() + " (" + _customField.getCustomFieldFormat() + ")");
            } else {
                _tipTextView.setText(_customField.getTip());
            }
        } else if (!misc.isEmptyOrNull(_customField.getCustomFieldFormat())) {
            _tipTextView.setVisibility(View.VISIBLE);
            _tipTextView.setText(_customField.getCustomFieldFormat());
        }

        _textEditText.setVisibility(View.VISIBLE);
        _textEditText.getEditableText().clear();
        //_textEditText.setText("", TextView.BufferType.EDITABLE);

        switch (type) {
            case DATE:
                if (!misc.isEmptyOrNull(_customFieldDateData)) {
                    _textEditText.setText(_customFieldDateData, TextView.BufferType.EDITABLE);
                } else if (!misc.isEmptyOrNull(_customField.getValue())) {
                    _textEditText.setText(_customField.getValue(), TextView.BufferType.EDITABLE);
                }
                break;
            case DATETIME:
                if (!misc.isEmptyOrNull(_customFieldDateTimeData)) {
                    _textEditText.setText(_customFieldDateTimeData, TextView.BufferType.EDITABLE);
                } else if (!misc.isEmptyOrNull(_customField.getValue())) {
                    _textEditText.setText(_customField.getValue(), TextView.BufferType.EDITABLE);
                }
                break;
            case TIME:
                if (!misc.isEmptyOrNull(_customFieldTimeData)) {
                    _textEditText.setText(_customFieldTimeData, TextView.BufferType.EDITABLE);
                } else if (!misc.isEmptyOrNull(_customField.getValue())) {
                    _textEditText.setText(_customField.getValue(), TextView.BufferType.EDITABLE);
                }
                break;
            case TEXT:
                if (!misc.isEmptyOrNull(_customFieldTextData)) {
                    _textEditText.setText(_customFieldTextData, TextView.BufferType.EDITABLE);
                } else if (!misc.isEmptyOrNull(_customField.getValue())) {
                    _textEditText.setText(_customField.getValue(), TextView.BufferType.EDITABLE);
                }
                break;
            case NUMBER:
                if (!misc.isEmptyOrNull(_customFieldNumberData)) {
                    _textEditText.setText(_customFieldNumberData, TextView.BufferType.EDITABLE);
                } else if (!misc.isEmptyOrNull(_customField.getValue())) {
                    _textEditText.setText(_customField.getValue(), TextView.BufferType.EDITABLE);
                }
                break;
            case PHONE:
                if (!misc.isEmptyOrNull(_customFieldPhoneNumberData)) {
                    _textEditText.setText(_customFieldPhoneNumberData, TextView.BufferType.EDITABLE);
                } else if (!misc.isEmptyOrNull(_customField.getValue())) {
                    _textEditText.setText(_customField.getValue(), TextView.BufferType.EDITABLE);
                }
                break;
        }

        switch (type) {
            case DATE:
                _dateTimeButton.setVisibility(View.VISIBLE);
                _textEditText.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                break;
            case DATETIME:
                _dateTimeButton.setVisibility(View.VISIBLE);
                _textEditText.setInputType(InputType.TYPE_CLASS_DATETIME);
                break;
            case TIME:
                _dateTimeButton.setVisibility(View.VISIBLE);
                _textEditText.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
                break;
            case TEXT:
                _textEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case NUMBER:
                _textEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case PHONE:
                _textEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case LIST:
                _spinnerLayout.setVisibility(View.VISIBLE);
                _textEditText.setVisibility(View.GONE);
                _okButton.setEnabled(true);
                if (_customField.getPredefinedValues() != null) {
                    Log.v(TAG, "PredefinedValues");
                    for (int i = 0; i < _customField.getPredefinedValues().length; i++) {
                        if (_customField.getPredefinedValues()[i] == null)
                            _customField.getPredefinedValues()[i] = "";
                    }

                    HintArrayAdapter adapter = HintArrayAdapter.createFromArray(
                            getActivity(),
                            _customField.getPredefinedValues(),
                            R.layout.view_spinner_item);

                    adapter.setDropDownViewResource(
                            android.support.design.R.layout.support_simple_spinner_dropdown_item);

                    _spinner.setAdapter(adapter);
                    if (!misc.isEmptyOrNull(_customField.getValue())) {
                        String val = _customField.getValue();
                        String[] values = _customField.getPredefinedValues();

                        for (int i = 0; i < values.length; i++) {
                            if (val.equals(values[i])) {
                                _spinner.setSelection(i);
                                _itemSelectedPosition = i;
                                break;
                            }
                        }
                    }
                }
                break;
        }

        _pickerCal = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        _datePicker = new DatePickerDialog(getActivity(), _date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        _timePicker = new TimePickerDialog(getActivity(), _time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
    }


    private final DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _pickerCal.set(year, monthOfYear, dayOfMonth);
            _expirationDate = (Calendar) _pickerCal.clone();
            switch (_customField.getFieldType()) {
                case DATE:
                    _textEditText.setText(DateUtils.formatDateForCF(_expirationDate));
                    break;
                case DATETIME:
                    _timePicker.setTag(_datePicker.getTag());
                    _timePicker.show();
                    break;
            }
        }
    };

    private final TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            _pickerCal.set(_pickerCal.get(Calendar.YEAR), _pickerCal.get(Calendar.MONTH),
                    _pickerCal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

            _expirationDate = (Calendar) _pickerCal.clone();

            switch (_customField.getFieldType()) {
                case DATETIME:
                    _textEditText.setText(DateUtils.formatDateTimeForCF(_expirationDate));
                    break;
                case TIME:
                    _textEditText.setText(DateUtils.formatTimeForCF(_expirationDate));
                    break;
            }
        }
    };

    private final View.OnClickListener _dateTime_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (_customField.getFieldType()) {
                case DATETIME:
                    _datePicker.show();
                    break;
                case DATE:
                    _datePicker.show();
                    break;
                case TIME:
                    _timePicker.show();
                    break;
            }
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            switch (_customField.getFieldType()) {
                case LIST:
                    _listener.onOk(_customField, (String) _spinner.getAdapter().getItem(_itemSelectedPosition));
                    break;
                default:
                    _listener.onOk(_customField, _textEditText.getText().toString());
            }
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private final TextWatcher _textEditText_watcherListener = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (_textEditText.getText().toString().trim().length() > 0) {
                _okButton.setEnabled(true);
            } else {
                _okButton.setEnabled(false);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    private final AdapterView.OnItemSelectedListener _spinner_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _itemSelectedPosition = position;
            Log.v(TAG, "onItemSelected");
            if (_okButton != null)
                _okButton.setEnabled(true);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Log.v(TAG, "onNothingSelected");
            if (_okButton != null)
                _okButton.setEnabled(false);
        }
    };

    public interface Listener {
        void onOk(CustomField field, String value);
    }
}
