package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.data.workorder.ReportProblemType;
import com.fieldnation.service.data.workorder.WorkorderClient;

/**
 * Created by Michael on 10/5/2016.
 */

public class CancelWarningDialog extends FullScreenDialog {
    private static final String TAG = "CancelWarningDialog";

    private static final String PARAM_WORKORDER = "workOrder";

    // Ui
    private Button _reviewTosButton;
    private Button _cancelWorkButton;
    private Button _acceptButton;

    // Data
    private WorkOrder _workOrder;

    public CancelWarningDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_cancel_warning, container, false);

        _reviewTosButton = (Button) v.findViewById(R.id.review_tos_button);
        _cancelWorkButton = (Button) v.findViewById(R.id.cancelWork_button);
        _acceptButton = (Button) v.findViewById(R.id.accept_button);

        return v;
    }

    @Override
    public void onAdded() {
        super.onAdded();

        _reviewTosButton.setOnClickListener(_reviewTos_onClick);
        _cancelWorkButton.setOnClickListener(_cancel_onClick);
        _acceptButton.setOnClickListener(_accept_onClick);
    }

    @Override
    public void show(Bundle params, boolean animate) {
        _workOrder = params.getParcelable(PARAM_WORKORDER);
        super.show(params, animate);
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    private final View.OnClickListener _reviewTos_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://app.fieldnation.com/legal/?a=provider"));
            ActivityResultClient.startActivity(App.get(), intent);
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ToastClient.toast(App.get(), "Cancel notification sent", Toast.LENGTH_SHORT);
            WorkorderClient.actionReportProblem(App.get(), _workOrder.getId(), "", ReportProblemType.CANNOT_MAKE_ASSIGNMENT);
            dismiss(true);
        }
    };

    private final View.OnClickListener _accept_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    public static class Controller extends com.fieldnation.fndialog.Controller {

        public Controller(Context context) {
            super(context, CancelWarningDialog.class);
        }

        public static void show(Context context, WorkOrder workOrder) {
            Bundle params = new Bundle();
            params.putParcelable(PARAM_WORKORDER, workOrder);

            show(context, CancelWarningDialog.class, params);
        }
    }
}
