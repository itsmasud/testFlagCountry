package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	public ShipmentSummary(Context context) {
		this(context, null, -1);
	}

	public ShipmentSummary(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public ShipmentSummary(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.view_shipment_summary, this);

		if (isInEditMode())
			return;

		_trackingIdTextView = (TextView) findViewById(R.id.trackingid_textview);
		_carrierTextView = (TextView) findViewById(R.id.carrier_textview);
		_descTextView = (TextView) findViewById(R.id.description_textview);
		_directionTextView = (TextView) findViewById(R.id.direction_textview);
		_deleteImageButton = (ImageButton) findViewById(R.id.delete_imagebutton);
		_deleteImageButton.setOnClickListener(_delete_onClick);
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _delete_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");

		}
	};
}
