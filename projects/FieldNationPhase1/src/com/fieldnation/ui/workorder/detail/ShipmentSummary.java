package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.ShipmentTracking;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShipmentSummary extends RelativeLayout {
	private static final String TAG = "ui.workorder.detail.ShipmentSummary";

	// UI
	private TextView _trackingIdTextView;
	private TextView _carrierTextView;
	private TextView _descTextView;
	private TextView _directionTextView;
	private ImageButton _deleteImageButton;

	// Data
	private ShipmentTracking _shipment;
	private Listener _listener;

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
	}

	public void setShipmentTracking(ShipmentTracking shipment) {
		_shipment = shipment;

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

	public void setListener(Listener listener) {
		_listener = listener;
	}

	public interface Listener {
		public void onDelete(ShipmentTracking shipment);
	}
}
