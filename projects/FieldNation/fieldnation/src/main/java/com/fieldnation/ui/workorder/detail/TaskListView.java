package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.ForLoopRunnable;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;

import java.util.List;

public class TaskListView extends RelativeLayout {
    private static final String TAG = "ui.workorder.detail.TaskListView";

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
    private Workorder _workorder;

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

    public void setData(Workorder workorder, List<Task> tasks) {
        _tasks = tasks;
        _workorder = workorder;

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

        boolean nocategories = misc.isEmptyOrNull(_tasks.get(0).getStage()) || "any".equals(_tasks.get(0).getStage());

        if (nocategories) {
            _onSiteList.removeAllViews();
            _postVisitList.removeAllViews();
            _preVisistTextView.setVisibility(View.GONE);
            _onSiteLayout.setVisibility(View.GONE);
            _postVisitLayout.setVisibility(View.GONE);

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
                    row.setData(_workorder, task);
                    if (_workorder.canModifyTasks()) {
                        row.setOnTaskClickListener(_task_onClick);
                    }
                }

                @Override
                public void finish(int count) throws Exception {
                    if (_preVisistList.getChildCount() > count) {
                        _preVisistList.removeViews(count - 1, count - _preVisistList.getChildCount());
                    }
                }
            };
            post(r);
        } else {
            ForLoopRunnable r = new ForLoopRunnable(_tasks.size(), new Handler()) {
                private int pre = 0;
                private int ons = 0;
                private int post = 0;

                @Override
                public void next(int i) throws Exception {
                    Task task = _tasks.get(i);
                    TaskRowView row = null;

                    if ("prep".equals(task.getStage())) {
                        if (pre < _preVisistList.getChildCount()) {
                            row = (TaskRowView) _preVisistList.getChildAt(pre);
                        } else {
                            row = new TaskRowView(getContext());
                            _preVisistList.addView(row);
                            _preVisistTextView.setVisibility(View.VISIBLE);
                        }
                        pre++;
                    } else if ("onsite".equals(task.getStage())) {
                        if (ons < _onSiteList.getChildCount()) {
                            row = (TaskRowView) _onSiteList.getChildAt(ons);
                        } else {
                            row = new TaskRowView(getContext());
                            _onSiteList.addView(row);
                            _onSiteLayout.setVisibility(View.VISIBLE);
                        }
                        ons++;
                    } else if ("post".equals(task.getStage())) {
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
                        row.setData(_workorder, task);
                        //if work order completed or canceled then hide/disable any controls actions
                        if (_workorder.canModifyTasks()) {
                            row.setOnTaskClickListener(_task_onClick);
                        }
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
            post(r);
        }
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private TaskRowView.Listener _task_onClick = new TaskRowView.Listener() {
        @Override
        public void onTaskClick(Task task) {
            if (_listener != null) {
                switch (task.getTaskType()) {
                    case CONFIRM_ASSIGNMENT:
                        _listener.onConfirmAssignment(task);
                        break;
                    case CLOSE_OUT_NOTES:
                        _listener.onCloseOutNotes(task);
                        break;
                    case CHECKIN:
                        _listener.onCheckin(task);
                        break;
                    case CHECKOUT:
                        _listener.onCheckout(task);
                        break;
                    case UPLOAD_FILE:
                        _listener.onUploadFile(task);
                        break;
                    case UPLOAD_PICTURE:
                        _listener.onUploadPicture(task);
                        break;
                    case CUSTOM_FIELD:
                        _listener.onCustomField(task);
                        break;
                    case PHONE:
                        _listener.onPhone(task);
                        break;
                    case EMAIL:
                        _listener.onEmail(task);
                        break;
                    case UNIQUE_TASK:
                        _listener.onUniqueTask(task);
                        break;
                    case SIGNATURE:
                        _listener.onSignature(task);
                        break;
                    case SHIPMENT_TRACKING:
                        _listener.onShipment(task);
                        break;
                    case DOWNLOAD:
                        _listener.onDownload(task);
                        break;
                }
            }
        }
    };

    public interface Listener {
        public void onCheckin(Task task);

        public void onCheckout(Task task);

        public void onCloseOutNotes(Task task);

        public void onConfirmAssignment(Task task);

        public void onCustomField(Task task);

        public void onDownload(Task task);

        public void onEmail(Task task);

        public void onPhone(Task task);

        public void onShipment(Task task);

        public void onSignature(Task task);

        public void onUploadFile(Task task);

        public void onUploadPicture(Task task);

        public void onUniqueTask(Task task);
    }
}
