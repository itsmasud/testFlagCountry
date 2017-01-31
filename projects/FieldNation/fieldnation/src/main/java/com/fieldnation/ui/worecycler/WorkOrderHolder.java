package com.fieldnation.ui.worecycler;

import android.view.View;

import com.fieldnation.ui.workorder.v2_old.WorkOrderCard;

/**
 * Created by Michael on 3/11/2016.
 */
public class WorkOrderHolder extends BaseHolder {
    public WorkOrderHolder(View itemView) {
        super(itemView, TYPE_OBJECT);
    }

    public WorkOrderCard getView() {
        return (WorkOrderCard) itemView;
    }
}
