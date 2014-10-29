package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.CustomField;

import java.util.List;

/**
 * Created by michael.carver on 10/29/2014.
 */
public class CustomFieldDialog extends DialogFragment {
    private static final String TAG = "ui.dialog.CustomFieldDialog";

    // State
    private static final String STATE_CUSTOM_FIELD = "CustomFieldDialog:STATE_CUSTOM_FIELD";

    // UI
    private TextView _titleTextView;
    private EditText _textEditText;
    private Button _dateTimeButton;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private FragmentManager _fm;
    private CustomField _customField;
    private Listener _listener;

    public static CustomFieldDialog getInstance(FragmentManager fm, String tag) {
        CustomFieldDialog d = null;
        List<Fragment> frags = fm.getFragments();
        if (frags != null) {
            for (int i = 0; i < frags.size(); i++) {
                Fragment frag = frags.get(i);
                if (frag instanceof CustomFieldDialog && frag.getTag().equals(tag)) {
                    d = (CustomFieldDialog) frag;
                    break;
                }
            }
        }
        if (d == null)
            d = new CustomFieldDialog();
        d._fm = fm;
        return d;
    }

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        CustomField.FieldType type = _customField.getFieldType();

        View v = null;
        if (type == CustomField.FieldType.LIST) {
            v = inflater.inflate(R.layout.dialog_custom_field_list, container, false);
        } else {
            v = inflater.inflate(R.layout.dialog_custom_field, container, false);
            _textEditText = (EditText) v.findViewById(R.id.text_edittext);
            _textEditText.setVisibility(View.GONE);
            _dateTimeButton = (Button) v.findViewById(R.id.datetime_button);
            _dateTimeButton.setVisibility(View.GONE);
        }

        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        switch (type) {
            case TEXT:
                _textEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
                _textEditText.setVisibility(View.VISIBLE);
                break;
            case DATE:
                _dateTimeButton.setVisibility(View.VISIBLE);
                break;
            case DATETIME:
                _dateTimeButton.setVisibility(View.VISIBLE);
                break;
            case NUMBER:
                _textEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                _textEditText.setVisibility(View.VISIBLE);
                break;
            case PHONE:
                _textEditText.setRawInputType(InputType.TYPE_CLASS_PHONE);
                _textEditText.setVisibility(View.VISIBLE);
                break;
            case TIME:
                _dateTimeButton.setVisibility(View.VISIBLE);
                break;
            case LIST:
                break;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(String tag, CustomField customField, Listener listener) {
        _customField = customField;
        _listener = listener;
        show(_fm, tag);
    }

    private View.OnClickListener _ok_onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener _cancel_onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };


    public interface Listener {

    }
}
