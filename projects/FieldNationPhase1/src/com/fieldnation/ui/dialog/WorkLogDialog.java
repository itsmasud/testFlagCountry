package com.fieldnation.ui.dialog;

import java.util.Calendar;

import com.fieldnation.R;
import com.fieldnation.utils.misc;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class WorkLogDialog extends Dialog {
	private static final String TAG = "ui.WorkLogDialog";

	// UI
	private Button _startButton;
	private Button _endButton;
	private EditText _devicesEditText;
	private RadioGroup _radioLayout;
	private RadioButton _onsiteRadioButton;
	private Button _okButton;
	private DatePickerDialog _datePicker;
	private TimePickerDialog _timePicker;

	// Data
	private FragmentManager _fm;
	private Calendar _startCalendar;
	private Calendar _endCalendar;
	private boolean _startIsSet = false;
	private boolean _endIsSet = false;
	private Listener _listener;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	public WorkLogDialog(Context context) {
		super(context);
		setContentView(R.layout.dialog_add_worklog);

		_startButton = (Button) findViewById(R.id.start_spinner);
		_startButton.setOnClickListener(_start_onClick);
		_endButton = (Button) findViewById(R.id.end_spinner);
		_endButton.setOnClickListener(_end_onClick);
		_devicesEditText = (EditText) findViewById(R.id.devices_edittext);
		_radioLayout = (RadioGroup) findViewById(R.id.radio_layout);
		_onsiteRadioButton = (RadioButton) findViewById(R.id.onsite_radio);
		_okButton = (Button) findViewById(R.id.ok_button);
		_okButton.setOnClickListener(_ok_onClick);

		final Calendar c = Calendar.getInstance();
		_datePicker = DatePickerDialog.newInstance(_date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		_datePicker.setCloseOnSingleTapDay(true);
		_timePicker = TimePickerDialog.newInstance(_time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
				false, false);

		_startCalendar = Calendar.getInstance();
		_endCalendar = Calendar.getInstance();
	}

	public void setFragmentManager(FragmentManager fm) {
		_fm = fm;
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _start_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_datePicker.show(_fm, "start");
		}
	};

	private View.OnClickListener _end_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_datePicker.show(_fm, "end");
		}
	};

	private DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
			String tag = datePickerDialog.getTag();
			if (tag.equals("start")) {
				_startCalendar.set(year, month, day);
			} else if (tag.equals("end")) {
				_endCalendar.set(year, month, day);
			}

			_timePicker.show(_fm, datePickerDialog.getTag());
		}
	};

	private TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute) {
			String tag = view.getTag();
			if (tag.equals("start")) {
				_startCalendar.set(_startCalendar.get(Calendar.YEAR), _startCalendar.get(Calendar.MONTH),
						_startCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
				_startIsSet = true;

				_startButton.setText(misc.formatDateTime(_startCalendar, false));

			} else if (tag.equals("end")) {
				_endCalendar.set(_endCalendar.get(Calendar.YEAR), _endCalendar.get(Calendar.MONTH),
						_endCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
				_endIsSet = true;
				_endButton.setText(misc.formatDateTime(_endCalendar, false));
			}
		}
	};

	private View.OnClickListener _ok_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (_startIsSet && _endIsSet) {
				if (_listener != null) {
					int deviceCount = 0;
					try {
						deviceCount = Integer.parseInt(_devicesEditText.getText().toString());
					} catch (Exception ex) {
					}
					boolean isOnSiteTime = _onsiteRadioButton.isChecked();

					_listener.onOk(_startCalendar, _endCalendar, deviceCount, isOnSiteTime);
				}

				WorkLogDialog.this.dismiss();
			}
		}
	};

	public void show(int titleResId, boolean showDeviceCount, boolean showWorkType, Listener listener) {
		show(getContext().getText(titleResId), showDeviceCount, showWorkType, listener);
	}

	public void show(CharSequence title, boolean showDeviceCount, boolean showWorkType, Listener listener) {
		_startIsSet = false;
		_endIsSet = false;
		_listener = listener;

		setTitle(title);

		if (showDeviceCount) {
			_devicesEditText.setVisibility(View.VISIBLE);
		} else {
			_devicesEditText.setVisibility(View.GONE);
		}

		if (showWorkType) {
			_radioLayout.setVisibility(View.VISIBLE);
		} else {
			_radioLayout.setVisibility(View.GONE);
		}

		show();
	}

	public interface Listener {
		public void onOk(Calendar start, Calendar end, int deviceCount, boolean isOnSiteTime);
	}
}
