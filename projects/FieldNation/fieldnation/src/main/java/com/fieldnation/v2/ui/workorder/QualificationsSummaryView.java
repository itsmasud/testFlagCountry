package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.v2.data.model.SelectionRuleCriteria;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.ListItemSummaryView;

/**
 * Created by Shoaib on 11/06/17.
 */

public class QualificationsSummaryView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "QualificationsSummaryView";

    // Ui
    private ListItemSummaryView _summaryView;

    //Data
    private WorkOrder _workOrder;

    public QualificationsSummaryView(Context context) {
        super(context);
        init();
    }

    public QualificationsSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QualificationsSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_qualifications_summary, this, true);

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

    private void populateUi() {
        if (_workOrder == null || _summaryView == null)
            return;

        if (_workOrder.getQualifications() == null
                || _workOrder.getQualifications().getSelectionRule() == null
                || _workOrder.getQualifications().getSelectionRule().getResults() == null
                || _workOrder.getQualifications().getSelectionRule().getResults().length == 0) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);
        _summaryView.setTitle(_summaryView.getResources().getString(R.string.qualifications));

        int totalQualifications = _workOrder.getQualifications().getSelectionRule().getResults().length;
        int numberMatched = 0;

        for (SelectionRuleCriteria criteria : _workOrder.getQualifications().getSelectionRule().getResults()) {
            if (criteria.getStatus() != null && criteria.getStatus().equals(SelectionRuleCriteria.StatusEnum.MATCH))
                numberMatched++;
        }

        _summaryView.setCount(String.valueOf(numberMatched) + "/" + String.valueOf(totalQualifications));
        _summaryView.setCountBg(numberMatched == totalQualifications ? R.drawable.round_rect_green : R.drawable.round_rect_red);

        setOnClickListener(_this_onClick);
    }

    private final OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
//            QualificationsDialog.show(App.get(), null, _workOrder.getId());
        }
    };
}
