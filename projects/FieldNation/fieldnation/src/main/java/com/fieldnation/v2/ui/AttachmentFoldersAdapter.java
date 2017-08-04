package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.AttachmentFolders;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 8/3/17.
 */

public class AttachmentFoldersAdapter extends RecyclerView.Adapter<AttachedFilesViewHolder> {

    private List<Tuple> objects = new LinkedList<>();

    public void setAttachments(AttachmentFolders folders) {
        objects.clear();

        Tuple t;
        AttachmentFolder[] attachmentFolders = folders.getResults();
        for (AttachmentFolder attachmentFolder : attachmentFolders) {
            t = new Tuple();
            t.type = AttachedFilesViewHolder.TYPE_HEADER;
            t.object = attachmentFolder;
            objects.add(t);

            Attachment[] attachments = attachmentFolder.getResults();
            for (Attachment attachment : attachments) {
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
            case AttachedFilesViewHolder.TYPE_HEADER:
                ListItemGroupView listItemGroupView = new ListItemGroupView(parent.getContext());
                holder = new AttachedFilesViewHolder(listItemGroupView);
                holder.type = viewType;
                break;
            case AttachedFilesViewHolder.TYPE_ATTACHMENT: {
                ListItemTwoVertView listItemTwoVertView = new ListItemTwoVertView(parent.getContext());
                listItemTwoVertView.setOnClickListener(_attachment_onClick);
                listItemTwoVertView.setOnLongClickListener(_attachment_onLongClick);
                holder = new AttachedFilesViewHolder(listItemTwoVertView);
                holder.type = viewType;
                break;
            }
            case AttachedFilesViewHolder.TYPE_ADD_VIEW:
                holder = new AttachedFilesViewHolder(new TextView(parent.getContext()));
                holder.type = viewType;
                break;
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
                view.set(a.getFile().getName(), a.getNotes());
                view.setTag(a);
                break;
            }
            case AttachedFilesViewHolder.TYPE_ADD_VIEW: {
                TextView view = (TextView) holder.itemView;
                view.setText("Add New...");
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
                // TODO download and show
            }
        }
    };

    private final View.OnLongClickListener _attachment_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            ListItemTwoVertView v = (ListItemTwoVertView) view;
            Attachment a = (Attachment) v.getTag();
            if (a.getActionsSet().contains(Attachment.ActionsEnum.DELETE)) {
                // TODO ask delete and delete
                return true;
            }
            return false;
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

    private static class Tuple {
        public int type;
        public Object object;
    }
}
