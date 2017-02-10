package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.ShipmentTracking;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fntools.ForLoopRunnable;

import java.util.Random;

public class ShipmentListView extends LinearLayout {
    private static final String TAG = "ShipmentListView";

    // UI
    private LinearLayout _shipmentsLayout;
    private TextView _noShipmentsTextView;
    private Button _addButton;

    // Data
    private Workorder _workorder;
    private Listener _listener;

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

        _shipmentsLayout = (LinearLayout) findViewById(R.id.shipments_linearlayout);
        _noShipmentsTextView = (TextView) findViewById(R.id.noShipments_textview);

        _addButton = (Button) findViewById(R.id.add_button);
        _addButton.setOnClickListener(_add_onClick);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        refresh();
    }

    private void refresh() {
        final ShipmentTracking[] shipments = _workorder.getShipmentTracking();

        if (_workorder.canChangeShipments()) {
            _addButton.setVisibility(View.VISIBLE);
        } else {
            _addButton.setVisibility(View.GONE);
        }

        if ((shipments == null || shipments.length == 0) && !_workorder.canChangeShipments()) {
            setVisibility(View.GONE);
            return;
        }
        setVisibility(View.VISIBLE);

        if (shipments == null || shipments.length == 0) {
            _shipmentsLayout.setVisibility(GONE);
            _noShipmentsTextView.setVisibility(VISIBLE);
            return;
        } else {
            _shipmentsLayout.setVisibility(VISIBLE);
            _noShipmentsTextView.setVisibility(GONE);
        }

        if (_shipmentsLayout.getChildCount() > shipments.length) {
            _shipmentsLayout.removeViews(shipments.length - 1, _shipmentsLayout.getChildCount() - shipments.length);
        }

        ForLoopRunnable r = new ForLoopRunnable(shipments.length, new Handler()) {
            private final ShipmentTracking[] _shipments = shipments;

            @Override
            public void next(int i) throws Exception {
                ShipmentRowView v = null;
                if (i < _shipmentsLayout.getChildCount()) {
                    v = (ShipmentRowView) _shipmentsLayout.getChildAt(i);
                } else {
                    v = new ShipmentRowView(getContext());
                    _shipmentsLayout.addView(v);
                }
                v.setData(_workorder, _shipments[i]);
                v.setListener(_summaryListener);
            }
        };
        postDelayed(r, new Random().nextInt(1000));
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnClickListener _add_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.addShipment();
        }
    };

    private final ShipmentRowView.Listener _summaryListener = new ShipmentRowView.Listener() {
        @Override
        public void onDelete(ShipmentTracking shipment) {
            if (_listener != null && _workorder.canChangeShipments()) {
                _listener.onDelete(_workorder, shipment);
            }
        }

        @Override
        public void onEdit(ShipmentTracking shipment) {
            // TODO need to show an edit dialog
            if (_listener != null && _workorder.canChangeShipments()) {
                _listener.onAssign(_workorder, shipment);
            }
        }
    };

    public interface Listener {
        void addShipment();

        void onDelete(Workorder workorder, ShipmentTracking shipmentId);

        void onAssign(Workorder workorder, ShipmentTracking shipmentId);
    }


}
