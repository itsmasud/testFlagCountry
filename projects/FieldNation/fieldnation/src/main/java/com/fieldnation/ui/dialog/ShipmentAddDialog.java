package com.fieldnation.ui.dialog;

import com.fieldnation.R;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class ShipmentAddDialog extends Dialog {
	private static final String TAG = "ui.workorder.detail.ShipmentAddDialog";

	// UI
	private EditText _trackingIdEditText;
	private Spinner _carrierSpinner;
	private ArrayAdapter<CharSequence> _carrierAdapter;
	private EditText _carrierEditText;
	private EditText _descriptionEditText;
	private RadioButton _shipToSiteRadio;
	private Button _okButton;
	private Button _cancelButton;
	private LinearLayout _carrierNameLayout;

	// Data
	private Listener _listener;
	private long _taskId = 0;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	public ShipmentAddDialog(Context context) {
		super(context);
		setContentView(R.layout.dialog_add_shipment);

		_trackingIdEditText = (EditText) findViewById(R.id.trackingid_edittext);
		_trackingIdEditText.setOnEditorActionListener(_onEditor);

		_carrierSpinner = (Spinner) findViewById(R.id.carrier_spinner);
		_carrierSpinner.setOnItemSelectedListener(_carrier_selected);

		_carrierAdapter = ArrayAdapter.createFromResource(getContext(), R.array.carrier_list,
				android.R.layout.simple_spinner_item);
		_carrierAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_carrierSpinner.setAdapter(_carrierAdapter);

		_carrierNameLayout = (LinearLayout) findViewById(R.id.carriername_layout);
		_carrierEditText = (EditText) findViewById(R.id.carrier_edittext);
		_carrierEditText.setOnEditorActionListener(_onEditor);
		_descriptionEditText = (EditText) findViewById(R.id.description_edittext);
		_descriptionEditText.setOnEditorActionListener(_onEditor);
		_shipToSiteRadio = (RadioButton) findViewById(R.id.shiptosite_radio);

		_okButton = (Button) findViewById(R.id.ok_button);
		_okButton.setOnClickListener(_okButton_onClick);

		_cancelButton = (Button) findViewById(R.id.cancel_button);
		_cancelButton.setOnClickListener(_cancel_onClick);

	}

	public void show(int titleResId, Listener listener) {
		show(getContext().getText(titleResId), listener);
	}

	public void show(CharSequence title, Listener listener) {
		_listener = listener;
		setTitle(title);
		show();
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private TextView.OnEditorActionListener _onEditor = new TextView.OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			boolean handled = false;

			if (actionId == EditorInfo.IME_ACTION_NEXT) {
				if (v == _trackingIdEditText) {
					if (_carrierNameLayout.getVisibility() == View.VISIBLE) {
						_carrierEditText.requestFocus();
						handled = true;
					} else {
						_descriptionEditText.requestFocus();
						handled = true;
					}
				} else if (v == _carrierEditText) {
					_descriptionEditText.requestFocus();
					handled = true;
				}
			} else if (actionId == EditorInfo.IME_ACTION_DONE) {
				_okButton_onClick.onClick(null);
				handled = true;
			}

			return handled;
		}
	};

	private AdapterView.OnItemSelectedListener _carrier_selected = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			if ("Other".equals(_carrierSpinner.getSelectedItem().toString())) {
				_carrierNameLayout.setVisibility(View.VISIBLE);
			} else {
				_carrierNameLayout.setVisibility(View.GONE);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	private View.OnClickListener _okButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO validate input
			if (_listener != null) {				
				if(_taskId != 0){
					if ("Other".equals(_carrierSpinner.getSelectedItem().toString())) {
						_listener.onOk(_trackingIdEditText.getText().toString(), _carrierEditText.getText().toString(),
								_descriptionEditText.getText().toString(), _shipToSiteRadio.isChecked(), _taskId);
					} else {
						_listener.onOk(_trackingIdEditText.getText().toString(),
								_carrierSpinner.getSelectedItem().toString(), _descriptionEditText.getText().toString(),
								_shipToSiteRadio.isChecked(), _taskId);
					}
					
				} else {				
					if ("Other".equals(_carrierSpinner.getSelectedItem().toString())) {
						_listener.onOk(_trackingIdEditText.getText().toString(), _carrierEditText.getText().toString(),
								_descriptionEditText.getText().toString(), _shipToSiteRadio.isChecked());
					} else {
						_listener.onOk(_trackingIdEditText.getText().toString(),
								_carrierSpinner.getSelectedItem().toString(), _descriptionEditText.getText().toString(),
							_shipToSiteRadio.isChecked());
				}
				}
			}
			ShipmentAddDialog.this.dismiss();
		}
	};

	private View.OnClickListener _cancel_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
		}
	};

	public interface Listener {
		public void onOk(String trackingId, String carrier, String description, boolean shipToSite, long taskId);
		public void onOk(String trackingId, String carrier, String description, boolean shipToSite);

		public void onCancel();
	}
	
	public void setTaskId(long taskId){
		_taskId = taskId;
	}

}
