package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.ListItemTwoHorizView;

/**
 * Created by shoaib.ahmed on 11/20/2017.
 */

public class EarnedPaySummaryView extends RelativeLayout implements WorkOrderRenderer, UUIDView {
    private static final String TAG = "EarnedPaySummaryView";

    // Ui
    private ListItemTwoHorizView _summaryView;


    //Data
    private WorkOrder _workOrder;
    private String _myUUID;


    public EarnedPaySummaryView(Context context) {
        super(context);
        init();
    }

    public EarnedPaySummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EarnedPaySummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_earned_pay_summary, this, true);

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
                || _workOrder.getPay().getTotal() == null
                || _workOrder.getPay().getTotal() == 0) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);
        _summaryView.set(_summaryView.getResources().getString(R.string.earned_pay), misc.toCurrency(_workOrder.getPay().getTotal()));

        setOnClickListener(_this_onClick);
    }

    private final OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
//            EarnedPayDialog.show(App.get(), null, _workOrder.getId());
        }
    };
}
