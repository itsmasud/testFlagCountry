package com.fieldnation.ui.dialog;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.CustomField;
import com.fieldnation.utils.misc;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Calendar;

/**
 * Created by michael.carver on 10/29/2014.
 */
public class CustomFieldDialog extends DialogFragmentBase {
    private static final String TAG = "CustomFieldDialog";

    // State
    private static final String STATE_CUSTOM_FIELD = "CustomFieldDialog:STATE_CUSTOM_FIELD";

    // UI
    private TextView _titleTextView;
    private EditText _textEditText;
    private Button _dateTimeButton;
    private LinearLayout _spinnerLayout;
    private MaterialBetterSpinner _spinner;
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

        setStyle(STYLE_NO_TITLE, 0);
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

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);

        _textEditText = (EditText) v.findViewById(R.id.text_edittext);
        _textEditText.addTextChangedListener(_textEditText_watcherListener);
        Log.v(TAG, "onCreateView() _textEditText = " + _textEditText.toString());

        _dateTimeButton = (Button) v.findViewById(R.id.datetime_button);
        _dateTimeButton.setOnClickListener(_dateTime_onClick);

        _spinnerLayout = (LinearLayout) v.findViewById(R.id.spinner_layout);
        _spinner = (MaterialBetterSpinner) v.findViewById(R.id.spinner);
        _spinner.setOnItemClickListener(_spinner_selected);

        _tipTextView = (TextView) v.findViewById(R.id.tip_textview);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void reset() {
    }

    @Override
    public void init() {
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
        if (!misc.isEmptyOrNull(_customField.getValue())) {
            _textEditText.setText(_customField.getValue(), TextView.BufferType.EDITABLE);
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                            R.layout.view_spinner_item,
                            _customField.getPredefinedValues());

                    adapter.setDropDownViewResource(android.support.design.R.layout.support_simple_spinner_dropdown_item);
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

        _pickerCal = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        _datePicker = DatePickerDialog.newInstance(_date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        _datePicker.setCloseOnSingleTapDay(true);
        _timePicker = TimePickerDialog.newInstance(_time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                false, false);

    }


    private final DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            _pickerCal.set(year, month, day);
            _expirationDate = (Calendar) _pickerCal.clone();
            switch (_customField.getFieldType()) {
                case DATE:
                    _textEditText.setText(misc.formatDateForCF(_expirationDate));
                    break;
                case DATETIME:
                    _timePicker.show(_fm, datePickerDialog.getTag());
                    break;
            }

        }
    };


    private final TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute) {
            String tag = view.getTag();
            _pickerCal.set(_pickerCal.get(Calendar.YEAR), _pickerCal.get(Calendar.MONTH),
                    _pickerCal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

            _expirationDate = (Calendar) _pickerCal.clone();

            switch (_customField.getFieldType()) {
                case DATETIME:
                    _textEditText.setText(misc.formatDateTimeForCF(_expirationDate));
                    break;
                case TIME:
                    _textEditText.setText(misc.formatTimeForCF(_expirationDate));
                    break;
            }


        }
    };


    private final View.OnClickListener _dateTime_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (_customField.getFieldType()) {
                case DATETIME:
                    _datePicker.show(_fm, TAG);
                    break;
                case DATE:
                    _datePicker.setCloseOnSingleTapDay(false);
                    _datePicker.show(_fm, TAG);
                    break;
                case TIME:
                    _timePicker.show(_fm, TAG);
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

    private final AdapterView.OnItemClickListener _spinner_selected = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            _itemSelectedPosition = position;
        }

    };


    public interface Listener {
        void onOk(CustomField field, String value);
    }
}
