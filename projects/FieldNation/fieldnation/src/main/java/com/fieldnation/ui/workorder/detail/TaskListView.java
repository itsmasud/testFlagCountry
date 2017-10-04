package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TaskListView extends RelativeLayout {
    private static final String TAG = "TaskListView";

    // UI
    private LinearLayout _preVisistLayout;
    private TextView _preVisistTextView;
    private LinearLayout _preVisistList;
    private LinearLayout _onSiteLayout;
    private LinearLayout _onSiteList;
    private LinearLayout _postVisitLayout;
    private LinearLayout _postVisitList;

    // Data
    String _groupId = null;
    private List<Task> _tasks;
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

        _preVisistLayout = findViewById(R.id.preVisit_layout);
        _preVisistTextView = findViewById(R.id.previsit_textview);
        _preVisistList = findViewById(R.id.previsit_list);
        _onSiteLayout = findViewById(R.id.onSite_layout);
        _onSiteList = findViewById(R.id.onsite_list);
        _postVisitLayout = findViewById(R.id.postVisit_layout);
        _postVisitList = findViewById(R.id.postvisit_list);
    }

    public void setData(WorkOrder workOrder, String groupId) {
        _workOrder = workOrder;
        _groupId = groupId;

        if (workOrder.getTasks().getResults().length > 0)
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

        if ("prep".equals(_groupId)) {
            _preVisistLayout.setVisibility(VISIBLE);
            _onSiteLayout.setVisibility(GONE);
            _postVisitLayout.setVisibility(GONE);
        } else if ("onsite".equals(_groupId)) {
            _preVisistLayout.setVisibility(GONE);
            _onSiteLayout.setVisibility(VISIBLE);
            _postVisitLayout.setVisibility(GONE);
        } else if ("post".equals(_groupId)) {
            _preVisistLayout.setVisibility(GONE);
            _onSiteLayout.setVisibility(GONE);
            _postVisitLayout.setVisibility(VISIBLE);
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

                    if ("prep".equals(task.getGroup().getId()) && _groupId.equals(task.getGroup().getId())) {
                        if (pre < _preVisistList.getChildCount()) {
                            row = (TaskRowView) _preVisistList.getChildAt(pre);
                        } else {
                            row = new TaskRowView(getContext());
                            _preVisistList.addView(row);
                            _preVisistTextView.setVisibility(View.VISIBLE);
                        }
                        pre++;
                        row.setData(_workOrder, task);
                    }

                    if ("onsite".equals(task.getGroup().getId()) && _groupId.equals(task.getGroup().getId())) {
                        if (ons < _onSiteList.getChildCount()) {
                            row = (TaskRowView) _onSiteList.getChildAt(ons);
                        } else {
                            row = new TaskRowView(getContext());
                            _onSiteList.addView(row);
                            _onSiteLayout.setVisibility(View.VISIBLE);
                        }
                        ons++;
                        row.setData(_workOrder, task);
                    }

                    if ("post".equals(task.getGroup().getId()) && _groupId.equals(task.getGroup().getId())) {
                        if (post < _postVisitList.getChildCount()) {
                            row = (TaskRowView) _postVisitList.getChildAt(post);
                        } else {
                            row = new TaskRowView(getContext());
                            _postVisitList.addView(row);
                            _postVisitLayout.setVisibility(View.VISIBLE);
                        }
                        post++;
                        row.setData(_workOrder, task);
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
}
