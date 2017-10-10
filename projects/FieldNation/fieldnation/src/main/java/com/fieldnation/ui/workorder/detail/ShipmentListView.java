package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.v2.data.model.Shipment;
import com.fieldnation.v2.data.model.Shipments;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

import java.util.LinkedList;
import java.util.List;

public class ShipmentListView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "ShipmentListView";

    // UI
    private LinearLayout _shipmentsLayout;
    private TextView _noShipmentsTextView;
    private Button _addButton;

    // Data
    private WorkOrder _workOrder;
    private Listener _listener;
    private ForLoopRunnable _forLoop;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public ShipmentListView(Context context) {
        this(context, null);
    }

    public ShipmentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_wd_shipment, this);

        if (isInEditMode())
            return;

        _shipmentsLayout = findViewById(R.id.shipments_linearlayout);
        _noShipmentsTextView = findViewById(R.id.noShipments_textview);

        _addButton = findViewById(R.id.add_button);
        _addButton.setOnClickListener(_add_onClick);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        if (_workOrder.getShipments() == null)
            return;

        final Shipment[] shipments = _workOrder.getShipments().getResults();

        if (shipments.length == 0 && !_workOrder.getShipments().getActionsSet().contains(Shipments.ActionsEnum.ADD)) {
            setVisibility(GONE);
            return;
        }

        if (_workOrder.getShipments().getActionsSet().contains(Shipments.ActionsEnum.ADD)) {
            _addButton.setVisibility(VISIBLE);
        } else {
            _addButton.setVisibility(GONE);
        }

        setVisibility(View.VISIBLE);

        if (shipments.length == 0) {
            _shipmentsLayout.removeAllViews();
            _noShipmentsTextView.setVisibility(VISIBLE);
            return;
        } else {
            _shipmentsLayout.setVisibility(VISIBLE);
            _noShipmentsTextView.setVisibility(GONE);
        }


        if (_forLoop != null) {
            _forLoop.cancel();
            _forLoop = null;
        }

        if (shipments.length > 0) {
            _forLoop = new ForLoopRunnable(shipments.length, new Handler()) {
                Shipment[] _shipments = shipments;
                List<View> views = new LinkedList<>();

                @Override
                public void next(int i) throws Exception {
                    ShipmentRowView v = new ShipmentRowView(getContext());
                    views.add(v);
                    v.setTag(_shipments[i]);
                    v.setData(_shipments[i]);
                    v.setOnLongClickListener(_shipment_onLongClick);
                    v.setOnClickListener(_shipment_onClick);
                    v.setEnabled(_workOrder.getShipments().getActionsSet().contains(Shipments.ActionsEnum.ADD));
                }

                @Override
                public void finish(int count) throws Exception {
                    _shipmentsLayout.removeAllViews();
                    for (View view : views) {
                        _shipmentsLayout.addView(view);
                    }
                }
            };
            postDelayed(_forLoop, 100);
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnClickListener _add_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null
                    && _workOrder.getShipments().getActionsSet().contains(Shipments.ActionsEnum.ADD)) {
                _listener.addShipment();
            }
        }


    };

    private final View.OnLongClickListener _shipment_onLongClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Shipment shipment = (Shipment) view.getTag();
            if (_listener != null
                    && shipment != null
                    && shipment.getActionsSet().contains(Shipment.ActionsEnum.DELETE)) {
                _listener.onDelete(_workOrder, shipment);
            } else {
                ToastClient.toast(App.get(), R.string.toast_cant_delete_shipment_permission, Toast.LENGTH_LONG);
            }
            return false;
        }
    };

    private final View.OnClickListener _shipment_onClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Shipment shipment = (Shipment) view.getTag();
            if (_listener != null
                    && shipment != null
                    && shipment.getActionsSet().contains(Shipments.ActionsEnum.ADD)) {
                _listener.onAssign(_workOrder, shipment);
            }
        }
    };

    public interface Listener {
        void addShipment();

        void onDelete(WorkOrder workOrder, Shipment shipment);

        void onAssign(WorkOrder workOrder, Shipment shipment);
    }

/*
    private final ShipmentListView.Listener _shipments_listener = new ShipmentListView.Listener() {
        @Override
        public void addShipment() {
            ShipmentAddDialog.show(App.get(), DIALOG_SHIPMENT_ADD, _workOrderId,
                    _workOrder.getAttachments(), getContext().getString(R.string.dialog_shipment_title), null, null);
        }

        @Override
        public void onDelete(WorkOrder workOrder, final Shipment shipment) {
            TwoButtonDialog.show(App.get(), DIALOG_DELETE_SHIPMENT,
                    R.string.dialog_delete_shipment_title,
                    R.string.dialog_delete_shipment_body,
                    R.string.btn_yes, R.string.btn_no, true, shipment);
        }

        @Override
        public void onAssign(WorkOrder workOrder, Shipment shipment) {
            // TODO STUB .onAssign()
            Log.v(TAG, "STUB .onAssign()");
            // TODO present a picker of the tasks that this can be assigned too
        }
    };
*/
}