package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Skillset;
import com.fieldnation.data.workorder.Workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SummaryView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.SummaryView";

	// UI
	private TextView _substatusTextView;
	private View _substatusProgress; // TODO, need to implement!
	private TextView _projectNameTextView;
	private TextView _clientNameTextView;
	private TextView _workTypeTextView;
	private TextView _skillsTextView;
	private TextView _dateTimeTextView;
	private TextView _workorderIdTextView;

	// Data
	private Workorder _workoder;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public SummaryView(Context context) {
		this(context, null);
	}

	public SummaryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_workorder_detail_sum, this);

		if (isInEditMode())
			return;

		_substatusTextView = (TextView) findViewById(R.id.substatus_textview);
		_substatusProgress = findViewById(R.id.substatus_progress);
		_projectNameTextView = (TextView) findViewById(R.id.projectname_textview);
		_clientNameTextView = (TextView) findViewById(R.id.clientname_textview);
		_workTypeTextView = (TextView) findViewById(R.id.worktype_textview);
		_skillsTextView = (TextView) findViewById(R.id.skills_textview);
		_dateTimeTextView = (TextView) findViewById(R.id.datetime_textview);
		_workorderIdTextView = (TextView) findViewById(R.id.workorderid_textview);

	}

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/

	@Override
	public void setWorkorder(Workorder workorder) {
		_workoder = workorder;

		refresh();
	}

	private void refresh() {
		_substatusTextView.setText(_workoder.getStatus());
		// TODO set progress bar here
		_projectNameTextView.setText(_workoder.getTitle());

		Location location = _workoder.getLocation();
		if (location != null) {
			if (location.getContactName() != null) {
				_clientNameTextView.setText(location.getContactName());
			}
		}

		_workTypeTextView.setText(_workoder.getTypeOfWork());

		String skillString = "";
		Skillset[] skills = _workoder.getSkillsets();
		if (skills != null) {
			for (int i = 0; i < skills.length; i++) {
				Skillset skillset = skills[i];

				skillString += skillset.getName();

				if (i < skills.length - 1) {
					skillString += " * ";
				}
			}
		}

		_skillsTextView.setText(skillString);

		Schedule schedule = _workoder.getSchedule();
		if (schedule != null) {
			String when = schedule.getFormatedTime();
			if (when != null) {
				_dateTimeTextView.setVisibility(VISIBLE);
				_dateTimeTextView.setText(when);
			} else {
				_dateTimeTextView.setVisibility(GONE);
			}
		} else {
			_dateTimeTextView.setVisibility(GONE);
		}

		_workorderIdTextView.setText("ID " + _workoder.getWorkorderId());

	}
}