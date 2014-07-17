package com.fieldnation.ui.workorder.detail;

import java.util.Calendar;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ScheduleView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.ScheduleView";

	// UI
	private Button _startButton;
	private DatePickerDialog _datePicker;
	private TimePickerDialog _timePicker;

	// Data
	private Workorder _workorder;
	private FragmentManager _fm;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public ScheduleView(Context context) {
		this(context, null);
	}

	public ScheduleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_workorder_detail_schedule, this);

		if (isInEditMode())
			return;

		final Calendar c = Calendar.getInstance();
		_datePicker = DatePickerDialog.newInstance(_dateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		_timePicker = TimePickerDialog.newInstance(_timeSetListener, c.get(Calendar.HOUR_OF_DAY),
				c.get(Calendar.MINUTE), false, false);

		_startButton = (Button) findViewById(R.id.start_button);
		_startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_datePicker.show(_fm, "datepicker");
			}
		});
	}

	public void setFragmentManager(FragmentManager fm) {
		_fm = fm;
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private DatePickerDialog.OnDateSetListener _dateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
			_timePicker.show(_fm, "timepicker");
		}
	};

	private TimePickerDialog.OnTimeSetListener _timeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
			// TODO Method Stub: onTimeSet()
			Log.v(TAG, "Method Stub: onTimeSet()");

		}
	};

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		refresh();
	}

	private void refresh() {
	}

}
