package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.CustomField;
import com.fieldnation.v2.data.model.CustomFieldCategory;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael.carver on 10/30/2014.
 */

public class CustomFieldListView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "CustomFieldListView";

    // Ui
    private LinearLayout _fieldsList;

    // Data
    private WorkOrder _workOrder;
    private List<Object> _fields = new LinkedList<>();
    private CustomFieldRowView.Listener _listener;
    private ForLoopRunnable _forLoop = null;

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

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;

        if (_workOrder.getCustomFields().getResults().length > 0) {
            _fields.clear();

            CustomFieldCategory[] categoryList = _workOrder.getCustomFields().getResults();

            for (CustomFieldCategory category : categoryList) {
                if (category.getRole().equals("buyer"))
                    continue;

                if (!misc.isEmptyOrNull(category.getName()))
                    _fields.add(category.getName());

                CustomField[] customFields = category.getResults();
                for (CustomField customField : customFields) {
                    _fields.add(customField);
                }
            }
        }

        populateUi();
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_fieldsList == null)
            return;

        if (_workOrder.getCustomFields().getResults().length == 0) {
            setVisibility(GONE);
            return;
        }

        if (_fields.size() == 0) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);

        if (_forLoop != null) {
            _forLoop.cancel();
            _forLoop = null;
        }

        _forLoop = new ForLoopRunnable(_fields.size(), new Handler()) {
            private List<View> _views = new LinkedList<>();

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
                    v.setData(_workOrder, (CustomField) obj, _listener);
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
}
