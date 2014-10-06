package com.fieldnation.ui.workorder.detail;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.ui.BaseActivity;

public class CounterOfferActivity extends ActionBarActivity {
	private static final String TAG = "ui.workorder.detail.CounterOfferActivity";

	private TextView _basisOldTextView;
	private TextView _basisNewTextView;
	private TextView _hourlyOldTextView;
	private TextView _hourlyNewTextView;
	private TextView _maxOldTextView;
	private TextView _maxNewTextView;
	private LinearLayout _editPaymentCounterLayout;
	private TextView _editPaymentCounterTextView;

	private TextView _scheduleTypeOldTextView;
	private TextView _scheduleTypeNewTextView;
	private TextView _scheduleDateOldTextView;
	private TextView _scheduleDateNewTextView;
	private TextView _scheduleTimeOldTextView;
	private TextView _scheduleTimeNewTextView;
	private LinearLayout _editScheduleLayout;
	private TextView _editScheduleTextView;

	private TextView _noExpensesTextView;

	private LinearLayout _addExpenseLayout;
	private TextView _addExpenseTextView;

	private EditText _requestReasonEditText;
	private CheckBox _deleteNotAcceptedCheckbox;
	private Button _offerTimeButton;

	private Button _cancelButton;
	private Button _sendButton;

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

		_scheduleTypeOldTextView = (TextView) findViewById(R.id.schedule_type_old_textview);
		_scheduleTypeNewTextView = (TextView) findViewById(R.id.schedule_type_new_textview);
		_scheduleDateOldTextView = (TextView) findViewById(R.id.schedule_date_old_textview);
		_scheduleDateNewTextView = (TextView) findViewById(R.id.schedule_date_new_textview);
		_scheduleTimeOldTextView = (TextView) findViewById(R.id.schedule_time_old_textview);
		_scheduleTimeNewTextView = (TextView) findViewById(R.id.schedule_time_new_textview);

		_editScheduleLayout = (LinearLayout) findViewById(R.id.edit_schedule_layout);
		_editScheduleLayout.setOnClickListener(_editScheduleLayout_onClick);
		_editScheduleTextView = (TextView) findViewById(R.id.edit_schedule_textview);

		_noExpensesTextView = (TextView) findViewById(R.id.no_expenses_textview);

		_addExpenseLayout = (LinearLayout) findViewById(R.id.add_expense_layout);
		_addExpenseLayout.setOnClickListener(_addExpenseLayout_onClick);
		_addExpenseTextView = (TextView) findViewById(R.id.add_expense_textview);

		_requestReasonEditText = (EditText) findViewById(R.id.request_reason_edittext);
		_deleteNotAcceptedCheckbox = (CheckBox) findViewById(R.id.delete_not_accepted_checkbox);

		_offerTimeButton = (Button) findViewById(R.id.offer_time_button);
		_offerTimeButton.setOnClickListener(_offerTimeButton_onClick);

		_cancelButton = (Button) findViewById(R.id.cancel_button);
		_cancelButton.setOnClickListener(_cancelButton_onClick);

		_sendButton = (Button) findViewById(R.id.send_button);
		_sendButton.setOnClickListener(_sendButton_onClick);
	}

	/*-*****************************-*/
	/*-			UI Events			-*/
	/*-*****************************-*/
	private View.OnClickListener _editPaymentLayout_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

	private View.OnClickListener _editScheduleLayout_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

	private View.OnClickListener _addExpenseLayout_onClick = new View.OnClickListener() {
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

	private View.OnClickListener _cancelButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

	private View.OnClickListener _sendButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};
}
