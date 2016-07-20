package com.fieldnation.ui.worecycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fieldnation.ui.workorder.WorkorderCardView;

/**
 * Created by Michael on 3/11/2016.
 */
public class WorkorderHolder extends BaseHolder {
    public WorkorderHolder(View itemView) {
        super(itemView, TYPE_WORKORDER);
    }

    public WorkorderCardView getView() {
        return (WorkorderCardView) itemView;
    }
}
