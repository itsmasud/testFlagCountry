package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.ui.dialog.DatePickerDialog;
import com.fieldnation.ui.dialog.TimePickerDialog;
import com.fieldnation.v2.data.model.CustomField;

import java.util.Calendar;

/**
 * Created by michael.carver on 10/29/2014.
 */
public class CustomFieldDialog extends SimpleDialog {
    private static final String TAG = "CustomFieldDialog";

    // UI
    private TextView _titleTextView;
    private EditText _textEditText;
    private Button _dateTimeButton;
    private HintSpinner _spinner;
    private TextView _tipTextView;
    private Button _okButton;
    private Button _cancelButton;

    // Dialogs
    private DatePickerDialog _datePicker;
    private TimePickerDialog _timePicker;

    // Data
    private CustomField _customField;
    private Calendar _pickerCal;
    private Calendar _expirationDate;
    private int _itemSelectedPosition = -1;
    private String _customFieldDateData;
    private String _customFieldDateTimeData;
    private String _customFieldTimeData;
    private String _customFieldTextData;
    private String _customFieldNumberData;
    private String _customFieldPhoneNumberData;

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public CustomFieldDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_custom_field, container, false);

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _textEditText = (EditText) v.findViewById(R.id.text_edittext);
        _dateTimeButton = (Button) v.findViewById(R.id.datetime_button);
        _spinner = (HintSpinner) v.findViewById(R.id.spinner);
        _tipTextView = (TextView) v.findViewById(R.id.tip_textview);
        _okButton = (Button) v.findViewById(R.id.ok_button);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _textEditText.addTextChangedListener(_textEditText_watcherListener);
        _dateTimeButton.setOnClickListener(_dateTime_onClick);
        _spinner.setOnItemSelectedListener(_spinner_selected);
        _okButton.setOnClickListener(_ok_onClick);
        _okButton.setEnabled(false);
        _cancelButton.setOnClickListener(_cancel_onClick);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);

        _customField = payload.getParcelable("customField");

        populateUi();
    }

/*
    @Override
    public void onRestoreDialogState(Bundle savedState) {
        super.onRestoreDialogState(savedState);
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

            if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD_LIST_ITEM_SELECTED))
                _itemSelectedPosition = savedInstanceState.getInt(STATE_CUSTOM_FIELD_LIST_ITEM_SELECTED);

            if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD)) {
                _customField = savedInstanceState.getParcelable(STATE_CUSTOM_FIELD);
            }

            CustomField.FieldType type = _customField.getType();
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

                case LIST:
                    if (savedInstanceState.containsKey(STATE_CUSTOM_FIELD_LIST_ITEM_SELECTED)) {
                        _itemSelectedPosition = savedInstanceState.getInt(STATE_CUSTOM_FIELD_LIST_ITEM_SELECTED);
                    }
                    break;
            }
        }
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        super.onSaveDialogState(outState);

        if (_customField != null)
            outState.putParcelable(STATE_CUSTOM_FIELD, _customField);

        CustomField.FieldType type = _customField.getType();
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
            case LIST:
                if (_itemSelectedPosition != -1) {
                    outState.putInt(STATE_CUSTOM_FIELD_LIST_ITEM_SELECTED, _itemSelectedPosition);
                }
                break;
        }
//        Log.e(TAG, "_customFieldTextData: " + _customFieldTextData);
    }
*/

    @Override
    public void dismiss(boolean animate) {
        misc.hideKeyboard(_cancelButton);
        super.dismiss(animate);
    }

    private void populateUi() {
        if (_textEditText == null || _dateTimeButton == null || _spinner == null
                || _tipTextView == null || _customField == null)
            return;

        _titleTextView.setText(_customField.getName());

        CustomField.TypeEnum type = _customField.getType();

        _textEditText.setVisibility(View.GONE);
        _dateTimeButton.setVisibility(View.GONE);
        _spinner.setVisibility(View.GONE);
        _tipTextView.setVisibility(View.GONE);

        if (!misc.isEmptyOrNull(_customField.getTip())) {
            _tipTextView.setVisibility(View.VISIBLE);
//            if (!misc.isEmptyOrNull(_customField.getCustomFieldFormat())) {
//                _tipTextView.setText(_customField.getTip() + " (" + _customField.getCustomFieldFormat() + ")");
//            } else {
            _tipTextView.setText(_customField.getTip());
//            }
//        } else if (!misc.isEmptyOrNull(_customField.getCustomFieldFormat())) {
//            _tipTextView.setVisibility(View.VISIBLE);
//            _tipTextView.setText(_customField.getCustomFieldFormat());
        } else {
            _tipTextView.setVisibility(View.GONE);
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
            case DATE_TIME:
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
            case NUMERIC:
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
            case DATE_TIME:
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
            case NUMERIC:
                _textEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case PHONE:
                _textEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case PREDEFINED:
                _spinner.setVisibility(View.VISIBLE);
                _textEditText.setVisibility(View.GONE);
                _okButton.setEnabled(true);
                if (_customField.getOptions() != null) {
                    Log.v(TAG, "PredefinedValues");
                    for (int i = 0; i < _customField.getOptions().length; i++) {
                        if (_customField.getOptions()[i] == null)
                            _customField.getOptions()[i] = "";
                    }

                    HintArrayAdapter adapter = HintArrayAdapter.createFromArray(
                            getView().getContext(),
                            _customField.getOptions(),
                            R.layout.view_spinner_item);

                    adapter.setDropDownViewResource(
                            android.support.design.R.layout.support_simple_spinner_dropdown_item);

                    _spinner.setAdapter(adapter);
                    if (!misc.isEmptyOrNull(_customField.getValue())) {
                        String val = _customField.getValue();
                        String[] values = _customField.getOptions();

                        for (int i = 0; i < values.length; i++) {
                            if (val.equals(values[i])) {
                                _spinner.setSelection(i);
                                break;
                            }
                        }
                    }
                    if (_itemSelectedPosition != -1)
                        _spinner.setSelection(_itemSelectedPosition);

                }
                break;
        }

        _pickerCal = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        _datePicker = new DatePickerDialog(getView().getContext(), _date_onSet, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        _timePicker = new TimePickerDialog(getView().getContext(), _time_onSet, c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE), false);
    }

    private final DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _pickerCal.set(year, monthOfYear, dayOfMonth);
            _expirationDate = (Calendar) _pickerCal.clone();
            switch (_customField.getType()) {
                case DATE:
                    _textEditText.setText(DateUtils.formatDateForCF(_expirationDate));
                    break;
                case DATE_TIME:
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

            switch (_customField.getType()) {
                case DATE_TIME:
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
            switch (_customField.getType()) {
                case DATE_TIME:
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
            dismiss(true);
            switch (_customField.getType()) {
                case PREDEFINED:
                    _onOkDispatcher.dispatch(getUid(), _customField, (String) _spinner.getAdapter().getItem(_itemSelectedPosition));
                    break;
                default:
                    _onOkDispatcher.dispatch(getUid(), _customField, _textEditText.getText().toString());
            }
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
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

    public static void show(Context context, String uid, CustomField customField) {
        Bundle params = new Bundle();
        params.putParcelable("customField", customField);

        Controller.show(context, uid, CustomFieldDialog.class, params);
    }

    /*-**********************-*/
    /*-         Ok           -*/
    /*-**********************-*/
    public interface OnOkListener {
        void onOk(CustomField field, String value);
    }

    private static KeyedDispatcher<OnOkListener> _onOkDispatcher = new KeyedDispatcher<OnOkListener>() {
        @Override
        public void onDispatch(OnOkListener listener, Object... parameters) {
            listener.onOk((CustomField) parameters[0], (String) parameters[1]);
        }
    };

    public static void addOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.add(uid, onOkListener);
    }

    public static void removeOnOkListener(String uid, OnOkListener onOkListener) {
        _onOkDispatcher.remove(uid, onOkListener);
    }

    public static void removeAllOnOkListener(String uid) {
        _onOkDispatcher.removeAll(uid);
    }
}
