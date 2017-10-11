package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.ui.workorder.detail.ShipmentRowView;
import com.fieldnation.v2.data.model.Shipment;
import com.fieldnation.v2.data.model.Shipments;

/**
 * Created by mc on 10/10/17.
 */

public class ShipmentAdapter extends RecyclerView.Adapter<ShipmentViewHolder> {
    private static final String TAG = "ShipmentAdapter";

    private Shipments shipments;
    private Listener listener;

    public void setShipments(Shipments shipments) {
        this.shipments = shipments;
        rebuild();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
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
            Shipment shipment = (Shipment) view.getTag();
            if (listener != null
                    && shipment.getActionsSet().contains(Shipment.ActionsEnum.DELETE)) {
                listener.onLongClick(view, shipment);
            } else {
                ToastClient.toast(App.get(), R.string.toast_cant_delete_shipment_permission, Toast.LENGTH_LONG);
            }
            return false;
        }
    };

    private final View.OnClickListener _shipment_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Shipment shipment = (Shipment) view.getTag();
            if (listener != null
                    && shipment.getActionsSet().contains(Shipments.ActionsEnum.ADD)) {
                listener.onClick(view, shipment);
            }
        }
    };

    public interface Listener {
        void onLongClick(View v, Shipment shipment);

        void onClick(View v, Shipment shipment);
    }
}
