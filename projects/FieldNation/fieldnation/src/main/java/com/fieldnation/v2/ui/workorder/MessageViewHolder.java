package com.fieldnation.v2.ui.workorder;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

/**
 * Created by mc on 8/30/17.
 */

public class MessageViewHolder extends ViewHolder {
    public static final int TYPE_EMPTY = 0;

    public static final int TYPE_RIGHT_TOP = 1;
    public static final int TYPE_RIGHT_CENTER = 2;
    public static final int TYPE_RIGHT_BOTTOM = 3;
    public static final int TYPE_RIGHT_FULL = 4;
    public static final int TYPE_LEFT_TOP = 5;
    public static final int TYPE_LEFT_CENTER = 6;
    public static final int TYPE_LEFT_BOTTOM = 7;
    public static final int TYPE_LEFT_FULL = 8;
    public static final int TYPE_HEADER_TIME = 9;

    public int type;

    public MessageViewHolder(View itemView, int type) {
        super(itemView);
        this.type = type;
    }
}
