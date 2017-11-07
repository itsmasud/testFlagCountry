package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.ListItemSummaryView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by shoaib.ahmed on 11/07/2017.
 */

public class PenaltySummaryView extends RelativeLayout implements WorkOrderRenderer, UUIDView {
    private static final String TAG = "PenaltySummaryView";

    // Ui
    private ListItemSummaryView _summaryView;


    //Data
    private WorkOrder _workOrder;
    private String _myUUID;


    public PenaltySummaryView(Context context) {
        super(context);
        init();
    }

    public PenaltySummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PenaltySummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_penalty_summary, this, true);

        if (isInEditMode()) return;

        _summaryView = findViewById(R.id.summary_view);
        setVisibility(GONE);
        populateUi();
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    @Override
    public void setUUID(String uuid) {
        _myUUID = uuid;
    }


    private void populateUi() {
        if (_workOrder == null || _summaryView == null)
            return;

        if (_workOrder.getPay() == null
                || _workOrder.getPay().getPenalties() == null
                || _workOrder.getPay().getPenalties().getResults() == null
                || _workOrder.getPay().getPenalties().getResults().length == 0) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);
        _summaryView.setTitle(_summaryView.getResources().getString(R.string.penalties));

        _summaryView.setCount(String.valueOf(_workOrder.getPay().getPenalties().getResults().length));
        _summaryView.setCountBg( R.drawable.round_rect_gray);

        setOnClickListener(_this_onClick);
    }

    private final OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
//            PenaltyDialog.show(App.get(), null, _workOrder.getId());
        }
    };
}
