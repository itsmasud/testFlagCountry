package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class ShipmentView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.ShipmentView";

	// UI
	private EditText _trackingIdEditText;
	private Button _carrierButton;
	private EditText _carrierEditText;
	private EditText _descEditText;
	private RadioButton _shipToSiteRadio;
	private Button _addButton;
	private LinearLayout _shipmentsLayout;

	// Data
	private Workorder _workorder;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public ShipmentView(Context context) {
		this(context, null);
	}

	public ShipmentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_workorder_detail_shipment, this);

		if (isInEditMode())
			return;

		_trackingIdEditText = (EditText) findViewById(R.id.trackingid_edittext);

		_carrierButton = (Button) findViewById(R.id.carrier_button);
		_carrierButton.setOnClickListener(_carrierButton_onClick);

		_carrierEditText = (EditText) findViewById(R.id.carrier_edittext);
		_descEditText = (EditText) findViewById(R.id.description_edittext);
		_shipToSiteRadio = (RadioButton) findViewById(R.id.shiptosite_radio);

		_addButton = (Button) findViewById(R.id.add_button);
		_addButton.setOnClickListener(_addButton_onClick);

		_shipmentsLayout = (LinearLayout) findViewById(R.id.shipments_linearlayout);

	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _carrierButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

	private View.OnClickListener _addButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		refresh();
	}

	private void refresh() {
	}

}
