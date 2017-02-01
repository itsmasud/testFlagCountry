package com.fieldnation.v2.ui.worecycler;

import android.view.View;

import com.fieldnation.v2.ui.workorder.WorkOrderCard;


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
