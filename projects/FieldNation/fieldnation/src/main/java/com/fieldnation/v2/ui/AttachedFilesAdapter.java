package com.fieldnation.v2.ui;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.ApatheticOnClickListener;
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
    private static final String TAG = "AttachedFilesAdapter";

    private AttachmentFolders folders = null;
    private List<Tuple> objects = new LinkedList<>();
    private Listener _listener;
    private Handler _handler = new Handler();
    private boolean _isClockRunning = false;

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

    /*-*********************************-*/
    /*-         Paused Uploads          -*/
    /*-*********************************-*/
    private static class PausedUploadTuple {
        long timestamp;
        int folderId;
        String name;
        String notes;
        WebTransaction transaction;
        UUIDGroup uuid;

        public PausedUploadTuple(WebTransaction webTransaction) {
            this.transaction = webTransaction;
            uuid = webTransaction.getUUID();
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

    private List<PausedUploadTuple> pausedUploads = new LinkedList<>();

    public void setPausedUploads(List<WebTransaction> webTransactions) {
        pausedUploads.clear();

        for (WebTransaction webTransaction : webTransactions) {
            try {
                TransactionParams params = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));

                if (params != null && params.apiFunction != null && "addAttachment".equals(params.apiFunction))
                    pausedUploads.add(new PausedUploadTuple(webTransaction));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
        rebuild();
        notifyDataSetChanged();
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
        String hash;
        UUIDGroup uuid;

        public FailedUploadTuple(WebTransaction webTransaction) {
            this.transaction = webTransaction;
            uuid = webTransaction.getUUID();
            try {
                TransactionParams params = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));
                JsonObject methodParams = new JsonObject(params.methodParams);

                this.folderId = methodParams.getInt("folderId");
                this.name = methodParams.getString("attachment.file.name");
                this.notes = methodParams.has("attachment.notes") ? methodParams.getString("attachment.notes") : "";
                this.timestamp = methodParams.getLong("timestamp");
                this.hash = methodParams.getString("fileHash");
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
                    if (ft.uuid.uuid.equals(ut.uuid.uuid)) {
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
        UUIDGroup uuid;

        public UploadTuple(UUIDGroup uuid, TransactionParams transactionParams, int progress) {
            this.uuid = uuid;
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

    public void uploadProgress(UUIDGroup uuid, TransactionParams transactionParams, int progress) {
        Log.v(TAG, "uploadProgress");
        String name = null;
        try {
            JsonObject methodParams = new JsonObject(transactionParams.methodParams);
            name = methodParams.getString("attachment.file.name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        for (UploadTuple ut : uploads) {
            if (ut.uuid.uuid.equals(uuid.uuid)) {
                ut.progress = progress;
                rebuild();
                notifyDataSetChanged();
                return;
            }
        }
        UploadTuple t = new UploadTuple(uuid, transactionParams, progress);
        uploads.add(t);
        // TODO find location and notify accordingly
        rebuild();
        notifyDataSetChanged();
    }

    public void uploadStop(UUIDGroup uuidGroup, TransactionParams transactionParams) {
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
            if (ut.uuid.uuid.equals(uuidGroup.uuid)) {
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

        boolean hasPaused = false;
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
                // Add all paused
                for (PausedUploadTuple pt : pausedUploads) {
                    if (pt.folderId == attachmentFolder.getId()) {
                        t = new Tuple();
                        t.type = AttachedFilesViewHolder.TYPE_PAUSED;
                        t.timestamp = pt.timestamp;
                        t.object = pt;
                        group.add(t);
                        hasPaused = true;
                    }
                }

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
                        if (ft.uuid.uuid.equals(ut.uuid.uuid))
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
                        //Log.v(TAG, ex);
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

        if (hasPaused && !_isClockRunning) {
            _isClockRunning = true;
            _handler.post(refreshClock);
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
            case AttachedFilesViewHolder.TYPE_PAUSED: {
                ListItemTwoVertView view = new ListItemTwoVertView(parent.getContext());
                view.setActionVisible(false);
                view.setProgressVisible(false);
                view.setAlertVisible(false);
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
                if (a.getActionsSet().contains(Attachment.ActionsEnum.VIEW)) {
                    view.setEnabled(true);
                } else {
                    view.setEnabled(false);
                }

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
            case AttachedFilesViewHolder.TYPE_PAUSED: {
                ListItemTwoVertView view = (ListItemTwoVertView) holder.itemView;
                PausedUploadTuple t = (PausedUploadTuple) objects.get(position).object;
                long timeLeft = t.transaction.getQueueTime() - System.currentTimeMillis();
                WebTransaction wt = t.transaction;
                if (timeLeft < 0) {
                    if (App.get().getOfflineState() != App.OfflineState.NORMAL) {
                        view.set(t.name, "Paused in offline mode");
                    } else if (wt.isWifiRequired() && !App.get().haveWifi()) {
                        view.set(t.name, "Waiting for wifi...");
                    }
                    view.set(t.name, "Waiting for network...");
                } else {
                    view.set(t.name, "Will retry in " + misc.convertMsToHuman(timeLeft, true));
                }
                view.setTag(t.transaction);
                view.setProgressVisible(false);
                break;
            }
        }
    }

    private final Runnable refreshClock = new Runnable() {
        @Override
        public void run() {
            boolean hasPaused = false;
            for (int i = 0; i < objects.size(); i++) {
                Tuple t = objects.get(i);
                if (t.object instanceof PausedUploadTuple) {
                    notifyItemChanged(i);
                    hasPaused = true;
                }
            }

            if (hasPaused)
                _handler.postDelayed(refreshClock, 1000);
            else
                _isClockRunning = false;
        }
    };

    private final View.OnClickListener _attachment_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View view) {
            ListItemTwoVertView v = (ListItemTwoVertView) view;
            Attachment a = (Attachment) v.getTag();
            if (a.getActionsSet().contains(Attachment.ActionsEnum.VIEW)) {
                if (_listener != null)
                    _listener.onShowAttachment(a);
            }
        }
    };

    private final View.OnClickListener _failed_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View view) {
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

    private final View.OnClickListener _addNew_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View view) {
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
