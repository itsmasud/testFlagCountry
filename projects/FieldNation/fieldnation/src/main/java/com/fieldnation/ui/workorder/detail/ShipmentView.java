package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fieldnation.R;
import com.fieldnation.data.workorder.ShipmentTracking;
import com.fieldnation.data.workorder.Workorder;

import java.util.Arrays;

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
    private Integer[] woStatus = {5, 6, 7}; //work order status approved, paid, canceled

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
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        refresh();
    }

    private void refresh() {
        ShipmentTracking[] shipments = _workorder.getShipmentTracking();

        _shipmentsLayout.removeAllViews();

        if (shipments == null)
            return;

        for (int i = 0; i < shipments.length; i++) {
            ShipmentSummary view = new ShipmentSummary(getContext());
            _shipmentsLayout.addView(view);
            view.setShipmentTracking(shipments[i]);
            view.setListener(_summaryListener);
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private View.OnClickListener _add_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!Arrays.asList(woStatus).contains(_workorder.getStatusId())) {
                if (_listener != null)
                    _listener.addShipment();
            }
        }
    };

    private ShipmentSummary.Listener _summaryListener = new ShipmentSummary.Listener() {
        @Override
        public void onDelete(ShipmentTracking shipment) {
            if (_listener != null && !Arrays.asList(woStatus).contains(_workorder.getStatusId())) {
                _listener.onDelete(_workorder, shipment.getWorkorderShipmentId());
            }
        }

        @Override
        public void onAssign(ShipmentTracking shipment) {
            if (_listener != null && !Arrays.asList(woStatus).contains(_workorder.getStatusId())) {
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
