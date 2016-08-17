package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.R;
import com.fieldnation.data.workorder.CustomField;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fntools.misc;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael.carver on 10/30/2014.
 */

public class CustomFieldListView extends RelativeLayout {
    private static final String TAG = "CustomFieldListView";

    // Ui
    private LinearLayout _fieldsList;

    // Data
    private Workorder _workorder;
    private List<Object> _fields;
    private CustomFieldRowView.Listener _listener;
    private ForLoopRunnable _forLoop = null;
    private final List<View> _views = new LinkedList<>();

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
        if (_fieldsList == null || _fields == null || _fields.size() == 0) {
            setVisibility(View.GONE);
            return;
        }

        setVisibility(View.VISIBLE);

        if (_forLoop != null) {
            _forLoop.cancel();
            _forLoop = null;
        }

//        if (_fieldsList.getChildCount() > _fields.size()) {
//            _fieldsList.removeViews(_fields.size() - 1, _fieldsList.getChildCount() - _fields.size());
//        }

        _views.clear();

        _forLoop = new ForLoopRunnable(_fields.size(), new Handler()) {
            @Override
            public void next(int i) throws Exception {
                Object obj = _fields.get(i);
                if (obj instanceof String) {
                    View v = LayoutInflater.from(getContext()).inflate(R.layout.view_customfield_header, null); // null since we will be adding it later
                    ((TextView) v.findViewById(R.id.customFieldHeader)).setText((String) obj);
                    if (i == 0) {
                        v.findViewById(R.id.spacer).setVisibility(GONE);
                    }
                    _views.add(v);
                } else {
                    CustomFieldRowView v = new CustomFieldRowView(getContext());
                    v.setData(_workorder, (CustomField) obj, _listener);
                    _views.add(v);
                }
            }

            @Override
            public void finish(int count) throws Exception {
                _fieldsList.removeAllViews();
                for (View v : _views) {
                    _fieldsList.addView(v);
                }
            }
        };
        postDelayed(_forLoop, 100);
    }

    public void setListener(CustomFieldRowView.Listener listener) {
        _listener = listener;
        populateUi();
    }

    public void setData(Workorder workorder, CustomField[] fieldList) {
        if (fieldList != null && fieldList.length > 0) {
            String section = fieldList[0].getSection();
            _fields = new LinkedList<>();

            if (!misc.isEmptyOrNull(section)) {
                _fields.add(section);
            }

            for (CustomField c : fieldList) {
                if (c.getSection() != null && !c.getSection().equals(section)) {
                    section = c.getSection();
                    _fields.add(section);
                }
                _fields.add(c);
            }
        }
        _workorder = workorder;
        populateUi();
    }
}
