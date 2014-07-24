package com.fieldnation.ui.workorder.detail;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

public class ShipmentView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.ShipmentView";

	private static final int WEB_ADD_SHIPMENT = 1;

	// UI
	private EditText _trackingIdEditText;
	private Spinner _carrierSpinner;
	private EditText _carrierEditText;
	private EditText _descEditText;
	private RadioButton _shipToSiteRadio;
	private Button _addButton;
	private LinearLayout _shipmentsLayout;

	// Data
	private Workorder _workorder;
	private GlobalState _gs;
	private WorkorderService _workorderService;

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

		_gs = (GlobalState) context.getApplicationContext();
		_gs.requestAuthentication(_authclient);

		_trackingIdEditText = (EditText) findViewById(R.id.trackingid_edittext);

		_carrierSpinner = (Spinner) findViewById(R.id.carrier_spinner);
		_carrierSpinner.setOnItemSelectedListener(_carrier_selected);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.carrier_list,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_carrierSpinner.setAdapter(adapter);

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
	private AuthenticationClient _authclient = new AuthenticationClient() {

		@Override
		public void onAuthenticationFailed(Exception ex) {
			_gs.requestAuthenticationDelayed(_authclient);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			_workorderService = new WorkorderService(getContext(), username, authToken, _resultReceiver);
		}

		@Override
		public GlobalState getGlobalState() {
			return _gs;
		}
	};

	private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(new Handler()) {
		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {

			Log.v(TAG, errorType);
			Log.v(TAG, resultData.toString());
			// if (_workorderService != null) {
			// _gs.invalidateAuthToken(_workorderService.getAuthToken());
			// }
			// _gs.requestAuthentication(_authclient);
		}

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			// TODO Method Stub: onSuccess()
			Log.v(TAG, "Method Stub: onSuccess()");
			Log.v(TAG, resultData.toString());
		}
	};

	private AdapterView.OnItemSelectedListener _carrier_selected = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			// TODO Method Stub: onItemSelected()
			Log.v(TAG, "Method Stub: onItemSelected()");

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Method Stub: onNothingSelected()
			Log.v(TAG, "Method Stub: onNothingSelected()");
		}
	};

	private View.OnClickListener _addButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			getContext().startService(
					_workorderService.addShipmentDetails(WEB_ADD_SHIPMENT, _workorder.getWorkorderId(),
							_descEditText.getText().toString(), _shipToSiteRadio.isSelected(),
							(String) _carrierSpinner.getSelectedItem(), null,
							_trackingIdEditText.getText().toString()));
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
