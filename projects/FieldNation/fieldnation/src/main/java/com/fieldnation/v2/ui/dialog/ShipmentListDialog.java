package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.fieldnation.ui.FnToolBarView;
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
    private FnToolBarView _fnToolbarView;
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

        _fnToolbarView = v.findViewById(R.id.fnToolbar);
        _fnToolbarView.getToolbar().setNavigationIcon(R.drawable.back_arrow);
        _fnToolbarView.getToolbar().inflateMenu(R.menu.dialog);
        _fnToolbarView.getToolbar().setTitle("Shipments");

        _finishMenu = _fnToolbarView.getToolbar().findViewById(R.id.primary_menu);
        _finishMenu.setText(R.string.btn_add);

        _list = v.findViewById(R.id.list);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _fnToolbarView.getToolbar().setOnMenuItemClickListener(_menu_onClick);
        _fnToolbarView.getToolbar().setNavigationOnClickListener(_toolbar_onClick);

        _list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        _list.setAdapter(_adapter);

        _adapter.setListener(_shipments_listener);

        _workOrdersApi.sub();

        TwoButtonDialog.addOnPrimaryListener(DIALOG_DELETE_SHIPMENT, _twoButtonDialog_deleteShipment);
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
    }

    @Override
    public void onStop() {
        _workOrdersApi.unsub();
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_DELETE_SHIPMENT, _twoButtonDialog_deleteShipment);
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
        public void onLongClick(View v, Shipment shipment) {
            TwoButtonDialog.show(App.get(), DIALOG_DELETE_SHIPMENT,
                    R.string.dialog_delete_shipment_title,
                    R.string.dialog_delete_shipment_body,
                    R.string.btn_yes, R.string.btn_no, true, shipment);
        }

        @Override
        public void onClick(View v, Shipment shipment) {
            // TODO edit not supported yet
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _twoButtonDialog_deleteShipment = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            AppMessagingClient.setLoading(true);
            WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SHIPMENTS);
            WorkordersWebApi.deleteShipment(App.get(), _workOrderId, ((Shipment) extraData), App.get().getSpUiContext());
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
                _shipments = workOrder.getShipments();
                _adapter.setShipments(_shipments);
                populateUi();
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
