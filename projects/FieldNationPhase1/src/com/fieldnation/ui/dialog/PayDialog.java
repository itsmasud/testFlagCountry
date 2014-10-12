package com.fieldnation.ui.dialog;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Pay;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class PayDialog extends Dialog {
	private static String TAG = "ui.payment.PayDialog";

	// Modes
	private static final int MODE_FIXED = 0;
	private static final int MODE_HOURLY = 1;
	private static final int MODE_PER_DEVICE = 2;
	private static final int MODE_BLENDED = 3;

	// UI
	private Spinner _typeSpinner;

	private LinearLayout _fixedLayout;
	private EditText _fixedEditText;

	private LinearLayout _hourlyLayout;
	private EditText _hourlyRateEditText;
	private EditText _maxHoursEditText;

	private LinearLayout _devicesLayout;
	private EditText _deviceRateEditText;
	private EditText _maxDevicesEditText;

	private LinearLayout _blendedLayout;
	private EditText _blendedHourlyEditText;
	private EditText _blendedMaxHoursEditText;
	private EditText _extraHourlyEditText;
	private EditText _extraMaxHoursEditText;

	private Button _okButton;
	private Button _cancelButton;

	// Data
	private Pay _pay;
	private Listener _listener;
	private int _mode = MODE_FIXED;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	public PayDialog(Context context) {
		super(context);
		setContentView(R.layout.dialog_pay);

		setTitle("Requested Payment");

		_typeSpinner = (Spinner) findViewById(R.id.type_spinner);
		_typeSpinner.setOnItemSelectedListener(_type_selected);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.pay_types,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_typeSpinner.setAdapter(adapter);

		// fixed
		_fixedLayout = (LinearLayout) findViewById(R.id.fixed_layout);
		_fixedEditText = (EditText) findViewById(R.id.fixed_edittext);

		// hourly
		_hourlyLayout = (LinearLayout) findViewById(R.id.hourly_layout);
		_hourlyRateEditText = (EditText) findViewById(R.id.hourlyrate_edittext);
		_maxHoursEditText = (EditText) findViewById(R.id.maxhours_edittext);

		// per device
		_devicesLayout = (LinearLayout) findViewById(R.id.devices_layout);
		_deviceRateEditText = (EditText) findViewById(R.id.devicerate_edittext);
		_maxDevicesEditText = (EditText) findViewById(R.id.maxdevices_edittext);

		// blended
		_blendedLayout = (LinearLayout) findViewById(R.id.blended_layout);
		_blendedHourlyEditText = (EditText) findViewById(R.id.blendedhourlyrate_edittext);
		_blendedMaxHoursEditText = (EditText) findViewById(R.id.blendedmaxhours_edittext);
		_extraHourlyEditText = (EditText) findViewById(R.id.extrahours_edittext);
		_extraMaxHoursEditText = (EditText) findViewById(R.id.extramaxhours_edittext);

		_okButton = (Button) findViewById(R.id.ok_button);
		_okButton.setOnClickListener(_ok_onClick);

		_cancelButton = (Button) findViewById(R.id.cancel_button);
		_cancelButton.setOnClickListener(_cancel_onClick);
	}

	public void show(Pay pay, Listener listener) {
		_listener = listener;
		_pay = pay;
		populateUi();
		super.show();
	}

	private void clearUi() {
		_fixedLayout.setVisibility(View.GONE);
		_hourlyLayout.setVisibility(View.GONE);
		_devicesLayout.setVisibility(View.GONE);
		_blendedLayout.setVisibility(View.GONE);
	}

	private void setMode(int mode) {
		_typeSpinner.setSelection(mode);
		clearUi();
		_mode = mode;
		switch (mode) {
		case MODE_FIXED:
			_fixedLayout.setVisibility(View.VISIBLE);
			break;
		case MODE_HOURLY:
			_hourlyLayout.setVisibility(View.VISIBLE);
			break;
		case MODE_PER_DEVICE:
			_devicesLayout.setVisibility(View.VISIBLE);
			break;
		case MODE_BLENDED:
			_blendedLayout.setVisibility(View.VISIBLE);
			break;
		}
	}

	private void populateUi() {
		if (_pay.isBlendedRate()) {
			setMode(MODE_BLENDED);
			_blendedHourlyEditText.setText(_pay.getBlendedFirstHours() + "");
			_blendedMaxHoursEditText.setText(_pay.getBlendedAdditionalRate() + "");
			_extraHourlyEditText.setText(_pay.getBlendedAdditionalHours() + "");
			_extraMaxHoursEditText.setText(_pay.getBlendedAdditionalHours() + "");
		} else if (_pay.isFixedRate()) {
			setMode(MODE_FIXED);
			_fixedEditText.setText(_pay.getFixedAmount() + "");
		} else if (_pay.isHourlyRate()) {
			setMode(MODE_HOURLY);
			_hourlyRateEditText.setText(_pay.getPerHour() + "");
			_maxHoursEditText.setText(_pay.getMaxHour() + "");
		} else if (_pay.isPerDeviceRate()) {
			setMode(MODE_PER_DEVICE);
			_deviceRateEditText.setText(_pay.getPerDevice() + "");
			_maxDevicesEditText.setText(_pay.getMaxDevice() + "");
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

	private AdapterView.OnItemSelectedListener _type_selected = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			setMode(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	private View.OnClickListener _cancel_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
			if (_listener != null)
				_listener.onNothing();
		}
	};

	private View.OnClickListener _ok_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
			if (_listener == null)
				return;

			switch (_mode) {
			case MODE_FIXED:
				_listener.onFixed(Double.parseDouble(_fixedEditText.getText().toString()));
				break;
			case MODE_HOURLY:
				_listener.onHourly(Double.parseDouble(_hourlyRateEditText.getText().toString()),
						Double.parseDouble(_maxHoursEditText.getText().toString()));
				break;
			case MODE_PER_DEVICE:
				_listener.onPerDevices(Double.parseDouble(_deviceRateEditText.getText().toString()),
						Double.parseDouble(_maxDevicesEditText.getText().toString()));
				break;
			case MODE_BLENDED:
				_listener.onBlended(Double.parseDouble(_blendedHourlyEditText.getText().toString()),
						Double.parseDouble(_blendedMaxHoursEditText.getText().toString()),
						Double.parseDouble(_extraHourlyEditText.getText().toString()),
						Double.parseDouble(_extraMaxHoursEditText.getText().toString()));
				break;
			}
		}
	};

	public interface Listener {
		public void onFixed(double amount);

		public void onHourly(double rate, double max);

		public void onPerDevices(double rate, double max);

		public void onBlended(double rate, double max, double rate2, double max2);

		public void onNothing();
	}

}
