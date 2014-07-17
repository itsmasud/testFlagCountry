package com.fieldnation.ui.workorder.detail;

import java.text.ParseException;

import com.fieldnation.R;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScheduleSummaryView extends RelativeLayout {
	private static final String TAG = "ui.workorder.detail.ScheduleSummaryView";

	// UI
	private TextView _timeTextView;
	private TextView _durationTextView;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public ScheduleSummaryView(Context context) {
		this(context, null, -1);
	}

	public ScheduleSummaryView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public ScheduleSummaryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.view_schedule_summary, this);

		if (isInEditMode())
			return;

		_timeTextView = (TextView) findViewById(R.id.time_textview);
		_durationTextView = (TextView) findViewById(R.id.duration_textview);
	}

	public void setLoggedWork(LoggedWork loggedWork) {
		String startDate = loggedWork.getStartDate();

		String date = "";
		try {
			date = misc.formatDateTime(ISO8601.toCalendar(startDate), false);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}

		try {
			String endDate = loggedWork.getEndDate();

			endDate = misc.formatDateTime(ISO8601.toCalendar(endDate), false);

			date += " to " + endDate;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		_timeTextView.setText(date);

		// TODO if endtime is not set, then calculate duration if and only if
		// the current time is still the same day as the start time.

		if (loggedWork.getHours() != null) {
			_durationTextView.setText(loggedWork.getHours().intValue() + "hrs");
		}

	}
}
