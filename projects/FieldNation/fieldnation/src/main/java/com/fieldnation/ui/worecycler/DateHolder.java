package com.fieldnation.ui.worecycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Michael on 3/11/2016.
 */
public class DateHolder extends BaseHolder {
    public DateHolder(View itemView) {
        super(itemView, TYPE_DATE);
    }

    public ListTimeHeader getView() {
        return (ListTimeHeader) itemView;
    }
}