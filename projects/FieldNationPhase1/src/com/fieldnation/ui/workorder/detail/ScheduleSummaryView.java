package com.fieldnation.ui.workorder.detail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScheduleSummaryView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.ScheduleSummaryView";

	private static final int WEB_SUBMIT_WORKLOG = 1;

	// UI
	private TextView _arriveTimeTextView;

	// Data
	private Workorder _workorder;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public ScheduleSummaryView(Context context) {
		super(context);
		init();
	}

	public ScheduleSummaryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_wd_schedule_summary, this);

		if (isInEditMode())
			return;

		_arriveTimeTextView = (TextView) findViewById(R.id.arrivetime_view);
		setVisibility(View.GONE);
	}

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		refresh();
	}

	private void refresh() {
		if (_workorder == null)
			return;

		Schedule schedule = _workorder.getSchedule();

		if (schedule == null)
			return;

		if (schedule.isExact()) {
			try {
				String dayDate;
				String time = "";
				Calendar cal;

				cal = ISO8601.toCalendar(schedule.getStartTime());

				dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + misc.formatDateLong(cal);
				time = misc.formatTime(cal, false) + " " + cal.getTimeZone().getDisplayName(false, TimeZone.SHORT);

				_arriveTimeTextView.setText("You will need to arrive exactly on " + dayDate + " at " + time + ".");

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
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

				_arriveTimeTextView.setText(msg);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		setVisibility(View.VISIBLE);
	}
}
