package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.v2.data.model.CustomField;
import com.fieldnation.v2.data.model.WorkOrder;

/**
 * Created by michael.carver on 10/29/2014.
 */
public class CustomFieldRowView extends RelativeLayout {
    private static final String TAG = "CustomFieldRowView";

    // Ui
    private IconFontTextView _iconView;
    private TextView _customFieldNameTextView;
    private TextView _descriptionTextView;
    private TextView _optionalTextView;

    // Data
    private Listener _listener;
    private WorkOrder _workOrder;
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

        _iconView = (IconFontTextView) findViewById(R.id.icon_view);
        _customFieldNameTextView = (TextView) findViewById(R.id.customFieldName_textview);
        _descriptionTextView = (TextView) findViewById(R.id.description_textview);
        _optionalTextView = (TextView) findViewById(R.id.optional_textview);

        setOnClickListener(_check_listener);

        populateUi();
    }

    public void setData(WorkOrder workOrder, CustomField customField, Listener listener) {
        _customField = customField;
        _listener = listener;
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        if (_iconView == null)
            return;

        if (_workOrder == null)
            return;

        if (_customField == null)
            return;


        setEnabled(_customField.getActionsSet().contains(CustomField.ActionsEnum.EDIT));
        if (_customField.getActionsSet().contains(CustomField.ActionsEnum.EDIT)) {
            _customFieldNameTextView.setTextColor(getResources().getColor(R.color.fn_dark_text));
            _descriptionTextView.setTextColor(getResources().getColor(R.color.fn_dark_text));
            _optionalTextView.setTextColor(getResources().getColor(R.color.fn_dark_text));
        } else {
            _customFieldNameTextView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
            _descriptionTextView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
            _optionalTextView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
        }

        if (misc.isEmptyOrNull(_customField.getValue())) {
            _customFieldNameTextView.setText(_customField.getName().trim());
        } else {
            _customFieldNameTextView.setText((_customField.getName() + "\n" + _customField.getValue()).trim());
        }

        if (misc.isEmptyOrNull(_customField.getTip())) {
            _descriptionTextView.setVisibility(GONE);
        } else {
            _descriptionTextView.setText(_customField.getTip());
            _descriptionTextView.setVisibility(VISIBLE);
        }

        if (_customField.getFlagsSet().contains(CustomField.FlagsEnum.REQUIRED)) {
            _optionalTextView.setVisibility(View.GONE);
        } else {
            _optionalTextView.setVisibility(View.VISIBLE);
        }

        updateCheckBox();
    }

    private void updateCheckBox() {
        if (_customField.getActionsSet().contains(CustomField.ActionsEnum.EDIT)) {
            // set enabled
            if (misc.isEmptyOrNull(_customField.getValue())) {
                _iconView.setTextColor(getResources().getColor(R.color.fn_light_text));
                _iconView.setText(R.string.icon_task);
            } else {
                _iconView.setTextColor(getResources().getColor(R.color.fn_accent_color));
                _iconView.setText(R.string.icon_task_done);
            }
        } else {
            if (misc.isEmptyOrNull(_customField.getValue())) {
                _iconView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
                _iconView.setText(R.string.icon_task);
            } else {
                _iconView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
                _iconView.setText(R.string.icon_task_done);
            }
        }
    }


    private final View.OnClickListener _check_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateCheckBox();

            if (_listener != null) {
                _listener.onClick(CustomFieldRowView.this, _customField);
            }
        }
    };

    public interface Listener {
        void onClick(CustomFieldRowView view, CustomField field);
    }

}
