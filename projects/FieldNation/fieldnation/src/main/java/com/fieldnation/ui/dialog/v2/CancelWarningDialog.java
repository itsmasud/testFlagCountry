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
import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.analytics.contexts.SpWorkOrderContext;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Problem;

/**
 * Created by Michael on 10/5/2016.
 */

public class CancelWarningDialog extends FullScreenDialog {
    private static final String TAG = "CancelWarningDialog";

    private static final String PARAM_PROBLEM = "problem";
    private static final String PARAM_WORKORDER_ID = "workOrderId";

    // Ui
    private Button _reviewTosButton;
    private Button _cancelWorkButton;
    private Button _acceptButton;

    // Data
    private int _workOrderId;
    private Problem _problem;

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
    public void onStart() {
        super.onStart();

        _reviewTosButton.setOnClickListener(_reviewTos_onClick);
        _cancelWorkButton.setOnClickListener(_cancel_onClick);
        _acceptButton.setOnClickListener(_accept_onClick);
    }

    @Override
    public void show(Bundle params, boolean animate) {
        _workOrderId = params.getInt(PARAM_WORKORDER_ID);
        _problem = params.getParcelable(PARAM_PROBLEM);

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
            intent.setData(Uri.parse("https://app.fieldnation.com/legal/?a=qualityassurance"));
            ActivityResultClient.startActivity(App.get(), intent);
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ToastClient.toast(App.get(), "Cancel notification sent", Toast.LENGTH_SHORT);

            WorkordersWebApi.addProblem(App.get(), _workOrderId, _problem, App.get().getSpUiContext());
            dismiss(true);
        }
    };

    private final View.OnClickListener _accept_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Tracker.event(App.get(), new CustomEvent.Builder()
                    .addContext(new SpWorkOrderContext.Builder().workOrderId((int) _workOrderId).build())
                    .addContext(new SpUIContext.Builder()
                            .elementAction("Click")
                            .elementIdentity("Abort Cancel Work Order")
                            .elementType("Button")
                            .page("Cancel Warning Dialog")
                            .build())
                    .build()
            );
            dismiss(true);
        }
    };

    public static void show(Context context, String uid, int workOrderId, Problem problem) {
        Bundle params = new Bundle();
        params.putInt(PARAM_WORKORDER_ID, workOrderId);
        params.putParcelable(PARAM_PROBLEM, problem);
        Controller.show(context, uid, CancelWarningDialog.class, params);
    }
}
