package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.data.workorder.CustomField;

/**
 * Created by michael.carver on 10/30/2014.
 */

public class CustomFieldListView extends RelativeLayout {
    private static final String TAG = "ui.workorder.detail.CustomFieldListView";

    // Ui
    private LinearLayout _fieldsList;

    // Data
    private CustomField[] _fields;
    private CustomFieldRowView.Listener _listener;

    public CustomFieldListView(Context context) {
        super(context);
        init();
    }

    public CustomFieldListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomFieldListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_custom_field_list, this);

        if (isInEditMode())
            return;

        _fieldsList = (LinearLayout) findViewById(R.id.fields_list);

        populateUi();
    }

    private void populateUi() {
        if (_fieldsList == null || _fields == null || _fields.length == 0) {
            setVisibility(View.GONE);
            return;
        }

        setVisibility(View.VISIBLE);

        _fieldsList.removeAllViews();
        for (int i = 0; i < _fields.length; i++) {
            CustomField field = _fields[i];

            CustomFieldRowView v = new CustomFieldRowView(getContext());
            v.setData(field, _listener);
            _fieldsList.addView(v);
        }
    }

    public void setListener(CustomFieldRowView.Listener listener) {
        _listener = listener;
    }

    public void setData(CustomField[] fieldList) {
        _fields = fieldList;

        populateUi();
    }
}
