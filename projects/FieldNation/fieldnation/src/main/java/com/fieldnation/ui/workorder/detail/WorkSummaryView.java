package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.workorder.BundleDetailActivity;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.ListItemWebView;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

public class WorkSummaryView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "WorkSummaryView";

    // UI
    private TextView _bundleWarningTextView;
    private View _bundleWarningLayout;

    private ListItemWebView _descriptionView;

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
        _standardInstructionsView = findViewById(R.id.standardInstructionsView);

        _confidentialInformationView = findViewById(R.id.confidentialInformationView);
        _policiesView = findViewById(R.id.policiesView);

        _bundleWarningTextView = findViewById(R.id.bundlewarning_textview);
        _bundleWarningTextView.setOnClickListener(_bundle_onClick);
        _bundleWarningLayout = findViewById(R.id.bundlewarning_layout);

        setVisibility(View.GONE);
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        refresh();
    }

    private void refresh() {
        setVisibility(View.VISIBLE);

        if (_workOrder.getBundle().getId() != null && _workOrder.getBundle().getId() > 0) {
            _bundleWarningTextView.setVisibility(View.VISIBLE);
            _bundleWarningLayout.setVisibility(VISIBLE);
        } else {
            _bundleWarningTextView.setVisibility(View.GONE);
            _bundleWarningLayout.setVisibility(GONE);
        }

        _descriptionView.setData(_workOrder.getDescription().getHtml());

        if (misc.isEmptyOrNull(_workOrder.getPolicyAndProcedures().getHtml())) {
            _policiesView.setVisibility(View.GONE);
        } else {
            _policiesView.setVisibility(View.VISIBLE);
            _policiesView.setTitle("Policies And Procedures");
            _policiesView.setData(_workOrder.getPolicyAndProcedures().getHtml());
        }

        if (misc.isEmptyOrNull(_workOrder.getConfidential().getHtml())) {
            _confidentialInformationView.setVisibility(View.GONE);
        } else {
            _confidentialInformationView.setVisibility(View.VISIBLE);
            _confidentialInformationView.setTitle("Confidential Information");
            _confidentialInformationView.setData(_workOrder.getConfidential().getHtml());
        }

        if (misc.isEmptyOrNull(_workOrder.getStandardInstructions().getHtml())) {
            _standardInstructionsView.setVisibility(GONE);
        } else {
            _standardInstructionsView.setVisibility(VISIBLE);
            _standardInstructionsView.setTitle("Standard Instructions");
            _standardInstructionsView.setData(_workOrder.getStandardInstructions().getHtml());
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