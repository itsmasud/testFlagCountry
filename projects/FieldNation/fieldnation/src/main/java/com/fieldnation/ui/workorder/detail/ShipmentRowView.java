package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Shipment;
import com.fieldnation.v2.data.model.ShipmentCarrier;
import com.fieldnation.v2.data.model.Shipments;
import com.fieldnation.v2.data.model.WorkOrder;

public class ShipmentRowView extends RelativeLayout {
    private static final String TAG = "ShipmentRowView";

    // UI
    private TextView _trackingIdTextView;
    private TextView _carrierTextView;
    private TextView _descTextView;
    private TextView _directionTextView;

    // Data
    private WorkOrder _workOrder;
    private Shipment _shipment;
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

    public void setData(WorkOrder workOrder, Shipment shipment) {
        _shipment = shipment;
        _workOrder = workOrder;

        populateUi();
    }

    private void populateUi() {
        if (_shipment == null)
            return;

        if (_trackingIdTextView == null)
            return;

        if (_shipment.getCarrier() != null && _shipment.getCarrier().getTracking() != null) {
            _trackingIdTextView.setVisibility(VISIBLE);
            _trackingIdTextView.setText(_shipment.getCarrier().getTracking());
        } else {
            _trackingIdTextView.setVisibility(GONE);
        }

        ShipmentCarrier.NameEnum carrier;
        if (_shipment.getCarrier() != null && (carrier = _shipment.getCarrier().getName()) != null) {
            switch (carrier) {
                case USPS:
                    _carrierTextView.setText(carrier.toString().toUpperCase());
                    break;
                case UPS:
                    _carrierTextView.setText(carrier.toString().toUpperCase());
                    break;
                case FEDEX:
                    _carrierTextView.setText(misc.capitalize(carrier.toString()));
                    break;
                case OTHER:
                    _carrierTextView.setText(misc.capitalize(carrier.toString()));
                    break;
            }
        }

        _descTextView.setText(_shipment.getName());
        if (_shipment.getDirection() != null && _shipment.getDirection() == Shipment.DirectionEnum.TO_SITE) {
            _directionTextView.setText("To Site");
        } else {
            _directionTextView.setText("From Site");
        }

        setEnabled(_workOrder.getShipments().getActionsSet().contains(Shipments.ActionsEnum.ADD));
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
        void onDelete(Shipment shipment);

        void onEdit(Shipment shipment);
    }
}
