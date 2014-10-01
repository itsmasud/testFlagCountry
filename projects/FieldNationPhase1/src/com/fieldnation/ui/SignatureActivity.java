package com.fieldnation.ui;

import com.fieldnation.R;
import com.fieldnation.utils.misc;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/*-
 * 
 * Based on code from:
 * http://www.intertech.com/Blog/android-gestureoverlayview-to-capture-a-quick-signature-or-drawing/
 */

public class SignatureActivity extends ActionBarActivity {
	private static final String TAG = "ui.SignatureActivity";

	public static final String RESULT_KEY_BITMAP = "com.fieldnation.ui.SignatureActivity:SIGNATURE";
	public static final String RESULT_KEY_ARRIVAL = "com.fieldnation.ui.SignatureActivity:ARRIVAL";
	public static final String RESULT_KEY_DEPARTURE = "com.fieldnation.ui.SignatureActivity:DEPARTURE";
	public static final String RESULT_KEY_NAME = "com.fieldnation.ui.SignatureActivity:NAME";

	// UI
	private SignatureView _sigView;
	private TextView _arrivalTextView;
	private TextView _departureTextView;
	private TextView _nameTextView;
	private LinearLayout _arrivalLayout;
	private LinearLayout _departureLayout;
	private DatePickerDialog _datePicker;
	private TimePickerDialog _timePicker;

	// Data
	private String _arrivalTime;
	private String _depatureTime;
	private String _name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signature);

		_sigView = (SignatureView) findViewById(R.id.sig_view);
		_arrivalTextView = (TextView) findViewById(R.id.arrival_textview);
		_arrivalLayout = (LinearLayout) findViewById(R.id.arrival_layout);
		_arrivalLayout.setOnClickListener(_arrival_onClick);
		_departureTextView = (TextView) findViewById(R.id.departure_textview);
		_departureLayout = (LinearLayout) findViewById(R.id.departure_layout);
		_departureLayout.setOnClickListener(_departure_onClick);
		_nameTextView = (TextView) findViewById(R.id.name_textview);
		_nameTextView.setOnClickListener(_name_onClick);

		Intent intent = getIntent();
		if (intent != null && intent.getExtras() != null) {
			Bundle bundle = intent.getExtras();

			if (bundle.containsKey(RESULT_KEY_ARRIVAL)) {
				_arrivalTime = bundle.getString(RESULT_KEY_ARRIVAL);
				if (!misc.isEmptyOrNull(_arrivalTime))
					_arrivalTextView.setText(_arrivalTime);
			}
			if (bundle.containsKey(RESULT_KEY_DEPARTURE)) {
				_depatureTime = bundle.getString(RESULT_KEY_DEPARTURE);
				if (!misc.isEmptyOrNull(_depatureTime))
					_departureTextView.setText(_depatureTime);
			}
			if (bundle.containsKey(RESULT_KEY_NAME)) {
				_name = bundle.getString(RESULT_KEY_NAME);
				if (!misc.isEmptyOrNull(_name))
					_nameTextView.setText(_name);
			}
		}

		// TODO set up the date/time pickers
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.signature, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.done_menuitem:
			onDone();
			return true;
		case R.id.clear_menuitem:
			_sigView.clear();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void onDone() {
		byte[] signature = _sigView.getSignature();

		Log.v(TAG, "Sig size: " + signature.length);

		Intent intent = new Intent();
		intent.putExtra(RESULT_KEY_BITMAP, signature);
		setResult(RESULT_OK, intent);
		finish();
	}

	private View.OnClickListener _arrival_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

	private View.OnClickListener _departure_onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

	private View.OnClickListener _name_onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};
}
