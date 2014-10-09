package com.fieldnation.ui.dialog;

import java.util.Calendar;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class ScheduleDialog extends Dialog {
	private static final String TAG = "ui.workorder.detail.ScheduleDialog";

	// Modes
	private static final int MODE_RANGE = 0;
	private static final int MODE_EXACT = 1;

	// UI
	private Spinner _typeSpinner;

	private LinearLayout _rangeLayout;

	private Button _startDateButton;
	private Button _endDateButton;

	private LinearLayout _exactLayout;
	private Button _dateTimeButton;

	private Button _cancelButton;
	private Button _okButton;

	private DatePickerDialog _datePicker;
	private TimePickerDialog _timePicker;

	// Data
	private int _mode;
	private Schedule _sched;
	private FragmentManager _fm;
	private Calendar _startCal;
	private Calendar _endCal;
	private boolean _startIsSet = false;
	private boolean _endIsSet = false;
	private Listener _listener;

	public ScheduleDialog(Context context) {
		super(context);
		setContentView(R.layout.dialog_schedule);
		setTitle("Requested Schedule");

		_typeSpinner = (Spinner) findViewById(R.id.type_spinner);
		_typeSpinner.setOnItemSelectedListener(_type_selected);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.schedule_types,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_typeSpinner.setAdapter(adapter);

		_rangeLayout = (LinearLayout) findViewById(R.id.range_layout);
		_exactLayout = (LinearLayout) findViewById(R.id.exact_layout);

		_startDateButton = (Button) findViewById(R.id.start_date_button);
		_startDateButton.setOnClickListener(_startDateButton_onClick);
		_endDateButton = (Button) findViewById(R.id.end_date_button);
		_endDateButton.setOnClickListener(_endDateButton_onClick);

		_dateTimeButton = (Button) findViewById(R.id.date_time_button);
		_dateTimeButton.setOnClickListener(_dateTimeButton_onClick);

		_cancelButton = (Button) findViewById(R.id.cancel_button);
		_cancelButton.setOnClickListener(_cancelButton_onClick);
		_okButton = (Button) findViewById(R.id.ok_button);
		_okButton.setOnClickListener(_okButton_onClick);

		final Calendar c = Calendar.getInstance();
		_datePicker = DatePickerDialog.newInstance(_date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		_datePicker.setCloseOnSingleTapDay(true);
		_timePicker = TimePickerDialog.newInstance(_time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
				false, false);

		_startCal = Calendar.getInstance();
		_endCal = Calendar.getInstance();

	}

	public void show(FragmentManager fm, Schedule schedule, Listener listener) {
		_fm = fm;
		setSchedule(schedule);
		_listener = listener;
		super.show();
	}

	public void setSchedule(Schedule schedule) {
		_sched = schedule;
		try {
			_startCal = ISO8601.toCalendar(_sched.getStartTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (_sched.isExact()) {
			setMode(MODE_EXACT);
		} else {
			try {
				_endCal = ISO8601.toCalendar(_sched.getEndTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
			setMode(MODE_RANGE);
		}
	}

	private void setMode(int mode) {
		_mode = mode;

		_typeSpinner.setSelection(_mode);

		switch (_mode) {
		case MODE_EXACT:
			_rangeLayout.setVisibility(View.GONE);
			_exactLayout.setVisibility(View.VISIBLE);

			_startDateButton.setText(misc.formatDateTimeLong(_startCal));
			break;
		case MODE_RANGE:
			_rangeLayout.setVisibility(View.VISIBLE);
			_exactLayout.setVisibility(View.GONE);

			if (_sched != null) {
				_startDateButton.setText(misc.formatDateTimeLong(_startCal));
				_endDateButton.setText(misc.formatDateTimeLong(_endCal));
			}
			break;
		}
	}

	/*-*****************************-*/
	/*-			UI Events			-*/
	/*-*****************************-*/
	private DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
			String tag = datePickerDialog.getTag();
			if (tag.equals("start")) {
				_startCal.set(year, month, day);
			} else if (tag.equals("end")) {
				_endCal.set(year, month, day);
			}

			_timePicker.show(_fm, datePickerDialog.getTag());
		}
	};

	private TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute) {
			String tag = view.getTag();
			if (tag.equals("start")) {
				_startCal.set(_startCal.get(Calendar.YEAR), _startCal.get(Calendar.MONTH),
						_startCal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
				_startIsSet = true;

				_startDateButton.setText(misc.formatDateTimeLong(_startCal));

			} else if (tag.equals("end")) {
				_endCal.set(_endCal.get(Calendar.YEAR), _endCal.get(Calendar.MONTH),
						_endCal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
				_endIsSet = true;
				_endDateButton.setText(misc.formatDateTimeLong(_endCal));
			}
		}
	};

	private AdapterView.OnItemSelectedListener _type_selected = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			setMode(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}

	};
	private View.OnClickListener _okButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (_startIsSet || _endIsSet) {
				dismiss();
				if (_listener != null) {
					switch (_mode) {
					case MODE_EXACT:
						_listener.onExact(ISO8601.fromCalendar(_startCal));
						break;
					case MODE_RANGE:
						_listener.onRange(ISO8601.fromCalendar(_startCal), ISO8601.fromCalendar(_endCal));
						break;
					}
				}
			} else {
				Toast.makeText(getContext(), "Please change the schedule or Cancel", Toast.LENGTH_LONG).show();
			}
		}
	};
	private View.OnClickListener _cancelButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
			if (_listener != null) {
				_listener.onCancel();
			}
		}
	};

	private View.OnClickListener _dateTimeButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_datePicker.show(_fm, "start");
		}
	};

	private View.OnClickListener _startDateButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_datePicker.show(_fm, "start");
		}
	};

	private View.OnClickListener _endDateButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_datePicker.show(_fm, "end");
		}
	};

	public interface Listener {
		public void onExact(String startDateTime);

		public void onRange(String startDateTime, String endDateTime);

		public void onCancel();
	}

}
