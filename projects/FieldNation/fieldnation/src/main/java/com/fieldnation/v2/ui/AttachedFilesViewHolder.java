package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mc on 8/3/17.
 */

public class AttachedFilesViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ATTACHMENT = 1;
    public static final int TYPE_ADD_VIEW = 2;
    public static final int TYPE_UPLOAD = 3;

    public int type;

    public AttachedFilesViewHolder(View itemView) {
        super(itemView);
    }
}
