package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.ListItemTaskRowView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TaskListView extends RelativeLayout {
    private static final String TAG = "TaskListView";

    // UI
    private RelativeLayout _incompleteLayout;
    private RelativeLayout _completeLayout;
    private LinearLayout _incompleteList;
    private LinearLayout _completeList;


    // Data
    private int _workOrderId = 0;
    private String _groupId = null;
    private WorkOrder _workOrder;

    public TaskListView(Context context) {
        super(context);
        init();
    }

    public TaskListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaskListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        Log.e(TAG, "init");
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_task_list, this);

        if (isInEditMode())
            return;

        _incompleteLayout = findViewById(R.id.incomplete_layout);
        _incompleteList = findViewById(R.id.incomplete_list);
        _completeLayout = findViewById(R.id.complete_layout);
        _completeLayout.setVisibility(View.GONE);
        _completeList = findViewById(R.id.complete_list);
        setVisibility(GONE);
    }

    @Override
    protected void onDetachedFromWindow() {
        _workOrderApi.unsub();
        super.onDetachedFromWindow();
    }

    public void setData(int workOrderId, String groupId) {
        _workOrderId = workOrderId;
        _groupId = groupId;

        if (_workOrderId != 0)
            WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, false, false);

        _workOrderApi.sub();
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_workOrder.getTasks().getResults().length == 0)
            return;

        final List<Task> tasks = Arrays.asList(_workOrder.getTasks().getResults());
        setVisibility(View.VISIBLE);


        _incompleteList.removeAllViews();
        _completeList.removeAllViews();

        ForLoopRunnable r = new ForLoopRunnable(tasks.size(), new Handler()) {

            @Override
            public void next(int i) throws Exception {
                if (_groupId.equals(tasks.get(i).getGroup().getId())) {

                    final Task task = tasks.get(i);
                    ListItemTaskRowView v = null;
                    if (task.getStatus().equals(Task.StatusEnum.COMPLETE)) {
                        if (i < _completeList.getChildCount()) {
                            v = (ListItemTaskRowView) _completeList.getChildAt(i);
                        } else {
                            v = new ListItemTaskRowView(getContext());
                            _completeList.addView(v);
                        }
                    } else {
                        if (i < _incompleteList.getChildCount()) {
                            v = (ListItemTaskRowView) _incompleteList.getChildAt(i);
                        } else {
                            v = new ListItemTaskRowView(getContext());
                            _incompleteList.addView(v);
                        }
                    }
                    v.setData(_workOrder, task);
                }
            }

            @Override
            public void finish(int count) throws Exception {
                super.finish(count);

                if (_completeList.getChildCount() == 0)
                    _completeLayout.setVisibility(GONE);
                else _completeLayout.setVisibility(VISIBLE);

                if (_incompleteList.getChildCount() == 0)
                    _incompleteLayout.setVisibility(GONE);
                else _incompleteLayout.setVisibility(VISIBLE);
            }
        };
        _incompleteList.postDelayed(r, new Random().nextInt(100));
        _completeList.postDelayed(r, new Random().nextInt(100));
    }


    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final WorkordersWebApi _workOrderApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
//            return methodName.equals("getTasks");
            return methodName.equals("getWorkOrder") || methodName.equals("updateTask");
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (successObject != null && (methodName.equals("getWorkOrder") || methodName.equals("updateTask"))) {
                WorkOrder workOrder = (WorkOrder) successObject;
                if (success) {
                    _workOrder = workOrder;
                    populateUi();

//                    _refreshView.refreshComplete();
                }
            }
        }
    };

}
