package com.fieldnation.ui.worecycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Michael on 3/11/2016.
 */
public class BaseHolder extends RecyclerView.ViewHolder {

    public static final int TYPE_DATE = 0;
    public static final int TYPE_OBJECT = 1;
    public static final int TYPE_RATE_ME = 2;

    public int type;

    public BaseHolder(View itemView, int id) {
        super(itemView);
        type = id;
    }
}
