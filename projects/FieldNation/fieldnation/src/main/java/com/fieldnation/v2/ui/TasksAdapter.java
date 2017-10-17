package com.fieldnation.v2.ui;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.Tasks;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shoaib on 09/10/17.
 */

public class TasksAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private static final String TAG = "TasksAdapter";

    private List<DataHolder> dataHolders = new LinkedList<>();

    private final int TYPE_HEADER_INCOMPLETE = 0;
    private final int TYPE_HEADER_COMPLETE = 1;
    private final int TYPE_TASK = 2;
    private final int TYPE_TASK_UPLOAD = 3;
    private final int TYPE_TASK_DOWNLOAD = 4;

    // data
    private Tasks _tasks = null;
    private String _groupId;
    private Listener _listener;

    private static class DataHolder {
        int type;
        Object object;
        UploadTuple uObject;
        DownloadTuple dObject;

        public DataHolder(int type, Object object) {
            this.type = type;
            this.object = object;
            this.uObject = null;
        }

        public DataHolder(int type, Object object, UploadTuple uObject) {
            this.type = type;
            this.object = object;
            this.uObject = uObject;
        }

        public DataHolder(int type, Object object, DownloadTuple dObject) {
            this.type = type;
            this.object = object;
            this.dObject = dObject;
        }

    }

    public void setData(Tasks tasks, String groupId) {
        _tasks = tasks;
        _groupId = groupId;
        dataHolders.clear();

        rebuild();
        notifyDataSetChanged();
    }

    public void setListener(TasksAdapter.Listener listener) {
        _listener = listener;
    }

    private void rebuild() {
        if (_tasks == null || misc.isEmptyOrNull(_groupId))
            return;

        final List<Task> incompleteTasks = new LinkedList<>();
        final List<Task> completeTasks = new LinkedList<>();

        for (Task task : _tasks.getResults()) {
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
                boolean match = false;

                if (task.getAttachments().getId() != null) {
                    for (UploadTuple ut : uploads) {
                        if (ut.folderId == task.getAttachments().getId()) {
                            dataHolders.add(new DataHolder(TYPE_TASK_UPLOAD, task, ut));
                            match = true;
                            break;
                        }
                    }
                }

                if (match) continue;

                // sensing downloading
                if (task.getAttachment().getId() != null) {
                    DownloadTuple tuple = new DownloadTuple();
                    tuple.attachmentId = task.getAttachment().getId();

                    for (DownloadTuple dt : downloads) {
                        if (dt.attachmentId == task.getAttachment().getId()) {
                            tuple.downloading = true;
                            break;
                        }
                    }

                    dataHolders.add(new DataHolder(TYPE_TASK_DOWNLOAD, task, tuple));
                    continue;
                }

                dataHolders.add(new DataHolder(TYPE_TASK, task));
            }
        }

        // populating complete list
        if (completeTasks.size() != 0) {
            dataHolders.add(new DataHolder(TYPE_HEADER_COMPLETE, App.get().getResources().getString(R.string.complete)));
            for (Task task : completeTasks) {
                boolean match = false;

                if (task.getAttachments().getId() != null) {
                    for (UploadTuple ut : uploads) {
                        if (ut.folderId == task.getAttachments().getId()) {
                            dataHolders.add(new DataHolder(TYPE_TASK_UPLOAD, task, ut));
                            match = true;
                            break;
                        }
                    }
                }

                if (match) continue;

                // sensing downloading
                if (task.getAttachment().getId() != null) {
                    DownloadTuple tuple = new DownloadTuple();
                    tuple.attachmentId = task.getAttachment().getId();

                    for (DownloadTuple dt : downloads) {
                        if (dt.attachmentId == task.getAttachment().getId()) {
                            tuple.downloading = true;
                            break;
                        }
                    }

                    dataHolders.add(new DataHolder(TYPE_TASK_DOWNLOAD, task, tuple));
                    continue;
                }

                dataHolders.add(new DataHolder(TYPE_TASK, task));
            }
        }
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

            case TYPE_TASK_UPLOAD: {
                TaskRowView view = new TaskRowView(parent.getContext());
                return new TaskViewHolder(view);
            }

            case TYPE_TASK_DOWNLOAD: {
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
                view.setTag(task);
                view.setOnClickListener(_task_onClick);
                view.setData(task);
                break;
            }

            case TYPE_TASK_UPLOAD: {
                TaskRowView view = (TaskRowView) holder.itemView;
                Task task = (Task) dataHolders.get(position).object;
                view.setTag(task);
                view.setOnClickListener(null);
                view.setData(task);

                UploadTuple ut = dataHolders.get(position).uObject;
                view.setProgress(ut.progress);

                break;
            }

            case TYPE_TASK_DOWNLOAD: {
                TaskRowView view = (TaskRowView) holder.itemView;
                Task task = (Task) dataHolders.get(position).object;
                view.setTag(task);
                view.setData(task);

                if (dataHolders.get(position).dObject.downloading) {
                    view.setProgressVisible(true);
                    view.setOnClickListener(null);
                } else {
                    view.setProgressVisible(false);
                    view.setOnClickListener(_task_onClick);
                }

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


    public TaskTypeEnum getType(Task task) {
        return TaskTypeEnum.fromTypeId(task.getType().getId());
    }

    /*-*********+****************-*/
    /*-         Uploads          -*/
    /*-**************************-*/

    private static class UploadTuple {
        long timestamp;
        int folderId;
        String name;
        String notes;
        int progress = -1;
        int index = -1;
        TransactionParams transactionParams;

        public UploadTuple(TransactionParams transactionParams, int progress) {
            try {
                JsonObject methodParams = new JsonObject(transactionParams.methodParams);
                this.folderId = methodParams.getInt("folderId");
                this.name = methodParams.getString("attachment.file.name");
                this.notes = methodParams.has("attachment.notes") ? methodParams.getString("attachment.notes") : "";
                this.timestamp = methodParams.getLong("timestamp");
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

            this.progress = progress;
            this.transactionParams = transactionParams;
        }
    }

    private List<UploadTuple> uploads = new LinkedList<>();

    public void uploadClear() {
        uploads.clear();
        rebuild();
        notifyDataSetChanged();
    }

    public void uploadStart(TransactionParams transactionParams) {
        Log.v(TAG, "uploadStart");

        String name = null;
        try {
            JsonObject methodParams = new JsonObject(transactionParams.methodParams);
            name = methodParams.getString("attachment.file.name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        for (UploadTuple ut : uploads) {
            if (ut.name.equals(name)) {
                rebuild();
                notifyDataSetChanged();
                return;
            }
        }
        UploadTuple t = new UploadTuple(transactionParams, -1);
        uploads.add(t);
        // TODO find position and notify accordingly
        rebuild();
        notifyDataSetChanged();
    }

    public void uploadProgress(TransactionParams transactionParams, int progress) {
        Log.v(TAG, "uploadProgress");

        String name = null;
        try {
            JsonObject methodParams = new JsonObject(transactionParams.methodParams);
            name = methodParams.getString("attachment.file.name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        for (UploadTuple ut : uploads) {
            if (ut.name.equals(name)) {
                ut.progress = progress;
                rebuild();
                notifyDataSetChanged();
                return;
            }
        }
        UploadTuple t = new UploadTuple(transactionParams, progress);
        uploads.add(t);
        // TODO find location and notify accordingly
        rebuild();
        notifyDataSetChanged();
    }

    public void uploadStop(TransactionParams transactionParams) {
        Log.v(TAG, "uploadStop");

        String name = null;
        try {
            JsonObject methodParams = new JsonObject(transactionParams.methodParams);
            name = methodParams.getString("attachment.file.name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        UploadTuple t = null;
        for (UploadTuple ut : uploads) {
            if (ut.name.equals(name)) {
                t = ut;
                break;
            }
        }
        uploads.remove(t);
        // TODO find location and upload accordingly
        rebuild();
        notifyDataSetChanged();
    }


    /*-*****************************-*/
    /*-         Downloads           -*/
    /*-*****************************-*/
    private List<DownloadTuple> downloads = new LinkedList<>();

    private static class DownloadTuple {
        int attachmentId;
        public boolean downloading = false;
    }

    public void downloadStart(int attachmentId) {

        // catching duplicate request
        for (DownloadTuple dt : downloads) {
            if (dt.attachmentId == attachmentId) {
                rebuild();
                notifyDataSetChanged();
                return;
            }
        }

        for (int i = 0; i < dataHolders.size(); i++) {
            DownloadTuple tuple = dataHolders.get(i).dObject;
            if (tuple == null) continue;

            if (tuple.attachmentId == attachmentId) {
                tuple.downloading = true;
                notifyItemChanged(i);
                downloads.add(tuple);
                break;
            }
        }
    }

    public void downloadComplete(int attachmentId) {
        for (int i = 0; i < dataHolders.size(); i++) {
            DownloadTuple tuple = dataHolders.get(i).dObject;
            if (tuple == null) continue;

            if (tuple.attachmentId == attachmentId) {
                tuple.downloading = false;
                notifyItemChanged(i);
                downloads.remove(tuple);
                return;
            }
        }
    }

    private final View.OnClickListener _task_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Task task = (Task) v.getTag();
            if (_listener != null)
                _listener.onTaskClick(v, task);
        }
    };

    public interface Listener {
        void onTaskClick(View view, Task task);
    }
}
