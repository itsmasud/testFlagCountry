package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.ListItemTwoHorizView;

/**
 * Created by Shoaib on 10/20/17.
 */

public class TimeLogSummaryView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "TimeLogSummaryView";

    // Ui
    private ListItemTwoHorizView _summaryView;

    // Data
    private WorkOrder _workOrder;

    public TimeLogSummaryView(Context context) {
        super(context);
        init();
    }

    public TimeLogSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeLogSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_timelog_summary_view, this, true);

        if (isInEditMode())
            return;

        _summaryView = findViewById(R.id.summary_view);
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        if (_summaryView == null) return;

        if (_workOrder == null) return;


        if (_workOrder.getTimeLogs().getHours() == null) {
            setVisibility(GONE);
            return;
        } else if (_workOrder.getTimeLogs().getHours() > 0) {
            _summaryView.set(_summaryView.getContext().getString(R.string.time_logged), String.format("%.2f", _workOrder.getTimeLogs().getHours()) + " hrs");
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
            return;
        }

        setOnClickListener(_this_onClick);
    }

    private final View.OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
//            TimelogsListDialog.show(App.get(), null, _workOrder.getId());
        }
    };

}
