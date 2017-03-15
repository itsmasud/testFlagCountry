package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TaskListView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "TaskListView";

    // UI
    private TextView _preVisistTextView;
    private LinearLayout _preVisistList;
    private LinearLayout _onSiteLayout;
    private LinearLayout _onSiteList;
    private LinearLayout _postVisitLayout;
    private LinearLayout _postVisitList;

    // Data
    private List<Task> _tasks;
    private Listener _listener;
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
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_task_list, this);

        if (isInEditMode())
            return;

        _preVisistTextView = (TextView) findViewById(R.id.previsit_textview);
        _preVisistList = (LinearLayout) findViewById(R.id.previsit_list);
        _onSiteLayout = (LinearLayout) findViewById(R.id.onsite_layout);
        _onSiteList = (LinearLayout) findViewById(R.id.onsite_list);
        _postVisitLayout = (LinearLayout) findViewById(R.id.postvisit_layout);
        _postVisitList = (LinearLayout) findViewById(R.id.postvisit_list);
    }

    public void setTaskListViewListener(TaskListView.Listener l) {
        _listener = l;
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        _tasks = Arrays.asList(workOrder.getTasks().getResults());

        populateUi();
    }


    private void populateUi() {
        if (_tasks == null)
            return;

        setVisibility(View.VISIBLE);

        if (_tasks.size() == 0) {
            setVisibility(View.GONE);
            return;
        } else {
            setVisibility(View.VISIBLE);
        }

        boolean nocategories = _tasks.get(0).getGroup() == null || "any".equals(_tasks.get(0).getGroup().getId());

        if (nocategories) {
            _onSiteList.removeAllViews();
            _postVisitList.removeAllViews();
            _preVisistTextView.setVisibility(View.GONE);
            _onSiteLayout.setVisibility(View.GONE);
            _postVisitLayout.setVisibility(View.GONE);

            if (_preVisistList.getChildCount() > _tasks.size()) {
                _preVisistList.removeViews(_tasks.size() - 1, _preVisistList.getChildCount() - _tasks.size());
            }

            ForLoopRunnable r = new ForLoopRunnable(_tasks.size(), new Handler()) {
                @Override
                public void next(int i) throws Exception {
                    TaskRowView row = null;
                    if (i < _preVisistList.getChildCount()) {
                        row = (TaskRowView) _preVisistList.getChildAt(i);
                    } else {
                        row = new TaskRowView(getContext());
                        _preVisistList.addView(row);
                    }

                    Task task = _tasks.get(i);
                    row.setData(_workOrder, task);
                    row.setOnTaskClickListener(_task_onClick);
                }
            };
            postDelayed(r, new Random().nextInt(1000));
        } else {
            ForLoopRunnable r = new ForLoopRunnable(_tasks.size(), new Handler()) {
                private int pre = 0;
                private int ons = 0;
                private int post = 0;

                @Override
                public void next(int i) throws Exception {
                    Task task = _tasks.get(i);
                    TaskRowView row = null;

                    if ("prep".equals(task.getGroup().getId())) {
                        if (pre < _preVisistList.getChildCount()) {
                            row = (TaskRowView) _preVisistList.getChildAt(pre);
                        } else {
                            row = new TaskRowView(getContext());
                            _preVisistList.addView(row);
                            _preVisistTextView.setVisibility(View.VISIBLE);
                        }
                        pre++;
                    } else if ("onsite".equals(task.getGroup().getId())) {
                        if (ons < _onSiteList.getChildCount()) {
                            row = (TaskRowView) _onSiteList.getChildAt(ons);
                        } else {
                            row = new TaskRowView(getContext());
                            _onSiteList.addView(row);
                            _onSiteLayout.setVisibility(View.VISIBLE);
                        }
                        ons++;
                    } else if ("post".equals(task.getGroup().getId())) {
                        if (post < _postVisitList.getChildCount()) {
                            row = (TaskRowView) _postVisitList.getChildAt(post);
                        } else {
                            row = new TaskRowView(getContext());
                            _postVisitList.addView(row);
                            _postVisitLayout.setVisibility(View.VISIBLE);
                        }
                        post++;
                    }

                    if (row != null) {
                        row.setData(_workOrder, task);
                        row.setOnTaskClickListener(_task_onClick);
                    } else {
                        // TODO this should never happen!
                    }
                }

                @Override
                public void finish(int count) throws Exception {
                    if (_preVisistList.getChildCount() > pre) {
                        _preVisistList.removeViews(pre, pre - _preVisistList.getChildCount());
                    }
                    if (_onSiteList.getChildCount() > ons) {
                        _onSiteList.removeViews(ons, ons - _onSiteList.getChildCount());
                    }
                    if (_postVisitList.getChildCount() > post) {
                        _postVisitList.removeViews(post, post - _postVisitList.getChildCount());
                    }
                }
            };
            postDelayed(r, new Random().nextInt(1000));
        }
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final TaskRowView.Listener _task_onClick = new TaskRowView.Listener() {
        @Override
        public void onTaskClick(Task task) {
            if (_listener != null) {
                WorkOrderTracker.onTaskEvent(App.get(), task.getType(), _workOrder.getWorkOrderId());
                switch (task.getType().getId()) {

                    case 1:
                        _listener.onSetEta(task);
                        break;
                    case 2:
                        _listener.onCloseOutNotes(task);
                        break;
                    case 3:
                        _listener.onCheckin(task);
                        break;
                    case 4:
                        _listener.onCheckout(task);
                        break;
                    case 5:
                        _listener.onUploadFile(task);
                        break;
                    case 6:
                        _listener.onUploadPicture(task);
                        break;
                    case 7:
                        _listener.onCustomField(task);
                        break;
                    case 8:
                        _listener.onPhone(task);
                        break;
                    case 9:
                        _listener.onEmail(task);
                        break;
                    case 10:
                        _listener.onUniqueTask(task);
                        break;
                    case 11:
                        _listener.onSignature(task);
                        break;
                    case 12:
                        _listener.onShipment(task);
                        break;
                    case 13:
                        _listener.onDownload(task);
                        break;
                }
            }
        }
    };

    public interface Listener {
        void onCheckin(Task task);

        void onCheckout(Task task);

        void onCloseOutNotes(Task task);

        void onSetEta(Task task);

        void onCustomField(Task task);

        void onDownload(Task task);

        void onEmail(Task task);

        void onPhone(Task task);

        void onShipment(Task task);

        void onSignature(Task task);

        void onUploadFile(Task task);

        void onUploadPicture(Task task);

        void onUniqueTask(Task task);
    }
}
