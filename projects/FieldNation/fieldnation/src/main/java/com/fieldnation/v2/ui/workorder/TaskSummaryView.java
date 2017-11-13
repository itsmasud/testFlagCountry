package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.CustomField;
import com.fieldnation.v2.data.model.CustomFieldCategory;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.CustomFieldsDialog;
import com.fieldnation.v2.ui.dialog.TasksDialog;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shoaib on 09/24/17.
 */

public class TaskSummaryView extends RelativeLayout implements WorkOrderRenderer, UUIDView {
    private static final String TAG = "TaskSummaryView";

    // Ui
    private LinearLayout _tasksList;
    private TaskSummaryRow _customFieldsView;

    // Data
    private WorkOrder _workOrder;
    private String _myUUID;

    public TaskSummaryView(Context context) {
        super(context);
        init();
    }

    public TaskSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaskSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_task_summary, this);

        if (isInEditMode())
            return;

        _tasksList = findViewById(R.id.tasks_list);

        _customFieldsView = findViewById(R.id.customFields_view);

        setVisibility(GONE);

        populateUi();
    }

    private static class Group {
        String id;
        String name;
        int total = 0;
        int completed = 0;

        public Group(String id, String name) {
            this.id = id;
            this.name = misc.capitalizeWords(name);
        }
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    public void setUUID(String uuid) {
        _myUUID = uuid;
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_workOrder.getTasks().getResults().length == 0)
            return;

        if (_tasksList == null)
            return;

        setVisibility(VISIBLE);

        List<Group> groups = new LinkedList<>();

        Task[] tasks = _workOrder.getTasks().getResults();
        for (Task task : tasks) {

            Group group = null;
            for (Group gr : groups) {
                if (gr.id.equals(task.getGroup().getId())) {
                    group = gr;
                }
            }
            if (group == null) {
                group = new Group(task.getGroup().getId(), task.getGroup().getLabel());
                groups.add(group);
            }

            if (task.getStatus().equals(Task.StatusEnum.COMPLETE)) {
                group.completed++;
            }
            group.total++;
        }

        _tasksList.removeAllViews();
        for (Group group : groups) {
            TaskSummaryRow view = new TaskSummaryRow(getContext());
            view.setTitle(group.name);
            view.setTag(group);
            view.setOnClickListener(_task_onClick);

            if (_workOrder.getStatus().getId() == 2 || _workOrder.getStatus().getId() == 9) {
                view.setCount(String.valueOf(group.total));
                view.setCountBg(R.drawable.round_rect_gray);
            } else if (_workOrder.getStatus().getId() == 3) {
                view.setCount(String.valueOf(group.completed) + "/" + String.valueOf(group.total));
                view.setCountBg(group.total == group.completed ? R.drawable.round_rect_green : R.drawable.round_rect_red);
            } else {
                view.setCount(String.valueOf(group.completed) + "/" + String.valueOf(group.total));
                view.setCountBg(R.drawable.round_rect_gray);
            }

            _tasksList.addView(view);
        }

        // custom fields
        if (_workOrder.getCustomFields().getResults().length == 0) {
            _customFieldsView.setVisibility(GONE);
            return;
        } else {
            _customFieldsView.setVisibility(VISIBLE);
        }

        int fteTotal = 0;
        int fteComplete = 0;
        int fteRequired = 0;
        int fteRequiredComplete = 0;
//        editable = false;

        for (CustomFieldCategory category : _workOrder.getCustomFields().getResults()) {
            if (category.getRole().equals("buyer"))
                continue;

            CustomField[] customFields = category.getResults();
            for (CustomField customField : customFields) {

//                editable = editable || customField.getActionsSet().contains(CustomField.ActionsEnum.EDIT);

                if (customField.getFlagsSet().contains(CustomField.FlagsEnum.REQUIRED))
                    fteRequired++;

                if (!misc.isEmptyOrNull(customField.getValue())) {
                    fteComplete++;

                    if (customField.getFlagsSet().contains(CustomField.FlagsEnum.REQUIRED))
                        fteRequiredComplete++;
                }
                fteTotal++;
            }
        }

        _customFieldsView.setTitle(getResources().getString(R.string.fields_to_enter));

        if (fteRequired == 0) {
            _customFieldsView.setOnClickListener(_fte_onClick);
            return;
        }

        if (_workOrder.getStatus().getId() == 2) {
            _customFieldsView.setCountBg(R.drawable.round_rect_gray);
            _customFieldsView.setCount(String.valueOf(fteTotal));
            _customFieldsView.setOnClickListener(null);

        } else if (_workOrder.getStatus().getId() == 3) {
            _customFieldsView.setOnClickListener(_fte_onClick);
            _customFieldsView.setCount(String.valueOf(fteRequiredComplete) + "/" + String.valueOf(fteRequired));
            _customFieldsView.setCountBg(fteRequired == fteRequiredComplete ? R.drawable.round_rect_green : R.drawable.round_rect_red);
        } else {
            _customFieldsView.setCountBg(R.drawable.round_rect_gray);
            _customFieldsView.setCount(String.valueOf(fteRequiredComplete) + "/" + String.valueOf(fteRequired));
            _customFieldsView.setOnClickListener(_fte_onClick);

        }
    }

    private final OnClickListener _task_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Group group = (Group) v.getTag();
            TasksDialog.show(App.get(), null, _myUUID, _workOrder.getId(), group.id, group.name);
        }
    };

    private final OnClickListener _fte_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            CustomFieldsDialog.show(App.get(), null, _workOrder.getId());
        }
    };
}
