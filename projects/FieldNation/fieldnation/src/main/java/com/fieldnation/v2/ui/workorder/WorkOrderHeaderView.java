package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.WorkOrder;

/**
 * Created by mc on 9/13/17.
 */

public class WorkOrderHeaderView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "WorkOrderHeaderView";

    // Ui
    private TextView _typeOfWorkTextView;
    private TextView _titleTextView;

    // Data
    private WorkOrder _workOrder;

    public WorkOrderHeaderView(Context context) {
        super(context);
        init();
    }

    public WorkOrderHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WorkOrderHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_work_order_header, this, true);

        if (isInEditMode())
            return;

        _typeOfWorkTextView = findViewById(R.id.typeOfWork_textview);
        _titleTextView = findViewById(R.id.title_textview);

        setVisibility(GONE);
        populateUi();
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        if (_typeOfWorkTextView == null)
            return;

        if (_workOrder == null) {
            setVisibility(GONE);
            return;
        }

        if (!misc.isEmptyOrNull(_workOrder.getTypeOfWork().getName())) {
            _typeOfWorkTextView.setVisibility(VISIBLE);
            _typeOfWorkTextView.setText(_workOrder.getTypeOfWork().getName());
        } else {
            _typeOfWorkTextView.setVisibility(GONE);
        }
        _titleTextView.setText(_workOrder.getTitle());

        setVisibility(VISIBLE);
    }
}
