package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class ShipmentAddDialog extends Dialog {
	private static final String TAG = "ui.workorder.detail.ShipmentAddDialog";

	// UI
	private EditText _trackingIdEditText;
	private Spinner _carrierSpinner;
	private ArrayAdapter<CharSequence> _carrierAdapter;
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

		_carrierAdapter = ArrayAdapter.createFromResource(getContext(), R.array.carrier_list,
				android.R.layout.simple_spinner_item);
		_carrierAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_carrierSpinner.setAdapter(_carrierAdapter);

		_carrierEditText = (EditText) findViewById(R.id.carrier_edittext);
		_carrierEditText.setVisibility(View.GONE);
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
			if ("Other".equals(_carrierSpinner.getSelectedItem().toString())) {
				_carrierEditText.setVisibility(View.VISIBLE);
			} else {
				_carrierEditText.setVisibility(View.GONE);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	private View.OnClickListener _addButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO validate input
			if (_listener != null) {
				if ("Other".equals(_carrierSpinner.getSelectedItem().toString())) {
					_listener.onOk(_trackingIdEditText.getText().toString(), _carrierEditText.getText().toString(),
							_descriptionEditText.getText().toString(), _shipToSiteRadio.isChecked());
				} else {
					_listener.onOk(_trackingIdEditText.getText().toString(),
							_carrierSpinner.getSelectedItem().toString(), _descriptionEditText.getText().toString(),
							_shipToSiteRadio.isChecked());
				}
			}
			ShipmentAddDialog.this.dismiss();
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
