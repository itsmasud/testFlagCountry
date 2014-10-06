package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class ScheduleDialog extends Dialog {
	private static final String TAG = "ui.workorder.detail.ScheduleDialog";

	private Spinner _typeSpinner;

	private LinearLayout _rangeLayout;

	private Button _startDateButton;
	private Button _endDateButton;

	private Button _startTimeButton;
	private Button _endTimeButton;

	private LinearLayout _exactLayout;
	private Button _dateTimeButton;

	private Button _cancelButton;
	private Button _okButton;

	public ScheduleDialog(Context context) {
		super(context);

		_typeSpinner = (Spinner) findViewById(R.id.type_spinner);

		_rangeLayout = (LinearLayout) findViewById(R.id.range_layout);
		_exactLayout = (LinearLayout) findViewById(R.id.exact_layout);

		_startDateButton = (Button) findViewById(R.id.start_date_button);
		_startDateButton.setOnClickListener(_startDateButton_onClick);
		_endDateButton = (Button) findViewById(R.id.end_date_button);
		_endDateButton.setOnClickListener(_endDateButton_onClick);

		_startTimeButton = (Button) findViewById(R.id.start_time_button);
		_startTimeButton.setOnClickListener(_startTimeButton_onClick);
		_endTimeButton = (Button) findViewById(R.id.end_time_button);
		_endTimeButton.setOnClickListener(_endTimeButton_onClick);

		_dateTimeButton = (Button) findViewById(R.id.date_time_button);
		_dateTimeButton.setOnClickListener(_dateTimeButton_onClick);

		_cancelButton = (Button) findViewById(R.id.cancel_button);
		_cancelButton.setOnClickListener(_cancelButton_onClick);
		_okButton = (Button) findViewById(R.id.ok_button);
		_okButton.setOnClickListener(_okButton_onClick);
	}

	/*-*****************************-*/
	/*-			UI Events			-*/
	/*-*****************************-*/
	private View.OnClickListener _okButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");

		}
	};

	private View.OnClickListener _dateTimeButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");

		}
	};

	private View.OnClickListener _endTimeButton_onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");

		}
	};

	private View.OnClickListener _startTimeButton_onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");

		}
	};

	private View.OnClickListener _startDateButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

	private View.OnClickListener _endDateButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

	private View.OnClickListener _cancelButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
		}
	};

	public interface Listener {

	}

}
