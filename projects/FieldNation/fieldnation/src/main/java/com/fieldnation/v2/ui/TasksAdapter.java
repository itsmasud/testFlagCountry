package com.fieldnation.v2.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.v2.data.model.CustomField;
import com.fieldnation.v2.data.model.CustomFieldCategory;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shoaib on 09/10/17.
 */

public abstract class TasksAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private static final String TAG = "TasksAdapter";

    private List<DataHolder> dataHolders = new LinkedList<>();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_TASK = 1;

    // data
    private WorkOrder _workOrder = null;

    private static class DataHolder {
        int type;
        Object object;

        public DataHolder(int type, Object object) {
            this.type = type;
            this.object = object;
        }
    }


    // TODO find a way how you can put header and cell in same layout
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        dataHolders.clear();

        List<Task> incompleteTasks = new LinkedList<>();
        List<Task> completeTasks = new LinkedList<>();

        for (Task task : workOrder.getTasks().getResults()) {
            if (task.getStatus().equals(Task.StatusEnum.COMPLETE)) {
                completeTasks.add(task);
            } else incompleteTasks.add(task);
        }


        // populating incomplete list
        if (incompleteTasks.size() != 0) {
            dataHolders.add(new DataHolder(TYPE_HEADER, R.string.incomplete));
            for (Task task : incompleteTasks) {
                dataHolders.add(new DataHolder(TYPE_TASK, task));
            }
        }

        // populating complete list
        if (completeTasks.size() != 0) {
            dataHolders.add(new DataHolder(TYPE_HEADER, R.string.complete));
            for (Task task : completeTasks) {
                dataHolders.add(new DataHolder(TYPE_TASK, task));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER: {
                ListItemGroupView view = new ListItemGroupView(parent.getContext());
                return new TaskViewHolder(view);
            }
            case TYPE_TASK: {
                ListItemTaskRowView view = new ListItemTaskRowView(parent.getContext());
                return new TaskViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER: {
                ListItemGroupView view = (ListItemGroupView) holder.itemView;
                view.setTitle((String) dataHolders.get(position).object);
                break;
            }
            case TYPE_TASK: {
                ListItemTaskRowView view = (ListItemTaskRowView) holder.itemView;
                Task task = (Task) dataHolders.get(position).object;
//                view.setOnClickListener(_customField_onClick);
//                view.setOnLongClickListener(_customField_onLongClick);
//                view.set(
//                        (customField.getFlagsSet().contains(CustomField.FlagsEnum.REQUIRED) ? "* " : "")
//                                + customField.getName(),
//                        misc.isEmptyOrNull(customField.getValue()) ? customField.getTip() : customField.getValue());
                view.setTag(task);
//                view.setActionVisible(false);


                // TODO
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

    private final View.OnClickListener _customField_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onCustomFieldClicked((CustomField) view.getTag());
        }
    };

    private final View.OnLongClickListener _customField_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            CustomField customField = (CustomField) view.getTag();
            ClipboardManager clipboard = (ClipboardManager) App.get().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(customField.getName(), customField.getValue());
            clipboard.setPrimaryClip(clip);
            ToastClient.toast(App.get(), R.string.toast_copied_to_clipboard, Toast.LENGTH_LONG);
            return true;
        }
    };

    public abstract void onCustomFieldClicked(CustomField customField);
}
