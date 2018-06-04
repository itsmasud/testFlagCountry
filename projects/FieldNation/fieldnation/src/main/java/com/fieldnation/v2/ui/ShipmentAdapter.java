package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionUtils;
import com.fieldnation.ui.workorder.detail.ShipmentRowView;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Shipment;
import com.fieldnation.v2.data.model.Shipments;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 10/10/17.
 */

public class ShipmentAdapter extends RecyclerView.Adapter<ShipmentViewHolder> {
    private static final String TAG = "ShipmentAdapter";

    private List<Object> objects = new LinkedList<>();
    private Shipments _shipments;
    private List<ShipmentAdapter.Tuple> addedShipments = new LinkedList<>();
    private Hashtable<Integer, WebTransaction> deletedShpiments = new Hashtable<>();
    private int _workOrderId;
    private int _running = 0;
    private boolean _runAgain = false;

    private Listener listener;

    private static class Tuple {
        public WebTransaction webTransaction;
        public Shipment shipment;

        public Tuple(WebTransaction webTransaction, Shipment shipment) {
            this.webTransaction = webTransaction;
            this.shipment = shipment;
        }
    }

    public void setShipments(int workOrderId, Shipments shipments) {
        this._shipments = shipments;
        this._workOrderId = workOrderId;
        if (_running == 0) {
            _running = 2;
            addedShipments.clear();
            deletedShpiments.clear();
            WebTransactionUtils.setData(_addShipment, WebTransactionUtils.KeyType.ADD_SHIPMENT, workOrderId);
            WebTransactionUtils.setData(_deleteShipment, WebTransactionUtils.KeyType.DELETE_SHIPMENT, workOrderId);
        } else {
            _runAgain = true;
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void rebuild() {
        if (_runAgain) {
            _runAgain = false;
            setShipments(_workOrderId, _shipments);
            return;
        }

        objects.clear();

        for (Tuple shipment : addedShipments) {
            objects.add(shipment);
        }

        for (Shipment shipment : _shipments.getResults()) {
            if (deletedShpiments.containsKey(shipment.getId()))
                continue;

            objects.add(shipment);
        }

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
        v.setTag(objects.get(position));
        Object object = objects.get(position);
        if (object instanceof Tuple) {
            v.setOffline(true);
            v.setData(((Tuple) object).shipment);
        } else if (object instanceof Shipment) {
            v.setOffline(false);
            Shipment shipment = (Shipment) object;
            v.setData(shipment);
        }
        v.setOnLongClickListener(_shipment_onLongClick);
        v.setEnabled(_shipments.getActionsSet().contains(Shipments.ActionsEnum.ADD));

    }

    @Override
    public int getItemCount() {
        if (objects == null)
            return 0;
        return objects.size();
    }

    private final View.OnLongClickListener _shipment_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Object object = view.getTag();
            WebTransaction webTransaction = null;
            Shipment shipment = null;
            if (object instanceof ShipmentAdapter.Tuple) {
                shipment = ((Tuple) object).shipment;
                webTransaction = ((ShipmentAdapter.Tuple) object).webTransaction;
            } else if (object instanceof Shipment) {
                shipment = (Shipment) object;
            }

            if (listener != null
                    && (shipment.getActionsSet().contains(Shipment.ActionsEnum.DELETE) || webTransaction != null)) {
                listener.onLongClick(view, shipment, webTransaction);
            } else {
                ToastClient.toast(App.get(), R.string.toast_cant_delete_shipment_permission, Toast.LENGTH_LONG);
            }
            return false;
        }
    };

    private final WebTransactionUtils.Listener _deleteShipment = new WebTransactionUtils.Listener() {
        @Override
        public void onFoundWebTransaction(WebTransactionUtils.KeyType keyType, int workOrderId, WebTransaction webTransaction, TransactionParams transactionParams, JsonObject methodParams) {
            try {
                TransactionParams tp = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));
                int id = new JsonObject(tp.methodParams).getInt("shipmentId");
                deletedShpiments.put(id, webTransaction);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onComplete() {
            _running--;
            if (_running <= 0) rebuild();
        }
    };
    private final WebTransactionUtils.Listener _addShipment = new WebTransactionUtils.Listener() {
        @Override
        public void onFoundWebTransaction(WebTransactionUtils.KeyType keyType, int workOrderId, WebTransaction webTransaction, TransactionParams transactionParams, JsonObject methodParams) {
            try {
                TransactionParams tp = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));
                Shipment shipment = Shipment.fromJson(new JsonObject(tp.methodParams).getJsonObject("shipment"));
                addedShipments.add(new ShipmentAdapter.Tuple(webTransaction, shipment));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onComplete() {
            _running--;
            if (_running <= 0) rebuild();
        }
    };


    public interface Listener {
        void onLongClick(View v, Shipment shipment, WebTransaction webTransaction);

        void onClick(View v, Shipment shipment);
    }

}
