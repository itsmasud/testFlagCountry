package com.fieldnation.ui;

import java.util.Calendar;

import com.fieldnation.R;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import android.content.DialogInterface;
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
	private EditTextAlertDialog _textDialog;

	// Data
	private String _name;
	private Calendar _arriveCal = Calendar.getInstance();
	private Calendar _departCal = Calendar.getInstance();

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
				String arrivalTime = bundle.getString(RESULT_KEY_ARRIVAL);
				if (!misc.isEmptyOrNull(arrivalTime)) {
					try {
						_arriveCal = ISO8601.toCalendar(arrivalTime);
					} catch (Exception ex) {
						_arriveCal = Calendar.getInstance();
					}
					_arrivalTextView.setText(misc.formatDateLong(_arriveCal) + " " + misc.formatTime(_arriveCal, false));
				}
			}
			if (bundle.containsKey(RESULT_KEY_DEPARTURE)) {
				String depatureTime = bundle.getString(RESULT_KEY_DEPARTURE);
				_departCal = Calendar.getInstance();
				if (!misc.isEmptyOrNull(depatureTime)) {
					try {
						_departCal = ISO8601.toCalendar(depatureTime);
					} catch (Exception ex) {
						_departCal = Calendar.getInstance();
					}
				}
				_departureTextView.setText(misc.formatDateLong(_departCal) + " " + misc.formatTime(_departCal, false));
			}
			if (bundle.containsKey(RESULT_KEY_NAME)) {
				_name = bundle.getString(RESULT_KEY_NAME);
				if (!misc.isEmptyOrNull(_name))
					_nameTextView.setText(_name);
			}
		}

		final Calendar c = Calendar.getInstance();
		_datePicker = DatePickerDialog.newInstance(_date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		_datePicker.setCloseOnSingleTapDay(true);
		_timePicker = TimePickerDialog.newInstance(_time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
				false, false);
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

		// validate input and ask for it
		if (misc.isEmptyOrNull(_name)) {
			_textDialog = new EditTextAlertDialog(SignatureActivity.this, "Signee Name", "Please enter the signee name");
			_textDialog.setPositiveButton("Ok", _editText_onOk_onClose);
			_textDialog.show();

			return;
		}

		Intent intent = new Intent();
		intent.putExtras(getIntent());
		intent.putExtra(RESULT_KEY_BITMAP, signature);
		intent.putExtra(RESULT_KEY_ARRIVAL, ISO8601.fromCalendar(_arriveCal));
		intent.putExtra(RESULT_KEY_DEPARTURE, ISO8601.fromCalendar(_departCal));
		intent.putExtra(RESULT_KEY_NAME, _name);
		setResult(RESULT_OK, intent);
		finish();
	}

	private DialogInterface.OnClickListener _editText_onOk = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			_name = _textDialog.getInput();
			_nameTextView.setText(_name);
		}
	};

	private DialogInterface.OnClickListener _editText_onOk_onClose = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			_name = _textDialog.getInput();
			_nameTextView.setText(_name);

			onDone();
		}
	};

	private DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
			String tag = datePickerDialog.getTag();
			if (tag.equals("arrive")) {
				_arriveCal.set(year, month, day);
			} else if (tag.equals("depart")) {
				_departCal.set(year, month, day);
			}

			_timePicker.show(getSupportFragmentManager(), datePickerDialog.getTag());
		}
	};

	private TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute) {
			String tag = view.getTag();
			if (tag.equals("arrive")) {
				_arriveCal.set(_arriveCal.get(Calendar.YEAR), _arriveCal.get(Calendar.MONTH),
						_arriveCal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
				_arrivalTextView.setText(misc.formatDateLong(_arriveCal) + " " + misc.formatTime(_arriveCal, false));
			} else if (tag.equals("depart")) {
				_departCal.set(_departCal.get(Calendar.YEAR), _departCal.get(Calendar.MONTH),
						_departCal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
				_departureTextView.setText(misc.formatDateLong(_departCal) + " " + misc.formatTime(_departCal, false));
			}
		}
	};

	private View.OnClickListener _arrival_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_datePicker.show(getSupportFragmentManager(), "arrive");
		}
	};

	private View.OnClickListener _departure_onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			_datePicker.show(getSupportFragmentManager(), "depart");
		}
	};

	private View.OnClickListener _name_onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			_textDialog = new EditTextAlertDialog(SignatureActivity.this, "Signee Name", "Please enter the signee name");
			_textDialog.setPositiveButton("Ok", _editText_onOk);
			_textDialog.show();
		}
	};

}
