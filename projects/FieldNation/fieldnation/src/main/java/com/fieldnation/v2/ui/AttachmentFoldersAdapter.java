package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.AttachmentFolders;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 8/3/17.
 */

public class AttachmentFoldersAdapter extends RecyclerView.Adapter<AttachedFilesViewHolder> {
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

    /*-*********+****************-*/
    /*-         Uploads          -*/
    /*-**************************-*/

    private static class UploadTuple {
        int folderId;
        String name;
        int progress = -1;
        int index = -1;

        public UploadTuple(int folderId, String name, int progress) {
            this.folderId = folderId;
            this.name = name;
            this.progress = progress;
        }
    }

    private List<UploadTuple> uploads = new LinkedList<>();

    public void uploadStart(int folderId, String name) {
        for (UploadTuple ut : uploads) {
            if (ut.name.equals(name)) {
                rebuild();
                notifyDataSetChanged();
                return;
            }
        }
        UploadTuple t = new UploadTuple(folderId, name, -1);
        uploads.add(t);
        // TODO find position and notify accordingly
        rebuild();
        notifyDataSetChanged();
    }

    public void uploadProgress(int folderId, String name, int progress) {
        for (UploadTuple ut : uploads) {
            if (ut.name.equals(name)) {
                ut.progress = progress;
                rebuild();
                notifyDataSetChanged();
                return;
            }
        }
        UploadTuple t = new UploadTuple(folderId, name, progress);
        uploads.add(t);
        // TODO find location and notify accordingly
        rebuild();
        notifyDataSetChanged();
    }

    public void uploadStop(int folderId, String name) {
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
        public int type;
        public Object object;
        public boolean downloading = false;
    }

    public void setAttachments(AttachmentFolders folders) {
        this.folders = folders;
        rebuild();
        notifyDataSetChanged();
    }

    private void rebuild() {
        objects.clear();

        Tuple t;
        AttachmentFolder[] attachmentFolders = folders.getResults();
        for (AttachmentFolder attachmentFolder : attachmentFolders) {
            t = new Tuple();
            t.type = AttachedFilesViewHolder.TYPE_HEADER;
            t.object = attachmentFolder;
            objects.add(t);

            //Add uploads
            for (UploadTuple ut : uploads) {
                if (ut.folderId == attachmentFolder.getId()) {
                    t = new Tuple();
                    t.type = AttachedFilesViewHolder.TYPE_UPLOAD;
                    t.object = ut;
                    objects.add(t);
                }
            }

            Attachment[] attachments = attachmentFolder.getResults();
            for (Attachment attachment : attachments) {
                // check if downloading...
                t = new Tuple();
                t.type = AttachedFilesViewHolder.TYPE_ATTACHMENT;
                t.object = attachment;
                objects.add(t);
            }

            if (attachmentFolder.getActionsSet().contains(AttachmentFolder.ActionsEnum.UPLOAD)) {
                t = new Tuple();
                t.type = AttachedFilesViewHolder.TYPE_ADD_VIEW;
                t.object = attachmentFolder;
                objects.add(t);
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
                ListItemTwoVertView listItemTwoVertView = new ListItemTwoVertView(parent.getContext());
                listItemTwoVertView.setOnClickListener(_attachment_onClick);
                listItemTwoVertView.setOnLongClickListener(_attachment_onLongClick);
                listItemTwoVertView.setActionVisible(false);
                holder = new AttachedFilesViewHolder(listItemTwoVertView);
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
                view.setProgressVisible(true);
                view.setActionVisible(false);
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
                if (af.getType().equals(AttachmentFolder.TypeEnum.DOCUMENT))
                    view.setTitle("Documents to Review");
                else
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

    private final View.OnLongClickListener _attachment_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
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
    }
}
