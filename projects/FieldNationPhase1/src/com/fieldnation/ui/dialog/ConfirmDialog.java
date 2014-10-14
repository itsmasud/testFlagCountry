package com.fieldnation.ui.dialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmDialog extends Dialog {
	private static final String TAG = "ui.dialog.ConfirmDialog";

	// Ui
	private Button _startDateButton;
	private Button _durationButton;
	private Button _okButton;
	private Button _cancelButton;
	private TextView _schedule1TextView;
	private TextView _schedule2TextView;
	private TextView _termsTextView;

	private DatePickerDialog _datePicker;
	private TimePickerDialog _timePicker;
	private DurationDialog _durationDialog;

	// Data
	private Listener _listener;
	private Calendar _startCalendar;
	private FragmentManager _fm;
	private long _durationMilliseconds;
	private Schedule _schedule;

	public ConfirmDialog(Context context) {
		super(context);
		setContentView(R.layout.dialog_confirm);
		setTitle(R.string.confirm);

		_startDateButton = (Button) findViewById(R.id.start_date_button);
		_startDateButton.setOnClickListener(_startDate_onClick);

		_durationButton = (Button) findViewById(R.id.duration_button);
		_durationButton.setOnClickListener(_duration_onClick);

		_okButton = (Button) findViewById(R.id.ok_button);
		_okButton.setOnClickListener(_ok_onClick);

		_cancelButton = (Button) findViewById(R.id.cancel_button);
		_cancelButton.setOnClickListener(_cancel_onClick);

		_schedule1TextView = (TextView) findViewById(R.id.schedule1_textview);
		_schedule2TextView = (TextView) findViewById(R.id.schedule2_textview);

		_termsTextView = (TextView) findViewById(R.id.terms_textview);
		_termsTextView.setOnClickListener(_terms_onClick);

		final Calendar c = Calendar.getInstance();
		_datePicker = DatePickerDialog.newInstance(_date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		_datePicker.setCloseOnSingleTapDay(true);
		_timePicker = TimePickerDialog.newInstance(_time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
				false, false);

		_startCalendar = Calendar.getInstance();

		_durationDialog = new DurationDialog(getContext());
	}

	public void show(FragmentManager fm, Schedule schedule, Listener listener) {
		_fm = fm;
		_listener = listener;
		_schedule = schedule;

		if (_schedule.isExact()) {
			_schedule2TextView.setVisibility(View.GONE);
			try {
				Calendar cal = ISO8601.toCalendar(schedule.getStartTime());
				String dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + misc.formatDateLong(cal);
				String time = misc.formatTime(cal, false) + " " + cal.getTimeZone().getDisplayName(false,
						TimeZone.SHORT);

				_schedule1TextView.setText("You will need to arrive exactly on " + dayDate + " at " + time + ".");

				_startCalendar = cal;
				_startDateButton.setText(misc.formatDateTimeLong(_startCalendar));
				_startDateButton.setEnabled(false);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			_schedule2TextView.setVisibility(View.GONE);
			try {
				Calendar cal = ISO8601.toCalendar(schedule.getStartTime());
				String dayDate;
				String time = "";

				dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + misc.formatDateLong(cal);
				time = misc.formatTime(cal, false);

				String msg = "You will need to arrive between " + dayDate + " at " + time + " and ";

				Calendar cal2 = ISO8601.toCalendar(schedule.getEndTime());

				// same day
				if (cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
					time = misc.formatTime(cal2, false) + " " + cal2.getTimeZone().getDisplayName(false, TimeZone.SHORT);
					msg += time + ".";

				} else {
					dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal2.getTime()) + " " + misc.formatDateLong(cal2);
					time = misc.formatTime(cal2, false) + " " + cal2.getTimeZone().getDisplayName(false, TimeZone.SHORT);
					msg += dayDate + " at " + time + ".";
				}

				_schedule1TextView.setText(msg);

				_startCalendar = cal;
				_startDateButton.setText(misc.formatDateTimeLong(_startCalendar));
				_startDateButton.setEnabled(true);
				_datePicker.setCloseOnSingleTapDay(true);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		show();
	}

	/*-*****************************-*/
	/*-				Events			-*/
	/*-*****************************-*/
	private DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
			_startCalendar.set(year, month, day);
			_timePicker.show(_fm, datePickerDialog.getTag());
		}
	};

	private TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute) {
			try {
				_startCalendar.set(_startCalendar.get(Calendar.YEAR), _startCalendar.get(Calendar.MONTH),
						_startCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

				long start = ISO8601.toUtc(_schedule.getStartTime());
				long end = ISO8601.toUtc(_schedule.getEndTime());

				long input = _startCalendar.getTimeInMillis();

				if (input < start || input > end) {
					Toast.makeText(getContext(), "Arrival time is out of range. Please try again", Toast.LENGTH_LONG).show();
					_startCalendar = ISO8601.toCalendar(_schedule.getStartTime());
				}
				
				_startDateButton.setText(misc.formatDateTimeLong(_startCalendar));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};

	private View.OnClickListener _terms_onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");

		}
	};
	private View.OnClickListener _startDate_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				Calendar start = ISO8601.toCalendar(_schedule.getStartTime());
				_datePicker.initialize(_date_onSet, start.get(Calendar.YEAR), start.get(Calendar.MONTH),
						start.get(Calendar.DAY_OF_MONTH), true);

				_timePicker.initialize(_time_onSet, start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), false,
						false);

				if (!_schedule.isExact()) {
					Calendar stop = ISO8601.toCalendar(_schedule.getEndTime());

					if (start.get(Calendar.YEAR) == stop.get(Calendar.YEAR) && start.get(Calendar.DAY_OF_YEAR) == stop.get(Calendar.DAY_OF_YEAR)) {
						_timePicker.show(_fm, "");
					} else {
						_datePicker.show(_fm, "");
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};

	private View.OnClickListener _cancel_onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			dismiss();
		}
	};
	private View.OnClickListener _ok_onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (_listener != null) {
				_listener.onOk(ISO8601.fromCalendar(_startCalendar), _durationMilliseconds);
			}
			dismiss();
		}
	};
	private View.OnClickListener _duration_onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			_durationDialog.show(_duration_listener);
		}
	};

	private DurationDialog.Listener _duration_listener = new DurationDialog.Listener() {

		@Override
		public void onOk(long timeMilliseconds) {
			_durationMilliseconds = timeMilliseconds;
			_durationButton.setText(misc.convertMsToHuman(_durationMilliseconds));
		}

		@Override
		public void onCancel() {
		}
	};

	public interface Listener {
		public void onOk(String startDate, long durationMilliseconds);

		public void onCancel();
	}

}