package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.ui.workorder.BundleDetailActivity;
import com.fieldnation.v2.data.model.WorkOrder;

/**
 * Created by shoaib.ahmed on 08/02/2017.
 */

public class BundleSummaryView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "BundleSummaryView";

    // Ui
    private TextView _countTextView;

    // Data
    private WorkOrder _workOrder;

    public BundleSummaryView(Context context) {
        super(context);
        init();
    }

    public BundleSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BundleSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_bundle_summary, this);

        if (isInEditMode())
            return;

        _countTextView = findViewById(R.id.count_textview);

        setOnClickListener(_this_onClick);
        setVisibility(GONE);

        populateUi();
    }

    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_countTextView == null)
            return;

        if (_workOrder.isBundle()) {
            setVisibility(VISIBLE);
            _countTextView.setText((_workOrder.getBundle().getMetadata().getTotal() + 1) + "");
        } else {
            setVisibility(GONE);
        }
    }

    private final OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            BundleDetailActivity.startNew(App.get(), _workOrder.getBundle().getId());
        }
    };
}
