package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.ShipmentTracking;
import com.fieldnation.data.workorder.Workorder;

public class ShipmentRowView extends RelativeLayout {
    private static final String TAG = "ShipmentRowView";

    // UI
    private TextView _trackingIdTextView;
    private TextView _carrierTextView;
    private TextView _descTextView;
    private TextView _directionTextView;

    // Data
    private Workorder _workorder;
    private ShipmentTracking _shipment;
    private Listener _listener;
    private boolean _taskMode = false;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public ShipmentRowView(Context context) {
        super(context);
        init();
    }

    public ShipmentRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShipmentRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_shipment_summary, this);

        if (isInEditMode())
            return;

        _trackingIdTextView = (TextView) findViewById(R.id.trackingid_textview);
        _carrierTextView = (TextView) findViewById(R.id.carrier_textview);
        _descTextView = (TextView) findViewById(R.id.description_textview);
        _directionTextView = (TextView) findViewById(R.id.direction_textview);

        setOnLongClickListener(_delete_onClick);
        setOnClickListener(_assign_onClick);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void hideForTaskShipmentDialog() {
        _taskMode = true;
        populateUi();
    }

    public void setData(Workorder workorder, ShipmentTracking shipment) {
        _shipment = shipment;
        _workorder = workorder;

        populateUi();
    }

    private void populateUi() {
        if (_shipment == null)
            return;

        if (_trackingIdTextView == null)
            return;

        if (_shipment.getTrackingId() != null) {
            _trackingIdTextView.setText(_shipment.getTrackingId());
        }

        String carrier = _shipment.getCarrier();
        if (carrier == null) {
            carrier = _shipment.getCarrierOther();
        }
        _carrierTextView.setText(carrier);

        _descTextView.setText(_shipment.getName());
        boolean toSite = _shipment.getDirection().equals("to_site");
        _directionTextView.setText(toSite ? "To Site" : "From Site");

        setEnabled(_workorder.canChangeShipments());
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnLongClickListener _delete_onClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (_listener != null) {
                _listener.onDelete(_shipment);
                return true;
            }
            return false;
        }
    };

    private final View.OnClickListener _assign_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onEdit(_shipment);
            }
        }
    };

    public interface Listener {
        void onDelete(ShipmentTracking shipment);

        void onEdit(ShipmentTracking shipment);
    }
}
