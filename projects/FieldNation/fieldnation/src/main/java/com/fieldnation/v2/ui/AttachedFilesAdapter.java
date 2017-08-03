package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.AttachmentFolders;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 8/3/17.
 */

public class AttachedFilesAdapter extends RecyclerView.Adapter<AttachedFilesViewHolder> {

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
                break;
            case AttachedFilesViewHolder.TYPE_ATTACHMENT: {
                break;
            }
            case AttachedFilesViewHolder.TYPE_ADD_VIEW:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(AttachedFilesViewHolder holder, int position) {
        switch (holder.type) {
            case AttachedFilesViewHolder.TYPE_HEADER:
                break;
            case AttachedFilesViewHolder.TYPE_ATTACHMENT:
                break;
            case AttachedFilesViewHolder.TYPE_ADD_VIEW:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    private static class Tuple {
        public int type;
        public Object object;
    }
}
