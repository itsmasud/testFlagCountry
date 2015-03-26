package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fieldnation.ForLoopRunnable;
import com.fieldnation.R;
import com.fieldnation.data.workorder.CustomField;
import com.fieldnation.data.workorder.Workorder;

/**
 * Created by michael.carver on 10/30/2014.
 */

public class CustomFieldListView extends RelativeLayout {
    private static final String TAG = "ui.workorder.detail.CustomFieldListView";

    // Ui
    private LinearLayout _fieldsList;

    // Data
    private Workorder _workorder;
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

        ForLoopRunnable r = new ForLoopRunnable(_fields.length, new Handler()) {
            @Override
            public void next(int i) throws Exception {
                CustomFieldRowView v = null;
                if (i < _fieldsList.getChildCount()) {
                    v = (CustomFieldRowView) _fieldsList.getChildAt(i);
                } else {
                    v = new CustomFieldRowView(getContext());
                    _fieldsList.addView(v);
                }
                CustomField field = _fields[i];
                v.setData(_workorder, field, _listener);
            }

            @Override
            public void finish(int count) throws Exception {
                if (_fieldsList.getChildCount() > count) {
                    _fieldsList.removeViews(count - 1, _fieldsList.getChildCount() - count);
                }
            }
        };
        post(r);
    }

    public void setListener(CustomFieldRowView.Listener listener) {
        _listener = listener;
        populateUi();
    }

    public void setData(Workorder workorder, CustomField[] fieldList) {
        _fields = fieldList;
        _workorder = workorder;
        populateUi();
    }
}
