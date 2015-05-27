package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.CustomField;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.utils.misc;

/**
 * Created by michael.carver on 10/29/2014.
 */
public class CustomFieldRowView extends RelativeLayout {
    private static final String TAG = "CustomFieldRowView";

    // Ui
    private IconFontTextView _iconView;
    private TextView _descriptionTextView;
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

        _iconView = (IconFontTextView) findViewById(R.id.icon_view);
        _descriptionTextView = (TextView) findViewById(R.id.description_textview);
        _optionalTextView = (TextView) findViewById(R.id.optional_textview);

        setOnClickListener(_check_listener);

        populateUi();
    }

    public void setData(Workorder workorder, CustomField customField, Listener listener) {
        _customField = customField;
        _listener = listener;
        _workorder = workorder;
        populateUi();
    }

    private void populateUi() {
        if (_iconView == null)
            return;

        if (_workorder == null)
            return;

        if (_customField == null)
            return;

        setEnabled(_workorder.canChangeCustomFields());
        if (_workorder.canChangeCustomFields()) {
            _descriptionTextView.setTextColor(getResources().getColor(R.color.fn_dark_text));
        } else {
            _descriptionTextView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
        }

        if (misc.isEmptyOrNull(_customField.getValue())) {
            _descriptionTextView.setText(_customField.getLabel());
        } else {
            _descriptionTextView.setText(_customField.getLabel() + "\n" + _customField.getValue());
        }

        if (_customField.getRequired()) {
            _optionalTextView.setVisibility(View.GONE);
        } else {
            _optionalTextView.setVisibility(View.VISIBLE);
        }

        updateCheckBox();
    }

    private void updateCheckBox() {
        if (_workorder.canChangeCustomFields()) {
            // set enabled
            if (misc.isEmptyOrNull(_customField.getValue())) {
                _iconView.setTextColor(getResources().getColor(R.color.fn_light_text));
                _iconView.setText(R.string.icfont_circle);
            } else {
                _iconView.setTextColor(getResources().getColor(R.color.fn_accent_color));
                _iconView.setText(R.string.icfont_circle_checked);
            }
        } else {
            if (misc.isEmptyOrNull(_customField.getValue())) {
                _iconView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
                _iconView.setText(R.string.icfont_circle);
            } else {
                _iconView.setTextColor(getResources().getColor(R.color.fn_light_text_50));
                _iconView.setText(R.string.icfont_circle_checked);
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
