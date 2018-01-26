package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.workorder.BundleDetailActivity;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.ListItemWebView;
import com.fieldnation.v2.ui.workorder.QualificationsSummaryView;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

public class WorkSummaryView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "WorkSummaryView";

    // UI
    private ListItemWebView _descriptionView;
    private QualificationsSummaryView _qualificationsSummaryView;

    private ListItemWebView _standardInstructionsView;
    private ListItemWebView _policiesView;
    private ListItemWebView _confidentialInformationView;

    // Data
    private WorkOrder _workOrder;
    private boolean _collapsed = true;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public WorkSummaryView(Context context) {
        super(context);
        init();
    }

    public WorkSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_sum, this);

        if (isInEditMode())
            return;

        _descriptionView = findViewById(R.id.descriptionView);
        _qualificationsSummaryView = findViewById(R.id.qualificationsSummary_view);

        _standardInstructionsView = findViewById(R.id.standardInstructionsView);

        _confidentialInformationView = findViewById(R.id.confidentialInformationView);
        _policiesView = findViewById(R.id.policiesView);

        setVisibility(View.GONE);
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        refresh();
    }

    private void refresh() {
        setVisibility(View.VISIBLE);

        _descriptionView.setData(null, _workOrder.getDescription().getHtml());
        _qualificationsSummaryView.setWorkOrder(_workOrder);

        if (misc.isEmptyOrNull(_workOrder.getPolicyAndProcedures().getHtml())) {
            _policiesView.setVisibility(View.GONE);
        } else {
            _policiesView.setVisibility(View.VISIBLE);
            _policiesView.setData("Policies and Procedures", _workOrder.getPolicyAndProcedures().getHtml());
        }

        if (misc.isEmptyOrNull(_workOrder.getConfidential().getHtml())) {
            _confidentialInformationView.setVisibility(View.GONE);
        } else {
            _confidentialInformationView.setVisibility(View.VISIBLE);
            _confidentialInformationView.setData("Confidential Information", _workOrder.getConfidential().getHtml());
        }

        if (misc.isEmptyOrNull(_workOrder.getStandardInstructions().getHtml())) {
            _standardInstructionsView.setVisibility(GONE);
        } else {
            _standardInstructionsView.setVisibility(VISIBLE);
            _standardInstructionsView.setData("Standard Instructions", _workOrder.getStandardInstructions().getHtml());
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnClickListener _bundle_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BundleDetailActivity.startNew(App.get(), _workOrder.getBundle().getId());
        }
    };
}