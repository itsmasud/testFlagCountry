package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.ShipmentTracking;
import com.fieldnation.data.workorder.Workorder;

public class ShipmentSummary extends RelativeLayout {
    private static final String TAG = "ui.workorder.detail.ShipmentSummary";

    // UI
    private TextView _trackingIdTextView;
    private TextView _carrierTextView;
    private TextView _descTextView;
    private TextView _directionTextView;
    private ImageButton _deleteImageButton;
    private Button _assignButton;

    // Data
    private Workorder _workorder;
    private ShipmentTracking _shipment;
    private Listener _listener;
    private boolean _taskMode = false;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public ShipmentSummary(Context context) {
        super(context);
        init();
    }

    public ShipmentSummary(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShipmentSummary(Context context, AttributeSet attrs, int defStyle) {
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

        _deleteImageButton = (ImageButton) findViewById(R.id.delete_imagebutton);
        _deleteImageButton.setOnClickListener(_delete_onClick);

        _assignButton = (Button) findViewById(R.id.assign_button);
        _assignButton.setOnClickListener(_assign_onClick);
    }

    public void setData(Workorder workorder, ShipmentTracking shipment) {
        _shipment = shipment;
        _workorder = workorder;

        populateUi();
    }

    private void populateUi() {
        if (_shipment == null)
            return;

        if (_assignButton == null)
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

        if (_workorder.canChangeShipments()) {
            if (_taskMode)
                _assignButton.setVisibility(View.VISIBLE);
            else
                _deleteImageButton.setVisibility(View.VISIBLE);

        } else {
            if (_taskMode)
                _assignButton.setVisibility(View.GONE);
            else
                _deleteImageButton.setVisibility(View.INVISIBLE);
        }

        if (_taskMode) {
            _deleteImageButton.setVisibility(INVISIBLE);
            _trackingIdTextView.setVisibility(GONE);
            _carrierTextView.setVisibility(GONE);

        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private View.OnClickListener _delete_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onDelete(_shipment);
            }
        }
    };

    private View.OnClickListener _assign_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onAssign(_shipment);
            }
        }
    };

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void hideForTaskShipmentDialog() {
        _taskMode = true;
        populateUi();
    }

    public interface Listener {
        public void onDelete(ShipmentTracking shipment);

        public void onAssign(ShipmentTracking shipment);
    }
}
