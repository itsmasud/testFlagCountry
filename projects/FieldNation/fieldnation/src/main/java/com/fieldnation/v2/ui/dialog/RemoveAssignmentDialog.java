package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.R;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;

public class RemoveAssignmentDialog extends FullScreenDialog {
    private static final String TAG = "RemoveAssignmentDialog";

    // Ui

    public RemoveAssignmentDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_remove_assignment, container, false);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _workOrderClient.sub();

        WorkordersWebApi.getAssignmentCancelReasons(getContext(), true, WebTransaction.Type.NORMAL);
    }

    @Override
    public void onStop() {
        _workOrderClient.unsub();
        super.onStop();
    }

    private final WorkordersWebApi _workOrderClient = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(UUIDGroup uuid, TransactionParams transactionParams, String methodName) {
            return methodName.equals("getAssignmentCancelReasons");
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            Log.v(TAG, "Got reasons!!");
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    public static void show(Context context, String uid) {
        Controller.show(context, uid, RemoveAssignmentDialog.class, null);
    }
}
