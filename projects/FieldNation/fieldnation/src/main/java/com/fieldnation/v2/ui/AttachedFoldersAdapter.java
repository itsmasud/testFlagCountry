package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.AttachmentFolders;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 8/24/17.
 */

public abstract class AttachedFoldersAdapter extends RecyclerView.Adapter<AttachedFoldersViewHolder> {
    private static final String TAG = "AttachedFoldersAdapter";

    private List<AttachmentFolder> _folders = new LinkedList<>();

    public void setAttachments(AttachmentFolders folders) {
        _folders.clear();

        for (AttachmentFolder folder : folders.getResults()) {
            if (folder.getActionsSet().contains(AttachmentFolder.ActionsEnum.UPLOAD))
                _folders.add(folder);
        }

        notifyDataSetChanged();
    }

    @Override
    public AttachedFoldersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AttachedFoldersViewHolder(new ListItemLinkView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(AttachedFoldersViewHolder holder, final int position) {
        ListItemLinkView v = (ListItemLinkView) holder.itemView;
        v.setTitle(_folders.get(position).getName());
        v.setOnClickListener(new ApatheticOnClickListener() {
            @Override
            public void onSingleClick(View view) {
                preOnItemClick(position);
            }
        });
    }

    private void preOnItemClick(int position) {
        onItemClick(_folders.get(position));
    }

    public abstract void onItemClick(AttachmentFolder attachmentFolder);

    @Override
    public int getItemCount() {
        return _folders.size();
    }
}
