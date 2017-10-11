package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.ExpenseListDialog;

/**
 * Created by mc on 10/10/17.
 */

public class ExpensesSummaryView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "ExpensesSummaryView";

    // Ui
    private TextView _titleTextView;
    private TextView _countTextView;

    //Data
    private WorkOrder _workOrder;

    public ExpensesSummaryView(Context context) {
        super(context);
        init();
    }

    public ExpensesSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExpensesSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_task_summary_row, this, true);

        if (isInEditMode()) return;

        _titleTextView = findViewById(R.id.title_textview);
        _countTextView = findViewById(R.id.count_textview);
        _countTextView.setBackgroundResource(R.drawable.round_rect_gray);
        populateUi();
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        if (_workOrder == null || _countTextView == null)
            return;

        _titleTextView.setText("Expenses");

        if (_workOrder.getPay() == null
                || _workOrder.getPay().getExpenses() == null
                || _workOrder.getPay().getExpenses().getResults() == null
                || _workOrder.getPay().getExpenses().getResults().length == 0) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);
        _countTextView.setText(_workOrder.getPay().getExpenses().getResults().length + "");
        setOnClickListener(_this_onClick);
    }

    private final View.OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            ExpenseListDialog.show(App.get(), null, _workOrder.getId());
        }
    };
}
