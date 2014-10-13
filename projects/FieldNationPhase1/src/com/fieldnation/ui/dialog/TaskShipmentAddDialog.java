package com.fieldnation.ui.dialog;

import com.fieldnation.R;
import com.fieldnation.data.workorder.ShipmentTracking;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.detail.ShipmentSummary;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class TaskShipmentAddDialog extends Dialog {
	private static final String TAG = "ui.workorder.detail.TaskShipmentAddDialog";

	// UI	
	private Button _addButton;
	private Button _cancelButton;
	private LinearLayout _shipmentsLayout;
	private ShipmentAddDialog _addDialog;

	// Data
	private Listener _listener;
	private Workorder _workorder;
	private long _taskId;
	

	/*-*****************************-*/
	/*-			Life Cycle			-*/
	/*-*****************************-*/
	public TaskShipmentAddDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	protected TaskShipmentAddDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	public TaskShipmentAddDialog(Context context) {
		super(context);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_task_add_shipment);

		_shipmentsLayout = (LinearLayout) findViewById(R.id.shipments_linearlayout);		
		
		_cancelButton = (Button) findViewById(R.id.cancel_button);
		_cancelButton.setOnClickListener(_cancel_onClick);
		_addButton = (Button) findViewById(R.id.add_button);
		_addButton.setOnClickListener(_add_onClick);

		_addDialog = new ShipmentAddDialog(getContext());		
		setTitle("Assign/Add New");
		shipmentList();
	}
	
	public void show(String title, Listener listener) {
		_listener = listener;
		setTitle(title);
		show();
	}
	
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		shipmentList();
	}
	
	public void setTaskId(long taskId){
		_taskId = taskId;
	}
	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/
	private ShipmentAddDialog.Listener _addDialog_listener = new ShipmentAddDialog.Listener() {
		@Override
		public void onOk(String trackingId, String carrier, String description, boolean shipToSite) {
			if (_listener != null) {
				_listener.onAddShipmentDetails(_workorder, description, shipToSite, carrier, trackingId);
			}
		}
		
		@Override
		public void onOk(String trackingId, String carrier, String description, boolean shipToSite, long taskId) {
			if (_listener != null) {
				_listener.onAddShipmentDetails(_workorder, description, shipToSite, carrier, trackingId, taskId);
			}
		}

		@Override
		public void onCancel() {
		}
	};
	
	private ShipmentSummary.Listener _summaryListener = new ShipmentSummary.Listener() {
		@Override
		public void onDelete(ShipmentTracking shipment) {
			if (_listener != null) {
				_listener.onDelete(_workorder, shipment.getWorkorderShipmentId());
			}
		}
		
		public void onAssign(ShipmentTracking shipment) {
			dismiss();
			if (_listener != null) {
				_listener.onAssign(_workorder, shipment.getWorkorderShipmentId(), _taskId);
			}
		}
	};
	
	private View.OnClickListener _add_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
			if (_listener != null) {
				_addDialog.setTaskId(_taskId);
				_addDialog.show(R.string.add, _addDialog_listener);
			}
		}
	};

	private View.OnClickListener _cancel_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
			if (_listener != null)
				_listener.onCancel();
		}
	};

	public interface Listener {		
		public void onCancel();		
		public void onDelete(Workorder workorder, int shipmentId);
		public void onAssign(Workorder workorder, int shipmentId, long taskId);
		
		public void onAddShipmentDetails(Workorder workorder, String description, boolean shipToSite, String carrier,
				String trackingId);
		public void onAddShipmentDetails(Workorder workorder, String description, boolean shipToSite, String carrier,
				String trackingId, long taskId);
	}
	
	private void shipmentList() {		
		if (_workorder == null)
			return;
		
		try{		
			ShipmentTracking[] shipments = _workorder.getShipmentTracking();
			_shipmentsLayout.removeAllViews();
			
			if (shipments == null)
				return;
			
			for (int i = 0; i < shipments.length; i++) {
				ShipmentSummary view = new ShipmentSummary(getContext());
				_shipmentsLayout.addView(view);
				view.setShipmentTracking(shipments[i]);
				view.hideForTaskShipmentDialog();
				view.setListener(_summaryListener);
			}
			
		} catch (Exception ex) {}
	}
}
