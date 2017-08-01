package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.v2.data.model.Problem;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.UnresolvedProblemsDialog;

/**
 * Created by mc on 5/24/17.
 */

public class ProblemSummaryView extends RelativeLayout {
    private static final String TAG = "ProblemSummaryView";

    // Ui
    private TextView _countTextView;

    // Data
    private WorkOrder _workOrder;

    public ProblemSummaryView(Context context) {
        super(context);
        init();
    }

    public ProblemSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProblemSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_problem_summary, this);

        if (isInEditMode())
            return;

        _countTextView = (TextView) findViewById(R.id.count_textview);

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

        int count = 0;
        if (_workOrder.getProblems().getResults().length > 0) {
            for (Problem problem : _workOrder.getProblems().getResults()) {
                if (problem != null
                        && problem.getActionsSet().contains(Problem.ActionsEnum.RESOLVE)) {
                    count++;
                }
            }
        }

        if (count == 0) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);
        _countTextView.setText(count + "");
    }

    private final View.OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            UnresolvedProblemsDialog.show(App.get(), null, _workOrder);
        }
    };
}
