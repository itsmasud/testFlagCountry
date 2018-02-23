package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by michaelcarver on 2/19/18.
 */

public class UnsyncedViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_TRANSACTION = 1;
    public static final int TYPE_VIEW_WO = 2;

    public int type;

    public UnsyncedViewHolder(View itemView) {
        super(itemView);
    }
}
