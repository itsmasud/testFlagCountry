package com.fieldnation.ui.workorder.detail;

import java.util.Calendar;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.AdditionalExpense;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.BaseActivity;
import com.fieldnation.ui.dialog.ExpenseDialog;
import com.fieldnation.ui.dialog.PayDialog;
import com.fieldnation.ui.dialog.ScheduleDialog;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

public class CounterOfferActivity extends ActionBarActivity {
	private static final String TAG = "ui.workorder.detail.CounterOfferActivity";

	// Key
	public static final String INTENT_WORKORDER = "com.fieldnation.ui.workorder.detail:WORKORDER";

	// UI
	// labels
	private TextView _labelHourlyRateTextView;
	private TextView _labelMaxHoursTextView;
	// data
	private TextView _basisOldTextView;
	private TextView _basisNewTextView;
	private TextView _hourlyOldTextView;
	private TextView _hourlyNewTextView;
	private TextView _maxOldTextView;
	private TextView _maxNewTextView;
	private LinearLayout _editPaymentCounterLayout;
	private TextView _editPaymentCounterTextView;
	private ImageView _editPaymentCounterImageView;

	private TextView _scheduleTypeOldTextView;
	private TextView _scheduleTypeNewTextView;
	private TextView _scheduleDateOldTextView;
	private TextView _scheduleDateNewTextView;
	private TextView _scheduleTimeOldTextView;
	private TextView _scheduleTimeNewTextView;
	private LinearLayout _editScheduleLayout;
	private TextView _editScheduleTextView;
	private ImageView _editScheduleImageView;

	private TextView _noExpensesTextView;

	private LinearLayout _expensesListLayout;
	private LinearLayout _addExpenseLayout;
	private TextView _addExpenseTextView;

	private LinearLayout _reasonsLayout;

	private EditText _requestReasonEditText;
	private CheckBox _deleteNotAcceptedCheckbox;
	private Button _offerTimeButton;

	private Button _cancelButton;
	private Button _sendButton;

	private PayDialog _payDialog;
	private ScheduleDialog _scheduleDialog;
	private ExpenseDialog _expenseDialog;

	// Data
	private Workorder _workorder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_counter_offer);

		_basisOldTextView = (TextView) findViewById(R.id.basis_old_textview);
		_basisNewTextView = (TextView) findViewById(R.id.basis_new_textview);
		_hourlyOldTextView = (TextView) findViewById(R.id.hourly_old_textview);
		_hourlyNewTextView = (TextView) findViewById(R.id.hourly_new_textview);
		_maxOldTextView = (TextView) findViewById(R.id.max_old_textview);
		_maxNewTextView = (TextView) findViewById(R.id.max_new_textview);
		_editPaymentCounterLayout = (LinearLayout) findViewById(R.id.edit_payment_counter_layout);
		_editPaymentCounterLayout.setOnClickListener(_editPaymentLayout_onClick);
		_editPaymentCounterTextView = (TextView) findViewById(R.id.edit_payment_counter_textview);
		_editPaymentCounterImageView = (ImageView) findViewById(R.id.edit_payment_counter_imageview);

		_labelHourlyRateTextView = (TextView) findViewById(R.id.hourly_rate_label_textview);
		_labelMaxHoursTextView = (TextView) findViewById(R.id.max_hours_label_textview);

		_scheduleTypeOldTextView = (TextView) findViewById(R.id.schedule_type_old_textview);
		_scheduleTypeNewTextView = (TextView) findViewById(R.id.schedule_type_new_textview);
		_scheduleDateOldTextView = (TextView) findViewById(R.id.schedule_date_old_textview);
		_scheduleDateNewTextView = (TextView) findViewById(R.id.schedule_date_new_textview);
		_scheduleTimeOldTextView = (TextView) findViewById(R.id.schedule_time_old_textview);
		_scheduleTimeNewTextView = (TextView) findViewById(R.id.schedule_time_new_textview);

		_editScheduleLayout = (LinearLayout) findViewById(R.id.edit_schedule_layout);
		_editScheduleLayout.setOnClickListener(_editScheduleLayout_onClick);
		_editScheduleTextView = (TextView) findViewById(R.id.edit_schedule_textview);
		_editScheduleImageView = (ImageView) findViewById(R.id.edit_schedule_imageview);

		_noExpensesTextView = (TextView) findViewById(R.id.no_expenses_textview);

		_expensesListLayout = (LinearLayout) findViewById(R.id.expenses_list_layout);
		_addExpenseLayout = (LinearLayout) findViewById(R.id.add_expense_layout);
		_addExpenseLayout.setOnClickListener(_addExpenseLayout_onClick);
		_addExpenseTextView = (TextView) findViewById(R.id.add_expense_textview);

		_reasonsLayout = (LinearLayout) findViewById(R.id.reasons_layout);

		_requestReasonEditText = (EditText) findViewById(R.id.request_reason_edittext);
		_deleteNotAcceptedCheckbox = (CheckBox) findViewById(R.id.delete_not_accepted_checkbox);

		_offerTimeButton = (Button) findViewById(R.id.offer_time_button);
		_offerTimeButton.setOnClickListener(_offerTimeButton_onClick);

		_cancelButton = (Button) findViewById(R.id.cancel_button);
		_cancelButton.setOnClickListener(_cancelButton_onClick);

		_sendButton = (Button) findViewById(R.id.send_button);
		_sendButton.setOnClickListener(_sendButton_onClick);

		_payDialog = new PayDialog(this);
		_scheduleDialog = new ScheduleDialog(this);
		_expenseDialog = new ExpenseDialog(this);

		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(INTENT_WORKORDER)) {
				_workorder = savedInstanceState.getParcelable(INTENT_WORKORDER);
			}
		}

		Intent intent = getIntent();
		if (intent != null && intent.getExtras() != null) {
			Bundle extras = intent.getExtras();
			if (extras.containsKey(INTENT_WORKORDER)) {
				_workorder = extras.getParcelable(INTENT_WORKORDER);
			}
		}

		showPayCounter(false);
		showReason(false);
		showScheduleCounter(false);

		if (_workorder != null)
			populateUi();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (_workorder != null) {
			outState.putParcelable(INTENT_WORKORDER, _workorder);
		}
		super.onSaveInstanceState(outState);
	}

	private void populateUi() {
		Pay pay = _workorder.getPay();
		// pay section
		_basisOldTextView.setText(pay.getPayRateBasis());
		_labelMaxHoursTextView.setText(R.string.max_hours);
		_labelHourlyRateTextView.setText(R.string.hourly_rate);

		// fixed rate
		if (pay.isFixedRate()) {
			_labelMaxHoursTextView.setText(" ");
			_labelHourlyRateTextView.setText(R.string.total_amount);

			_hourlyOldTextView.setText(misc.toCurrencyTrim(pay.getFixedAmount()));
			_maxOldTextView.setText(" ");
		}

		// blended
		if (pay.isBlendedRate()) {

			_hourlyOldTextView.setText(misc.toCurrencyTrim(pay.getBlendedStartRate()) + " - " + pay.getBlendedFirstHours() + " Hours");
			_maxOldTextView.setText(misc.toCurrencyTrim(pay.getBlendedAdditionalRate()) + " - " + pay.getBlendedAdditionalHours() + " Hours");
		}

		// hourly
		if (pay.isHourlyRate()) {
			_hourlyOldTextView.setText(misc.toCurrencyTrim(pay.getPerHour()) + " per hour");
			_maxOldTextView.setText(pay.getMaxHour() + " Hours Max");
		}

		// per device
		if (pay.isPerDeviceRate()) {
			_hourlyOldTextView.setText(misc.toCurrencyTrim(pay.getPerDevice()) + " per device");
			_maxOldTextView.setText(pay.getMaxDevice() + " Devices Max");
			_labelMaxHoursTextView.setText("Device Rate");
			_labelHourlyRateTextView.setText("Max Devices");
		}

		// schedule
		Schedule schedule = _workorder.getSchedule();

		if (schedule.isExact()) {
			try {
				_scheduleTypeOldTextView.setText("Exact");
				Calendar cal = ISO8601.toCalendar(schedule.getStartTime());
				_scheduleDateOldTextView.setText(misc.formatDateLong(cal));
				_scheduleTimeOldTextView.setText(misc.formatTime(cal, false));

				// _scheduleDateOldTextView.setText(misc);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			try {
				_scheduleTypeOldTextView.setText("Range");
				Calendar cal = ISO8601.toCalendar(schedule.getStartTime());
				Calendar cal2 = ISO8601.toCalendar(schedule.getEndTime());

				_scheduleDateOldTextView.setText(String.format(Locale.US, "%tB", cal) + " " + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal2.get(Calendar.DAY_OF_MONTH) + ", " + cal.get(Calendar.YEAR));

				_scheduleTimeOldTextView.setText(misc.formatTime2(cal) + "-" + misc.formatTime(cal2, false));

				// _scheduleDateOldTextView.setText(misc);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		// expenses
		AdditionalExpense[] expenses = _workorder.getAdditionalExpenses();
		if (expenses != null && expenses.length > 0) {
			_noExpensesTextView.setVisibility(View.GONE);
			_expensesListLayout.removeAllViews();
			for (int i = 0; i < expenses.length; i++) {
				AdditionalExpense expense = expenses[i];
				ExpenseView v = new ExpenseView(this);
				v.setAdditionalExpense(expense, i + 1);
				_expensesListLayout.addView(v);
			}
		} else {
			_noExpensesTextView.setVisibility(View.VISIBLE);
		}

	}

	private void showPayCounter(boolean show) {
		if (show) {
			_basisNewTextView.setVisibility(View.VISIBLE);
			_hourlyNewTextView.setVisibility(View.VISIBLE);
			_maxNewTextView.setVisibility(View.VISIBLE);
			_editPaymentCounterTextView.setText(R.string.edit_payment_counter);
			_editPaymentCounterImageView.setBackgroundResource(R.drawable.ic_edit_12);
		} else {
			_basisNewTextView.setVisibility(View.GONE);
			_hourlyNewTextView.setVisibility(View.GONE);
			_maxNewTextView.setVisibility(View.GONE);
			_editPaymentCounterTextView.setText(R.string.request_payment_change);
			_editPaymentCounterImageView.setBackgroundResource(R.drawable.ic_wo_detail_counter_offer);
		}
	}

	private void showScheduleCounter(boolean show) {
		if (show) {
			_scheduleTypeNewTextView.setVisibility(View.VISIBLE);
			_scheduleDateNewTextView.setVisibility(View.VISIBLE);
			_scheduleTimeNewTextView.setVisibility(View.VISIBLE);
			_editScheduleTextView.setText(R.string.edit_schedule_counter);
			_editScheduleImageView.setBackgroundResource(R.drawable.ic_edit_12);
		} else {
			_scheduleTypeNewTextView.setVisibility(View.GONE);
			_scheduleDateNewTextView.setVisibility(View.GONE);
			_scheduleTimeNewTextView.setVisibility(View.GONE);
			_editScheduleTextView.setText(R.string.request_schedule_change);
			_editScheduleImageView.setBackgroundResource(R.drawable.ic_clock_large);
		}
	}

	private void showReason(boolean show) {
		if (show)
			_reasonsLayout.setVisibility(View.VISIBLE);
		else
			_reasonsLayout.setVisibility(View.GONE);
	}

	/*-*****************************-*/
	/*-			UI Events			-*/
	/*-*****************************-*/
	private PayDialog.Listener _payDialog_listener = new PayDialog.Listener() {
		@Override
		public void onPerDevices(double rate, double max) {
			// TODO Method Stub: onPerDevices()
			Log.v(TAG, "Method Stub: onPerDevices()");
		}

		@Override
		public void onNothing() {
			// TODO Method Stub: onNothing()
			Log.v(TAG, "Method Stub: onNothing()");
		}

		@Override
		public void onHourly(double rate, double max) {
			// TODO Method Stub: onHourly()
			Log.v(TAG, "Method Stub: onHourly()");
		}

		@Override
		public void onFixed(double amount) {
			// TODO Method Stub: onFixed()
			Log.v(TAG, "Method Stub: onFixed()");
		}

		@Override
		public void onBlended(double rate, double max, double rate2, double max2) {
			// TODO Method Stub: onBlended()
			Log.v(TAG, "Method Stub: onBlended()");
		}
	};

	private ScheduleDialog.Listener _schedule_listener = new ScheduleDialog.Listener() {

		@Override
		public void onRange(String startDateTime, String endDateTime) {
			// TODO Method Stub: onRange()
			Log.v(TAG, "Method Stub: onRange()");

		}

		@Override
		public void onExact(String startDateTime) {
			// TODO Method Stub: onExact()
			Log.v(TAG, "Method Stub: onExact()");

		}

		@Override
		public void onCancel() {
			// TODO Method Stub: onCancel()
			Log.v(TAG, "Method Stub: onCancel()");

		}
	};

	private ExpenseDialog.Listener _expense_listener = new ExpenseDialog.Listener() {

		@Override
		public void onOk(String description, double amount, ExpenseCategory category) {
			// TODO Method Stub: onOk()
			Log.v(TAG, "Method Stub: onOk()");

		}

		@Override
		public void onCancel() {
			// TODO Method Stub: onCancel()
			Log.v(TAG, "Method Stub: onCancel()");

		}
	};

	private View.OnClickListener _editPaymentLayout_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_payDialog.show(_workorder.getPay(), _payDialog_listener);
		}
	};

	private View.OnClickListener _editScheduleLayout_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_scheduleDialog.show(getSupportFragmentManager(), _workorder.getSchedule(), _schedule_listener);
		}
	};

	private View.OnClickListener _addExpenseLayout_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_expenseDialog.show("Add Additional Expense", _expense_listener);
		}
	};

	private View.OnClickListener _sendButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

	private View.OnClickListener _cancelButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

	private View.OnClickListener _offerTimeButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

}
