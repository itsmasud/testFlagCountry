package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;

/**
 * Created by michael.carver on 10/29/2014.
 */
public class CustomFieldRowView extends RelativeLayout {
    private static final String TAG = "ui.workorder.CustomFieldRowView";

    // Ui
    private CheckBox _checkbox;
    private TextView _optionalTextView;

    // Data


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
        _optionalTextView = (TextView) findViewById(R.id.optional_textview);

    }


}
