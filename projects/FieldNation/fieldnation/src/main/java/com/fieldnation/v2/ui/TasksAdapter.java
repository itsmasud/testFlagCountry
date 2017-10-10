package com.fieldnation.v2.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shoaib on 09/10/17.
 */

public class TasksAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private static final String TAG = "TasksAdapter";

    private List<DataHolder> dataHolders = new LinkedList<>();

    private static final int TYPE_HEADER_INCOMPLETE = 0;
    private static final int TYPE_HEADER_COMPLETE = 1;
    private static final int TYPE_TASK = 2;

    // data
    private WorkOrder _workOrder = null;
    private String _groupId = null;

    private static class DataHolder {
        int type;
        Object object;

        public DataHolder(int type, Object object) {
            this.type = type;
            this.object = object;
        }
    }


    // TODO find a way how you can put header and cell in same layout
    public void setData(WorkOrder workOrder, String groupId) {
        _workOrder = workOrder;
        _groupId = groupId;
        dataHolders.clear();

        List<Task> incompleteTasks = new LinkedList<>();
        List<Task> completeTasks = new LinkedList<>();

        for (Task task : workOrder.getTasks().getResults()) {
            if (!_groupId.equals(task.getGroup().getId()))
                continue;

            if (task.getStatus().equals(Task.StatusEnum.COMPLETE)) {
                completeTasks.add(task);
            } else incompleteTasks.add(task);
        }


        // populating incomplete list
        if (incompleteTasks.size() != 0) {
            dataHolders.add(new DataHolder(TYPE_HEADER_INCOMPLETE, App.get().getResources().getString(R.string.incomplete)));
            for (Task task : incompleteTasks) {
                dataHolders.add(new DataHolder(TYPE_TASK, task));
            }
        }

        // populating complete list
        if (completeTasks.size() != 0) {
            dataHolders.add(new DataHolder(TYPE_HEADER_COMPLETE, App.get().getResources().getString(R.string.complete)));
            for (Task task : completeTasks) {
                dataHolders.add(new DataHolder(TYPE_TASK, task));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER_INCOMPLETE: {
                ListItemGroupView view = new ListItemGroupView(parent.getContext());
                return new TaskViewHolder(view);
            }
            case TYPE_HEADER_COMPLETE: {
                ListItemGroupView view = new ListItemGroupView(parent.getContext());
                return new TaskViewHolder(view);
            }
            case TYPE_TASK: {
                TaskRowView view = new TaskRowView(parent.getContext());
                return new TaskViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER_INCOMPLETE: {
                ListItemGroupView view = (ListItemGroupView) holder.itemView;
                view.setTitle((String) dataHolders.get(position).object);
                view.setIcon(App.get().getResources().getString(R.string.icon_x), ContextCompat.getColor(App.get(), R.color.fn_red));
                break;
            }
            case TYPE_HEADER_COMPLETE: {
                ListItemGroupView view = (ListItemGroupView) holder.itemView;
                view.setTitle((String) dataHolders.get(position).object);
                view.setIcon(App.get().getResources().getString(R.string.icon_checkmark), ContextCompat.getColor(App.get(), R.color.fn_accent_color_medium));
                break;
            }
            case TYPE_TASK: {
                TaskRowView view = (TaskRowView) holder.itemView;
                Task task = (Task) dataHolders.get(position).object;
//                view.setOnClickListener(_customField_onClick);
//                view.setOnLongClickListener(_customField_onLongClick);
//                view.set(
//                        (customField.getFlagsSet().contains(CustomField.FlagsEnum.REQUIRED) ? "* " : "")
//                                + customField.getName(),
//                        misc.isEmptyOrNull(customField.getValue()) ? customField.getTip() : customField.getValue());
                view.setTag(task);
//                view.setActionVisible(false);


                view.setData(_workOrder, task);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataHolders.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataHolders.get(position).type;
    }
}
