package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.CustomField;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;

/**
 * Created by michael.carver on 10/29/2014.
 */
public class CustomFieldRowView extends RelativeLayout {
    private static final String TAG = "ui.workorder.CustomFieldRowView";

    // Ui
    private CheckBox _checkbox;
    private TextView _optionalTextView;

    // Data
    private Listener _listener;
    private Workorder _workorder;
    private CustomField _customField;

    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/
    public CustomFieldRowView(Context context) {
        super(context);
        init();
    }

    public CustomFieldRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomFieldRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_custom_field_row, this);

        if (isInEditMode())
            return;

        _checkbox = (CheckBox) findViewById(R.id.checkbox);
        _checkbox.setOnClickListener(_check_listener);
        _optionalTextView = (TextView) findViewById(R.id.optional_textview);

        populateUi();
    }

    public void setData(Workorder workorder, CustomField customField, Listener listener) {
        _customField = customField;
        _listener = listener;
        _workorder = workorder;
        populateUi();
    }

    private void populateUi() {
        if (_checkbox == null || _optionalTextView == null || _customField == null)
            return;

        if (misc.isEmptyOrNull(_customField.getValue())) {
            _checkbox.setChecked(false);
            _checkbox.setText(_customField.getLabel());
        } else {
            _checkbox.setChecked(true);
            _checkbox.setText(_customField.getLabel() + "\n" + _customField.getValue());
        }

        if (_customField.getRequired()) {
            _optionalTextView.setVisibility(View.GONE);
        } else {
            _optionalTextView.setVisibility(View.VISIBLE);
        }

        _checkbox.setEnabled(_workorder.canChangeCustomFields());
    }

    private View.OnClickListener _check_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _checkbox.setChecked(!misc.isEmptyOrNull(_customField.getValue()));

            if (_listener != null) {
                _listener.onClick(CustomFieldRowView.this, _customField);
            }
        }
    };

    public interface Listener {
        public void onClick(CustomFieldRowView view, CustomField field);
    }

}
