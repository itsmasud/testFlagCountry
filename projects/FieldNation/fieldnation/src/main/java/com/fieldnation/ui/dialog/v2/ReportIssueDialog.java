package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;

/**
 * Created by Michael on 10/5/2016.
 */

public class ReportIssueDialog extends SimpleDialog {
    private static final String TAG = "ReportIssueDialog";

    private static final String DIALOG_REPORT_PROBLEM = TAG + ".reportProblemDialog";

    private static final String PARAM_WORKORDER = "workOrder";

    // Ui
    private Button _rescheduleButton;
    private Button _cancelWorkButton;
    private Button _reportOtherButton;
    private Button _cancelButton;

    // Data
    private WorkOrder _workOrder;

    public ReportIssueDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_report_issue, container, false);

        _rescheduleButton = (Button) v.findViewById(R.id.reschedule_button);
        _cancelWorkButton = (Button) v.findViewById(R.id.cancelWork_button);
        _reportOtherButton = (Button) v.findViewById(R.id.reportother_button);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _rescheduleButton.setOnClickListener(_reschedule_onClick);
        _cancelWorkButton.setOnClickListener(cancelWork_onClick);
        _reportOtherButton.setOnClickListener(_reportOther_onClick);
        _cancelButton.setOnClickListener(_cancel_onClick);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        _workOrder = payload.getParcelable(PARAM_WORKORDER);
        super.show(payload, animate);
    }

    private final View.OnClickListener _reschedule_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO, reschedule? dialog
        }
    };

    private final View.OnClickListener cancelWork_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //CancelWarningDialog.Controller.show(App.get(), _workOrder, );
            dismiss(true);
        }
    };

    private final View.OnClickListener _reportOther_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ReportProblemDialog.show(App.get(), DIALOG_REPORT_PROBLEM, _workOrder.getId());
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    public static void show(Context context, WorkOrder workOrder) {
        Bundle params = new Bundle();
        params.putParcelable(PARAM_WORKORDER, workOrder);

        Controller.show(context, null, ReportIssueDialog.class, params);
    }
}
