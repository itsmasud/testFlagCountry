package com.fieldnation.v2.ui.dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.AttachmentFolders;
import com.fieldnation.v2.data.model.Shipment;
import com.fieldnation.v2.data.model.Shipments;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.ShipmentAdapter;

/**
 * Created by mc on 10/10/17.
 */

public class ShipmentListDialog extends FullScreenDialog {
    private static final String TAG = "ShipmentListDialog";

    private static final String DIALOG_DELETE_SHIPMENT = TAG + ".deleteShipmentDialog";

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;
    private OverScrollRecyclerView _list;

    // Data
    private int _workOrderId;
    private AttachmentFolders _folders;
    private Shipments _shipments;
    private ShipmentAdapter _adapter = new ShipmentAdapter();

    public ShipmentListDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_shipment_list, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.inflateMenu(R.menu.dialog);
        _toolbar.setTitle("Shipments");

        _finishMenu = _toolbar.findViewById(R.id.primary_menu);
        _finishMenu.setText(R.string.btn_add);

        _list = v.findViewById(R.id.list);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        _list.setAdapter(_adapter);

        _adapter.setListener(_shipments_listener);

        _workOrdersApi.sub();

        TwoButtonDialog.addOnPrimaryListener(DIALOG_DELETE_SHIPMENT, _twoButtonDialog_deleteShipment);
        LocalBroadcastManager.getInstance(App.get()).registerReceiver(_webTransactionChanged, new IntentFilter(WebTransaction.BROADCAST_ON_CHANGE));

    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);
        _workOrderId = params.getInt("workOrderId");
        AppMessagingClient.setLoading(true);
        WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, true, WebTransaction.Type.NORMAL);
    }

    private void populateUi() {
        _finishMenu.setVisibility(View.GONE);
        if (_list == null) return;
        if (_shipments == null) return;
        if (_shipments.getActionsSet().contains(Shipments.ActionsEnum.ADD)) {
            _finishMenu.setVisibility(View.VISIBLE);
        }

        _adapter.setShipments(_workOrderId, _shipments);
    }

    @Override
    public void onStop() {
        _workOrdersApi.unsub();
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_DELETE_SHIPMENT, _twoButtonDialog_deleteShipment);
        LocalBroadcastManager.getInstance(App.get()).unregisterReceiver(_webTransactionChanged);
        super.onStop();
    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            ShipmentAddDialog.show(App.get(), null, _workOrderId,
                    _folders, getContext().getString(R.string.dialog_shipment_title), null, null);
            return false;
        }
    };

    private final ShipmentAdapter.Listener _shipments_listener = new ShipmentAdapter.Listener() {
        @Override
        public void onLongClick(View v, Shipment shipment, WebTransaction webTransaction) {

            Bundle bundle = new Bundle();
            if (webTransaction != null)
                bundle.putParcelable("wt", webTransaction);
            bundle.putParcelable("ship", shipment);


            TwoButtonDialog.show(App.get(), DIALOG_DELETE_SHIPMENT,
                    R.string.dialog_delete_shipment_title,
                    R.string.dialog_delete_shipment_body,
                    R.string.btn_yes, R.string.btn_no, true, bundle);
        }

        @Override
        public void onClick(View v, Shipment shipment) {
            // TODO edit not supported yet
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _twoButtonDialog_deleteShipment = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            if (((Bundle) extraData).containsKey("wt")) {
                WebTransaction webTransaction = ((Bundle) extraData).getParcelable("wt");
                WebTransaction.delete(webTransaction.getId());
                populateUi();
            } else {
                WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SHIPMENTS);
                WorkordersWebApi.deleteShipment(App.get(), _workOrderId, (Shipment) ((Bundle) extraData).getParcelable("ship"), App.get().getSpUiContext());
            }
        }
    };

    private final BroadcastReceiver _webTransactionChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (_shipments != null) {
                String op = intent.getStringExtra("op");
                if (intent.hasExtra("key")) {
                    String key = intent.getStringExtra("key");
                    if (key != null && (key.contains("addShipmentByWorkOrder") || key.contains("deleteShipmentByWorkOrderAndShipment")))
                        populateUi();
                }
            }
        }
    };


    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName) {
            if (transactionParams.getMethodParamInt("workOrderId") == null
                    || transactionParams.getMethodParamInt("workOrderId") != _workOrderId)
                return false;

            return methodName.toLowerCase().contains("workorder");
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (success && successObject != null && successObject instanceof WorkOrder) {
                WorkOrder workOrder = (WorkOrder) successObject;
                _folders = workOrder.getAttachments();
                Shipments shipments = workOrder.getShipments();
                if (_shipments != null) {
                    if (_shipments.getResults().length != shipments.getResults().length) {
                        _shipments = shipments;
                        populateUi();
                    } else {
                        for (int i = 0; i < _shipments.getResults().length; i++) {
                            if (_shipments.getResults()[i].getId() != shipments.getResults()[i].getId().intValue()) {
                                _shipments = shipments;
                                populateUi();
                                break;
                            }
                        }
                    }
                } else {
                    _shipments = shipments;
                    populateUi();
                }
                AppMessagingClient.setLoading(false);
            } else {
                AppMessagingClient.setLoading(true);
                WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, false, WebTransaction.Type.NORMAL);
            }
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    public static void show(Context context, String uid, int workOrderId) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);

        Controller.show(context, uid, ShipmentListDialog.class, params);
    }
}
