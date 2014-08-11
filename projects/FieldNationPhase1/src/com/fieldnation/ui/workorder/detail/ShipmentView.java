package com.fieldnation.ui.workorder.detail;

import java.util.Calendar;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.ShipmentTracking;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

public class ShipmentView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.ShipmentView";

	private static final int WEB_ADD_SHIPMENT = 1;
	private static final int WEB_DEL_SHIPMENT = 2;

	// UI
	private Button _addButton;
	private LinearLayout _shipmentsLayout;
	private ShipmentAddDialog _addDialog;

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

		_addButton = (Button) findViewById(R.id.add_button);
		_addButton.setOnClickListener(_addButton_onClick);

		_shipmentsLayout = (LinearLayout) findViewById(R.id.shipments_linearlayout);

		_addDialog = new ShipmentAddDialog(getContext());

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
			//Log.v(TAG, resultData.getString(WorkorderService.KEY_RESPONSE_DATA));
			Log.v(TAG, resultData.getString(WorkorderService.KEY_RESPONSE_ERROR));
			// if (_workorderService != null) {
			// _gs.invalidateAuthToken(_workorderService.getAuthToken());
			// }
			// _gs.requestAuthentication(_authclient);
			_workorder.dispatchOnChange();
		}

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			// TODO Method Stub: onSuccess()
			Log.v(TAG, "Method Stub: onSuccess()");
			Log.v(TAG, resultData.toString());
			_workorder.dispatchOnChange();
		}
	};

	private View.OnClickListener _addButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_addDialog.show(R.string.add, _addDialog_listener);
		}
	};

	private ShipmentSummary.Listener _summaryListener = new ShipmentSummary.Listener() {
		@Override
		public void onDelete(ShipmentTracking shipment) {
			getContext().startService(
					_workorderService.deleteShipment(WEB_DEL_SHIPMENT, _workorder.getWorkorderId(),
							shipment.getWorkorderShipmentId()));
		}
	};

	private ShipmentAddDialog.Listener _addDialog_listener = new ShipmentAddDialog.Listener() {
		@Override
		public void onOk(String trackingId, String carrier, String description, boolean shipToSite) {
			getContext().startService(
					_workorderService.addShipmentDetails(WEB_ADD_SHIPMENT, _workorder.getWorkorderId(), description,
							shipToSite, carrier, null, trackingId));
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

}
