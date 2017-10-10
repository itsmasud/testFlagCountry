package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.ui.workorder.detail.ShipmentRowView;
import com.fieldnation.v2.data.model.Shipment;
import com.fieldnation.v2.data.model.Shipments;

/**
 * Created by mc on 10/10/17.
 */

public class ShipmentAdapter extends RecyclerView.Adapter<ShipmentViewHolder> {
    private static final String TAG = "ShipmentAdapter";

    private Shipments shipments;

    public void setShipments(Shipments shipments) {
        this.shipments = shipments;
        rebuild();
    }

    private void rebuild() {
        notifyDataSetChanged();
    }

    @Override
    public ShipmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ShipmentRowView v = new ShipmentRowView(parent.getContext());
        return new ShipmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ShipmentViewHolder holder, int position) {
        ShipmentRowView v = (ShipmentRowView) holder.itemView;
        Shipment shipment = shipments.getResults()[position];
        v.setTag(shipment);
        v.setData(shipment);
        v.setOnLongClickListener(_shipment_onLongClick);
        v.setOnClickListener(_shipment_onClick);
        v.setEnabled(shipments.getActionsSet().contains(Shipments.ActionsEnum.ADD));
    }

    @Override
    public int getItemCount() {
        if (shipments == null || shipments.getResults() == null)
            return 0;
        return shipments.getResults().length;
    }

    private final View.OnLongClickListener _shipment_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
/*
            if (_listener != null
                    && shipment != null
                    && shipment.getActionsSet().contains(Shipment.ActionsEnum.DELETE)) {
                _listener.onDelete(_workOrder, shipment);
            } else {
                ToastClient.toast(App.get(), R.string.toast_cant_delete_shipment_permission, Toast.LENGTH_LONG);
            }
*/
            return false;
        }
    };

    private final View.OnClickListener _shipment_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
/*
            if (_listener != null
                    && shipment != null
                    && shipment.getActionsSet().contains(Shipments.ActionsEnum.ADD)) {
                _listener.onAssign(_workOrder, shipment);
            }
*/
        }
    };
}
