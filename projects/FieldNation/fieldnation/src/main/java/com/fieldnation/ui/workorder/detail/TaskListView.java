package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;

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
    private String _groupId = null;
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

        _incompleteLayout = findViewById(R.id.incomplete_layout);
        _incompleteList = findViewById(R.id.incomplete_list);
        _completeLayout = findViewById(R.id.complete_layout);
        _completeLayout.setVisibility(View.GONE);
        _completeList = findViewById(R.id.complete_list);

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

        _incompleteList.removeAllViews();
        _completeList.removeAllViews();

        ForLoopRunnable r = new ForLoopRunnable(_tasks.size(), new Handler()) {

            @Override
            public void next(int i) throws Exception {
                if (_groupId.equals(_tasks.get(i).getGroup().getId())) {

                    final Task task = _tasks.get(i);
                    TaskRowView v = null;
                    if (task.getStatus().equals(Task.StatusEnum.COMPLETE)) {
                        if (i < _completeList.getChildCount()) {
                            v = (TaskRowView) _completeList.getChildAt(i);
                        } else {
                            v = new TaskRowView(getContext());
                            _completeList.addView(v);
                        }
                    } else {
                        if (i < _incompleteList.getChildCount()) {
                            v = (TaskRowView) _incompleteList.getChildAt(i);
                        } else {
                            v = new TaskRowView(getContext());
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
        _incompleteList.postDelayed(r, new Random().nextInt(1000));
        _completeList.postDelayed(r, new Random().nextInt(1000));
    }
}
