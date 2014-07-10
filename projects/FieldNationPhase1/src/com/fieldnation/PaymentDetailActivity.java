package com.fieldnation;

import java.text.ParseException;
import java.util.Calendar;

import com.fieldnation.data.payments.Payment;
import com.fieldnation.json.JsonObject;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentDetailActivity extends BaseActivity {
	private static final String TAG = "PaymentDetailActivity";

	// UI
	private TextView _idTextView;
	private TextView _paymentTextView;
	private TextView _paymentTypeTextView;
	private TextView _dateTextView;
	private TextView _workorderCountTextView;
	private TextView _feesCountTextView;
	private ListView _listView;

	// Data
	private Payment _paid;
	private PaymentDetailAdapter _adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payments_detail);

		Intent intent = getIntent();

		if (intent == null) {
			finish();
			return;
		}

		try {
			_paid = Payment.fromJson(new JsonObject(intent.getStringExtra("PAYMENT_INFO")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (_paid == null) {
			Toast.makeText(this, this.getString(R.string.could_not_load_payment), Toast.LENGTH_LONG).show();
			finish();
		}

		_idTextView = (TextView) findViewById(R.id.id_textview);
		_paymentTextView = (TextView) findViewById(R.id.payment_textview);
		_paymentTypeTextView = (TextView) findViewById(R.id.paymenttype_textview);
		_dateTextView = (TextView) findViewById(R.id.date_textview);
		_workorderCountTextView = (TextView) findViewById(R.id.workordercount_textview);
		_feesCountTextView = (TextView) findViewById(R.id.feescount_textview);

		_listView = (ListView) findViewById(R.id.items_listview);
		_adapter = new PaymentDetailAdapter(_paid);
		_listView.setAdapter(_adapter);

		_idTextView.setText("ID " + _paid.getPaymentId());
		_paymentTextView.setText(misc.toCurrency(_paid.getAmount()));
		_paymentTypeTextView.setText(misc.capitalize(_paid.getPayMethod()));

		try {
			if (_paid.getDatePaid() != null) {
				String when = "";
				Calendar cal = ISO8601.toCalendar(_paid.getDatePaid());

				when = misc.formatDate(cal);

				_dateTextView.setVisibility(View.VISIBLE);
				_dateTextView.setText(this.getString(R.string.estimated) + " " + when);
			} else {
				_dateTextView.setVisibility(View.GONE);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			_dateTextView.setVisibility(View.GONE);
		}

		_workorderCountTextView.setText(_paid.getWorkorders().length + " " + this.getString(R.string.work_orders));
		// TODO add fees lookup here

	}
}
