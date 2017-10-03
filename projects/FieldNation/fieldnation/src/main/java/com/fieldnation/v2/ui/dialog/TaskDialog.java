package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.ui.workorder.detail.TaskListView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shoaib on 09/27/17.
 */

public class TaskDialog extends FullScreenDialog {
    private static final String TAG = "TaskDialog";

    // State

    // Params
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_WORK_ORDER_ID = "workOrderId";
    private static final String PARAM_DIALOG_TITLE = "finishActivity";

    // Ui
    private Toolbar _toolbar;
    private TaskListView _taskList;

    // Data
    private int _workOrderId = 0;
    private String _dialogTitle;
    private List<Task> tasks = new LinkedList<>();

    public TaskDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_task, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);

        _taskList = v.findViewById(R.id.tasks_list);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
    }

    @Override
    public void onResume() {
        super.onResume();
        _workOrdersApi.sub();
    }

    @Override
    public void onStop() {
        super.onStop();
        _workOrdersApi.unsub();

    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        super.onRestoreDialogState(savedState);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        _workOrderId = payload.getInt(PARAM_WORK_ORDER_ID);
        _dialogTitle = payload.getString(PARAM_DIALOG_TITLE);

        super.show(payload, animate);

        if (_workOrderId != 0) {
//            WorkordersWebApi.getTasks(App.get(), _workOrderId, false, false);
            WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, false, false);
        }

        populateUi();
    }

    private void populateUi() {
        _toolbar.setTitle(_dialogTitle);

    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        super.onSaveDialogState(outState);
    }

    @Override
    public void cancel() {
        super.cancel();
        _onCanceledDispatcher.dispatch(getUid());
    }

    private void onDeclined() {
        _onDeclinedDispatcher.dispatch(getUid(), _workOrderId);
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            cancel();
            dismiss(true);
        }
    };


    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            Log.e(TAG, "processTransaction");
//            return methodName.equals("getTasks");
            return methodName.equals("getWorkOrder");
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            Log.e(TAG, "onComplete");
            if (successObject != null && methodName.equals("getWorkOrder")) {
                WorkOrder workOrder = (WorkOrder) successObject;

                if (success) {
                    Log.e(TAG, "success");
                    _taskList.setWorkOrder(workOrder);
//                    dismiss(true);

//                    _refreshView.refreshComplete();
                }
            }
        }
    };


    /**
     * @param context
     * @param uid
     * @param workOrderId
     * @param dialogTitle
     */
    public static void show(Context context, String uid, int workOrderId, String dialogTitle) {
        Bundle params = new Bundle();
        params.putInt(PARAM_WORK_ORDER_ID, workOrderId);
        params.putString(PARAM_DIALOG_TITLE, dialogTitle);

        Controller.show(context, uid, TaskDialog.class, params);
    }

    /*-****************************-*/
    /*-         Declined           -*/
    /*-****************************-*/
    public interface OnDeclinedListener {
        void onDeclined(int workOrderId);
    }

    private static KeyedDispatcher<OnDeclinedListener> _onDeclinedDispatcher = new KeyedDispatcher<OnDeclinedListener>() {
        @Override
        public void onDispatch(OnDeclinedListener listener, Object... parameters) {
            listener.onDeclined((Integer) parameters[0]);
        }
    };

    public static void addOnDeclinedListener(String uid, OnDeclinedListener onDeclinedListener) {
        _onDeclinedDispatcher.add(uid, onDeclinedListener);
    }

    public static void removeOnDeclinedListener(String uid, OnDeclinedListener onDeclinedListener) {
        _onDeclinedDispatcher.remove(uid, onDeclinedListener);
    }

    public static void removeAllOnDeclinedListener(String uid) {
        _onDeclinedDispatcher.removeAll(uid);
    }

    /*-****************************-*/
    /*-         Canceled           -*/
    /*-****************************-*/
    public interface OnCanceledListener {
        void onCanceled();
    }

    private static KeyedDispatcher<OnCanceledListener> _onCanceledDispatcher = new KeyedDispatcher<OnCanceledListener>() {
        @Override
        public void onDispatch(OnCanceledListener listener, Object... parameters) {
            listener.onCanceled();
        }
    };

    public static void addOnCanceledListener(String uid, OnCanceledListener onCanceledListener) {
        _onCanceledDispatcher.add(uid, onCanceledListener);
    }

    public static void removeOnCanceledListener(String uid, OnCanceledListener onCanceledListener) {
        _onCanceledDispatcher.remove(uid, onCanceledListener);
    }

    public static void removeAllOnCanceledListener(String uid) {
        _onCanceledDispatcher.removeAll(uid);
    }

}
