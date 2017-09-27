package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.AttachmentFolders;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 8/3/17.
 */

public class AttachedFilesAdapter extends RecyclerView.Adapter<AttachedFilesViewHolder> {
    private static final String TAG = "AttachmentFoldersAdapter";

    private AttachmentFolders folders = null;
    private List<Tuple> objects = new LinkedList<>();
    private Listener _listener;

    public void setListener(Listener listener) {
        _listener = listener;
    }

    /*-*****************************-*/
    /*-         Downloads           -*/
    /*-*****************************-*/
    public void downloadStart(int attachmentId) {
        for (int i = 0; i < objects.size(); i++) {
            Tuple tuple = objects.get(i);
            if (tuple.type == AttachedFilesViewHolder.TYPE_ATTACHMENT) {
                Attachment attachment = (Attachment) tuple.object;
                if (attachment.getId() == attachmentId) {
                    tuple.downloading = true;
                    notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    public void downloadProgress(int attachmentId, int progress) {
    }

    public void downloadComplete(int attachmentId) {
        for (int i = 0; i < objects.size(); i++) {
            Tuple tuple = objects.get(i);
            if (tuple.type == AttachedFilesViewHolder.TYPE_ATTACHMENT) {
                Attachment attachment = (Attachment) tuple.object;
                if (attachment.getId() == attachmentId) {
                    tuple.downloading = false;
                    notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    /*-*********+***********************-*/
    /*-         Failed Uploads          -*/
    /*-*********************************-*/
    private static class FailedUploadTuple {
        long timestamp;
        int folderId;
        String name;
        String notes;
        WebTransaction transaction;
        UploadTuple ut = null;

        public FailedUploadTuple(WebTransaction webTransaction) {
            this.transaction = webTransaction;
            try {
                TransactionParams params = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));
                JsonObject methodParams = new JsonObject(params.methodParams);

                this.folderId = methodParams.getInt("folderId");
                this.name = methodParams.getString("attachment.file.name");
                this.notes = methodParams.has("attachment.notes") ? methodParams.getString("attachment.notes") : "";
                this.timestamp = methodParams.getLong("timestamp");
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    }

    private List<FailedUploadTuple> failedUploads = new LinkedList<>();

    public void setFailedUploads(List<WebTransaction> webTransactions) {
        failedUploads.clear();

        if (webTransactions == null || webTransactions.size() == 0) {
            rebuild();
            notifyDataSetChanged();
            return;
        }

        for (WebTransaction webTransaction : webTransactions) {
            try {
                FailedUploadTuple ft = new FailedUploadTuple(webTransaction);
                for (UploadTuple ut : uploads) {
                    if (ft.name.equals(ut.name)) {
                        ft.ut = ut;
                        break;
                    }
                }
                failedUploads.add(ft);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
        rebuild();
        notifyDataSetChanged();
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
    /*-         Main Parser         -*/
    /*-*****************************-*/
    private static class Tuple {
        long timestamp;
        public int type;
        public Object object;
        public boolean downloading = false;
    }

    public void setAttachments(AttachmentFolders folders) {
        this.folders = folders;
        rebuild();
        notifyDataSetChanged();
    }

    private boolean hasFt(int folderId) {
        for (FailedUploadTuple ft : failedUploads) {
            if (ft.folderId == folderId)
                return true;
        }

        return false;
    }

    private boolean hasUt(int folderId) {
        for (UploadTuple ut : uploads) {
            if (ut.folderId == folderId)
                return true;
        }

        return false;
    }

    private void rebuild() {
        objects.clear();

        if (folders == null || folders.getResults() == null) {
            return;
        }

        Tuple t;
        AttachmentFolder[] attachmentFolders = folders.getResults();
        for (AttachmentFolder attachmentFolder : attachmentFolders) {
            if (attachmentFolder.getResults().length > 0
                    || hasFt(attachmentFolder.getId())
                    || hasUt(attachmentFolder.getId())
                    || attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.UPLOAD)
                    || attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.DELETE)
                    || attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.EDIT)) {
                t = new Tuple();
                t.type = AttachedFilesViewHolder.TYPE_HEADER;
                t.object = attachmentFolder;
                objects.add(t);

                List<Tuple> group = new LinkedList<>();

                //Add all failed
                for (FailedUploadTuple ft : failedUploads) {
                    if (ft.folderId == attachmentFolder.getId()) {
                        t = new Tuple();
                        t.type = AttachedFilesViewHolder.TYPE_FAILED;
                        t.timestamp = ft.timestamp;
                        t.object = ft;
                        group.add(t);
                    }
                }

                //Add uploads
                for (UploadTuple ut : uploads) {
                    boolean match = false;
                    for (FailedUploadTuple ft : failedUploads) {
                        if (ft.name.equals(ut.name))
                            match = true;
                    }

                    if (match)
                        continue;

                    if (ut.folderId == attachmentFolder.getId()) {
                        t = new Tuple();
                        t.timestamp = ut.timestamp;
                        t.type = AttachedFilesViewHolder.TYPE_UPLOAD;
                        t.object = ut;
                        group.add(t);
                    }
                }

                Attachment[] attachments = attachmentFolder.getResults();
                for (Attachment attachment : attachments) {
                    // check if downloading...
                    t = new Tuple();
                    try {
                        t.timestamp = attachment.getCreated().getUtcLong();
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                    t.type = AttachedFilesViewHolder.TYPE_ATTACHMENT;
                    t.object = attachment;
                    group.add(t);
                }

                Collections.sort(group, new Comparator<Tuple>() {
                    @Override
                    public int compare(Tuple tuple, Tuple t1) {
                        if (tuple.timestamp < t1.timestamp)
                            return 1;
                        else if (tuple.timestamp > t1.timestamp)
                            return -1;
                        return 0;
                    }
                });

                objects.addAll(group);

                if (attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.UPLOAD)) {
                    t = new Tuple();
                    t.type = AttachedFilesViewHolder.TYPE_ADD_VIEW;
                    t.object = attachmentFolder;
                    objects.add(t);
                }
            }
        }
    }

    @Override
    public AttachedFilesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AttachedFilesViewHolder holder = null;
        switch (viewType) {
            case AttachedFilesViewHolder.TYPE_HEADER: {
                ListItemGroupView listItemGroupView = new ListItemGroupView(parent.getContext());
                holder = new AttachedFilesViewHolder(listItemGroupView);
                holder.type = viewType;
                break;
            }
            case AttachedFilesViewHolder.TYPE_ATTACHMENT: {
                ListItemTwoVertView view = new ListItemTwoVertView(parent.getContext());
                view.setOnClickListener(_attachment_onClick);
                view.setOnLongClickListener(_attachment_onLongClick);
                view.setActionVisible(false);
                view.setProgressVisible(false);
                view.setAlertVisible(false);
                holder = new AttachedFilesViewHolder(view);
                holder.type = viewType;
                break;
            }
            case AttachedFilesViewHolder.TYPE_ADD_VIEW: {
                ListItemLinkView view = new ListItemLinkView(parent.getContext());
                view.setOnClickListener(_addNew_onClick);
                holder = new AttachedFilesViewHolder(view);
                holder.type = viewType;
                break;
            }
            case AttachedFilesViewHolder.TYPE_UPLOAD: {
                ListItemTwoVertView view = new ListItemTwoVertView(parent.getContext());
                view.setActionVisible(false);
                view.setProgressVisible(true);
                view.setAlertVisible(false);
                holder = new AttachedFilesViewHolder(view);
                holder.type = viewType;
                break;
            }
            case AttachedFilesViewHolder.TYPE_FAILED: {
                ListItemTwoVertView view = new ListItemTwoVertView(parent.getContext());
                view.setActionVisible(false);
                view.setProgressVisible(false);
                view.setAlertVisible(true);
                holder = new AttachedFilesViewHolder(view);
                holder.type = viewType;
                break;
            }
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(AttachedFilesViewHolder holder, int position) {
        switch (holder.type) {
            case AttachedFilesViewHolder.TYPE_HEADER: {
                ListItemGroupView view = (ListItemGroupView) holder.itemView;
                AttachmentFolder af = (AttachmentFolder) objects.get(position).object;
                view.setTitle(af.getName());
                break;
            }
            case AttachedFilesViewHolder.TYPE_ATTACHMENT: {
                ListItemTwoVertView view = (ListItemTwoVertView) holder.itemView;
                Attachment a = (Attachment) objects.get(position).object;
                view.setTag(a);

                if (objects.get(position).downloading) {
                    view.set(a.getFile().getName(), null);
                    view.setProgressVisible(true);
                } else {
                    view.set(a.getFile().getName(), a.getNotes());
                    view.setProgressVisible(false);
                }
                break;
            }
            case AttachedFilesViewHolder.TYPE_ADD_VIEW: {
                AttachmentFolder af = (AttachmentFolder) objects.get(position).object;
                ListItemLinkView view = (ListItemLinkView) holder.itemView;
                view.setTitle("Add New...");
                view.setTag(af);
                break;
            }
            case AttachedFilesViewHolder.TYPE_UPLOAD: {
                UploadTuple ut = (UploadTuple) objects.get(position).object;
                ListItemTwoVertView view = (ListItemTwoVertView) holder.itemView;
                view.set(ut.name, "");
                view.setProgress(ut.progress);
                break;
            }
            case AttachedFilesViewHolder.TYPE_FAILED: {
                FailedUploadTuple ft = (FailedUploadTuple) objects.get(position).object;
                ListItemTwoVertView view = (ListItemTwoVertView) holder.itemView;
                view.set(ft.name, ft.notes);
                view.setTag(ft.transaction);

                if (ft.ut == null) {
                    view.setActionVisible(false);
                    view.setProgressVisible(false);
                    view.setOnClickListener(_failed_onClick);
                    view.setOnLongClickListener(_failed_onLongClick);
                } else {
                    view.setActionVisible(false);
                    view.setProgressVisible(true);
                    view.setProgress(ft.ut.progress);
                    view.setOnClickListener(null);
                    view.setOnLongClickListener(null);
                }

                break;
            }
        }
    }

    private final View.OnClickListener _attachment_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ListItemTwoVertView v = (ListItemTwoVertView) view;
            Attachment a = (Attachment) v.getTag();
            if (a.getActionsSet().contains(Attachment.ActionsEnum.VIEW)) {
                if (_listener != null)
                    _listener.onShowAttachment(a);
            }
        }
    };

    private final View.OnClickListener _failed_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ListItemTwoVertView v = (ListItemTwoVertView) view;
            WebTransaction webTransaction = (WebTransaction) view.getTag();
            if (_listener != null)
                _listener.onFailedClick(webTransaction);
        }
    };

    private final View.OnLongClickListener _failed_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            ListItemTwoVertView v = (ListItemTwoVertView) view;
            WebTransaction webTransaction = (WebTransaction) view.getTag();
            if (_listener != null) {
                _listener.onFailedLongClick(webTransaction);
                return true;
            }
            return false;
        }
    };

    private final View.OnLongClickListener _attachment_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Log.v(TAG, "AttachmentFoldersAdapters");
            ListItemTwoVertView v = (ListItemTwoVertView) view;
            Attachment a = (Attachment) v.getTag();
            if (a.getActionsSet().contains(Attachment.ActionsEnum.DELETE) && _listener != null) {
                _listener.onDeleteAttachment(a);
                return true;
            }
            return false;
        }
    };

    private final View.OnClickListener _addNew_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (_listener != null)
                _listener.onAdd((AttachmentFolder) view.getTag());
        }
    };

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public int getItemViewType(int position) {
        return objects.get(position).type;
    }

    public interface Listener {
        void onShowAttachment(Attachment attachment);

        void onDeleteAttachment(Attachment attachment);

        void onAdd(AttachmentFolder attachmentFolder);

        void onFailedClick(WebTransaction webTransaction);

        void onFailedLongClick(WebTransaction webTransaction);
    }
}
