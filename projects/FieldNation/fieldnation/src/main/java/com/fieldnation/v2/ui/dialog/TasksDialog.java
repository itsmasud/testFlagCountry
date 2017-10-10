package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.TasksAdapter;

/**
 * Created by Shoaib on 09/09/17.
 */

public class TasksDialog extends FullScreenDialog {
    private static final String TAG = "TasksDialog";

    // Params
    private static final String PARAM_WORK_ORDER_ID = "workOrderId";
    private static final String PARAM_DIALOG_TITLE = "dialogTitle";
    private static final String PARAM_GROUP_ID = "groupId";

    // Ui
    private Toolbar _toolbar;
    private OverScrollRecyclerView _list;

    // Data
    private int _workOrderId = 0;
    private WorkOrder _workOrder;
    private String _groupId = null;
    private String _dialogTitle;
    private final TasksAdapter _adapter = new TasksAdapter();


    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public TasksDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_v2_toolbar_recycle, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);

        _list = v.findViewById(R.id.list);
        _list.setItemAnimator(new DefaultItemAnimator());
        _list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        _list.setAdapter(_adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        _workOrdersApi.sub();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);

        _workOrderId = params.getInt(PARAM_WORK_ORDER_ID);
        _dialogTitle = params.getString(PARAM_DIALOG_TITLE);
        _groupId = params.getString(PARAM_GROUP_ID);
        super.show(params, animate);
        populateUi();

        AppMessagingClient.setLoading(true);
        WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, true, false);
        populateUi();
    }

    private void populateUi() {
        if (_list == null) return;

        if (_workOrder == null) return;

        _toolbar.setTitle(_dialogTitle);

        _adapter.setData(_workOrder, _groupId);
    }

    @Override
    public void onPause() {
        _workOrdersApi.unsub();
        super.onPause();
    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss(true);
        }
    };

    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.equals("getWorkOrder")
                    || methodName.equals("updateTask");
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (successObject != null && (methodName.equals("getWorkOrder"))) {
                WorkOrder workOrder = (WorkOrder) successObject;
                if (success) {
                    _workOrder = workOrder;
                    populateUi();
                    AppMessagingClient.setLoading(false);
                }
            } else if (methodName.equals("updateTask")) {
                WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, true, false);
                AppMessagingClient.setLoading(true);
            }

        }
    };

    /**
     * @param context
     * @param uid
     * @param workOrderId
     * @param groupId
     * @param dialogTitle
     */
    public static void show(Context context, String uid, int workOrderId, String groupId, String dialogTitle) {
        Bundle params = new Bundle();
        params.putInt(PARAM_WORK_ORDER_ID, workOrderId);
        params.putString(PARAM_GROUP_ID, groupId);
        params.putString(PARAM_DIALOG_TITLE, dialogTitle);

        Controller.show(context, uid, TasksDialog.class, params);
    }
}
