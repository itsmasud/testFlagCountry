package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.AttachmentFolders;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by mc on 8/3/17.
 */

public class AttachedFilesAdapter extends RecyclerView.Adapter<AttachedFilesViewHolder> {
    private static final String TAG = "AttachedFilesAdapter";

    private AttachmentFolders existingFolders = null;
    private List<Tuple> displayObjects = new LinkedList<>();
    private Set<Integer> deletedAttachments = new HashSet<>();
    private List<PausedUploadTuple> pausedUploads = new LinkedList<>();
    private List<FailedUploadTuple> failedUploads = new LinkedList<>();
    private List<UploadTuple> uploads = new LinkedList<>();

    private Listener _listener;
    private int _workOrderId;

    private boolean _parseAgain = false;
    private boolean _parseRunning = false;

    private boolean _rebuildAgain = false;
    private boolean _rebuildRunning = false;


    public void setWorkOrderId(int workOrderId) {
        _workOrderId = workOrderId;
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    /*-*****************************-*/
    /*-         Downloads           -*/
    /*-*****************************-*/
    /*- Can only apply to existing folders/files -*/
    // modifies existing folders/files
    public void downloadStart(int attachmentId) {
        for (int i = 0; i < displayObjects.size(); i++) {
            Tuple tuple = displayObjects.get(i);
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
        for (int i = 0; i < displayObjects.size(); i++) {
            Tuple tuple = displayObjects.get(i);
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

    /*-*************************-*/
    /*-         Deleted         -*/
    /*-*************************-*/
    /*- Can only apply to existing folders/files -*/
    // Removes a real entry, cancels a download?

    public void setDeleted(List<WebTransaction> webTransactions) {

    }

    /*-*********************************-*/
    /*-         Paused Uploads          -*/
    /*-*********************************-*/
    // Manages a fake entry
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

    /*-*********+***********************-*/
    /*-         Failed Uploads          -*/
    /*-*********************************-*/
    // Manages a fake entry
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

    /*-*********+****************-*/
    /*-         Uploads          -*/
    /*-**************************-*/
    // Manages a fake entry

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


    public void uploadClear() {
        uploads.clear();
        rebuild();
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
                return;
            }
        }
        UploadTuple t = new UploadTuple(uuid, transactionParams, progress);
        uploads.add(t);
        // TODO find location and notify accordingly
        rebuild();
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
        this.existingFolders = folders;

        if (_parseRunning && _parseAgain) {
            // do nothing
        } else if (_parseRunning && !_parseAgain) {
            _parseAgain = true;
        } else if (!_parseRunning && _parseAgain) {
            // should not happen
        } else if (!_parseRunning && !_parseAgain) {
            new ParserTask(this, _workOrderId).executeEx();
        }
    }

    private static class ParserTask extends AsyncTaskEx<Object, Object, Object> {
        private List<PausedUploadTuple> pausedUploads = new LinkedList<>();
        private List<FailedUploadTuple> failedUploads = new LinkedList<>();
        private Set<Integer> deletedAttachments = new HashSet<>();
        private AttachedFilesAdapter adapter;
        private int _workOrderId;

        public ParserTask(AttachedFilesAdapter adapter, int workOrderId) {
            this.adapter = adapter;
            this._workOrderId = workOrderId;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            adapter._parseRunning = true;

            List<WebTransaction> webTransactions = WebTransaction.findByKey("%Attachment%/workorders/" + _workOrderId + "/%");

            failedUploads.clear();
            pausedUploads.clear();
            //deletedAttachments.clear();
            for (WebTransaction webTransaction : webTransactions) {
                // Failed
                if (webTransaction.wasZombie() && webTransaction.getState() != WebTransaction.State.IDLE) {
                    try {
                        FailedUploadTuple ft = new FailedUploadTuple(webTransaction);
                        for (UploadTuple ut : adapter.uploads) {
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

                // Paused
                if (webTransaction.getState() == WebTransaction.State.IDLE && webTransaction.getKey().contains("addAttachmentByWorkOrderAndFolder")) {
                    try {
                        TransactionParams params = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));
                        if (params != null && params.apiFunction != null && "addAttachment".equals(params.apiFunction))
                            pausedUploads.add(new PausedUploadTuple(webTransaction));
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                }

                // Deleted
                if (webTransaction.getState() == WebTransaction.State.IDLE && webTransaction.getKey().contains("deleteAttachmentByWorkOrderAndFolderAndAttachment")) {
                    try {
                        TransactionParams params = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));
                        JsonObject methodParams = new JsonObject(params.methodParams);
                        deletedAttachments.add(methodParams.getInt("attachmentId"));
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            adapter.deletedAttachments.addAll(deletedAttachments);
            adapter.failedUploads.clear();
            adapter.failedUploads.addAll(failedUploads);
            adapter.pausedUploads.clear();
            adapter.pausedUploads.addAll(pausedUploads);
            adapter.rebuild();

            if (adapter._parseAgain) {
                new ParserTask(adapter, _workOrderId).executeEx();
                adapter._parseAgain = false;
            } else {
                adapter._parseRunning = false;
            }
        }
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

    public void rebuild() {
        if (_rebuildRunning && _rebuildAgain) {
            // do nothing
        } else if (_rebuildRunning && !_rebuildAgain) {
            _rebuildAgain = true;
        } else if (!_rebuildRunning && _rebuildAgain) {
            // shouldn't be here
        } else if (!_rebuildRunning && !_rebuildAgain) {
            new RebuildTask(this).executeEx();
        }
    }

    private static class RebuildTask extends AsyncTaskEx<Object, Object, Object> {
        private AttachedFilesAdapter adapter;
        private List<Tuple> displayObjects = new LinkedList<>();
        private AttachmentFolders existingFolders = null;
        private Set<Integer> deletedAttachments = new HashSet<>();
        private List<PausedUploadTuple> pausedUploads = new LinkedList<>();
        private List<FailedUploadTuple> failedUploads = new LinkedList<>();
        private List<UploadTuple> uploads = new LinkedList<>();

        public RebuildTask(AttachedFilesAdapter adapter) {
            this.adapter = adapter;
            existingFolders = adapter.existingFolders;
            deletedAttachments.addAll(adapter.deletedAttachments);
            pausedUploads.addAll(adapter.pausedUploads);
            failedUploads.addAll(adapter.failedUploads);
            uploads.addAll(adapter.uploads);
        }

        @Override
        protected Object doInBackground(Object... objects) {
            adapter._rebuildRunning = true;
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            displayObjects.clear();

            if (existingFolders == null || existingFolders.getResults() == null) {
                return null;
            }

            boolean hasPaused = false;
            Tuple t;
            AttachmentFolder[] attachmentFolders = existingFolders.getResults();
            for (AttachmentFolder attachmentFolder : attachmentFolders) {
                if (attachmentFolder.getResults().length > 0
                        || adapter.hasFt(attachmentFolder.getId())
                        || adapter.hasUt(attachmentFolder.getId())
                        || attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.UPLOAD)
                        || attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.DELETE)
                        || attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.EDIT)) {
                    t = new Tuple();
                    t.type = AttachedFilesViewHolder.TYPE_HEADER;
                    t.object = attachmentFolder;
                    displayObjects.add(t);

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
                        if (deletedAttachments.contains(attachment.getId()))
                            continue;

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

                    displayObjects.addAll(group);

                    if (attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.UPLOAD)) {
                        t = new Tuple();
                        t.type = AttachedFilesViewHolder.TYPE_ADD_VIEW;
                        t.object = attachmentFolder;
                        displayObjects.add(t);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            adapter.displayObjects.clear();
            adapter.displayObjects.addAll(displayObjects);
            adapter.notifyDataSetChanged();

            if (adapter._rebuildAgain) {
                adapter._rebuildAgain = false;
                new RebuildTask(adapter).executeEx();
            } else {
                adapter._rebuildRunning = false;
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
                AttachmentFolder af = (AttachmentFolder) displayObjects.get(position).object;
                view.setTitle(af.getName());
                break;
            }
            case AttachedFilesViewHolder.TYPE_ATTACHMENT: {
                ListItemTwoVertView view = (ListItemTwoVertView) holder.itemView;
                Attachment a = (Attachment) displayObjects.get(position).object;
                view.setTag(a);
                if (a.getActionsSet().contains(Attachment.ActionsEnum.VIEW)) {
                    view.setEnabled(true);
                } else {
                    view.setEnabled(false);
                }

                if (displayObjects.get(position).downloading) {
                    view.set(a.getFile().getName(), null);
                    view.setProgressVisible(true);
                } else {
                    view.set(a.getFile().getName(), a.getNotes());
                    view.setProgressVisible(false);
                }
                break;
            }
            case AttachedFilesViewHolder.TYPE_ADD_VIEW: {
                AttachmentFolder af = (AttachmentFolder) displayObjects.get(position).object;
                ListItemLinkView view = (ListItemLinkView) holder.itemView;
                view.setTitle("Add New...");
                view.setTag(af);
                break;
            }
            case AttachedFilesViewHolder.TYPE_UPLOAD: {
                UploadTuple ut = (UploadTuple) displayObjects.get(position).object;
                ListItemTwoVertView view = (ListItemTwoVertView) holder.itemView;
                view.set(ut.name, "");
                view.setProgress(ut.progress);
                break;
            }
            case AttachedFilesViewHolder.TYPE_FAILED: {
                FailedUploadTuple ft = (FailedUploadTuple) displayObjects.get(position).object;
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
                PausedUploadTuple t = (PausedUploadTuple) displayObjects.get(position).object;
                long timeLeft = t.transaction.getQueueTime() - System.currentTimeMillis();
                WebTransaction wt = t.transaction;
                if (timeLeft < 0) {
                    if (App.get().getOfflineState() != App.OfflineState.NORMAL) {
                        view.set(t.name, "Paused in offline mode");
                    } else if (wt.isWifiRequired() && !App.get().haveWifi()) {
                        view.set(t.name, "Waiting for wifi...");
                    }
                    if (App.get().getOfflineState() == App.OfflineState.NORMAL)
                        view.set(t.name, App.get().getString(R.string.waiting_for_network));
                    else
                        view.set(t.name, App.get().getString(R.string.offline_mode_waiting_for_sync));
                } else {
                    view.set(t.name, "Will retry in " + misc.convertMsToHuman(timeLeft, true));
                }
                view.setTag(t.transaction);
                view.setProgressVisible(false);
                break;
            }
        }
    }

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
        return displayObjects.size();
    }

    @Override
    public int getItemViewType(int position) {
        return displayObjects.get(position).type;
    }

    public interface Listener {
        void onShowAttachment(Attachment attachment);

        void onDeleteAttachment(Attachment attachment);

        void onAdd(AttachmentFolder attachmentFolder);

        void onFailedClick(WebTransaction webTransaction);

        void onFailedLongClick(WebTransaction webTransaction);
    }
}
