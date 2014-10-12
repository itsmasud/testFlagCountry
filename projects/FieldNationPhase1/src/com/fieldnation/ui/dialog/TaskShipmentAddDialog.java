package com.fieldnation.ui.dialog;

import com.fieldnation.R;
import com.fieldnation.data.workorder.ShipmentTracking;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.detail.ShipmentSummary;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

public class TaskShipmentAddDialog extends Dialog {
	private static final String TAG = "ui.workorder.detail.TaskShipmentAddDialog";

	// UI
	private Button _addButton;
	private Button _cancelButton;
	private LinearLayout _shipmentsLayout;

	// Data
	private Listener _listener;
	private Workorder _workorder;

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
		shipmentList();
		
		_cancelButton = (Button) findViewById(R.id.cancel_button);
		_cancelButton.setOnClickListener(_cancel_onClick);
		_addButton = (Button) findViewById(R.id.add_button);
		_addButton.setOnClickListener(_add_onClick);

		setTitle("Add");
	}
	
	public void show(Listener listener) {
		_listener = listener;
		//setTitle(title);
		show();
	}
	
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		shipmentList();
	}
	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/
	private View.OnClickListener _add_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
			if (_listener != null){
				//@TODO
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
		public void onOk(String message);

		public void onCancel();
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
			}
		} catch (Exception ex) {}
	}
}
