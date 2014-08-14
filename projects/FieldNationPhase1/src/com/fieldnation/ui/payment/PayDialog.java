package com.fieldnation.ui.payment;

import com.fieldnation.R;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class PayDialog extends Dialog {
	private static String TAG = "ui.payment.PayDialog";

	// UI
	private Spinner _typeSpinner;

	private FrameLayout _fixedLayout;
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

	// Data

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	public PayDialog(Context context) {
		super(context);
		setContentView(R.layout.dialog_pay);

		_typeSpinner = (Spinner) findViewById(R.id.type_spinner);
		_typeSpinner.setOnItemSelectedListener(_type_selected);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.pay_types,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_typeSpinner.setAdapter(adapter);

		// fixed
		_fixedLayout = (FrameLayout) findViewById(R.id.fixed_layout);
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
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

	private void clearUi() {
		_fixedLayout.setVisibility(View.GONE);
		_hourlyLayout.setVisibility(View.GONE);
		_devicesLayout.setVisibility(View.GONE);
		_blendedLayout.setVisibility(View.GONE);
	}

	private AdapterView.OnItemSelectedListener _type_selected = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			clearUi();
			switch (position) {
			case 0:
				_fixedLayout.setVisibility(View.VISIBLE);
				break;
			case 1:
				_hourlyLayout.setVisibility(View.VISIBLE);
				break;
			case 2:
				_devicesLayout.setVisibility(View.VISIBLE);
				break;
			case 3:
				_blendedLayout.setVisibility(View.VISIBLE);
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Method Stub: onNothingSelected()
			Log.v(TAG, "Method Stub: onNothingSelected()");
		}
	};

	private View.OnClickListener _ok_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

}
