package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fieldnation.ForLoopRunnable;
import com.fieldnation.R;
import com.fieldnation.data.workorder.ShipmentTracking;
import com.fieldnation.data.workorder.Workorder;

public class ShipmentView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "ui.workorder.detail.ShipmentView";

    private static final int WEB_ADD_SHIPMENT = 1;
    private static final int WEB_DEL_SHIPMENT = 2;

    // UI
    private LinearLayout _shipmentsLayout;
    private LinearLayout _addLayout;

    // Data
    private Workorder _workorder;
    private Listener _listener;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public ShipmentView(Context context) {
        this(context, null);
    }

    public ShipmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_wd_shipment, this);

        if (isInEditMode())
            return;

        _addLayout = (LinearLayout) findViewById(R.id.add_layout);
        _addLayout.setOnClickListener(_add_onClick);
        _shipmentsLayout = (LinearLayout) findViewById(R.id.shipments_linearlayout);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    public void setWorkorder(Workorder workorder, boolean isCached) {
        _workorder = workorder;
        refresh();
    }

    private void refresh() {
        final ShipmentTracking[] shipments = _workorder.getShipmentTracking();

        if (_workorder.canChangeShipments()) {
            _addLayout.setVisibility(View.VISIBLE);
        } else {
            _addLayout.setVisibility(View.GONE);
        }

        if ((shipments == null || shipments.length == 0) && !_workorder.canChangeShipments()) {
            setVisibility(View.GONE);
            return;
        }
        setVisibility(View.VISIBLE);

        if (shipments == null || shipments.length == 0)
            return;

        ForLoopRunnable r = new ForLoopRunnable(shipments.length, new Handler()) {
            private ShipmentTracking[] _shipments = shipments;

            @Override
            public void next(int i) throws Exception {
                ShipmentSummary v = null;
                if (i < _shipmentsLayout.getChildCount()) {
                    v = (ShipmentSummary) _shipmentsLayout.getChildAt(i);
                } else {
                    v = new ShipmentSummary(getContext());
                    _shipmentsLayout.addView(v);
                }
                v.setData(_workorder, _shipments[i]);
                v.setListener(_summaryListener);
            }

            @Override
            public void finish(int count) throws Exception {
                if (_shipmentsLayout.getChildCount() > count) {
                    _shipmentsLayout.removeViews(count - 1, _shipmentsLayout.getChildCount() - count);
                }
            }
        };
        post(r);
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private View.OnClickListener _add_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.addShipment();
        }
    };

    private ShipmentSummary.Listener _summaryListener = new ShipmentSummary.Listener() {
        @Override
        public void onDelete(ShipmentTracking shipment) {
            if (_listener != null && _workorder.canChangeShipments()) {
                _listener.onDelete(_workorder, shipment.getWorkorderShipmentId());
            }
        }

        @Override
        public void onAssign(ShipmentTracking shipment) {
            if (_listener != null && _workorder.canChangeShipments()) {
                _listener.onAssign(_workorder, shipment.getWorkorderShipmentId());
            }
        }
    };

//    private ShipmentAddDialog.Listener _addDialog_listener = new ShipmentAddDialog.Listener() {
//        @Override
//        public void onOk(String trackingId, String carrier, String description, boolean shipToSite) {
//            if (_listener != null && !Arrays.asList(woStatus).contains(_workorder.getStatusId())) {
//                _listener.onAddShipmentDetails(_workorder, description, shipToSite, carrier, trackingId);
//            }
//        }
//
//        @Override
//        public void onOk(String trackingId, String carrier, String description, boolean shipToSite, long taskId) {
//            if (_listener != null && !Arrays.asList(woStatus).contains(_workorder.getStatusId())) {
//                _listener.onAddShipmentDetails(_workorder, description, shipToSite, carrier, trackingId, taskId);
//            }
//        }
//
//        @Override
//        public void onCancel() {
//        }
//    };

    public interface Listener {
        public void addShipment();
//        public void onAddShipmentDetails(Workorder workorder, String description, boolean shipToSite, String carrier,
//                                         String trackingId);
//
//        public void onAddShipmentDetails(Workorder workorder, String description, boolean shipToSite, String carrier,
//                                         String trackingId, long taskId);

        public void onDelete(Workorder workorder, int shipmentId);

        public void onAssign(Workorder workorder, int shipmentId);
    }


}
