package com.fieldnation;

import java.text.ParseException;
import java.util.List;

import com.fieldnation.data.payments.Payment;
import com.fieldnation.json.JsonObject;
import com.fieldnation.utils.misc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

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
	private JsonObject _paymentInfo;
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
			_paymentInfo = new JsonObject(intent.getStringExtra("PAYMENT_INFO"));
			_paid = Payment.fromJson(_paymentInfo);
		} catch (ParseException e) {
			e.printStackTrace();
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

		_idTextView.setText(_paid.getPaymentId() + "");
		_paymentTextView.setText(misc.toCurrency(_paid.getAmount()));
		_paymentTypeTextView.setText(misc.capitalize(_paid.getPayMethod()));
		_dateTextView.setText("Estimated" + _paid.getDatePaid());
		_workorderCountTextView.setText(_paid.getWorkorders().length + " Work Orders");
		// TODO add fees lookup here

	}

}
