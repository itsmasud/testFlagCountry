package com.fieldnation.ui.workorder.detail;

import java.util.Calendar;

import com.fieldnation.R;
import com.fieldnation.ui.WorkLogDialog.Listener;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class ShipmentAddDialog extends Dialog {
	private static final String TAG = "ui.workorder.detail.ShipmentAddDialog";

	// UI
	private EditText _trackingIdEditText;
	private Spinner _carrierSpinner;
	private EditText _carrierEditText;
	private EditText _descriptionEditText;
	private RadioButton _shipToSiteRadio;
	private Button _addButton;

	// Data
	private Listener _listener;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	public ShipmentAddDialog(Context context) {
		super(context);
		setContentView(R.layout.dialog_add_shipment);

		_trackingIdEditText = (EditText) findViewById(R.id.trackingid_edittext);
		_carrierSpinner = (Spinner) findViewById(R.id.carrier_spinner);
		_carrierSpinner.setOnItemSelectedListener(_carrier_selected);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.carrier_list,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_carrierSpinner.setAdapter(adapter);

		_carrierEditText = (EditText) findViewById(R.id.carrier_edittext);
		_descriptionEditText = (EditText) findViewById(R.id.description_edittext);
		_shipToSiteRadio = (RadioButton) findViewById(R.id.shiptosite_radio);

		_addButton = (Button) findViewById(R.id.add_button);
		_addButton.setOnClickListener(_addButton_onClick);
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
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
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

	public void show(int titleResId, Listener listener) {
		show(getContext().getText(titleResId), listener);
	}

	public void show(CharSequence title, Listener listener) {
		_listener = listener;

		setTitle(title);

		show();
	}

	public interface Listener {
		public void onOk(String trackingId, String carrier, String description, boolean shipToSite);
	}

}
